package it.polimi.ingsw.model;

/**
 * this class represent the resource marble.
 */
public class ResourceMarble extends Marble {

    private Resource resource;

    public ResourceMarble(Resource resource) {
        this.resource = resource;
    }

    /**
     * @param game is Game
     * @return false because marble can't be converted
     * this method increase Warehouse of this.resource. If it isn't possible, increase other player faith points by 1.
     */
    @Override
    public boolean useMarble(LightGame game) {
        if(!(game.increaseWarehouse(resource)))
            game.increaseOneFaithPointOtherPlayers();
        return false;
    }
}
