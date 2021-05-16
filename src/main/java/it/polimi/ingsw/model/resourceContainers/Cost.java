package it.polimi.ingsw.model.resourceContainers;

import it.polimi.ingsw.exceptions.InsufficientResourceException;

import it.polimi.ingsw.model.player.*;

import java.util.ArrayList;

/**
 * Cost is the class that represent the generic cost. This class always receives player's resources and increase or decrease
 * them by required resources.
 */
public class Cost {

    private final ArrayList<ResourceContainer> resourceContainers = new ArrayList<>();

    /**
     * @param resource stands for the type of resource to count.
     * @return the count @param resource in Cost. If @param resource is not in Cost @return 0.
     */
    public int getNumOfResource(Resource resource) {
        for(ResourceContainer resourceContainer: resourceContainers){
            if(resourceContainer.getResource() == resource)
                return resourceContainer.getAmount();
        }
        return 0;
    }

    /**
     * @param warehouse is player's Warehouse.
     * @param strongbox is player's Strongbox.
     * @return true if player has enough resource to pay Cost requirements.
     */
    public boolean enoughResource(Warehouse warehouse, Strongbox strongbox){
        for (ResourceContainer resourceContainer: resourceContainers){
            if(resourceContainer.getAmount() >
                    (warehouse.getNumOfResource(resourceContainer.getResource()) + strongbox.getNumOfResource(resourceContainer.getResource())))
                return false;
        }
        return true;
    }

    /**
     * @param strongbox is player's strongbox.
     * this method increases @param strongbox resources by an amount equal to Cost.
     * Cost remain unchanged.
     */
    public void  increaseResource(Strongbox strongbox){
        for(Resource resource: Resource.values()){
            strongbox.increaseResourceType(resource, this.getNumOfResource(resource));
        }
    }

    /**
     * @param warehouse is player's Warehouse.
     * @param strongbox is player's Strongbox.
     * @param choice refers to a player's choice. If @param choice == 1, the player has chosen to firstly decreased
     * resource from @param warehouse and then, if necessary, from @param strongbox, vice versa if @param choice == 2.
     * @throws InsufficientResourceException if player has not enough resources.
     * this method, firstly evaluates if the player has enough resources and then decreases his resources for an
     * amount equal to Cost.
     * Cost remain unchanged.
     */
    public void  decreaseResource(Warehouse warehouse, Strongbox strongbox, int choice) throws InsufficientResourceException{
        if(!(enoughResource(warehouse, strongbox)))
            throw new InsufficientResourceException();
        else {
            if (choice == 1) {
                decreaseWarehouseResource(warehouse, strongbox);
            } else {
                decreaseStrongboxResource(warehouse, strongbox);
            }
        }
    }

    /**
     * @param warehouse is player's Warehouse.
     * @param strongbox is player's Strongbox.
     * this method firstly decreased resource from @param warehouse and then, if necessary, from @param strongbox.
     * Cost remain unchanged.
     */
    private void decreaseWarehouseResource(Warehouse warehouse, Strongbox strongbox){
        for (Resource resource : Resource.values()) {
            int temp = warehouse.decreaseResource(resource, this.getNumOfResource(resource));
            strongbox.decreaseResourceType(resource, temp);
            /*
             temp stands for the amount of resource not decreased in @param warehouse
             if temp == 0, strongbox.decreaseResourceType(resource, temp) does nothing, otherwise decrease
             resource also in @param strongbox.
             */
        }
    }

    /**
     * @param warehouse is player's Warehouse.
     * @param strongbox is player's Strongbox.
     * this method firstly decreased resource from @param strongbox and then, if necessary, from @param warehouse.
     * Cost remain unchanged.
     */
    private void decreaseStrongboxResource(Warehouse warehouse, Strongbox strongbox){
        for (Resource resource : Resource.values()) {
            int temp = strongbox.decreaseResourceType(resource, this.getNumOfResource(resource));
            warehouse.decreaseResource(resource, temp);
            /*
             temp stands for the amount of resource not decreased in @param strongbox
             if temp == 0, warehouse.decreaseResource(resource, temp) does nothing, otherwise decrease
             resource also in @param warehouse.
             */
        }
    }

    /**
     * @param resource stands for the type of resource to add.
     * @param amount stands for the amount of @param resource to add.
     */
    public void addResource(Resource resource, int amount){
        if(amount == 0)
            return;
        for (ResourceContainer resourceContainer: resourceContainers)
            if(resourceContainer.getResource() == resource) {
                resourceContainer.increaseAmount(amount);
                return;
            }
        ResourceContainer container = new ResourceContainer(resource, amount);
        resourceContainers.add(container);
        /*
         if don't exist a ResourceContainer in Cost with @param resource, it creates a new ResourceContainer
         initialized with @param resource and @param amount, and add to resourceContainers.
         */
    }

    /**
     * @param resource stands fot the type of resource to decrease by 1.
     * @return true if Cost had at least 1 @param resource, otherwise @return false.
     */
    public boolean discount(Resource resource){
        if(getNumOfResource(resource) >= 1){
            for(ResourceContainer resourceContainer: resourceContainers) {
                if (resourceContainer.getResource() == resource)
                    resourceContainer.decreaseAmount(1);
            }
            return true;
        }
        return false;
    }

    /**
     * @param resource stands fot the type of resource to increase by 1.
     */
    public void recount(Resource resource){
        addResource(resource, 1);
    }

    @Override
    public String toString() {
        return resourceContainers.toString();
    }
}
