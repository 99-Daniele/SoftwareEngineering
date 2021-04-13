package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Color c1 = Color.GREEN;
    Color c2 = Color.PURPLE;
    Color c3 = Color.BLUE;
    Color c4 = Color.YELLOW;

    /**
     * this test verifies the correct creations of decks.
     */
    @Test
    void createDecks()
            throws EmptyDevelopmentCardDeckException{

        Game game = new SinglePlayerGame();
        assertFalse(game.zeroRemainingColorCards());
        assertEquals(1, game.getColorDeck(c1).getFirstCard().getLevel());
        assertEquals(1, game.getColorDeck(c2).getFirstCard().getLevel());
        assertEquals(1, game.getColorDeck(c3).getFirstCard().getLevel());
        assertEquals(1, game.getColorDeck(c4).getFirstCard().getLevel());
        assertSame(Color.GREEN, game.getColorDeck(c1).getFirstCard().getColor());
        assertSame(Color.PURPLE, game.getColorDeck(c2).getFirstCard().getColor());
        assertSame(Color.BLUE, game.getColorDeck(c3).getFirstCard().getColor());
        assertSame(Color.YELLOW, game.getColorDeck(c4).getFirstCard().getColor());
    }

    /**
     * this test verifies the correct obtaining of casual leaderCard
     */
    @Test
    void getCasualLeaderCards(){

        Game game = new SinglePlayerGame();
        LeaderCard card1 = game.getCasualLeaderCard();
        LeaderCard card2 = game.getCasualLeaderCard();
        LeaderCard card3 = game.getCasualLeaderCard();
        assertNotSame(card1, card2);
        assertNotSame(card1, card3);
        assertNotSame(card2, card3);
    }

    /**
     * test that controls if increase the victory points of the players in the vatican section
     */
    @Test
    void faithTrackMovement() throws AlreadyTakenNicknameException {

        Game game = new MultiPlayerGame(2);
        game.createPlayer("Daniele");
        game.createPlayer("Matteo");
        game.getPlayer(0).increaseFaithPoints(10);
        game.faithTrackMovement();
        assertEquals(2, game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());
        game.getPlayer(0).increaseFaithPoints(16);
        game.getPlayer(1).increaseFaithPoints(14);
        game.faithTrackMovement();
        assertEquals(5, game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(3, game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());
    }

    /**
     * test that controls if it sets the victory points related to the position in the faith track of all the player
     * except the current and if it increases the victory points of all the players in the vatican section
     */
    @Test
    void faithTrackMovementExceptCurrentPlayer() throws AlreadyTakenNicknameException {

        Game game = new MultiPlayerGame(2);
        game.createPlayer("Daniele");
        game.createPlayer("Matteo");
        game.getPlayer(0).increaseFaithPoints(4);
        game.getPlayer(1).increaseFaithPoints(10);
        game.faithTrackMovementExceptCurrentPlayer();
        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(2, game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(4, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
    }
}