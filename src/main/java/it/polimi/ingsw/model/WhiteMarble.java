package it.polimi.ingsw.model;

/**
 * this class represent the white marble.
 */
public class WhiteMarble extends Marble {

    /**
     * @param game is Game
     * @return true if the marble has to be discarded, otherwise @return false
     * this method verifies if there are any active WhiteConversionCard and in case, there are two ask player which one
     * to use. Then increase Warehouse.
     * if there is no active WhiteConversionCard does nothing.
     */
    @Override
    public boolean useMarble(Game game) {
        Resource r1 = game.getCurrentPlayer().whiteConversion(0);
        Resource r2 = game.getCurrentPlayer().whiteConversion(1);
        if(r1 != Resource.WHITE && r2 != Resource.WHITE) {
            Resource chosen = game.askWhiteMarbleResourceConversionToPlayer(r1, r2);
            return game.getCurrentPlayer().increaseWarehouse(chosen);
        }
        else if(r1 != Resource.WHITE)
            return game.getCurrentPlayer().increaseWarehouse(r1);
        else if(r2 != Resource.WHITE)
            return game.getCurrentPlayer().increaseWarehouse(r2);
        else
            return false;
    }
}
