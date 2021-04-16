package it.polimi.ingsw.model.playersTests;

import it.polimi.ingsw.exceptions.InsufficientResourceException;
import it.polimi.ingsw.model.developmentCards.Color;
import it.polimi.ingsw.model.developmentCards.DevelopmentCard;
import it.polimi.ingsw.model.leaderCards.LeaderRequirements;
import it.polimi.ingsw.model.player.SlotDevelopmentCards;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class SlotDevelopmentCardsTest {


    /**
     * this test verifies if DevelopmentCard has the required level to be inserted in SlotDevelopmentCards
     */
    @Test
    void correctRequiredLevel(){

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();

        DevelopmentCard card1 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 1);
        assertFalse(slot.haveRequiredLevel(card1));

        DevelopmentCard card2 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        assertTrue(slot.haveRequiredLevel(card2));
    }

    /**
     * this test verifies the correct addition of DevelopmentCard
     */
    @Test
    void correctAdditionDevelopmentCard(){

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();

        DevelopmentCard card1 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 1);
        assertFalse(slot.addDevelopmentCard(card1));
        /*
         card1 has level == 2, while required level is 1
         */

        DevelopmentCard card2 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        assertTrue(slot.addDevelopmentCard(card2));

        DevelopmentCard card3 = new DevelopmentCard(Color.YELLOW, 2, c1, 1, c2, c3, 1);
        assertTrue(slot.addDevelopmentCard(card3));

        assertFalse(slot.addDevelopmentCard(card1));
        /*
         card1 has same level of card3
         */
    }

    /**
     * this test tries to activate a production power if there are not enough resources
     */
    @Test
    void incorrectProductionPower(){

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.SHIELD;

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Warehouse w = new Warehouse();
        w.increaseResource(r1);
        Strongbox s = new Strongbox();

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 2);
        Cost c3 = new Cost();
        c3.addResource(r2, 1);
        DevelopmentCard card = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> slot.activateProductionActiveCard(w, s, 1));

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of production power of active DevelopmentCard
     */
    @Test
    void correctProductionPower() throws InsufficientResourceException{

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.SHIELD;

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Warehouse w = new Warehouse();
        w.increaseResource(r1);
        Strongbox s = new Strongbox();

        assertEquals(0, slot.activateProductionActiveCard(w, s, 1));

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 1);
        Cost c3 = new Cost();
        c3.addResource(r2, 1);
        DevelopmentCard card = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card);
        assertEquals(1, w.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));

        assertEquals(1, slot.activateProductionActiveCard(w, s, 1));
        assertEquals(0, w.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));
    }

    /**
     * this test tries to remove resources by using production power if there are not enough resources
     */
    @Test
    void incorrectRemoveResourceProductionPower(){

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.SHIELD;

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Warehouse w = new Warehouse();
        w.increaseResource(r1);
        Strongbox s = new Strongbox();

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 2);
        Cost c3 = new Cost();
        c3.addResource(r2, 1);
        DevelopmentCard card = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> slot.removeProductionPowerResource(w, s));

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct removing of resources by using production power of active DevelopmentCard
     */
    @Test
    void correctRemoveResourceProductionPower() throws InsufficientResourceException {

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.SHIELD;

        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Warehouse w = new Warehouse();
        w.increaseResource(r1);
        Strongbox s = new Strongbox();

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 1);
        Cost c3 = new Cost();
        c3.addResource(r2, 1);

        slot.removeProductionPowerResource(w, s);
        assertEquals(1, w.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));

        DevelopmentCard card = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card);
        assertEquals(1, w.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));

        slot.removeProductionPowerResource(w, s);
        assertEquals(0, w.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));
    }

    /**
     * this test verifies the correct update of player's LeaderRequirements
     */
    @Test
    void correctUpdateLeaderRequirements() {

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
    void calculateSumResources() {

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
