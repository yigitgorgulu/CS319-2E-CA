package display.networkDisplay;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import network.Client;
import network.ServerConnection;

import java.io.IOException;

public class ServerScene {
    private Group root;
    private Scene scene;
    Client[] clients;

    public ServerScene(Stage gameView) throws IOException {
        root = new Group();

        HBox h = new HBox();
        h.setVisible(true);
        h.getChildren().add(new Label("Waiting for the other players..."));

        root.getChildren().add(h);

        ServerConnection serverConnection = new ServerConnection(gameView);
        serverConnection.start();

    }

    public Scene getScene(){
        scene = new Scene(root);
        return scene;
    }
}

