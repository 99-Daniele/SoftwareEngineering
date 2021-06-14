package it.polimi.ingsw.view;

import it.polimi.ingsw.model.games.states.GameStates;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.model_view.GameView;
import it.polimi.ingsw.view.model_view.MarketView;
import it.polimi.ingsw.view.model_view.ResourceContainerView;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public abstract class ClientView extends Application implements Observer {

    private static GameView game;
    private static GameStates currentState;
    private static ArrayList<String> serverMessages = new ArrayList<>();

    public ClientView() {
        game = new GameView();
        currentState = GameStates.FIRST_ACTION_STATE;
    }

    public void launchGUI(){}

    public void launchGUI(String hostName, int port){}

    public void launchCLI(){}

    public void launchCLI(String hostname, int port){}

    @Override
    public void start(Stage stage) throws Exception {}

    public static void endGame(){
        ClientSocket.setDisconnected();
        ClientSocket.disconnect();
        System.exit(1);
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
        return game.get_Row_Column(cardID);
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

    public static boolean isSlotEmpty(int player, int slot){
        return game.isSlotEmpty(player, slot);
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

    public static int getFaithPoints(int position){return game.getFaithPoints(position);}

    public static int getVictoryPoints(int position){return game.getVictoryPoints(position);}

    public static int getCurrentPope(){return game.getCurrentPope();}

    public static int getNumOfPlayers(){
        return game.getNumOfPlayers();
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            Message returnMessage = (Message) arg;
            switch (returnMessage.getMessageType()) {
                case LOGIN:
                    login_message(returnMessage);
                    break;
                case NEW_PLAYER:
                    new_player_message(returnMessage);
                    break;
                case PLAYERS:
                    players_message(returnMessage);
                    break;
                case LEADER_CARD:
                    leader_card_choice(returnMessage);
                    break;
                case START_GAME:
                    start_game_message();
                    break;
                case MARKET:
                    market_message(returnMessage);
                    break;
                case DECKBOARD:
                    deckBoard_message(returnMessage);
                    break;
                case OK:
                    ok_message();
                    break;
                case PONG:
                    break;
                case TURN:
                    turn_message(returnMessage);
                    break;
                case END_TURN:
                    end_turn_message(returnMessage);
                    break;
                case BUY_CARD:
                    buy_card_message(returnMessage);
                    break;
                case CHOSEN_SLOT:
                    chosen_slot_message(returnMessage);
                    break;
                case CARD_REMOVE:
                    card_remove_message(returnMessage);
                    break;
                case RESOURCE_AMOUNT:
                    resource_amount_message(returnMessage);
                    break;
                case END_PRODUCTION:
                    endProductionMessage(returnMessage);
                    break;
                case TAKE_MARBLE:
                    take_marble_message(returnMessage);
                    break;
                case MARKET_CHANGE:
                    market_change(returnMessage);
                    break;
                case WHITE_CONVERSION_CARD:
                    white_conversion_card_message(returnMessage);
                    break;
                case FAITH_POINTS_INCREASE:
                    faith_points_message(returnMessage);
                    break;
                case VATICAN_REPORT:
                    vatican_report_message(returnMessage);
                    break;
                case INCREASE_WAREHOUSE:
                    increase_warehouse_message(returnMessage);
                    break;
                case SWITCH_DEPOT:
                    switch_depot_message(returnMessage);
                    break;
                case LEADER_CARD_ACTIVATION:
                    leader_card_activation_message(returnMessage);
                    break;
                case EXTRA_DEPOT:
                    extra_depot_message(returnMessage);
                    break;
                case LEADER_CARD_DISCARD:
                    leader_card_discard_message(returnMessage);
                    break;
                case QUIT:
                    quit_message(returnMessage);
                    break;
                case END_GAME:
                    end_game_message(returnMessage);
                    break;
                case ERR:
                    error_message(returnMessage);
                    break;
                default:
                    System.err.println("\nUnexpected message from Server.");
                    break;
            }
        } catch (IOException | InterruptedException | ClassCastException e) {
        }
    }

    public abstract void login_message(Message message);

    public abstract void new_player_message(Message message);

    public void players_message(Message message){
        MessageArrayListString m = (MessageArrayListString) message;
        game.setPlayers(m.getNickNames());
    }

    public abstract void start_game_message() throws IOException;

    public void startGame(){
        game.startGame();
    }

    public boolean isGameStarted(){
        return game.isStartGame();
    }

    public void market_message(Message message){
        MessageMarket m = (MessageMarket) message;
        game.setMarket(m.getMarket());
    }

    public void deckBoard_message(Message message){
        MessageArrayListInt m = (MessageArrayListInt) message;
        game.setFirstDeckCards(m.getParams());
    }

    public abstract void leader_card_choice(Message message) throws IOException, InterruptedException;

    public void ok_message() throws IOException, InterruptedException{
        switch (getCurrentState()){
            case BUY_CARD_STATE:
                setCurrentState(GameStates.END_TURN_STATE);
                break;
            case FIRST_POWER_STATE:
                setCurrentState(GameStates.ACTIVATE_PRODUCTION_STATE);
                break;
        }
    }

    public abstract void turn_message(Message message) throws InterruptedException, IOException;

    public abstract void end_turn_message(Message message) throws InterruptedException, IOException;

    public void buy_card_message(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        game.addDevelopmentCard(m.getClientID(), m.getPar1(), m.getPar2());
    }

    public void card_remove_message(Message message){
        MessageFourParameterInt m = (MessageFourParameterInt) message;
        game.replaceCard(m.getPar1(), m.getPar2(), m.getPar4());
    }

    public void resource_amount_message(Message message) {
        MessageOneResourceTwoInt m = (MessageOneResourceTwoInt) message;
        game.newAmount(m.getClientID(), m.getResource(), m.getPar1(), m.getPar2());
    }

    public abstract void endProductionMessage(Message message);

    public void market_change(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        if (m.getPar1() == 0) {
            game.slideRow(m.getPar2());
        } else {
            game.slideColumn(m.getPar2());
        }
    }

    public abstract void white_conversion_card_message(Message message) throws IOException;

    public void faith_points_message(Message message){
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        game.increaseFaithPoints(m.getClientID(), m.getPar());
    }

    public void increase_warehouse_message(Message message){
        MessageOneIntOneResource m = (MessageOneIntOneResource) message;
        if(m.getPar1() != -1) {
            game.increaseWarehouse(m.getClientID(), m.getResource(), m.getPar1());
        }
    }

    public void switch_depot_message(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        game.switchDepot(m.getClientID(), m.getPar1(), m.getPar2());
    }

    public void vatican_report_message(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        game.increaseVictoryPoints(m.getClientID(), m.getPar2());
    }

    public void leader_card_activation_message(Message message){
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        game.addLeaderCard(m.getClientID(), m.getPar());
    }

    public void extra_depot_message(Message message){
        MessageOneIntOneResource m = (MessageOneIntOneResource) message;
        game.addExtraDepot(m.getClientID(), m.getResource());
    }

    public void leader_card_discard_message(Message message) {
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        game.discardLeaderCard(m.getClientID(), m.getPar());
    }

    public abstract void chosen_slot_message(Message message) throws IOException;

    public void take_marble_message(Message message) {
        MessageArrayListMarble m = (MessageArrayListMarble) message;
        game.setChosenMarbles(m.getMarbles());
    }

    public abstract void quit_message(Message message);

    public abstract void end_game_message(Message message);

    public void error_message(Message message) throws IOException, InterruptedException{
        ErrorMessage m = (ErrorMessage) message;
        switch (m.getErrorType()){
            case ALREADY_TAKEN_NICKNAME:
                already_taken_nickName_error();
                break;
            case WRONG_PARAMETERS:
                wrong_parameters_error();
                break;
            case NOT_YOUR_TURN:
                wrong_turn_error();
                break;
            case FULL_SLOT:
                full_slot_error();
                break;
            case EMPTY_DECK:
                empty_deck_error();
                break;
            case EMPTY_SLOT:
                empty_slot_error();
                break;
            case WRONG_POWER:
                wrong_power_error();
                break;
            case NOT_ENOUGH_CARDS:
                not_enough_cards_error();
                break;
            case ILLEGAL_OPERATION:
                illegal_operation_error();
                break;
            case IMPOSSIBLE_SWITCH:
                impossible_switch_error();
                break;
            case NOT_ENOUGH_RESOURCES:
                not_enough_resource_error();
                break;
            case ALREADY_ACTIVE_LEADER_CARD:
                already_active_error();
                break;
            case ALREADY_DISCARD_LEADER_CARD:
                already_discard_error();
                break;
        }
    }

    public abstract void already_taken_nickName_error() ;

    public abstract void wrong_parameters_error();

    public abstract void wrong_turn_error();

    public abstract void empty_deck_error() ;

    public abstract void empty_slot_error();

    public abstract void wrong_power_error();

    public abstract void not_enough_cards_error() ;

    public abstract void full_slot_error();

    public abstract void illegal_operation_error();

    public abstract void impossible_switch_error();

    public abstract void not_enough_resource_error();

    public abstract void already_active_error();

    public abstract void already_discard_error();

}
