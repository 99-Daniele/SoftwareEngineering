package it.polimi.ingsw.model.faithTrack;

/**
 * Section is used to represents an interval in FaithTrack.
 */
public class Section {
    private final int firstSpace;
    private final int lastSpace;
    private final int victoryPointValue;

    /**
     * @param firstSpace is the value of the first space of the section.
     * @param lastSpace is the value of the last space of the section.
     * @param victoryPointValue is the value of the victory point related to that section.
     */
    public Section(int firstSpace, int lastSpace, int victoryPointValue) {
        this.firstSpace = firstSpace;
        this.lastSpace = lastSpace;
        this.victoryPointValue = victoryPointValue;
    }

    public int getVictoryPointValue() {
        return victoryPointValue;
    }

    public int getLastSpace(){
        return lastSpace;
    }

    /**
     * @param playerFaithPoints is the value of the player's faith points.
     * @return a boolean that is true if the value of the player's faith points is inside the section or false if not.
     *
     * method that return if the player is in the section.
     */
    public boolean isPlayerInSection(int playerFaithPoints){
        return playerFaithPoints >= firstSpace && playerFaithPoints <= lastSpace;
    }
}
