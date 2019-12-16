package network;

import game.player.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {

    static int idCounter = 0;
    int id;
    Socket socket;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player player;
    public ObjectOutputStream os;
    public ObjectInputStream in;

    public Client(Socket socket, ObjectOutputStream os, ObjectInputStream in) {
        this.socket = socket;
        this.os = os;
        this.in = in;

        id = ++idCounter;
    }


}
