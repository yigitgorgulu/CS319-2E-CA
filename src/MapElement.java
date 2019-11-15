import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.lang.Math;

class MapElement extends Circle {
    Location loc;
    enum Types { EMPTY, VILLAGE, CITY, ROAD };
    Types type;

    MapElement(Location loc) {
        super( loc.getRawDisplayPosition().getX(), loc.getRawDisplayPosition().getY(),
                loc.type == Location.Types.CORNER ? 4.0D : 3.0D );
        this.loc = loc;
        type = Types.EMPTY;
    }

    void construct() {
        this.setFill(Color.DARKSLATEBLUE);
        if ( loc.type == Location.Types.CORNER) {
            if( type == Types.EMPTY )
                type = Types.VILLAGE;
            else
                type = Types.CITY;
        } else if ( loc.type == Location.Types.SIDE ) {
            type = Types.ROAD;
        }
        type = Types.VILLAGE;
    }
}
