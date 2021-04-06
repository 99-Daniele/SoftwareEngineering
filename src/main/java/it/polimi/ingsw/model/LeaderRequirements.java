package it.polimi.ingsw.model;

import java.util.ArrayList;

/**
 * LeaderRequirements contains all CardRequirement requested, each one for any different type of color of cards
 */
public class LeaderRequirements {

    private ArrayList<CardRequirement> cardRequirements = new ArrayList<>();

    public boolean enoughCardRequirements(LeaderRequirements leaderRequirements){
        return false;
    }

    public int getNumOfCards(Color color){
        return 0;
    }

    public int getMaxLevelOfCards(Color color){
        return 0;
    }

    public void addCardRequirement(DevelopmentCard card){}
}
