package it.polimi.ingsw;

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
public class ClientSocket{

    private Thread threadIn;
    private Thread threadOut;
    private final Timer turnTimer = new Timer();
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    private Scanner stdIn = new Scanner(new InputStreamReader(System.in));
    private int position;
    private boolean quit;
    private boolean ok;
    private boolean turn;
    private boolean startGame;
    private final Object lock = new Object();

    public ClientSocket(Socket socket) throws IOException {
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.quit = false;
        this.ok = false;
        this.turn = false;
        this.startGame = false;
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
                    disconnect();
                    System.out.println("\nDisconnessione.");
                    threadOut.interrupt();
                    System.exit(1);
                } catch (InterruptedException e) {
                    System.err.println("\nClient non più connesso al Server.");
                    disconnect();
                    System.exit(0);
                }
            });
            threadIn.start();
        }
        if (threadOut == null) {
            threadOut = new Thread(() -> receiveMessage());
            threadOut.start();
        }
        turnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    isMyTurn();
                } catch (InterruptedException e) {
                    System.err.println("\nClient non più connesso al Server.");disconnect();
                    System.exit(0);
                }
            }
        }, 0, 10000);
    }

    /**
     * until player doesn't insert a valid nickName, constantly ask a new one. Then waits Server response to know which
     * number of player it is. If it's the first player, also send the number of players of new game to Server.
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void login() throws InterruptedException {
        String userInput;
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nInserisci il tuo nickName:");
        userInput = stdIn.nextLine();
        while (userInput.length() == 0) {
            System.out.println("\nInserisci un nickName valido:");
            userInput = stdIn.nextLine();
        }
        Message message = new Message_One_Parameter_String(MessageType.LOGIN, position,  userInput);
        sendMessage(message);
        if(position == 1){
            System.out.println("\nSei il primo giocatore. Quanti giocatori vuoi in partita? (1 - 4)");
            int userInt;
            while(true){
                try {
                    userInt = stdIn.nextInt();
                    if(userInt < 1 || userInt > 4){
                        System.err.println("\nInserisci un numero da 1 a 4.\n");
                        System.out.println("\nQuanti giocatori vuoi in partita? (1 - 4)");
                    }
                    else break;
                }
                catch (InputMismatchException e){
                    System.err.println("\nInserisci un numero.");
                    System.out.println("\nQuanti giocatori vuoi in partita? (1 - 4)");
                    stdIn.next();
                }
            }
            Message numPlayerMessage = new Message_One_Parameter_Int(MessageType.NUM_PLAYERS, position, userInt);
            sendMessage(numPlayerMessage);
        }
        else if(!startGame)
            System.out.println("\nSei entrato in una partita che sta per cominciare.\nSei il giocatore in posizione " + position);
    }

    private void waiting(){
        try {
            TimeUnit.SECONDS.sleep(1);
            if (!startGame)
                System.out.println("\nIn attesa di altri giocatori...");
        } catch (InterruptedException e) {
        }
    }

    /**
     * ask user to insert 1 int to chose which action activate. Then send it to Server and waits an OK message return.
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void turnAction() throws InterruptedException {
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

    private void chose_action(int choice) throws InterruptedException {
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
        synchronized (lock){
            while(!turn)
                lock.wait();
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

    private void lastInput() throws InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        int userInput;
        System.out.println("\n1 - ACTIVATE LEADER CARD\n2 - DISCARD LEADER CARD\n0 - END TURN");
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
                System.out.println("\n1  - DISCARD LEADER CARD\n0 - END TURN");
                while (true) {
                    try {
                        userInput = stdIn.nextInt();
                        if (userInput < 0 || userInput > 1) {
                            System.err.println("\nInserisci un numero da 0 a 1.\n");
                            System.out.println("\n1  - DISCARD LEADER CARD\n0 - END TURN");
                        } else break;
                    } catch (InputMismatchException e) {
                        System.err.println("\nInserisci un numero.");
                        System.out.println("\n1  - DISCARD LEADER CARD\n0 - END TURN");
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
                System.out.println("\n1  - ACTIVATE LEADER CARD\n0 - END TURN");
                while (true) {
                    try {
                        userInput = stdIn.nextInt();
                        if (userInput < 0 || userInput > 1) {
                            System.err.println("\nInserisci un numero da 0 a 1.\n");
                            System.out.println("\n1  - ACTIVATE LEADER CARD\n0 - END TURN");
                        } else break;
                    } catch (InputMismatchException e) {
                        System.err.println("\nInserisci un numero.");
                        System.out.println("\n1  - ACTIVATE LEADER CARD\n0 - END TURN");
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

    private void take_market_marble() throws InterruptedException {
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
            System.out.println("\n" + message.toString());
            sendMessage(message);
        }
    }

    /**
     * until user doesn't insert a valid input, ask him 1 int to chose the deck to where buy card. Then send it to Server
     * and waits an OK message return.
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void buy_card() throws InterruptedException {
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

    private void activate_production() throws InterruptedException {
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

    private void slot_card_production() throws InterruptedException {
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

    private void basic_production() throws InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nQuale risorsa vuoi eliminare??\n1 - MONETA\n2 - SCUDO\n" +
                "\n3 - ROCCIA\n4 - SERVO");
        Resource r1 = chose_resource();
        System.out.println("\nQuale risorsa vuoi eliminare??\n1 - MONETA\n2 - SCUDO\n" +
                "\n3 - ROCCIA\n4 - SERVO");
        Resource r2 = chose_resource();
        System.out.println("\nQuale risorsa vuoi ricevere?\n1 - MONETA\n2 - SCUDO\n" +
                "\n3 - ROCCIA\n4 - SERVO");
        Resource r3 = chose_resource();
        int choice = chose_warehouse_strongbox();
        Message message = new Message_Three_Resource_One_Int(MessageType.BASIC_POWER, position, r1, r2, r3, choice);
        System.out.println("\n" + message.toString());
        sendMessage(message);
    }

    private void leader_card_production() throws InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        int x = chose_leader_card();
        System.out.println("\nQuale risorsa vuoi ricevere?\n1 - MONETA\n2 - SCUDO\n" +
                "\n3 - ROCCIA\n4 - SERVO");
        Resource r = chose_resource();
        int choice = chose_warehouse_strongbox();
        Message message = new Message_One_Resource_Two_Int(MessageType.LEADER_CARD_POWER, position, r, x, choice);
        System.out.println("\n" + message.toString());
        sendMessage(message);
    }

    private void end_production() throws InterruptedException {
        Message message = new Message(MessageType.END_PRODUCTION, position);
        sendMessage(message);
    }

    private void activate_leader_card() throws InterruptedException {
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
                    y = chose_leader_card();
                    message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_ACTIVATION, position, y);
                    System.out.println("\n" + message.toString());
                    sendMessage(message);
                }
            } catch (InputMismatchException e) {
                stdIn.next();
            }
        }
    }

    private void discard_leader_card() throws InterruptedException {
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
                    y = chose_leader_card();
                    message = new Message_One_Parameter_Int(MessageType.LEADER_CARD_DISCARD, position, y);
                    System.out.println("\n" + message.toString());
                    sendMessage(message);
                }
            } catch (InputMismatchException e) {
                stdIn.next();
            }
        }
    }

    private void end_turn() throws InterruptedException {
        Message end_turn = new Message(MessageType.END_TURN, position);
        sendMessage(end_turn);
        turn = false;
        wake_up();
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
     * until user doesn't insert a valid input, ask him 2 int to chose two depots to switch. Then send it to Server and
     * waits an OK message return.
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void switch_depot() throws InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        int x = choseDepot(1);
        int y = choseDepot(2);
        Message message = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, position, x, y);
        System.out.println("\n" + message.toString());
        if (turn) {
            sendMessage(message);
            turn = false;
            synchronized (lock) {
                lock.notifyAll();
            }
        }
        else
            System.err.println("\nNon è il tuo turno.");
    }

    private int choseDepot(int depot){
        System.out.println("\nScegli il " + depot +"° deposito:");
        while(true) {
            try {
                return stdIn.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\nScegli il primo deposito:");
                stdIn.next();
            }
        }
    }

    /**
     * send to Server a TURN message and wait until a message return by Server.
     * if thread is awaken cause 10 seconds passed, send another TURN request to Server.
     * if after 30 seconds Server didn't return any message, connection with Server is considered as lost.
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private  void isMyTurn() throws InterruptedException {
        synchronized (lock) {
            while (turn || !startGame) {
                lock.wait();
            }
        }
        try {
            long initTime = System.currentTimeMillis();
            Message message = new Message(MessageType.TURN, position);
            out.flush();
            out.writeObject(message);
            while(true) {
                synchronized (lock) {
                    lock.wait(10000);
                    if (isTimePassed(initTime, System.currentTimeMillis(), 30000))
                        throw new InterruptedException();
                    else if (!(isTimePassed(initTime, System.currentTimeMillis(), 10000)))
                        break;
                    else{
                        out.flush();
                        out.writeObject(message);
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("\nClient non più connesso al Server.");
            threadOut.interrupt();
            disconnect();
            System.exit(0);
        }
    }

    /**
     * constantly wait for Server input and handle it by return a new message to Server or notifying other thread.
     */
    private void receiveMessage(){
        try {
            while(true) {
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
                    case DECKBOARD:
                        deckBoard_message(returnMessage);
                        break;
                    case OK:
                        ok_message();
                        break;
                    case PING:
                        ping_message();
                        break;
                    case TURN:
                        turn_message(returnMessage);
                        break;
                    case PLAYERBOARD:
                        playerBoard_message(returnMessage);
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
                        System.err.println("\nRicevuto messaggio inaspettato dal Server");
                        wake_up();
                        break;
                }
            }
        } catch (IOException e) {
        } catch (ClassNotFoundException | ClassCastException e) {
            System.err.println("\nRicevuto messaggio inaspettato dal Server");
            wake_up();
        }
    }

    private void wake_up() {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    private void login_message(Message message){
        position = message.getClientID();
        ok = true;
        wake_up();
    }

    private void new_player_message(Message message){
        Message_One_Parameter_String m = (Message_One_Parameter_String) message;
        System.out.println("\nNuovo giocatore: " + m.getPar() + " in posizione " + m.getClientID());
        wake_up();
    }

    private void leader_card_choice(Message message) throws IOException {
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
        out.flush();
        out.writeObject(returnMessage);
        wake_up();
    }

    private void start_game_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        System.out.println("\nLa partita è iniziata. N° giocatori: " + m.getPar());
        turn = false;
        startGame = true;
        wake_up();
    }

    private void market_message(Message message){
        Message_Market m = (Message_Market) message;
        System.out.println("\nRicevuto mercato");
        wake_up();
    }

    private void deckBoard_message(Message message){
        Message_ArrayList_Int m = (Message_ArrayList_Int) message;
        System.out.println("\nRicevuta disposizione delle carte sviluppo");
        wake_up();
    }

    private void ok_message(){
        ok = true;
        wake_up();
    }

    private void ping_message() throws IOException {
        Message ping = new Message(MessageType.PING, position);
        out.flush();
        out.writeObject(ping);
        wake_up();
    }

    private void turn_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        if (m.getPar() == 1) turn = true;
        else turn = false;
        wake_up();
    }

    private void playerBoard_message(Message message){
        Message_PlayerBoard m = (Message_PlayerBoard) message;
        System.out.println("\nRicevuta la playerBoard del giocatore " + m.getClientID());
        wake_up();
    }

    private void buy_card_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha comprato la carta: "
                + m.getPar1() + " e l'ha inseria nel " + m.getPar2() +"° slot.");
        wake_up();
    }

    private void chosen_slot_message(Message message) throws IOException {
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
        out.flush();
        out.writeObject(returnMessage);
        wake_up();
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
        wake_up();
    }

    private void resource_amount_message(Message message){
        Message_One_Resource_Two_Int m = (Message_One_Resource_Two_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha " + m.getPar1() + " "
                + m.getResource() + " nel magazzino e " + m.getPar2() + " nel forziere");
        wake_up();
    }

    private void take_marble_message(Message message) throws IOException {
        Message_Four_Parameter_Marble m = (Message_Four_Parameter_Marble) message;
        ArrayList<Marble> marbles = new ArrayList<>();
        marbles.add(m.getMarble1());
        marbles.add(m.getMarble2());
        marbles.add(m.getMarble3());
        if(m.getMarble4() == null){
            for(int i = 0; i < 3; i++){
                chose_marble(marbles);
            }
        }
        else {
            marbles.add(m.getMarble4());
            for(int i = 0; i < 4; i++){
                chose_marble(marbles);
            }
        }
        wake_up();
    }

    private void send_chosen_marble(Marble m) throws IOException {
        Message message = new Message_One_Parameter_Marble(MessageType.USE_MARBLE, position, m);
        out.flush();
        out.writeObject(message);
    }

    private void chose_marble(ArrayList<Marble> marbles) throws IOException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nScegli uno tra queste biglie:\n " + marbles.get(0).toString());
        for(int i = 1; i < marbles.size(); i++)
            System.out.println(", " + marbles.get(i).toString());
        int choice;
        while(true) {
            try {
                choice = stdIn.nextInt();
                if (choice < 1 || choice > marbles.size()) {
                    System.err.println("\nInserisci un numero corretto.\n");
                } else break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                stdIn.next();
            }
        }
        Marble chosenMarble = marbles.get(choice -1);
        marbles.remove(choice);
        send_chosen_marble(chosenMarble);
    }

    private void market_change(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        if(m.getPar1() == 0) {
            System.out.println("\nIl giocatore " + m.getClientID() + " ha scelto la riga " + m.getPar2() + " del mercato.");
        }
        else{
            System.out.println("\nIl giocatore " + m.getClientID() + " ha scelto la colonna " + m.getPar2() + " del mercato.");
        }
        wake_up();
    }

    private void white_conversion_card_message(Message message) throws IOException {
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
        out.flush();
        out.writeObject(returnMessage);
        wake_up();
    }

    private void faith_points_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha incrementato di " + m.getPar()
        + " i suoi punti fede.");
        wake_up();
    }

    private void increase_warehouse_message(Message message){
        Message_One_Int_One_Resource m = (Message_One_Int_One_Resource) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha incrementato di 1 " + m.getResource()
                + " il suo " + m.getPar1() + "° deposito.");
        wake_up();
    }

    private void switch_depot_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha invertito il suo " + m.getPar1()
                + "° deposito con il suo " + m.getPar1() + "° deposito.");
        wake_up();
    }

    private void leader_card_activation_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha attivato la carta leader: " + m.getPar());
        wake_up();
    }

    private void leader_card_discard_message(Message message){
        Message_One_Parameter_Int m = (Message_One_Parameter_Int) message;
        System.out.println("\nIl giocatore " + m.getClientID() + " ha scartato la carta leader: " + m.getPar());
        wake_up();
    }

    private void quit_message(Message message){
        if(startGame)
            System.out.println("\nIl giocatore in posizione " + message.getClientID() + " si è disconnesso. La partita è finita.");
        else
            System.out.println("\nIl giocatore in posizione " + message.getClientID() + " si è disconnesso prima che iniziasse la partita");
        quit = true;
        disconnect();
        System.exit(1);
    }

    private void end_game_message(Message message){
        Message_Two_Parameter_Int m = (Message_Two_Parameter_Int) message;
        System.out.println("\nIl vincitore è il giocatore " + m.getClientID()
                + " che ha totalizzato " + m.getPar1() + " punti vittoria e " + m.getPar2()
                + " risorse totali.");
        quit = true;
        disconnect();
        System.exit(1);
    }

    private void error_message(Message message){
        Message_One_Parameter_String m = (Message_One_Parameter_String) message;
        System.err.println(m.getPar());
        wake_up();
    }

    /**
     * send @param message to Server and wait until a OK message return by Server.
     * if thread is awaken before 10 seconds but not for a OK message return, send another @param message to Server.
     * if thread is awaken cause 10 seconds passed, send another @param message to Server.
     * after 30 seconds Server didn't send any, connection with Server is considered as lost.
     * @param message is the message to send to Server
     * @throws InterruptedException if thread is interrupted during the waiting.
     */
    private void sendMessage(Message message) throws InterruptedException {
        try {
            ok = false;
            out.flush();
            out.writeObject(message);
            long initTime = System.currentTimeMillis();
            long interTime = initTime;
            synchronized (lock) {
                while (!ok) {
                    lock.wait(10000);
                    if (!ok) {
                        if (isTimePassed(initTime, System.currentTimeMillis(), 30000))
                            throw new InterruptedException();
                        else if(isTimePassed(interTime, System.currentTimeMillis(), 10000)){
                            interTime = System.currentTimeMillis();
                            out.flush();
                            out.writeObject(message);
                        }
                        else {
                            initTime = System.currentTimeMillis();
                            interTime = initTime;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("\nClient non più connesso al Server.");
            threadOut.interrupt();
            disconnect();
            System.exit(0);
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