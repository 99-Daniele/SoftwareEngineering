package it.polimi.ingsw.model;

import java.util.ArrayList;

import it.polimi.ingsw.exceptions.ImpossibleSwitchDepotException;


/**
 * Warehouse is player's warehouse. It has two attributes: a WarehouseDepot[3] warehouseDepots and an ArrayList</ExtraDepot>
 * which is initialized after player actives an ExtraDepotLeaderCard.
 */
public class Warehouse {

    private final WarehouseDepot[] warehouseDepots;
    private ArrayList <ExtraDepot> extraDepots;

    /**
     * the constructor creates 3 new WarehouseDepot with 1, 2 and 3 as maxAmount, and put them in order in warehouseDepots.
     */
    public Warehouse(){
        warehouseDepots = new WarehouseDepot[3];
        warehouseDepots[0] = new WarehouseDepot(1);
        warehouseDepots[1] = new WarehouseDepot(2);
        warehouseDepots[2] = new WarehouseDepot(3);
    }

    /**
     * @return if extraDepots has been initialized.
     */
    public boolean existExtraDepot (){
        return (extraDepots != null);
    }

    /**
     * @param resource stands for the resource of new ExtraDepot which has to be added in extraDepots.
     * if extraDepots wasn't previously initialized, it creates a new ArrayList</ExtraDepot>.
     */
    public void addExtraDepot(Resource resource){
        ExtraDepot extraDepot = new ExtraDepot(resource);
        if(!(existExtraDepot()))
            extraDepots = new ArrayList<>();
        extraDepots.add(extraDepot);
    }

    /**
     * @param resource stands for the type of resource to count.
     * @return the count of @param resource in the Warehouse. If exists an ExtraDepot it calculate both in extraDepots
     * and warehouseDepotList, otherwise only in warehouseDepots. If @param resource is not in Warehouse @return 0.
     */
    public int getNumOfResource(Resource resource){
        if(existExtraDepot())
            return (getNumOfResourceInExtraDepots(resource) + getNumOfResourceInWarehouseDepots(resource));
        else
            return getNumOfResourceInWarehouseDepots(resource);
    }

    /**
     * @param resource stands for the type of resource to count in extraDepots.
     * @return the count of @param resource in extraDepots.
     * for each ExtraDepot in extraDepots evaluates if contains @param resource and in case @return its amount.
     * if @param resource is not in extraDepots @return 0.
     */
    private int getNumOfResourceInExtraDepots(Resource resource){
        for(ExtraDepot extraDepot: extraDepots)
            if(extraDepot.getResource() == resource)
                return extraDepot.getAmount();
        return 0;
    }

    /**
     * @param resource stands for the type of resource to count in warehouseDepots.
     * @return the count of @param resource in warehouseDepots.
     * for each WarehouseDepot in warehouseDepots evaluates if contains @param resource and in case @return its amount.
     * if @param resource is not in warehouseDepots @return 0.
     */
    private int getNumOfResourceInWarehouseDepots(Resource resource){
        for(WarehouseDepot warehouseDepot: warehouseDepots){
            if(warehouseDepot.getResource() == resource && !(warehouseDepot.isEmpty()))
                return warehouseDepot.getAmount();
        }
        return 0;
    }

    /**
     * @param resource stands for the resource to find in warehouseDepots.
     * @return the position of the not empty WarehouseDepot in warehouseDepots which contains @param resource,
     * otherwise @return -1.
     */
    private int getDepotPosition(Resource resource){
        for(int i = 0; i < 3; i++){
            if(warehouseDepots[i].getResource() == resource && !(warehouseDepots[i].isEmpty()))
                return i;
        }
        return -1;
    }

    /**
     * @return the position of the first empty WarehouseDepot in warehouseDepots, otherwise @return -1.
     */
    private int emptyDepotPosition(){
        for(int i = 0; i < 3; i++){
            if(warehouseDepots[i].isEmpty())
                return i;
        }
        return -1;
    }

    /**
     * @param resource stands for the type of resource to increase by 1 in warehouseDepots.
     * @return true if @param resource is correctly increased, otherwise @return false.
     * the method increases in the correct WarehouseDepot, otherwise in the first empty WarehouseDepot
     */
    private boolean increaseWarehouseDepot(Resource resource){
        int pos = getDepotPosition(resource);
        if (pos == -1) {
            pos = emptyDepotPosition();
            if (pos == -1) {
                return false;
                /*
                 if there is no WarehouseDepot with @param resource and there is no empty
                 WarehouseDepot @return false.
                 */
            }
            else {
                warehouseDepots[pos].setResource(resource);
                return (warehouseDepots[pos].increaseAmount());
                /*
                 if there is no WarehouseDepot in warehouseDepots with @param resource, but there is at least 1 empty
                 WarehouseDepot, find it, set the resource and @return increaseAmount().
                 */
            }
        } else {
            return (warehouseDepots[pos].increaseAmount());
            /*
             if there is a WarehouseDepot in warehouseDepots with @param resource, find it and @return increaseAmount().
             */
        }
    }

