package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.model.resourceContainers.ResourceContainer;

import java.util.ArrayList;

/**
 * Strongbox is player's strongbox. It can contains unlimited resources.
 */
public class Strongbox {

    private final ArrayList<ResourceContainer> resourceContainers = new ArrayList<>();

    /**
     * @param resource stands for the type of resource to count.
     * @return the count @param resource in Strongbox. If @param resource is not in Strongbox @return 0.
     */
    public int getNumOfResource(Resource resource) {
        for(ResourceContainer resourceContainer: resourceContainers){
            if(resourceContainer.getResource() == resource)
                return resourceContainer.getAmount();
        }
        return 0;
    }

    /**
     * @param resource stands for the type of resource to increase.
     * @param amount stands for the amount of @param resource to increase.
     * this method firstly verifies if already exist ResourceContainer which contains @param resource and, if not,
     * create a new one, and increase.
     */
    public void increaseResourceType (Resource resource, int amount){
        if(amount == 0)
            return;
        for(ResourceContainer resourceContainer: resourceContainers){
            if(resourceContainer.getResource() == resource) {
                resourceContainer.increaseAmount(amount);
                return;
            }
        }
        ResourceContainer resourceContainer = new ResourceContainer(resource, amount);
        resourceContainers.add(resourceContainer);
    }

    /**
     * @param resource stands for the type of resource to decrease.
     * @param amount stands for the amount of @param resource to decrease.
     * @return 0 if in Strongbox there are enough resources, otherwise @return the amount of not decreased resource.
     */
    public int decreaseResourceType (Resource resource, int amount){
        if(amount == 0)
            return 0;
        for(ResourceContainer resourceContainer: resourceContainers) {
            if (resourceContainer.getResource() == resource) {
                return resourceContainer.decreaseAmount(amount);
                /*
                 if exist ResourceContainer in Strongbox which contains @param resource, find it and
                 @return decreaseAmount() method of ResourceContainer
                 */
            }
        }
        return amount;
    }

    /**
     * @return a copy of Strongbox.
     * create a new Strongbox which is a copy of this.Strongbox.
     */
    public Strongbox copyThisStrongbox(){
        Strongbox s = new Strongbox();
        for(Resource resource: Resource.values()){
            s.increaseResourceType(resource, this.getNumOfResource(resource));
        }
        return s;
    }

    /**
     * @return the sum of all resource in the Strongbox.
     */
    public int sumStrongboxResource(){
        int sum = 0;
        for(ResourceContainer resourceContainer: resourceContainers)
            sum += resourceContainer.getAmount();
        return sum;
    }
}
