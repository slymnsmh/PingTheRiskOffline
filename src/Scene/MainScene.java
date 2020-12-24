package Scene;

import Main.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class MainScene
{
    Parent root;

    public MainScene() throws IOException
    {
        root = FXMLLoader.load(getClass().getResource("/Scene/MainScene.fxml"));
        Main.stage.getScene().setRoot(root);
    }
}
