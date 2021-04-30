package it.polimi.ingsw.model.gamesTests;

import it.polimi.ingsw.exceptions.*;

import it.polimi.ingsw.model.developmentCards.*;
import it.polimi.ingsw.model.faithTrack.FaithTrack;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.model.leaderCards.*;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.model.resourceContainers.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
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
     * this test verifies the correct return of buyable DevelopmentCards by player
     */
    @Test
    void buyableDevelopmentCards() throws AlreadyTakenNicknameException, InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {

        Game game = new Game(3);
        game.createPlayer("Roberta");
        assertEquals(0, game.buyableDevelopmentCards().size());

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

        assertNotEquals(0, game.buyableDevelopmentCards().size());
        assertEquals(1, game.buyableDevelopmentCards().get(0).getLevel());
    }

    /**
     * this test tries to buy a DevelopmentCard if current player does not have enough resource
     */
    @Test
    void notEnoughResourceBuyDevelopmentCard() throws AlreadyTakenNicknameException {

        Game game = new Game(2);
        game.createPlayer("Roberta");

        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> game.buyDevelopmentCardFromMarket(0, 0, 1, 1));

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


        game.buyDevelopmentCardFromMarket(0, 0, 1, 1);

        game.increaseWarehouse(Resource.SERVANT);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.COIN);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);
        game.increaseWarehouse(Resource.SHIELD);

        ImpossibleDevelopmentCardAdditionException thrown =
                assertThrows(ImpossibleDevelopmentCardAdditionException.class, () -> game.buyDevelopmentCardFromMarket(0, 0, 1, 1));

        String expectedMessage = "Non puoi comprare questa carta";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verify the correct update of available slots after buy DevelomentCards
     */
    @Test
    void findRightSlotsAfterBuyDevelopmentCard()
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException, EmptyDevelopmentCardDeckException, ImpossibleDevelopmentCardAdditionException, AlreadyTakenNicknameException {

        Game game = new Game(2);
        game.createPlayer("Giorgia");

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

        game.buyDevelopmentCardFromMarket(0, 0, 1, 1);
        assertNotEquals(7, game.getPlayer(0).sumTotalResource());
        assertNotEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByCards());
        assertEquals(3, game.getDeck(0, 0).numberOfCards());
        assertNotSame(card, game.getDeck(0, 0).getFirstCard());
    }

    /**
     * this test verifies the correct buying of input DevelopmentCard
     */
    @Test
    void buyDevelopmentCardDirectly() throws AlreadyTakenNicknameException {

        Game game = new Game(3);
        game.createPlayer("Enrico");

        Cost c = new Cost();
        DevelopmentCard card = new DevelopmentCard(Color.GREEN, 1, c, 2, c, c, 1);

        assertEquals(0, game.getPlayer(0).getVictoryPoints().getVictoryPointsByCards());
        assertEquals(4, game.getDeck(0, 0).numberOfCards());

        game.buyDevelopmentCard(card, 1, 1);
        assertEquals(2, game.getPlayer(0).getVictoryPoints().getVictoryPointsByCards());
        assertEquals(3, game.getDeck(0, 0).numberOfCards());

        DevelopmentCard card2 = new DevelopmentCard(Color.GREEN, 2, c, 5, c, c, 1);
        game.buyDevelopmentCard(card2, 1, 1);
        assertEquals(7, game.getPlayer(0).getVictoryPoints().getVictoryPointsByCards());
        assertEquals(3, game.getDeck(1, 0).numberOfCards());
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
     * this test verifies the correct increase of current player faith points after activating production power
     */
    @Test
    void faithTrackMovementAfterActivateProduction() throws AlreadyTakenNicknameException, InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException, NoSuchProductionPowerException {

        Game game = new Game(2);
        game.createPlayer("Alessio");
        game.getPlayer(0).increaseWarehouse(Resource.COIN);
        game.getPlayer(0).increaseWarehouse(Resource.SERVANT);
        game.getPlayer(0).increaseWarehouse(Resource.SHIELD);
        game.getPlayer(0).increaseWarehouse(Resource.SHIELD);

        Cost c = new Cost();
        LeaderCard leaderCard1 = new AdditionalProductionPowerCard(Resource.SHIELD, c, 1);
        LeaderCard leaderCard2 = new AdditionalProductionPowerCard(Resource.SERVANT, c, 1);
        game.selectCurrentPlayerLeaderCards(leaderCard1, leaderCard2);
        game.activateLeaderCard(1);
        game.activateLeaderCard(2);

        PowerProductionPlayerChoice choice1 = new PowerProductionPlayerChoice();
        choice1.setBasicPower(Resource.COIN, Resource.SHIELD, Resource.STONE);
        game.activateProduction(choice1);
        assertEquals(0, game.getPlayer(0).getFaithPoints());

        PowerProductionPlayerChoice choice2 = new PowerProductionPlayerChoice();
        choice2.setFirstAdditionalPower(Resource.COIN);
        choice2.setSecondAdditionalPower(Resource.STONE);
        game.activateProduction(choice2);
        assertEquals(2, game.getPlayer(0).getFaithPoints());
    }

    /**
     * this test tries to activate a not existing DevelopmentCard production power
     */
    @Test
    void incorrectActivateStandardProductionPowerNoSuchCard() throws AlreadyTakenNicknameException {

        Game game = new Game(2);
        game.createPlayer("Regina");

        Strongbox s = new Strongbox();
        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> game.removeDevelopmentCardProductionResource(3, s, 2));

        String expectedMessage = "Non esistono carte per attivare questo potere di produzione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to activate DevelopmentCard production power if there aren't not enough resources
     */
    @Test
    void incorrectActivateStandardProductionPowerNotEnoughResource() throws AlreadyTakenNicknameException, InsufficientResourceException, ImpossibleDevelopmentCardAdditionException {

        Game game = new Game(2);
        game.createPlayer("Regina");

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(Resource.STONE, 1);
        Cost c3 = new Cost();

        DevelopmentCard card = new DevelopmentCard(Color.GREEN, 1,  c1, 1, c2, c3, 5);
        game.getPlayer(0).buyDevelopmentCard(card, 1, 1);

        Strongbox s = new Strongbox();
        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> game.removeDevelopmentCardProductionResource(1, s, 1));

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of DevelopmentCard production power
     */
    @Test
    void correctActivateStandardProductionPower()
            throws AlreadyTakenNicknameException, InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, NoSuchProductionPowerException {

        Game game = new Game(2);
        game.createPlayer("Regina");
        game.increaseWarehouse(Resource.STONE);
        assertEquals(1, game.getPlayer(0).sumTotalResource());

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        c2.addResource(Resource.STONE, 1);
        Cost c3 = new Cost();
        c3.addResource(Resource.COIN, 2);

        DevelopmentCard card = new DevelopmentCard(Color.GREEN, 1,  c1, 1, c2, c3, 5);
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

        Game game = new Game(2);
        game.createPlayer("Regina");
        Strongbox s = new Strongbox();
        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> game.basicProductionPower(Resource.COIN, Resource.COIN, Resource.STONE, s, 2));

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of basic production power
     */
    @Test
    void correctActivateBasicProductionPower()
            throws AlreadyTakenNicknameException, InsufficientResourceException{

        Game game = new Game(2);
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

        Game game = new Game(2);
        game.createPlayer("Regina");

        Strongbox s = new Strongbox();
        NoSuchProductionPowerException thrown =
                assertThrows(NoSuchProductionPowerException.class, () -> game.removeAdditionalProductionPowerCardResource(2, Resource.COIN, s, 1));

        String expectedMessage = "Non esistono carte per attivare questo potere di produzione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test tries to activate AdditionalProductionPowerCard production power if there aren't not enough resources
     */
    @Test
    void incorrectActivateAdditionalProductionPowerNotEnoughResource()
            throws AlreadyTakenNicknameException, InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException {

        Game game = new Game(2);
        game.createPlayer("Regina");

        Cost c = new Cost();
        LeaderCard card = new AdditionalProductionPowerCard(Resource.STONE, c, 1);
        game.selectCurrentPlayerLeaderCards(card, card);
        game.activateLeaderCard(1);

        Strongbox s = new Strongbox();
        InsufficientResourceException thrown =
                assertThrows(InsufficientResourceException.class, () -> game.removeAdditionalProductionPowerCardResource(1, Resource.COIN, s, 2));

        String expectedMessage = "Non hai abbastanza risorse per effettuare questa operazione";
        String actualMessage = thrown.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    /**
     * this test verifies the correct activation of AdditionalProductionPowerCard production power
     */
    @Test
    void correctActivateAdditionalProductionPower()
            throws AlreadyTakenNicknameException, InsufficientResourceException, InsufficientCardsException, ActiveLeaderCardException, AlreadyDiscardLeaderCardException, NoSuchProductionPowerException {

        Game game = new Game(2);
        game.createPlayer("Regina");
        game.increaseWarehouse(Resource.STONE);
        assertEquals(1, game.getPlayer(0).sumTotalResource());

        Cost c = new Cost();
        LeaderCard card = new AdditionalProductionPowerCard(Resource.STONE, c, 1);
        game.selectCurrentPlayerLeaderCards(card, card);
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

        FaithTrack.resetFaithTrack();
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

        FaithTrack.resetFaithTrack();
        Game game = new Game(2);
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

        Game game = new Game(2);
        game.createPlayer("Giorgio");

        Cost c1 = new Cost();
        Cost c2 = new Cost();
        Cost c3 = new Cost();

        DevelopmentCard developmentCard1 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard1, 1, 1);

        DevelopmentCard developmentCard2 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard2, 2, 1);

        DevelopmentCard developmentCard3 = new DevelopmentCard(Color.BLUE, 1, c1, 1, c2, c3, 0);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard3, 3, 1);

        DevelopmentCard developmentCard4 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard4, 1, 1);

        DevelopmentCard developmentCard5 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard5, 2, 1);

        DevelopmentCard developmentCard6 = new DevelopmentCard(Color.BLUE, 3, c1, 1, c2, c3, 0);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard6, 1, 1);

        assertFalse(game.isEndGame());

        DevelopmentCard developmentCard7 = new DevelopmentCard(Color.BLUE, 2, c1, 1, c2, c3, 0);
        game.getCurrentPlayer().buyDevelopmentCard(developmentCard7, 3, 1);
        assertTrue(game.isEndGame());
    }

    /**
     * this test verifies iis ended if one player reach the end of FaithTrack
     */
    @Test
    void endFaithTrackWinner() throws AlreadyTakenNicknameException {

        FaithTrack.resetFaithTrack();
        Game game = new Game(4);
        game.createPlayer("Giorgio");

        game.getCurrentPlayer().increaseFaithPoints(10);
        game.faithTrackMovement();

        game.getCurrentPlayer().increaseFaithPoints(8);
        game.faithTrackMovement();

        game.getCurrentPlayer().increaseFaithPoints(7);
        game.faithTrackMovement();

        assertTrue(FaithTrack.getFaithTrack().zeroRemainingPope());
        assertTrue(game.isEndGame());
    }

    /**
     * this test calculates the winner of the Game
     */
    @Test
    void endGameWinner()
            throws AlreadyTakenNicknameException, InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, NoSuchProductionPowerException {

        FaithTrack.resetFaithTrack();
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