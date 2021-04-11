package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.*;

import java.util.ArrayList;

public abstract class Game {

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
    public Game(int numOfPlayers) {
        market = new Market();
        faithTrack = new FaithTrack();
        this.numOfPlayers = numOfPlayers;
        currentPlayer = 0;
        leaderCards = new ArrayList<>(16);
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

    public abstract void startGame();

    /**
     * shift current player to his subsequent or return to first player
     */
    public void nextPlayer() {
        if(currentPlayer < numOfPlayers)
            currentPlayer++;
        else
            currentPlayer = 0;
    }

    /**
     * @param card1 is the LeaderCard chosen by current player
     * @param card2 is the LeaderCard chosen by current player
     */
    public void selectCurrentPlayerLeaderCards(LeaderCard card1, LeaderCard card2){
        players.get(currentPlayer).addLeaderCard(card1);
        players.get(currentPlayer).addLeaderCard(card2);
    }

    /**
     * @param isRow is true if current player has selected a market row, is false if current player has selected a market column
     * @param choice is current player's choice of market row or column
     * @throws WrongParametersException if current player has inserted wrong parameters
     */
    public void takeMarketMarble(boolean isRow, int choice) throws WrongParametersException {
        Marble [] marbles;
        Marble chosenMarble;
        if(isRow) {
            marbles = market.getRowMarbles(choice);
        }
        else {
            marbles = market.getColumnMarbles(choice);
        }
        for(int i = 0; i < marbles.length; i++){
            chosenMarble = askPlayerChosenMarble(marbles);
            chosenMarble.useMarble(this);
        }
    }

    public Marble askPlayerChosenMarble(Marble[] marbles){
        return marbles[0];
    }

    /**
     * @param resource stands for the type of resource to increase by 1 in current player Warehouse.
     * @return 1 if @param resource has to be discarded, otherwise @return 0
     */
    public int increaseWarehouse(Resource resource){
        if(players.get(currentPlayer).increaseWarehouse(resource))
            return 0;
        return 1;
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
     * @throws InsufficientResourceException if player has not enough resources
     * @throws ImpossibleDevelopmentCardAdditionException if is not possible to add the card in player's slots
     * @throws EmptyDevelopmentCardDeckException if player has chosen an empty deck
     * this method firstly verifies if buying is possible and then remove DevelopmentCard from the deck
     * where it was contained.
     */
    public void buyDevelopmentCard(int deckRow, int deckColumn, int choice)
            throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, EmptyDevelopmentCardDeckException {
        DevelopmentCard card = deck[deckRow][deckColumn].getFirstCard();
        /*
         verifies that the deck is not empty and get the first card
         */

        PlayerBoard player = players.get(currentPlayer);
        if(!(player.isBuyable(card)))
            throw new InsufficientResourceException();
            /*
            evaluates if player has enough resources
            */

        ArrayList <Integer> slots = player.findAvailableSlot(card);
        if (slots.size() == 0)
            throw new ImpossibleDevelopmentCardAdditionException();
            /*
            evaluates if player has at least one available slot to insert the card
            */

        else if(slots.size() == 1) {
            player.buyDevelopmentCard(card, slots.get(0), choice);
            deck[deckRow][deckColumn].removeDevelopmentCard();
            /*
             if there is only one, directly insert in it
             */
        }
        else {
            int slot = askSlotToPlayer(slots);
            player.buyDevelopmentCard(card, slot, choice);
            deck[deckRow][deckColumn].removeDevelopmentCard();
            /*
             ask player to indicate one of available slots and then insert the card in the chosen slot
             */
        }
    }

    private int askSlotToPlayer(ArrayList<Integer> slots){
        return slots.get(0);
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
            faithTrackMovement(currentPlayer);
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
        players.get(currentPlayer).activateLeaderCard(chosenLeaderCard);
    }

    /**
     * @param chosenLeaderCard is current player's choice about which leader card to discard
     * @throws AlreadyDiscardLeaderCardException if current player already discard this LeaderCard previously
     * @throws ActiveLeaderCardException if chosen LeaderCard is already active
     */
    public void discardLeaderCard(int chosenLeaderCard)
            throws ActiveLeaderCardException, AlreadyDiscardLeaderCardException {
        players.get(currentPlayer).discardLeaderCard(chosenLeaderCard);
        faithTrackMovement(currentPlayer);
    }

    public void faithTrackMovement(int chosenPlayer){}

    public void faithTrackMovementExceptCurrentPlayer(){}

    public void verifyPopeSectionExceptCurrentPlayer(){}

    /**
     * @return 1 if white marble has to be discarded, otherwise @return 0 if successfully increased to current player
     * Warehouse or if white marble is not converted at all.
     * this method verifies if exist WhiteConversionCard active for current player and in case convert white marble.
     */
    public int whiteConversion(){
        Resource r1 = players.get(currentPlayer).whiteConversion(0);
        Resource r2 = players.get(currentPlayer).whiteConversion(1);
        int discardMarble = 0;
        if(r1 != Resource.WHITE && r2 != Resource.WHITE){
            Resource chosen = askWhiteMarbleResourceConversionToPlayer(r1, r2);
            discardMarble = increaseWarehouse(chosen);
            /*
             exist 2 active WhiteConversionCard for current player. Ask to the player to indicate to which resource wants to convert.
             Then increase chosen resource to current player Warehouse. If the increase is not successfully discardMarble = 1,
             otherwise discardMarble = 0.
             */
        }
        else if(r1 != Resource.WHITE)
            discardMarble = increaseWarehouse(r1);
        else if(r2 != Resource.WHITE)
            discardMarble = increaseWarehouse(r2);
        /*
         player has 1 active WhiteConversionCard. Directly increase converted resource to current player Warehouse.
         if the increase is not successfully discardMarble = 1, otherwise discardMarble = 0.
         */

        return discardMarble;
        /*
         if player has no active WhiteConversionCard discardMarble remains 0
         */
    }

    private Resource askWhiteMarbleResourceConversionToPlayer(Resource r1, Resource r2){
        return r1;
    }

    public SimplePlayerBoard endGame(){
        return players.get(0);
    }
}
