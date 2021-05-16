package it.polimi.ingsw.cards.leaderCards;

import it.polimi.ingsw.resourceContainers.Cost;
import it.polimi.ingsw.resourceContainers.Resource;

/**
 * WhiteConversionCard is a type of LeaderCard with the effect to convert a WhiteMarble into ResourceMarble.
 */
public class WhiteConversionCard extends LeaderCard {

    public WhiteConversionCard(Resource resource, Cost resourceCost, int victoryPoints, int cardID) {
        super(resource, resourceCost, victoryPoints, cardID);
    }

    public WhiteConversionCard(Resource resource, LeaderRequirements leaderRequirements, int victoryPoints, int cardID) {
        super(resource, leaderRequirements, victoryPoints, cardID);
    }

    /**
     * @return true if the cart is active so the white marble can be converted.
     */
    @Override
    public boolean whiteConversion() {
        return isActive();
    }
}
