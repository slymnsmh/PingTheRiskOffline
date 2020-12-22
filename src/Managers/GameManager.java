package Managers;

import Scene.GameScene;
import StorageRelatedClasses.Country;
import StorageRelatedClasses.Database;
import StorageRelatedClasses.Player;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ForkJoinPool;

public class GameManager implements Initializable
{
    @FXML public Pane map_pane;
    @FXML public ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12, img13, img14, img15, img16,
            img17, img18, img19, img20, img21, img22, img23, img24, img25, img26, img27, img28, img29, img30, img31, img32, img33, img34,
            img35, img36;
    @FXML public Label num1_lbl, num2_lbl, num3_lbl, num4_lbl, num5_lbl, num6_lbl, num7_lbl, num8_lbl, num9_lbl, num10_lbl, num11_lbl, num12_lbl,
            num13_lbl, num14_lbl, num15_lbl, num16_lbl, num17_lbl, num18_lbl, num19_lbl, num20_lbl, num21_lbl, num22_lbl, num23_lbl,
            num24_lbl, num25_lbl, num26_lbl, num27_lbl, num29_lbl, num30_lbl, num31_lbl, num32_lbl, num33_lbl, num34_lbl, num35_lbl, num36_lbl;
    @FXML public Label p1Nick_lbl, p2Nick_lbl, p3Nick_lbl, p4Nick_lbl;
    @FXML public GridPane settings_grid;
    @FXML public VBox nicknames_vbox;
    @FXML public HBox cards_hbox;
    @FXML public Pane c1_img, c2_img, c3_img, c4_img, c5_img, c6_img, c7_img, c8_img, c9_img, c10_img, c11_img;
    @FXML public Pane selectedCard1_img, selectedCard2_img, selectedCard3_img;
    @FXML public Button combineCards_btn;
    @FXML public Label info_lbl;
    @FXML public Pane cards_pane;
    @FXML public Button hire_btn, hack_btn, fortify_btn;
    @FXML public ImageView cards_img;
    @FXML public SplitMenuButton hackerNum_menu;
    @FXML public Label infoGame_lbl;
    @FXML public Button nextPhase_btn;
    public int turnOwner = 1;
    ArrayList<Player> players;
    int playerNumber;
    private final int TOTAL_NUM_OF_COUNTRIES = 36;
    private final int TOTAL_NUM_OF_HACKERS = 120;
    String[] allColors = {"BLUE", "RED", "GREEN", "ORANGE"};
    int hackerNumBeginning;
    int countryNumBeginning;
    ArrayList<Integer> givenCountries = new ArrayList<>();
    ArrayList<Country> allCountries = new ArrayList<>();
    public static Country baseCountry;
    public static Country targetCountry;
    int turnType;
    public boolean first = true;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        turnType = 1;
        players = GameScene.players;
        playerNumber = players.size();
        hackerNumBeginning = TOTAL_NUM_OF_HACKERS / playerNumber;
        countryNumBeginning = TOTAL_NUM_OF_COUNTRIES / playerNumber;
        try {
            startGame();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void nextPhaseClicked()
    {
        turnType++;
        disableNodes();
        baseCountry = null;
        targetCountry = null;
    }

    public void getCountriesFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/StorageRelatedClasses/Countries.txt"));
        String line = reader.readLine();
        while (line != null)
        {
            System.out.println("Line: " + line);
            line = line.substring(1);
            String countryId = line.substring(0, line.indexOf("|"));  //1
            line = line.substring(line.indexOf("|") + 1);
            String countryName = line.substring(0, line.indexOf("|")); //1
            line = line.substring(line.indexOf("|") + 1);
            String location = line;
            Country country = new Country(Integer.parseInt(countryId), countryName, location);
            allCountries.add(country);
            line = reader.readLine();
        }
    }

    public void startGame() throws IOException {
        disableNodes();
        getCountriesFromFile();
        showNicknames();
        showTurnOwner();
        assignColorsToPlayers();
        assignHackerNumsToPlayers();
        assignCountriesToPlayers();
        assignHackerNumsToCountries();
        int counter = 0;
        for (Player p : players) {
            counter++;
            System.out.println("PLAYER: " + counter);
            System.out.println("NICK: " + p.getNickname());
            System.out.println("COLOR: " + p.getColor());
            for (int i = 0; i < p.getCountries().size(); i++) {
                System.out.println("COUNTRY " + i + ": " + p.getCountries().get(i) + " -> hacker num: " + p.getCountries().get(i).getHackerNumber());
            }
            System.out.println("NUM OF COUNTRIES: " + p.getNumOfCountries());
            System.out.println("NUM OF HACKERS: " + p.getNumOfHackers());
        }
        setCountryColors();
        setCountryHackerNumLabels();
        assignBonusHackers();
        setHackerNumMenu(players.get(turnOwner - 1).getNumOfBonusHackers(), false);
    }

