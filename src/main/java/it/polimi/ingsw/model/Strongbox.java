package it.polimi.ingsw.model;

import java.util.ArrayList;

public class Strongbox {

    private ArrayList<ResourceContainer> resourceContainers = new java.util.ArrayList<>();

    public int getNumOfResource(Resource resource) {
        for(ResourceContainer resourceContainer: resourceContainers){
            if(resourceContainer.getResource() == resource)
                return resourceContainer.getAmount();
        }
        return 0;
    }

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

    public int decreaseResourceType (Resource resource, int amount){
        if(amount == 0)
            return 0;
        for(ResourceContainer resourceContainer: resourceContainers) {
            if (resourceContainer.getResource() == resource) {
                return resourceContainer.decreaseAmount(amount);
            }
        }
        return amount;
    }
}
