package it.polimi.ingsw.modelTests.marketTests;

import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.cards.leaderCards.WhiteConversionCard;
import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.RedMarble;
import it.polimi.ingsw.model.market.ResourceMarble;
import it.polimi.ingsw.model.market.WhiteMarble;

import it.polimi.ingsw.model.resourceContainers.Cost;
import it.polimi.ingsw.model.resourceContainers.Resource;
import org.junit.jupiter.api.Assertions;
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
        LeaderCard card = new WhiteConversionCard(Resource.COIN, c, 1, 0);
        LeaderCard card2 = new WhiteConversionCard(Resource.COIN, c, 1, 0);
        game.selectPlayerLeaderCards(card, card2, 0);

        Assertions.assertEquals(0, game.getPlayer(0).getFaithPoints());
        Assertions.assertEquals(0, game.getPlayer(1).getFaithPoints());
        Assertions.assertEquals(0, game.getPlayer(0).sumTotalResource());

        assertFalse(marble1.useMarble(game));
        Assertions.assertEquals(1, game.getPlayer(0).getFaithPoints());
        Assertions.assertEquals(0, game.getPlayer(1).getFaithPoints());
        Assertions.assertEquals(0, game.getPlayer(0).sumTotalResource());

        assertFalse(marble2.useMarble(game));
        Assertions.assertEquals(1, game.getPlayer(0).getFaithPoints());
        Assertions.assertEquals(0, game.getPlayer(1).getFaithPoints());
        Assertions.assertEquals(1, game.getPlayer(0).sumTotalResource());

        assertFalse(marble2.useMarble(game));
        Assertions.assertEquals(1, game.getPlayer(0).getFaithPoints());
        Assertions.assertEquals(1, game.getPlayer(1).getFaithPoints());
        Assertions.assertEquals(1, game.getPlayer(0).sumTotalResource());

        assertFalse(marble3.useMarble(game));
        Assertions.assertEquals(1, game.getPlayer(0).getFaithPoints());
        Assertions.assertEquals(1, game.getPlayer(1).getFaithPoints());
        Assertions.assertEquals(1, game.getPlayer(0).sumTotalResource());

        game.getPlayer(0).activateLeaderCard(1);
        assertFalse(marble3.useMarble(game));
        Assertions.assertEquals(1, game.getPlayer(0).getFaithPoints());
        Assertions.assertEquals(1, game.getPlayer(1).getFaithPoints());
        Assertions.assertEquals(2, game.getPlayer(0).sumTotalResource());

        assertFalse(marble3.useMarble(game));
        Assertions.assertEquals(1, game.getPlayer(0).getFaithPoints());
        Assertions.assertEquals(1, game.getPlayer(1).getFaithPoints());
        Assertions.assertEquals(3, game.getPlayer(0).sumTotalResource());

        assertFalse(marble3.useMarble(game));
        Assertions.assertEquals(1, game.getPlayer(0).getFaithPoints());
        Assertions.assertEquals(2, game.getPlayer(1).getFaithPoints());
        Assertions.assertEquals(3, game.getPlayer(0).sumTotalResource());

        game.getPlayer(0).activateLeaderCard(2);
        assertTrue(marble3.useMarble(game));
        Assertions.assertEquals(1, game.getPlayer(0).getFaithPoints());
        Assertions.assertEquals(2, game.getPlayer(1).getFaithPoints());
        Assertions.assertEquals(3, game.getPlayer(0).sumTotalResource());
    }
}
