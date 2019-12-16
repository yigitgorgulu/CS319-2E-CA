package display.networkDisplay;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.ClientConnection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class ClientScene {

    private Group root;
    private Scene scene;
    ObjectOutputStream outx;
    ObjectInputStream inx;
    ClientConnection clientConnection;

    public ClientScene(Stage gameView) throws IOException {
        root = new Group();


        Scanner scan = new Scanner(System.in);
        String name = ((int)(Math.random() * 9999999)) + "";

        clientConnection = new ClientConnection(gameView, name);

        clientConnection.setDaemon(true);
        clientConnection.start();

    }

    public Scene getScene() {
        scene = new Scene(root);
        return scene;
    }

}

