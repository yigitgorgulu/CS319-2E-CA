package display.networkDisplay;

import display.GameScene;
import display.ResourceBox;
import game.map.MapButton;
import game.map.MapElement;
import game.player.Player;
import network.Connection;
import network.requests.BuildRequest;
import network.requests.PlayerInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class NetworkGameScene extends GameScene {
    // properties
    Player player;
    Connection connection;
    List<MapButton> mapButtonList;

    // constructors
    public NetworkGameScene(Connection connection) throws IOException {
        super();
        mapButtonList = new ArrayList<>();
        this.connection = connection;
    }

    // methods
    @Override
    protected void createPlayerResourceBoxes() throws IOException {
        resourceBoxes[0] = new ResourceBox(player, "BRICK");
        resourceBoxes[1] = new ResourceBox(player, "WOOD");
        resourceBoxes[2] = new ResourceBox(player, "SHEEP");
        resourceBoxes[3] = new ResourceBox(player, "WHEAT");
        resourceBoxes[4] = new ResourceBox(player, "ORE");
    }

    @Override
    protected void nonTileMouseClicked(MapButton mb, MapElement a) {
        try {
            connection.send(new BuildRequest(a,mb, new PlayerInfo(player)));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateResources(Player player, String playerName) {
        resourceBoxes[0].update(player);
        resourceBoxes[1].update(player);
        resourceBoxes[2].update(player);
        resourceBoxes[3].update(player);
        resourceBoxes[4].update(player);
        turnOfPlayer.setText("Turn of player " + playerName);
    }

    public MapButton findMapButton(int x, int y) {
        for(MapButton button: mapButtonList) {
            if (x == button.x && y == button.y) {
                return button;
            }
        }
        return null;
    }

    public Player getPlayer() {
        return this.player;
    }
}
