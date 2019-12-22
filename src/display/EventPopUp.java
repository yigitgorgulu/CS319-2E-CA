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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import network.ServerConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class EventPopUp {
    private static final double NORMAL_FONT_SIZE = 10.0;
    private static final double LARGE_FONT_SIZE = 5.0;
    Stage window;

    public EventPopUp( /*Stage stageItWillBeAdded*/ ) throws FileNotFoundException {

        /*this.gameView = stageItWillBeAdded;
        this.paneWillBeBlurredOut = paneWillBeBlurredOut;
        this.connection = connection;
        this.boolProperty = booleanProperty;*/

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
        gameEvent();

    }

    private void gameEvent() throws FileNotFoundException {
        Text title = new Text("A game Event Has Occured");
        Text explanation = new Text("I explain it here");
        //title.setFont(fNormal);
        //explanation.setFont(fNormal);

        VBox waitingText = new VBox();
        Node paneWillBeBlurredOut;
        Button returnButton = new Button("okay");
        returnButton.setOnMouseClicked(e->{
            window.close();
        });
        waitingText.setAlignment(Pos.CENTER);
        waitingText.getChildren().addAll(title, explanation);
        VBox buttonAndTexts = new VBox();
        buttonAndTexts.getChildren().addAll(waitingText, returnButton);
        buttonAndTexts.setSpacing(50);
        buttonAndTexts.setAlignment(Pos.CENTER);

        //Sets pop up to the window
        /*root.add(this);*/
        Scene scene = new Scene(buttonAndTexts);
        window.setScene(scene);
        window.showAndWait();
    }

}
