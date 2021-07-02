package it.polimi.ingsw.controllerTests;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.controller.GameStartingState;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.exceptions.IllegalStateException;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.View;

import it.polimi.ingsw.view.VirtualView;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.Observable;

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

        cg.removeView(v, null);
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(0, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());

        cg.addView(v);
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());

        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 2));
        assertSame(GameStartingState.WAITING_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(2, cg.getMaxNumPlayers());

        cg.removeView(v, "Carlo");
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

        cg.update(new Observable(), new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(1, cg.getMaxNumPlayers());

        FullGameException thrown =
                assertThrows(FullGameException.class, () -> cg.addView(v1));
        String expectedMessage = "This game is full of players";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct functioning of quit with null nickname
     */
    @Test
    void quitNullNickname() throws FullGameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("Carlo");
        cg.addView(v);
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());

        cg.quitGame(v, null);
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(0, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());
    }

    /**
     * this test verifies the correct functioning of quit with only one player
     */
    @Test
    void quitOnePlayer() throws FullGameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("Carlo");
        cg.addView(v);
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());

        cg.quitGame(v, "Carlo");
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(0, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());
    }

    /**
     * this test verifies the correct functioning of quit when game is not started
     */
    @Test
    void quitGameNotStarted() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        VirtualView v2 = new VirtualView();
        v1.setNickName("Carlo");
        v2.setNickName("Giorgio");
        cg.addView(v1);
        cg.addView(v2);
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(2, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());

        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 3));
        assertSame(GameStartingState.WAITING_PLAYERS, cg.getState());
        assertEquals(2, cg.getCurrentNumPlayers());
        assertEquals(3, cg.getMaxNumPlayers());

        cg.quitGame(v1, "Carlo");
        assertSame(GameStartingState.WAITING_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(3, cg.getMaxNumPlayers());
    }

    /**
     * this test verifies the correct functioning of quit when game is started
     */
    @Test
    void quitGameStarted() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        VirtualView v2 = new VirtualView();
        v1.setNickName("Carlo");
        v2.setNickName("Giorgio");
        cg.addView(v1);
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());

        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 2));
        assertSame(GameStartingState.WAITING_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(2, cg.getMaxNumPlayers());

        cg.addView(v2);
        assertSame(GameStartingState.WAITING_PLAYERS_CHOICES, cg.getState());
        assertEquals(2, cg.getCurrentNumPlayers());
        assertEquals(2, cg.getMaxNumPlayers());

        cg.quitGame(v1, "Carlo");
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(0, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());
    }

    /**
     * this test tries to create new game during wrong game starting state
     */
    @Test
    void newGameWrongState() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        VirtualView v2 = new VirtualView();
        v1.setNickName("Carlo");
        v2.setNickName("Carlo");
        cg.addView(v1);
        cg.addPlayer(v1, "Carlo");
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());

        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 2));
        assertSame(GameStartingState.WAITING_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(2, cg.getMaxNumPlayers());

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 2)));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to new game with wrong parameters
     */
    @Test
    void newGameWrongParameters() throws FullGameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        VirtualView v2 = new VirtualView();
        v1.setNickName("Carlo");
        v2.setNickName("Carlo");
        cg.addView(v1);
        cg.addPlayer(v1, "Carlo");
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 0)));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        thrown =
                assertThrows(WrongParametersException.class, () -> cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 5)));
        expectedMessage = "You have inserted wrong parameters";
        actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    /**
     * this test verifies all different add player situations.
     */
    @Test
    void addPlayerWrongState() throws WrongParametersException, IllegalStateException, FullGameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        VirtualView v2 = new VirtualView();
        v1.setNickName("Carlo");
        v2.setNickName("Carlo");
        cg.addView(v1);
        cg.addPlayer(v1, "Carlo");
        assertSame(GameStartingState.WAITING_NUM_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(0, cg.getMaxNumPlayers());

        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 2));
        assertSame(GameStartingState.WAITING_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(2, cg.getMaxNumPlayers());

        cg.update(v2, new Message(MessageType.LOGIN, 1));
        assertSame(GameStartingState.WAITING_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(2, cg.getMaxNumPlayers());

        cg.update(v2, new MessageOneParameterString(MessageType.LOGIN, 1, "Carlo"));
        assertSame(GameStartingState.WAITING_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(2, cg.getMaxNumPlayers());

        cg.update(v2, new MessageOneParameterString(MessageType.LOGIN, 1, null));
        assertSame(GameStartingState.WAITING_PLAYERS, cg.getState());
        assertEquals(1, cg.getCurrentNumPlayers());
        assertEquals(2, cg.getMaxNumPlayers());

        cg.addView(v2);
        v2.setNickName("Giacomo");
        cg.update(v2, new MessageOneParameterString(MessageType.LOGIN, 1, "Giacomo"));
        assertSame(GameStartingState.WAITING_PLAYERS_CHOICES, cg.getState());
        assertEquals(2, cg.getCurrentNumPlayers());
        assertEquals(2, cg.getMaxNumPlayers());

        cg.update(v2, new MessageOneParameterString(MessageType.LOGIN, 1, "Giacomo"));
        assertSame(GameStartingState.WAITING_PLAYERS_CHOICES, cg.getState());
        assertEquals(2, cg.getCurrentNumPlayers());
        assertEquals(2, cg.getMaxNumPlayers());
    }

    /**
     * this test verifies the correct functioning of isMyTurn
     */
    @Test
    void correctTurn() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        v1.setNickName("Carlo");
        cg.addView(v1);
        cg.addPlayer(v1, "Carlo");
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));
        cg.update(v1, new Message(MessageType.TURN, 0));
    }

    /**
     * this test tries to chose LeaderCard in wrong game starting state
     */
    @Test
    void leaderCardWrongState() throws FullGameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        v1.setNickName("Carlo");
        cg.addView(v1);
        cg.addPlayer(v1, "Carlo");

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.leaderCardHandler(new MessageTwoParameterInt(MessageType.LEADER_CARD, 0, 47, 59)));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to chose LeaderCard with wrong parameters
     */
    @Test
    void leaderCardWrongParams() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        v1.setNickName("Carlo");
        cg.addView(v1);
        cg.addPlayer(v1, "Carlo");
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> cg.leaderCardHandler(new MessageTwoParameterInt(MessageType.LEADER_CARD, 0, 1, 70)));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v1, new MessageTwoParameterInt(MessageType.LEADER_CARD, 0, 49, 57));
        cg.endGame();
    }

    /**
     * this test tries to chose first resource if player is first or fourth
     */
    @Test
    void choseResourceFirstFourthPlayer() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        VirtualView v2 = new VirtualView();
        VirtualView v3 = new VirtualView();
        VirtualView v4 = new VirtualView();
        v1.setNickName("Carlo");
        assertNotNull(v1.getNickname());
        v2.setNickName("Franco");
        assertNotNull(v2.getNickname());
        v3.setNickName("Germano");
        assertNotNull(v3.getNickname());
        v4.setNickName("Elio");
        assertNotNull(v4.getNickname());
        cg.addView(v1);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 4));
        cg.addView(v2);
        cg.addView(v3);
        assertSame(cg.getState(), GameStartingState.WAITING_PLAYERS);
        cg.addView(v4);
        assertSame(cg.getState(), GameStartingState.WAITING_PLAYERS_CHOICES);

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.oneResourceHandle(new MessageOneIntOneResource(MessageType.ONE_FIRST_RESOURCE, 0, Resource.COIN, 1)));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        thrown =
                assertThrows(IllegalStateException.class, () -> cg.oneResourceHandle(new MessageOneIntOneResource(MessageType.ONE_FIRST_RESOURCE, 3, Resource.COIN, 1)));
        expectedMessage = "You can't do this operation at this moment";
        actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        cg.update(v2, new MessageOneIntOneResource(MessageType.ONE_FIRST_RESOURCE, 1, Resource.COIN, 1));
        cg.update(v2, new MessageOneIntOneResource(MessageType.ONE_FIRST_RESOURCE, 2, Resource.COIN, 1));
    }

    /**
     * this test tries to chose resource in wrong game starting state
     */
    @Test
    void choseResourceWrongState() throws FullGameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        v1.setNickName("Carlo");
        cg.addView(v1);
        cg.addPlayer(v1, "Carlo");

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.oneResourceHandle(new MessageOneIntOneResource(MessageType.ONE_FIRST_RESOURCE, 1, Resource.COIN, 1)));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to chose first resource if player is first or second or third
     */
    @Test
    void choseResourceFirstSecondThirdPlayer() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        VirtualView v2 = new VirtualView();
        VirtualView v3 = new VirtualView();
        VirtualView v4 = new VirtualView();
        v1.setNickName("Carlo");
        assertNotNull(v1.getNickname());
        v2.setNickName("Franco");
        assertNotNull(v2.getNickname());
        v3.setNickName("Germano");
        assertNotNull(v3.getNickname());
        v4.setNickName("Elio");
        assertNotNull(v4.getNickname());
        cg.addView(v1);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 4));
        cg.addView(v2);
        cg.addView(v3);
        assertSame(cg.getState(), GameStartingState.WAITING_PLAYERS);
        cg.addView(v4);
        assertSame(cg.getState(), GameStartingState.WAITING_PLAYERS_CHOICES);

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.twoResourceHandle(new MessageTwoResource(MessageType.TWO_FIRST_RESOURCE, 0, Resource.COIN, Resource.COIN)));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        thrown =
                assertThrows(IllegalStateException.class, () -> cg.twoResourceHandle(new MessageTwoResource(MessageType.TWO_FIRST_RESOURCE, 1, Resource.COIN, Resource.COIN)));
        expectedMessage = "You can't do this operation at this moment";
        actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        thrown =
                assertThrows(IllegalStateException.class, () -> cg.twoResourceHandle(new MessageTwoResource(MessageType.TWO_FIRST_RESOURCE, 2, Resource.COIN, Resource.COIN)));
        expectedMessage = "You can't do this operation at this moment";
        actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        cg.update(v4, new MessageTwoResource(MessageType.TWO_FIRST_RESOURCE, 3, Resource.COIN, Resource.COIN));
    }

    /**
     * this test tries to chose two resource in wrong game starting state
     */
    @Test
    void choseTwoResourceWrongState() throws FullGameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        v1.setNickName("Carlo");
        cg.addView(v1);
        cg.addPlayer(v1, "Carlo");

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.twoResourceHandle(new MessageTwoResource(MessageType.TWO_FIRST_RESOURCE, 3, Resource.COIN, Resource.COIN)));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to perform one operation if it's not player's turn
     */
    @Test
    void notYourTurn() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v1 = new VirtualView();
        v1.setNickName("Carlo");
        cg.addView(v1);
        VirtualView v2 = new VirtualView();
        v2.setNickName("Olacr");
        cg.addView(v2);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 2));
        cg.update(v1, new Message(MessageType.LEADER_CARD_POWER, 1));
    }
}
