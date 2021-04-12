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
     * @return true if the marble has to be discarded, otherwise @return false
     * this method increase Warehouse of this.resource.
     */
    @Override
    public boolean useMarble(Game game) {
        return game.getCurrentPlayer().increaseWarehouse(resource);
    }
}
