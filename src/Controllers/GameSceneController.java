package Controllers;

import Managers.GameManager;
import Scene.GameScene;
import StorageRelatedClasses.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class GameSceneController implements Initializable
{
    @Override
    public void initialize( URL location, ResourceBundle resources )
    {
        GameManager gameManager = new GameManager();
        players = GameScene.players;
        int index = 0;
        for (Player p : players)
        {
            Label label = (Label) nicknames_vbox.getChildren().get(index);
            label.setText(p.getNickname());
            index++;
        }
        p1Nick_lbl.setStyle("-fx-border-color: white;");
    }

    public ArrayList<String> divideString(String strWillBeDivided)
    {
        ArrayList<String> allParts = new ArrayList<>();

            while (!strWillBeDivided.equals("")) {
                int index = 0;
                if (strWillBeDivided.contains(",")) {
                    while (strWillBeDivided.charAt(index) != ',') {
                        index++;
                    }
                    String part = strWillBeDivided.substring(0, index);
                    allParts.add(part);
                    strWillBeDivided = strWillBeDivided.substring(index + 1);
                }
                else {
                    allParts.add(strWillBeDivided);
                    strWillBeDivided = "";
                }
            }
        return allParts;
    }
}