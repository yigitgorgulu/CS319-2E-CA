package display;

import game.Game;
import game.map.*;
import game.player.Civilization;
import game.player.DevelopmentCards;
import game.player.Player;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SingleGameScene extends GameScene {
    int numberOfPlayers;
    Game game;
    Player[] players;

    public SingleGameScene(Stage primaryStage, Player[] players, int numberOfPlayers) throws IOException, InterruptedException {
        super(primaryStage);
        map = new Map();
        this.numberOfPlayers = numberOfPlayers;
        System.out.println(numberOfPlayers);

        this.players = players;
        addBackground();
        createGameAndTiles();
        addPlayerResourcesMenu();
        createDevCardList();
        createPlayerList();
        updatePlayerList();
        displayDice(game.getDie1(), game.getDie2());
    }

    @Override
    protected void createPlayerResourceBoxes() throws IOException {
        resourceBoxes[0] = new ResourceBox(players[0], "BRICK");
        resourceBoxes[1] = new ResourceBox(players[0], "WOOD");
        resourceBoxes[2] = new ResourceBox(players[0], "SHEEP");
        resourceBoxes[3] = new ResourceBox(players[0], "WHEAT");
        resourceBoxes[4] = new ResourceBox(players[0], "ORE");
    }


    @Override
    protected void setupButtons() {
        endTurnButton.setOnMouseClicked(e -> {
            game.endTurn();
            updateResources(game.getCurrentPlayer());
            updateDevCards(game.getCurrentPlayer());
            displayDice(game.getDie1(), game.getDie2());
            updatePlayerList();
            EventPopUp popUp = checkGameEvent(game);
            if( popUp != null ) {
                try {
                    popUp.initPopUp(gameView);
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                }
            }
        });
        buyDevCardButton.setOnMouseClicked(e -> {
            game.buyDevelopmentCard();
            updateResources(game.getCurrentPlayer());
            updateDevCards(game.getCurrentPlayer());
        });
    }

    protected void nonTileMouseClicked(MapButton mb, MapElement a) {
        mb.setOnMouseClicked(e -> {
            game.build(a.getLocation());
            mb.update();
            updateResources(game.getCurrentPlayer());
        });
    }

    @Override
    protected void createGameAndTiles() throws FileNotFoundException {
        game = new Game(map, new ArrayList<Player>(Arrays.asList(players)));
        super.createGameAndTiles();
    }

    @Override
    protected void tileMouseClicked(MapButton mb, MapTile a) {
        mb.setOnMouseClicked(e->{
            game.moveRobber(a.getLocation());
        });
    }

    private void updateResources(Player player) {
        for ( int i = 0; i < resourceBoxes.length; i++ ) {
            resourceBoxes[i].update(player);
        }
        turnOfPlayer.setText("Turn of " + game.getCurrentPlayer().name);
        turnOfPlayer.setTextFill(game.getCurrentPlayer().getColor());
    }


    protected void updatePlayerList() {
        for(int i = 0; i < numberOfPlayers; i++){
            VBox a = new VBox(3);
            Text nameOfThePlayer = new Text(players[i].name);
            Text civOfThePlayer = new Text(players[i].getCivilizationType().name());
            a.getChildren().addAll(nameOfThePlayer,civOfThePlayer);
            playerList.getItems().add(a);
        }
    }

    private void updateDevCards(Player player) {
        ObservableList<String> devCardNames = FXCollections.observableArrayList();
        ObservableList<Button> devCardButtons = FXCollections.observableArrayList();
        List<DevelopmentCards> devCards = player.getDevelopmentCards();
        for( DevelopmentCards d : devCards ) {
            Button button = new Button(""+d);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    game.playDevelopmentCard(d);
                    updateDevCards(player);
                    updatePlayerList();
                }
            });
            devCardButtons.add(button);
        }
        devCardList.setItems(devCardButtons);
    }
}
