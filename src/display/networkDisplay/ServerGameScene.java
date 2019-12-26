package display.networkDisplay;

import display.EventPopUp;
import display.MapButton;
import javafx.stage.Stage;
import network.ServerConnection;
import game.Game;
import game.map.*;
import game.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ServerGameScene extends NetworkGameScene {
    Game game;

    public ServerGameScene(ArrayList<Player> p, Stage primaryStage, ServerConnection connection) throws IOException {
        super(primaryStage, connection);
        this.player = p.get(0);

        map = new Map();
        addBackground();
        createGameAndTiles(p);
        addPlayerResourcesMenu();
        displayDice(game.getDie1(), game.getDie2());
    }

    public void endTurnProcess() throws IOException {
        game.endTurn();
        updateResources(player);
        displayDice(game.getDie1(), game.getDie2());

        EventPopUp popUp = checkGameEvent(game);

        if(game.getCurrentPlayer().equals(player)) {
            endTurnButton.setDisable(false);
        }

        ((ServerConnection)connection).sendEndTurnInfo(game.getCurrentPlayer(), game.getDie1(), game.getDie2(), popUp);
        if( popUp != null )
            popUp.initPopUp(root,gameView);

    }

    public void endTurnProcess(CountDownLatch countDownLatch) throws IOException {
        endTurnProcess();
        countDownLatch.countDown();
    }

    @Override
    protected void setupButtons() {
        endTurnButton.setOnMouseClicked(e->{
            endTurnButton.setDisable(true);
            try {
                endTurnProcess();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @Override
    protected void tileMouseClicked(MapButton mb, MapTile a) {
        return;
    }

    @Override
    protected void nonTileMouseClicked(MapButton mb, MapElement a) {
        mb.setOnMouseClicked(e -> {
            if( game.getCurrentPlayer().equals(player)) {
                boolean built = game.build(a.getLocation());
                mb.update();
                updateResources(game.getCurrentPlayer());
                if(built) {
                    super.nonTileMouseClicked(mb, a);
                }
            }
        });
        mapButtonList.add(mb);
    }

    protected void createGameAndTiles(ArrayList<Player> p) throws FileNotFoundException {
        game = new Game(map, p);
        createGameAndTiles();
    }

    public void updateResources(Player player) {
        super.updateResources(player, game.getCurrentPlayer().name);
    }

    public Game getGame() {
        return this.game;
    }

    public Map getMap() {
        return this.map;
    }
}

