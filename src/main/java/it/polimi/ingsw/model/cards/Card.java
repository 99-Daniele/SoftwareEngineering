package it.polimi.ingsw.model.cards;

import it.polimi.ingsw.model.cards.developmentCards.Color;
import it.polimi.ingsw.model.cards.leaderCards.LeaderRequirements;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.view.modelView.CardView;

/**
 * Card is the abstract class which is inherited by DevelopmentCard or LeaderCard.
 */
public abstract class Card implements CardView {

    private final int victoryPoints;
    private final int cardID;

    public Card(int victoryPoints, int cardID) {
        this.victoryPoints = victoryPoints;
        this.cardID = cardID;
    }

    @Override
    public Resource getResource() {
        return null;
    }

    @Override
    public LeaderRequirements getLeaderRequirements() {
        return null;
    }

    public int getVictoryPoints(){return victoryPoints;}

    @Override
    public int getCardID() {
        return cardID;
    }

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public Cost getProductionPowerResourceRequired() {
        return null;
    }

    @Override
    public Cost getProductionPowerResourceGiven() {
        return null;
    }

    @Override
    public int getFaithPointsGiven() {
        return 0;
    }

    @Override
    public int getLevel() {
        return 0;
    }
}
