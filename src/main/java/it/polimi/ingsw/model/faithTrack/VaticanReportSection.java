package it.polimi.ingsw.model.faithTrack;

/**
 * VaticanReportSection extends Section and represents the section of Vatican report in FaithTrack.
 */
public class VaticanReportSection extends Section {

    /**
     * @param firstSpace is the value of the first space of the papal section.
     * @param lastSpace is the value of the last space of the papal section.
     * @param victoryPointValue is the value of the victory point related to that papal section.
     */
    public VaticanReportSection(int firstSpace, int lastSpace, int victoryPointValue) {
        super(firstSpace, lastSpace, victoryPointValue);
    }

    /**
     * method that return if the player is in the pope space or beyond.
     * @param playerFaithPoints is the value of the player's faith points.
     * @return a boolean that is true if the value of the player's faith points is inside or beyond the pope space and false if not.
     */
    public boolean IsPlayerInPopeSpace(int playerFaithPoints){
        if(playerFaithPoints >= super.getLastSpace())
            return true;
        else
            return false;
    }
}

