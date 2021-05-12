package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.games.LightGame;
import it.polimi.ingsw.model.resourceContainers.Resource;

/**
 * ResourceMarble is the Marble which increase player's warehouse by 1 resource.
 */
public class ResourceMarble extends Marble {

    private final Resource resource;

    public ResourceMarble(Resource resource) {
        this.resource = resource;
    }

    /**
     * @param game is Game.
     * @return false because marble can't be converted.
     * this method increase Warehouse of this.resource. If it isn't possible, increase other player faith points by 1.
     */
    @Override
    public boolean useMarble(LightGame game) {
        if(!(game.increaseWarehouse(resource)))
            game.increaseOneFaithPointOtherPlayers();
        return false;
    }

    @Override
    public String toString() {
        switch (resource){
            case COIN:
                return "Y";
            case STONE:
                return "G";
            case SHIELD:
                return "B";
            case SERVANT:
                return "P";
            default:
                return "";
        }
    }
}
