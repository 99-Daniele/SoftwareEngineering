package it.polimi.ingsw.modelTests.developmentCardsTests;

import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;

import it.polimi.ingsw.model.cards.developmentCards.*;
import it.polimi.ingsw.model.cards.developmentCards.DevelopmentCard;
import it.polimi.ingsw.model.resourceContainers.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    
    /**
     * this test verifies the correct addition of DevelopmentCard
     */
    @Test
    void correctAdditionDevelopmentCard() throws EmptyDevelopmentCardDeckException {

        Resource r1 = Resource.COIN;
        Cost c1 = new Cost();
        c1.addResource(r1, 1);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1, cardID);

        Deck d = new Deck(Color.BLUE, 1);

        d.addDevelopmentCard(developmentCard);
        assertSame(developmentCard, d.getFirstCard());
    }

    /**
     * this test tries to get the first card of an empty Deck
     */
    @Test
    void incorrectGetFirstCard(){

        Deck d = new Deck(Color.BLUE, 1);

        EmptyDevelopmentCardDeckException thrown =
                assertThrows(EmptyDevelopmentCardDeckException.class, () -> d.getFirstCard());
        /*
         developmentCard has level = 2 while deck has level = 1
         */

        String expectedMessage = "You have chosen an empty deck";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to remove the first card of an empty Deck
     */
    @Test
    void incorrectRemoveFirstCard(){

        Deck d = new Deck(Color.BLUE, 1);

        EmptyDevelopmentCardDeckException thrown =
                assertThrows(EmptyDevelopmentCardDeckException.class, () -> d.removeDevelopmentCard());
        /*
         developmentCard has level = 2 while deck has level = 1
         */

        String expectedMessage = "You have chosen an empty deck";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct removing of the first card of Deck
     */
    @Test
    void correctRemoveFirstCard() throws EmptyDevelopmentCardDeckException {

        Resource r1 = Resource.COIN;
        Cost c1 = new Cost();
        c1.addResource(r1, 1);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        DevelopmentCard developmentCard1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1, cardID);
        DevelopmentCard developmentCard2 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1, cardID);

        Deck d = new Deck(Color.BLUE, 1);

        d.addDevelopmentCard(developmentCard1);
        d.addDevelopmentCard(developmentCard2);

        assertSame(developmentCard1, d.getFirstCard());
        assertNotSame(developmentCard2, d.getFirstCard());
        assertEquals(2, d.numberOfCards());

        d.removeDevelopmentCard();

        assertNotSame(developmentCard1, d.getFirstCard());
        assertSame(developmentCard2, d.getFirstCard());
        assertEquals(1, d.numberOfCards());

        d.removeDevelopmentCard();
        assertTrue(d.isEmpty());
        assertEquals(0, d.numberOfCards());
    }
}
