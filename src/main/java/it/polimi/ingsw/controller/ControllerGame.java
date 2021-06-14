package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.exceptions.IllegalStateException;
import it.polimi.ingsw.model.games.GameManager;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.*;

import java.util.*;

/**
 * ControllerGame controls the first phases of the game when players are logging in, and coordinates model and view.
 * ControllerGame observes View and update when View receives message from player.
 */
public class ControllerGame implements Observer {

    private GameManager gameManager;
    private TurnController turnController;
    private LinkedList<View> views;
    private GameStartingState state;
    private final ArrayList<ArrayList<LeaderCard>> leaderCards;

    public ControllerGame() {
        views = new LinkedList<>();
        state = GameStartingState.WAITING_NUM_PLAYERS;
        leaderCards = new ArrayList<>();
    }

    /**
     * When all players disconnected, ControllerGame is reset to the initial configuration.
     */
    public void resetControllerGame(){
        if(gameManager != null)
            System.out.println("GAME ENDED");
        gameManager = null;
        views = new LinkedList<>();
        state = GameStartingState.WAITING_NUM_PLAYERS;
        ControllerConnection.newGame();
    }

    /**
     * @return the chosen number of players, or @return 0 if is not yet been chosen.
     */
    public int getMaxNumPlayers() {
        if(gameManager != null)
            return gameManager.getNumOfPlayers();
        return 0;
    }

    /**
     * @return how many players are connected at this moment.
     */
    public int getCurrentNumPlayers() {
        return views.size();
    }

    /**
     * @param view is a new view tries to connect to ControllerGame.
     * @throws FullGameException if Game has fulled its number of players.
     * add @param view to views, and if player has already chose his nickName, create a new player.
     */
    public synchronized void addView(View view) throws FullGameException{
        if(gameManager != null && views.size() == gameManager.getNumOfPlayers())
            throw new FullGameException();
        views.add(view);
        if(view.getNickname() != null){
            addPlayer(view, view.getNickname());
        }
    }

    /**
     * @param view is the view of disconnecting player.
     * @param nickName is the nickName of the player.
     * delete player from Game and from views, and if there are no remaining views, reset ControllerGame.
     */
    public synchronized void removeView(View view, String nickName) {
        try {
            gameManager.deletePlayer(nickName);
            views.remove(view);
            if(views.size() == 0){
                resetControllerGame();
            }
        } catch (NullPointerException e) {
            resetControllerGame();
        }
    }

    /**
     * @param view is the view of disconnecting player.
     * @param nickName is the nickName of the player.
     * if player disconnect before game started, only informs other player. Instead, if player has already started,
     * informs player to disconnect.
     */
    public synchronized void quitGame(View view, String nickName) {
        if(nickName == null){
            views.remove(view);
            if(views.size() == 0)
                resetControllerGame();
        }
        else if (views.size() == 1) {
            removeView(view, nickName);
            resetControllerGame();
        }
        else if (state != GameStartingState.START_GAME) {
            removeView(view, nickName);
            for (View otherView : views)
                otherView.exit(nickName);
        }
        else {
            removeView(view, nickName);
            for (View otherView : views)
                otherView.quit(nickName);
            resetControllerGame();
        }
    }

