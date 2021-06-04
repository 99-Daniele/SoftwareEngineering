package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.App;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.Message_Four_Parameter_Int;
import it.polimi.ingsw.view.CLI.CLI_Printer;
import it.polimi.ingsw.view.ClientView;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;



public class GUI extends ClientView {

    private static boolean connected = false;
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

    public static void setRoot(SceneController sceneController, String fxml) {
        SceneController.changeRootPane(sceneController, fxml);
    }

    public static void setRoot(String fxml) {
        SceneController.changeRootPane(fxml);
    }

    @Override
    public void login_message(Message message) {
        position = message.getClientID();
        Platform.runLater(() -> {
            if(position == 0) {
                NicknameSceneController.askNumPlayer();
            }
            else {
                NicknameSceneController.waitPlayers();
            }
        });
    }

    @Override
    public void new_player_message(Message message) {
        //SceneController isc = new InitSceneController();
        //setRoot(isc, "/fxml/initScene");
    }

    @Override
    public void start_game_message() throws IOException {
        super.startGame();
    }

    @Override
    public void leader_card_choice(Message message){
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        Platform.runLater(() -> {
            setRoot("/fxml/initScene");
            try {
                InitSceneController.askLeaders(m.getPar1(), m.getPar2(), m.getPar3(), m.getPar4());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void ok_message(){
        Platform.runLater(() -> {
            if(!super.getGame().isStartGame())
                NicknameSceneController.waitPlayers();
        });
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
