package display.networkDisplay;

import display.*;
import network.ClientConnection;
import network.requests.BuildRequest;
import network.requests.PlayerInfo;
import network.requests.Requests;
import game.map.*;
import game.player.Player;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import javafx.scene.control.Button;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ClientGameScene extends GameScene {
    Player player;
    ClientConnection clientConnection;
    List<MapButton> mapButtonList;

    public ClientGameScene(CountDownLatch mapLatch, Map map, ClientConnection clientConnection, Player player) throws IOException {
        super();
        mapButtonList = new ArrayList<>();
        this.player = player;
        this.clientConnection = clientConnection;
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
    protected void createPlayerResourceBoxes() throws IOException {
        resourceBoxes[0] = new ResourceBox(player, "BRICK");
        resourceBoxes[1] = new ResourceBox(player, "WOOD");
        resourceBoxes[2] = new ResourceBox(player, "SHEEP");
        resourceBoxes[3] = new ResourceBox(player, "WHEAT");
        resourceBoxes[4] = new ResourceBox(player, "ORE");
    }

    @Override
    protected void setupEndTurnButton() {
        endTurnButton.setDisable(true);
        endTurnButton.setOnAction(e->{
            try {
                System.out.println("SENDING ENT TURN REQUEST");
                clientConnection.send(Requests.END_TURN);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            endTurnButton.setDisable(true);
        });
    }

    @Override
    protected void nonTileMouseClicked(MapButton mb, MapElement a) {
        mb.setOnMouseClicked(e -> {
            try {
                System.out.println("SENDING BUILD BY PLAYER:");
                System.out.println(player.name);
                clientConnection.send(new BuildRequest(a,mb, new PlayerInfo(player)));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        mapButtonList.add(mb);
    }

    public void updateResources(Player player, String playerName) {
        System.out.println(player.name);
        System.out.println("Brick, Ore, Wood, Sheep, Wheat"  + player.getBrick() + " "
                +player.getOre()+ " " +  player.getWood() + " " + player.getSheep() + " " +
                + player.getWheat());
        this.player = player;
        // box1.update(player);
        // box2.update(player);
        // box3.update(player);
        // box4.update(player);
        // box5.update(player);
        turnOfPlayer.setText("Turn of player " + playerName);
    }

    public MapButton findMapButton(int x, int y) {
        for(MapButton button: mapButtonList) {
            if (x == button.x && y == button.y) {
                return button;
            }
        }
        return null;
    }

    public void enableEndTurn() {
        this.endTurnButton.setDisable(false);
    }

    public Player getPlayer() {
        return this.player;
    }
}


