package it.polimi.ingsw.controllerTests;

import it.polimi.ingsw.controller.TurnController;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TurnControllerTest {

    /**
     * test the correct turn player
     */
    @Test
    void correctTurnPlayer(){

        TurnController turnController = new TurnController(4);

        assertFalse(turnController.isMyTurn(1));
        assertFalse(turnController.isMyTurn(2));
        assertFalse(turnController.isMyTurn(3));
        assertTrue(turnController.isMyTurn(0));

        turnController.nextTurn();
        assertFalse(turnController.isMyTurn(0));
        assertFalse(turnController.isMyTurn(2));
        assertFalse(turnController.isMyTurn(3));
        assertTrue(turnController.isMyTurn(1));

        turnController.nextTurn();
        assertFalse(turnController.isMyTurn(0));
        assertFalse(turnController.isMyTurn(1));
        assertFalse(turnController.isMyTurn(3));
        assertTrue(turnController.isMyTurn(2));

        turnController.nextTurn();
        assertFalse(turnController.isMyTurn(0));
        assertFalse(turnController.isMyTurn(1));
        assertFalse(turnController.isMyTurn(2));
        assertTrue(turnController.isMyTurn(3));

        turnController.nextTurn();
        assertFalse(turnController.isMyTurn(1));
        assertFalse(turnController.isMyTurn(2));
        assertFalse(turnController.isMyTurn(3));
        assertTrue(turnController.isMyTurn(0));
    }

    /**
     * test the correct end game
     */
    @Test
    void correctEndGame(){

        TurnController turnController = new TurnController(3);
        assertFalse(turnController.isEndGame());

        turnController.nextTurn();
        turnController.nextTurn();
        assertFalse(turnController.isEndGame());

        turnController.nextTurn();
        turnController.endGame();
        assertFalse(turnController.isEndGame());

        turnController.nextTurn();
        assertFalse(turnController.isEndGame());

        turnController.nextTurn();
        assertTrue(turnController.isEndGame());
    }
}
