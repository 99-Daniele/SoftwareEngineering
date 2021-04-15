package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyDiscardLeaderCardException;

/**
 * this class represent the white marble.
 */
public class WhiteMarble extends Marble {

    /**
     * @param game is Game
     * @return true if player has to chose which LeaderCard use to convert, otherwise @return false
     * this method verifies if there are two active WhiteConversionCard and in case, @return true.
     * if there is only one active WhiteConversionCard increase player's warehouse by card resource and in case it
     * isn't possible, increase other players faith points by 1.
     * if there is no active WhiteConversionCard does nothing.
     */
    @Override
    public boolean useMarble(LightGame game) {
        if(game.isActiveWhiteConversionCard(0) && game.isActiveWhiteConversionCard(1)) {
            return true;
        }
        else if(game.isActiveWhiteConversionCard(0)) {
            try {
                LeaderCard card = game.getCurrentPlayerLeaderCard(0);
                game.whiteMarbleConversion(card);
            } catch (AlreadyDiscardLeaderCardException e) {
                e.printStackTrace();
            }
        }
        else if(game.isActiveWhiteConversionCard(1)){
            try {
                LeaderCard card = game.getCurrentPlayerLeaderCard(1);
                game.whiteMarbleConversion(card);
            } catch (AlreadyDiscardLeaderCardException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
