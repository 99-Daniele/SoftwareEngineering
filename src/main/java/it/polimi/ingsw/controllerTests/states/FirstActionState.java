package it.polimi.ingsw.controllerTests.states;

import it.polimi.ingsw.controllerTests.ControllerGame;
import it.polimi.ingsw.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.market.Marble;
import it.polimi.ingsw.player.Strongbox;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public class FirstActionState implements State_Controller{

    @Override
    public void nextState(ControllerGame controllerGame, MessageType wantedMessage){
        switch (wantedMessage){
            case USE_MARBLE:
                controllerGame.setCurrentState(new TakeMarbleState());
                break;
            case CHOSEN_SLOT:
                controllerGame.setCurrentState(new BuyCardState());
                break;
            case END_PRODUCTION:
                controllerGame.setCurrentState(new ActivateProductionState());
                break;
            case END_TURN:
                controllerGame.setCurrentState(new EndTurnState());
                break;
        }
    }

    @Override
    public boolean isRightState(CONTROLLER_STATES state) {
        return state == CONTROLLER_STATES.FIRST_ACTION_STATE;
    }

    @Override
    public Strongbox getStrongbox() {
        return null;
    }

    @Override
    public void setStrongbox(Strongbox s) {}

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
