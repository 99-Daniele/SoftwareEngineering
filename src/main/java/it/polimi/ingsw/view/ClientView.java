package it.polimi.ingsw.view;

import it.polimi.ingsw.model.games.states.GameStates;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.modelView.*;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * ClientView is View on Client. It his an abstract class which could be override by CLI or GUI.
 */
public abstract class ClientView extends Application implements Observer {

    private static GameView game;
    private static GameStates currentState;
    private static final ArrayList<String> serverMessages = new ArrayList<>();

    public ClientView() {
        game = new GameView();
        currentState = GameStates.FIRST_ACTION_STATE;
    }

    public void launchGUI(){}

    public void launchGUI(String hostName, int port){}

    public void launchCLI(){}

    public void launchCLI(String hostname, int port){}

    @Override
    public void start(Stage stage) {}

    /**
     * disconnect from Server.
     */
    public static void endGame(){
        ClientSocket.setDisconnected();
        ClientSocket.disconnect();
    }

    public GameView getGame() {
        return game;
    }

    public static GameStates getCurrentState() {
        return currentState;
    }

    public static boolean isState(GameStates state){
        return  currentState  == state;
    }

    public static void setCurrentState(GameStates currentState) {
        ClientView.currentState = currentState;
    }

    public static ArrayList<String> getServerMessages() {
        return serverMessages;
    }

    public static void addServerMessage(String message){
        serverMessages.add(0, message);
    }

    public int[] getRowColumn(int cardID){
        return game.getRowColumn(cardID);
    }

    public static void discardLeaderCard(int position, int chosenLeaderCard){
        game.discardLeaderCard(position, chosenLeaderCard);
    }

    public static void setLeaderCard(int position, int card1, int card2){
        game.setMyLeaderCards(position, card1, card2);
    }

    public static String getNickname(int player){
        return game.getNickname(player);
    }

    public static MarketView getMarket(){
        return game.getMarket();
    }

    public static ArrayList<Integer> getLeaderCards(int position){
        return game.getLeaderCards(position);
    }

    public static boolean isLeaderCardActive(int player, int leaderCard){
        return game.isLeaderCardActive(player, leaderCard);
    }

    public static boolean isAdditionalPowerCard(int player, int leaderCard){
        return game.isAdditionalPowerCard(player, leaderCard);
    }

    public static boolean isSlotEmpty(int player, int slot){
        return !game.isSlotEmpty(player, slot);
    }

    public static ArrayList<Integer> getSlotCards(int position){
        return game.getDevelopmentCards(position);
    }

    public static ArrayList<Integer> getDevelopmentCards(){
        return game.getDecks().getDevelopmentCards();
    }

    public static ArrayList<ResourceContainerView> getWarehouse(int position){
        return game.getWarehouse(position);
    }

    public static ArrayList<Marble> getMarbles(){
        return game.getChosenMarbles();
    }

    public static int coinAmount(int position){
        return game.coinAmount(position);
    }

    public static int shieldAmount(int position){
        return game.shieldAmount(position);
    }

    public static int stoneAmount(int position){
        return game.stoneAmount(position);
    }

    public static int servantAmount(int position){
        return game.servantAmount(position);
    }

    public static boolean isSecondDepot(int position){return game.isSecondDepot(position);}

    public static int getFaithPoints(int position){return game.getFaithPoints(position);}

    public static int getVictoryPoints(int position){return game.getVictoryPoints(position);}

    public static int getCurrentPope(){return game.getCurrentPope();}

    public static int getNumOfPlayers(){
        return game.getNumOfPlayers();
    }

