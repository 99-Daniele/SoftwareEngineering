package it.polimi.ingsw.model.games.states;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.games.GameManager;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * FirstActionState is one of game states.
 * In this state current player can only send messages about the first action of its turn, any other message will be discarded.
 */
public class FirstActionState implements GameState {

    /**
     * @param gameManager is the GameManager of the game.
     * @param wantedMessage is the type of message Server wants from Client.
     *
     * based on different @param wantedMessage go to a different game state.
     */
    @Override
    public void nextState(GameManager gameManager, MessageType wantedMessage){
        switch (wantedMessage){
            case USE_MARBLE:
                gameManager.setCurrentState(new TakeMarbleState());
                break;
            case CHOSEN_SLOT:
                gameManager.setCurrentState(new BuyCardState());
                break;
            case END_PRODUCTION:
                gameManager.setCurrentState(new ActivateProductionState());
                break;
            case END_TURN:
                gameManager.setCurrentState(new EndTurnState());
                break;
        }
    }

    @Override
    public boolean isRightState(GameStates state) {
        return state == GameStates.FIRST_ACTION_STATE;
    }

    @Override
    public Strongbox getStrongbox() {
        return null;
    }

    @Override
    public void setStrongbox(Strongbox s) {}

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
