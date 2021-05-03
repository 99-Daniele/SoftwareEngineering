package it.polimi.ingsw;

import it.polimi.ingsw.model.games.Game;

public class GamePos {
    private Game game;
    private int position;

    public GamePos(Game game,int position){
        this.game=game;
        this.position=position;
    }

    public Game getGame() {
        return game;
    }

    public int getPosition() {
        return position;
    }
}
