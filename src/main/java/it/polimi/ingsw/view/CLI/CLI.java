package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.controller.states.CONTROLLER_STATES;
import it.polimi.ingsw.model.market.Marble;
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
    private Game_View game;
    private CONTROLLER_STATES currentState;
    private int position;

    public CLI(ClientSocket clientSocket){
        game = new Game_View();
        currentState = CONTROLLER_STATES.WAITING_PLAYERS_STATE;
        position = 0;
        clientSocket.addObserver(this);
    }

    public void username(){
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
        }
    }

    private void players_message(Message message){
        Message_ArrayList_String m = (Message_ArrayList_String) message;
        System.out.println("All players connected.\nPlayers: " + m.getNickNames());
        game.setPlayers(m.getNickNames());
        this.position = m.getClientID();
        System.out.println("You are the " + (position + 1) + "° player");
    }

    public void start_game_message() throws IOException {
        System.out.println("All players have made their choices. Game started");
        CLI_Printer.printInitialGame(game);
        currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
        ClientSocket.sendMessage(new Message(MessageType.TURN, position));
    }

    private void market_message(Message message){
        Message_Market m = (Message_Market) message;
        game.setMarket(m.getMarket());
    }

    private void deckBoard_message(Message message){
        Message_ArrayList_Int m = (Message_ArrayList_Int) message;
        game.setFirstDeckCards(m.getParams());
    }

    private void chose_action(int choice) throws IOException{
        switch (choice) {
            case 1:
                take_market_marble();
                break;
            case 2:
                buy_card();
                break;
            case 3:
                activate_production();
                break;
            case 4:
                activate_leader_card();
                break;
            case 5:
                discard_leader_card();
                break;
        }
    }

    private int first_input() {
        System.out.println("Your turn");
        System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n3 - ACTIVATE PRODUCTION\n" +
                "4 - ACTIVATE LEADER CARD\n5 - DISCARD LEADER CARD");
        int userInput = 0;
        try {
            userInput = numberInput(1, 5, "What do you want to do?");
        } catch (ExecutionException e) {
            System.out.println("error");
        }
        return userInput;
    }

    private void lastInput(){
        System.out.println("You want to do something else?\n1 - ACTIVATE LEADER CARD\n2 - DISCARD LEADER CARD\n0 - END TURN");
        int userInput = 0;
        try{
            userInput = numberInput(0, 2, "What do you want to do");
        } catch (ExecutionException e) {
            System.out.println("error");
        }
        switch (userInput) {
            case 1:
                activate_leader_card();
                break;
            case 2:
                discard_leader_card();
                break;
            case 0:
                currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
                end_turn();
                break;
        }
    }

    private void take_market_marble() throws IOException {
        System.out.println("TAKE MARBLE FROM MARKET");
        int x;
        int y;
        currentState = CONTROLLER_STATES.TAKE_MARBLE_STATE;
        try {
            CLI_Printer.printMarket(game);
            x=numberInput(0,1,"Do you want a row or a column?\n0 - ROW\n1 - COLUMN");
            if (x==0)
                try {
                    y=numberInput(1,3,"Choose which row (1 to 3)");
                    CLI_Printer.printRowMarket(game, y);
                    ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, position, x, y));
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            else
                try {
                    y=numberInput(1,4,"Choose which column (1 to 4)");
                    CLI_Printer.printColumnMarket(game, y);
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
        int[] row_column;
        System.out.println("BUY_DEVELOPMENT_CARD");
        // print all cards
        do {
            ArrayList<Integer> cardID = new ArrayList<>(1);
            try {
                cardID.add(numberInput(1, 48, "Choose a card by his CardID"));
            } catch (ExecutionException e) {
                System.out.println("error");
            }
            row_column = game.get_Row_Column(cardID.get(0));
            if(row_column[0] == -1 || row_column[1] == -1){
                int otherCard = 0;
                try {
                    otherCard = numberInput(0, 1, "You chose a wrong card. You want to take a different one?\n1 - YES\n 0 - NO");
                } catch (ExecutionException e) {
                    System.out.println("error");
                }
                if(otherCard == 0)
                    chose_action(first_input());
            }
            else
                break;
        } while (true);
        //print card
        int z = chose_warehouse_strongbox();
        if(z == -1) {
            System.out.println("error");
            return;
        }
        currentState = CONTROLLER_STATES.BUY_CARD_STATE;
        Message message = new Message_Three_Parameter_Int(MessageType.BUY_CARD, position, row_column[0], row_column[1], z);
        ClientSocket.sendMessage(message);
    }

    private void activate_another_production() throws IOException{
        try {
            if (numberInput(0,1,"Do you want to activate another power production?\n1 - YES\n0 - NO") == 1)
                activate_production();
            else
                end_production();
        } catch (ExecutionException e) {
            end_production();
        }
    }

    private void activate_production() throws IOException{
        System.out.println("ACTIVATE PRODUCTION");
        int x;
        try {
            while (true) {
                x = numberInput(1, 3, "\nWhich production do you want to activate?\n1 - DEVELOPMENT CARD POWER" +
                        "\n2 - BASIC POWER\n3 - LEADER CARD POWER");
                boolean power = false;
                switch (x) {
                    case 1:
                        power = slot_card_production();
                        break;
                    case 2:
                        basic_production();
                        power = true;
                        break;
                    case 3:
                        power = leader_card_production();
                        break;
                }
                if(power == false){
                    if (numberInput(0,1,"Do you want to activate another power production?\n1 - YES\n0 - NO") == 0)
                        chose_action(first_input());
                }
                else{
                  currentState = CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE;
                }
            }
        } catch (ExecutionException e) {
            System.out.println("error");
        }
    }

    public boolean slot_card_production() {
        int x;
        int y;
        try {
            ArrayList<Integer> cards = game.getDevelopmentCards(position);
            // print cards
            if(cards.size() > 0) {
                if (cards.size() > 1)
                    x = numberInput(1, cards.size(), "Which card do you want to activate production? (1 - " + cards.size() + ")");
                else
                    x = cards.get(0);
                y = chose_warehouse_strongbox();
                if (y == -1) {
                    System.out.println("error");
                    return false;
                }
                Message message = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, position, x, y);
                ClientSocket.sendMessage(message);
                return true;
            }
            else {
                System.out.println("You don't have any development cards");
                return false;
            }
        } catch (ExecutionException | IOException e) {
            System.out.println("error");
        }
        return false;
    }

    public void basic_production() throws IOException {
        System.out.println("Which resource you want to delete?");
        Resource r1 = chose_resource();
        System.out.println("Which resource you want to delete?");
        Resource r2 = chose_resource();
        System.out.println("Which resource you want to gain?");
        Resource r3 = chose_resource();
        int choice = chose_warehouse_strongbox();
        if (choice==-1) {
            System.out.println("error");
            return;
        }
        Message message = new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, position, r1, r2, r3, choice);
        ClientSocket.sendMessage(message);
    }

    public boolean leader_card_production() throws IOException {
        int x = chose_leader_card();
        if(x == -1) {
            System.out.println("You no longer have any leader cards");
            return false;
        }
        System.out.println("Which resource you want to gain?");
        Resource r = chose_resource();
        int choice = chose_warehouse_strongbox();
        if (choice==-1) {
            System.out.println("error");
            return false;
        }
        Message message = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, position, r, x, choice);
        ClientSocket.sendMessage(message);
        return true;
    }

    public void end_production() throws IOException {
        Message message = new Message(MessageType.END_PRODUCTION, position);
        currentState = CONTROLLER_STATES.END_TURN_STATE;
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
        game.discardLeaderCard(position, x);
        Message message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, position, x);
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    private void end_turn() {
        Message end_turn = new Message(MessageType.END_TURN, position);
        System.out.println("You finished your turn. Wait for other players.");
        try {
            ClientSocket.sendMessage(end_turn);
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    private int chose_leader_card() {
        int chosenLeaderCard = -1;
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
                System.out.println("You are the 1st player.\nWait for the other players to make their choices...");
                break;
            case 1: {
                System.out.println("You are the 2nd player.\nYou gain 1 resource. Chose the resource:");
                Resource r = chose_resource();
                Message message = new Message_One_Int_One_Resource(MessageType.ONE_FIRST_RESOURCE, position, r, 1);
                ClientSocket.sendMessage(message);
            }
            break;
            case 2: {
                System.out.println("You are the 3rd player.\nYou gain 1 resource and 1 faith point. Chose the resource:");
                Resource r = chose_resource();
                Message message = new Message_One_Int_One_Resource(MessageType.ONE_FIRST_RESOURCE, position, r, 1);
                ClientSocket.sendMessage(message);
            }
            break;
            case 3: {
                System.out.println("You are the 4th player.\nYou gain 2resources and 1 faith point. Chose the resources:");
                Resource r1 = chose_resource();
                Resource r2 = chose_resource();
                Message message = new Message_Two_Resource(MessageType.TWO_FIRST_RESOURCE, position, r1, r2);
                ClientSocket.sendMessage(message);
            }
            break;
        }
        game.startGame();
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
            choice=numberInput(0,1,"Where you preferred to get the resource from?\n0 - WAREHOUSE\n1 - STRONGBOX");
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
        CLI_Printer.printWarehouse(game, position);
        int x = choseDepot(1);
        int y = choseDepot(2);
        if(x==-1 || y==-1) {
            System.out.println("error");
            return;
        }
        Message message = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, position, x, y);
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            System.out.println("error");
        }
    }

    private int choseDepot(int depot) {
        int choice=-1;
        try {
            choice=numberInput(1,5,"Chose the " + depot + "° depot:");
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
        ArrayList<Integer> choice=new ArrayList<>(2);
        try {
            choice.add(numberInput(1,4,"Chose between this 4 leader cards. Insert the cardID "));
            // print cards
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        while (true) {
            try {
                int number = Integer.parseInt(readLine());
                choice.add(number);
                if (choice.get(0) == choice.get(1)) {
                    System.err.println("You can't chose the same card twice\n");
                } else if (choice.get(1) != leaderCard1 && choice.get(1) != leaderCard2 &&
                        choice.get(1) != leaderCard3 && choice.get(1) != leaderCard4) {
                    System.err.println("Chose a correct cardID\n");
                } else break;
            } catch (InputMismatchException | ExecutionException e) {
                System.err.println("Insert a number\n");
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
            chose_action(first_input());
        }
        else
            System.out.println("Wait for other players to finish their turns...");
    }
    private void end_turn_message(Message message) throws InterruptedException, IOException {
        if (message.getClientID() != position) {
            System.out.println("Player " + game.getNickname(message.getClientID()) + " has finished his turn.\nThis are its resources:");
        }
        CLI_Printer.printWarehouseStrongbox(game, message.getClientID());
        if (position == 0) {
            if (message.getClientID() == game.getNumOfPlayers()- 1) {
                chose_action(first_input());
            }
        } else if (message.getClientID() == position - 1) {
            chose_action(first_input());
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
            CLI_Printer.printRowMarket(game, m.getPar2());
            game.slideRow(m.getPar2());
        }
        else{
            System.out.println("Player " + game.getNickname(m.getClientID()) + " has chosen column " + m.getPar2() + " of the market");
            CLI_Printer.printColumnMarket(game, m.getPar2());
            game.slideColumn(m.getPar2());
        }
        CLI_Printer.printMarket(game);
    }

    private void faith_points_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        if(game.getNumOfPlayers() == 1 && m.getClientID() == 2)
            System.out.println("Ludovico has increased his faith points. Now it has " + m.getPar());
        else
            System.out.println("Player " + game.getNickname(m.getClientID()) + " has increased its faith points. Now it has " + m.getPar());
        game.increaseFaithPoints(m.getClientID(), m.getPar());
        CLI_Printer.printFaithTrack(game);
    }

    private void increase_warehouse_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        if(m.getPar1() != -1) {
            System.out.println("Player " + game.getNickname(m.getClientID()) + " has inserted by 1 " + m.getResource()
                    + " its " + m.getPar1() + "° depot");
            game.increaseWarehouse(m.getClientID(), m.getResource(), m.getPar1());
            CLI_Printer.printWarehouse(game, m.getClientID());
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
        CLI_Printer.printWarehouse(game, m.getClientID());
    }

    private void vatican_report_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if(game.getNumOfPlayers() == 1 && m.getPar1() == 1)
            System.out.println("Ludovico activated Vatican Report." +
                    " Now you have " + m.getPar2() + " victory points from Vatican Report");
        else
            System.out.println("Player " + game.getNickname(m.getPar1()) + " activated Vatican Report." +
                    " Now you have " + m.getPar2() + " victory points from Vatican Report");
        CLI_Printer.printFaithTrack(game);
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
        CLI_Printer.printWarehouse(game, m.getClientID());
    }

    private void leader_card_discard_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        System.out.println("Player " + m.getClientID() + " has discarded one leader card: ");
        // print leader card
    }

    private void ok_message() throws IOException {
        switch (currentState) {
            case WAITING_PLAYERS_STATE:
                if (game.isStartGame())
                    System.out.println("In attesa che tutti i giocatori facciano le proprie scelte...");
                else
                    chose_first_resources();
                break;
            case FIRST_ACTION_STATE:
                chose_action(first_input());
                break;
            case TAKE_MARBLE_STATE:
                chose_marble(game.getChosenMarbles());
                break;
            case BUY_CARD_STATE:
                lastInput();
                break;
            case ACTIVATE_PRODUCTION_STATE:
                activate_another_production();
                break;
            case END_TURN_STATE:
                lastInput();
                break;
        }
    }

    private void chosen_slot_message(Message message) throws IOException {
        Message_Three_Parameter_Int m = (Message_Three_Parameter_Int) message;
        int choice = 0;
        if (m.getPar3() == -1) {
            try {
                choice = numberInput(m.getPar1(), m.getPar2(), "Chose which slot insert the card into: " + m.getPar1() + ", " + m.getPar2());
            } catch (ExecutionException e) {
                System.out.println("error");
            }
        } else {
            try {
                choice = numberInput(1, 3, "Chose which slot insert the card into: 1, 2, 3");
            } catch (ExecutionException e) {
                System.out.println("error");
            }
        }
        Message returnMessage = new Message_One_Parameter_Int(MessageType.CHOSEN_SLOT, position, choice);
        ClientSocket.sendMessage(returnMessage);
    }

    private void take_marble_message(Message message) throws IOException {
        Message_ArrayList_Marble m = (Message_ArrayList_Marble) message;
        game.setChosenMarbles(m.getMarbles());
        chose_marble(m.getMarbles());
    }

    private void chose_marble(ArrayList<Marble> marbles) throws IOException {
        Marble chosenMarble;
        System.out.println("You have chosen this marbles: ");
        CLI_Printer.printMarbles(game, marbles);
        try {
            int switchDepot = numberInput(0, 1, "Do you wnat to switch your depots?\n1 - YES\n0 - NO");
            if (switchDepot == 1) {
                switch_depot();
                return;
            }
        } catch (ExecutionException e) {
            System.out.println("error");
        }
        if (marbles.size() == 1) {
            chosenMarble = marbles.remove(0);
            game.setChosenMarbles(marbles);
            currentState = CONTROLLER_STATES.END_TURN_STATE;
            Message message = new Message_One_Parameter_Marble(MessageType.USE_MARBLE, position, chosenMarble);
            ClientSocket.sendMessage(message);
            return;
        }
        System.out.println("Chose one marble\nR - RED\nW - WHITE\nB - BLUE\nG - GREY\nY - YELLOW\nP - PURPLE");
        while (true) {
            try {
                String choice = readLine();
                choice = choice.toUpperCase();
                chosenMarble = correct_marble(choice, marbles);
                if (chosenMarble != null)
                    break;
                else
                    System.err.println("Chose a right marble");
            } catch (ExecutionException e) {
                System.out.println("error");
            }
        }
        if (marbles.size() == 0)
            currentState = CONTROLLER_STATES.END_TURN_STATE;
        else
            game.setChosenMarbles(marbles);
        Message message = new Message_One_Parameter_Marble(MessageType.USE_MARBLE, position, chosenMarble);
        ClientSocket.sendMessage(message);
    }

    private Marble correct_marble(String input, ArrayList<Marble> marbles){
        for(int i = 0; i < marbles.size(); i++){
            if(input.equals(marbles.get(i).toString()))
                return marbles.remove(i);
        }
        return null;
    }

    private void white_conversion_card_message(Message message) throws IOException {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("You have chosen a white marble and you have two possible conversion");
        System.out.println("Your leader cards: ");
        //print leader cards
        int choice = 0;
        try {
            choice = numberInput(1, 2, "You use the first or second leader card? (1 or 2)");
        } catch (ExecutionException e) {
            System.out.println("error");
        }
        Message returnMessage;
        if (choice == 1) {
            returnMessage = new Message_One_Parameter_Int(MessageType.WHITE_CONVERSION_CARD, position, m.getPar1());
        } else {
            returnMessage = new Message_One_Parameter_Int(MessageType.WHITE_CONVERSION_CARD, position, m.getPar2());
        }
        ClientSocket.sendMessage(returnMessage);
    }

    private void quit_message(Message message){
        Message_One_Parameter_String m = (Message_One_Parameter_String) message;
        if(game.getNumOfPlayers() != 0) {
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

    private void error_message(Message message) throws IOException, InterruptedException {
        ErrorMessage m = (ErrorMessage) message;
        switch (m.getErrorType()){
            case ALREADY_TAKEN_NICKNAME:
                already_taken_nickName_error();
                break;
            case WRONG_PARAMETERS:
                wrong_parameters_error();
                break;
            case NOT_YOUR_TURN:
                wrong_turn_error();
                break;
            case FULL_SLOT:
                full_slot_error();
                break;
            case EMPTY_DECK:
                empty_deck_error();
                break;
            case EMPTY_SLOT:
                empty_slot_error();
                break;
            case WRONG_POWER:
                wrong_power_error();
                break;
            case NOT_ENOUGH_CARDS:
                not_enough_cards_error();
                break;
            case ILLEGAL_OPERATION:
                illegal_operation_error();
                break;
            case IMPOSSIBLE_SWITCH:
                impossible_switch_error();
                break;
            case NOT_ENOUGH_RESOURCES:
                not_enough_resource_error();
                break;
            case ALREADY_ACTIVE_LEADER_CARD:
                already_active_error();
                break;
            case ALREADY_DISCARD_LEADER_CARD:
                already_discard_error();
                break;
        }
    }

    private void already_taken_nickName_error() throws IOException {
        System.out.println("Nickname already taken. Chose a different one");
        String userInput = null;
        try {
            while (true) {
                userInput = readLine();
                if (userInput == null) {
                    System.err.println("Insert a valid nickname");
                } else break;
            }
        } catch (ExecutionException e) {
            System.out.println("error");
        }
        Message message = new Message_One_Parameter_String(MessageType.LOGIN, position, userInput);
        ClientSocket.sendMessage(message);
    }
    private void wrong_parameters_error() throws  IOException {
        System.out.println("You have inserted wrong parameters");
        if(currentState == CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE)
            activate_another_production();
        else{
            currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
            chose_action(first_input());
        }
    }

    private void wrong_turn_error(){
        System.err.println("It's not your turn");
    }

    private void empty_deck_error() throws IOException {
        System.out.println("You have chosen an empty deck");
        try {
            int x = numberInput(0, 1, "Do you want to buy a different card?\n1 - YES\n0 - NO");
            if (x == 1) {
                buy_card();
            } else {
                currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
                chose_action(first_input());
            }
        } catch (ExecutionException e) {
            System.out.println("error");
        }
    }

    private void empty_slot_error() throws IOException{
        System.out.println("You have no cards in this slot");
        activate_another_production();
    }

    private void wrong_power_error() throws IOException{
        System.out.println("You can't activate this production power");
        activate_another_production();
    }

    private void not_enough_cards_error() throws IOException {
        System.out.println("You don't have enough development cards to activate this leader card");
        if(currentState == CONTROLLER_STATES.FIRST_ACTION_STATE)
            chose_action(first_input());
        else{
            currentState = CONTROLLER_STATES.END_TURN_STATE;
            lastInput();
        }
    }

    private void full_slot_error() throws IOException{
        System.out.println("You can't insert this card in any slot");
        try {
            int x = numberInput(0, 1, "Do you want to buy a different card?\n1 - YES\n0 - NO");
            if (x == 1) {
                buy_card();
            } else {
                currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
                chose_action(first_input());
            }
        } catch (ExecutionException e) {
            System.out.println("error");
        }
    }

    private void illegal_operation_error() throws IOException {
        System.out.println("You can't do this operation at this moment");
        if(currentState == CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE)
            activate_another_production();
        else{
            currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
            chose_action(first_input());
        }
    }

    private void impossible_switch_error() throws IOException{
        System.out.println("You can't switch this depots");
        chose_marble(game.getChosenMarbles());
    }

    private void not_enough_resource_error() throws IOException {
        System.out.println("You have not enough resources to do this operation");
        if(currentState == CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE)
            activate_another_production();
        else if (currentState == CONTROLLER_STATES.END_TURN_STATE)
            end_turn();
        else{
            currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
            chose_action(first_input());
        }
    }

    private void already_active_error() throws IOException{
        System.out.println("You activated this leader card previously");
        if(currentState == CONTROLLER_STATES.FIRST_ACTION_STATE)
            chose_action(first_input());
        else{
            currentState = CONTROLLER_STATES.END_TURN_STATE;
            lastInput();
        }
    }

    private void already_discard_error() throws  IOException {
        System.out.println("You discard this leader card previously");
        if(currentState == CONTROLLER_STATES.FIRST_ACTION_STATE)
            chose_action(first_input());
        else{
            currentState = CONTROLLER_STATES.END_TURN_STATE;
            lastInput();
        }
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