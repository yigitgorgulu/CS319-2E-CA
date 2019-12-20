package game.map;

import game.player.Player;
import javafx.scene.shape.Circle;

import java.io.Serializable;

public class MapButton extends Circle implements Serializable {
    private MapElement me;

    public MapButton(double x, double y, double r, MapElement me) {
        super( x, y, r );
        this.me = me;
    }

    public void update() {
        if( !me.isEmpty() ){
            /*this.setFill(Color.WHITESMOKE);*/
            this.setOpacity(0.7);
            Player p = ( (Buildable) me ).getPlayer();
            this.setFill(p.getColor());
        }
    }
    public void clientUpdate(Player player) {
        this.setOpacity(0.7);
        this.setFill(player.getColor());

    }

    public int getXLoc() {
        return me.getLocation().x;
    }
    public int getYLoc() {
        return me.getLocation().y;
    }
    public Location.Types getLocType() {
        return me.getLocation().type;
    }

}
