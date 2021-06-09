package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.App;
import it.polimi.ingsw.model.games.states.GAME_STATES;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.CLI.CLI_Printer;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.GUI.sceneController.InitSceneController;
import it.polimi.ingsw.view.GUI.sceneController.NicknameSceneController;
import it.polimi.ingsw.view.GUI.sceneController.YourTurnSceneController;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;


public class GUI extends ClientView {

    private static boolean connected = false;
    private static int position = -1;
    private static boolean turn;
    private static ArrayList<String> serverMessages = new ArrayList<>();

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

    public static ArrayList<String> getServerMessages() {
        return serverMessages;
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
        Platform.runLater(() -> {
            try {
                if (message.getClientID() == 0) {
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
        System.out.println(GUI.position);
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
            if(isState(GAME_STATES.BUY_CARD_STATE))
                YourTurnSceneController.endTurn();
            if(isState(GAME_STATES.FIRST_POWER_STATE) || ClientView.isState(GAME_STATES.ACTIVATE_PRODUCTION_STATE)){
                setCurrentState(GAME_STATES.ACTIVATE_PRODUCTION_STATE);
                YourTurnSceneController.production();
            }
            if(isState(GAME_STATES.END_TURN_STATE)){
                YourTurnSceneController.endTurn();
            }
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
        if (position == 0) {
            if (message.getClientID() == getNumOfPlayers() - 1) {
                setCurrentState(GAME_STATES.FIRST_ACTION_STATE);
                turn = true;
                if (SceneController.isCurrentScene("#radiobutBuyCard"))
                    Platform.runLater(() -> YourTurnSceneController.yourTurn());
            }
        }
        else if (message.getClientID() == position - 1) {
            setCurrentState(GAME_STATES.FIRST_ACTION_STATE);
            turn = true;
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.yourTurn());
        }
        else {
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.notYourTurn());
        }
    }

    public static boolean isMyTurn() {
        return turn;
    }

    @Override
    public void buy_card_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        String newMessage = "Player " + getNickname(m.getClientID()) + " bought a new card and inserted it in " +
                "the " + m.getPar2() + "° slot.";
        super.buy_card_message(message);
        if(m.getClientID() != GUI.getPosition()) {
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.buy_card_message(m.getPar2(), m.getPar1(), newMessage));
        serverMessages.add(0, newMessage);
    }

    @Override
    public void card_remove_message(Message message){
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        String newMessage;
        super.card_remove_message(message);
        if(getNumOfPlayers() == 1 && m.getClientID() == 1)
            newMessage = "Ludovico has removed one deck card from row " + (m.getPar1() +1 + " and" + " column " + (m.getPar2() +1));
        else
            newMessage = "Deck card from row " + (m.getPar1() + 1) + " and column " + (m.getPar2() + 1) + " has been removed";
        if (SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.cardRemoveMessage(m.getPar1(), m.getPar2(), m.getPar4(), newMessage));
        serverMessages.add(0, newMessage);
    }

    @Override
    public void market_change(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        String newMessage;
        if (m.getPar1() == 0) {
            newMessage = "Player " + getNickname(m.getClientID()) + " has chosen row " + m.getPar2() + " of the market";
        } else {
            newMessage = "Player " + getNickname(m.getClientID()) + " has chosen column " + m.getPar2() + " of the market";
        }
        super.market_change(message);
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.marketChangeMessage(m.getPar1() == 0, m.getPar2(), newMessage));
        serverMessages.add(0, newMessage);
    }

