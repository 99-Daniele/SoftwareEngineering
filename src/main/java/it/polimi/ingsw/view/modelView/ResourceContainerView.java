package it.polimi.ingsw.view.modelView;

import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.view.CLI.ColorAnsi;

public class ResourceContainerView {

    private Resource resource;
    private int amount;

    public ResourceContainerView() {
        this.resource = Resource.COIN;
        this.amount = 0;
    }

    public ResourceContainerView(Resource resource) {
        this.resource = resource;
        this.amount = 0;
    }

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

    public void print(){};

    public void printCliWarehouse() {
        switch (resource) {
            case COIN:
                for (int i = 0; i < amount; i++)
                    System.out.print(ColorAnsi.ANSI_YELLOW.escape() + "● " + ColorAnsi.RESET);
                break;
            case SERVANT:
                for (int i = 0; i < amount; i++)
                    System.out.print(ColorAnsi.ANSI_PURPLE.escape() + "● " + ColorAnsi.RESET);
                break;
            case SHIELD:
                for (int i = 0; i < amount; i++)
                    System.out.print(ColorAnsi.ANSI_CYAN.escape() + "● " + ColorAnsi.RESET);
                break;
            case STONE:
                for (int i = 0; i < amount; i++)
                    System.out.print(ColorAnsi.ANSI_WHITE.escape() + "● " + ColorAnsi.RESET);
                break;
        }
    }

    public void printCliStrongbox(){
        switch (resource) {
            case COIN:
                    System.out.println(ColorAnsi.ANSI_YELLOW.escape() + "● " + ColorAnsi.RESET+amount);
                break;
            case SERVANT:
                    System.out.println(ColorAnsi.ANSI_PURPLE.escape() + "● " + ColorAnsi.RESET+amount);
                break;
            case SHIELD:
                    System.out.println(ColorAnsi.ANSI_CYAN.escape() + "● " + ColorAnsi.RESET+amount);
                break;
            case STONE:
                    System.out.println(ColorAnsi.ANSI_WHITE.escape() + "● " + ColorAnsi.RESET+amount);
                break;
        }
    }
}
