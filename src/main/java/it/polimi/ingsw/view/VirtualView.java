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

    public void start() {
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

    private void ping() throws InterruptedException, IOException {
        long initTime = System.currentTimeMillis();
        long interTime = initTime;
        Message message = new Message(MessageType.PING, viewID);
        sendMessage(message);
        while (true) {
            synchronized (lock) {
                lock.wait(10000);
                if (isTimePassed(initTime, System.currentTimeMillis(), 30000)) {
                    throw new IOException();
                }
                else {
                    if (!(isTimePassed(interTime, System.currentTimeMillis(), 10000))) {
                        TimeUnit.SECONDS.sleep(10 - (System.currentTimeMillis() - interTime) / 10000);
                        initTime = System.currentTimeMillis();
                    }
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
                    case OK:
                    case QUIT:
                    case MARKET:
                    case PLAYERS:
                    case END_GAME:
                    case DECKBOARD:
                    case NEW_PLAYER:
                    case START_GAME:
                    case CARD_REMOVE:
                    case EXTRA_DEPOT:
                    case MARKET_CHANGE:
                    case VATICAN_REPORT:
                    case RESOURCE_AMOUNT:
                    case INCREASE_WAREHOUSE:
                    case FAITH_POINTS_INCREASE:
                        printMessage(message);
                        errorMessage(ErrorType.ILLEGAL_OPERATION);
                        break;
                    case TURN:
                        setChanged();
                        notifyObservers(message);
                        break;
                    case LOGIN:
                        nickName = ((MessageOneParameterString) message).getPar();
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
    public void allPlayerConnected(int position, ArrayList<String> nickNames) {
        viewID = position;
        Message message = new MessageArrayListString(MessageType.PLAYERS, position, nickNames);
        sendMessage(message);
    }

    @Override
    public void startGame(){
        Message message = new Message(MessageType.START_GAME, viewID);
        sendMessage(message);
    }

    @Override
    public void choseLeaderCards(ArrayList<LeaderCard> leaderCards){
        int cardID1 = leaderCards.get(0).getCardID();
        int cardID2 = leaderCards.get(1).getCardID();
        int cardID3 = leaderCards.get(2).getCardID();
        int cardID4 = leaderCards.get(3).getCardID();
        Message message = new MessageFourParameterInt(MessageType.LEADER_CARD, viewID, cardID1, cardID2, cardID3, cardID4);
        sendMessage(message);
    }

    @Override
    public void exit(String nickName){
        Message quitMessage = new MessageOneParameterString(MessageType.QUIT, viewID, nickName);
        sendMessage(quitMessage);
    }

    @Override
    public void ok(){
        Message okMessage = new Message(MessageType.OK, viewID);
        sendMessage(okMessage);
    }

    @Override
    public void isMyTurn(boolean turn){
        Message turnMessage;
        if (turn) {
            turnMessage = new MessageOneParameterInt(MessageType.TURN, viewID, 1);
        } else {
            turnMessage = new MessageOneParameterInt(MessageType.TURN, viewID, 2);
        }
        sendMessage(turnMessage);
    }

    @Override
    public void availableSlot(ArrayList<Integer> availableSlots){
        int firstSlot = availableSlots.get(0);
        int secondSlot = availableSlots.get(1);
        int thirdSlot;
        if (availableSlots.size() == 3)
            thirdSlot = availableSlots.get(2);
        else
            thirdSlot = -1;
        Message message = new MessageThreeParameterInt(MessageType.CHOSEN_SLOT, viewID, firstSlot, secondSlot, thirdSlot);
        sendMessage(message);
    }

    @Override
    public void chosenMarble(Marble[] marbles){
        ArrayList<Marble> chosenMarbles = new ArrayList<>();
        Collections.addAll(chosenMarbles, marbles);
        Message message = new MessageArrayListMarble(MessageType.TAKE_MARBLE, viewID, chosenMarbles);
        sendMessage(message);
    }

    @Override
    public void choseWhiteConversionCard(LeaderCard[] leaderCards){
        int leaderCard1 = leaderCards[0].getCardID();
        int leaderCard2 = leaderCards[1].getCardID();
        Message message = new MessageTwoParameterInt(MessageType.WHITE_CONVERSION_CARD, viewID, leaderCard1, leaderCard2);
        sendMessage(message);
    }

    public void quit(String nickName){
        connected = false;
        System.out.println(this.nickName + " is disconnected");
        exit(nickName);
        disconnect();
    }

    public void sendMessage(Message message){
        try {
            out.flush();
            out.writeObject(message);
        } catch (IOException e) {
            disconnect();
        }
    }

    public void endGame(Message message){
        connected = false;
        System.out.println(nickName + " is disconnected");
        sendMessage(message);
        disconnect();
    }

    public void errorMessage(ErrorType errorType){
        if(errorType == ErrorType.ALREADY_TAKEN_NICKNAME)
            nickName = null;
        sendMessage(new ErrorMessage( viewID, errorType));
    }

    private void disconnect(){
        if(connected) {
            if (nickName == null)
                System.err.println("Client disconnected brutally");
            else
                System.err.println(nickName + " disconnected brutally");
            waitAMoment();
            connected = false;
        }
        pingThread.interrupt();
        inThread.interrupt();
        try {
            in.close();
            out.close();
        } catch (IOException ignored) {
        }
    }

    @Override
    public String toString() {
        return nickName;
    }

    @Override
    public void update(Observable o, Object arg) {
        Message message = (Message) arg;
        if(message.getMessageType() == MessageType.END_GAME)
            endGame(message);
        else
            sendMessage(message);
        waitAMoment();
    }

    private void waitAMoment(){
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException ignored) {}
    }
}
