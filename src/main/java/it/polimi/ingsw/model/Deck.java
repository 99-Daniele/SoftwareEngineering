package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;
import it.polimi.ingsw.exceptions.WrongDevelopmentCardInsertionException;

import java.util.ArrayList;

public class Deck {

    private final Color color;
    private final int level;
    private ArrayList<DevelopmentCard> developmentCards;
    private boolean empty;

    public Deck(Color color, int level) {
        this.color = color;
        this.level = level;
        developmentCards = new ArrayList<>();
        this.empty = true;
    }

    /**
     * this method prepare deck to start a new game. The four cards in developmentCards are switched randomly
     */
    public void prepareDeck(){}

    public Color getColor() {
        return color;
    }

    public boolean isEmpty() {
        return empty;
    }

    /**
     * @param developmentCard is a developmentCard with same this.color and this.level
     * @throws WrongDevelopmentCardInsertionException if developmentCard has different color or level
     */
    public void addDevelopmentCard(DevelopmentCard developmentCard) throws WrongDevelopmentCardInsertionException {
        if(developmentCard.getColor() != this.color || developmentCard.getLevel() != this.level)
            throw new WrongDevelopmentCardInsertionException();
        this.empty = false;
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
        if(developmentCards.size() == 0)
            this.empty = true;
    }
}
