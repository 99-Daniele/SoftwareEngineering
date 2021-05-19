package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

import static java.util.Collections.swap;

public class Warehouse_Strongbox_View {

    private ArrayList<Resource_Container_View> depots;
    private final Resource_Container_View coin;
    private final Resource_Container_View shield;
    private final Resource_Container_View servant;
    private final Resource_Container_View stone;

    public Warehouse_Strongbox_View() {
        depots = new ArrayList<>(3);
        coin = new Resource_Container_View(Resource.COIN);
        shield = new Resource_Container_View(Resource.SHIELD);
        servant = new Resource_Container_View(Resource.SERVANT);
        stone = new Resource_Container_View(Resource.STONE);
    }

    public void increaseWarehouse(Resource resource, int depot){
        if(depots.get(depot-1).getAmount() > 0)
            depots.get(depot-1).increase();
        else
            depots.get(depot-1).setNewResource(resource);
    }

    public void newAmount(Resource resource, int warehouseAmount, int strongboxAmount){
        warehouseHandler(resource, warehouseAmount);
        strongboxHandler(resource, strongboxAmount);
    }

    public void switchDepot(int depot1, int depot2) {
        swap(depots, depot1 - 1, depot2 - 1);
    }

    public void addExtraDepot(Resource r){
        depots.add(new Resource_Container_View(r));
    }

    public void print(){};

    private void strongboxHandler(Resource r, int strongboxAmount){
        switch (r){
            case COIN:
                coin.setAmount(strongboxAmount);
                break;
            case SHIELD:
                shield.setAmount(strongboxAmount);
                break;
            case SERVANT:
                servant.setAmount(strongboxAmount);
                break;
            case STONE:
                stone.setAmount(strongboxAmount);
                break;
        }
    }

    private void warehouseHandler(Resource r, int warehouseAmount){
        int difference = warehouseAmount - extraDepotAmount(r);
        int warehouseDepot = rightDepot(r);
        if(warehouseDepot != -1) {
            if (difference >= 0)
                depots.get(warehouseDepot).setAmount(difference);
            else {
                depots.get(warehouseDepot).setAmount(0);
                decreaseExtraDepot(r, (-difference));
            }
        }
        else if(difference < 0)
            decreaseExtraDepot(r, (-difference));
    }

    private void decreaseExtraDepot(Resource r, int amount){
        if(existExtraDepot1() && depots.get(3).getResource() == r)
            depots.get(3).decrease(amount);
        else if(existExtraDepot2() && depots.get(4).getResource() == r)
            depots.get(4).decrease(amount);
    }

    private int rightDepot(Resource r){
        for(int i = 0; i < 2; i++){
            if(depots.get(i).getAmount() > 0 && depots.get(i).getResource() == r)
                return i;
        }
        return -1;
    }

    private int extraDepotAmount(Resource r){
        if(existExtraDepot1() && depots.get(3).getResource() == r)
            return depots.get(3).getAmount();
        if(existExtraDepot2() && depots.get(4).getResource() == r)
            return depots.get(4).getAmount();
        return 0;
    }

    private boolean existExtraDepot1(){
        return depots.size() == 4;
    }

    private boolean existExtraDepot2(){
        return depots.size() == 5;
    }

}
