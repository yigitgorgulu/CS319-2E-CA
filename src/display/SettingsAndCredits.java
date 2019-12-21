package display;

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.awt.image.ImageObserver.WIDTH;

public class SettingsAndCredits{

    private Font flarge;
    final int LARGE_FONT_SIZE = (int)DefaultUISpecifications.SCREEN_WIDTH / 40;

    public SettingsAndCredits(Stage gameview) throws IOException {
        StackPane pane = new StackPane();
        flarge = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), LARGE_FONT_SIZE);

        final HBox credits = new HBox();
        String names = "CREDITS \n \n Programmers \n\n Elif Demir \n Sonat Uzun \n Yusuf Avcı \n Yiğit Görgülü \n Artun Cura \n\n Settlers Of Catan 2019 Extended";
        movingCredits(credits,names);

        pane.getChildren().addAll(getImgView(),credits);
        Scene scene = new Scene(pane);
        gameview.setScene(scene);
    }

    public Node getImgView() throws IOException {
        InputStream is = Files.newInputStream(Paths.get("res/images/MainMenu/background.png"));
        Image img = new Image(is);
        is.close();

        return new ImageView(img);
    }

    public void movingCredits(HBox parent, String text) {
        DropShadow drop = new DropShadow(10, Color.BLACK);
        drop.setInput(new Glow());
        parent.setAlignment(Pos.TOP_CENTER);
        Text scrollingText = new Text(text);
        scrollingText.setEffect(drop);
        scrollingText.setFill(Color.WHITE);
        scrollingText.setFont(flarge);
        scrollingText.setTextAlignment(TextAlignment.CENTER);
        parent.getChildren().add(scrollingText);
        TranslateTransition tt = new TranslateTransition(Duration.millis(10000), scrollingText);
        tt.setFromY(-13 * LARGE_FONT_SIZE);
        tt.setToY(DefaultUISpecifications.SCREEN_HEIGHT);
        tt.setCycleCount(Timeline.INDEFINITE); // repeats for ever
        tt.setAutoReverse(false); //Always start over
        tt.play();
    }
}
