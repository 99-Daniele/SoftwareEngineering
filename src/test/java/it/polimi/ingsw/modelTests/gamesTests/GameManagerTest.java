package it.polimi.ingsw.modelTests.gamesTests;

import it.polimi.ingsw.controller.ControllerGame;
import it.polimi.ingsw.controller.GameStartingState;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.exceptions.IllegalStateException;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.games.GameManager;
import it.polimi.ingsw.model.games.states.*;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.RedMarble;
import it.polimi.ingsw.model.market.ResourceMarble;
import it.polimi.ingsw.model.market.WhiteMarble;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.parser.CardMapCLI;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.VirtualView;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameManagerTest {

    /**
     * this test verifies the correct creation of GameManager
     */
    @Test
    void createGameManager(){

        ControllerGame cg = new ControllerGame();
        GameManager gm = new GameManager(2, cg);
        assertEquals(2, gm.getNumOfPlayers());
        assertTrue(gm.getCurrentState().isRightState(GameStates.FIRST_ACTION_STATE));
    }

    /**
     * this test verifies the correct creation and delete of a player
     */
    @Test
    void createAndDeletePlayer() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(3, cg);
        assertEquals(3, gm.getNumOfPlayers());

        gm.createPlayer(v, "Daniele");
        assertSame("Daniele", gm.getPlayerPosition(0));

        gm.createPlayer(v, "Carlo");
        assertSame("Carlo", gm.getPlayerPosition(1));

        gm.deletePlayer("Carlo");
        assertThrows(IndexOutOfBoundsException.class, () -> gm.getPlayerPosition(1));

        gm.createPlayer(v, "Carlo");
        assertSame("Carlo", gm.getPlayerPosition(1));

        gm.createPlayer(v, "Giacomo");
    }

    /**
     * this test tries to create two players with same nickName
     */
    @Test
    void createSameNickNamePlayers() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(2, cg);
        assertEquals(2, gm.getNumOfPlayers());

        gm.createPlayer(v, "Daniele");
        assertSame("Daniele", gm.getPlayerPosition(0));

        AlreadyTakenNicknameException thrown =
                assertThrows(AlreadyTakenNicknameException.class, () -> gm.createPlayer(v, "Daniele"));
        String expectedMessage = "This nickname has already been taken";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to select LeaderCards two times with the same player
     */
    @Test
    void twiceLeaderCards() throws AlreadyTakenNicknameException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        v.setNickName("Daniele");
        gm.getControllerGame().startGame();

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add((LeaderCard) CardMapCLI.getCard(49));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(57));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(60));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(61));

        gm.leaderCardHandler(0, 49, 60, leaderCards);

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.leaderCardHandler(0, 57, 61, leaderCards));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to select first resource two times with the same player
     */
    @Test
    void wrongPhaseLeaderCards() throws AlreadyTakenNicknameException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add((LeaderCard) CardMapCLI.getCard(49));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(57));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(60));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(61));

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.leaderCardHandler(0, 57, 61, leaderCards));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to select resources two times with the same player
     */
    @Test
    void twiceFirstResources() throws AlreadyTakenNicknameException, IllegalStateException, FullGameException, WrongParametersException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        cg.addView(v);
        GameManager gm = new GameManager(3, cg);
        gm.createPlayer(v, "Daniele");
        gm.createPlayer(v, "Carlo");
        gm.createPlayer(v, "Giacomo");
        v.setNickName("Daniele");
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 3));
        gm.getControllerGame().startGame();

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add((LeaderCard) CardMapCLI.getCard(49));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(57));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(60));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(61));

        gm.leaderCardHandler(0, 49, 60, leaderCards);
        gm.leaderCardHandler(1, 57, 61, leaderCards);
        gm.leaderCardHandler(2, 60, 61, leaderCards);

        gm.oneResourceHandle(1, Resource.COIN);

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.oneResourceHandle(1, Resource.COIN));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));


        assertSame(gm.getControllerGame().getState(), GameStartingState.WAITING_PLAYERS_CHOICES);
        gm.oneResourceHandle(2, Resource.STONE);
        assertSame(gm.getControllerGame().getState(), GameStartingState.START_GAME);
    }

    /**
     * this test tries to select resources if it's first player
     */
    @Test
    void firstPlayerChoseResource() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        GameManager gm = new GameManager(2, cg);
        gm.createPlayer(v, "Daniele");
        v.setNickName("Daniele");
        gm.startGame();
        gm.getControllerGame().startGame();

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.oneResourceHandle(0, Resource.COIN));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        thrown =
                assertThrows(IllegalStateException.class, () -> gm.twoResourceHandle(0, Resource.COIN, Resource.STONE));
        expectedMessage = "You can't do this operation at this moment";
        actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to select two resources if it's second or third player
     */
    @Test
    void secondThirdPlayerChoseResource() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        GameManager gm = new GameManager(4, cg);
        gm.createPlayer(v, "Daniele");
        gm.createPlayer(v, "Daniee");
        gm.createPlayer(v, "Danele");
        gm.createPlayer(v, "anele");
        v.setNickName("Daniele");
        gm.getControllerGame().startGame();

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.twoResourceHandle(1, Resource.COIN, Resource.STONE));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        thrown =
                assertThrows(IllegalStateException.class, () -> gm.twoResourceHandle(2, Resource.COIN, Resource.STONE));
        expectedMessage = "You can't do this operation at this moment";
        actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to select two resources two times with the same player
     */
    @Test
    void twiceFirstTwoResources() throws AlreadyTakenNicknameException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        GameManager gm = new GameManager(4, cg);
        gm.createPlayer(v, "Daniele");
        gm.createPlayer(v, "Carlo");
        gm.createPlayer(v, "Calo");
        gm.createPlayer(v, "Crlo");
        v.setNickName("Daniele");
        gm.getControllerGame().startGame();

        gm.twoResourceHandle(3, Resource.COIN, Resource.STONE);

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.twoResourceHandle(3, Resource.COIN, Resource.STONE));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to select first resource in a wrong game phase
     */
    @Test
    void wrongPhasesOneResource() throws AlreadyTakenNicknameException{

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.oneResourceHandle(0, Resource.COIN));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to select first two resource in a wrong game phase
     */
    @Test
    void wrongPhasesTwoResource() throws AlreadyTakenNicknameException{

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.twoResourceHandle(0, Resource.COIN, Resource.STONE));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies if game start after all players made their choice
     */
    @Test
    void correctStartGameChoices() throws AlreadyTakenNicknameException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        GameManager gm = new GameManager(4, cg);
        gm.createPlayer(v, "Daniele");
        gm.createPlayer(v, "Carlo");
        gm.createPlayer(v, "Calo");
        gm.createPlayer(v, "Crlo");
        v.setNickName("Daniele");
        gm.getControllerGame().startGame();

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add((LeaderCard) CardMapCLI.getCard(49));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(57));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(60));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(61));

        gm.leaderCardHandler(0, 49, 57, leaderCards);
        gm.leaderCardHandler(3, 49, 61, leaderCards);
        gm.leaderCardHandler(2, 57, 60, leaderCards);
        gm.leaderCardHandler(1, 60, 61, leaderCards);

        gm.oneResourceHandle(2, Resource.STONE);
        gm.oneResourceHandle(1, Resource.COIN);

        assertSame(gm.getControllerGame().getState(), GameStartingState.WAITING_PLAYERS_CHOICES);
        gm.twoResourceHandle(3, Resource.SHIELD, Resource.SERVANT);
        assertSame(gm.getControllerGame().getState(), GameStartingState.START_GAME);
        gm.endGame();
    }

    /**
     * this test tries to buy card in wrong state
     */
    @Test
    void buyCardWrongState() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new EndTurnState());

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.buyCardHandler(0, 1, 1));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to buy card with wrong parameters
     */
    @Test
    void buyCardWrongParameters() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> cg.buyCardHandler(new MessageThreeParameterInt(MessageType.BUY_CARD, 0, 4, 3, 5)));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new MessageThreeParameterInt(MessageType.BUY_CARD, 0, 0, 1, 1));
    }

    /**
     * this test tries to buy card which can't be added to any slot
     */
    @Test
    void buyCardNotAvailableSlots() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");

        ImpossibleDevelopmentCardAdditionException thrown =
                assertThrows(ImpossibleDevelopmentCardAdditionException.class, () -> gm.buyCardHandler(2, 1, 1));
        String expectedMessage = "You have not available slots to buy this card";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to chose slot to buy card in a wrong game state
     */
    @Test
    void choseSlotWrongState() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.chosenSlotHandler(new MessageOneParameterInt(MessageType.CHOSEN_SLOT, 0, 1)));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        cg.update(v, new MessageOneParameterInt(MessageType.CHOSEN_SLOT, 0, 1));
        ArrayList<Integer> slots = new ArrayList<>();
        slots.add(1);
        slots.add(3);
        cg.choseSlot(0, slots);
    }

    /**
     * this test tries to chose a wrong slot
     */
    @Test
    void choseWrongSlot() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new BuyCardState());
        ArrayList<Integer> slots = new ArrayList<>();
        slots.add(2);
        slots.add(3);
        gm.getCurrentState().setAvailableSlots(slots);

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> gm.chosenSlotHandler(1));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to buy card without any resource
     */
    @Test
    void buyExpensiveCard() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new BuyCardState());
        assertTrue(gm.getCurrentState().isRightState(GameStates.BUY_CARD_STATE));
        ArrayList<Integer> slots = new ArrayList<>();
        slots.add(2);
        slots.add(1);
        gm.getCurrentState().setAvailableSlots(slots);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> gm.chosenSlotHandler(1));
        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        assertFalse(gm.getCurrentState().isRightState(GameStates.END_TURN_STATE));
        assertTrue(gm.getCurrentState().isRightState(GameStates.FIRST_ACTION_STATE));
    }

    /**
     * this test tries to take marble in wrong state
     */
    @Test
    void takeMarbleWrongState() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new EndTurnState());

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.takeMarbleHandler(0, 1));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to take marble with wrong parameters
     */
    @Test
    void takeMarbleWrongParameters() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> cg.takeMarbleHandler(new MessageTwoParameterInt(MessageType.TAKE_MARBLE, 0, 2, 5)));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new MessageTwoParameterInt(MessageType.TAKE_MARBLE, 0, 2, 5));
    }

    /**
     * this test verifies the correct taking of marbles
     */
    @Test
    void correctTakeMarble() throws AlreadyTakenNicknameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");

        gm.takeMarbleHandler(0, 1);
        assertTrue(gm.getCurrentState().isRightState(GameStates.TAKE_MARBLE_STATE));
        assertNotNull(gm.getCurrentState().getMarbles());
    }

    /**
     * this test tries to use marble in wrong state
     */
    @Test
    void useMarbleWrongState() throws WrongParametersException, IllegalStateException, FullGameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.useMarbleHandler(new MessageOneParameterMarble(MessageType.USE_MARBLE, 0, new WhiteMarble())));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new MessageOneParameterMarble(MessageType.USE_MARBLE, 0, new WhiteMarble()));
    }

    /**
     * this test tries to use marble with wrong parameters
     */
    @Test
    void useMarbleWrongParameters() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new TakeMarbleState());
        ArrayList<Marble> marbles = new ArrayList<>();
        marbles.add(new RedMarble());
        gm.getCurrentState().setMarbles(marbles);

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> gm.useMarbleHandler(new WhiteMarble()));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct use marble
     */
    @Test
    void correctUseMarble() throws AlreadyTakenNicknameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new TakeMarbleState());
        ArrayList<Marble> marbles = new ArrayList<>();
        marbles.add(new RedMarble());
        marbles.add(new ResourceMarble(Resource.COIN));
        gm.getCurrentState().setMarbles(marbles);

        assertTrue(gm.useMarbleHandler(new ResourceMarble(Resource.COIN)));
        assertTrue(gm.getCurrentState().isRightState(GameStates.TAKE_MARBLE_STATE));

        assertTrue(gm.useMarbleHandler(new RedMarble()));
        assertTrue(gm.getCurrentState().isRightState(GameStates.END_TURN_STATE));
    }

    /**
     * this test verifies the correct functioning of use marble in case of two active WhiteConversionCards
     */
    @Test
    void twoActiveWhiteMarbleCards()
            throws AlreadyTakenNicknameException, WrongParametersException, IllegalStateException, InsufficientCardsException, AlreadyDiscardLeaderCardException, InsufficientResourceException, ActiveLeaderCardException, FullGameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        cg.addView(v);
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.leaderActivationHandler(0);
        gm.setCurrentState(new TakeMarbleState());
        ArrayList<Marble> marbles = new ArrayList<>();
        marbles.add(new WhiteMarble());
        gm.getCurrentState().setMarbles(marbles);
        assertTrue(gm.getCurrentState().isRightState(GameStates.TAKE_MARBLE_STATE));

        assertFalse(gm.useMarbleHandler(new WhiteMarble()));
        assertTrue(gm.getCurrentState().isRightState(GameStates.WHITE_CONVERSION_CARD_STATE));
    }

    /**
     * this test tries to white conversion in wrong state
     */
    @Test
    void whiteConversionWrongState() throws WrongParametersException, IllegalStateException, FullGameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.whiteConversionCardHandler(new MessageOneParameterInt(MessageType.WHITE_CONVERSION_CARD, 0, 1)));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new MessageOneParameterInt(MessageType.WHITE_CONVERSION_CARD, 0, 1));
    }

    /**
     * this test tries to white conversion with wrong parameters
     */
    @Test
    void whiteConversionWrongParameters() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new WhiteConversionCardState());

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> gm.whiteConversionCardHandler(4));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct functioning of white conversion
     */
    @Test
    void correctWhiteConversion()
            throws FullGameException, AlreadyTakenNicknameException, WrongParametersException, IllegalStateException, InsufficientCardsException, AlreadyDiscardLeaderCardException, InsufficientResourceException, ActiveLeaderCardException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        cg.addView(v);
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.leaderActivationHandler(0);
        gm.setCurrentState(new WhiteConversionCardState());
        ArrayList<Marble> marbles = new ArrayList<>();
        marbles.add(new WhiteMarble());
        gm.getCurrentState().setMarbles(marbles);
        LeaderCard[] leaderCards = new LeaderCard[2];
        leaderCards[0] = (LeaderCard) CardMapCLI.getCard(57);
        leaderCards[1] = (LeaderCard) CardMapCLI.getCard(58);
        gm.getCurrentState().setLeaderCards(leaderCards);
        assertTrue(gm.getCurrentState().isRightState(GameStates.WHITE_CONVERSION_CARD_STATE));

        gm.whiteConversionCardHandler(1);
        assertTrue(gm.getCurrentState().isRightState(GameStates.TAKE_MARBLE_STATE));

        assertFalse(gm.useMarbleHandler(new WhiteMarble()));
        assertTrue(gm.getCurrentState().isRightState(GameStates.WHITE_CONVERSION_CARD_STATE));

        gm.whiteConversionCardHandler(2);
        assertTrue(gm.getCurrentState().isRightState(GameStates.END_TURN_STATE));
    }

    /**
     * this test tries to switch depots in wrong state
     */
    @Test
    void switchDepotWrongState() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.switchHandler(new MessageTwoParameterInt(MessageType.SWITCH_DEPOT, 0, 1, 2)));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new MessageTwoParameterInt(MessageType.SWITCH_DEPOT, 0, 1, 2));
    }

    /**
     * this test tries to switchDepots with wrong parameters
     */
    @Test
    void switchDepotWrongParameters() throws AlreadyTakenNicknameException, WrongParametersException, IllegalStateException, ImpossibleSwitchDepotException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new TakeMarbleState());

        gm.switchHandler(1, 2);

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> gm.switchHandler(5, -1));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to development card power in wrong state
     */
    @Test
    void cardPowerWrongState() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new EndTurnState());

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.developmentCardPowerHandler(1, 1));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to development card power with wrong parameters
     */
    @Test
    void cardPowerWrongParameters() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> cg.developmentCardPowerHandler(new MessageTwoParameterInt(MessageType.DEVELOPMENT_CARD_POWER, 0, 4, 3)));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new MessageTwoParameterInt(MessageType.DEVELOPMENT_CARD_POWER, 0, 4, 3));
    }

    /**
     * this test tries to development card power and empty slot
     */
    @Test
    void cardPowerEmptySlot() throws AlreadyTakenNicknameException{

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new ActivateProductionState());


        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> gm.developmentCardPowerHandler(1, 1));
        String expectedMessage = "You don't have any card to activate this power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to use development card power twice
     */
    @Test
    void cardPowerTwice() throws AlreadyTakenNicknameException{

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new ActivateProductionState());
        gm.getCurrentState().addDevelopmentCardSlot(1);

        AlreadyUsedProductionPowerException thrown =
                assertThrows(AlreadyUsedProductionPowerException.class, () -> gm.developmentCardPowerHandler(1, 1));
        String expectedMessage = "You already used this production power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to basic power in wrong state
     */
    @Test
    void basicPowerWrongState() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new EndTurnState());

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.basicPowerHandler(Resource.SHIELD, Resource.STONE, Resource.COIN, 1));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to basic power with wrong parameters
     */
    @Test
    void basicPowerWrongParameters() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> cg.basicPowerHandler(new MessageThreeResourceOneInt(MessageType.BASIC_POWER, 0, Resource.SHIELD, Resource.STONE, Resource.COIN, 2)));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new MessageThreeResourceOneInt(MessageType.BASIC_POWER, 0, Resource.SHIELD, Resource.STONE, Resource.COIN, 2));
    }

    /**
     * this test tries to use basic power twice
     */
    @Test
    void basicPowerTwice() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new ActivateProductionState());
        gm.getCurrentState().setBasicPower();

        AlreadyUsedProductionPowerException thrown =
                assertThrows(AlreadyUsedProductionPowerException.class, () -> gm.basicPowerHandler(Resource.SHIELD, Resource.STONE, Resource.COIN, 0));
        String expectedMessage = "You already used this production power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }


    /**
     * this test tries to leader card power in wrong state
     */
    @Test
    void leaderPowerWrongState() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new EndTurnState());

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.leaderCardPowerHandler(1, Resource.COIN, 1));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to leader card power with wrong parameters
     */
    @Test
    void leaderPowerWrongParameters() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> cg.leaderCardPowerHandler(new MessageOneResourceTwoInt(MessageType.LEADER_CARD_POWER, 0, Resource.COIN, 4, 3)));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new MessageOneResourceTwoInt(MessageType.LEADER_CARD_POWER, 0, Resource.COIN, 4, 3));
    }

    /**
     * this test tries to leader card power not existing
     */
    @Test
    void leaderPowerEmptySlot() throws AlreadyTakenNicknameException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add((LeaderCard) CardMapCLI.getCard(49));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(57));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(60));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(61));
        gm.getControllerGame().startGame();
        gm.leaderCardHandler(0, 49, 60, leaderCards);
        gm.setCurrentState(new FirstActionState());

        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> gm.leaderCardPowerHandler(1, Resource.COIN, 1));
        String expectedMessage = "You don't have any card to activate this power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to leader card power twice
     */
    @Test
    void leaderPowerTwice() throws AlreadyTakenNicknameException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add((LeaderCard) CardMapCLI.getCard(49));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(57));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(60));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(61));
        gm.getControllerGame().startGame();
        gm.leaderCardHandler(0, 49, 60, leaderCards);
        gm.setCurrentState(new ActivateProductionState());
        gm.getCurrentState().addLeaderCard(1);

        AlreadyUsedProductionPowerException thrown =
                assertThrows(AlreadyUsedProductionPowerException.class, () -> gm.leaderCardPowerHandler(1, Resource.COIN, 1));
        String expectedMessage = "You already used this production power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

    }

    /**
     * this test tries to end production in wrong state
     */
    @Test
    void endProductionWrongState() throws WrongParametersException, IllegalStateException, FullGameException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.endProductionHandler(new Message(MessageType.END_PRODUCTION, 0)));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new Message(MessageType.END_PRODUCTION, 0));
    }

    /**
     * this test verifies the correct functioning of end production
     */
    @Test
    void correctEndProduction() throws AlreadyTakenNicknameException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new ActivateProductionState());
        gm.getCurrentState().setStrongbox(new Strongbox());
        assertTrue(gm.getCurrentState().isRightState(GameStates.ACTIVATE_PRODUCTION_STATE));

        gm.endProductionHandler();
        assertTrue(gm.getCurrentState().isRightState(GameStates.END_TURN_STATE));
    }

    /**
     * this test tries to activate LeaderCard with wrong parameters
     */
    @Test
    void activateLeaderCardWrongParameters() throws FullGameException, WrongParametersException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> cg.leaderActivationHandler(new MessageOneParameterInt(MessageType.LEADER_CARD_ACTIVATION, 0, 3)));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new MessageOneParameterInt(MessageType.LEADER_CARD_ACTIVATION, 0, 3));
    }

    /**
     * this test tries to activate LeaderCard during wrong state of game
     */
    @Test
    void activateLeaderCardWrongState() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new TakeMarbleState());

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.leaderActivationHandler(1));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies if state don't change after leader activation in FirstActionState
     */
    @Test
    void leaderActivationFirstAction()
            throws AlreadyTakenNicknameException, WrongParametersException, IllegalStateException, InsufficientCardsException, AlreadyDiscardLeaderCardException, InsufficientResourceException, ActiveLeaderCardException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");

        assertTrue(gm.getCurrentState().isRightState(GameStates.FIRST_ACTION_STATE));
        gm.leaderActivationHandler(0);
        assertTrue(gm.getCurrentState().isRightState(GameStates.FIRST_ACTION_STATE));
    }

    /**
     * this test verifies if state don't change after leader activation in EndTurnState
     */
    @Test
    void leaderActivationEndTurn()
            throws AlreadyTakenNicknameException, IllegalStateException, FullGameException, WrongParametersException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        cg.addView(v);
        v.setNickName("Daniele");
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add((LeaderCard) CardMapCLI.getCard(49));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(57));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(60));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(61));

        gm.leaderCardHandler(0, 57, 60, leaderCards);
        gm.setCurrentState(new EndTurnState());

        assertTrue(gm.getCurrentState().isRightState(GameStates.END_TURN_STATE));
        InsufficientCardsException thrown =
                assertThrows(InsufficientCardsException.class, () -> gm.leaderActivationHandler(1));
        String expectedMessage = "You don't have enough cards to do this operation";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertTrue(gm.getCurrentState().isRightState(GameStates.END_TURN_STATE));
    }

    /**
     * this test tries to discard leader card with wrong parameters
     */
    @Test
    void discardLeaderWrongParameters() throws FullGameException, WrongParametersException, IllegalStateException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        WrongParametersException thrown =
                assertThrows(WrongParametersException.class, () -> cg.leaderDiscardHandler(new MessageOneParameterInt(MessageType.LEADER_CARD_DISCARD, 0 ,0)));
        String expectedMessage = "You have inserted wrong parameters";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new MessageOneParameterInt(MessageType.LEADER_CARD_DISCARD, 0 ,0));
    }

    /**
     * this test tries to discard LeaderCard during wrong state of game
     */
    @Test
    void leaderDiscardWrongState() throws AlreadyTakenNicknameException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new TakeMarbleState());

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> gm.leaderDiscardHandler(1));
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct discard LeaderCard
     */
    @Test
    void correctLeaderDiscard() throws FullGameException, AlreadyTakenNicknameException, WrongParametersException, IllegalStateException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        cg.addView(v);
        v.setNickName("Daniele");
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add((LeaderCard) CardMapCLI.getCard(49));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(57));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(60));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(61));
        gm.leaderCardHandler(0, 57, 60, leaderCards);

        gm.leaderDiscardHandler(1);
    }

    /**
     * this test tries to end turn during wrong state of game
     */
    @Test
    void endTurnWrongState() throws IllegalStateException, FullGameException, WrongParametersException {

        ControllerGame cg = new ControllerGame();
        VirtualView v = new VirtualView();
        v.setNickName("D");
        cg.addView(v);
        cg.newGame(new MessageOneParameterInt(MessageType.NUM_PLAYERS, 0, 1));

        IllegalStateException thrown =
                assertThrows(IllegalStateException.class, () -> cg.endTurnHandler());
        String expectedMessage = "You can't do this operation at this moment";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        cg.update(v, new Message(MessageType.END_TURN, 0));
    }

    /**
     * this test verifies the correct end turn procedure
     */
    @Test
    void correctEndTurn() throws AlreadyTakenNicknameException, IllegalStateException {

        ControllerGame cg = new ControllerGame();
        View v = new VirtualView();
        GameManager gm = new GameManager(1, cg);
        gm.createPlayer(v, "Daniele");
        gm.setCurrentState(new EndTurnState());

        assertTrue(gm.getCurrentState().isRightState(GameStates.END_TURN_STATE));
        gm.endTurnHandler();
        assertTrue(gm.getCurrentState().isRightState(GameStates.FIRST_ACTION_STATE));
    }
}
