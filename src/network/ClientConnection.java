package network;

import display.CivilizationSelectionScene;
import display.networkDisplay.ClientGameScene;
import javafx.beans.property.SimpleBooleanProperty;
import network.requests.BuildRequest;
import network.requests.EndTurnInfo;
import network.requests.PlayerInfo;
import network.requests.Requests;
import game.map.Map;
import display.MapButton;
import game.player.Civilization;
import game.player.Player;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

public class ClientConnection extends Connection {
    String name;
    String IPAddress;
    SimpleBooleanProperty connectionFailed;
    Color color;
    Civilization.CivType civType;

    public ClientConnection(Stage gameView, String name, String IPAddress, SimpleBooleanProperty connectionFailed) {
        super(gameView);
        this.name = name;
        this.IPAddress = IPAddress;
        this.connectionFailed = connectionFailed;
    }

    @Override
    public void send(Serializable data) throws Exception {
        os.writeObject(data);
    }

    @Override
    public void run() {

        System.out.println("Something Happened");

        try (Socket s = new Socket(IPAddress, 31923);
             ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(s.getInputStream())
        ) {

            os = out;

            Player px = null;
            boolean playerAccepted = false;
            while(!playerAccepted) {
                final CivilizationSelectionScene[] civSelection = new CivilizationSelectionScene[1];
                CountDownLatch cc = new CountDownLatch(1);
                Platform.runLater(new Runnable() {
                    @Override public void run() {
                        try {
                            closePopUp();
                            civSelection[0] = new CivilizationSelectionScene(gameView,cc, 0, "MULTIPLAYER");
                        } catch (IOException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                cc.await();
                name = civSelection[0].getName();
                System.out.println(name);
                color = civSelection[0].getColor();
                civType = civSelection[0].getCivType();

                px = new Player(color, civType, name);
                PlayerInfo playerInfo = new PlayerInfo(px);
                send(playerInfo);

                System.out.println("YAAAY");
                Requests playerAcceptedResult = (Requests) in.readObject();
                if(playerAcceptedResult.equals(Requests.PLAYER_OK)) {
                    send(Requests.GAME_INIT);
                    playerAccepted = true;
                }
            }
            Player pl = px;


            while (true) {

                try {
                    Serializable data = (Serializable) in.readObject();
                    System.out.println(data.toString());
                    if(data instanceof Map) {
                        Platform.runLater(() -> {
                            try {
                                ClientGameScene clientGameScene = new ClientGameScene(mapLatch, (Map)data, gameView,this, pl);
                                gameView.setScene(clientGameScene.getScene());
                                this.networkGameScene = clientGameScene;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });


                    }
                    else if(data instanceof BuildRequest) {

                        Platform.runLater(() ->{
                            MapButton mb = ((BuildRequest)data).mapButton;
                            MapButton mapB = networkGameScene.findMapButton(mb);
                            Player builderPlayer = new Player(((BuildRequest)data).playerInfo);
                            mapB.clientUpdate(builderPlayer, ((BuildRequest)data).getHasCity());
                            if(builderPlayer.equals(pl)) {
                                System.out.println("UPDATING MY RES");
                                networkGameScene.updateResourcesInTurn(builderPlayer);
                            }
                        });

                    }

                    else if(data instanceof EndTurnInfo) {
                        EndTurnInfo endTurnInfo = (EndTurnInfo) data;

                        Platform.runLater(() ->{
                            ((ClientGameScene) networkGameScene).displayDice(endTurnInfo.getDie1(), endTurnInfo.getDie2());

                            networkGameScene.updateResources(new Player(endTurnInfo.getPlayerInfo()),endTurnInfo.getCurrentPlayerInfo().name);
                            System.out.println(endTurnInfo.getCurrentPlayerInfo().name + "\n" +
                                    endTurnInfo.getPlayerInfo().name + "\n" +
                                    networkGameScene.getPlayer().name);
                            if(new Player(endTurnInfo.getCurrentPlayerInfo()).equals(networkGameScene.getPlayer()))
                                ((ClientGameScene) networkGameScene).enableEndTurn();
                            if( endTurnInfo.getPopUp() != null ) {
                                try {
                                    ((ClientGameScene) networkGameScene).showPopUp(endTurnInfo.getPopUp());
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problem");
            connectionFailed.set(true);
        }
    }

}

