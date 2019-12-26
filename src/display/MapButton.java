package display;

import game.map.Buildable;
import game.map.Location;
import game.map.MapCorner;
import game.map.MapElement;
import game.player.Player;
import javafx.scene.paint.Color;
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
        this.setOpacity(0.1);
    }

    public void update() {
        if( !me.isEmpty() ){
            this.setOpacity(1);
            Player p = ( (Buildable) me ).getPlayer();

            paintButton(hasCity(), p);

        }
    }

    public void darken(){
        if(me.isEmpty())
            this.setOpacity(1);
    }

    public void lighten(){
        if(me.isEmpty())
            this.setOpacity(0);
    }

    public void clientUpdate(Player player, boolean hasCity) {
        this.setOpacity(1);
        paintButton(hasCity, player);
    }

    private void paintButton(boolean hasCity, Player player) {
        if(hasCity) {
            this.setFill(player.getColor().darker().darker());
            this.setRadius(this.getRadius() + 2);
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

    public void lightlyPaint(Player player) {
        if(me.isEmpty()){
            this.setFill(player.getColor());
            this.setOpacity(0.4);
        }
    }

    public void removeLightlyPaint() {
        if(me.isEmpty())
            this.setFill(Color.TRANSPARENT);
    }
}
