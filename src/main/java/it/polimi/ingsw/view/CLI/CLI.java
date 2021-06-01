package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.model.games.states.GAME_STATES;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.ClientView;

import java.io.*;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.*;

public class CLI extends ClientView{

    private Thread inputThread;
    private GAME_STATES currentState;
    private BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    private int position;

    public CLI(){
        position = 0;
    }

    public void launchCLI(){
        try {
            connectionInfo();
        } catch (IOException | ExecutionException e) {
            e.printStackTrace();
        }
        printLogo();
        startCLI();
    }

    public void launchCLI(String hostName, int port){
        try {
            if(!connectionInfo(hostName, port))
                connectionInfo();
        } catch (IOException | ExecutionException e) {
            e.printStackTrace();
        }
        printLogo();
        startCLI();
    }

    private void printLogo(){
        System.out.println("" +
                "8b     d8    d888b     d888888b  888888888  888888888  888888b                                                      \n" +
                "88b   d88   d8bnd8b   d888b      888888888  888        88    88                                                     \n" +
                "888bud888  d8     8b   d888b        888     888888     888888b                                                      \n" +
                "88 d8b 88  888888888     d888b      888     888888     8888b                                                        \n" +
                "88  8  88  88     88      d888b     888     888        888 d8b                                                      \n" +
                "88     88  88     88  d888888b      888     888888888  888  888                                                     \n" +
                "                                                                                                                    \n" +
                "888888888  888888888                                                                                                \n" +
                "888888888  888                                                                                                      \n" +
                "888   888  888888888                                                                                                \n" +
                "888   888  888                                                                                                      \n" +
                "888888888  888                                                                                                      \n" +
                "888888888  888                                                                                                      \n" +
                "                                                                                                                    \n" +
                "888888b   888888888  88b     88    d888b    888   d888888b   d888888b    d888b    88b     88    d8888888  888888888 \n" +
                "88    88  888        888b    88   d8bnd8b   888  d888b      d888b       d8bnd8b   888b    88   d88888888  888       \n" +
                "888888b   888888     88 d8b  88  d8     8b  888   d888b      d888b     d8     8b  88 d8b  88  d888        888888    \n" +
                "8888b     888888     88  d8b 88  888888888  888     d888b      d888b   888888888  88  d8b 88  d888        888888    \n" +
                "888 d8b   888        88    d888  88     88  888      d888b      d888b  88     88  88    d888   d88888888  888       \n" +
                "888  888  888888888  88     d88  88     88  888  d888888b   d888888b   88     88  88     d88    d8888888  888888888 \n");

        System.out.println("Write a \"username\" command");
    }

