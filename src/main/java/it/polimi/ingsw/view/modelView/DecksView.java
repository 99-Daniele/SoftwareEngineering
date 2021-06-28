package it.polimi.ingsw.view.modelView;

import it.polimi.ingsw.parser.CardMapCLI;

import java.util.*;

/**
 * DecksView is the View version of Decks Model class.
 * In this case all DevelopmentCards are coded by Integer and are saved only the 16 or less buyable cards, divided by level.
 */
public class DecksView {

    private final List<Integer> firstLevelCards;
    private final List<Integer> secondLevelCards;
    private final List<Integer> thirdLevelCards;

    public DecksView(ArrayList<Integer> initialCards){
        firstLevelCards = initialCards.subList(0, 4);
        secondLevelCards = initialCards.subList(4, 8);
        thirdLevelCards = initialCards.subList(8, 12);
    }

    /**
     * @return a list of all DevelopmentCards.
     */
    public ArrayList<Integer> getDevelopmentCards(){
        ArrayList<Integer> developmentCards = new ArrayList<>();
        developmentCards.addAll(firstLevelCards);
        developmentCards.addAll(secondLevelCards);
        developmentCards.addAll(thirdLevelCards);
        return developmentCards;
    }

    /**
     * @param row is deck row.
     * @param column is deck column.
     * @param cardID is new DevelopmentCard's cardID.
     *
     * if cardID = -1 the deck is empty.
     */
    public void replaceCard(int row, int column, int cardID){
        if(row == 0)
            firstLevelCards.set(column, cardID);
        else if (row == 1)
            secondLevelCards.set(column, cardID);
        else
            thirdLevelCards.set(column, cardID);
    }

    /**
     * @param cardID is DevelopmentCard's cardID.
     * @return the row and the column of the selected DevelopmentCard.
     */
    public int[] getRowColumn(int cardID){
        final int[] cardCoordinates = new int[2];
        for(int i = 0; i < 4; i++){
            if(firstLevelCards.get(i) == cardID) {
                cardCoordinates[0] = 1;
                cardCoordinates[1] = i+1;
                return cardCoordinates;
            }
        }
        for(int i = 0; i < 4; i++){
            if(secondLevelCards.get(i) == cardID) {
                cardCoordinates[0] = 2;
                cardCoordinates[1] = i+1;
                return cardCoordinates;
            }
        }
        for(int i = 0; i < 4; i++){
            if(thirdLevelCards.get(i) == cardID) {
                cardCoordinates[0] = 3;
                cardCoordinates[1] = i+1;
                return cardCoordinates;
            }
        }
        cardCoordinates[0] = -1;
        cardCoordinates[1] = -1;
        return cardCoordinates;
    }

    /**
     * CLI print of all DevelopmentCards.
     */
    public void print() {
        System.out.println("\nDEVELOPMENT_CARDS:");
        for (int j = 0; j < 4; j++) {
            System.out.println("Deck " + 1 + " - " + (j + 1) + ":");
            if (firstLevelCards.get(j) == -1)
                System.out.println("empty");
            else
                CardMapCLI.getCard(firstLevelCards.get(j)).print();
        }
        for (int j = 0; j < 4; j++) {
            System.out.println("Deck " + 2 + " - " + (j + 1));
            if (secondLevelCards.get(j) == -1)
                System.out.println("empty");
            else
                CardMapCLI.getCard(secondLevelCards.get(j)).print();
        }
        for (int j = 0; j < 4; j++) {
            System.out.println("Deck " + 3 + " - " + (j + 1));
            if (thirdLevelCards.get(j) == -1)
                System.out.println("empty");
            else
                CardMapCLI.getCard(thirdLevelCards.get(j)).print();
        }
    }
}
