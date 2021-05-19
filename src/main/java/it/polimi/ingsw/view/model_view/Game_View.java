package it.polimi.ingsw.view.model_view;

import it.polimi.ingsw.model.resourceContainers.Resource;

import java.util.ArrayList;

public class Game_View {

    private ArrayList<PlayerBoard_View> players;
    private FaithTrack_View faithTrack;
    private Market_View market;
    private Decks_View decks;

    public Game_View(String myPlayer){
        players = new ArrayList<>();
        players.add(new PlayerBoard_View(myPlayer));
    }

    public void addPlayer(String newPlayer){
        players.add(new PlayerBoard_View(newPlayer));
    }

    public void removePlayer(String deletedPlayer){
        players.removeIf(player -> player.getNickName().equals(deletedPlayer));
    }

    public void orderPlayer(ArrayList<String> nickNames){
        ArrayList<PlayerBoard_View> orderedPlayers = new ArrayList<>();
        for (int i = 0; i < players.size(); i++){
            for (PlayerBoard_View player: players) {
                if (player.getNickName().equals(nickNames.get(i)))
                    orderedPlayers.add(player);
            }
        }
        players = orderedPlayers;
    }

    public void setMarket(Market_View market){
        this.market = market;
    }

    public void setFirstDeckCards(ArrayList<Integer> cards){
        decks = new Decks_View(cards);
    }

    public void initFaithTrack(){
        faithTrack = new FaithTrack_View(players.size());
    }

    public void increaseFaithPoints(int numPlayer, int faithPoints){
        faithTrack.increaseFaithPoints(numPlayer, faithPoints);
    }

    void slideColumn(int selectedColumn) {
        market.slideColumn(selectedColumn);
    }

    void slideRow(int selectedRow){
        market.slideRow(selectedRow);
    }

    public void replaceCard(int row, int column, int cardID){
        decks.replaceCard(row, column, cardID);
    }

    public int[] get_Row_Column(int cardID){
        return decks.get_Row_Column(cardID);
    }

    public String getNickName(int viewID) {
        return players.get(viewID).getNickName();
    }

    public void increaseWarehouse(int viewID, Resource resource, int depot){
        players.get(viewID).increaseWarehouse(resource, depot);
    }

    public void newAmount(int viewID, Resource resource, int warehouseAmount, int strongboxAmount){
        players.get(viewID).newAmount(resource, warehouseAmount, strongboxAmount);
    }

    public void switchDepot(int viewID, int depot1, int depot2) {
        players.get(viewID).switchDepot(depot1, depot2);
    }

    public void addExtraDepot(int viewID, Resource r){
        players.get(viewID).addExtraDepot(r);
    }

    public void setLeaderCards(int viewID, int firstLeaderCard, int secondLeaderCard){
        players.get(viewID).setLeaderCards(firstLeaderCard, secondLeaderCard);
    }

    public void addLeaderCard(int viewID, int leaderCard){
        players.get(viewID).addLeaderCard(leaderCard);
    }

    public void addDevelopmentCard(int viewID, int cardID, int slot){
        players.get(viewID).addDevelopmentCard(cardID, slot);
    }

    public void discardLeaderCard(int viewID, int chosenLeaderCard){
        players.get(viewID).discardLeaderCard(chosenLeaderCard);
    }

    public void setVictoryPoints(int viewID, int victoryPoints){
        players.get(viewID).setVictoryPoints(victoryPoints);
    }
}
