package it.polimi.ingsw.modelTests.resourceContainersTests;


import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.model.resourceContainers.ResourceContainer;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResourceContainerTest {

    /**
     * this test verifies the correct increase of resource.
     */
    @Test
    void correctIncrease(){

        ResourceContainer r = new ResourceContainer(Resource.COIN, 0);

        assertEquals(0, r.getAmount());

        r.increaseAmount(1);
        assertEquals(1, r.getAmount());

        r.increaseAmount(999999999);
        assertEquals(1000000000, r.getAmount());
        /*
         resource container can contains unlimited resources
         */
    }

    /**
     * this test verifies the correct decrease of resource.
     */
    @Test
    void correctDecrease(){

        ResourceContainer r = new ResourceContainer(Resource.COIN, 3);

        assertEquals(3, r.getAmount());
        assertEquals(0, r.decreaseAmount(1));
        /*
         @return 0 because getAmount() > @param amount.
         */

        assertEquals(2, r.getAmount());
        assertEquals(1, r.decreaseAmount(3));
        /*
         @return 3 - 2 -> 1 because getAmount() < @param amount.
         */

        assertEquals(0, r.getAmount());
        assertEquals(4, r.decreaseAmount(4));
        assertEquals(0, r.getAmount());
        /*
         @return 4 - 0 -> 4 because getAmount == 0
         */
    }
}
