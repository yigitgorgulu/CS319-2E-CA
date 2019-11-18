package display;

import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;

public class MainMenu extends Parent {
    public MainMenu( Stage gameView, Scene gameScene) throws FileNotFoundException {
        VBox menu0 = new VBox(15);

        menu0.setTranslateX(DefaultUISpecifications.SCREEN_WIDTH / 2 - MenuButton.RECTANGLE_WIDTH / 2);
        menu0.setTranslateY(DefaultUISpecifications.SCREEN_HEIGHT / 2 + 50);
        //final int offset = 400;

        MenuButton btnCreateAGame = new MenuButton("Create A New Game");
        MenuButton btnJoinAGame = new MenuButton("Join an existing game");
        MenuButton btnProfile = new MenuButton( "Profile");
        MenuButton btnSettingsCredits = new MenuButton("Settings and Credits");
        MenuButton btnQuit = new MenuButton("Quit Game");
        btnQuit.setOnMouseClicked(e->System.exit(0));
        btnCreateAGame.setOnMouseClicked(e->gameView.setScene(gameScene));
        menu0.setAlignment(Pos.CENTER);
        menu0.getChildren().addAll(btnCreateAGame,btnJoinAGame,btnProfile,btnSettingsCredits,btnQuit);

        //Rectangle bg = new Rectangle(SCREEN_WIDTH,SCREEN_HEIGHT);
        //bg.setFill(Color.GRAY);
        //bg.setOpacity(0.3);
        getChildren().add(menu0);
    }
}
