package it.polimi.ingsw.view.GUI;

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
        connect.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> connectButton());
    }
    private void connectButton(){
        int port=Integer.parseInt(serverPort.getText());
        String address=serverAddress.getText();
        if(port==12460 && address.equals("localhost")) {
            /*
            apro la connessione
             */
            try {
                GUI.setRoot("/fxml/nicknameScene");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        try {
            GUI.setRoot("/fxml/connectionScene");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
