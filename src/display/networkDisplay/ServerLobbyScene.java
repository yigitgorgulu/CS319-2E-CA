package display.networkDisplay;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import network.ServerConnection;

public class ServerLobbyScene extends LobbyScene {

    public ServerLobbyScene(Stage gameView) {
        super();
        HBox h = new HBox();
        h.setVisible(true);
        h.getChildren().add(new Label("Waiting for the other players..."));
        root.getChildren().add(h);

        connection = new ServerConnection(gameView);
        connection.setDaemon(true);
        connection.start();

    }
}

