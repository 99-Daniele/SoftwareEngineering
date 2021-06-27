package it.polimi.ingsw.model.games.states;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.games.GameManager;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.network.messages.MessageType;

import java.util.ArrayList;

/**
 * GameState is the generic interface for game state.
 * All methods could be override by different game state classes.
 */
public interface GameState {

    /**
     * @param gameManager is the GameManager of the game.
     * @param wantedMessage is the type of message Server wants from Client.
     *
     * this method switch @param gameManager currentStare to the next state of the game, based on @param wantedMessage.
     */
    void nextState (GameManager gameManager, MessageType wantedMessage);

    /**
     * @param state is one game state.
     * @return if @param state is the current state of the game.
     */
    boolean isRightState (GameStates state);

    Strongbox getStrongbox();

    void setStrongbox(Strongbox s);

    ArrayList<Integer> getChosenSlots();

    void addDevelopmentCardSlot(int slot);

    boolean isBasicPower();

    void setBasicPower();

    ArrayList<Integer> getChosenLeaderCards();

    void addLeaderCard(int chosenLeaderCard);

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
