package it.polimi.ingsw.view.CLI;


import it.polimi.ingsw.model.cards.leaderCards.LeaderCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.ErrorType;
import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.Message_One_Parameter_Int;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.model_view.Game_View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class CLI implements Observer{

    private BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    private Thread inThread;
    private final Object okLock = new Object();
    private Game_View game;

    public CLI(ClientSocket clientSocket) {
        clientSocket.addObserver(this);
    }

    public void askNumPlayers() {
        stdIn = new BufferedReader(new InputStreamReader(System.in));
        int userInt;
        System.out.println("\nSei il primo giocatore. Quanti giocatori vuoi in partita? (1 - 4)");
        while (true) {
            try {
                userInt = stdIn.read();
                if (userInt < 1 || userInt > 4) {
                    System.err.println("\nInserisci un numero da 1 a 4.\n");
                    System.out.println("\nQuanti giocatori vuoi in partita? (1 - 4)");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\nQuanti giocatori vuoi in partita? (1 - 4)");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return userInt;
    }

    public void newPlayer(String nickname) {
        System.out.println("\nNuovo giocatore: " + nickname);
    }

    public void allPlayerConnected(int position, int numPLayer, ArrayList<String> nickNames) {

    }

    public void startGame(int numPlayer) {

    }

    public void choseLeaderCards(ArrayList<Integer> leaderCards) {
        int leaderCard1 = leaderCards.get(0);
        int leaderCard2 = leaderCards.get(1);
        int leaderCard3 = leaderCards.get(2);
        int leaderCard4 = leaderCards.get(3);
        TimeUnit.SECONDS.sleep(1);
        System.out.println("\nScegli due tra queste 4 carte leader: " +
                leaderCard1 + ", " + leaderCard2 + ", " + leaderCard3 + ", " + leaderCard4);
        int firstChoice;
        int secondChoice;
        while (true) {
            try {
                firstChoice = stdIn.nextInt();
                if (firstChoice != leaderCard1 && firstChoice != leaderCard2 &&
                        firstChoice != leaderCard3 && firstChoice != leaderCard4) {
                    System.err.println("\nInserisci un numero corretto.\n");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                stdIn.next();
            }
        }
        while (true) {
            try {
                secondChoice = stdIn.nextInt();
                if (secondChoice == firstChoice) {
                    System.err.println("\nNon puoi scegliere due carte uguali\n");
                } else if (secondChoice != leaderCard1 && secondChoice != leaderCard2 &&
                        secondChoice != leaderCard3 && secondChoice != leaderCard4) {
                    System.err.println("\nInserisci un numero corretto.\n");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                stdIn.next();
            }
        }
    }

    public void available_slot(ArrayList<Integer> availableSlots) {

    }

    public void chosen_marble(Marble[] marbles) {

    }

    public void choseWhiteConversionCard(LeaderCard[] leaderCards) {

    }

    public void isMyTurn(boolean turn) {

    }

    public void exit(String nickName) {

    }

    public void quit(String nickName) {

    }

    public void ok() {

    }

    public void errorMessage(ErrorType errorType) {

    }

    public void update(Observable o, Object arg) {

    }
}
