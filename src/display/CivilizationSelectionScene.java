package display;

import display.networkDisplay.NetworkMainMenu;
import game.Sound;
import game.player.Civilization;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

public class CivilizationSelectionScene {

    Civilization.CivType selectedCiv = null;
    String name;
    private Color selectedColor;
    private Font fnormal;
    private Font flarge;
    final int NORMAL_FONT_SIZE = (int)DefaultUISpecifications.SCREEN_WIDTH / 110;
    final int LARGE_FONT_SIZE = (int)DefaultUISpecifications.SCREEN_WIDTH / 60;

    public CivilizationSelectionScene(Stage gameview, CountDownLatch cc) throws IOException {
        super();
        double heightOfACivBack = DefaultUISpecifications.SCREEN_HEIGHT / 3;
        double widthOfACivBack = DefaultUISpecifications.SCREEN_WIDTH / 7;
        StackPane pane = new StackPane();
        fnormal = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), NORMAL_FONT_SIZE);
        flarge = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), LARGE_FONT_SIZE);

        DropShadow drop = new DropShadow(10, Color.WHITE);
        drop.setInput(new Glow());

        HBox lineOne = new HBox();
        HBox lineTwo = new HBox();
        VBox vBoxCivilizations = new VBox();
        VBox texts = new VBox();
        VBox textsAndCivilizations = new VBox(DefaultUISpecifications.SCREEN_HEIGHT / 50);
        int i = 0;

        Text typeYourNameText = new Text("Please Type Your Name");
        typeYourNameText.setFont(fnormal);
        typeYourNameText.setFill(Color.WHITE);
        TextField textFieldTypeYourName = new TextField();
        textFieldTypeYourName.setPrefSize(DefaultUISpecifications.SCREEN_WIDTH / 5, NORMAL_FONT_SIZE);
        textFieldTypeYourName.setStyle("-fx-focus-color: transparent;");
        textFieldTypeYourName.setAlignment(Pos.CENTER);
        texts.getChildren().addAll(typeYourNameText,textFieldTypeYourName);
        texts.setAlignment(Pos.CENTER);
        texts.setSpacing(DefaultUISpecifications.SCREEN_HEIGHT / 140);

        VBox textsAndSelectionText = new VBox(DefaultUISpecifications.SCREEN_HEIGHT / 100);
        Text youveSelected = new Text("");
        youveSelected.setFont(flarge);
        youveSelected.setFill(Color.WHITE);

        textsAndSelectionText.setAlignment(Pos.CENTER);
        textsAndSelectionText.getChildren().addAll(texts,youveSelected);

        StackPane[] civilizationBoxes = new StackPane[6];
        for(Civilization.CivType e: Civilization.CivType.values()){
            StackPane civilizationBox = new StackPane();
            Rectangle backgroundForCivilizations = new Rectangle(widthOfACivBack,heightOfACivBack);
            backgroundForCivilizations.setFill(Color.DARKGREY);
            civilizationBox.setOnMouseEntered(event->{
                civilizationBox.setEffect(drop);
                civilizationBox.scaleXProperty().set(1.02);
                civilizationBox.scaleYProperty().set(1.02);
            });
            civilizationBox.setOnMouseExited(event -> {
                if(!selectedCiv.equals(e)){
                civilizationBox.setEffect(null);
                civilizationBox.scaleXProperty().set(1);
                civilizationBox.scaleYProperty().set(1);
                }
            });
            civilizationBox.setOnMouseClicked(event -> {
                selectedCiv = e;
                for (int s = 0; s<Civilization.CivType.values().length; s++){
                    civilizationBoxes[s].setEffect(null);
                    civilizationBoxes[s].scaleXProperty().set(1);
                    civilizationBoxes[s].scaleYProperty().set(1);
                }
                civilizationBox.setEffect(drop);
                civilizationBox.scaleXProperty().set(1.02);
                civilizationBox.scaleYProperty().set(1.02);
                selectedCiv = e;
                Sound.coolSelection(pane);
                youveSelected.setText(e.name());
            });

            MenuButton selectButton = new MenuButton(e.name(),pane);
            selectButton.setDisable(true);
            selectButton.setPrefWidth(widthOfACivBack);

            VBox thingsOtherThanBackground = new VBox();
            thingsOtherThanBackground.getChildren().add(selectButton);

            thingsOtherThanBackground.setAlignment(Pos.BOTTOM_CENTER);
            Image bg = getSelectedCivBackgrounds(e);
            backgroundForCivilizations.setFill(new ImagePattern(bg));
            civilizationBox.getChildren().addAll(backgroundForCivilizations,thingsOtherThanBackground);
            if(i < 3)
                lineOne.getChildren().add(civilizationBox);
            else
                lineTwo.getChildren().add(civilizationBox);
            civilizationBoxes[i] = civilizationBox;
            i++;
        }

        lineOne.setSpacing(DefaultUISpecifications.SCREEN_WIDTH / 50);
        lineOne.setAlignment(Pos.CENTER);

        lineTwo.setSpacing(DefaultUISpecifications.SCREEN_WIDTH / 50);
        lineTwo.setAlignment(Pos.CENTER);

        vBoxCivilizations.setAlignment(Pos.CENTER);
        vBoxCivilizations.setSpacing(DefaultUISpecifications.SCREEN_WIDTH / 140);
        vBoxCivilizations.getChildren().addAll(lineOne,lineTwo);

        MenuButton returnBack = new MenuButton("Exit",pane);
        returnBack.setOnMouseClicked(e->{
            System.exit(0);
        });

        MenuButton continueToGame = new MenuButton("Continue",pane);
        continueToGame.setDisable(true);
        continueToGame.setOpacity(0.5);
        BooleanBinding booleanBind = textFieldTypeYourName.textProperty().isEmpty().or(youveSelected.textProperty().isEmpty());
        continueToGame.disableProperty().bind(booleanBind);
        continueToGame.opacityProperty().bind(Bindings.when(booleanBind).then(0.5).otherwise(1));
        continueToGame.setOnMouseClicked(e->{

        });


        HBox buttons = new HBox(widthOfACivBack / 5);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(returnBack,continueToGame);


        textsAndCivilizations.setAlignment(Pos.CENTER);
        textsAndCivilizations.getChildren().addAll(textsAndSelectionText,vBoxCivilizations,buttons);

        pane.getChildren().addAll(getBackgroundImage(),textsAndCivilizations);

        Scene scene = new Scene(pane);
        gameview.setScene(scene);
        gameview.show();
    }

    private Node getBackgroundImage() throws IOException {
        InputStream is = Files.newInputStream(Paths.get("res/images/MainMenu/background.png"));
        Image img = new Image(is);
        is.close();

        return new ImageView(img);
    }

    private Image getSelectedCivBackgrounds(Civilization.CivType type) throws IOException{
        InputStream is;
        Image img;
        switch (type){
            case OTTOMANS:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/ottomanselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
            case SPAIN:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/spainselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
            case FRANCE:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/franceselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
            case MAYA:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/mayaselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
            case ENGLAND:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/englandselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
            case TURKEY:
                is = Files.newInputStream(Paths.get("res/images/selectionSceneBackgrounds/turkeyselection.jpg"));
                img = new Image(is);
                is.close();
                return img;
        }

        return null;
    }

    public Color getColor() {
        return selectedColor;
    }

    public Civilization.CivType getCivType() {
        return selectedCiv;
    }

    public String getName(){
        return name;
    }
}