    /**
     * @param o is ClientSocket.
     * @param arg is one message received from Server.
     */
    @Override
    public void update(Observable o, Object arg) {
        try {
            Message returnMessage = (Message) arg;
            switch (returnMessage.getMessageType()) {
                case LOGIN:
                    loginMessage(returnMessage);
                    break;
                case NEW_PLAYER:
                    newPlayerMessage(returnMessage);
                    break;
                case PLAYERS:
                    playersMessage(returnMessage);
                    break;
                case LEADER_CARD:
                    leaderCardChoice(returnMessage);
                    break;
                case START_GAME:
                    startGameMessage();
                    break;
                case MARKET:
                    marketMessage(returnMessage);
                    break;
                case DECKBOARD:
                    deckBoardMessage(returnMessage);
                    break;
                case OK:
                    okMessage();
                    break;
                case PONG:
                    break;
                case TURN:
                    turnMessage(returnMessage);
                    break;
                case END_TURN:
                    endTurnMessage(returnMessage);
                    break;
                case BUY_CARD:
                    buyCardMessage(returnMessage);
                    break;
                case CHOSEN_SLOT:
                    chosenSlotMessage(returnMessage);
                    break;
                case CARD_REMOVE:
                    cardRemoveMessage(returnMessage);
                    break;
                case RESOURCE_AMOUNT:
                    resourceAmountMessage(returnMessage);
                    break;
                case END_PRODUCTION:
                    endProductionMessage(returnMessage);
                    break;
                case TAKE_MARBLE:
                    takeMarbleMessage(returnMessage);
                    break;
                case MARKET_CHANGE:
                    marketChange(returnMessage);
                    break;
                case WHITE_CONVERSION_CARD:
                    whiteConversionCardMessage(returnMessage);
                    break;
                case FAITH_POINTS_INCREASE:
                    faithPointsMessage(returnMessage);
                    break;
                case VATICAN_REPORT:
                    vaticanReportMessage(returnMessage);
                    break;
                case INCREASE_WAREHOUSE:
                    increaseWarehouseMessage(returnMessage);
                    break;
                case SWITCH_DEPOT:
                    switchDepotMessage(returnMessage);
                    break;
                case LEADER_CARD_ACTIVATION:
                    leaderCardActivationMessage(returnMessage);
                    break;
                case EXTRA_DEPOT:
                    extraDepotMessage(returnMessage);
                    break;
                case LEADER_CARD_DISCARD:
                    leaderCardDiscardMessage(returnMessage);
                    break;
                case QUIT:
                    quitMessage(returnMessage);
                    break;
                case END_GAME:
                    endGameMessage(returnMessage);
                    break;
                case ERR:
                    errorMessage(returnMessage);
                    break;
                default:
                    System.err.println("\nUnexpected message from Server.");
                    break;
            }
        } catch (IOException | ClassCastException ignored) {
        }
    }

    public abstract void loginMessage(Message message);

    public abstract void newPlayerMessage(Message message);

    /**
     * @param message is a PLAYERS message.
     *
     * set received player's nicknames.
     */
    public void playersMessage(Message message){
        MessageArrayListString m = (MessageArrayListString) message;
        game.setPlayers(m.getNickNames());
    }

    public abstract void startGameMessage() throws IOException;

    public void startGame(){
        game.startGame();
    }

    public static boolean isGameStarted(){
        return game.isStartGame();
    }

    /**
     * @param message is a MARKET message.
     *
     * set received market.
     */
    public void marketMessage(Message message){
        MessageMarket m = (MessageMarket) message;
        game.setMarket(m.getMarket());
    }

    /**
     * @param message is a DECKBOARD message.
     *
     * set received list of 16 first DevelopmentCards.
     */
    public void deckBoardMessage(Message message){
        MessageArrayListInt m = (MessageArrayListInt) message;
        game.setFirstDeckCards(m.getParams());
    }

    public abstract void leaderCardChoice(Message message);

    public void okMessage() {
        switch (getCurrentState()){
            case BUY_CARD_STATE:
                setCurrentState(GameStates.END_TURN_STATE);
                break;
            case FIRST_POWER_STATE:
                setCurrentState(GameStates.ACTIVATE_PRODUCTION_STATE);
                break;
        }
    }

    public abstract void turnMessage(Message message);

    public abstract void endTurnMessage(Message message);

    /**
     * @param message is a BUY_CARD message.
     *
     * add to selected player received DevelopmentCards.
     */
    public void buyCardMessage(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        game.addDevelopmentCard(m.getClientID(), m.getPar1(), m.getPar2());
    }

    /**
     * @param message is CARD_REMOVE message.
     *
     * replace card on selected row and column with received DevelopmentCards.
     */
    public void cardRemoveMessage(Message message){
        MessageFourParameterInt m = (MessageFourParameterInt) message;
        game.replaceCard(m.getPar1(), m.getPar2(), m.getPar4());
    }

    /**
     * @param message is NEW_AMOUNT message.
     *
     * set warehouse and strongbox amount of selected resource.
     */
    public void resourceAmountMessage(Message message) {
        MessageOneResourceTwoInt m = (MessageOneResourceTwoInt) message;
        game.newAmount(m.getClientID(), m.getResource(), m.getPar1(), m.getPar2());
    }

    public abstract void endProductionMessage(Message message);

