package game.map;

import game.player.Player;
import javafx.scene.shape.Circle;

import java.io.Serializable;

public class MapButton extends Circle implements Serializable {
    public int x;
    public int y;
    MapElement me;

    public MapButton(double x, double y, double r, MapElement me) {
        super( x, y, r );
        this.x = (int) x;
        this.y = (int) y;
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

}
