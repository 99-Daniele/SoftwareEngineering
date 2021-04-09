package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VictoryPointsTest {

    /**
     * this test calculates the sum of victoryPoints
     */
    @Test
    void sumVictoryPoints(){

        VictoryPoints v = new VictoryPoints();

        v.increaseVictoryPointsByVaticanReport(3);
        v.setVictoryPointsByFaithTrack(13);
        v.increaseVictoryPointsByCards(2);

        assertEquals(18, v.sumVictoryPoints());

        v.increaseVictoryPointsByVaticanReport(2);
        assertEquals(20, v.sumVictoryPoints());

        v.setVictoryPointsByFaithTrack(16);
        assertEquals(23, v.sumVictoryPoints());

        v.increaseVictoryPointsByCards(1);
        assertEquals(24, v.sumVictoryPoints());
    }
}
