package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.App;
import it.polimi.ingsw.model.games.states.GameStates;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.ClientView;
import it.polimi.ingsw.view.GUI.sceneController.InitSceneController;
import it.polimi.ingsw.view.GUI.sceneController.NicknameSceneController;
import it.polimi.ingsw.view.GUI.sceneController.YourTurnSceneController;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;


public class GUI extends ClientView {

    private static boolean connected = false;
    private static int position = -1;
    private static boolean turn;

    @Override
    public void launchGUI() {
        App.createClient(this);
        launch();
    }

    @Override
    public void launchGUI(String hostname, int port){
        App.createClient(this);
        connectToSever(hostname, port);
    }

    public static int getPosition(){
        return position;
    }

    private void connectToSever(String hostname, int port){
        try {
            App.startClient(hostname, port);
            connected = true;
            launch();
        } catch (IOException e) {
            launch();
        }
    }

    @Override
    public void start(Stage stage) {
        Platform.runLater(() -> {
            try {
                if (connected) {
                    SceneController.setScene(stage, "/fxml/nicknameScene");
                } else {
                    SceneController.setScene(stage, "/fxml/connectionScene");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void stop() {
        endGame();
    }

    @Override
    public void loginMessage(Message message) {
        Platform.runLater(() -> {
            try {
                if (message.getClientID() == 0) {
                    NicknameSceneController.askNumPlayer();
                } else {
                    NicknameSceneController.waitPlayers();
                }
            } catch (NullPointerException ignored){}
        });
    }

    @Override
    public void newPlayerMessage(Message message) {
    }

    @Override
    public void playersMessage(Message message) {
        super.playersMessage(message);
        GUI.position = message.getClientID();
    }

    @Override
    public void startGameMessage() throws IOException {
        super.startGame();
        ClientSocket.sendMessage(new Message(MessageType.TURN, position));
    }

    @Override
    public void leaderCardChoice(Message message){
        MessageFourParameterInt m = (MessageFourParameterInt) message;
        Platform.runLater(() -> {
            SceneController.changeRootPane("/fxml/initScene");
            InitSceneController.askLeaders(m.getPar1(), m.getPar2(), m.getPar3(), m.getPar4());
        });
    }

    @Override
    public void okMessage() {
        Platform.runLater(() -> {
            if (SceneController.isCurrentScene("#nicknameLabel"))
                NicknameSceneController.waitPlayers();
            else if(isState(GameStates.BUY_CARD_STATE) || isState(GameStates.END_TURN_STATE)) {
                setCurrentState(GameStates.END_TURN_STATE);
                YourTurnSceneController.endTurn();
            }
            else if(isState(GameStates.FIRST_POWER_STATE) || ClientView.isState(GameStates.ACTIVATE_PRODUCTION_STATE)){
                setCurrentState(GameStates.ACTIVATE_PRODUCTION_STATE);
                YourTurnSceneController.production();
            }
            else if(isState(GameStates.TAKE_MARBLE_STATE)){
                if(getMarbles().size() == 0){
                    SceneController.setVisible("#message", false);
                    SceneController.setVisible("#marbleShow1", false);
                    setCurrentState(GameStates.END_TURN_STATE);
                    YourTurnSceneController.endTurn();
                }
                else
                    YourTurnSceneController.chooseMarble();
            }
            else if(isState(GameStates.WHITE_CONVERSION_CARD_STATE)){
                if(getMarbles().size() == 0){
                    setCurrentState(GameStates.END_TURN_STATE);
                    YourTurnSceneController.endTurn();
                }
                else {
                    setCurrentState(GameStates.TAKE_MARBLE_STATE);
                    YourTurnSceneController.chooseMarble();
                }
            }
        });
    }

    @Override
    public void turnMessage(Message message){
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        setCurrentState(GameStates.FIRST_ACTION_STATE);
        Platform.runLater(() -> {
            turn = (m.getPar() == 1);
            if (m.getPar() == 1) {
                SceneController.changeRootPane("/fxml/yourTurnScene");
            }
            else {
                SceneController.changeRootPane("/fxml/yourTurnScene");
                YourTurnSceneController.notYourTurn();
            }
        });
    }

    @Override
    public void endTurnMessage(Message message) {
        if (position == 0 && message.getClientID() == getNumOfPlayers() -1) {
            setCurrentState(GameStates.FIRST_ACTION_STATE);
            turn = true;
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> {
                            YourTurnSceneController.yourTurn();
                            if (getNumOfPlayers() > 1)
                                SceneController.errorMessage("It's your turn");
                        }
                );
        } else if (message.getClientID() == position - 1) {
            setCurrentState(GameStates.FIRST_ACTION_STATE);
            turn = true;
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> {
                    YourTurnSceneController.yourTurn();
                    SceneController.errorMessage("It's your turn");
                });
        } else {
            turn = false;
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> {
                    YourTurnSceneController.notYourTurn();
                    if(message.getClientID() != position)
                        SceneController.errorMessage("Player " + getNickname(message.getClientID()) + " has finished his turn.");
                });
        }
    }

    public static boolean isMyTurn() {
        return turn;
    }

    @Override
    public void buyCardMessage(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        String newMessage = "Player " + getNickname(m.getClientID()) + " bought a new card and inserted it in " +
                "the " + m.getPar2() + "° slot.";
        super.buyCardMessage(message);
        if(m.getClientID() != GUI.getPosition()) {
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.buyCardMessage(m.getPar2(), m.getPar1(), newMessage));
        addServerMessage(newMessage);
    }

    @Override
    public void cardRemoveMessage(Message message){
        MessageFourParameterInt m = (MessageFourParameterInt) message;
        String newMessage;
        super.cardRemoveMessage(message);
        if(getNumOfPlayers() == 1 && m.getClientID() == 1)
            newMessage = "Ludovico has removed one deck card from row " + (m.getPar1() +1 + " and" + " column " + (m.getPar2() +1));
        else
            newMessage = "Deck card from row " + (m.getPar1() + 1) + " and column " + (m.getPar2() + 1) + " has been removed";
        if (SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.cardRemoveMessage(m.getPar1(), m.getPar2(), m.getPar4(), newMessage));
        addServerMessage(newMessage);
    }

    @Override
    public void marketChange(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        String newMessage;
        if (m.getPar1() == 0) {
            newMessage = "Player " + getNickname(m.getClientID()) + " has chosen row " + m.getPar2() + " of the market";
        } else {
            newMessage = "Player " + getNickname(m.getClientID()) + " has chosen column " + m.getPar2() + " of the market";
        }
        super.marketChange(message);
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.marketChangeMessage(m.getPar1() == 0, m.getPar2(), newMessage));
        addServerMessage(newMessage);
    }

    @Override
    public void faithPointsMessage(Message message) {
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        String newMessage;
        if (getNumOfPlayers() == 1 && m.getClientID() == 1)
            newMessage = "Ludovico has increased his faith points. Now it has " + m.getPar();
        else
            newMessage = "Player " + getNickname(m.getClientID()) + " has increased its faith points";
        super.faithPointsMessage(message);
        if(m.getClientID() == position){
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.increaseFaithPointsMessage(m.getPar(), false, newMessage));
        }
        else if (GUI.getNumOfPlayers() > 1) {
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else if (GUI.getNumOfPlayers() == 1) {
            if (SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.increaseFaithPointsMessage(m.getPar(), true, newMessage));
        }
        addServerMessage(newMessage);
    }

    @Override
    public void increaseWarehouseMessage(Message message){
        MessageOneIntOneResource m = (MessageOneIntOneResource) message;
        String newMessage;
        if(m.getPar1() != -1)
            newMessage = "Player " + getNickname(m.getClientID()) + " has inserted 1 " + m.getResource().toString()
                + " in its " + m.getPar1() + "° depot";
        else
            newMessage = "Player " + getNickname(m.getClientID()) + " has discarded 1 marble";
        super.increaseWarehouseMessage(message);
        if(m.getPar1() == -1 || m.getClientID() != position){
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else{
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.increaseWarehouseMessage(m.getPar1(), newMessage));
        }
        addServerMessage(newMessage);
    }

    @Override
    public void switchDepotMessage(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        String newMessage = "Player " + getNickname(m.getClientID()) + " has switched its " + m.getPar1()
                + "° depot with its " + m.getPar2() + "° depot";
        super.switchDepotMessage(message);
        if(m.getClientID() != position) {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.switchDepotMessage(m.getPar1(), m.getPar2(), newMessage));
        }
        addServerMessage(newMessage);
    }

