package it.polimi.ingsw.view.modelView;

import it.polimi.ingsw.model.cards.developmentCards.Color;
import it.polimi.ingsw.model.cards.leaderCards.LeaderRequirements;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;

/**
 * CardView is the generic interface of Leader and Development Cards.
 */
public interface CardView {

    int getCardID();

    int getLevel();

    int getVictoryPoints();

    Resource getResource();

    Color getColor();

    Cost getResourceCost();

    Cost getProductionPowerResourceRequired();

    Cost getProductionPowerResourceGiven();

    int getFaithPointsGiven();

    LeaderRequirements getLeaderRequirements();
}
