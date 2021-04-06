package it.polimi.ingsw.model;

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
        assertEquals(1, l.getMaxLevelOfCards(Color.BLUE));
    }

    /**
     * this test verifies the correct addition of CardRequirement in LeaderRequirements
     */
    @Test
    void correctAdditionCardRequirement(){

        Resource r1 = Resource.COIN;
        Cost c1 = new Cost();
        c1.addResource(r1, 1);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 1);

        LeaderRequirements l = new LeaderRequirements();

        l.addCardRequirement(developmentCard);
        assertEquals(1, l.getNumOfCards(developmentCard.getColor()));
        assertEquals(2, l.getMaxLevelOfCards(developmentCard.getColor()));

        l.addCardRequirement(developmentCard);
        assertEquals(2, l.getNumOfCards(developmentCard.getColor()));
        assertEquals(2, l.getMaxLevelOfCards(developmentCard.getColor()));
    }

    /**
     * this test verifies if different LeaderRequirements has enough required cards.
     */
    @Test
    void verifyEnoughRequiredCards(){

        LeaderRequirements l1 = new LeaderRequirements();
        LeaderRequirements l2 = new LeaderRequirements();
        LeaderRequirements l3 = new LeaderRequirements();

        l1.createCardRequirement(Color.BLUE, 2, 1);
        l2.createCardRequirement(Color.BLUE, 4, 2);
        l3.createCardRequirement(Color.YELLOW, 1, 1);

        assertTrue(l1.enoughCardRequirements(l2));
        assertFalse(l1.enoughCardRequirements(l3));
    }

}
