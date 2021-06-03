package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.App;
import it.polimi.ingsw.network.client.ClientSocket;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;


public class ConnectionSceneController {

    @FXML
    private Button connect;
    @FXML
    private TextField serverPort;
    @FXML
    private TextField serverAddress;
    @FXML
    public void initialize(){
        connect.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                connectButton();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void connectButton() throws IOException {
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
            GUI.setRoot("/fxml/nicknameScene");
        } catch (NumberFormatException | IOException e) {
            GUI.setRoot("/fxml/connectionScene");
        }
    }



}
