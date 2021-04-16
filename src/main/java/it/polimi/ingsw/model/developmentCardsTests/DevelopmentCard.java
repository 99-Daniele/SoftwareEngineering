package it.polimi.ingsw.model.developmentCardsTests;

import it.polimi.ingsw.exceptions.InsufficientResourceException;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.player.Warehouse;

/**
 * DevelopmentCard is one of 48 cards buyable by players.
 */
public class DevelopmentCard {

    private final Color color;
    private final int level;
    private final Cost resourceCost;
    private final int victoryPoints;
    private final Cost productionPowerResourceRequired;
    private final Cost productionPowerResourceGiven;
    private final int faithPointsGiven;

    public DevelopmentCard(Color color, int level, Cost resourceCost, int victoryPoints, Cost productionPowerResourceRequired,
                           Cost productionPowerResourceGiven, int faithPointsGiven) {
        this.color = color;
        this.level = level;
        this.resourceCost = resourceCost;
        this.victoryPoints = victoryPoints;
        this.productionPowerResourceRequired = productionPowerResourceRequired;
        this.productionPowerResourceGiven = productionPowerResourceGiven;
        this.faithPointsGiven = faithPointsGiven;
    }

    public Color getColor() {
        return color;
    }

    public int getLevel() {
        return level;
    }

    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * @param w is player's warehouse
     * @param s is player's strongbox
     * @return true if player has enough resources to buy this card
     */
    public boolean isBuyable(Warehouse w, Strongbox s){
        return resourceCost.enoughResource(w, s);
    }

    /**
     * this method allows to buy a new card from the deck.
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param choice if 1, indicate that the resources in warehouse will be the first to be decreased. Else, strongbox is the first.
     * @throws InsufficientResourceException thrown when there aren't enough resources.
     */
    public void buyCard(Warehouse w, Strongbox s, int choice) throws InsufficientResourceException {
        controlDiscardResource(w,s,choice,false);
    }

    /**
     * this method active the production of a this card.
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param choice if 1, indicate that the resources in warehouse will be the first to be decreased. Else, strongbox is the first.
     * @return the faith points of the card.
     * @throws InsufficientResourceException thrown when there aren't enough resources.
     */
    public int activateProduction(Warehouse w, Strongbox s, int choice) throws InsufficientResourceException{
        controlDiscardResource(w,s,choice,true);
        productionPowerResourceGiven.increaseResource(s);
        return faithPointsGiven;
    }

    /**
     * this method control if there are enough resources in warehouse or strongbox. If yes, discard the resources.
     * @param w is player's warehouse.
     * @param s is player's strongbox.
     * @param choice if 1, indicate that the resources in warehouse will be the first to be decreased. Else, strongbox is the first.
     * @param isPowerProduction if true indicate that i want to active a card. Else, i want to buy a card.
     * @throws InsufficientResourceException thrown when there aren't enough resources.
     */
    public void controlDiscardResource(Warehouse w, Strongbox s, int choice, boolean isPowerProduction)
            throws InsufficientResourceException {
        Cost cost;
        if(isPowerProduction)cost=productionPowerResourceRequired;
        else cost=resourceCost;
        cost.decreaseResource(w,s,choice);
    }

    /**
     * this method allows to decrease by 1 unit the amount of a selected resource.
     * @param resource the specific resource.
     * @return true if the operation of decreasing by 1 unit a selected resource is successful, else false.
     */
    public boolean discount(Resource resource){
        if(resourceCost.getNumOfResource(resource) > 0) {
            resourceCost.discount(resource);
            return true;
        }
        return false;
    }

    /**
     * this method allows to increase by 1 unit the amount of a selected resource.
     * @param resource indicates the resource that i want to decrease the amount of.
     */
    public void recount(Resource resource){
        resourceCost.recount(resource);
    }
}
