package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.ClientView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import java.net.URL;
import java.util.Observable;
import java.util.Observer;


public class GUI extends Application implements Observer {

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


    public static void main(String[] args) {
    @Override
    public void launchGUI() {
        launch();
    }

    @Override
    public void launchGUI(String hostName, int port){launch();}

    @Override
    public void askPlayersNumber() {

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
    public void error_message(Message message) throws IOException, InterruptedException {

    }
}
