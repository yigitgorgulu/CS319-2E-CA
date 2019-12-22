package network.requests;

import display.MapButton;
import game.map.MapElement;

import java.io.Serializable;

public class BuildRequest implements Serializable {
    public MapElement element;
    public MapButton mapButton;
    public PlayerInfo playerInfo;

    public BuildRequest(MapElement element, MapButton mapButton, PlayerInfo playerInfo) {
        this.element = element;
        this.mapButton = mapButton;
        this.playerInfo = playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }
}
