package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.App;
import it.polimi.ingsw.model.games.states.GameStates;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.client.ClientSocket;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.ClientView;

import java.io.*;
import java.net.UnknownHostException;
import java.util.*;

public class CLI extends ClientView{

    private Thread inputThread;
    private final BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
    private Message receivedMessage;
    private final Object lock = new Object();
    private int position;
    private boolean turn;

    public CLI(){
        position = 0;
        turn = false;
    }

    @Override
    public void launchCLI(){
        App.createClient(this);
        try {
            connectToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printLogo();
        try {
            login();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void launchCLI(String hostname, int port){
        App.createClient(this);
        try {
            connectToServer(hostname, port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        printLogo();
        try {
            login();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void connectToServer() throws IOException {
        while (true) {
            System.out.println("\nEnter hostname [localhost]: ");
            String hostName = stdIn.readLine();
            if (hostName == null || hostName.isBlank() || hostName.equals(""))
                hostName = "localhost";
            System.out.println("Enter port [12460]: ");
            String portNumber = stdIn.readLine();
            if (portNumber == null || portNumber.isBlank() || portNumber.equals(""))
                portNumber = "12460";
            try {
                App.startClient(hostName, Integer.parseInt(portNumber));
                System.out.println("Accepted by Server\n");
                break;
            } catch (UnknownHostException e) {
                System.err.println("Unknown host " + hostName);
            } catch (IOException e) {
                System.err.println("Can't connect to host " + hostName);
            } catch (NumberFormatException e) {
                System.err.println("You have to insert a number!");
            }
        }
    }

    private void connectToServer(String hostname, int port) throws IOException {
        try {
            App.startClient(hostname, port);
            System.out.println("Accepted by Server\n");
            return;
        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + hostname);
        } catch (IOException e) {
            System.err.println("Can't connect to host " + hostname);
        } catch (NumberFormatException e) {
            System.err.println("You have to insert a number!");
        }
        connectToServer();
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
    }

    private void login() throws InterruptedException {
        inputThread = new Thread(() -> {
            try {
                username();
                choseLeaderCards();
            } catch (IOException | InterruptedException e) {
            }
        });
        inputThread.start();
        inputThread.join();
    }

    private void username() throws IOException, InterruptedException {
        while (true) {
            System.out.println("\nEnter username: ");
            String username = stdIn.readLine();
            if (username == null || username.isBlank() || username.equals(""))
                System.err.println("Insert a valid username");
            else {
                ClientSocket.sendMessage(new MessageOneParameterString(MessageType.LOGIN, position, username));
                break;
            }
        }
        waitMessage();
        if(receivedMessage.getMessageType() == MessageType.ERR)
            username();
        else if(receivedMessage.getMessageType() == MessageType.LOGIN && receivedMessage.getClientID() == 0)
            numPlayers();
        else
            waitMessage();
    }

    private void numPlayers() throws InterruptedException {
        while (true) {
            try {
                System.out.println("How many players are going to play? 1 to 4");
                int playerNumber = Integer.parseInt(stdIn.readLine());
                if (playerNumber < 1 || playerNumber > 4)
                    System.err.println("You have inserted a wrong number (1 - 4)");
                else {
                    ClientSocket.sendMessage(new MessageOneParameterInt(MessageType.NUM_PLAYERS, position, playerNumber));
                    break;
                }
            } catch (NumberFormatException | IOException e) {
                System.err.println("You have to insert one number!");
            }
        }
        waitMessage();
        if(receivedMessage.getMessageType() == MessageType.ERR)
            numPlayers();
        else if(receivedMessage.getMessageType() == MessageType.OK ){
            System.out.println("Waiting other players...");
            waitMessage();
        }
    }

    private void choseLeaderCards() {
        MessageFourParameterInt m = (MessageFourParameterInt) receivedMessage;
        int card1;
        int card2;
        while (true) {
            try {
                System.out.println("Chose the first leader card:");
                card1 = Integer.parseInt(stdIn.readLine());
                if (card1 != m.getPar1() && card1 != m.getPar2() && card1 != m.getPar3() && card1 != m.getPar4())
                    System.err.println("You have selected a wrong card");
                else
                    break;
            } catch (NumberFormatException | IOException e) {
                System.err.println("You have to insert a number!");
            }
        }
        while (true) {
            try {
                System.out.println("Chose the second leader card:");
                card2 = Integer.parseInt(stdIn.readLine());
                if (card2 != m.getPar1() && card2 != m.getPar2() && card2 != m.getPar3() && card2 != m.getPar4())
                    System.err.println("You have selected a wrong card");
                else if (card1 == card2)
                    System.err.println("You can't chose two same cards.");
                else {
                    ClientView.setLeaderCard(position, card1, card2);
                    ClientSocket.sendMessage(new MessageTwoParameterInt(MessageType.LEADER_CARD, position, card1, card2));
                    choseFirstResources();
                    break;
                }
            } catch (NumberFormatException | IOException e) {
                System.err.println("You have to insert a number!");
            }
        }
    }

    private void choseFirstResources() throws IOException {
        switch (position) {
            case 0:
                System.out.println("You are the 1st player. Wait for the other players to make their choices...");
                break;
            case 1: {
                System.out.println("You are the 2nd player. You gain 1 resource.");
                Resource r = choseResource("Chose the resource:");
                ClientSocket.sendMessage(new MessageOneIntOneResource(MessageType.ONE_FIRST_RESOURCE, position, r, 1));
            }
                break;
            case 2: {
                System.out.println("You are the 3rd player. You gain 1 resource and 1 faith point.");
                Resource r = choseResource("Chose the resource:");
                ClientSocket.sendMessage(new MessageOneIntOneResource(MessageType.ONE_FIRST_RESOURCE, position, r, 1));
            }
                break;
            case 3: {
                System.out.println("You are the 4th player. You gain 2 resources and 1 faith point.");
                Resource r1 = choseResource("Chose first resource:");
                Resource r2 = choseResource("Chose second resource:");
                ClientSocket.sendMessage(new MessageTwoResource(MessageType.TWO_FIRST_RESOURCE, position, r1, r2));
            }
                break;
        }
    }

    private Resource choseResource(String text) throws IOException {
        while (true) {
            System.out.println(text);
            System.out.println("COIN(CO) / SHIELD(SH) / SERVANT/(SE) / STONE(ST)");
            Resource r = correctResource(stdIn.readLine());
            if(r != null)
                return r;
            else
                System.err.println("Chose a correct resource");
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

    private int choseWarehouseStrongbox() throws IOException {
        while (true) {
            System.out.println("Where do you want to take the resources from?");
            System.out.println("WAREHOUSE(W) / STRONGBOX(S)");
            String choice = stdIn.readLine();
            choice = choice.toUpperCase(Locale.ROOT);
            if(choice.equals("WAREHOUSE") || choice.equals("W"))
                return 0;
            else if(choice.equals("STRONGBOX") || choice.equals("S"))
                return 1;
            else
                System.err.println("Insert a correct input");
        }
    }

    private void startCLI() {
        for(int i = getServerMessages().size() - 1; i >= 0; i--)
            System.out.println(getServerMessages().get(i));
        inputThread = new Thread(() -> {
            try {
                readCommand();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        inputThread.start();
    }

    private void readCommand() throws IOException, InterruptedException {
        while (true){
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

    private void commandInterpreter(ArrayList<String> command) throws IOException, InterruptedException {
        if(command.get(0).equals("help")  || command.get(0).equals("-h")){
            helpRequest();
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
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void helpRequest() throws IOException {
        String file = null;
        if(!turn)
            file = "src/main/resources/helpCommand/notTurnHelp.txt";
        else {
            switch (getCurrentState()) {
                case FIRST_ACTION_STATE:
                case BUY_CARD_STATE:
                    file = "src/main/resources/helpCommand/firstHelp";
                    break;
                case TAKE_MARBLE_STATE:
                    file = "src/main/resources/helpCommand/marbleHelp";
                    break;
                case FIRST_POWER_STATE:
                case ACTIVATE_PRODUCTION_STATE:
                    file = "src/main/resources/helpCommand/powerHelp.txt";
                    break;
                case END_TURN_STATE:
                    file = "src/main/resources/helpCommand/endTurnHelp";
                    break;
            }
        }
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }

    private void oneCommandRequest(ArrayList<String> command) throws IOException {
        switch (command.get(0)){
            case "turn":
            case "-tu":
                ClientSocket.sendMessage(new Message(MessageType.TURN, position));
                break;
            case "-sm":
                CLIPrinter.printMarket(super.getGame());
                break;
            case "-sf":
                CLIPrinter.printFaithTrack(super.getGame());
                break;
            case "-sd":
                CLIPrinter.printDecks(super.getGame());
                break;
            case "-smp":
                CLIPrinter.printPlayerBoard(super.getGame(), position);
                break;
            case "-smw":
                CLIPrinter.printWarehouse(super.getGame(), position);
                break;
            case "-sms":
                CLIPrinter.printStrongbox(super.getGame(), position);
                break;
            case "-smc":
                CLIPrinter.printCardSlot(super.getGame(), position);
                break;
            case "-sml":
                CLIPrinter.printLeaderCard(super.getGame(), position);
                break;
            case"-smb":
                if(isState(GameStates.TAKE_MARBLE_STATE) && getMarbles().size() > 0)
                    CLIPrinter.printMarbles(super.getGame(), getMarbles());
                else
                    System.err.println("Illegal command from player");
                break;
            case "-bp":
                basicPowerRequest();
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
        if(isState(GameStates.ACTIVATE_PRODUCTION_STATE)) {
            ClientSocket.sendMessage(new Message(MessageType.END_PRODUCTION, position));
            setCurrentState(GameStates.END_TURN_STATE);
        }
        else
            System.err.println("You can't do this operation at this moment");
    }

    private void endTurnRequest() throws IOException {
        if(isState(GameStates.END_TURN_STATE)) {
            ClientSocket.sendMessage(new Message(MessageType.END_TURN, position));
            setCurrentState(GameStates.FIRST_ACTION_STATE);
        }
        else
            System.err.println("You can't do this operation at this moment");
    }

    private void twoCommandRequest(ArrayList<String> command) throws IOException, InterruptedException {
        switch (command.get(0)){
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
                useMarbleRequest(command.get(1), getMarbles());
                break;
            case "buycard":
            case "buy":
            case "-bc":
                buyCardRequest(command);
                break;
            case "basic":
                if(command.get(1).equals("power"))
                    basicPowerRequest();
                else
                    System.err.println("Illegal command from player");
                break;
            case "end":
                endRequest(command.get(1));
                break;
            case "-cp":
                cardPowerRequest(command);
                break;
            case "-lp":
                leaderPowerRequest(command);
                break;
            case "-al":
                leaderActiveRequest(command.get(1));
                break;
            case "-dl":
                leaderDiscardRequest(command.get(1));
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void seeRequest(String request){
        switch (request){
            case "market":
            case "m":
                CLIPrinter.printMarket(super.getGame());
                break;
            case "faithtrack":
            case "f":
                CLIPrinter.printFaithTrack(super.getGame());
                break;
            case "decks":
            case "d":
                CLIPrinter.printDecks(super.getGame());
                break;
            case "marbles":
            case "mb":
                if(isState(GameStates.TAKE_MARBLE_STATE) && getMarbles().size() > 0)
                    CLIPrinter.printMarbles(super.getGame(), getMarbles());
                else
                    System.err.println("Illegal command from player");
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void useMarbleRequest(String marble, ArrayList<Marble> marbles) throws IOException, InterruptedException {
        if(marbles == null){
            System.err.println("You can't do this operation at this moment");
            return;
        }
        marble = marble.toUpperCase();
        Marble chosenMarble = correctMarble(marble, marbles);
        if (chosenMarble != null){
            if(marbles.size() == 0)
                setCurrentState(GameStates.END_TURN_STATE);
            Message message = new MessageOneParameterMarble(MessageType.USE_MARBLE, position, chosenMarble);
            ClientSocket.sendMessage(message);
            waitMessage();
            if(receivedMessage.getMessageType() == MessageType.WHITE_CONVERSION_CARD)
                whiteConversionRequest();
        }
        else
            System.err.println("You have chosen a wrong marble");
    }

    private Marble correctMarble(String input, ArrayList<Marble> marbles){
        for(int i = 0; i < marbles.size(); i++){
            if(input.equals(marbles.get(i).toString()))
                return marbles.remove(i);
            if(input.equals(marbles.get(i).toStringAbb()))
                return marbles.remove(i);
        }
        return null;
    }

    private void whiteConversionRequest() throws IOException {
        while (true){
            try {
                int leaderCard = Integer.parseInt(stdIn.readLine());
                if (leaderCard < 1 || leaderCard > 2)
                    System.err.println("You have inserted a wrong number (1 - 2)");
                else {
                    Message returnMessage = new MessageOneParameterInt(MessageType.WHITE_CONVERSION_CARD, position, leaderCard);
                    ClientSocket.sendMessage(returnMessage);
                    break;
                }
            } catch (NumberFormatException e) {
                System.err.println("You have to insert one number!");
            }
        }
    }

    private void leaderActiveRequest(String leader) throws IOException {
        int leaderCard = Integer.parseInt(leader);
        if (leaderCard < 1 || leaderCard > 2)
            System.err.println("You have inserted a wrong number (1 - 2)");
        else
            ClientSocket.sendMessage(new MessageOneParameterInt(MessageType.LEADER_CARD_ACTIVATION, position, leaderCard));
    }

    private void leaderDiscardRequest(String leader) throws IOException {
        int leaderCard = Integer.parseInt(leader);
        if (leaderCard < 1 || leaderCard > 2)
            System.err.println("You have inserted a wrong number (1 - 2)");
        else {
            discardLeaderCard(position, leaderCard);
            ClientSocket.sendMessage(new MessageOneParameterInt(MessageType.LEADER_CARD_DISCARD, position, leaderCard));
        }
    }

    private void endRequest(String command) throws IOException {
        switch (command) {
            case "power":
                endPowerRequest();
                break;
            case "turn":
                endTurnRequest();
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void threeCommandRequest(ArrayList<String> command) throws IOException {
        switch (command.get(0)){
            case "see":
                seeRequest(command);
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
            case "-cp":
                cardPowerRequest(command);
                break;
            case "active":
                if(command.get(1).equals("leader"))
                    leaderActiveRequest(command.get(2));
                else
                    System.err.println("Illegal command from player");
                break;
            case "discard":
                if(command.get(1).equals("leader"))
                    leaderDiscardRequest(command.get(2));
                else
                    System.err.println("Illegal command from player");
            case "card":
                if(command.get(1).equals("power")){
                    command.remove(0);
                    cardPowerRequest(command);
                }
                else
                    System.err.println("Illegal command from player");
                break;
            case "leader":
                if(command.get(1).equals("power")){
                    command.remove(0);
                    leaderPowerRequest(command);
                }
                else
                    System.err.println("Illegal command from player");
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
                CLIPrinter.printPlayerBoard(super.getGame(), position);
                break;
            case "warehouse":
            case "wh":
                CLIPrinter.printWarehouse(super.getGame(), position);
                break;
            case "strongbox":
            case "sb":
                CLIPrinter.printStrongbox(super.getGame(), position);
                break;
            case "cards":
            case "c":
                CLIPrinter.printCardSlot(super.getGame(), position);
                break;
            case "leader":
            case "l":
                CLIPrinter.printLeaderCard(super.getGame(), position);
                break;
            default:
                System.err.println("Illegal command from player");
                break;
        }
    }

    private void seePlayerBoardRequest(String board){
        try {
            int player = Integer.parseInt(board);
            if(player < 1 || player > getNumOfPlayers())
                System.err.println("You have inserted a wrong number (1 - " + getNumOfPlayers() + ")");
            else
                CLIPrinter.printPlayerBoard(super.getGame(), player-1);
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void seeWarehouseRequest(String warehouse){
        try {
            int player = Integer.parseInt(warehouse);
            if(player < 1 || player > getNumOfPlayers())
                System.err.println("You have inserted a wrong number (1 - " + getNumOfPlayers() + ")");
            else
                CLIPrinter.printWarehouse(super.getGame(), player-1);
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void seeStrongboxRequest(String strongbox){
        try {
            int player = Integer.parseInt(strongbox);
            if(player < 1 || player > getNumOfPlayers())
                System.err.println("You have inserted a wrong number (1 - " + getNumOfPlayers() + ")");
            else
                CLIPrinter.printStrongbox(super.getGame(), player -1);
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void seeCardsRequest(String cards){
        try {
            int player = Integer.parseInt(cards);
            if(player < 1 || player > getNumOfPlayers())
                System.err.println("You have inserted a wrong number (1 - " + getNumOfPlayers() + ")");
            else
                CLIPrinter.printCardSlot(super.getGame(), player-1);
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void seeLeaderRequest(String leader){
        try {
            int player = Integer.parseInt(leader);
            if(player < 1 || player > getNumOfPlayers())
                System.err.println("You have inserted a wrong number (1 - " + getNumOfPlayers() + ")");
            else
                CLIPrinter.printLeaderCard(super.getGame(), player-1);
        } catch (NumberFormatException e){
            System.err.println("You have to insert one number!");
        }
    }

    private void buyCardRequest(ArrayList<String> command) throws IOException {
        if(isState(GameStates.FIRST_ACTION_STATE)) {
            try {
                int cardID = Integer.parseInt(command.get(1));
                if (cardID < 1 || cardID > 48)
                    System.err.println("You have inserted a wrong number (1 - 48)");
                else {
                    int[] rowColumn = super.getRowColumn(cardID);
                    if (rowColumn[0] == -1) {
                        System.err.println("You have chosen a wrong card");
                        return;
                    }
                    int choice = choseWarehouseStrongbox();
                    setCurrentState(GameStates.BUY_CARD_STATE);
                    Message message = new MessageThreeParameterInt(MessageType.BUY_CARD, position, rowColumn[0], rowColumn[1], choice);
                    ClientSocket.sendMessage(message);
                    waitMessage();
                    if (receivedMessage.getMessageType() == MessageType.CHOSEN_SLOT)
                        choseSlotRequest();
                    else if (receivedMessage.getMessageType() == MessageType.OK) {
                        CLIPrinter.printCardSlot(super.getGame(), position);
                        setCurrentState(GameStates.END_TURN_STATE);
                    } else {
                        setCurrentState(GameStates.FIRST_ACTION_STATE);
                    }
                }
            } catch (NumberFormatException | InterruptedException e) {
                System.err.println("Illegal command from player");
            }
        }
        else
            System.err.println("You can't do this operation at this moment");
    }

    private void choseSlotRequest() throws IOException {
        MessageThreeParameterInt m = (MessageThreeParameterInt) receivedMessage;
        CLIPrinter.printCardSlot(super.getGame(), position);
        while (true){
            try {
                System.out.println("\nChose one slot:");
                int chosenSlot = Integer.parseInt(stdIn.readLine());
                if (chosenSlot != m.getPar1() && chosenSlot != m.getPar2() && chosenSlot != m.getPar3())
                    System.err.println("You have chosen a wrong slot");
                else {
                    ClientSocket.sendMessage(new MessageOneParameterInt(MessageType.CHOSEN_SLOT, position, chosenSlot));
                    waitMessage();
                    if(receivedMessage.getMessageType() == MessageType.OK) {
                        CLIPrinter.printCardSlot(super.getGame(), position);
                        setCurrentState(GameStates.END_TURN_STATE);
                    }
                    else
                        setCurrentState(GameStates.FIRST_ACTION_STATE);
                    break;
                }
            } catch (NumberFormatException | InterruptedException e) {
                System.err.println("You have to insert one number!");
            }
        }
    }

    private void takeMarbleRequest(ArrayList<String> command) throws IOException {
        if(isState(GameStates.FIRST_ACTION_STATE)) {
            try {
                if (command.get(1).equals("row") || command.get(1).equals("r")) {
                    int index = Integer.parseInt(command.get(2));
                    if (index < 1 || index > 3)
                        System.err.println("You have inserted a wrong number (1 - 3)");
                    else {
                        setCurrentState(GameStates.TAKE_MARBLE_STATE);
                        ClientSocket.sendMessage(new MessageTwoParameterInt(MessageType.TAKE_MARBLE, position, 0, index));
                    }
                } else if (command.get(1).equals("column") || command.get(1).equals("c")) {
                    int index = Integer.parseInt(command.get(2));
                    if (index < 1 || index > 4)
                        System.err.println("You have inserted a wrong number (1 - 4)");
                    else {
                        setCurrentState(GameStates.TAKE_MARBLE_STATE);
                        ClientSocket.sendMessage(new MessageTwoParameterInt(MessageType.TAKE_MARBLE, position, 1, index));
                    }
                } else
                    System.err.println("Illegal command from player");
            } catch (NumberFormatException e) {
                System.err.println("Illegal command from player");
            }
        }
        else
            System.err.println("You can't do this operation at this moment");
    }

    private void switchRequest(ArrayList<String> command) throws IOException {
        if(isState(GameStates.TAKE_MARBLE_STATE)) {
            try {
                int depot1 = Integer.parseInt(command.get(1));
                int depot2 = Integer.parseInt(command.get(2));
                if (depot1 < 1 || depot1 > 5)
                    System.err.println("You have inserted a wrong number (1 - 5)");
                else if (depot2 < 1 || depot2 > 5)
                    System.err.println("You have inserted a wrong number (1 - 5)");
                else
                    ClientSocket.sendMessage(new MessageTwoParameterInt(MessageType.SWITCH_DEPOT, position, depot1, depot2));
            } catch (NumberFormatException e) {
                System.err.println("Illegal command from player");
            }
        }
        else
            System.err.println("You can't do this operation at this moment");
    }

    private void cardPowerRequest(ArrayList<String> command) throws IOException {
        if (isState(GameStates.FIRST_ACTION_STATE) || isState(GameStates.ACTIVATE_PRODUCTION_STATE)) {
            try {
                int slot = Integer.parseInt(command.get(1));
                int choice = choseWarehouseStrongbox();
                if (isState(GameStates.FIRST_ACTION_STATE))
                    setCurrentState(GameStates.FIRST_POWER_STATE);
                Message message = new MessageTwoParameterInt(MessageType.DEVELOPMENT_CARD_POWER, position, slot, choice);
                ClientSocket.sendMessage(message);
            } catch (NumberFormatException e) {
                System.err.println("Illegal command from player");
            }
        } else
            System.err.println("You can't do this operation at this moment");
    }

    private void leaderPowerRequest(ArrayList<String> command) throws IOException {
        if (isState(GameStates.FIRST_ACTION_STATE) || isState(GameStates.ACTIVATE_PRODUCTION_STATE)) {
            try {
                int slot = Integer.parseInt(command.get(1));
                Resource r = choseResource("Which resource you want to gain?");
                int choice = choseWarehouseStrongbox();
                if (isState(GameStates.FIRST_ACTION_STATE))
                    setCurrentState(GameStates.FIRST_POWER_STATE);
                Message message = new MessageOneResourceTwoInt(MessageType.LEADER_CARD_POWER, position, r, slot, choice);
                ClientSocket.sendMessage(message);
            } catch (NumberFormatException e) {
                System.err.println("Illegal command from player");
            }
        } else
            System.err.println("You can't do this operation at this moment");
    }

    private void basicPowerRequest() throws IOException {
        if(isState(GameStates.FIRST_ACTION_STATE) || isState(GameStates.ACTIVATE_PRODUCTION_STATE)) {
            Resource r1 = choseResource("Which resource you want to decrease?");
            Resource r2 = choseResource("Which resource you want to decrease?");
            Resource r3 = choseResource("Which resource you want to gain?");
            int choice = choseWarehouseStrongbox();
            if (isState(GameStates.FIRST_ACTION_STATE))
                setCurrentState(GameStates.FIRST_POWER_STATE);
            Message message = new MessageThreeResourceOneInt(MessageType.BASIC_POWER, position, r1, r2, r3, choice);
            ClientSocket.sendMessage(message);
        }
        else
            System.err.println("You can't do this operation at this moment");
    }

    @Override
    public void loginMessage(Message message) {
        notifyMessage(message);
        if(message.getClientID() == 0)
            System.out.println("You are the first player");
        else
            System.out.println("Waiting other players...");
    }

    @Override
    public void newPlayerMessage(Message message){
        MessageOneParameterString m = (MessageOneParameterString) message;
        if (m.getClientID() != this.position) {
            System.out.println("New player: " + m.getPar());
        }
    }

    @Override
    public void playersMessage(Message message){
        super.playersMessage(message);
        MessageArrayListString m = (MessageArrayListString) message;
        System.out.println("All players connected.\nPlayers: " + m.getNickNames());
        this.position = m.getClientID();
        System.out.println("You are the " + (position + 1) + "° player");
    }

    @Override
    public void startGameMessage() throws IOException {
        super.startGame();
        startCLI();
        System.out.println("All players have made their choices. Game started");
        ClientSocket.sendMessage(new Message(MessageType.TURN, position));
    }

    @Override
    public void leaderCardChoice(Message message) {
        MessageFourParameterInt m = (MessageFourParameterInt) message;
        CLIPrinter.printCard(m.getPar1());
        CLIPrinter.printCard(m.getPar2());
        CLIPrinter.printCard(m.getPar3());
        CLIPrinter.printCard(m.getPar4());
        notifyMessage(m);
    }

    @Override
    public void turnMessage(Message message){
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        setCurrentState(GameStates.FIRST_ACTION_STATE);
        if (m.getPar() == 1) {
            turn = true;
            System.out.println("It's your turn");
        }
        else {
            turn = false;
            System.out.println("It's not your turn. Wait for other players to finish their turns...");
        }
    }

    @Override
    public void endTurnMessage(Message message) {
        if (message.getClientID() != position) {
            System.out.println("Player " + getNickname(message.getClientID()) + " has finished his turn.");
        }
        if (position == 0) {
            if (message.getClientID() == getNumOfPlayers() - 1) {
                turn = true;
                System.out.println("It's your turn");
            }
        } else if (message.getClientID() == position - 1) {
            turn = true;
            System.out.println("It's your turn");
        }
    }

    @Override
    public void buyCardMessage(Message message) {
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        if(m.getClientID() != position)
            System.out.println("Player " + getNickname(m.getClientID()) + " bought a new card and inserted it in " +
                "the " + m.getPar2() + "° slot.");
        super.buyCardMessage(message);
    }

    @Override
    public void cardRemoveMessage(Message message) {
        MessageFourParameterInt m = (MessageFourParameterInt) message;
        if(getNumOfPlayers() == 1 && m.getClientID() == 1)
            System.out.println("Ludovico has removed one deck card from row " + (m.getPar1() +1 + " and" +
                    " column " + (m.getPar2() +1)));
        else if(m.getClientID() != position)
            System.out.println("Deck card from row " + (m.getPar1()+1) + " and column "
                    + (m.getPar2() +1) + " has been removed");
        super.cardRemoveMessage(message);
    }

    @Override
    public void endProductionMessage(Message message) {
        if(message.getClientID() != position)
            System.out.println("Player " + getNickname(message.getClientID()) + " has activated production powers");
        else
            CLIPrinter.printWarehouseStrongbox(super.getGame(), position);
    }

    @Override
    public void marketChange(Message message) {
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        if (m.getPar1() == 0) {
            if(m.getClientID() != position) {
                System.out.println("Player " + getNickname(m.getClientID()) + " has chosen row " + m.getPar2() + " of the market");
                CLIPrinter.printRowMarket(super.getGame(), m.getPar2());
            }
        } else {
            if(m.getClientID() != position) {
                System.out.println("Player " + getNickname(m.getClientID()) + " has chosen column " + m.getPar2() + " of the market");
                CLIPrinter.printColumnMarket(super.getGame(), m.getPar2());
            }
        }
        super.marketChange(message);
    }

    @Override
    public void faithPointsMessage(Message message){
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        String serverMessage;
        if(m.getClientID() != position) {
            if (getNumOfPlayers() == 1 && m.getClientID() == 1)
                serverMessage = "Ludovico has increased his faith points. Now it has " + m.getPar();
            else
                serverMessage = "Player " + getNickname(m.getClientID()) + " has increased its faith points. Now it has " + m.getPar();
            if(super.isGameStarted())
                System.out.println(serverMessage);
            else
                addServerMessage(serverMessage);
        }
        super.faithPointsMessage(message);
    }

    @Override
    public void increaseWarehouseMessage(Message message){
        MessageOneIntOneResource m = (MessageOneIntOneResource) message;
        super.increaseWarehouseMessage(message);
        String serverMessage;
        if(m.getPar1() != -1) {
            if(m.getClientID() != position) {
                serverMessage = "Player " + getNickname(m.getClientID()) + " has inserted 1 " + Resource.printResource(m.getResource())
                        + " in its " + m.getPar1() + "° depot";
                if(super.isGameStarted())
                    System.out.println(serverMessage);
                else
                    addServerMessage(serverMessage);
            }
            else
                CLIPrinter.printWarehouse(super.getGame(), position);
        }
        else if(m.getClientID() != position) {
            serverMessage = "Player " + getNickname(m.getClientID()) + " has discarded 1 " + Resource.printResource(m.getResource())
                    + " marble";
            if(super.isGameStarted())
                System.out.println(serverMessage);
            else
                addServerMessage(serverMessage);
        }
    }

    @Override
    public void switchDepotMessage(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        super.switchDepotMessage(message);
        if(m.getClientID() != position)
            System.out.println("Player " + getNickname(m.getClientID()) + " has switched its " + m.getPar1()
                + "° depot with its " + m.getPar2() + "° depot.");
        else
            CLIPrinter.printWarehouse(super.getGame(), position);
    }

    @Override
    public void vaticanReportMessage(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        if(getNumOfPlayers() == 1 && m.getPar1() == 1)
            System.out.println("Ludovico activated Vatican Report." +
                    " Now you have " + m.getPar2() + " victory points from Vatican Report");
        else if(m.getClientID() == position)
            System.out.println("Player " + getNickname(m.getPar1()) + " activated Vatican Report." +
                    " Now you have " + m.getPar2() + " victory points from Vatican Report");
        super.vaticanReportMessage(message);
    }

    @Override
    public void leaderCardActivationMessage(Message message){
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        if(m.getClientID() != position) {
            System.out.println("Player " + getNickname(m.getClientID()) + " has activated a leader card");
        }
        super.leaderCardActivationMessage(message);
    }

    @Override
    public void extraDepotMessage(Message message){
        MessageOneIntOneResource m = (MessageOneIntOneResource) message;
        super.extraDepotMessage(message);
        if(m.getClientID() != position) {
            System.out.println("Player " + getNickname(m.getClientID()) + " has a new extra depot of " + Resource.printResource(m.getResource()));
        }
        else
            CLIPrinter.printWarehouse(super.getGame(), position);
    }

    @Override
    public void leaderCardDiscardMessage(Message message){
        MessageOneParameterInt m = (MessageOneParameterInt) message;
        if(m.getClientID() != position) {
            System.out.println("Player " + getNickname(m.getClientID()) + " has discarded one leader card");
        }
        else
            super.leaderCardDiscardMessage(message);
    }

    @Override
    public void okMessage() {
        if (getCurrentState() != GameStates.BUY_CARD_STATE)
            System.out.println("Request successfully completed.\n");
        if (isState(GameStates.TAKE_MARBLE_STATE)) {
            if(getMarbles().size() == 0)
                setCurrentState(GameStates.END_TURN_STATE);
        }
        if(isState(GameStates.FIRST_POWER_STATE))
            setCurrentState(GameStates.ACTIVATE_PRODUCTION_STATE);
        notifyMessage(new Message(MessageType.OK, position));
    }

    @Override
    public void chosenSlotMessage(Message message){
        MessageThreeParameterInt m = (MessageThreeParameterInt) message;
        if(m.getPar3() == -1)
            System.out.println("You can insert your card in this slots: " + m.getPar1() + ", " + m.getPar2());
        else
            System.out.println("You can insert your card in this slots: " + m.getPar1() + ", " + m.getPar2() + ", " + m.getPar3());
        notifyMessage(message);
    }

    @Override
    public void takeMarbleMessage(Message message) {
        MessageArrayListMarble m = (MessageArrayListMarble) message;
        super.takeMarbleMessage(message);
        System.out.println("You have chosen this marbles: ");
        CLIPrinter.printMarbles(super.getGame(), m.getMarbles());
    }

    @Override
    public void whiteConversionCardMessage(Message message) {
        CLIPrinter.printLeaderCard(getGame(), position);
        System.out.println("You have chosen a white marble and you have two active WhiteConversionCard. Chose one of them");
        notifyMessage(message);
    }

    @Override
    public void quitMessage(Message message) {
        MessageOneParameterString m = (MessageOneParameterString) message;
        if (getNumOfPlayers() != 0) {
            System.out.println("Player " + m.getPar() + " disconnected. Game ended.");
            System.out.println("\nExit.\n");
            endGame();
        }
        else if (m.getPar() != null)
            System.out.println("Player " + m.getPar() + " disconnected before game is started");
        else
            System.out.println("One player disconnected before game is started");
    }

    @Override
    public void endGameMessage(Message message){
        MessageTwoParameterInt m = (MessageTwoParameterInt) message;
        if(getNumOfPlayers() == 1 && m.getClientID() == 1){
            System.out.println("Game ended. Ludovico win.");
        }
        else {
            System.out.println("Game ended. " + getNickname(m.getClientID()) + " win the game."
                    + " It made " + m.getPar1() + " victory points and " + m.getPar2()
                    + " total resources.");
        }
        System.out.println("\nExit.\n");
        endGame();
    }

    @Override
    public void alreadyTakenNickNameError() {
        System.err.println("Nickname already taken. Chose a different one");
        notifyMessage(new ErrorMessage(position, ErrorType.ALREADY_TAKEN_NICKNAME));
    }

    @Override
    public void wrongParametersError() {
        System.err.println("You have inserted wrong parameters");
    }

    @Override
    public void wrongTurnError(){
        System.err.println("It's not your turn");
    }

    @Override
    public void emptyDeckError() {
        System.err.println("You have chosen an empty deck");
        notifyMessage(new ErrorMessage(position, ErrorType.EMPTY_DECK));
    }

    @Override
    public void emptySlotError() {
        System.err.println("You have no cards in this slot");
    }

    @Override
    public void wrongPowerError() {
        if(isState(GameStates.FIRST_POWER_STATE))
            setCurrentState(GameStates.FIRST_ACTION_STATE);
        System.err.println("You can't activate this production power");
    }

    @Override
    public void notEnoughCardsError() {
        System.err.println("You don't have enough development cards to activate this leader card");
    }

    @Override
    public void fullSlotError() {
        System.err.println("You can't insert this card in any slot");
        notifyMessage(new ErrorMessage(position, ErrorType.FULL_SLOT));
    }

    @Override
    public void illegalOperationError() {
        System.err.println("You can't do this operation at this moment");
    }

    @Override
    public void impossibleSwitchError() {
        System.err.println("You can't switch this depots");
    }

    @Override
    public void notEnoughResourceError() {
        if(isState(GameStates.FIRST_POWER_STATE)) {
            setCurrentState(GameStates.FIRST_ACTION_STATE);
        }
        System.err.println("You have not enough resources to do this operation");
        notifyMessage(new ErrorMessage(position, ErrorType.NOT_ENOUGH_RESOURCES));
    }

    @Override
    public void alreadyActiveError() {
        System.err.println("You activated this leader card previously");
    }

    @Override
    public void alreadyDiscardError() {
        System.err.println("You discard this leader card previously");
    }

    private void waitMessage() throws InterruptedException {
        receivedMessage = null;
        while (receivedMessage == null) {
            synchronized (lock) {
                lock.wait();
            }
        }
    }

    private void notifyMessage(Message message){
        receivedMessage = message;
        synchronized (lock){
            lock.notifyAll();
        }
    }
}