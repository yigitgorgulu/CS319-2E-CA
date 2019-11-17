package Display;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class DefaultUISpecifications {
    public static double SCREEN_WIDTH = 1000;
    public static double SCREEN_HEIGHT = 1000;

    public void setScreenDimensions(Stage stage) {
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

        SCREEN_HEIGHT = primaryScreenBounds.getHeight();
        SCREEN_WIDTH = primaryScreenBounds.getWidth();

        stage.setX(primaryScreenBounds.getMinX());
        stage.setY(primaryScreenBounds.getMinY());

        stage.setWidth(SCREEN_WIDTH);
        stage.setHeight(SCREEN_HEIGHT);
    }
}
