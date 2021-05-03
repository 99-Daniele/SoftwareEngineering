package it.polimi.ingsw.model.leaderCardsTests;

import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.developmentCards.*;
import it.polimi.ingsw.model.leaderCards.*;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.resourceContainers.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WhiteConversionCardTest {

    /**
     * this test verifies the return of victory points if WhiteConversionCard is active or not
     */
    @Test
    void getVictoryPoints() throws InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Resource r1 = Resource.COIN;
        Cost c = new Cost();
        LeaderCard card = new WhiteConversionCard(r1, c, 2);

        assertEquals(0, card.getVictoryPoints());

        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();
        LeaderRequirements l = new LeaderRequirements();
        card.activateCard(w, s, l);

        assertEquals(2, card.getVictoryPoints());
    }

    /**
     * this test tries to activate WhiteConversionCard if player has not enough resources
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

        LeaderCard card = new WhiteConversionCard(r1, c, 2);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> card.activateCard(w, s, l));
        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertFalse(card.isActive());
    }

    /**
     * this test tries to activate WhiteConversionCard if player has not enough cards
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

        LeaderCard card = new WhiteConversionCard(r1, leaderRequirements, 2);

        InsufficientCardsException thrown =
                assertThrows(InsufficientCardsException.class, () -> card.activateCard(w, s, l));
        String expectedMessage = "Non hai abbastanza carte per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
        assertFalse(card.isActive());
    }

    /**
     * this test tries to activate an already active WhiteConversionCard
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

        LeaderCard card = new WhiteConversionCard(r2, c, 2);
        card.activateCard(w, s, l);
        assertTrue(card.isActive());

        ActiveLeaderCardException thrown =
                assertThrows(ActiveLeaderCardException.class, () -> card.activateCard(w, s, l));

        String expectedMessage = "Questa carta è stata già attivata in precedenza";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of WhiteConversionCard
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

        LeaderCard card = new WhiteConversionCard(r2, c, 2);

        card.activateCard(w, s, l);
        assertTrue(card.isActive());
    }
}