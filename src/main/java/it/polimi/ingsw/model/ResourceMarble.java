package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Marble;
import it.polimi.ingsw.model.Resource;


/**
 * this class represent the resource marble.
 */
public class ResourceMarble extends Marble {
    private Resource resourceType;

    public ResourceMarble(Resource resourceType) {
        this.resourceType = resourceType;
    }

    @Override
    public void useMarble(Game game) {
        super.useMarble(game);
    }
}
