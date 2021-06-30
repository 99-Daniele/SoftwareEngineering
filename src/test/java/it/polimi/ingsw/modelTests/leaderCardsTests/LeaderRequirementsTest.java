package it.polimi.ingsw.modelTests.leaderCardsTests;

import it.polimi.ingsw.model.cards.developmentCards.*;
import it.polimi.ingsw.model.cards.leaderCards.*;
import it.polimi.ingsw.model.resourceContainers.Cost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LeaderRequirementsTest {

    /**
     * this test verifies the correct creation of a new CardRequirement to add in LeaderRequirements
     */
    @Test
    void correctCreationCardRequirement(){

        LeaderRequirements l = new LeaderRequirements();

        l.createCardRequirement(Color.BLUE, 2, 1);

        assertEquals(2, l.getNumOfCards(Color.BLUE));
        assertTrue(l.getCardRequirement(Color.BLUE).containsLevel(1));
    }

    /**
     * this test verifies the correct addition of CardRequirement in LeaderRequirements
     */
    @Test
    void correctAdditionCardRequirement(){

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard1 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 1, cardID);

        LeaderRequirements l = new LeaderRequirements();
        l.addCardRequirement(developmentCard1);

        assertEquals(1, l.getNumOfCards(Color.BLUE));
        assertFalse(l.getCardRequirement(Color.BLUE).containsLevel(1));
        assertTrue(l.getCardRequirement(Color.BLUE).containsLevel(2));

        DevelopmentCard developmentCard2 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1, cardID);
        l.addCardRequirement(developmentCard2);

        assertEquals(2, l.getNumOfCards(Color.BLUE));
        assertTrue(l.getCardRequirement(Color.BLUE).containsLevel(1));
        assertTrue(l.getCardRequirement(Color.BLUE).containsLevel(2));
    }

    /**
     * this test verifies if different LeaderRequirements has enough required cards.
     */
    @Test
    void verifyEnoughRequiredCards(){

        LeaderRequirements l1 = new LeaderRequirements();
        LeaderRequirements l2 = new LeaderRequirements();
        LeaderRequirements l3 = new LeaderRequirements();
        LeaderRequirements l4 = new LeaderRequirements();
        LeaderRequirements l5 = new LeaderRequirements();

        l1.createCardRequirement(Color.BLUE, 2, 1);
        l2.createCardRequirement(Color.BLUE, 3, 1);
        l3.createCardRequirement(Color.BLUE, 4, 2);
        l4.createCardRequirement(Color.BLUE, 1, 1);
        l5.createCardRequirement(Color.YELLOW, 3, 1);

        assertTrue(l1.enoughCardRequirements(l2));
        assertFalse(l1.enoughCardRequirements(l3));
        assertFalse(l1.enoughCardRequirements(l4));
        assertFalse(l1.enoughCardRequirements(l5));

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard card = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1, cardID);
        l3.addCardRequirement(card);
        l4.addCardRequirement(card);
        l5.addCardRequirement(card);

        assertTrue(l1.enoughCardRequirements(l3));
        assertTrue(l1.enoughCardRequirements(l4));
        assertFalse(l1.enoughCardRequirements(l5));

        l5.addCardRequirement(card);
        assertTrue(l1.enoughCardRequirements(l5));
    }

    /**
     * this test counts the number of cards of one color
     */
    @Test
    void countNumOfCards(){

        LeaderRequirements l = new LeaderRequirements();

        l.createCardRequirement(Color.BLUE, 2, 1);
        assertEquals(2, l.getNumOfCards(Color.BLUE));

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.YELLOW, 2, c1, 1, c2, c3, 1, cardID);

        l.addCardRequirement(developmentCard);
        assertEquals(2, l.getNumOfCards(Color.BLUE));
        assertEquals(0, l.getNumOfCards(Color.GREEN));
        assertEquals(1, l.getNumOfCards(Color.YELLOW));
        assertEquals(0, l.getNumOfCards(Color.PURPLE));
    }

    /**
     * this test verifies the correct getting of CardRequirement
     */
    @Test
    void correctGetCardRequirement(){

        LeaderRequirements l = new LeaderRequirements();
        l.createCardRequirement(Color.BLUE, 2, 1);

        CardRequirement cardRequirement1 = l.getCardRequirement(Color.BLUE);
        CardRequirement cardRequirement2 = l.getCardRequirement(Color.YELLOW);

        assertSame(Color.BLUE, cardRequirement1.getColor());
        assertSame(Color.YELLOW, cardRequirement2.getColor());

        assertEquals(2, cardRequirement1.getNumOfCards());
        assertEquals(0, cardRequirement2.getNumOfCards());

        assertTrue(cardRequirement1.containsLevel(1));
        assertFalse(cardRequirement2.containsLevel(1));
        assertTrue(cardRequirement2.containsLevel(0));

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.YELLOW, 1, c1, 1, c2, c3, 1, cardID);

        l.addCardRequirement(developmentCard);
        CardRequirement cardRequirement3 = l.getCardRequirement(Color.YELLOW);

        assertSame(Color.YELLOW, cardRequirement3.getColor());
        assertEquals(1, cardRequirement3.getNumOfCards());
        assertTrue(cardRequirement3.containsLevel(1));
    }
}