    /**
     * @param o is one view observed by ControllerGame.
     * @param arg is one message from player.
     * updates when player send one message to Server. If game is already started, firstly valuates if its effectively player's
     * turn, and then proceeds to handle his request.
     */
    @Override
    public void update(Observable o, Object arg) {
        Message m = (Message) arg;
        int viewID = m.getClientID();
        try {
            if (m.getMessageType() == MessageType.LOGIN) {
                addPlayer((View) o, m);
                return;
            }
            if (m.getMessageType() == MessageType.NUM_PLAYERS) {
                newGame(m);
                return;
            }
            if(m.getMessageType() == MessageType.LEADER_CARD){
                leaderCardHandler(m);
                return;
            }
            if(m.getMessageType() == MessageType.ONE_FIRST_RESOURCE){
                oneResourceHandle(m);
                return;
            }
            if(m.getMessageType() == MessageType.TWO_FIRST_RESOURCE){
                twoResourceHandle(m);
                return;
            }
            if (m.getMessageType() == MessageType.TURN) {
                isMyTurn(m);
                return;
            }
            if (!turnController.isMyTurn(viewID)) {
                errorHandler(ErrorType.NOT_YOUR_TURN, viewID);
                return;
            }
            switch (m.getMessageType()) {
                case TAKE_MARBLE:
                    takeMarbleHandler(m);
                    break;
                case USE_MARBLE:
                    useMarbleHandler(m);
                    break;
                case WHITE_CONVERSION_CARD:
                    whiteConversionCardHandler(m);
                    break;
                case SWITCH_DEPOT:
                    switchHandler(m);
                    break;
                case BUY_CARD:
                    buyCardHandler(m);
                    break;
                case CHOSEN_SLOT:
                    chosenSlotHandler(m);
                    break;
                case DEVELOPMENT_CARD_POWER:
                    developmentCardPowerHandler(m);
                    break;
                case BASIC_POWER:
                    basicPowerHandler(m);
                    break;
                case LEADER_CARD_POWER:
                    leaderCardPowerHandler(m);
                    break;
                case END_PRODUCTION:
                    endProductionHandler(m);
                    break;
                case LEADER_CARD_ACTIVATION:
                    leaderActivationHandler(m);
                    break;
                case LEADER_CARD_DISCARD:
                    leaderDiscardHandler(m);
                    break;
                case END_TURN:
                    endTurnHandler();
                    break;
                default:
                    views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                    break;
            }
        } catch (ClassCastException | WrongParametersException e) {
            errorHandler(ErrorType.WRONG_PARAMETERS, viewID);
        } catch (IllegalStateException illegalStateException) {
            errorHandler(ErrorType.ILLEGAL_OPERATION, viewID);
        } catch (EmptyDevelopmentCardDeckException e) {
            errorHandler(ErrorType.EMPTY_DECK, viewID);
        } catch (ImpossibleDevelopmentCardAdditionException e) {
            errorHandler(ErrorType.FULL_SLOT, viewID);
        } catch (InsufficientResourceException e) {
            errorHandler(ErrorType.NOT_ENOUGH_RESOURCES, viewID);
        } catch (AlreadyDiscardLeaderCardException e) {
            errorHandler(ErrorType.ALREADY_DISCARD_LEADER_CARD, viewID);
        } catch (ImpossibleSwitchDepotException e) {
            errorHandler(ErrorType.IMPOSSIBLE_SWITCH, viewID);
        } catch (InsufficientCardsException e) {
            errorHandler(ErrorType.NOT_ENOUGH_CARDS, viewID);
        } catch (ActiveLeaderCardException e) {
            errorHandler(ErrorType.ALREADY_ACTIVE_LEADER_CARD, viewID);
        }
    }

