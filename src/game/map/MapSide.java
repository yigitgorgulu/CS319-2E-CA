package game.map;
import game.player.Player;

import java.io.Serializable;

public class MapSide implements MapElement, Buildable, Serializable {
    Location loc;
    boolean hasRoad;
    Player player = null;
    MapPort port = null;

    MapSide(Location l ) {
        loc = l;
    }

    public Location getLocation() {
        return loc;
    }

    public boolean isEmpty() {
        return !hasRoad;
    }

    public void build( Player player) {
        hasRoad = true;
        this.player = player;
    }

    public Player.Actions getCost() { return Player.Actions.BUILD_ROAD; };

    public Player getPlayer() {
        return player;
    }

}
