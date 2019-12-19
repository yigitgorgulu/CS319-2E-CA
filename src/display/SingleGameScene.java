package display;

import game.Game;
import game.map.*;
import game.player.Civilization;
import game.player.Player;
import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class SingleGameScene extends GameScene {
    final int NUMBER_OF_PLAYERS = 3;
    Game game;
    Player[] players;

    public SingleGameScene() throws IOException {
        super();
        map = new Map();
        players = new Player[NUMBER_OF_PLAYERS];

        addBackground();
        createGameAndTiles();
        addPlayerResourcesMenu();
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
    protected void setupEndTurnButton() {
        endTurnButton.setOnAction(e -> {
            game.endTurn();
            updateResources(game.getCurrentPlayer());
            displayDice(game.getDie1(), game.getDie2());
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
        players[0] = new Player(Color.RED, Civilization.CivilizationEnum.OTTOMANS, "Player 1");
        players[1] = new Player(Color.GREEN, Civilization.CivilizationEnum.SPAIN, "Player 2");
        players[2] = new Player(Color.BLUE, Civilization.CivilizationEnum.ENGLAND, "Player 3");
        game = new Game(map, new ArrayList<Player>(Arrays.asList(players)));
        super.createGameAndTiles();
    }

    private void updateResources(Player player) {
        for ( int i = 0; i < resourceBoxes.length; i++ ) {
            resourceBoxes[i].update(player);
        }
        turnOfPlayer.setText("Turn of player " + game.getCurrentPlayerNo());
    }
}
