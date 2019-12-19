package display.networkDisplay;

import javafx.scene.Group;
import javafx.scene.Scene;
import network.Connection;

public class LobbyScene {
    // properties
    protected Group root;
    protected Scene scene;
    Connection connection;

    // constructors
    public LobbyScene() {
        root = new Group();
    }

    // methods
    public Scene getScene(){
        scene = new Scene(root);
        return scene;
    }
}
