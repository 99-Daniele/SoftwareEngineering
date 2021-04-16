package it.polimi.ingsw.model.marketTests;

import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;

import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.market.*;
import it.polimi.ingsw.model.resourceContainers.Resource;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ResourceMarbleTest {
    /**
     * control if the conversion ResourceMarble-Resource has been done successfully, and if the warehouse has been increased correctly.
     * @throws AlreadyTakenNicknameException when a nickname is already present.
     */
    @Test
    void ControlIncrease() throws AlreadyTakenNicknameException {

        Marble resourceMarble=new ResourceMarble(Resource.COIN);
        Marble resourceMarble2=new ResourceMarble(Resource.SHIELD);

        Game game=new Game(3);
        game.createPlayer("user");
        game.createPlayer("user2");
        game.createPlayer("user3");

        game.getPlayer(1).increaseFaithPoints(3);
        game.getPlayer(2).increaseFaithPoints(3);
        assertEquals(0, game.getPlayer(0).getFaithPoints());
        assertEquals(3, game.getPlayer(1).getFaithPoints());
        assertEquals(3, game.getPlayer(2).getFaithPoints());

        assertFalse(resourceMarble.useMarble(game));
        assertFalse(resourceMarble2.useMarble(game));
        assertFalse(resourceMarble2.useMarble(game));

        assertEquals(3,game.getCurrentPlayer().sumTotalResource());
        assertEquals(3, game.getPlayer(1).getFaithPoints());
        assertEquals(3, game.getPlayer(2).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(0, game.getPlayer(2).getVictoryPoints().getVictoryPointsByFaithTrack());

        assertFalse(resourceMarble2.useMarble(game));
        assertEquals(3,game.getCurrentPlayer().sumTotalResource());
        assertEquals(4, game.getPlayer(1).getFaithPoints());
        assertEquals(4, game.getPlayer(2).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(0, game.getPlayer(2).getVictoryPoints().getVictoryPointsByFaithTrack());
        /*
         this resource can't be increased in current player's warehouse. So other players increase their faith points,
         but not their victory points
         */

        game.faithTrackMovementAllPlayers();
        assertEquals(4, game.getPlayer(1).getFaithPoints());
        assertEquals(4, game.getPlayer(2).getFaithPoints());
        assertEquals(1, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(1, game.getPlayer(2).getVictoryPoints().getVictoryPointsByFaithTrack());
        /*
         only after faithTrackMovementAllPlayer() other player victoryPoints could increased
         */
    }
}
