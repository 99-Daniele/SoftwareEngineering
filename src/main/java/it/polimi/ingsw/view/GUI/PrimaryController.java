package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;

import java.io.IOException;

public class PrimaryController {

    @FXML
    private Button ciao;
    @FXML
    private CheckBox first_action;
    @FXML
    private void switchToSecondary() throws IOException {
        GUI.setRoot("secondary");
    }

    public void setPrimaryButton(){
        ciao.setText("bello");
        first_action.setText("action");
        first_action.setDisable(true);
    }
}
