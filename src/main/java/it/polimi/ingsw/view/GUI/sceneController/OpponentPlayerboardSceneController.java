package it.polimi.ingsw.view.GUI.sceneController;

import it.polimi.ingsw.model.games.states.GameStates;
import it.polimi.ingsw.parser.CardMapGUI;
import it.polimi.ingsw.parser.ResourceMapGUI;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.GUI.GUI;
import it.polimi.ingsw.view.GUI.SceneController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.InputStream;
import java.util.Objects;

public class OpponentPlayerboardSceneController {

    private final int position;

    @FXML
    private Button goBack;
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
    private Label servantAmount;
    @FXML
    private Label shieldAmount;
    @FXML
    private Label stoneAmount;
    @FXML
    private Label coinAmount;
    @FXML
    private ImageView leader1;
    @FXML
    private ImageView leader2;
    @FXML
    private ImageView extraResource11;
    @FXML
    private ImageView extraResource12;
    @FXML
    private ImageView extraResource21;
    @FXML
    private ImageView extraResource22;
    @FXML
    private ImageView pope1;
    @FXML
    private ImageView pope2;
    @FXML
    private ImageView pope3;
    @FXML
    private Label croceRossa;
    @FXML
    private ImageView calamaio;

    public OpponentPlayerboardSceneController(int position){
        this.position = position;
    }

    @FXML
    public void initialize() {
        showPlayerboard();
        goBack.addEventHandler(MouseEvent.MOUSE_CLICKED,mouseEvent -> goBackButton());
    }

    private void goBackButton() {
        Platform.runLater(() -> {
            SceneController.changeRootPane("/fxml/yourTurnScene");
            if (!GUI.isMyTurn())
                YourTurnSceneController.notYourTurn();
            else if(ClientView.isState(GameStates.END_TURN_STATE))
                YourTurnSceneController.endTurn();
            else if(ClientView.isState(GameStates.ACTIVATE_PRODUCTION_STATE))
                YourTurnSceneController.production();
        });
    }

    private void showPlayerboard(){
        setCard(leader1, ClientView.getLeaderCards(position).get(0));
        setCard(leader2, ClientView.getLeaderCards(position).get(1));
        setCard(slot11, ClientView.getSlotCards(position).get(0));
        setCard(slot12, ClientView.getSlotCards(position).get(1));
        setCard(slot13, ClientView.getSlotCards(position).get(2));
        setCard(slot21, ClientView.getSlotCards(position).get(3));
        setCard(slot22, ClientView.getSlotCards(position).get(4));
        setCard(slot23, ClientView.getSlotCards(position).get(5));
        setCard(slot31, ClientView.getSlotCards(position).get(6));
        setCard(slot32, ClientView.getSlotCards(position).get(7));
        setCard(slot33, ClientView.getSlotCards(position).get(8));
        setFirstDepot();
        setSecondDepot();
        setThirdDepot();
        setExtraDepot1();
        setExtraDepot2();
        setStrongbox();
        setFaithPoints();
        setVictoryPoints();
        if(position == 0)
            calamaio.setVisible(true);
    }

    private void setImage(ImageView image, String file){
        if(file.equals(""))
            image.setImage(null);
        else {
            InputStream fis = SceneController.class.getResourceAsStream(file);
            image.setImage(new Image(Objects.requireNonNull(fis)));
        }
    }

    private void setCard(ImageView image, int cardID) {
        if (cardID != -1)
            setImage(image, CardMapGUI.getCard(cardID));
        else
            setImage(image, "");
    }

    private void setFirstDepot() {
        if(ClientView.getWarehouse(position).get(0).getAmount() == 1)
            setImage(deposit11, ResourceMapGUI.getResource(ClientView.getWarehouse(position).get(0).getResource()));
    }

    private void setSecondDepot() {
        if(ClientView.getWarehouse(position).get(1).getAmount() >= 1)
            setImage(deposit21, ResourceMapGUI.getResource(ClientView.getWarehouse(position).get(1).getResource()));
        if(ClientView.getWarehouse(position).get(1).getAmount() == 2)
            setImage(deposit22, ResourceMapGUI.getResource(ClientView.getWarehouse(position).get(1).getResource()));
    }

    private void setThirdDepot() {
        if(ClientView.getWarehouse(position).get(2).getAmount() >= 1)
            setImage(deposit31, ResourceMapGUI.getResource(ClientView.getWarehouse(position).get(2).getResource()));
        if(ClientView.getWarehouse(position).get(2).getAmount() >= 2)
            setImage(deposit32, ResourceMapGUI.getResource(ClientView.getWarehouse(position).get(2).getResource()));
        if(ClientView.getWarehouse(position).get(2).getAmount() == 3)
            setImage(deposit33, ResourceMapGUI.getResource(ClientView.getWarehouse(position).get(2).getResource()));
    }

    private void setExtraDepot1() {
        if(ClientView.getWarehouse(position).size() >= 4){
            if(ClientView.getWarehouse(position).get(3).getAmount() >= 1)
                setImage(extraResource11, ResourceMapGUI.getResource(ClientView.getWarehouse(position).get(3).getResource()));
            if(ClientView.getWarehouse(position).get(3).getAmount() == 2)
                setImage(extraResource12, ResourceMapGUI.getResource(ClientView.getWarehouse(position).get(3).getResource()));
        }
    }

    private void setExtraDepot2() {
        if(ClientView.getWarehouse(position).size() == 5){
            if(ClientView.getWarehouse(position).get(4).getAmount() >= 1)
                setImage(extraResource21, ResourceMapGUI.getResource(ClientView.getWarehouse(position).get(4).getResource()));
            if(ClientView.getWarehouse(position).get(4).getAmount() == 2)
                setImage(extraResource22, ResourceMapGUI.getResource(ClientView.getWarehouse(position).get(4).getResource()));
        }
    }

    private void setStrongbox(){
        coinAmount.setText(String.valueOf(ClientView.coinAmount(position)));
        servantAmount.setText(String.valueOf(ClientView.servantAmount(position)));
        shieldAmount.setText(String.valueOf(ClientView.shieldAmount(position)));
        stoneAmount.setText(String.valueOf(ClientView.stoneAmount(position)));
    }

    private void setFaithPoints() {
        int faithPoints = ClientView.getFaithPoints(position);
        if(faithPoints < 3){
            croceRossa.setLayoutX(50*faithPoints + 80);
        }
        else if (faithPoints < 5) {
            croceRossa.setLayoutX(180);
            croceRossa.setLayoutY(261 - faithPoints*47);
        }
        else if(faithPoints < 10){
            croceRossa.setLayoutX(50*faithPoints - 20);
            croceRossa.setLayoutY(73);
        }
        else if (faithPoints < 12) {
            croceRossa.setLayoutX(430);
            croceRossa.setLayoutY(faithPoints*47 - 350);
        }
        else if(faithPoints < 17){
            croceRossa.setLayoutX(50*faithPoints - 120);
            croceRossa.setLayoutY(167);
        }
        else if(faithPoints < 19){
            croceRossa.setLayoutX(680);
            croceRossa.setLayoutY(919 - 47*faithPoints);
        }
        else {
            if(faithPoints > 24)
                faithPoints = 24;
            croceRossa.setLayoutX(50*faithPoints - 220);
            croceRossa.setLayoutY(73);
        }
    }

    private void setVictoryPoints() {
        int victoryPoints = ClientView.getVictoryPoints(position);
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
}
