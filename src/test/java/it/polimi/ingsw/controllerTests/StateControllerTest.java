package it.polimi.ingsw.controllerTests;

import it.polimi.ingsw.model.cards.leaderCards.AdditionalProductionPowerCard;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.controller.states.*;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.RedMarble;
import it.polimi.ingsw.model.market.ResourceMarble;
import it.polimi.ingsw.model.market.WhiteMarble;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class StateControllerTest {

    /**
     * test the correct transition to one state to the next one.
     */
    @Test
    void correctStateTransition(){

        ControllerGame controllerGame = new ControllerGame();
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.WAITING_PLAYERS_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.LOGIN);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.WAITING_PLAYERS_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.START_GAME);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.USE_MARBLE);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.TAKE_MARBLE_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.USE_MARBLE);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.TAKE_MARBLE_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.WHITE_CONVERSION_CARD);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.WHITE_CONVERSION_CARD_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.USE_MARBLE);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.TAKE_MARBLE_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.SWITCH_DEPOT);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.TAKE_MARBLE_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.END_TURN);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.END_TURN_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.END_TURN);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.END_TURN_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.END_TURN);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.END_TURN_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.BUY_CARD);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.END_TURN);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.END_TURN_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.BUY_CARD);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.CHOSEN_SLOT);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.BUY_CARD_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.END_TURN);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.END_TURN_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.BUY_CARD);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.BUY_CARD);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.END_PRODUCTION);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.DEVELOPMENT_CARD_POWER);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.BASIC_POWER);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.LEADER_CARD_POWER);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.END_PRODUCTION);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.END_TURN_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.BASIC_POWER);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE));

        controllerGame.getCurrentState().nextState(controllerGame, MessageType.END_TURN);
        assertTrue(controllerGame.getCurrentState().isRightState(CONTROLLER_STATES.END_TURN_STATE));
    }

    /**
     * test the correct overriding of State_Controller methods
     */
    @Test
    void correctOverriding(){

        State_Controller s1 = new WaitingPlayerState();
        State_Controller s2 = new FirstActionState();
        State_Controller s3 = new BuyCardState();
        State_Controller s4 = new TakeMarbleState();
        State_Controller s5 = new WhiteConversionCardState();
        State_Controller s6 = new ActivateProductionState();
        State_Controller s7 = new EndTurnState();

        assertTrue(s1.isRightState(CONTROLLER_STATES.WAITING_PLAYERS_STATE));
        assertTrue(s2.isRightState(CONTROLLER_STATES.FIRST_ACTION_STATE));
        assertTrue(s3.isRightState(CONTROLLER_STATES.BUY_CARD_STATE));
        assertTrue(s4.isRightState(CONTROLLER_STATES.TAKE_MARBLE_STATE));
        assertTrue(s5.isRightState(CONTROLLER_STATES.WHITE_CONVERSION_CARD_STATE));
        assertTrue(s6.isRightState(CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE));
        assertTrue(s7.isRightState(CONTROLLER_STATES.END_TURN_STATE));

        Strongbox s = new Strongbox();

        s1.setStrongbox(s);
        s2.setStrongbox(s);
        s3.setStrongbox(s);
        s4.setStrongbox(s);
        s5.setStrongbox(s);
        s6.setStrongbox(s);
        s7.setStrongbox(s);

        assertNull(s1.getStrongbox());
        assertNull(s2.getStrongbox());
        assertNull(s3.getStrongbox());
        assertNull(s4.getStrongbox());
        assertNull(s5.getStrongbox());
        assertSame(s, s6.getStrongbox());
        assertNull(s7.getStrongbox());

        int row = 1;
        int column = 2;
        int choice = 0;

        s1.setRow(row);
        s1.setColumn(column);
        s1.setChoice(choice);
        s2.setRow(row);
        s2.setColumn(column);
        s2.setChoice(choice);
        s3.setRow(row);
        s3.setColumn(column);
        s3.setChoice(choice);
        s4.setRow(row);
        s4.setColumn(column);
        s4.setChoice(choice);
        s5.setRow(row);
        s5.setColumn(column);
        s5.setChoice(choice);
        s6.setRow(row);
        s6.setColumn(column);
        s6.setChoice(choice);
        s7.setRow(row);
        s7.setColumn(column);
        s7.setChoice(choice);

        assertEquals(-1, s1.getRow());
        assertEquals(-1, s1.getColumn());
        assertEquals(-1, s1.getChoice());
        assertEquals(-1, s2.getRow());
        assertEquals(-1, s2.getColumn());
        assertEquals(-1, s2.getChoice());
        assertEquals(1, s3.getRow());
        assertEquals(2, s3.getColumn());
        assertEquals(0, s3.getChoice());
        assertEquals(-1, s4.getRow());
        assertEquals(-1, s4.getColumn());
        assertEquals(-1, s4.getChoice());
        assertEquals(-1, s5.getRow());
        assertEquals(-1, s5.getColumn());
        assertEquals(-1, s5.getChoice());
        assertEquals(-1, s6.getRow());
        assertEquals(-1, s6.getColumn());
        assertEquals(-1, s6.getChoice());
        assertEquals(-1, s7.getRow());
        assertEquals(-1, s7.getColumn());
        assertEquals(-1, s7.getChoice());

        Cost c = new Cost();
        LeaderCard l1 = new AdditionalProductionPowerCard(Resource.COIN, c, 1, 0);
        LeaderCard l2 = new AdditionalProductionPowerCard(Resource.COIN, c, 1, 0);
        LeaderCard[] leaderCards = new LeaderCard[2];
        leaderCards[0] = l1;
        leaderCards[1] = l2;

        s1.setLeaderCards(leaderCards);
        s2.setLeaderCards(leaderCards);
        s3.setLeaderCards(leaderCards);
        s4.setLeaderCards(leaderCards);
        s5.setLeaderCards(leaderCards);
        s6.setLeaderCards(leaderCards);
        s7.setLeaderCards(leaderCards);

        assertNull(s1.getLeaderCard1());
        assertNull(s1.getLeaderCard2());
        assertNull(s2.getLeaderCard1());
        assertNull(s2.getLeaderCard2());
        assertNull(s3.getLeaderCard1());
        assertNull(s3.getLeaderCard2());
        assertNull(s4.getLeaderCard1());
        assertNull(s4.getLeaderCard2());
        assertSame(l1, s5.getLeaderCard1());
        assertSame(l2, s5.getLeaderCard2());
        assertNull(s6.getLeaderCard1());
        assertNull(s6.getLeaderCard2());
        assertNull(s7.getLeaderCard1());
        assertNull(s7.getLeaderCard2());

        ArrayList<Integer> availableSlots = new ArrayList<>();
        availableSlots.add(1);
        availableSlots.add(2);
        availableSlots.add(3);

        s1.setAvailableSlots(availableSlots);
        s2.setAvailableSlots(availableSlots);
        s3.setAvailableSlots(availableSlots);
        s4.setAvailableSlots(availableSlots);
        s5.setAvailableSlots(availableSlots);
        s6.setAvailableSlots(availableSlots);
        s7.setAvailableSlots(availableSlots);

        assertNull(s1.getAvailableSlots());
        assertNull(s2.getAvailableSlots());
        assertSame(availableSlots, s3.getAvailableSlots());
        assertNull(s4.getAvailableSlots());
        assertNull(s5.getAvailableSlots());
        assertNull(s6.getAvailableSlots());
        assertNull(s7.getAvailableSlots());

        ArrayList<Marble> marbles = new ArrayList<>();
        marbles.add(new RedMarble());

        s1.setMarbles(marbles);
        s2.setMarbles(marbles);
        s3.setMarbles(marbles);
        s4.setMarbles(marbles);
        s5.setMarbles(marbles);
        s6.setMarbles(marbles);
        s7.setMarbles(marbles);

        assertNull(s1.getMarbles());
        assertNull(s2.getMarbles());
        assertNull(s3.getMarbles());
        assertSame(marbles, s4.getMarbles());
        assertSame(marbles, s5.getMarbles());
        assertNull(s6.getMarbles());
        assertNull(s7.getMarbles());

        Marble[] marbles1 = new Marble[1];
        marbles1[0] = new WhiteMarble();

        s1.setMarbles(marbles1);
        s2.setMarbles(marbles1);
        s3.setMarbles(marbles1);
        s4.setMarbles(marbles1);
        s5.setMarbles(marbles1);
        s6.setMarbles(marbles1);
        s7.setMarbles(marbles1);

        assertNull(s1.getMarbles());
        assertNull(s2.getMarbles());
        assertNull(s3.getMarbles());
        assertSame(marbles1[0], s4.getMarbles().get(0));
        assertSame(marbles1[0], s5.getMarbles().get(0));
        assertNull(s6.getMarbles());
        assertNull(s7.getMarbles());
    }

    /**
     * this test verifies the correct remove of marbles by WhiteConversionCardState and TakeMarbleState
     */
    @Test
    void correctRemoveMarble(){

        ArrayList<Marble> marbles = new ArrayList<>();
        marbles.add(new WhiteMarble());
        marbles.add(new ResourceMarble(Resource.COIN));
        marbles.add(new WhiteMarble());
        marbles.add(new RedMarble());

        ArrayList<Marble> marbles2 = new ArrayList<>();
        marbles2.add(new WhiteMarble());
        marbles2.add(new ResourceMarble(Resource.COIN));
        marbles2.add(new WhiteMarble());
        marbles2.add(new RedMarble());

        State_Controller s1 = new TakeMarbleState();
        State_Controller s2 = new WhiteConversionCardState();

        s1.setMarbles(marbles);
        s2.setMarbles(marbles2);

        assertEquals(4, s1.getMarbles().size());
        assertEquals(4, s2.getMarbles().size());

        s1.removeMarble(new WhiteMarble());
        s2.removeMarble(new RedMarble());

        assertEquals(3, s1.getMarbles().size());
        assertEquals(3, s2.getMarbles().size());

        assertTrue(s1.getMarbles().get(0) instanceof ResourceMarble);
        assertTrue(s2.getMarbles().get(0) instanceof WhiteMarble);
    }
}
