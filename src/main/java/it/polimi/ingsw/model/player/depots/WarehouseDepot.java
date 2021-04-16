package it.polimi.ingsw.model.player.depots;

import it.polimi.ingsw.model.player.depots.Depot;

/**
 * WarehouseDepot is the standard Depot in Warehouse. It has 3 possible maxAmount(1, 2 or 3) and its resource attribute
 * could change during the game.
 */
public class WarehouseDepot extends Depot {

    /**
     * @param maxAmount stands for the maxAmount of the WarehouseDepot. The constructor calls the super method of Depot
     * class with maxAmount as a @param.
     */
    public WarehouseDepot(int maxAmount) {
        super(maxAmount);
    }
}
