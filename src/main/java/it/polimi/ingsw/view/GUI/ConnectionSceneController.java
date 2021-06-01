package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

import java.io.IOException;



public class ConnectionSceneController {
    @FXML
    private Button connect;
    @FXML
    public void initialize(){
        connect.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                method();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    private void method() throws IOException {
        GUI.setRoot("/fxml/nicknameScene");
    }
}
