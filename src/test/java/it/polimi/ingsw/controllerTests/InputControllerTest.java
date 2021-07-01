package it.polimi.ingsw.controllerTests;

import it.polimi.ingsw.controller.InputController;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.*;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.parser.CardMapCLI;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class InputControllerTest {

    /**
     * test the correct check parameters of login message.
     */
    @Test
    void correctLoginCheck(){

        MessageOneParameterString m1 = new MessageOneParameterString(MessageType.LOGIN, 0, null);
        MessageOneParameterString m2 = new MessageOneParameterString(MessageType.LOGIN, 1, "Daniele");
        assertFalse(InputController.loginCheck(m1.getPar()));
        assertTrue(InputController.loginCheck(m2.getPar()));
    }

    /**
     * test the correct check of num players message.
     */
    @Test
    void correctNumPlayersCheck(){

        assertFalse(InputController.numPlayersCheck(0));
        assertFalse(InputController.numPlayersCheck(5));
        assertTrue(InputController.numPlayersCheck(3));
    }

    /**
     * test the correct check of first leader cards choices.
     */
    @Test
    void correctLeaderCardsCheck(){

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        leaderCards.add((LeaderCard) CardMapCLI.getCard(49));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(53));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(64));
        leaderCards.add((LeaderCard) CardMapCLI.getCard(57));

        assertFalse(InputController.leaderCardCheck(10, 74, leaderCards));
        assertFalse(InputController.leaderCardCheck(51, 64, leaderCards));
        assertFalse(InputController.leaderCardCheck(51, 64, leaderCards));
        assertFalse(InputController.leaderCardCheck(53, 56, leaderCards));
        assertFalse(InputController.leaderCardCheck(51, 51, leaderCards));
        assertFalse(InputController.leaderCardCheck(53, 53, leaderCards));
        assertTrue(InputController.leaderCardCheck(49, 57, leaderCards));
    }

    /**
     * test the correct check of taken marbles message.
     */
    @Test
    void correctTakeMarbleCheck(){

        assertFalse(InputController.takenMarblesCheck(2, 3));
        assertFalse(InputController.takenMarblesCheck(0, 0));
        assertFalse(InputController.takenMarblesCheck(1, 5));
        assertTrue(InputController.takenMarblesCheck(0, 3));
    }

    /**
     * test the correct check of switch depot message.
     */
    @Test
    void correctSwitchDepotCheck(){

        assertFalse(InputController.switchDepotCheck(0, 7));
        assertFalse(InputController.switchDepotCheck(0, 4));
        assertFalse(InputController.switchDepotCheck(1, 6));
        assertFalse(InputController.switchDepotCheck(4, 4));
        assertFalse(InputController.switchDepotCheck(0, 0));
        assertTrue(InputController.switchDepotCheck(3, 4));
    }

    /**
     * test the correct check of buy card message.
     */
    @Test
    void correctBuyCardCheck(){

        assertFalse(InputController.buyCardCheck(0, 5, 2));
        assertFalse(InputController.buyCardCheck(0, 4, 2));
        assertFalse(InputController.buyCardCheck(1, 5, 2));
        assertFalse(InputController.buyCardCheck(0, 5, 0));
        assertFalse(InputController.buyCardCheck(1, 4, 2));
        assertFalse(InputController.buyCardCheck(0, 3, 1));
        assertFalse(InputController.buyCardCheck(1, 0, 1));
        assertTrue(InputController.buyCardCheck(1, 3, 0));
    }

    /**
     * test the correct check of chosen slot message.
     */
    @Test
    void correctChosenSlotCheck(){

        ArrayList<Integer> slots1 = new ArrayList<>(2);
        ArrayList<Integer> slots2 = new ArrayList<>(2);
        ArrayList<Integer> slots3 = new ArrayList<>(2);
        ArrayList<Integer> slots4 = new ArrayList<>(3);

        slots1.add(1);
        slots1.add(2);
        assertFalse(InputController.chosenSlotCheck(0, slots1));
        assertFalse(InputController.chosenSlotCheck(5, slots1));
        assertFalse(InputController.chosenSlotCheck(3, slots1));
        assertTrue(InputController.chosenSlotCheck(1, slots1));
        assertTrue(InputController.chosenSlotCheck(2, slots1));

        slots2.add(1);
        slots2.add(3);
        assertFalse(InputController.chosenSlotCheck(0, slots2));
        assertFalse(InputController.chosenSlotCheck(5, slots2));
        assertFalse(InputController.chosenSlotCheck(2, slots2));
        assertTrue(InputController.chosenSlotCheck(1, slots2));
        assertTrue(InputController.chosenSlotCheck(3, slots2));

        slots3.add(2);
        slots3.add(3);
        assertFalse(InputController.chosenSlotCheck(0, slots3));
        assertFalse(InputController.chosenSlotCheck(5, slots3));
        assertFalse(InputController.chosenSlotCheck(1, slots3));
        assertTrue(InputController.chosenSlotCheck(3, slots3));
        assertTrue(InputController.chosenSlotCheck(2, slots3));

        slots4.add(1);
        slots4.add(2);
        slots4.add(3);
        assertFalse(InputController.chosenSlotCheck(0, slots4));
        assertFalse(InputController.chosenSlotCheck(5, slots4));
        assertTrue(InputController.chosenSlotCheck(1, slots4));
        assertTrue(InputController.chosenSlotCheck(2, slots4));
        assertTrue(InputController.chosenSlotCheck(3, slots4));
    }

    /**
     * test the correct check of development card power message.
     */
    @Test
    void correctDevelopmentCardPowerCheck(){

        ArrayList<Integer> chosenSlot = new ArrayList<>();
        assertFalse(InputController.developmentCardPowerCheck(0, 2));
        assertFalse(InputController.developmentCardPowerCheck(0, 0));
        assertFalse(InputController.developmentCardPowerCheck(1, 2));
        assertTrue(InputController.developmentCardPowerCheck(3, 0));
        assertTrue(InputController.alreadyUsedDevelopmentCardPowerCheck(3, chosenSlot));
        chosenSlot.add(3);
        assertFalse(InputController.alreadyUsedDevelopmentCardPowerCheck(3, chosenSlot));
        assertTrue(InputController.alreadyUsedDevelopmentCardPowerCheck(1, chosenSlot));
        assertTrue(InputController.alreadyUsedDevelopmentCardPowerCheck(2, chosenSlot));
    }

    /**
     * test the correct check of basic power message.
     */
    @Test
    void correctBasicPowerCheck(){

        assertFalse(InputController.basicPowerCheck(2));
        assertTrue(InputController.basicPowerCheck(0));
        assertTrue(InputController.basicPowerCheck(1));
    }

    /**
     * test the correct check of leader card power message.
     */
    @Test
    void correctLeaderCardPowerCheck(){

        ArrayList<Integer> chosenLeaderCards = new ArrayList<>();
        assertFalse(InputController.leaderCardPowerCheck(0, 2));
        assertFalse(InputController.leaderCardPowerCheck(0, 0));
        assertFalse(InputController.leaderCardPowerCheck(1, 2));
        assertTrue(InputController.leaderCardPowerCheck(2, 0));
        assertTrue(InputController.alreadyUsedLeaderCardPowerCheck(2, chosenLeaderCards));
        chosenLeaderCards.add(2);
        assertFalse(InputController.alreadyUsedLeaderCardPowerCheck(2, chosenLeaderCards));
        assertTrue(InputController.alreadyUsedLeaderCardPowerCheck(1, chosenLeaderCards));
    }

    /**
     * test the correct check of white conversion card message.
     */
    @Test
    void correctWhiteConversionCardCheck(){

        assertFalse(InputController.whiteConversionCardCheck(0));
        assertTrue(InputController.whiteConversionCardCheck(1));
        assertTrue(InputController.whiteConversionCardCheck(2));
    }

    /**
     * test the correct check of use marbles message.
     */
    @Test
    void correctUseMarbleCheck(){

        ArrayList<Marble> marbles = new ArrayList<>(4);
        marbles.add(new WhiteMarble());
        marbles.add(new RedMarble());
        marbles.add(new ResourceMarble(Resource.COIN));
        marbles.add(new ResourceMarble(Resource.SERVANT));

        assertFalse(InputController.chosenCorrectMarble(new ResourceMarble(Resource.STONE), marbles));
        assertFalse(InputController.chosenCorrectMarble(new ResourceMarble(Resource.SHIELD), marbles));
        assertTrue(InputController.chosenCorrectMarble(new WhiteMarble(), marbles));
        assertTrue(InputController.chosenCorrectMarble(new ResourceMarble(Resource.COIN), marbles));
        assertTrue(InputController.chosenCorrectMarble(new ResourceMarble(Resource.SERVANT), marbles));
        assertTrue(InputController.chosenCorrectMarble(new RedMarble(), marbles));
    }

    /**
     * test the correct check of leader card activation or discard message.
     */
    @Test
    void correctLeaderCardChoiceCheck(){

        assertTrue(InputController.leaderCardChoice(0));
        assertTrue(InputController.leaderCardChoice(1));
        assertTrue(InputController.leaderCardChoice(2));
        assertFalse(InputController.leaderCardChoice(-1));
        assertFalse(InputController.leaderCardChoice(3));
    }
}
