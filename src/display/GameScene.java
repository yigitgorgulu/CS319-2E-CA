package display;

import game.map.*;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class GameScene {
    // properties
    public static double hexagon_short_diagonal_length;
    public static double hexagon_long_diagonal_length;
    public static double hexagon_edge_length;
    protected Group root;
    protected Scene scene;
    protected Map map;
    protected double differenceX;
    protected double differenceY;
    protected Font font;
    protected ResourceBox[] resourceBoxes;
    protected Rectangle separatorRectangle;
    protected Button endTurnButton;
    protected Button buyDevCardButton;
    protected Label turnOfPlayer;
    protected Die[] dice;
    protected HBox diceBox;

    // constructors
    public GameScene() throws IOException {
        root = new Group();
        resourceBoxes = new ResourceBox[5];
        dice = new Die[2];
    }

    // methods
    protected void displayDice(int dieNum1, int dieNum2) {
        if ( root.getChildren().contains(diceBox) )
            root.getChildren().remove(diceBox);
        dice[0] = new Die(dieNum1);
        dice[1] = new Die(dieNum2);
        diceBox = new HBox(dice[0], dice[1]);
        diceBox.setTranslateX(200);
        diceBox.setTranslateY(200);
        root.getChildren().add(diceBox);
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

        createPlayerResourceBoxes();
        separatorRectangle = new Rectangle(20,0);

        endTurnButton = new Button("End Turn");
        buyDevCardButton = new Button( "Buy Development Card");
        setupButtons();

        HBox hBox = new HBox();
        for ( ResourceBox rb : resourceBoxes )
            hBox.getChildren().add(rb);
        hBox.getChildren().addAll(separatorRectangle, endTurnButton, buyDevCardButton);

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

    protected abstract void createPlayerResourceBoxes() throws IOException;

    protected abstract void setupButtons();

    protected void addBackground() {
        Rectangle bg = new Rectangle(DefaultUISpecifications.SCREEN_WIDTH, DefaultUISpecifications.SCREEN_HEIGHT);
        bg.setFill(Color.LIGHTSKYBLUE);
        root.getChildren().add(bg);
    }

    protected void createGameAndTiles() throws FileNotFoundException {
        font = javafx.scene.text.Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), 30);
        differenceX = map.getTile(0,1).getLocation().getRawDisplayPosition().getX()
                - map.getTile(0,0).getLocation().getRawDisplayPosition().getX();

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
            nonTileMouseClicked(mb, a);
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

    protected void setImage(MapTile.Types a, double x, double y, Group root) throws IOException {
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

    protected abstract void nonTileMouseClicked(MapButton mb, MapElement a);

    public Scene getScene(){
        scene = new Scene(root);
        return scene;
    }
}
