package it.polimi.ingsw.view.GUI.sceneController;

import it.polimi.ingsw.App;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.GUI.SceneController;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * ConnectionSceneController handle the connection scene of GUI.
 */
public class ConnectionSceneController{

    @FXML
    private Button connect;
    @FXML
    private TextField serverPort;
    @FXML
    private TextField serverAddress;
    @FXML
    private Label errorLabel;
    @FXML
    private Button ok;

    @FXML
    public void initialize(){
        connect.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> connectButton());
    }

    /**
     * if player clicks on connect button tries to connect by player's chosen hostname and port address.
     */
    private void connectButton() {
        try {
            String address = serverAddress.getText();
            int portNumber;
            if(address.isBlank())
                address = "127.0.0.1";
            String port = serverPort.getText();
            if(port.isBlank())
                portNumber = 12460;
            else
                portNumber = Integer.parseInt(port);
            App.startClient(address, portNumber);
            Platform.runLater(() -> SceneController.changeRootPane("/fxml/nicknameScene"));
        } catch (NumberFormatException | IOException e) {
            errorConnection();
        }
    }

    /**
     * display to player and error message due to failed connection.
     */
    private void errorConnection(){
        serverPort.clear();
        serverAddress.clear();
        errorLabel.setVisible(true);
        ok.setVisible(true);
        ok.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            errorLabel.setVisible(false);
            ok.setVisible(false);
        });
    }

}
