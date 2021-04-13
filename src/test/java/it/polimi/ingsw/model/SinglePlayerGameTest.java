package it.polimi.ingsw.model;

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
    void discardDeckDevelopmentCards() {
        try {
            SinglePlayerGame singlePlayerGame=new SinglePlayerGame();
            singlePlayerGame.discardDeckDevelopmentCards(Color.GREEN);
            assertEquals(2,singlePlayerGame.getDeck(0,0).numberOfCards());
            singlePlayerGame.getDeck(0,0).removeDevelopmentCard();
            singlePlayerGame.discardDeckDevelopmentCards(Color.GREEN);
            assertEquals(0,singlePlayerGame.getDeck(0,0).numberOfCards());
            assertEquals(3,singlePlayerGame.getDeck(1,0).numberOfCards());
        }catch (EmptyDevelopmentCardDeckException e){}
    }
}