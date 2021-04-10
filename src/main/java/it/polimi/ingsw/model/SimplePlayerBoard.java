package it.polimi.ingsw.model;

public class SimplePlayerBoard{

    private int faithPoints;

    public int getFaithPoints() {
        return faithPoints;
    }

    public void increaseFaithPoints(int faithPoints) {
        this.faithPoints += faithPoints;
    }
}
