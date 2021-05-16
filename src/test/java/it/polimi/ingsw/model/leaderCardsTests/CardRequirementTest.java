package it.polimi.ingsw.model.leaderCardsTests;

import it.polimi.ingsw.model.developmentCards.*;
import it.polimi.ingsw.model.leaderCards.CardRequirement;
import it.polimi.ingsw.model.resourceContainers.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardRequirementTest {

    /**
     * check if CardRequirement has been initialized correctly by DevelopmentCard
     */
    @Test
    void correctLeaderRequirementsCreationByDevelopmentCard(){

        Resource r1 = Resource.COIN;
        Cost c1 = new Cost();
        c1.addResource(r1, 1);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 1, cardID);

        CardRequirement cardRequirement = new CardRequirement(developmentCard);

        assertSame(Color.BLUE, cardRequirement.getColor());
        assertEquals(1, cardRequirement.getNumOfCards());
        assertTrue(cardRequirement.containsLevel(2));
    }

    /**
     * check if CardRequirement has been initialized correctly by input value.
     */
    @Test
    void correctLeaderRequirementsCreationByInputValue() {

        CardRequirement cardRequirement = new CardRequirement(Color.YELLOW,1, 3);

        assertSame(Color.YELLOW,cardRequirement.getColor());
        assertNotSame(Color.BLUE,cardRequirement.getColor());
        assertEquals(1, cardRequirement.getNumOfCards());
        assertTrue(cardRequirement.containsLevel(3));
    }

    /**
     * this test verifies if CardRequirement contains required levels
     */
    @Test
    void containsRequiredLevel(){

        CardRequirement cardRequirement = new CardRequirement(Color.YELLOW,1, 3);

        assertTrue(cardRequirement.containsLevel(0));
        assertFalse(cardRequirement.containsLevel(1));
        assertFalse(cardRequirement.containsLevel(2));
        assertTrue(cardRequirement.containsLevel(3));
    }

    /**
     * check if number of cards have been initialized correctly by input value,
     * and check numOfCards after increasing its number by 1 unit.
     */
    @Test
    void increaseCardNumberTest() {

        CardRequirement cardRequirement = new CardRequirement(Color.YELLOW, 1, 2);

        assertEquals(1, cardRequirement.getNumOfCards());

        cardRequirement.increaseNumOfCards();
        assertEquals(2, cardRequirement.getNumOfCards());
        assertNotEquals(1, cardRequirement.getNumOfCards());

        CardRequirement cardRequirement1=new CardRequirement(Color.GREEN,3,2);

        assertEquals(3, cardRequirement1.getNumOfCards());
        assertNotEquals(2,cardRequirement1.getNumOfCards());

        cardRequirement1.increaseNumOfCards();
        assertEquals(4, cardRequirement1.getNumOfCards());
    }

    /**
     * check the level after adding another value.
     */
    @Test
    void addLevelTest() {

        CardRequirement cardRequirements = new CardRequirement(Color.BLUE,1, 1);

        assertTrue(cardRequirements.containsLevel(1));

        cardRequirements.addLevel(3);
        assertTrue(cardRequirements.containsLevel(1));
        assertTrue(cardRequirements.containsLevel(3));

        cardRequirements.addLevel(2);
        assertTrue(cardRequirements.containsLevel(1));
        assertTrue(cardRequirements.containsLevel(2));
        assertTrue(cardRequirements.containsLevel(3));
    }

    /**
     * this test verifies the correct comparison of levels between CardRequirements
     */
    @Test
    void compareCardRequirements(){

        CardRequirement cardRequirements1 = new CardRequirement(Color.BLUE,2, 2);
        CardRequirement cardRequirements2 = new CardRequirement(Color.BLUE,1, 2);
        CardRequirement cardRequirements3 = new CardRequirement(Color.YELLOW,3, 1);

        assertTrue(cardRequirements1.enoughLevels(cardRequirements2));
        assertFalse(cardRequirements1.enoughLevels(cardRequirements3));

        cardRequirements3.addLevel(2);
        assertTrue(cardRequirements1.enoughLevels(cardRequirements3));

        CardRequirement cardRequirements0 = new CardRequirement(Color.GREEN,5, 0);
        assertTrue(cardRequirements0.enoughLevels(cardRequirements1));
        assertTrue(cardRequirements0.enoughLevels(cardRequirements2));
        assertTrue(cardRequirements0.enoughLevels(cardRequirements3));
    }
}
