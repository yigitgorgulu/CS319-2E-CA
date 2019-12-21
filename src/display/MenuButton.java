package display;

import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MenuButton extends StackPane{
    private Text text;
    public static final double RECTANGLE_WIDTH = 200;
    public static final double RECTANGLE_HEIGHT = 30;

    public static Font fsmall;

    public MenuButton(String name) throws FileNotFoundException {
        fsmall = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), 16);
        text = new Text(name);
        text.setFont(fsmall);
        text.setFill(Color.WHITE);

        Rectangle bg = new Rectangle(RECTANGLE_WIDTH,RECTANGLE_HEIGHT);
        bg.setOpacity(0.6);
        bg.setFill(Color.BLACK);

        setAlignment(Pos.CENTER);
        getChildren().addAll(bg,text);

        this.setOnMouseEntered(e->{
            bg.setFill(Color.WHITE);
            text.setFill(Color.BLACK);
        });

        this.setOnMouseExited(e->{
            bg.setFill(Color.BLACK);
            text.setFill(Color.WHITE);
        });

        DropShadow drop = new DropShadow(15, Color.WHITE);
        drop.setInput(new Glow());

        setOnMousePressed(e->{
            setEffect(drop);
        });
        setOnMouseReleased(e->setEffect(null));
    }
}
