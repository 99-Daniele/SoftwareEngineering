package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;

import java.io.IOException;

public class PrimaryController {
    public GUI gui;
    @FXML
    private Button ciao;
    @FXML
    private RadioButton first_action;
    @FXML
    private void switchToSecondary() throws IOException {
        gui.setRoot("/fxml/init");
    }

    public void setPrimaryButton(){
        ciao.setText("bello");
        first_action.setText("action");
        first_action.setDisable(true);
    }
}
