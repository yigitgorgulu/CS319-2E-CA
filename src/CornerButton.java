import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import java.lang.Math;

class CornerButton extends Circle {
    Location loc;
    enum Types { EMPTY, VILLAGE, SETTLEMENT };

    CornerButton(Location loc) {
        super( loc.getRawDisplayPosition().getX(), loc.getRawDisplayPosition().getY(), 4.0D);
        this.loc = loc;
    }

    void mark() {
        this.setFill(Color.DARKSLATEBLUE);
    }
}
