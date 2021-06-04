package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.parser.CardMapGUI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class InitSceneController extends SceneController{

    public GUI gui;
    @FXML
    private ImageView leader1;
    @FXML
    private ImageView leader2;
    @FXML
    private ImageView leader3;
    @FXML
    private ImageView leader4;
    @FXML
    private ImageView coin;
    @FXML
    private ImageView servant;
    @FXML
    private ImageView stone;
    @FXML
    private ImageView shield;
    @FXML
    private Button chooseLeader1;
    @FXML
    private Button chooseLeader2;
    @FXML
    private Button chooseLeader3;
    @FXML
    private Button chooseLeader4;
    @FXML
    private Button resource1;
    @FXML
    private Button resource2;
    @FXML
    private Button resource3;
    @FXML
    private Button resource4;
    @FXML
    private Button start;
    @FXML
    public void initialize() throws FileNotFoundException {
        //FileInputStream fis = new FileInputStream(CardMapGUI.getCard(50));
        //leader1 = new ImageView(new Image(fis));
        start.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> startButton());
    }
    private void startButton(){
        SceneController ytsc = new YourTurnSceneController();
        GUI.setRoot(ytsc, "/fxml/yourTurnScene");
    }
}
