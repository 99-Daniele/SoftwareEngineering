package it.polimi.ingsw.model.faithTrackTests;


import it.polimi.ingsw.model.faithTrack.FaithTrack;
import it.polimi.ingsw.model.player.VictoryPoints;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;


class FaithTrackTest {

    /**
     * test that check if you receive the right value of victory points when inside a section
     */
    @Test
    void victoryPointsFaithTrack() {
        FaithTrack faithTrack=new FaithTrack();
        VictoryPoints victoryPoints=new VictoryPoints();
        faithTrack.victoryPointsFaithTrack(victoryPoints,10);
        assertEquals(4,victoryPoints.getVictoryPointsByFaithTrack());
        faithTrack.victoryPointsFaithTrack(victoryPoints,26);
        assertEquals(20,victoryPoints.getVictoryPointsByFaithTrack());
    }

    /**
     * test that check if the method increases in the right way the victory points and if it doesn't increase if that
     * pope section is already used
     */
    @Test
    void victoryPointsVaticanReport() {
        FaithTrack faithTrack=new FaithTrack();
        VictoryPoints victoryPoints=new VictoryPoints();
        faithTrack.victoryPointsVaticanReport(victoryPoints,6);
        assertEquals(2,victoryPoints.getVictoryPointsByVaticanReport());
        faithTrack.DecreaseRemainingPope();
        faithTrack.victoryPointsVaticanReport(victoryPoints,6);
        assertEquals(2,victoryPoints.getVictoryPointsByVaticanReport());
        faithTrack.victoryPointsVaticanReport(victoryPoints,18);
        assertEquals(5,victoryPoints.getVictoryPointsByVaticanReport());
        faithTrack.DecreaseRemainingPope();
        faithTrack.DecreaseRemainingPope();
        assertEquals(5, victoryPoints.getVictoryPointsByVaticanReport());
        faithTrack.DecreaseRemainingPope();
        assertEquals(5,victoryPoints.getVictoryPointsByVaticanReport());
    }

    /**
     * test that check if we are in or beyond the pope and if we reach a pope space already reached it controls that
     * it says false as if the number of the remaining popes is zero
     */
    @Test
    void reachPope() {
        FaithTrack faithTrack=new FaithTrack();
        assertFalse(faithTrack.reachPope(7));
        assertTrue(faithTrack.reachPope(8));
        faithTrack.DecreaseRemainingPope();
        assertFalse(faithTrack.reachPope(10));
        assertTrue(faithTrack.reachPope(18));
        faithTrack.DecreaseRemainingPope();
        faithTrack.DecreaseRemainingPope();
        assertFalse(faithTrack.reachPope(20));
    }
}