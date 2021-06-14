package it.polimi.ingsw.model.games.states;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.games.GameManager;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public class ActivateProductionState implements GameState {

    private Strongbox s;
    private final ArrayList<Integer> chosenSlots = new ArrayList<>();
    private boolean basicPower = false;
    private final ArrayList<Integer> chosenLeaderCards = new ArrayList<>();

    @Override
    public void nextState(GameManager gameManager, MessageType wantedMessage) {
        if(wantedMessage == MessageType.END_TURN)
            gameManager.setCurrentState(new EndTurnState());
    }

    @Override
    public boolean isRightState(GameStates state) {
        return state == GameStates.ACTIVATE_PRODUCTION_STATE;
    }

    @Override
    public Strongbox getStrongbox() {
        return s;
    }

    @Override
    public void setStrongbox(Strongbox s) {
        this.s = s;
    }

    @Override
    public void addDevelopmentCardSlot(int slot){
        this.chosenSlots.add(slot);
    }

    @Override
    public ArrayList<Integer> getChosenSlots() {
        return chosenSlots;
    }

    @Override
    public boolean isBasicPower() {
        return basicPower;
    }

    @Override
    public void setBasicPower() {
        this.basicPower = true;
    }

    @Override
    public ArrayList<Integer> getChosenLeaderCards() {
        return chosenLeaderCards;
    }

    @Override
    public void addLeaderCard(int chosenLeaderCard) {
        this.chosenLeaderCards.add(chosenLeaderCard);
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
