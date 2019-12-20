package display.networkDisplay;

import display.DefaultUISpecifications;
import display.MenuButton;
import display.PopUp;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import network.ServerConnection;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServerLobbyScene extends LobbyScene {

    Font fNormal;
    Font fLarge;
    final double LARGE_FONT_SIZE = 120;
    final double NORMAL_FONT_SIZE = 60;
    final double SPACING_BETWEEN_OBJECTS_IN_THE_SAME_GROUPS = 0;
    final double SPACING_BETWEEN_OBJECTS_IN_THE_DIFFERENT_GROUPS = 70;
    PopUp popUp;

    public ServerLobbyScene(Stage gameView,Scene root) throws IOException {
        super();
        //Creates the main VBox
        VBox group = new VBox();

        fLarge = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), LARGE_FONT_SIZE);
        fNormal = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), NORMAL_FONT_SIZE);

        //Sets the Create A Game Header for the Server Lobby Scene
        Text createAGame = new Text("Create A Game");
        createAGameText(createAGame);

        //Creates the Room Name text and Room Name Textfield for user Input, and puts them in a VBox for later use.
        VBox roomNameBox = new VBox();
        Text roomName = new Text("Room Name");
        TextField roomNameTextField = new TextField();
        createARoomNameBoxAndTextField(roomName,roomNameTextField, roomNameBox); //Edits the text and textfield

        //Creates a Room Size Text and ComboBox. Puts them in a VBox.
        VBox roomSizeBox = new VBox();
        Text roomSize = new Text("Room Size");
        final ComboBox<String> roomSizeComboBox = new ComboBox<>();
        createARoomSizeBoxAndTextField(roomSizeBox,roomSize,roomSizeComboBox); //Edits the text and combobox

        /*Creates the buttons for selection between classic and extended Catan versions. Puts buttons in a HBox,
        then puts them and "Version: " text to a VBox*/
        VBox versionBox = new VBox();
        Text version = new Text("Version: CLASSIC");
        HBox versionButtons = new HBox();
        display.MenuButton classicButton = new display.MenuButton("Catan Classic");
        display.MenuButton extendedButton = new display.MenuButton("Catan Extended");
        createVersionButtonsAndSetListeners(versionBox,version,versionButtons,classicButton, extendedButton);

        //Creates the return and continue buttons. Gives necessary functionality to them and puts them in a HBox.
        display.MenuButton returnButton = new display.MenuButton("Return");
        display.MenuButton continueButton = new display.MenuButton("Continue");
        HBox returnContinueButtons = new HBox();
        createReturnAndContinueButtons(returnButton,continueButton,returnContinueButtons, classicButton, extendedButton,version,roomSizeComboBox,gameView,roomNameTextField, roomName, group, root);

        //Align all the boxes and put them together.
        group.setAlignment(Pos.CENTER);
        group.setSpacing(SPACING_BETWEEN_OBJECTS_IN_THE_DIFFERENT_GROUPS);
        group.getChildren().addAll(createAGame,roomNameBox,roomSizeBox,versionBox, returnContinueButtons);
        this.root.getChildren().add(group);
    }

    private void createReturnAndContinueButtons(MenuButton returnButton, MenuButton continueButton, HBox returnContinueButtons, MenuButton classicButton, MenuButton extendedButton, Text version, ComboBox<String> roomSizeComboBox, Stage gameView, TextField roomNameTextField, Text roomName, VBox group, Scene root) {
        //If return button is clicked go back to the root, which contains main menu, scene
        returnButton.setOnMouseClicked(e->{
            gameView.setScene(root);
        });

        //Set everything disabled on click.
        continueButton.setOnMouseClicked(e->{
            roomNameTextField.setDisable(true);
            classicButton.setDisable(true);
            extendedButton.setDisable(true);
            roomSizeComboBox.setDisable(true);
            returnButton.setDisable(true);

            createAServer(gameView,roomName.getText(),version.getText().substring(9,version.getText().length()),Integer.parseInt(roomSizeComboBox.getValue()));
            try {
                //Try to create a pop up which waits for other players, and updates itself accordingly.
                waitingPopUp(gameView, group);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        //This checks whether all necessary data is filled up. If it is full, continue button gets enabled.
        BooleanBinding booleanBind = roomNameTextField.textProperty().isEmpty().or(roomSizeComboBox.valueProperty().isNull());
        continueButton.disableProperty().bind(booleanBind);

        //Sets HBox for buttons
        returnContinueButtons.setAlignment(Pos.CENTER);
        returnContinueButtons.setMaxSize(DefaultUISpecifications.SCREEN_WIDTH/ 3,NORMAL_FONT_SIZE);
        returnContinueButtons.setSpacing(100);
        returnContinueButtons.getChildren().addAll(returnButton,continueButton);

    }

    private void createVersionButtonsAndSetListeners(VBox versionBox, Text version, HBox versionButtons, MenuButton classicButton, MenuButton extendedButton) throws FileNotFoundException {
        version.setFont(fNormal);
        version.setFill(Color.WHITE);
        classicButton.setDisable(true);
        classicButton.setOpacity(0.5);
        classicButton.setOnMousePressed(e->{
            classicButton.setDisable(true);
            extendedButton.setDisable(false);
            version.setText("Version: Classic");
            classicButton.setOpacity(0.5);
            extendedButton.setOpacity(1.0);
        });
        extendedButton.setOnMousePressed(e->{
            extendedButton.setDisable(true);
            classicButton.setDisable(false);
            version.setText("Version: Extended");
            extendedButton.setOpacity(0.5);
            classicButton.setOpacity(1.0);
        });

        versionButtons.getChildren().addAll(classicButton,extendedButton);
        versionButtons.setMaxSize(DefaultUISpecifications.SCREEN_WIDTH/3, NORMAL_FONT_SIZE);
        versionButtons.setAlignment(Pos.CENTER);
        versionBox.setAlignment(Pos.CENTER);
        versionBox.setSpacing(SPACING_BETWEEN_OBJECTS_IN_THE_SAME_GROUPS);
        versionBox.getChildren().addAll(version,versionButtons);
    }

    private void createARoomSizeBoxAndTextField(VBox roomSizeBox, Text roomSize, ComboBox<String> roomSizeComboBox) {
        roomSize.setFont(fNormal);
        roomSize.setFill(Color.WHITE);
        roomSizeComboBox.getItems().addAll("2", "3", "4", "5", "6");
        roomSizeComboBox.setMaxSize(NORMAL_FONT_SIZE, NORMAL_FONT_SIZE);
        roomSizeComboBox.setStyle("-fx-focus-color: transparent;");;
        roomSizeBox.setSpacing(SPACING_BETWEEN_OBJECTS_IN_THE_SAME_GROUPS);
        roomSizeBox.setAlignment(Pos.CENTER);
        roomSizeBox.getChildren().addAll(roomSize,roomSizeComboBox);
    }

    private void createARoomNameBoxAndTextField(Text roomName, TextField roomNameTextField, VBox roomNameBox) {
        roomName.setFont(fNormal);
        roomName.setFill(Color.WHITE);
        roomNameTextField.setMaxSize(DefaultUISpecifications.SCREEN_WIDTH / 5, NORMAL_FONT_SIZE);
        roomNameTextField.setStyle("-fx-focus-color: transparent;");
        roomNameBox.setSpacing(SPACING_BETWEEN_OBJECTS_IN_THE_SAME_GROUPS);
        roomNameBox.setAlignment(Pos.CENTER);
        roomNameBox.getChildren().addAll(roomName,roomNameTextField);
    }

    private void createAGameText(Text createAGame) {
        createAGame.setFont(fLarge);
        createAGame.setFill(Color.WHITE);
        createAGame.setLineSpacing(12);
    }

    private void waitingPopUp(Stage gameView, Pane group) throws FileNotFoundException {
        popUp = new PopUp("WAITING", group,gameView, (ServerConnection) connection);
    }

    private void createAServer(Stage gameView, String roomName, String selectedVersion, int roomSize) {
        connection = new ServerConnection(gameView,roomName,selectedVersion,roomSize);
        connection.setDaemon(true);
        connection.start();
    }
}

