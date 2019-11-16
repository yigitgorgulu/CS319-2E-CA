import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.lang.Math;

class MapButton extends Circle {
    Location loc;
    MapElement me;

    MapButton( MapElement me) {
        super( me.getLocation().getRawDisplayPosition().getX(), me.getLocation().getRawDisplayPosition().getY(),
                me.getLocation().type == Location.Types.CORNER ? 4.0D : 3.0D );
        this.loc = loc;
        this.me = me;
    }

    void update() {
        if( !me.isEmpty() )
            this.setFill(Color.DARKSLATEBLUE);
    }
}