    public void assignBonusHackers()
    {
        if (first){
            for (Player p : players)
            {
                p.setNumOfBonusHackers(3);
            }
            first = false;
        }
    }

    public void startTurn(int turnType) {
        assignBonusHackers();
        decidePart(turnType);
    }

    public void setHackerNumMenu(int hackerNum, boolean zero)
    {
        hackerNum_menu.getItems().clear();
        for (int i = 1; i <= hackerNum; i++)
        {
            if(zero) {
                i = -1;
                zero = false;
                continue;
            }
            MenuItem item = new MenuItem(i + "");
            item.setOnAction(event);
            hackerNum_menu.getItems().add(item);
            hackerNum_menu.setText(hackerNum_menu.getItems().get(0).getText());
        }
    }

    EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            MenuItem item = (MenuItem) event.getSource();
            hackerNum_menu.setText(item.getText());
        }
    };

    public void showNicknames()
    {
        int index = 0;
        for (Player p : players)
        {
            Label nickname = (Label) nicknames_vbox.getChildren().get(index);
            nickname.setText(p.getNickname());
            index++;
        }
    }

    public void showTurnOwner()
    {
        Label label = (Label) nicknames_vbox.getChildren().get(turnOwner - 1);
        label.setStyle("-fx-border-color: white;");
    }

    public void disableNodes()
    {
        if (turnType == 1)
        {
            infoGame_lbl.setText("Player \"" + players.get(turnOwner - 1).getNickname() + "\" -> Part: " + "Hire");
            hire_btn.setDisable(false);
            hack_btn.setDisable(true);
            fortify_btn.setDisable(true);
        }
        else if (turnType == 2)
        {
            infoGame_lbl.setText("Player \"" + players.get(turnOwner - 1).getNickname() + "\" -> Part: " + "Hack");
            hire_btn.setDisable(true);
            hack_btn.setDisable(false);
            fortify_btn.setDisable(true);
            cards_img.setDisable(true);
        }
        else if (turnType == 3){
            infoGame_lbl.setText("Player \"" + players.get(turnOwner - 1).getNickname() + "\" -> Part: " + "Fortify");
            cards_img.setDisable(true);
            hire_btn.setDisable(true);
            hack_btn.setDisable(true);
            fortify_btn.setDisable(false);
        }
    }

    public void decidePart(int turnType){
        if (turnType == 1) {
            startHirePart();
        }
        else if(turnType == 2) {
            setHackerNumMenu(0, false);
            startHack();
        }
        else if(turnType == 3){
            setHackerNumMenu(0, false);
            startFortifyPart();
        }
        else
            endTurn();
    }

    public void startHirePart(){
        if (baseCountry != null) {
            if(players.get(turnOwner - 1).getNumOfBonusHackers() != 0)
            {
                System.out.println("asdd " + hackerNum_menu.getText());
                Hire hire = new Hire(players.get(turnOwner - 1), baseCountry, Integer.parseInt(hackerNum_menu.getText()));
            }
            setHackerNumMenu(players.get(turnOwner - 1).getNumOfBonusHackers(), false);
        }
    }

    public void startHack(){
        Player attacker = players.get(turnOwner - 1);
        int win = attacker.getNumOfWins();
        Hack hack = new Hack(baseCountry, targetCountry);
        if(win != attacker.getNumOfWins()) {
            updateScene(baseCountry,targetCountry);
            System.out.println("targetcountryhacker " + targetCountry.getHackerNumber());
            setCountryColors();
            infoGame_lbl.setText("Choose number of hackers to move to gained country!");
            setHackerNumMenu(baseCountry.getHackerNumber() - 1, true);
            hack_btn.setText("MOVE");
        }
        else {
            updateScene(baseCountry, targetCountry);
            enableMap();
        }
    }

    public void disableMap(){
        for(Node n : map_pane.getChildren()){
            n.setDisable(true);
        }
    }

    public void enableMap(){
        for(Node n : map_pane.getChildren()){
            n.setDisable(false);
        }
    }

    public void startFortifyPart(){
        //Fortify fortify = new Fortify(turnOwner);
    }

    public void giveBonusHackers(){

    }

    public void endTurn(){
        turnOwner++;
        turnType = 1;
        first = true;
        startTurn(turnType);
    }

    public void setCountryColors() {

        for (int i = 0; i < TOTAL_NUM_OF_COUNTRIES; i++) {
            ImageView countryImageView = (ImageView) map_pane.getChildren().get(i);
            countryImageView.setPickOnBounds(false);
            if (playerNumber >= 1) {
                if (allCountries.get(i).getOwner() == players.get(0)) {
                    countryImageView.setImage(new Image("/MapComponents/" + (i + 1) + "/" + (i + 1) + "b.png"));
                    continue;
                }
            }
            if (playerNumber >= 2) {
                if (allCountries.get(i).getOwner() == players.get(1)) {
                    countryImageView.setImage(new Image("/MapComponents/" + (i + 1) + "/" + (i + 1) + "r.png"));
                    continue;
                }
            }
            if (playerNumber >= 3) {
                if (allCountries.get(i).getOwner() == players.get(2)) {
                    countryImageView.setImage(new Image("/MapComponents/" + (i + 1) + "/" + (i + 1) + "g.png"));
                    continue;
                }
            }
            if (playerNumber >= 4) {
                if (allCountries.get(i).getOwner() == players.get(3)) {
                    countryImageView.setImage(new Image("/MapComponents/" + (i + 1) + "/" + (i + 1) + "o.png"));
                }
            }
        }
    }

    public void setCountryHackerNumLabels() {
        for (int i = TOTAL_NUM_OF_COUNTRIES; i < map_pane.getChildren().size(); i++) {
            Label label = (Label) map_pane.getChildren().get(i);
            label.setText(allCountries.get(i - TOTAL_NUM_OF_COUNTRIES).getHackerNumber() + "");
        }
    }

    public void assignColorsToPlayers()
    {
        int i = 0;
        for (Player p : players)
        {
            p.setColor(allColors[i]);
            i++;
        }
    }

    public void assignHackerNumsToPlayers() {
        for (Player p : players) {
            p.setNumOfHackers(hackerNumBeginning);
            System.out.println(p.getNickname() + " - Hacker updated!");
        }
    }

    public void assignCountriesToPlayers() {
        for (Player p : players) {
            ArrayList<Country> countries = new ArrayList<>();
            ArrayList<Integer> countryNumbers = generateRandomCountryNumbers(); //25 - 32 - 21
            for (int i : countryNumbers)
            {
                countries.add(allCountries.get(i - 1));
            }
            p.setCountries(countries);
            p.setNumOfCountries(countryNumBeginning);
            for (Country c : countries) {
                c.setOwner(p);
            }
        }
    }

    public ArrayList<Integer> generateRandomCountryNumbers()
    {
        int[] countryNumbers = new int[countryNumBeginning];
        int counter = 0;
        while ( counter != countryNumbers.length )
        {
            int randomNumber = new Random().nextInt(TOTAL_NUM_OF_COUNTRIES) + 1;
            boolean isThere = false;
            for ( int i = 0; i < countryNumbers.length; i++ )
            {
                if ( countryNumbers[i] == randomNumber )
                    isThere = true;
            }
            for (int i = 0; i < givenCountries.size(); i++)
            {
                if (givenCountries.get(i) == randomNumber)
                    isThere = true;
            }
            if ( !isThere )
            {
                countryNumbers[counter] = randomNumber;
                givenCountries.add(randomNumber);
                counter++;
            }
        }
        ArrayList<Integer> countryNums = new ArrayList<>();
        for (int a : countryNumbers)
        {
            countryNums.add(a);
        }
        return countryNums;
    }

    public void assignHackerNumsToCountries() {
        int minHackerForACountry = hackerNumBeginning / countryNumBeginning;
        System.out.println("MIN HACKER FOR A COUNTRY: " + minHackerForACountry);
        String checker = "";
        for (Country country : allCountries)
        {
            if (checker.equals("..")) {
                country.setHackerNumber(minHackerForACountry + 1);
                System.out.println(country.getId() + " -> hacker num ===> " + country.getHackerNumber());
                checker = "";
            }
            else if (checker.equals("") || checker.equals(".")) {
                country.setHackerNumber(minHackerForACountry);
                System.out.println(country.getId() + " -> hacker num ===> " + country.getHackerNumber());
                checker += ".";
            }
        }
    }

    @FXML
    public void regionClicked(MouseEvent e)
    {
        if (turnType == 1)
        {
            ImageView x = (ImageView)e.getSource();
            System.out.println(x.getId());
            int countryIndex = Integer.parseInt(x.getId().substring(3));
            for(Country c : players.get(turnOwner - 1).getCountries()){
                if(c.getId() == countryIndex)
                    baseCountry = c;
            }
        }
        else if (turnType == 2)
        {
            ImageView x = (ImageView)e.getSource();
            System.out.println(x.getId());
            int countryIndex = Integer.parseInt(x.getId().substring(3));

            for (Player p : players) {
                for (Country c : p.getCountries()){
                    if(c.getId() == countryIndex){
                        if(c.getOwner() == players.get(turnOwner -1)){
                            baseCountry = c;
                        }
                        else
                            targetCountry = c;
                    }
                }
            }
            if (baseCountry.getHackerNumber() >= 3)
                setHackerNumMenu(3, false);
            else
                setHackerNumMenu(baseCountry.getHackerNumber(), false);
        }
        else{

        }
    }

    @FXML
    public void hireClicked()
    {
        if(baseCountry == null){
            infoGame_lbl.setText("You must select one of your own countries!");
        }
        else{
            startTurn(turnType);
            updateScene(baseCountry, null);
            if (players.get(turnOwner - 1).getNumOfBonusHackers() == 0){
                infoGame_lbl.setText("No hackers left to hire. Go to hack phase!");
            }
        }
    }

    private void updateScene(Country baseCountry, Country targetCountry) {
        if (baseCountry != null) {
            if (turnType == 1) {
                int cId = baseCountry.getId();
                for (int i = TOTAL_NUM_OF_COUNTRIES; i < map_pane.getChildren().size(); i++) {
                    Label label = (Label) map_pane.getChildren().get(i);
                    if (label.getId().substring(3, label.getId().indexOf("_")).equals(String.valueOf(cId))) {
                        label.setText(baseCountry.getHackerNumber() + "");
                        break;
                    }
                }
            } else if (turnType == 2) {
                int cId = baseCountry.getId();
                int cId2 = targetCountry.getId();
                for (int i = TOTAL_NUM_OF_COUNTRIES; i < map_pane.getChildren().size(); i++) {
                    Label label = (Label) map_pane.getChildren().get(i);
                    if (label.getId().substring(3, label.getId().indexOf("_")).equals(String.valueOf(cId))) {
                        label.setText(baseCountry.getHackerNumber() + "");
                    }
                    if (label.getId().substring(3, label.getId().indexOf("_")).equals(String.valueOf(cId2))) {
                        label.setText(targetCountry.getHackerNumber() + "");
                    }
                }
            } else {

            }
        }
    }

    @FXML
    public void hackClicked()
    {
        if(hack_btn.getText().equals("HACK")){
            if(baseCountry == null || targetCountry == null) {
                infoGame_lbl.setText("Choose countries for hack!");
            }
            else if(baseCountry.getHackerNumber() <= 1)
            {
                System.out.println("gir aq");
                infoGame_lbl.setText("Choose a country with more hackers!");
            }
            else
            {
                disableMap();
                startTurn(turnType);
            }
        }
        else if(hack_btn.getText().equals("MOVE")){
            System.out.println("menu " + hackerNum_menu.getText());
            System.out.println("basecountryhacekrafterhack" + baseCountry.getHackerNumber());
            baseCountry.setHackerNumber(baseCountry.getHackerNumber() - Integer.parseInt(hackerNum_menu.getText()));
            targetCountry.setHackerNumber(targetCountry.getHackerNumber() + Integer.parseInt(hackerNum_menu.getText()));
            hack_btn.setText("HACK");
            enableMap();
            updateScene(baseCountry,targetCountry);
            baseCountry = null;
            targetCountry = null;
        }
    }

    @FXML
    public void fortifyClicked()
    {
        turnType = 4;
        startTurn(turnType);
    }

    @FXML
    public void howToPlayClicked()
    {
    }

    @FXML
    public void settingsClicked(MouseEvent e)
    {
        if (settings_grid.isVisible())
            settings_grid.setVisible(false);
        else
            settings_grid.setVisible(true);
    }

    @FXML
    public void cardsClicked()
    {
        if (!cards_pane.isVisible())
            cards_pane.setVisible(true);
        else
            cards_pane.setVisible(false);
    }







/*    public void endGame()
    {
        if ( checkIfFinished() )
        {
            //Finish the game
            uploadDatabase();
        }
        else
        {
            //Start next turn
        }

    }



    public void uploadDatabase()
    {
        //When the game finished, upload all game info to database
    }*/

    /*@FXML
    public void howToPlayClicked()
    {}

    @FXML
    public void settingsClicked()
    {}*/

    /*public void assignColorsToCountries()
    {}

    public boolean checkIfFinished()
    {
        return false;
    }

    public void setTurnOwner(int turnOwner)
    {
        this.turnOwner = turnOwner;
    }*/

    //Oyunculara renk ata
    //Oyunculara hacker sayısı ata
    //Ülkeleri oyunculara ata
    //Ülkelere renk ata
    //Ülkere hacker sayısı ata
    //Eğer tüm ülkeler tek bir kişinin değilse
    //Turn sahibini belirle
    //Turn başlat
    //Eğer tüm ülkeler tek bir kişininse
    //Oyun bilgilerini (kazanan, puanlar vs) güncelle (database)
    //Oyunu bitir ve oyuncuları lobby sayfasına döndür
}