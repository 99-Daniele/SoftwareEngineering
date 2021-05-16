package it.polimi.ingsw.model.marketTests;

import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.leaderCards.*;
import it.polimi.ingsw.model.market.*;
import it.polimi.ingsw.model.resourceContainers.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WhiteMarbleTest {

    /**
     * this test verifies the correct operation of useMarble() if there are 0, 1 or 2 active WhiteConversionCard
     */
    @Test
    void useWhiteMarble()
            throws AlreadyTakenNicknameException, InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {

        Marble whiteMarble = new WhiteMarble();
        Game game = new Game(2);
        game.createPlayer("Alberto");
        game.createPlayer("Giovanni");
        Cost c = new Cost();
        LeaderCard card1 = new WhiteConversionCard(Resource.COIN, c, 1, 0);
        LeaderCard card2 = new WhiteConversionCard(Resource.SERVANT, c, 1, 0);
        game.selectPlayerLeaderCards(card1, card2, 0);
        game.getPlayer(1).increaseFaithPoints(2);

        assertEquals(0, game.getPlayer(0).getFaithPoints());
        assertEquals(2, game.getPlayer(1).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(0, game.getPlayer(0).sumTotalResource());

        assertFalse(whiteMarble.useMarble(game));
        assertEquals(0, game.getPlayer(0).sumTotalResource());
        assertEquals(2, game.getPlayer(1).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        /*
         nothing change because there aren't any active WhiteConversionCard
         */

        game.activateLeaderCard(1);
        assertFalse(whiteMarble.useMarble(game));
        assertEquals(1, game.getPlayer(0).sumTotalResource());
        assertEquals(2, game.getPlayer(1).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        /*
         current player num of resources increase by 1 Resource.COIN
         */

        assertFalse(whiteMarble.useMarble(game));
        assertEquals(1, game.getPlayer(0).sumTotalResource());
        assertEquals(3, game.getPlayer(1).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        /*
         current player num of resources remains 1 because resource can't be added to his warehouse. In the meanwhile
         other player faith points increase by 1 because current player has discarded a marble, but his victory points
         remains the same.
         */

        game.activateLeaderCard(2);
        assertTrue(whiteMarble.useMarble(game));
        assertEquals(1, game.getPlayer(0).sumTotalResource());
        assertEquals(3, game.getPlayer(1).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        /*
         this time useMarble @return true because there are two active WhiteConversionCard.
         nothing has changed
         */

        game.faithTrackMovementAllPlayers();
        assertEquals(3, game.getPlayer(1).getFaithPoints());
        assertEquals(1, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        /*
         only after faithTrackMovementAllPlayer() other player victoryPoints could increased
         */
    }
}
