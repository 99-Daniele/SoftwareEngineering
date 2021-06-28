package it.polimi.ingsw.view.modelView;

import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

import static java.util.Collections.swap;

/**
 * WarehouseStrongbox is the View version of Warehouse and Strongbox Model classes combined.
 * In this case all depots and all Strongbox resources are coded as ResourceContainerView.
 */
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

    /**
     * @param resource is the increased resource.
     * @param depot is which depot has been increased.
     *
     * if @param depot is empty set new resource.
     */
    public void increaseWarehouse(Resource resource, int depot){
        if(depots.get(depot).getAmount() > 0)
            depots.get(depot).increase();
        else
            depots.get(depot).setNewResource(resource);
    }

    /**
     * @param resource is one resource
     * @param warehouseAmount is warehouse amount of @param resource
     * @param strongboxAmount is strongbox amount of @param resource.
     *
     * firstly set warehouse resources and then set strongbox amount.
     */
    public void newAmount(Resource resource, int warehouseAmount, int strongboxAmount){
        warehouseHandler(resource, warehouseAmount);
        strongboxHandler(resource, strongboxAmount);
    }

    /**
     * @param depot1 is one depot.
     * @param depot2 is one depot.
     *
     * swap chosen depots.
     */
    public void switchDepot(int depot1, int depot2) {
        swap(depots, depot1, depot2);
    }

    /**
     * @param r is the new extra depot resource.
     * @param secondDepot refers if new activated ExtraDepotCard is the player's second LeaderCard activated.
     */
    public void addExtraDepot(Resource r, boolean secondDepot){
        depots.add(new ResourceContainerView(r));
        this.secondDepot = secondDepot;
    }

    /**
     * @param r is one resource
     * @param strongboxAmount is strongbow amount of @param resource.
     */
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

    /**
     * @param r is one resource
     * @param warehouseAmount is warehouse amount of @param resource.
     *
     * firstly calculates the difference between @param warehouseAmount and an eventually extra depot amount.Then find if
     * exists a warehouse depot with @param r. If exist and difference is >= 0 set this depot amount to difference. Instead
     * if this difference is < 0 set warehouse amount as 0 and decrease extra depot amount by difference.
     * instead if there isn't any warehouse depot with @param r and the difference is negative, simply decrease extra
     * depot amount by difference
     */
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

    /**
     * @param r is one resource
     * @param amount is the amount of decreased @param resource.
     *
     * find the right extra depot of @param r and decrease its resource by @param amount.
     */
    private void decreaseExtraDepot(Resource r, int amount){
        if(existExtraDepot1() && depots.get(3).getResource() == r)
            depots.get(3).decrease(amount);
        else if(existExtraDepot2() && depots.get(4).getResource() == r)
            depots.get(4).decrease(amount);
    }

    /**
     * @param r is one resource
     * @return the position of warehouse depot which contains @param r. If there isn't any warehouse depot, @return -1.
     */
    private int rightDepot(Resource r){
        for(int i = 0; i < 3; i++){
            if(depots.get(i).getAmount() > 0 && depots.get(i).getResource() == r)
                return i;
        }
        return -1;
    }

    /**
     * @param r is one resource
     * @return extra depot amount of @param r.
     *
     * if there isn't any extra depot of @param r @return 0.
     */
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

    /**
     * CLI printer of Warehouse and STrongobox.
     */
    public void printCLIWarehouseStrongbox(){
        printCliWarehouse();
        printCliStrongbox();
    }

    /**
     * CLI printer of Warehouse.
     */
    public void printCliWarehouse(){
        System.out.println("WAREHOUSE:");
        for (int i=0;i<3;i++) {
            System.out.print(i+1+" ");
            depots.get(i).printCliWarehouse();
            System.out.println();
        }
        printCliExtraDepot();
    }

    /**
     * CLI printer of Strongbox.
     */
    public void printCliStrongbox(){
        System.out.println("STRONGBOX:");
        coin.printCliStrongbox();
        shield.printCliStrongbox();
        servant.printCliStrongbox();
        stone.printCliStrongbox();
    }

    /**
     * CLI printer of extra depots, if existing.
     */
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
