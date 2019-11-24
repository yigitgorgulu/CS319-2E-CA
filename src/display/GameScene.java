package display;

import game.Game;
import game.map.*;
import game.player.Civilization;
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
import java.util.Arrays;

public class GameScene{
    public static double hexagon_short_diagonal_length;
    public static double hexagon_long_diagonal_length;
    public static double hexagon_edge_length;
    private Group root;
    private Scene scene;
    Game game;
    Map map;
    private double differenceX;
    private double differenceY;
    Font font;
    Player player1;
    Player player2;
    Player player3;
    ResourceBox box1;
    ResourceBox box2;
    ResourceBox box3;
    ResourceBox box4;
    ResourceBox box5;
    Rectangle seperatorRectangle;
    Button endTurn;
    Label turnOfPlayer;
    Die die1;
    Die die2;
    HBox diceBox;

    public GameScene() throws IOException {
        map = new Map();
        root = new Group();
        addBackground();
        createGameAndTiles();
        addPlayerResourcesMenu();
        dice();
    }

    private void dice() {
        if ( game.gameTurns != 0 ) root.getChildren().remove(diceBox);
        die1 = new Die(game.getDie1());
        die2 = new Die(game.getDie2());

        diceBox = new HBox(die1, die2);
        diceBox.setTranslateX(200);
        diceBox.setTranslateY(200);
        root.getChildren().add(diceBox);
    }

    private void addPlayerResourcesMenu() throws IOException {
        double widthOfRectangle = DefaultUISpecifications.SCREEN_WIDTH / 3;
        double heightOfRectangle = DefaultUISpecifications.SCREEN_HEIGHT / 7;
        double leftUpperCornerOfTheRectangleX = DefaultUISpecifications.SCREEN_WIDTH / 2 - widthOfRectangle / 2;
        double leftUpperCornerOfTheRectangleY = DefaultUISpecifications.SCREEN_HEIGHT - heightOfRectangle;
        Rectangle resourcesBackground = new Rectangle(widthOfRectangle, heightOfRectangle);
        resourcesBackground.setTranslateX(leftUpperCornerOfTheRectangleX);
        resourcesBackground.setTranslateY(leftUpperCornerOfTheRectangleY - 20);
        resourcesBackground.setFill(Color.WHITESMOKE);
        root.getChildren().add(resourcesBackground);

        box1 = new ResourceBox(player1, "BRICK");
        box2 = new ResourceBox(player1, "WOOD");
        box3 = new ResourceBox(player1, "SHEEP");
        box4 = new ResourceBox(player1, "WHEAT");
        box5 = new ResourceBox(player1, "ORE");

        seperatorRectangle = new Rectangle(20,0);

        endTurn = new Button("End Turn");
        endTurn.setOnAction(e->{
            game.endTurn();
            updateResources(game.getCurrentPlayer());
            dice();
        });

        HBox hBox = new HBox();

        hBox.getChildren().addAll(box1,box2,box3,box4,box5, seperatorRectangle,endTurn);
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

    private void addBackground(){
    Rectangle bg = new Rectangle(DefaultUISpecifications.SCREEN_WIDTH,DefaultUISpecifications.SCREEN_HEIGHT);
        bg.setFill(Color.LIGHTSKYBLUE);
        root.getChildren().add(bg);
    }

    private void createGameAndTiles() throws FileNotFoundException {
        player1 = new Player(Color.RED, Civilization.CivilizationEnum.OTTOMANS, "Player 1");
        player2 = new Player(Color.GREEN, Civilization.CivilizationEnum.SPAIN, "Player 2");
        player3 = new Player(Color.BLUE, Civilization.CivilizationEnum.GB, "Player 3");
        game = new Game(map, new ArrayList<Player>(Arrays.asList(player1, player2, player3)));

        font = javafx.scene.text.Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), 30);
        differenceX = map.getTile(0,1).getLocation().getRawDisplayPosition().getX() - map.getTile(0,0)
                .getLocation().getRawDisplayPosition().getX();

        hexagon_edge_length = (differenceX / Math.sqrt(3));
        hexagon_short_diagonal_length = differenceX;
        hexagon_long_diagonal_length = hexagon_edge_length * 2;

        map.getTileElements().forEach(a -> {
            double x = a.getLocation().getRawDisplayPosition().getX();
            double y = a.getLocation().getRawDisplayPosition().getY();
            try {
                setImage(a.getType(),x,y,root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        map.getNonTileElements().forEach(a -> {
            double x = a.getLocation().getRawDisplayPosition().getX();
            double y = a.getLocation().getRawDisplayPosition().getY();
            double r = 0.0;
            if (a instanceof MapCorner)
                r = 13.0;
            if (a instanceof MapSide)
                r = 11.0;
            MapButton mb = new MapButton(x, y, r, a);
            mb.setFill(Color.GRAY);
            mb.setOpacity(0.0);
            mb.setOnMouseClicked(e -> {
                game.build(a.getLocation());
                mb.update();
                updateResources(game.getCurrentPlayer());
            });
            root.getChildren().add(mb);
        });

        map.getTileElements().forEach(a->{
            if(a.getNumber() > 0) {
                double radius = 35;
                double x = a.getLocation().getRawDisplayPosition().getX();
                double y = a.getLocation().getRawDisplayPosition().getY();
                x = x + differenceX / 2;
                y = y + (differenceX / (2 * Math.sqrt(3)));
                MapToken nextToken = null;
                try {
                    nextToken = new MapToken(radius, x, y, a.getNumber());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                VBox vBox = (VBox) nextToken.getVBox();
                vBox.setTranslateX(x - radius);
                vBox.setPrefWidth(radius * 2);
                vBox.setPrefHeight(radius* 2);
                vBox.setTranslateY(y - radius);
                root.getChildren().addAll(nextToken, vBox);
            }
        });
    }

    private void updateResources(Player player) {
        box1.update(player);
        box2.update(player);
        box3.update(player);
        box4.update(player);
        box5.update(player);
        turnOfPlayer.setText("Turn of player " + game.getCurrentPlayerNo());
    }


    public Scene getScene(){
        scene = new Scene(root);
        return scene;
    }

    private void setImage(MapTile.Types a, double x, double y, Group root) throws IOException {
        InputStream is;
        is = Files.newInputStream(Paths.get("res/images/tiles/brick.png"));
        switch (a.toString()) {
            case "BRICK":
                is = Files.newInputStream(Paths.get("res/images/tiles/brick.png"));
                break;
            case "MOUNTAIN":
                is = Files.newInputStream(Paths.get("res/images/tiles/mountain.png"));
                break;
            case "DESERT":
                is = Files.newInputStream(Paths.get("res/images/tiles/desert.png"));
                break;
            case "PASTURE":
                is = Files.newInputStream(Paths.get("res/images/tiles/pasture.png"));
                break;
            case "FIELD":
                is = Files.newInputStream(Paths.get("res/images/tiles/field.png"));
                break;
            case "FOREST":
                is = Files.newInputStream(Paths.get("res/images/tiles/forest.png"));
                break;
        }
        Image img = new Image(is);
        is.close();
        ImageView tile = new ImageView(img);
        tile.setPreserveRatio(true);
        tile.setFitWidth(differenceX);
        tile.setX(x);
        tile.setY(y - hexagon_edge_length / 2);
        tile.setOpacity(1);
        root.getChildren().add(tile);
    }

}
