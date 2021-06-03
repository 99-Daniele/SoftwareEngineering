package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.App;
import it.polimi.ingsw.model.games.states.GAME_STATES;
import it.polimi.ingsw.network.messages.ErrorMessage;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.ClientView;
import javafx.application.Platform;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import java.net.URL;


public class GUI extends ClientView {


    private static Scene scene;
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
        if(connected)
            scene = new Scene(loadFXML("/fxml/nicknameScene"), 640, 480);
        else
            scene = new Scene(loadFXML("/fxml/connectionScene"), 640, 480);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void login_message(Message message) {
        position = message.getClientID();
        try {
            setRoot("/fxml/nicknameScene");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }


    private static Parent loadFXML(String fxml) throws IOException {
        //was:FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        URL url = GUI.class.getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        return fxmlLoader.load();
    }

    @Override
    public void new_player_message(Message message) {
        try {
            setRoot("/fxml/initScene");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start_game_message() throws IOException {

    }

    @Override
    public void leader_card_choice(Message message) throws IOException, InterruptedException {

    }

    @Override
    public void ok_message() throws IOException, InterruptedException {

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
