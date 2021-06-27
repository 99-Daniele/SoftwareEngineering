package it.polimi.ingsw.model.games.states;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.games.GameManager;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * BuyCardState is one of game states.
 * In this state current player can only send chosen slot message, any other message will be discarded.
 * This state saves player's chosen deck row and column, choice about which between strongbox and warehouse has the priority
 * to be decreased, and the available slots where player could insert the chosen card, to avoid it choices a wrong slot.
 */
public class BuyCardState implements GameState {

    private int row;
    private int column;
    private int choice;
    private ArrayList<Integer> availableSlots = new ArrayList<>();

    /**
     * @param gameManager is the GameManager of the game.
     * @param wantedMessage is the type of message Server wants from Client.
     *
     * if @param wantedMessage = BUY_CARD switch to FirstActionState, in any other case go to EndTurnState.
     */
    @Override
    public void nextState(GameManager gameManager, MessageType wantedMessage) {
        if(wantedMessage == MessageType.BUY_CARD)
            gameManager.setCurrentState(new FirstActionState());
        else
            gameManager.setCurrentState(new EndTurnState());
    }

    @Override
    public boolean isRightState(GameStates state) {
        return state == GameStates.BUY_CARD_STATE;
    }

    @Override
    public Strongbox getStrongbox() {
        return null;
    }

    @Override
    public void setStrongbox(Strongbox s) {

    }

    @Override
    public ArrayList<Integer> getChosenSlots() {
        return null;
    }

    @Override
    public void addDevelopmentCardSlot(int slot) {

    }

    @Override
    public boolean isBasicPower() {
        return false;
    }

    @Override
    public void setBasicPower() {

    }

    @Override
    public ArrayList<Integer> getChosenLeaderCards() {
        return null;
    }

    @Override
    public void addLeaderCard(int chosenLeaderCard) {
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public int getChoice() {
        return choice;
    }

    @Override
    public void setRow(int row) {
        this.row = row;
    }

    @Override
    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public void setChoice(int choice) {
        this.choice = choice;
    }

    @Override
    public LeaderCard getLeaderCard1(){
        return null;
    }

    @Override
    public LeaderCard getLeaderCard2(){
        return null;
    }

    @Override
    public void setLeaderCards(LeaderCard[] leaderCards){

    }

    @Override
    public ArrayList<Integer> getAvailableSlots() {
        return availableSlots;
    }

    @Override
    public void setAvailableSlots(ArrayList<Integer> availableSlots) {
        this.availableSlots = availableSlots;
    }

    @Override
    public ArrayList<Marble> getMarbles() {
        return null;
    }

    @Override
    public void setMarbles(Marble[] marbles) {

    }

    @Override
    public void setMarbles(ArrayList<Marble> marbles) {

    }

    @Override
    public void removeMarble(Marble m) {

    }
}
