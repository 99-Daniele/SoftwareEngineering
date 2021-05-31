package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXML;

import java.io.IOException;

public class SecondaryController {
    public GUI gui;
    @FXML
    private void switchToPrimary() throws IOException {
        gui.setRoot("/fxml/yourTurn");
    }
}
