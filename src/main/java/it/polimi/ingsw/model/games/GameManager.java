package it.polimi.ingsw.model.games;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.controller.GameStartingState;
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

/**
 * GameManager handle all different phases and states of the game, avoiding players to make illegal operations.
 */
public class GameManager {

    private final Game game;
    private final ControllerGame controllerGame;
    private GameState currentState;

    /**
     * @param numPlayers is the number of players of the game
     * @param controllerGame is the controllerGame
     *
     * if @param numPlayers == 1 create a new SinglePlayerGame, otherwise create a new Game. Then set currentState first action.
     */
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

    /**
     * @param view is player's view.
     * @param nickName is player's nickname.
     * @throws AlreadyTakenNicknameException if player has chosen an already taken nickname.
     *
     * tries tp create a new player if nickname has not chosen yet and then add his view to game observers.
     */
    public synchronized void createPlayer(View view, String nickName) throws AlreadyTakenNicknameException{
        game.createPlayer(nickName);
        game.addObserver((VirtualView) view);
    }

    public void deletePlayer(String nickName){
        game.deletePlayer(nickName);
    }

    public String getPlayerPosition(int position){
        return game.getPlayerPosition(position);
    }

    /**
     * if game has reached the correct num of players, send to everyone nickname of all players and 4 different leader cards.
     */
    public void startGame(){
        if(!game.allPlayersConnected())
            return;
        controllerGame.startGame();
        for(int i = 0; i < game.getNumOfPlayers(); i++){
            controllerGame.sendNickNames(i, game.getPlayersNickname());
            controllerGame.sendLeaderCards(i, game.casualLeaderCards());
        }
    }

    /**
     * @param player is one player.
     * @param card1 is one LeaderCard.
     * @param card2 is one LeaderCard.
     * @param leaderCards is the list of leader cards from which player could choose.
     * @throws IllegalStateException if player has already chosen his leader cards or game already started.
     *
     * if player has not already chosen his leader cards, add to his playerboard. If all players have made their choices,
     * start the game.
     */
    public void leaderCardHandler(int player, int card1, int card2, ArrayList<LeaderCard> leaderCards) throws IllegalStateException{
        if(game.alreadySelectedLeaderCards(player) || controllerGame.getState() == GameStartingState.START_GAME)
            throw new IllegalStateException();
        LeaderCard leaderCard1 = chosenLeaderCard(card1, leaderCards);
        LeaderCard leaderCard2 = chosenLeaderCard(card2, leaderCards);
        game.selectPlayerLeaderCards(leaderCard1, leaderCard2, player);
        if(game.allPlayersReady()) {
            currentState.nextState(this, MessageType.BUY_CARD);
            controllerGame.playersReady();
        }
    }

    /**
     * @param choice is player's chosen LeaderCard cardID.
     * @param leaderCards is the list of leader cards from which player could choose.
     * @return the chosen LeaderCard.
     */
    private LeaderCard chosenLeaderCard(int choice, ArrayList<LeaderCard> leaderCards){
        for (LeaderCard leaderCard: leaderCards)
            if(leaderCard.getCardID() == choice)
                return leaderCard;
        return null;
    }

    /**
     * @param player is one player.
     * @param resource is player's first chosen resource.
     * @throws IllegalStateException if player has already chosen his first resource or game already started.
     *
     * if player has not already chosen his resource, add to his warehouse. If all players have made their choices,
     * start the game.
     */
    public void oneResourceHandle(int player, Resource resource) throws IllegalStateException {
        if(game.alreadySelectedResource(player) || controllerGame.getState() == GameStartingState.START_GAME)
            throw new IllegalStateException();
        game.firstIncreaseWarehouse(resource, player);
        if(game.allPlayersReady()) {
            currentState.nextState(this, MessageType.BUY_CARD);
            controllerGame.playersReady();
        }
    }

    /**
     * @param player is one player.
     * @param r1 is player's first chosen resource.
     * @param r2 is player's second chosen resource.
     * @throws IllegalStateException if player has already chosen his first resource or game already started.
     *
     * if player has not already chosen his resources, add to his warehouse. If all players have made their choices,
     * start the game.
     */
    public void twoResourceHandle(int player, Resource r1, Resource r2) throws IllegalStateException {
        if(game.alreadySelectedResource(player) || controllerGame.getState() == GameStartingState.START_GAME)
            throw new IllegalStateException();
        game.firstDoubleIncreaseWarehouse(r1, r2);
        if(game.allPlayersReady()) {
            currentState.nextState(this, MessageType.BUY_CARD);
            controllerGame.playersReady();
        }
    }

