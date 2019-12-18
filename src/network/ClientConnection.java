package network;

import display.networkDisplay.ClientGameScene;
import display.networkDisplay.ServerGameScene;
import network.requests.BuildRequest;
import network.requests.EndTurnInfo;
import network.requests.PlayerInfo;
import network.requests.Requests;
import game.map.Map;
import game.map.MapButton;
import game.player.Civilization;
import game.player.Player;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class ClientConnection extends Connection {
    String name;

    public ClientConnection(Stage gameView, String name) {
        super(gameView);
        this.name = name;
    }

    @Override
    public void send(Serializable data) throws Exception {
        os.writeObject(data);
    }

    @Override
    public void run() {

        System.out.println("Something Happened");

        try (Socket s = new Socket("localhost", 31923);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(s.getInputStream())
        ) {

            os = out;

            Player pl = new Player(Color.GREEN, Civilization.CivilizationEnum.SPAIN, name);
            PlayerInfo playerInfo = new PlayerInfo(pl);
            send(playerInfo);

            while (true) {

                try {
                    Serializable data = (Serializable) in.readObject();
                    System.out.println(data.toString());
                    if(data instanceof Map) {
                        Platform.runLater(() -> {
                            try {

                                ClientGameScene clientGameScene = new ClientGameScene(mapLatch, (Map)data, this, pl);
                                gameView.setScene(clientGameScene.getScene());
                                this.gameScene = clientGameScene;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });


                    }
                    else if(data instanceof BuildRequest) {
                        System.out.println("OKEY");

                        Platform.runLater(() ->{
                            MapButton mb = ((BuildRequest)data).mapButton;
                            MapButton mapB = ((ClientGameScene)gameScene).findMapButton(mb.x, mb.y);
                            mapB.clientUpdate(new Player(((BuildRequest)data).playerInfo));
                        });

                    }

                    else if(data instanceof EndTurnInfo) {
                        EndTurnInfo endTurnInfo = (EndTurnInfo) data;

                        Platform.runLater(() ->{
                            ((ClientGameScene)gameScene).displayDice(endTurnInfo.getDie1(), endTurnInfo.getDie2());

                            ((ClientGameScene)gameScene).updateResources(new Player(endTurnInfo.getPlayerInfo()),endTurnInfo.getCurrentPlayerInfo().name);
                            System.out.println(endTurnInfo.getCurrentPlayerInfo().name + "\n" +
                                    endTurnInfo.getPlayerInfo().name + "\n" +
                                    ((ClientGameScene)gameScene).getPlayer().name);
                            if(new Player(endTurnInfo.getCurrentPlayerInfo()).equals(((ClientGameScene)gameScene).getPlayer()))
                                ((ClientGameScene)gameScene).enableEndTurn();
                        });


                    }
                    else if(data.equals(Requests.ADDED)) {
                        System.out.println("SENDIN");
                        send(Requests.GAME_INIT);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problem");
        }
    }


}
