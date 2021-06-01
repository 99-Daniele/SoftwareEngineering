package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXML;

import java.io.IOException;

public class InitSceneController {
    public GUI gui;

    @FXML
    private void switchToPrimary() throws IOException {
        gui.setRoot("/fxml/yourTurn");
    }
    @FXML
    public void initialize(){

    }
}
