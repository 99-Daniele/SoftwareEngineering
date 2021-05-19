package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.resourceContainers.Resource;

public class Resource_Container_View {

    private Resource resource;
    private int amount;

    public Resource_Container_View(Resource resource) {
        this.resource = resource;
    }

    public void print(){};

    public Resource getResource() {
        return resource;
    }

    public int getAmount() {
        return amount;
    }

    public void increase(){
        this.amount++;
    }

    public void decrease(int amount){
        this.amount -= amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }

    public void setNewResource(Resource resource){
        this.resource = resource;
        amount = 1;
    }
}
