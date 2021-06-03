package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class OpponentPlayerboardSceneController extends SceneController{
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
        SceneController ytsc = new YourTurnSceneController();
        GUI.setRoot(ytsc, "/fxml/yourTurnScene");
    }

    private void showBoard(){
    }

}
