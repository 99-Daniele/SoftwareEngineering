package it.polimi.ingsw.view.GUI.sceneController;

import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.SceneController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class OpponentPlayerboardSceneController {
    @FXML
    private ImageView deposit31;
    @FXML
    private Button goBack;
    @FXML
    public void initialize() {
        showBoard();
        goBack.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> goBackButton());
    }

    private void goBackButton(){
        GUI.setRoot("/fxml/yourTurnScene");
    }

    private void showBoard(){
    }

}
