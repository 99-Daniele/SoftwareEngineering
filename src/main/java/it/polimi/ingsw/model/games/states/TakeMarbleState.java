package it.polimi.ingsw.model.games.states;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.games.GameManager;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.Collections;

/**
 * TakeMarbleState is one of game states.
 * In this state current player can only send use marble or switch messages, any other message will be discarded.
 * This state saves the chosen marbles to avoid player uses wrong ones.
 */
public class TakeMarbleState implements GameState {

    private ArrayList<Marble> marbles = new ArrayList<>();

    /**
     * @param gameManager is the GameManager of the game.
     * @param wantedMessage is the type of message Server wants from Client.
     *
     * if @param wantedMessage = WHITE_CONVERSION_CARD switch to WhiteConversionCardState, in any other case go to EndTurnState.
     */
    @Override
    public void nextState(GameManager gameManager, MessageType wantedMessage) {
        if(wantedMessage == MessageType.WHITE_CONVERSION_CARD)
            gameManager.setCurrentState(new WhiteConversionCardState());
        else if(wantedMessage == MessageType.END_TURN)
            gameManager.setCurrentState(new EndTurnState());
    }

    @Override
    public boolean isRightState(GameStates state) {
        return state == GameStates.TAKE_MARBLE_STATE;
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
        return -1;
    }

    @Override
    public int getColumn() {
        return -1;
    }

    @Override
    public int getChoice() {
        return -1;
    }

    @Override
    public void setRow(int row) {

    }

    @Override
    public void setColumn(int column) {

    }

    @Override
    public void setChoice(int choice) {

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
        return null;
    }

    @Override
    public void setAvailableSlots(ArrayList<Integer> availableSlots) {

    }

    @Override
    public ArrayList<Marble> getMarbles() {
        return marbles;
    }

    @Override
    public void setMarbles(Marble[] marbles) {
        ArrayList<Marble> newMarbles = new ArrayList<>();
        Collections.addAll(newMarbles, marbles);
        this.marbles = newMarbles;
    }

    @Override
    public void setMarbles(ArrayList<Marble> marbles){this.marbles = marbles;}

    @Override
    public void removeMarble(Marble m) {
        for(Marble marble: marbles) {
            if (marble.toString().equals(m.toString())) {
                marbles.remove(marble);
                break;
            }
        }
    }
}
