package network;

import display.networkDisplay.ServerGameScene;
import network.Client;
import network.requests.BuildRequest;
import network.requests.EndTurnInfo;
import network.requests.PlayerInfo;
import network.requests.Requests;
import game.map.MapButton;
import game.map.MapElement;
import game.player.Civilization;
import game.player.Player;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ServerConnection extends Thread {
    private final int PLAYER_COUNT = 3;
    Client[] clients;

    ObjectOutputStream os;
    ObjectInputStream is;
    boolean gameInit = false;
    List<Serializable> requests;
    ArrayList<Player> players;
    ServerSocket ss;
    Socket s;
    Stage gameView;
    ServerGameScene serverGameScene;

    static int initRequestCount = 0;
    CountDownLatch mapCount;

    public ServerConnection(Stage gameView) {
        super();
        clients = new Client[PLAYER_COUNT - 1];

        requests = Collections.synchronizedList(new ArrayList<Serializable>());
        this.gameView = gameView;
        players = new ArrayList<>();
        System.out.println("ADDED OTTOMANS");
        players.add(new Player(Color.RED, Civilization.CivilizationEnum.OTTOMANS, "server"));
        System.out.println(players.get(0).name);
    }

    public void sendEveryone(Serializable data) throws Exception {
        for (Client clss: clients) {
            clss.os.writeObject(data);
        }
    }

    @Override
    public void run() {
        mapCount = new CountDownLatch(1);
        try {
            ss = new ServerSocket(19999);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int playerCount = 0;
        while (playerCount < PLAYER_COUNT - 1) {

            try {

                Socket s = ss.accept();
                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(s.getInputStream());

                System.out.println("Someting Happened");

                clients[playerCount] = new Client(s, out, in);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Problem");
            }
            playerCount++;
        }


        while (true) {
            for (Client client : clients) {
                try {
                    Serializable data;
                    System.out.println("AWAITING");
                    if(!gameInit || client.player.equals(serverGameScene.getGame().getCurrentPlayer())) {
                         data = (Serializable) client.in.readObject();
                    }
                    else {
                        continue;
                    }

                    if(data.equals(Requests.GAME_INIT)) {
                        initRequestCount++;
                    }
                    if(initRequestCount == PLAYER_COUNT - 1) {

                        Platform.runLater(() -> {
                            try {

                                System.out.println("Players:");
                                for (Player plrr: players) {
                                    System.out.println(plrr.name);
                                }
                                System.out.println("---------");
                                ServerGameScene serverGameScene = new ServerGameScene(players, this);

                                gameView.setScene(serverGameScene.getScene());
                                this.serverGameScene = serverGameScene;
                                mapCount.countDown();
                                gameInit = true;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        mapCount.await();
                        for(Client cls : clients) {
                            cls.os.writeObject(serverGameScene.getMap());
                        }
                        initRequestCount--;
                    }

                    else if(data instanceof PlayerInfo) {
                        Player plrrx = new Player((PlayerInfo)data);
                        players.add(plrrx);
                        System.out.println("One player Added");
                        client.os.writeObject(Requests.ADDED);
                        client.setPlayer(plrrx);
                    }

                    else if(data.equals(Requests.END_TURN)) {
                        System.out.println("RECEIVED END TURN REQUEST:");
                        Platform.runLater(() -> serverGameScene.endTurnProcess());
                    }

                    else if(data instanceof BuildRequest) {
                        MapElement a = ((BuildRequest)data).element;
                        MapButton mb = ((BuildRequest)data).mapButton;
                        Player ply = new Player(((BuildRequest)data).playerInfo);

                        System.out.println("RECEIVED BUILD REQUEST BY:");
                        System.out.println(ply.name);

                        if(serverGameScene.getGame().getCurrentPlayer().equals(ply)) {
                            boolean built = serverGameScene.getGame().build(a.getLocation());

                            MapButton mapB = serverGameScene.findMapButton(mb.x, mb.y);
                            mapB.update();
                            serverGameScene.updateResources(serverGameScene.getPlayer());

                            if (built) {
                                sendEveryone(data);
                            }
                        }
                        System.out.println("DONE BUILD");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void sendEndTurnInfo(Player currentPlayer, int die1, int die2) {

        try {
            for (Client client : clients) {
                EndTurnInfo endTurnInfo = new EndTurnInfo(new PlayerInfo(currentPlayer),
                        new PlayerInfo(client.player), die1, die2);

                client.os.writeObject(endTurnInfo);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
