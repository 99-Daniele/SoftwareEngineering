package it.polimi.ingsw.controller;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.exceptions.IllegalStateException;
import it.polimi.ingsw.model.games.GameManager;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.*;

import java.util.*;

public class ControllerGame implements Observer {

    private GameManager gameManager;
    private TurnController turnController;
    private LinkedList<View> views;
    private GAME_STARTING_STATES state;
    private final ArrayList<ArrayList<LeaderCard>> leaderCards;

    public ControllerGame() {
        views = new LinkedList<>();
        state = GAME_STARTING_STATES.WAITING_NUM_PLAYERS;
        leaderCards = new ArrayList<>();
    }

    public void resetControllerGame(){
        if(gameManager != null)
            System.out.println("GAME ENDED");
        gameManager = null;
        views = new LinkedList<>();
        state = GAME_STARTING_STATES.WAITING_NUM_PLAYERS;
        ControllerConnection.newGame();
    }

    public int getMaxNumPlayers() {
        if(gameManager != null)
            return gameManager.getNumOfPlayers();
        return 0;
    }

    public int getCurrentNumPlayers() {
        return views.size();
    }

    /**
     * @param view is the view that is added to the list of views in controllerGame and also added to the observers of game
     */
    public synchronized void addView(View view) throws FullGameException {
        if(gameManager != null && views.size() == gameManager.getNumOfPlayers())
            throw new FullGameException();
        views.add(view);
        if(view.getNickname() != null)
            addPlayer(view, view.getNickname());
    }

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
        else if (state != GAME_STARTING_STATES.START_GAME) {
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
        } catch (AlreadyTakenNicknameException alreadyTakenNicknameException) {
            errorHandler(ErrorType.ALREADY_TAKEN_NICKNAME, viewID);
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

    private synchronized void addPlayer(View view, Message loginMessage) {
        try {
            Message_One_Parameter_String m = (Message_One_Parameter_String) loginMessage;
            String nickName = m.getPar();
            addPlayer(view, nickName);
        } catch (ClassCastException e) {
            view.errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    private synchronized void addPlayer(View view, String nickName){
        if (state != GAME_STARTING_STATES.WAITING_PLAYERS && state != GAME_STARTING_STATES.WAITING_NUM_PLAYERS) {
            view.errorMessage(ErrorType.ILLEGAL_OPERATION);
            return;
        }
        if (!InputController.login_check(nickName)) {
            view.errorMessage(ErrorType.WRONG_PARAMETERS);
            return;
        }
        createNewPlayer(view, nickName);
    }

    private void createNewPlayer(View view, String nickName){
        if (state == GAME_STARTING_STATES.WAITING_NUM_PLAYERS) {
            view.login(0);
            System.out.println("1");
        } else {
            try {
                gameManager.createPlayer(view, nickName);
                view.login(views.size() - 1);
            } catch (AlreadyTakenNicknameException e) {
                view.errorMessage(ErrorType.ALREADY_TAKEN_NICKNAME);
            }
        }
    }

    public synchronized void newGame(Message numPlayerMessage)
            throws WrongParametersException, IllegalStateException, AlreadyTakenNicknameException{
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) numPlayerMessage;
        int numPlayers = m.getPar();
        if (!InputController.num_players_check(numPlayers))
            throw new WrongParametersException();
        if (state != GAME_STARTING_STATES.WAITING_NUM_PLAYERS)
            throw new IllegalStateException();
        else {
            gameManager = new GameManager(numPlayers, this);
            turnController = new TurnController(numPlayers);
            gameManager.createPlayer(views.get(0), views.get(0).getNickname());
            if (numPlayers > 1) {
                state = GAME_STARTING_STATES.WAITING_PLAYERS;
                views.get(0).ok();
            }
            ControllerConnection.newGame();
        }
    }

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
        state = GAME_STARTING_STATES.WAITING_PLAYERS_CHOICES;
    }

    public void sendNickNames(int player, int numPlayers, ArrayList<String> nickNames){
        views.get(player).allPlayerConnected(player, numPlayers, nickNames);
    }

    public void sendLeaderCards(int player, ArrayList<LeaderCard> leaderCards){
        this.leaderCards.add(leaderCards);
        views.get(player).choseLeaderCards(leaderCards);
    }

    public void isMyTurn(Message m) {
        int viewID = m.getClientID();
        views.get(viewID).isMyTurn(turnController.isMyTurn(viewID));
    }

    public void playersReady(){
        state = GAME_STARTING_STATES.START_GAME;
        System.out.println("All players have made their choice.");
        for(View view: views)
            view.startGame();
    }

    public void endGame() {
        turnController.endGame();
    }

    private void leaderCardHandler(Message message) throws IllegalStateException, WrongParametersException {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        int viewID = m.getClientID();
        if(state != GAME_STARTING_STATES.WAITING_PLAYERS_CHOICES)
            throw new IllegalStateException();
        if(!InputController.leader_card_check(m.getPar1(), m.getPar2(),  leaderCards.get(viewID)))
            throw new WrongParametersException();
        gameManager.leaderCardHandler(viewID, m.getPar1(), m.getPar2(), leaderCards.get(viewID));
        views.get(viewID).ok();
    }

    private void oneResourceHandle(Message message) throws IllegalStateException {
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        int viewID = m.getClientID();
        if(viewID != 1 && viewID != 2 || state != GAME_STARTING_STATES.WAITING_PLAYERS_CHOICES)
            throw new IllegalStateException();
        gameManager.oneResourceHandle(viewID, m.getResource());
        views.get(viewID).ok();
    }

    private void twoResourceHandle(Message message) throws IllegalStateException {
        Message_Two_Resource m = (Message_Two_Resource) message;
        int viewID = m.getClientID();
        if(viewID != 3 || state != GAME_STARTING_STATES.WAITING_PLAYERS_CHOICES)
            throw new IllegalStateException();
        gameManager.twoResourceHandle(viewID, m.getR1(), m.getR2());
        views.get(viewID).ok();
    }

    private void buyCardHandler(Message message)
            throws IllegalStateException, WrongParametersException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, InsufficientResourceException {
        Message_Three_Parameter_Int m = (Message_Three_Parameter_Int) message;
        int viewID = m.getClientID();
        if(gameManager.buyCardHandler(m.getPar1(), m.getPar2(), m.getPar3()))
            views.get(viewID).ok();
    }

    public void choseSlot(int currentPlayer, ArrayList<Integer> slots){
        views.get(currentPlayer).available_slot(slots);
    }

    private void chosenSlotHandler(Message message)
            throws IllegalStateException, WrongParametersException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, InsufficientResourceException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        int viewID = m.getClientID();
        int chosenSlot = m.getPar();
        gameManager.chosenSlotHandler(chosenSlot);
        views.get(viewID).ok();
    }

