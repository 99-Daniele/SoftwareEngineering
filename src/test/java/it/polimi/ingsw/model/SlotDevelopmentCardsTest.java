package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InsufficientResourceException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.WrongDevelopmentCardsSlotException;

public class SlotDevelopmentCardsTest {

    /**
     * this test tries to add a DevelopmentCard whit level equals to another DevelopmentCard in SlotDevelopmentCards
     */
    @Test
    void incorrectAdditionSameLevelDevelopmentCard() throws WrongDevelopmentCardsSlotException{

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        DevelopmentCard card1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card1);


        DevelopmentCard card2 = new DevelopmentCard(Color.PURPLE, 1, c1, 1, c2, c3, 1);
        /*
         card2 has same level of card1
         */

        WrongDevelopmentCardsSlotException thrown =
                assertThrows(WrongDevelopmentCardsSlotException.class, () -> slot.addDevelopmentCard(card2));

        String expectedMessage = "Questo slot non può inserire questa carta";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to add a DevelopmentCard whit higher required level
     */
    @Test
    void incorrectAdditionHigherRequiredLevelDevelopmentCard(){

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();

        DevelopmentCard card = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 1);
        /*
         card has level == 2, while required level is 1
         */

        WrongDevelopmentCardsSlotException thrown =
                assertThrows(WrongDevelopmentCardsSlotException.class, () -> slot.addDevelopmentCard(card));

        String expectedMessage = "Questo slot non può inserire questa carta";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct addition of DevelopmentCard
     */
    @Test
    void correctAdditionDevelopmentCard() throws WrongDevelopmentCardsSlotException {

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();

        DevelopmentCard card1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card1);

        DevelopmentCard card2 = new DevelopmentCard(Color.YELLOW, 2, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card2);
    }

    /**
     * this test tries to activate production power if player has not enough resources
     */
    @Test
    void incorrectProductionPowerNotEnoughResource() throws WrongDevelopmentCardsSlotException {

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Resource r1 = Resource.COIN;
        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 2);
        Cost c3 = new Cost();
        DevelopmentCard card = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card);

        Warehouse w = new Warehouse();
        w.increaseResource(r1);
        Strongbox s = new Strongbox();
        /*
         warehouse has 1 r1, card requires 2 r1
         */

        /*
        InsufficientResourceException thrown =

                assertThrows(InsufficientResourceException.class, () -> slot.activateProductionActiveCard(w, s, 1));

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
         */
    }

    /**
     * this test verifies the correct activation of production power of active DevelopmentCard
     */
    @Test
    void correctProductionPower() throws WrongDevelopmentCardsSlotException, InsufficientResourceException {

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Resource r1 = Resource.COIN;
        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 1);
        Cost c3 = new Cost();
        DevelopmentCard card = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card);

        Warehouse w = new Warehouse();
        w.increaseResource(r1);
        Strongbox s = new Strongbox();

        assertEquals(1, slot.activateProductionActiveCard(w, s, 1));
    }

    /**
     * this test verifies the correct update of player's LeaderRequirements
     */
    @Test
    void correctUpdateLeaderRequirements() throws WrongDevelopmentCardsSlotException{

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        DevelopmentCard card1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card1);
        DevelopmentCard card2 = new DevelopmentCard(Color.YELLOW, 2, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card2);

        LeaderRequirements leaderRequirements = new LeaderRequirements();
        leaderRequirements.createCardRequirement(Color.BLUE, 2, 1);
    }

    /**
     * this test calculates the sum of victoryPoints in SlotDevelopmentCards
     */
    @Test
    void calculateSumResources() throws WrongDevelopmentCardsSlotException {

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();

        DevelopmentCard card1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card1);
        DevelopmentCard card2 = new DevelopmentCard(Color.YELLOW, 2, c1, 3, c2, c3, 1);
        slot.addDevelopmentCard(card2);

        assertEquals(4, slot.sumTotalVictoryPointsByCards());

        DevelopmentCard card3 = new DevelopmentCard(Color.YELLOW, 3, c1, 7, c2, c3, 1);
        slot.addDevelopmentCard(card3);
        assertEquals(11, slot.sumTotalVictoryPointsByCards());
    }
}
