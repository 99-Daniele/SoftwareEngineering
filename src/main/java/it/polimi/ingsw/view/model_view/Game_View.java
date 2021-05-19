package it.polimi.ingsw.view.model_view;

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
}