    private void takeMarbleHandler(Message message) throws IllegalStateException, WrongParametersException {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        int viewID = m.getClientID();
        Marble[] marbles = gameManager.takeMarbleHandler(m.getPar1(), m.getPar2());
        views.get(viewID).chosen_marble(marbles);
    }

    private void useMarbleHandler(Message message)
            throws IllegalStateException, AlreadyDiscardLeaderCardException, WrongParametersException {
        Message_One_Parameter_Marble m = (Message_One_Parameter_Marble) message;
        int viewID = m.getClientID();
        if(gameManager.useMarbleHandler(m.getMarble()))
            views.get(viewID).ok();
    }

    public void choseWhiteConversionCard(int currentPlayer, LeaderCard[] leaderCards){
        views.get(currentPlayer).choseWhiteConversionCard(leaderCards);
    }

    private void whiteConversionCardHandler(Message message) throws IllegalStateException, WrongParametersException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        int viewID = m.getClientID();
        gameManager.whiteConversionCardHandler(m.getPar());
        views.get(viewID).ok();
    }

    private void switchHandler(Message message) throws IllegalStateException, WrongParametersException, ImpossibleSwitchDepotException {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        int viewID = m.getClientID();
        gameManager.switchHandler(m.getPar1(), m.getPar2());
        views.get(viewID).ok();
    }

    private void developmentCardPowerHandler(Message message)
            throws IllegalStateException, WrongParametersException, InsufficientResourceException {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        int viewID = m.getClientID();
        try {
            gameManager.developmentCardPowerHandler(m.getPar1(), m.getPar2());
            views.get(viewID).ok();
        } catch (NoSuchProductionPowerException e) {
            errorHandler(ErrorType.EMPTY_SLOT, viewID);
        }
    }

    private void basicPowerHandler(Message message)
            throws InsufficientResourceException, IllegalStateException, WrongParametersException {
        Message_Three_Resource_One_Int m = (Message_Three_Resource_One_Int) message;
        int viewID = m.getClientID();
        gameManager.basicPowerHandler(m.getResource1(), m.getResource2(), m.getResource3(), m.getPar());
        views.get(viewID).ok();
    }

    private void leaderCardPowerHandler(Message message)
            throws InsufficientResourceException, IllegalStateException, WrongParametersException {
        Message_One_Resource_Two_Int m = (Message_One_Resource_Two_Int) message;
        int viewID = m.getClientID();
        try {
            gameManager.leaderCardPowerHandler(m.getPar1(), m.getResource(), m.getPar2());
            views.get(viewID).ok();
        } catch (NoSuchProductionPowerException e) {
            errorHandler(ErrorType.WRONG_POWER, viewID);
        }
    }

    private void endProductionHandler(Message m) throws IllegalStateException {
        int viewID = m.getClientID();
        gameManager.endProductionHandler();
        views.get(viewID).ok();
    }

    private void leaderActivationHandler(Message message)
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException, IllegalStateException, WrongParametersException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        int viewID = m.getClientID();
        gameManager.leaderActivationHandler(m.getPar());
        views.get(viewID).ok();
    }

    private void leaderDiscardHandler(Message message)
            throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException, IllegalStateException, WrongParametersException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        int viewID = m.getClientID();
        gameManager.leaderDiscardHandler(m.getPar());
        views.get(viewID).ok();
    }

    private void endTurnHandler() throws IllegalStateException {
        gameManager.endTurnHandler();
        if(turnController.isEndGame())
            gameManager.endGame();
        else
            turnController.nextTurn();
    }

    private void errorHandler(ErrorType errorType, int viewID){
        views.get(viewID).errorMessage(errorType);
    }
}

