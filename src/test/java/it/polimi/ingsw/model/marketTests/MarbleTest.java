package it.polimi.ingsw.model.marketTests;

import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.leaderCards.LeaderCard;
import it.polimi.ingsw.model.leaderCards.WhiteConversionCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.RedMarble;
import it.polimi.ingsw.model.market.ResourceMarble;
import it.polimi.ingsw.model.market.WhiteMarble;
import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MarbleTest {

    /**
     * this test verifies the correct overriding of useMarble() method
     */
    @Test
    void correctOverriding() throws AlreadyTakenNicknameException, InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {

        Marble marble1 = new RedMarble();
        Marble marble2 = new ResourceMarble(Resource.SHIELD);
        Marble marble3 = new WhiteMarble();

        Game game = new Game(2);
        game.createPlayer("Alberto");
        game.createPlayer("Giovanni");
        Cost c = new Cost();
        LeaderCard card1 = new WhiteConversionCard(Resource.COIN, c, 1);
        LeaderCard card2 = new WhiteConversionCard(Resource.SERVANT, c, 1);
        game.selectCurrentPlayerLeaderCards(card1, card2);

        assertEquals(0, game.getPlayer(0).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getFaithPoints());
        assertEquals(0, game.getPlayer(0).sumTotalResource());

        assertFalse(marble1.useMarble(game));
        assertEquals(1, game.getPlayer(0).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getFaithPoints());
        assertEquals(0, game.getPlayer(0).sumTotalResource());

        assertFalse(marble2.useMarble(game));
        assertEquals(1, game.getPlayer(0).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getFaithPoints());
        assertEquals(1, game.getPlayer(0).sumTotalResource());

        assertFalse(marble2.useMarble(game));
        assertEquals(1, game.getPlayer(0).getFaithPoints());
        assertEquals(1, game.getPlayer(1).getFaithPoints());
        assertEquals(1, game.getPlayer(0).sumTotalResource());

        assertFalse(marble3.useMarble(game));
        assertEquals(1, game.getPlayer(0).getFaithPoints());
        assertEquals(1, game.getPlayer(1).getFaithPoints());
        assertEquals(1, game.getPlayer(0).sumTotalResource());

        game.getPlayer(0).activateLeaderCard(1);
        assertFalse(marble3.useMarble(game));
        assertEquals(1, game.getPlayer(0).getFaithPoints());
        assertEquals(1, game.getPlayer(1).getFaithPoints());
        assertEquals(2, game.getPlayer(0).sumTotalResource());

        assertFalse(marble3.useMarble(game));
        assertEquals(1, game.getPlayer(0).getFaithPoints());
        assertEquals(1, game.getPlayer(1).getFaithPoints());
        assertEquals(3, game.getPlayer(0).sumTotalResource());

        assertFalse(marble3.useMarble(game));
        assertEquals(1, game.getPlayer(0).getFaithPoints());
        assertEquals(2, game.getPlayer(1).getFaithPoints());
        assertEquals(3, game.getPlayer(0).sumTotalResource());

        game.getPlayer(0).activateLeaderCard(2);
        assertTrue(marble3.useMarble(game));
        assertEquals(1, game.getPlayer(0).getFaithPoints());
        assertEquals(2, game.getPlayer(1).getFaithPoints());
        assertEquals(3, game.getPlayer(0).sumTotalResource());
    }
}
