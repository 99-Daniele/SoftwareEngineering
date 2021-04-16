package it.polimi.ingsw.model.market;

import it.polimi.ingsw.exceptions.AlreadyDiscardLeaderCardException;

import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.games.LightGame;

/**
 * WhiteMarble is the Marble which can be converted into ResourceMable if player has at least one active WhiteConversionCard.
 * otherwise does nothing.
 */
public class WhiteMarble extends Marble {

    /**
     * @param game is Game.
     * @return true if player has to chose which LeaderCard use to convert, otherwise @return false.
     * this method verifies if there are two active WhiteConversionCard and in case, @return true.
     * if there is only one active WhiteConversionCard increase player's warehouse by card resource and in case it
     * isn't possible, increase other players faith points by 1.
     * if there is no active WhiteConversionCard does nothing.
     */
    @Override
    public boolean useMarble(LightGame game) {
        if(game.isActiveWhiteConversionCard(1) && game.isActiveWhiteConversionCard(2)) {
            return true;
        }
        if(!(useWhiteConversionCard(game, 1)))
            useWhiteConversionCard(game, 2);
        return false;
    }

    private boolean useWhiteConversionCard(LightGame game, int chosenLeaderCard){
        if(!(game.isActiveWhiteConversionCard(chosenLeaderCard)))
            return false;
        try {
            LeaderCard card = game.getCurrentPlayerLeaderCard(chosenLeaderCard);
            game.whiteMarbleConversion(card);
        } catch (AlreadyDiscardLeaderCardException e) {
            e.printStackTrace();
        }
        return true;
    }
}
