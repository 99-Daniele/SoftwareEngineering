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


/**
 * CLI is the command line interface of ClientView.
 * position is player's position in game.
 * turn refers to if it's player's turn or not.
 * connected refers to if player connect to Server or not.
 */
public class GUI extends ClientView {

    private static boolean connected = false;
    private static int position = -1;
    private static boolean turn;

    /**
     * create a new ClientSocket and add this as observer. Then launch a new GUI without any info about server connection.
     */
    @Override
    public void launchGUI() {
        App.createClient(this);
        launch();
    }

    /**
     * create a new ClientSocket and add this as observer. Then launch a new GUI with info about server connection.
     */
    @Override
    public void launchGUI(String hostname, int port){
        App.createClient(this);
        connectToSever(hostname, port);
    }

    public static int getPosition(){
        return position;
    }

    /**
     * @param hostname is Server's hostname.
     * @param port is portNumber.
     *
     * try to connect to Server with user's main parameters and then launch GUI.
     */
    private void connectToSever(String hostname, int port){
        try {
            App.startClient(hostname, port);
            connected = true;
            launch();
        } catch (IOException e) {
            launch();
        }
    }

    /**
     * @param stage is current stage.
     *
     * if Client is connected to Server directly start from NickName scene. Otherwise start from Connection scene.
     */
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

    /**
     * close connections and exit.
     */
    @Override
    public void stop() {
        endGame();
        System.exit(1);
    }

    /**
     * @param message is LOGIN message.
     *
     * if clientID = 0 ask player to chose number of players.
     */
    @Override
    public void loginMessage(Message message) {
        Platform.runLater(() -> {
                if (message.getClientID() == 0) {
                    NicknameSceneController.askNumPlayer();
                } else {
                    NicknameSceneController.waitPlayers();
                }
        });
    }

    @Override
    public void newPlayerMessage(Message message) {}

    /**
     * @param message is PLAYERS message.
     *
     * set position with received one.
     */
    @Override
    public void playersMessage(Message message) {
        super.playersMessage(message);
        GUI.position = message.getClientID();
    }

    /**
     * start game and send a new TURN message to Server.
     */
    @Override
    public void startGameMessage() throws IOException {
        super.startGame();
        ClientSocket.sendMessage(new Message(MessageType.TURN, position));
    }

    /**
     * @param message is LEADER_CHOICE message.
     *
     * go to Init scene and ask player to chose two of the four received LeaderCards.
     */
    @Override
    public void leaderCardChoice(Message message){
        MessageFourParameterInt m = (MessageFourParameterInt) message;
        Platform.runLater(() -> {
            SceneController.changeRootPane("/fxml/initScene");
            InitSceneController.askLeaders(m.getPar1(), m.getPar2(), m.getPar3(), m.getPar4());
        });
    }

    /**
     * if current scene is Nickname scene wait other players.
     * if currentState is BUY_CARD_STATE or END_TURN_STATE set currentState as END_TURN_STATE and disable YourTurnScene buttons.
     * if currentState is FIRST_POWER_STATE or ACTIVATE_PRODUCTION_STATE set currentState as ACTIVATE_PRODUCTION_STATE and
     * enable production buttons.
     * if currentState is TAKE_MARBLE_STATE, ask player to chose another marble or if there aren't any remaining set
     * currentState as END_TURN_STATE.
     * if currentState is WHITE_CONVERSION_STATE go to TAKE_MARBLE_STATE if there are remaining marbles to be chosen,
     * otherwise go to END_TURN_STATE.
     */
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

    /**
     * @param message is TURN message.
     *
     * if param = 1 set turn as true and enable action buttons, otherwise set turn as false.
     */
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

    /**
     * @param message is END_TURN message.
     *
     * if player has just finished it's turn simply set turn as false. Instead if it wasn't player's turn, evaluates if now
     * it's him turn and in case set turn as true and enable action buttons.
     */
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

    /**
     * @param message is BUY_CARD message.
     *
     * if player has bought card, add to his slot.
     * add received message to messages ScrollPane.
     */
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

    /**
     * @param message is CARD_REMOVE message.
     *
     * replace deck card with the new received.
     * add received message to messages ScrollPane.
     */
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

    /**
     * @param message is MARKET_CHANGE message.
     *
     * slide market row or column.
     * add received message to messages ScrollPane.
     */
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

    /**
     * @param message is FAITH_POINTS_INCREASE message.
     *
     * if player has increased faith points (or Ludovico), move red (or black) cross on faith track.
     * add received message to messages ScrollPane.
     */
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

    /**
     * @param message is INCREASE_WAREHOUSE message.
     *
     * if player has increased his warehouse, add new resource in the chosen depot.
     * add received message to messages ScrollPane.
     */
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

    /**
     * @param message is SWITCH_DEPOT message.
     *
     * if player has switched his depots, swap it.
     * add received message to messages ScrollPane.
     */
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

    /**
     * @param message is LEADER_ACTIVATION message.
     *
     * if player has activate a LeaderCard, make LeaderCard opacity as 1, and if necessary switch with the other LeaderCard.
     * add received message to messages ScrollPane.
     */
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

