package it.polimi.ingsw.controllerTests;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.controller.GameStartingState;
import it.polimi.ingsw.exceptions.FullGameException;
import it.polimi.ingsw.exceptions.IllegalStateException;
import it.polimi.ingsw.exceptions.InsufficientResourceException;
import it.polimi.ingsw.exceptions.WrongParametersException;
import it.polimi.ingsw.network.messages.MessageOneParameterInt;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.view.View;

import it.polimi.ingsw.view.VirtualView;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerGameTest {

    /**
     * this test verifies the correct reset of ControllerGame
     */
    @Test
    void correctReset(){

        ControllerGame cg = new ControllerGame();
        cg.resetControllerGame();
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(0, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());
    }

    /**
     * this test verifies the correct add and remove of View
     */
    @Test
    void correctAddAndRemoveView() throws FullGameException, WrongParametersException, IllegalStateException{

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("Carlo");
        cg.addView(v);
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());

        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(1, cg.getMaxNumPlayers());

        cg.removeView(v, null);
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(0, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());
    }

    /**
     * this test tries to add two view in a SinglePlayerGame
     */
    @Test
    void twoPlayerInSinglePlayer() throws FullGameException, WrongParametersException, IllegalStateException{

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        v1.setNickName("Carlo");
        cg.addView(v1);
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());

        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(1, cg.getMaxNumPlayers());

        FullGameException thrown =
                assertThrows(FullGameException.class, () -> cg.addView(v1));
        String expectedMessage = "This game is full of players";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


}
