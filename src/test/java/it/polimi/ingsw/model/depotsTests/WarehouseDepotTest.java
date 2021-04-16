package it.polimi.ingsw.model.depotsTests;

import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.player.depots.WarehouseDepot;
import org.junit.jupiter.api.Test;

public class WarehouseDepotTest {

    /**
     * this test verifies the correct construction of WarehouseDepot.
     */
    @Test
    void creationWarehouseDepot(){

        WarehouseDepot d = new WarehouseDepot(1);

        assertTrue(d.isEmpty());
        assertEquals(1, d.getMaxAmount());
    }

    /**
     * this test verifies the correct set of resource.
     */
    @Test
    void correctSetAmountWarehouseDepot(){

        WarehouseDepot d = new WarehouseDepot(1);

        assertTrue(d.isEmpty());
        d.setAmount(1);
        assertEquals(1, d.getAmount());
        assertFalse(d.isEmpty());

        d.setAmount(0);
        assertEquals(0, d.getAmount());
        assertTrue(d.isEmpty());
        /*
         after setAmount(0) WarehouseDepot is empty.
         */
    }

    /**
     * this test verifies the correct increase of resource.
     */
    @Test
    void correctIncreaseAmountWarehouseDepot(){

        WarehouseDepot d = new WarehouseDepot(1);

        assertTrue(d.isEmpty());
        assertTrue(d.increaseAmount());
        assertFalse(d.isEmpty());

        assertEquals(d.getMaxAmount(), d.getAmount());
        assertFalse(d.increaseAmount());
        assertEquals(d.getMaxAmount(), d.getAmount());
        /*
         increaseAmount() @return false because the WarehouseDepot was already full.
         */
    }

    /**
     * this test verifies the correct decrease of resource.
     */
    @Test
    void correctDecreaseAmountWarehouseDepot(){

        WarehouseDepot d = new WarehouseDepot(3);

        d.increaseAmount();
        d.increaseAmount();

        assertEquals(2, d.getAmount());
        assertEquals(0, d.decreaseAmount(1));
        /*
         @return 0 because getAmount() > @param amount.
         */

        assertEquals(1, d.getAmount());
        assertEquals(3, d.decreaseAmount(4));
        assertEquals(0, d.getAmount());
        /*
         @return 4 - 1 -> 3 because getAmount() < @param amount.
         */

        assertTrue(d.isEmpty());
        assertEquals(1, d.decreaseAmount(1));
        assertTrue(d.isEmpty());
        /*
         @return 1 - 0 -> 1 because WarehouseDepot was empty.
         */
    }
}
