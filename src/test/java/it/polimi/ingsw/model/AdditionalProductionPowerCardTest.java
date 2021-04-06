package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.InsufficientCardsException;
import it.polimi.ingsw.exceptions.InsufficientResourceException;

public class AdditionalProductionPowerCardTest {

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

        LeaderCard card = new AdditionalProductionPowerCard(r1, c, 2);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> card.activateCard(w, s, l));
        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
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
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        LeaderRequirements leaderRequirements = new LeaderRequirements();
        leaderRequirements.addCardRequirement(developmentCard);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new AdditionalProductionPowerCard(r1, leaderRequirements, 2);

        InsufficientCardsException thrown =
                assertThrows(InsufficientCardsException.class, () -> card.activateCard(w, s, l));
        String expectedMessage = "Non hai abbastanza carte per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of AdditionalProductionPowerCard
     */
    @Test
    void correctActivation() throws InsufficientResourceException, InsufficientCardsException{

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.STONE;
        Cost c = new Cost();
        c.addResource(r1, 3);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 3);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new AdditionalProductionPowerCard(r2, c, 2);

        card.activateCard(w, s, l);
    }



    /**
     * this test tries to use additionalProductionPower if player has not enough resources
     */
    @Test
    void incorrectAdditionalProductionPower() throws InsufficientResourceException, InsufficientCardsException{

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.STONE;
        Cost c = new Cost();
        c.addResource(r1, 3);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 3);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new AdditionalProductionPowerCard(r2, c, 2);

        card.activateCard(w, s, l);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> card.additionalProductionPower(w, s, 1, r2));
        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct operation of AdditionalProductionPower
     */
    @Test
    void correctAdditionalProductionPower() throws InsufficientResourceException, InsufficientCardsException {

        Resource r1 = Resource.COIN;
        Resource r2 = Resource.STONE;
        Cost c = new Cost();
        c.addResource(r1, 3);

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        s.increaseResourceType(r1, 3);
        LeaderRequirements l = new LeaderRequirements();

        LeaderCard card = new AdditionalProductionPowerCard(r1, c, 2);

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
