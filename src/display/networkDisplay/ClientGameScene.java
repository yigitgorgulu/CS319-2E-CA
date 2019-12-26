package display.networkDisplay;

import display.MapButton;
import javafx.stage.Stage;
import network.ClientConnection;
import network.requests.Requests;
import game.map.*;
import game.player.Player;

import java.io.*;
import java.util.concurrent.CountDownLatch;

public class ClientGameScene extends NetworkGameScene {
    public ClientGameScene(CountDownLatch mapLatch, Map map, Stage primaryStage, ClientConnection connection, Player player) throws IOException {
        super(primaryStage, connection);
        this.player = player;
        this.map = map;
        addBackground();
        createGameAndTiles();
        addPlayerResourcesMenu();
    }

    @Override
    public void displayDice(int dieNum1, int dieNum2) {
        super.displayDice(dieNum1, dieNum2);
    }

    @Override
    protected void setupButtons() {
        endTurnButton.setDisable(true);
        endTurnButton.setOnMouseClicked(e->{
            try {
                connection.send(Requests.END_TURN);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            endTurnButton.setDisable(true);
        });
    }

    @Override
    protected void tileMouseClicked(MapButton mb, MapTile a) {
        return;
    }

    @Override
    protected void nonTileMouseClicked(MapButton mb, MapElement a) {
        mb.setOnMouseClicked(e -> super.nonTileMouseClicked(mb, a));
        mapButtonList.add(mb);
    }

    @Override
    public void updateResources(Player player, String playerName) {
        this.player = player;
        super.updateResources(player, playerName);
    }

    public void enableEndTurn() {
        this.endTurnButton.setDisable(false);
    }
}


