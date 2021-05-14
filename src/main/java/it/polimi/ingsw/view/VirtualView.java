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

public class VirtualView extends Observable implements View, Observer{


    private final Object lock = new Object();
    private Thread threadIn;
    private Thread pingThread;
    private boolean connected;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private String nickName;
    private int param;
    private int position;

    public VirtualView(ObjectInputStream in, ObjectOutputStream out){
        this.in=in;
        this.out=out;
        this.connected = true;
    }

    public void addController(ControllerGame controllerGame){
        addObserver(controllerGame);
    }

    public void start() {
        if (threadIn == null) {
            threadIn = new Thread(() -> {
                receiveMessage();
            });
            threadIn.start();
        }
        if(pingThread == null){
            pingThread = new Thread(() -> {
                    ping();
            });
            pingThread.start();
        }
    }

    public void join() throws InterruptedException {
        threadIn.join();
        pingThread.join();
    }

    public void ping(){
        try {
            TimeUnit.SECONDS.sleep(1);
            long initTime = System.currentTimeMillis();
            Message message = new Message(MessageType.PING, position);
            sendMessage(message);
            while (true) {
                synchronized (lock) {
                    lock.wait(10000);
                    if (isTimePassed(initTime, System.currentTimeMillis(), 30000))
                        throw new InterruptedException();
                    else if (!(isTimePassed(initTime, System.currentTimeMillis(), 10000))) {
                        TimeUnit.SECONDS.sleep(10);
                        initTime = System.currentTimeMillis();
                    } else {
                        out.flush();
                        out.writeObject(message);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            if (connected) {
                if (nickName != null)
                    System.err.println(nickName + " disconnesso brutalmente.");
                else
                    System.err.println("Client disconnesso brutalmente");
                setChanged();
                notifyObservers(new Message_One_Parameter_String(MessageType.QUIT, position, nickName));
            } else if (nickName != null)
                System.out.println(nickName + " si è disconnesso.\n");
            else
                System.err.println("Client si è disconnesso");
            disconnect();
        }
    }

    public int getPosition() {
        return position;
    }

    private void receiveMessage() {
        while (true) {
            try {
                Message message = (Message) in.readObject();
                if(message.getMessageType() != MessageType.PONG || message.getMessageType() != MessageType.PING ||
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
                    case END_TURN:
                        ok();
                        break;
                    default:
                        setChanged();
                        notifyObservers(message);
                        break;
                }
            } catch (IOException e) {
                System.out.println(nickName + " si è disconnesso.\n");
                disconnect();
                break;
            } catch (ClassNotFoundException | ClassCastException e) {
                System.err.println("Messaggi inaspettati dal Client");
            }
        }
    }

    @Override
    public String getNickname() throws IOException, ClassNotFoundException {
        if(nickName != null)
            return nickName;
        Message message = (Message) in.readObject();
        while (message.getMessageType() != MessageType.LOGIN)
            message = (Message) in.readObject();
        Message_One_Parameter_String m = (Message_One_Parameter_String) message;
        System.out.println(m.toString());
        nickName = m.getPar();
        return nickName;
    }

    public String nicknameTaken() throws IOException, ClassNotFoundException {
        String oldNickName = nickName;
        while (nickName.equals(oldNickName)) {
            try {
                Message errMessage = new ErrorMessage(MessageType.ERR, position, ErrorType.ALREADY_TAKEN_NICKNAME);
                sendMessage(errMessage);
            } catch (IOException e) {
                System.err.println("Client disconnesso brutalmente.");
                disconnect();
                throw new IOException();
            }
            Message message = (Message) in.readObject();
            while (message.getMessageType() != MessageType.LOGIN)
                message = (Message) in.readObject();
            System.out.println(message.toString());
            Message_One_Parameter_String m = (Message_One_Parameter_String) message;
            nickName = m.getPar();
            System.out.println(nickName);
        }
        return nickName;
    }

    public int getNumberPlayers() throws IOException, ClassNotFoundException {
        Message message = new Message(MessageType.LOGIN, 1);
        sendMessage(message);
        Message numPlayerMessage = (Message) in.readObject();
        while (true) {
            while (numPlayerMessage.getMessageType() != MessageType.NUM_PLAYERS)
                numPlayerMessage = (Message) in.readObject();
            Message_One_Parameter_Int m = (Message_One_Parameter_Int) numPlayerMessage;
            try {
                if (m.getPar() < 1 || m.getPar() > 4) {
                    ErrorMessage errMessage = new ErrorMessage(MessageType.ERR, 1, ErrorType.WRONG_PARAMETERS);
                    sendMessage(errMessage);
                } else {
                    System.out.println(m.toString());
                    ok();
                    return m.getPar();
                }
            } catch (IOException e) {
                System.err.println(nickName + " disconnesso brutalmente.");
                disconnect();
                return -1;
            }
        }
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
        if(position != 1) {
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
        try {
            sendMessage(quitMessage);
        }catch (SocketException e){}
    }

    public void ok() throws IOException {
        Message okMessage = new Message(MessageType.OK, position);
        System.out.println("OK");
        try {
            sendMessage(okMessage);
        }catch (SocketException e){}
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
            } catch (SocketException | ClassNotFoundException | ClassCastException e) {
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
            } catch (SocketException | ClassNotFoundException | ClassCastException e) {
            }
        }
    }

    public void sendMessage(Message message) throws IOException {
        try {
            out.flush();
            out.writeObject(message);
        }catch (SocketException e){}
        if(message.getMessageType() == MessageType.END_GAME)
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
        try {
            sendMessage(message);
            disconnect();
        }catch (SocketException e){}
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
