package it.polimi.ingsw.model;

public class VictoryPoints {

    private int victoryPointsByVaticanReport;
    private int victoryPointsByFaithTrack;
    private int victoryPointsByCards;

    public VictoryPoints() {
        victoryPointsByFaithTrack = 0;
        victoryPointsByVaticanReport = 0;
        victoryPointsByCards = 0;
    }

    public void increaseVictoryPointsByVaticanReport(int victoryPointsByVaticanReport){
        this.victoryPointsByVaticanReport += victoryPointsByVaticanReport;
    }

    public void setVictoryPointsByFaithTrack(int victoryPointsByFaithTrack) {
        this.victoryPointsByFaithTrack = victoryPointsByFaithTrack;
    }

    public void setVictoryPointsByCards(int victoryPointsByCards) {
        this.victoryPointsByCards = victoryPointsByCards;
    }

    public int sumVictoryPoints(){
        return (victoryPointsByVaticanReport + victoryPointsByFaithTrack + victoryPointsByCards);
    }
}
