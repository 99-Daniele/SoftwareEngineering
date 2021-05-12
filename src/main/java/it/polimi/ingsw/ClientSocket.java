package it.polimi.ingsw;

import it.polimi.ingsw.network.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
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
    private final Socket socket;
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
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.quit = false;
        this.ok = false;
        this.turn = false;
        this.startGame = false;
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
        switch (player_input()) {
            case 1:
                buy_card();
                break;
            case 2:
                switch_depot();
                break;
            default:
                quit();
                break;
        }
    }

    /**
     * until user doesn't insert a valid input, ask him 1 int to chose which action activate.
     * @return the action chosen by user.
     */
    private int player_input() throws InterruptedException {
        synchronized (lock){
            while(!turn)
                lock.wait();
        }
        System.out.println("\nE' il tuo turno");
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\n1 - BUY_CARD\n2 - SWITCH_DEPOT\n0 - QUIT");
        int userInput;
        while(true){
            try {
                userInput = stdIn.nextInt();
                if(userInput < 0 || userInput > 2){
                    System.err.println("\nInserisci un numero da 0 a 2.\n");
                    System.out.println("\n1 - BUY_CARD\n2 - SWITCH_DEPOT\n0 - QUIT\n");
                }
                else break;
            }
            catch (InputMismatchException e){
                System.err.println("\nInserisci un numero.");
                System.out.println("\n1 - BUY_CARD\n2 - SWITCH_DEPOT\n0 - QUIT");
                stdIn.next();
            }
        }
        return userInput;
    }

    /**
     * until user doesn't insert a valid input, ask him 1 int to chose the deck to where buy card. Then send it to Server
     * and waits an OK message return.
     * @throws InterruptedException if the connection with Server breaks during waiting.
     */
    private void buy_card() throws InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nScegli un mazzetto:");
        int x;
        while(true) {
            try {
                x = stdIn.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\nScegli un mazzetto:");
                stdIn.next();
            }
        }
        Message message = new Message_One_Parameter_Int(MessageType.BUY_CARD, position, x);
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
     * send to Server a QUIT message and close socket without waiting Server answer.
     */
    private void quit() {
        try {
            Message message = new Message(MessageType.QUIT, position);
            System.out.println("\n" + message.toString());
            out.flush();
            out.writeObject(message);
            quit = true;
        } catch (IOException e) {
            System.err.println("\nClient già disconesso dal Server.");
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
                    case LOGIN: {
                        position = returnMessage.getClientID();
                        ok = true;
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }
                    break;
                    case START_GAME: {
                        Message_One_Parameter_Int message = (Message_One_Parameter_Int) returnMessage;
                        System.out.println("\nLa partita è iniziata. N° giocatori: " + message.getPar());
                        turn = false;
                        startGame = true;
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }
                    break;
                    case OK:
                        ok = true;
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                        break;
                    case PING:
                        out.flush();
                        out.writeObject(returnMessage);
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                        break;
                    case TURN: {
                        Message_One_Parameter_Int message = (Message_One_Parameter_Int) returnMessage;
                        if (message.getPar() == 1) turn = true;
                        else turn = false;
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                    }
                    break;
                    case QUIT:
                        System.err.println("\nUn altro giocatore si è disconnesso. La partita è finita.");
                        quit = true;
                        break;
                    default:
                        System.out.println("" + returnMessage.toString());
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                        break;
                }
            }
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
            System.err.println("\nValore di ritorno errato.");
        }
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
            out.close();
            in.close();
            socket.close();
        } catch (IOException e) {
            System.exit(0);
        }
    }
}