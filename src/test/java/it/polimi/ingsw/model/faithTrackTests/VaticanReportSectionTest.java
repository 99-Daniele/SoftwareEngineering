package it.polimi.ingsw.model.faithTrackTests;

import it.polimi.ingsw.model.faithTrack.VaticanReportSection;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class VaticanReportSectionTest {

    /**
     * this test control the correct operation of the method that tells if the player is in or beyond the papal space
     */
    @Test
    void isPlayerInPopeSpace() {
        VaticanReportSection vaticanReportSection=new VaticanReportSection(3,6,2);
        assertFalse(vaticanReportSection.IsPlayerInPopeSpace(3));
        assertTrue(vaticanReportSection.IsPlayerInPopeSpace(6));
        assertTrue(vaticanReportSection.IsPlayerInPopeSpace(7));
    }
}