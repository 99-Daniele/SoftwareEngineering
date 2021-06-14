package it.polimi.ingsw.model.games;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.model.games.states.FirstActionState;
import it.polimi.ingsw.model.games.states.GameState;
import it.polimi.ingsw.model.games.states.GameStates;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.exceptions.IllegalStateException;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;

public class GameManager {

    private final Game game;
    private final ControllerGame controllerGame;
    private GameState currentState;

    public GameManager(int numPlayers, ControllerGame controllerGame) {
        if(numPlayers == 1)
            game = new SinglePlayerGame();
        else
            game = new Game(numPlayers);
        this.controllerGame = controllerGame;
        currentState = new FirstActionState();
    }

    public GameState getCurrentState(){
        return currentState;
    }

    public void setCurrentState(GameState newState){
        currentState = newState;
    }

    public int getNumOfPlayers(){
        return game.getNumOfPlayers();
    }

    public synchronized void createPlayer(View view, String nickName) throws AlreadyTakenNicknameException{
        game.createPlayer(nickName);
        game.addObserver((VirtualView) view);
        if(game.allPlayersConnected())
            startGame();
    }

    public void deletePlayer(String nickName){
        game.deletePlayer(nickName);
    }

    public String getPlayerPosition(int position){
        return game.getPlayerPosition(position);
    }

    public void startGame(){
        controllerGame.startGame();
        for(int i = 0; i < game.getNumOfPlayers(); i++){
            controllerGame.sendNickNames(i, game.getNumOfPlayers(), game.getPlayersNickname());
        }
        for(int i = 0; i < game.getNumOfPlayers(); i++){
            controllerGame.sendLeaderCards(i, game.casualLeaderCards());
        }
    }

    public void leaderCardHandler(int player, int card1, int card2, ArrayList<LeaderCard> leaderCards) throws IllegalStateException{
        if(game.alreadySelectedLeaderCards(player))
            throw new IllegalStateException();
        LeaderCard leaderCard1 = chosenLeaderCard(card1, leaderCards);
        LeaderCard leaderCard2 = chosenLeaderCard(card2, leaderCards);
        game.selectPlayerLeaderCards(leaderCard1, leaderCard2, player);
        if(game.allPlayersReady()) {
            currentState.nextState(this, MessageType.BUY_CARD);
            controllerGame.playersReady();
        }
    }

    private LeaderCard chosenLeaderCard(int choice, ArrayList<LeaderCard> leaderCards){
        for (LeaderCard leaderCard: leaderCards)
            if(leaderCard.getCardID() == choice)
                return leaderCard;
        return null;
    }

    public void oneResourceHandle(int player, Resource resource) throws IllegalStateException {
        if(game.alreadySelectedResource(player))
            throw new IllegalStateException();
        game.firstIncreaseWarehouse(resource, player);
        if(game.allPlayersReady()) {
            currentState.nextState(this, MessageType.BUY_CARD);
            controllerGame.playersReady();
        }
    }

    public void twoResourceHandle(int player, Resource r1, Resource r2) throws IllegalStateException {
        if(game.alreadySelectedResource(player))
            throw new IllegalStateException();
        game.firstDoubleIncreaseWarehouse(r1, r2);
        if(game.allPlayersReady()) {
            currentState.nextState(this, MessageType.BUY_CARD);
            controllerGame.playersReady();
        }
    }

