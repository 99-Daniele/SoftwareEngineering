package it.polimi.ingsw.model.player.depots;

import it.polimi.ingsw.model.resourceContainers.Resource;

/**
 * Depot is a container of only one type of resource with its amount limited to maxAmount.
 */
public abstract class Depot{

    private Resource resource;
    private final int maxAmount;
    private int amount;

    /**
     * @param maxAmount stands for the max amount of resource that the depot can contain in itself (1, 2, or 3).
     * the Depot is initialized as empty.
     */
    public Depot(int maxAmount) {
        this.maxAmount = maxAmount;
        this.amount = 0;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public int getAmount() {
        return amount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    /**
     * @param amount set the new amount of Depot. @param amount is previously verified to be less than maxAmount.
     *
     * if @param amount == 0 also set Depot as empty.
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isEmpty() {
        return amount == 0;
    }

    /**
     * @return false if Depot was already full before the call of the method, otherwise @return true.
     *
     * this method increase the amount of resource in this Depot by 1.
     */
    public boolean increaseAmount(){
        if(this.amount + 1 <= maxAmount) {
            this.amount += 1;
            return true;
        }
        else{
            this.amount = maxAmount;
            return false;
        }
    }

    /**
     * @param amount stands for the amount of Depot-type resource to decrease.
     * @return 0 if the Depot amount is higher than @param amount, otherwise @return the amount of not decreased
     * resource and set Depot as empty.
     */
    public int decreaseAmount(int amount){
        if(this.amount - amount > 0) {
            this.amount -= amount;
            return 0;
        }
        else{
            int temp = amount - this.amount;
            this.amount = 0;
            return temp;
        }
    }
}
