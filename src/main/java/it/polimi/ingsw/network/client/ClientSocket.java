package it.polimi.ingsw.network.client;

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
public class ClientSocket extends Observable{

    private Thread threadOut;
    private Thread connectedThread;
    private static ObjectOutputStream out = null;
    private static ObjectInputStream in;
    private final Object pingLock = new Object();
    private static boolean connected;

    public void addObserver(Observer o){
        super.addObserver(o);
    }

    /**
     * start threadIn and threadOut.
     * threadIn firstly login player and then constantly waits user inputs from System.in
     * threadOut constantly waits Server inputs.
     */
    public void start(Socket socket) throws IOException {
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        connected = true;
        if (connectedThread == null) {
            connectedThread = new Thread(() -> {
                try {
                    receivePing();
                } catch (InterruptedException | IOException e) {
                    brutalDisconnection();
                }
            });
            connectedThread.start();
        }
        if (threadOut == null) {
            threadOut = new Thread(() -> {
                try {
                    receiveMessage();
                } catch (InterruptedException | IOException e) {
                    brutalDisconnection();
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
     * constantly wait for Server input and handle it by return a new message to Server or notifying other thread.
     */
    private void receiveMessage() throws IOException, InterruptedException {
        while (true) {
            try {
                Message returnMessage = (Message) in.readObject();
                wake_up();
                if(returnMessage.getMessageType() == MessageType.PING){
                    ping_message();
                }
                else {
                    Thread thread = new Thread(() -> {
                        setChanged();
                        notifyObservers(returnMessage);
                    });
                    thread.start();
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.err.println("\nUnexpected message from Server.");
            }
        }
    }

    private void wake_up() {
        synchronized (pingLock) {
            pingLock.notifyAll();
        }
    }

    private void ping_message() throws IOException {
        Message ping = new Message(MessageType.PONG, 0);
        sendMessage(ping);
    }

    /**
     * send @param message to Server and wait until a OK message return by Server.
     * if thread is awaken before 10 seconds but not for a OK message return, send another @param message to Server.
     * if thread is awaken cause 10 seconds passed, send another @param message to Server.
     * after 30 seconds Server didn't send any, connection with Server is considered as lost.
     * @param message is the message to send to Server
     */
    public static void sendMessage(Message message) throws IOException{
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

    public static void setDisconnected(){
        connected = false;
    }

    /**
     * close inputStream, outputStream and socket connection with Server.
     */
    public static void disconnect() {
        connected = false;
        try {
            in.close();
            out.close();
        } catch (IOException e) {
        }
    }

    private void brutalDisconnection(){
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException interruptedException) {
        }
        if(connected) {
            System.err.println("\nClient no longer connected to the Server");
            connected = false;
            disconnect();
            System.exit(0);
        }
    }
}