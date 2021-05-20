package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.states.CONTROLLER_STATES;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

public class Cli {

    private Thread inputThread;
    private int position;

    public Cli(Socket socket) throws IOException {

        this.position = 0;
    }

    public void username(){
        System.out.println("Enter your nickname: ");
        try {
            String nickname = readLine();
            ClientSocket.sendMessage( new Message_One_Parameter_String(MessageType.LOGIN,position,nickname));
        } catch (ExecutionException | IOException e) {
            showInputErrorMessage();
        }
    }

    public void askPlayersNumber() {
        int playerNumber;
        try {
            playerNumber = numberInput(2, 3,  "How many players are going to play? 1 to 4 ");
            ClientSocket.sendMessage( new Message_One_Parameter_Int(MessageType.NUM_PLAYERS,position,playerNumber));
        } catch (ExecutionException | IOException e) {
            showInputErrorMessage();
        }
    }


    public void take_market_marble() {

        System.out.println("TAKE MARBLE FROM MARKET");
        int input=-1;
        int x;
        int y;
        int maxValue;
        try {
            x=numberInput(0,1,"\nScegli una riga o una colonna?\n0 - RIGA\n1 - COLONNA");
            if (input==0)
                maxValue=3;
            else
                maxValue=4;
                try {
                    y=numberInput(1,maxValue,"Inserisci un numero da 1 a" +maxValue);
                    try {
                        ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, position, x, y));
                    } catch (IOException e) {
                        showSendError();
                    }
                } catch (ExecutionException e) {
                    showInputErrorMessage();
                }
        } catch (ExecutionException e) {
            showInputErrorMessage();
        }
    }

    /**
     * until user doesn't insert a valid input, ask him 1 int to chose the deck to where buy card. Then send it to Server
     * and waits an OK message return.
     */
    public void buy_card() throws IOException {
        ArrayList<Integer> pos=new ArrayList<>(2);
        System.out.println("BUY_DEVELOPMENT_CARD");
        try {
            pos.add(numberInput(1,3,"\nScegli una riga (1 - 3):"));
        } catch (ExecutionException e) {
            showInputErrorMessage();
        }
        try {
            pos.add(numberInput(1,4,"\nScegli una colonna (1 - 4):"));
        } catch (ExecutionException e) {
            showInputErrorMessage();
        }
        int z = chose_warehouse_strongbox();
        if(z==-1)
        {
            showInputErrorMessage();
            return;
        }
        Message message = new Message_Three_Parameter_Int(MessageType.BUY_CARD, position, pos.get(0), pos.get(1), z);
        System.out.println("\n" + message.toString());
        ClientSocket.sendMessage(message);
    }


    public void slot_card_production() {
        int x;
        try {
            x=numberInput(1,3,"Di quale carta vuoi attivare il potere di produzione? (1 - 3)");
            int y = chose_warehouse_strongbox();
            if (y==-1)
            {
                showInputErrorMessage();
                return;
            }
            Message message = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, position, x, y);
            System.out.println("\n" + message.toString());
            ClientSocket.sendMessage(message);
        } catch (ExecutionException e) {
            showInputErrorMessage();
        } catch (IOException e) {
            showSendError();
        }
    }

    public void basic_production() {
        System.out.println("Quale risorsa vuoi eliminare?");
        Resource r1 = chose_resource();
        System.out.println("Quale risorsa vuoi eliminare?");
        Resource r2 = chose_resource();
        System.out.println("Quale risorsa vuoi ricevere?");
        Resource r3 = chose_resource();
        int choice = chose_warehouse_strongbox();
        if (choice==-1)
        {
            showInputErrorMessage();
            return;
        }
        Message message = new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, position, r1, r2, r3, choice);
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            showSendError();
        }
    }

    public void leader_card_production() {

        int x = chose_leader_card();
        System.out.println("Quale risorsa vuoi ricevere?");
        Resource r = chose_resource();
        int choice = chose_warehouse_strongbox();
        if (choice==-1)
        {
            showInputErrorMessage();
            return;
        }
        Message message = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, position, r, x, choice);
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            showSendError();
        }
    }

    public void end_production() throws IOException {
        Message message = new Message(MessageType.END_PRODUCTION, position);
        ClientSocket.sendMessage(message);
    }

    public void activate_leader_card() {
        System.out.println("ACTIVATE LEADER CARD");
        int x = chose_leader_card();
        if(x==-1)
        {
            showInputErrorMessage();
            return;
        }
        Message message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_ACTIVATION, position, x);
        System.out.println(message.toString());
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            showSendError();
        }
    }

    public void discard_leader_card() {

        System.out.println("DISCARD LEADER CARD");
        int x = chose_leader_card();
        if(x==-1)
        {
            showInputErrorMessage();
            return;
        }
        Message message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, position, x);

        System.out.println(message.toString());
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            showSendError();
        }
    }

    private void end_turn() {
        Message end_turn = new Message(MessageType.END_TURN, position);
        System.out.println("Hai finito il tuo turno. Attendi che gli altri giocatori facciano il proprio.");
        try {
            ClientSocket.sendMessage(end_turn);
        } catch (IOException e) {
            showSendError();
        }
    }

    private int chose_leader_card() {
        int chosenLeaderCard=-1;
        /*
        System.out.println("Le tue carte leader: " + leaderCard1 + ", " + leaderCard2);
         */
        try {
            chosenLeaderCard=numberInput(1,2,"Quale carta leader scegli? (1 - 2)");
            return chosenLeaderCard;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return chosenLeaderCard;
    }

    public void chose_first_resources() throws IOException {
        switch (position) {
            case 0:
                System.out.println("In attesa che tutti i giocatori facciano le proprie scelte...");
                break;
            case 1: {
                System.out.println("Sei il giocatore in posizione 2. Hai diritto a 1 risorsa.\nScegli la risorsa:");
                Resource r = chose_resource();
                Message message = new Message_One_Int_One_Resource(MessageType.ONE_FIRST_RESOURCE, position, r, 1);
                ClientSocket.sendMessage(message);
            }
            break;
            case 2: {
                System.out.println("Sei il giocatore in posizione 2. Hai diritto a 1 risorsa e 1 punto fede.\nScegli la risorsa:");
                Resource r = chose_resource();
                Message message = new Message_One_Int_One_Resource(MessageType.ONE_FIRST_RESOURCE, position, r, 1);
                ClientSocket.sendMessage(message);
            }
            break;
            case 3: {
                System.out.println("Sei il giocatore in posizione 4. Hai diritto a 2 risorse e 1 punto fede.\nScegli le risorse:");
                Resource r1 = chose_resource();
                Resource r2 = chose_resource();
                Message message = new Message_Two_Resource(MessageType.TWO_FIRST_RESOURCE, position, r1, r2);
                ClientSocket.sendMessage(message);
            }
            break;
        }
    }

    private Resource chose_resource() {
        Resource chosenResource = null;
        int x;
        try {
            x=numberInput(1,4,"\n1 - MONETA\n2 - SCUDO\n3 - ROCCIA\n4 - SERVO");
            switch (x) {
                case 1:
                    chosenResource = Resource.COIN;
                    break;
                case 2:
                    chosenResource = Resource.SHIELD;
                    break;
                case 3:
                    chosenResource = Resource.STONE;
                    break;
                case 4:
                    chosenResource = Resource.SERVANT;
                    break;
            }
        } catch (ExecutionException e) {
            showInputErrorMessage();
        }
        return chosenResource;
    }

    private int chose_warehouse_strongbox() {
        int choice=-1;
        try {
            choice=numberInput(0,1,"Da dove preferiresti prendere le risorse?\n0 - MAGAZZINO\n1 - FORZIERE");
            return choice;
        } catch (ExecutionException e) {
            showInputErrorMessage();
        }
        return choice;
    }

    /**
     * until user doesn't insert a valid input, ask him 2 int to chose two depots to switch. Then send it to Server and
     * waits an OK message return.
     *
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    public void switch_depot() {
        int x = choseDepot(1);
        int y = choseDepot(2);
        if(x==-1 || y==-1)
        {
            showInputErrorMessage();
            return;
        }
        Message message = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, position, x, y);
        System.out.println(message.toString());
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            showSendError();
        }
    }

    private int choseDepot(int depot) {
        int choice=-1;
        try {
            choice=numberInput(1,5,"\nScegli il " + depot + "° deposito:");
            return choice;
        } catch (ExecutionException e) {
            showInputErrorMessage();
        }
        return choice;
    }


    private void leader_card_choice(Message message) {
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        int leaderCard1 = m.getPar1();
        int leaderCard2 = m.getPar2();
        int leaderCard3 = m.getPar3();
        int leaderCard4 = m.getPar4();
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            System.out.println("Interrupted sleeping");
        }
        ArrayList<Integer> choice=new ArrayList<>(2);
        try {
            choice.add(numberInput(1,4,"\nScegli due tra queste 4 carte leader: " +
                    leaderCard1 + ", " + leaderCard2 + ", " + leaderCard3 + ", " + leaderCard4));
        } catch (ExecutionException e) {
            showInputErrorMessage();
        }
        try {
            choice.add(numberInputWithAvoid(1,4,choice.get(0),"\nScegli due tra queste 4 carte leader: " +
                    leaderCard1 + ", " + leaderCard2 + ", " + leaderCard3 + ", " + leaderCard4));
        } catch (ExecutionException e) {
            showInputErrorMessage();
        }
        Message returnMessage = new Message_Two_Parameter_Int(MessageType.LEADER_CARD, position, choice.get(0), choice.get(1));
        try {
            ClientSocket.sendMessage(returnMessage);
        } catch (IOException e) {
            showSendError();
        }
    }


    public void showInputErrorMessage(){
        System.out.println("Input Error");
    }

    public void showSendError()
    {
        System.out.println("Send request failed");
    }

    public String readLine() throws ExecutionException {
        FutureTask<String> futureTask = new FutureTask<>(new ReadInput());
        inputThread = new Thread(futureTask);
        inputThread.start();

        String input = null;

        try {
            input = futureTask.get();
        } catch (InterruptedException e) {
            futureTask.cancel(true);
            Thread.currentThread().interrupt();
        }
        return input;
    }

    private int numberInput(int minValue, int maxValue, String question) throws ExecutionException {
        int number = minValue - 1;
        do {
            try {
                System.out.println(question);
                number = Integer.parseInt(readLine());

                if (number < minValue || number > maxValue) {
                    System.out.println("Invalid number! Please try again.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please try again.\n");
            }
        } while (number < minValue || number > maxValue);
        return number;
    }

    private int numberInputWithAvoid(int minValue, int maxValue,int avoid, String question) throws ExecutionException {
        int number = minValue - 1;
        do {
            try {
                System.out.println(question);
                number = Integer.parseInt(readLine());

                if (number < minValue || number > maxValue) {
                    System.out.println("Invalid number! Please try again.\n");
                }else if (avoid==number) {
                    System.out.println("This number cannot be selected! Please try again.\n");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please try again.\n");
            }
        } while (number < minValue || number > maxValue);
        return number;
    }

    public void exit(String message) {
        inputThread.interrupt();
        System.out.println("\nERROR: " + message);
        System.exit(1);
    }
}
