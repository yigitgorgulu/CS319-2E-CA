import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main extends Application {
    protected static double SCREEN_WIDTH = 1000;
    protected static double SCREEN_HEIGHT = 1000;
    protected static Font flarge;
    private GameMenu gameMenu;
    private Text text;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        flarge = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), 120);
        createScreenDimensions(primaryStage);
        Pane root = new Pane();
        root.setPrefSize(SCREEN_WIDTH,SCREEN_HEIGHT);

        //Set an Image here
        gameMenu = new GameMenu();
        root.getChildren().addAll(getImgView(), createText(), gameMenu);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Node getImgView() throws IOException {
        InputStream is = Files.newInputStream(Paths.get("res/images/background.png"));
        Image img = new Image(is);
        is.close();

        return new ImageView(img);
    }

    private void createScreenDimensions(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        SCREEN_HEIGHT = primaryScreenBounds.getHeight();
        SCREEN_WIDTH = primaryScreenBounds.getWidth();
        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());
        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
    }

    private Node createText() {
        text = new Text("SETTLERS OF\nCATAN!");
        text.setFill(Color.WHITE);
        text.setFont(flarge);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle("-fx-line-spacing: 1px;");
        text.setTranslateX(SCREEN_WIDTH / 2 - text.getLayoutBounds().getWidth()/ 2);
        text.setTranslateY(SCREEN_HEIGHT / 3 - 50);
        return text;
    }
}
