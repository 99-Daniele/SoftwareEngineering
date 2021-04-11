package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    /**
     * test that controls if increase the victory points of the players in the vatican section
     */
    @Test
    void faithTrackMovement() {
        Game game=new MultiPlayerGame(2);
        try {
            game.createPlayer("Daniele");
            game.createPlayer("Matteo");
            game.getPlayer(0).increaseFaithPoints(10);
            game.faithTrackMovement();
            assertEquals(2,game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
            assertEquals(0,game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());
            game.getPlayer(0).increaseFaithPoints(16);
            game.getPlayer(1).increaseFaithPoints(14);
            game.faithTrackMovement();
            assertEquals(5,game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
            assertEquals(3,game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());

        } catch (AlreadyTakenNicknameException e){}
    }

    /**
     * test that controls if it sets the victory points related to the position in the faith track of all the player
     * except the current and if it increases the victory points of all the players in the vatican section
     */
    @Test
    void faithTrackMovementExceptCurrentPlayer() {
        Game game=new MultiPlayerGame(2);
        try {
            game.createPlayer("Daniele");
            game.createPlayer("Matteo");
            game.getPlayer(0).increaseFaithPoints(4);
            game.getPlayer(1).increaseFaithPoints(10);
            game.faithTrackMovementExceptCurrentPlayer();
            assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
            assertEquals(2, game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());
            assertEquals(0,game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
            assertEquals(4,game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());

        }catch (AlreadyTakenNicknameException e){}
    }
}