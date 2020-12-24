package Scene;

import Main.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class HowToPlayScene
{
    public HowToPlayScene() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/Scene/HowToPlayScene.fxml"));
        Main.stage.getScene().setRoot(root);
    }
}
