package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.games.states.GAME_STATES;
import it.polimi.ingsw.network.messages.ErrorMessage;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.ClientView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import java.net.URL;


public class GUI extends ClientView {


    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("/fxml/yourTurnScene"), 640, 480);
        stage.setMaximized(true);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void login_message(Message message) {

    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }


    private static Parent loadFXML(String fxml) throws IOException {
        //was:FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        URL url = GUI.class.getResource(fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        return fxmlLoader.load();
    }

    @Override
    public void launchGUI() {
        launch();
    }

    @Override
    public void new_player_message(Message message) {

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
    public void error_message(Message message) {
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

    private void already_taken_nickName_error() {
    }

    private void wrong_parameters_error() {
    }

    private void wrong_turn_error(){
    }

    private void empty_deck_error() {
    }

    private void empty_slot_error() {
    }

    private void wrong_power_error() {
    }

    private void not_enough_cards_error() {
    }

    private void full_slot_error() {
    }

    private void illegal_operation_error() {
    }

    private void impossible_switch_error() {
    }

    private void not_enough_resource_error() {
    }

    private void already_active_error() {
    }

    private void already_discard_error() {
    }
}
