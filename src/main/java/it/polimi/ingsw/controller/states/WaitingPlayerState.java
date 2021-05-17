package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public class WaitingPlayerState implements State_Controller {

    private ArrayList<Integer> playerChosenLeaderCards = new ArrayList<>();
    private ArrayList<Integer> playerChosenResource = new ArrayList<>();
    private ArrayList<ArrayList<LeaderCard>> leaderCards = new ArrayList<>();

    @Override
    public void nextState(ControllerGame controllerGame, MessageType wantedMessage) {
        if(wantedMessage != MessageType.LOGIN)
            controllerGame.setCurrentState(new FirstActionState());
    }

    @Override
    public boolean isRightState(CONTROLLER_STATES state) {
        return state == CONTROLLER_STATES.WAITING_PLAYERS_STATE;
    }

    @Override
    public void putPlayerLeaderCards(int position){
        playerChosenLeaderCards.add(position);
    }

    @Override
    public void putPlayerResource(int position){
        playerChosenResource.add(position);
    }

    @Override
    public void putLeaderCards(ArrayList<LeaderCard> leaderCards){
        this.leaderCards.add(leaderCards);
    }

    @Override
    public ArrayList<Integer> getPlayerChosenLeaderCards() {
        return playerChosenLeaderCards;
    }

    @Override
    public ArrayList<Integer> getPlayerChosenResource() {
        return playerChosenResource;
    }

    @Override
    public ArrayList<LeaderCard> getLeaderCards(int position) {
        return leaderCards.get(position);
    }

    @Override
    public Strongbox getStrongbox() {
        return null;
    }

    @Override
    public void setStrongbox(Strongbox s) {
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
