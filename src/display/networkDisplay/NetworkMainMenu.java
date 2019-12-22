package display.networkDisplay;

import display.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class NetworkMainMenu extends Parent {
    private Scene root;
    private Pane paneMainMenu;
    public NetworkMainMenu(Stage gameView, Pane pane) throws IOException {
        this.paneMainMenu = pane;
        VBox menu0 = new VBox(15);
        menu0.setTranslateX(DefaultUISpecifications.SCREEN_WIDTH / 2 - MenuButton.RECTANGLE_WIDTH / 2);
        menu0.setTranslateY(DefaultUISpecifications.SCREEN_HEIGHT / 2 + 50);
        //final int offset = 400;

        //Create buttons
        MenuButton btnCreateOfflineGame = new MenuButton( "Create an Offline Game" ,paneMainMenu);
        MenuButton btnCreateAGame = new MenuButton("Create an Online Game" ,paneMainMenu);
        MenuButton btnJoinAGame = new MenuButton("Join an existing game" ,paneMainMenu);
        MenuButton btnSettings = new MenuButton("Settings",paneMainMenu);
        MenuButton btnCredits = new MenuButton("Credits" ,paneMainMenu);
        MenuButton btnQuit = new MenuButton("Quit Game" ,paneMainMenu);
        btnQuit.setOnMouseClicked(e->System.exit(0));

        /*btnCreateAGame.setOnMouseClicked(e->gameView.setScene(gameScene));*/

        SingleGameScene singleGameScene = new SingleGameScene();

        btnCreateOfflineGame.setOnMouseClicked(e-> {
            gameView.setScene(singleGameScene.getScene());
        });

        btnCreateAGame.setOnMouseClicked(e-> {
            try {
                ServerLobbyScene serverLobbyScene = new ServerLobbyScene(gameView, root);
                gameView.setScene(serverLobbyScene.getScene());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        btnJoinAGame.setOnMouseClicked(e-> {
            try {
                new PopUp("ASK_IP", paneMainMenu ,gameView, null, null);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        btnSettings.setOnMouseClicked(e->{
            try {
                new PopUp("SETTINGS", paneMainMenu, gameView, null,null);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        btnCredits.setOnMouseClicked(e->{
            try {
                Credits settingsAndCredits = new Credits(gameView,root);
            } catch (IOException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        menu0.setAlignment(Pos.CENTER);
        menu0.getChildren().addAll(btnCreateOfflineGame,btnCreateAGame,btnJoinAGame,btnSettings,btnCredits,btnQuit);

        //Rectangle bg = new Rectangle(SCREEN_WIDTH,SCREEN_HEIGHT);
        //bg.setFill(Color.GRAY);
        //bg.setOpacity(0.3);
        getChildren().add(menu0);
    }

    public void setScene(Scene root) {
        this.root = root;
    }
}
