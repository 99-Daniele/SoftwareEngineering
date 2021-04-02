package it.polimi.ingsw.model;

/**
 * ResourceContainer is a container of only one type of resource with its amount
 */
public class ResourceContainer {

    private Resource resource;
    private int amount;

    /**
     * @param resource identifies the type of resource of the ResourceContainer
     * @param amount stands for the number of resource in the ResourceContainer
     */
    public ResourceContainer(Resource resource, int amount) {
        this.resource = resource;
        this.amount = amount;
    }

    public Resource getResource() {
        return resource;
    }

    public int getAmount() {
        return amount;
    }

    /**
     * @param amount stands for the amount of ResourceContainer-type resource to increase
     */
    public void increaseAmount(int amount){
        this.amount +=amount;
    }

    /**
     * @param amount stands for the amount of ResourceContainer-type resource to decrease
     * @return 0 if the ResourceContainer amount is higher than @param amount,
     * otherwise @return the amount of not decreased resource and set ResourceContainer as empty
     */
    public int decreaseAmount(int amount){
        if(this.amount >= amount) {
            this.amount -= amount;
            return 0;
        }
        else {
            int temp = amount - this.amount;
            this.amount = 0;
            return temp;
        }
    }
}