    /**
     * @param view is player's view trying to logging in.
     * @param loginMessage is one message of LOGIN.
     */
    private synchronized void addPlayer(View view, Message loginMessage){
        try {
            MessageOneParameterString m = (MessageOneParameterString) loginMessage;
            String nickName = m.getPar();
            addPlayer(view, nickName);
        } catch (ClassCastException e) {
            view.errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    /**
     * @param view is player's view trying to logging in.
     * @param nickName is player's chosen nickName.
     */
    private synchronized void addPlayer(View view, String nickName){
        if (state != GameStartingState.WAITING_PLAYERS && state != GameStartingState.WAITING_NUM_PLAYERS) {
            view.errorMessage(ErrorType.ILLEGAL_OPERATION);
            return;
        }
        if (!InputController.loginCheck(nickName)) {
            view.errorMessage(ErrorType.WRONG_PARAMETERS);
            return;
        }
        try {
            createNewPlayer(view, nickName);
        } catch (AlreadyTakenNicknameException e) {
            view.errorMessage(ErrorType.ALREADY_TAKEN_NICKNAME);
        }
    }

    /**
     * @param view is player's view trying to logging in.
     * @param nickName is player's chosen nickName.
     * @throws AlreadyTakenNicknameException if nickName was already taken by another player.
     * If it's the first player, simply send a LOGIN message with clientID as 0. Otherwise create the player and then
     * sends a login message to player.
     */
    private synchronized void createNewPlayer(View view, String nickName) throws AlreadyTakenNicknameException {
        if (state == GameStartingState.WAITING_NUM_PLAYERS) {
            view.login(0);
        } else {
            gameManager.createPlayer(view, nickName);
            view.login(views.size() - 1);
        }
    }

    /**
     * @param numPlayerMessage is a message of NUM_PLAYERS.
     * @throws WrongParametersException if player has chosen a wrong number of players.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     * creates a new GameManager and a new TurnController of @param numPlayers and adds the first player to game.
     */
    public synchronized void newGame(Message numPlayerMessage)
            throws WrongParametersException, IllegalStateException{
        MessageOneParameterInt m = (MessageOneParameterInt) numPlayerMessage;
        int numPlayers = m.getPar();
        if (!InputController.numPlayersCheck(numPlayers))
            throw new WrongParametersException();
        if (state != GameStartingState.WAITING_NUM_PLAYERS)
            throw new IllegalStateException();
        else {
            try {
                gameManager = new GameManager(numPlayers, this);
                turnController = new TurnController(numPlayers);
                gameManager.createPlayer(views.get(0), views.get(0).getNickname());
                if (numPlayers > 1) {
                    state = GameStartingState.WAITING_PLAYERS;
                }
                ControllerConnection.newGame();
                views.get(0).ok();
            } catch (AlreadyTakenNicknameException ignored) {}
        }
    }

    /**
     * rearranges views like players in game. Then informs player that all players connected and tells them their position.
     */
    public void startGame(){
        LinkedList<View> newViewsPosition = new LinkedList<>();
        for(int i= 0; i < views.size(); i++){
            String nickName = gameManager.getPlayerPosition(i);
            View selectedView = null;
            for (View view: views)
                if(view.getNickname().equals(nickName))
                    selectedView = view;
            newViewsPosition.add(selectedView);
        }
        views = newViewsPosition;
        System.out.println("NEW GAME STARTED -> PLAYERS: " + views);
        state = GameStartingState.WAITING_PLAYERS_CHOICES;
    }

    /**
     * @param player is one player.
     * @param numPlayers is the num of players.
     * @param nickNames are players nickNames.
     * sends to each players an ordered list of nickNames of other player.
     */
    public void sendNickNames(int player, int numPlayers, ArrayList<String> nickNames){
        views.get(player).allPlayerConnected(player, nickNames);
    }

    /**
     * @param player is one player.
     * @param leaderCards are 4 casual leaderCards.
     * sends to each player 4 different casual leaderCards.
     */
    public void sendLeaderCards(int player, ArrayList<LeaderCard> leaderCards){
        this.leaderCards.add(leaderCards);
        views.get(player).choseLeaderCards(leaderCards);
    }

    /**
     * @param m is one TURN message.
     * answers player with a new TURN message specifying if it's his turn or not.
     */
    public void isMyTurn(Message m) {
        int viewID = m.getClientID();
        views.get(viewID).isMyTurn(turnController.isMyTurn(viewID));
    }

    /**
     * sends to all players that each one of them have made all choices. Now game could start.
     */
    public void playersReady(){
        state = GameStartingState.START_GAME;
        System.out.println("All players have made their choice.");
        for(View view: views)
            view.startGame();
    }

    /**
     * activates the end game procedure.
     */
    public void endGame() {
        turnController.endGame();
    }

    /**
     * @param message is a message with player's choices about which LeaderCards chose.
     * @throws WrongParametersException if player has chosen a wrong leader cards.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     */
    private void leaderCardHandler(Message message) throws IllegalStateException, WrongParametersException {
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        int viewID = m.getClientID();
        if(state != GameStartingState.WAITING_PLAYERS_CHOICES)
            throw new IllegalStateException();
        if(!InputController.leaderCardCheck(m.getPar1(), m.getPar2(),  leaderCards.get(viewID)))
            throw new WrongParametersException();
        gameManager.leaderCardHandler(viewID, m.getPar1(), m.getPar2(), leaderCards.get(viewID));
        views.get(viewID).ok();
    }

    /**
     * @param message is message with player's chosen first resource.
     * @throws IllegalStateException if player has send this message during a wrong phases or if this message has been
     * sent by the first or fourth player.
     */
    private void oneResourceHandle(Message message) throws IllegalStateException {
        MessageOneIntOneResource m = (MessageOneIntOneResource) message;
        int viewID = m.getClientID();
        if(viewID != 1 && viewID != 2 || state != GameStartingState.WAITING_PLAYERS_CHOICES)
            throw new IllegalStateException();
        gameManager.oneResourceHandle(viewID, m.getResource());
        views.get(viewID).ok();
    }

    /**
     * @param message is message with player's chosen first two resource.
     * @throws IllegalStateException if player has send this message during a wrong phases or if this message has been
     * sent by the first, second or third player.
     */
    private void twoResourceHandle(Message message) throws IllegalStateException {
        MessageTwoResource m = (MessageTwoResource) message;
        int viewID = m.getClientID();
        if(viewID != 3 || state != GameStartingState.WAITING_PLAYERS_CHOICES)
            throw new IllegalStateException();
        gameManager.twoResourceHandle(viewID, m.getR1(), m.getR2());
        views.get(viewID).ok();
    }

    /**
     * @param message is a BUY_CARD message.
     * @throws WrongParametersException if player has chosen a wrong deck card.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     * @throws EmptyDevelopmentCardDeckException if player has chosen an empty deck.
     * @throws ImpossibleDevelopmentCardAdditionException if player can't add chosen card to his slots.
     * @throws InsufficientResourceException if player has not enough resources.
     */
    private void buyCardHandler(Message message)
            throws IllegalStateException, WrongParametersException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, InsufficientResourceException {
        MessageThreeParameterInt m = (MessageThreeParameterInt) message;
        int viewID = m.getClientID();
        if(gameManager.buyCardHandler(m.getPar1(), m.getPar2(), m.getPar3()))
            views.get(viewID).ok();
    }

    /**
     * @param currentPlayer is the player that have the turn.
     * @param slots are a list of available slots.
     */
    public void choseSlot(int currentPlayer, ArrayList<Integer> slots){
        views.get(currentPlayer).availableSlot(slots);
    }

    /**
     * @param message is a CHOSEN_SLOT message.
     * @throws WrongParametersException if player has chosen a wrong card slot.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     * @throws EmptyDevelopmentCardDeckException if player has chosen an empty deck.
     * @throws ImpossibleDevelopmentCardAdditionException if player can't add chosen card to his slots.
     * @throws InsufficientResourceException if player has not enough resources.
     */
    private void chosenSlotHandler(Message message)
            throws IllegalStateException, WrongParametersException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, InsufficientResourceException {
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        int viewID = m.getClientID();
        int chosenSlot = m.getPar();
        gameManager.chosenSlotHandler(chosenSlot);
        views.get(viewID).ok();
    }

    /**
     * @param message is a TAKE_MARBLE message.
     * @throws WrongParametersException if player has chosen a wrong market row or column.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     */
    private void takeMarbleHandler(Message message) throws IllegalStateException, WrongParametersException {
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        int viewID = m.getClientID();
        Marble[] marbles = gameManager.takeMarbleHandler(m.getPar1(), m.getPar2());
        views.get(viewID).chosenMarble(marbles);
    }

    /**
     * @param message is a USE_MARBLE message
     * @throws WrongParametersException if player has chosen a wrong number of players.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     */
    private void useMarbleHandler(Message message)
            throws IllegalStateException, WrongParametersException {
        MessageOneParameterMarble m = (MessageOneParameterMarble) message;
        int viewID = m.getClientID();
        if(gameManager.useMarbleHandler(m.getMarble()))
            views.get(viewID).ok();
    }

    /**
     * @param currentPlayer is the player that have the turn.
     * @param leaderCards are @param current player active white conversion cards.
     */
    public void choseWhiteConversionCard(int currentPlayer, LeaderCard[] leaderCards){
        views.get(currentPlayer).choseWhiteConversionCard(leaderCards);
    }

    /**
     * @param message is a WHITE_CONVERSION_CARD message.
     * @throws WrongParametersException if player has chosen a wrong number of players.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     */
    private void whiteConversionCardHandler(Message message) throws IllegalStateException, WrongParametersException {
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        int viewID = m.getClientID();
        gameManager.whiteConversionCardHandler(m.getPar());
        views.get(viewID).ok();
    }

    /**
     * @param message is a SWITCH message.
     * @throws WrongParametersException if player has chosen a wrong depots.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     * @throws ImpossibleSwitchDepotException if switch is not possible.
     */
    private void switchHandler(Message message) throws IllegalStateException, WrongParametersException, ImpossibleSwitchDepotException {
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        int viewID = m.getClientID();
        gameManager.switchHandler(m.getPar1(), m.getPar2());
        views.get(viewID).ok();
    }

    /**
     * @param message is a DEVELOPMENT_CARD_POWER message.
     * @throws WrongParametersException if player has chosen a wrong development card.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     * @throws InsufficientResourceException if player has not enough resources.
     */
    private void developmentCardPowerHandler(Message message)
            throws IllegalStateException, WrongParametersException, InsufficientResourceException {
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        int viewID = m.getClientID();
        try {
            gameManager.developmentCardPowerHandler(m.getPar1(), m.getPar2());
            views.get(viewID).ok();
        } catch (NoSuchProductionPowerException e) {
            errorHandler(ErrorType.EMPTY_SLOT, viewID);
        } catch (AlreadyUsedProductionPowerException e) {
            errorHandler(ErrorType.WRONG_POWER, viewID);
        }
    }

    /**
     * @param message is a BASIC_POWER message.
     * @throws WrongParametersException if player has chosen a wrong basic power.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     * @throws InsufficientResourceException if player has not enough resources.
     */
    private void basicPowerHandler(Message message)
            throws InsufficientResourceException, IllegalStateException, WrongParametersException {
        MessageThreeResourceOneInt m = (MessageThreeResourceOneInt) message;
        int viewID = m.getClientID();
        try {
            gameManager.basicPowerHandler(m.getResource1(), m.getResource2(), m.getResource3(), m.getPar());
            views.get(viewID).ok();
        } catch (AlreadyUsedProductionPowerException e) {
            errorHandler(ErrorType.WRONG_POWER, viewID);
        }
    }

    /**
     * @param message is a LEADER_CARD_POWER message.
     * @throws WrongParametersException if player has chosen a wrong leader card.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     * @throws InsufficientResourceException if player has not enough resources.
     */
    private void leaderCardPowerHandler(Message message)
            throws InsufficientResourceException, IllegalStateException, WrongParametersException {
        MessageOneResourceTwoInt m = (MessageOneResourceTwoInt) message;
        int viewID = m.getClientID();
        try {
            gameManager.leaderCardPowerHandler(m.getPar1(), m.getResource(), m.getPar2());
            views.get(viewID).ok();
        } catch (NoSuchProductionPowerException | AlreadyUsedProductionPowerException e) {
            errorHandler(ErrorType.WRONG_POWER, viewID);
        }
    }

    /**
     * @param m is a END_PRODUCTION message.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     */
    private void endProductionHandler(Message m) throws IllegalStateException {
        int viewID = m.getClientID();
        gameManager.endProductionHandler();
        views.get(viewID).ok();
    }

    /**
     * @param message is a LEADER_CARD_ACTIVATION message.
     * @throws WrongParametersException if player has chosen a wrong leader card.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     * @throws InsufficientResourceException if player has not enough resources.
     * @throws AlreadyDiscardLeaderCardException if player previously discarded leader card.
     * @throws ActiveLeaderCardException if player previously activated leader card.
     * @throws InsufficientCardsException if player has not enough cards.
     */
    private void leaderActivationHandler(Message message)
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException, IllegalStateException, WrongParametersException {
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        int viewID = m.getClientID();
        gameManager.leaderActivationHandler(m.getPar());
        views.get(viewID).ok();
    }

    /**
     * @param message is a LEADER_CARD_DISCARD message.
     * @throws WrongParametersException if player has chosen a wrong leader card.
     * @throws IllegalStateException if player has send this message during a wrong phases.
     * @throws AlreadyDiscardLeaderCardException if player previously discarded leader card.
     * @throws ActiveLeaderCardException if player previously activated leader card.
     */
    private void leaderDiscardHandler(Message message)
            throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException, IllegalStateException, WrongParametersException {
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        int viewID = m.getClientID();
        gameManager.leaderDiscardHandler(m.getPar());
        views.get(viewID).ok();
    }

    /**
     * @throws IllegalStateException if player has send this message during a wrong phases.
     * if game is ended, proceeds to end game procedure, otherwise go to the next turn.
     */
    private void endTurnHandler() throws IllegalStateException {
        gameManager.endTurnHandler();
        if(turnController.isEndGame())
            gameManager.endGame();
        else
            turnController.nextTurn();
    }

    /**
     * @param errorType is the type of error committed by player.
     * @param viewID is the player.
     * send to player an error message.
     */
    private void errorHandler(ErrorType errorType, int viewID){
        views.get(viewID).errorMessage(errorType);
    }
}

