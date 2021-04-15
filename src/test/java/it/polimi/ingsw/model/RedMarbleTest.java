package it.polimi.ingsw.model;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;
import org.junit.jupiter.api.Test;


public class RedMarbleTest {
    /**
     * control what happens when a player only increase his faith points by using a redMarble
     * @throws AlreadyTakenNicknameException when nickname is already present.
     */
    @Test
    void Control() throws AlreadyTakenNicknameException {

        Marble redMarble=new RedMarble();
        Game game=new Game(3);
        game.createPlayer("user1");

        game.getCurrentPlayer().increaseFaithPoints(5);
        // check if the player doesn't get victory points even though he is in a faithTrackVictoryPoints section.
        assertEquals(5,game.getCurrentPlayer().getFaithPoints());
        assertEquals(0,game.getCurrentPlayer().getVictoryPoints().getVictoryPointsByFaithTrack());

        redMarble.useMarble(game);
        redMarble.useMarble(game);
        // after calling the method useMarble, victoryPointsByFaithTrack remain the same, but faithPoints increase.
        assertEquals(7,game.getCurrentPlayer().getFaithPoints());
        assertEquals(0,game.getCurrentPlayer().getVictoryPoints().getVictoryPointsByFaithTrack());

        game.faithTrackMovement();
        // after calling the method faithTrackMovement, victoryPointsByFaithTrack increase.
        assertEquals(7,game.getCurrentPlayer().getFaithPoints());
        assertEquals(2,game.getCurrentPlayer().getVictoryPoints().getVictoryPointsByFaithTrack());
    }
}
