package it.polimi.ingsw.model;

/**
 * FaithTrack represents the faith track and it has 3 attributes:
 * remainingPopes that indicate the remaining popes
 * vaticanReportSections that is an array of VaticanReportSection
 * faithTrackVictoryPointsSection that is an array of Section
 */
public class FaithTrack {
    private int remainingPopes;
    private final VaticanReportSection[] vaticanReportSections;
    private final Section[] faithTrackVictoryPointsSection;

    /**
     * constructor that creates the different section and vatican section and set remainingPopes to 3
     */
    public FaithTrack(){
        remainingPopes=3;
        vaticanReportSections=new VaticanReportSection[3];
        vaticanReportSections[0]=new VaticanReportSection(5,8,2);
        vaticanReportSections[1]=new VaticanReportSection(12,16,3);
        vaticanReportSections[2]=new VaticanReportSection(19,24,4);
        faithTrackVictoryPointsSection=new Section[9];
        faithTrackVictoryPointsSection[0]=new Section(0,2,0);
        faithTrackVictoryPointsSection[1]=new Section(3,5,1);
        faithTrackVictoryPointsSection[2]=new Section(6,8,2);
        faithTrackVictoryPointsSection[3]=new Section(9,11,4);
        faithTrackVictoryPointsSection[4]=new Section(12,14,6);
        faithTrackVictoryPointsSection[5]=new Section(15,17,9);
        faithTrackVictoryPointsSection[6]=new Section(18,20,12);
        faithTrackVictoryPointsSection[7]=new Section(21,23,16);
        faithTrackVictoryPointsSection[8]=new Section(24,24,20);
    }

    /**
     *
     * @return boolean that is true if the number of pope is zero, otherwise false
     */
    public boolean zeroRemainingPope(){
        return remainingPopes == 0;
    }

    /**
     * method that decrease the number of pope by one and cannot go below zero
     */
    public void DecreaseRemainingPope(){
        if(remainingPopes>0)
            remainingPopes--;
        else
            remainingPopes=0;
    }

    /**
     * method that set the value of my victory points related the position in the faith track
     * @param victoryPoints are the victory points of the player
     * @param playerFaithPoints are the player's faith points which describe the position in the faith track
     */
    public void victoryPointsFaithTrack(VictoryPoints victoryPoints, int playerFaithPoints){
        for(Section section:faithTrackVictoryPointsSection)
        {
            if(section.isPlayerInSection(playerFaithPoints))
                victoryPoints.setVictoryPointsByFaithTrack(section.getVictoryPointValue());
        }
        if(playerFaithPoints>24)
            victoryPoints.setVictoryPointsByFaithTrack(20);
    }

    /**
     * method that increase my victory points if i am in or beyond the vatican section and there is the pope
     * @param victoryPoints are the victory points of the player
     * @param playerFaithTrackPoints are the player's faith points which describe the position in the faith track
     */
    public void victoryPointsVaticanReport (VictoryPoints victoryPoints, int playerFaithTrackPoints){
        if(remainingPopes!=0)
            if(vaticanReportSections[3-remainingPopes].isPlayerInSection(playerFaithTrackPoints)
                    ||vaticanReportSections[3-remainingPopes].IsPlayerInPopeSpace(playerFaithTrackPoints))
                victoryPoints.increaseVictoryPointsByVaticanReport(vaticanReportSections[3-remainingPopes].getVictoryPointValue());
    }

    /**
     *
     * @param FaithTrackPoints are the player's faith points which describe the position in the faith track
     * @return true if in or beyond pope space
     */
    public boolean reachPope(int FaithTrackPoints){
        if(remainingPopes!=0)
            return vaticanReportSections[3 - remainingPopes].IsPlayerInPopeSpace(FaithTrackPoints);
        else
            return false;
    }
}
