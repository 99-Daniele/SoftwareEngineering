package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.App;
import it.polimi.ingsw.model.games.states.GAME_STATES;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.GUI.sceneController.InitSceneController;
import it.polimi.ingsw.view.GUI.sceneController.NicknameSceneController;
import it.polimi.ingsw.view.GUI.sceneController.YourTurnSceneController;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;


public class GUI extends ClientView {

    private static boolean connected = false;
    private static int position = -1;
    private static boolean turn;

    @Override
    public void launchGUI() {
        App.createClient(this);
        launch();
    }

    @Override
    public void launchGUI(String hostname, int port){
        App.createClient(this);
        connectToSever(hostname, port);
    }

    public static int getPosition(){
        return position;
    }

    private void connectToSever(String hostname, int port){
        try {
            App.startClient(hostname, port);
            connected = true;
            launch();
        } catch (IOException e) {
            launch();
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        Platform.runLater(() -> {
            try {
                if (connected) {
                    SceneController.setScene(stage, "/fxml/nicknameScene");
                } else {
                    SceneController.setScene(stage, "/fxml/connectionScene");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void login_message(Message message) {
        position = message.getClientID();
        Platform.runLater(() -> {
            try {
                if (position == 0) {
                    NicknameSceneController.askNumPlayer();
                } else {
                    NicknameSceneController.waitPlayers();
                }
            } catch (NullPointerException e){}
        });
    }

    @Override
    public void new_player_message(Message message) {
    }

    @Override
    public void players_message(Message message) {
        super.players_message(message);
        GUI.position = message.getClientID();
    }

    @Override
    public void start_game_message() throws IOException {
        super.startGame();
        ClientSocket.sendMessage(new Message(MessageType.TURN, position));
    }

    @Override
    public void leader_card_choice(Message message){
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        Platform.runLater(() -> {
            SceneController.changeRootPane("/fxml/initScene");
            try {
                InitSceneController.askLeaders(m.getPar1(), m.getPar2(), m.getPar3(), m.getPar4());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void ok_message() {
        Platform.runLater(() -> {
            if (SceneController.isCurrentScene("#nicknameLabel"))
                NicknameSceneController.waitPlayers();
        });
    }

    @Override
    public void turn_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        setCurrentState(GAME_STATES.FIRST_ACTION_STATE);
        Platform.runLater(() -> {
            turn = (m.getPar() == 1);
            if (m.getPar() == 1) {
                SceneController.changeRootPane("/fxml/yourTurnScene");
            }
            else {
                SceneController.changeRootPane("/fxml/yourTurnScene");
                YourTurnSceneController.notYourTurn();
            }
        });
    }

    @Override
    public void end_turn_message(Message message) {
        if (message.getClientID() != position) {
            //segnalo che un altro giocatore ha finito il suo turno
        }
        if (position == 0) {
            if (message.getClientID() == getNumOfPlayers() - 1) {
                turn = true;
                Platform.runLater(() ->
                        SceneController.changeRootPane("/fxml/yourTurnScene"));
            }
        } else if (message.getClientID() == position - 1) {
            turn = true;
            Platform.runLater(() ->
                    SceneController.changeRootPane("/fxml/yourTurnScene"));
        }
    }

    public static boolean isMyTurn() {
        return turn;
    }

    @Override
    public void buy_card_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        super.buy_card_message(message);
        if(m.getClientID() != GUI.getPosition());
        //    System.out.println("Player " + super.getNickname(m.getClientID()) + " bought a new card and inserted it in " +
        //"the " + m.getPar2() + "° slot.");//mettere nel quadrato gui laterale
        else if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.buy_card_message(m.getPar2(), m.getPar1()));
    }

    @Override
    public void card_remove_message(Message message){
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        super.card_remove_message(message);
        if(getNumOfPlayers() == 1 && m.getClientID() == 1);
        //System.out.println("Ludovico has removed one deck card from row " + (m.getPar1() +1 + " and" + " column " + (m.getPar2() +1)));
        else if(m.getClientID() != GUI.getPosition());
        //print message of removed card
        else if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.cardRemoveMessage(m.getPar1(), m.getPar2(), m.getPar4()));
    }

    @Override
    public void market_change(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        super.market_change(message);
        if(m.getClientID() != GUI.getPosition());
        //print change market message
        else if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.marketChangeMessage(m.getPar1() == 0, m.getPar2()));
    }

    @Override
    public void faith_points_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        super.faith_points_message(message);
        if(m.getClientID() == position){
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.increaseFaithPointsMessage(m.getPar(), false));
        }
        else if(GUI.getNumOfPlayers() == 1){
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.increaseFaithPointsMessage(m.getPar(), true));
        }
        else;
        //print faith points increase
    }

    @Override
    public void increase_warehouse_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        super.increase_warehouse_message(message);
        if(m.getClientID() != position);
        //print warehouse increase
        else if(m.getPar1() != -1){
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.increaseWarehouseMessage(m.getPar1()));
        }
        else;
    }

    @Override
    public void switch_depot_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        super.switch_depot_message(message);
        if(m.getClientID() != position);
        //print switch depot message
        else {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.switchDepotMessage(m.getPar1(), m.getPar2()));
        }
    }

    @Override
    public void leader_card_activation_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        super.leader_card_activation_message(message);
        if(m.getClientID() != position);
        //print message of activate leader
        else {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.leaderCardActivationMessage(m.getPar()));
        }
    }

    @Override
    public void extra_depot_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        super.extra_depot_message(message);
        if(m.getClientID() != position);
        //print message of extra depot
    }

    @Override
    public void leader_card_discard_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        super.leader_card_discard_message(message);
        if(m.getClientID() != position);
        //print message of discard leader and print leader card
        else {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.leaderCardDiscardMessage(m.getPar()));
        }
    }

    @Override
    public void take_marble_message(Message message) {
        Message_ArrayList_Marble m = (Message_ArrayList_Marble) message;
        super.take_marble_message(message);
        if(m.getClientID() != position);
        //print chosen marbles
    }

    @Override
    public void endProductionMessage(Message message) {
        if(message.getClientID() != position);
        //print message of end production
    }

    @Override
    public void white_conversion_card_message(Message message){
        YourTurnSceneController.whiteConversionMessage();
    }

    @Override
    public void vatican_report_message(Message message) {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        super.vatican_report_message(message);
        if(m.getClientID() != position);
        //message of vatican report
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.vaticanReportMessage(m.getPar1(), m.getPar2()));
    }

    @Override
    public void chosen_slot_message(Message message){
        Message_Three_Parameter_Int m = (Message_Three_Parameter_Int) message;
        YourTurnSceneController.choseSlotMessage(m.getPar1(), m.getPar2(), m.getPar3());
    }

    @Override
    public void quit_message(Message message) {
        super.stop();
    }

    @Override
    public void end_game_message(Message message) {
        super.stop();
    }

    @Override
    public void already_taken_nickName_error() {
        Platform.runLater(() -> NicknameSceneController.alreadyTakenNickName());
    }

    @Override
    public void wrong_parameters_error() {
    }

    @Override
    public void wrong_turn_error(){
    }

    @Override
    public void empty_deck_error() {
    }

    @Override
    public void empty_slot_error() {
    }

    @Override
    public void wrong_power_error() {
    }

    @Override
    public void not_enough_cards_error() {
    }

    @Override
    public void full_slot_error() {
    }

    @Override
    public void illegal_operation_error() {
    }

    @Override
    public void impossible_switch_error() {
    }

    @Override
    public void not_enough_resource_error() {
    }

    @Override
    public void already_active_error() {
    }

    @Override
    public void already_discard_error() {
    }
}
