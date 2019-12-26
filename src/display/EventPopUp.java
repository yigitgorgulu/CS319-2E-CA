package display;

import game.player.Civilization;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class EventPopUp implements Serializable {
    private static final double NORMAL_FONT_SIZE = DefaultUISpecifications.SCREEN_WIDTH / 100;
    private static final double LARGE_FONT_SIZE = DefaultUISpecifications.SCREEN_WIDTH / 95;
    String titleString;
    String explanationString = "";
    Civilization.CivType civType;



    public EventPopUp(String titleString, String explanationString, Civilization.CivType civType) throws IOException {
        this.titleString = titleString;
        this.explanationString = explanationString;
        this.civType = civType;
    }

    public void initPopUp(Group group, Stage window ) throws IOException {
        Font fLarge = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), LARGE_FONT_SIZE);
        Font fNormal = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), NORMAL_FONT_SIZE);
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        //blurBackground(group);
        double widthOfPopUp = DefaultUISpecifications.SCREEN_WIDTH / 4.16;
        double heightOfPopUp = DefaultUISpecifications.SCREEN_HEIGHT / 1.8;
        Rectangle bg = new Rectangle(widthOfPopUp,heightOfPopUp);

        window.setWidth(widthOfPopUp);
        window.setHeight(heightOfPopUp);

        //Put window to the center
        window.setX(DefaultUISpecifications.SCREEN_WIDTH / 2 - widthOfPopUp / 2);
        window.setY(DefaultUISpecifications.SCREEN_HEIGHT / 2 - heightOfPopUp / 2);

        //remove the Windows frame around the stage
        window.initStyle(StageStyle.UNDECORATED);

        //Blur the background, then constructing the pop-up
        /*blurBackground(paneWillBeBlurredOut);*/

        DropShadow shadow = new DropShadow(10, Color.BLACK);

        Text title = new Text(titleString);
        title.setWrappingWidth(NORMAL_FONT_SIZE * 28);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setFont(fLarge);
        title.setFill(Color.WHITE);
        title.setEffect(shadow);

        Text explanation = new Text(explanationString);
        explanation.setWrappingWidth(NORMAL_FONT_SIZE * 25);
        explanation.setTextAlignment(TextAlignment.CENTER);
        explanation.setFont(fNormal);
        explanation.setFill(Color.WHITE);
        explanation.setEffect(shadow);

        VBox waitingText = new VBox();
        MenuButton returnButton = new MenuButton("Continue",waitingText);
        Stage finalWindow = window;
        returnButton.setOnMouseClicked(e->{
            close(finalWindow);
        });

        waitingText.setAlignment(Pos.CENTER);
        waitingText.getChildren().addAll(title, explanation);
        waitingText.setSpacing(20);
        VBox buttonAndTexts = new VBox();
        buttonAndTexts.getChildren().addAll(waitingText, returnButton);
        buttonAndTexts.setSpacing(50);
        buttonAndTexts.setAlignment(Pos.CENTER);

        StackPane pane = new StackPane();
        if ( civType != null )
            bg.setFill(new ImagePattern(getImg(civType)));

        pane.getChildren().addAll(bg, buttonAndTexts);
        Scene scene = new Scene(pane);
        window.setScene(scene);
        window.showAndWait();
    }

    private Image getImg(Civilization.CivType type) throws IOException{
        InputStream is;
        Image img;
        switch (type){
            case OTTOMANS:
                is = Files.newInputStream(Paths.get("res/images/events/ottomansevent.png"));
                img = new Image(is);
                is.close();
                return img;
            case SPAIN:
                is = Files.newInputStream(Paths.get("res/images/events/spainevent.png"));
                img = new Image(is);
                is.close();
                return img;
            case FRANCE:
                is = Files.newInputStream(Paths.get("res/images/events/francevent.png"));
                img = new Image(is);
                is.close();
                return img;
            case MAYA:
                is = Files.newInputStream(Paths.get("res/images/events/mayaevent.png"));
                img = new Image(is);
                is.close();
                return img;
            case ENGLAND:
                is = Files.newInputStream(Paths.get("res/images/events/englandevent.png"));
                img = new Image(is);
                is.close();
                return img;
            case TURKEY:
                is = Files.newInputStream(Paths.get("res/images/events/turkeyevent.png"));
                img = new Image(is);
                is.close();
                return img;
        }
        return null;
    }

    /**
     * Blurs the background pane
     *
     * @param pane pane which will be blurred
     * @throws FileNotFoundException
     */
    public void blurBackground(Group pane) throws FileNotFoundException {
        ColorAdjust adjustment = new ColorAdjust(0, -0.2, -0.2, 0);
        GaussianBlur blur = new GaussianBlur(145); // 55 is just to show edge effect more clearly.
        adjustment.setInput(blur);
        pane.setEffect(adjustment);
    }

    /**
     * Unblurs the background pane
     *
     * @param pane pane which will be blurred
     */
    public void unblurBackground(Group pane) {
        ColorAdjust adjustment = new ColorAdjust(0, 0, 0, 0);
        GaussianBlur blur = new GaussianBlur(0); // 55 is just to show edge effect more clearly.
        adjustment.setInput(blur);
        pane.setEffect(adjustment);
    }

    public void close(Stage window) {
        window.close();
        //unblurBackground(group);
    }



}
