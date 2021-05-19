package it.polimi.ingsw.view.model_view;

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
        if(row == 0){
            firstLevelCards.set(column, cardID);
        }
        else if(row == 1){
            secondLevelCards.set(column, cardID);
        }
        else{
            thirdLevelCards.set(column, cardID);
        }
    }

    public int[] getRow_Column(int cardID){
        final int[] card_coordinates = new int[2];
        for(int i = 0; i < 4; i++){
            if(firstLevelCards.get(i) == cardID) {
                card_coordinates[0] = 0;
                card_coordinates[1] = i;
                return card_coordinates;
            }
        }
        for(int i = 0; i < 4; i++){
            if(firstLevelCards.get(i) == cardID) {
                card_coordinates[0] = 1;
                card_coordinates[1] = i;
                return card_coordinates;
            }
        }
        for(int i = 0; i < 4; i++){
            if(firstLevelCards.get(i) == cardID) {
                card_coordinates[0] = 2;
                card_coordinates[1] = i;
                return card_coordinates;
            }
        }
        card_coordinates[0] = -1;
        card_coordinates[1] = -1;
        return card_coordinates;
    }

    public void print(){};
}
