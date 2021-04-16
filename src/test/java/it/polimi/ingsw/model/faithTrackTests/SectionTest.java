package it.polimi.ingsw.model.faithTrackTests;

import it.polimi.ingsw.model.faithTrack.Section;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SectionTest {

    /**
     * this test control the correct operation of the method that tells if the player is in the section
     */
    @Test
    void isPlayerInSection() {
        Section section=new Section(3,6,2);
        assertTrue(section.isPlayerInSection(3));
        assertFalse(section.isPlayerInSection(7));
    }
}