    private void connectionInfo() throws IOException, ExecutionException {
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("\nEnter hostname [localhost]: ");
            String hostName = stdIn.readLine();
            if (hostName == null || hostName.isBlank() || hostName.equals(""))
                hostName = "localhost";
            System.out.println("Enter port [12460]: ");
            String portNumber = stdIn.readLine();
            if (portNumber == null || portNumber.isBlank() ||portNumber.equals(""))
                portNumber = "12460";
            if(connectionInfo(hostName, Integer.parseInt(portNumber)))
                break;
        }
    }

    private boolean connectionInfo(String hostName, int port) throws IOException, ExecutionException {
        try {
            super.serverConnection(hostName, port);
            System.out.println("Accepted by Server");
            return true;
        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + hostName);
        } catch (IOException e) {
            System.err.println("Can't connect to host " + hostName);
        }
        return false;
    }

    private void startCLI(){
        if(inputThread == null){
            inputThread = new Thread(() -> {
                try {
                    readCommand();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            inputThread.start();
        }
    }

    private void readCommand() throws IOException {
        while (true){
            //System.out.println("[" + super.getNickname(position) + "]: ");
            ArrayList<String> command = new ArrayList<>();
            String line = stdIn.readLine();
            int j = 0;
            for(int i = 0; i < line.length(); i++){
                if(line.charAt(i) == ' ' ) {
                    command.add(line.substring(j, i));
                    j = i + 1;
                }
            }
            command.add(line.substring(j));
            commandInterpreter(command);
        }
    }

    private void commandInterpreter(ArrayList<String> command) throws IOException {
        if(command.get(0).equals("username") || command.get(0).equals("user") || command.get(0).equals("-un")){
            usernameRequest(command);
            return;
        }
        if(command.get(0).equals("help")  || command.get(0).equals("-h")){
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/helpCommand.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
            return;
        }
        switch (command.size()){
            case 1:
                oneCommandRequest(command);
                break;
            case 2:
                twoCommandRequest(command);
                break;
            case 3:
                threeCommandRequest(command);
                break;
            case 4:
                fourCommandRequest(command);
                break;
            case 5:
                fiveCommandRequest(command);
                break;
            case 6:
                sixCommandRequest(command);
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void usernameRequest(ArrayList<String> nickName) throws IOException {
        String nick = nickName.get(1);
        for (int i = 2; i < nickName.size(); i++) {
            nick += " ";
            nick += nickName.get(i);
        }
        ClientSocket.sendMessage(new Message_One_Parameter_String(MessageType.LOGIN, position, nick));
    }

    private void oneCommandRequest(ArrayList<String> command) throws IOException {
        switch (command.get(0)){
            case "turn":
            case "-tu":
                ClientSocket.sendMessage(new Message(MessageType.TURN, position));
                break;
            case "-sm":
                CLI_Printer.printMarket(super.getGame());
                break;
            case "-sf":
                CLI_Printer.printFaithTrack(super.getGame());
                break;
            case "-sd":
                CLI_Printer.printDecks(super.getGame());
                break;
            case "-smp":
                CLI_Printer.printPlayerBoard(super.getGame(), position);
                break;
            case "-smw":
                CLI_Printer.printWarehouse(super.getGame(), position);
                break;
            case "-sms":
                CLI_Printer.printStrongbox(super.getGame(), position);
                break;
            case "-smc":
                CLI_Printer.printCardSlot(super.getGame(), position);
                break;
            case "-sml":
                CLI_Printer.printLeaderCard(super.getGame(), position);
                break;
            case "-ep":
                endPowerRequest();
                break;
            case "-et":
                endTurnRequest();
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void endPowerRequest() throws IOException {
        ClientSocket.sendMessage(new Message(MessageType.END_PRODUCTION, position));
    }

    private void endTurnRequest() throws IOException {
        ClientSocket.sendMessage(new Message(MessageType.END_TURN, position));
    }

    private void twoCommandRequest(ArrayList<String> command) throws IOException {
        switch (command.get(0)){
            case "numplayer":
            case "num":
            case "-np":
                numPlayerRequest(command.get(1));
                break;
            case "see":
                seeRequest(command.get(1));
                break;
            case "-sp":
                seePlayerBoardRequest(command.get(1));
                break;
            case "-swh":
                seeWarehouseRequest(command.get(1));
                break;
            case "-ssb":
                seeStrongboxRequest(command.get(1));
                break;
            case "-sc":
                seeCardsRequest(command.get(1));
                break;
            case "-sl":
                seeLeaderRequest(command.get(1));
                break;
            case "usemarble":
            case "marble":
            case "-um":
                useMarbleRequest(command.get(1), super.getMarbles());
                break;
            case "whiteconversion":
            case "white":
            case "-wc":
                whiteConversionRequest(command.get(1));
                break;
            case "power":
                endPowerRequest(command.get(1));
                break;
            case"turn":
                endTurnRequest(command.get(1));
                break;
            case "resource":
            case "-cr":
                choseResourceRequest(command.get(1));
                break;
            case "slot":
            case "-cs":
                choseSlotRequest(command.get(1));
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void numPlayerRequest(String numPlayers) throws IOException {
        try {
            int playerNumber = Integer.parseInt(numPlayers);
            if(playerNumber < 1 || playerNumber > 4)
                System.err.println("You have inserted a wrong number (1 - 4)");
            else
                ClientSocket.sendMessage(new Message_One_Parameter_Int(MessageType.NUM_PLAYERS, position, playerNumber));
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void seeRequest(String request){
        switch (request){
            case "market":
            case "m":
                CLI_Printer.printMarket(super.getGame());
                break;
            case "faithtrack":
            case "f":
                CLI_Printer.printFaithTrack(super.getGame());
                break;
            case "decks":
            case "d":
                CLI_Printer.printDecks(super.getGame());
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void useMarbleRequest(String marble, ArrayList<Marble> marbles) throws IOException {
        if(marbles == null){
            System.err.println("You can't do this now");
            return;
        }
        marble = marble.toUpperCase();
        Marble chosenMarble = correct_marble(marble, marbles);
        if (chosenMarble != null){
            super.setMarbles(marbles);
            Message message = new Message_One_Parameter_Marble(MessageType.USE_MARBLE, position, chosenMarble);
            ClientSocket.sendMessage(message);
        }
        else
            System.err.println("You have chosen a wrong marble");
    }

    private Marble correct_marble(String input, ArrayList<Marble> marbles){
        for(int i = 0; i < marbles.size(); i++){
            if(input.equals(marbles.get(i).toString()))
                return marbles.remove(i);
            if(input.equals(marbles.get(i).toStringAbb()))
                return marbles.remove(i);
        }
        return null;
    }

    private void whiteConversionRequest(String chosenLeaderCard) throws IOException {
        try {
            int leaderCard = Integer.parseInt(chosenLeaderCard);
            if(leaderCard < 1 || leaderCard > 2)
                System.err.println("You have inserted a wrong number (1 - 2)");
            else {
                Message returnMessage = new Message_One_Parameter_Int(MessageType.WHITE_CONVERSION_CARD, position, leaderCard);
                ClientSocket.sendMessage(returnMessage);
            }
        } catch (NumberFormatException  e){
            System.err.println("You have to insert one number!");
        }
    }

    private void endPowerRequest(String command) throws IOException {
        if(command.equals("end"))
            endPowerRequest();
        else
            System.err.println("Illegal command from player");
    }

    private void endTurnRequest(String command) throws IOException {
        if(command.equals("end"))
            endTurnRequest();
        else
            System.err.println("Illegal command from player");

    }

    private void threeCommandRequest(ArrayList<String> command) throws IOException {
        switch (command.get(0)){
            case "see":
                seeRequest(command);
                break;
            case "chose":
                choseRequest(command);
                break;
            case "buycard":
            case "buy":
            case "-bc":
                buyCardRequest(command);
                break;
            case "takemarble":
            case "market":
            case "-tm":
                takeMarbleRequest(command);
                break;
            case "switch":
            case "-sw":
                switchRequest(command);
                break;
            case "leader":
            case "-la":
            case "-ld":
            case "-cl":
                leaderRequest(command);
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void seeRequest(ArrayList<String> command){
        try {
            switch (command.get(1)) {
                case "my":
                    seeMyRequest(command.get(2));
                    break;
                case "playerboard":
                case "board":
                case "p":
                    seePlayerBoardRequest(command.get(2));
                    break;
                case "warehouse":
                case "wh":
                    seeWarehouseRequest(command.get(2));
                    break;
                case "strongbox":
                case "sb":
                    seeStrongboxRequest(command.get(2));
                    break;
                case "cards":
                case "c":
                    seeCardsRequest(command.get(2));
                    break;
                case "leader":
                case "l":
                    seeLeaderRequest(command.get(2));
                    break;
                default:
                    System.err.println("Illegal command from player");
                    break;

            }
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void seeMyRequest(String command){
        switch (command){
            case "playerboard":
            case "board":
            case "p":
                CLI_Printer.printPlayerBoard(super.getGame(), position);
                break;
            case "warehouse":
            case "wh":
                CLI_Printer.printWarehouse(super.getGame(), position);
                break;
            case "strongbox":
            case "sb":
                CLI_Printer.printStrongbox(super.getGame(), position);
                break;
            case "cards":
            case "c":
                CLI_Printer.printCardSlot(super.getGame(), position);
                break;
            case "leader":
            case "l":
                CLI_Printer.printLeaderCard(super.getGame(), position);
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void seePlayerBoardRequest(String board){
        try {
            int player = Integer.parseInt(board);
            if(player < 1 || player > super.getNumOfPlayers())
                System.err.println("You have inserted a wrong number (1 - " + super.getNumOfPlayers() + ")");
            else
                CLI_Printer.printPlayerBoard(super.getGame(), player-1);
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void seeWarehouseRequest(String warehouse){
        try {
            int player = Integer.parseInt(warehouse);
            if(player < 1 || player > super.getNumOfPlayers())
                System.err.println("You have inserted a wrong number (1 - " + super.getNumOfPlayers() + ")");
            else
                CLI_Printer.printWarehouse(super.getGame(), player-1);
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void seeStrongboxRequest(String strongbox){
        try {
            int player = Integer.parseInt(strongbox);
            if(player < 1 || player > super.getNumOfPlayers())
                System.err.println("You have inserted a wrong number (1 - " + super.getNumOfPlayers() + ")");
            else
                CLI_Printer.printStrongbox(super.getGame(), player -1);
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void seeCardsRequest(String cards){
        try {
            int player = Integer.parseInt(cards);
            if(player < 1 || player > super.getNumOfPlayers())
                System.err.println("You have inserted a wrong number (1 - " + super.getNumOfPlayers() + ")");
            else
                CLI_Printer.printCardSlot(super.getGame(), player-1);
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void seeLeaderRequest(String leader){
        try {
            int player = Integer.parseInt(leader);
            if(player < 1 || player > super.getNumOfPlayers())
                System.err.println("You have inserted a wrong number (1 - " + super.getNumOfPlayers() + ")");
            else
                CLI_Printer.printLeaderCard(super.getGame(), player-1);
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void choseRequest(ArrayList<String> command) throws IOException {
        switch(command.get(1)){
            case "resource":
            case "r":
                choseResourceRequest(command.get(2));
                break;
            case "slot":
            case "s":
                choseSlotRequest(command.get(2));
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void choseResourceRequest(String resource) throws IOException {
        Resource r = correctResource(resource);
        if(r == null)
            System.err.println("Illegal command from player");
        else {
            Message message = new Message_One_Int_One_Resource(MessageType.ONE_FIRST_RESOURCE, position, r, 1);
            ClientSocket.sendMessage(message);
        }
    }

    private Resource correctResource(String resource){
        resource = resource.toUpperCase(Locale.ROOT);
        switch (resource){
            case "COIN":
            case "CO":
                return Resource.COIN;
            case "SHIELD":
            case "SH":
                return Resource.SHIELD;
            case "STONE":
            case "ST":
                return Resource.STONE;
            case "SERVANT":
            case "SE":
                return Resource.SERVANT;
            default:
                return null;
        }
    }

    private void choseSlotRequest(String slot) throws IOException {
        try {
            int chosenSlot = Integer.parseInt(slot);
            if(chosenSlot < 1 || chosenSlot > 3)
                System.err.println("You have inserted a wrong number (1 - 3)");
            else
                ClientSocket.sendMessage(new Message_One_Parameter_Int(MessageType.CHOSEN_SLOT, position, chosenSlot));
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void buyCardRequest(ArrayList<String> command) throws IOException {
        try {
            int cardID = Integer.parseInt(command.get(1));
            if(cardID < 1 || cardID > 48)
                System.err.println("You have inserted a wrong number (1 - 48)");
            else{
                int[] rowColumn = super.getRowColumn(cardID);
                if(command.get(2).equals("warehouse") || command.get(2).equals("w")) {
                    Message message = new Message_Three_Parameter_Int(MessageType.BUY_CARD, position, rowColumn[0], rowColumn[1], 1);
                    ClientSocket.sendMessage(message);
                }
                else if(command.get(2).equals("strongbox") || command.get(2).equals("s")){
                    Message message = new Message_Three_Parameter_Int(MessageType.BUY_CARD, position, rowColumn[0], rowColumn[1], 2);
                    ClientSocket.sendMessage(message);
                }
                else
                    System.err.println("Illegal command from player");
            }
        } catch (NumberFormatException e){
            System.err.println("Illegal command from player");
        }
    }

    private void takeMarbleRequest(ArrayList<String> command) throws IOException {
        try{
            if(command.get(1).equals("row") || command.get(1).equals("r")){
                int index = Integer.parseInt(command.get(2));
                if(index < 1 || index > 3)
                    System.err.println("You have inserted a wrong number (1 - 3)");
                else
                    ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, position, 0, index));
            }
            else if(command.get(1).equals("column") || command.get(1).equals("c")){
                int index = Integer.parseInt(command.get(2));
                if(index < 1 || index > 4)
                    System.err.println("You have inserted a wrong number (1 - 4)");
                else
                    ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, position, 1, index));
            }
            else
                System.err.println("Illegal command from player");
        } catch (NumberFormatException e){
            System.err.println("Illegal command from player");
        }
    }

    private void switchRequest(ArrayList<String> command) throws IOException {
        try{
            int depot1 = Integer.parseInt(command.get(1));
            int depot2 = Integer.parseInt(command.get(2));
            if(depot1 < 1 || depot1 > 5)
                System.err.println("You have inserted a wrong number (1 - 5)");
            else if(depot2 < 1 || depot2 > 5)
                System.err.println("You have inserted a wrong number (1 - 5)");
            else
                ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, position, depot1, depot2));
        } catch (NumberFormatException e){
            System.err.println("Illegal command from player");
        }
    }

    private void leaderRequest(ArrayList<String> command) throws IOException {
        try{
            if(command.get(1).equals("active")){
               int leaderCard = Integer.parseInt(command.get(2));
               if(leaderCard < 1 || leaderCard > 2)
                   System.err.println("You have inserted a wrong number (1 - 2)");
               else
                   ClientSocket.sendMessage(new Message_One_Parameter_Int(MessageType.LEADER_CARD_ACTIVATION, position, leaderCard));
            }
            else
            if(command.get(1).equals("discard")){
                int leaderCard = Integer.parseInt(command.get(2));
                if(leaderCard < 1 || leaderCard > 2)
                    System.err.println("You have inserted a wrong number (1 - 2)");
                else
                    ClientSocket.sendMessage(new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, position, leaderCard));
            }
            else {
                int card1 = Integer.parseInt(command.get(1));
                int card2 = Integer.parseInt(command.get(2));
                if (card1 < 49 || card1 > 64)
                    System.err.println("You have inserted a wrong number (49 - 64)");
                else if (card2 < 49 || card2 > 64)
                    System.err.println("You have inserted a wrong number (49 - 64)");
                else if (card1 == card2)
                    System.err.println("You can't chose two same cards.");
                else {
                    super.setLeaderCard(position, card1, card2);
                    ClientSocket.sendMessage(new Message_Two_Parameter_Int(MessageType.LEADER_CARD, position, card1, card2));
                    chose_first_resources();
                }
            }
        } catch (NumberFormatException e){
            System.err.println("Illegal command from player");
        }
    }

    private void fourCommandRequest(ArrayList<String> command) throws IOException {
        switch (command.get(0)){
            case "chose":
                command.remove(0);
                if(command.get(0).equals("leader") || command.get(0).equals("l"))
                    leaderRequest(command);
                else
                    System.err.println("Illegal command from player");
                break;
            case "power":
                cardPowerRequest(command);
                break;
            case "lp":
                leaderPowerRequest(command);
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void cardPowerRequest(ArrayList<String> command) throws IOException {
        try {
            int slot = Integer.parseInt(command.get(2));
            int choice;
            if(command.get(3).equals("warehouse") || command.get(3).equals("w"))
                choice = 0;
            else if(command.get(3).equals("strongbox") || command.get(3).equals("s"))
                choice = 1;
            else {
                System.err.println("Illegal command from player");
                return;
            }
            if (command.get(1).equals("card") || command.get(1).equals("c")) {
                Message message = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, position, slot, choice);
                ClientSocket.sendMessage(message);
            }
            else {
                System.err.println("Illegal command from player");
            }
        } catch (NumberFormatException e){
            System.err.println("Illegal command from player");
        }
    }

    private void leaderPowerRequest(ArrayList<String> command) throws IOException {
        try {
            int slot = Integer.parseInt(command.get(1));
            Resource r = correctResource(command.get(2));
            if(r == null){
                System.err.println("Illegal command from player");
                return;
            }
            int choice;
            if(command.get(3).equals("warehouse") || command.get(3).equals("w"))
                choice = 0;
            else if(command.get(3).equals("strongbox") || command.get(3).equals("s"))
                choice = 1;
            else {
                System.err.println("Illegal command from player");
                return;
            }
            if (command.get(1).equals("leader") || command.get(1).equals("l")) {
                Message message = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, position, r, slot, choice);
                ClientSocket.sendMessage(message);
            }
            else {
                System.err.println("Illegal command from player");
            }
        } catch (NumberFormatException e){
            System.err.println("Illegal command from player");
        }
    }

    private void fiveCommandRequest(ArrayList<String> command) throws IOException {
        if(command.get(0).equals("power") && (command.get(1).equals("leader") || command.get(1).equals("l"))){
            command.remove(0);
            cardPowerRequest(command);
        }
        else if(command.get(0).equals("-bp"))
            basicPowerRequest(command);
        else
            System.err.println("Illegal command from player");
    }

    private void sixCommandRequest(ArrayList<String> command) throws IOException {
        if(command.get(0).equals("power") && (command.get(1).equals("basic") || command.get(1).equals("b"))){
            command.remove(0);
            basicPowerRequest(command);
        }
        else
            System.err.println("Illegal command from player");
    }

    private void basicPowerRequest(ArrayList<String> command) throws IOException {
        Resource r1 = correctResource(command.get(1));
        Resource r2 = correctResource(command.get(2));
        Resource r3 = correctResource(command.get(3));
        if(r1 == null || r2 == null || r3 == null){
            System.err.println("Illegal command from player");
            return;
        }
        if(command.get(4).equals("warehouse") || command.get(4).equals("w")) {
            Message message = new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, position, r1, r2, r3,0);
            ClientSocket.sendMessage(message);
        }
        else if(command.get(4).equals("strongbox") || command.get(4).equals("s")){
            Message message = new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, position, r1, r2, r3, 1);
            ClientSocket.sendMessage(message);
        }
        else
            System.err.println("Illegal command from player");
    }

    private void username(){
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nEnter your nickname: ");
        while (true) {
            try {
                String nickname = stdIn.readLine();
                if (nickname != null && !nickname.isBlank() && !nickname.equals("")) {
                    ClientSocket.sendMessage(new Message_One_Parameter_String(MessageType.LOGIN, position, nickname));
                    break;
                }
            } catch (IOException e) {
                System.out.println("error");
            }
        }
    }

    @Override
    public void login_message(Message message) {
        if(message.getClientID() == 0)
            System.out.println("You are the first player. Write a \"numplayer\" command.");
        else
            System.out.println("Waiting other players...");
    }

    @Override
    public void askPlayersNumber() {
        int playerNumber;
        try {
            playerNumber = numberInput(1, 4,  "How many players are going to play? 1 to 4 ");
            ClientSocket.sendMessage( new Message_One_Parameter_Int(MessageType.NUM_PLAYERS,position,playerNumber));
        } catch (ExecutionException | IOException e) {
            System.err.println("error");
        }
    }

    @Override
    public void new_player_message(Message message){
        Message_One_Parameter_String m = (Message_One_Parameter_String) message;
        if (m.getClientID() != this.position) {
            System.out.println("New player: " + m.getPar());
        }
    }

    @Override
    public void players_message(Message message){
        super.players_message(message);
        Message_ArrayList_String m = (Message_ArrayList_String) message;
        System.out.println("All players connected.\nPlayers: " + m.getNickNames());
        this.position = m.getClientID();
        System.out.println("You are the " + (position + 1) + "° player");
    }

    @Override
    public void start_game_message() throws IOException {
        System.out.println("All players have made their choices. Game started");
        ClientSocket.sendMessage(new Message(MessageType.TURN, position));
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
                if(activate_leader_card())
                    chose_action(first_input());
                break;
            case 5:
                if(discard_leader_card())
                    chose_action(first_input());
                break;
        }
    }

    private int first_input() {
        System.out.println("Your turn");
        System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n3 - ACTIVATE PRODUCTION\n" +
                "4 - ACTIVATE LEADER CARD\n5 - DISCARD LEADER CARD\n6 - SEE THE STATE OF THE GAME");
        int userInput = 0;
        try {
            userInput = numberInput(1, 6, "What do you want to do?");
        } catch (ExecutionException e) {
            System.out.println("error");
        }
        return userInput;
    }

    private void lastInput() throws IOException {
        System.out.println("You want to do something else?\n1 - ACTIVATE LEADER CARD\n2 - DISCARD LEADER CARD\n3 - SEE THE STATE OF THE GAME\n0 - END TURN");
        int userInput = 0;
        try{
            userInput = numberInput(0, 3, "What do you want to do?");
        } catch (ExecutionException e) {
            System.out.println("error");
        }
        switch (userInput) {
            case 1:
                if(activate_leader_card())
                    lastInput();
                break;
            case 2:
                if(discard_leader_card())
                    lastInput();
                break;
            case 0:
                currentState = GAME_STATES.FIRST_ACTION_STATE;
                end_turn();
                break;
        }
    }

    private void take_market_marble() throws IOException {
        System.out.println("TAKE MARBLE FROM MARKET");
        int x;
        int y;
        currentState = GAME_STATES.TAKE_MARBLE_STATE;
        CLI_Printer.printWarehouse(super.getGame(), position);
        try {
            CLI_Printer.printMarket(super.getGame());
            x=numberInput(0,1,"Do you want a row or a column?\n0 - ROW\n1 - COLUMN");
            if (x==0)
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
        int[] row_column;
        System.out.println("BUY_DEVELOPMENT_CARD");
        CLI_Printer.printDecks(super.getGame());
        do {
            int cardID = 0;
            try {
               cardID = (numberInput(1, 48, "Choose a card by his CardID"));
            } catch (ExecutionException e) {
                System.out.println("error");
            }
            row_column = super.getRowColumn(cardID);
            if(row_column[0] == -1 || row_column[1] == -1){
                int otherCard = 0;
                try {
                    otherCard = numberInput(0, 1, "You chose a wrong card. You want to take a different one?\n1 - YES\n0 - NO");
                } catch (ExecutionException e) {
                    System.out.println("error");
                }
                if(otherCard == 0) {
                    chose_action(first_input());
                    return;
                }
            }
            else {
                CLI_Printer.printCard(cardID);
                break;
            }
        } while (true);
        int z = chose_warehouse_strongbox();
        if(z == -1) {
            System.out.println("error");
            return;
        }
        currentState = GAME_STATES.BUY_CARD_STATE;
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
            CLI_Printer.printWarehouseStrongbox(super.getGame(), position);
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
                if(!power){
                    if (numberInput(0,1,"Do you want to activate another power production?\n1 - YES\n0 - NO") == 0) {
                        if(currentState == GAME_STATES.FIRST_ACTION_STATE)
                            chose_action(first_input());
                        else
                            activate_another_production();
                        return;
                    }
                }
                else{
                  currentState = GAME_STATES.ACTIVATE_PRODUCTION_STATE;
                  break;
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
            ArrayList<Integer> cards = super.getDevelopmentCards(position);
            CLI_Printer.printCardSlot(super.getGame(), position);
            if(cards.size() > 0) {
                if(cards.size() == 1)
                    x = cards.get(0);
                else if(cards.size() == 2)
                    x = numberInput(cards.get(0), cards.get(1), "Which card do you want to activate production? (" + cards.get(0) + " - " + cards.get(1) + ")");
                else
                    x = numberInput(1, 3, "Which card do you want to activate production? (1 - 3)");
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
        CLI_Printer.printWarehouseStrongbox(super.getGame(), position);
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
        currentState = GAME_STATES.END_TURN_STATE;
        ClientSocket.sendMessage(message);
    }

    public boolean activate_leader_card() {
        System.out.println("ACTIVATE LEADER CARD");
        int x = chose_leader_card();
        if(x == -1) {
            System.out.println("You no longer have any leader cards");
            return true;
        }
        Message message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_ACTIVATION, position, x);
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            System.out.println("error");
        }
        return false;
    }

    public boolean discard_leader_card() {
        System.out.println("DISCARD LEADER CARD");
        int x = chose_leader_card();
        if(x == -1) {
            System.out.println("You no longer have any leader cards");
            return true;
        }
        super.discardLeaderCard(position, x);
        Message message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, position, x);
        try {
            ClientSocket.sendMessage(message);
        } catch (IOException e) {
            System.out.println("error");
        }
        return false;
    }

    private void end_turn(){
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
        int leaderCard1 = super.getLeaderCard(position, 1);
        int leaderCard2 = super.getLeaderCard(position, 2);
        if(leaderCard1 != -1 && leaderCard2 != -1){
            System.out.println("Your leader cards: " );
            CLI_Printer.printLeaderCard(super.getGame(), position);
            try {
                chosenLeaderCard=numberInput(1,2,"Which leader card you choose? (1 or 2)");
                return chosenLeaderCard;
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        else if(leaderCard1 != -1){
            System.out.println("Your leader card: ");
            CLI_Printer.printLeaderCard(super.getGame(), position);
            chosenLeaderCard = 1;
        }
        return chosenLeaderCard;
    }

    public void chose_first_resources(){
        switch (position) {
            case 0:
                System.out.println("You are the 1st player. Wait for the other players to make their choices...");
                break;
            case 1:
                System.out.println("You are the 2nd player. You gain 1 resource. Chose the resource:");
                break;
            case 2:
                System.out.println("You are the 3rd player. You gain 1 resource and 1 faith point. Chose the resource:");
                break;
            case 3:
                System.out.println("You are the 4th player. You gain 2 resources and 1 faith point. Chose the resources:");
                break;
        }
        super.startGame();
    }

    private Resource chose_resource() {
        Resource chosenResource = null;
        int x;
        try {
            x=numberInput(1,4,"\n1 - " + Resource.printResource(Resource.COIN) +
                    "\n2 - " + Resource.printResource(Resource.SHIELD) + "\n3 - " + Resource.printResource(Resource.STONE) +
                    "\n4 - " + Resource.printResource(Resource.SERVANT));
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
        int choice = -1;
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
        CLI_Printer.printWarehouse(super.getGame(), position);
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

    @Override
    public void leader_card_choice(Message message) {
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        CLI_Printer.printCard(m.getPar1());
        CLI_Printer.printCard(m.getPar2());
        CLI_Printer.printCard(m.getPar3());
        CLI_Printer.printCard(m.getPar4());
        System.out.println("Chose 2 between this 4 leader cards.");
    }

    private int numberInput(int minValue, int maxValue, String question) throws ExecutionException {
        int number = minValue - 1;
        do {
            try {
                System.out.println(question);
                number = Integer.parseInt(readLine());
                if (number < minValue || number > maxValue) {
                    System.err.println("Invalid number! Please try again.\n");
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid input! Please try again.\n");
            }
        } while (number < minValue || number > maxValue);
        return number;
    }

    private String readLine() throws ExecutionException {
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

    @Override
    public void turn_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        currentState = GAME_STATES.FIRST_ACTION_STATE;
        if (m.getPar() == 1) {
            System.out.println("It's your turn");
        }
        else {
            System.out.println("Wait for other players to finish their turns...");
        }
    }

    @Override
    public void end_turn_message(Message message) {
        if (message.getClientID() != position) {
            System.out.println("Player " + super.getNickname(message.getClientID()) + " has finished his turn.");
        }
        if (position == 0) {
            if (message.getClientID() == super.getNumOfPlayers() - 1) {
                System.out.println("It's your turn");
            }
        } else if (message.getClientID() == position - 1) {
            System.out.println("It's your turn");
        }
    }

    @Override
    public void buy_card_message(Message message) {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if(m.getClientID() != position)
            System.out.println("Player " + super.getNickname(m.getClientID()) + " bought a new card and inserted it in " +
                "the " + m.getPar2() + "° slot.");
        super.buy_card_message(message);
    }

    @Override
    public void card_remove_message(Message message) {
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        if(super.getNumOfPlayers() == 1 && m.getClientID() == 1)
            System.out.println("Ludovico has removed one deck card from row " + (m.getPar1() +1 + " and" +
                    " column " + (m.getPar2() +1)));
        else if(m.getClientID() != position)
            System.out.println("Deck card from row " + (m.getPar1()+1) + " and column "
                    + (m.getPar2() +1) + " has been removed");
        super.card_remove_message(message);
    }

    @Override
    public void endProductionMessage(Message message) {
        if(message.getClientID() != position)
            System.out.println("Player " + super.getNickname(message.getClientID()) + " has activated production powers.");
    }

    @Override
    public void market_change(Message message) {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if (m.getPar1() == 0) {
            if(m.getClientID() != position) {
                System.out.println("Player " + super.getNickname(m.getClientID()) + " has chosen row " + m.getPar2() + " of the market");
                CLI_Printer.printRowMarket(super.getGame(), m.getPar2());
            }
        } else {
            if(m.getClientID() != position) {
                System.out.println("Player " + super.getNickname(m.getClientID()) + " has chosen column " + m.getPar2() + " of the market");
                CLI_Printer.printColumnMarket(super.getGame(), m.getPar2());
            }
        }
        super.market_change(message);
    }

    @Override
    public void faith_points_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        if(m.getClientID() != position) {
            if (super.getNumOfPlayers() == 1 && m.getClientID() == 1)
                System.out.println("Ludovico has increased his faith points. Now it has " + m.getPar());
            else
                System.out.println("Player " + super.getNickname(m.getClientID()) + " has increased its faith points. Now it has " + m.getPar());
        }
        super.faith_points_message(message);
    }

    @Override
    public void increase_warehouse_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        if(m.getPar1() != -1) {
            if(m.getClientID() != position)
                System.out.println("Player " + super.getNickname(m.getClientID()) + " has inserted 1 " + Resource.printResource(m.getResource())
                    + " in its " + m.getPar1() + "° depot");
        }
        else if(m.getClientID() != position)
            System.out.println("Player " + super.getNickname(m.getClientID()) + " has discarded 1 " + Resource.printResource(m.getResource())
                    + " marble");
        super.increase_warehouse_message(message);
    }

    @Override
    public void switch_depot_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if(m.getClientID() != position)
            System.out.println("Player " + super.getNickname(m.getClientID()) + " has switched its " + m.getPar1()
                + "° depot with its " + m.getPar2() + "° depot.");
        super.switch_depot_message(message);
    }

    @Override
    public void vatican_report_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if(super.getNumOfPlayers() == 1 && m.getPar1() == 1)
            System.out.println("Ludovico activated Vatican Report." +
                    " Now you have " + m.getPar2() + " victory points from Vatican Report");
        else if(m.getClientID() == position)
            System.out.println("Player " + super.getNickname(m.getPar1()) + " activated Vatican Report." +
                    " Now you have " + m.getPar2() + " victory points from Vatican Report");
        super.vatican_report_message(message);
    }

    @Override
    public void leader_card_activation_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        if(m.getClientID() != position) {
            System.out.println("Player " + super.getNickname(m.getClientID()) + " has activated a leader card");
        }
        super.leader_card_activation_message(message);
    }

    @Override
    public void extra_depot_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        if(m.getClientID() != position) {
            System.out.println("Player " + super.getNickname(m.getClientID()) + " has a new extra depot of " + Resource.printResource(m.getResource()));
        }
        super.extra_depot_message(message);
    }

    @Override
    public void leader_card_discard_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        if(m.getClientID() != position) {
            System.out.println("Player " + super.getNickname(m.getClientID()) + " has discarded one leader card: ");
            CLI_Printer.printCard(m.getPar());
        }
        else
            super.leader_card_discard_message(message);
    }

    @Override
    public void ok_message() {
        if(currentState == null){
            if (super.getGame().isStartGame())
                System.out.println("Waiting players made their choices...");
        }
    }

    @Override
    public void chosen_slot_message(Message message){
        Message_Three_Parameter_Int m = (Message_Three_Parameter_Int) message;
        if(m.getPar3() == -1)
            System.out.println("You can insert your card in this slots: " + m.getPar1() + ", " + m.getPar2());
        else
            System.out.println("You can insert your card in this slots: " + m.getPar1() + ", " + m.getPar2() + ", " + m.getPar3());
    }

    @Override
    public void take_marble_message(Message message) {
        Message_ArrayList_Marble m = (Message_ArrayList_Marble) message;
        super.take_marble_message(message);
        System.out.println("You have chosen this marbles: ");
        CLI_Printer.printMarbles(super.getGame(), m.getMarbles());
    }

    private void chose_marble(ArrayList<Marble> marbles) throws IOException {
        Marble chosenMarble;
        System.out.println("You have chosen this marbles: ");
        CLI_Printer.printMarbles(super.getGame(), marbles);
        try {
            CLI_Printer.printWarehouse(super.getGame(), position);
            int switchDepot = numberInput(0, 1, "Do you want to switch your depots?\n1 - YES\n0 - NO");
            if (switchDepot == 1) {
                switch_depot();
                return;
            }
        } catch (ExecutionException e) {
            System.out.println("error");
        }
        if (marbles.size() == 1) {
            chosenMarble = marbles.remove(0);
            super.setMarbles(marbles);
            currentState = GAME_STATES.END_TURN_STATE;
            Message message = new Message_One_Parameter_Marble(MessageType.USE_MARBLE, position, chosenMarble);
            ClientSocket.sendMessage(message);
            return;
        }
        printMarbles(marbles);
        while (true) {
            String choice = null;
            try {
                choice = readLine();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            choice = choice.toUpperCase();
            chosenMarble = correct_marble(choice, marbles);
            if (chosenMarble != null)
                break;
            else
                System.err.println("Chose a right marble");
        }
        if (marbles.size() == 0)
            currentState = GAME_STATES.END_TURN_STATE;
        else
            super.setMarbles(marbles);
        Message message = new Message_One_Parameter_Marble(MessageType.USE_MARBLE, position, chosenMarble);
        ClientSocket.sendMessage(message);
    }

    private void printMarbles(ArrayList<Marble> marbles){
        System.out.println("Chose one marble:");
        for(Marble marble: marbles){
            System.out.println(marble.toString() + " - " + marble.colorString());
        }
    }

    @Override
    public void white_conversion_card_message(Message message) {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("You have chosen a white marble and you have two active WhiteConversionCard. Chose one of them");
    }

    @Override
    public void quit_message(Message message) {
        Message_One_Parameter_String m = (Message_One_Parameter_String) message;
        if (super.getNumOfPlayers() != 0) {
            System.out.println("Player " + m.getPar() + " disconnected. Game ended.");
            ClientSocket.setDisconnected();
            ClientSocket.disconnect();
            System.out.println("\nDisconnecting.\n");
            System.exit(1);
        } else if (m.getPar() != null)
            System.out.println("Player " + m.getPar() + " disconnected before game is started");
        else
            System.out.println("One player disconnected before game is started");
    }

    @Override
    public void end_game_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if(super.getNumOfPlayers() == 1 && m.getClientID() == 1){
            System.out.println("Game ended. Ludovico win.");
        }
        else {
            System.out.println("Game ended. " + super.getNickname(m.getClientID()) + " win the game."
                    + " It made " + m.getPar1() + " victory points and " + m.getPar2()
                    + " total resources.");
        }
        ClientSocket.setDisconnected();
        ClientSocket.disconnect();
        System.out.println("\nDisconnecting.\n");
        System.exit(1);
    }

    @Override
    public void error_message(Message message) {
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

    private void already_taken_nickName_error() {
        System.err.println("Nickname already taken. Chose a different one");
    }

    private void wrong_parameters_error() {
        System.err.println("You have inserted wrong parameters");
    }

    private void wrong_turn_error(){
        System.err.println("It's not your turn");
    }

    private void empty_deck_error() {
        System.err.println("You have chosen an empty deck");
    }

    private void empty_slot_error() {
        System.err.println("You have no cards in this slot");
    }

    private void wrong_power_error() {
        System.err.println("You can't activate this production power");
    }

    private void not_enough_cards_error() {
        System.err.println("You don't have enough development cards to activate this leader card");
    }

    private void full_slot_error() {
        System.err.println("You can't insert this card in any slot");
    }

    private void illegal_operation_error() {
        System.err.println("You can't do this operation at this moment");
    }

    private void impossible_switch_error() {
        System.err.println("You can't switch this depots");
    }

    private void not_enough_resource_error() {
        System.err.println("You have not enough resources to do this operation");
    }

    private void already_active_error() {
        System.err.println("You activated this leader card previously");
    }

    private void already_discard_error() {
        System.err.println("You discard this leader card previously");
    }


}