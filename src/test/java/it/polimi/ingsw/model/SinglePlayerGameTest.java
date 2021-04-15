package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;
import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;
import it.polimi.ingsw.exceptions.ImpossibleDevelopmentCardAdditionException;
import it.polimi.ingsw.exceptions.InsufficientResourceException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SinglePlayerGameTest {

    /**
     * test that controls the case in which if there aren't enough cards in the deck it takes the cards from the deck whit
     * a higher level
     */
    @Test
    void discardDeckDevelopmentCards() throws EmptyDevelopmentCardDeckException {

            SinglePlayerGame singlePlayerGame=new SinglePlayerGame();
            singlePlayerGame.discardDeckDevelopmentCards(Color.GREEN);
            assertEquals(2,singlePlayerGame.getDeck(0,0).numberOfCards());
            singlePlayerGame.getDeck(0,0).removeDevelopmentCard();
            singlePlayerGame.discardDeckDevelopmentCards(Color.GREEN);
            assertEquals(0,singlePlayerGame.getDeck(0,0).numberOfCards());
            assertEquals(3,singlePlayerGame.getDeck(1,0).numberOfCards());
    }

    /**
     * this test verifies the correct increment of Ludovico in FaithTrack
     */
    @Test
    void correctLudovicoFaithTrackMovement() throws AlreadyTakenNicknameException {

        SinglePlayerGame singlePlayerGame=new SinglePlayerGame();
        singlePlayerGame.createPlayer("p");
        assertEquals(0, singlePlayerGame.getCurrentPlayer().getFaithPoints());
        assertEquals(0, singlePlayerGame.getCurrentPlayer().getVictoryPoints().getVictoryPointsByVaticanReport());

        singlePlayerGame.LudovicoFaithTrackMovement(10);
        assertEquals(0, singlePlayerGame.getCurrentPlayer().getVictoryPoints().getVictoryPointsByVaticanReport());

        singlePlayerGame.getCurrentPlayer().increaseFaithPoints(13);
        singlePlayerGame.LudovicoFaithTrackMovement(10);
        assertEquals(3, singlePlayerGame.getCurrentPlayer().getVictoryPoints().getVictoryPointsByVaticanReport());
    }

    /**
     * this test verifies if player wins having 7 developmentCards
     */
    @Test
    void sevenCardsWinner()
            throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, AlreadyTakenNicknameException {

        Game singlePlayerGame = new SinglePlayerGame();
        singlePlayerGame.createPlayer("Giorgio");

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();

        DevelopmentCard developmentCard1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard1, 1, 1);

        DevelopmentCard developmentCard2 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard2, 2, 1);

        DevelopmentCard developmentCard3 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard3, 3, 1);

        DevelopmentCard developmentCard4 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard4, 1, 1);

        DevelopmentCard developmentCard5 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard5, 2, 1);

        DevelopmentCard developmentCard6 = new DevelopmentCard(Color.BLUE, 3, c1, 1, c2, c3, 0);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard6, 1, 1);

        DevelopmentCard developmentCard7 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0);
        singlePlayerGame.getCurrentPlayer().buyDevelopmentCard(developmentCard7, 3, 1);

        PlayerBoard winner = singlePlayerGame.endGame();
        assertNotNull(winner);
        assertSame("Giorgio", winner.getNickname());
        assertTrue(singlePlayerGame.getCurrentPlayer().haveSevenDevelopmentCards());
    }

    /**
     * this test verifies if player wins reaching the end of FaithTrack
     */
    @Test
    void endFaithTrackWinner() throws AlreadyTakenNicknameException {

        Game singlePlayerGame = new SinglePlayerGame();
        singlePlayerGame.createPlayer("Giorgio");
        singlePlayerGame.getCurrentPlayer().increaseFaithPoints(21);

        PlayerBoard winner = singlePlayerGame.endGame();
        assertNotNull(winner);
        assertSame("Giorgio", winner.getNickname());
        assertTrue(singlePlayerGame.getCurrentPlayer().getFaithPoints() >= 20);
    }

    /**
     * this test verifies if player loses if has less than 7 DevelopmentCsrds and less than 20 faithPoints
     */
    @Test
    void playerLost() throws AlreadyTakenNicknameException {

        Game singlePlayerGame = new SinglePlayerGame();
        singlePlayerGame.createPlayer("Giorgio");

        PlayerBoard winner = singlePlayerGame.endGame();
        assertNull(winner);
    }
}