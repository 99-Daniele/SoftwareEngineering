package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.games.LightGame;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.view.ColorAnsi;

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
    public String toString(){
        switch (resource){
            case COIN:
                return "YELLOW";
            case STONE:
                return "GREY";
            case SHIELD:
                return "BLUE";
            case SERVANT:
                return "PURPLE";
        }
        return null;
    }

    @Override
    public String toStringAbb() {
        switch (resource){
            case COIN:
                return "Y";
            case STONE:
                return "G";
            case SHIELD:
                return "B";
            case SERVANT:
                return "P";
        }
        return null;
    }

    @Override
    public String colorString() {
        switch (resource){
            case COIN:
                return ColorAnsi.ANSI_YELLOW.escape() + "YELLOW" + ColorAnsi.RESET;
            case STONE:
                return ColorAnsi.ANSI_WHITE.escape() + "GREY" + ColorAnsi.RESET;
            case SHIELD:
                return ColorAnsi.ANSI_CYAN.escape() + "BLUE" + ColorAnsi.RESET;
            case SERVANT:
                return ColorAnsi.ANSI_PURPLE.escape() + "PURPLE" + ColorAnsi.RESET;
        }
        return null;
    }
}
