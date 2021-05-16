package it.polimi.ingsw.network.client;

import it.polimi.ingsw.market.Marble;
import it.polimi.ingsw.resourceContainers.Resource;
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

    private Thread threadIn;
    private Thread threadOut;
    private Thread connectedThread;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private Scanner stdIn = new Scanner(new InputStreamReader(System.in));
    private String nickName;
    private int position;
    private boolean startGame;
    private boolean turn;
    private boolean quit;
    private boolean ok;
    private final Object lock1 = new Object();
    private final Object lock2 = new Object();

    public ClientSocket(Socket socket) throws IOException {
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.startGame = false;
        this.turn = true;
        this.quit = false;
        this.ok = false;
        this.position = 0;
    }

    /**
     * start threadIn and threadOut.
     * threadIn firstly login player and then constantly waits user inputs from System.in
     * threadOut constantly waits Server inputs.
     */
    public void start() {
        if (threadIn == null) {
            threadIn = new Thread(() -> {
                try {
                    login();
                    waiting();
                    while (!quit)
                        turnAction();
                }  catch (InterruptedException | IOException e) {
                    System.err.println("\nClient non più connesso al Server.");
                    disconnect();
                    System.exit(0);
                }
            });
            threadIn.start();
        }
        if(connectedThread == null){
            connectedThread = new Thread(() -> {
                try {
                    while (true){
                        if(startGame)
                            isMyTurn(MessageType.TURN);
                        else
                            isMyTurn(MessageType.PING);
                    }
                } catch (InterruptedException | IOException e) {
                    System.err.println("\nClient non più connesso al Server.");
                    disconnect();
                    System.exit(0);
                }
            });
            connectedThread.start();
        }
        try {
            receiveMessage();
        } catch (InterruptedException | IOException e) {
            System.err.println("\nClient non più connesso al Server.");
            disconnect();
            System.exit(0);
        }
    }

    /**
     * until player doesn't insert a valid nickName, constantly ask a new one. Then waits Server response to know which
     * number of player it is. If it's the first player, also send the number of players of new game to Server.
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void login() throws InterruptedException, IOException {
        String userInput;
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nInserisci il tuo nickName:");
        userInput = stdIn.nextLine();
        while (userInput.length() == 0) {
            System.err.println("\nInserisci un nickName valido:");
            userInput = stdIn.nextLine();
        }
        nickName = userInput;
        Message message = new Message_One_Parameter_String(MessageType.LOGIN, position, nickName);
        out.flush();
        out.writeObject(message);
        long initTime = System.currentTimeMillis();
        synchronized (lock1) {
            lock1.wait(30000);
            if (isTimePassed(initTime, System.currentTimeMillis(), 30000))
                throw new InterruptedException();
        }
        if (position == 0) {
            System.out.println("\nSei il primo giocatore. Quanti giocatori vuoi in partita? (1 - 4)");
            int userInt;
            while (true) {
                try {
                    userInt = stdIn.nextInt();
                    if (userInt < 1 || userInt > 4) {
                        System.err.println("\nInserisci un numero da 1 a 4.\n");
                        System.out.println("\nQuanti giocatori vuoi in partita? (1 - 4)");
                    } else break;
                } catch (InputMismatchException e) {
                    System.err.println("\nInserisci un numero.");
                    System.out.println("\nQuanti giocatori vuoi in partita? (1 - 4)");
                    stdIn.next();
                }
            }
            Message numPlayerMessage = new Message_One_Parameter_Int(MessageType.NUM_PLAYERS, position, userInt);
            out.flush();
            out.writeObject(numPlayerMessage);
            initTime = System.currentTimeMillis();
            synchronized (lock1) {
                lock1.wait(30000);
                if (isTimePassed(initTime, System.currentTimeMillis(), 30000))
                    throw new InterruptedException();
            }
        } else if (!startGame)
            System.out.println("\nSei entrato in una partita che sta per cominciare.\nSei il giocatore in posizione " + (position +1));
        turn = false;
        wake_up(lock2);
    }

    private void waiting() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        if (!startGame)
            System.out.println("\nIn attesa di altri giocatori...");
    }

    /**
     * ask user to insert 1 int to chose which action activate. Then send it to Server and waits an OK message return.
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void turnAction() throws InterruptedException, IOException {
        int firstChoice = first_input();
        chose_action(firstChoice);
        if(firstChoice == 4 || firstChoice == 5){
            int secondChoice = secondInput(firstChoice);
            chose_action(secondChoice);
            if(secondChoice == 4 || secondChoice == 5)
                chose_action(thirdInput());
        }
        lastInput();
    }

    private void chose_action(int choice) throws InterruptedException, IOException {
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
     * @return the action chosen by user.
     */
    private int first_input() throws InterruptedException {
        synchronized (lock1){
            while(!turn)
                lock1.wait();
        }
        System.out.println("\nE' il tuo turno");
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n3 - ACTIVATE PRODUCTION\n" +
                "4 - ACTIVATE LEADER CARD\n5 - DISCARD LEADER CARD");
        int userInput;
        while(true){
            try {
                userInput = stdIn.nextInt();
                if(userInput < 1 || userInput > 5){
                    System.err.println("\nInserisci un numero da 1 a 5.\n");
                    System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n" +
                            "3 - ACTIVATE PRODUCTION\n4 - ACTIVATE LEADER CARD\n5 - DISCARD LEADER CARD");
                }
                else break;
            }
            catch (InputMismatchException e){
                System.err.println("\nInserisci un numero.");
                System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n" +
                        "3 - ACTIVATE PRODUCTION\n4 - ACTIVATE LEADER CARD\n5 - DISCARD LEADER CARD");
                stdIn.next();
            }
        }
        return userInput;
    }

    private int secondInput(int first_choice){
        stdIn = new Scanner(new InputStreamReader(System.in));
        int userInput;
        if(first_choice == 4) {
            System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n3 - ACTIVATE PRODUCTION\n" +
                    "4 - DISCARD LEADER CARD");
            while (true) {
                try {
                    userInput = stdIn.nextInt();
                    if (userInput < 1 || userInput > 4) {
                        System.err.println("\nInserisci un numero da 1 a 4.\n");
                        System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n" +
                                "3 - ACTIVATE PRODUCTION\n4  - DISCARD LEADER CARD");
                    } else break;
                } catch (InputMismatchException e) {
                    System.err.println("\nInserisci un numero.");
                    System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n" +
                            "3 - ACTIVATE PRODUCTION\n4 -  DISCARD LEADER CARD");
                    stdIn.next();
                }
            }
            if(userInput == 4)
                userInput ++;
        }
        else {
            System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n3 - ACTIVATE PRODUCTION\n" +
                    "4 - ACTIVATE LEADER CARD");
            while (true) {
                try {
                    userInput = stdIn.nextInt();
                    if (userInput < 1 || userInput > 4) {
                        System.err.println("\nInserisci un numero da 1 a 4.\n");
                        System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n" +
                                "3 - ACTIVATE PRODUCTION\n4  - ACTIVATE LEADER CARD");
                    } else break;
                } catch (InputMismatchException e) {
                    System.err.println("\nInserisci un numero.");
                    System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n" +
                            "3 - ACTIVATE PRODUCTION\n4 -  ACTIVATE LEADER CARD");
                    stdIn.next();
                }
            }
        }
        return userInput;
    }

    private int thirdInput() {
        stdIn = new Scanner(new InputStreamReader(System.in));
        int userInput;
        System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n3 - ACTIVATE PRODUCTION");
        while (true) {
            try {
                userInput = stdIn.nextInt();
                if (userInput < 1 || userInput > 3) {
                    System.err.println("\nInserisci un numero da 1 a 3.\n");
                    System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n" +
                            "3 - ACTIVATE PRODUCTION");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\n1 - TAKE MARBLE FROM MARKET\n2 - BUY DEVELOPMENT CARD\n" +
                        "3 - ACTIVATE PRODUCTION");
                stdIn.next();
            }
        }
        return userInput;
    }

    private void lastInput() throws InterruptedException, IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        int userInput;
        System.out.println("\nVuoi fare altro?\n1 - ACTIVATE LEADER CARD\n2 - DISCARD LEADER CARD\n0 - END TURN");
        while (true) {
            try {
                userInput = stdIn.nextInt();
                if (userInput < 0 || userInput > 2) {
                    System.err.println("\nInserisci un numero da 0 a 2.\n");
                    System.out.println("\n1 - ACTIVATE LEADER CARD\n2 - DISCARD LEADER CARD\n0 - END TURN");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\n1 - ACTIVATE LEADER CARD\n2 - DISCARD LEADER CARD\n0 - END TURN");
                stdIn.next();
            }
        }
        switch (userInput){
            case 1: {
                activate_leader_card();
                System.out.println("\n1 - DISCARD LEADER CARD\n0 - END TURN");
                while (true) {
                    try {
                        userInput = stdIn.nextInt();
                        if (userInput < 0 || userInput > 1) {
                            System.err.println("\nInserisci un numero da 0 a 1.\n");
                            System.out.println("\n1 - DISCARD LEADER CARD\n0 - END TURN");
                        } else break;
                    } catch (InputMismatchException e) {
                        System.err.println("\nInserisci un numero.");
                        System.out.println("\n1 - DISCARD LEADER CARD\n0 - END TURN");
                        stdIn.next();
                    }
                }
                if (userInput == 1)
                    discard_leader_card();
                end_turn();
            }
            break;
            case 2: {
                discard_leader_card();
                System.out.println("\n1 - ACTIVATE LEADER CARD\n0 - END TURN");
                while (true) {
                    try {
                        userInput = stdIn.nextInt();
                        if (userInput < 0 || userInput > 1) {
                            System.err.println("\nInserisci un numero da 0 a 1.\n");
                            System.out.println("\n1 - ACTIVATE LEADER CARD\n0 - END TURN");
                        } else break;
                    } catch (InputMismatchException e) {
                        System.err.println("\nInserisci un numero.");
                        System.out.println("\n1 - ACTIVATE LEADER CARD\n0 - END TURN");
                        stdIn.next();
                    }
                }
                if (userInput == 1)
                    activate_leader_card();
                end_turn();
            }
            break;
            case 0:
                end_turn();
        }
    }

    private void take_market_marble() throws IOException, InterruptedException {
        if(!turn)
            System.err.println("\nNon è il tuo turno");
        else {
            stdIn = new Scanner(new InputStreamReader(System.in));
            System.out.println("\nTAKE MARBLE FROM MARKET\n");
            System.out.println("\nScegli una riga o una colonna?\n0 - RIGA\n1 - COLONNA");
            int x;
            while (true) {
                try {
                    x = stdIn.nextInt();
                    if (x < 0 || x > 1) {
                        System.err.println("\nInserisci un numero da 0 a 1.\n");
                        System.out.println("\n0 - RIGA\n1 - COLONNA");
                    } else break;
                } catch (InputMismatchException e) {
                    System.err.println("\nInserisci un numero.");
                    System.out.println("\n0 - RIGA\n1 - COLONNA");
                    stdIn.next();
                }
            }
            int y;
            if(x == 0){
                System.out.println("\nScegli la riga (0 - 2):");
                while (true) {
                    try {
                        y = stdIn.nextInt();
                        if (y < 0 || y > 2) {
                            System.err.println("\nInserisci un numero da 0 a 2.\n");
                            System.out.println("\nScegli la riga:");
                        } else break;
                    } catch (InputMismatchException e) {
                        System.err.println("\nInserisci un numero.");
                        System.out.println("\nScegli la riga:");
                        stdIn.next();
                    }
                }
            }
            else {
                System.out.println("\nScegli la colonna (0 - 3):");
                while (true) {
                    try {
                        y = stdIn.nextInt();
                        if (y < 0 || y > 3) {
                            System.err.println("\nInserisci un numero da 0 a 3.\n");
                            System.out.println("\nScegli la colonna:");
                        } else break;
                    } catch (InputMismatchException e) {
                        System.err.println("\nInserisci un numero.");
                        System.out.println("\nScegli la colonna:");
                        stdIn.next();
                    }
                }
            }
            Message message = new Message_Two_Parameter_Int(MessageType.TAKE_MARBLE, position, x, y);
            sendMessage(message);
        }
    }

    /**
     * until user doesn't insert a valid input, ask him 1 int to chose the deck to where buy card. Then send it to Server
     * and waits an OK message return.
     */
    private void buy_card() throws IOException, InterruptedException {
        if(!turn)
            System.err.println("\nNon è il tuo turno");
        else {
            stdIn = new Scanner(new InputStreamReader(System.in));
            System.out.println("\nBUY_DEVELOPMENT_CARD\n");
            System.out.println("\nScegli una riga (0 - 2):");
            int x;
            while (true) {
                try {
                    x = stdIn.nextInt();
                    if (x < 0 || x > 2) {
                        System.err.println("\nInserisci un numero da 0 a 2.\n");
                        System.out.println("\nScegli una riga:");
                    } else break;
                } catch (InputMismatchException e) {
                    System.err.println("\nInserisci un numero.");
                    System.out.println("\nScegli una riga:");
                    stdIn.next();
                }
            }
            System.out.println("\nScegli una colonna (0 - 3):");
            int y;
            while (true) {
                try {
                    y = stdIn.nextInt();
                    if (y < 0 || y > 3) {
                        System.err.println("\nInserisci un numero da 0 a 3.\n");
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
            sendMessage(message);
        }
    }

    private void activate_production() throws IOException, InterruptedException {
        if(!turn)
            System.err.println("\nNon è il tuo turno");
        else {
            stdIn = new Scanner(new InputStreamReader(System.in));
            System.out.println("\nACTIVATE PRODUCTION\n");
            while(true) {
                System.out.println("\nQuale produzione vuoi attivare?\n1 - CARTA SVILUPPO\n2 - POTERE BASE\n3 - CARTA LEADER");
                int x;
                while (true) {
                    try {
                        x = stdIn.nextInt();
                        if (x < 1 || x > 3) {
                            System.err.println("\nInserisci un numero da 1 a 3.\n");
                            System.out.println("\n1 - CARTA SVILUPPO\n2 - POTERE BASE\n3 - CARTA LEADER");
                        } else break;
                    } catch (InputMismatchException e) {
                        System.err.println("\nInserisci un numero.");
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
                System.out.println("\nVuoi attivare un'altra produzione?\n1 - SI\n0 - NO");
                try{
                    int y = stdIn.nextInt();
                    if(y != 1) {
                        end_production();
                        break;
                    }
                } catch (InputMismatchException e) {
                    stdIn.next();
                    end_production();
                    break;
                }
            }
        }
    }

    private void slot_card_production() throws IOException, InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nDi quale carta vuoi attivare il potere di produzione? (0 - 2)");
        int x;
        while (true) {
            try {
                x = stdIn.nextInt();
                if (x < 0 || x > 2) {
                    System.err.println("\nInserisci un numero da 0 a 2.\n");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                stdIn.next();
            }
        }
        int y = chose_warehouse_strongbox();
        Message message = new Message_Two_Parameter_Int(MessageType.DEVELOPMENT_CARD_POWER, position, x, y);
        System.out.println("\n" + message.toString());
        sendMessage(message);
    }

    private void basic_production() throws IOException, InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nQuale risorsa vuoi eliminare?\n1 - MONETA\n2 - SCUDO\n" +
                "3 - ROCCIA\n4 - SERVO");
        Resource r1 = chose_resource();
        System.out.println("\nQuale risorsa vuoi eliminare?\n1 - MONETA\n2 - SCUDO\n" +
                "3 - ROCCIA\n4 - SERVO");
        Resource r2 = chose_resource();
        System.out.println("\nQuale risorsa vuoi ricevere?\n1 - MONETA\n2 - SCUDO\n" +
                "3 - ROCCIA\n4 - SERVO");
        Resource r3 = chose_resource();
        int choice = chose_warehouse_strongbox();
        Message message = new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, position, r1, r2, r3, choice);
        System.out.println("\n" + message.toString());
        sendMessage(message);
    }

    private void leader_card_production() throws IOException, InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        int x = chose_leader_card();
        System.out.println("\nQuale risorsa vuoi ricevere?\n1 - MONETA\n2 - SCUDO\n" +
                "3 - ROCCIA\n4 - SERVO");
        Resource r = chose_resource();
        int choice = chose_warehouse_strongbox();
        Message message = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, position, r, x, choice);
        System.out.println("\n" + message.toString());
        sendMessage(message);
    }

    private void end_production() throws IOException, InterruptedException {
        Message message = new Message(MessageType.END_PRODUCTION, position);
        sendMessage(message);
    }

    private void activate_leader_card() throws IOException, InterruptedException {
        if (!turn)
            System.err.println("\nNon è il tuo turno");
        else {
            stdIn = new Scanner(new InputStreamReader(System.in));
            System.out.println("\nACTIVATE LEADER CARD\n");
            int x = chose_leader_card();
            Message message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_ACTIVATION, position, x);
            System.out.println("\n" + message.toString());
            sendMessage(message);
            System.out.println("\nVuoi attivare un'altra carta leader?\n1 - SI\n0 - NO");
            try {
                int y = stdIn.nextInt();
                if (y == 1) {
                    if(x == 1)
                        y = 0;
                    message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_ACTIVATION, position, y);
                    System.out.println("\n" + message.toString());
                    sendMessage(message);
                }
            } catch (InputMismatchException e) {
                stdIn.next();
            }
        }
    }

    private void discard_leader_card() throws IOException, InterruptedException {
        if (!turn)
            System.err.println("\nNon è il tuo turno");
        else {
            stdIn = new Scanner(new InputStreamReader(System.in));
            System.out.println("\nDISCARD LEADER CARD\n");
            int x = chose_leader_card();
            Message message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, position, x);
            System.out.println("\n" + message.toString());
            sendMessage(message);
            System.out.println("\nVuoi scartare un'altra carta leader?\n1 - SI\n0 - NO");

            try {
                int y = stdIn.nextInt();
                if (y == 1) {
                    if(x == 1)
                        y = 0;
                    message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, position, y);
                    System.out.println("\n" + message.toString());
                    sendMessage(message);
                }
            } catch (InputMismatchException e) {
                stdIn.next();
            }
        }
    }

    private void end_turn() throws IOException, InterruptedException {
        Message end_turn = new Message(MessageType.END_TURN, position);
        sendMessage(end_turn);
        turn = false;
        wake_up(lock1);
    }

    private int chose_leader_card() {
        System.out.println("\nQuale carta leader scegli? (0 - 1)");
        int x;
        while (true) {
            try {
                x = stdIn.nextInt();
                if (x < 0 || x > 1) {
                    System.err.println("\nInserisci un numero da 0 a 1.\n");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                stdIn.next();
            }
        }
        return x;
    }


    private Resource chose_resource(){
        int x;
        while (true) {
            try {
                x = stdIn.nextInt();
                if (x < 1 || x > 4) {
                    System.err.println("\nInserisci un numero da 1 a 4.\n");
                    System.out.println("\n1 - MONETA\n2 - SCUDO\n3 - ROCCIA\n4 - SERVO");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\n1 - MONETA\n2 - SCUDO\n3 - ROCCIA\n4 - SERVO");
                stdIn.next();
            }
        }
        switch (x){
            case 1:
                return Resource.COIN;
            case 2:
                return Resource.SHIELD;
            case 3:
                return Resource.STONE;
            case 4:
                return Resource.SERVANT;
            default:
                return null;
        }
    }

    private int chose_warehouse_strongbox() {
        System.out.println("\nDa dove preferiresti prendere le risorse?\n0 - MAGAZZINO\n1 - FORZIERE");
        int choice;
        while (true) {
            try {
                choice = stdIn.nextInt();
                if (choice < 0 || choice > 1) {
                    System.err.println("\nInserisci un numero da 0 a 1.\n");
                    System.out.println("\n0 - MAGAZZINO\n1 - FORZIERE");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\n0 - MAGAZZINO\n1 - FORZIERE");
                stdIn.next();
            }
        }
        return choice;
    }

    /**
     * send to Server a TURN message and wait until a message return by Server.
     * if thread is awaken cause 10 seconds passed, send another TURN request to Server.
     * if after 30 seconds Server didn't return any message, connection with Server is considered as lost.
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void isMyTurn(MessageType type) throws IOException, InterruptedException {
        long initTime = System.currentTimeMillis();
        long interTime = initTime;
        Message message = new Message(type, position);
        out.flush();
        out.writeObject(message);
        while (true) {
            synchronized (lock2) {
                lock2.wait(10000);
                if (isTimePassed(initTime, System.currentTimeMillis(), 30000)) {
                    throw new IOException();
                } else if (!(isTimePassed(interTime, System.currentTimeMillis(), 10000))) {
                    TimeUnit.SECONDS.sleep(10);
                    break;
                } else {
                    interTime = System.currentTimeMillis();
                    out.flush();
                    out.writeObject(message);
                }
            }
        }
    }

    /**
     * constantly wait for Server input and handle it by return a new message to Server or notifying other thread.
     */
    private void receiveMessage() throws IOException, InterruptedException {
        while(true) {
            try {
                Message returnMessage = (Message) in.readObject();
                switch (returnMessage.getMessageType()) {
                    case LOGIN:
                        login_message(returnMessage);
                        break;
                    case NEW_PLAYER:
                        new_player_message(returnMessage);
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
                    case OK:
                        ok_message();
                        break;
                    case PING:
                        ping_message();
                        break;
                    case PONG:
                        wake_up(lock2);
                        break;
                    case TURN:
                        turn_message(returnMessage);
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
                        wake_up(lock1);
                        break;
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.err.println("\nRicevuto messaggio inaspettato dal Server.");
            }
        }
    }

    private void wake_up(Object lock) {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private void login_message(Message message){
        position = message.getClientID();
        ok = true;
        wake_up(lock1);
    }

    private void new_player_message(Message message){
        Message_One_Parameter_String m = (Message_One_Parameter_String) message;
        if(m.getClientID() != position)
            System.out.println("\nNuovo giocatore: " + m.getPar() + " in posizione " + (m.getClientID()+1));
    }

    private void leader_card_choice(Message message) throws IOException, InterruptedException {
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        stdIn = new Scanner(new InputStreamReader(System.in));
        int leaderCard1 = m.getPar1();
        int leaderCard2 = m.getPar2();
        int leaderCard3 = m.getPar3();
        int leaderCard4 = m.getPar4();
        System.out.println("\nScegli due tra queste 4 carte leader: " +
                leaderCard1 + ", " + leaderCard2 + ", " + leaderCard3 + ", " + leaderCard4);
        int firstChoice;
        int secondChoice;
        while(true) {
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
        while(true) {
            try {
                secondChoice = stdIn.nextInt();
                if(secondChoice == firstChoice){
                    System.err.println("\nNon puoi scegliere due carte uguali\n");
                }
                else if (secondChoice != leaderCard1 && secondChoice != leaderCard2 &&
                        secondChoice != leaderCard3 && secondChoice != leaderCard4) {
                    System.err.println("\nInserisci un numero corretto.\n");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                stdIn.next();
            }
        }
        Message returnMessage = new Message_Two_Parameter_Int(MessageType.LEADER_CARD, position, firstChoice, secondChoice);
        sendMessage(returnMessage);
    }

    private void start_game_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        System.out.println("\nLa partita è iniziata. N° giocatori: " + m.getPar());
        turn = false;
        startGame = true;
        wake_up(lock1);
    }

    private void market_message(Message message){
        Message_Market m = (Message_Market) message;
        System.out.println("\nRicevuto mercato");
        ok = true;
        wake_up(lock1);
    }

    private void ok_message(){
        ok = true;
        wake_up(lock1);
    }

    private void ping_message() throws IOException {
        Message ping = new Message(MessageType.PONG, position);
        out.flush();
        out.writeObject(ping);
        wake_up(lock2);
    }

    private void turn_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        if (m.getPar() == 1) turn = true;
        else turn = false;
        wake_up(lock1);
    }

    private void buy_card_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha comprato la carta: "
                + m.getPar1() + " e l'ha inseria nel " + m.getPar2() +"° slot.");
    }

    private void chosen_slot_message(Message message) throws IOException, InterruptedException {
        Message_Three_Parameter_Int m = (Message_Three_Parameter_Int) message;
        stdIn = new Scanner(new InputStreamReader(System.in));
        int choice;
        if(m.getPar3() == -1){
            System.out.println("\nScegli uno slot in cui inserire la carta: " + m.getPar1() + ", " + m.getPar2());
            while(true) {
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
        }
        else{
            System.out.println("\nScegli uno slot in cui inserire la carta: " + m.getPar1() + ", " + m.getPar2()
            + ", " + m.getPar3());
            while(true) {
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

    private void card_remove_message(Message message){
        Message_Four_Parameter_Int m = (Message_Four_Parameter_Int) message;
        System.out.println("\nLa carta nel mazzetto di riga: " + m.getPar1() + " e colonna: "
        + m.getPar2() + " è stata rimossa.");
        if(m.getPar3() == 1){
            System.out.println("Il mazzetto ora è vuoto");
        }
        else
            System.out.println("La nuova carta del mazzetto è: " + m.getPar4());
    }

    private void resource_amount_message(Message message){
        Message_One_Resource_Two_Int m = (Message_One_Resource_Two_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha " + m.getPar1() + " "
                + m.getResource() + " nel magazzino e " + m.getPar2() + " nel forziere");
    }

    private void take_marble_message(Message message) throws IOException, InterruptedException {
        Message_ArrayList_Marble m = (Message_ArrayList_Marble) message;
        chose_marble(m.getMarbles());
    }

    /**
     * until user doesn't insert a valid input, ask him 2 int to chose two depots to switch. Then send it to Server and
     * waits an OK message return.
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void switch_depot() throws IOException, InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nVuoi scambiare i tuoi depositi?\n1 - SI\n0 - NO");
        while (true){
            try {
                int choice = stdIn.nextInt();
                if(choice == 0)
                    break;
                int x = choseDepot(1);
                int y = choseDepot(2);
                Message message = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, position, x, y);
                System.out.println(message.toString());
                sendMessage(message);
                System.out.println("\nVuoi fare un altro scambio?\n1 - SI\n0 - NO");
            }catch (InputMismatchException e) {
                stdIn.next();
                break;
            }
        }

    }

    private int choseDepot(int depot){
        System.out.println("\nScegli il " + depot +"° deposito:");
        while(true) {
            try {
                return stdIn.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\nScegli il " + depot +"° deposito:");
                stdIn.next();
            }
        }
    }

    private void chose_marble(ArrayList<Marble> marbles) throws IOException, InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nScegli una tra queste biglie:\n" + marbles.get(0).toString());
        for(int i = 1; i < marbles.size(); i++)
            System.out.println(marbles.get(i).toString());
        int chosenMarble;
        while(true) {
            try {
                String choice = stdIn.nextLine();
                chosenMarble = correct_marble(choice, marbles);
                if(chosenMarble != -1)
                    break;
                else
                    System.err.println("\nInserisci un valore corretto.\n");
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un valore corretto.\n");
                stdIn.next();
            }
        }
        Message message = new Message_One_Parameter_Int(MessageType.USE_MARBLE, position,  chosenMarble);
        sendMessage(message);
    }

    private int correct_marble(String input, ArrayList<Marble> marbles){
        for(int i = 0; i < marbles.size(); i++){
            if(input.equals(marbles.get(i).toString()))
                return i;
        }
        return -1;
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

    private void white_conversion_card_message(Message message) throws IOException, InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("\nHai selezionato una biglia bianca e hai due possibili conversioni");
        System.out.println("\nScegli una tra queste carte leader:\n " + m.getPar1() + ", " + m.getPar2());
        int choice;
        while(true) {
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
        if(choice == 1){
            returnMessage = new Message_One_Parameter_Int(MessageType.WHITE_CONVERSION_CARD, position, m.getPar1());
        }
        else{
            returnMessage = new Message_One_Parameter_Int(MessageType.WHITE_CONVERSION_CARD, position, m.getPar2());
        }
        sendMessage(returnMessage);
    }

    private void faith_points_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha incrementato di " + m.getPar()
        + " i suoi punti fede.");
    }

    private void increase_warehouse_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha incrementato di 1 " + m.getResource()
                + " il suo " + m.getPar1() + "° deposito.");
    }

    private void switch_depot_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha invertito il suo " + m.getPar1()
                + "° deposito con il suo " + m.getPar1() + "° deposito.");
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
        if(startGame) {
            System.out.println("\nIl giocatore " + m.getPar() + " si è disconnesso. La partita è finita.");
            quit = true;
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

    private void error_message(Message message) throws  IOException {
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
        System.err.println("\nNickName già scelto");
        System.out.println("\nInserisci un nickName diverso:");
        String userInput = stdIn.nextLine();
        while (true) {
            if (userInput.length() == 0) {
                System.err.println("\nInserisci un nickName valido:");
                userInput = stdIn.nextLine();
            }
            else if(userInput.equals(nickName)){
                System.err.println("\nNon puoi inserire lo stesso nickName");
                userInput = stdIn.nextLine();
            }
            else break;
        }
        nickName = userInput;
        Message message = new Message_One_Parameter_String(MessageType.LOGIN, position, nickName);
        out.flush();
        out.writeObject(message);
        while (true){
            try {
                Message m = (Message) in.readObject();
                if(m.getMessageType() == MessageType.LOGIN){
                    login_message(m);
                    break;
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.err.println("\nRicevuto messaggio inaspettato dal Client.");
            }
        }
    }

    private void wrong_parameters_error(){
        System.err.println("\nHai inserito dei parametri sbagliati");
    }

    private void wrong_turn_error(){
        System.err.println("\nNon è il tuo turno. Non puoi compiere questa azione adesso");
    }

    private void empty_deck_error(){
        System.err.println("\nHai scelto un mazzetto vuoto");
    }

    private void empty_slot_error(){
        System.err.println("\nNon hai alcuna carta in questo slot");
    }

    private void wrong_power_error(){
        System.err.println("\nNon puoi attivare questa potere di produzione");
    }

    private void not_enough_cards_error(){
        System.err.println("\nNon hai abbastanza carte per attivare questa carta leader");
    }

    private void full_slot_error(){
        System.err.println("\nNon puoi inserire questa carta in nessuno slot");
    }

    private void illegal_operation_error(){
        System.err.println("\nNon puoi eseguire questa azione in questo momento");
    }

    private void impossible_switch_error(){
        System.err.println("\nNon puoi scambiare questa depositi");
    }

    private void not_enough_resource_error(){
        System.err.println("\nNon hai abbastanza risorse per effettuare questa operazione");
    }

    private void already_active_error(){
        System.err.println("\nHai già attivato questa carta in precedenza");
    }

    private void already_discard_error(){
        System.err.println("\nHai già scartato questa carta in precedenza");
    }

    /**
     * send @param message to Server and wait until a OK message return by Server.
     * if thread is awaken before 10 seconds but not for a OK message return, send another @param message to Server.
     * if thread is awaken cause 10 seconds passed, send another @param message to Server.
     * after 30 seconds Server didn't send any, connection with Server is considered as lost.
     * @param message is the message to send to Server
     * @throws InterruptedException if thread is interrupted during the waiting.
     */
    private void sendMessage(Message message) throws IOException, InterruptedException {
        ok = false;
        out.flush();
        out.writeObject(message);
        while (!ok) {
            synchronized (lock1) {
                lock1.wait();
            }
        }
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
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            System.exit(0);
        }
    }
}