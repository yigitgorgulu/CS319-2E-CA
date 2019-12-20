package display.networkDisplay;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import network.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class LobbyScene {
    // properties
    protected StackPane root;
    protected Scene scene;
    Connection connection;

    // constructors
    public LobbyScene() throws IOException {
        root = new StackPane();
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(getImgView());
    }

    // methods
    public Scene getScene(){
        scene = new Scene(root);
        return scene;
    }

    private Node getImgView() throws IOException {
        InputStream is = Files.newInputStream(Paths.get("res/images/MainMenu/background.png"));
        Image img = new Image(is);
        is.close();

        return new ImageView(img);
    }
}
