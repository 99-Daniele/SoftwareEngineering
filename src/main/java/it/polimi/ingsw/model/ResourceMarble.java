package it.polimi.ingsw.model;

/**
 * this class represent the resource marble.
 */
public class ResourceMarble extends Marble {

    private Resource resource;

    public ResourceMarble(Resource resource) {
        this.resource = resource;
    }

    @Override
    public void useMarble(Game game) {
        super.useMarble(game);
    }
}
