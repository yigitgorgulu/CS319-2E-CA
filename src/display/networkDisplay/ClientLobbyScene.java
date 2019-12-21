package display.networkDisplay;

import display.CivilizationSelectionScene;
import display.MenuButton;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.Stage;
import network.ClientConnection;

import java.awt.*;
import java.io.IOException;

public class ClientLobbyScene extends LobbyScene {

    public ClientLobbyScene(Stage gameView, String IPAddress, SimpleBooleanProperty connectionFailed) throws IOException {
        super();
        String name = ((int)(Math.random() * 9999999)) + "";
        setConnection(gameView,name, IPAddress ,connectionFailed);
    }

    public void setPopUp(Stage stage){
        connection.setPopUp(stage);
    }

    public void setConnection(Stage gameView, String name, String IPAddress, SimpleBooleanProperty connectionFailed){
        connection = new ClientConnection(gameView, name, IPAddress,connectionFailed);
        connection.setDaemon(true);
        connection.start();
    }
}

