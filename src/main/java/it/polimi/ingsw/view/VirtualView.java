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

/**
 * VirtualView implements View. Observes Game class and it's observed vy ControllerGame class.
 * pingThread evaluates if Client is still connected to the Server.
 * inThread constantly received messages from Client.
 * out and in are Client-Server serializable objects streams.
 * nickname is player's chosen nickname.
 * viewID is player's position in the game.
 * connected is true if Client is theoretically connected to Server.
 */
public class VirtualView extends Observable implements View, Observer{

    private Thread pingThread;
    private Thread inThread;
    private final Object lock = new Object();
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String nickName;
    private int viewID;
    private boolean connected;

    public VirtualView(){
    }

    public VirtualView(Socket socket) throws IOException {
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.connected = true;
    }

    /**
     * start pingThread and inThread.
     * inThread constantly interpret received Client messages.
     * pingThread constantly evaluates if Client is still connected to Server.
     */
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

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * @param viewID is player's vieID.
     *
     * send to player a LOGIN message with his position in the game.
     */
    @Override
    public void login(int viewID){
        this.viewID = viewID;
        sendMessage(new Message(MessageType.LOGIN, viewID));
    }

    /**
     * @throws InterruptedException if wit is interrupted
     * @throws IOException if Client isn't connected anymore.
     *
     * send PING message to Client every 10 seconds. if Client has not answer with PONG message 3 times in a row and
     * any has not sent any message for 30 second, connection with Client is considered as lost.
     */
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

    /**
     * @throws IOException if Client isn't connected anymore.
     *
     * interpret messages received from Client.
     * in case of PING message answer Client with a PONG message
     * in case of PONG message wake up pingThread.
     * in case of unexpected message print an error message.
     * in any other case inform ControllerGame of received message from Client.
     */
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

    /**
     * @param position is player's position in the game.
     * @param nickNames is a list of player's nicknames.
     *
     * send to player a PLAYERS message with the list of player's nicknames adn hi snew position.
     */
    @Override
    public void allPlayerConnected(int position, ArrayList<String> nickNames) {
        viewID = position;
        Message message = new MessageArrayListString(MessageType.PLAYERS, position, nickNames);
        sendMessage(message);
    }

    /**
     * send to player a START_GAME message.
     */
    @Override
    public void startGame(){
        Message message = new Message(MessageType.START_GAME, viewID);
        sendMessage(message);
    }

    /**
     * @param leaderCards is a list of 4 LeaderCards which player can chose.
     */
    @Override
    public void choseLeaderCards(ArrayList<LeaderCard> leaderCards){
        int cardID1 = leaderCards.get(0).getCardID();
        int cardID2 = leaderCards.get(1).getCardID();
        int cardID3 = leaderCards.get(2).getCardID();
        int cardID4 = leaderCards.get(3).getCardID();
        Message message = new MessageFourParameterInt(MessageType.LEADER_CARD, viewID, cardID1, cardID2, cardID3, cardID4);
        sendMessage(message);
    }

    /**
     * @param nickName is nickname of quitting player.
     *
     * send to player a QUIT message with @param player.
     */
    @Override
    public void exit(String nickName){
        Message quitMessage = new MessageOneParameterString(MessageType.QUIT, viewID, nickName);
        sendMessage(quitMessage);
    }

    /**
     * send to player an OK message.
     */
    @Override
    public void ok(){
        Message okMessage = new Message(MessageType.OK, viewID);
        sendMessage(okMessage);
    }

    /**
     * @param turn refers to if it's player turn or not.
     *
     * send to player a TURN message with param = 1 if it's his turn or = 2 if not.
     */
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

    /**
     * @param availableSlots is a list of available slots where insert the buying DevelopmentCard.
     *
     * send to player a CHOSEN_SLOT message with @param availableSlots.
     */
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

    /**
     * @param marbles is an array of player's chosen marbles.
     *
     * send to player a TAKE_MARBLE message with @param marbles as arrayList.
     */
    @Override
    public void chosenMarble(Marble[] marbles){
        ArrayList<Marble> chosenMarbles = new ArrayList<>();
        Collections.addAll(chosenMarbles, marbles);
        Message message = new MessageArrayListMarble(MessageType.TAKE_MARBLE, viewID, chosenMarbles);
        sendMessage(message);
    }

    /**
     * @param leaderCards are player's LeaderCards
     *
     * send to player a QHITE_CONVERSION_CARD message with @param leaderCards.
     */
    @Override
    public void choseWhiteConversionCard(LeaderCard[] leaderCards){
        int leaderCard1 = leaderCards[0].getCardID();
        int leaderCard2 = leaderCards[1].getCardID();
        Message message = new MessageTwoParameterInt(MessageType.WHITE_CONVERSION_CARD, viewID, leaderCard1, leaderCard2);
        sendMessage(message);
    }

    /**
     * @param nickName is nickname of quitting player.
     *
     * send to player a QUIT message and close connections.
     */
    public void quit(String nickName){
        connected = false;
        System.out.println(this.nickName + " is disconnected");
        exit(nickName);
        disconnect();
    }

    /**
     * @param message is one message.
     *
     * send to player @param message.
     */
    public void sendMessage(Message message){
        try {
            out.flush();
            out.writeObject(message);
        } catch (IOException e) {
            disconnect();
        } catch (NullPointerException ignored){}
        //NullPointerException is ignored because out will be null only for tests to avoid view sending messages.
    }

    /**
     * @param message summarize winner info.
     *
     * send to player @param message.
     */
    public void endGame(Message message){
        connected = false;
        System.out.println(nickName + " is disconnected");
        sendMessage(message);
        disconnect();
    }

    /**
     * @param errorType is the type of error to be communicated to player.
     *
     * send to player an error message with @param errorType. In case of ALREADY_TAKEN_NICKNAME also set nickname as null.
     */
    public void errorMessage(ErrorType errorType){
        if(errorType == ErrorType.ALREADY_TAKEN_NICKNAME)
            nickName = null;
        sendMessage(new ErrorMessage( viewID, errorType));
    }

    /**
     * interrupt pingThread and inThread and then close connections.
     */
    private void disconnect(){
        if(connected) {
            if (nickName == null)
                System.err.println("Client disconnected brutally");
            else
                System.err.println(nickName + " disconnected brutally");
            waitAMoment();
            connected = false;
        }
        try {
            pingThread.interrupt();
            inThread.interrupt();
        } catch (NullPointerException ignored){}
        try {
            in.close();
            out.close();
        } catch (IOException | NullPointerException ignored) {}
    }

    @Override
    public String toString() {
        return nickName;
    }

    /**
     * @param o is game class.
     * @param arg is message.
     *
     * any received message from Game is sent to player.
     */
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