    /**
     * @param message is EXTRA_DEPOT message.
     *
     * add received message to messages ScrollPane.
     */
    @Override
    public void extraDepotMessage(Message message){
        MessageOneIntOneResource m = (MessageOneIntOneResource) message;
        String newMessage = "Player " + getNickname(m.getClientID()) + " has a new extra depot of " + m.getResource().toString();
        super.extraDepotMessage(message);
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> SceneController.addMessage(newMessage));
        addServerMessage(newMessage);
    }

    /**
     * @param message is LEADER_DISCARD message.
     *
     * if player has discard a LeaderCard, make LeaderCard ImageView as null
     * add received message to messages ScrollPane.
     */
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

    /**
     * @param message is TAKE_MARBLE message.
     *
     * set chosen marbles and set currentState as TAKE_MARBLE_STATE. Then ask player to chose one of them.
     */
    @Override
    public void takeMarbleMessage(Message message) {
        super.takeMarbleMessage(message);
        setCurrentState(GameStates.TAKE_MARBLE_STATE);
        Platform.runLater(() -> YourTurnSceneController.chooseMarble());
    }

    /**
     * @param message is END_PRODUCTION message.
     *
     * add received message to messages ScrollPane.
     */
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

    /**
     * @param message is NEW_AMOUNT message.
     *
     * modifies player's warehouse and strongbox based on received amount for resource.
     */
    @Override
    public void resourceAmountMessage(Message message) {
        super.resourceAmountMessage(message);
        if(SceneController.isCurrentScene("#radiobutBuyCard"))
            Platform.runLater(() -> YourTurnSceneController.modifiedResource());
    }

    /**
     * @param message is WHITE_CONVERSION message.
     *
     * ask player to chose one of his two active WhiteConversionCard.
     */
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

    /**
     * @param message is VATICAN_REPORT message.
     *
     * add right vatican report tile on player's faithTrack based on his victory points.
     * add received message to messages ScrollPane.
     */
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

    /**
     * @param message is CHOSEN_SLOT message.
     *
     * ask player to chose one available slot for insert DevelopmentCard.
     */
    @Override
    public void chosenSlotMessage(Message message){
        MessageThreeParameterInt m = (MessageThreeParameterInt) message;
        YourTurnSceneController.choseSlotMessage(m.getPar1(), m.getPar2(), m.getPar3());
    }

    /**
     * @param message is a QUIT message.
     *
     * this message could be received if another player disconnect or if Client disconnect from Server.
     * in case game already started, display a disconnection message.
     * in case game is not started yet, do nothing.
     * disable all buttons except radiobutOtherPlayboard and make end game button visible.
     */
    @Override
    public void quitMessage(Message message) {
        MessageOneParameterString m = (MessageOneParameterString) message;
        String quitMessage;
        if(m.getClientID() == -1)
            quitMessage = "Client no longer connected to the Server.";
        else if (getNumOfPlayers() != 0)
            quitMessage = "Player " + m.getPar() + " disconnected. Game ended.";
        else
            return;
        Platform.runLater(() -> quitPhase(quitMessage));
    }

    /**
     * @param message is a END_GAME message.
     *
     * display info about winner victory points and resource.
     * disable all buttons except radiobutOtherPlayboard and make end game button visible.
     */
    @Override
    public void
    endGameMessage(Message message) {
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

    /**
     * ask player to chose another nickName.
     */
    @Override
    public void alreadyTakenNickNameError() {
        Platform.runLater(() -> NicknameSceneController.alreadyTakenNickName());
    }

    /**
     * inform player has sent wrong parameters to Server.
     */
    @Override
    public void wrongParametersError() {
        String errorMessage = "You have inserted wrong parameters";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    /**
     * inform player it's not his turn.
     */
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

    /**
     * inform player has chosen an empty card deck.
     */
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

    /**
     * inform player has chosen an empty slot for card production.
     * if currentState it's FIRST_POWER_STATE return to FIRST_ACTION_STATE and enable actions button.
     */
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

    /**
     * inform player has chosen a wrong production power, such as leader card power of a not AdditionalProductionPowerCard
     * or an inactive LeaderCard, or choosing one production power for the second time.
     * if currentState it's FIRST_POWER_STATE return to FIRST_ACTION_STATE and enable actions button.
     */
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

    /**
     * inform player that he can't activate the chosen LeaderCard.
     */
    @Override
    public void notEnoughCardsError() {
        String errorMessage = "You don't have enough development cards to activate this leader card";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    /**
     * inform player that he can't insert selected DevelopmentCard in any slot.
     * set currentState as FIRST_ACTION_STATE.
     */
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

    /**
     * inform player that he can't perform selected operation at this moment.
     */
    @Override
    public void illegalOperationError() {
        String errorMessage = "You can't do this operation at this moment";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    /**
     * inform player that he can't switch selected depots.
     */
    @Override
    public void impossibleSwitchError() {
        String errorMessage = "You can't switch this depots";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    /**
     * inform player that he hasn't enough resource to perform selected operation.
     * if currentState is BUY_CARD_STATE return to FIRST_ACTION_STATE.
     * if currentState is FIRST_POWER_STATE, disable all production buttons and return to FIRST_ACTION_STATE.
     */
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

    /**
     * inform player that he tried to activate or discard an active LeaderCard.
     */
    @Override
    public void alreadyActiveError() {
        String errorMessage = "You activated this leader card previously";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    /**
     * inform player that he tried to activate or discard a discarded LeaderCard.
     */
    @Override
    public void alreadyDiscardError() {
        String errorMessage = "You discard this leader card previously";
        Platform.runLater(() -> {
            if(!SceneController.isCurrentScene("#radiobutBuyCard"))
                SceneController.changeRootPane("/fxml/yourTurnScene");
            SceneController.errorMessage(errorMessage);
        });
    }

    /**
     * @param quitMessage is the message which is displayed.
     *
     * set currentState as END_GAME_STATE, close connection with Server and disable all action buttons, except radiobutOtherPlayboard.
     * then make end game button visible, which will exit after being clicked.
     */
    private static void quitPhase(String quitMessage) {
        setCurrentState(GameStates.END_GAME_STATE);
        endGame();
        SceneController.changeRootPane("/fxml/yourTurnScene");
        SceneController.errorMessage(quitMessage);
        YourTurnSceneController.endGame();
    }
}
