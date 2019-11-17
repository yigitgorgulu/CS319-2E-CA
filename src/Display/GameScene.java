package Display;

import Game.Game;
import Game.Map.*;
import Game.Player.Player;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

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
    ResourceBox box1;
    ResourceBox box2;
    ResourceBox box3;
    ResourceBox box4;
    ResourceBox box5;
    Button endTurn;

    public GameScene() throws IOException {
        map = new Map();
        root = new Group();
        addBackground();
        createGameAndTiles();
        addPlayerResourcesMenu();
        endTurnButton();
    }

    private void endTurnButton() {
        endTurn = new Button("End Button");
        endTurn.setOnAction(e->{
            System.out.println("SONAT ROCKS!");
            game.endTurn();
        });
        root.getChildren().add(endTurn);
    }

    private void addPlayerResourcesMenu() throws IOException {
        double widthOfRectangle = DefaultUISpecifications.SCREEN_WIDTH / 3;
        double heightOfRectangle = DefaultUISpecifications.SCREEN_HEIGHT / 7;
        double leftUpperCornerOfTheRectangleX = DefaultUISpecifications.SCREEN_WIDTH / 2 - widthOfRectangle / 2;
        double leftUpperCornerOfTheRectangleY = DefaultUISpecifications.SCREEN_HEIGHT - heightOfRectangle;
        Rectangle resourcesBackground = new Rectangle(widthOfRectangle, heightOfRectangle);
        resourcesBackground.setTranslateX(leftUpperCornerOfTheRectangleX);
        resourcesBackground.setTranslateY(leftUpperCornerOfTheRectangleY);
        resourcesBackground.setFill(Color.WHITESMOKE);
        root.getChildren().add(resourcesBackground);

        box1 = new ResourceBox(player1, "BRICK");
        box2 = new ResourceBox(player1, "WOOD");
        box3 = new ResourceBox(player1, "SHEEP");
        box4 = new ResourceBox(player1, "WHEAT");
        box5 = new ResourceBox(player1, "ORE");

        HBox hBox = new HBox();
        hBox.getChildren().addAll(box1,box2,box3,box4,box5);
        hBox.setTranslateX(leftUpperCornerOfTheRectangleX);
        hBox.setTranslateY(leftUpperCornerOfTheRectangleY);

        hBox.setPadding(new Insets(4,4,4,4));
        root.getChildren().add(hBox);
    }

    private void addBackground(){
    Rectangle bg = new Rectangle(DefaultUISpecifications.SCREEN_WIDTH,DefaultUISpecifications.SCREEN_HEIGHT);
        bg.setFill(Color.LIGHTSKYBLUE);
        root.getChildren().add(bg);
    }

    private void createGameAndTiles() throws FileNotFoundException {
        player1 = new Player();
        player2 = new Player();
        game = new Game(map, new ArrayList<Player>(Arrays.asList(player1, player2)));

        font = javafx.scene.text.Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), 30);
        differenceX = map.getTile(0,1).getLocation().getRawDisplayPosition().getX() - map.getTile(0,0).getLocation().getRawDisplayPosition().getX();

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
            //System.out.println("notSure               "+x + "\n");
            if (a instanceof MapCorner)
                r = 13.0;
            if (a instanceof MapSide)
                r = 11.0;
            MapButton mb = new MapButton(x, y, r, a);
            mb.setOpacity(0.4);
            mb.setFill(Color.GRAY);
            mb.setOnMouseClicked(e -> {
                game.build(a.getLocation());
                mb.update();
                updateResources();
            });
            root.getChildren().add(mb);
        });

        map.getTileElements().forEach(a->{
            if(a.getNumber() > 0){
                double radius = 25;
                double x = a.getLocation().getRawDisplayPosition().getX();
                double y = a.getLocation().getRawDisplayPosition().getY();
                x = x + differenceX / 2;
                y = y + (differenceX / (2 * Math.sqrt(3)));
                Circle circle = new Circle(x, y, radius);
                circle.setOpacity(0.7);
                Text number = new Text(Integer.toString(a.getNumber()));
                number.setFill(Color.BLACK);
                number.setTranslateX(x - 15);
                number.setTranslateY(y);
                number.setFont(font);
                circle.setFill(Color.WHITESMOKE);
                root.getChildren().addAll(circle, number);
            }
        });
    }

    private void updateResources() {
        box1.update();
        box2.update();
        box3.update();
        box4.update();
        box5.update();
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
        return;
    }

}