    /**
     * @param message is MARKET_CHANGE message.
     *
     * slide selected market row or column.
     */
    public void marketChange(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        if (m.getPar1() == 0) {
            game.slideRow(m.getPar2());
        } else {
            game.slideColumn(m.getPar2());
        }
    }

    public abstract void whiteConversionCardMessage(Message message);

    /**
     * @param message is FAITH_POINTS_INCREASE message.
     *
     * set selected player received faith points.
     */
    public void faithPointsMessage(Message message){
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        game.increaseFaithPoints(m.getClientID(), m.getPar());
    }

    /**
     * @param message is INCREASE_WAREHOUSE message.
     *
     * increase selected player warehouse by received resource.
     */
    public void increaseWarehouseMessage(Message message){
        MessageOneIntOneResource m = (MessageOneIntOneResource) message;
        if(m.getPar1() != -1) {
            game.increaseWarehouse(m.getClientID(), m.getResource(), m.getPar1());
        }
    }

    /**
     * @param message is SWITCH_DEPOT message.
     *
     * switch sleected depots.
     */
    public void switchDepotMessage(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        game.switchDepot(m.getClientID(), m.getPar1(), m.getPar2());
    }

    /**
     * @param message is VATICAN_REPORT message.
     *
     * set selected player received victory points.
     */
    public void vaticanReportMessage(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        game.increaseVictoryPoints(m.getClientID(), m.getPar2());
    }

    /**
     * @param message is LEADER_ACTIVATION message.
     *
     * add received LeaderCard to selected player.
     */
    public void leaderCardActivationMessage(Message message){
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        game.addLeaderCard(m.getClientID(), m.getPar());
    }

    /**
     * @param message is EXTRA_DEPOT message.
     *
     * add new ExtraDepot of received resource to selected player.
     */
    public void extraDepotMessage(Message message){
        MessageOneIntOneResource m = (MessageOneIntOneResource) message;
        game.addExtraDepot(m.getClientID(), m.getResource());
    }

    /**
     * @param message is LEADER_ACTIVATION message.
     *
     * remove received LeaderCard to selected player.
     */
    public void leaderCardDiscardMessage(Message message) {
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        game.discardLeaderCard(m.getClientID(), m.getPar());
    }

    public abstract void chosenSlotMessage(Message message);

    /**
     * @param message is TAKE_MARBLE message.
     *
     * set received marbles.
     */
    public void takeMarbleMessage(Message message) {
        MessageArrayListMarble m = (MessageArrayListMarble) message;
        game.setChosenMarbles(m.getMarbles());
    }

    public abstract void quitMessage(Message message);

    public abstract void endGameMessage(Message message);

    /**
     * @param message is an ERR message.
     */
    public void errorMessage(Message message) {
        ErrorMessage m = (ErrorMessage) message;
        switch (m.getErrorType()){
            case ALREADY_TAKEN_NICKNAME:
                alreadyTakenNickNameError();
                break;
            case WRONG_PARAMETERS:
                wrongParametersError();
                break;
            case NOT_YOUR_TURN:
                wrongTurnError();
                break;
            case FULL_SLOT:
                fullSlotError();
                break;
            case EMPTY_DECK:
                emptyDeckError();
                break;
            case EMPTY_SLOT:
                emptySlotError();
                break;
            case WRONG_POWER:
                wrongPowerError();
                break;
            case NOT_ENOUGH_CARDS:
                notEnoughCardsError();
                break;
            case ILLEGAL_OPERATION:
                illegalOperationError();
                break;
            case IMPOSSIBLE_SWITCH:
                impossibleSwitchError();
                break;
            case NOT_ENOUGH_RESOURCES:
                notEnoughResourceError();
                break;
            case ALREADY_ACTIVE_LEADER_CARD:
                alreadyActiveError();
                break;
            case ALREADY_DISCARD_LEADER_CARD:
                alreadyDiscardError();
                break;
        }
    }

    public abstract void alreadyTakenNickNameError() ;

    public abstract void wrongParametersError();

    public abstract void wrongTurnError();

    public abstract void emptyDeckError() ;

    public abstract void emptySlotError();

    public abstract void wrongPowerError();

    public abstract void notEnoughCardsError() ;

    public abstract void fullSlotError();

    public abstract void illegalOperationError();

    public abstract void impossibleSwitchError();

    public abstract void notEnoughResourceError();

    public abstract void alreadyActiveError();

    public abstract void alreadyDiscardError();
}
