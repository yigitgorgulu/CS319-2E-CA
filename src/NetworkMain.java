import display.DefaultUISpecifications;
import display.networkDisplay.NetworkMainMenu;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import game.Sound;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NetworkMain extends Application {
    private static Font flarge;
    private NetworkMainMenu networkMainMenu;
    private Text text;

    public static void main(String[] args) {launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //primaryStage.setFullScreen(true);
        DefaultUISpecifications specifications = new DefaultUISpecifications();
        specifications.setScreenDimensions(primaryStage);
        Pane root = new Pane();
        root.setPrefSize(DefaultUISpecifications.SCREEN_WIDTH,DefaultUISpecifications.SCREEN_HEIGHT);

        //Set an Image here
        networkMainMenu = new NetworkMainMenu(primaryStage,root);
        root.getChildren().addAll(getImgView(), createText(), networkMainMenu);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        Sound.gymnopedie(root);
        networkMainMenu.setScene(scene);
        primaryStage.show();
    }

    public Node getImgView() throws IOException {
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
