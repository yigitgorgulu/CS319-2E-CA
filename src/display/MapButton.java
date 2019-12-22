package display;

import game.map.Buildable;
import game.map.Location;
import game.map.MapCorner;
import game.map.MapElement;
import game.player.Player;
import javafx.scene.shape.Circle;

import java.io.Serializable;

public class MapButton extends Circle implements Serializable {
    public MapElement getMapElement() {
        return me;
    }

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

            paintButton(hasCity(), p);

        }
    }
    public void clientUpdate(Player player, boolean hasCity) {
        this.setOpacity(0.7);
        paintButton(hasCity, player);
    }

    private void paintButton(boolean hasCity, Player player) {
        if(hasCity) {
            this.setFill(player.getColor().darker().darker());
        }
        else {
            this.setFill(player.getColor());
        }
    }

    public int getXLoc() {
        return me.getLocation().getX();
    }
    public int getYLoc() {
        return me.getLocation().getY();
    }
    public Location.Types getLocType() {
        return me.getLocation().getType();
    }

    public boolean hasCity() {
        if(me instanceof MapCorner) {
            return (((MapCorner) me).hasCity());
        }
        return false;
    }

}
