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
        ResourceMarble resourceMarble1=new ResourceMarble(Resource.SHIELD);
        Game game=new Game(1) {
            @Override
            public void startGame() {

            }
        };
        game.createPlayer("user");
        assertTrue(resourceMarble1.useMarble(game));
        assertTrue(resourceMarble.useMarble(game));
        assertTrue(resourceMarble.useMarble(game));
        assertEquals(3,game.getCurrentPlayer().sumTotalResource());
    }
}
