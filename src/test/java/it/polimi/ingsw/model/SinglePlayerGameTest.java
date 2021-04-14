package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;
import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

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
}