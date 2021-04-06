package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.exceptions.WrongDevelopmentCardInsertionException;
import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;

public class DeckTest {

    /**
     * this test tries to add wrong DevelopmentCard
     */
    @Test
    void wrongAdditionDevelopmentCard(){

        Resource r1 = Resource.COIN;
        Cost c1 = new Cost();
        c1.addResource(r1, 1);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 1);

        Deck d = new Deck(Color.BLUE, 1);

        WrongDevelopmentCardInsertionException thrown =
                assertThrows(WrongDevelopmentCardInsertionException.class, () -> d.addDevelopmentCard(developmentCard));
        /*
         developmentCard has level = 2 while deck has level = 1
         */

        String expectedMessage = "Questa carta non può essere inserita in questo mazzetto";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct addition of DevelopmentCard
     */
    @Test
    void correctAdditionDevelopmentCard() throws WrongDevelopmentCardInsertionException, EmptyDevelopmentCardDeckException {

        Resource r1 = Resource.COIN;
        Cost c1 = new Cost();
        c1.addResource(r1, 1);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);

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

        String expectedMessage = "Questo mazzetto è vuoto";
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

        String expectedMessage = "Questo mazzetto è vuoto";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct removing of the first card of Deck
     */
    @Test
    void correctRemoveFirstCard() throws WrongDevelopmentCardInsertionException, EmptyDevelopmentCardDeckException {

        Resource r1 = Resource.COIN;
        Cost c1 = new Cost();
        c1.addResource(r1, 1);
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        DevelopmentCard developmentCard1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);
        DevelopmentCard developmentCard2 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 1);

        Deck d = new Deck(Color.BLUE, 1);

        d.addDevelopmentCard(developmentCard1);
        d.addDevelopmentCard(developmentCard2);

        assertSame(developmentCard1, d.getFirstCard());
        assertNotSame(developmentCard2, d.getFirstCard());

        d.removeDevelopmentCard();

        assertNotSame(developmentCard1, d.getFirstCard());
        assertSame(developmentCard2, d.getFirstCard());

        d.removeDevelopmentCard();
        assertTrue(d.isEmpty());
    }
}
