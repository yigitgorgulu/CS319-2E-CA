package sound;

import display.GameScene;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;

public class Sound {
    public Sound(Pane scene) {
        String musicFile = "res/sound/gymnopedie.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);

        MediaView mediaView = new MediaView(mediaPlayer);
        (scene).getChildren().add(mediaView);

        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        
        mediaPlayer.play();
    }
}