package it.polimi.ingsw.controllerTests.states;

import it.polimi.ingsw.controllerTests.ControllerGame;
import it.polimi.ingsw.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.market.Marble;
import it.polimi.ingsw.player.Strongbox;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

public interface State_Controller {

    void nextState (ControllerGame controllerGame, MessageType wantedMessage);

    boolean isRightState (CONTROLLER_STATES state);

    Strongbox getStrongbox();

    void setStrongbox(Strongbox s);

    int getRow();

    int getColumn();

    int getChoice();

    void setRow(int row);

    void setColumn(int column);

    void setChoice(int choice);

    LeaderCard getLeaderCard1();

    LeaderCard getLeaderCard2();

    void setLeaderCards(LeaderCard[] leaderCards);

    ArrayList<Integer> getAvailableSlots();

    void setAvailableSlots(ArrayList<Integer> availableSlots);

    ArrayList<Marble> getMarbles();

    void setMarbles(Marble[] marbles);

    void setMarbles(ArrayList<Marble> marbles);

    void removeMarble(Marble m);

}
