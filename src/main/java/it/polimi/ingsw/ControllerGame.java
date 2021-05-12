package it.polimi.ingsw;

import it.polimi.ingsw.exceptions.AlreadyTakenNicknameException;
import it.polimi.ingsw.model.games.Game;
import it.polimi.ingsw.network.messages.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ControllerGame implements Observer{

    private Game game;
    private ArrayList<View> views;

    public ControllerGame(int numPlayers){
        this.game = new Game(numPlayers);
        views = new ArrayList<>();
    }

    public int getNumPlayers() {
        return game.getNumOfPlayers();
    }

    public void addView(View view){
        views.add(view);
        game.addObservers((VirtualView) view);
    }

    public void addNickname(String nickname) throws IOException{
        boolean error=true;
        while (error) {
            try {
                game.createPlayer(nickname);
                error = false;
            } catch (AlreadyTakenNicknameException e) {
                views.get(views.size()-1).nicknameTaken();
                nickname = views.get(views.size()-1).getNickname();
                System.out.println(nickname);
            }
        }
    }

    public synchronized void waitPlayers() {
        if (views.size() == game.getNumOfPlayers()) {
            notifyAll();
        } else {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void isMyTurn(Message message) throws IOException {
        int pos = message.getClientID() -1;
        if(pos!= game.getCurrentPosition())
            views.get(pos).myTurn(false);
        else
            views.get(pos).myTurn(true);
    }

    @Override
    public void update(Observable o, Object arg) {
        /*Message m = (Message) arg;
        isMyTurn(i);
        game.nextPlayer();
         */
        Message m = (Message) arg;
        System.out.println(m.toString());
        try {
            switch (m.getMessageType()) {
                case TURN:
                    isMyTurn(m);
                    break;
                default:
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void endGame() throws IOException {
        for(View view: views)
            view.endGame();
    }


    private void nextTurn()
    {
        game.nextPlayer();
    }

   /* public int getCurrentTurn(){
        return game.getCurrentPosition();
    }*/
}

