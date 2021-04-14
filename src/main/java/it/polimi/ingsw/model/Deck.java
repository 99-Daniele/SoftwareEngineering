package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;
import it.polimi.ingsw.exceptions.WrongDevelopmentCardInsertionException;

import java.util.ArrayList;

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
     * this method prepare deck to start a new game. The four cards in developmentCards are shuffled randomly
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
     * @param developmentCard is a developmentCard with same this.color and this.level
     * @throws WrongDevelopmentCardInsertionException if developmentCard has different color or level
     */
    public void addDevelopmentCard(DevelopmentCard developmentCard) throws WrongDevelopmentCardInsertionException {
        if(developmentCard.getColor() != this.color || developmentCard.getLevel() != this.level)
            throw new WrongDevelopmentCardInsertionException();
        developmentCards.add(developmentCard);
    }

    /**
     * @return the first DevelopmentCard of Deck
     * @throws EmptyDevelopmentCardDeckException if Deck is already empty
     */
    public DevelopmentCard getFirstCard() throws EmptyDevelopmentCardDeckException {
        if(isEmpty())
            throw new EmptyDevelopmentCardDeckException();
        return developmentCards.get(0);
    }

    /**
     * this method remove the first DevelopmentCard of Deck
     * @throws EmptyDevelopmentCardDeckException if Deck is already empty
     */
    public void removeDevelopmentCard() throws EmptyDevelopmentCardDeckException{
        if(isEmpty())
            throw new EmptyDevelopmentCardDeckException();
        developmentCards.remove(0);
    }

    /**
     *
     * @return the number of cards in the deck
     */
    public int numberOfCards()
    {
        return developmentCards.size();
    }
}
