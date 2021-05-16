package it.polimi.ingsw.player.depots;

import it.polimi.ingsw.resourceContainers.Resource;

/**
 * ExtraDepot is a Depot which could be added after an activation of a ExtraDepotCard by the player. It always has
 * maxAmount == 2 and its attribute resource can't be modified.
 */
public class ExtraDepot extends Depot {

    /**
     * @param resource stands for the only type of the resource that this ExtraDepot can contains.
     * the constructor calls the super method of Depot class with maxAmount == 2 as a @param and set the resource of
     * ExtraDepot as @param resource.
     */
    public ExtraDepot(Resource resource) {
        super(2);
        setResource(resource);
    }
}
