package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.view.ClientView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUI extends ClientView {


    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GUI.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

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
    public void waiting() {

    }

    @Override
    public void new_player_message(Message message) {

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
