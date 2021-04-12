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
     * @return 1 if the marble has to be discarded, otherwise @return 0
     * this method increase Warehouse of this.resource.
     */
    @Override
    public int useMarble(Game game) {
        return game.increaseWarehouse(resource);
    }
}