    /**
     * @param row is DevelopmentCard deck's row
     * @param column is DevelopmentCard deck's column.
     * @param choice refers to which between strongbox and warehouse has the priority to be decreased.
     * @return true if card has been bought, instead @return false if there are two or three available slots.
     * @throws IllegalStateException if it's not the right turn state.
     * @throws WrongParametersException if player has sent wrong parameters.
     * @throws EmptyDevelopmentCardDeckException if player has chosen an empty deck.
     * @throws ImpossibleDevelopmentCardAdditionException if player has not available slots to insert card.
     * @throws InsufficientResourceException if player has not enough resources.
     *
     * if there are two or three available slots to buy the card, change currentState to BUY_CARD_STATE and save all data
     * about player's row and column choices.
     */
    public boolean buyCardHandler(int row, int column, int choice)
            throws IllegalStateException, WrongParametersException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, InsufficientResourceException {
        if (!currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            throw new IllegalStateException();
        if (!InputController.buyCardCheck(row, column, choice))
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

    /**
     * @param slot is player's chosen slot.
     * @throws IllegalStateException if it's not the right turn state.
     * @throws WrongParametersException if player has sent wrong parameters.
     * @throws EmptyDevelopmentCardDeckException if player has chosen an empty deck.
     * @throws ImpossibleDevelopmentCardAdditionException if player has not available slots to insert card.
     * @throws InsufficientResourceException if player has not enough resources.
     *
     * if player has enough resources set currentState to END_TURN_STATE, otherwise return to FIRST_ACTION_STATE.
     */
    public void chosenSlotHandler(int slot)
            throws IllegalStateException, WrongParametersException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, InsufficientResourceException {
        if (!currentState.isRightState(GameStates.BUY_CARD_STATE))
            throw new IllegalStateException();
        if (!InputController.chosenSlotCheck(slot, currentState.getAvailableSlots()))
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

    /**
     * @param choice == 0 if player has chosen a market row, or @param choice == 1 if player has chosen market column.
     * @param index is player's chosen row or column.
     * @return the chosen marbles.
     * @throws IllegalStateException if it's not the right turn state.
     * @throws WrongParametersException if player has sent wrong parameters.
     *
     * set currentState to TAKE_MARBLE_STATE and save data about chosen marbles.
     */
    public Marble[] takeMarbleHandler(int choice, int index) throws IllegalStateException, WrongParametersException {
        if (!currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            throw new IllegalStateException();
        if (!InputController.takenMarblesCheck(choice, index))
            throw new WrongParametersException();
        boolean row = choice == 0;
        Marble[] marbles = game.takeMarketMarble(row, index);
        currentState.nextState(this, MessageType.USE_MARBLE);
        currentState.setMarbles(marbles);
        return marbles;
    }

    /**
     * @param chosenMarble is player's chosen marble.
     * @return false if player has chosen a white marble and it own two active WhiteConversionCard, otherwise @return true.
     * @throws IllegalStateException if it's not the right turn state.
     * @throws WrongParametersException if player has sent wrong parameters.
     *
     * if player has chosen a white marble and player has two active WhiteConversionCard, setCurrentState to
     * WHITE_CONVERSION_STATE and save data about chosen marbles.
     * in any other cases, use marble, remove from chosen marbles and in case it was the last marble, set currenStae to
     * END_TURN_STATE.
     */
    public boolean useMarbleHandler(Marble chosenMarble)
            throws IllegalStateException,  WrongParametersException {
        if (!currentState.isRightState(GameStates.TAKE_MARBLE_STATE))
            throw new IllegalStateException();
        if(!InputController.chosenCorrectMarble(chosenMarble, currentState.getMarbles()))
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
            } catch (AlreadyDiscardLeaderCardException ignored) {}
            return false;
        }
    }

    /**
     * @param chosenLeaderCard is player's chosen LeaderCard (1 or 2).
     * @throws IllegalStateException if it's not the right turn state.
     * @throws WrongParametersException if player has sent wrong parameters.
     *
     * if there weren't any remaining marble set currentState to END_TURN_STATE, otherwise return to TAKE_MARBLE_STATE.
     */
    public void whiteConversionCardHandler(int chosenLeaderCard) throws IllegalStateException, WrongParametersException {
        if (!currentState.isRightState(GameStates.WHITE_CONVERSION_CARD_STATE))
            throw new IllegalStateException();
        if (!InputController.whiteConversionCardCheck(chosenLeaderCard))
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

    /**
     * @param depot1 is player's first chosen depot.
     * @param depot2 is player's second chosen depot.
     * @throws IllegalStateException if it's not the right turn state.
     * @throws WrongParametersException if player has sent wrong parameters.
     * @throws ImpossibleSwitchDepotException if player can't switch the chosen depots.
     */
    public void switchHandler(int depot1, int depot2) throws IllegalStateException, WrongParametersException, ImpossibleSwitchDepotException {
        if (!currentState.isRightState(GameStates.TAKE_MARBLE_STATE))
            throw new IllegalStateException();
        if (!InputController.switchDepotCheck(depot1, depot2))
            throw new WrongParametersException();
        game.switchDepots(depot1, depot2);
    }

    /**
     * @param chosenSlot is player's chosen card to activate production power.
     * @param choice refers to which between strongbox and warehouse has the priority to be decreased.
     * @throws IllegalStateException if it's not the right turn state.
     * @throws WrongParametersException if player has sent wrong parameters.
     * @throws InsufficientResourceException if player hos not enough resources.
     * @throws NoSuchProductionPowerException if player has chosen an empty SlotDevelopmentCard.
     * @throws AlreadyUsedProductionPowerException if player has already chosen this power production.
     *
     * if currentState is FIRST_ACTION_STATE create a new Strongbox otherwise get the saved one. Then decrease player
     * resources by the amount required and increase this Strongbox with the amount given and then saved Strongbox.
     * also set currentState to ACTIVATE_PRODUCTION_STATE.
     */
    public void developmentCardPowerHandler(int chosenSlot, int choice)
            throws IllegalStateException, WrongParametersException, InsufficientResourceException, NoSuchProductionPowerException, AlreadyUsedProductionPowerException {
        Strongbox s;
        if (currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            s = new Strongbox();
        else if (currentState.isRightState(GameStates.ACTIVATE_PRODUCTION_STATE))
            s = currentState.getStrongbox();
        else
            throw new IllegalStateException();
        if (!InputController.developmentCardPowerCheck(chosenSlot, choice))
            throw new WrongParametersException();
        if(!InputController.alreadyUsedDevelopmentCardPowerCheck(chosenSlot, currentState.getChosenSlots()))
            throw new AlreadyUsedProductionPowerException();
        game.removeDevelopmentCardProductionResource(chosenSlot, s, choice);
        currentState.nextState(this, MessageType.END_PRODUCTION);
        currentState.setStrongbox(s);
        currentState.addDevelopmentCardSlot(chosenSlot);
    }

    /**
     * @param r1 is player's chosen resource to be decreased.
     * @param r2 is player's chosen resource to be decreased.
     * @param r3 is player's chosen resource to be increased.
     * @param choice refers to which between strongbox and warehouse has the priority to be decreased.
     * @throws IllegalStateException if it's not the right turn state.
     * @throws WrongParametersException if player has sent wrong parameters.
     * @throws InsufficientResourceException if player hos not enough resources.
     * @throws AlreadyUsedProductionPowerException if player has already chosen this power production.
     *
     * if currentState is FIRST_ACTION_STATE create a new Strongbox otherwise get the saved one. Then decrease player
     * resources by the amount required and increase this Strongbox with the amount given and then saved Strongbox.
     * also set currentState to ACTIVATE_PRODUCTION_STATE.
     */
    public void basicPowerHandler(Resource r1, Resource r2, Resource r3, int choice)
            throws InsufficientResourceException, IllegalStateException, WrongParametersException, AlreadyUsedProductionPowerException {
        Strongbox s;
        if (currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            s = new Strongbox();
        else if (currentState.isRightState(GameStates.ACTIVATE_PRODUCTION_STATE))
            s = currentState.getStrongbox();
        else
            throw new IllegalStateException();
        if (!InputController.basicPowerCheck(choice))
            throw new WrongParametersException();
        if(currentState.isBasicPower())
            throw new AlreadyUsedProductionPowerException();
        game.basicProductionPower(r1, r2, r3, s, choice);
        currentState.nextState(this, MessageType.END_PRODUCTION);
        currentState.setStrongbox(s);
        currentState.setBasicPower();
    }

    /**
     * @param chosenLeaderCard is player's chosen LeaderCard (1 or 2)
     * @param r is player's chosen resource to be added.
     * @param choice refers to which between strongbox and warehouse has the priority to be decreased.
     * @throws IllegalStateException if it's not the right turn state.
     * @throws WrongParametersException if player has sent wrong parameters.
     * @throws InsufficientResourceException if player hos not enough resources.
     * @throws NoSuchProductionPowerException if player has chosen an empty SlotDevelopmentCard.
     * @throws AlreadyUsedProductionPowerException if player has already chosen this power production.
     *
     * if currentState is FIRST_ACTION_STATE create a new Strongbox otherwise get the saved one. Then decrease player
     * resources by the amount required and increase this Strongbox with the amount given and then saved Strongbox.
     * also set currentState to ACTIVATE_PRODUCTION_STATE.
     */
    public void leaderCardPowerHandler(int chosenLeaderCard, Resource r, int choice)
            throws InsufficientResourceException, IllegalStateException, WrongParametersException, NoSuchProductionPowerException, AlreadyUsedProductionPowerException {
        Strongbox s;
        if (currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            s = new Strongbox();
        else if (currentState.isRightState(GameStates.ACTIVATE_PRODUCTION_STATE))
            s = currentState.getStrongbox();
        else
            throw new IllegalStateException();
        if (!InputController.leaderCardPowerCheck(chosenLeaderCard, choice))
            throw new WrongParametersException();
        if(!InputController.alreadyUsedLeaderCardPowerCheck(chosenLeaderCard, currentState.getChosenLeaderCards()))
            throw new AlreadyUsedProductionPowerException();
        game.removeAdditionalProductionPowerCardResource(chosenLeaderCard, r, s, choice);
        currentState.nextState(this, MessageType.END_PRODUCTION);
        currentState.setStrongbox(s);
        currentState.addLeaderCard(chosenLeaderCard);
    }

    /**
     * @throws IllegalStateException if it's not the right turn state.
     *
     * set currentState to END_TURN_STATE.
     */
    public void endProductionHandler() throws IllegalStateException {
        if (!currentState.isRightState(GameStates.ACTIVATE_PRODUCTION_STATE))
            throw new IllegalStateException();
        Strongbox s = currentState.getStrongbox();
        game.increaseCurrentPlayerStrongbox(s);
        currentState.nextState(this, MessageType.END_TURN);
    }

    /**
     * @param chosenLeaderCard is player's chosen LeaderCard.
     * @throws InsufficientResourceException if player has not enough resources.
     * @throws AlreadyDiscardLeaderCardException if player has not enough DevelopmentCards.
     * @throws ActiveLeaderCardException if player has already activated this LeaderCard previously.
     * @throws InsufficientCardsException if player has already discarded this LeaderCard previously.
     * @throws IllegalStateException if it's not the right turn state.
     * @throws WrongParametersException if player has sent wrong parameters.
     */
    public void leaderActivationHandler(int chosenLeaderCard)
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException, IllegalStateException, WrongParametersException {
        if(!InputController.leaderCardActivation(chosenLeaderCard))
            throw new WrongParametersException();
        if (currentState.isRightState(GameStates.FIRST_ACTION_STATE))
            currentState.nextState(this, MessageType.BUY_CARD);
        else if (currentState.isRightState(GameStates.END_TURN_STATE))
            currentState.nextState(this, MessageType.END_TURN);
        else
            throw new IllegalStateException();
        game.activateLeaderCard(chosenLeaderCard);
    }


    /**
     * @param chosenLeaderCard is player's chosen LeaderCard.
     * @throws AlreadyDiscardLeaderCardException if player has not enough DevelopmentCards.
     * @throws ActiveLeaderCardException if player has already activated this LeaderCard previously.
     * @throws IllegalStateException if it's not the right turn state.
     * @throws WrongParametersException if player has sent wrong parameters.
     */
    public void leaderDiscardHandler(int chosenLeaderCard)
            throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException, IllegalStateException, WrongParametersException {
        if(!InputController.leaderCardDiscard(chosenLeaderCard))
            throw new WrongParametersException();
        if (!currentState.isRightState(GameStates.FIRST_ACTION_STATE) && !currentState.isRightState(GameStates.END_TURN_STATE))
            throw new IllegalStateException();
        game.discardLeaderCard(chosenLeaderCard);
    }

    /**
     * @throws IllegalStateException if it's not the right turn state.
     */
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