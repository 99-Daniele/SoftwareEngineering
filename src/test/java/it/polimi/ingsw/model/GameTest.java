package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Color c1 = Color.GREEN;
    Color c2 = Color.PURPLE;
    Color c3 = Color.BLUE;
    Color c4 = Color.YELLOW;

    /**
     * this test tries to create a player with an already taken nickname
     */
    @Test
    void alreadyTakenNicknamePlayer() throws AlreadyTakenNicknameException {

        Game game = new MultiPlayerGame(2);
        game.createPlayer("Daniele");

        AlreadyTakenNicknameException thrown =
                assertThrows(AlreadyTakenNicknameException.class, () -> game.createPlayer("Daniele"));
        String expectedMessage = "Questo nickname è già stato scelto.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct creation of a new player
     */
    @Test
    void createPlayer(){

        Game game = new MultiPlayerGame(2);
        try{
            game.createPlayer("Daniele");
            assertSame("Daniele", game.getPlayer(0).getNickname());
            assertEquals(0, game.getPlayer(0).getFaithPoints());
            assertEquals(0, game.getPlayer(0).sumVictoryPoints());
            assertEquals(0, game.getPlayer(0).sumTotalResource());
            assertFalse(game.getPlayer(0).haveSevenDevelopmentCards());
        }
        catch (AlreadyTakenNicknameException e) {
            e.printStackTrace();
        }
    }

    /**
     * this test verifies the correct creations of decks.
     */
    @Test
    void createDecks() throws EmptyDevelopmentCardDeckException{

        Game game = new MultiPlayerGame(2);
        assertFalse(game.zeroRemainingColorCards());

        assertEquals(1, game.getDeck(0, 0).getFirstCard().getLevel());
        assertEquals(1, game.getDeck(0, 1).getFirstCard().getLevel());
        assertEquals(1, game.getDeck(0, 2).getFirstCard().getLevel());
        assertEquals(1, game.getDeck(0, 3).getFirstCard().getLevel());

        assertSame(Color.GREEN, game.getDeck(0, 0).getFirstCard().getColor());
        assertSame(Color.PURPLE, game.getDeck(0, 1).getFirstCard().getColor());
        assertSame(Color.BLUE, game.getDeck(0, 2).getFirstCard().getColor());
        assertSame(Color.YELLOW, game.getDeck(0, 3).getFirstCard().getColor());

        assertEquals(4, game.getDeck(0, 0).numberOfCards());
        assertEquals(4, game.getDeck(0, 1).numberOfCards());
        assertEquals(4, game.getDeck(0, 2).numberOfCards());
        assertEquals(4, game.getDeck(0, 3).numberOfCards());

        assertEquals(2, game.getDeck(1, 0).getFirstCard().getLevel());
        assertEquals(2, game.getDeck(1, 1).getFirstCard().getLevel());
        assertEquals(2, game.getDeck(1, 2).getFirstCard().getLevel());
        assertEquals(2, game.getDeck(1, 3).getFirstCard().getLevel());

        assertSame(Color.GREEN, game.getDeck(1, 0).getFirstCard().getColor());
        assertSame(Color.PURPLE, game.getDeck(1, 1).getFirstCard().getColor());
        assertSame(Color.BLUE, game.getDeck(1, 2).getFirstCard().getColor());
        assertSame(Color.YELLOW, game.getDeck(1, 3).getFirstCard().getColor());

        assertEquals(4, game.getDeck(1, 0).numberOfCards());
        assertEquals(4, game.getDeck(1, 1).numberOfCards());
        assertEquals(4, game.getDeck(1, 2).numberOfCards());
        assertEquals(4, game.getDeck(1, 3).numberOfCards());

        assertEquals(3, game.getDeck(2, 0).getFirstCard().getLevel());
        assertEquals(3, game.getDeck(2, 1).getFirstCard().getLevel());
        assertEquals(3, game.getDeck(2, 2).getFirstCard().getLevel());
        assertEquals(3, game.getDeck(2, 3).getFirstCard().getLevel());

        assertSame(Color.GREEN, game.getDeck(2, 0).getFirstCard().getColor());
        assertSame(Color.PURPLE, game.getDeck(2, 1).getFirstCard().getColor());
        assertSame(Color.BLUE, game.getDeck(2, 2).getFirstCard().getColor());
        assertSame(Color.YELLOW, game.getDeck(2, 3).getFirstCard().getColor());

        assertEquals(4, game.getDeck(2, 0).numberOfCards());
        assertEquals(4, game.getDeck(2, 1).numberOfCards());
        assertEquals(4, game.getDeck(2, 2).numberOfCards());
        assertEquals(4, game.getDeck(2, 3).numberOfCards());
    }

    /**
     * this test verifies the correct parsing from Json File DevelopmentCards.json
     */
    @Test
    void correctJsonParsingDevelopmentCards() throws IOException {

        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader("src/main/resources/DevelopmentCards.json"));
        DevelopmentCard[] developmentCards = gson.fromJson(reader, DevelopmentCard[].class);

        assertSame(Color.GREEN, developmentCards[0].getColor());
        assertEquals(1, developmentCards[0].getLevel());

        assertSame(Color.YELLOW, developmentCards[47].getColor());
        assertEquals(3, developmentCards[47].getLevel());

        assertSame(Color.PURPLE, developmentCards[23].getColor());
        assertEquals(2, developmentCards[23].getLevel());
    }

    /**
     * this test verifies the correct obtaining of casual leaderCard
     */
    @Test
    void getCasualLeaderCards(){

        Game game = new MultiPlayerGame(3);

        ArrayList<LeaderCard> cards = game.possibleCardLeader();
        assertNotSame(cards.get(0), cards.get(1));
        assertNotSame(cards.get(0), cards.get(2));
        assertNotSame(cards.get(0), cards.get(3));
        assertNotSame(cards.get(1), cards.get(2));
        assertNotSame(cards.get(1), cards.get(3));
        assertNotSame(cards.get(2), cards.get(3));
    }

    /**
     * test that controls if increase the victory points of the players in the vatican section
     */
    @Test
    void faithTrackMovement() throws AlreadyTakenNicknameException {

        Game game = new MultiPlayerGame(2);
        game.createPlayer("Daniele");
        game.createPlayer("Matteo");

        game.getPlayer(0).increaseFaithPoints(10);
        game.faithTrackMovement();

        assertEquals(2, game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(4, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());

        game.nextPlayer();
        game.getPlayer(1).increaseFaithPoints(14);
        game.faithTrackMovement();
        assertEquals(2, game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(4, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(6, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());

        game.nextPlayer();
        game.getPlayer(0).increaseFaithPoints(8);
        game.faithTrackMovement();
        assertEquals(5, game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(12, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(3, game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(6, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
    }

    /**
     * test that controls if it sets the victory points related to the position in the faith track of all the player
     * except the current and if it increases the victory points of all the players in the vatican section
     */
    @Test
    void faithTrackMovementExceptCurrentPlayer() throws AlreadyTakenNicknameException {

        Game game = new MultiPlayerGame(2);
        game.createPlayer("Daniele");
        game.createPlayer("Matteo");
        game.getPlayer(0).increaseFaithPoints(4);
        game.getPlayer(1).increaseFaithPoints(10);
        game.faithTrackMovementExceptCurrentPlayer();
        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(2, game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(4, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
    }

    /**
     * this test calculates the winner of the Game
     */
    @Test
    void endGameWinner()
            throws AlreadyTakenNicknameException, InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, ImpossibleSwitchDepotException {

        Game game = new MultiPlayerGame(4);
        game.createPlayer("Daniele");
        game.createPlayer("Matteo");
        game.createPlayer("Luca");
        game.createPlayer("Antonio");

        game.getPlayer(0).increaseFaithPoints(5);
        game.faithTrackMovement();
        game.nextPlayer();

        game.getPlayer(1).increaseFaithPoints(4);
        game.faithTrackMovement();
        game.nextPlayer();

        game.getPlayer(2).increaseFaithPoints(8);
        game.faithTrackMovement();

        assertEquals(3, game.getPlayer(0).sumVictoryPoints());
        assertEquals(1, game.getPlayer(1).sumVictoryPoints());
        assertEquals(4, game.getPlayer(2).sumVictoryPoints());
        assertEquals(0, game.getPlayer(3).sumVictoryPoints());

        assertEquals(0, game.getPlayer(0).sumTotalResource());
        assertEquals(0, game.getPlayer(1).sumTotalResource());
        assertEquals(0, game.getPlayer(2).sumTotalResource());
        assertEquals(0, game.getPlayer(3).sumTotalResource());

        PlayerBoard winner = game.endGame();

        assertSame("Luca", winner.getNickname());

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        c3.addResource(Resource.COIN, 1);
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 4, c2, c3, 0);
        game.getPlayer(3).buyDevelopmentCard(developmentCard, 0, 1);

        PowerProductionPlayerChoice player1choice = new PowerProductionPlayerChoice();
        player1choice.setFirstPower();
        game.getPlayer(3).activateProduction(player1choice);
        game.nextPlayer();

        assertEquals(4, game.getPlayer(2).sumVictoryPoints());
        assertEquals(4, game.getPlayer(3).sumVictoryPoints());

        assertEquals(0, game.getPlayer(2).sumTotalResource());
        assertEquals(1, game.getPlayer(3).sumTotalResource());

        PlayerBoard winner2 = game.endGame();

        assertSame("Antonio", winner2.getNickname());
    }
}