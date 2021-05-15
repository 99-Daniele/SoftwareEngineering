package it.polimi.ingsw.controller.states;

import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;
import java.util.Collections;

public class TakeMarbleState implements State_Controller{

    private ArrayList<Marble> marbles = new ArrayList<>();

    @Override
    public State_Controller nextState (MessageType wantedMessage) {
        if(wantedMessage == MessageType.USE_MARBLE)
            return this;
        else if(wantedMessage == MessageType.WHITE_CONVERSION_CARD)
            return new WhiteConversionCardState();
        else
            return new EndTurnState();
    }

    @Override
    public boolean isRightState(CONTROLLER_STATES state) {
        return state == CONTROLLER_STATES.TAKE_MARBLE_STATE;
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
        return marbles;
    }

    @Override
    public void setMarbles(Marble[] marbles) {
        Collections.addAll(this.marbles, marbles);
    }

    @Override
    public void setMarbles(ArrayList<Marble> marbles){this.marbles = marbles;}

    @Override
    public void removeMarble(Marble m) {
        for(Marble marble: marbles) {
            if (marble.equals(m)) {
                marbles.remove(marble);
                break;
            }
        }
    }
}
