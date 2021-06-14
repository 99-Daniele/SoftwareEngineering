package it.polimi.ingsw.view.modelView;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

public class GameView {

    private ArrayList<PlayerBoardView> players = new ArrayList<>();
    private FaithTrackView faithTrack;
    private MarketView market;
    private DecksView decks;
    private ArrayList<Marble> chosenMarbles;
    private boolean startGame;

    public void setPlayers(ArrayList<String> players){
        this.players = new ArrayList<>();
        for (String player: players)
            addPlayer(player);
        initFaithTrack();
        startGame = false;
    }

    public boolean isStartGame() {
        return startGame;
    }

    public void startGame() {
        startGame = true;
    }

    public ArrayList<String> getNickNames(){
        ArrayList<String> nickNames = new ArrayList<>();
        for(PlayerBoardView player: players)
            nickNames.add(player.getNickName());
        if(players.size() == 1)
            nickNames.add("LorenzoIlMagnifico");
        return nickNames;
    }

    public int getNumOfPlayers(){
        return players.size();
    }

    public PlayerBoardView getPlayer(int player){
        return players.get(player);
    }

    public String getNickname(int player){
        return players.get(player).getNickName();
    }

    public void addPlayer(String newPlayer){
        players.add(new PlayerBoardView(newPlayer));
    }

    public FaithTrackView getFaithTrack() {
        return faithTrack;
    }

    public MarketView getMarket() {
        return market;
    }

    public DecksView getDecks() {
        return decks;
    }

    public WarehouseStrongboxView getWarehouseStrongbox(int viewID){
        return players.get(viewID).getWarehouseStrongboxView();
    }

    public CardsSlotsView getSlotCards(int viewID){
        return players.get(viewID).getCardsSlotsView();
    }

    public void setMarket(MarketView market){
        this.market = market;
    }

    public void setFirstDeckCards(ArrayList<Integer> cards){
        decks = new DecksView(cards);
    }

    public void initFaithTrack(){
        if(players.size() > 1)
            faithTrack = new FaithTrackView(players.size(), getNickNames());
        else
            faithTrack = new FaithTrackView(2, getNickNames());
    }

    public void increaseFaithPoints(int viewID, int faithPoints){
        faithTrack.increaseFaithPoints(viewID, faithPoints);
    }

    public void increaseVictoryPoints(int viewID, int victoryPoints){
        faithTrack.increaseVictoryPoints(viewID, victoryPoints);
    }

    public void setChosenMarbles(ArrayList<Marble> marbles){
        chosenMarbles = marbles;
    }

    public ArrayList<Marble> getChosenMarbles(){
        return chosenMarbles;
    }

    public void slideRow(int selectedRow){
        market.slideRow(selectedRow);
    }

    public void slideColumn(int selectedColumn) {
        market.slideColumn(selectedColumn);
    }

    public void replaceCard(int row, int column, int cardID){
        decks.replaceCard(row, column, cardID);
    }

    public int[] getRowColumn(int cardID){
        return decks.getRowColumn(cardID);
    }

    public String getNickName(int viewID) {
        return players.get(viewID).getNickName();
    }

    public void increaseWarehouse(int viewID, Resource resource, int depot){
        players.get(viewID).increaseWarehouse(resource, depot-1);
    }

    public void newAmount(int viewID, Resource resource, int warehouseAmount, int strongboxAmount){
        players.get(viewID).newAmount(resource, warehouseAmount, strongboxAmount);
    }

    public void switchDepot(int viewID, int depot1, int depot2) {
        players.get(viewID).switchDepot(depot1-1, depot2-1);
    }

    public void addExtraDepot(int viewID, Resource r){
        players.get(viewID).addExtraDepot(r);
    }

    public void setMyLeaderCards(int viewID, int firstLeaderCard, int secondLeaderCard){
        players.get(viewID).setLeaderCards(firstLeaderCard, secondLeaderCard);
    }

    public ArrayList<Integer> getDevelopmentCards(int viewID){
        return players.get(viewID).getDevelopmentCards();
    }

    public ArrayList<Integer> getLeaderCards(int viedID){
        return players.get(viedID).getLeaderCards();
    }

    public void addLeaderCard(int viewID, int leaderCard){
        players.get(viewID).addLeaderCard(leaderCard);
    }

    public boolean isLeaderCardActive(int player, int leaderCard){
        return players.get(player).isLeaderCardActive(leaderCard);
    }

    public boolean isSlotEmpty(int viewID, int slot){
        return players.get(viewID).getCardsSlotsView().isSlotEmpty(slot);
    }

    public void addDevelopmentCard(int viewID, int cardID, int slot){
        players.get(viewID).addDevelopmentCard(cardID, slot);
    }

    public void discardLeaderCard(int viewID, int chosenLeaderCard){
        players.get(viewID).discardLeaderCard(chosenLeaderCard);
    }

    public ArrayList<ResourceContainerView> getWarehouse(int viewID){
        return players.get(viewID).getWarehouse();
    }

    public int coinAmount(int viewID){
        return players.get(viewID).coinAmount();
    }

    public int shieldAmount(int viewID){
        return players.get(viewID).shieldAmount();
    }

    public int stoneAmount(int viewID){
        return players.get(viewID).stoneAmount();
    }

    public int servantAmount(int viewID){
        return players.get(viewID).servantAmount();
    }

    public int getFaithPoints(int viewID){return faithTrack.getFaithPoints(viewID);}

    public int getVictoryPoints(int viewID){return faithTrack.getVictoryPoints(viewID);}

    public int getCurrentPope(){return faithTrack.getCurrentPope();}
}
