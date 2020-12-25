package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;

public class Main extends Application
{
    public static Stage stage;
    public static MediaPlayer mediaPlayer;
    public static Media media;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
//        String path = new File("src/Pictures/Solar.wav").getAbsolutePath();
//        media = new Media(new File(path).toURI().toString());
//        mediaPlayer = new MediaPlayer(media);
//        mediaPlayer.play();
        stage = primaryStage;
        Parent root = FXMLLoader.load(getClass().getResource("/Scene/MainScene.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Ping The Risk v1.0");

        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }
}
