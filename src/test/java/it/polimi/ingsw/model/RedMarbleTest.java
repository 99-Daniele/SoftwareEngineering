package it.polimi.ingsw.model;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;
import org.junit.jupiter.api.Test;


public class RedMarbleTest {
    /**
     * control what happens when a player only increase his faith points but doesn't activate the method faithTrackMovement,
     * and control what happens when he does.
     * @throws AlreadyTakenNicknameException when nickname is already present.
     */
    @Test
    void Control() throws AlreadyTakenNicknameException {
        RedMarble redMarble=new RedMarble();
        Game game=new MultiPlayerGame(3);
        game.createPlayer("user1");
        game.createPlayer("user2");
        game.createPlayer("user3");

        game.getCurrentPlayer().increaseFaithPoints(5);
        // check if the player doesn't get victory points even though he is in a faithTrackVictoryPoints section.
        assertEquals(0,game.getCurrentPlayer().getVictoryPoints().getVictoryPointsByFaithTrack());
        redMarble.useMarble(game);
        redMarble.useMarble(game);
        // after calling the method useMarble, check if the victoryPointsByFaithTrack are correctly increased.
        assertEquals(2,game.getCurrentPlayer().getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(7,game.getCurrentPlayer().getFaithPoints());

        game.nextPlayer();
        redMarble.useMarble(game);
        redMarble.useMarble(game);
        redMarble.useMarble(game);
        assertEquals(1,game.getCurrentPlayer().getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(3,game.getCurrentPlayer().getFaithPoints());

        game.nextPlayer();
        game.getCurrentPlayer().increaseFaithPoints(8);
        // check if the player doesn't get victoryPointsByVaticanReport even though he is in a PopeSpace section.
        assertEquals(0,game.getCurrentPlayer().getVictoryPoints().getVictoryPointsByVaticanReport());
        redMarble.useMarble(game);
        assertEquals(4,game.getCurrentPlayer().getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(9,game.getCurrentPlayer().getFaithPoints());
        // after calling the method useMarble, check if the victoryPointsByVaticanReport are correctly increased.
        assertEquals(2,game.getCurrentPlayer().getVictoryPoints().getVictoryPointsByVaticanReport());

        //check other players victoryPointsByVaticanReport.
        game.nextPlayer();
        assertEquals(2,game.getCurrentPlayer().getVictoryPoints().getVictoryPointsByVaticanReport());
        game.nextPlayer();
        assertEquals(0,game.getCurrentPlayer().getVictoryPoints().getVictoryPointsByVaticanReport());
    }
}
