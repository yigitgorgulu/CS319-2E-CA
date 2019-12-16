package display.networkDisplay;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import network.ClientConnection;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ClientScene {

    private Group root;
    private Scene scene;
    ObjectOutputStream outx;
    ObjectInputStream inx;
    ClientConnection clientConnection;

    public ClientScene(Stage gameView) throws IOException {
        root = new Group();
        String name = 

        clientConnection = new ClientConnection(gameView);

        clientConnection.start();

    }

    public Scene getScene() {
        scene = new Scene(root);
        return scene;
    }

}

