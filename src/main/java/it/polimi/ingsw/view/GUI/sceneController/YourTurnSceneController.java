package it.polimi.ingsw.view.GUI.sceneController;

import it.polimi.ingsw.model.games.states.GameStates;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.parser.*;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.GUI.*;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

/**
 * YourTurnSceneController handle the display of player's board.
 */
public class YourTurnSceneController {

    @FXML
    private RadioButton one;
    @FXML
    private RadioButton two;
    @FXML
    private RadioButton three;
    @FXML
    private RadioButton four;
    @FXML
    private RadioButton five;
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
    private RadioButton radiobutEndGame;
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
    private Node croceRossa;
    @FXML
    private Node croceNera;
    @FXML
    private Label message;
    @FXML
    private Button yes;
    @FXML
    private Button no;
    @FXML
    private RadioButton radiobutEndProd;
    @FXML
    private Pane panelBuy;
    @FXML
    private Button radiobutWarehouse;
    @FXML
    private Button radiobutStrongbox;
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
    @FXML
    private ImageView calamaio;
    @FXML
    private RadioButton radiobutCheat;

    private ArrayList<Integer> switchCounter=new ArrayList<>(2);

    /**
     * handle all buttons actions and show current state playerboard.
     */
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
        if(ClientView.isDevelopers()){
            radiobutCheat.setVisible(true);
            radiobutCheat.setOnMouseClicked(mouseEvent -> {
                try {
                    cheat();
                    radiobutCheat.setVisible(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        radiobutOtherPlayboard.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> otherPlayerboardButton());
        radiobutEndProd.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> endProductionButton());
        radiobutEndTurn.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> endTurnButton());
        radiobutEndGame.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> System.exit(1));
        chooseBase.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            chooseBase.setDisable(true);
            basicProduction();
        });
        row1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            row1.setSelected(false);
            takeMarketMarbleRow(3);
        });
        row2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            row2.setSelected(false);
            takeMarketMarbleRow(2);
        });
        row3.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            row3.setSelected(false);
            takeMarketMarbleRow(1);
        });
        column1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            column1.setSelected(false);
            takeMarketMarbleColumn(1);
        });
        column2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            column2.setSelected(false);
            takeMarketMarbleColumn(2);
        });
        column3.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            column3.setSelected(false);
            takeMarketMarbleColumn(3);
        });
        column4.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            column4.setSelected(false);
            takeMarketMarbleColumn(4);
        });
        chooseSlot1.setOnMouseClicked(mouseEvent -> choseSlot(1));
        chooseSlot2.setOnMouseClicked(mouseEvent -> choseSlot(2));
        chooseSlot3.setOnMouseClicked(mouseEvent -> choseSlot(3));
        chooseLeader1.setOnMouseClicked(mouseEvent -> choseLeader(1));
        chooseLeader2.setOnMouseClicked(mouseEvent -> choseLeader(2));
        okError.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> errorPane.setVisible(false));
        yes.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            yes.setDisable(true);
            no.setDisable(true);
            switchCounter = new ArrayList<>(2);
            switchDepots();
        });
        no.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            disableSwitches();
            yes.setVisible(false);
            no.setVisible(false);
            SceneController.setText("#message", "Choose which marble use");
            disableMarbleShow(false);
        });
        marbleShow1.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                ClientSocket.sendMessage(new MessageOneParameterMarble(MessageType.USE_MARBLE, GUI.getPosition(), ClientView.getMarbles().remove(0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        marbleShow2.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                ClientSocket.sendMessage(new MessageOneParameterMarble(MessageType.USE_MARBLE, GUI.getPosition(), ClientView.getMarbles().remove(1)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        marbleShow3.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                ClientSocket.sendMessage(new MessageOneParameterMarble(MessageType.USE_MARBLE, GUI.getPosition(), ClientView.getMarbles().remove(2)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        marbleShow4.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->{
            try {
                ClientSocket.sendMessage(new MessageOneParameterMarble(MessageType.USE_MARBLE, GUI.getPosition(), ClientView.getMarbles().remove(3)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        one.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
            clickedSwitch(1));
        two.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
            clickedSwitch(2));
        three.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
            clickedSwitch(3));
        four.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
            clickedSwitch(4));
        five.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent ->
            clickedSwitch(5));
    }

    /**
     * @return true if one of both LeaderCards are still not activated or discarded.
     */
    private static boolean leaderCardAvailable(){
        return ((!ClientView.isLeaderCardActive(GUI.getPosition(), 1) && ClientView.getLeaderCards(GUI.getPosition()).get(0) != -1)
                    || (!ClientView.isLeaderCardActive(GUI.getPosition(), 2) && ClientView.getLeaderCards(GUI.getPosition()).get(1) != -1));
    }

    /**
     * show current player's board.
     * if LeaderCard is not activated set his opacity as 0,5.
     * if it's single player game, make radiobutOtherPlayboard invisible.
     * if player is the first one, make calamaio visible.
     */
    private void showPlayerboard() {
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
        setFaithPoints(false);
        setVictoryPoints();
        if(GUI.getNumOfPlayers() == 1) {
            radiobutOtherPlayboard.setVisible(false);
            setFaithPoints(true);
        }
        if(GUI.getPosition() == 0)
            calamaio.setVisible(true);
        setMessages();
    }

    /**
     * @param image one of ImageView.
     * @param file is the new image.
     *
     * if @param file equals "" set @param image to null, otherwise load by @param file.
     */
    private void setImage(ImageView image, String file){
        if(file.equals(""))
            image.setImage(null);
        else {
            InputStream fis = SceneController.class.getResourceAsStream(file);
            image.setImage(new Image(Objects.requireNonNull(fis)));
        }
    }

    /**
     * @param image is one DevelopmentCards ImageView.
     * @param cardID is one DevelopmentCards cardID.
     *
     * if @param cardID = -1 set @param image to null, otherwise convert @param cardID to CardView and then set @param image.
     */
    private void setCard(ImageView image, int cardID) {
        if (cardID != -1)
            setImage(image, CardMapGUI.getCard(cardID));
        else
            setImage(image, "");
    }

    /**
     * set first depot resource image.
     */
    private void setFirstDepot() {
        if (ClientView.getWarehouse(GUI.getPosition()).get(0).getAmount() == 1)
            setImage(deposit11, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(0).getResource()));
    }

    /**
     * set second depot resource images.
     */
    private void setSecondDepot() {
        if (ClientView.getWarehouse(GUI.getPosition()).get(1).getAmount() >= 1)
            setImage(deposit21, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(1).getResource()));
        if (ClientView.getWarehouse(GUI.getPosition()).get(1).getAmount() == 2)
            setImage(deposit22, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(1).getResource()));
    }

    /**
     * set third depot resource images.
     */
    private void setThirdDepot() {
        if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() >= 1)
            setImage(deposit31, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
        if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() >= 2)
            setImage(deposit32, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
        if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() == 3)
            setImage(deposit33, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
    }

    /**
     * set first extra depot resource images.
     */
    private void setExtraDepot1() {
        if (ClientView.getWarehouse(GUI.getPosition()).size() >= 4) {
            if (ClientView.getWarehouse(GUI.getPosition()).get(3).getAmount() >= 1) {
                if(ClientView.isSecondDepot(GUI.getPosition()))
                    setImage(extra21, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(3).getResource()));
                else
                    setImage(extra11, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(3).getResource()));
            }
            if (ClientView.getWarehouse(GUI.getPosition()).get(3).getAmount() == 2) {
                if(ClientView.isSecondDepot(GUI.getPosition()))
                    setImage(extra22, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(3).getResource()));
                else
                    setImage(extra12, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(3).getResource()));
            }
        }
    }

    /**
     * set second extra depot resource images.
     */
    private void setExtraDepot2() {
        if (ClientView.getWarehouse(GUI.getPosition()).size() == 5) {
            if (ClientView.getWarehouse(GUI.getPosition()).get(4).getAmount() >= 1)
                setImage(extra21, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(4).getResource()));
            if (ClientView.getWarehouse(GUI.getPosition()).get(4).getAmount() == 2)
                setImage(extra22, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(4).getResource()));
        }
    }

    /**
     * set strongbox resources amount.
     */
    private void setStrongbox() {
        coinAmount.setText(String.valueOf(ClientView.coinAmount(GUI.getPosition())));
        servantAmount.setText(String.valueOf(ClientView.servantAmount(GUI.getPosition())));
        shieldAmount.setText(String.valueOf(ClientView.shieldAmount(GUI.getPosition())));
        stoneAmount.setText(String.valueOf(ClientView.stoneAmount(GUI.getPosition())));
    }

    /**
     * set redCross position in faithTrack
     */
    private void setFaithPoints(boolean Ludovico) {
        int faithPoints;
        int offsetX = 0;
        int offsetY = 0;
        int x;
        int y;
        if(!Ludovico) {
            faithPoints = ClientView.getFaithPoints(GUI.getPosition());
        }
        else {
            faithPoints = ClientView.getFaithPoints(1);
            croceNera.setVisible(true);
            offsetX = 5;
            offsetY = 7;
        }
        if(faithPoints < 3){
            x = 30*faithPoints + 6 + offsetX;
            y = 107 + offsetY;
        }
        else if (faithPoints < 5) {
            x = 66 + offsetX;
            y = 159 + offsetY - 26*faithPoints;
        }
        else if(faithPoints < 10){
            x = 30*faithPoints - 54 + offsetX;
            y = 55 + offsetY;
        }
        else if (faithPoints < 12) {
            x = 216 + offsetX;
            y = 26*faithPoints - 179 + offsetY;
        }
        else if(faithPoints < 17){
            x = 30*faithPoints - 114 + offsetX;
            y = 107 + offsetY;
        }
        else if(faithPoints < 19){
            x = 366 + offsetX;
            y = 523 - 26*faithPoints + offsetY;
        }
        else {
            if(faithPoints > 24)
                faithPoints = 24;
            x = 30*faithPoints - 174 + offsetX;
            y = 55 + offsetY;
        }
        if(Ludovico){
            croceNera.setLayoutX(x);
            croceNera.setLayoutY(y);
        }
        else {
            croceRossa.setLayoutX(x);
            croceRossa.setLayoutY(y);
        }
    }

    /**
     * set vatican report tiles.
     */
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

    /**
     * set messages received from Server.
     */
    private void setMessages(){
        for (String message: GUI.getServerMessages())
            display.getChildren().add(new Label(message));
    }

    /**
     * @param i is one warehouse or extra depot (1 to 5).
     *
     * if it's the second clicked depot send to Server a new SWITCH message.
     */
    private void clickedSwitch(int i){
        switchCounter.add(i);
        if (switchCounter.size()==2) {
            one.setSelected(false);
            one.setDisable(true);
            two.setSelected(false);
            two.setDisable(true);
            three.setSelected(false);
            three.setDisable(true);
            four.setSelected(false);
            four.setDisable(true);
            five.setSelected(false);
            five.setDisable(true);
            yes.setDisable(false);
            no.setDisable(false);
            try {
                ClientSocket.sendMessage(new MessageTwoParameterInt(MessageType.SWITCH_DEPOT, GUI.getPosition(), switchCounter.get(0), switchCounter.get(1)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param disabled refers to if marble has to be disabled or not.
     *
     * set showing marbles as @param disabled.
     */
    private static void disableMarbleShow(Boolean disabled){
        SceneController.setDisable("#marbleShow1", disabled);
        SceneController.setDisable("#marbleShow2", disabled);
        SceneController.setDisable("#marbleShow3", disabled);
        SceneController.setDisable("#marbleShow4", disabled);
    }

    /**
     * make all depots button as disabled.
     */
    private void disableSwitches(){
        one.setVisible(false);
        two.setVisible(false);
        three.setVisible(false);
        four.setVisible(false);
        five.setVisible(false);
    }

    /**
     * show current saved chosen marbles.
     */
    public static void chooseMarble() {
        ArrayList<Marble> marbles = ClientView.getMarbles();
        SceneController.setVisible("#message", true);
        SceneController.setImage("#marbleShow1", MarbleMapGUI.getMarble(marbles.get(0)));
        SceneController.setVisible("#marbleShow1", true);
        if (ClientView.getMarbles().size() > 1) {
            SceneController.setImage("#marbleShow2", MarbleMapGUI.getMarble(marbles.get(1)));
            SceneController.setVisible("#marbleShow2", true);
        } else
            SceneController.setVisible("#marbleShow2", false);

        if (ClientView.getMarbles().size() > 2) {
            SceneController.setImage("#marbleShow3", MarbleMapGUI.getMarble(marbles.get(2)));
            SceneController.setVisible("#marbleShow3", true);
        } else
            SceneController.setVisible("#marbleShow3", false);

        if (ClientView.getMarbles().size() > 3) {
            SceneController.setImage("#marbleShow4", MarbleMapGUI.getMarble(marbles.get(3)));
            SceneController.setVisible("#marbleShow4", true);
        } else
            SceneController.setVisible("#marbleShow4", false);
        ask();
    }

    /**
     * display switch question to player and make yes or no buttons visible.
     */
    private static void ask(){
        disableMarbleShow(true);
        SceneController.setText("#message","Do you want to switch your depots?");
        SceneController.setVisible("#yes",true);
        SceneController.setVisible("#no",true);
    }

    /**
     * make depots buttons visible and enabled.
     * in case there are any active ExtraDepotCard also add buttons for their depots.
     */
    private void switchDepots(){
        one.setVisible(true);
        one.setDisable(false);
        two.setVisible(true);
        two.setDisable(false);
        three.setVisible(true);
        three.setDisable(false);
        if(ClientView.getWarehouse(GUI.getPosition()).size() >= 4) {
            four.setVisible(true);
            four.setDisable(false);
            if(ClientView.isSecondDepot(GUI.getPosition()))
                four.setLayoutX(177.0);
        }
        if(ClientView.getWarehouse(GUI.getPosition()).size() == 5) {
            five.setVisible(true);
            five.setDisable(false);
        }
    }

    /**
     * @param i is one market row.
     *
     * send to Server a new TAKE_MARBLE message with @param i as row.
     */
    private void takeMarketMarbleRow(int i) {
        disableMarketButton();
        try {
            ClientSocket.sendMessage(new MessageTwoParameterInt(MessageType.TAKE_MARBLE, GUI.getPosition(), 0, i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param i is one market column.
     *
     * send to Server a new TAKE_MARBLE message with @param i as column.
     */
    private void takeMarketMarbleColumn(int i) {
        disableMarketButton();
        try {
            ClientSocket.sendMessage(new MessageTwoParameterInt(MessageType.TAKE_MARBLE, GUI.getPosition(), 1, i));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * disable all row and column buttons.
     */
    private void disableMarketButton(){
        row1.setDisable(true);
        row2.setDisable(true);
        row3.setDisable(true);
        column1.setDisable(true);
        column2.setDisable(true);
        column3.setDisable(true);
        column4.setDisable(true);
    }

    /**
     * handle radiobutOtherPlayboard clicking based on game number of players.
     */
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

    /**
     * show other player's board.
     */
    private void twoPlayersOtherPlayerboard(){
        final OpponentPlayerboardSceneController[] opsc = new OpponentPlayerboardSceneController[1];
        if(GUI.getPosition() == 0)
            opsc[0] = new OpponentPlayerboardSceneController(1);
        else
            opsc[0] = new OpponentPlayerboardSceneController(0);
        Platform.runLater(() ->
                SceneController.changeRootPane(opsc[0], "/fxml/opponentPlayerboardScene"));
    }

    /**
     * show otherPlayers pane with second and third player buttons to allow player to chose one of them.
     */
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

    /**
     * show otherPlayers pane with second, third and fourth player buttons to allow player to chose one of them.
     */
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

    /**
     * make visible LeaderCards buttons if LeaderCard is not active or discarded.
     */
    private void choseLeaderButton(){
        disableAllButton();
        if(!ClientView.isLeaderCardActive(GUI.getPosition(), 1) && ClientView.getLeaderCards(GUI.getPosition()).get(0) != -1)
            chooseLeader1.setVisible(true);
        if(!ClientView.isLeaderCardActive(GUI.getPosition(), 2) && ClientView.getLeaderCards(GUI.getPosition()).get(1) != -1)
            chooseLeader2.setVisible(true);
    }

    /**
     * enable all production buttons and set currentState as FIRST_POWER_STATE.
     */
    private void activProducButton() {
        radiobutActivProduc.setSelected(false);
        if(ClientView.isState(GameStates.FIRST_ACTION_STATE))
            ClientView.setCurrentState(GameStates.FIRST_POWER_STATE);
        disableAllButton();
        enableProductionsButton();
    }

    /**
     * show pane with 4 resource which player can chose 3 times: the first two for the decreasing resource and the last
     * one for the gaining resource.
     */
    private void basicProduction(){
        basePanel.setVisible(true);
        MessageThreeResourceOneInt messageToSend = new MessageThreeResourceOneInt(MessageType.BASIC_POWER, GUI.getPosition());
        coin1.setOnMouseClicked(MouseEvent -> choseResource(Resource.COIN, messageToSend));
        servant1.setOnMouseClicked(MouseEvent -> choseResource(Resource.SERVANT, messageToSend));
        shield1.setOnMouseClicked(MouseEvent -> choseResource(Resource.SHIELD, messageToSend));
        stone1.setOnMouseClicked(MouseEvent -> choseResource(Resource.STONE, messageToSend));
    }

    /**
     * @param slot is one slot of DevelopmentCards.
     *
     * show warehouse_strongbox panel to allow player to chose which between warehouse and strongbox has the priority to
     * be decreased. Then send to Server a new DEVELOPMENT_CARD_POWER message.
     */
    private void cardProduction(int slot) {
        panelBuy.setVisible(true);
        radiobutWarehouse.setOnMouseClicked(mouseEvent1 -> {
            panelBuy.setVisible(false);
            Message message = new MessageTwoParameterInt(MessageType.DEVELOPMENT_CARD_POWER, GUI.getPosition(), slot, 0);
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        radiobutStrongbox.setOnMouseClicked(mouseEvent1 -> {
            panelBuy.setVisible(false);
            Message message = new MessageTwoParameterInt(MessageType.DEVELOPMENT_CARD_POWER, GUI.getPosition(), slot, 1);
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @param leader is one LeaderCard.
     *
     * show pane with 4 resource to allow player to chose one resource.
     */
    private void leaderProduction(int leader) {
        basePanel.setVisible(true);
        MessageOneResourceTwoInt messageToSend = new MessageOneResourceTwoInt(MessageType.LEADER_CARD_POWER, GUI.getPosition(), leader);
        coin1.setOnMouseClicked(MouseEvent -> choseResource(Resource.COIN, messageToSend));
        servant1.setOnMouseClicked(MouseEvent -> choseResource(Resource.SERVANT, messageToSend));
        shield1.setOnMouseClicked(MouseEvent -> choseResource(Resource.SHIELD, messageToSend));
        stone1.setOnMouseClicked(MouseEvent -> choseResource(Resource.STONE, messageToSend));
    }

    /**
     * @param resource is one resource
     * @param messageToSend is one message which has to be delivered to Server.
     *
     * after the third resource choice, show warehouse_strongbox panel to allow player to chose which between warehouse
     * and strongbox has the priority to be decreased. Then send to Server a new BASIC_POWER message.
     */
    private void choseResource(Resource resource, MessageThreeResourceOneInt messageToSend) {
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
                panelBuy.setVisible(false);
                messageToSend.setPar(0);
                try {
                    ClientSocket.sendMessage(messageToSend);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            radiobutStrongbox.setOnMouseClicked(mouseEvent1 -> {
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

    /**
     * @param resource is one resource
     * @param messageToSend is one message which has to be delivered to Server.
     *
     * after the resource choice, show warehouse_strongbox panel to allow player to chose which between warehouse
     * and strongbox has the priority to be decreased. Then send to Server a new LEADER_CARD_POWER message.
     */
    private void choseResource(Resource resource, MessageOneResourceTwoInt messageToSend) {
        messageToSend.setResource(resource);
        basePanel.setVisible(false);
        panelBuy.setVisible(true);
        radiobutWarehouse.setOnMouseClicked(mouseEvent1 -> {
            panelBuy.setVisible(false);
            messageToSend.setPar2(0);
            try {
                ClientSocket.sendMessage(messageToSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        radiobutStrongbox.setOnMouseClicked(mouseEvent1 -> {
            panelBuy.setVisible(false);
            messageToSend.setPar2(1);
            try {
                ClientSocket.sendMessage(messageToSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * enable all market row and column buttons.
     */
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

    /**
     * send to Server an END_PRODUCTION message.
     */
    private void endProductionButton(){
        radiobutEndProd.setDisable(true);
        radiobutEndProd.setSelected(false);
        disableProductionButton();
        ClientView.setCurrentState(GameStates.END_TURN_STATE);
        Message message = new Message(MessageType.END_PRODUCTION, GUI.getPosition());
        try{
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * send to Server an END_TURN message.
     */
    private void endTurnButton(){
        radiobutEndTurn.setSelected(false);
        if(ClientView.isState(GameStates.END_TURN_STATE)) {
            Message message = new Message(MessageType.END_TURN, GUI.getPosition());
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            radiobutEndTurn.setDisable(true);
        }
    }

    /**
     * enable all action buttons.
     */
    private void enableAllButton() {
        radiobutBuyCard.setDisable(false);
        radiobutActLeader.setDisable(false);
        radiobutActivProduc.setDisable(false);
        radiobutDiscardLeader.setDisable(false);
        radiobutOtherPlayboard.setDisable(false);
        radiobutTakeMarble.setDisable(false);
        radiobutCheat.setDisable(false);
    }

    /**
     * enable production buttons.
     * basic button is always visible.
     * card button is visible only if selected slot has at least one DevelopmentCard.
     * leader button is visible only if LeaderCard is active.
     */
    private void enableProductionsButton() {
        if (ClientView.isSlotEmpty(GUI.getPosition(), 1)) {
            chooseSlot1.setVisible(true);
            chooseSlot1.setDisable(false);
        }
        if (ClientView.isSlotEmpty(GUI.getPosition(), 2)) {
            chooseSlot2.setVisible(true);
            chooseSlot2.setDisable(false);
        }
        if (ClientView.isSlotEmpty(GUI.getPosition(), 3)) {
            chooseSlot3.setVisible(true);
            chooseSlot3.setDisable(false);
        }
        chooseBase.setVisible(true);
        chooseBase.setDisable(false);
        if (ClientView.isAdditionalPowerCard(GUI.getPosition(), 1)) {
            chooseLeader1.setVisible(true);
            chooseLeader1.setDisable(false);
        }
        if (ClientView.isAdditionalPowerCard(GUI.getPosition(), 2)) {
            chooseLeader2.setVisible(true);
            chooseLeader2.setDisable(false);
        }
    }

    /**
     * enable end turn button.
     */
    private void enableEndTurnButton(){
        radiobutEndTurn.setDisable(false);
        radiobutActLeader.setDisable(false);
        radiobutDiscardLeader.setDisable(false);
        radiobutCheat.setDisable(false);
    }

    /**
     * disable all action buttons.
     */
    private void disableAllButton() {
        radiobutEndProd.setDisable(true);
        radiobutBuyCard.setDisable(true);
        radiobutActLeader.setDisable(true);
        radiobutActivProduc.setDisable(true);
        radiobutDiscardLeader.setDisable(true);
        radiobutOtherPlayboard.setDisable(true);
        radiobutTakeMarble.setDisable(true);
        radiobutEndTurn.setDisable(true);
        radiobutCheat.setDisable(true);
    }

    /**
     * disable all production buttons.
     */
    private void disableProductionButton(){
        chooseSlot1.setVisible(false);
        chooseSlot2.setVisible(false);
        chooseSlot3.setVisible(false);
        chooseBase.setVisible(false);
        chooseLeader1.setVisible(false);
        chooseLeader2.setVisible(false);
    }

    /**
     * for all DevelopmentCards if deck is not empty enable card.
     * set currentState as BUY_CARD_STATE.
     */
    private void buyCardButton() {
        radiobutBuyCard.setSelected(false);
        ClientView.setCurrentState(GameStates.BUY_CARD_STATE);
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

    /**
     * @param column is player's chosen DevelopmentCard column.
     * @param row is player's chosen DevelopmentCard row.
     *
     * show warehouse_strongbox panel to allow player to chose which between warehousv and strongbox has the priority to
     *  be decreased. Then send to Server a new BUY_CARD message.
     */
    private void deckEvent(int column, int row) {
        setDisableAllDecks(true);
        panelBuy.setVisible(true);
        radiobutWarehouse.setOnMouseClicked(mouseEvent1 -> {
            panelBuy.setVisible(false);
            Message message = new MessageThreeParameterInt(MessageType.BUY_CARD, GUI.getPosition(), row, column, 0);
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        radiobutStrongbox.setOnMouseClicked(mouseEvent1 -> {
            panelBuy.setVisible(false);
            Message message = new MessageThreeParameterInt(MessageType.BUY_CARD, GUI.getPosition(), row, column, 1);
            try {
                ClientSocket.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * @param slot is one DevelopmentCard slot.
     *
     * if currentState is BUY_CARD_STATE send to Server a new CHOSEN_SLOT message.
     * if currentState is FIRST_POWER_STATE or ACTIVATE_PRODUCTION_STATE start a new cardProduction procedure.
     */
    private void choseSlot(int slot){
        try {
            if (ClientView.isState(GameStates.BUY_CARD_STATE)) {
                ClientSocket.sendMessage(new MessageOneParameterInt(MessageType.CHOSEN_SLOT, GUI.getPosition(), slot));
                chooseSlot1.setVisible(false);
                chooseSlot2.setVisible(false);
                chooseSlot3.setVisible(false);
            }
            if(ClientView.isState(GameStates.FIRST_POWER_STATE) || ClientView.isState(GameStates.ACTIVATE_PRODUCTION_STATE)){
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

    /**
     * @param leader is one LeaederCard
     *
     * if currentState is WHITE_CONVERSION_CARD_STATE send to Server a new WHITE_CONVERSION message.
     * if currentState is FIRST_POWER_STATE or ACTIVATE_PRODUCTION_STATE start a new leaderProduction procedure.
     * if currenyState is FIRST_ACTION_STATE or END_TURN_STATE send to Server a new ACTIVE_LEADER or DISCARD_LEADER message.
     */
    private void choseLeader(int leader){
        try {
            if (ClientView.isState(GameStates.WHITE_CONVERSION_CARD_STATE)) {
                ClientSocket.sendMessage(new MessageOneParameterInt(MessageType.WHITE_CONVERSION_CARD, GUI.getPosition(), leader));
                chooseLeader1.setVisible(false);
                chooseLeader2.setVisible(false);
            }
            if(ClientView.isState(GameStates.FIRST_POWER_STATE) || ClientView.isState(GameStates.ACTIVATE_PRODUCTION_STATE)){
                if(leader == 1)
                    chooseLeader1.setDisable(true);
                else
                    chooseLeader2.setDisable(true);
                leaderProduction(leader);
            }
            if(ClientView.isState(GameStates.FIRST_ACTION_STATE) || ClientView.isState(GameStates.END_TURN_STATE)){
                if(radiobutActLeader.isSelected()) {
                    radiobutActLeader.setSelected(false);
                    ClientSocket.sendMessage(new MessageOneParameterInt(MessageType.LEADER_CARD_ACTIVATION, GUI.getPosition(), leader));
                }
                else if(radiobutDiscardLeader.isSelected()){
                    radiobutDiscardLeader.setSelected(false);
                    ClientSocket.sendMessage(new MessageOneParameterInt(MessageType.LEADER_CARD_DISCARD, GUI.getPosition(), leader));
                }
                chooseLeader1.setVisible(false);
                chooseLeader2.setVisible(false);
                if (ClientView.isState(GameStates.FIRST_ACTION_STATE))
                    enableAllButton();
                else
                    enableEndTurnButton();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param b is disabled or not.
     *
     * make all DevelopmentCards disabled or not based on @param b.
     */
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

    /**
     * @throws IOException if occurs a problem with readLine or sendMessage.
     *
     * this command cheats the game. Send to server a new LEADER_ACTIVATION with leaderCard = 0. In this case Server
     * change player's LeaderCard with two WhiteConversionCard and active them without having enough requirements.
     */
    private void cheat() throws IOException {
        setCard(leader1, 57);
        setCard(leader2, 58);
        ClientView.setLeaderCard(GUI.getPosition(), 57, 58);
        ClientSocket.sendMessage(new MessageOneParameterInt(MessageType.LEADER_CARD_ACTIVATION, GUI.getPosition(), 0));
    }

    /**
     * @param slot is DevelopmentCard's slot.
     * @param cardID is DevelopmentCard's cardID.
     * @param newMessage is the message received from Server.
     *
     * when receiving a BUY_CARD message from Server, add received DevelopmentCard and add message.
     */
    public static void buyCardMessage(int slot, int cardID, String newMessage) {
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
        SceneController.setDisable("#radiobutActLeader", false);
        SceneController.setDisable("#radiobutDiscardLeader", false);
        SceneController.setDisable("#radiobutOtherPlayboard", false);
        SceneController.setDisable("#radiobutEndTurn", false);
        SceneController.setDisable("#radiobutCheat", false);
        SceneController.addMessage(newMessage);
    }

    /**
     * @param row is one decks row.
     * @param column is one decks column.
     * @param cardID is the new DevelopmentCard's cardID.
     * @param newMessage is the message received from Server.
     *
     * when receiving a CARD_REMOVE message from Server, replace received DevelopmentCard and add message.
     */
    public static void cardRemoveMessage(int row, int column, int cardID, String newMessage){
        String cardFile;
        if(cardID == -1)
            cardFile = "";
        else
            cardFile = CardMapGUI.getCard(cardID);
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
    }

    /**
     * @param row is true for wor and false for column.
     * @param index is selected row or column.
     * @param newMessage is the message received from Server.
     *
     * when receiving a MARKET_CHANGE message from Server, slide market row or column and add message.
     */
    public static void marketChangeMessage(boolean row, int index, String newMessage){
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
    }

    /**
     * @param faithPoints is player's faithPoints.
     * @param Ludovico refers if Ludovico has increased his faith points.
     * @param newMessage is the message received from Server.
     *
     * when receiving a FAITH_POINTS_INCREASE message from Server, move red or black cross and add message.
     */
    public static void increaseFaithPointsMessage(int faithPoints, boolean Ludovico, String newMessage){
        int offsetX = 0;
        int offsetY = 0;
        int x;
        int y;
        if(Ludovico){
            offsetX = 5;
            offsetY = 7;
        }
        if(faithPoints < 3){
            x = 30*faithPoints + 6 + offsetX;
            y = 107 + offsetY;
        }
        else if (faithPoints < 5) {
            x = 66 + offsetX;
            y = 159 + offsetY - 26*faithPoints;
        }
        else if(faithPoints < 10){
            x = 30*faithPoints - 54 + offsetX;
            y = 55 + offsetY;
        }
        else if (faithPoints < 12) {
            x = 216 + offsetX;
            y = 26*faithPoints - 179 + offsetY;
        }
        else if(faithPoints < 17){
            x = 30*faithPoints - 114 + offsetX;
            y = 107 + offsetY;
        }
        else if(faithPoints < 19){
            x = 366 + offsetX;
            y = 523 - 26*faithPoints + offsetY;
        }
        else {
            if(faithPoints > 24)
                faithPoints = 24;
            x = 30*faithPoints - 174 + offsetX;
            y = 55 + offsetY;
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

    /**
     * @param depot is one Warehouse or Extra depot.
     * @param newMessage is the message received from Server.
     *
     * when receiving a INCREASE_WAREHOUSE message from Server, add resource to Warehouse and add message.
     */
    public static void increaseWarehouseMessage(int depot, String newMessage){
        Resource r = ClientView.getWarehouse(GUI.getPosition()).get(depot - 1).getResource();
        int amount = ClientView.getWarehouse(GUI.getPosition()).get(depot - 1).getAmount();
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
                if(amount == 1) {
                    if(ClientView.isSecondDepot(GUI.getPosition()))
                        SceneController.setImage("#extra21", ResourceMapGUI.getResource(r));
                    else
                        SceneController.setImage("#extra11", ResourceMapGUI.getResource(r));
                }
                else {
                    if(ClientView.isSecondDepot(GUI.getPosition()))
                        SceneController.setImage("#extra22", ResourceMapGUI.getResource(r));
                    else
                        SceneController.setImage("#extra12", ResourceMapGUI.getResource(r));
                };
                break;
            case 5:
                if(amount == 1)
                    SceneController.setImage("#extra21", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#extra22", ResourceMapGUI.getResource(r));
                break;
        }
        SceneController.addMessage(newMessage);
    }

    /**
     * @param depot1 is one Warehouse or Extra depot.
     * @param depot2 is one Warehouse or Extra depot.
     * @param newMessage is the message received from Server.
     *
     * when receiving a SWITCH_DEPOT message from Server, swap depots, set thier resources and add message.
     */
    public static void switchDepotMessage(int depot1, int depot2, String newMessage){
        setSwitchedDepot(depot1);
        setSwitchedDepot(depot2);
        SceneController.addMessage(newMessage);
    }

    /**
     * @param depot is one Warehouse or Extra depot.
     *
     * set @param depot with saved data.
     */
    private static void setSwitchedDepot(int depot) {
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
                else
                    SceneController.setImage("#deposit22", "");
                if(amount >= 1)
                    SceneController.setImage("#deposit21", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#deposit21", "");
                break;
            case 3:
                if(amount == 3)
                    SceneController.setImage("#deposit33", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#deposit33", "");
                if (amount >= 2)
                    SceneController.setImage("#deposit32", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#deposit32", "");
                if(amount >= 1)
                    SceneController.setImage("#deposit31", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#deposit31", "");
                break;
            case 4:
                String selector1;
                String selector2;
                if(ClientView.isSecondDepot(GUI.getPosition())){
                    selector1 = "#extra21";
                    selector2 = "#extra22";
                }
                else {
                    selector1 = "#extra11";
                    selector2 = "#extra12";
                }
                if (amount == 2)
                    SceneController.setImage(selector1, ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage(selector1, "");
                if(amount >= 1)
                    SceneController.setImage(selector2, ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage(selector2, "");
                break;
            case 5:
                if (amount == 2)
                    SceneController.setImage("#extra22", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#extra22", "");
                if(amount >= 1)
                    SceneController.setImage("#extra21", ResourceMapGUI.getResource(r));
                else
                    SceneController.setImage("#extra21", "");
                break;
        }
    }

    /**
     * @param newMessage is the message received from Server.
     *
     * when receiving a LEADER_ACTIVATION message from Server, change LeaderCard opacity and add message.
     */
    public static void leaderCardActivationMessage(String newMessage){
        SceneController.setImage("#leader1", CardMapGUI.getCard(ClientView.getLeaderCards(GUI.getPosition()).get(0)));
        SceneController.setImage("#leader2", CardMapGUI.getCard(ClientView.getLeaderCards(GUI.getPosition()).get(1)));
        if (ClientView.isLeaderCardActive(GUI.getPosition(), 1))
            SceneController.setOpacity("#leader1", 1);
        if (ClientView.isLeaderCardActive(GUI.getPosition(), 2)) {
            SceneController.setOpacity("#leader2", 1);
        }
        if (!leaderCardAvailable()) {
            SceneController.setDisable("#radiobutActLeader", true);
            SceneController.setDisable("#radiobutDiscardLeader", true);
        } else {
            SceneController.setDisable("#radiobutActLeader", false);
            SceneController.setDisable("#radiobutDiscardLeader", false);
        }
        SceneController.setDisable("#radiobutCheat", false);
        SceneController.addMessage(newMessage);
    }

    /**
     * @param newMessage is the message received from Server.
     *
     * when receiving a LEADER_DISCARD message from Server, remove one LeaderCard and add message.
     */
    public static void leaderCardDiscardMessage(String newMessage){
        if(ClientView.getLeaderCards(GUI.getPosition()).get(0) != -1)
            SceneController.setImage("#leader1", CardMapGUI.getCard(ClientView.getLeaderCards(GUI.getPosition()).get(0)));
        else
            SceneController.setImage("#leader1", "");
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
        SceneController.setDisable("#radiobutCheat", false);
    }

    /**
     * @param newMessage is the message received from Server.
     *
     * when receiving a VATICAN_REPORT message from Server, add Vatican report tiles and add message.
     */
    public static void vaticanReportMessage(String newMessage){
        int victoryPoints = ClientView.getVictoryPoints(GUI.getPosition());
        switch (ClientView.getCurrentPope()){
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
                break;
        }
        SceneController.addMessage(newMessage);
    }

    /**
     * disable all buttons and enable LeaderCards.
     */
    public static void whiteConversionMessage(){
        notYourTurn();
        SceneController.setDisable("#radiobutOtherPlayboard", true);
        SceneController.setVisible("#chooseLeader1", true);
        SceneController.setVisible("#chooseLeader2", true);
    }

    /**
     * @param slot1 is one available slot.
     * @param slot2 is one available slot.
     * @param slot3 is one available slot.
     *
     * didable all buttons and enable available slots buttons.
     */
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

    /**
     * modify warehouse and strongbox resource based on saved one on Client.
     */
    public static void modifiedResource() {
        if (ClientView.getWarehouse(GUI.getPosition()).get(0).getAmount() == 1)
            SceneController.setImage("#deposit11", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(0).getResource()));
        else
            SceneController.setImage("#deposit11", "");
        if (ClientView.getWarehouse(GUI.getPosition()).get(1).getAmount() >= 1)
            SceneController.setImage("#deposit21", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(1).getResource()));
        else
            SceneController.setImage("#deposit21", "");
        if (ClientView.getWarehouse(GUI.getPosition()).get(1).getAmount() == 2)
            SceneController.setImage("#deposit22", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(1).getResource()));
        else
            SceneController.setImage("#deposit22", "");
        if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() >= 1)
            SceneController.setImage("#deposit31", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
        else
            SceneController.setImage("#deposit31", "");
        if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() >= 2)
            SceneController.setImage("#deposit32", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
        else
            SceneController.setImage("#deposit32", "");
        if (ClientView.getWarehouse(GUI.getPosition()).get(2).getAmount() == 3)
            SceneController.setImage("#deposit33", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(2).getResource()));
        else
            SceneController.setImage("#deposit33", "");
        if (ClientView.getWarehouse(GUI.getPosition()).size() >= 4) {
            String selector1;
            String selector2;
            if(ClientView.isSecondDepot(GUI.getPosition())){
                selector1 = "#extra21";
                selector2 = "#extra22";
            }
            else {
                selector1 = "#extra11";
                selector2 = "#extra12";
            }
            if (ClientView.getWarehouse(GUI.getPosition()).get(3).getAmount() >= 1)
                SceneController.setImage(selector1, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(3).getResource()));
            else
                SceneController.setImage(selector1, "");
            if (ClientView.getWarehouse(GUI.getPosition()).get(3).getAmount() == 2)
                SceneController.setImage(selector2, ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(3).getResource()));
            else
                SceneController.setImage(selector2, "");
        }
        if (ClientView.getWarehouse(GUI.getPosition()).size() == 5) {
            if (ClientView.getWarehouse(GUI.getPosition()).get(4).getAmount() >= 1)
                SceneController.setImage("#extra21", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(4).getResource()));
            else
                SceneController.setImage("#extra21", "");
            if (ClientView.getWarehouse(GUI.getPosition()).get(4).getAmount() == 2)
                SceneController.setImage("#extra22", ResourceMapGUI.getResource(ClientView.getWarehouse(GUI.getPosition()).get(4).getResource()));
            else
                SceneController.setImage("#extra22", "");
        }
        SceneController.setText("#coinAmount", String.valueOf(ClientView.coinAmount(GUI.getPosition())));
        SceneController.setText("#servantAmount", String.valueOf(ClientView.servantAmount(GUI.getPosition())));
        SceneController.setText("#shieldAmount", String.valueOf(ClientView.shieldAmount(GUI.getPosition())));
        SceneController.setText("#stoneAmount", String.valueOf(ClientView.stoneAmount(GUI.getPosition())));
    }

    /**
     * enable all action buttons.
     * in case it's not possible activate or discard leader anymore, disable their buttons.
     */
    public static void yourTurn(){
        SceneController.setDisable("#radiobutTakeMarble", false);
        SceneController.setDisable("#radiobutBuyCard", false);
        SceneController.setDisable("#radiobutActivProduc", false);
        SceneController.setDisable("#radiobutOtherPlayboard", false);
        if(!leaderCardAvailable()){
            SceneController.setDisable("#radiobutActLeader", true);
            SceneController.setDisable("#radiobutDiscardLeader", true);
        }
        else {
            SceneController.setDisable("#radiobutActLeader", false);
            SceneController.setDisable("#radiobutDiscardLeader", false);
        }
        SceneController.setDisable("#radiobutCheat", false);
    }

    /**
     * disable all production buttons.
     */
    public static void disableProductions(){
        SceneController.setVisible("#chooseSlot1", false);
        SceneController.setVisible("#chooseSlot2", false);
        SceneController.setVisible("#chooseSlot3", false);
        SceneController.setVisible("#chooseBase", false);
        SceneController.setVisible("#chooseLeader1", false);
        SceneController.setVisible("#chooseLeader2", false);
    }

    /**
     * disable all action buttons and enable end production button.
     */
    public static void production(){
        SceneController.setDisable("#radiobutTakeMarble", true);
        SceneController.setDisable("#radiobutBuyCard", true);
        SceneController.setDisable("#radiobutActivProduc", true);
        SceneController.setDisable("#radiobutActLeader", true);
        SceneController.setDisable("#radiobutDiscardLeader", true);
        SceneController.setDisable("#radiobutEndProd", false);
        SceneController.setDisable("#radiobutOtherPlayboard", true);
        SceneController.setDisable("#radiobutCheat", true);
    }

    /**
     * disable all action buttons and enable end turn button.
     * in case it's still possible activate or discard leader, enable their buttons.
     */
    public static void endTurn(){
        SceneController.setDisable("#radiobutTakeMarble", true);
        SceneController.setDisable("#radiobutBuyCard", true);
        SceneController.setDisable("#radiobutActivProduc", true);
        SceneController.setDisable("#radiobutEndProd", true);
        SceneController.setDisable("#radiobutEndTurn", false);
        if(!leaderCardAvailable()){
            SceneController.setDisable("#radiobutActLeader", true);
            SceneController.setDisable("#radiobutDiscardLeader", true);
        }
        else {
            SceneController.setDisable("#radiobutActLeader", false);
            SceneController.setDisable("#radiobutDiscardLeader", false);
        }
        SceneController.setDisable("#radiobutCheat", false);
        if(GUI.getNumOfPlayers() > 1){
            SceneController.setVisible("#radiobutOtherPlayboard", true);
            SceneController.setDisable("#radiobutOtherPlayboard", false);
        }
    }

    /**
     * disable all buttons except radiobutOtherPlayboard.
     */
    public static void notYourTurn(){
        SceneController.setDisable("#radiobutTakeMarble", true);
        SceneController.setDisable("#radiobutBuyCard", true);
        SceneController.setDisable("#radiobutActivProduc", true);
        SceneController.setDisable("#radiobutActLeader", true);
        SceneController.setDisable("#radiobutDiscardLeader", true);
        SceneController.setDisable("#radiobutEndTurn", true);
        SceneController.setDisable("#radiobutEndProd", true);
        SceneController.setDisable("#radiobutCheat", true);
    }

    /**
     * disable all buttons except radiobutOtherPlayboard and end game buttons.
     */
    public static void endGame(){
        SceneController.setDisable("#radiobutBuyCard", true);
        SceneController.setDisable("#radiobutTakeMarble", true);
        SceneController.setDisable("#radiobutActivProduc", true);
        SceneController.setDisable("#radiobutActLeader", true);
        SceneController.setDisable("#radiobutDiscardLeader", true);
        SceneController.setDisable("#radiobutEndProd", true);
        SceneController.setDisable("#radiobutEndTurn", true);
        SceneController.setVisible("#radiobutOtherPlayboard", ClientView.getNumOfPlayers() > 1);
        SceneController.setVisible("#radiobutEndGame", true);
        SceneController.setVisible("#radiobutCheat", false);
    }
}
