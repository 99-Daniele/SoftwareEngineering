package it.polimi.ingsw.network.client;

import it.polimi.ingsw.controller.states.CONTROLLER_STATES;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.resourceContainers.Resource;
import it.polimi.ingsw.network.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * threadIn is the thread which receive input from System.in
 * threadOut is the thread which receive input from Server.
 * turnTimer is a timer which ask server if it's player's turn every 10 seconds. Furthermore certificate that connection
 * still remains.
 * socket is the socket which connect Client and Server.
 * out and in are Client-Server serializable objects streams.
 * quit is true when user decides to quit.
 * ok summarize Server sent OK messages.
 * turn summarize Server sent TURN messages.
 * lock is a Lock for thread.wait()
 */
public class ClientSocket {

    private Thread threadOut;
    private Thread connectedThread;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private Scanner stdIn = new Scanner(new InputStreamReader(System.in));
    private String nickName;
    private int position;
    private int numPlayers;
    private int startGame;
    private ArrayList<String> players;
    private int leaderCard1;
    private int leaderCard2;
    private CONTROLLER_STATES currentState;
    private ArrayList<Marble> remainingMarbles = new ArrayList<>();
    private final Object pingLock = new Object();
    private boolean connected;

    public ClientSocket(Socket socket) throws IOException {
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.startGame = 0;
        this.position = 0;
        this.connected = true;
        currentState = CONTROLLER_STATES.WAITING_PLAYERS_STATE;
    }

    /**
     * start threadIn and threadOut.
     * threadIn firstly login player and then constantly waits user inputs from System.in
     * threadOut constantly waits Server inputs.
     */
    public void start() {
        login();
        if (connectedThread == null) {
            connectedThread = new Thread(() -> {
                try {
                    receivePing();
                } catch (InterruptedException | IOException e) {
                    if(connected) {
                        System.err.println("\nClient no longer connected to the Server");
                        connected = false;
                        disconnect();
                        System.exit(0);
                    }
                }
            });
            connectedThread.start();
        }
        if (threadOut == null) {
            threadOut = new Thread(() -> {
                try {
                    receiveMessage();
                } catch (InterruptedException | IOException e) {
                    if(connected) {
                        System.err.println("\nClient no longer connected to the Server");
                        connected = false;
                        disconnect();
                        System.exit(0);
                    }
                }
            });
            threadOut.start();
        }
    }

