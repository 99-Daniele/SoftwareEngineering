package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardRequirementTest {
    /**
     * check if the color has been initialized correctly, to the input value.
     */
    @Test
    void exactColorTest()
    {
        CardRequirement cardRequirement=new CardRequirement(it.polimi.ingsw.model.Color.YELLOW,3);
        assertSame(Color.YELLOW,cardRequirement.getColor());
        assertNotSame(Color.BLUE,cardRequirement.getColor());
    }

    /**
     * check if number of cards have been initialized correctly, to the input value,
     * and check the numOfCards after increasing its number by 1 unit.
     */
    @Test
    void increaseCardNumberTest() {
        CardRequirement cardRequirement = new CardRequirement(Color.YELLOW, 2);
        assertEquals(1, cardRequirement.getNumOfCards());
        cardRequirement.increaseNumOfCards();
        assertEquals(2, cardRequirement.getNumOfCards());
        assertNotEquals(1, cardRequirement.getNumOfCards());


        CardRequirement cardRequirement1=new CardRequirement(Color.GREEN,3,2);
        assertEquals(3, cardRequirement1.getNumOfCards());
        assertNotEquals(1,cardRequirement1.getNumOfCards());
        cardRequirement1.increaseNumOfCards();
        assertEquals(4, cardRequirement1.getNumOfCards());
    }

    /**
     * check if the max level has been initialized correctly, to the input value,
     * and check the level after setting this to another value.
     */
    @Test
    void setLevelTest()
    {
        CardRequirement cardRequirements=new CardRequirement(Color.BLUE,1);
        assertEquals(1,cardRequirements.getMaxLevel());
        cardRequirements.setMaxLevel(3);
        assertNotEquals(1,cardRequirements.getMaxLevel());
        assertEquals(3,cardRequirements.getMaxLevel());
    }

}
