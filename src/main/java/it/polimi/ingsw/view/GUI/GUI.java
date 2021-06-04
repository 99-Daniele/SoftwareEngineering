package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.App;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.ClientView;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;



public class GUI extends ClientView {

    private static boolean connected = false;
    private static SceneController sceneController = new SceneController();
    private static int position = -1;

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
        /*
        if(connected) {
            sceneController = new NicknameSceneController();
            Platform.runLater(() -> {
                try {
                    sceneController.setScene(stage,"/fxml/nicknameScene");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        else{
            sceneController = new ConnectionSceneController();
            Platform.runLater(() -> {
                try {
                    sceneController.setScene(stage,"/fxml/connectionScene");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
         */

        Platform.runLater(() -> {
            try {
                sceneController.setScene(stage,"/fxml/nicknameScene");
                //sceneController.setScene(stage,"/fxml/connectionScene");
                //sceneController.setScene(stage,"/fxml/opponentPlayerboardScene");
                //sceneController.setScene(stage,"/fxml/initScene");
                //sceneController.setScene(stage,"/fxml/yourTurnScene");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void setRoot(SceneController sceneController, String fxml) {
        GUI.sceneController = sceneController;
        sceneController.changeRootPane(fxml);
    }

    @Override
    public void login_message(Message message) {
        position = message.getClientID();
        if(position == 0) {
            //SceneController isc = new NicknameSceneController();
            //setRoot(sceneController, "/fxml/nickNameScene");
            sceneController.askNumPlayer();
        }
        else {
            SceneController isc = new InitSceneController();
            setRoot(isc, "/fxml/initScene");
        }
    }

    @Override
    public void new_player_message(Message message) {
        SceneController isc = new InitSceneController();
        setRoot(isc, "/fxml/initScene");
    }

    @Override
    public void start_game_message() throws IOException {
        SceneController isc = new InitSceneController();
        setRoot(isc, "/fxml/initScene");
    }

    @Override
    public void leader_card_choice(Message message) throws IOException, InterruptedException {

    }

    @Override
    public void ok_message() throws IOException, InterruptedException {
        SceneController isc = new InitSceneController();
        setRoot(isc, "/fxml/initScene");
    }

    @Override
    public void turn_message(Message message) throws InterruptedException, IOException {

    }

    @Override
    public void end_turn_message(Message message) throws InterruptedException, IOException {

    }

    @Override
    public void endProductionMessage(Message message) {

    }

    @Override
    public void white_conversion_card_message(Message message) throws IOException {

    }

    @Override
    public void vatican_report_message(Message message) {

    }

    @Override
    public void chosen_slot_message(Message message) throws IOException {

    }

    @Override
    public void quit_message(Message message) {

    }

    @Override
    public void end_game_message(Message message) {

    }

    @Override
    public void already_taken_nickName_error() {
        SceneController nsc = new NicknameSceneController();
        setRoot(nsc, "/fxml/nicknameScene");
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
