package Display;

import Game.Game;
import Game.Map.*;
import Game.Player.Player;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class GameScene{
    private Group root;
    private Scene scene;
    Map map;
    private double differenceX;
    private double differenceY;
    Font font;

    public GameScene() throws FileNotFoundException {
        map = new Map();
        root = new Group();
        Game game = new Game(map, new ArrayList<Player>(Arrays.asList(new Player(), new Player())));
        font = javafx.scene.text.Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), 30);

        differenceX = map.getTile(0,1).getLocation().getRawDisplayPosition().getX() - map.getTile(0,0).getLocation().getRawDisplayPosition().getX();
        differenceY = map.getTile(1,0).getLocation().getRawDisplayPosition().getY() - map.getTile(0,0).getLocation().getRawDisplayPosition().getY();

        map.getTileElements().forEach(a -> {
            double x = a.getLocation().getRawDisplayPosition().getX();
            double y = a.getLocation().getRawDisplayPosition().getY();
            try {
                setImage(a.getType(),x,y,differenceX,differenceY,root);
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
            });
            root.getChildren().add(mb);
        });

        map.getTileElements().forEach(a->{
            if(a.getNumber() == 0){
                System.out.println(":)");
            }else{
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



    public Scene getScene(){
        scene = new Scene(root);
        return scene;
    }

    private void setImage(MapTile.Types a, double x, double y, double differenceX, double differenceY, Group root) throws IOException {
        InputStream is;
        is = Files.newInputStream(Paths.get("res/images/brick.png"));
        switch (a.toString()) {
            case "BRICK":
                is = Files.newInputStream(Paths.get("res/images/brick.png"));
                break;
            case "MOUNTAIN":
                is = Files.newInputStream(Paths.get("res/images/mountain.png"));
                break;
            case "DESERT":
                is = Files.newInputStream(Paths.get("res/images/desert.png"));
                break;
            case "PASTURE":
                is = Files.newInputStream(Paths.get("res/images/pasture.png"));
                break;
            case "FIELD":
                is = Files.newInputStream(Paths.get("res/images/field.png"));
                break;
            case "FOREST":
                is = Files.newInputStream(Paths.get("res/images/forest.png"));
                break;
        }
        Image img = new Image(is);
        is.close();
        ImageView tile = new ImageView(img);
        tile.setPreserveRatio(true);
        tile.setFitWidth(differenceX);
        tile.setX(x);
        tile.setY(y - (differenceX / (2 * Math.sqrt(3))));
        tile.setOpacity(1);
        root.getChildren().add(tile);
        return;
    }

}
