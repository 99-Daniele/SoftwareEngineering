package it.polimi.ingsw.modelTests.cardTests;

import it.polimi.ingsw.model.cards.Card;
import it.polimi.ingsw.model.cards.developmentCards.*;
import it.polimi.ingsw.model.cards.leaderCards.*;
import it.polimi.ingsw.model.resourceContainers.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    /**
     * this test verifies the correct Card overriding in DevelopmentCard and LeaderCard
     */
    @Test
    void correctOverriding(){

        Cost c = new Cost();
        Cost r = new Cost();
        Cost g = new Cost();
        LeaderRequirements l = new LeaderRequirements();
        Card dc = new DevelopmentCard(Color.BLUE, 2, c, 1, r, g, 4, 5);
        Card lc1 = new AdditionalProductionPowerCard(Resource.COIN, c, 3, 2);
        Card lc2 = new DiscountCard(Resource.STONE, l, 0, 14);

        assertNull(dc.getResource());
        assertSame(Resource.COIN, lc1.getResource());
        assertSame(Resource.STONE, lc2.getResource());

        assertSame(c, dc.getResourceCost());
        assertSame(c, lc1.getResourceCost());
        assertNull(lc2.getResourceCost());

        assertNull(dc.getLeaderRequirements());
        assertNull(lc1.getLeaderRequirements());
        assertSame(l, lc2.getLeaderRequirements());

        assertEquals(1, dc.getVictoryPoints());
        assertEquals(3, lc1.getVictoryPoints());
        assertEquals(0, lc2.getVictoryPoints());

        assertEquals(5, dc.getCardID());
        assertEquals(2, lc1.getCardID());
        assertEquals(14, lc2.getCardID());

        assertSame(Color.BLUE, dc.getColor());
        assertNull(lc1.getColor());
        assertNull(lc2.getColor());

        assertSame(r, dc.getProductionPowerResourceRequired());
        assertNull(lc1.getProductionPowerResourceRequired());
        assertNull(lc2.getProductionPowerResourceRequired());

        assertSame(g, dc.getProductionPowerResourceGiven());
        assertNull(lc1.getProductionPowerResourceGiven());
        assertNull(lc2.getProductionPowerResourceGiven());

        assertEquals(4, dc.getFaithPointsGiven());
        assertEquals(0, lc1.getFaithPointsGiven());
        assertEquals(0, lc2.getFaithPointsGiven());

        assertEquals(2, dc.getLevel());
        assertEquals(0, lc1.getLevel());
        assertEquals(0, lc2.getLevel());
    }
}