    @Override
    public void faith_points_message(Message message) {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        String newMessage;
        if (getNumOfPlayers() == 1 && m.getClientID() == 1)
            newMessage = "Ludovico has increased his faith points. Now it has " + m.getPar();
        else
            newMessage = "Player " + getNickname(m.getClientID()) + " has increased its faith points";
        super.faith_points_message(message);
        if(m.getClientID() == position){
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.increaseFaithPointsMessage(m.getPar(), false, newMessage));
        }
        else if (GUI.getNumOfPlayers() > 1) {
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else if (GUI.getNumOfPlayers() == 1) {
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.increaseFaithPointsMessage(m.getPar(), true, newMessage));
        }
        serverMessages.add(0, newMessage);
    }

    @Override
    public void increase_warehouse_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        String newMessage;
        if(m.getPar1() != -1)
            newMessage = "Player " + getNickname(m.getClientID()) + " has inserted 1 " + m.getResource().toString()
                + " in its " + m.getPar1() + "° depot";
        else
            newMessage = "Player " + getNickname(m.getClientID()) + " has discarded 1 marble";
        super.increase_warehouse_message(message);
        if(m.getPar1() == -1 || m.getClientID() != position){
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else{
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.increaseWarehouseMessage(m.getPar1(), newMessage));
        }
        serverMessages.add(0, newMessage);
    }

    @Override
    public void switch_depot_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        String newMessage = "Player " + getNickname(m.getClientID()) + " has switched its " + m.getPar1()
                + "° depot with its " + m.getPar2() + "° depot";
        super.switch_depot_message(message);
        if(m.getClientID() != position) {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.switchDepotMessage(m.getPar1(), m.getPar2(), newMessage));
        }
        serverMessages.add(0, newMessage);
    }

    @Override
    public void leader_card_activation_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        String newMessage = "Player " + getNickname(m.getClientID()) + " has activated a leader card";
        super.leader_card_activation_message(message);
        if(m.getClientID() != position) {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.leaderCardActivationMessage(newMessage));
        }
        serverMessages.add(0, newMessage);
    }

    @Override
    public void extra_depot_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        String newMessage = "Player " + getNickname(m.getClientID()) + " has a new extra depot of " + m.getResource().toString();
        super.extra_depot_message(message);
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> SceneController.addMessage(newMessage));
        serverMessages.add(0, newMessage);
    }

    @Override
    public void leader_card_discard_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        String newMessage = "Player " + getNickname(m.getClientID()) + " has discarded one leader card";
        super.leader_card_discard_message(message);
        if(m.getClientID() != position) {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.leaderCardDiscardMessage(newMessage));
        }
        serverMessages.add(0, newMessage);
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
        String newMessage = "Player " + getNickname(message.getClientID()) + " has activated production powers";
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> SceneController.addMessage(newMessage));
        serverMessages.add(0, newMessage);
    }

    @Override
    public void resource_amount_message(Message message) {
        super.resource_amount_message(message);
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.modifiedResource());
    }

    @Override
    public void white_conversion_card_message(Message message){
        ClientView.setCurrentState(GAME_STATES.WHITE_CONVERSION_CARD_STATE);
        YourTurnSceneController.whiteConversionMessage();
    }

    @Override
    public void vatican_report_message(Message message) {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        String newMessage;
        if(getNumOfPlayers() == 1 && m.getPar1() == 1)
            newMessage = "Ludovico activated Vatican Report";
        else
            newMessage = "Player " + getNickname(m.getPar1()) + " activated Vatican Report";
        super.vatican_report_message(message);
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.vaticanReportMessage(m.getPar1(), m.getPar2(), newMessage));
        serverMessages.add(0, newMessage);
    }

    @Override
    public void chosen_slot_message(Message message){
        Message_Three_Parameter_Int m = (Message_Three_Parameter_Int) message;
        YourTurnSceneController.choseSlotMessage(m.getPar1(), m.getPar2(), m.getPar3());
    }

    @Override
    public void quit_message(Message message) {
        //print quit message
        super.stop();
    }

    @Override
    public void end_game_message(Message message) {
        //print end game message
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
        //empty deck message
        Platform.runLater(() -> YourTurnSceneController.yourTurn());
    }

    @Override
    public void empty_slot_error() {
    }

    @Override
    public void wrong_power_error() {
        //wrong power message
        Platform.runLater(() -> {
            if(isState(GAME_STATES.FIRST_POWER_STATE)){
                setCurrentState(GAME_STATES.FIRST_ACTION_STATE);
                YourTurnSceneController.disableProductions();
                YourTurnSceneController.yourTurn();
            }
            else {
                YourTurnSceneController.production();
            }
        });
    }

    @Override
    public void not_enough_cards_error() {
    }

    @Override
    public void full_slot_error() {
        Platform.runLater(() -> YourTurnSceneController.yourTurn());
    }

    @Override
    public void illegal_operation_error() {
    }

    @Override
    public void impossible_switch_error() {
    }

    @Override
    public void not_enough_resource_error() {
        Platform.runLater(() -> {
            if(isState(GAME_STATES.BUY_CARD_STATE))
                YourTurnSceneController.yourTurn();
            else if(isState(GAME_STATES.FIRST_POWER_STATE)){
                setCurrentState(GAME_STATES.FIRST_ACTION_STATE);
                YourTurnSceneController.disableProductions();
                YourTurnSceneController.yourTurn();
            }
            else if(isState(GAME_STATES.ACTIVATE_PRODUCTION_STATE)){
                YourTurnSceneController.production();
            }
        });
    }

    @Override
    public void already_active_error() {
        //already activated error message
    }

    @Override
    public void already_discard_error() {
        //already discarded error message
    }
}
