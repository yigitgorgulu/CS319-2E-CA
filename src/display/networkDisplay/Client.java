package display.networkDisplay;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client {
    Socket socket;
    ObjectOutputStream os;
    ObjectInputStream in;

    public Client(Socket socket, ObjectOutputStream os, ObjectInputStream in) {
        this.socket = socket;
        this.os = os;
        this.in = in;
    }
}
