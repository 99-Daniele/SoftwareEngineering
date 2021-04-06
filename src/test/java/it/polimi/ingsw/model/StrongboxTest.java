package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import it.polimi.ingsw.exceptions.InsufficientResourceException;

public class StrongboxTest {

    Resource r1 = Resource.COIN;
    Resource r2 = Resource.SERVANT;
    Resource r3 = Resource.STONE;
    Resource r4 = Resource.SHIELD;

    /**
     * this test verifies the correct calculation of the num of resource.
     */
    @Test
    void calculateCostNumOfResource(){

        Strongbox s = new Strongbox();

        assertEquals(0, s.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));
        assertEquals(0, s.getNumOfResource(r3));
        assertEquals(0, s.getNumOfResource(r4));

        s.increaseResourceType(r1, 1);
        s.increaseResourceType(r2, 1);
        s.increaseResourceType(r3, 1);
        s.increaseResourceType(r4, 1);

        assertEquals(1, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));
        assertEquals(1, s.getNumOfResource(r3));
        assertEquals(1, s.getNumOfResource(r4));
    }

    /**
     * this test verifies the correct increase of resource
     */
    @Test
    void correctIncreaseResource(){

        Strongbox s = new Strongbox();

        s.increaseResourceType(r1, 2);
        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));

        s.increaseResourceType(r2, 2);
        assertEquals(2, s.getNumOfResource(r2));

        s.increaseResourceType(r2, 3);
        assertEquals(5, s.getNumOfResource(r2));
    }

    /**
     * this test verifies the correct decrease of resource
     */
    @Test
    void correctDecreaseResource() {

        Strongbox s = new Strongbox();

        s.increaseResourceType(r1, 2);
        s.increaseResourceType(r2, 5);
        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(5, s.getNumOfResource(r2));

        assertEquals(1, s.decreaseResourceType(r1, 3));
        assertEquals(0, s.decreaseResourceType(r2, 2));
        assertEquals(0, s.getNumOfResource(r1));
        assertEquals(3, s.getNumOfResource(r2));

        assertEquals(3, s.decreaseResourceType(r1, 3));
        assertEquals(0, s.decreaseResourceType(r2, 3));
        assertEquals(0, s.getNumOfResource(r1));
        assertEquals(0, s.getNumOfResource(r2));
    }

    /**
     * this test verifies the correct increase of Strongbox by increaseResource() method in Cost
     */
    @Test
    void increaseStrongboxByCost(){

        Strongbox s = new Strongbox();
        Cost c = new Cost();

        c.addResource(r1, 3);
        c.addResource(r2, 2);
        s.increaseResourceType(r1, 1);
        s.increaseResourceType(r2, 4);

        c.increaseResource(s);
        assertEquals(4, s.getNumOfResource(r1));
        assertEquals(6, s.getNumOfResource(r2));
    }

    /**
     * this test verifies the correct decrease of Strongbox by decreaseResource() method in Cost
     */
    @Test
    void decreaseResourceByCost() throws InsufficientResourceException{

        Strongbox s = new Strongbox();
        Warehouse w = new Warehouse();
        Cost c = new Cost();

        c.addResource(r1, 3);
        c.addResource(r2, 2);
        s.increaseResourceType(r1, 4);
        s.increaseResourceType(r2, 4);

        c.decreaseResource(w, s, 2);
        assertEquals(1, s.getNumOfResource(r1));
        assertEquals(2, s.getNumOfResource(r2));
    }

    /**
     * this test verifies the correct copying of Strongbox
     */
    @Test
    void correctWarehouseCopyWithExtraDepot(){

        Strongbox s1 = new Strongbox();
        Strongbox s2;

        s1.increaseResourceType(r1, 2);
        s1.increaseResourceType(r2, 1);
        s1.increaseResourceType(r3, 4);
        s1.increaseResourceType(r4, 2);
        assertEquals(2, s1.getNumOfResource(r1));
        assertEquals(1, s1.getNumOfResource(r2));
        assertEquals(4, s1.getNumOfResource(r3));
        assertEquals(2, s1.getNumOfResource(r4));

        s2 = s1.copyThisStrongbox();
        assertEquals(2, s1.getNumOfResource(r1));
        assertEquals(1, s1.getNumOfResource(r2));
        assertEquals(4, s1.getNumOfResource(r3));
        assertEquals(2, s1.getNumOfResource(r4));

        s1.increaseResourceType(r2, 1);
        s2.increaseResourceType(r2, 3);
        assertEquals(2, s1.getNumOfResource(r2));
        assertEquals(4, s2.getNumOfResource(r2));

        assertEquals(1, s1.decreaseResourceType(r2, 3));
        assertEquals(0, s2.decreaseResourceType(r2, 3));
        assertEquals(0, s1.getNumOfResource(r2));
        assertEquals(1, s2.getNumOfResource(r2));
    }

    /**
     * this test calculates the sum of all resource in Strongbox
     */
    @Test
    void calculateSumResources(){

        Strongbox s = new Strongbox();

        s.increaseResourceType(r1, 2);
        s.increaseResourceType(r2, 1);
        s.increaseResourceType(r3, 4);
        s.increaseResourceType(r4, 2);
        assertEquals(2, s.getNumOfResource(r1));
        assertEquals(1, s.getNumOfResource(r2));
        assertEquals(4, s.getNumOfResource(r3));
        assertEquals(2, s.getNumOfResource(r4));

        assertEquals(9, s.sumStrongboxResource());

        s.increaseResourceType(r2, 1);
        assertEquals(10, s.sumStrongboxResource());

        assertEquals(0, s.decreaseResourceType(r3, 3));
        assertEquals(7, s.sumStrongboxResource());
    }
}
