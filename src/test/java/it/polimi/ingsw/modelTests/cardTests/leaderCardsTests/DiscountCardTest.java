package it.polimi.ingsw.modelTests.cardTests.leaderCardsTests;

import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.cards.developmentCards.*;
import it.polimi.ingsw.model.cards.leaderCards.*;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.resourceContainers.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class DiscountCardTest {

    /**
     * this test verifies the return of victory points if DiscountCard is active or not
     */
    @Test
    void getVictoryPoints() throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Resource r1 = Resource.COIN;
        Cost c = new Cost();
        LeaderCard card = new DiscountCard(r1, c, 2, 0);

        assertEquals(0, card.getCurrentVictoryPoints());

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        LeaderRequirements l = new LeaderRequirements();
        card.activateCard(w, s, l);

        assertEquals(2, card.getVictoryPoints());
    }

    /**
     * this test tries to activate DiscountCard if player has not enough resources
     */
    @Test
    void incorrectActivationCardInsufficientResources(){

        Resource r1 = Resource.COIN;
        Cost c = new Cost();
        c.addResource(r1, 3);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 2);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new DiscountCard(r1, c, 2, 0);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> card.activateCard(w, s, l));
        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertFalse(card.isActive());
    }

    /**
     * this test tries to activate DiscountCard if player has not enough cards
     */
    @Test
    void incorrectActivationCardInsufficientCards(){

        Resource r1 = Resource.COIN;
        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1, cardID);
        LeaderRequirements leaderRequirements = new LeaderRequirements();
        leaderRequirements.addCardRequirement(developmentCard);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new DiscountCard(r1, leaderRequirements, 2, 0);

        InsufficientCardsException thrown =
                assertThrows(InsufficientCardsException.class, () -> card.activateCard(w, s, l));
        String expectedMessage = "You don't have enough cards to do this operation";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertFalse(card.isActive());
    }

    /**
     * this test tries to activate an already active DiscountCard
     */
    @Test
    void incorrectActivationCardActive() throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.STONE;
        Cost c = new Cost();
        c.addResource(r1, 3);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 3);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new DiscountCard(r2, c, 2, 0);
        card.activateCard(w, s, l);
        assertTrue(card.isActive());

        ActiveLeaderCardException thrown =
                assertThrows(ActiveLeaderCardException.class, () -> card.activateCard(w, s, l));

        String expectedMessage = "This card has already been activate";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of DiscountCard
     */
    @Test
    void correctActivation() throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.STONE;
        Cost c = new Cost();
        c.addResource(r1, 3);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 3);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new DiscountCard(r2, c, 2, 0);

        card.activateCard(w, s, l);
        assertTrue(card.isActive());
    }

    /**
     * this test verify the correct discount
     */
    @Test
    void correctDiscount() throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

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

        LeaderCard card1 = new DiscountCard(r1, c, 2, 0);
        LeaderCard card2 = new DiscountCard(r2, c, 2, 0);

        assertFalse(card1.discount(developmentCard));
        /*
         card is yet inactive
         */

        card1.activateCard(w, s, l);
        card2.activateCard(w, s, l);

        assertTrue(card1.discount(developmentCard));
        assertFalse(card2.discount(developmentCard));
        /*
         developmentCard now costs 1 r1
         */

        assertTrue(card1.discount(developmentCard));
        assertFalse(card1.discount(developmentCard));
        /*
         developmentCard noe costs 0 r1
         */
    }

    /**
     * this test verify the correct recount
     */
    @Test
    void correctRecount() throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Resource r1 = Resource.COIN;
        Cost c1 = new Cost();
        c1.addResource(r1, 1);
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

        LeaderCard card = new DiscountCard(r1, c, 2, 0);

        card.activateCard(w, s, l);

        assertTrue(card.discount(developmentCard));
        assertFalse(card.discount(developmentCard));
        /*
         developmentCard now costs 0 r1
         */

        card.recount(developmentCard);
        /*
         developmentCard now costs 1 r1
         */

        assertTrue(card.discount(developmentCard));
        /*
         developmentCard now costs 0 r1
         */
    }
}
