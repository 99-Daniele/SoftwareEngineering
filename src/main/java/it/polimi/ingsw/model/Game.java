package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public abstract class Game implements LightGame{

    private ArrayList<PlayerBoard> players = new ArrayList<>();
    private Market market;
    private FaithTrack faithTrack;
    private Deck[][] deck = new Deck[3][4];
    private final int numOfPlayers;
    private int currentPlayer;
    private ArrayList<LeaderCard> leaderCards;

    /**
     * @param numOfPlayers is the chosen number of players
     */
    public Game(int numOfPlayers){
        market = new Market();
        faithTrack =new FaithTrack();
        this.numOfPlayers = numOfPlayers;
        currentPlayer = 0;
        createDecks();
        leaderCards = new ArrayList<>(16);
        createLeaderCards();
    }

    /**
     * this method method creates all 12 decks and all 48 DevelopmentCards parsing by Json file.
     * then add each card to is correct deck and prepare all decks.
     */
    private void createDecks(){
        deck[0][0]=new Deck(Color.GREEN,1);
        deck[0][1]=new Deck(Color.PURPLE,1);
        deck[0][2]=new Deck(Color.BLUE,1);
        deck[0][3]=new Deck(Color.YELLOW,1);
        deck[1][0]=new Deck(Color.GREEN,2);
        deck[1][1]=new Deck(Color.PURPLE,2);
        deck[1][2]=new Deck(Color.BLUE,2);
        deck[1][3]=new Deck(Color.YELLOW,2);
        deck[2][0]=new Deck(Color.GREEN,3);
        deck[2][1]=new Deck(Color.PURPLE,3);
        deck[2][2]=new Deck(Color.BLUE,3);
        deck[2][3]=new Deck(Color.YELLOW,3);
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader("src/main/resources/DevelopmentCards.json"));
            DevelopmentCard[] developmentCards = gson.fromJson(reader, DevelopmentCard[].class);
            int i = 0;
            for(int j = 0; j < 3; j++){
                for (int k = 0; k < 4; k++){
                    for(int h = 0; h < 4; h++) {
                        deck[j][k].addDevelopmentCard(developmentCards[i]);
                        i++;
                    }
                    deck[j][k].prepareDeck();
                }
            }
        } catch (IOException | WrongDevelopmentCardInsertionException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method creates the list of 16 LeaderCards by paring Json File
     */
    private void createLeaderCards(){
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Deck getDeck(int line, int column) {
        return deck[line][column];
    }

    /**
     * @param color is the color of DevelopmentCards contained in deck
     * @return the first not empty deck which contains DevelopmentCards of @param color, or the last deck of same kind of cards.
     */
    public Deck getColorDeck(Color color){
        int i = 0;
        while(deck[0][i].getColor() != color)
            i++;
        if (!deck[0][i].isEmpty())
            return deck[0][i];
        if(!deck[1][i].isEmpty())
            return deck[1][i];
        return deck[2][i];
    }

    /**
     * @return true if there are 0 remaining DevelopmentCards of one kind of color. In this case, SinglePlayerGame finish.
     */
    public boolean zeroRemainingColorCards(){
        for(int i = 0; i < 4; i++) {
            if (deck[0][i].isEmpty() && deck[1][i].isEmpty() && deck[2][i].isEmpty())
                return true;
        }
        return false;

    }

    /**
     * @return arraylist of 4 leader cards
     */
    public synchronized ArrayList<LeaderCard> possibleCardLeader() {
        ArrayList<LeaderCard> NewLeaderCards=new ArrayList<>();
        for(int count=0; count<4; count++)
        {
            int i = (int) (Math.random() * leaderCards.size());
            NewLeaderCards.add(leaderCards.remove(i));
        }
        return NewLeaderCards;
    }

    /**
     * @param nickname is player chosen nickname
     * @throws AlreadyTakenNicknameException if @param nickname is already taken by another player
     */
    public void createPlayer(String nickname) throws AlreadyTakenNicknameException {
        if(isNicknameTaken(nickname))
            throw new AlreadyTakenNicknameException();
        PlayerBoard player = new PlayerBoard(nickname);
        players.add(player);
    }

    /**
     * @param nickname is player chosen nickname
     * @return true if @param nickname is already taken by another player
     */
    private boolean isNicknameTaken(String nickname){
        for(PlayerBoard player: players)
            if(player.getNickname().equals(nickname))
                return true;
        return false;
    }

    /**
     * shift current player to his subsequent or return to first player
     */
    public void nextPlayer() {
        if(currentPlayer < numOfPlayers -1)
            currentPlayer++;
        else
            currentPlayer = 0;
    }

    /**
     * @param card1 is the LeaderCard chosen to be added current PlayerBoard
     * @param card2 is the LeaderCard chosen to be added current PlayerBoard
     */
    public void selectCurrentPlayerLeaderCards(LeaderCard card1, LeaderCard card2){
        players.get(currentPlayer).addLeaderCard(card1, card2);
    }

    /**
     * @return the two actives LeaderCards of current player
     * @throws AlreadyDiscardLeaderCardException if chosen LeaderCArd was previously discarded
     */
    public LeaderCard[] getCurrentPlayerActiveLeaderCards() throws AlreadyDiscardLeaderCardException {
        LeaderCard[] currentPlayerLeaderCards = new LeaderCard[2];
        currentPlayerLeaderCards[0] = players.get(currentPlayer).getLeaderCard(1);
        currentPlayerLeaderCards[1] = players.get(currentPlayer).getLeaderCard(2);
        return currentPlayerLeaderCards;
    }

    /**
     * @param isRow is true if current player has selected a market row, is false if current player has selected a market column
     * @param choice is current player's choice of market row or column
     * @throws WrongParametersException if current player has inserted wrong parameters
     * this method return marbles selected by players and ask the order to play them.
     * at the end if there are any discarded marbles, increase faith points of other players by the amount of them and
     * calls fatihTrackMovementExceptCurrentPlayer() method
     */
    public Marble[] takeMarketMarble(boolean isRow, int choice) throws WrongParametersException {
        Marble [] marbles;
        if(isRow) {
            marbles = market.getRowMarbles(choice);
            market.slideRow(choice);
        }
        else {
            marbles = market.getColumnMarbles(choice);
            market.slideColumn(choice);
        }
        return marbles;
    }

    /**
     * @param chosenLeaderCard is one player's LeaderCard
     * @return true if the chosen LeaderCard is an active WhiteConversionCard
     */
    @Override
    public boolean isActiveWhiteConversionCard(int chosenLeaderCard) {
        return players.get(currentPlayer).isWhiteConversionLeaderCardActive(chosenLeaderCard);
    }

    /**
     * @param chosenLeaderCard is one player's LeaderCard
     * @return current player chosen LeaderCard
     */
    @Override
    public LeaderCard getCurrentPlayerLeaderCard(int chosenLeaderCard) throws AlreadyDiscardLeaderCardException {
        return players.get(currentPlayer).getLeaderCard(chosenLeaderCard);
    }
    /**
     * @param resource stands for the type of resource to increase by 1 in Warehouse.
     * @return false if @param resource has to be discarded
     */
    @Override
    public boolean increaseWarehouse(Resource resource) {
        return players.get(currentPlayer).increaseWarehouse(resource);
    }

    /**
     * @param leaderCard is the chosen WhiteConversionCard to convert white marble
     * this method try to increase current player warehouse by 1 LeaderCard resource. If it isn't possible
     * increase other players faith points by 1.
     */
    @Override
    public void whiteMarbleConversion(LeaderCard leaderCard){
        if(!(increaseWarehouse(leaderCard.getResource())))
            increaseOneFaithPointOtherPlayers();
    }

    @Override
    public void increaseOneFaithPointOtherPlayers() {
        for(int i = 0; i < numOfPlayers; i++) {
            if (i != currentPlayer)
                players.get(i).increaseFaithPoints(1);
        }
    }

    @Override
    public void increaseOneFaithPointCurrentPlayer() {
        players.get(currentPlayer).increaseFaithPoints(1);
    }

    /**
     * @param depot1 stands for position of the first Depot in current player Warehouse to switch.
     * @param depot2 stands for position of the second Depot in current player Warehouse to switch.
     * @throws ImpossibleSwitchDepotException if the switch is not possible.
     */
    public void switchDepots(int depot1, int depot2) throws ImpossibleSwitchDepotException {
        players.get(currentPlayer).switchDepots(depot1, depot2);
    }

    /**
     * @param deckRow is the row of the chosen deck
     * @param deckColumn is the column of the chosen deck
     * @param choice is player's choice about which between warehouse and strongbox has the priority to be decreased
     * @param slot is player's choice about in which slot want to insert the chosen card
     * @throws InsufficientResourceException if player has not enough resources
     * @throws ImpossibleDevelopmentCardAdditionException if is not possible to add the card in player's slots
     * @throws EmptyDevelopmentCardDeckException if player has chosen an empty deck
     * this method firstly verifies if buying is possible and then remove DevelopmentCard from the deck
     * where it was contained.
     */
    public void buyDevelopmentCard(int deckRow, int deckColumn, int choice, int slot)
            throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, EmptyDevelopmentCardDeckException {
        DevelopmentCard card = deck[deckRow][deckColumn].getFirstCard();
        if(!(players.get(currentPlayer).isBuyable(card)))
            throw new InsufficientResourceException();
        players.get(currentPlayer).buyDevelopmentCard(card, slot, choice);
        deck[deckRow][deckColumn].removeDevelopmentCard();
    }

    /**
     * @param deckRow is the row of the chosen deck
     * @param deckColumn is the column of the chosen deck
     * @return an ArrayList </Integer> of slots where @card can be inserted
     * @throws EmptyDevelopmentCardDeckException if player has chosen an empty deck
     * @throws InsufficientResourceException if player has not enough resources
     */
    private ArrayList<Integer> findAvailableSlots(int deckRow, int deckColumn)
            throws EmptyDevelopmentCardDeckException, InsufficientResourceException {
        DevelopmentCard card = deck[deckRow][deckColumn].getFirstCard();
        if(!(players.get(currentPlayer).isBuyable(card)))
            throw new InsufficientResourceException();
        return players.get(currentPlayer).findAvailableSlot(card);
    }

    /**
     * @param choice summarize all player's choices about which production powers activate
     * @throws InsufficientResourceException if player has not enough resources to activate all production powers together
     * @throws ImpossibleSwitchDepotException by the signature of enoughTotalProductionPowerResource()
     * this method activate all production powers together and if player has increased his faith points, calls faithTrackMovement
     */
    public void activateProduction(PowerProductionPlayerChoice choice)
            throws InsufficientResourceException, ImpossibleSwitchDepotException {
        if(players.get(currentPlayer).activateProduction(choice))
            faithTrackMovement();
    }

    /**
     * @param chosenLeaderCard is current player's choice about which leader card to activate
     * @throws InsufficientResourceException if current player has not enough resources
     * @throws InsufficientCardsException if current player has not enough cards
     * @throws AlreadyDiscardLeaderCardException if current player already discard this LeaderCard previously
     * @throws ActiveLeaderCardException if chosen LeaderCard is already active
     */
    public void activateLeaderCard(int chosenLeaderCard)
            throws InsufficientResourceException, AlreadyDiscardLeaderCardException, ActiveLeaderCardException, InsufficientCardsException {
        players.get(currentPlayer).activateLeaderCard(chosenLeaderCard );
    }

    /**
     * @param chosenLeaderCard is current player's choice about which leader card to discard
     * @throws AlreadyDiscardLeaderCardException if current player already discard this LeaderCard previously
     * @throws ActiveLeaderCardException if chosen LeaderCard is already active
     */
    public void discardLeaderCard(int chosenLeaderCard)
            throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException {
        players.get(currentPlayer).discardLeaderCard(chosenLeaderCard);
        faithTrackMovement();
    }

    /**
     * method that sets the current player's victory points related to the position in the Faith Track and controls if
     * the player is in or beyond the pope space and if it's true it increases the victory points of all the players in the vatican section
     */
    @Override
    public void faithTrackMovement(){
        faithTrack.victoryPointsFaithTrack(players.get(currentPlayer).getVictoryPoints(), players.get(currentPlayer).getFaithPoints());
        if(faithTrack.reachPope(players.get(currentPlayer).getFaithPoints()))
        {
            for(PlayerBoard player:players)
            {
                faithTrack.victoryPointsVaticanReport(player.getVictoryPoints(), player.getFaithPoints());
            }
            faithTrack.DecreaseRemainingPope();
        }
    }

    /**
     * @param position the position in the arraylist
     * @return PlayerBoard in @param position
     */
    public PlayerBoard getPlayer(int position){
        return players.get(position);
    }

    /**
     * @return PlayerBoard in current position
     */
    public PlayerBoard getCurrentPlayer(){
        return players.get(currentPlayer);
    }

    public FaithTrack getFaithTrack(){
        return faithTrack;
    }

    /**
     * method that sets the victory points related to the position in the Faith Track of all the players and controls if
     * the players are in or beyond the pope space and if it's true it increases the victory points of all the players in the vatican section
     */
    public void faithTrackMovementAllPlayer(){
        int flag=0;
        for(PlayerBoard player: players)
        {
                faithTrack.victoryPointsFaithTrack(player.getVictoryPoints(), player.getFaithPoints());
                if (faithTrack.reachPope(player.getFaithPoints()))
                    flag=1;
        }
        if (flag==1)
        {
            for (PlayerBoard player: players)
                faithTrack.victoryPointsVaticanReport(player.getVictoryPoints(), player.getFaithPoints());
            faithTrack.DecreaseRemainingPope();
        }
    }

    /**
     * @return the winner of the game
     * this method find the player with more victory points. In case more players have both max victory points, find
     * which one has more amount of resources.
     */
    public PlayerBoard endGame() {
        int maxVictoryPoints = 0;
        int maxNumOfResources = 0;
        PlayerBoard winner = players.get(0);
        for (PlayerBoard player : players) {
            if (player.sumVictoryPoints() > maxVictoryPoints) {
                maxVictoryPoints = player.sumVictoryPoints();
                maxNumOfResources = player.sumTotalResource();
                winner = player;
            }
            else if(player.sumVictoryPoints() == maxVictoryPoints){
                if(player.sumTotalResource() > maxNumOfResources){
                    maxNumOfResources = player.sumTotalResource();
                    winner = player;
                }
            }
        }
        return winner;
    }
}
