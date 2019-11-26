package display.networkDisplay;

import display.Die;
import display.ResourceBox;
import game.Game;
import game.map.Map;
import game.player.Player;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;

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

