package display.networkDisplay;

import network.ServerConnection;
import game.Game;
import game.map.*;
import game.player.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class ServerGameScene extends NetworkGameScene {
    Game game;

    public ServerGameScene(ArrayList<Player> p, ServerConnection connection) throws IOException {
        super(connection);
        this.player = p.get(0);
        System.out.println("SERVER PLAYER COLOR:" + player.getColor().toString());
        map = new Map();
        addBackground();
        createGameAndTiles(p);
        addPlayerResourcesMenu();
        displayDice(game.getDie1(), game.getDie2());
    }

    public void endTurnProcess() {
        game.endTurn();
        updateResources(player);
        displayDice(game.getDie1(), game.getDie2());

        System.out.println("end turn done");
        System.out.println(game.getCurrentPlayer().name + "\n" + player.name);

        if(game.getCurrentPlayer().equals(player)) {
            endTurnButton.setDisable(false);
        }

        ((ServerConnection)connection).sendEndTurnInfo(game.getCurrentPlayer(), game.getDie1(), game.getDie2());

    }

    public void endTurnProcess(CountDownLatch countDownLatch) {
        endTurnProcess();
        countDownLatch.countDown();
    }

    @Override
    protected void setupButtons() {
        endTurnButton.setOnAction(e->{
            endTurnProcess();
            endTurnButton.setDisable(true);
        });
    }

    @Override
    protected void nonTileMouseClicked(MapButton mb, MapElement a) {
        mb.setOnMouseClicked(e -> {
            System.out.println(player.name);
            System.out.println(game.getCurrentPlayer().name);
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

