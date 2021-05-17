package it.polimi.ingsw.model.cards.leaderCards;

import it.polimi.ingsw.exceptions.InsufficientResourceException;

import it.polimi.ingsw.exceptions.NoSuchProductionPowerException;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.player.Warehouse;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;

/**
 * AdditionalProductionPowerCard is a type of LeaderCard with the effect to give player an additional production power
 * which convert a specific resource in a resource chosen by player and 1 faith point.
 */
public class AdditionalProductionPowerCard extends LeaderCard {

    public AdditionalProductionPowerCard(Resource resource, Cost resourceCost, int victoryPoints, int cardID) {
        super(resource, resourceCost, victoryPoints, cardID);
    }

    public AdditionalProductionPowerCard(Resource resource, LeaderRequirements leaderRequirements, int victoryPoints, int cardID) {
        super(resource, leaderRequirements, victoryPoints, cardID);
    }

    /**
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param choice is player's choice about which between warehouse and strongbox has priority to be decreased.
     * @throws InsufficientResourceException if player has not enough resources.
     * this method only decreases player resources by 1 this.resource.
     */
    @Override
    public void decreaseProductionPowerResources(Warehouse w, Strongbox s, int choice)
            throws InsufficientResourceException, NoSuchProductionPowerException {
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
        else
            throw new NoSuchProductionPowerException();
    }

    /**
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param choice is player's choice about which between warehouse and strongbox has priority to be decreased.
     * @param resource is player's choice about the extra resource given.
     * @throws InsufficientResourceException if player has not enough resources.
     * @return the faith points given by the additional production power.
     * this method decrease by 1 this.resource and increase by 1 @param resource.
     */
    @Override
    public int additionalProductionPower(Warehouse w, Strongbox s, int choice, Resource resource) throws InsufficientResourceException, NoSuchProductionPowerException {
        if(this.isActive()) {
            decreaseProductionPowerResources(w, s, choice);
            s.increaseResourceType(resource, 1);
            return 1;
        }
        return 0;
    }
}
