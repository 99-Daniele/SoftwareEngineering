package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public class EndTurnState implements State_Controller{

    @Override
    public State_Controller nextState(MessageType wantedMessage) {
        if(wantedMessage == MessageType.END_TURN)
            return this;
        else
            return new FirstActionState();
    }

    @Override
    public boolean isRightState(CONTROLLER_STATES state) {
        return state == CONTROLLER_STATES.END_TURN_STATE;
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
