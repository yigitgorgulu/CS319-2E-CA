package display;

import game.player.Player;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResourceBox extends StackPane {
    Player player;
    int itemNumber;
    String itemName;
    Pane pane;
    Text itemQuantity;
    public static Font font;
    //Rectangle background;

    public ResourceBox(Player player, String itemName) throws IOException {
        font = Font.loadFont(new FileInputStream(new File("res/MinionPro-BoldCn.otf")), 20);
        pane = new Pane();
        itemQuantity = new Text("?");
        this.itemName = itemName;
        this.player = player;
        //background = new Rectangle();
        InputStream is;
        is = Files.newInputStream(Paths.get("res/images/cards/brick.png"));
        switch (itemName) {
            case "BRICK":
                is = Files.newInputStream(Paths.get("res/images/cards/brick.png"));
                itemQuantity.setText(Integer.toString(player.getBrick()));
                break;
            case "ORE":
                is = Files.newInputStream(Paths.get("res/images/cards/ore.png"));
                itemQuantity.setText(Integer.toString(player.getOre()));
                break;
            case "SHEEP":
                is = Files.newInputStream(Paths.get("res/images/cards/sheep.png"));
                itemQuantity.setText(Integer.toString(player.getSheep()));
                break;
            case "WHEAT":
                is = Files.newInputStream(Paths.get("res/images/cards/wheat.png"));
                itemQuantity.setText(Integer.toString(player.getWheat()));
                break;
            case "WOOD":
                is = Files.newInputStream(Paths.get("res/images/cards/wood.png"));
                itemQuantity.setText(Integer.toString(player.getWood()));
                break;
        }
        javafx.scene.image.Image img = new Image(is);
        is.close();
        javafx.scene.image.ImageView cardImg = new javafx.scene.image.ImageView(img);
        cardImg.setPreserveRatio(true);
        cardImg.setFitHeight(100);
        itemQuantity.setFill(Color.WHITE);
        itemQuantity.setFont(font);
        itemQuantity.setTranslateX(cardImg.getFitWidth() /2 - itemQuantity.getWrappingWidth() / 2);
        itemQuantity.setTranslateY(cardImg.getFitHeight() / 4);
        getChildren().add(cardImg);
        getChildren().add(itemQuantity);
        System.out.println(itemQuantity.getText());
    }

    public void update(Player player) {
        switch (itemName) {
            case "BRICK":
                itemQuantity.setText(Integer.toString(player.getBrick()));
                break;
            case "ORE":
                itemQuantity.setText(Integer.toString(player.getOre()));
                break;
            case "SHEEP":
                itemQuantity.setText(Integer.toString(player.getSheep()));
                break;
            case "WHEAT":
                itemQuantity.setText(Integer.toString(player.getWheat()));
                break;
            case "WOOD":
                itemQuantity.setText(Integer.toString(player.getWood()));
                break;
        }
        }
}
