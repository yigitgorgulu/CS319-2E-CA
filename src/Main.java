import display.DefaultUISpecifications;
import display.GameScene;
import display.MainMenu;
import sound.Sound;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.scene.image.Image;

public class Main extends Application {
    private static Font flarge;
    private MainMenu gameMenu;
    private Text text;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        DefaultUISpecifications specifications = new DefaultUISpecifications();
        specifications.setScreenDimensions(primaryStage);
        GameScene gameScene = new GameScene();
        Pane root = new Pane();
        root.setPrefSize(DefaultUISpecifications.SCREEN_WIDTH,DefaultUISpecifications.SCREEN_HEIGHT);

        //Set an Image here
        gameMenu = new MainMenu(primaryStage, gameScene.getScene());
        root.getChildren().addAll(getImgView(), createText(), gameMenu);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        new Sound();
        primaryStage.show();
    }

    private Node getImgView() throws IOException {
        InputStream is = Files.newInputStream(Paths.get("res/images/MainMenu/background.png"));
        Image img = new Image(is);
        is.close();

        return new ImageView(img);
    }

    private Node createText() throws FileNotFoundException {
        flarge = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), 120);
        text = new Text("SETTLERS OF\nCATAN!");
        text.setFill(Color.WHITE);
        text.setFont(flarge);
        text.setTextAlignment(TextAlignment.CENTER);
        text.setStyle("-fx-line-spacing: 1px;");
        text.setTranslateX(DefaultUISpecifications.SCREEN_WIDTH / 2 - text.getLayoutBounds().getWidth()/ 2);
        text.setTranslateY(DefaultUISpecifications.SCREEN_HEIGHT / 3 - 50);
        return text;
    }
}
