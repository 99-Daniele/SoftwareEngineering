package it.polimi.ingsw.model.leaderCardsTests;

import it.polimi.ingsw.exceptions.ActiveLeaderCardException;
import it.polimi.ingsw.model.developmentCardsTests.Color;
import it.polimi.ingsw.model.developmentCardsTests.DevelopmentCard;
import it.polimi.ingsw.model.leaderCards.*;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.InsufficientCardsException;
import it.polimi.ingsw.exceptions.InsufficientResourceException;

public class LeaderCardTest {

    /**
     * this test verifies the correct overriding of the 4 type of leaderCard
     */
    @Test
    void correctOverriding() throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.SHIELD;
        Cost c1 = new Cost();
        c1.addResource(r1, 2);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);

        Cost c = new Cost();
        c.addResource(r1, 3);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 3);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card1 = new AdditionalProductionPowerCard(r1, c, 2);
        LeaderCard card2 = new DiscountCard(r1, c, 2);
        LeaderCard card3 = new WhiteConversionCard(r1, c, 2);
        LeaderCard card4 = new ExtraDepotCard(r1, c, 2);

        assertFalse(w.existExtraDepot());

        card1.activateCard(w, s, l);
        assertTrue(card1.isActive());
        assertFalse(w.existExtraDepot());

        card2.activateCard(w, s, l);
        assertTrue(card2.isActive());
        assertFalse(w.existExtraDepot());

        card3.activateCard(w, s, l);
        assertTrue(card3.isActive());
        assertFalse(w.existExtraDepot());

        card4.activateCard(w, s, l);
        assertTrue(card4.isActive());
        assertTrue(w.existExtraDepot());
        /*
         card4 is ExtraDepotCard so creates a new ExtraDepot, other cards simply activate
         */

        assertEquals(3, s.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));

        assertEquals(1, card1.additionalProductionPower(w, s, 2, r2));
        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));

        assertEquals(0, card2.additionalProductionPower(w, s, 2, r2));
        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));

        assertEquals(0, card3.additionalProductionPower(w, s, 2, r2));
        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));

        assertEquals(0, card4.additionalProductionPower(w, s, 2, r2));
        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));
        /*
         card1 is AdditionalProductionPowerCard so convert 1 r1 in @param resource r2 and @return 1 faith point,
         other cards do nothing
         */

        card1.decreaseProductionPowerResources(w, s, 2);
        assertEquals(1, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));

        card2.decreaseProductionPowerResources(w, s, 2);
        assertEquals(1, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));

        card3.decreaseProductionPowerResources(w, s, 2);
        assertEquals(1, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));

        card4.decreaseProductionPowerResources(w, s, 2);
        assertEquals(1, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));
        /*
         card1 is AdditionalProductionPowerCard so decrease by 1 r1, other cards do nothing
         */

        assertFalse(card1.discount(developmentCard));
        assertTrue(card2.discount(developmentCard));
        assertFalse(card3.discount(developmentCard));
        assertFalse(card4.discount(developmentCard));
        /*
         card2 is DiscountCard so decrease by 1 r1 the cost of developmentCard and @return true, other cards @return false
         */

        assertFalse(card1.whiteConversion());
        assertFalse(card2.whiteConversion());
        assertTrue(card3.whiteConversion());
        assertFalse(card4.whiteConversion());
        /*
         card3 is WhiteConversionCard so @return true because is active, other cards @return false
         */
    }

}
