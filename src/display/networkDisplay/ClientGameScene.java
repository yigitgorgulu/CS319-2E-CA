package display.networkDisplay;

import network.ClientConnection;
import network.requests.Requests;
import game.map.*;
import game.player.Player;

import java.io.*;
import java.util.concurrent.CountDownLatch;

public class ClientGameScene extends NetworkGameScene {
    public ClientGameScene(CountDownLatch mapLatch, Map map, ClientConnection connection, Player player) throws IOException {
        super(connection);
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
        endTurnButton.setOnAction(e->{
            try {
                System.out.println("SENDING END TURN REQUEST");
                connection.send(Requests.END_TURN);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            endTurnButton.setDisable(true);
        });
    }

    @Override
    protected void nonTileMouseClicked(MapButton mb, MapElement a) {
        mb.setOnMouseClicked(e -> super.nonTileMouseClicked(mb, a));
        mapButtonList.add(mb);
    }

    @Override
    public void updateResources(Player player, String playerName) {
        System.out.println(player.name);
        System.out.println("Brick, Ore, Wood, Sheep, Wheat"  + player.getBrick() + " "
                +player.getOre()+ " " +  player.getWood() + " " + player.getSheep() + " " +
                + player.getWheat());
        this.player = player;
        super.updateResources(player, playerName);
    }

    public void enableEndTurn() {
        this.endTurnButton.setDisable(false);
    }
}


