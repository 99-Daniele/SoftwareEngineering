package it.polimi.ingsw.model;

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
            currentPlayer = 1;
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

    public void buyDevelopmentCard(int deckRow, int deckColumn, int chosenDevelopmentCardSlot,int choice){}

    public void activateProduction(){}

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
