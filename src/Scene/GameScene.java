package Scene;

import Main.Main;
import StorageRelatedClasses.Player;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.ArrayList;

public final class GameScene {
    public static ArrayList<Player> players;
    private static GameScene instance;

    private GameScene(ArrayList<Player> players) throws IOException {
        this.players = players;
        Parent root = FXMLLoader.load(getClass().getResource("/Scene/GameScene.fxml"));
        Main.stage.getScene().setRoot(root);
    }

    public static GameScene getInstance(ArrayList<Player> players) throws IOException {
        if(instance == null){
            instance = new GameScene(players);
        }
        return instance;
    }
}
