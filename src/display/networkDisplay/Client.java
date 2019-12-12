package display.networkDisplay;

import game.player.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    Socket socket;

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    Player player;
    ObjectOutputStream os;
    ObjectInputStream in;

    public Client(Socket socket, ObjectOutputStream os, ObjectInputStream in) {
        this.socket = socket;
        this.os = os;
        this.in = in;
    }


}
