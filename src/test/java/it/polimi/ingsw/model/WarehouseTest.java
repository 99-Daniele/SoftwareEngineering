package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.exceptions.ImpossibleSwitchDepotException;

public class WarehouseTest {

    Resource r1 = Resource.COIN;
    Resource r2 = Resource.SERVANT;
    Resource r3 = Resource.STONE;
    Resource r4 = Resource.SHIELD;

    /**
     * this test verifies the correct construction of Warehouse
     */
    @Test
    void creationWarehouse(){

        Warehouse w = new Warehouse();

        assertFalse(w.existExtraDepot());
        assertEquals(0, w.getNumOfResource(r1));
        assertEquals(0, w.getNumOfResource(r2));
        assertEquals(0, w.getNumOfResource(r3));
        assertEquals(0, w.getNumOfResource(r4));
    }

    /**
     * this test verifies the correct addition of a new ExtraDepot
     */
    @Test
    void addExtraDepots(){

        Warehouse w = new Warehouse();

        assertFalse(w.existExtraDepot());

        w.addExtraDepot(r1);
        assertTrue(w.existExtraDepot());
    }

    /**
     * this test verifies the correct calculation of the num of resource if not exist ExtraDepot
     */
    @Test
    void warehouseNumOfResourceNoExtraDepots(){

        Warehouse w = new Warehouse();

        assertEquals(0, w.getNumOfResource(r1));
        assertEquals(0, w.getNumOfResource(r2));
        assertEquals(0, w.getNumOfResource(r3));
        assertEquals(0, w.getNumOfResource(r4));

        assertTrue(w.increaseResource(r1));
        assertTrue(w.increaseResource(r2));
        assertTrue(w.increaseResource(r3));
        assertFalse(w.increaseResource(r4));
        assertEquals(1, w.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r2));
        assertEquals(1, w.getNumOfResource(r3));
        assertEquals(0, w.getNumOfResource(r4));
        /*
         getNumOfResource(r4) @return 0 because there are already 3 different type of resource in the Warehouse
         */

    }

    /**
     * this test verifies the correct calculation of the num of resource if exist ExtraDepot
     */
    @Test
    void warehouseNumOfResourceWithExtraDepots(){

        Warehouse w = new Warehouse();

        w.addExtraDepot(r1);
        w.addExtraDepot(r2);
        assertTrue(w.existExtraDepot());

        assertEquals(0, w.getNumOfResource(r1));
        assertEquals(0, w.getNumOfResource(r2));
        assertEquals(0, w.getNumOfResource(r3));
        assertEquals(0, w.getNumOfResource(r4));

        assertTrue(w.increaseResource(r1));
        assertTrue(w.increaseResource(r2));
        assertTrue(w.increaseResource(r3));
        assertTrue(w.increaseResource(r4));

        assertEquals(1, w.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r2));
        assertEquals(1, w.getNumOfResource(r3));
        assertEquals(1, w.getNumOfResource(r4));
        /*
         r1 is in first ExtraDepot, r2 in second ExtraDepot, r3 in first WarehouseDepot, r4 in second WarehouseDepot
         */
    }

    /**
     * this test verifies the correct increase of Warehouse if not exist ExtraDepot
     */
    @Test
    void correctIncreaseNoExtraDepots(){

        Warehouse w = new Warehouse();

        assertFalse(w.existExtraDepot());

        assertTrue(w.increaseResource(r1));
        assertFalse(w.increaseResource(r1));
        assertEquals(1, w.getNumOfResource(r1));

        assertTrue(w.increaseResource(r2));
        assertTrue(w.increaseResource(r2));
        assertFalse(w.increaseResource(r2));
        assertEquals(2, w.getNumOfResource(r2));

        assertTrue(w.increaseResource(r3));
        assertTrue(w.increaseResource(r3));
        assertTrue(w.increaseResource(r3));
        assertFalse(w.increaseResource(r3));
        assertEquals(3, w.getNumOfResource(r3));

        assertFalse(w.increaseResource(r4));
        /*
         increaseResource(r4) @return false because there are already 3 different type of resource in the Warehouse
         */
    }

    /**
     * this test verifies the correct increase of Warehouse if exist ExtraDepot
     */
    @Test
    void correctIncreaseWithExtraDepots(){

        Warehouse w = new Warehouse();

        assertFalse(w.existExtraDepot());

        assertTrue(w.increaseResource(r1));
        assertFalse(w.increaseResource(r1));
        assertEquals(1, w.getNumOfResource(r1));

        w.addExtraDepot(r1);
        assertTrue(w.existExtraDepot());

        assertTrue(w.increaseResource(r1));
        assertTrue(w.increaseResource(r1));
        assertFalse(w.increaseResource(r1));
        assertEquals(3, w.getNumOfResource(r1));
        /*
         1 r1 is first in WarehouseDepot, 2 r1 are in ExtraDepot
         */

        w.addExtraDepot(r2);
        assertTrue(w.increaseResource(r2));
        assertTrue(w.increaseResource(r2));
        assertEquals(2, w.getNumOfResource(r2));
        /*
         2 r2 are in ExtraDepot
         */

        assertTrue(w.increaseResource(r3));
        assertTrue(w.increaseResource(r3));
        assertFalse(w.increaseResource(r3));
        assertEquals(2, w.getNumOfResource(r3));

        assertTrue(w.increaseResource(r4));
        assertTrue(w.increaseResource(r4));
        assertTrue(w.increaseResource(r4));
        assertFalse(w.increaseResource(r4));
        assertEquals(3, w.getNumOfResource(r4));

        assertFalse(w.increaseResource(r2));
        /*
         increaseResource(r2) @return false because there are already 3 different type of resource  in the Warehouse
         */
    }

    /**
     * this test verifies the correct decrease of Warehouse if not exist ExtraDepot
     */
    @Test
    void correctDecreaseNoExtraDepots(){

        Warehouse w = new Warehouse();

        assertFalse(w.existExtraDepot());

        assertEquals(0, w.decreaseResource(r1, 0));

        assertTrue(w.increaseResource(r1));
        assertTrue(w.increaseResource(r2));
        assertTrue(w.increaseResource(r2));
        assertTrue(w.increaseResource(r3));
        assertTrue(w.increaseResource(r3));
        assertTrue(w.increaseResource(r3));
        assertEquals(1, w.getNumOfResource(r1));
        assertEquals(2, w.getNumOfResource(r2));
        assertEquals(3, w.getNumOfResource(r3));
        assertEquals(0, w.getNumOfResource(r4));

        assertEquals(0, w.decreaseResource(r1, 1));
        assertEquals(0, w.getNumOfResource(r1));
        /*
         @return 0 because getNumOfResource(r1) == @param amount
         */

        assertEquals(0, w.decreaseResource(r2, 1));
        assertEquals(1, w.getNumOfResource(r2));
        /*
         @return 0 because getNumOfResource(r2) > @param amount
         */

        assertEquals(1, w.decreaseResource(r3, 4));
        assertEquals(0, w.getNumOfResource(r3));
        /*
         @return 4 - 3 -> 1 because getNumOfResource(r3) < @param amount
         */

        assertEquals(5, w.decreaseResource(r4, 5));
        assertEquals(0, w.getNumOfResource(r4));
        /*
         @return @param amount because there is no r4 in warehouseDepots
         */
    }

    /**
     * this test verifies the correct decrease of Warehouse if exist ExtraDepot
     */
    @Test
    void correctDecreaseWithExtraDepots(){

        Warehouse w = new Warehouse();

        assertFalse(w.existExtraDepot());

        assertEquals(0, w.decreaseResource(r1, 0));

        w.addExtraDepot(r1);
        w.addExtraDepot(r2);
        assertTrue(w.increaseResource(r1));
        assertTrue(w.increaseResource(r1));
        assertTrue(w.increaseResource(r1));
        assertTrue(w.increaseResource(r2));
        assertTrue(w.increaseResource(r2));
        assertTrue(w.increaseResource(r3));
        assertTrue(w.increaseResource(r3));
        assertTrue(w.increaseResource(r4));
        assertTrue(w.increaseResource(r4));
        assertTrue(w.increaseResource(r4));

        assertEquals(3, w.getNumOfResource(r1));
        /*
         1 r1 is in first WarehouseDepot, 2 r1 are in ExtraDepot
         */

        assertEquals(2, w.getNumOfResource(r2));
        /*
         2 r2 in ExtraDepot
         */

        assertEquals(2, w.getNumOfResource(r3));
        assertEquals(3, w.getNumOfResource(r4));
        /*
         2 r3 in second WarehouseDepot, 3 r4 in third WarehouseDepot
         */

        assertEquals(0, w.decreaseResource(r1, 2));
        assertEquals(1, w.getNumOfResource(r1));
        /*
         @return 0 because getNumOfResource(r1) > @param amount
         */

        assertEquals(0, w.decreaseResource(r2, 2));
        assertEquals(0, w.getNumOfResource(r2));
        /*
         @return 0 because getNumOfResource(r2) == @param amount
         */

        assertEquals(1, w.decreaseResource(r3, 3));
        assertEquals(0, w.getNumOfResource(r3));
        /*
         @return 3 - 2 -> 1 because getNumOfResource(r3) < @param amount
         */

        assertEquals(2, w.decreaseResource(r4, 5));
        assertEquals(0, w.getNumOfResource(r4));
        /*
         @return 5 - 3 -> 2 because getNumOfResource(r4) < @param amount
         */
    }

    /**
     * this test tries to switch two WarehouseDepot where one of them has getAmount() > maxAMount of the other one
     */
    @Test
    void incorrectWarehouseDepotWithSmallerWarehouseDepotSwitch(){

        Warehouse w = new Warehouse();

        assertTrue(w.increaseResource(r1));
        assertTrue(w.increaseResource(r2));
        assertTrue(w.increaseResource(r2));

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(0, 1));
        String expectedMessage = "Non è possibile scambiare questi depositi";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to switch two ExtraDepot
     */
    @Test
    void incorrectExtraDepotsSwitch(){

        Warehouse w = new Warehouse();

        w.addExtraDepot(r1);
        w.addExtraDepot(r2);
        assertTrue(w.increaseResource(r1));
        assertTrue(w.increaseResource(r2));

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(3, 4));
        String expectedMessage = "Non è possibile scambiare questi depositi";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to switch ExtraDepot with an empty WarehouseDepot, where already exist a not empty WarehouseDepot
     * in warehouseDepots which contains ExtraDepot resource
     */
    @Test
    void incorrectExtraDepotWithEmptyWarehouseDepotSwitch(){

        Warehouse w = new Warehouse();

        assertTrue(w.increaseResource(r1));
        w.addExtraDepot(r1);
        assertTrue(w.increaseResource(r1));
        /*
         1 r1 is in first WarehouseDepot, 1 r1 in ExtraDepot
         */

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(1, 3));
        String expectedMessage = "Non è possibile scambiare questi depositi";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to switch ExtraDepot with an empty WarehouseDepot, where ExtraDepot has getAmount() > maxAmount
     * of WarehouseDepot
     */
    @Test
    void incorrectExtraDepotWithEmptySmallerWarehouseDepotSwitch(){

        Warehouse w = new Warehouse();

        w.addExtraDepot(r1);
        assertTrue(w.increaseResource(r1));
        assertTrue(w.increaseResource(r1));

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(0, 3));
        String expectedMessage = "Non è possibile scambiare questi depositi";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to switch ExtraDepot with WarehouseDepot which contains a different resource
     */
    @Test
    void incorrectExtraDepotWithDifferentResourceWarehouseDepotSwitch(){

        Warehouse w = new Warehouse();

        assertTrue(w.increaseResource(r1));
        w.addExtraDepot(r2);
        assertTrue(w.increaseResource(r2));

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(0, 3));
        String expectedMessage = "Non è possibile scambiare questi depositi";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to switch ExtraDepot with  WarehouseDepot, where one of them has getAmount() > maxAMount
     * of the other one
     */
    @Test
    void incorrectDepotWithSmallerDepotSwitch(){

        Warehouse w = new Warehouse();

        assertTrue(w.increaseResource(r1));
        w.addExtraDepot(r2);
        assertTrue(w.increaseResource(r2));
        assertTrue(w.increaseResource(r2));
        assertTrue(w.increaseResource(r3));
        assertTrue(w.increaseResource(r3));
        assertTrue(w.increaseResource(r4));
        assertTrue(w.increaseResource(r4));
        assertTrue(w.increaseResource(r4));
        /*
         ExtraDepot has 2 r2, third WarehouseDepot has 3 r4
         */

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(2, 3));
        String expectedMessage = "Non è possibile scambiare questi depositi";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }


    /**
     * this test verifies the correct switch of depots if not exist ExtraDepot
     */
    @Test
    void correctSwitchNoExtraDepots() throws ImpossibleSwitchDepotException{

        Warehouse w = new Warehouse();

        assertFalse(w.existExtraDepot());

        assertTrue(w.increaseResource(r1));
        assertFalse(w.increaseResource(r1));
        assertEquals(1, w.getNumOfResource(r1));

        w.switchDepots(0, 1);
        w.switchDepots(1, 2);
        w.switchDepots(0, 2);
        assertEquals(1, w.getNumOfResource(r1));

        assertTrue(w.increaseResource(r2));
        assertFalse(w.increaseResource(r1));
        assertEquals(1, w.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r2));

        w.switchDepots(0, 1);
        assertTrue(w.increaseResource(r1));
        assertFalse(w.increaseResource(r2));
        assertEquals(2, w.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r2));

        assertTrue(w.increaseResource(r3));
        w.switchDepots(0, 2);
        assertTrue(w.increaseResource(r2));
        assertFalse(w.increaseResource(r3));
        assertEquals(2, w.getNumOfResource(r1));
        assertEquals(2, w.getNumOfResource(r2));
        assertEquals(1, w.getNumOfResource(r3));
    }

    /**
     * this test verifies the correct switch of depots if exist ExtraDepot
     */
    @Test
    void correctSwitchWithExtraDepots() throws ImpossibleSwitchDepotException{

        Warehouse w = new Warehouse();

        assertFalse(w.existExtraDepot());

        assertTrue(w.increaseResource(r1));
        assertFalse(w.increaseResource(r1));
        assertEquals(1, w.getNumOfResource(r1));
        w.addExtraDepot(r1);
        w.addExtraDepot(r2);
        assertTrue(w.existExtraDepot());

        assertTrue(w.increaseResource(r1));
        w.switchDepots(0, 3);
        assertEquals(2, w.getNumOfResource(r1));
        assertTrue(w.increaseResource(r2));
        w.switchDepots(1, 4);

        assertTrue(w.increaseResource(r1));
        assertFalse(w.increaseResource(r1));
        assertEquals(3, w.getNumOfResource(r1));
        w.switchDepots(0, 1);
        assertTrue(w.increaseResource(r1));
        assertEquals(4, w.getNumOfResource(r1));
    }

    /**
     * this test simulate possible states of the Warehouse during a game
     */
    @Test
    void warehouseGameEvolution() throws ImpossibleSwitchDepotException{

        Warehouse w = new Warehouse();

        assertTrue(w.increaseResource(r1));
        assertFalse(w.increaseResource(r1));
        w.switchDepots(0, 1);
        assertEquals(1, w.getNumOfResource(r1));

        assertTrue(w.increaseResource(r1));
        assertEquals(2, w.getNumOfResource(r1));
        assertTrue(w.increaseResource(r2));
        assertFalse(w.increaseResource(r2));
        assertEquals(1, w.getNumOfResource(r2));

        w.switchDepots(0, 2);
        assertTrue(w.increaseResource(r2));
        assertEquals(2, w.getNumOfResource(r2));

        assertFalse(w.increaseResource(r1));
        w.switchDepots(1, 2);
        assertTrue(w.increaseResource(r1));

        assertTrue(w.increaseResource(r3));
        assertFalse(w.increaseResource(r3));
        assertFalse(w.increaseResource(r4));

        assertEquals(3, w.decreaseResource(r2, 5));
        assertTrue(w.increaseResource(r4));
        w.switchDepots(0, 1);
        assertTrue(w.increaseResource(r3));

        assertFalse(w.increaseResource(r4));
        w.addExtraDepot(r4);
        w.switchDepots(0, 3);

        assertTrue(w.increaseResource(r4));
        assertTrue(w.increaseResource(r2));
        assertEquals(0, w.decreaseResource(r1, 2));

        assertEquals(1, w.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r2));
        assertEquals(2, w.getNumOfResource(r3));
        assertEquals(2, w.getNumOfResource(r4));
    }
}