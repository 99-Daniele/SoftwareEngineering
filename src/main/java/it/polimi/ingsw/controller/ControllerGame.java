package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.states.CONTROLLER_STATES;
import it.polimi.ingsw.controller.states.State_Controller;
import it.polimi.ingsw.controller.states.WaitingPlayerState;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.games.*;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.PlayerBoard;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

public class ControllerGame implements Observer{

    private Game game;
    private String firstPlayer;
    private int numPlayers;
    private final LinkedList<View> views;
    private State_Controller currentState;

    public ControllerGame(){
        numPlayers = 1;
        views = new LinkedList<>();
        currentState = new WaitingPlayerState();
    }

    public int getMaxNumPlayers() {
        return numPlayers;
    }

    public int getCurrentNumPlayers(){
        return views.size();
    }

    /**
     * @param view is the view that is added to the list of views in controllerGame and also added to the observers of game
     */
    public int addView(View view){
        views.add(view);
        if(game != null)
            game.addObservers((VirtualView) view);
        return views.size() -1;
    }

    public synchronized void removeView(String nickName, int viewID){
        game.deletePlayer(nickName);
        try {
            views.remove(viewID);
        }catch (IndexOutOfBoundsException e){}
    }

    public void quitGame(String nickName, int viewID) throws IOException {
        removeView(nickName, viewID);
        if(views.size() == 0)
            System.out.println("GAME ENDED");
        else if(currentState.isRightState(CONTROLLER_STATES.WAITING_PLAYERS_STATE))
            for (View view: views)
                view.exit(nickName);
        else
            for(View view: views)
                view.quit(nickName);
    }

    @Override
    public void update(Observable o, Object arg) {
        Message m = (Message) arg;
        int viewID = m.getClientID();
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
            views.get(viewID).errorMessage(ErrorType.NOT_YOUR_TURN);
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
    }

