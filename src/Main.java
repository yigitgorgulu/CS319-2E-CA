import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
        map.getAllCorners().forEach(a -> {
            MapButton mb = new MapButton(a);
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
