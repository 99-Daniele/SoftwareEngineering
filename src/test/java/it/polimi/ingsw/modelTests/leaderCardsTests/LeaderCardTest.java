package it.polimi.ingsw.modelTests.leaderCardsTests;

import it.polimi.ingsw.model.cards.developmentCards.DevelopmentCard;
import it.polimi.ingsw.model.cards.leaderCards.*;
import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.cards.developmentCards.Color;

import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LeaderCardTest {

    /**
     * this test verifies the correct overriding of the 4 type of leaderCard
     */
    @Test
    void correctOverriding()
            throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException, NoSuchProductionPowerException {

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.SHIELD;
        Cost c1 = new Cost();
        c1.addResource(r1, 2);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1, cardID);

        Cost c = new Cost();
        c.addResource(r1, 3);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 3);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card1 = new AdditionalProductionPowerCard(r1, c, 2, 0);
        LeaderCard card2 = new DiscountCard(r1, c, 2, 0);
        LeaderCard card3 = new WhiteConversionCard(r1, c, 2, 0);
        LeaderCard card4 = new ExtraDepotCard(r1, c, 2, 0);

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

        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> card2.additionalProductionPower(w, s, 1, r2));
        String expectedMessage = "You don't have any card to activate this power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        assertTrue(actualMessage.contains(expectedMessage));
        assertEquals(3, s.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));

        thrown = assertThrows(NoSuchProductionPowerException.class, () -> card3.additionalProductionPower(w, s, 1, r2));
        expectedMessage = "You don't have any card to activate this power";
        actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        assertEquals(3, s.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));

        thrown = assertThrows(NoSuchProductionPowerException.class, () -> card4.additionalProductionPower(w, s, 1, r2));
        expectedMessage = "You don't have any card to activate this power";
        actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        assertEquals(3, s.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));

        assertEquals(1, card1.additionalProductionPower(w, s, 2, r2));
        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));
        /*
         card1 is AdditionalProductionPowerCard so convert 1 r1 in @param resource r2 and @return 1 faith point,
         other cards do nothing
         */

        thrown = assertThrows(NoSuchProductionPowerException.class, () -> card2.decreaseProductionPowerResources(w, s, 1));
        expectedMessage = "You don't have any card to activate this power";
        actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));

        thrown = assertThrows(NoSuchProductionPowerException.class, () -> card3.decreaseProductionPowerResources(w, s, 1));
        expectedMessage = "You don't have any card to activate this power";
        actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));

        thrown = assertThrows(NoSuchProductionPowerException.class, () -> card4.decreaseProductionPowerResources(w, s, 1));
        expectedMessage = "You don't have any card to activate this power";
        actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));

        card1.decreaseProductionPowerResources(w, s, 2);
        assertEquals(1, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));
        /*
         card1 is AdditionalProductionPowerCard so decrease by 1 r1, other cards do nothing
         */

        assertFalse(developmentCard.isBuyable(w, s));

        assertFalse(card1.discount(developmentCard));
        assertFalse(developmentCard.isBuyable(w, s));

        assertFalse(card3.discount(developmentCard));
        assertFalse(developmentCard.isBuyable(w, s));

        assertFalse(card4.discount(developmentCard));
        assertFalse(developmentCard.isBuyable(w, s));

        assertTrue(card2.discount(developmentCard));
        assertTrue(developmentCard.isBuyable(w, s));
        /*
         card2 is DiscountCard so decrease by 1 r1 the cost of developmentCard and @return true. Now the card is buyable
         */

        card1.recount(developmentCard);
        assertTrue(developmentCard.isBuyable(w, s));

        card3.recount(developmentCard);
        assertTrue(developmentCard.isBuyable(w, s));

        card4.recount(developmentCard);
        assertTrue(developmentCard.isBuyable(w, s));

        card2.recount(developmentCard);
        assertFalse(developmentCard.isBuyable(w, s));
        /*
         card2 is DiscountCard so increase by 1 r1 the cost of developmentCard and @return true. Now the card
         return to the original cost so it's not buyable
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