    public void addPlayer(Message loginMessage){
        try {
            Message_One_Parameter_String m = (Message_One_Parameter_String) loginMessage;
            String nickName = m.getPar();
            if(!currentState.isRightState(CONTROLLER_STATES.WAITING_PLAYERS_STATE)){
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
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    if (getCurrentNumPlayers() == numPlayers) {
                        currentState.nextState(MessageType.START_GAME);
                        for (View view : views)
                            view.startGame(numPlayers);
                        System.out.println("NEW GAME STARTED: " + views.toString());
                    }
                    else
                        currentState.nextState(MessageType.LOGIN);
                } catch (AlreadyTakenNicknameException e) {
                    views.getLast().errorMessage(ErrorType.ALREADY_TAKEN_NICKNAME);
                }
            }
        } catch (ClassCastException e){
            views.getLast().errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    public void newGame(Message numPlayerMessage) {
        try {
            Message_One_Parameter_Int m = (Message_One_Parameter_Int) numPlayerMessage;
            int numPlayers = m.getPar();
            int viewID = numPlayerMessage.getClientID();
            if (!InputController.num_players_check(m)) {
                views.get(viewID).errorMessage(ErrorType.WRONG_PARAMETERS);
                return;
            }
            if (game != null) {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            } else {
                this.numPlayers = numPlayers;
                if (numPlayers == 1) {
                    game = new SinglePlayerGame();
                    try {
                        game.createPlayer(firstPlayer);
                    } catch (AlreadyTakenNicknameException e) {
                    }// can't be thrown because it's the first player
                    game.addObservers((VirtualView) views.get(0));
                    views.get(0).startGame(1);
                    currentState.nextState(MessageType.START_GAME);
                    System.out.println("NEW GAME STARTED: " + views.toString());
                } else {
                    game = new Game(numPlayers);
                    try {
                        game.createPlayer(firstPlayer);
                        game.addObservers((VirtualView) views.get(0));
                        views.get(0).ok();
                    } catch (AlreadyTakenNicknameException e) {
                    }// can't be thrown because it's the first player
                }
            }
        } catch (ClassCastException e){
            views.get(numPlayerMessage.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    private boolean isCurrentPlayer(int viewID){
        if(viewID == game.getCurrentPosition())
            return true;
        return false;
    }

    public void isMyTurn(Message m){
        int viewID = m.getClientID();
        views.get(viewID).isMyTurn(isCurrentPlayer(viewID));
    }

    public void endGame(){
        game.endGame();
        System.out.println("GAME ENDED");
    }

    public void buyCardHandler(Message message) {
        try {
            Message_Three_Parameter_Int m = (Message_Three_Parameter_Int) message;
            ArrayList<Integer> availableSlots;
            int viewID = m.getClientID();
            if (!currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE)) {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            if (!InputController.buy_card_check(m)) {
                views.get(viewID).errorMessage(ErrorType.WRONG_PARAMETERS);
                return;
            }
            try {
                availableSlots = game.findAvailableSlots(m.getPar1(), m.getPar2());
            } catch (EmptyDevelopmentCardDeckException e) {
                views.get(viewID).errorMessage(ErrorType.EMPTY_DECK);
                return;
            }
            if (availableSlots.isEmpty()) {
                views.get(viewID).errorMessage(ErrorType.FULL_SLOT);
                return;
            }
            int row = m.getPar1();
            int column = m.getPar2();
            int choice = m.getPar3();
            if (availableSlots.size() == 1) {
                try {
                    game.buyDevelopmentCardFromMarket(row, column, choice, availableSlots.get(0));
                } catch (InsufficientResourceException e) {
                    views.get(viewID).errorMessage(ErrorType.NOT_ENOUGH_RESOURCES);
                    return;
                } catch (ImpossibleDevelopmentCardAdditionException e) {
                    views.get(viewID).errorMessage(ErrorType.FULL_SLOT);
                    return;
                } catch (EmptyDevelopmentCardDeckException e) {
                    views.get(viewID).errorMessage(ErrorType.EMPTY_DECK);
                    return;
                }
                currentState.nextState(MessageType.END_TURN);
                views.get(viewID).ok();
            } else {
                currentState.nextState(MessageType.CHOSEN_SLOT);
                currentState.setRow(row);
                currentState.setColumn(column);
                currentState.setChoice(choice);
                currentState.setAvailableSlots(availableSlots);
                views.get(viewID).available_slot(availableSlots);
            }
        } catch (ClassCastException e){
            views.get(message.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }


    public void chosenSlotHandler(Message message) {
        try {
            Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
            int viewID = m.getClientID();
            int chosenSlot = m.getClientID();
            if (!currentState.isRightState(CONTROLLER_STATES.BUY_CARD_STATE)) {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            if(!currentState.getAvailableSlots().contains(chosenSlot)) {
                views.get(viewID).errorMessage(ErrorType.WRONG_PARAMETERS);
                return;
            }
            int row = currentState.getRow();
            int column = currentState.getColumn();
            int choice = currentState.getChoice();
            try {
                game.buyDevelopmentCardFromMarket(row, column, choice, chosenSlot);
                currentState.nextState(MessageType.END_TURN);
                views.get(viewID).ok();
            } catch (InsufficientResourceException e) {
                views.get(viewID).errorMessage(ErrorType.NOT_ENOUGH_RESOURCES);
            } catch (ImpossibleDevelopmentCardAdditionException e) {
                views.get(viewID).errorMessage(ErrorType.FULL_SLOT);
            } catch (EmptyDevelopmentCardDeckException e) {
                views.get(viewID).errorMessage(ErrorType.EMPTY_DECK);
            }
        } catch (ClassCastException e){
            views.get(message.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }


    public void takeMarbleHandler(Message message) {
        try {
            Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
            int viewID = m.getClientID();
            if (!currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE)) {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            if (!InputController.taken_marbles_check(m)) {
                views.get(viewID).errorMessage(ErrorType.WRONG_PARAMETERS);
                return;
            }
            boolean choice = m.getPar1() == 0 ? true : false;
            try {
                Marble[] marbles = game.takeMarketMarble(choice, m.getPar2());
                currentState.nextState(MessageType.USE_MARBLE);
                currentState.setMarbles(marbles);
                views.get(viewID).chosen_marble(marbles);
            } catch (WrongParametersException e) {
                views.get(viewID).errorMessage(ErrorType.WRONG_PARAMETERS);
            }
        } catch (ClassCastException e){
            views.get(message.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    public void useMarbleHandler(Message message) {
        try {
            Message_One_Parameter_Marble m = (Message_One_Parameter_Marble) message;
            int viewID = m.getClientID();
            Marble chosenMarble = m.getMarble();
            if (!currentState.isRightState(CONTROLLER_STATES.TAKE_MARBLE_STATE)) {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            if(!chosenMarble.useMarble(game)){
                currentState.removeMarble(chosenMarble);
                if(currentState.getMarbles().size() == 0)
                    currentState.nextState(MessageType.END_TURN);
                else
                    currentState.nextState(MessageType.USE_MARBLE);
                views.get(viewID).ok();
            }
            else {
                try {
                    LeaderCard[] whiteConversionCards = game.getCurrentPlayerActiveLeaderCards();
                    ArrayList<Marble> remainingMarbles = currentState.getMarbles();
                    currentState.nextState(MessageType.WHITE_CONVERSION_CARD);
                    currentState.setLeaderCards(whiteConversionCards);
                    currentState.setMarbles(remainingMarbles);
                    views.get(viewID).choseWhiteConversionCard(whiteConversionCards);
                } catch (AlreadyDiscardLeaderCardException e) {
                    views.get(viewID).errorMessage(ErrorType.ALREADY_DISCARD_LEADER_CARD);
                }
            }
        } catch (ClassCastException e){
            views.get(message.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    /* richiede l'identificativo leaderCard*/
    public void whiteConversionCardHandler(Message message) {
        try {
            Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
            int viewID = m.getClientID();
            int chosenLeaderCard = m.getPar();
            if (!currentState.isRightState(CONTROLLER_STATES.WHITE_CONVERSION_CARD_STATE)) {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            LeaderCard leaderCard;
            if(chosenLeaderCard == 0)
                leaderCard = currentState.getLeaderCard1();
            else
                leaderCard = currentState.getLeaderCard2();
            game.whiteMarbleConversion(leaderCard);
            if(currentState.getMarbles().size() == 0)
                currentState.nextState(MessageType.END_TURN);
            else {
                ArrayList<Marble> remainingMarbles = currentState.getMarbles();
                currentState.nextState(MessageType.USE_MARBLE);
                currentState.setMarbles(remainingMarbles);
            }
            views.get(viewID).ok();
        } catch (ClassCastException e){
            views.get(message.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }


    public void switchHandler(Message message) {
        try {
            Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
            int viewID = m.getClientID();
            if (!currentState.isRightState(CONTROLLER_STATES.TAKE_MARBLE_STATE)) {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            if (!InputController.switch_depot_check(m)) {
                views.get(viewID).errorMessage(ErrorType.WRONG_PARAMETERS);
                return;
            }
            try {
                game.switchDepots(m.getPar1(), m.getPar2());
                views.get(viewID).ok();
            } catch (ImpossibleSwitchDepotException e) {
                views.get(message.getClientID()).errorMessage(ErrorType.IMPOSSIBLE_SWITCH);
            }
        } catch (ClassCastException e){
            views.get(message.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    public void developmentCardPowerHandler(Message message) {
        try {
            Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
            int viewID = m.getClientID();
            int chosenSlot = m.getPar1();
            int choice = m.getPar2();
            Strongbox s;
            if (currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
                s = new Strongbox();
            else if(currentState.isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE))
                s = currentState.getStrongbox();
            else {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            if(!InputController.development_card_power_check(m)) {
                views.get(viewID).errorMessage(ErrorType.WRONG_PARAMETERS);
                return;
            }
            try {
                game.removeDevelopmentCardProductionResource(chosenSlot, s, choice);
                currentState.nextState(MessageType.END_PRODUCTION);
                currentState.setStrongbox(s);
                views.get(viewID).ok();
            } catch (InsufficientResourceException e) {
                views.get(viewID).errorMessage(ErrorType.NOT_ENOUGH_RESOURCES);
            } catch (NoSuchProductionPowerException e) {
                views.get(viewID).errorMessage(ErrorType.EMPTY_SLOT);
            }
        } catch (ClassCastException e){
            views.get(message.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    public void basicPowerHandler(Message message){
        try {
            Message_Three_Resource_One_Int m = (Message_Three_Resource_One_Int) message;
            int viewID = m.getClientID();
            Resource r1 = m.getResource1();
            Resource r2 = m.getResource2();
            Resource r3 = m.getResource3();
            int choice = m.getPar();
            Strongbox s;
            if (currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
                s = new Strongbox();
            else if(currentState.isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE))
                s = currentState.getStrongbox();
            else {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            if(!InputController.basic_power_check(m)) {
                views.get(viewID).errorMessage(ErrorType.WRONG_PARAMETERS);
                return;
            }
            try {
                game.basicProductionPower(r1, r2, r3, s, choice);
                currentState.nextState(MessageType.END_PRODUCTION);
                currentState.setStrongbox(s);
                views.get(viewID).ok();
            } catch (InsufficientResourceException e) {
                views.get(viewID).errorMessage(ErrorType.NOT_ENOUGH_RESOURCES);
            }
        } catch (ClassCastException e){
            views.get(message.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    public void leaderCardPowerHandler(Message message) {
        try {
            Message_One_Resource_Two_Int m = (Message_One_Resource_Two_Int) message;
            int viewID = m.getClientID();
            int chosenLeaderCard = m.getPar1();
            Resource r = m.getResource();
            int choice = m.getPar2();
            Strongbox s;
            if (currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
                s = new Strongbox();
            else if(currentState.isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE))
                s = currentState.getStrongbox();
            else {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            if(!InputController.leader_card_power_check(m)) {
                views.get(viewID).errorMessage(ErrorType.WRONG_PARAMETERS);
                return;
            }
            try {
                game.removeAdditionalProductionPowerCardResource(chosenLeaderCard, r, s, choice);
                currentState.nextState(MessageType.END_PRODUCTION);
                currentState.setStrongbox(s);
                views.get(viewID).ok();
            } catch (InsufficientResourceException e) {
                views.get(viewID).errorMessage(ErrorType.NOT_ENOUGH_RESOURCES);
            } catch (NoSuchProductionPowerException e) {
                views.get(viewID).errorMessage(ErrorType.WRONG_POWER);
            }
        } catch (ClassCastException e){
            views.get(message.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    public void endProductionHandler(Message m) {
        int viewID = m.getClientID();
        if (!currentState.isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE)) {
            views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
            return;
        }
        Strongbox s = currentState.getStrongbox();
        game.increaseCurrentPlayerStrongbox(s);
        currentState.nextState(MessageType.BUY_CARD);
        views.get(viewID).ok();
    }

    public void leaderActivationHandler(Message message) {
        try {
            Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
            int viewID = m.getClientID();
            int chosenLeaderCard = m.getPar();
            if(currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
                currentState.nextState(MessageType.BUY_CARD);
            else if(currentState.isRightState(CONTROLLER_STATES.END_TURN_STATE))
                currentState.nextState(MessageType.END_TURN);
            else {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            try {
                game.activateLeaderCard(chosenLeaderCard);
                views.get(viewID).ok();
            } catch (InsufficientResourceException e) {
                views.get(viewID).errorMessage(ErrorType.NOT_ENOUGH_RESOURCES);
            } catch (AlreadyDiscardLeaderCardException e) {
                views.get(viewID).errorMessage(ErrorType.ALREADY_DISCARD_LEADER_CARD);
            } catch (ActiveLeaderCardException e) {
                views.get(viewID).errorMessage(ErrorType.ALREADY_ACTIVE_LEADER_CARD);
            } catch (InsufficientCardsException e) {
                views.get(viewID).errorMessage(ErrorType.NOT_ENOUGH_CARDS);
            }
        } catch (ClassCastException e){
            views.get(message.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    public void leaderDiscardHandler(Message message) {
        try {
            Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
            int viewID = m.getClientID();
            int chosenLeaderCard = m.getPar();
            if(currentState.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE))
                currentState.nextState(MessageType.BUY_CARD);
            else if(currentState.isRightState(CONTROLLER_STATES.END_TURN_STATE))
                currentState.nextState(MessageType.END_TURN);
            else {
                views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
                return;
            }
            try {
                game.discardLeaderCard(chosenLeaderCard);
                views.get(viewID).ok();
            } catch (AlreadyDiscardLeaderCardException e) {
                views.get(viewID).errorMessage(ErrorType.ALREADY_DISCARD_LEADER_CARD);
            } catch (ActiveLeaderCardException e) {
                views.get(viewID).errorMessage(ErrorType.ALREADY_ACTIVE_LEADER_CARD);
            }
        } catch (ClassCastException e){
            views.get(message.getClientID()).errorMessage(ErrorType.WRONG_PARAMETERS);
        }
    }

    public void endTurnHandler(Message m){
        int viewID = m.getClientID();
        if(!currentState.isRightState(CONTROLLER_STATES.END_TURN_STATE)){
            views.get(viewID).errorMessage(ErrorType.ILLEGAL_OPERATION);
            return;
        }
        currentState.nextState(MessageType.BUY_CARD);
        game.nextPlayer();
        if(game.isEndGame())
            game.endGame();
    }
}

