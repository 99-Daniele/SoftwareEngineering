package it.polimi.ingsw.modelTests.gamesTests;

import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.cards.developmentCards.DevelopmentCard;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.games.SinglePlayerGame;
import it.polimi.ingsw.model.cards.developmentCards.Color;
import it.polimi.ingsw.model.resourceContainers.Cost;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SinglePlayerGameTest {

    /**
     * test that controls the case in which if there aren't enough cards in the deck it takes the cards from the deck whit
     * a higher level
     */
    @Test
    void discardDeckDevelopmentCards() throws EmptyDevelopmentCardDeckException {

        SinglePlayerGame singlePlayerGame = new SinglePlayerGame();
        singlePlayerGame.discardDeckDevelopmentCards(Color.GREEN);
        assertEquals(2, singlePlayerGame.getDeck(0, 0).numberOfCards());
        singlePlayerGame.getDeck(0, 0).removeDevelopmentCard();
        singlePlayerGame.discardDeckDevelopmentCards(Color.GREEN);
        assertEquals(0, singlePlayerGame.getDeck(0, 0).numberOfCards());
        assertEquals(3, singlePlayerGame.getDeck(1, 0).numberOfCards());
    }

    /**
     * this test verifies the correct increment of Ludovico in FaithTrack
     */
    @Test
    void correctLudovicoFaithTrackMovement() throws AlreadyTakenNicknameException {

        SinglePlayerGame singlePlayerGame = new SinglePlayerGame();
        singlePlayerGame.createPlayer("p");
        assertEquals(0, singlePlayerGame.getCurrentPlayer().getFaithPoints());
        assertEquals(0, singlePlayerGame.getCurrentPlayer().getVictoryPoints().getVictoryPointsByVaticanReport());

        singlePlayerGame.getCurrentPlayer().increaseFaithPoints(10);
        singlePlayerGame.LorenzoFaithTrackMovement(10);
        assertEquals(2, singlePlayerGame.getCurrentPlayer().getVictoryPoints().getVictoryPointsByVaticanReport());

        singlePlayerGame.LorenzoFaithTrackMovement(13);
        assertEquals(2, singlePlayerGame.getCurrentPlayer().getVictoryPoints().getVictoryPointsByVaticanReport());

        singlePlayerGame.getCurrentPlayer().increaseFaithPoints(10);
        singlePlayerGame.increaseOneFaithPointOtherPlayers();
        assertEquals(6, singlePlayerGame.getCurrentPlayer().getVictoryPoints().getVictoryPointsByVaticanReport());
    }

    /**
     * this test verifies if player wins having 7 developmentCards
     */
    @Test
    void sevenCardsWinner()
            throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, AlreadyTakenNicknameException {

        SinglePlayerGame singlePlayerGame = new SinglePlayerGame();
        singlePlayerGame.createPlayer("Giorgio");

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;

        DevelopmentCard developmentCard1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0, cardID);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard1, 1, 1);

        DevelopmentCard developmentCard2 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0, cardID);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard2, 2, 1);

        DevelopmentCard developmentCard3 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0, cardID);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard3, 3, 1);

        DevelopmentCard developmentCard4 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0, cardID);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard4, 1, 1);

        DevelopmentCard developmentCard5 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0, cardID);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard5, 2, 1);

        DevelopmentCard developmentCard6 = new DevelopmentCard(Color.BLUE, 3, c1, 1, c2, c3, 0, cardID);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard6, 1, 1);

        assertFalse(singlePlayerGame.isEndGame());

        DevelopmentCard developmentCard7 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0, cardID);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard7, 3, 1);
        assertTrue(singlePlayerGame.isEndGame());

        int winner = singlePlayerGame.endGame();
        assertEquals(0, winner);
        assertSame("Giorgio", singlePlayerGame.getPlayer(winner).getNickname());
        assertTrue(singlePlayerGame.getCurrentPlayer().haveSevenDevelopmentCards());
    }

    /**
     * this test verifies if player wins reaching the end of FaithTrack
     */
    @Test
    void endFaithTrackWinner() throws AlreadyTakenNicknameException {

        SinglePlayerGame singlePlayerGame = new SinglePlayerGame();
        singlePlayerGame.createPlayer("Giorgio");

        singlePlayerGame.getCurrentPlayer().increaseFaithPoints(10);
        singlePlayerGame.faithTrackMovement();

        singlePlayerGame.getCurrentPlayer().increaseFaithPoints(8);
        singlePlayerGame.faithTrackMovement();

        singlePlayerGame.getCurrentPlayer().increaseFaithPoints(7);
        singlePlayerGame.faithTrackMovement();

        assertTrue(singlePlayerGame.getFaithTrack().zeroRemainingPope());
        assertTrue(singlePlayerGame.isEndGame());

        int winner = singlePlayerGame.endGame();
        assertEquals(0, winner);
        assertSame("Giorgio", singlePlayerGame.getPlayer(winner).getNickname());
        assertTrue(singlePlayerGame.getFaithTrack().zeroRemainingPope());
    }

    /**
     * this test verifies if player loses if has less than 7 DevelopmentCards and less than 20 faithPoints
     */
    @Test
    void playerLost() throws AlreadyTakenNicknameException {

        SinglePlayerGame singlePlayerGame = new SinglePlayerGame();
        singlePlayerGame.createPlayer("Giorgio");

        int winner = singlePlayerGame.endGame();
        assertEquals(1, winner);
    }

    /**
     * this test verifies the correct next player overriding
     */
    @Test
    void correctNextPlayer(){

        Game game = new SinglePlayerGame();
        assertEquals(0, game.getCurrentPosition());

        game.nextPlayer();
        assertEquals(0, game.getCurrentPosition());
    }
}