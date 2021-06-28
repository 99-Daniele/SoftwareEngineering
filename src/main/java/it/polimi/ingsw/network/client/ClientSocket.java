package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.messages.*;

import java.io.*;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * connectedThread is the thread which evaluates if Client is still connected to Server.
 * threadOut is the thread which receive input from Server.
 * out and in are Client-Server serializable objects streams.
 * connected is true if Client is theoretically connected to Server.
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
     * @param socket is the socket connect with ServerSocket
     * @throws IOException if connection crash at any moment.
     *
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
                } catch (IOException e) {
                    brutalDisconnection();
                }
            });
            threadOut.start();
        }
    }

    /**
     * @throws IOException if the connection with Server breaks during waiting.
     *
     * wait messages from Server. After 30 seconds if Server has not sent any message, connection with Server is considered as lost.
     */
    private void receivePing() throws InterruptedException, IOException {
        while (true) {
            long initTime = System.currentTimeMillis();
            synchronized (pingLock) {
                pingLock.wait(30000);
                if (isTimePassed(initTime, System.currentTimeMillis()))
                    throw new IOException();
            }
        }
    }

    /**
     * constantly wait for Server input and handle it by return a new message to Server or notifying ClientView.
     */
    private void receiveMessage() throws IOException {
        while (true) {
            try {
                Message returnMessage = (Message) in.readObject();
                wakeUp();
                if(returnMessage.getMessageType() == MessageType.PING){
                    pingMessage();
                }
                else {
                    setChanged();
                    notifyObservers(returnMessage);
                }
            } catch (ClassNotFoundException | ClassCastException e) {
                System.err.println("\nUnexpected message from Server.");
            }
        }
    }

    /**
     * wake pingLock thread when message arrives from Server.
     */
    private void wakeUp() {
        synchronized (pingLock) {
            pingLock.notifyAll();
        }
    }

    /**
     * @throws IOException if connection crash at any moment.
     *
     * send to Server a PONG message after PING.
     */
    private void pingMessage() throws IOException {
        Message ping = new Message(MessageType.PONG, 0);
        sendMessage(ping);
    }

    /**
     * @param message is the message to send to Server.
     *
     * send @param message to Server.
     */
    public static void sendMessage(Message message) throws IOException{
        out.flush();
        out.writeObject(message);
    }

    /**
     * @param initTime is the time in milliseconds from which start counting.
     * @param currentTime is the current time in milliseconds.
     * @return if 30 seconds have passed.
     */
    private boolean isTimePassed(long initTime, long currentTime){
        return ((currentTime - initTime) >= 30000);
    }

    public static void setDisconnected(){
        connected = false;
    }

    /**
     * close inputStream and outputStream with Server.
     */
    public static void disconnect() {
        connected = false;
        try {
            in.close();
            out.close();
        } catch (IOException | RuntimeException ignored) {
        }
    }

    /**
     * this method is called when Client brutally lost Server connection. Inform Client and then disconnect.
     */
    private void brutalDisconnection(){
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException ignored) {
        }
        if(connected) {
            connected = false;
            disconnect();
            setChanged();
            notifyObservers(new MessageOneParameterString(MessageType.QUIT, -1, null));
        }
    }
}