package Scene;

import Main.Main;
import StorageRelatedClasses.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.ArrayList;

public class GameScene {
    public static ArrayList<Player> players;

    public GameScene(ArrayList<Player> players) throws IOException {
        GameScene.players = players;
        Parent root = FXMLLoader.load(getClass().getResource("/Scene/GameScene.fxml"));
        Main.stage.getScene().setRoot(root);
    }
}
