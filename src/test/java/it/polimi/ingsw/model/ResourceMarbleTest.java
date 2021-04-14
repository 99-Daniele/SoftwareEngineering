package it.polimi.ingsw.model;
import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;
public class ResourceMarbleTest {
    /**
     * control if the conversion ResourceMarble-Resource has been done successfully, and if the warehouse has been increased correctly.
     * @throws AlreadyTakenNicknameException when a nickname is already present.
     */
    @Test
    void ControlIncrease() throws AlreadyTakenNicknameException {
        ResourceMarble resourceMarble=new ResourceMarble(Resource.COIN);
        ResourceMarble resourceMarble2=new ResourceMarble(Resource.SHIELD);
        Game game=new MultiPlayerGame(3);
        game.createPlayer("user");
        assertTrue(resourceMarble.useMarble(game));
        assertFalse(resourceMarble.useMarble(game));
        assertTrue(resourceMarble2.useMarble(game));
        assertTrue(resourceMarble2.useMarble(game));
        assertEquals(3,game.getCurrentPlayer().sumTotalResource());
    }
}
