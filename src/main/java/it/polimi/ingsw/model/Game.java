package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.EmptyDevelopmentCardDeckException;
import it.polimi.ingsw.exceptions.ImpossibleDevelopmentCardAdditionException;
import it.polimi.ingsw.exceptions.ImpossibleSwitchDepotException;
import it.polimi.ingsw.exceptions.InsufficientResourceException;

import java.util.ArrayList;

public abstract class Game {

    private ArrayList<PlayerBoard> players = new ArrayList<>();
    private Market market;
    private FaithTrack faithTrack;
    private Deck[][] deck = new Deck[3][4];
    private final int numOfPlayers;
    private int currentPlayer;
    private ArrayList<LeaderCard> leaderCards;

    public Game(int numOfPlayers) {
        market = new Market();
        faithTrack = new FaithTrack();
        this.numOfPlayers = numOfPlayers;
        currentPlayer = 1;
        leaderCards = new ArrayList<>(16);
    }

    public void createPlayer(String nickname){    }

    private boolean isNicknameTaken(String nickname){
        return false;
    }

    public abstract void startGame();

    public void nextPlayer() {
        if(currentPlayer < numOfPlayers)
            currentPlayer++;
        else
            currentPlayer = 0;
    }

    public void selectCurrentPlayerLeaderCards(LeaderCard card1, LeaderCard card2){}

    public Marble[] takeMarketMarble(boolean isRow, int choice){
        return new Marble[1];
    }

    public void selectMarble(Marble marble){}

    public void selectWhiteMarbleResourceConversion(Resource resource){}

    public int increaseWarehouse(Resource resource){
        return 0;
    }

    public void switchDepots(int Depot1, int Depot2){}

    /**
     * @param deckRow is the row of the chosen deck
     * @param deckColumn is the column of the chosen deck
     * @param choice is player's choice about which between warehouse and strongbox has the priority to be decreased
     * @throws InsufficientResourceException if player has not enough resources
     * @throws ImpossibleDevelopmentCardAdditionException if is not possible to add the card in player's slots
     * @throws EmptyDevelopmentCardDeckException if player has chosen an empty deck
     * this method firstly verifies that the deck is not empty and, if yes, get the first card.
     * then evaluates if player has enough resources.
     * then evaluates if player has at least one available slot to insert the card.
     * if there is only one, directly insert in it, otherwise ask player to indicate one of available slots and then
     * insert the card in the chosen slot
     */
    public void buyDevelopmentCard(int deckRow, int deckColumn, int choice)
            throws InsufficientResourceException, ImpossibleDevelopmentCardAdditionException, EmptyDevelopmentCardDeckException {
        DevelopmentCard card = deck[deckRow][deckColumn].getFirstCard();
        PlayerBoard player = players.get(currentPlayer);
        ArrayList <Integer> slots = new ArrayList<>();
        if(!(player.isBuyable(card)))
            throw new InsufficientResourceException();
        player.findAvailableSlot(card, slots);
        if (slots.size() == 0)
            throw new ImpossibleDevelopmentCardAdditionException();
        else if(slots.size() == 1) {
            player.buyDevelopmentCard(card, slots.get(0), choice);
            deck[deckRow][deckColumn].removeDevelopmentCard();
        }
        else {
            int slot = askSlotToPlayer(slots);
            player.buyDevelopmentCard(card, slot, choice);
            deck[deckRow][deckColumn].removeDevelopmentCard();
        }
    }

    private int askSlotToPlayer(ArrayList<Integer> slots){
        return slots.get(0);
    }

    /**
     * @param choice summarize all player's choices about which production powers activate
     * @throws InsufficientResourceException if player has not enough resources to activate all production powers together
     * @throws ImpossibleSwitchDepotException by the signature of enoughTotalProductionPowerResource()
     * this method firstly evaluates if current player has enough resources to activate all production powers together,
     * then, if yes, activate them and if player increased his faith points, calls faithTrackMovement
     */
    public void activateProduction(PowerProductionPlayerChoice choice)
            throws InsufficientResourceException, ImpossibleSwitchDepotException {
        players.get(currentPlayer).enoughTotalProductionPowerResource(choice);
        if(players.get(currentPlayer).activateProduction(choice))
            faithTrackMovement(currentPlayer);
    }

    public void activateLeaderCard(int chosenLeaderCard){}

    public void discardLeaderCard(int chosenLeaderCard){}

    public void faithTrackMovement(int chosenPlayer){}

    public void faithTrackMovementExceptCurrentPlayer(){}

    public void verifyPopeSectionExceptCurrentPlayer(){}

    public void whiteConversion(){}

    public SimplePlayerBoard endGame(){
        return players.get(0);
    }
}
