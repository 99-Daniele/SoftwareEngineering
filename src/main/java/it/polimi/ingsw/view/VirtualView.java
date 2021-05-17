package it.polimi.ingsw.view;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class VirtualView extends Observable implements View, Observer{

    private Thread pingThread;
    private Thread inThread;
    private final Object lock = new Object();
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private String nickName;
    private int viewID;
    private boolean connected;

    public VirtualView(Socket socket) throws IOException {
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.connected = true;
    }

    public void start(int viewID) {
        this.viewID = viewID;
        if (pingThread == null)
            pingThread = new Thread(() -> {
                try {
                    ping();
                } catch (InterruptedException | IOException e) {
                    disconnect();
                }
            });
        pingThread.start();
        if (inThread == null) {
            inThread = new Thread(() -> {
                try {
                    receiveMessage();
                } catch (IOException e) {
                    disconnect();
                }
            });
            inThread.start();
        }
    }

    public void join() throws InterruptedException {
        pingThread.join();
        inThread.join();
    }

    @Override
    public String getNickname(){
        return nickName;
    }

    @Override
    public void login(int viewID){
        this.viewID = viewID;
        sendMessage(new Message(MessageType.LOGIN, viewID));
    }

    public int getViewID() {
        return viewID;
    }

    private void ping() throws InterruptedException, IOException {
        TimeUnit.SECONDS.sleep(1);
        long initTime = System.currentTimeMillis();
        long interTime = initTime;
        Message message = new Message(MessageType.PING, viewID);
        sendMessage(message);
        while (true) {
            synchronized (lock) {
                lock.wait(10000);
                if (isTimePassed(initTime, System.currentTimeMillis(), 30000)) {
                    throw new IOException();
                } else if (!(isTimePassed(interTime, System.currentTimeMillis(), 10000))) {
                    TimeUnit.SECONDS.sleep(10);
                    initTime = System.currentTimeMillis();
                    interTime = initTime;
                } else {
                    interTime = System.currentTimeMillis();
                    sendMessage(message);
                }
            }
        }
    }

    private void receiveMessage() throws IOException {
        while (true) {
            try {
                Message message = (Message) in.readObject();
                switch (message.getMessageType()) {
                    case PING:
                        sendMessage(new Message(MessageType.PONG, viewID));
                        break;
                    case PONG:
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                        break;
                    case ERR:
                        printMessage(message);
                        errorMessage(ErrorType.ILLEGAL_OPERATION);
                        break;
                    case TURN:
                        setChanged();
                        notifyObservers(message);
                        break;
                    case LOGIN:
                        nickName = ((Message_One_Parameter_String) message).getPar();
                    default:
                        printMessage(message);
                        setChanged();
                        notifyObservers(message);
                        break;
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                errorMessage(ErrorType.ILLEGAL_OPERATION);
            }
        }
    }

    private void printMessage(Message m){
        System.out.println("[" + nickName + "]: " + m.toString());
    }

    private boolean isTimePassed(long initTime, long currentTime, int delta){
        return ((currentTime - initTime) >= delta);
    }

    @Override
    public void newPlayer(String nickname, int position){
        if(this.viewID != position) {
            Message m = new Message_One_Parameter_String(MessageType.NEW_PLAYER,position, nickname);
            sendMessage(m);
        }
    }

    @Override
    public void allPlayerConnected(int position, int numPLayer) {
        viewID = position;
        Message message = new Message_One_Parameter_Int(MessageType.START_GAME, position, numPLayer);
        sendMessage(message);
    }

    @Override
    public void startGame(int numPlayers){
        Message message = new Message_One_Parameter_Int(MessageType.START_GAME, viewID, numPlayers);
        sendMessage(message);
    }

    @Override
    public void choseLeaderCards(ArrayList<LeaderCard> leaderCards){
        int cardID_1 = leaderCards.get(0).getCardID();
        int cardID_2 = leaderCards.get(1).getCardID();
        int cardID_3 = leaderCards.get(2).getCardID();
        int cardID_4 = leaderCards.get(3).getCardID();
        Message message = new Message_Four_Parameter_Int(MessageType.LEADER_CARD, viewID, cardID_1, cardID_2, cardID_3, cardID_4);
        sendMessage(message);
    }

    @Override
    public void exit(String nickName){
        Message quitMessage = new Message_One_Parameter_String(MessageType.QUIT, viewID, nickName);
        sendMessage(quitMessage);
    }

    @Override
    public void ok(){
        Message okMessage = new Message(MessageType.OK, viewID);
        System.out.println("OK");
        sendMessage(okMessage);
    }

    @Override
    public void isMyTurn(boolean turn){
        Message turnMessage;
        if (turn) {
            turnMessage = new Message_One_Parameter_Int(MessageType.TURN, viewID, 1);
        } else {
            turnMessage = new Message_One_Parameter_Int(MessageType.TURN, viewID, 2);
        }
        sendMessage(turnMessage);
    }

    @Override
    public void available_slot(ArrayList<Integer> availableSlots){
        int firstSlot = availableSlots.get(0);
        int secondSlot = availableSlots.get(1);
        int thirdSlot;
        if (availableSlots.size() == 3)
            thirdSlot = availableSlots.get(2);
        else
            thirdSlot = -1;
        Message message = new Message_Three_Parameter_Int(MessageType.CHOSEN_SLOT, viewID, firstSlot, secondSlot, thirdSlot);
        sendMessage(message);
    }

    @Override
    public void chosen_marble(Marble[] marbles){
        ArrayList<Marble> chosenMarbles = new ArrayList<>();
        Collections.addAll(chosenMarbles, marbles);
        Message message = new Message_ArrayList_Marble(MessageType.TAKE_MARBLE, viewID, chosenMarbles);
        sendMessage(message);
    }

    @Override
    public void choseWhiteConversionCard(LeaderCard[] leaderCards){
        int leaderCard1 = leaderCards[0].getCardID();
        int leaderCard2 = leaderCards[1].getCardID();
        Message message = new Message_Two_Parameter_Int(MessageType.WHITE_CONVERSION_CARD, viewID, leaderCard1, leaderCard2);
        sendMessage(message);
    }

    public void quit(String nickName){
        System.out.println(nickName + " is disconnected");
        exit(nickName);
        disconnect();
    }

    public void sendMessage(Message message){
        try {
            out.flush();
            out.writeObject(message);
        } catch (IOException e) {
            disconnect();
            Thread.currentThread().interrupt();
        }
    }

    public void endGame(Message message){
        System.out.println(nickName + " is disconnected");
        sendMessage(message);
        disconnect();
    }

    public void errorMessage(ErrorType errorType){
        sendMessage(new ErrorMessage(MessageType.ERR, viewID, errorType));
    }

    private void disconnect(){
        if(connected) {
            if (nickName == null)
                System.err.println("Client disconnected brutally");
            else
                System.err.println(nickName + " disconnected brutally");
            connected = false;
        }
        inThread.interrupt();
        pingThread.interrupt();
        try {
            in.close();
            out.close();
        } catch (IOException e) {
        }
        finally {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        Message message = (Message) arg;
        if(message.getMessageType() == MessageType.END_GAME)
            endGame(message);
        else
            sendMessage(message);
    }

    @Override
    public String toString() {
        return nickName;
    }
}
