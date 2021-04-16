package it.polimi.ingsw.model;

/**
 * SimplePlayerBoard is Ludovico PlayerBoard. It can only increase his faith points.
 */
public class SimplePlayerBoard{

    private int faithPoints;

    public int getFaithPoints() {
        return faithPoints;
    }

    public void increaseFaithPoints(int faithPoints) {
        this.faithPoints += faithPoints;
    }
}
