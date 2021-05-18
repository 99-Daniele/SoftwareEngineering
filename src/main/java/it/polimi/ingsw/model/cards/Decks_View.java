package it.polimi.ingsw.model.cards;

import java.util.ArrayList;

public class Decks_View {

    private ArrayList<Integer> firstLevelCards;
    private ArrayList<Integer> secondLevelCards;
    private ArrayList<Integer> thirdLevelCards;

    public Decks_View(ArrayList<Integer> initialCards){
        firstLevelCards = (ArrayList<Integer>) initialCards.subList(0, 4);
        secondLevelCards = (ArrayList<Integer>) initialCards.subList(4, 8);
        thirdLevelCards = (ArrayList<Integer>) initialCards.subList(8, 12);
    }

    public void replaceCard(int row, int column, int cardID){
        Integer oldValue;
        if(row == 1){
            oldValue = firstLevelCards.get(column);
        }
        else if(row == 2){
            oldValue = secondLevelCards.get(column);
        }
        else{
            oldValue = thirdLevelCards.get(column);
        }
        oldValue = cardID;
    }
}
