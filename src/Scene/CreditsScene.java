package Scene;

import Main.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

public class CreditsScene
{
    public CreditsScene() throws IOException
    {
        Parent root = FXMLLoader.load(getClass().getResource("/Scene/CreditsScene.fxml"));
        Main.stage.getScene().setRoot(root);
    }
}
