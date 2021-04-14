package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ExtraDepotTest {

    /**
     * this test verifies the correct construction of ExtraDepot.
     */
    @Test
    void creationExtraDepot(){

        ExtraDepot d = new ExtraDepot(Resource.COIN);

        assertTrue(d.isEmpty());
        assertEquals(2, d.getMaxAmount());
        assertSame(Resource.COIN, d.getResource());
    }

    /**
     * this test verifies correct set of resource.
     */
    @Test
    void correctSetAmountExtraDepot(){

        ExtraDepot d = new ExtraDepot(Resource.COIN);

        assertTrue(d.isEmpty());
        d.setAmount(1);
        assertEquals(1, d.getAmount());
        assertFalse(d.isEmpty());

        d.setAmount(0);
        assertEquals(0, d.getAmount());
        assertTrue(d.isEmpty());
        /*
         after setAmount(0) ExtraDepot is empty.
         */
    }

    /**
     * this test verifies the correct increase of resource.
     */
    @Test
    void correctIncreaseAmountExtraDepotDepot(){

        ExtraDepot d = new ExtraDepot(Resource.COIN);

        assertTrue(d.isEmpty());
        assertTrue(d.increaseAmount());
        assertFalse(d.isEmpty());
        assertEquals(1, d.getAmount());
        assertTrue(d.increaseAmount());

        assertEquals(2, d.getAmount());
        assertFalse(d.increaseAmount());
        assertEquals(2, d.getAmount());
        /*
         increaseAmount() @return false because ExtraDepot was already full.
         */
    }

    /**
     * this test verifies the correct decrease of resource in case of ExtraDepot.
     */
    @Test
    void correctDecreaseAmountExtraDepot(){

        ExtraDepot d = new ExtraDepot(Resource.COIN);

        d.increaseAmount();
        d.increaseAmount();
        assertFalse(d.isEmpty());

        assertEquals(2, d.getAmount());
        assertEquals(0, d.decreaseAmount(1));
        /*
         @return 0 because getAmount() == @param amount.
         */

        assertEquals(1, d.getAmount());
        assertEquals(1, d.decreaseAmount(2));
        assertEquals(0, d.getAmount());
        /*
         @return 2 - 1 -> 1 because getAmount() < @param amount.
         */

        assertTrue(d.isEmpty());
        assertEquals(4, d.decreaseAmount(4));
        assertTrue(d.isEmpty());
        /*
         @return 4 - 0 -> 4 because ExtraDepot was empty.
         */
    }
}
