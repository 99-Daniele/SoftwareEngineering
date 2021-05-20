package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.parser.CardMap;
import it.polimi.ingsw.view.model_view.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class CLI implements Observer{

    private Thread inputThread;
    private CLI_Printer cli_printer;
    private Game_View game;
    private int position;

    public CLI(ClientSocket clientSocket){
        this.position = 0;
        clientSocket.addObserver(this);
    }

    private void username(){
        System.out.println("Enter your nickname: ");
        try {
            String nickname = readLine();
            ClientSocket.sendMessage( new Message_One_Parameter_String(MessageType.LOGIN,position,nickname));
        } catch (ExecutionException | IOException e) {
            System.out.println("error");
        }
    }

    private void login_message(Message message) {
        if(message.getClientID() == 0)
            askPlayersNumber();
        System.out.println("Waiting other players...");
    }

    private void askPlayersNumber() {
        int playerNumber;
        try {
            playerNumber = numberInput(1, 4,  "How many players are going to play? 1 to 4 ");
            ClientSocket.sendMessage( new Message_One_Parameter_Int(MessageType.NUM_PLAYERS,position,playerNumber));
        } catch (ExecutionException | IOException e) {
            System.out.println("error");
        }
    }

    private void new_player_message(Message message){
        Message_One_Parameter_String m = (Message_One_Parameter_String) message;
        if (m.getClientID() != this.position) {
            System.out.println("New player: " + m.getPar());
            game.addPlayer(m.getPar());
        }
    }

    private void players_message(Message message){
        Message_ArrayList_String m = (Message_ArrayList_String) message;
        System.out.println("All players connected.\nPlayers: " + m.getNickNames());
        game = new Game_View(m.getNickNames());
        this.position = m.getClientID();
        System.out.println("You are the " + (position + 1) + "° player");
    }

    public void start_game_message() throws IOException {
        System.out.println("All players have made their choices. Game started");
        ClientSocket.sendMessage(new Message(MessageType.TURN, position));
    }

    private void market_message(Message message){
        Message_Market m = (Message_Market) message;
        game.setMarket(m.getMarket());
        System.out.println("This is the market:\n");
        // print market
    }

    private void deckBoard_message(Message message){
        Message_ArrayList_Int m = (Message_ArrayList_Int) message;
        game.setFirstDeckCards(m.getParams());
        System.out.println("This are the cards of the decks:\n");
        // print cards
    }

    public void take_market_marble() throws IOException {
        System.out.println("TAKE MARBLE FROM MARKET");
        int input=-1;
        int x;
        int y;
        try {
            x=numberInput(0,1,"Do you want a row or a column?\n0 - ROW\n1 - COLUMN");
            if (input==0)
                try {
                    y=numberInput(1,3,"Choose which row (1 to 3)");
                    ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, position, x, y));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            else
                try {
                    y=numberInput(1,4,"Choose which column (1 to 4)");
                    ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, position, x, y));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
        } catch (ExecutionException e) {
            System.out.println("error");
        }
    }

    /**
     * until user doesn't insert a valid input, ask him 1 int to chose the deck to where buy card. Then send it to Server
     * and waits an OK message return.
     */
    public void buy_card() throws IOException {
        ArrayList<Integer> pos = new ArrayList<>(2);
        System.out.println("BUY_DEVELOPMENT_CARD");
        try {
            pos.add(numberInput(1,3,"Choose a row (1 to 3)"));
        } catch (ExecutionException e) {
            System.out.println("error");
        }
        try {
            pos.add(numberInput(1,4,"\nScegli una colonna (1 - 4):"));
        } catch (ExecutionException e) {
            System.out.println("error");
        }
        int z = chose_warehouse_strongbox();
        if(z==-1)
        {
            System.out.println("error");
            return;
        }
        Message message = new Message_Three_Parameter_Int(MessageType.BUY_CARD, position, pos.get(0), pos.get(1), z);
        System.out.println("\n" + message.toString());
        ClientSocket.sendMessage(message);
    }

    /*
    forse non ci va qui
     */
    private void activate_another_production() throws IOException, InterruptedException {
        try {
            if (numberInput(0,1,"Vuoi attivare un altro potere di produzione?\n1 - SI\n0 - NO")==1)
                activate_production();
            else
                end_production();
        } catch (ExecutionException e) {
            end_production();
        }
    }

    /*
    forse non ci va
     */
    private void activate_production() throws IOException, InterruptedException {
        System.out.println("ACTIVATE PRODUCTION");
        int x;
        try {
            x=numberInput(1,3,"\nQuale produzione vuoi attivare?\n1 - CARTA SVILUPPO\n2 - POTERE BASE\n3 - CARTA LEADER");
            switch (x) {
                case 1:
                    slot_card_production();
                    break;
                case 2:
                    basic_production();
                    break;
                case 3:
                    leader_card_production();
                    break;
            }
        } catch (ExecutionException e) {
            System.out.println("error");
        }
    }

    public void slot_card_production() {
        int x;
        try {
            x=numberInput(1,3,"Di quale carta vuoi attivare il potere di produzione? (1 - 3)");
            int y = chose_warehouse_strongbox();
            if (y==-1)
            {
                System.out.println("error");
                return;
            }
            Message message = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, position, x, y);
            System.out.println("\n" + message.toString());
            ClientSocket.sendMessage(message);
        } catch (ExecutionException e) {
            System.out.println("error");
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    public void basic_production() throws IOException {
        System.out.println("Quale risorsa vuoi eliminare?");
        Resource r1 = chose_resource();
        System.out.println("Quale risorsa vuoi eliminare?");
        Resource r2 = chose_resource();
        System.out.println("Quale risorsa vuoi ricevere?");
        Resource r3 = chose_resource();
        int choice = chose_warehouse_strongbox();
        if (choice==-1)
        {
            System.out.println("error");
            return;
        }
        Message message = new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, position, r1, r2, r3, choice);
        ClientSocket.sendMessage(message);
    }

    public void leader_card_production() {
        int x = chose_leader_card();
        if(x == -1) {
            System.out.println("You no longer have any leader cards");
            return;
        }
        System.out.println("Which resource you choose?");
        Resource r = chose_resource();
        int choice = chose_warehouse_strongbox();
        if (choice==-1) {
            System.out.println("error");
            return;
        }
        Message message = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, position, r, x, choice);
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    public void end_production() throws IOException {
        Message message = new Message(MessageType.END_PRODUCTION, position);
        ClientSocket.sendMessage(message);
    }

    public void activate_leader_card() {
        System.out.println("ACTIVATE LEADER CARD");
        int x = chose_leader_card();
        if(x == -1) {
            System.out.println("You no longer have any leader cards");
            return;
        }
        Message message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_ACTIVATION, position, x);
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    public void discard_leader_card() {
        System.out.println("DISCARD LEADER CARD");
        int x = chose_leader_card();
        if(x == -1) {
            System.out.println("You no longer have any leader cards");
            return;
        }
        Message message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, position, x);

        System.out.println(message.toString());
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    private void end_turn() {
        Message end_turn = new Message(MessageType.END_TURN, position);
        System.out.println("Hai finito il tuo turno. Attendi che gli altri giocatori facciano il proprio.");
        try {
            ClientSocket.sendMessage(end_turn);
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    private int chose_leader_card() {
        int chosenLeaderCard=-1;
        int leaderCard1 = game.getMyLeaderCard(position, 1);
        int leaderCard2 = game.getMyLeaderCard(position, 2);
        Card_View firstLeaderCard = null;
        Card_View secondLeaderCard = null;
        if(leaderCard1 != -1 && leaderCard2 != -2){
            firstLeaderCard = CardMap.getCard(leaderCard1);
            secondLeaderCard = CardMap.getCard(leaderCard2);
            System.out.println("Your leader cards: " );
            // print leader cards
            try {
                chosenLeaderCard=numberInput(1,2,"Which leader card you choose? (1 or 2)");
                return chosenLeaderCard;
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else if(leaderCard1 != -1){
            firstLeaderCard = CardMap.getCard(leaderCard1);
            System.out.println("Your leader card: ");
            // print leader card
            chosenLeaderCard = 1;
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
            x=numberInput(1,4,"\n1 - COIN\n2 - SHIELD\n3 - STONE\n4 - SERVANT");
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
            System.out.println("error");
        }
        return chosenResource;
    }

    private int chose_warehouse_strongbox() {
        int choice=-1;
        try {
            choice=numberInput(0,1,"Da dove preferiresti prendere le risorse?\n0 - MAGAZZINO\n1 - FORZIERE");
            return choice;
        } catch (ExecutionException e) {
            e.printStackTrace();
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
        if(x==-1 || y==-1) {
            System.out.println("error");
            return;
        }
        Message message = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, position, x, y);
        System.out.println(message.toString());
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    private int choseDepot(int depot) {
        int choice=-1;
        try {
            choice=numberInput(1,5,"\nScegli il " + depot + "° deposito:");
            return choice;
        } catch (ExecutionException e) {
            System.out.println("error");
        }
        return choice;
    }

    private void leader_card_choice(Message message) throws IOException, InterruptedException {
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        int leaderCard1 = m.getPar1();
        int leaderCard2 = m.getPar2();
        int leaderCard3 = m.getPar3();
        int leaderCard4 = m.getPar4();
        TimeUnit.SECONDS.sleep(2);
        ArrayList<Integer> choice=new ArrayList<>(2);
        try {
            choice.add(numberInput(1,4,"\nScegli due tra queste 4 carte leader: " +
                    leaderCard1 + ", " + leaderCard2 + ", " + leaderCard3 + ", " + leaderCard4));
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                int number = Integer.parseInt(readLine());
                choice.add(number);
                if (choice.get(0) == choice.get(1)) {
                    System.err.println("\nNon puoi scegliere due carte uguali\n");
                } else if (choice.get(1) != leaderCard1 && choice.get(1) != leaderCard2 &&
                        choice.get(1) != leaderCard3 && choice.get(1) != leaderCard4) {
                    System.err.println("\nInserisci un numero corretto.\n");
                } else break;
            } catch (InputMismatchException | ExecutionException e) {
                System.err.println("\nInserisci un numero.");
            }
        }
        game.setMyLeaderCards(position, choice.get(0), choice.get(1));
        Message returnMessage = new Message_Two_Parameter_Int(MessageType.LEADER_CARD, position, choice.get(0), choice.get(2));
        ClientSocket.sendMessage(returnMessage);
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

    private void turn_message(Message message) throws InterruptedException, IOException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        if (m.getPar() == 1) {
            // azione iniziale
        }
        else
            System.out.println("Attendi che gli altri giocatori facciano il proprio turno...");
    }
    private void end_turn_message(Message message) throws InterruptedException, IOException {
        if (message.getClientID() != position) {
            System.out.println("Player " + game.getNickname(message.getClientID()) + " has finishe his turn.\nThis are its resources:");
        }
        // print player warehouse_strongbox
        if (position == 0) {
            if (message.getClientID() == game.getNumOfPlayers()- 1) {
                // azione iniziale
            }
        } else if (message.getClientID() == position - 1) {
            // azione iniziale
        }
    }

    private void buy_card_message(Message message) {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("Player " + game.getNickname(m.getClientID()) + " bought a new card and inserted it in " +
                "the " + m.getPar2() + "° slot.");
        // print card
        game.addDevelopmentCard(m.getClientID(), m.getPar1(), m.getPar2());
    }

    private void card_remove_message(Message message) {
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        if(game.getNumOfPlayers() == 1 && m.getClientID() == 1)
            System.out.println("Ludovico has removed one deck card from row " + (m.getPar1()) + " and" +
                    " column " + (m.getPar2()));
        else
            System.out.println("Deck card from row " + (m.getPar1()+1) + " and column "
                    + (m.getPar2()) + " has been removed");
        if (m.getPar3() == 1) {
            System.out.println("Deck is now empty");
        }
        else {
            System.out.println("New deck card is:");
            // print card
        }
        game.replaceCard(m.getPar1(), m.getPar2(), m.getPar4());
    }

    private void resource_amount_message(Message message) {
        Message_One_Resource_Two_Int m = (Message_One_Resource_Two_Int) message;
        game.newAmount(m.getClientID(), m.getResource(), m.getPar1(), m.getPar2());
    }

    private void market_change(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if(m.getPar1() == 0) {
            System.out.println("Player " + game.getNickname(m.getClientID()) + " has chosen row " + m.getPar2() + " of the market");
            game.slideRow(m.getPar2());
            //print row
        }
        else{
            System.out.println("Player " + game.getNickname(m.getClientID()) + " has chosen column " + m.getPar2() + " of the market");
            game.slideColumn(m.getPar2());
            //print column
        }
        // print new market
    }

    private void faith_points_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        if(game.getNumOfPlayers() == 1 && m.getClientID() == 2)
            System.out.println("Ludovico has increased his faith points. Now it has " + m.getPar());
        else
            System.out.println("Player " + game.getNickname(m.getClientID()) + " has increased its faith points. Now it has " + m.getPar());
        game.increaseFaithPoints(m.getClientID(), m.getPar());
        // print faith track
    }

    private void increase_warehouse_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        if(m.getPar1() != -1) {
            System.out.println("Player " + game.getNickname(m.getClientID()) + " has inserted by 1 " + m.getResource()
                    + " its " + m.getPar1() + "° depot");
            game.increaseWarehouse(m.getClientID(), m.getResource(), m.getPar1());
            //print warehouse_strongbox
        }
        else
            System.out.println("Player " + game.getNickname(m.getClientID()) + " has discarded 1 " + m.getResource()
                    + " marble");
    }
    private void switch_depot_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("Player " + game.getNickname(m.getClientID()) + " has switched its " + m.getPar1()
                + "° depot with its " + m.getPar2() + "° depot.");
        game.switchDepot(m.getClientID(), m.getPar1(), m.getPar2());
        //print warehouse_depot
    }

    private void vatican_report_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if(game.getNumOfPlayers() == 1 && m.getPar1() == 1)
            System.out.println("Ludovico activated Vatican Report." +
                    " Now you have " + m.getPar2() + " victory points from Vatican Report");
        else
            System.out.println("Player " + game.getNickname(m.getPar1()) + " activated Vatican Report." +
                    " Now you have " + m.getPar2() + " victory points from Vatican Report");
        // print faith track
    }

    private void leader_card_activation_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        System.out.println("Player " + game.getNickname(m.getClientID()) + " has activated one leader card: ");
        game.addLeaderCard(m.getClientID(), m.getPar());
        // print leader card
    }

    private void extra_depot_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        System.out.println("Player " + game.getNickname(m.getClientID()) + " has a new extra depot of " + m.getResource());
        game.addExtraDepot(m.getClientID(), m.getResource());
    }

    private void leader_card_discard_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        System.out.println("Player " + m.getClientID() + " has discarded one leader card: ");
        // print leader card
    }

    private void ok_message(){}

    private void chosen_slot_message(Message message){}

    private void take_marble_message(Message message){}

    private void white_conversion_card_message(Message message){}

    private void error_message(Message message){}

    private void quit_message(Message message){
        Message_One_Parameter_String m = (Message_One_Parameter_String) message;
        if(game != null) {
            System.out.println("Player " + m.getPar() + " disconnected. Game ended.");
            ClientSocket.setDisconnected();
            ClientSocket.disconnect();
            System.exit(1);
        }
        else if(m.getPar() != null)
            System.out.println("Player " + m.getPar() + " disconnected before game is started");
        else
            System.out.println("One player disconnected before game is started");
    }

    private void end_game_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if(game.getNumOfPlayers() == 1 && m.getClientID() == 1){
            System.out.println("Game ended. Ludovico win.");
        }
        else {
            System.out.println("Game ended. " + game.getNickname(m.getClientID()) + " win the game."
                    + " It made " + m.getPar1() + " victory points and " + m.getPar2()
                    + " total resources.");
        }
        ClientSocket.setDisconnected();
        System.out.println("\nDisconnecting.");
        ClientSocket.disconnect();
        System.exit(1);
    }

    @Override
    public void update(Observable o, Object arg) {
        try {
            Message returnMessage = (Message) arg;
            switch (returnMessage.getMessageType()) {
                case LOGIN:
                    login_message(returnMessage);
                    break;
                case NEW_PLAYER:
                    new_player_message(returnMessage);
                    break;
                case PLAYERS:
                    players_message(returnMessage);
                    break;
                case LEADER_CARD:
                    leader_card_choice(returnMessage);
                    break;
                case START_GAME:
                    start_game_message();
                    break;
                case MARKET:
                    market_message(returnMessage);
                    break;
                case DECKBOARD:
                    deckBoard_message(returnMessage);
                    break;
                case OK:
                    ok_message();
                    break;
                case PONG:
                    break;
                case TURN:
                    turn_message(returnMessage);
                    break;
                case END_TURN:
                    end_turn_message(returnMessage);
                    break;
                case BUY_CARD:
                    buy_card_message(returnMessage);
                    break;
                case CHOSEN_SLOT:
                    chosen_slot_message(returnMessage);
                    break;
                case CARD_REMOVE:
                    card_remove_message(returnMessage);
                    break;
                case RESOURCE_AMOUNT:
                    resource_amount_message(returnMessage);
                    break;
                case TAKE_MARBLE:
                    take_marble_message(returnMessage);
                    break;
                case MARKET_CHANGE:
                    market_change(returnMessage);
                    break;
                case WHITE_CONVERSION_CARD:
                    white_conversion_card_message(returnMessage);
                    break;
                case FAITH_POINTS_INCREASE:
                    faith_points_message(returnMessage);
                    break;
                case VATICAN_REPORT:
                    vatican_report_message(returnMessage);
                    break;
                case INCREASE_WAREHOUSE:
                    increase_warehouse_message(returnMessage);
                    break;
                case SWITCH_DEPOT:
                    switch_depot_message(returnMessage);
                    break;
                case LEADER_CARD_ACTIVATION:
                    leader_card_activation_message(returnMessage);
                    break;
                case EXTRA_DEPOT:
                    extra_depot_message(returnMessage);
                    break;
                case LEADER_CARD_DISCARD:
                    leader_card_discard_message(returnMessage);
                    break;
                case QUIT:
                    quit_message(returnMessage);
                    break;
                case END_GAME:
                    end_game_message(returnMessage);
                    break;
                case ERR:
                    error_message(returnMessage);
                    break;
                default:
                    System.err.println("\nRicevuto messaggio inaspettato dal Server.");
                    break;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } catch (ClassCastException e){
            e.printStackTrace();
        }
    }
}