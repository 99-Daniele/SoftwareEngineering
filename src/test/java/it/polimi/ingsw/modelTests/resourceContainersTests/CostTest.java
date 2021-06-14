package it.polimi.ingsw.modelTests.resourceContainersTests;

import it.polimi.ingsw.exceptions.InsufficientResourceException;


import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class CostTest {

    final Resource r1 = Resource.COIN;
    final Resource r2 = Resource.SERVANT;

    /**
     * this test verifies the correct calculation of the num of resource.
     */
    @Test
    void calculateCostNumOfResource(){

        Cost c = new Cost();

        c.addResource(r1, 2);
        assertEquals(2, c.getNumOfResource(r1));
        assertEquals(0, c.getNumOfResource(r2));

        c.addResource(r2, 2);
        assertEquals(2, c.getNumOfResource(r2));

        c.addResource(r2, 3);
        assertEquals(5, c.getNumOfResource(r2));

        assertTrue(c.discount(r1));
        assertEquals(1, c.getNumOfResource(r1));

        c.recount(r1);
        assertEquals(2, c.getNumOfResource(r1));
    }

    /**
     * this test verify if different Warehouse+Strongbox has enough resource.
     */
    @Test
    void verifyEnoughAmountOfResource(){

        Cost c = new Cost();
        Warehouse w1 = new Warehouse();
        Warehouse w2 = new Warehouse();
        Warehouse w3 = new Warehouse();
        Warehouse w4 = new Warehouse();
        Strongbox s1 = new Strongbox();
        Strongbox s2 = new Strongbox();
        Strongbox s3 = new Strongbox();
        Strongbox s4 = new Strongbox();

        c.addResource(r1, 1);
        c.addResource(r2, 2);

        w1.increaseResource(r1);
        w1.increaseResource(r2);
        w1.increaseResource(r2);
        s2.increaseResourceType(r1, 3);
        assertTrue(c.enoughResource(w1, s1));

        w2.increaseResource(r1);
        w2.increaseResource(r2);
        s2.increaseResourceType(r1, 3);
        s2.increaseResourceType(r2, 2);
        assertTrue(c.enoughResource(w2, s2));

        s3.increaseResourceType(r1, 3);
        s3.increaseResourceType(r2, 3);
        assertTrue(c.enoughResource(w3, s3));

        w4.increaseResource(r1);
        s4.increaseResourceType(r1, 2);
        s4.increaseResourceType(r2, 1);
        assertFalse(c.enoughResource(w4, s4));
    }

    /**
     * this test verify the correct increase of resource in Strongbox.
     */
    @Test
    void correctIncrease(){

        Cost c = new Cost();
        Strongbox s1 = new Strongbox();
        Strongbox s2 = new Strongbox();

        c.addResource(r1, 2);
        s1.increaseResourceType(r1, 2);
        s2.increaseResourceType(r2, 3);

        c.increaseResource(s1);
        assertEquals(4, s1.getNumOfResource(r1));

        c.increaseResource(s2);
        assertEquals(2, s2.getNumOfResource(r1));
        assertEquals(3, s2.getNumOfResource(r2));
    }

    /**
     * this test tries to decrease resource from Warehouse + Strongbox which don't have enough resource
     */
    @Test
    void InsufficientWarehouseStrongboxDecrease(){

        Cost c = new Cost();
        Warehouse w = new Warehouse();
        Strongbox s = new Strongbox();

        c.addResource(r1, 1);
        c.addResource(r2, 2);

        assertEquals(0, w.increaseResource(r1));
        s.increaseResourceType(r1, 2);
        s.increaseResourceType(r2, 1);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> c.decreaseResource(w, s, 1));
        /*
         since there aren't enough resource, doesn't count if @param choice is 1 or 2.
         */

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct decrease of resource if player chooses priority to Warehouse (@param choice == 1).
     * have been tested Warehouse which already contains enough resource, or not contains enough resource, or is empty
     */
    @Test
    void correctWarehouseDecrease() throws InsufficientResourceException{

        Cost c = new Cost();
        Warehouse w1 = new Warehouse();
        Warehouse w2 = new Warehouse();
        Warehouse w3 = new Warehouse();
        Strongbox s1 = new Strongbox();
        Strongbox s2 = new Strongbox();
        Strongbox s3 = new Strongbox();

        c.addResource(r1, 1);
        c.addResource(r2, 2);
        assertEquals(1, c.getNumOfResource(r1));
        assertEquals(2, c.getNumOfResource(r2));

        w1.increaseResource(r1);
        w1.increaseResource(r2);
        w1.increaseResource(r2);
        s1.increaseResourceType(r1, 3);
        s1.increaseResourceType(r2, 3);

        c.decreaseResource(w1, s1, 0);
        assertEquals(0, w1.getNumOfResource(r1));
        assertEquals(0, w1.getNumOfResource(r2));
        assertEquals(3, s1.getNumOfResource(r1));
        assertEquals(3, s1.getNumOfResource(r2));

        w2.increaseResource(r2);
        w2.increaseResource(r1);
        w2.increaseResource(r1);
        s2.increaseResourceType(r1, 3);
        s2.increaseResourceType(r2, 3);

        c.decreaseResource(w2, s2, 0);
        assertEquals(1, w2.getNumOfResource(r1));
        assertEquals(0, w2.getNumOfResource(r2));
        assertEquals(3, s2.getNumOfResource(r1));
        assertEquals(2, s2.getNumOfResource(r2));

        s3.increaseResourceType(r1, 3);
        s3.increaseResourceType(r2, 3);

        c.decreaseResource(w3, s3, 0);
        assertEquals(0, w3.getNumOfResource(r1));
        assertEquals(2, s3.getNumOfResource(r1));
        assertEquals(0, w3.getNumOfResource(r2));
        assertEquals(1, s3.getNumOfResource(r2));
    }

    /**
     * this test verifies the correct decrease of resource if player chooses priority to Strongbox (@param choice == 2)
     * have been tested Strongbox which already contains enough resource, or not contains enough resource, or is empty
     */
    @Test
    void correctStrongboxDecrease() throws InsufficientResourceException{

        Cost c = new Cost();
        Strongbox s1 = new Strongbox();
        Strongbox s2 = new Strongbox();
        Strongbox s3 = new Strongbox();
        Warehouse w1 = new Warehouse();
        Warehouse w2 = new Warehouse();
        Warehouse w3 = new Warehouse();

        c.addResource(r1, 1);
        c.addResource(r2, 2);
        assertEquals(1, c.getNumOfResource(r1));
        assertEquals(2, c.getNumOfResource(r2));

        s1.increaseResourceType(r1, 2);
        s1.increaseResourceType(r2, 2);
        w1.increaseResource(r1);
        w1.increaseResource(r2);
        w1.increaseResource(r2);

        c.decreaseResource(w1, s1, 2);
        assertEquals(1, s1.getNumOfResource(r1));
        assertEquals(0, s1.getNumOfResource(r2));
        assertEquals(1, w1.getNumOfResource(r1));
        assertEquals(2, w1.getNumOfResource(r2));

        s2.increaseResourceType(r1, 1);
        s2.increaseResourceType(r2, 1);
        w2.increaseResource(r1);
        w2.increaseResource(r2);
        w2.increaseResource(r2);

        c.decreaseResource(w2, s2, 2);
        assertEquals(0, s2.getNumOfResource(r1));
        assertEquals(0, s2.getNumOfResource(r2));
        assertEquals(1, w2.getNumOfResource(r1));
        assertEquals(1,w2.getNumOfResource(r2));

        w3.increaseResource(r1);
        w3.increaseResource(r2);
        w3.increaseResource(r2);

        c.decreaseResource(w3, s3, 2);
        assertEquals(0, s3.getNumOfResource(r1));
        assertEquals(0, s3.getNumOfResource(r2));
        assertEquals(0, w3.getNumOfResource(r1));
        assertEquals(0, w3.getNumOfResource(r2));
    }

    /**
     * this test verifies the correct addition of resource
     */
    @Test
    void addResource(){

        Cost c = new Cost();
        assertEquals(0, c.getNumOfResource(r1));
        assertEquals(0, c.getNumOfResource(r2));

        c.addResource(r1, 0);
        assertEquals(0, c.getNumOfResource(r1));
        assertEquals(0, c.getNumOfResource(r2));

        c.addResource(r1, 1);
        assertEquals(1, c.getNumOfResource(r1));
        assertEquals(0, c.getNumOfResource(r2));
        /*
         * addResource(r1, 1) create a new ResourceContainer(r1, 1)
         */

        c.addResource(r1, 3);
        assertEquals(4, c.getNumOfResource(r1));
        assertEquals(0, c.getNumOfResource(r2));

        c.addResource(r2, 3);
        assertEquals(4, c.getNumOfResource(r1));
        assertEquals(3, c.getNumOfResource(r2));
    }

    /**
     * this test verifies the correct discount of Cost by one
     */
    @Test
    void correctDiscount(){

        Cost c = new Cost();

        c.addResource(r1, 1);
        assertEquals(1, c.getNumOfResource(r1));
        assertEquals(0, c.getNumOfResource(r2));

        assertTrue(c.discount(r1));
        assertFalse(c.discount(r2));
        assertEquals(0, c.getNumOfResource(r1));
        assertEquals(0, c.getNumOfResource(r2));
    }

    /**
     * this test verifies the correct recount of Cost by one
     */
    @Test
    void correctRecount(){

        Cost c = new Cost();

        assertEquals(0, c.getNumOfResource(r1));
        c.recount(r1);
        assertEquals(1, c.getNumOfResource(r1));
    }
}

