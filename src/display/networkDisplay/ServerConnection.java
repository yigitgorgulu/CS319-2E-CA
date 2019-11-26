package display.networkDisplay;

import display.networkDisplay.requests.BuildRequest;
import display.networkDisplay.requests.PlayerInfo;
import display.networkDisplay.requests.Requests;
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
import java.util.concurrent.CountDownLatch;

public class ServerConnection extends Thread {
    final int PLAYER_COUNT = 2;
    Client[] clients;

    ObjectOutputStream os;
    ObjectInputStream is;

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
        this.gameView = gameView;
        players = new ArrayList<>();
        System.out.println("ADDED OTTOMANS");
        players.add(new Player(Color.RED, Civilization.CivilizationEnum.OTTOMANS, "Player 1"));
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
                    Serializable data = (Serializable) client.in.readObject();
                    if(data.equals(Requests.GAME_INIT)) {
                        initRequestCount++;
                    }
                    if(initRequestCount == PLAYER_COUNT - 1) {

                        Platform.runLater(() -> {
                            try {
                                //
                                //player3 = new Player(Color.BLUE, Civilization.CivilizationEnum.GB, "Player 3");

                                System.out.println("Players:");
                                for (Player plrr: players) {
                                    System.out.println(plrr.name);
                                }
                                System.out.println("---------");
                                ServerGameScene serverGameScene = new ServerGameScene(players, this);

                                gameView.setScene(serverGameScene.getScene());
                                this.serverGameScene = serverGameScene;
                                mapCount.countDown();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });

                        mapCount.await();
                        for(Client cls : clients) {
                            cls.os.writeObject(serverGameScene.map);
                        }
                        initRequestCount--;
                    }

                    else if(data instanceof PlayerInfo) {
                        players.add(new Player((PlayerInfo)data));
                        System.out.println("One player Added");
                        client.os.writeObject(Requests.ADDED);
                    }

                    else if(data instanceof BuildRequest) {
                        MapElement a = ((BuildRequest)data).element;
                        MapButton mb = ((BuildRequest)data).mapButton;
                        Player ply = new Player(((BuildRequest)data).playerInfo);

                        System.out.println("RECEIVED BUILD REQUEST BY:");
                        System.out.println(ply.name);

                        if(serverGameScene.game.getCurrentPlayer().equals(ply)) {
                            boolean built = serverGameScene.game.build(a.getLocation());

                            MapButton mapB = serverGameScene.findMapButton(mb.x, mb.y);
                            mapB.update();
                            serverGameScene.updateResources(serverGameScene.game.getCurrentPlayer());

                            if (built) {
                                sendEveryone(data);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
