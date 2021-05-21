package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.view.ColorAnsi;

public class Resource_Container_View {

    private Resource resource;
    private int amount;

    public Resource_Container_View(Resource resource) {
        this.resource = resource;
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
