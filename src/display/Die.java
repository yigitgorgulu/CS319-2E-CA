package display;

import javafx.geometry.Insets;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Die extends GridPane {
    // properties
    public static final int SIDE_LENGTH = 40;
    public static final int CIRCLE_RADIUS = 7;
    public static final int SPACING = 3;
    int number;

    // constructors
    public Die(int number) {
        super();
        this.setPrefHeight(SIDE_LENGTH);
        this.setPrefWidth(SIDE_LENGTH);
        this.setBorder(new Border(new BorderStroke(null, BorderStrokeStyle.SOLID, null, BorderStroke.MEDIUM)));
        this.setHgap(SPACING);
        this.setVgap(SPACING);
        this.setPadding(new Insets(SPACING));
        this.number = number;

        switch (number) {
            case 1:
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 0, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 2, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 0, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 1, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 2, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 0, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 2, 2);
                break;
            case 2:
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 0, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 2, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 0, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 2, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 0, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 2, 2);
                break;
            case 3:
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 0, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 2, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 0, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 1, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 2, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 0, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 2, 2);
                break;
            case 4:
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 0, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 2, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 0, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 2, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 0, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 2, 2);
                break;
            case 5:
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 0, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 2, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 0, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 1, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 2, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 0, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 2, 2);
                break;
            case 6:
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 0, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 2, 0);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 0, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 2, 1);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 0, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.SKYBLUE), 1, 2);
                this.add(new Circle(CIRCLE_RADIUS, Color.BLACK), 2, 2);
                break;
        }
    }
    // methods
    public void setNumber(int number) {
        this.number = number;
    }
}
