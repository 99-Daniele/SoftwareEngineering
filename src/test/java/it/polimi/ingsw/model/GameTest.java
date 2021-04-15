package it.polimi.ingsw.model;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import it.polimi.ingsw.exceptions.*;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    /**
     * this test tries to create a player with an already taken nickname
     */
    @Test
    void alreadyTakenNicknamePlayer() throws AlreadyTakenNicknameException {

        Game game = new Game(2);
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

        Game game = new Game(2);
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

        Game game = new Game(2);
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
        assertEquals(1, developmentCards[0].getVictoryPoints());

        assertSame(Color.YELLOW, developmentCards[47].getColor());
        assertEquals(3, developmentCards[47].getLevel());
        assertEquals(12, developmentCards[47].getVictoryPoints());

        assertSame(Color.PURPLE, developmentCards[23].getColor());
        assertEquals(2, developmentCards[23].getLevel());
        assertEquals(8, developmentCards[23].getVictoryPoints());
    }

    /**
     * this test verifies the correct parsing from Json File of LeaderCards
     */
    @Test
    void correctJsonParsingLeaderCards() throws IOException{

        ArrayList<LeaderCard> leaderCards = new ArrayList<>();
        Gson gson = new Gson();
        JsonReader reader1 = new JsonReader(new FileReader("src/main/resources/DiscountCards.json"));
        LeaderCard[] discountCards = gson.fromJson(reader1, DiscountCard[].class);
        JsonReader reader2 = new JsonReader(new FileReader("src/main/resources/ExtraDepotCards.json"));
        LeaderCard[] extraDepotCards = gson.fromJson(reader2, ExtraDepotCard[].class);
        JsonReader reader3 = new JsonReader(new FileReader("src/main/resources/WhiteConversionCards.json"));
        LeaderCard[] whiteConversionCards = gson.fromJson(reader3, WhiteConversionCard[].class);
        JsonReader reader4 = new JsonReader(new FileReader("src/main/resources/AdditionalProductionPowerCards.json"));
        LeaderCard[] additionalProductionPowerCards = gson.fromJson(reader4, AdditionalProductionPowerCard[].class);

        leaderCards.addAll(Arrays.asList(discountCards).subList(0, 4));
        leaderCards.addAll(Arrays.asList(extraDepotCards).subList(0, 4));
        leaderCards.addAll(Arrays.asList(whiteConversionCards).subList(0, 4));
        leaderCards.addAll(Arrays.asList(additionalProductionPowerCards).subList(0, 4));

        assertSame(Resource.SERVANT, leaderCards.get(0).getResource());
        assertEquals(0, leaderCards.get(0).getVictoryPoints());
        assertFalse(leaderCards.get(0).isActive());

        assertSame(Resource.COIN, leaderCards.get(15).getResource());
        assertEquals(0, leaderCards.get(15).getVictoryPoints());
        assertFalse(leaderCards.get(15).isActive());

        assertSame(Resource.SERVANT, leaderCards.get(5).getResource());
        assertEquals(0, leaderCards.get(5).getVictoryPoints());
        assertFalse(leaderCards.get(5).isActive());
        /*
         victory points are set at 0 because cards are not active.
         */
    }

    /**
     * this test verifies the getting of the correct deck by color
     */
    @Test
    void getCorrectColorDeck() throws EmptyDevelopmentCardDeckException {

        Game game = new Game(2);

        assertSame(game.getDeck(0, 0), game.getColorDeck(Color.GREEN));
        assertSame(game.getDeck(0, 1), game.getColorDeck(Color.PURPLE));
        assertSame(game.getDeck(0, 2), game.getColorDeck(Color.BLUE));
        assertSame(game.getDeck(0, 3), game.getColorDeck(Color.YELLOW));

        game.getDeck(0,0).removeDevelopmentCard();
        game.getDeck(0,0).removeDevelopmentCard();
        game.getDeck(0,0).removeDevelopmentCard();
        game.getDeck(0,0).removeDevelopmentCard();
        assertNotSame(game.getDeck(0, 0), game.getColorDeck(Color.GREEN));
        assertSame(game.getDeck(1, 0), game.getColorDeck(Color.GREEN));

        game.getDeck(1,0).removeDevelopmentCard();
        game.getDeck(1,0).removeDevelopmentCard();
        game.getDeck(1,0).removeDevelopmentCard();
        game.getDeck(1,0).removeDevelopmentCard();
        assertNotSame(game.getDeck(0, 0), game.getColorDeck(Color.GREEN));
        assertNotSame(game.getDeck(1, 0), game.getColorDeck(Color.GREEN));
        assertSame(game.getDeck(2, 0), game.getColorDeck(Color.GREEN));

        game.getDeck(2,0).removeDevelopmentCard();
        game.getDeck(2,0).removeDevelopmentCard();
        game.getDeck(2,0).removeDevelopmentCard();
        game.getDeck(2,0).removeDevelopmentCard();
        assertTrue(game.zeroRemainingColorCards());
        assertNotSame(game.getDeck(0, 0), game.getColorDeck(Color.GREEN));
        assertNotSame(game.getDeck(1, 0), game.getColorDeck(Color.GREEN));
        assertSame(game.getDeck(2, 0), game.getColorDeck(Color.GREEN));
        /*
         even if deck[2][0] is empty still return it
         */
    }

    /**
     * this test verifies if remains zero DevelopmentCard of one kind of color
     */
    @Test
    void zeroRemainingColorCards() throws EmptyDevelopmentCardDeckException {

        Game game = new Game(2);
        assertFalse(game.zeroRemainingColorCards());

        game.getDeck(0,0).removeDevelopmentCard();
        game.getDeck(0,0).removeDevelopmentCard();
        game.getDeck(0,0).removeDevelopmentCard();
        game.getDeck(0,0).removeDevelopmentCard();
        game.getDeck(1,0).removeDevelopmentCard();
        game.getDeck(1,0).removeDevelopmentCard();
        game.getDeck(1,0).removeDevelopmentCard();
        game.getDeck(1,0).removeDevelopmentCard();
        game.getDeck(2,0).removeDevelopmentCard();
        game.getDeck(2,0).removeDevelopmentCard();
        game.getDeck(2,0).removeDevelopmentCard();
        game.getDeck(2,0).removeDevelopmentCard();
        assertTrue(game.zeroRemainingColorCards());
    }

    /**
     * this test verifies the correct obtaining of 4 casual LeaderCards
     */
    @Test
    void getCasualLeaderCards(){

        Game game = new Game(3);

        ArrayList<LeaderCard> cards = game.possibleCardLeader();
        assertNotSame(cards.get(0), cards.get(1));
        assertNotSame(cards.get(0), cards.get(2));
        assertNotSame(cards.get(0), cards.get(3));
        assertNotSame(cards.get(1), cards.get(2));
        assertNotSame(cards.get(1), cards.get(3));
        assertNotSame(cards.get(2), cards.get(3));
    }

    /**
     * this test verifies the correct move to next player
     */
    @Test
    void correctNextPlayer() throws AlreadyTakenNicknameException {

        Game game = new Game(4);
        game.createPlayer("Alfredo");
        game.createPlayer("Gustavo");
        game.createPlayer("Domenico");
        game.createPlayer("Quasimodo");

        assertSame(game.getPlayer(0), game.getCurrentPlayer());

        game.nextPlayer();
        assertSame(game.getPlayer(1), game.getCurrentPlayer());

        game.nextPlayer();
        assertSame(game.getPlayer(2), game.getCurrentPlayer());

        game.nextPlayer();
        assertSame(game.getPlayer(3), game.getCurrentPlayer());

        game.nextPlayer();
        assertSame(game.getPlayer(0), game.getCurrentPlayer());
        /*
         this nextPlayer() return to the first player
         */
    }

    /**
     * this test verifies the correct selection, activation and getting of current player LeaderCards
     */
    @Test
    void selectActivateAndGetCurrentPlayerLeaderCards() throws AlreadyTakenNicknameException, AlreadyDiscardLeaderCardException, InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException {

        Game game = new Game(2);
        game.createPlayer("Paolo");

        Cost c = new Cost();
        LeaderCard card1 = new DiscountCard(Resource.COIN, c, 1);
        LeaderCard card2 = new WhiteConversionCard(Resource.COIN, c, 2);

        game.selectCurrentPlayerLeaderCards(card1, card2);
        assertSame(card1, game.getPlayer(0).getLeaderCard(1));
        assertSame(card2, game.getPlayer(0).getLeaderCard(2));

        game.activateLeaderCard(1);
        game.activateLeaderCard(2);
        LeaderCard[] leaderCards = game.getCurrentPlayerActiveLeaderCards();
        assertSame(card1, leaderCards[0]);
        assertSame(card2, leaderCards[1]);
    }

    /**
     * correct number of taken marbles if player chose row or column
     */
    @Test
    void numOfTakenMarbles() throws WrongParametersException {

        Game game = new Game(3);

        Marble[] rowMarbles = game.takeMarketMarble(true, 1);
        assertEquals(4, rowMarbles.length);

        Marble[] columnMarbles = game.takeMarketMarble(false, 1);
        assertEquals(3, columnMarbles.length);
    }

    /**
     * this test verifies correct operation of a white marble converted by a LeaderCard
     */
    @Test
    void correctWhiteMarbleConversion() throws AlreadyTakenNicknameException {

        Game game = new Game(2);
        game.createPlayer("Angelo");
        game.createPlayer("Gustavo");

        Cost c = new Cost();
        LeaderCard card = new WhiteConversionCard(Resource.COIN, c, 1);

        assertEquals(0, game.getPlayer(0).sumTotalResource());
        assertEquals(0, game.getPlayer(1).getFaithPoints());

        game.whiteMarbleConversion(card);
        assertEquals(1, game.getPlayer(0).sumTotalResource());
        assertEquals(0, game.getPlayer(1).getFaithPoints());

        game.whiteMarbleConversion(card);
        assertEquals(1, game.getPlayer(0).sumTotalResource());
        assertEquals(1, game.getPlayer(1).getFaithPoints());
    }

    /**
     * this test verifies the correct increase of other players faith points
     */
    @Test
    void otherPlayersIncreaseFaithPoints() throws AlreadyTakenNicknameException {

        Game game = new Game(4);
        game.createPlayer("Enrico");
        game.createPlayer("Pietro");
        game.createPlayer("Angela");
        game.createPlayer("Giulia");

        assertEquals(0, game.getPlayer(0).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getFaithPoints());
        assertEquals(0, game.getPlayer(2).getFaithPoints());
        assertEquals(0, game.getPlayer(3).getFaithPoints());

        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(0, game.getPlayer(2).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(0, game.getPlayer(3).getVictoryPoints().getVictoryPointsByFaithTrack());

        game.increaseOneFaithPointOtherPlayers();
        game.increaseOneFaithPointOtherPlayers();
        game.increaseOneFaithPointOtherPlayers();

        assertEquals(0, game.getPlayer(0).getFaithPoints());
        assertEquals(3, game.getPlayer(1).getFaithPoints());
        assertEquals(3, game.getPlayer(2).getFaithPoints());
        assertEquals(3, game.getPlayer(3).getFaithPoints());

        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(0, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(0, game.getPlayer(2).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(0, game.getPlayer(3).getVictoryPoints().getVictoryPointsByFaithTrack());

        game.faithTrackMovementAllPlayer();

        assertEquals(0, game.getPlayer(0).getFaithPoints());
        assertEquals(3, game.getPlayer(1).getFaithPoints());
        assertEquals(3, game.getPlayer(2).getFaithPoints());
        assertEquals(3, game.getPlayer(3).getFaithPoints());

        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(1, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(1, game.getPlayer(2).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(1, game.getPlayer(3).getVictoryPoints().getVictoryPointsByFaithTrack());
    }

    /**
     * this test tries to buy a DevelopmentCard if current player does not have enough resource
     */
    @Test
    void notEnoughResourceBuyDevelopmentCard() throws AlreadyTakenNicknameException {

        Game game = new Game(2);
        game.createPlayer("Roberta");

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> game.buyDevelopmentCard(0, 0, 1, 1));

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to but a DevelopmentCard and add to a wrong SlotDevelopmentCards
     */
    @Test
    void wrongSlotDevelopmentCards() throws AlreadyTakenNicknameException, InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException, ImpossibleDevelopmentCardAdditionException, EmptyDevelopmentCardDeckException {

        Game game = new Game(2);
        game.createPlayer("Roberta");

        Cost c = new Cost();
        LeaderCard leaderCard1 = new ExtraDepotCard(Resource.COIN, c, 1);
        LeaderCard leaderCard2 = new ExtraDepotCard(Resource.SHIELD, c, 1);
        game.selectCurrentPlayerLeaderCards(leaderCard1, leaderCard2);
        game.activateLeaderCard(1);
        game.activateLeaderCard(2);

        game.increaseWarehouse(Resource.SERVANT);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.STONE);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);


        game.buyDevelopmentCard(0, 0, 1, 1);

        game.increaseWarehouse(Resource.SERVANT);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);

        ImpossibleDevelopmentCardAdditionException thrown =
                assertThrows(ImpossibleDevelopmentCardAdditionException.class, () -> game.buyDevelopmentCard(0, 0, 1, 1));

        String expectedMessage = "Non puoi comprare questa carta";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct update of Game after the buying of a DevelopmentCard
     */
    @Test
    void updateAfterBuyDevelopmentCard()
            throws AlreadyTakenNicknameException, InsufficientResourceException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        Game game = new Game(2);
        game.createPlayer("Roberta");

        Cost c = new Cost();
        LeaderCard leaderCard1 = new ExtraDepotCard(Resource.STONE, c, 0);
        LeaderCard leaderCard2 = new ExtraDepotCard(Resource.SERVANT, c, 0);
        game.selectCurrentPlayerLeaderCards(leaderCard1, leaderCard2);
        game.activateLeaderCard(1);

        game.increaseWarehouse(Resource.SERVANT);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.STONE);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);

        assertEquals(7, game.getPlayer(0).sumTotalResource());
        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByCards());
        assertEquals(4, game.getDeck(0, 0).numberOfCards());

        DevelopmentCard card = game.getDeck(0 , 0).getFirstCard();
        assertSame(Color.GREEN, card.getColor());

        game.buyDevelopmentCard(0, 0, 1, 1);
        assertNotEquals(7, game.getPlayer(0).sumTotalResource());
        assertNotEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByCards());
        assertEquals(3, game.getDeck(0, 0).numberOfCards());
        assertNotSame(card, game.getDeck(0, 0).getFirstCard());
    }

    /**
     * this test verifies if player move in FaithTrack after discarding a LeaderCard
     */
    @Test
    void faithTrackMovementAfterDiscardLeaderCard() throws AlreadyTakenNicknameException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        Game game = new Game(4);
        game.createPlayer("Gianfranco");

        Cost c = new Cost();
        LeaderCard leaderCard1 = new ExtraDepotCard(Resource.STONE, c, 1);
        LeaderCard leaderCard2 = new ExtraDepotCard(Resource.SERVANT, c, 1);
        game.selectCurrentPlayerLeaderCards(leaderCard1, leaderCard2);

        game.getPlayer(0).increaseFaithPoints(2);
        assertEquals(2, game.getPlayer(0).getFaithPoints());
        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());

        game.discardLeaderCard(1);
        assertEquals(3, game.getPlayer(0).getFaithPoints());
        assertEquals(1, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
    }

    /**
     * test that controls if increase the victory points of the players in the vatican section
     */
    @Test
    void faithTrackMovement() throws AlreadyTakenNicknameException {

        Game game = new Game(2);
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
     * and if it increases the victory points of all the players in the vatican section
     */
    @Test
    void faithTrackMovementAllPlayer() throws AlreadyTakenNicknameException {

        Game game = new Game(2);
        game.createPlayer("Daniele");
        game.createPlayer("Matteo");
        game.getPlayer(0).increaseFaithPoints(4);
        game.getPlayer(1).increaseFaithPoints(10);
        game.faithTrackMovementAllPlayer();
        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(2, game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(1, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(4, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
    }

    /**
     * this test calculates the winner of the Game
     */
    @Test
    void endGameWinner()
            throws AlreadyTakenNicknameException, InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, ImpossibleSwitchDepotException {

        Game game = new Game(4);
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
        game.getPlayer(3).buyDevelopmentCard(developmentCard, 1, 1);

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