package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InsufficientResourceException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DevelopmentCardTest {
    //indicate the cost to buy a card
    Cost cost=new Cost();
    //indicate the cost to activate a card.
    Cost required=new Cost();
    //indicate the resources you receive when activating a card
    Cost given=new Cost();
    Warehouse warehouse=new Warehouse();
    Strongbox strongbox=new Strongbox();
    DevelopmentCard developmentCard=new DevelopmentCard(Color.GREEN, 2, cost, 3, required, given, 3);

    /**
     * control if every get method in the class return the correct value.
     */
    @Test
    void testGetMethod(){
        setEmpty();
        assertEquals(developmentCard.getColor(),Color.GREEN);
        assertNotEquals(developmentCard.getColor(),Color.BLUE);
        assertEquals(2,developmentCard.getLevel());
        assertEquals(3,developmentCard.getVictoryPoints());
    }

    /**
     * set the quantity of every resource to 0 in warehouse,strongbox,cost,required,given.
     */
    @Test
    void setEmpty(){
        for (Resource resource:Resource.values())
        {
            strongbox.decreaseResourceType(resource,strongbox.getNumOfResource(resource));
            warehouse.decreaseResource(resource,warehouse.getNumOfResource(resource));
            assertEquals(0,warehouse.getNumOfResource(resource));
            assertEquals(0,strongbox.getNumOfResource(resource));
            for (int i=0;i<cost.getNumOfResource(resource);i++)
                cost.discount(resource);
            for (int j=0;j<required.getNumOfResource(resource);j++)
                required.discount(resource);
            for (int k=0;k<given.getNumOfResource(resource);k++)
                given.discount(resource);
        }
    }

    /**
     * test if the resources in the attribute cost are decreased or decreased correctly(by 1 unit).
     */
    @Test
    void testRecountDiscount(){
        setEmpty();
        developmentCard.discount(Resource.SERVANT);
        assertEquals(0,cost.getNumOfResource(Resource.SERVANT));
        developmentCard.recount(Resource.SERVANT);
        assertEquals(1,cost.getNumOfResource(Resource.SERVANT));
        developmentCard.discount(Resource.SHIELD);
        assertEquals(0,cost.getNumOfResource(Resource.SHIELD));
        developmentCard.recount((Resource.SHIELD));
        cost.addResource(Resource.SHIELD,2);
        assertEquals(3,cost.getNumOfResource(Resource.SHIELD));
        cost.addResource(Resource.SERVANT,1);
        assertEquals(2,cost.getNumOfResource(Resource.SERVANT));
    }


    /**
     * case: choice 2, not enough resource in strongbox.
     * @throws InsufficientResourceException thrown when there aren't enough resources.
     */
    @Test
    void buyCardTest() throws InsufficientResourceException {
        setEmpty();
        warehouse.increaseResource(Resource.SERVANT);
        warehouse.increaseResource(Resource.COIN);
        warehouse.increaseResource(Resource.COIN);
        warehouse.increaseResource(Resource.SHIELD);
        warehouse.increaseResource(Resource.SHIELD);
        warehouse.increaseResource(Resource.SHIELD);
        strongbox.increaseResourceType(Resource.COIN,3);
        strongbox.increaseResourceType(Resource.STONE,1);
        cost.addResource(Resource.COIN,5);
        cost.addResource(Resource.SERVANT,1);
        cost.addResource(Resource.SHIELD,3);

        developmentCard.buyCard(warehouse,strongbox,2);
        assertEquals(0,warehouse.getNumOfResource(Resource.COIN));
        assertEquals(0,strongbox.getNumOfResource(Resource.COIN));
        assertEquals(0,warehouse.getNumOfResource(Resource.SHIELD));
        assertEquals(0,warehouse.getNumOfResource(Resource.SERVANT));
        assertEquals(1,strongbox.getNumOfResource(Resource.STONE));
    }

    /**
     * this test control if the resources has been added correctly in strongbox, when a card is activated successfully.
     * @throws InsufficientResourceException thrown when there aren't enough resources.
     */
    @Test
    void productionChoice2() throws InsufficientResourceException {
        setEmpty();
        warehouse.increaseResource(Resource.STONE);
        strongbox.increaseResourceType(Resource.COIN,5);
        strongbox.increaseResourceType(Resource.SHIELD,5);
        strongbox.increaseResourceType(Resource.SERVANT,2);
        required.addResource(Resource.COIN,5);
        required.addResource(Resource.SERVANT,1);
        required.addResource(Resource.SHIELD,4);
        given.addResource(Resource.COIN,1);
        given.addResource(Resource.STONE,2);

        developmentCard.activateProduction(warehouse,strongbox,2);
        assertEquals(1,warehouse.getNumOfResource(Resource.STONE));
        assertEquals(2, strongbox.getNumOfResource(Resource.STONE));
        assertEquals(1,strongbox.getNumOfResource(Resource.COIN));
        assertEquals(1,strongbox.getNumOfResource(Resource.SHIELD));
        assertEquals(1,strongbox.getNumOfResource(Resource.SERVANT));
    }

    /**
     * this test control if the resources remain unchanged after i failed to activate a card.
     */
    @Test
    void productionException() {
        setEmpty();
        warehouse.increaseResource(Resource.STONE);
        strongbox.increaseResourceType(Resource.COIN,1);
        strongbox.increaseResourceType(Resource.SHIELD,1);
        strongbox.increaseResourceType(Resource.SERVANT,2);
        required.addResource(Resource.COIN,3);
        required.addResource(Resource.SERVANT,4);
        required.addResource(Resource.SHIELD,4);
        given.addResource(Resource.COIN,1);
        given.addResource(Resource.STONE,2);

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> developmentCard.activateProduction(warehouse,strongbox,1));
        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));

        assertEquals(1,warehouse.getNumOfResource(Resource.STONE));
        assertEquals(0,strongbox.getNumOfResource(Resource.STONE));
        assertEquals(1,strongbox.getNumOfResource(Resource.COIN));
        assertEquals(1,strongbox.getNumOfResource(Resource.SHIELD));
        assertEquals(2,strongbox.getNumOfResource(Resource.SERVANT));
    }
}



