package it.polimi.ingsw.model.player;

/**
 * VictoryPoints contains the 3 different types of victory points.
 * victoryPointsByVaticanReport represents the number of victory points related to the vatican sections.
 * victoryPointsByFaithTrack represents the number of victory points related to the position in the faith track.
 * victoryPointsByCards represents the number of victory points related to cards.
 */
public class VictoryPoints {

    private int victoryPointsByVaticanReport;
    private int victoryPointsByFaithTrack;
    private int victoryPointsByCards;

    /**
     * method that increase the number of victory points related to the vatican sections.
     * @param victoryPointsByVaticanReport is the victory points related to the vatican section.
     */
    public void increaseVictoryPointsByVaticanReport(int victoryPointsByVaticanReport){
        this.victoryPointsByVaticanReport += victoryPointsByVaticanReport;
    }

    /**
     * method that set the value of victory points related to the faith track.
     * @param victoryPointsByFaithTrack is the victory points related to the section in which it is located.
     */
    public void setVictoryPointsByFaithTrack(int victoryPointsByFaithTrack){
        this.victoryPointsByFaithTrack = victoryPointsByFaithTrack;
    }

    /**
     * method that increase the value of victory points related to player's owned DevelopmentCards.
     * @param victoryPointsByCards is the victory points related to all the cards that the player has.
     */
    public void increaseVictoryPointsByCards(int victoryPointsByCards){
        this.victoryPointsByCards += victoryPointsByCards;
    }

    /**
     * method that sum all the different type of victory points.
     * @return the sum of all the victory points.
     */
    public int sumVictoryPoints(){
        return (victoryPointsByCards + victoryPointsByVaticanReport + victoryPointsByFaithTrack);
    }

    public int getVictoryPointsByVaticanReport() {
        return victoryPointsByVaticanReport;
    }

    public int getVictoryPointsByFaithTrack() {
        return victoryPointsByFaithTrack;
    }

    public int getVictoryPointsByCards() {
        return victoryPointsByCards;
    }
}