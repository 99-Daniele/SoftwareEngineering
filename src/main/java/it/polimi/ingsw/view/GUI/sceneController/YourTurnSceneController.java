package it.polimi.ingsw.view.GUI.sceneController;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.games.states.GAME_STATES;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.parser.CardMapCLI;
import it.polimi.ingsw.parser.CardMapGUI;
import it.polimi.ingsw.parser.MarbleMapGUI;
import it.polimi.ingsw.parser.ResourceMapGUI;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.SceneController;
import it.polimi.ingsw.view.GUI.sceneController.OpponentPlayerboardSceneController;
import it.polimi.ingsw.view.model_view.Game_View;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.URISyntaxException;


public class YourTurnSceneController {

    @FXML
    private Button chooseSlot1;
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
    private ImageView leader1;
    @FXML
    private ImageView leader2;
    @FXML
    private ImageView marble11;
    @FXML
    private ImageView marble12;
    @FXML
    private ImageView marble13;
    @FXML
    private ImageView marble14;
    @FXML
    private ImageView marble21;
    @FXML
    private ImageView marble22;
    @FXML
    private ImageView marble23;
    @FXML
    private ImageView marble24;
    @FXML
    private ImageView marble31;
    @FXML
    private ImageView marble32;
    @FXML
    private ImageView marble33;
    @FXML
    private ImageView marble34;
    @FXML
    private ImageView marbleExt;
    @FXML
    private ImageView deposit11;
    @FXML
    private ImageView deposit21;
    @FXML
    private ImageView deposit22;
    @FXML
    private ImageView deposit31;
    @FXML
    private ImageView deposit32;
    @FXML
    private ImageView deposit33;
    @FXML
    private ImageView extra11;
    @FXML
    private ImageView extra12;
    @FXML
    private ImageView extra21;
    @FXML
    private ImageView extra22;
    @FXML
    private ImageView marbleShow1;
    @FXML
    private ImageView marbleShow2;
    @FXML
    private ImageView marbleShow3;
    @FXML
    private ImageView marbleShow4;
    @FXML
    private Button chooseLeader1;
    @FXML
    private Button chooseLeader2;
    @FXML
    private Button chooseSlot2;
    @FXML
    private Button chooseSlot3;
    @FXML
    private Button chooseBase;
    @FXML
    private Label coinAmount;
    @FXML
    private Label stoneAmount;
    @FXML
    private Label servantAmount;
    @FXML
    private Label shieldAmount;
    @FXML
    private Label croceRossa;
    @FXML
    private ImageView croceNera;
    @FXML
    private Button move;
    @FXML
    private Label message;
    @FXML
    private Button yes;
    @FXML
    private Button no;
    @FXML
    private Button ok;
    @FXML
    private RadioButton radiobutEndProd;
    @FXML
    private RadioButton switch1;
    @FXML
    private RadioButton switch2;
    @FXML
    private RadioButton switch3;
    @FXML
    private Pane panelBuy;
    @FXML
    private RadioButton radiobutWarehouse;
    @FXML
    private RadioButton radiobutStrongbox;
    @FXML
    private ImageView slot11;
    @FXML
    private ImageView slot12;
    @FXML
    private ImageView slot13;
    @FXML
    private ImageView slot21;
    @FXML
    private ImageView slot22;
    @FXML
    private ImageView slot23;
    @FXML
    private ImageView slot31;
    @FXML
    private ImageView slot32;
    @FXML
    private ImageView slot33;
    @FXML
    private Pane basePanel;
    @FXML
    private ImageView servant1;
    @FXML
    private ImageView stone1;
    @FXML
    private ImageView coin1;
    @FXML
    private ImageView shield1;
    @FXML
    private Label baseLabel;
    @FXML
    private ImageView pope1;
    @FXML
    private ImageView pope2;
    @FXML
    private ImageView pope3;
    @FXML
    private Pane otherPlayers;
    @FXML
    private RadioButton player13;
    @FXML
    private RadioButton player23;
    @FXML
    private RadioButton player33;
    @FXML
    private RadioButton player12;
    @FXML
    private RadioButton player22;
    @FXML
    private VBox display;
    @FXML
    private Pane errorPane;
    @FXML
    private Button okError;

    private int marbleCount;

