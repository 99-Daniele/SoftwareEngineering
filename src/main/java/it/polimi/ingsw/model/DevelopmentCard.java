package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.InsufficientResourceException;

/**
 * DevelopmentCard is one of 64 cards contained in Deck
 */
public class DevelopmentCard {

    private final Color color;
    private final int level;
    private final Cost resourceCost;
    private final int victoryPoints;
    private  Cost productionPowerResourceRequired;
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

    public void buyCard(Warehouse w, Strongbox s, int choice) throws InsufficientResourceException{}

    public int activateProduction(Warehouse w, Strongbox s, int choice) throws InsufficientResourceException{
        return faithPointsGiven;
    }

    public boolean discount(Resource resource){
        if(resourceCost.getNumOfResource(resource) > 0) {
            resourceCost.discount(resource);
            return true;
        }
        return false;
    }

    public void recount(Resource resource){
        resourceCost.recount(resource);
    }
}
