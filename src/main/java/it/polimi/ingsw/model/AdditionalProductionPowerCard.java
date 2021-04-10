package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InsufficientResourceException;

/**
 * AdditionalProductionPowerCard is a type of LeaderCard with the effect to give player an additional production power
 * which convert a specific resource in a resource chosen by player and 1 faith point
 */
public class AdditionalProductionPowerCard extends LeaderCard{

    public AdditionalProductionPowerCard(Resource resource, Cost resourceCost, int victoryPoints) {
        super(resource, resourceCost, victoryPoints);
    }

    public AdditionalProductionPowerCard(Resource resource, LeaderRequirements leaderRequirements, int victoryPoints) {
        super(resource, leaderRequirements, victoryPoints);
    }

    @Override
    public void decreaseProductionPowerResources(Warehouse w, Strongbox s, int choice, Resource resource)
            throws InsufficientResourceException {
        if(this.isActive()) {
            if (choice == 1) {
                if (w.decreaseResource(this.getResource(), 1) == 1) {
                    if (s.decreaseResourceType(this.getResource(), 1) == 1)
                        throw new InsufficientResourceException();
                }
            } else {
                if (s.decreaseResourceType(this.getResource(), 1) == 1) {
                    if (w.decreaseResource(this.getResource(), 1) == 1)
                        throw new InsufficientResourceException();
                }
            }
            /*
             if both decreaseResource() @return 1 means player has 0 this.resource so throws InsufficientResourceException
             */
        }
    }

    /**
     * @param w is player's warehouse
     * @param s is player's strongbox
     * @param choice is player's choice about which between warehouse and strongbox has priority to be decreased
     * @param resource is player's choice about the extra resource given
     * @return 1 if player has at least 1 this.resource, otherwise @return 0
     * this method decrease by 1 this.resource and increase by 1 @param resource
     */
    @Override
    public int additionalProductionPower(Warehouse w, Strongbox s, int choice, Resource resource) throws InsufficientResourceException{
        if(this.isActive()) {
            decreaseProductionPowerResources(w, s, choice, resource);
            s.increaseResourceType(resource, 1);
            return 1;
        }
        return 0;
    }
}
