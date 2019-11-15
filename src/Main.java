import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map map = new Map();
        Group root = new Group();
        map.getAllCorners().forEach(a -> root.getChildren().add(a));
        Scene s = new Scene(root, 500 ,500);
        primaryStage.setScene(s);
        primaryStage.show();
    }



}
