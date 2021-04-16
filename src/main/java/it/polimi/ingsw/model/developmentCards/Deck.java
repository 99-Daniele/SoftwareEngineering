package it.polimi.ingsw.model.developmentCards;

import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;

import java.util.ArrayList;

/**
 * Deck is one of 12 Decks in Game. It contains at the beginning 4 DevelopmentCards, all with same color and level.
 */
public class Deck {

    private final Color color;
    private final int level;
    private ArrayList<DevelopmentCard> developmentCards;

    public Deck(Color color, int level) {
        this.color = color;
        this.level = level;
        developmentCards = new ArrayList<>();
    }

    /**
     * this method prepare deck to start a new game. The four cards in developmentCards are shuffled randomly.
     */
    public void prepareDeck(){
        ArrayList<DevelopmentCard> newDevelopmentCards = new ArrayList<>();
        for(int count = 4; count > 0; count--){
            int i = (int) (Math.random() * count);
            DevelopmentCard card = developmentCards.remove(i);
            newDevelopmentCards.add(card);
        }
        developmentCards = newDevelopmentCards;
    }

    public Color getColor() {
        return color;
    }

    public boolean isEmpty() {
        return developmentCards.size() == 0;
    }

    /**
     * @param developmentCard is a developmentCard with same this.color and this.level.
     */
    public void addDevelopmentCard(DevelopmentCard developmentCard) {
        developmentCards.add(developmentCard);
    }

    /**
     * @return the first DevelopmentCard of Deck.
     * @throws EmptyDevelopmentCardDeckException if Deck is already empty.
     */
    public DevelopmentCard getFirstCard() throws EmptyDevelopmentCardDeckException {
        if(isEmpty())
            throw new EmptyDevelopmentCardDeckException();
        return developmentCards.get(0);
    }

    /**
     * this method remove the first DevelopmentCard of Deck.
     * @throws EmptyDevelopmentCardDeckException if Deck is already empty.
     */
    public void removeDevelopmentCard() throws EmptyDevelopmentCardDeckException{
        if(isEmpty())
            throw new EmptyDevelopmentCardDeckException();
        developmentCards.remove(0);
    }

    /**
     * @return the number of cards in the deck.
     */
    public int numberOfCards() {
        return developmentCards.size();
    }
}
