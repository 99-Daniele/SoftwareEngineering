package it.polimi.ingsw.view.modelView;

import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

import static java.util.Collections.swap;

public class WarehouseStrongboxView {

    private final ArrayList<ResourceContainerView> depots;
    private final ResourceContainerView coin;
    private final ResourceContainerView shield;
    private final ResourceContainerView servant;
    private final ResourceContainerView stone;
    private boolean secondDepot = false;

    public WarehouseStrongboxView() {
        depots = new ArrayList<>(3);
        depots.add(new ResourceContainerView());
        depots.add(new ResourceContainerView());
        depots.add(new ResourceContainerView());
        coin = new ResourceContainerView(Resource.COIN);
        shield = new ResourceContainerView(Resource.SHIELD);
        servant = new ResourceContainerView(Resource.SERVANT);
        stone = new ResourceContainerView(Resource.STONE);
    }

    public void increaseWarehouse(Resource resource, int depot){
        if(depots.get(depot).getAmount() > 0)
            depots.get(depot).increase();
        else
            depots.get(depot).setNewResource(resource);
    }

    public void newAmount(Resource resource, int warehouseAmount, int strongboxAmount){
        warehouseHandler(resource, warehouseAmount);
        strongboxHandler(resource, strongboxAmount);
    }

    public void switchDepot(int depot1, int depot2) {
        swap(depots, depot1, depot2);
    }

    public void addExtraDepot(Resource r, boolean secondDepot){
        depots.add(new ResourceContainerView(r));
        this.secondDepot = secondDepot;
    }

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
        for(int i = 0; i < 3; i++){
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
        return depots.size() >= 4;
    }

    public boolean existExtraDepot2(){
        return depots.size() == 5;
    }

    public void printCLIWarehouseStrongbox(){
        printCliWarehouse();
        printCliStrongbox();
    }

    public void printCliWarehouse(){
        System.out.println("WAREHOUSE:");
        for (int i=0;i<3;i++) {
            System.out.print(i+1+" ");
            depots.get(i).printCliWarehouse();
            System.out.println();
        }
        printCliExtraDepot();
    }

    public void printCliStrongbox(){
        System.out.println("STRONGBOX:");
        coin.printCliStrongbox();
        shield.printCliStrongbox();
        servant.printCliStrongbox();
        stone.printCliStrongbox();
    }

    public void printCliExtraDepot(){
        if (existExtraDepot1()) {
            System.out.println("EXTRA DEPOTS:");
            System.out.print("4 ");
            depots.get(3).printCliWarehouse();
            System.out.println();
            if (existExtraDepot2()) {
                System.out.print("5 ");
                depots.get(4).printCliWarehouse();
                System.out.println();
            }
        }
    }

    public ArrayList<ResourceContainerView> getWarehouse(){
        return depots;
    }

    public int coinAmount(){
        return coin.getAmount();
    }

    public int shieldAmount(){
        return shield.getAmount();
    }

    public int stoneAmount(){
        return stone.getAmount();
    }

    public int servantAmount(){
        return servant.getAmount();
    }

    public boolean isSecondDepot() {
        return secondDepot;
    }
}
