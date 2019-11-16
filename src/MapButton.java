import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.lang.Math;

class MapButton extends Circle {
    Location loc;
    MapElement me;

    MapButton( double x, double y, double r, MapElement me) {
        super( x, y, r );
        this.loc = loc;
        this.me = me;
    }

    void update() {
        if( !me.isEmpty() )
            this.setFill(Color.DARKSLATEBLUE);
    }
}