    /**
     * @param resource stands for the type of resource to increase by 1 in Warehouse.
     * @return true if @param resource is correctly increased, otherwise @return false.
     * the method increases in the correct ExtraDepot, otherwise in the correct WarehouseDepot,
     * otherwise in the first empty WarehouseDepot
     */
    public boolean increaseResource(Resource resource) {
        if (existExtraDepot()) {
            for (ExtraDepot extraDepot : extraDepots) {
                if (extraDepot.getResource() == resource) {
                    if (extraDepot.increaseAmount() == false)
                        return increaseWarehouseDepot(resource);
                    return true;
                }
            }
            /*
             if exist ExtraDepot in extraDepots, for each one of it try to increase.
             if the increase is successfully @return true, otherwise @return increaseWarehouseDepot().
             */
        }
        return increaseWarehouseDepot(resource);
        /*
         if not exist ExtraDepot in extraDepots, @return increaseWarehouseDepot().
         */
    }

    /**
     * @param resource stands for the type of resource to decrease in warehouseDepots.
     * @param amount stands fot the amount of @param resource to decrease.
     * @return 0 if in depotList there are enough resources, otherwise @return the amount of not decreased resource.
     */
    private int decreaseWarehouseDepot(Resource resource, int amount){
        int pos = getDepotPosition(resource);
        if(pos == -1) {
            return amount;
            /*
             if there is no WarehouseDepot with @param resource @return @param amount.
             */
        }
        return (warehouseDepots[pos].decreaseAmount(amount));
        /*
         if there is a WarehouseDepot with @param resource, find it and @return decreaseAmount().
         */
    }

    /**
     * @param resource stands for the type of resource to decrease in Warehouse.
     * @param amount stands for the amount of @param resource to decrease.
     * @return 0 if in Warehouse there are enough resources, otherwise @return the amount of not decreased resource.
     * the method firstly decreases in warehouseDepots and, if necessary, in extraDepots.
     */
    public int decreaseResource(Resource resource, int amount){
        if(amount == 0)
            return 0;
        int temp = decreaseWarehouseDepot(resource, amount);
        if(temp == 0) {
            return 0;
            /*
             if @resource is correctly decreased in warehouseDepots @return 0.
             */
        }
        if(existExtraDepot()) {
            for (ExtraDepot extraDepot : extraDepots) {
                if (extraDepot.getResource() == resource)
                    return extraDepot.decreaseAmount(temp);
            }
            /*
             if there are remaining @resource to decreased and exist ExtraDepot with @param resource, find it
             and @return decreaseAmount().
             */
        }
        return temp;
        /*
         if not exist ExistDepot with @param resource, @return the amount of @param resource previously not decreased.
         */
    }

    /**
     * @param pos stands for position of WarehouseDepot in warehouseDepots to be modified.
     * @param amount stands for the amount of resource to be set.
     * @param resource stands for the type of resource to be set.
     */
    private void setWarehouseDepot(int pos, int amount, Resource resource){
        warehouseDepots[pos].setAmount(amount);
        warehouseDepots[pos].setResource(resource);
    }

    /**
     * @param pos stands for position of ExtraDepot in extraDepots to be modified.
     * @param amount stands for the amount of resource to be set.
     */
    private void setExtraDepot(int pos, int amount){
        extraDepots.get(pos).setAmount(amount);
    }

    /**
     * this method switch two different WarehouseDepot.
     * @param depot1 stands for position of the first WarehouseDepot in warehouseDepots to switch.
     * @param depot2 stands for position of the second WarehouseDepot in warehouseDepots to switch.
     * @throws ImpossibleSwitchDepotException if the switch is not possible.
     */
    private void switchWarehouseDepots(int depot1, int depot2) throws ImpossibleSwitchDepotException{
        int amount1 = warehouseDepots[depot1].getAmount();
        int amount2 = warehouseDepots[depot2].getAmount();
        Resource resourceTemp1 = warehouseDepots[depot1].getResource();
        Resource resourceTemp2 = warehouseDepots[depot2].getResource();
        if(amount1 > warehouseDepots[depot2].getMaxAmount()
                || amount2 > warehouseDepots[depot1].getMaxAmount()){
            throw new ImpossibleSwitchDepotException();
            /*
             if one or both WarehouseDepot has more resources than maxAmount of the other one, throw ImpossibleSwitchDepotException()
             */
        }
        setWarehouseDepot(depot1, amount2, resourceTemp2);
        setWarehouseDepot(depot2, amount1, resourceTemp1);
        /*
         if both WarehouseDepot has less resources than maxAmount of the other one, set each WarehouseDepot with
         other one parameters.
         */
    }

