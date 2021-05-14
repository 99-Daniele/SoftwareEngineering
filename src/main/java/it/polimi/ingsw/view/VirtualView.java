package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class VirtualView extends Observable implements View, Observer, Runnable{


    private final Object lock = new Object();
    private Thread pingThread;
    private boolean connected;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private String nickName;
    private int position;

    public VirtualView(ObjectInputStream in, ObjectOutputStream out){
        this.in=in;
        this.out=out;
        this.connected = true;
    }

    public void addController(ControllerGame controllerGame){
        addObserver(controllerGame);
    }


    @Override
    public void run() {
        if(pingThread == null) {
            pingThread = new Thread(() -> {
                try {
                    ping();
                } catch (InterruptedException | IOException e) {
                    if(connected){
                        if(nickName == null)
                            System.err.println("Client si è disconesso brutalmente.");
                        else
                            System.err.println(nickName + " si è disconnesso brutalmente.");
                    }
                    else {
                        if(nickName == null)
                            System.out.println("Client si è disconesso.");
                        else
                            System.out.println(nickName + " si è disconnesso.");
                    }
                    disconnect();
                }
            });
            pingThread.start();
        }
        try {
            receiveMessage();
        } catch (IOException e) {
            if(connected){
                if(nickName == null)
                    System.err.println("Client si è disconesso brutalmente.");
                else
                    System.err.println(nickName + " si è disconnesso brutalmente.");
            }
            else {
                if(nickName == null)
                    System.out.println("Client si è disconesso.");
                else
                    System.out.println(nickName + " si è disconnesso.");
            }
            disconnect();
        }
    }

    public void ping() throws InterruptedException, IOException {
        TimeUnit.SECONDS.sleep(1);
        long initTime = System.currentTimeMillis();
        long interTime = initTime;
        Message message = new Message(MessageType.PING, position);
        sendMessage(message);
        while (true) {
            synchronized (lock) {
                lock.wait(10000);
                if (isTimePassed(initTime, System.currentTimeMillis(), 30000)) {
                    System.out.println(new Date(initTime) + " - " + new Date(interTime) + " - " + new Date(System.currentTimeMillis()));
                    throw new IOException();
                } else if (!(isTimePassed(interTime, System.currentTimeMillis(), 10000))) {
                    TimeUnit.SECONDS.sleep(10);
                    initTime = System.currentTimeMillis();
                    interTime = initTime;
                } else {
                    interTime = System.currentTimeMillis();
                    out.flush();
                    out.writeObject(message);
                }
            }
        }
    }

    public int getPosition() {
        return position;
    }

    private void receiveMessage() throws IOException {
        while (true) {
            try {
                Message message = (Message) in.readObject();
                if(message.getMessageType() != MessageType.PONG && message.getMessageType() != MessageType.PING &&
                        message.getMessageType() != MessageType.TURN)
                    System.out.println(message.toString());
                switch (message.getMessageType()) {
                    case PING:
                        sendMessage(new Message(MessageType.PONG, position));
                        break;
                    case PONG:
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                        break;
                    case TURN:
                    case BUY_CARD:
                    case TAKE_MARBLE:
                    case DEVELOPMENT_CARD_POWER:
                    case BASIC_POWER:
                    case LEADER_CARD_POWER:
                    case END_PRODUCTION:
                    case LEADER_CARD_ACTIVATION:
                    case LEADER_CARD_DISCARD:
                    case END_TURN:
                        setChanged();
                        notifyObservers(message);
                        break;
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.err.println("\nRicevuto messaggio inaspettato dal Client.");
            }
        }
    }

    @Override
    public String getNickname() throws IOException{
        try {
            if(nickName != null)
                return nickName;
            Message message = (Message) in.readObject();
            while (message.getMessageType() != MessageType.LOGIN)
                message = (Message) in.readObject();
            Message_One_Parameter_String m = (Message_One_Parameter_String) message;
            System.out.println(m.toString());
            nickName = m.getPar();
            return nickName;
        } catch (ClassNotFoundException | ClassCastException e) {
            System.err.println("\nRicevuto messaggio inaspettato dal Client.");
        }
        return null;
    }

    public String nicknameTaken() throws IOException{
        String oldNickName = nickName;
        while (nickName.equals(oldNickName)) {
            Message errMessage = new ErrorMessage(MessageType.ERR, position, ErrorType.ALREADY_TAKEN_NICKNAME);
            sendMessage(errMessage);
            try {
                Message message = (Message) in.readObject();
                while (message.getMessageType() != MessageType.LOGIN)
                    message = (Message) in.readObject();
                System.out.println(message.toString());
                Message_One_Parameter_String m = (Message_One_Parameter_String) message;
                nickName = m.getPar();
            } catch (ClassNotFoundException | ClassCastException e) {
                System.err.println("\nRicevuto messaggio inaspettato dal Client.");
            }
        }
        return nickName;
    }

    public int getNumberPlayers() throws IOException{
        Message message = new Message(MessageType.LOGIN, 0);
        sendMessage(message);
        try {
            Message numPlayerMessage = (Message) in.readObject();
            while (true) {
                while (numPlayerMessage.getMessageType() != MessageType.NUM_PLAYERS)
                    numPlayerMessage = (Message) in.readObject();
                Message_One_Parameter_Int m = (Message_One_Parameter_Int) numPlayerMessage;
                if (m.getPar() < 1 || m.getPar() > 4) {
                    ErrorMessage errMessage = new ErrorMessage(MessageType.ERR, 1, ErrorType.WRONG_PARAMETERS);
                    sendMessage(errMessage);
                } else {
                    System.out.println(m.toString());
                    ok();
                    return m.getPar();
                }
            }
        } catch (ClassNotFoundException | ClassCastException e) {
            System.err.println("\nRicevuto messaggio inaspettato dal Client.");
        }
        return -1;
    }

    public void myTurn(boolean turn) throws IOException {
        Message turnMessage;
        if (turn) {
            turnMessage = new Message_One_Parameter_Int(MessageType.TURN, position, 1);
        } else {
            turnMessage = new Message_One_Parameter_Int(MessageType.TURN, position, 2);
        }
        sendMessage(turnMessage);
    }

    @Override
    public void newPlayer(String nickname, int position) throws IOException {
        if(this.position != position) {
            Message m = new Message_One_Parameter_String(MessageType.NEW_PLAYER,position, nickname);
            sendMessage(m);
        }
    }

    public void position(int position) throws IOException {
        this.position = position;
        if(position != 0) {
            Message message = new Message(MessageType.LOGIN, position);
            System.out.println(message.toString());
            sendMessage(message);
        }
    }

    public void pronto(int numPlayers) throws IOException {
        Message message = new Message_One_Parameter_Int(MessageType.START_GAME, position, numPlayers);
        sendMessage(message);
    }

    public void quit(String nickName) throws IOException {
        connected = false;
        Message quitMessage = new Message_One_Parameter_String(MessageType.QUIT, position, nickName);
        sendMessage(quitMessage);
        disconnect();
    }

    public void ok() throws IOException {
        Message okMessage = new Message(MessageType.OK, position);
        System.out.println("OK");
        sendMessage(okMessage);
    }

    private boolean isTimePassed(long initTime, long currentTime, int delta){
        return ((currentTime - initTime) >= delta);
    }

    @Override
    public int available_slot(int clientID, ArrayList<Integer> availableSlots) throws IOException {
        int firstSlot = availableSlots.get(0);
        int secondSlot = availableSlots.get(1);
        int thirdSlot;
        if (availableSlots.size() == 3)
            thirdSlot = availableSlots.get(2);
        else
            thirdSlot = -1;
        Message message = new Message_Three_Parameter_Int(MessageType.CHOSEN_SLOT, clientID, firstSlot, secondSlot, thirdSlot);
        sendMessage(message);
        while (true) {
            try {
                Message returnMessage = (Message) in.readObject();
                Message_One_Parameter_Int m = (Message_One_Parameter_Int) returnMessage;
                if(m.getMessageType() == MessageType.CHOSEN_SLOT) {
                    System.out.println("CHOSEN_SLOT: " + m.getPar());
                    ok();
                    return m.getPar();
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.err.println("\nRicevuto messaggio inaspettato dal Server.");
            }
        }
    }

    @Override
    public void chosen_marble(int clientID, ArrayList<Marble> marbles) throws IOException {
        Message message = new Message_ArrayList_Marble(MessageType.TAKE_MARBLE, clientID, marbles);
        sendMessage(message);
        while (true) {
            try {
                Message returnMessage = (Message) in.readObject();
                Message_One_Parameter_Int m = (Message_One_Parameter_Int) returnMessage;
                if(m.getMessageType() == MessageType.USE_MARBLE) {
                    marbles.remove(m.getPar());
                    if(marbles.size() == 1)
                        ok();
                    break;
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.err.println("\nRicevuto messaggio inaspettato dal Client.");
            }
        }
    }

    public void sendMessage(Message message) throws IOException{
        out.flush();
        out.writeObject(message);
        if (message.getMessageType() == MessageType.END_GAME || message.getMessageType() == MessageType.QUIT)
            connected = false;
    }

    private void disconnect(){
        connected = false;
        try {
            in.close();
            out.close();
        } catch (IOException e) {
        }
    }

    public void endGame(Message message) throws IOException {
        sendMessage(message);
        disconnect();
    }
/*
    private Scanner CurrentViewIn(int currentPos){
        return viewsPos.get(currentPos).getIn();
    }
    private PrintWriter CurrentViewOut(int currentPos){
        return viewsPos.get(currentPos).getOut();
    }
    */

    @Override
    public void update(Observable o, Object arg) {
        Message message = (Message) arg;
        try {
            sendMessage(message);
        } catch (IOException e) { }
    }
}
