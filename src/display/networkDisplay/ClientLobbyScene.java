package display.networkDisplay;

import javafx.stage.Stage;
import network.ClientConnection;

public class ClientLobbyScene extends LobbyScene {

    public ClientLobbyScene(Stage gameView) {
        super();
        String name = ((int)(Math.random() * 9999999)) + "";

        connection = new ClientConnection(gameView, name);
        connection.setDaemon(true);
        connection.start();

    }
}

