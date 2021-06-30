package it.polimi.ingsw.view.GUI.sceneController;

import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.parser.CardMapGUI;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.GUI.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * InitSceneController handle the chosen LeaderCards and resources scene of GUI.
 */
public class InitSceneController{

    private static int card1;
    private static int card2;
    private static int card3;
    private static int card4;
    private int chosenCard1;
    private int chosenCard2;
    private Resource r1;

    @FXML
    private Label leaderLabel;
    @FXML
    private Label resourceLabel;
    @FXML
    private ImageView leader1;
    @FXML
    private ImageView leader2;
    @FXML
    private ImageView leader3;
    @FXML
    private ImageView leader4;
    @FXML
    private ImageView stone;
    @FXML
    private ImageView coin;
    @FXML
    private ImageView servant;
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
    public void initialize(){
        chooseLeader1.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader1, card1));
        chooseLeader2.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader2, card2));
        chooseLeader3.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader3, card3));
        chooseLeader4.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> leaderButton(chooseLeader4, card4));
        resource1.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> resourceButton(Resource.STONE));
        resource2.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> resourceButton(Resource.COIN));
        resource3.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> resourceButton(Resource.SERVANT));
        resource4.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> resourceButton(Resource.SHIELD));
        start.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> startButton());
    }

    /**
     * @param card1 is cardID of one LeaderCard.
     * @param card2 is cardID of one LeaderCard.
     * @param card3 is cardID of one LeaderCard.
     * @param card4 is cardID of one LeaderCard.
     *
     * set LeaderCards ImageView to received from Server LeaderCards.
     */
    public static void askLeaders(int card1, int card2, int card3, int card4) {
        SceneController.setImage("#leader1", CardMapGUI.getCard(card1));
        SceneController.setImage("#leader2", CardMapGUI.getCard(card2));
        SceneController.setImage("#leader3", CardMapGUI.getCard(card3));
        SceneController.setImage("#leader4", CardMapGUI.getCard(card4));
        InitSceneController.card1 = card1;
        InitSceneController.card2 = card2;
        InitSceneController.card3 = card3;
        InitSceneController.card4 = card4;
    }

    /**
     * @param leaderButton is one of 4 button refers to one chosen LeaderCard
     * @param card is the referred card to @param leaderButton.
     *
     * set chosenCard1 or chosenCard2 as @param card. if player has chosen 2 LeaderCards before, remove the first chosen
     * LeaderCard from the 2 chosen LeaderCards and add the new chosen LeaderCard.
     */
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

    /**
     * @param r is one chosen resource.
     *
     * send to Server a new first chosen resource message whit r as param. But if it's the fourth player (Gui.getPosition = 3)
     * send only when player chooses 2 resources.
     */
    private void resourceButton(Resource r){
        if(GUI.getPosition() == 3){
            if(r1 == null)
                r1 = r;
            else {
                allInvisible();
                resourceLabel.setVisible(true);
                resourceLabel.setText("WAITING OTHER PLAYERS CHOICES");
                Message m = new MessageTwoResource(MessageType.TWO_FIRST_RESOURCE, GUI.getPosition(), r1, r);
                try {
                    ClientSocket.sendMessage(m);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            allInvisible();
            resourceLabel.setVisible(true);
            resourceLabel.setText("WAITING OTHER PLAYERS CHOICES");
            Message m = new MessageOneIntOneResource(MessageType.ONE_FIRST_RESOURCE, GUI.getPosition(), r, 1);
            try {
                ClientSocket.sendMessage(m);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * send to Server a new leader choices message and in case it's not the first player (GUI.getPosition = 0), make
     * resources visible.
     */
    private void startButton() {
        ClientView.setLeaderCard(GUI.getPosition(), chosenCard1, chosenCard2);
        Message message = new MessageTwoParameterInt(MessageType.LEADER_CARD, GUI.getPosition(), chosenCard1, chosenCard2);
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        allInvisible();
        if(GUI.getPosition() != 0) {
            coin.setVisible(true);
            servant.setVisible(true);
            stone.setVisible(true);
            shield.setVisible(true);
            resource1.setVisible(true);
            resource2.setVisible(true);
            resource3.setVisible(true);
            resource4.setVisible(true);
        }
        else {
            resourceLabel.setText("WAITING OTHER PLAYERS CHOICES");
        }
        resourceLabel.setVisible(true);
    }

    /**
     * make all nodes invisible.
     */
    private void allInvisible(){
        resourceLabel.setVisible(false);
        leader1.setVisible(false);
        leader2.setVisible(false);
        leader3.setVisible(false);
        leader4.setVisible(false);
        chooseLeader1.setVisible(false);
        chooseLeader2.setVisible(false);
        chooseLeader3.setVisible(false);
        chooseLeader4.setVisible(false);
        leaderLabel.setVisible(false);
        start.setVisible(false);
        coin.setVisible(false);
        resource1.setVisible(false);
        shield.setVisible(false);
        resource2.setVisible(false);
        stone.setVisible(false);
        resource3.setVisible(false);
        servant.setVisible(false);
        resource4.setVisible(false);
    }
}