    /**
     * this method switch an ExtraDepot with an empty WarehouseDepot.
     * @param extraDepotPos stands for position of the ExtraDepot in extraDepots to switch.
     * @param warehouseDepotPos stands for position of the empty WarehouseDepot in warehouseDepots to switch.
     * @throws  ImpossibleSwitchDepotException if the switch is not possible.
     */
    private void switchExtraDepotWithEmptyWarehouseDepot(int extraDepotPos, int warehouseDepotPos) throws ImpossibleSwitchDepotException{
        for (int i = 0; i < 3; i++){
            if(!(warehouseDepots[i].isEmpty()) && warehouseDepots[i].getResource() == extraDepots.get(extraDepotPos).getResource()) {
                throw new ImpossibleSwitchDepotException();
                /*
                 if exist a not empty WarehouseDepot with the same type of resource of ExtraDepot, throw ImpossibleSwitchDepotException()
                 */
            }
        }
        if(extraDepots.get(extraDepotPos).getAmount() > warehouseDepots[warehouseDepotPos].getMaxAmount()) {
            throw new ImpossibleSwitchDepotException();
            /*
             if ExtraDepot has more resources than maxAmount of WarehouseDepot, throw ImpossibleSwitchDepotException()
             */
        }
        setWarehouseDepot(warehouseDepotPos, extraDepots.get(extraDepotPos).getAmount(), extraDepots.get(extraDepotPos).getResource());
        setExtraDepot(extraDepotPos, 0);
        /*
         if the switch is possible, set the empty WarehouseDepot with ExtraDepot parameters and set WarehouseDepot as empty.
         */
    }

    /**
     * this method switch a WarehouseDepot with an ExtraDepot.
     * @param warehouseDepotPos stands for position of the WarehouseDepot in warehouseDepots to switch.
     * @param extraDepotPos stands for position of the ExtraDepot in extraDepots to switch.
     * @throws  ImpossibleSwitchDepotException if thw switch is not possible.
     */
    private void switchWarehouseDepotWithExtraDepot(int warehouseDepotPos, int extraDepotPos) throws ImpossibleSwitchDepotException{
        extraDepotPos -= 3;
        if(warehouseDepots[warehouseDepotPos].isEmpty()){
            switchExtraDepotWithEmptyWarehouseDepot(extraDepotPos, warehouseDepotPos);
        }
        if (warehouseDepots[warehouseDepotPos].getResource() != extraDepots.get(extraDepotPos).getResource()) {
            throw new ImpossibleSwitchDepotException();
            /*
            if Depots contains different resources, throw ImpossibleSwitchDepotException()
             */
        }
        if (warehouseDepots[warehouseDepotPos].getMaxAmount() > 2
                || extraDepots.get(extraDepotPos).getAmount() > warehouseDepots[warehouseDepotPos].getMaxAmount()) {
            throw new ImpossibleSwitchDepotException();
            /*
             if one or both Depots have more resource than maxAmount of the other one, throw ImpossibleSwitchDepotException()
             */
        }
        int amount1 = warehouseDepots[warehouseDepotPos].getAmount();
        int amount2 = extraDepots.get(extraDepotPos).getAmount();
        setWarehouseDepot(warehouseDepotPos, amount2, warehouseDepots[warehouseDepotPos].getResource());
        setExtraDepot(extraDepotPos, amount1);
        /*
         if both Depots have less resource than maxAmount of the other one, set each Depot with other one parameters
         */
    }

    /**
     * this method switch two Depot.
     * @param depot1 stands for position of the first Depot in Warehouse to switch.
     * @param depot2 stands for position of the second Depot in Warehouse to switch.
     * @throws ImpossibleSwitchDepotException if the switch is not possible
     * if @param is 0 stands for the first WarehouseDepot, 1 for the second one, 2 for the third one,
     * 3 for the first ExtraDepot, 4 for the second ExtraDepot.
     */
    public void switchDepots(int depot1, int depot2) throws ImpossibleSwitchDepotException{
        if(depot1 < 3 && depot2 < 3) {
            switchWarehouseDepots(depot1, depot2);
        }
        else if(depot1 >= 3 && depot2 >= 3) {
            throw new ImpossibleSwitchDepotException();
        }
        else if(depot1 < 3)
            switchWarehouseDepotWithExtraDepot(depot1, depot2);
        else
            switchWarehouseDepotWithExtraDepot(depot2, depot1);
    }

    /**
     * @return the sum of all resource in the Warehouse.
     */
    public int sumWarehouseResource(){
        int sum = 0;
        for(WarehouseDepot depot: warehouseDepots)
            sum += depot.getAmount();
        if(existExtraDepot()){
            for (ExtraDepot extraDepot: extraDepots)
                sum += extraDepot.getAmount();
        }
        return sum;
    }
}