    @Override
    public void leaderCardActivationMessage(Message message){
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        String newMessage = "Player " + getNickname(m.getClientID()) + " has activated a leader card";
        super.leaderCardActivationMessage(message);
        if(m.getClientID() != position) {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.leaderCardActivationMessage(newMessage));
        }
        addServerMessage(newMessage);
    }

    @Override
    public void extraDepotMessage(Message message){
        MessageOneIntOneResource m = (MessageOneIntOneResource) message;
        String newMessage = "Player " + getNickname(m.getClientID()) + " has a new extra depot of " + m.getResource().toString();
        super.extraDepotMessage(message);
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> SceneController.addMessage(newMessage));
        addServerMessage(newMessage);
    }

    @Override
    public void leaderCardDiscardMessage(Message message){
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        String newMessage = "Player " + getNickname(m.getClientID()) + " has discarded one leader card";
        super.leaderCardDiscardMessage(message);
        if(m.getClientID() != position) {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> SceneController.addMessage(newMessage));
        }
        else {
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.leaderCardDiscardMessage(newMessage));
        }
        addServerMessage(newMessage);
    }

    @Override
    public void takeMarbleMessage(Message message) {
        MessageArrayListMarble m = (MessageArrayListMarble) message;
        super.takeMarbleMessage(message);
        setCurrentState(GameStates.TAKE_MARBLE_STATE);
        Platform.runLater(() -> YourTurnSceneController.showMarbles(m.getMarbles()));
    }

    @Override
    public void endProductionMessage(Message message) {
        String newMessage = "Player " + getNickname(message.getClientID()) + " has activated production powers";
        setCurrentState(GameStates.END_TURN_STATE);
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> {
                SceneController.addMessage(newMessage);
                YourTurnSceneController.endTurn();
            });
        addServerMessage(newMessage);
    }

    @Override
    public void resourceAmountMessage(Message message) {
        super.resourceAmountMessage(message);
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.modifiedResource());
    }

    @Override
    public void whiteConversionCardMessage(Message message){
        ClientView.setCurrentState(GameStates.WHITE_CONVERSION_CARD_STATE);
        SceneController.setVisible("#message", false);
        SceneController.setVisible("#marbleShow1", false);
        SceneController.setVisible("#marbleShow2", false);
        SceneController.setVisible("#marbleShow3", false);
        SceneController.setVisible("#marbleShow4", false);
        YourTurnSceneController.whiteConversionMessage();
    }

    @Override
    public void vaticanReportMessage(Message message) {
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        String newMessage;
        super.vaticanReportMessage(message);
        if(getNumOfPlayers() == 1 && m.getPar1() == 1) {
            newMessage = "Ludovico activated Vatican Report";
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.vaticanReportMessage(newMessage));
            addServerMessage(newMessage);
        }
        else if(message.getClientID() == position) {
            newMessage = "Player " + getNickname(m.getPar1()) + " activated Vatican Report";
            if(SceneController.isCurrentScene("#radiobutBuyCard"))
                Platform.runLater(() -> YourTurnSceneController.vaticanReportMessage(newMessage));
            addServerMessage(newMessage);
        }
    }

    @Override
    public void chosenSlotMessage(Message message){
        MessageThreeParameterInt m = (MessageThreeParameterInt) message;
        YourTurnSceneController.choseSlotMessage(m.getPar1(), m.getPar2(), m.getPar3());
    }

    @Override
    public void quitMessage(Message message) {
        MessageOneParameterString m = (MessageOneParameterString) message;
        String quitMessage;
        if (getNumOfPlayers() != 0) {
            quitMessage = "Player " + m.getPar() + " disconnected. Game ended.";
        }
        else
            return;
        Platform.runLater(() -> quitPhase(quitMessage));
    }

    @Override
    public void endGameMessage(Message message) {
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        String endMessage;
        if(getNumOfPlayers() == 1 && m.getClientID() == 1){
            endMessage = "Game ended. Ludovico win.";
        }
        else {
            endMessage = "Game ended. " + getNickname(m.getClientID()) + " win the game.\n"
                    + m.getPar1() + " victory points and " + m.getPar2()
                    + " total resources.";
        }
        Platform.runLater(() -> quitPhase(endMessage));
    }

    @Override
    public void alreadyTakenNickNameError() {
        Platform.runLater(() -> NicknameSceneController.alreadyTakenNickName());
    }

    @Override
    public void wrongParametersError() {
        String errorMessage = "You have inserted wrong parameters";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    @Override
    public void wrongTurnError(){
        String errorMessage = "It's not your turn";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
            YourTurnSceneController.notYourTurn();
        });
    }

    @Override
    public void emptyDeckError() {
        String errorMessage = "You have chosen an empty deck";
        setCurrentState(GameStates.FIRST_ACTION_STATE);
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
            YourTurnSceneController.yourTurn();
        });
    }

    @Override
    public void emptySlotError() {
        String errorMessage = "You have no cards in this slot";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
            if(isState(GameStates.FIRST_POWER_STATE)){
                setCurrentState(GameStates.FIRST_ACTION_STATE);
                YourTurnSceneController.disableProductions();
                YourTurnSceneController.yourTurn();
            }
            else if(isState(GameStates.ACTIVATE_PRODUCTION_STATE)){
                YourTurnSceneController.production();
            }
        });
    }

    @Override
    public void wrongPowerError() {
        String errorMessage = "You can't activate this production power";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
            if(isState(GameStates.FIRST_POWER_STATE)){
                setCurrentState(GameStates.FIRST_ACTION_STATE);
                YourTurnSceneController.disableProductions();
                YourTurnSceneController.yourTurn();
            }
            else {
                YourTurnSceneController.production();
            }
        });
    }

    @Override
    public void notEnoughCardsError() {
        String errorMessage = "You don't have enough development cards to activate this leader card";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    @Override
    public void fullSlotError() {
        String errorMessage = "You can't insert this card in any slot";
        setCurrentState(GameStates.FIRST_ACTION_STATE);
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
            YourTurnSceneController.yourTurn();
        });
    }

    @Override
    public void illegalOperationError() {
        String errorMessage = "You can't do this operation at this moment";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    @Override
    public void impossibleSwitchError() {
        String errorMessage = "You can't switch this depots";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    @Override
    public void notEnoughResourceError() {
        String errorMessage = "You have not enough resources to do this operation";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
            if(isState(GameStates.BUY_CARD_STATE)) {
                setCurrentState(GameStates.FIRST_ACTION_STATE);
                YourTurnSceneController.yourTurn();
            }
            else if(isState(GameStates.FIRST_POWER_STATE)){
                setCurrentState(GameStates.FIRST_ACTION_STATE);
                YourTurnSceneController.disableProductions();
                YourTurnSceneController.yourTurn();
            }
            else if(isState(GameStates.ACTIVATE_PRODUCTION_STATE)){
                YourTurnSceneController.production();
            }
        });
    }

    @Override
    public void alreadyActiveError() {
        String errorMessage = "You activated this leader card previously";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    @Override
    public void alreadyDiscardError() {
        String errorMessage = "You discard this leader card previously";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    private void quitPhase(String quitMessage) {
        SceneController.changeRootPane("/fxml/yourTurnScene");
        SceneController.errorMessage(quitMessage);
        SceneController.setDisable("#radiobutBuyCard", true);
        SceneController.setDisable("#radiobutTakeMarble", true);
        SceneController.setDisable("#radiobutActivProduc", true);
        SceneController.setDisable("#radiobutActLeader", true);
        SceneController.setDisable("#radiobutDiscardLeader", true);
        SceneController.setDisable("#radiobutEndProd", true);
        SceneController.setDisable("#radiobutEndTurn", true);
        SceneController.setDisable("#radiobutOtherPlayboard", super.isGameStarted());
        SceneController.setVisible("#radiobutEndGame", true);
    }
}
