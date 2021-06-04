package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.Message_Two_Parameter_Int;
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

    private static int card1;
    private static int card2;
    private static int card3;
    private static int card4;
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

    /*
    public InitSceneController(int card1, int card2, int card3, int card4) {
        this.card1 = card1;
        this.card2 = card2;
        this.card3 = card3;
        this.card4 = card4;
    }
    */

    @FXML
    public void initialize(){
        chooseLeader1.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader1, card1));
        chooseLeader2.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader2, card2));
        chooseLeader3.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader3, card3));
        chooseLeader4.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader4, card4));
        start.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> startButton());
    }

    public static void askLeaders(int card1, int card2, int card3, int card4) throws FileNotFoundException {
        SceneController.setImage("#leader1", CardMapGUI.getCard(card1));
        SceneController.setImage("#leader2", CardMapGUI.getCard(card2));
        SceneController.setImage("#leader3", CardMapGUI.getCard(card3));
        SceneController.setImage("#leader4", CardMapGUI.getCard(card4));
        InitSceneController.card1 = card1;
        InitSceneController.card2 = card2;
        InitSceneController.card3 = card3;
        InitSceneController.card4 = card4;
        //SceneController.setVisible("#leader1", false);
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
        Message message = new Message_Two_Parameter_Int(MessageType.LEADER_CARD, GUI.getPosition(), card1, card2);
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