    /**
     * send to Server a TURN message and wait until a message return by Server.
     * if thread is awaken cause 10 seconds passed, send another TURN request to Server.
     * if after 30 seconds Server didn't return any message, connection with Server is considered as lost.
     *
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void receivePing() throws InterruptedException, IOException {
        while (true) {
            long initTime = System.currentTimeMillis();
            synchronized (pingLock) {
                pingLock.wait(30000);
                if (isTimePassed(initTime, System.currentTimeMillis(), 30000))
                    throw new IOException();
            }
        }
    }

    /**
     * until player doesn't insert a valid nickName, constantly ask a new one. Then waits Server response to know which
     * number of player it is. If it's the first player, also send the number of players of new game to Server.
     *
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void login(){
        Thread thread = new Thread(() -> {
            try {
                stdIn = new Scanner(new InputStreamReader(System.in));
                System.out.println("\nInserisci il tuo nickName:");
                String userInput = stdIn.nextLine();
                while (userInput == null || userInput.length() == 0 || userInput.isBlank()) {
                    System.err.println("Inserisci un nickName valido:");
                    userInput = stdIn.nextLine();
                }
                nickName = userInput;
                Message message = new Message_One_Parameter_String(MessageType.LOGIN, position, nickName);
                sendMessage(message);
            } catch (IOException e) {
                System.err.println("\nClient no longer connected to the Server");
                disconnect();
                System.exit(0);
            }
        });
        thread.start();
    }

    private void waiting() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
        }
        System.out.println("In attesa di altri giocatori...");
    }

    private void chose_action(int choice) throws IOException, InterruptedException {
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

    /**
     * until user doesn't insert a valid input, ask him 1 int to chose which action activate.
     *
     * @return the action chosen by user.
     */
    private int first_input() {
        System.out.println("E' il tuo turno");
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n3 - ACTIVATE PRODUCTION\n" +
                "4 - ACTIVATE LEADER CARD\n5 - DISCARD LEADER CARD");
        int userInput;
        while (true) {
            try {
                userInput = stdIn.nextInt();
                if (userInput < 1 || userInput > 5) {
                    System.err.println("Inserisci un numero da 1 a 5.");
                    System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n" +
                            "3 - ACTIVATE PRODUCTION\n4 - ACTIVATE LEADER CARD\n5 - DISCARD LEADER CARD");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("Inserisci un numero.");
                System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n" +
                        "3 - ACTIVATE PRODUCTION\n4 - ACTIVATE LEADER CARD\n5 - DISCARD LEADER CARD");
                stdIn.next();
            }
        }
        return userInput;
    }

    private void lastInput() throws IOException{
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("Vuoi fare altro?\n1 - ACTIVATE LEADER CARD\n2 - DISCARD LEADER CARD\n0 - END TURN");
        int userInput;
        while (true) {
            try {
                userInput = stdIn.nextInt();
                if (userInput < 0 || userInput > 2) {
                    System.err.println("Inserisci un numero da 0 a 2.");
                    System.out.println("\n1 - ACTIVATE LEADER CARD\n2 - DISCARD LEADER CARD\n0 - END TURN");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("Inserisci un numero.");
                System.out.println("\n1 - ACTIVATE LEADER CARD\n2 - DISCARD LEADER CARD\n0 - END TURN");
                stdIn.next();
            }
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
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("TAKE MARBLE FROM MARKET");
        System.out.println("\nScegli una riga o una colonna?\n0 - RIGA\n1 - COLONNA");
        int x;
        int y;
        while (true) {
            try {
                x = stdIn.nextInt();
                if (x < 0 || x > 1) {
                    System.err.println("Inserisci un numero da 0 a 1.");
                    System.out.println("\n0 - RIGA\n1 - COLONNA");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("Inserisci un numero.");
                System.out.println("\n0 - RIGA\n1 - COLONNA");
                stdIn.next();
            }
        }
        if (x == 0) {
            System.out.println("Scegli la riga (1 - 3):");
            while (true) {
                try {
                    y = stdIn.nextInt();
                    if (y < 1 || y > 3) {
                        System.err.println("Inserisci un numero da 1 a 3.");
                        System.out.println("\nScegli la riga:");
                    } else break;
                } catch (InputMismatchException e) {
                    System.err.println("Inserisci un numero.");
                    System.out.println("\nScegli la riga:");
                    stdIn.next();
                }
            }
        } else {
            System.out.println("Scegli la colonna (1 - 4):");
            while (true) {
                try {
                    y = stdIn.nextInt();
                    if (y < 1 || y > 4) {
                        System.err.println("Inserisci un numero da 1 a 4.");
                        System.out.println("\nScegli la colonna:");
                    } else break;
                } catch (InputMismatchException e) {
                    System.err.println("Inserisci un numero.");
                    System.out.println("\nScegli la colonna:");
                    stdIn.next();
                }
            }
        }
        currentState = CONTROLLER_STATES.TAKE_MARBLE_STATE;
        Message message = new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, position, x, y);
        sendMessage(message);
    }

    /**
     * until user doesn't insert a valid input, ask him 1 int to chose the deck to where buy card. Then send it to Server
     * and waits an OK message return.
     */
    private void buy_card() throws IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("BUY_DEVELOPMENT_CARD");
        System.out.println("\nScegli una riga (1 - 3):");
        int x;
        int y;
        while (true) {
            try {
                x = stdIn.nextInt();
                if (x < 1 || x > 3) {
                    System.err.println("Inserisci un numero da 1 a 3.");
                    System.out.println("\nScegli una riga:");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("Inserisci un numero.");
                System.out.println("\nScegli una riga:");
                stdIn.next();
            }
        }
        System.out.println("\nScegli una colonna (1 - 4):");
        while (true) {
            try {
                y = stdIn.nextInt();
                if (y < 1 || y > 4) {
                    System.err.println("\nInserisci un numero da 1 a 4.\n");
                    System.out.println("\nScegli una colonna:");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\nScegli una colonna:");
                stdIn.next();
            }
        }
        int z = chose_warehouse_strongbox();
        Message message = new Message_Three_Parameter_Int(MessageType.BUY_CARD, position, x, y, z);
        System.out.println("\n" + message.toString());
        currentState = CONTROLLER_STATES.END_TURN_STATE;
        sendMessage(message);
    }

    private void activate_another_production() throws IOException, InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("Vuoi attivare un altro potere di produzione?\n1 - SI\n0 - NO");
        try {
            int x = stdIn.nextInt();
            if (x == 1)
                activate_production();
            else {
                end_production();
            }
        } catch (InputMismatchException e) {
            end_production();
        }
    }

    private void activate_production() throws IOException, InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("ACTIVATE PRODUCTION");
        System.out.println("\nQuale produzione vuoi attivare?\n1 - CARTA SVILUPPO\n2 - POTERE BASE\n3 - CARTA LEADER");
        currentState = CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE;
        int x;
        while (true) {
            try {
                x = stdIn.nextInt();
                if (x < 1 || x > 3) {
                    System.err.println("Inserisci un numero da 1 a 3.");
                    System.out.println("\n1 - CARTA SVILUPPO\n2 - POTERE BASE\n3 - CARTA LEADER");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("Inserisci un numero.");
                System.out.println("\n1 - CARTA SVILUPPO\n2 - POTERE BASE\n3 - CARTA LEADER");
                stdIn.next();
            }
        }
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
    }

    private void slot_card_production() throws IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("Di quale carta vuoi attivare il potere di produzione? (1 - 3)");
        int x;
        while (true) {
            try {
                x = stdIn.nextInt();
                if (x < 1 || x > 3) {
                    System.err.println("Inserisci un numero da 1 a 3.");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("Inserisci un numero.");
                stdIn.next();
            }
        }
        int y = chose_warehouse_strongbox();
        Message message = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, position, x, y);
        System.out.println("\n" + message.toString());
        sendMessage(message);
    }

    private void basic_production() throws IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("Quale risorsa vuoi eliminare?");
        Resource r1 = chose_resource();
        System.out.println("Quale risorsa vuoi eliminare?");
        Resource r2 = chose_resource();
        System.out.println("Quale risorsa vuoi ricevere?");
        Resource r3 = chose_resource();
        int choice = chose_warehouse_strongbox();
        Message message = new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, position, r1, r2, r3, choice);
        sendMessage(message);
    }

    private void leader_card_production() throws IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        int x = chose_leader_card();
        System.out.println("Quale risorsa vuoi ricevere?");
        Resource r = chose_resource();
        int choice = chose_warehouse_strongbox();
        Message message = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, position, r, x, choice);
        sendMessage(message);
    }

    private void end_production() throws IOException {
        Message message = new Message(MessageType.END_PRODUCTION, position);
        currentState = CONTROLLER_STATES.END_TURN_STATE;
        sendMessage(message);
    }

    private void activate_leader_card() throws IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("ACTIVATE LEADER CARD");
        int x = chose_leader_card();
        Message message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_ACTIVATION, position, x);
        System.out.println(message.toString());
        sendMessage(message);
    }

    private void discard_leader_card() throws IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("DISCARD LEADER CARD");
        int x = chose_leader_card();
        Message message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, position, x);
        System.out.println(message.toString());
        sendMessage(message);
    }

    private void end_turn() throws IOException {
        Message end_turn = new Message(MessageType.END_TURN, position);
        currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
        System.out.println("Hai finito il tuo turno. Attendi che gli altri giocatori facciano il proprio.");
        sendMessage(end_turn);
    }

    private int chose_leader_card() {
        System.out.println("Le tue carte leader: " + leaderCard1 + ", " + leaderCard2);
        System.out.println("Quale carta leader scegli? (1 - 2)");
        stdIn = new Scanner(new InputStreamReader(System.in));
        int chosenLeaderCard;
        while (true) {
            try {
                chosenLeaderCard = stdIn.nextInt();
                if (chosenLeaderCard < 1 || chosenLeaderCard > 2) {
                    System.err.println("Inserisci un numero da 1 a 2.");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("Inserisci un numero.");
                stdIn.next();
            }
        }
        return chosenLeaderCard;
    }

    private void chose_first_resources() throws IOException {
        switch (position) {
            case 0:
                System.out.println("In attesa che tutti i giocatori facciano le proprie scelte...");
                break;
            case 1: {
                System.out.println("Sei il giocatore in posizione 2. Hai diritto a 1 risorsa.\nScegli la risorsa:");
                Resource r = chose_resource();
                Message message = new Message_One_Int_One_Resource(MessageType.ONE_FIRST_RESOURCE, position, r, 1);
                sendMessage(message);
            }
            break;
            case 2: {
                System.out.println("Sei il giocatore in posizione 2. Hai diritto a 1 risorsa e 1 punto fede.\nScegli la risorsa:");
                Resource r = chose_resource();
                Message message = new Message_One_Int_One_Resource(MessageType.ONE_FIRST_RESOURCE, position, r, 1);
                sendMessage(message);
            }
            break;
            case 3: {
                System.out.println("Sei il giocatore in posizione 4. Hai diritto a 2 risorse e 1 punto fede.\nScegli le risorse:");
                Resource r1 = chose_resource();
                Resource r2 = chose_resource();
                Message message = new Message_Two_Resource(MessageType.TWO_FIRST_RESOURCE, position, r1, r2);
                sendMessage(message);
            }
            break;
        }
        startGame = 2;
    }

    private Resource chose_resource() {
        Resource chosenResource = null;
        int x;
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\n1 - MONETA\n2 - SCUDO\n3 - ROCCIA\n4 - SERVO");
        while (true) {
            try {
                x = stdIn.nextInt();
                if (x < 1 || x > 4) {
                    System.err.println("Inserisci un numero da 1 a 4.");
                    System.out.println("\n1 - MONETA\n2 - SCUDO\n3 - ROCCIA\n4 - SERVO");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("Inserisci un numero.");
                System.out.println("\n1 - MONETA\n2 - SCUDO\n3 - ROCCIA\n4 - SERVO");
                stdIn.next();
            }
        }
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
        return chosenResource;
    }

    private int chose_warehouse_strongbox() {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("Da dove preferiresti prendere le risorse?\n0 - MAGAZZINO\n1 - FORZIERE");
        int choice;
        while (true) {
            try {
                choice = stdIn.nextInt();
                if (choice < 0 || choice > 1) {
                    System.err.println("Inserisci un numero da 0 a 1.");
                    System.out.println("\n0 - MAGAZZINO\n1 - FORZIERE");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("Inserisci un numero.");
                System.out.println("\n0 - MAGAZZINO\n1 - FORZIERE");
                stdIn.next();
            }
        }
        return choice;
    }

    /**
     * until user doesn't insert a valid input, ask him 2 int to chose two depots to switch. Then send it to Server and
     * waits an OK message return.
     *
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void switch_depot() throws IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        int x = choseDepot(1);
        int y = choseDepot(2);
        Message message = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, position, x, y);
        System.out.println(message.toString());
        sendMessage(message);
    }

    private int choseDepot(int depot) {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nScegli il " + depot + "° deposito:");
        int choice;
        while (true) {
            try {
                choice = stdIn.nextInt();
                if (choice < 1 || choice > 5) {
                    System.err.println("\nInserisci un numero da 1 a 5.\n");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\nScegli il " + depot + "° deposito:");
                stdIn.next();
            }
        }
        return choice;
    }

    /**
     * constantly wait for Server input and handle it by return a new message to Server or notifying other thread.
     */
    private void receiveMessage() throws IOException, InterruptedException {
        while (true) {
            try {
                Message returnMessage = (Message) in.readObject();
                wake_up();
                Thread thread = new Thread(() -> {
                    try {
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
                                start_game_message(returnMessage);
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
                            case PING:
                                ping_message();
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
                    } catch (InterruptedException | IOException e) {
                        System.err.println("\nClient no longer connected to the Server");
                        disconnect();
                        System.exit(0);
                    }
                });
                thread.start();
            } catch (ClassNotFoundException | ClassCastException e) {
                System.err.println("\nRicevuto messaggio inaspettato dal Server.");
            }
        }
    }

    private void wake_up() {
        synchronized (pingLock) {
            pingLock.notifyAll();
        }
    }

    private void login_message(Message message) throws IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        position = message.getClientID();
        if (position == 0) {
            final int[] userInt = new int[1];
            System.out.println("\nSei il primo giocatore. Quanti giocatori vuoi in partita? (1 - 4)");
            while (true) {
                try {
                    userInt[0] = stdIn.nextInt();
                    if (userInt[0] < 1 || userInt[0] > 4) {
                        System.err.println("\nInserisci un numero da 1 a 4.\n");
                        System.out.println("\nQuanti giocatori vuoi in partita? (1 - 4)");
                    } else break;
                } catch (InputMismatchException e) {
                    System.err.println("\nInserisci un numero.");
                    System.out.println("\nQuanti giocatori vuoi in partita? (1 - 4)");
                    stdIn.next();
                }
            }
            Message numPlayerMessage = new Message_One_Parameter_Int(MessageType.NUM_PLAYERS, position, userInt[0]);
            sendMessage(numPlayerMessage);
        } else if (startGame == 0) {
            System.out.println("\nSei entrato in una partita che sta per cominciare.");
            waiting();
        }
    }

    private void new_player_message(Message message) {
        Message_One_Parameter_String m = (Message_One_Parameter_String) message;
        if (m.getClientID() != (position))
            System.out.println("\nNuovo giocatore: " + m.getPar());
    }

    private void leader_card_choice(Message message) throws IOException, InterruptedException {
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        stdIn = new Scanner(new InputStreamReader(System.in));
        int leaderCard1 = m.getPar1();
        int leaderCard2 = m.getPar2();
        int leaderCard3 = m.getPar3();
        int leaderCard4 = m.getPar4();
        TimeUnit.SECONDS.sleep(2);
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
        this.leaderCard1 = firstChoice;
        this.leaderCard2 = secondChoice;
        Message returnMessage = new Message_Two_Parameter_Int(MessageType.LEADER_CARD, position, firstChoice, secondChoice);
        sendMessage(returnMessage);
    }

    private void start_game_message(Message message) throws IOException, InterruptedException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Tutti i giocatori hanno fatto le proprie scelte.\nLa partita è iniziata");
        currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
        sendMessage(new Message(MessageType.TURN, position));
    }

    private void players_message(Message message) throws InterruptedException {
        Message_ArrayList_String m = (Message_ArrayList_String) message;
        TimeUnit.SECONDS.sleep(1);
        System.out.println("Tutti i giocatori si sono collegati.\nGiocatori: " + m.getNickNames().toString());
        position = m.getClientID();
        players = m.getNickNames();
        System.out.println("\nSei il giocatore in posizione " + (position + 1));
        numPlayers = players.size();
        startGame = 1;
    }

    private void market_message(Message message) {
        Message_Market m = (Message_Market) message;
        System.out.println("\nRicevuto mercato");
    }

    private void deckBoard_message(Message message) {
        Message_ArrayList_Int m = (Message_ArrayList_Int) message;
        System.out.println("\nRicevute carte dei deck: " + m.getParams().toString());
    }

    private void ok_message() throws IOException, InterruptedException {
        switch (currentState) {
            case WAITING_PLAYERS_STATE:
                if (startGame == 2)
                    System.out.println("In attesa che tutti i giocatori facciano le proprie scelte...");
                else if (startGame == 1)
                    chose_first_resources();
                else
                    waiting();
                break;
            case FIRST_ACTION_STATE:
                chose_action(first_input());
                break;
            case TAKE_MARBLE_STATE:
                chose_marble(remainingMarbles);
                break;
            case ACTIVATE_PRODUCTION_STATE:
                activate_another_production();
                break;
            case END_TURN_STATE:
                lastInput();
                break;
        }
    }

    private void ping_message() throws IOException {
        Message ping = new Message(MessageType.PONG, position);
        sendMessage(ping);
    }

    private void turn_message(Message message) throws InterruptedException, IOException {
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        if (m.getPar() == 1) {
            chose_action(first_input());
        }
        else
            System.out.println("Attendi che gli altri giocatori facciano il proprio turno...");
    }

    private void end_turn_message(Message message) throws InterruptedException, IOException {
        TimeUnit.SECONDS.sleep(1);
        if (message.getClientID() != position) {
            System.out.println("\nIl giocatore in posizione " + (message.getClientID() +1) + " ha finito il suo turno.");
        }
        if (position == 0) {
            if (message.getClientID() == numPlayers - 1)
                chose_action(first_input());
        } else if (message.getClientID() == position - 1)
            chose_action(first_input());
    }

    private void buy_card_message(Message message) {
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + 1 + " ha comprato la carta: "
                + m.getPar1() + " e l'ha inseria nel " + m.getPar2() + "° slot.");
    }

    private void chosen_slot_message(Message message) throws IOException {
        Message_Three_Parameter_Int m = (Message_Three_Parameter_Int) message;
        stdIn = new Scanner(new InputStreamReader(System.in));
        int choice;
        if (m.getPar3() == -1) {
            System.out.println("\nScegli uno slot in cui inserire la carta: " + m.getPar1() + ", " + m.getPar2());
            while (true) {
                try {
                    choice = stdIn.nextInt();
                    if (choice != m.getPar1() && choice != m.getPar2()) {
                        System.err.println("\nInserisci un numero corretto.\n");
                    } else break;
                } catch (InputMismatchException e) {
                    System.err.println("\nInserisci un numero.");
                    stdIn.next();
                }
            }
        } else {
            System.out.println("\nScegli uno slot in cui inserire la carta: " + m.getPar1() + ", " + m.getPar2()
                    + ", " + m.getPar3());
            while (true) {
                try {
                    choice = stdIn.nextInt();
                    if (choice != m.getPar1() && choice != m.getPar2() && choice != m.getPar3()) {
                        System.err.println("\nInserisci un numero corretto.\n");
                    } else break;
                } catch (InputMismatchException e) {
                    System.err.println("\nInserisci un numero.");
                    stdIn.next();
                }
            }
        }
        Message returnMessage = new Message_One_Parameter_Int(MessageType.CHOSEN_SLOT, position, choice);
        sendMessage(returnMessage);
    }

    private void card_remove_message(Message message) {
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        if(numPlayers == 1 && m.getClientID() == 2)
            System.out.println("\nLudovico ha rimosso una carta nel mazzetto di riga: " + m.getPar1() + " e" +
                    " colonna: " + m.getPar2());

        else
            System.out.println("\nLa carta nel mazzetto di riga: " + m.getPar1() + " e colonna: "
                    + m.getPar2() + " è stata rimossa.");
        if (m.getPar3() == 1) {
            System.out.println("Il mazzetto ora è vuoto");
        } else
            System.out.println("La nuova carta del mazzetto è: " + m.getPar4());
    }

    private void resource_amount_message(Message message) {
        Message_One_Resource_Two_Int m = (Message_One_Resource_Two_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha " + m.getPar1() + " "
                + m.getResource() + " nel magazzino e " + m.getPar2() + " nel forziere");
    }

    private void take_marble_message(Message message) throws IOException, InterruptedException {
        Message_ArrayList_Marble m = (Message_ArrayList_Marble) message;
        chose_marble(m.getMarbles());
    }

    private void chose_marble(ArrayList<Marble> marbles) throws IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        Marble chosenMarble;
        if (marbles.size() == 1) {
            chosenMarble = marbles.remove(0);
            currentState = CONTROLLER_STATES.END_TURN_STATE;
            Message message = new Message_One_Parameter_Marble(MessageType.USE_MARBLE, position, chosenMarble);
            sendMessage(message);
            return;
        }
        System.out.println("Hai scelto queste biglie: " + marbles);
        remainingMarbles = marbles;
        System.out.println("\nVuoi scambiare i tuoi depositi?\n1 - SI\n0 - NO");
        try {
            int x = stdIn.nextInt();
            if (x == 1) {
                switch_depot();
                return;
            }
        } catch (InputMismatchException e){}
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nScegli una biglia:");
        while (true) {
            try {
                String choice = stdIn.nextLine();
                choice = choice.toUpperCase();
                chosenMarble = correct_marble(choice, marbles);
                if (chosenMarble != null)
                    break;
                else
                    System.err.println("\nInserisci un valore corretto.\n");
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un valore corretto.\n");
                stdIn.next();
            }
        }
        if (marbles.size() == 0)
            currentState = CONTROLLER_STATES.END_TURN_STATE;
        else
            remainingMarbles = marbles;
        Message message = new Message_One_Parameter_Marble(MessageType.USE_MARBLE, position, chosenMarble);
        sendMessage(message);
    }

    private Marble correct_marble(String input, ArrayList<Marble> marbles){
        for(int i = 0; i < marbles.size(); i++){
            if(input.equals(marbles.get(i).toString()))
                return marbles.remove(i);
        }
        return null;
    }

    private void market_change(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if(m.getPar1() == 0) {
            System.out.println("\nIl giocatore " + m.getClientID() + " ha scelto la riga " + m.getPar2() + " del mercato.");
        }
        else{
            System.out.println("\nIl giocatore " + m.getClientID() + " ha scelto la colonna " + m.getPar2() + " del mercato.");
        }
    }

    private void white_conversion_card_message(Message message) throws IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("\nHai selezionato una biglia bianca e hai due possibili conversioni");
        System.out.println("\nScegli una tra queste carte leader:\n " + m.getPar1() + ", " + m.getPar2());
        int choice;
        while (true) {
            try {
                choice = stdIn.nextInt();
                if (choice < 1 || choice > 2) {
                    System.err.println("\nInserisci un numero corretto.\n");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                stdIn.next();
            }
        }
        Message returnMessage;
        if (choice == 1) {
            returnMessage = new Message_One_Parameter_Int(MessageType.WHITE_CONVERSION_CARD, position, m.getPar1());
        } else {
            returnMessage = new Message_One_Parameter_Int(MessageType.WHITE_CONVERSION_CARD, position, m.getPar2());
        }
        sendMessage(returnMessage);
    }

    private void faith_points_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        if(numPlayers == 1 && m.getClientID() == 2)
            System.out.println("\nLudovico ha incrementato i suoi punti fede. Ora ne ha " + m.getPar());
        else
            System.out.println("\nIl giocatore " + m.getClientID() + " ha incrementato i suoi punti fede. Ora ne ha " + m.getPar());
    }

    private void increase_warehouse_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        if(m.getPar1() != -1)
            System.out.println("\nIl giocatore " + m.getClientID() + " ha incrementato di 1 " + m.getResource()
                + " il suo " + m.getPar1() + "° deposito.");
        else
            System.out.println("\nIl giocatore " + m.getClientID() + " ha scartato 1 " + m.getResource()
                    + " dal mercato.");
    }

    private void switch_depot_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha invertito il suo " + m.getPar1()
                + "° deposito con il suo " + m.getPar2() + "° deposito.");
    }

    private void vatican_report_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("Il giocatore " + m.getPar1() + " è finito su una casella del papa." +
                " Ora hai " + m.getPar2() + " punti vittoria dal tracciato fede");
    }

    private void leader_card_activation_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha attivato la carta leader: " + m.getPar());
    }

    private void leader_card_discard_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha scartato la carta leader: " + m.getPar());
    }

    private void quit_message(Message message){
        Message_One_Parameter_String m = (Message_One_Parameter_String) message;
        if(startGame == 2) {
            System.out.println("\nIl giocatore " + m.getPar() + " si è disconnesso. La partita è finita.");
            disconnect();
            System.exit(1);
        }
        else if(m.getPar() != null)
            System.out.println("\nIl giocatore " + m.getPar() + " si è disconnesso prima che iniziasse la partita.");
        else
            System.out.println("\nUn giocatore di è disconnesso prima che iniziasse la partita");
    }

    private void end_game_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("\nIl vincitore è il giocatore " + m.getClientID()
                + " che ha totalizzato " + m.getPar1() + " punti vittoria e " + m.getPar2()
                + " risorse totali.");
        System.out.println("\nDisconnessione.");
        disconnect();
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
        System.out.println("\nNickName già scelto");
        System.out.println("\nInserisci un nickName diverso:");
        stdIn = new Scanner(new InputStreamReader(System.in));
        String userInput = stdIn.nextLine();
        while (true) {
            if (userInput == null) {
                System.err.println("\nInserisci un nickName valido");
                userInput = stdIn.nextLine();
            } else if (userInput.equals(nickName)) {
                System.err.println("\nNon puoi inserire lo stesso nickName");
                userInput = stdIn.nextLine();
            } else break;
        }
        nickName = userInput;
        Message message = new Message_One_Parameter_String(MessageType.LOGIN, position, nickName);
        sendMessage(message);
    }

    private void wrong_parameters_error() throws InterruptedException, IOException {
        System.out.println("\nHai inserito dei parametri sbagliati");
        if(currentState == CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE)
            activate_another_production();
        else{
            currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
            chose_action(first_input());
        }
    }

    private void wrong_turn_error(){
        System.err.println("\nNon è il tuo turno. Non puoi compiere questa azione adesso");
    }

    private void empty_deck_error() throws IOException, InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nHai scelto un mazzetto vuoto");
        System.out.println("\nVuoi comprare una carta diversa?\n1 - SI\n0 - NO");
        try {
            int x = stdIn.nextInt();
            if (x == 1) {
                buy_card();
            } else {
                currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
                chose_action(first_input());
            }
        } catch (InputMismatchException e) {
            chose_action(first_input());
        }
    }

    private void empty_slot_error() throws IOException, InterruptedException {
        System.out.println("\nNon hai alcuna carta in questo slot");
        activate_another_production();
    }

    private void wrong_power_error() throws IOException, InterruptedException {
        System.out.println("\nNon puoi attivare questa potere di produzione");
        activate_another_production();
    }

    private void not_enough_cards_error() throws IOException, InterruptedException {
        System.out.println("\nNon hai abbastanza carte per attivare questa carta leader");
        if(currentState == CONTROLLER_STATES.FIRST_ACTION_STATE)
            chose_action(first_input());
        else{
            currentState = CONTROLLER_STATES.END_TURN_STATE;
            lastInput();
        }
    }

    private void full_slot_error() throws IOException, InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nNon puoi inserire questa carta in nessuno slot");
        System.out.println("\nVuoi comprare una carta diversa?\n1 - SI\n0 - NO");
        try {
            int x = stdIn.nextInt();
            if (x == 1) {
                buy_card();
            } else {
                chose_action(first_input());
                currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
            }
        } catch (InputMismatchException e) {
            chose_action(first_input());
        }
    }

    private void illegal_operation_error() throws IOException, InterruptedException {
        System.out.println("\nNon puoi eseguire questa azione in questo momento");
        if(currentState == CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE)
            activate_another_production();
        else{
            currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
            chose_action(first_input());
        }
    }

    private void impossible_switch_error() throws IOException, InterruptedException {
        System.out.println("\nNon puoi scambiare questa depositi");
        chose_marble(remainingMarbles);
    }

    private void not_enough_resource_error() throws IOException, InterruptedException {
        System.out.println("\nNon hai abbastanza risorse per effettuare questa operazione");
        if(currentState == CONTROLLER_STATES.ACTIVATE_PRODUCTION_STATE)
            activate_another_production();
        else if (currentState == CONTROLLER_STATES.END_TURN_STATE)
            end_turn();
        else{
            currentState = CONTROLLER_STATES.FIRST_ACTION_STATE;
            chose_action(first_input());
        }
    }

    private void already_active_error() throws IOException, InterruptedException {
        System.out.println("\nHai già attivato questa carta in precedenza");
        if(currentState == CONTROLLER_STATES.FIRST_ACTION_STATE)
            chose_action(first_input());
        else{
            currentState = CONTROLLER_STATES.END_TURN_STATE;
            lastInput();
        }
    }

    private void already_discard_error() throws InterruptedException, IOException {
        System.out.println("\nHai già scartato questa carta in precedenza");
        if(currentState == CONTROLLER_STATES.FIRST_ACTION_STATE)
            chose_action(first_input());
        else{
            currentState = CONTROLLER_STATES.END_TURN_STATE;
            lastInput();
        }
    }

    /**
     * send @param message to Server and wait until a OK message return by Server.
     * if thread is awaken before 10 seconds but not for a OK message return, send another @param message to Server.
     * if thread is awaken cause 10 seconds passed, send another @param message to Server.
     * after 30 seconds Server didn't send any, connection with Server is considered as lost.
     * @param message is the message to send to Server
     */
    private void sendMessage(Message message) throws IOException{
        out.flush();
        out.writeObject(message);
    }

    /**
     * @param initTime is the time in milliseconds from which start counting.
     * @param currentTime is the current time in milliseconds.
     * @param delta is the delta to compare @param currentTime and @param initTime.
     * @return if @param delta seconds have passed.
     */
    private boolean isTimePassed(long initTime, long currentTime, int delta){
        return ((currentTime - initTime) >= delta);
    }

    /**
     * close inputStream, outputStream and socket connection with Server.
     */
    private void disconnect() {
        connectedThread.interrupt();
        threadOut.interrupt();
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            System.exit(0);
        }
    }
}