    public boolean buyCardHandler(int row, int column, int choice)
            throws IllegalStateException, WrongParametersException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, InsufficientResourceException {
        if (!currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            throw new IllegalStateException();
        if (!InputController.buy_card_check(row, column, choice))
            throw new WrongParametersException();
        row--;
        column--;
        ArrayList<Integer> availableSlots = game.findAvailableSlots(row, column);
        if (availableSlots.isEmpty()) {
            throw new ImpossibleDevelopmentCardAdditionException();
        }
        if (availableSlots.size() == 1) {
            game.buyDevelopmentCardFromMarket(row, column, choice, availableSlots.get(0));
            currentState.nextState(this, MessageType.END_TURN);
            return true;
        } else {
            currentState.nextState(this, MessageType.CHOSEN_SLOT);
            currentState.setRow(row);
            currentState.setColumn(column);
            currentState.setChoice(choice);
            currentState.setAvailableSlots(availableSlots);
            controllerGame.choseSlot(game.getCurrentPosition(), availableSlots);
            return false;
        }
    }

    public void chosenSlotHandler(int slot)
            throws IllegalStateException, WrongParametersException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, InsufficientResourceException {
        if (!currentState.isRightState(GameStates.BUY_CARD_STATE))
            throw new IllegalStateException();
        if (!InputController.chosen_slot_check(slot, currentState.getAvailableSlots()))
            throw new WrongParametersException();
        int row = currentState.getRow();
        int column = currentState.getColumn();
        int choice = currentState.getChoice();
        try {
            game.buyDevelopmentCardFromMarket(row, column, choice, slot);
            currentState.nextState(this, MessageType.END_TURN);
        } catch (InsufficientResourceException e) {
            currentState.nextState(this, MessageType.BUY_CARD);
            throw new InsufficientResourceException();
        }
    }

    public Marble[] takeMarbleHandler(int choice, int index) throws IllegalStateException, WrongParametersException {
        if (!currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            throw new IllegalStateException();
        if (!InputController.taken_marbles_check(choice, index))
            throw new WrongParametersException();
        boolean row = choice == 0 ? true : false;
        Marble[] marbles = game.takeMarketMarble(row, index);
        currentState.nextState(this, MessageType.USE_MARBLE);
        currentState.setMarbles(marbles);
        return marbles;
    }

    public boolean useMarbleHandler(Marble chosenMarble)
            throws IllegalStateException,  WrongParametersException {
        if (!currentState.isRightState(GameStates.TAKE_MARBLE_STATE))
            throw new IllegalStateException();
        if(!InputController.chosen_correct_marble(chosenMarble, currentState.getMarbles()))
            throw new WrongParametersException();
        currentState.removeMarble(chosenMarble);
        if (!chosenMarble.useMarble(game)) {
            if (currentState.getMarbles().size() == 0) {
                game.faithTrackMovementAllPlayers();
                currentState.nextState(this, MessageType.END_TURN);
            }
            else
                currentState.nextState(this, MessageType.USE_MARBLE);
            return true;
        } else {
            try {
                LeaderCard[] whiteConversionCards = game.getCurrentPlayerActiveLeaderCards();
                ArrayList<Marble> remainingMarbles = currentState.getMarbles();
                currentState.nextState(this, MessageType.WHITE_CONVERSION_CARD);
                currentState.setLeaderCards(whiteConversionCards);
                currentState.setMarbles(remainingMarbles);
                controllerGame.choseWhiteConversionCard(game.getCurrentPosition(), whiteConversionCards);
            } catch (AlreadyDiscardLeaderCardException e) {}
            return false;
        }
    }

    public void whiteConversionCardHandler(int chosenLeaderCard) throws IllegalStateException, WrongParametersException {
        if (!currentState.isRightState(GameStates.WHITE_CONVERSION_CARD_STATE))
            throw new IllegalStateException();
        if (!InputController.white_conversion_card_check(chosenLeaderCard))
            throw new WrongParametersException();
        LeaderCard leaderCard;
        if (chosenLeaderCard == 1)
            leaderCard = currentState.getLeaderCard1();
        else
            leaderCard = currentState.getLeaderCard2();
        game.whiteMarbleConversion(leaderCard);
        if (currentState.getMarbles().size() == 0) {
            game.faithTrackMovementAllPlayers();
            currentState.nextState(this, MessageType.END_TURN);
        }
        else {
            ArrayList<Marble> remainingMarbles = currentState.getMarbles();
            currentState.nextState(this, MessageType.USE_MARBLE);
            currentState.setMarbles(remainingMarbles);
        }
    }

    public void switchHandler(int depot1, int depot2) throws IllegalStateException, WrongParametersException, ImpossibleSwitchDepotException {
        if (!currentState.isRightState(GameStates.TAKE_MARBLE_STATE))
            throw new IllegalStateException();
        if (!InputController.switch_depot_check(depot1, depot2))
            throw new WrongParametersException();
        game.switchDepots(depot1, depot2);
    }

    public void developmentCardPowerHandler(int chosenSlot, int choice)
            throws IllegalStateException, WrongParametersException, InsufficientResourceException, NoSuchProductionPowerException, AlreadyUsedProductionPowerException {
        Strongbox s;
        if (currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            s = new Strongbox();
        else if (currentState.isRightState(GameStates.ACTIVATE_PRODUCTION_STATE))
            s = currentState.getStrongbox();
        else
            throw new IllegalStateException();
        if (!InputController.development_card_power_check(chosenSlot, choice))
            throw new WrongParametersException();
        if(!InputController.already_used_development_card_power_check(chosenSlot, currentState.getChosenSlots()))
            throw new AlreadyUsedProductionPowerException();
        game.removeDevelopmentCardProductionResource(chosenSlot, s, choice);
        currentState.nextState(this, MessageType.END_PRODUCTION);
        currentState.setStrongbox(s);
        currentState.addDevelopmentCardSlot(chosenSlot);
    }

    public void basicPowerHandler(Resource r1, Resource r2, Resource r3, int choice)
            throws InsufficientResourceException, IllegalStateException, WrongParametersException, AlreadyUsedProductionPowerException {
        Strongbox s;
        if (currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            s = new Strongbox();
        else if (currentState.isRightState(GameStates.ACTIVATE_PRODUCTION_STATE))
            s = currentState.getStrongbox();
        else
            throw new IllegalStateException();
        if (!InputController.basic_power_check(choice))
            throw new WrongParametersException();
        if(currentState.isBasicPower())
            throw new AlreadyUsedProductionPowerException();
        game.basicProductionPower(r1, r2, r3, s, choice);
        currentState.nextState(this, MessageType.END_PRODUCTION);
        currentState.setStrongbox(s);
        currentState.setBasicPower();
    }

    public void leaderCardPowerHandler(int chosenLeaderCard, Resource r, int choice)
            throws InsufficientResourceException, IllegalStateException, WrongParametersException, NoSuchProductionPowerException, AlreadyUsedProductionPowerException {
        Strongbox s;
        if (currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            s = new Strongbox();
        else if (currentState.isRightState(GameStates.ACTIVATE_PRODUCTION_STATE))
            s = currentState.getStrongbox();
        else
            throw new IllegalStateException();
        if (!InputController.leader_card_power_check(chosenLeaderCard, choice))
            throw new WrongParametersException();
        if(!InputController.already_used_leader_card_power_check(chosenLeaderCard, currentState.getChosenLeaderCards()))
            throw new AlreadyUsedProductionPowerException();
        game.removeAdditionalProductionPowerCardResource(chosenLeaderCard, r, s, choice);
        currentState.nextState(this, MessageType.END_PRODUCTION);
        currentState.setStrongbox(s);
        currentState.addLeaderCard(chosenLeaderCard);
    }

    public void endProductionHandler() throws IllegalStateException {
        if (!currentState.isRightState(GameStates.ACTIVATE_PRODUCTION_STATE))
            throw new IllegalStateException();
        Strongbox s = currentState.getStrongbox();
        game.increaseCurrentPlayerStrongbox(s);
        currentState.nextState(this, MessageType.END_TURN);
    }

    public void leaderActivationHandler(int chosenLeaderCard)
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException, IllegalStateException, WrongParametersException {
        if(!InputController.leader_card_activation(chosenLeaderCard))
            throw new WrongParametersException();
        if (currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            currentState.nextState(this, MessageType.BUY_CARD);
        else if (currentState.isRightState(GameStates.END_TURN_STATE))
            currentState.nextState(this, MessageType.END_TURN);
        else
            throw new IllegalStateException();
        game.activateLeaderCard(chosenLeaderCard);
    }

    public void leaderDiscardHandler(int chosenLeaderCard)
            throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException, IllegalStateException, WrongParametersException {
        if(!InputController.leader_card_discard(chosenLeaderCard))
            throw new WrongParametersException();
        if (currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            currentState.nextState(this, MessageType.BUY_CARD);
        else if (currentState.isRightState(GameStates.END_TURN_STATE))
            currentState.nextState(this, MessageType.END_TURN);
        else
            throw new IllegalStateException();
        game.discardLeaderCard(chosenLeaderCard);
    }

    public void endTurnHandler() throws IllegalStateException {
        if(!currentState.isRightState(GameStates.END_TURN_STATE))
            throw new IllegalStateException();
        currentState.nextState(this, MessageType.BUY_CARD);
        if(game.isEndGame())
            controllerGame.endGame();
        game.nextPlayer();
        if(game.isEndGame())
            controllerGame.endGame();
    }

    public void endGame(){
        game.endGame();
    }
}
