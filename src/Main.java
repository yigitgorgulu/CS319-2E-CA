import Game.Game;
import Game.Map.Map;
import Game.Map.MapButton;
import Game.Map.MapCorner;
import Game.Map.MapSide;
import Game.Player.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map map = new Map();
        Game game = new Game(map, new ArrayList<Player>( Arrays.asList( new Player(), new Player() )));
        Group root = new Group();
        map.getAllElements().forEach(a -> {
            double x = a.getLocation().getRawDisplayPosition().getX() * 2.0 + 80.0;
            double y = a.getLocation().getRawDisplayPosition().getY() * 2.0 + 60.0;
            double r = 0.0;
            if ( a instanceof MapCorner)
                r = 8.0;
            if ( a instanceof MapSide)
                r = 6.0;
            MapButton mb = new MapButton(x, y, r, a);
            mb.setOnMouseClicked(e -> {
                game.build( a.getLocation() );
                mb.update();
            });
            root.getChildren().add(mb);
        });
        Scene s = new Scene(root, 500 ,500);
        primaryStage.setScene(s);
        primaryStage.show();
    }



}
