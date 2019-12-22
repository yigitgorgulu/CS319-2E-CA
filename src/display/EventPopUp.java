package display;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import network.ServerConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.io.Serializable;

public class EventPopUp implements Serializable {
    private static final double NORMAL_FONT_SIZE = 10.0;
    private static final double LARGE_FONT_SIZE = 5.0;
    String titleString = "";
    String explanationString = "";
    Font fLarge;
    Font fNormal;

    public EventPopUp( String titleString, String explanationString ) throws FileNotFoundException {


        this.titleString = titleString;
        this.explanationString = explanationString;

        fLarge = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), LARGE_FONT_SIZE);
        fNormal = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), NORMAL_FONT_SIZE);

    }

    public void initPopUp(Stage window) throws FileNotFoundException {
        Font fLarge = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), LARGE_FONT_SIZE);
        Font fNormal = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), NORMAL_FONT_SIZE);
        window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);

        double widthOfPopUp = DefaultUISpecifications.SCREEN_WIDTH / 3;
        double heightOfPopUp = DefaultUISpecifications.SCREEN_HEIGHT / 3;

        window.setWidth(widthOfPopUp);
        window.setHeight(heightOfPopUp);

        //Put window to the center
        window.setX(DefaultUISpecifications.SCREEN_WIDTH / 2 - widthOfPopUp / 2);
        window.setY(DefaultUISpecifications.SCREEN_HEIGHT / 2 - heightOfPopUp / 2);

        //remove the Windows frame around the stage
        window.initStyle(StageStyle.UNDECORATED);

        //Blur the background, then constructing the pop-up
        /*blurBackground(paneWillBeBlurredOut);*/

        Text title = new Text(titleString);
        title.setWrappingWidth(NORMAL_FONT_SIZE * 25);
        title.setTextAlignment(TextAlignment.CENTER);

        Text explanation = new Text(explanationString);
        explanation.setWrappingWidth(NORMAL_FONT_SIZE * 25);
        explanation.setTextAlignment(TextAlignment.CENTER);
        title.setFont(fLarge);
        explanation.setFont(fNormal);

        VBox waitingText = new VBox();
        MenuButton returnButton = new MenuButton("Continue",waitingText);
        Stage finalWindow = window;
        returnButton.setOnMouseClicked(e->{
            finalWindow.close();
        });
        waitingText.setAlignment(Pos.CENTER);
        waitingText.getChildren().addAll(title, explanation);
        waitingText.setSpacing(20);
        VBox buttonAndTexts = new VBox();
        buttonAndTexts.getChildren().addAll(waitingText, returnButton);
        buttonAndTexts.setSpacing(50);
        buttonAndTexts.setAlignment(Pos.CENTER);

        Scene scene = new Scene(buttonAndTexts);
        window.setScene(scene);
        window.showAndWait();
    }

}
