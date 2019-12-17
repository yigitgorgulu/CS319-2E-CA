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

    public void dice(int dieNum1, int dieNum2) {
        // root.getChildren().remove(diceBox);
        // die1 = new Die(dieNum1);
        // die2 = new Die(dieNum2);
        //
        // diceBox = new HBox(die1, die2);
        // diceBox.setTranslateX(200);
        // diceBox.setTranslateY(200);
        // root.getChildren().add(diceBox);
    }

    @Override
    protected void createDie() {

    }

    protected void addPlayerResourcesMenu() throws IOException {
        // box1 = new ResourceBox(player, "BRICK");
        // box2 = new ResourceBox(player, "WOOD");
        // box3 = new ResourceBox(player, "SHEEP");
        // box4 = new ResourceBox(player, "WHEAT");
        // box5 = new ResourceBox(player, "ORE");
        //
        // endTurn.setDisable(true);
        // endTurn.setOnAction(e->{
        //     try {
        //         System.out.println("SENDING ENT TURN REQUEST");
        //         clientConnection.send(Requests.END_TURN);
        //     } catch (Exception ex) {
        //         ex.printStackTrace();
        //     }
        //     endTurn.setDisable(true);
        // });
    }

    @Override
    protected void createPlayerResourceBoxes() throws IOException {

    }

    @Override
    protected void setupEndTurnButton() {

    }

    @Override
    protected void createGameAndTiles() throws FileNotFoundException {
        // map.getNonTileElements().forEach(a -> {
        //     double x = a.getLocation().getRawDisplayPosition().getX();
        //     double y = a.getLocation().getRawDisplayPosition().getY();
        //     double r = 0.0;
        //     if (a instanceof MapCorner)
        //         r = 13.0;
        //     if (a instanceof MapSide)
        //         r = 11.0;
        //     MapButton mb = new MapButton(x, y, r, a);
        //     mb.setFill(Color.GRAY);
        //     mb.setOpacity(0.0);
        //     mb.setOnMouseClicked(e -> {
        //         try {
        //             System.out.println("SENDING BUILD BY PLAYER:");
        //             System.out.println(player.name);
        //             clientConnection.send(new BuildRequest(a,mb, new PlayerInfo(player)));
        //         } catch (Exception ex) {
        //             ex.printStackTrace();
        //         }
        //     });
        //     mapButtonList.add(mb);
        //     root.getChildren().add(mb);
        // });
    }

    @Override
    protected void nonTileMouseClicked(MapButton mb, MapElement a) {

    }

    @Override
    protected void creationSetup() {

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


