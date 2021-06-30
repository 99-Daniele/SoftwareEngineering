package it.polimi.ingsw.modelTests.actionsTests;

import it.polimi.ingsw.model.actions.*;
import it.polimi.ingsw.model.cards.developmentCards.Color;
import it.polimi.ingsw.model.games.SinglePlayerGame;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ActionTest {

    /**
     * this test verifies the correct overriding of triggerAction() method
     */
    @Test
    void overrideTriggerAction(){

        SinglePlayerGame singlePlayerGame = new SinglePlayerGame();
        Action action1 = new DiscardAction(Color.GREEN);
        Action action2 = new LorenzoTwoMoveAction();
        Action action3 = new LorenzoMoveAndShuffleAction();
        assertEquals(4, singlePlayerGame.getDeck(0, 0).numberOfCards());
        assertEquals(0, singlePlayerGame.getLorenzoIlMagnifico().getFaithPoints());

        action1.actionTrigger(singlePlayerGame);
        assertEquals(2, singlePlayerGame.getDeck(0, 0).numberOfCards());
        assertEquals(0, singlePlayerGame.getLorenzoIlMagnifico().getFaithPoints());

        action2.actionTrigger(singlePlayerGame);
        assertEquals(2, singlePlayerGame.getDeck(0, 0).numberOfCards());
        assertEquals(2, singlePlayerGame.getLorenzoIlMagnifico().getFaithPoints());

        action3.actionTrigger(singlePlayerGame);
        assertEquals(2, singlePlayerGame.getDeck(0, 0).numberOfCards());
        assertEquals(3, singlePlayerGame.getLorenzoIlMagnifico().getFaithPoints());
    }
}
