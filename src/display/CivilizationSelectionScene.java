package display;

import display.networkDisplay.NetworkMainMenu;
import game.player.Civilization;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CivilizationSelectionScene {

    Civilization.CivType selectedCiv;

    public CivilizationSelectionScene(Stage gameview) throws IOException {
        super();
        double heightOfACivBack = DefaultUISpecifications.SCREEN_WIDTH / 4;
        double widthOfACivBack = DefaultUISpecifications.SCREEN_HEIGHT / 4;



        DropShadow drop = new DropShadow(15, Color.WHITE);
        drop.setInput(new Glow());

        HBox lineOne = new HBox();
        HBox lineTwo = new HBox();
        VBox vBoxCivilizations = new VBox();
        VBox texts = new VBox();
        VBox textsAndCivilizations = new VBox();
        int i = 0;

        Text typeYourName = new Text("Please Type Your Name");
        TextField textFieldTypeYourName = new TextField();

        texts.getChildren().addAll(typeYourName,textFieldTypeYourName);
        texts.setAlignment(Pos.CENTER);
        texts.setSpacing(50);

        for(Civilization.CivType e: Civilization.CivType.values()){
            StackPane civilizationBox = new StackPane();
            Rectangle backgroundForCivilizations = new Rectangle(widthOfACivBack,heightOfACivBack);
            backgroundForCivilizations.setOpacity(0.8);
            backgroundForCivilizations.setFill(Color.DARKGREY);
            Text nameOfTheCivilization = new Text(e.name());
            civilizationBox.setOnMouseEntered(event->{
                civilizationBox.setEffect(drop);
                civilizationBox.scaleXProperty().set(1.2);
            });
            civilizationBox.setOnMouseExited(event -> {
                civilizationBox.setEffect(null);
                civilizationBox.scaleXProperty().set(1);
            });
            civilizationBox.setOnMouseClicked(event -> {
                selectedCiv = e;
            });
            MenuButton selectButton = new MenuButton("Select" + e.name());
            selectButton.setOnMouseClicked(event -> {
                selectedCiv = e;
            });
            VBox thingsOtherThanBackground = new VBox();
            thingsOtherThanBackground.getChildren().addAll(nameOfTheCivilization,selectButton);
            thingsOtherThanBackground.setAlignment(Pos.CENTER);
            civilizationBox.getChildren().addAll(backgroundForCivilizations,thingsOtherThanBackground);
            if(i < 3)
                lineOne.getChildren().add(civilizationBox);
            else
                lineTwo.getChildren().add(civilizationBox);
            i++;
        }

        lineOne.setSpacing(50);
        lineOne.setAlignment(Pos.CENTER);

        lineTwo.setSpacing(50);
        lineTwo.setAlignment(Pos.CENTER);

        vBoxCivilizations.getChildren().addAll(lineOne,lineTwo);
        vBoxCivilizations.setAlignment(Pos.CENTER);
        vBoxCivilizations.setSpacing(40);
        textsAndCivilizations.getChildren().addAll(texts,vBoxCivilizations);
        textsAndCivilizations.setAlignment(Pos.CENTER);
        textsAndCivilizations.setSpacing(40);
        Scene scene = new Scene(textsAndCivilizations);
        gameview.setScene(scene);
        gameview.show();
    }

    private Node getImgView() throws IOException {
        InputStream is = Files.newInputStream(Paths.get("res/images/MainMenu/background.png"));
        Image img = new Image(is);
        is.close();

        return new ImageView(img);
    }
}