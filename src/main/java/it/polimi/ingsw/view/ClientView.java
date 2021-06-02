package it.polimi.ingsw.view;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.model_view.Game_View;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public abstract class ClientView extends Application implements Observer {

    private final Game_View game;

    public ClientView() {
        game = new Game_View();
    }

    public void launchGUI(){}

    public void launchCLI(){}

    @Override
    public void start(Stage stage) throws Exception {}

    public Game_View getGame() {
        return game;
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
        Message_ArrayList_String m = (Message_ArrayList_String) message;
        game.setPlayers(m.getNickNames());
    }

    public abstract void start_game_message() throws IOException;

    public void startGame(){
        game.startGame();
    }

    private void market_message(Message message){
        Message_Market m = (Message_Market) message;
        game.setMarket(m.getMarket());
    }

    private void deckBoard_message(Message message){
        Message_ArrayList_Int m = (Message_ArrayList_Int) message;
        game.setFirstDeckCards(m.getParams());
    }

    public int[] getRowColumn(int cardID){
        return game.get_Row_Column(cardID);
    }

    public ArrayList<Integer> getDevelopmentCards(int position){
        return game.getDevelopmentCards(position);
    }

    public void discardLeaderCard(int position, int chosenLeaderCard){
        game.discardLeaderCard(position, chosenLeaderCard);
    }

    public int getLeaderCard(int position, int card){
        return game.getLeaderCard(position, card);
    }

    public void setLeaderCard(int position, int card1, int card2){
        game.setMyLeaderCards(position, card1, card2);
    }

    public String getNickname(int player){
        return game.getNickname(player);
    }

    public int getNumOfPlayers(){
        return game.getNumOfPlayers();
    }

    public abstract void leader_card_choice(Message message) throws IOException, InterruptedException;

    public abstract void ok_message() throws IOException, InterruptedException;

    public abstract void turn_message(Message message) throws InterruptedException, IOException;

    public abstract void end_turn_message(Message message) throws InterruptedException, IOException;

    public void buy_card_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        game.addDevelopmentCard(m.getClientID(), m.getPar1(), m.getPar2());
    }

    public void card_remove_message(Message message){
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        game.replaceCard(m.getPar1(), m.getPar2(), m.getPar4());
    }

    private void resource_amount_message(Message message) {
        Message_One_Resource_Two_Int m = (Message_One_Resource_Two_Int) message;
        game.newAmount(m.getClientID(), m.getResource(), m.getPar1(), m.getPar2());
    }

    public abstract void endProductionMessage(Message message);

    public void market_change(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if (m.getPar1() == 0) {
            game.slideRow(m.getPar2());
        } else {
            game.slideColumn(m.getPar2());
        }
    }

    public abstract void white_conversion_card_message(Message message) throws IOException;

    public void faith_points_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        game.increaseFaithPoints(m.getClientID(), m.getPar());
    }

    public void increase_warehouse_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        if(m.getPar1() != -1) {
            game.increaseWarehouse(m.getClientID(), m.getResource(), m.getPar1());
        }
    }

    public void switch_depot_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        game.switchDepot(m.getClientID(), m.getPar1(), m.getPar2());
    }

    public void vatican_report_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        game.increaseVictoryPoints(m.getClientID(), m.getPar2());
    }

    public void leader_card_activation_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        game.addLeaderCard(m.getClientID(), m.getPar());
    }

    public void extra_depot_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        game.addExtraDepot(m.getClientID(), m.getResource());
    }

    public void leader_card_discard_message(Message message) {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        game.discardLeaderCard(m.getClientID(), m.getPar());
    }

    public abstract void chosen_slot_message(Message message) throws IOException;

    public void take_marble_message(Message message) {
        Message_ArrayList_Marble m = (Message_ArrayList_Marble) message;
        game.setChosenMarbles(m.getMarbles());
    }

    public ArrayList<Marble> getMarbles(){
        return game.getChosenMarbles();
    }

    public void setMarbles(ArrayList <Marble> marbles){
        game.setChosenMarbles(marbles);
    }

    public abstract void quit_message(Message message);

    public abstract void end_game_message(Message message);

    public abstract void error_message(Message message) throws IOException, InterruptedException;
}
