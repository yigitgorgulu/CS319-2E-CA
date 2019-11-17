package Display;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MapToken extends Circle {
    int number;
    int noOfPoints;
    Text no;
    VBox vBox;
    HBox hBox;
    Font font;

    public MapToken(double radius, double x, double y, int number) throws FileNotFoundException {
        super(x, y, radius);
        font = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), 30);
        this.number = number;
        this.noOfPoints = 6 - Math.abs(7 - number);
        no = new Text(Integer.toString(number));
        no.setFill(Color.BLACK);
        no.setTextAlignment(TextAlignment.CENTER);
        no.setFont(font);
        this.setFill(Color.WHITESMOKE);
        this.setOpacity(0.7);
        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(3);
        for (int i = 0; i < noOfPoints; i++) {
            hBox.getChildren().add(new Circle(3));
        }
        vBox = new VBox(no, hBox);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(4);
    }

    public Node getVBox() {
        return vBox;
    }

    public double getTextWidth(){
        return no.getWrappingWidth();
    }
}
