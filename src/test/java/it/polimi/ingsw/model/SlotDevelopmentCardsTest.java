package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InsufficientResourceException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class SlotDevelopmentCardsTest {

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
     * this test verifies the correct activation of production power of active DevelopmentCard
     */
    @Test
    void correctProductionPower() throws InsufficientResourceException{

        Resource r1 = Resource.COIN;
        SlotDevelopmentCards slot = new SlotDevelopmentCards();
        Warehouse w = new Warehouse();
        w.increaseResource(r1);
        Strongbox s = new Strongbox();

        assertEquals(0, slot.activateProductionActiveCard(w, s, 1));

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(r1, 1);
        Cost c3 = new Cost();
        DevelopmentCard card = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        slot.addDevelopmentCard(card);

        assertEquals(1, slot.activateProductionActiveCard(w, s, 1));
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
