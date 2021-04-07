package it.polimi.ingsw.model;

/**
 * Section is used to represents an interval
 */
public class Section {
    private final int firstSpace;
    private final int lastSpace;
    private final int victoryPointValue;

    /**
     * constructor of Section
     * @param firstSpace is the value of the first space of the section
     * @param lastSpace is the value of the last space of the section
     * @param victoryPointValue is the value of the victory point related to that section
     */
    public Section(int firstSpace, int lastSpace, int victoryPointValue) {
        this.firstSpace = firstSpace;
        this.lastSpace = lastSpace;
        this.victoryPointValue = victoryPointValue;
    }

    /**
     *
     * @return the value of the section victory point
     */
    public int getVictoryPointValue() {
        return victoryPointValue;
    }

    /**
     *
     * @return the value of the section last space
     */
    public int getLastSpace(){
        return lastSpace;
    }

    /**
     * method that return if the player is in the section
     * @param playerFaithPoints is the value of the player's faith points
     * @return a boolean that is true if the value of the player's faith points is inside the section or false if not
     */
    public boolean isPlayerInSection(int playerFaithPoints){
        if(playerFaithPoints>=firstSpace && playerFaithPoints<=lastSpace)
            return true;
        else
            return false;
    }
}
