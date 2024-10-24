package it.polimi.ingsw.modelTests.playersTests;

import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.resourceContainers.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {

    final Resource r1 = Resource.COIN;
    final Resource r2 = Resource.SERVANT;
    final Resource r3 = Resource.STONE;
    final Resource r4 = Resource.SHIELD;

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

        assertEquals(w.increaseResource(r1), 0);
        assertEquals(w.increaseResource(r2), 1);
        assertEquals(w.increaseResource(r3), 2);
        assertEquals(-1, w.increaseResource(r4));
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

        assertEquals(w.increaseResource(r1), 3);
        assertEquals(w.increaseResource(r2), 4);
        assertEquals(w.increaseResource(r3), 0);
        assertEquals(w.increaseResource(r4), 1);

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

        assertEquals(w.increaseResource(r1), 0);
        assertEquals(-1, w.increaseResource(r1));
        assertEquals(1, w.getNumOfResource(r1));

        assertEquals(w.increaseResource(r2), 1);
        assertEquals(w.increaseResource(r2), 1);
        assertEquals(w.increaseResource(r2), -1);
        assertEquals(2, w.getNumOfResource(r2));

        assertEquals(w.increaseResource(r3), 2);
        assertEquals(w.increaseResource(r3), 2);
        assertEquals(w.increaseResource(r3), 2);
        assertEquals(-1, w.increaseResource(r3));
        assertEquals(3, w.getNumOfResource(r3));

        assertEquals(-1, w.increaseResource(r4));
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

        assertEquals(w.increaseResource(r1), 0);
        assertEquals(-1, w.increaseResource(r1));
        assertEquals(1, w.getNumOfResource(r1));

        w.addExtraDepot(r1);
        assertTrue(w.existExtraDepot());

        assertEquals(w.increaseResource(r1), 3);
        assertEquals(w.increaseResource(r1), 3);
        assertEquals(-1, w.increaseResource(r1));
        assertEquals(3, w.getNumOfResource(r1));
        /*
         1 r1 is first in WarehouseDepot, 2 r1 are in ExtraDepot
         */

        w.addExtraDepot(r2);
        assertEquals(w.increaseResource(r2), 4);
        assertEquals(w.increaseResource(r2), 4);
        assertEquals(2, w.getNumOfResource(r2));
        /*
         2 r2 are in ExtraDepot
         */

        assertEquals(w.increaseResource(r3), 1);
        assertEquals(w.increaseResource(r3), 1);
        assertEquals(-1, w.increaseResource(r3));
        assertEquals(2, w.getNumOfResource(r3));

        assertEquals(w.increaseResource(r4), 2);
        assertEquals(w.increaseResource(r4), 2);
        assertEquals(w.increaseResource(r4), 2);
        assertEquals(-1, w.increaseResource(r4));
        assertEquals(3, w.getNumOfResource(r4));

        assertEquals(-1, w.increaseResource(r2));
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

        assertEquals(w.increaseResource(r1), 0);
        assertEquals(w.increaseResource(r2), 1);
        assertEquals(w.increaseResource(r2), 1);
        assertEquals(w.increaseResource(r3), 2);
        assertEquals(w.increaseResource(r3), 2);
        assertEquals(w.increaseResource(r3), 2);
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
        assertEquals(w.increaseResource(r1), 3);
        assertEquals(w.increaseResource(r1), 3);
        assertEquals(w.increaseResource(r1), 0);
        assertEquals(w.increaseResource(r2), 4);
        assertEquals(w.increaseResource(r2), 4);
        assertEquals(w.increaseResource(r3), 1);
        assertEquals(w.increaseResource(r3), 1);
        assertEquals(w.increaseResource(r4), 2);
        assertEquals(w.increaseResource(r4), 2);
        assertEquals(w.increaseResource(r4), 2);

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
     * this test verifies the correct decrease of Warehouse by decreaseResource() method in Cost
     */
    @Test
    void decreaseResourceByCost() throws InsufficientResourceException {

        Strongbox s = new Strongbox();
        Warehouse w = new Warehouse();
        Cost c = new Cost();

        c.addResource(r1, 1);
        c.addResource(r2, 1);
        w.increaseResource(r1);
        w.increaseResource(r2);
        w.increaseResource(r2);

        c.decreaseResource(w, s, 1);
        assertEquals(0, w.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r2));
    }

    /**
     * this test tries to switch two WarehouseDepot where one of them has getAmount() > maxAMount of the other one
     */
    @Test
    void incorrectWarehouseDepotWithSmallerWarehouseDepotSwitch(){

        Warehouse w = new Warehouse();

        assertEquals(w.increaseResource(r1), 0);
        assertEquals(w.increaseResource(r2), 1);
        assertEquals(w.increaseResource(r2), 1);

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(0, 1));
        String expectedMessage = "You can't switch this depots";
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
        assertEquals(w.increaseResource(r1), 3);
        assertEquals(w.increaseResource(r2), 4);

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(3, 4));
        String expectedMessage = "You can't switch this depots";
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

        assertEquals(w.increaseResource(r1), 0);
        w.addExtraDepot(r1);
        assertEquals(w.increaseResource(r1), 3);
        /*
         1 r1 is in first WarehouseDepot, 1 r1 in ExtraDepot
         */

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(1, 3));
        String expectedMessage = "You can't switch this depots";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to switch a WarehouseDepot with a not existing ExtraDepot
     */
    @Test
    void incorrectNotExistingExtraDepot(){

        Warehouse w = new Warehouse();
        assertFalse(w.existExtraDepot());

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(3, 1));
        String expectedMessage = "You can't switch this depots";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(1, 4));
        expectedMessage = "You can't switch this depots";
        actualMessage = thrown.getMessage();
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
        assertEquals(w.increaseResource(r1), 3);
        assertEquals(w.increaseResource(r1), 3);

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(0, 3));
        String expectedMessage = "You can't switch this depots";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to switch ExtraDepot with WarehouseDepot which contains a different resource
     */
    @Test
    void incorrectExtraDepotWithDifferentResourceWarehouseDepotSwitch(){

        Warehouse w = new Warehouse();

        assertEquals(w.increaseResource(r1), 0);
        w.addExtraDepot(r2);
        assertEquals(w.increaseResource(r2), 3);

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(0, 3));
        String expectedMessage = "You can't switch this depots";
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

        assertEquals(w.increaseResource(r1), 0);
        w.addExtraDepot(r2);
        assertEquals(w.increaseResource(r2), 3);
        assertEquals(w.increaseResource(r2), 3);
        assertEquals(w.increaseResource(r3), 1);
        assertEquals(w.increaseResource(r3), 1);
        assertEquals(w.increaseResource(r2), 2);
        assertEquals(w.increaseResource(r2), 2);
        assertEquals(w.increaseResource(r2), 2);
        /*
         ExtraDepot has 2 r2, third WarehouseDepot has 3 r2
         */

        ImpossibleSwitchDepotException thrown =
                assertThrows(ImpossibleSwitchDepotException.class, () -> w.switchDepots(3, 2));
        String expectedMessage = "You can't switch this depots";
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

        assertEquals(w.increaseResource(r1), 0);
        assertEquals(-1, w.increaseResource(r1));
        assertEquals(1, w.getNumOfResource(r1));

        w.switchDepots(0, 1);
        w.switchDepots(1, 2);
        w.switchDepots(0, 2);
        assertEquals(1, w.getNumOfResource(r1));

        assertEquals(w.increaseResource(r2), 1);
        assertEquals(-1, w.increaseResource(r1));
        assertEquals(1, w.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r2));

        w.switchDepots(0, 1);
        assertEquals(w.increaseResource(r1), 1);
        assertEquals(-1, w.increaseResource(r2));
        assertEquals(2, w.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r2));

        assertEquals(w.increaseResource(r3), 2);
        w.switchDepots(0, 2);
        assertEquals(w.increaseResource(r2), 2);
        assertEquals(-1, w.increaseResource(r3));
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

        assertEquals(w.increaseResource(r1), 0);
        assertEquals(-1, w.increaseResource(r1));
        assertEquals(1, w.getNumOfResource(r1));
        w.addExtraDepot(r1);
        w.addExtraDepot(r2);
        assertTrue(w.existExtraDepot());

        assertEquals(w.increaseResource(r1), 3);
        w.switchDepots(0, 3);
        assertEquals(2, w.getNumOfResource(r1));
        assertEquals(w.increaseResource(r2), 4);
        w.switchDepots(1, 4);

        assertEquals(w.increaseResource(r1), 3);
        assertEquals(-1, w.increaseResource(r1));
        assertEquals(3, w.getNumOfResource(r1));
        w.switchDepots(0, 1);
        assertEquals(w.increaseResource(r1), 1);
        assertEquals(4, w.getNumOfResource(r1));
    }

    /**
     * this test simulate possible states of the Warehouse during a game
     */
    @Test
    void warehouseGameEvolution() throws ImpossibleSwitchDepotException{

        Warehouse w = new Warehouse();

        assertEquals(w.increaseResource(r1), 0);
        assertEquals(-1, w.increaseResource(r1));
        w.switchDepots(0, 1);
        assertEquals(1, w.getNumOfResource(r1));

        assertEquals(w.increaseResource(r1), 1);
        assertEquals(2, w.getNumOfResource(r1));
        assertEquals(w.increaseResource(r2), 0);
        assertEquals(-1, w.increaseResource(r2));
        assertEquals(1, w.getNumOfResource(r2));

        w.switchDepots(0, 2);
        assertEquals(w.increaseResource(r2), 2);
        assertEquals(2, w.getNumOfResource(r2));

        assertEquals(-1, w.increaseResource(r1));
        w.switchDepots(1, 2);
        assertEquals(w.increaseResource(r1), 2);

        assertEquals(w.increaseResource(r3), 0);
        assertEquals(-1, w.increaseResource(r3));
        assertEquals(-1, w.increaseResource(r4));

        assertEquals(3, w.decreaseResource(r2, 5));
        assertEquals(w.increaseResource(r4), 1);
        w.switchDepots(0, 1);
        assertEquals(w.increaseResource(r3), 1);

        assertEquals(-1, w.increaseResource(r4));
        w.addExtraDepot(r4);
        w.switchDepots(0, 3);

        assertEquals(w.increaseResource(r4), 3);
        assertEquals(w.increaseResource(r2), 0);
        assertEquals(0, w.decreaseResource(r1, 2));

        assertEquals(1, w.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r2));
        assertEquals(2, w.getNumOfResource(r3));
        assertEquals(2, w.getNumOfResource(r4));
    }

    /**
     * this test calculates the sum of all resource in Warehouse
     */
    @Test
    void calculateSumResources(){

        Warehouse w = new Warehouse();

        w.addExtraDepot(r1);
        w.addExtraDepot(r2);
        assertEquals(w.increaseResource(r1), 3);
        assertEquals(w.increaseResource(r1), 3);
        assertEquals(w.increaseResource(r3), 0);
        assertEquals(w.increaseResource(r1), 1);
        assertEquals(w.increaseResource(r1), 1);
        assertEquals(w.increaseResource(r2), 4);
        assertEquals(w.increaseResource(r4), 2);
        assertEquals(w.increaseResource(r4), 2);

        assertEquals(4, w.getNumOfResource(r1));
        assertEquals(1, w.getNumOfResource(r2));
        assertEquals(1, w.getNumOfResource(r3));
        assertEquals(2, w.getNumOfResource(r4));

        assertEquals(8, w.sumWarehouseResource());

        assertEquals(w.increaseResource(r4), 2);
        assertEquals(9, w.sumWarehouseResource());

        assertEquals(0, w.decreaseResource(r1, 3));
        assertEquals(6, w.sumWarehouseResource());
    }
}
