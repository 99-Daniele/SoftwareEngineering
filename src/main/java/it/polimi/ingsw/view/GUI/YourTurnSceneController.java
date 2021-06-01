package it.polimi.ingsw.view.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

public class YourTurnSceneController {
    public GUI gui;
    @FXML
    private Button chooseSlot1;
    @FXML
    private RadioButton first_action;
    @FXML
    private RadioButton radiobutBuyCard;
    @FXML
    private RadioButton row1;
    @FXML
    private RadioButton row2;
    @FXML
    private RadioButton row3;
    @FXML
    private RadioButton column1;
    @FXML
    private RadioButton column2;
    @FXML
    private RadioButton column3;
    @FXML
    private RadioButton column4;
    @FXML
    private RadioButton radiobutTakeMarble;
    @FXML
    private RadioButton radiobutActivProduc;
    @FXML
    private RadioButton radiobutActLeader;
    @FXML
    private RadioButton radiobutDiscardLeader;
    @FXML
    private RadioButton radiobutOtherPlayboard;
    @FXML
    private RadioButton radiobutEndTurn;
    @FXML
    private ImageView card11;
    @FXML
    private ImageView card12;
    @FXML
    private ImageView card13;
    @FXML
    private ImageView card21;
    @FXML
    private ImageView card22;
    @FXML
    private ImageView card23;
    @FXML
    private ImageView card31;
    @FXML
    private ImageView card32;
    @FXML
    private ImageView card33;
    @FXML
    private ImageView card41;
    @FXML
    private ImageView card42;
    @FXML
    private ImageView card43;
    @FXML
    private void switchToSecondary() throws IOException {
        gui.setRoot("/fxml/init");
    }

    @FXML
    public void initialize(){
        radiobutTakeMarble.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> TakeMarbleButton());
        radiobutBuyCard.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> buyCardButton());
    }


    private void TakeMarbleButton(){
        disableAllButton();
        row1.setDisable(false);
        row2.setDisable(false);
        row3.setDisable(false);
        column1.setDisable(false);
        column2.setDisable(false);
        column3.setDisable(false);
        column4.setDisable(false);
    }

    private void buyCardButton(){
        disableAllButton();
        card11.setDisable(false);
        card12.setDisable(false);
        card13.setDisable(false);
        card21.setDisable(false);
        card22.setDisable(false);
        card23.setDisable(false);
        card31.setDisable(false);
        card32.setDisable(false);
        card33.setDisable(false);
        card41.setDisable(false);
        card42.setDisable(false);
        card43.setDisable(false);
    }

    @FXML
    public void ciao(){
        card12.setVisible(false);
    }
    private void disableAllButton(){
        radiobutBuyCard.setDisable(true);
        radiobutActLeader.setDisable(true);
        radiobutActivProduc.setDisable(true);
        radiobutDiscardLeader.setDisable(true);
        radiobutOtherPlayboard.setDisable(true);
        radiobutTakeMarble.setDisable(true);
    }


}
