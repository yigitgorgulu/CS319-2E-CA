package display.networkDisplay;

import javafx.stage.Stage;
import network.ClientConnection;

import java.io.IOException;

public class ClientLobbyScene extends LobbyScene {

    public ClientLobbyScene(Stage gameView, String IPAddress) throws IOException {
        super();
        String name = ((int)(Math.random() * 9999999)) + "";

        connection = new ClientConnection(gameView, name, IPAddress);
        connection.setDaemon(true);
        connection.start();
    }

    public void setPopUp(Stage stage){
        connection.setPopUp(stage);
    }
}

