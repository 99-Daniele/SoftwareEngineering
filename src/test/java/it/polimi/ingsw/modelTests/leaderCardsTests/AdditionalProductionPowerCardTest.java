package it.polimi.ingsw.modelTests.leaderCardsTests;

import it.polimi.ingsw.model.cards.developmentCards.Color;
import it.polimi.ingsw.model.cards.developmentCards.DevelopmentCard;
import it.polimi.ingsw.model.cards.leaderCards.AdditionalProductionPowerCard;
import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.cards.leaderCards.LeaderRequirements;
import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AdditionalProductionPowerCardTest {

    /**
     * this test verifies the return of victory points if AdditionalProductionPowerCard is active or not
     */
    @Test
    void getVictoryPoints() throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Resource r1 = Resource.COIN;
        Cost c = new Cost();
        LeaderCard card = new AdditionalProductionPowerCard(r1, c, 2, 1);

        assertEquals(0, card.getVictoryPoints());

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        LeaderRequirements l = new LeaderRequirements();
        card.activateCard(w, s, l);

        assertEquals(2, card.getVictoryPoints());
    }

    /**
     * this test tries to activate AdditionalProductionPowerCard if player has not enough resources
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

        LeaderCard card = new AdditionalProductionPowerCard(r1, c, 2, 0);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> card.activateCard(w, s, l));
        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertFalse(card.isActive());
    }

    /**
     * this test tries to activate AdditionalProductionPowerCard if player has not enough cards
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

        LeaderCard card = new AdditionalProductionPowerCard(r1, leaderRequirements, 2, 0);

        InsufficientCardsException thrown =
                assertThrows(InsufficientCardsException.class, () -> card.activateCard(w, s, l));
        String expectedMessage = "You don't have enough cards to do this operation";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertFalse(card.isActive());
    }

    /**
     * this test tries to activate an already active AdditionalProductionPowerCard
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

        LeaderCard card = new AdditionalProductionPowerCard(r2, c, 2, 0);
        card.activateCard(w, s, l);
        assertTrue(card.isActive());

        ActiveLeaderCardException thrown =
                assertThrows(ActiveLeaderCardException.class, () -> card.activateCard(w, s, l));

        String expectedMessage = "This card has already been activate";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of AdditionalProductionPowerCard
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

        LeaderCard card = new AdditionalProductionPowerCard(r2, c, 2, 0);

        card.activateCard(w, s, l);
        assertTrue(card.isActive());
    }

    /**
     * this test tries to decrease resources if player has not enough resources
     */
    @Test
    void incorrectDecreaseProductionPowerResources()
            throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.STONE;
        Cost c = new Cost();
        c.addResource(r1, 3);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 3);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new AdditionalProductionPowerCard(r2, c, 2, 0);

        card.activateCard(w, s, l);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> card.decreaseProductionPowerResources(w, s, 1));
        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct operation of AdditionalProductionPower
     */
    @Test
    void correctDecreaseProductionPowerResources()
            throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Resource r1 = Resource.COIN;
        Cost c = new Cost();
        c.addResource(r1, 2);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 1);
        w.increaseResource(r1);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new AdditionalProductionPowerCard(r1, c, 2, 0);

        card.decreaseProductionPowerResources(w, s, 2);
        assertEquals(1, s.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r1));
        /*
         card is yet inactive
         */

        card.activateCard(w, s, l);

        card.decreaseProductionPowerResources(w, s, 2);
        assertEquals(0, s.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r1));

        card.decreaseProductionPowerResources(w, s, 2);
        assertEquals(0, s.getNumOfResource(r1));
        assertEquals(0, w.getNumOfResource(r1));
    }

    /**
     * this test tries to use AdditionalProductionPower if player has not enough resources
     */
    @Test
    void incorrectAdditionalProductionPower() throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.STONE;
        Cost c = new Cost();
        c.addResource(r1, 3);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 3);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new AdditionalProductionPowerCard(r2, c, 2, 0);

        card.activateCard(w, s, l);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> card.additionalProductionPower(w, s, 1, r2));
        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct operation of AdditionalProductionPower
     */
    @Test
    void correctAdditionalProductionPower() throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.STONE;
        Cost c = new Cost();
        c.addResource(r1, 3);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 3);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new AdditionalProductionPowerCard(r1, c, 2, 0);

        assertEquals(0, card.additionalProductionPower(w, s, 2, r2));
        assertEquals(3, s.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));
        /*
         card is yet inactive
         */

        card.activateCard(w, s, l);

        assertEquals(1, card.additionalProductionPower(w, s, 2, r2));
        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));
    }
}
