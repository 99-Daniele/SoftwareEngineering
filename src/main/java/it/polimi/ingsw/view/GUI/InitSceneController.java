package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.market.WhiteMarble;
import it.polimi.ingsw.parser.CardMapGUI;
import it.polimi.ingsw.parser.MarbleMapGUI;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class InitSceneController extends SceneController{

    private int card1;
    private int card2;
    private int card3;
    private int card4;
    private int chosenCard1;
    private int chosenCard2;

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

    public InitSceneController(int card1, int card2, int card3, int card4) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
        this.card4 = card4;
    }

    @FXML
    public void initialize() throws FileNotFoundException {
        FileInputStream fis = new FileInputStream(CardMapGUI.getCard(card1));
        leader1.setImage(new Image(fis));
        fis = new FileInputStream(CardMapGUI.getCard(card2));
        leader2.setImage(new Image(fis));
        fis = new FileInputStream(CardMapGUI.getCard(card3));
        leader3.setImage(new Image(fis));
        fis = new FileInputStream(CardMapGUI.getCard(card4));
        leader4.setImage(new Image(fis));
        chooseLeader1.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader1, card1));
        chooseLeader2.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader2, card2));
        chooseLeader3.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader3, card3));
        chooseLeader4.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader4, card4));
        start.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> startButton());
    }

    private void leaderButton(Button leaderButton, int card){
        leaderButton.setDisable(true);
        if(chosenCard1 == 0)
            chosenCard1 = card;
        else if(chosenCard2 == 0) {
            chosenCard2 = card;
            start.setDisable(false);
        }
        else {
            if(chosenCard1 == card1)
                chooseLeader1.setDisable(false);
            if(chosenCard1 == card2)
                chooseLeader2.setDisable(false);
            if(chosenCard1 == card3)
                chooseLeader3.setDisable(false);
            if(chosenCard1 == card4)
                chooseLeader4.setDisable(false);
            chosenCard1 = card;
        }
    }

    private void startButton(){
        System.out.println(chosenCard2 + " - " + chosenCard2);
    }
}
