package display.networkDisplay;

import display.*;
import network.ServerConnection;
import game.Game;
import game.map.*;
import game.player.Player;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import javafx.scene.control.Button;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ServerGameScene extends GameScene {
    Game game;
    Player player;
    ServerConnection serverConnection;
    List<MapButton> mapButtonList;

    public ServerGameScene(ArrayList<Player> p, ServerConnection serverConnection) throws IOException {
        super();
        player = p.get(0);
        System.out.println("SERVER PLAYER COLOR:" + player.getColor().toString());
        mapButtonList = new ArrayList<>();
        this.serverConnection = serverConnection;

        map = new Map();
        addBackground();
        createGameAndTiles(p);
        addPlayerResourcesMenu();
        displayDice();
    }

    @Override
    protected void createDie() {
        dice[0] = new Die(game.getDie1());
        dice[1] = new Die(game.getDie2());
    }

    public void endTurnProcess() {
        game.endTurn();
        updateResources(player);
        displayDice();

        System.out.println("end turn done");
        System.out.println(game.getCurrentPlayer().name + "\n" + player.name);

        if(game.getCurrentPlayer().equals(player)) {
            endTurnButton.setDisable(false);
        }

        serverConnection.sendEndTurnInfo(game.getCurrentPlayer(), game.getDie1(), game.getDie2());

    }

    public void endTurnProcess(CountDownLatch countDownLatch) {
        game.endTurn();
        updateResources(player);
        displayDice();

        System.out.println("end turn done");
        System.out.println(game.getCurrentPlayer().name + "\n" + player.name);

        if(game.getCurrentPlayer().equals(player)) {
            endTurnButton.setDisable(false);
        }

        serverConnection.sendEndTurnInfo(game.getCurrentPlayer(), game.getDie1(), game.getDie2());

        countDownLatch.countDown();
    }

    protected void addPlayerResourcesMenu() throws IOException {
        double widthOfRectangle = DefaultUISpecifications.SCREEN_WIDTH / 3;
        double heightOfRectangle = DefaultUISpecifications.SCREEN_HEIGHT / 7;
        double leftUpperCornerOfTheRectangleX = DefaultUISpecifications.SCREEN_WIDTH / 2 - widthOfRectangle / 2;
        double leftUpperCornerOfTheRectangleY = DefaultUISpecifications.SCREEN_HEIGHT - heightOfRectangle;
        Rectangle resourcesBackground = new Rectangle(widthOfRectangle, heightOfRectangle);
        resourcesBackground.setTranslateX(leftUpperCornerOfTheRectangleX);
        resourcesBackground.setTranslateY(leftUpperCornerOfTheRectangleY - 20);
        resourcesBackground.setFill(Color.WHITESMOKE);
        root.getChildren().add(resourcesBackground);

        resourceBoxes[0] = new ResourceBox(player, "BRICK");
        resourceBoxes[1] = new ResourceBox(player, "WOOD");
        resourceBoxes[2] = new ResourceBox(player, "SHEEP");
        resourceBoxes[3] = new ResourceBox(player, "WHEAT");
        resourceBoxes[4] = new ResourceBox(player, "ORE");

        separatorRectangle = new Rectangle(20,0);

        endTurnButton = new Button("End Turn");
        endTurnButton.setOnAction(e->{
            endTurnProcess();
            endTurnButton.setDisable(true);
        });

        HBox hBox = new HBox();

        hBox.getChildren().addAll(resourceBoxes[0],resourceBoxes[1],resourceBoxes[2],resourceBoxes[3],resourceBoxes[4], separatorRectangle,endTurnButton);
        turnOfPlayer = new Label("Turn of player 1");
        turnOfPlayer.setFont(new Font("Calibri", 14));
        turnOfPlayer.setTextFill(Color.BROWN);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(turnOfPlayer, hBox);
        vBox.setTranslateX(leftUpperCornerOfTheRectangleX);
        vBox.setTranslateY(leftUpperCornerOfTheRectangleY - 20);

        vBox.setPadding(new Insets(4,4,60,4));
        root.getChildren().add(vBox);
    }

    @Override
    protected void createPlayerResourceBoxes() throws IOException {

    }

    @Override
    protected void setupEndTurnButton() {

    }

    protected void addBackground(){
        Rectangle bg = new Rectangle(DefaultUISpecifications.SCREEN_WIDTH,DefaultUISpecifications.SCREEN_HEIGHT);
        bg.setFill(Color.LIGHTSKYBLUE);
        root.getChildren().add(bg);
    }

    @Override
    protected void nonTileMouseClicked(MapButton mb, MapElement a) {

    }

    @Override
    protected void creationSetup() { }

    protected void createGameAndTiles(ArrayList<Player> p) throws FileNotFoundException {
        game = new Game(map, p);
        createGameAndTiles();
    }

    public void updateResources(Player player) {
        resourceBoxes[0].update(player);
        resourceBoxes[1].update(player);
        resourceBoxes[2].update(player);
        resourceBoxes[3].update(player);
        resourceBoxes[4].update(player);
        turnOfPlayer.setText("Turn of player " + game.getCurrentPlayer().name);
    }

    public MapButton findMapButton(int x, int y) {
        for(MapButton button: mapButtonList) {
            if (x == button.x && y == button.y) {
                return button;
            }
        }
        return null;
    }

    public Game getGame() {
        return this.game;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Map getMap() {
        return this.map;
    }
}

