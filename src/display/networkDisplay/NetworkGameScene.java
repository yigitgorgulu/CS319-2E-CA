package display.networkDisplay;

import display.EventPopUp;
import display.GameScene;
import display.ResourceBox;
import display.MapButton;
import game.map.MapElement;
import game.player.Player;
import javafx.stage.Stage;
import network.Connection;
import network.requests.BuildRequest;
import network.requests.PlayerInfo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class NetworkGameScene extends GameScene {
    // properties
    Player player;
    Connection connection;
    List<MapButton> mapButtonList;

    // constructors
    public NetworkGameScene(Stage gameView,Connection connection) throws IOException {
        super(gameView);
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
            BuildRequest buildRequest = new BuildRequest(a,mb, new PlayerInfo(player));
            buildRequest.setHasCity(mb.hasCity());
            connection.send(buildRequest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateResources(Player player, String playerName) {
        updateResourcesInTurn(player);
        turnOfPlayer.setText("Turn of player " + playerName);
    }

    public void updateResourcesInTurn(Player player) {
        resourceBoxes[0].update(player);
        resourceBoxes[1].update(player);
        resourceBoxes[2].update(player);
        resourceBoxes[3].update(player);
        resourceBoxes[4].update(player);
    }

    public MapButton findMapButton(MapButton mapButton) {
        for(MapButton button: mapButtonList) {
            if (mapButton.getXLoc() == button.getXLoc() && mapButton.getYLoc() == button.getYLoc()
                    //A location can be a corner or a side
                    && mapButton.getLocType().equals(button.getLocType())) {
                return button;
            }
        }
        return null;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void showPopUp(EventPopUp popUp) throws FileNotFoundException {
        popUp.initPopUp(gameView);
    };
}
