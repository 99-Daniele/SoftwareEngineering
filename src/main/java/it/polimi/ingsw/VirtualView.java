package it.polimi.ingsw;

import it.polimi.ingsw.network.messages.Message;
import it.polimi.ingsw.network.messages.MessageType;
import it.polimi.ingsw.network.messages.Message_One_Parameter_Int;
import it.polimi.ingsw.network.messages.Message_One_Parameter_String;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.Observable;
import java.util.Observer;

public class VirtualView extends Observable implements View, Observer, Runnable {


    private Object lock = new Object();
    private boolean connected;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String nickName;
    private int position;

    public VirtualView(ObjectInputStream in, ObjectOutputStream out){
        this.in=in;
        this.out=out;
        this.connected = true;
    }

    public void addController(ControllerGame controllerGame){
        addObserver(controllerGame);
    }

    @Override
    public void run(){
        while (connected) {
            try {
                Message message = (Message) in.readObject();
                if (receiveMessage(message)) {
                    System.out.println(nickName + " si è disconnesso.\n");
                    break;
                }
            } catch (IOException e) {
                if(connected) {
                    System.err.println(nickName + " disconnesso brutalmente.");
                    setChanged();
                    notifyObservers(new Message(MessageType.QUIT, position));
                }
                else
                    System.out.println(nickName + " si è disconnesso.\n");
                break;
            } catch (ClassNotFoundException | ClassCastException e) {
                System.err.println(nickName + " invia messaggi con formato errato.");
            }

        }
    }

    private boolean receiveMessage(Message message) throws IOException {
        setChanged();
        notifyObservers(message);
        return (message.getMessageType() == MessageType.QUIT);
    }

    public void quit(int clientID) throws IOException {
        Message quitMessage = new Message(MessageType.QUIT, clientID);
        try {
            out.flush();
            out.writeObject(quitMessage);
        }catch (SocketException e){}
    }

    public void endGame(Message message) throws IOException {
        try {
            out.flush();
            out.writeObject(message);
            in.close();
            out.close();
        }catch (SocketException e){}
        connected = false;
    }

    @Override
    public String getNickname() throws IOException {
        while (true) {
            try {
                Message_One_Parameter_String loginMessage = (Message_One_Parameter_String) in.readObject();
                System.out.println(loginMessage.toString());
                nickName = loginMessage.getPar();
                break;
            } catch (ClassCastException | ClassNotFoundException e) {
            }  catch (IOException e) {
                System.err.println("Client disconnesso brutalmente.");
                throw new IOException();
            }
        }
        return nickName;
    }

    public void nicknameTaken() throws IOException {
        try {
            Message errMessage = new Message_One_Parameter_String(MessageType.ERR, position, "NickName già scelto");
            out.flush();
            out.writeObject(errMessage);
        } catch (IOException e) {
            System.err.println("Client disconnesso brutalmente.");
            throw new IOException();
        }
    }

    public int getNumberPlayers() throws IOException {
        try {
            Message message = new Message(MessageType.LOGIN, 1);
            out.flush();
            out.writeObject(message);
            while (true) {
                try {
                    Message_One_Parameter_Int loginMessage = (Message_One_Parameter_Int) in.readObject();
                    System.out.println(loginMessage.toString());
                    if (loginMessage.getPar() >= 1 && loginMessage.getPar() <= 4 &&
                            loginMessage.getMessageType() == MessageType.NUM_PLAYERS) {
                        message = new Message(MessageType.OK, 1);
                        out.flush();
                        out.writeObject(message);
                        return loginMessage.getPar();
                    } else {
                        Message errMessage;
                        if (loginMessage.getMessageType() != MessageType.NUM_PLAYERS) {
                            errMessage = new Message_One_Parameter_String(MessageType.ERR, 1, "Inserisci numero di giocatori");
                        } else {
                            errMessage = new Message_One_Parameter_String(MessageType.ERR, 1, "Numero non valido");
                        }
                        out.flush();
                        out.writeObject(errMessage);
                        loginMessage = (Message_One_Parameter_Int) in.readObject();
                        System.out.println(loginMessage.toString());
                    }
                } catch (ClassNotFoundException | ClassCastException e) {
                    Message_One_Parameter_String errMessage = new Message_One_Parameter_String(MessageType.ERR, 1, "Inserisci numero di giocatori");
                    out.flush();
                    out.writeObject(errMessage);
                }
            }
        } catch (IOException e) {
            System.err.println(nickName + " disconnesso brutalmente.");
            throw new IOException();
        }
    }

    public void myTurn(boolean turn) throws IOException {
        Message turnMessage;
        if (turn) {
            turnMessage = new Message_One_Parameter_Int(MessageType.TURN, position, 1);
        } else {
            turnMessage = new Message_One_Parameter_Int(MessageType.TURN, position, 2);
        }
        out.flush();
        out.writeObject(turnMessage);
    }

    public void position(int position) throws IOException {
        this.position = position;
        Message message = new Message(MessageType.LOGIN, position);
        out.flush();
        out.writeObject(message);
    }

    public void pronto(int numPlayers) throws IOException {
        Message message = new Message_One_Parameter_Int(MessageType.START_GAME, position, numPlayers);
        out.flush();
        out.writeObject(message);
    }

/*
    private Scanner CurrentViewIn(int currentPos){
        return viewsPos.get(currentPos).getIn();
    }
    private PrintWriter CurrentViewOut(int currentPos){
        return viewsPos.get(currentPos).getOut();
    }
    */

    @Override
    public void update(Observable o, Object arg) {
        Message message = (Message) arg;
        try {
            switch (message.getMessageType()) {
                case END_GAME:
                    endGame(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
