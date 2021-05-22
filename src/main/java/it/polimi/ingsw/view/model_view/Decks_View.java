package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.parser.CardMap;

import java.util.*;

public class Decks_View {

    private List<Integer> firstLevelCards;
    private List<Integer> secondLevelCards;
    private List<Integer> thirdLevelCards;

    public Decks_View(ArrayList<Integer> initialCards){
        firstLevelCards =  initialCards.subList(0, 4);
        secondLevelCards =  initialCards.subList(4, 8);
        thirdLevelCards =  initialCards.subList(8, 12);
    }

    public ArrayList<Integer> getDevelopmentCards(){
        ArrayList<Integer> developmentCards = new ArrayList<>();
        developmentCards.addAll(firstLevelCards);
        developmentCards.addAll(secondLevelCards);
        developmentCards.addAll(thirdLevelCards);
        return developmentCards;
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

    public int[] get_Row_Column(int cardID){
        final int[] card_coordinates = new int[2];
        for(int i = 0; i < 4; i++){
            if(firstLevelCards.get(i) == cardID) {
                card_coordinates[0] = 1;
                card_coordinates[1] = i+1;
                return card_coordinates;
            }
        }
        for(int i = 0; i < 4; i++){
            if(secondLevelCards.get(i) == cardID) {
                card_coordinates[0] = 2;
                card_coordinates[1] = i+1;
                return card_coordinates;
            }
        }
        for(int i = 0; i < 4; i++){
            if(thirdLevelCards.get(i) == cardID) {
                card_coordinates[0] = 3;
                card_coordinates[1] = i+1;
                return card_coordinates;
            }
        }
        card_coordinates[0] = -1;
        card_coordinates[1] = -1;
        return card_coordinates;
    }

    public void print() {
        System.out.println("\nDEVELOPMENT_CARDS:");
        for (int j = 0; j < 4; j++) {
            System.out.println("Deck " + 1 + " - " + (j + 1) + ":");
            if (firstLevelCards.get(j) == -1)
                System.out.println("empty");
            else
                CardMap.getCard(firstLevelCards.get(j)).print();
        }
        for (int j = 0; j < 4; j++) {
            System.out.println("Deck " + 2 + " - " + (j + 1));
            if (secondLevelCards.get(j) == -1)
                System.out.println("empty");
            else
                CardMap.getCard(secondLevelCards.get(j)).print();
        }
        for (int j = 0; j < 4; j++) {
            System.out.println("Deck " + 3 + " - " + (j + 1));
            if (thirdLevelCards.get(j) == -1)
                System.out.println("empty");
            else
                CardMap.getCard(thirdLevelCards.get(j)).print();
        }

    }
}
