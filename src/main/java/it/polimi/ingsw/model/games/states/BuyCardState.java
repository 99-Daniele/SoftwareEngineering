package it.polimi.ingsw.model.games.states;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.games.GameManager;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public class BuyCardState implements GameState {

    private int row;
    private int column;
    private int choice;
    private ArrayList<Integer> availableSlots = new ArrayList<>();

    @Override
    public void nextState(GameManager gameManager, MessageType wantedMessage) {
        if(wantedMessage == MessageType.BUY_CARD)
            gameManager.setCurrentState(new FirstActionState());
        else
            gameManager.setCurrentState(new EndTurnState());
    }

    @Override
    public boolean isRightState(GAME_STATES state) {
        return state == GAME_STATES.BUY_CARD_STATE;
    }

    @Override
    public ArrayList<LeaderCard> getLeaderCards(int position) {
        return null;
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
