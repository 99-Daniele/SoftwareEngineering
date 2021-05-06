package it.polimi.ingsw;

import it.polimi.ingsw.network.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * threadIn is the thread which receive input from System.in
 * threadOut is the thread which receive input from Server.
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
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    Scanner stdIn = new Scanner(new InputStreamReader(System.in));
    private boolean quit;
    private int ok;
    private int turn;
    private final Object lock = new Object();

    public ClientSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.quit = false;
    }

    /**
     * start threadIn and threadOut.
     * threadIn firstly login player and then constantly waits user inputs from System.in
     * threadOut constantly waits Server inputs.
     */
    public void start(){
        if(threadIn == null){
            threadIn = new Thread(() -> {
                try {
                    login();
                    while (!quit)
                        turnAction();
                    System.out.println("\nDisconnessione.");
                    disconnect();
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
        if(threadOut == null){
            threadOut = new Thread(() -> receiveCommand());
            threadOut.start();
        }
    }

    /**
     * until player doesn't insert a valid nickName, constantly ask a new one. Then send it to Server and waits an OK
     * message return.
     * @throws InterruptedException if thread is interrupted during the waiting.
     */
    private void login() throws  InterruptedException {
        String userInput;
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nInserisci il tuo nickName:");
        userInput = stdIn.nextLine();
        while (userInput.length() == 0) {
            System.out.println("\nInserisci un nickName valido:");
            userInput = stdIn.nextLine();
        }
        Message message = new Message_One_Parameter_String(MessageType.LOGIN, userInput);
        sendCommand(message);
    }

    /**
     * ask user to insert 1 int to chose which action activate. Then send it to Server and waits an OK message return.
     * @throws InterruptedException if thread is interrupted during the waiting.
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
    private int player_input(){
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
     * @throws InterruptedException if thread is interrupted during the waiting.
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
        Message message = new Message_One_Parameter_Int(MessageType.BUY_CARD, x);
        System.out.println("\n" + message.toString());
        if (isMyTurn())
            sendCommand(message);
    }

    /**
     * until user doesn't insert a valid input, ask him 2 int to chose two depots to switch. Then send it to Server and
     * waits an OK message return.
     * @throws InterruptedException if thread is interrupted during the waiting.
     */
    private void switch_depot() throws InterruptedException {
        stdIn = new Scanner(new InputStreamReader(System.in));
        System.out.println("\nScegli il primo deposito:");
        int x;
        while(true) {
            try {
                x = stdIn.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\nScegli il primo deposito:");
                stdIn.next();
            }
        }
        System.out.println("\nScegli il secondo deposito:");
        int y;
        while(true) {
            try {
                y = stdIn.nextInt();
                break;
            } catch (InputMismatchException e) {
                System.err.println("\nInserisci un numero.");
                System.out.println("\nScegli il secondo deposito:");
                stdIn.next();
            }
        }
        Message message = new Message_Two_Parameter_Int(MessageType.SWITCH_DEPOT, x, y);
        System.out.println("\n" + message.toString());
        if(isMyTurn())
            sendCommand(message);
    }

    /**
     * send to Server a QUIT message and close socket without waiting Server answer.
     */
    private void quit() {
        try {
            Message message = new Message(MessageType.QUIT);
            System.out.println("\n" + message.toString());
            out.flush();
            out.writeObject(message);
            quit = true;
        } catch (IOException e) {
            System.err.println("\nClient già disconesso dal Server.");
        }
    }

    /**
     * send to Server a TURN message and wait thread until a TURN message return by Server or after 10 seconds.
     * if thread is awaken before 10 seconds but not for a TURN message return, send another TURN request to Server.
     * if thread is awaken cause 10 seconds passed, connection with Server is considered as lost.
     * @return if is player turn.
     * @throws InterruptedException if thread is interrupted during the waiting.
     */
    private boolean isMyTurn() throws InterruptedException {
        try {
            turn = 0;
            Message message = new Message(MessageType.TURN);
            out.flush();
            out.writeObject(message);
            long initTime = System.currentTimeMillis();
            synchronized (lock) {
                while (true) {
                    lock.wait(10000);
                    switch (turn) {
                        case 0: {
                            if (isTimePassed(initTime, System.currentTimeMillis()))
                                throw new InterruptedException();
                        }
                        break;
                        case 1:
                            return true;
                        case 2: {
                            out.flush();
                            out.writeObject(message);
                            ok = 0;
                        }
                        break;
                        case 3:
                            System.out.println("\nNon è il tuo turno");
                            return false;
                    }
                }
            }
        }
        catch (IOException e) {
            System.err.println("\nClient non più connesso al Server.");
            threadOut.interrupt();
            disconnect();
            System.exit(0);
            return false;
        }
    }

    /**
     * constantly wait for Server input and handle it by return a new message to Server or notifying other thread.
     */
    private void receiveCommand(){
        try {
            while(true) {
                Message returnMessage = (Message) in.readObject();
                switch (returnMessage.getMessageType()) {
                    case OK:
                        ok = 1;
                        turn = 2;
                        synchronized (lock) {
                            lock.notifyAll();
                        }
                        break;
                    case PING:
                        ok = 2;
                        turn = 2;
                        out.flush();
                        out.writeObject(returnMessage);
                        break;
                    case TURN:
                        ok = 2;
                        Message_One_Parameter_Int cmd = (Message_One_Parameter_Int) returnMessage;
                        if(cmd.getPar() == 1)
                            turn = 1;
                        else
                            turn = 3;
                        synchronized (lock){
                            lock.notifyAll();
                        }
                        break;
                    default:
                        System.out.println("" + returnMessage.toString());
                }
            }
        } catch (IOException e) {
        } catch (ClassNotFoundException e) {
            System.err.println("\nValore di ritorno errato.");
        }
    }

    /**
     * send @param message to Server and wait thread until a OK message return by Server or after 10 seconds.
     * if thread is awaken before 10 seconds but not for a OK message return, send another @param message to Server.
     * if thread is awaken cause 10 seconds passed, connection with Server is considered as lost.
     * @param message is the message to send to Server
     * @throws InterruptedException if thread is interrupted during the waiting.
     */
    private void sendCommand(Message message) throws InterruptedException {
        try {
            ok = 0;
            out.flush();
            out.writeObject(message);
            long initTime = System.currentTimeMillis();
            synchronized (lock) {
                while (ok != 1) {
                    lock.wait(10000);
                    if(ok == 0) {
                        if (isTimePassed(initTime, System.currentTimeMillis()))
                            throw new InterruptedException();
                    }
                    else if(ok == 2){
                        out.flush();
                        out.writeObject(message);
                        ok = 0;
                    }
                }
                System.out.println("\n" + message.toString() + " è stato eseguito.");
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
     * @return if 10 seconds have passed
     */
    private boolean isTimePassed(long initTime, long currentTime){
        return ((currentTime - initTime) >= 10000);
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