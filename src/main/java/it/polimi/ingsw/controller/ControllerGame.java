package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.states.CONTROLLER_STATES;
import it.polimi.ingsw.controller.states.State_Controller;
import it.polimi.ingsw.controller.states.WaitingPlayerState;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.exceptions.IllegalStateException;
import it.polimi.ingsw.model.games.*;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class ControllerGame implements Observer {

    private Game game;
    private String firstPlayer;
    private int numPlayers;
    private final LinkedList<View> views;
    private State_Controller currentState;

    public ControllerGame() {
        numPlayers = 1;
        views = new LinkedList<>();
        currentState = new WaitingPlayerState();
    }

    public int getMaxNumPlayers() {
        return numPlayers;
    }

    public int getCurrentNumPlayers() {
        return views.size();
    }

    public void setCurrentState(State_Controller newState) {
        currentState = newState;
    }

    /**
     * @param view is the view that is added to the list of views in controllerGame and also added to the observers of game
     */
    public int addView(View view) {
        views.add(view);
        if (game != null)
            game.addObservers((VirtualView) view);
        return views.size() - 1;
    }

    public synchronized void removeView(String nickName, int viewID) {
        game.deletePlayer(nickName);
        try {
            views.remove(viewID);
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void quitGame(String nickName, int viewID) throws IOException {
        removeView(nickName, viewID);
        if (views.size() == 0)
            System.out.println("GAME ENDED");
        else if (currentState.isRightState(CONTROLLER_STATES.WAITING_PLAYERS_STATE))
            for (View view : views)
                view.exit(nickName);
        else
            for (View view : views)
                view.quit(nickName);
    }

    @Override
    public void update(Observable o, Object arg) {
        Message m = (Message) arg;
        int viewID = m.getClientID();
        try {
            if (m.getMessageType() == MessageType.LOGIN) {
                addPlayer(m);
                return;
            }
            if (m.getMessageType() == MessageType.NUM_PLAYERS) {
                newGame(m);
                return;
            }
            if (m.getMessageType() == MessageType.TURN) {
                isMyTurn(m);
                return;
            }
            if (!isCurrentPlayer(viewID)) {
                errorHandler(ErrorType.NOT_YOUR_TURN, viewID);
                return;
            }
            switch (m.getMessageType()) {
                case LEADER_CARD:
                    break;
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
                    endTurnHandler(m);
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

    public void addPlayer(Message loginMessage) {
        try {
            Message_One_Parameter_String m = (Message_One_Parameter_String) loginMessage;
            String nickName = m.getPar();
            if (!currentState.isRightState(CONTROLLER_STATES.WAITING_PLAYERS_STATE)) {
                views.getLast().errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            if (!InputController.login_check(m)) {
                views.getLast().errorMessage(ErrorType.WRONG_PARAMETERS);
                return;
            }
            if (game == null) {
                firstPlayer = nickName;
                views.get(0).sendMessage(new Message(MessageType.LOGIN, 0));
            } else {
                try {
                    game.createPlayer(nickName);

                    views.getLast().sendMessage(new Message(MessageType.LOGIN, views.size() - 1));
                    for (View view : views) {
                        view.newPlayer(nickName, views.size() - 1);
                    }
                    if (getCurrentNumPlayers() == numPlayers) {
                        System.out.println(currentState);
                        currentState.nextState(this, MessageType.START_GAME);
                        System.out.println(currentState);
                        for (View view : views)
                            view.startGame(numPlayers);
                        System.out.println("NEW GAME STARTED: " + views);
                    } else
                        currentState.nextState(this, MessageType.LOGIN);
                } catch (AlreadyTakenNicknameException e) {
                    views.getLast().errorMessage(ErrorType.ALREADY_TAKEN_NICKNAME);
                }
            }
        } catch (ClassCastException e) {
            views.getLast().errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    public void newGame(Message numPlayerMessage)
            throws WrongParametersException, IllegalStateException, AlreadyTakenNicknameException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) numPlayerMessage;
        int numPlayers = m.getPar();
        if (!InputController.num_players_check(m))
            throw new WrongParametersException();
        if (game != null)
            throw new IllegalStateException();
        else {
            this.numPlayers = numPlayers;
            if (numPlayers == 1) {
                game = new SinglePlayerGame();
                game.createPlayer(firstPlayer);
                game.addObservers((VirtualView) views.get(0));
                views.get(0).startGame(1);
                currentState.nextState(this, MessageType.START_GAME);
                System.out.println("NEW GAME STARTED: " + views);
            } else {
                game = new Game(numPlayers);
                game.createPlayer(firstPlayer);
                game.addObservers((VirtualView) views.get(0));
                views.get(0).ok();
            }
        }
    }

    private boolean isCurrentPlayer(int viewID) {
        if (viewID == game.getCurrentPosition())
            return true;
        return false;
    }

    public void isMyTurn(Message m) {
        int viewID = m.getClientID();
        views.get(viewID).isMyTurn(isCurrentPlayer(viewID));
    }

    public void endGame() {
        game.endGame();
        System.out.println("GAME ENDED");
    }

    public void buyCardHandler(Message message)
            throws IllegalStateException, WrongParametersException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, InsufficientResourceException {
        Message_Three_Parameter_Int m = (Message_Three_Parameter_Int) message;
        ArrayList<Integer> availableSlots;
        int viewID = m.getClientID();
        if (!currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
            throw new IllegalStateException();
        if (!InputController.buy_card_check(m))
            throw new WrongParametersException();
        availableSlots = game.findAvailableSlots(m.getPar1(), m.getPar2());
        if (availableSlots.isEmpty()) {
            throw new ImpossibleDevelopmentCardAdditionException();
        }
        int row = m.getPar1();
        int column = m.getPar2();
        int choice = m.getPar3();
        if (availableSlots.size() == 1) {
            game.buyDevelopmentCardFromMarket(row, column, choice, availableSlots.get(0));
            currentState.nextState(this, MessageType.END_TURN);
            views.get(viewID).ok();
        } else {
            currentState.nextState(this, MessageType.CHOSEN_SLOT);
            currentState.setRow(row);
            currentState.setColumn(column);
            currentState.setChoice(choice);
            currentState.setAvailableSlots(availableSlots);
            views.get(viewID).available_slot(availableSlots);
        }
    }


    public void chosenSlotHandler(Message message)
            throws IllegalStateException, WrongParametersException, InsufficientResourceException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        int viewID = m.getClientID();
        int chosenSlot = m.getClientID();
        if (!currentState.isRightState(CONTROLLER_STATES.BUY_CARD_STATE))
            throw new IllegalStateException();
        if (!currentState.getAvailableSlots().contains(chosenSlot))
            throw new WrongParametersException();
        int row = currentState.getRow();
        int column = currentState.getColumn();
        int choice = currentState.getChoice();
        game.buyDevelopmentCardFromMarket(row, column, choice, chosenSlot);
        currentState.nextState(this, MessageType.END_TURN);
        views.get(viewID).ok();
    }


    public void takeMarbleHandler(Message message) throws IllegalStateException, WrongParametersException {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        int viewID = m.getClientID();
        if (!currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
            throw new IllegalStateException();
        if (!InputController.taken_marbles_check(m))
            throw new WrongParametersException();
        boolean choice = m.getPar1() == 0 ? true : false;
        Marble[] marbles = game.takeMarketMarble(choice, m.getPar2());
        currentState.nextState(this, MessageType.USE_MARBLE);
        currentState.setMarbles(marbles);
        views.get(viewID).chosen_marble(marbles);
    }

    public void useMarbleHandler(Message message) throws IllegalStateException, AlreadyDiscardLeaderCardException {
        Message_One_Parameter_Marble m = (Message_One_Parameter_Marble) message;
        int viewID = m.getClientID();
        Marble chosenMarble = m.getMarble();
        if (!currentState.isRightState(CONTROLLER_STATES.TAKE_MARBLE_STATE))
            throw new IllegalStateException();
        if (!chosenMarble.useMarble(game)) {
            currentState.removeMarble(chosenMarble);
            if (currentState.getMarbles().size() == 0)
                currentState.nextState(this, MessageType.END_TURN);
            else
                currentState.nextState(this, MessageType.USE_MARBLE);
            views.get(viewID).ok();
        } else {
            LeaderCard[] whiteConversionCards = game.getCurrentPlayerActiveLeaderCards();
            ArrayList<Marble> remainingMarbles = currentState.getMarbles();
            currentState.nextState(this, MessageType.WHITE_CONVERSION_CARD);
            currentState.setLeaderCards(whiteConversionCards);
            currentState.setMarbles(remainingMarbles);
            views.get(viewID).choseWhiteConversionCard(whiteConversionCards);
        }
    }

    public void whiteConversionCardHandler(Message message) throws IllegalStateException, WrongParametersException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        int viewID = m.getClientID();
        int chosenLeaderCard = m.getPar();
        if (!currentState.isRightState(CONTROLLER_STATES.WHITE_CONVERSION_CARD_STATE))
            throw new IllegalStateException();
        if (!InputController.white_conversion_card_check(chosenLeaderCard))
            throw new WrongParametersException();
        LeaderCard leaderCard;
        if (chosenLeaderCard == 0)
            leaderCard = currentState.getLeaderCard1();
        else
            leaderCard = currentState.getLeaderCard2();
        game.whiteMarbleConversion(leaderCard);
        if (currentState.getMarbles().size() == 0)
            currentState.nextState(this, MessageType.END_TURN);
        else {
            ArrayList<Marble> remainingMarbles = currentState.getMarbles();
            currentState.nextState(this, MessageType.USE_MARBLE);
            currentState.setMarbles(remainingMarbles);
        }
        views.get(viewID).ok();
    }

    public void switchHandler(Message message) throws IllegalStateException, WrongParametersException, ImpossibleSwitchDepotException {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        int viewID = m.getClientID();
        if (!currentState.isRightState(CONTROLLER_STATES.TAKE_MARBLE_STATE))
            throw new IllegalStateException();
        if (!InputController.switch_depot_check(m))
            throw new WrongParametersException();
        game.switchDepots(m.getPar1(), m.getPar2());
        views.get(viewID).ok();
    }

    public void developmentCardPowerHandler(Message message)
            throws InsufficientResourceException, IllegalStateException, WrongParametersException {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        int viewID = m.getClientID();
        int chosenSlot = m.getPar1();
        int choice = m.getPar2();
        Strongbox s;
        if (currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
            s = new Strongbox();
        else if (currentState.isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE))
            s = currentState.getStrongbox();
        else
            throw new IllegalStateException();
        if (!InputController.development_card_power_check(m))
            throw new WrongParametersException();
        try {
            game.removeDevelopmentCardProductionResource(chosenSlot, s, choice);
            currentState.nextState(this, MessageType.END_PRODUCTION);
            currentState.setStrongbox(s);
            views.get(viewID).ok();
        } catch (NoSuchProductionPowerException e) {
            errorHandler(ErrorType.EMPTY_SLOT, viewID);
        }
    }

    public void basicPowerHandler(Message message)
            throws InsufficientResourceException, IllegalStateException, WrongParametersException {
        Message_Three_Resource_One_Int m = (Message_Three_Resource_One_Int) message;
        int viewID = m.getClientID();
        Resource r1 = m.getResource1();
        Resource r2 = m.getResource2();
        Resource r3 = m.getResource3();
        int choice = m.getPar();
        Strongbox s;
        if (currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
            s = new Strongbox();
        else if (currentState.isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE))
            s = currentState.getStrongbox();
        else
            throw new IllegalStateException();
        if (!InputController.basic_power_check(m))
            throw new WrongParametersException();
        game.basicProductionPower(r1, r2, r3, s, choice);
        currentState.nextState(this, MessageType.END_PRODUCTION);
        currentState.setStrongbox(s);
        views.get(viewID).ok();
    }

    public void leaderCardPowerHandler(Message message)
            throws InsufficientResourceException, IllegalStateException, WrongParametersException {
        Message_One_Resource_Two_Int m = (Message_One_Resource_Two_Int) message;
        int viewID = m.getClientID();
        int chosenLeaderCard = m.getPar1();
        Resource r = m.getResource();
        int choice = m.getPar2();
        Strongbox s;
        if (currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
            s = new Strongbox();
        else if (currentState.isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE))
            s = currentState.getStrongbox();
        else
            throw new IllegalStateException();
        if (!InputController.leader_card_power_check(m))
            throw new WrongParametersException();
        try {
            game.removeAdditionalProductionPowerCardResource(chosenLeaderCard, r, s, choice);
            currentState.nextState(this, MessageType.END_PRODUCTION);
            currentState.setStrongbox(s);
            views.get(viewID).ok();
        } catch (NoSuchProductionPowerException e) {
            errorHandler(ErrorType.WRONG_POWER, viewID);
        }
    }

    public void endProductionHandler(Message m) throws IllegalStateException {
        int viewID = m.getClientID();
        if (!currentState.isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE))
            throw new IllegalStateException();
        Strongbox s = currentState.getStrongbox();
        game.increaseCurrentPlayerStrongbox(s);
        currentState.nextState(this, MessageType.BUY_CARD);
        views.get(viewID).ok();
    }

    public void leaderActivationHandler(Message message)
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException, IllegalStateException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        int viewID = m.getClientID();
        int chosenLeaderCard = m.getPar();
        if (currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
            currentState.nextState(this, MessageType.BUY_CARD);
        else if (currentState.isRightState(CONTROLLER_STATES.END_TURN_STATE))
            currentState.nextState(this, MessageType.END_TURN);
        else
            throw new IllegalStateException();
        game.activateLeaderCard(chosenLeaderCard);
        views.get(viewID).ok();
    }

    public void leaderDiscardHandler(Message message)
            throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException, IllegalStateException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        int viewID = m.getClientID();
        int chosenLeaderCard = m.getPar();
        if (currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
            currentState.nextState(this, MessageType.BUY_CARD);
        else if (currentState.isRightState(CONTROLLER_STATES.END_TURN_STATE))
            currentState.nextState(this, MessageType.END_TURN);
        else
            throw new IllegalStateException();
        game.discardLeaderCard(chosenLeaderCard);
        views.get(viewID).ok();
    }

    public void endTurnHandler(Message m) throws IllegalStateException {
        if(!currentState.isRightState(CONTROLLER_STATES.END_TURN_STATE))
            throw new IllegalStateException();
        currentState.nextState(this, MessageType.BUY_CARD);
        game.nextPlayer();
        if(game.isEndGame())
            endGame();
    }

    public void errorHandler(ErrorType errorType, int viewID){
        views.get(viewID).errorMessage(errorType);
    }
}
