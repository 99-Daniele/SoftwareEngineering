package it.polimi.ingsw.modelTests.gamesTests;

import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.cards.developmentCards.*;
import it.polimi.ingsw.model.cards.leaderCards.*;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.view.modelView.CardView;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.parser.*;
import it.polimi.ingsw.model.player.Strongbox;
import it.polimi.ingsw.model.resourceContainers.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    /**
     * this test tries to create a player with an already taken nickname.
     */
    @Test
    void alreadyTakenNicknamePlayer() throws AlreadyTakenNicknameException {

        Game game = new Game(4);
        game.createPlayer("Daniele");

        AlreadyTakenNicknameException thrown =
                assertThrows(AlreadyTakenNicknameException.class, () -> game.createPlayer("Daniele"));
        String expectedMessage = "This nickname has already been taken";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct creation of a new player.
     */
    @Test
    void createPlayer(){

        Game game = new Game(4);
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
     * this test verifies if all players are connected.
     */
    @Test
    void allPlayersConnected(){

        Game game = new Game(4);
        try {
            game.createPlayer("Daniele");
            game.createPlayer("Danilo");
            game.createPlayer("Emanuele");
            assertFalse(game.allPlayersConnected());
            game.createPlayer("Daniela");
            assertTrue(game.allPlayersConnected());
            game.shufflePlayers();
            assertTrue(game.allPlayersConnected());
        } catch (AlreadyTakenNicknameException e) {
            e.printStackTrace();
        }
    }

    /**
     * this test verifies the correct delete of a player.
     */
    @Test
    void deletePlayer(){

        Game game = new Game(2);
        try {
            game.createPlayer("Daniele");
            game.createPlayer("Giorgio");
            assertSame("Daniele", game.getPlayerPosition(0));
            game.deletePlayer("Daniele");
            assertSame("Giorgio", game.getPlayerPosition(0));
        } catch (AlreadyTakenNicknameException e) {
            e.printStackTrace();
        }
    }

    /**
     * this test verifies the correct nickName of players by position.
     */
    @Test
    void correctGetNickNameFromPosition(){

        Game game = new Game(4);
        try {
            game.createPlayer("Daniele");
            game.createPlayer("Danilo");
            game.createPlayer("Emanuele");
            game.createPlayer("Daniela");
            assertSame("Daniele", game.getPlayerPosition(0));
            assertSame("Danilo", game.getPlayerPosition(1));
            assertSame("Emanuele", game.getPlayerPosition(2));
            assertSame("Daniela", game.getPlayerPosition(3));
        } catch (AlreadyTakenNicknameException e) {
            e.printStackTrace();
        }
    }

    /**
     * this test verifies if player already selected leader cards.
     */
    @Test
    void alreadySelectedLeaderCards(){

        Game game = new Game(2);
        try {
            game.createPlayer("Daniele");
            assertFalse(game.alreadySelectedLeaderCards(0));
            game.selectPlayerLeaderCards((LeaderCard) CardMapCLI.getCard(49), (LeaderCard) CardMapCLI.getCard(50), 0);
            assertTrue(game.alreadySelectedLeaderCards(0));
        } catch (AlreadyTakenNicknameException e) {
            e.printStackTrace();
        }
    }

    /**
     * this test verifies if player already selected resources
     */
    @Test
    void alreadySelectedResources(){

        Game game = new Game(4);
        try {
            game.createPlayer("Daniele");
            game.createPlayer("Danilo");
            game.createPlayer("Emanuele");
            game.createPlayer("Daniela");
            assertTrue(game.alreadySelectedResource(0));
            assertFalse(game.alreadySelectedResource(1));
            assertFalse(game.alreadySelectedResource(2));
            assertFalse(game.alreadySelectedResource(3));
            assertFalse(game.alreadySelectedResource(4));
            game.firstIncreaseWarehouse(Resource.COIN, 1);
            game.firstIncreaseWarehouse(Resource.COIN, 2);
            game.firstDoubleIncreaseWarehouse(Resource.COIN, Resource.COIN);
            assertTrue(game.alreadySelectedResource(0));
            assertTrue(game.alreadySelectedResource(1));
            assertTrue(game.alreadySelectedResource(2));
            assertTrue(game.alreadySelectedResource(3));
        } catch (AlreadyTakenNicknameException e) {
            e.printStackTrace();
        }
    }

    /**
     * this test verifies if players have made all their choices
     */
    @Test
    void allPlayersReady(){

        Game game = new Game(4);
        try {
            game.createPlayer("Daniele");
            game.createPlayer("Danilo");
            game.createPlayer("Emanuele");
            game.createPlayer("Daniela");
            assertFalse(game.allPlayersReady());
            game.selectPlayerLeaderCards((LeaderCard) CardMapCLI.getCard(49), (LeaderCard) CardMapCLI.getCard(50), 0);
            game.selectPlayerLeaderCards((LeaderCard) CardMapCLI.getCard(51), (LeaderCard) CardMapCLI.getCard(52), 1);
            game.selectPlayerLeaderCards((LeaderCard) CardMapCLI.getCard(53), (LeaderCard) CardMapCLI.getCard(54), 2);
            game.selectPlayerLeaderCards((LeaderCard) CardMapCLI.getCard(55), (LeaderCard) CardMapCLI.getCard(56), 3);
            game.firstIncreaseWarehouse(Resource.COIN, 1);
            game.firstIncreaseWarehouse(Resource.COIN, 2);
            game.firstDoubleIncreaseWarehouse(Resource.COIN, Resource.COIN);
            assertTrue(game.allPlayersReady());
        } catch (AlreadyTakenNicknameException e) {
            e.printStackTrace();
        }
    }

    /**
     * this test verifies the correct list of nickNames of players.
     */
    @Test
    void correctNickNamesList() {

        Game game = new Game(4);
        try {
            game.createPlayer("Daniele");
            game.createPlayer("Danilo");
            game.createPlayer("Emanuele");
            game.createPlayer("Daniela");
            assertEquals(4, game.getPlayersNickname().size());
            assertSame("Daniele", game.getPlayersNickname().get(0));
            assertSame("Danilo", game.getPlayersNickname().get(1));
            assertSame("Emanuele", game.getPlayersNickname().get(2));
            assertSame("Daniela", game.getPlayersNickname().get(3));
        } catch (AlreadyTakenNicknameException e) {
            e.printStackTrace();
        }
    }

        /**
     * this test verifies the correct creations of decks.
     */
    @Test
    void createDecks() throws EmptyDevelopmentCardDeckException{

        Game game = new Game(4);
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
    void correctJsonParsingDevelopmentCards(){

        DevelopmentCardsParser developmentCardsParser = new DevelopmentCardsParser();
        DevelopmentCard[] developmentCards = developmentCardsParser.getDevelopmentCards();
        assertEquals(48, developmentCards.length);

        assertSame(Color.GREEN, developmentCards[0].getColor());
        assertEquals(1, developmentCards[0].getLevel());
        assertEquals(1, developmentCards[0].getVictoryPoints());
        assertEquals(1, developmentCards[0].getCardID());

        assertSame(Color.YELLOW, developmentCards[47].getColor());
        assertEquals(3, developmentCards[47].getLevel());
        assertEquals(12, developmentCards[47].getVictoryPoints());
        assertEquals(48, developmentCards[47].getCardID());

        assertSame(Color.PURPLE, developmentCards[23].getColor());
        assertEquals(2, developmentCards[23].getLevel());
        assertEquals(8, developmentCards[23].getVictoryPoints());
        assertEquals(24, developmentCards[23].getCardID());
    }

    /**
     * this test verifies the correct parsing from Json File of LeaderCards
     */
    @Test
    void correctJsonParsingLeaderCards(){

        LeaderCardsParser leaderCardsParser = new LeaderCardsParser();
        ArrayList<LeaderCard> leaderCards = leaderCardsParser.getLeaderCards();
        assertEquals(16, leaderCards.size());

        assertSame(Resource.SERVANT, leaderCards.get(0).getResource());
        assertEquals(0, leaderCards.get(0).getCurrentVictoryPoints());
        assertEquals(2, leaderCards.get(0).getVictoryPoints());
        assertFalse(leaderCards.get(0).isActive());
        assertTrue(leaderCards.get(0).getCardID() > 48);
        assertTrue(leaderCards.get(0).getCardID() < 65);

        assertSame(Resource.COIN, leaderCards.get(15).getResource());
        assertEquals(0, leaderCards.get(15).getCurrentVictoryPoints());
        assertEquals(4, leaderCards.get(15).getVictoryPoints());
        assertFalse(leaderCards.get(15).isActive());
        assertTrue(leaderCards.get(15).getCardID() > 48);
        assertTrue(leaderCards.get(15).getCardID() < 65);

        assertSame(Resource.SERVANT, leaderCards.get(5).getResource());
        assertEquals(0, leaderCards.get(5).getCurrentVictoryPoints());
        assertEquals(3, leaderCards.get(5).getVictoryPoints());
        assertFalse(leaderCards.get(5).isActive());
        assertTrue(leaderCards.get(5).getCardID() > 48);
        assertTrue(leaderCards.get(5).getCardID() < 65);
        /*
         current victory points are set at 0 because cards are not active.
         */
    }

    /**
     * this test verifies the correct mapping of Cards
     */
    @Test
    void correctMapping(){

        CardView card1 = CardMapCLI.getCard(1);

        assertEquals(1, card1.getCardID());
        assertTrue(card1 instanceof DevelopmentCard);

        CardView card34 = CardMapCLI.getCard(34);

        assertEquals(34, card34.getCardID());
        assertTrue(card34 instanceof DevelopmentCard);

        CardView card49 = CardMapCLI.getCard(49);

        assertEquals(49, card49.getCardID());
        assertTrue(card49 instanceof DiscountCard);

        CardView card64 = CardMapCLI.getCard(64);

        assertEquals(64, card64.getCardID());
        assertTrue(card64 instanceof AdditionalProductionPowerCard);
    }

    /**
     * this test verifies the getting of the correct deck by color
     */
    @Test
    void getCorrectColorDeck() throws EmptyDevelopmentCardDeckException {

        Game game = new Game(4);

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

        Game game = new Game(4);
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

        Game game = new Game(4);

        ArrayList<LeaderCard> cards = game.casualLeaderCards();
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

        Game game = new Game(4);
        game.createPlayer("Paolo");

        Cost c = new Cost();
        LeaderCard card1 = new DiscountCard(Resource.COIN, c, 1, 0);
        LeaderCard card2 = new WhiteConversionCard(Resource.COIN, c, 2, 0);

        game.selectPlayerLeaderCards(card1, card2, 0);
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
    void numOfTakenMarbles(){

        Game game = new Game(4);

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
        LeaderCard card = new WhiteConversionCard(Resource.COIN, c, 1, 0);

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
     * this test verifies the correct first increase of warehouse
     */
    @Test
    void firstWarehouseIncrease() throws AlreadyTakenNicknameException {

        Game game = new Game(4);
        game.createPlayer("Enrico");
        game.createPlayer("Pietro");
        game.createPlayer("Angela");
        game.createPlayer("Giulia");
        assertEquals(0, game.getPlayer(0).sumTotalResource());
        assertEquals(0, game.getPlayer(1).sumTotalResource());
        assertEquals(0, game.getPlayer(2).sumTotalResource());
        assertEquals(0, game.getPlayer(3).sumTotalResource());

        assertEquals(0, game.getPlayer(0).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getFaithPoints());
        assertEquals(0, game.getPlayer(2).getFaithPoints());
        assertEquals(0, game.getPlayer(3).getFaithPoints());

        game.firstIncreaseWarehouse(Resource.COIN, 1);
        game.firstIncreaseWarehouse(Resource.COIN, 2);
        game.firstDoubleIncreaseWarehouse(Resource.COIN, Resource.COIN);
        assertEquals(0, game.getPlayer(0).sumTotalResource());
        assertEquals(1, game.getPlayer(1).sumTotalResource());
        assertEquals(1, game.getPlayer(2).sumTotalResource());
        assertEquals(2, game.getPlayer(3).sumTotalResource());

        assertEquals(0, game.getPlayer(0).getFaithPoints());
        assertEquals(0, game.getPlayer(1).getFaithPoints());
        assertEquals(1, game.getPlayer(2).getFaithPoints());
        assertEquals(1, game.getPlayer(3).getFaithPoints());
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

        game.faithTrackMovementAllPlayers();

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

        Game game = new Game(4);
        game.createPlayer("Roberta");

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> game.buyDevelopmentCardFromMarket(0, 0, 1, 1));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to but a DevelopmentCard and add to a wrong SlotDevelopmentCards
     */
    @Test
    void wrongSlotDevelopmentCards() throws AlreadyTakenNicknameException, InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException, ImpossibleDevelopmentCardAdditionException, EmptyDevelopmentCardDeckException {

        Game game = new Game(4);
        game.createPlayer("Roberta");

        Cost c = new Cost();
        LeaderCard leaderCard1 = new ExtraDepotCard(Resource.COIN, c, 1, 0);
        LeaderCard leaderCard2 = new ExtraDepotCard(Resource.SHIELD, c, 1, 0);
        game.selectPlayerLeaderCards(leaderCard1, leaderCard2, 0);
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


        game.buyDevelopmentCardFromMarket(0, 0, 1, 1);

        game.increaseWarehouse(Resource.SERVANT);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);

        ImpossibleDevelopmentCardAdditionException thrown =
                assertThrows(ImpossibleDevelopmentCardAdditionException.class, () -> game.buyDevelopmentCardFromMarket(0, 0, 1, 1));

        String expectedMessage = "You have not available slots to buy this card";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verify the correct update of available slots after buy DevelopmentCards
     */
    @Test
    void findRightSlotsAfterBuyDevelopmentCard()
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, AlreadyTakenNicknameException {

        Game game = new Game(4);
        game.createPlayer("Giorgia");

        Cost c = new Cost();
        LeaderCard leaderCard1 = new ExtraDepotCard(Resource.COIN, c, 1, 0);
        LeaderCard leaderCard2 = new ExtraDepotCard(Resource.SHIELD, c, 1, 0);
        game.selectPlayerLeaderCards(leaderCard1, leaderCard2, 0);
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

        ArrayList<Integer> slots1 = game.findAvailableSlots(0, 0);
        assertEquals(3, slots1.size());
        assertEquals(1, slots1.get(0));
        assertEquals(2, slots1.get(1));
        assertEquals(3, slots1.get(2));

        game.buyDevelopmentCardFromMarket(0, 0, 1, 2);

        game.increaseWarehouse(Resource.SERVANT);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);

        ArrayList<Integer> slots2 = game.findAvailableSlots(0, 0);
        assertEquals(2, slots2.size());
        assertEquals(1, slots2.get(0));
        assertEquals(3, slots2.get(1));
    }

    /**
     * this test verifies the correct update of Game after the buying of a DevelopmentCard
     */
    @Test
    void updateAfterBuyDevelopmentCard()
            throws AlreadyTakenNicknameException, InsufficientResourceException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        Game game = new Game(4);
        game.createPlayer("Roberta");

        Cost c = new Cost();
        LeaderCard leaderCard1 = new ExtraDepotCard(Resource.STONE, c, 0, 0);
        LeaderCard leaderCard2 = new ExtraDepotCard(Resource.SERVANT, c, 0, 0);
        game.selectPlayerLeaderCards(leaderCard1, leaderCard2, 0);
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

        game.buyDevelopmentCardFromMarket(0, 0, 1, 1);
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
        LeaderCard leaderCard1 = new ExtraDepotCard(Resource.STONE, c, 1, 0);
        LeaderCard leaderCard2 = new ExtraDepotCard(Resource.SERVANT, c, 1, 0);
        game.selectPlayerLeaderCards(leaderCard1, leaderCard2, 0);

        game.getPlayer(0).increaseFaithPoints(2);
        assertEquals(2, game.getPlayer(0).getFaithPoints());
        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());

        game.discardLeaderCard(1);
        assertEquals(3, game.getPlayer(0).getFaithPoints());
        assertEquals(1, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
    }

    /**
     * this test tries to activate a not existing DevelopmentCard production power
     */
    @Test
    void incorrectActivateStandardProductionPowerNoSuchCard() throws AlreadyTakenNicknameException {

        Game game = new Game(4);
        game.createPlayer("Regina");

        Strongbox s = new Strongbox();
        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> game.removeDevelopmentCardProductionResource(3, s, 2));

        String expectedMessage = "You don't have any card to activate this power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to activate DevelopmentCard production power if there aren't not enough resources
     */
    @Test
    void incorrectActivateStandardProductionPowerNotEnoughResource() throws AlreadyTakenNicknameException, InsufficientResourceException, ImpossibleDevelopmentCardAdditionException {

        Game game = new Game(4);
        game.createPlayer("Regina");

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(Resource.STONE, 1);
        Cost c3 = new Cost();
        int cardID = 0;

        DevelopmentCard card = new DevelopmentCard(Color.GREEN, 1,  c1, 1, c2, c3, 5, cardID);
        game.getPlayer(0).buyDevelopmentCard(card, 1, 1);

        Strongbox s = new Strongbox();
        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> game.removeDevelopmentCardProductionResource(1, s, 1));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of DevelopmentCard production power
     */
    @Test
    void correctActivateStandardProductionPower()
            throws AlreadyTakenNicknameException, InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, NoSuchProductionPowerException {

        Game game = new Game(4);
        game.createPlayer("Regina");
        game.increaseWarehouse(Resource.STONE);
        assertEquals(1, game.getPlayer(0).sumTotalResource());

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(Resource.STONE, 1);
        Cost c3 = new Cost();
        c3.addResource(Resource.COIN, 2);
        int cardID = 0;

        DevelopmentCard card = new DevelopmentCard(Color.GREEN, 1,  c1, 1, c2, c3, 5, cardID);
        game.getPlayer(0).buyDevelopmentCard(card, 1, 1);
        assertEquals(1, game.getPlayer(0).sumTotalResource());
        assertEquals(0, game.getPlayer(0).getFaithPoints());

        Strongbox s = new Strongbox();
        game.removeDevelopmentCardProductionResource(1, s, 1);
        assertEquals(0, game.getPlayer(0).sumTotalResource());
        assertEquals(5, game.getPlayer(0).getFaithPoints());
        assertEquals(1, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(2, s.getNumOfResource(Resource.COIN));
    }

    /**
     * this test tries to activate basic production power if there aren't not enough resources
     */
    @Test
    void incorrectActivateBasicProductionPowerNotEnoughResource() throws AlreadyTakenNicknameException {

        Game game = new Game(4);
        game.createPlayer("Regina");
        Strongbox s = new Strongbox();
        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> game.basicProductionPower(Resource.COIN, Resource.COIN, Resource.STONE, s, 2));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of basic production power
     */
    @Test
    void correctActivateBasicProductionPower()
            throws AlreadyTakenNicknameException, InsufficientResourceException{

        Game game = new Game(4);
        game.createPlayer("Regina");
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.STONE);
        assertEquals(2, game.getPlayer(0).sumTotalResource());

        Strongbox s = new Strongbox();
        game.basicProductionPower(Resource.SHIELD, Resource.STONE, Resource.COIN, s, 1);
        assertEquals(0, game.getPlayer(0).sumTotalResource());
        assertEquals(1, s.getNumOfResource(Resource.COIN));
    }

    /**
     * this test tries to activate a not existing AdditionalProductionPowerCard production power
     */
    @Test
    void incorrectActivateAdditionalProductionPowerNoSuchCard() throws AlreadyTakenNicknameException {

        Game game = new Game(4);
        game.createPlayer("Regina");

        Strongbox s = new Strongbox();
        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> game.removeAdditionalProductionPowerCardResource(2, Resource.COIN, s, 1));

        String expectedMessage = "You don't have any card to activate this power";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to activate AdditionalProductionPowerCard production power if there aren't not enough resources
     */
    @Test
    void incorrectActivateAdditionalProductionPowerNotEnoughResource()
            throws AlreadyTakenNicknameException, InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        Game game = new Game(4);
        game.createPlayer("Regina");

        Cost c = new Cost();
        LeaderCard card = new AdditionalProductionPowerCard(Resource.STONE, c, 1, 0);
        game.selectPlayerLeaderCards(card, card, 0);
        game.activateLeaderCard(1);

        Strongbox s = new Strongbox();
        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> game.removeAdditionalProductionPowerCardResource(1, Resource.COIN, s, 2));

        String expectedMessage = "You don't have enough resources to do this operation.";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of AdditionalProductionPowerCard production power
     */
    @Test
    void correctActivateAdditionalProductionPower()
            throws AlreadyTakenNicknameException, InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException, NoSuchProductionPowerException {

        Game game = new Game(4);
        game.createPlayer("Regina");
        game.increaseWarehouse(Resource.STONE);
        assertEquals(1, game.getPlayer(0).sumTotalResource());

        Cost c = new Cost();
        LeaderCard card = new AdditionalProductionPowerCard(Resource.STONE, c, 1, 0);
        game.selectPlayerLeaderCards(card, card, 0);
        game.activateLeaderCard(1);

        Strongbox s = new Strongbox();
        game.removeAdditionalProductionPowerCardResource(1, Resource.COIN, s, 2);
        assertEquals(0, game.getPlayer(0).sumTotalResource());
        assertEquals(1, s.getNumOfResource(Resource.COIN));
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

        Game game = new Game(4);
        game.createPlayer("Daniele");
        game.createPlayer("Matteo");
        game.getPlayer(0).increaseFaithPoints(4);
        game.getPlayer(1).increaseFaithPoints(10);
        game.faithTrackMovementAllPlayers();
        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(2, game.getPlayer(1).getVictoryPoints().getVictoryPointsByVaticanReport());
        assertEquals(1, game.getPlayer(0).getVictoryPoints().getVictoryPointsByFaithTrack());
        assertEquals(4, game.getPlayer(1).getVictoryPoints().getVictoryPointsByFaithTrack());
    }

    /**
     * this test verifies if game is ended if one player have 7 developmentCards
     */
    @Test
    void sevenCardsWinner()
            throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, AlreadyTakenNicknameException {

        Game game = new Game(4);
        game.createPlayer("Giorgio");

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;

        DevelopmentCard developmentCard1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0, cardID);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard1, 1, 1);

        DevelopmentCard developmentCard2 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0, cardID);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard2, 2, 1);

        DevelopmentCard developmentCard3 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0, cardID);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard3, 3, 1);

        DevelopmentCard developmentCard4 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0, cardID);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard4, 1, 1);

        DevelopmentCard developmentCard5 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0, cardID);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard5, 2, 1);

        DevelopmentCard developmentCard6 = new DevelopmentCard(Color.BLUE, 3, c1, 1, c2, c3, 0, cardID);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard6, 1, 1);

        assertFalse(game.isEndGame());

        DevelopmentCard developmentCard7 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0, cardID);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard7, 3, 1);
        assertTrue(game.isEndGame());
    }

    /**
     * this test verifies iis ended if one player reach the end of FaithTrack
     */
    @Test
    void endFaithTrackWinner() throws AlreadyTakenNicknameException {

        Game game = new Game(4);
        game.createPlayer("Giorgio");

        game.getCurrentPlayer().increaseFaithPoints(10);
        game.faithTrackMovement();

        game.getCurrentPlayer().increaseFaithPoints(8);
        game.faithTrackMovement();

        game.getCurrentPlayer().increaseFaithPoints(7);
        game.faithTrackMovement();

        assertTrue(game.getFaithTrack().zeroRemainingPope());
        assertTrue(game.isEndGame());
    }

    /**
     * this test calculates the winner of the Game
     */
    @Test
    void endGameWinner()
            throws AlreadyTakenNicknameException, InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, NoSuchProductionPowerException {

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

        int winner = game.endGame();

        assertSame(2, winner);

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();
        int cardID = 0;
        c3.addResource(Resource.COIN, 2);
        DevelopmentCard developmentCard = new DevelopmentCard(Color.BLUE, 1, c1, 4, c2, c3, 0, cardID);
        game.nextPlayer();
        game.getPlayer(3).buyDevelopmentCard(developmentCard, 1, 1);

        Strongbox s = new Strongbox();
        game.getPlayer(3).activateDevelopmentCardProductionPower(1, s, 0);
        game.increaseCurrentPlayerStrongbox(s);

        assertEquals(4, game.getPlayer(2).sumVictoryPoints());
        assertEquals(4, game.getPlayer(3).sumVictoryPoints());

        assertEquals(0, game.getPlayer(2).sumTotalResource());
        assertEquals(2, game.getPlayer(3).sumTotalResource());

        int winner2 = game.endGame();

        assertSame(3, winner2);
    }

    /**
     * this test verifies the correct switch depots
     */
    @Test
    void correctSwitchDepots() throws AlreadyTakenNicknameException, ImpossibleSwitchDepotException {

        Game game = new Game(2);
        game.createPlayer("Daniele");
        game.increaseWarehouse(Resource.COIN);
        assertEquals(1, game.getCurrentPlayer().getWarehouse().getNumOfResource(Resource.COIN));
        game.switchDepots(1, 2);
        assertEquals(1, game.getCurrentPlayer().getWarehouse().getNumOfResource(Resource.COIN));
    }

    /**
     * this test verifies the correct get of currentPosition and numPlayers
     */
    @Test
    void getPositionAndNumPLayers(){

        Game game = new Game(2);
        assertEquals(0, game.getCurrentPosition());
        assertEquals(2, game.getNumOfPlayers());
    }
}