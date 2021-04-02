package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ResourceContainerTest {

    /**
     * this test verifies the correct increase of resource
     */
    @Test
    void correctIncrease(){

        ResourceContainer r = new ResourceContainer(Resource.COIN, 0);

        assertEquals(0, r.getAmount());

        r.increaseAmount(1);
        assertEquals(1, r.getAmount());

        r.increaseAmount(999999999);
        assertEquals(1000000000, r.getAmount());
    }

    /**
     * this test verifies the correct decrease of resource
     */
    @Test
    void correctDecrease(){

        ResourceContainer r = new ResourceContainer(Resource.COIN, 3);

        assertEquals(3, r.getAmount());

        assertEquals(0, r.decreaseAmount(1));
        assertEquals(2, r.getAmount());
        /*
         the ResourceContainer had amount == 3. After a decrease of 1, amount == 2 and the method @return 0 because
         the amount was successfully decreased
         */

        assertEquals(1, r.decreaseAmount(3));
        assertEquals(0, r.getAmount());
        /*
         after another decrease of 3, the ResourceContainer is now empty and the method @return 3 - 2 -> 1,
         which is the amount of resource still not decremented
         */

        assertEquals(4, r.decreaseAmount(4));
        assertEquals(0, r.getAmount());
        /*
         from now on, the ResourceContainer is empty so every decreaseAmount() call @return always @param amount
         */
    }
}
