import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

import java.io.FileNotFoundException;

public class GameMenu extends Parent {
    public GameMenu() throws FileNotFoundException {
        VBox menu0 = new VBox(15);

        menu0.setTranslateX(Main.SCREEN_WIDTH / 2 - MenuButton.RECTANGLE_WIDTH / 2);
        menu0.setTranslateY(Main.SCREEN_HEIGHT / 2 + 50);
        //final int offset = 400;

        MenuButton btnCreateAGame = new MenuButton("Create A New Game");
        MenuButton btnJoinAGame = new MenuButton("Join an existing game");
        MenuButton btnProfile = new MenuButton( "Profile");
        MenuButton btnSettingsCredits = new MenuButton("Settings and Credits");
        MenuButton btnQuit = new MenuButton("Quit Game");
        btnQuit.setOnMouseClicked(e->System.exit(0));

        menu0.setAlignment(Pos.CENTER);
        menu0.getChildren().addAll(btnCreateAGame,btnJoinAGame,btnProfile,btnSettingsCredits,btnQuit);

        //Rectangle bg = new Rectangle(SCREEN_WIDTH,SCREEN_HEIGHT);
        //bg.setFill(Color.GRAY);
        //bg.setOpacity(0.3);
        getChildren().add(menu0);
    }
}