    @FXML
    public void initialize() {
        showPlayerboard();
        radiobutTakeMarble.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> takeMarbleButton());
        radiobutBuyCard.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> buyCardButton());
        radiobutActivProduc.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> activProducButton());
        if(leaderCardAvailable()) {
            radiobutActLeader.setOnMouseClicked(mouseEvent -> choseLeaderButton());
            radiobutDiscardLeader.setOnMouseClicked(mouseEvent -> choseLeaderButton());
        }
        radiobutOtherPlayboard.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> otherPlayerboardButton());
        radiobutEndProd.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> endProductionButton());
        radiobutEndTurn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> endTurnButton());
        chooseBase.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            chooseBase.setDisable(true);
            basicProduction();
        });
        row1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            row1.setSelected(false);
            take_market_marble_row(1);
        });
        row2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            row2.setSelected(false);
            take_market_marble_row(2);
        });
        row3.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            row3.setSelected(false);
            take_market_marble_row(3);
        });
        column1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            column1.setSelected(false);
            take_market_marble_column(1);
        });
        column2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            column2.setSelected(false);
            take_market_marble_column(2);
        });
        column3.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            column3.setSelected(false);
            take_market_marble_column(3);
        });
        column4.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            column4.setSelected(false);
            take_market_marble_column(4);
        });
        chooseSlot1.setOnMouseClicked(mouseEvent -> choseSlot(1));
        chooseSlot2.setOnMouseClicked(mouseEvent -> choseSlot(2));
        chooseSlot3.setOnMouseClicked(mouseEvent -> choseSlot(3));
        chooseLeader1.setOnMouseClicked(mouseEvent -> choseLeader(1));
        chooseLeader2.setOnMouseClicked(mouseEvent -> choseLeader(2));
        okError.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> errorPane.setVisible(false));
    }

    private static boolean leaderCardAvailable(){
        return ((!ClientView.isLeaderCardActive(GUI.getPosition(), 1) && ClientView.getLeaderCards(GUI.getPosition()).get(0) != -1)
                    || (!ClientView.isLeaderCardActive(GUI.getPosition(), 2) && ClientView.getLeaderCards(GUI.getPosition()).get(1) != -1));
    }

    private void showPlayerboard() {
        try {
            setCard(card11, ClientView.getDevelopmentCards().get(0));
            setCard(card21, ClientView.getDevelopmentCards().get(1));
            setCard(card31, ClientView.getDevelopmentCards().get(2));
            setCard(card41, ClientView.getDevelopmentCards().get(3));
            setCard(card12, ClientView.getDevelopmentCards().get(4));
            setCard(card22, ClientView.getDevelopmentCards().get(5));
            setCard(card32, ClientView.getDevelopmentCards().get(6));
            setCard(card42, ClientView.getDevelopmentCards().get(7));
            setCard(card13, ClientView.getDevelopmentCards().get(8));
            setCard(card23, ClientView.getDevelopmentCards().get(9));
            setCard(card33, ClientView.getDevelopmentCards().get(10));
            setCard(card43, ClientView.getDevelopmentCards().get(11));
            setCard(leader1, ClientView.getLeaderCards(GUI.getPosition()).get(0));
            if(!ClientView.isLeaderCardActive(GUI.getPosition(), 1))
                leader1.setOpacity(0.5);
            setCard(leader2, ClientView.getLeaderCards(GUI.getPosition()).get(1));
            if(!ClientView.isLeaderCardActive(GUI.getPosition(), 2))
                leader2.setOpacity(0.5);
            setCard(slot11, ClientView.getSlotCards(GUI.getPosition()).get(0));
            setCard(slot12, ClientView.getSlotCards(GUI.getPosition()).get(1));
            setCard(slot13, ClientView.getSlotCards(GUI.getPosition()).get(2));
            setCard(slot21, ClientView.getSlotCards(GUI.getPosition()).get(3));
            setCard(slot22, ClientView.getSlotCards(GUI.getPosition()).get(4));
            setCard(slot23, ClientView.getSlotCards(GUI.getPosition()).get(5));
            setCard(slot31, ClientView.getSlotCards(GUI.getPosition()).get(6));
            setCard(slot32, ClientView.getSlotCards(GUI.getPosition()).get(7));
            setCard(slot33, ClientView.getSlotCards(GUI.getPosition()).get(8));
            setImage(marble11, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 0)));
            setImage(marble12, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 1)));
            setImage(marble13, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 2)));
            setImage(marble14, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 3)));
            setImage(marble21, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 0)));
            setImage(marble22, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 1)));
            setImage(marble23, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 2)));
            setImage(marble24, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 3)));
            setImage(marble31, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 0)));
            setImage(marble32, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 1)));
            setImage(marble33, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 2)));
            setImage(marble34, MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 3)));
            setImage(marbleExt, MarbleMapGUI.getMarble(ClientView.getMarket().getExternalMarble()));
            setFirstDepot();
            setSecondDepot();
            setThirdDepot();
            setExtraDepot1();
            setExtraDepot2();
            setStrongbox();
            setFaithPoints();
            setVictoryPoints();
            if(GUI.getNumOfPlayers() == 1)
                radiobutOtherPlayboard.setVisible(false);
            setMessages();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setImage(ImageView image, String file){
        if(file.equals(""))
            image.setImage(null);
        else {
            InputStream fis = SceneController.class.getResourceAsStream(file);
            image.setImage(new Image(fis));
        }
    }

    private void setCard(ImageView image, int cardID) throws FileNotFoundException{
        if (cardID != -1)
            setImage(image, CardMapGUI.getCard(cardID));
        else
            setImage(image, "");
    }

    private void setFirstDepot() throws FileNotFoundException{
        if (ClientView.getWarehouse(GUI.getPosition()).get(0).getAmount() == 1)
            setImage(deposit11, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(0).getResource()));
    }

    private void setSecondDepot() throws FileNotFoundException{
        if (ClientView.getWarehouse(GUI.getPosition()).get(1).getAmount() >= 1)
            setImage(deposit21, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(1).getResource()));
        if (ClientView.getWarehouse(GUI.getPosition()).get(1).getAmount() == 2)
            setImage(deposit22, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(1).getResource()));
    }

    private void setThirdDepot() throws FileNotFoundException{
        if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() >= 1)
            setImage(deposit31, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
        if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() >= 2)
            setImage(deposit32, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
        if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() == 3)
            setImage(deposit33, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
    }

    private void setExtraDepot1() throws FileNotFoundException{
        if (ClientView.getWarehouse(GUI.getPosition()).size() >= 4) {
            if (ClientView.getWarehouse(GUI.getPosition()).get(3).getAmount() >= 1)
                setImage(extra11, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(3).getResource()));
            if (ClientView.getWarehouse(GUI.getPosition()).get(3).getAmount() == 2)
                setImage(extra12, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(3).getResource()));
        }
    }

    private void setExtraDepot2() throws FileNotFoundException{
        if (ClientView.getWarehouse(GUI.getPosition()).size() == 5) {
            if (ClientView.getWarehouse(GUI.getPosition()).get(4).getAmount() >= 1)
                setImage(extra21, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(4).getResource()));
            if (ClientView.getWarehouse(GUI.getPosition()).get(4).getAmount() == 2)
                setImage(extra22, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(4).getResource()));
        }
    }

    private void setStrongbox() {
        coinAmount.setText(String.valueOf(ClientView.coinAmount(GUI.getPosition())));
        servantAmount.setText(String.valueOf(ClientView.servantAmount(GUI.getPosition())));
        shieldAmount.setText(String.valueOf(ClientView.shieldAmount(GUI.getPosition())));
        stoneAmount.setText(String.valueOf(ClientView.stoneAmount(GUI.getPosition())));
    }

    private void setFaithPoints() {
        int faithPoints = ClientView.getFaithPoints(GUI.getPosition());
        if(faithPoints < 3){
            croceRossa.setLayoutX(30*faithPoints + 7);
        }
        else if (faithPoints >= 3 && faithPoints < 5) {
            croceRossa.setLayoutX(67);
            croceRossa.setLayoutY(168 - faithPoints*28);
        }
        else if(faithPoints >= 5 && faithPoints < 10){
            croceRossa.setLayoutX(30*faithPoints - 52);
            croceRossa.setLayoutY(56);
        }
        else if (faithPoints >= 10 && faithPoints < 12) {
            croceRossa.setLayoutX(218);
            croceRossa.setLayoutY(faithPoints*28 - 200);
        }
        else if(faithPoints >= 12 && faithPoints < 17){
            croceRossa.setLayoutX(30*faithPoints - 112);
            croceRossa.setLayoutY(107);
        }
        else if(faithPoints >= 17 && faithPoints < 19){
            croceRossa.setLayoutX(368);
            croceRossa.setLayoutY(555 - 28*faithPoints);
        }
        else {
            if(faithPoints > 24)
                faithPoints = 24;
            croceRossa.setLayoutX(30*faithPoints - 172);
            croceRossa.setLayoutY(56);
        }
        if(GUI.getNumOfPlayers() == 1){
            faithPoints = ClientView.getFaithPoints(1);
            croceNera.setVisible(true);
            if(faithPoints < 3){
                croceNera.setLayoutX(30*faithPoints + 7);
            }
            else if (faithPoints >= 3 && faithPoints < 5) {
                croceNera.setLayoutX(67);
                croceNera.setLayoutY(168 - faithPoints*28);
            }
            else if(faithPoints >= 5 && faithPoints < 10){
                croceNera.setLayoutX(30*faithPoints - 52);
                croceNera.setLayoutY(56);
            }
            else if (faithPoints >= 10 && faithPoints < 12) {
                croceNera.setLayoutX(218);
                croceNera.setLayoutY(faithPoints*28 - 200);
            }
            else if(faithPoints >= 12 && faithPoints < 17){
                croceNera.setLayoutX(30*faithPoints - 112);
                croceNera.setLayoutY(107);
            }
            else if(faithPoints >= 17 && faithPoints < 19){
                croceNera.setLayoutX(368);
                croceNera.setLayoutY(555 - 28*faithPoints);
            }
            else {
                if(faithPoints > 24)
                    faithPoints = 24;
                croceNera.setLayoutX(30*faithPoints - 172);
                croceNera.setLayoutY(56);
            }
        }
    }

    private void setVictoryPoints() {
        int victoryPoints = ClientView.getVictoryPoints(GUI.getPosition());
        switch (ClientView.getCurrentPope()) {
            case 1:
                if (victoryPoints == 0)
                    setImage(pope1, "/photos/quadrato giallo.png");
                else
                    setImage(pope1, "/photos/pope_favor1_front.png");
                break;
            case 2:
                if (victoryPoints == 0 || victoryPoints == 2)
                    setImage(pope2, "/photos/quadrato arancione.png");
                else
                    setImage(pope2, "/photos/pope_favor2_front.png");
                break;
            case 3:
                if (victoryPoints == 0 || victoryPoints == 2 || victoryPoints == 3 || victoryPoints == 5)
                    setImage(pope3, "/photos/quadrato rosso.png");
                else
                    setImage(pope3, "/photos/pope_favor3_front.png");
        }
    }

    private void setMessages(){
        for (String message: GUI.getServerMessages())
            display.getChildren().add(new Label(message));
    }

    private void chooseSwitch(Message switchmessage) {
        Message_ArrayList_Marble m = (Message_ArrayList_Marble) switchmessage;
        //ClientView.take_marble_message(message);
        System.out.println("You have chosen this marbles: ");
        message.setVisible(true);
        message.setText("Wanna switch your deposit?");
        yes.setVisible(true);
        no.setVisible(true);
        //show marbles
        yes.setOnMouseClicked(mouseEvent -> yesSwitch());
        no.setOnMouseClicked(mouseEvent -> noSwitch());
    }

    private void noSwitch() {
        marbleCount--;
        if (marbleCount > 0)
            showReducedMarble();

    }

    private void showReducedMarble() {

    }

    private void yesSwitch() {
        switch1.setDisable(false);
        switch2.setDisable(false);
        switch3.setDisable(false);
        switch1.setOnMouseClicked(mouseEvent -> {
            switch1.setDisable(true);
            switch1.setSelected(false);
            clickedSwitch();
        });
        switch2.setOnMouseClicked(mouseEvent -> {
            switch2.setDisable(true);
            switch2.setSelected(false);
            clickedSwitch();
        });
        switch3.setOnMouseClicked(mouseEvent -> {
            switch3.setDisable(true);
            switch3.setSelected(false);
            clickedSwitch();
        });
    }

    private void clickedSwitch() {
        int par1 = 0;
        int par2 = 0;
        if (switch1.isDisable() && switch2.isDisable()) {
            par1 = 1;
            par2 = 2;
        }
        if (switch1.isDisable() && switch3.isDisable()) {
            par1 = 1;
            par2 = 3;
        }
        if (switch2.isDisable() && switch3.isDisable()) {
            par1 = 2;
            par2 = 3;
        }
        if (par1 != 0) {
            try {
                ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, GUI.getPosition(), par1, par2));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void take_market_marble_row(int i) {
        marbleCount = 4;
        try {
            ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, GUI.getPosition(), 0, i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void take_market_marble_column(int i) {
        marbleCount = 3;
        try {
            ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, GUI.getPosition(), 1, i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void otherPlayerboardButton() {
        radiobutOtherPlayboard.setSelected(false);
        if(GUI.getNumOfPlayers() == 2){
            twoPlayersOtherPlayerboard();
        }
        else if(GUI.getNumOfPlayers() == 3){
            threePlayersOtherPlayerboard();
        }
        else
            fourPlayersOtherPlayerboard();
    }

    private void twoPlayersOtherPlayerboard(){
        final OpponentPlayerboardSceneController[] opsc = new OpponentPlayerboardSceneController[1];
        if(GUI.getPosition() == 0)
            opsc[0] = new OpponentPlayerboardSceneController(1);
        else
            opsc[0] = new OpponentPlayerboardSceneController(0);
        Platform.runLater(() ->
                SceneController.changeRootPane(opsc[0], "/fxml/opponentPlayerboardScene"));
    }

    private void threePlayersOtherPlayerboard(){
        final OpponentPlayerboardSceneController[] opsc = new OpponentPlayerboardSceneController[1];
        disableAllButton();
        otherPlayers.setVisible(true);
        player12.setVisible(true);
        player22.setVisible(true);
        final int player1;
        final int player2;
        switch (GUI.getPosition()){
            case 0:
                player1 = 1;
                player2 = 2;
                break;
            case 1:
                player1 = 0;
                player2 = 2;
                break;
            case 2:
                player1 = 0;
                player2 = 1;
                break;
            default:
                player1 = 0;
                player2 = 0;
                break;
        }
        player12.setText(GUI.getNickname(player1));
        player22.setText(GUI.getNickname(player2));
        player12.setOnMouseClicked(mouseEvent -> {
            opsc[0] = new OpponentPlayerboardSceneController(player1);
            Platform.runLater(() ->
                    SceneController.changeRootPane(opsc[0], "/fxml/opponentPlayerboardScene"));
        });
        player22.setOnMouseClicked(mouseEvent -> {
            opsc[0] = new OpponentPlayerboardSceneController(player2);
            Platform.runLater(() ->
                    SceneController.changeRootPane(opsc[0], "/fxml/opponentPlayerboardScene"));
        });
    }

    private void fourPlayersOtherPlayerboard(){
        final OpponentPlayerboardSceneController[] opsc = new OpponentPlayerboardSceneController[1];
        disableAllButton();
        otherPlayers.setVisible(true);
        player13.setVisible(true);
        player23.setVisible(true);
        player33.setVisible(true);
        final int player1;
        final int player2;
        final int player3;
        switch (GUI.getPosition()){
            case 0:
                player1 = 1;
                player2 = 2;
                player3 = 3;
                break;
            case 1:
                player1 = 0;
                player2 = 2;
                player3 = 3;
                break;
            case 2:
                player1 = 0;
                player2 = 1;
                player3 = 3;
                break;
            case 3:
                player1 = 0;
                player2 = 1;
                player3 = 2;
                break;
            default:
                player1 = 0;
                player2 = 0;
                player3 = 0;
                break;
        }
        player13.setText(GUI.getNickname(player1));
        player23.setText(GUI.getNickname(player2));
        player33.setText(GUI.getNickname(player3));
        player13.setOnMouseClicked(mouseEvent -> {
            opsc[0] = new OpponentPlayerboardSceneController(player1);
            Platform.runLater(() ->
                    SceneController.changeRootPane(opsc[0], "/fxml/opponentPlayerboardScene"));
        });
        player23.setOnMouseClicked(mouseEvent -> {
            opsc[0] = new OpponentPlayerboardSceneController(player2);
            Platform.runLater(() ->
                    SceneController.changeRootPane(opsc[0], "/fxml/opponentPlayerboardScene"));
        });
        player33.setOnMouseClicked(mouseEvent -> {
            opsc[0] = new OpponentPlayerboardSceneController(player3);
            Platform.runLater(() ->
                    SceneController.changeRootPane(opsc[0], "/fxml/opponentPlayerboardScene"));
        });
    }

    private void choseLeaderButton(){
        disableAllButton();
        if(!ClientView.isLeaderCardActive(GUI.getPosition(), 1) && ClientView.getLeaderCards(GUI.getPosition()).get(0) != -1)
            chooseLeader1.setVisible(true);
        if(!ClientView.isLeaderCardActive(GUI.getPosition(), 2) && ClientView.getLeaderCards(GUI.getPosition()).get(1) != -1)
            chooseLeader2.setVisible(true);
    }

    private void activProducButton() {
        radiobutActivProduc.setSelected(false);
        if(ClientView.isState(GAME_STATES.FIRST_ACTION_STATE))
            ClientView.setCurrentState(GAME_STATES.FIRST_POWER_STATE);
        disableAllButton();
        enableProductionsButton();
    }

    private void basicProduction(){
        basePanel.setVisible(true);
        Message_Three_Resource_One_Int messageToSend = new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, GUI.getPosition());
        coin1.setOnMouseClicked(MouseEvent -> {
            choseResource(Resource.COIN, messageToSend);
        });
        servant1.setOnMouseClicked(MouseEvent -> {
            choseResource(Resource.SERVANT, messageToSend);
        });
        shield1.setOnMouseClicked(MouseEvent -> {
            choseResource(Resource.SHIELD, messageToSend);
        });
        stone1.setOnMouseClicked(MouseEvent -> {
            choseResource(Resource.STONE, messageToSend);
        });
    }

    private void cardProduction(int slot) {
        panelBuy.setVisible(true);
        radiobutWarehouse.setOnMouseClicked(mouseEvent1 -> {
            radiobutWarehouse.setSelected(false);
            panelBuy.setVisible(false);
            Message message = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, GUI.getPosition(), slot, 0);
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        radiobutStrongbox.setOnMouseClicked(mouseEvent1 -> {
            radiobutStrongbox.setSelected(false);
            panelBuy.setVisible(false);
            Message message = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, GUI.getPosition(), slot, 1);
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void leaderProduction(int leader) {
        basePanel.setVisible(true);
        Message_One_Resource_Two_Int messageToSend = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, GUI.getPosition(), leader);
        coin1.setOnMouseClicked(MouseEvent -> {
            choseResource(Resource.COIN, messageToSend);
        });
        servant1.setOnMouseClicked(MouseEvent -> {
            choseResource(Resource.SERVANT, messageToSend);
        });
        shield1.setOnMouseClicked(MouseEvent -> {
            choseResource(Resource.SHIELD, messageToSend);
        });
        stone1.setOnMouseClicked(MouseEvent -> {
            choseResource(Resource.STONE, messageToSend);
        });
    }

    private void choseResource(Resource resource, Message_Three_Resource_One_Int messageToSend) {
        if (messageToSend.getResource1() == null)
            messageToSend.setResource1(resource);
        else if (messageToSend.getResource2() == null) {
            messageToSend.setResource2(resource);
            baseLabel.setText("Choose produced resource");
        }
        else {
            basePanel.setVisible(false);
            baseLabel.setText("Choose resources required");
            messageToSend.setResource3(resource);
            panelBuy.setVisible(true);
            radiobutWarehouse.setOnMouseClicked(mouseEvent1 -> {
                radiobutWarehouse.setSelected(false);
                panelBuy.setVisible(false);
                messageToSend.setPar(0);
                try {
                    ClientSocket.sendMessage(messageToSend);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            radiobutStrongbox.setOnMouseClicked(mouseEvent1 -> {
                radiobutStrongbox.setSelected(false);
                panelBuy.setVisible(false);
                messageToSend.setPar(1);
                try {
                    ClientSocket.sendMessage(messageToSend);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private void choseResource(Resource resource, Message_One_Resource_Two_Int messageToSend) {
        messageToSend.setResource(resource);
        basePanel.setVisible(false);
        panelBuy.setVisible(true);
        radiobutWarehouse.setOnMouseClicked(mouseEvent1 -> {
            radiobutWarehouse.setSelected(false);
            panelBuy.setVisible(false);
            messageToSend.setPar2(0);
            try {
                ClientSocket.sendMessage(messageToSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        radiobutStrongbox.setOnMouseClicked(mouseEvent1 -> {
            radiobutStrongbox.setSelected(false);
            panelBuy.setVisible(false);
            messageToSend.setPar2(1);
            try {
                ClientSocket.sendMessage(messageToSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void takeMarbleButton() {
        disableAllButton();
        row1.setDisable(false);
        row2.setDisable(false);
        row3.setDisable(false);
        column1.setDisable(false);
        column2.setDisable(false);
        column3.setDisable(false);
        column4.setDisable(false);
    }

    private void endProductionButton(){
        radiobutEndProd.setDisable(true);
        radiobutEndProd.setSelected(false);
        disableProductionButton();
        ClientView.setCurrentState(GAME_STATES.END_TURN_STATE);
        Message message = new Message(MessageType.END_PRODUCTION, GUI.getPosition());
        try{
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void endTurnButton(){
        radiobutEndTurn.setSelected(false);
        if(ClientView.isState(GAME_STATES.END_TURN_STATE)) {
            Message message = new Message(MessageType.END_TURN, GUI.getPosition());
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            radiobutEndTurn.setDisable(true);
        }
    }

    private void enableAllButton() {
        radiobutBuyCard.setDisable(false);
        radiobutActLeader.setDisable(false);
        radiobutActivProduc.setDisable(false);
        radiobutDiscardLeader.setDisable(false);
        radiobutOtherPlayboard.setDisable(false);
        radiobutTakeMarble.setDisable(false);
    }

    private void enableProductionsButton() {
        if (!ClientView.isSlotEmpty(GUI.getPosition(), 1)) {
            chooseSlot1.setVisible(true);
            chooseSlot1.setDisable(false);
        }
        if (!ClientView.isSlotEmpty(GUI.getPosition(), 2)) {
            chooseSlot2.setVisible(true);
            chooseSlot2.setDisable(false);
        }
        if (!ClientView.isSlotEmpty(GUI.getPosition(), 3)) {
            chooseSlot3.setVisible(true);
            chooseSlot3.setDisable(false);
        }
        chooseBase.setVisible(true);
        chooseBase.setDisable(false);
        if (ClientView.isLeaderCardActive(GUI.getPosition(), 1)) {
            chooseLeader1.setVisible(true);
            chooseLeader1.setDisable(false);
        }
        if (ClientView.isLeaderCardActive(GUI.getPosition(), 2)) {
            chooseLeader2.setVisible(true);
            chooseLeader2.setDisable(false);
        }
    }

    private void enableEndTurnButton(){
        radiobutEndTurn.setDisable(false);
        radiobutActLeader.setDisable(false);
        radiobutDiscardLeader.setDisable(false);
    }

    private void disableAllButton() {
        radiobutEndProd.setDisable(true);
        radiobutBuyCard.setDisable(true);
        radiobutActLeader.setDisable(true);
        radiobutActivProduc.setDisable(true);
        radiobutDiscardLeader.setDisable(true);
        radiobutOtherPlayboard.setDisable(true);
        radiobutTakeMarble.setDisable(true);
        radiobutEndTurn.setDisable(true);
    }

    private void disableProductionButton(){
        chooseSlot1.setVisible(false);
        chooseSlot2.setVisible(false);
        chooseSlot3.setVisible(false);
        chooseBase.setVisible(false);
        chooseLeader1.setVisible(false);
        chooseLeader2.setVisible(false);
    }

    private void buyCardButton() {
        radiobutBuyCard.setSelected(false);
        ClientView.setCurrentState(GAME_STATES.BUY_CARD_STATE);
        disableAllButton();
        setDisableAllDecks(false);
        if(ClientView.getDevelopmentCards().get(0) != -1) {
            card11.setOnMouseClicked(mouseEvent -> deckEvent(1, 1));
        }
        else
            card11.setDisable(true);
        if(ClientView.getDevelopmentCards().get(4) != -1) {
            card12.setOnMouseClicked(mouseEvent -> deckEvent(1, 2));
        }
        else
            card12.setDisable(true);
        if(ClientView.getDevelopmentCards().get(8) != -1) {
            card13.setOnMouseClicked(mouseEvent -> deckEvent(1, 3));
        }
        else
            card13.setDisable(true);
        if(ClientView.getDevelopmentCards().get(1) != -1) {
            card21.setOnMouseClicked(mouseEvent -> deckEvent(2, 1));
        }
        else
            card21.setDisable(true);
        if(ClientView.getDevelopmentCards().get(5) != -1) {
            card22.setOnMouseClicked(mouseEvent -> deckEvent(2, 2));
        }
        else
            card22.setDisable(true);
        if(ClientView.getDevelopmentCards().get(9) != -1) {
            card23.setOnMouseClicked(mouseEvent -> deckEvent(2, 3));
        }
        else
            card23.setDisable(true);
        if(ClientView.getDevelopmentCards().get(2) != -1) {
            card31.setOnMouseClicked(mouseEvent -> deckEvent(3, 1));
        }
        else
            card31.setDisable(true);
        if(ClientView.getDevelopmentCards().get(6) != -1) {
            card32.setOnMouseClicked(mouseEvent -> deckEvent(3, 2));
        }
        else
            card32.setDisable(true);
        if(ClientView.getDevelopmentCards().get(10) != -1) {
            card33.setOnMouseClicked(mouseEvent -> deckEvent(3, 3));
        }
        else
            card33.setDisable(true);
        if(ClientView.getDevelopmentCards().get(3) != -1) {
            card41.setOnMouseClicked(mouseEvent -> deckEvent(4, 1));
        }
        else
            card41.setDisable(true);
        if(ClientView.getDevelopmentCards().get(7) != -1) {
            card42.setOnMouseClicked(mouseEvent -> deckEvent(4, 2));
        }
        else
            card42.setDisable(true);
        if(ClientView.getDevelopmentCards().get(11) != -1) {
            card43.setOnMouseClicked(mouseEvent -> deckEvent(4, 3));
        }
        else
            card43.setDisable(true);
    }

    private void deckEvent(int column, int row) {
        setDisableAllDecks(true);
        panelBuy.setVisible(true);
        radiobutWarehouse.setOnMouseClicked(mouseEvent1 -> {
            radiobutWarehouse.setSelected(false);
            panelBuy.setVisible(false);
            Message message = new Message_Three_Parameter_Int(MessageType.BUY_CARD, GUI.getPosition(), row, column, 0);
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        radiobutStrongbox.setOnMouseClicked(mouseEvent1 -> {
            radiobutStrongbox.setSelected(false);
            panelBuy.setVisible(false);
            Message message = new Message_Three_Parameter_Int(MessageType.BUY_CARD, GUI.getPosition(), row, column, 1);
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void choseSlot(int slot){
        try {
            if (ClientView.isState(GAME_STATES.BUY_CARD_STATE)) {
                ClientSocket.sendMessage(new Message_One_Parameter_Int(MessageType.CHOSEN_SLOT, GUI.getPosition(), slot));
                chooseSlot1.setVisible(false);
                chooseSlot2.setVisible(false);
                chooseSlot3.setVisible(false);
            }
            if(ClientView.isState(GAME_STATES.FIRST_POWER_STATE) || ClientView.isState(GAME_STATES.ACTIVATE_PRODUCTION_STATE)){
                if(slot == 1)
                    chooseSlot1.setDisable(true);
                else if(slot == 2)
                    chooseSlot2.setDisable(true);
                else
                    chooseSlot3.setDisable(true);
                cardProduction(slot);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void choseLeader(int leader){
        try {
            if (ClientView.isState(GAME_STATES.WHITE_CONVERSION_CARD_STATE)) {
                ClientSocket.sendMessage(new Message_One_Parameter_Int(MessageType.WHITE_CONVERSION_CARD, GUI.getPosition(), leader));
                chooseLeader1.setVisible(false);
                chooseLeader2.setVisible(false);
            }
            if(ClientView.isState(GAME_STATES.FIRST_POWER_STATE) || ClientView.isState(GAME_STATES.ACTIVATE_PRODUCTION_STATE)){
                if(leader == 1)
                    chooseLeader1.setDisable(true);
                else
                    chooseLeader2.setDisable(true);
                leaderProduction(leader);
            }
            if(ClientView.isState(GAME_STATES.FIRST_ACTION_STATE) || ClientView.isState(GAME_STATES.END_TURN_STATE)){
                if(radiobutActLeader.isSelected()) {
                    radiobutActLeader.setSelected(false);
                    ClientSocket.sendMessage(new Message_One_Parameter_Int(MessageType.LEADER_CARD_ACTIVATION, GUI.getPosition(), leader));
                }
                else if(radiobutDiscardLeader.isSelected()){
                    radiobutDiscardLeader.setSelected(false);
                    ClientSocket.sendMessage(new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, GUI.getPosition(), leader));
                }
                chooseLeader1.setVisible(false);
                chooseLeader2.setVisible(false);
                if (ClientView.isState(GAME_STATES.FIRST_ACTION_STATE))
                    enableAllButton();
                else
                    enableEndTurnButton();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDisableAllDecks(boolean b) {
        card11.setDisable(b);
        card12.setDisable(b);
        card13.setDisable(b);
        card21.setDisable(b);
        card22.setDisable(b);
        card23.setDisable(b);
        card31.setDisable(b);
        card32.setDisable(b);
        card33.setDisable(b);
        card41.setDisable(b);
        card42.setDisable(b);
        card43.setDisable(b);
    }

    public static void buy_card_message(int slot, int cardID, String newMessage) {
        try {
            switch (slot) {
                case 1:
                    if ((CardMapCLI.getCard(cardID)).getLevel() == 1)
                        SceneController.setImage("#slot11", CardMapGUI.getCard(cardID));
                    else if ((CardMapCLI.getCard(cardID)).getLevel() == 2)
                        SceneController.setImage("#slot12", CardMapGUI.getCard(cardID));
                    else
                        SceneController.setImage("#slot13", CardMapGUI.getCard(cardID));
                    break;
                case 2:
                    if ((CardMapCLI.getCard(cardID)).getLevel() == 1)
                        SceneController.setImage("#slot21", CardMapGUI.getCard(cardID));
                    else if ((CardMapCLI.getCard(cardID)).getLevel() == 2)
                        SceneController.setImage("#slot22", CardMapGUI.getCard(cardID));
                    else
                        SceneController.setImage("#slot23", CardMapGUI.getCard(cardID));
                    break;
                case 3:
                    if ((CardMapCLI.getCard(cardID)).getLevel() == 1)
                        SceneController.setImage("#slot31", CardMapGUI.getCard(cardID));
                    else if ((CardMapCLI.getCard(cardID)).getLevel() == 2)
                        SceneController.setImage("#slot32", CardMapGUI.getCard(cardID));
                    else
                        SceneController.setImage("#slot33", CardMapGUI.getCard(cardID));
                    break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        SceneController.setDisable("#radiobutActLeader", false);
        SceneController.setDisable("#radiobutDiscardLeader", false);
        SceneController.setDisable("#radiobutOtherPlayboard", false);
        SceneController.setDisable("#radiobutEndTurn", false);
        SceneController.addMessage(newMessage);
    }

    public static void cardRemoveMessage(int row, int column, int cardID, String newMessage){
        String cardFile;
        if(cardID == -1)
            cardFile = "";
        else
            cardFile = CardMapGUI.getCard(cardID);
        try {
            switch (column) {
                case 0:
                    if (row == 0)
                        SceneController.setImage("#card11", cardFile);
                    else if (row == 1)
                        SceneController.setImage("#card12", cardFile);
                    else
                        SceneController.setImage("#card13", cardFile);
                    break;
                case 1:
                    if (row == 0)
                        SceneController.setImage("#card21", cardFile);
                    else if (row == 1)
                        SceneController.setImage("#card22", cardFile);
                    else
                        SceneController.setImage("#card23", cardFile);
                    break;
                case 2:
                    if (row == 0)
                        SceneController.setImage("#card31", cardFile);
                    else if (row == 1)
                        SceneController.setImage("#card32", cardFile);
                    else
                        SceneController.setImage("#card33", cardFile);
                    break;
                case 3:
                    if (row == 0)
                        SceneController.setImage("#card41", cardFile);
                    else if (row == 1)
                        SceneController.setImage("#card42", cardFile);
                    else
                        SceneController.setImage("#card43", cardFile);
                    break;
            }
            SceneController.addMessage(newMessage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void marketChangeMessage(boolean row, int index, String newMessage){
        try {
            if (row) {
                switch (index) {
                    case 1:
                        SceneController.setImage("#marble11", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 0)));
                        SceneController.setImage("#marble12", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 1)));
                        SceneController.setImage("#marble13", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 2)));
                        SceneController.setImage("#marble14", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 3)));
                        break;
                    case 2:
                        SceneController.setImage("#marble21", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 0)));
                        SceneController.setImage("#marble22", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 1)));
                        SceneController.setImage("#marble23", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 2)));
                        SceneController.setImage("#marble24", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 3)));
                        break;
                    case 3:
                        SceneController.setImage("#marble31", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 0)));
                        SceneController.setImage("#marble32", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 1)));
                        SceneController.setImage("#marble33", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 2)));
                        SceneController.setImage("#marble34", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 3)));
                        break;
                }
            } else {
                switch (index) {
                    case 1:
                        SceneController.setImage("#marble11", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 0)));
                        SceneController.setImage("#marble21", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 0)));
                        SceneController.setImage("#marble31", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 0)));
                        break;
                    case 2:
                        SceneController.setImage("#marble12", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 1)));
                        SceneController.setImage("#marble22", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 1)));
                        SceneController.setImage("#marble32", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 1)));
                        break;
                    case 3:
                        SceneController.setImage("#marble13", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 2)));
                        SceneController.setImage("#marble23", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 2)));
                        SceneController.setImage("#marble33", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 2)));
                        break;
                    case 4:
                        SceneController.setImage("#marble14", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(0, 3)));
                        SceneController.setImage("#marble24", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(1, 3)));
                        SceneController.setImage("#marble34", MarbleMapGUI.getMarble(ClientView.getMarket().getMarble(2, 3)));
                        break;
                }
            }
            SceneController.addMessage(newMessage);
            SceneController.setImage("#marbleExt", MarbleMapGUI.getMarble(ClientView.getMarket().getExternalMarble()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void increaseFaithPointsMessage(int faithPoints, boolean Ludovico, String newMessage){
        int x;
        int y;
        if(faithPoints < 3){
            x = 30*faithPoints + 7;
            y = 107;
        }
        else if (faithPoints >= 3 && faithPoints < 5) {
            x = 67;
            y = 168 - faithPoints*28;
        }
        else if(faithPoints >= 5 && faithPoints < 10){
            x = 30*faithPoints - 52;
            y = 56;
        }
        else if (faithPoints >= 10 && faithPoints < 12) {
            x = 218;
            y = faithPoints*28 - 200;
        }
        else if(faithPoints >= 12 && faithPoints < 17){
            x = 30*faithPoints - 112;
            y = 107;
        }
        else if(faithPoints >= 17 && faithPoints < 19){
            x = 368;
            y = 555 - 28*faithPoints;
        }
        else {
            if(faithPoints > 24)
                faithPoints = 24;
            x = 30*faithPoints - 172;
            y = 56;
        }
        if(Ludovico){
            SceneController.setLayoutX("#croceNera", x);
            SceneController.setLayoutY("#croceNera", y);
        }
        else {
            SceneController.setLayoutX("#croceRossa", x);
            SceneController.setLayoutY("#croceRossa", y);
        }
        SceneController.addMessage(newMessage);
    }

    public static void increaseWarehouseMessage(int depot, String newMessage){
        Resource r = ClientView.getWarehouse(GUI.getPosition()).get(depot - 1).getResource();
        int amount = ClientView.getWarehouse(GUI.getPosition()).get(depot - 1).getAmount();
        try {
            switch (depot){
                case 1:
                    SceneController.setImage("#deposit11", ResourceMapGUI.getResource(r));
                    break;
                case 2:
                    if(amount == 1)
                        SceneController.setImage("#deposit21", ResourceMapGUI.getResource(r));
                    else
                        SceneController.setImage("#deposit22", ResourceMapGUI.getResource(r));
                    break;
                case 3:
                    if(amount == 1)
                        SceneController.setImage("#deposit31", ResourceMapGUI.getResource(r));
                    else if(amount == 2)
                        SceneController.setImage("#deposit32", ResourceMapGUI.getResource(r));
                    else
                        SceneController.setImage("#deposit33", ResourceMapGUI.getResource(r));
                    break;
                case 4:
                    if(amount == 1)
                        SceneController.setImage("#extra11", ResourceMapGUI.getResource(r));
                    else
                        SceneController.setImage("#extra12", ResourceMapGUI.getResource(r));
                    break;
                case 5:
                    if(amount == 1)
                        SceneController.setImage("#extra21", ResourceMapGUI.getResource(r));
                    else
                        SceneController.setImage("#extra22", ResourceMapGUI.getResource(r));
                    break;
            }
            SceneController.addMessage(newMessage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void switchDepotMessage(int depot1, int depot2, String newMessage){
        try {
            setSwitchedDepot(depot1);
            setSwitchedDepot(depot2);
            SceneController.addMessage(newMessage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void setSwitchedDepot(int depot) throws FileNotFoundException {
        Resource r = ClientView.getWarehouse(GUI.getPosition()).get(depot - 1).getResource();
        int amount = ClientView.getWarehouse(GUI.getPosition()).get(depot - 1).getAmount();
        switch (depot) {
            case 1:
                if (amount > 0)
                    SceneController.setImage("#deposit11", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#deposit11", "");
                break;
            case 2:
                if (amount == 2)
                    SceneController.setImage("#deposit22", ResourceMapGUI.getResource(r));
                if(amount >= 1)
                    SceneController.setImage("#deposit21", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#deposit21", "");
                break;
            case 3:
                if (amount >= 2)
                    SceneController.setImage("#deposit32", ResourceMapGUI.getResource(r));
                if(amount >= 1)
                    SceneController.setImage("#deposit31", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#deposit31", "");
                break;
            case 4:
                if (amount == 2)
                    SceneController.setImage("#extra12", ResourceMapGUI.getResource(r));
                if(amount >= 1)
                    SceneController.setImage("#extra11", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#extra11", "");
                break;
            case 5:
                if (amount == 2)
                    SceneController.setImage("#extra22", ResourceMapGUI.getResource(r));
                if(amount >= 1)
                    SceneController.setImage("#extra21", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#extra21", "");
                break;
        }
    }

    public static void leaderCardActivationMessage(String newMessage){
        if(ClientView.isLeaderCardActive(GUI.getPosition(), 1))
            SceneController.setOpacity("#leader1", 1);
        if (ClientView.isLeaderCardActive(GUI.getPosition(), 2))
            SceneController.setOpacity("#leader2", 1);
        if(!leaderCardAvailable()){
            SceneController.setDisable("#radiobutActLeader", true);
            SceneController.setDisable("#radiobutDiscardLeader", true);
        }
        else {
            SceneController.setDisable("#radiobutActLeader", false);
            SceneController.setDisable("#radiobutDiscardLeader", false);
        }
        SceneController.addMessage(newMessage);
    }

    public static void leaderCardDiscardMessage(String newMessage){
        try {
            SceneController.setImage("#leader1", CardMapGUI.getCard(ClientView.getLeaderCards(GUI.getPosition()).get(0)));
            SceneController.setImage("#leader2", "");
            SceneController.addMessage(newMessage);
            if(!leaderCardAvailable()){
                SceneController.setDisable("#radiobutActLeader", true);
                SceneController.setDisable("#radiobutDiscardLeader", true);
            }
            else {
                SceneController.setDisable("#radiobutActLeader", false);
                SceneController.setDisable("#radiobutDiscardLeader", false);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void vaticanReportMessage(int pope, int victoryPoints, String newMessage){
        try {
            switch (pope){
                case 1:
                    if(victoryPoints == 0)
                        SceneController.setImage("#pope1", "/photos/quadrato giallo.png");
                    else
                        SceneController.setImage("#pope1", "/photos/pope_favor1_front.png");
                    break;
                case 2:
                    if(victoryPoints == 0 || victoryPoints == 2)
                        SceneController.setImage("#pope2", "/photos/quadrato arancione.png");
                    else
                        SceneController.setImage("#pope2", "/photos/pope_favor2_front.png");
                    break;
                case 3:
                    if(victoryPoints == 0 || victoryPoints == 2 || victoryPoints == 3 || victoryPoints == 5)
                        SceneController.setImage("#pope3", "/photos/quadrato rosso.png");
                    else
                        SceneController.setImage("#pope3", "/photos/pope_favor3_front.png");
            }
            SceneController.addMessage(newMessage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void whiteConversionMessage(){
        notYourTurn();
        SceneController.setDisable("#radiobutOtherPlayboard", true);
        SceneController.setVisible("#chooseLeader1", true);
        SceneController.setVisible("#chooseLeader2", true);
    }

    public static void choseSlotMessage(int slot1, int slot2, int slot3){
        notYourTurn();
        SceneController.setDisable("#radiobutOtherPlayboard", true);
        if(slot1 == 1) {
            SceneController.setVisible("#chooseSlot1", true);
            SceneController.setDisable("#chooseSlot1", false);
        }
        if(slot2 == 2 || slot1 == 2) {
            SceneController.setVisible("#chooseSlot2", true);
            SceneController.setDisable("#chooseSlot2", false);
        }
        if(slot3 == 3 || slot2 == 3) {
            SceneController.setVisible("#chooseSlot3", true);
            SceneController.setDisable("#chooseSlot3", false);
        }
    }

    public static void modifiedResource() {
        try {
            if (ClientView.getWarehouse(GUI.getPosition()).get(0).getAmount() == 1)
                SceneController.setImage("#deposit11", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(0).getResource()));
            if (ClientView.getWarehouse(GUI.getPosition()).get(1).getAmount() >= 1)
                SceneController.setImage("#deposit21", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(1).getResource()));
            if (ClientView.getWarehouse(GUI.getPosition()).get(1).getAmount() == 2)
                SceneController.setImage("#deposit22", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(1).getResource()));
            if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() >= 1)
                SceneController.setImage("#deposit31", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
            if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() >= 2)
                SceneController.setImage("#deposit32", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
            if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() == 3)
                SceneController.setImage("#deposit33", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
            if (ClientView.getWarehouse(GUI.getPosition()).size() >= 4) {
                if (ClientView.getWarehouse(GUI.getPosition()).get(3).getAmount() >= 1)
                    SceneController.setImage("#extra11", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(3).getResource()));
                if (ClientView.getWarehouse(GUI.getPosition()).get(3).getAmount() == 2)
                    SceneController.setImage("#extra12", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(3).getResource()));
            }
            if (ClientView.getWarehouse(GUI.getPosition()).size() == 5) {
                if (ClientView.getWarehouse(GUI.getPosition()).get(4).getAmount() >= 1)
                    SceneController.setImage("#extra21", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(4).getResource()));
                if (ClientView.getWarehouse(GUI.getPosition()).get(4).getAmount() == 2)
                    SceneController.setImage("#extra22", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(4).getResource()));
            }
            SceneController.setText("#coinAmount", String.valueOf(ClientView.coinAmount(GUI.getPosition())));
            SceneController.setText("#servantAmount", String.valueOf(ClientView.servantAmount(GUI.getPosition())));
            SceneController.setText("#shieldAmount", String.valueOf(ClientView.shieldAmount(GUI.getPosition())));
            SceneController.setText("#stoneAmount", String.valueOf(ClientView.stoneAmount(GUI.getPosition())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void yourTurn(){
        SceneController.setDisable("#radiobutTakeMarble", false);
        SceneController.setDisable("#radiobutBuyCard", false);
        SceneController.setDisable("#radiobutActivProduc", false);
        if(!leaderCardAvailable()){
            SceneController.setDisable("#radiobutActLeader", true);
            SceneController.setDisable("#radiobutDiscardLeader", true);
        }
        else {
            SceneController.setDisable("#radiobutActLeader", false);
            SceneController.setDisable("#radiobutDiscardLeader", false);
        }
        SceneController.setDisable("#move", false);
    }

    public static void disableProductions(){
        SceneController.setVisible("#chooseSlot1", false);
        SceneController.setVisible("#chooseSlot2", false);
        SceneController.setVisible("#chooseSlot3", false);
        SceneController.setVisible("#chooseBase", false);
        SceneController.setVisible("#chooseLeader1", false);
        SceneController.setVisible("#chooseLeader2", false);
    }

    public static void production(){
        SceneController.setDisable("#radiobutEndProd", false);
        SceneController.setDisable("#radiobutOtherPlayboard", false);
    }

    public static void endTurn(){
        ClientView.setCurrentState(GAME_STATES.END_TURN_STATE);
        SceneController.setDisable("#radiobutEndTurn", false);
        if(!leaderCardAvailable()){
            SceneController.setDisable("#radiobutActLeader", true);
            SceneController.setDisable("#radiobutDiscardLeader", true);
        }
        else {
            SceneController.setDisable("#radiobutActLeader", false);
            SceneController.setDisable("#radiobutDiscardLeader", false);
        }
        if(GUI.getNumOfPlayers() > 1){
            SceneController.setVisible("#radiobutOtherPlayboard", true);
            SceneController.setDisable("#radiobutOtherPlayboard", false);
        }
    }

    public static void notYourTurn(){
        SceneController.setDisable("#radiobutTakeMarble", true);
        SceneController.setDisable("#radiobutBuyCard", true);
        SceneController.setDisable("#radiobutActivProduc", true);
        SceneController.setDisable("#radiobutActLeader", true);
        SceneController.setDisable("#radiobutDiscardLeader", true);
        SceneController.setDisable("#radiobutEndTurn", true);
        SceneController.setDisable("#radiobutEndProd", true);
        SceneController.setDisable("#move", true);
    }

/*
    public void update() {
        Task<Void> task = new Task<Void>() {
            String a = "Initial text";

            @Override
            public Void call() throws Exception {
                int i = 0;

                while (true) {

                    if (i > 4)
                        a = "I is bigger than 4";

                    if (i > 10)
                        a = "I is bigger than 10";

                    Platform.runLater(() -> {
                        testo.setText(a);
                        // If you want to you can also move the text here
                        testo.relocate(10, 10);
                    });

                    i++;
                    Thread.sleep(1000);
                }
            }
        };
        Thread th = new Thread(task);
        th.setDaemon(true);
        th.start();

    }

 */

}
