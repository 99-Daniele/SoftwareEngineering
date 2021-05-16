package it.polimi.ingsw.modelTests.playersTests;

import it.polimi.ingsw.model.player.PowerProductionPlayerChoice;
import it.polimi.ingsw.model.resourceContainers.Resource;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PowerProductionPlayerChoiceTest {

    Resource r1 = Resource.COIN;
    Resource r2 = Resource.SHIELD;
    Resource r3 = Resource.STONE;

    /**
     * this test verifies the correct set and get of resources if basic power
     */
    @Test
    void setAndGetBasicPower(){

        PowerProductionPlayerChoice choice = new PowerProductionPlayerChoice();
        assertFalse(choice.isBasicPower());

        choice.setBasicPower(r1, r2, r3);
        assertTrue(choice.isBasicPower());
        assertSame(r1, choice.getResources()[0]);
        assertSame(r2, choice.getResources()[1]);
        assertSame(r3, choice.getResources()[2]);
    }

    /**
     * this test verifies the correct get and set of resources if additional power from AdditionalProductionPowerCard
     */
    @Test
    void setAndGetAdditionalPower(){

        PowerProductionPlayerChoice choice = new PowerProductionPlayerChoice();
        assertFalse(choice.isFirstAdditionalPower());
        assertFalse(choice.isSecondAdditionalPower());

        choice.setFirstAdditionalPower(r1);
        choice.setSecondAdditionalPower(r3);
        assertTrue(choice.isFirstAdditionalPower());
        assertTrue(choice.isSecondAdditionalPower());
        assertSame(r1, choice.getAdditionalResource1());
        assertSame(r3, choice.getAdditionalResource2());
    }
}
