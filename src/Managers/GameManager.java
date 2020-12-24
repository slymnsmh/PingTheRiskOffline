package Managers;

import Scene.GameScene;
import StorageRelatedClasses.Country;
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
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

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
    @FXML public ImageView chosenCard1, chosenCard2, chosenCard3;
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
    int click = 0;
    boolean put = false;

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
        if(nextPhase_btn.getText().equals("Next Phase")) {
            turnType++;
            disableNodes();
            baseCountry = null;
            targetCountry = null;
        }
        else{
            endTurn();
        }
    }

    public void getCountriesFromFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("src/StorageRelatedClasses/Countries.txt"));
        String line = reader.readLine();
        while (line != null)
        {
            line = line.substring(1);
            String countryId = line.substring(0, line.indexOf("|"));
            line = line.substring(line.indexOf("|") + 1);
            String countryName = line.substring(0, line.indexOf("|"));
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
        for(int i = 0; i < nicknames_vbox.getChildren().size(); i++){
            Label label = (Label) nicknames_vbox.getChildren().get(i);
            label.setStyle("-fx-border-color: transparent;");
        }
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
        }
        else if (turnType == 3){
            infoGame_lbl.setText("Player \"" + players.get(turnOwner - 1).getNickname() + "\" -> Part: " + "Fortify");
            hire_btn.setDisable(true);
            hack_btn.setDisable(true);
            fortify_btn.setDisable(false);
            nextPhase_btn.setText("End Turn");
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
            startFortifyPart();
        }
        else
            endTurn();
    }

    public void startHirePart(){
        if (baseCountry != null) {
            if(players.get(turnOwner - 1).getNumOfBonusHackers() != 0)
            {
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
            setCountryColors();
            infoGame_lbl.setText("Choose number of hackers to move to gained country!");
            setHackerNumMenu(baseCountry.getHackerNumber() - 1, true);
            hack_btn.setText("MOVE");
        }
        else {
            updateScene(baseCountry, targetCountry);
            enableMap();
        }
        updateCardsScene();
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
        Fortify fortify = new Fortify(baseCountry, targetCountry, Integer.parseInt(hackerNum_menu.getText()));
    }

    public void endTurn(){
        for (Player p : players) {
            if (p.getCountries().size() == 0) {
                players.remove(p);
                showNicknames();
            }
        }
        if(players.size() >= 2) {
            if (players.size() == turnOwner)
                turnOwner = 0;
            turnOwner++;
            turnType = 1;
            first = true;
            showTurnOwner();
            nextPhase_btn.setText("Next Phase");
            disableNodes();
            baseCountry = null;
            targetCountry = null;
            assignBonusHackers();
            setHackerNumMenu(players.get(turnOwner - 1).getNumOfBonusHackers(), false);
            startTurn(turnType);
        }
        else{
            endGame();
        }
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
        for (int i = TOTAL_NUM_OF_COUNTRIES; i < map_pane.getChildren().size() - TOTAL_NUM_OF_COUNTRIES; i++) {
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
        String checker = "";
        for (Country country : allCountries)
        {
            if (checker.equals("..")) {
                country.setHackerNumber(minHackerForACountry + 1);
                checker = "";
            }
            else if (checker.equals("") || checker.equals(".")) {
                country.setHackerNumber(minHackerForACountry);
                checker += ".";
            }
        }
    }

    public void hideArrows()
    {
        for (int i = TOTAL_NUM_OF_COUNTRIES * 2; i < map_pane.getChildren().size(); i++)
        {
            map_pane.getChildren().get(i).setVisible(false);
        }
    }

    @FXML
    public void regionClicked(MouseEvent e)
    {
        ImageView x = (ImageView)e.getSource();
        int countryIndex = Integer.parseInt(x.getId().substring(3));
        if (turnType == 1)
        {
            for(Country c : players.get(turnOwner - 1).getCountries()){
                if(c.getId() == countryIndex) {
                    hideArrows();
                    map_pane.getChildren().get(c.getId() + (TOTAL_NUM_OF_COUNTRIES * 2) - 1).setVisible(true);
                    baseCountry = c;
                }
            }
        }
        else if (turnType == 2)
        {
            for (Player p : players) {
                for (Country c : p.getCountries()){
                    if(c.getId() == countryIndex){
                        if(c.getOwner() == players.get(turnOwner -1)){
                            hideArrows();
                            map_pane.getChildren().get(c.getId() + (TOTAL_NUM_OF_COUNTRIES * 2) - 1).setVisible(true);
                            baseCountry = c;
                        }
                        else {
                            hideArrows();
                            map_pane.getChildren().get(c.getId() + (TOTAL_NUM_OF_COUNTRIES * 2) - 1).setVisible(true);
                            targetCountry = c;
                        }
                    }
                }
            }
            if(baseCountry != null) {
                setHackerNumMenu(Math.min(baseCountry.getHackerNumber(), 3), false);
            }
        }
        else{
            for (Country c : players.get(turnOwner - 1).getCountries()) {
                if (c.getId() == countryIndex) {
                    hideArrows();
                    map_pane.getChildren().get(c.getId() + (TOTAL_NUM_OF_COUNTRIES * 2) - 1).setVisible(true);
                    if(click % 2 == 0) {
                        targetCountry = null;
                        baseCountry = c;
                    }
                    else
                        targetCountry = c;
                    click++;
                }
            }
            if(baseCountry != null)
                setHackerNumMenu(baseCountry.getHackerNumber() - 1, false);
            else
                hackerNum_menu.setText("0");
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
                hackerNum_menu.setText("0");
            }
        }
    }

    private void updateScene(Country baseCountry, Country targetCountry) {
        if (baseCountry != null) {
            int cId = baseCountry.getId();
            if (turnType == 1) {
                for (int i = TOTAL_NUM_OF_COUNTRIES; i < map_pane.getChildren().size() - TOTAL_NUM_OF_COUNTRIES; i++) {
                    Label label = (Label) map_pane.getChildren().get(i);
                    if (label.getId().substring(3, label.getId().indexOf("_")).equals(String.valueOf(cId))) {
                        label.setText(baseCountry.getHackerNumber() + "");
                        break;
                    }
                }
            } else {
                int cId2 = targetCountry.getId();
                for (int i = TOTAL_NUM_OF_COUNTRIES; i < map_pane.getChildren().size() - TOTAL_NUM_OF_COUNTRIES; i++) {
                    Label label = (Label) map_pane.getChildren().get(i);
                    if (label.getId().substring(3, label.getId().indexOf("_")).equals(String.valueOf(cId))) {
                        label.setText(baseCountry.getHackerNumber() + "");
                    }
                    if (label.getId().substring(3, label.getId().indexOf("_")).equals(String.valueOf(cId2))) {
                        label.setText(targetCountry.getHackerNumber() + "");
                    }
                }
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
                infoGame_lbl.setText("Choose a country with more hackers!");
            }
            else
            {
                disableMap();
                startTurn(turnType);
            }
        }
        else if(hack_btn.getText().equals("MOVE")){
            baseCountry.setHackerNumber(baseCountry.getHackerNumber() - Integer.parseInt(hackerNum_menu.getText()));
            targetCountry.setHackerNumber(targetCountry.getHackerNumber() + Integer.parseInt(hackerNum_menu.getText()));
            hack_btn.setText("HACK");
            enableMap();
            updateScene(baseCountry,targetCountry);
            baseCountry = null;
            targetCountry = null;
            hackerNum_menu.setText("0");
        }
    }

    @FXML
    public void fortifyClicked()
    {
        if(baseCountry == null)
            infoGame_lbl.setText("You must select a country to send hackers!");
        else if (targetCountry == null)
            infoGame_lbl.setText("You must select a country to get hackers!");
        else if (baseCountry == targetCountry)
            infoGame_lbl.setText("Base country and target country can't be same. Please select new ones!");
        else if (baseCountry.getHackerNumber() <= 1){
            infoGame_lbl.setText("There should be at least 1 hacker in base country!");
        }
        else{
            startTurn(turnType);
            updateScene(baseCountry, targetCountry);
        }
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

    private void updateCardsScene() {
        Player currentPlayer = players.get(turnOwner - 1);
        for (int i = 0; i < currentPlayer.getCards().size(); i++)
        {
            Pane pane = (Pane) cards_hbox.getChildren().get(i);
            ImageView imageView = (ImageView) pane.getChildren().get(0);
            int cardType = currentPlayer.getCards().get(i).getType();
            if (cardType == 1)
                imageView.setImage(new Image("/Pictures/lCard.jpg"));
            else if (cardType == 2)
                imageView.setImage(new Image("/Pictures/wCard.jpg"));
            else if (cardType == 3)
                imageView.setImage(new Image("/Pictures/gCard.jpg"));
            else if (cardType == 4)
                imageView.setImage(new Image("/Pictures/bCard.jpg"));
        }
    }

    public void endGame()
    {
        infoGame_lbl.setText("WINNER IS: " + players.get(0) + " !!!!");
    }

    @FXML
    public void selectedClicked(MouseEvent e)
    {
        ImageView imageView = (ImageView) e.getSource();
        if (imageView.getImage() != null)
            imageView.setImage(null);
    }

    @FXML
    public void cardSelected(MouseEvent e)
    {
        ImageView clicked = (ImageView) e.getSource();
        if (chosenCard1.getImage() == null)
        {
            chosenCard1.setImage(clicked.getImage());
            put = true;
        }
        else if (chosenCard2.getImage() == null && !put)
        {
            chosenCard2.setImage(clicked.getImage());
            put = true;
        }
        else if (chosenCard3.getImage() == null && !put)
        {
            chosenCard3.setImage(clicked.getImage());
            put = true;
        }
    }

    @FXML
    public void combineCardsClicked()
    {
        Image lCard = new Image("/Pictures/lCard.jpg");
        Image wCard = new Image("/Pictures/wCard.jpg");
        Image gCard = new Image("/Pictures/gCard.jpg");
        Image bCard = new Image("/Pictures/bCard.jpg");
        System.out.println("BEFORE: " + players.get(turnOwner - 1).getNumOfBonusHackers());
        if (chosenCard1.getImage() == lCard && chosenCard2.getImage() == lCard && chosenCard3.getImage() == lCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 3);
        else if (chosenCard1.getImage() == gCard && chosenCard2.getImage() == gCard && chosenCard3.getImage() == gCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 5);
        else if (chosenCard1.getImage() == bCard && chosenCard2.getImage() == bCard && chosenCard3.getImage() == bCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 7);
        else if (chosenCard1.getImage() == lCard && chosenCard2.getImage() == gCard && chosenCard3.getImage() == bCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 9);
        else if (chosenCard1.getImage() == lCard && chosenCard2.getImage() == bCard && chosenCard3.getImage() == gCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 9);
        else if (chosenCard1.getImage() == bCard && chosenCard2.getImage() == lCard && chosenCard3.getImage() == gCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 9);
        else if (chosenCard1.getImage() == bCard && chosenCard2.getImage() == gCard && chosenCard3.getImage() == lCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 9);
        else if (chosenCard1.getImage() == gCard && chosenCard2.getImage() == lCard && chosenCard3.getImage() == bCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 9);
        else if (chosenCard1.getImage() == gCard && chosenCard2.getImage() == bCard && chosenCard3.getImage() == lCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 9);
        else if (chosenCard1.getImage() == lCard && chosenCard2.getImage() == lCard && chosenCard3.getImage() == bCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 11);
        else if (chosenCard1.getImage() == gCard && chosenCard2.getImage() == gCard && chosenCard3.getImage() == bCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 11);
        else if (chosenCard1.getImage() == wCard && chosenCard2.getImage() == wCard && chosenCard3.getImage() == bCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 11);
        else if (chosenCard1.getImage() == bCard && chosenCard2.getImage() == bCard && chosenCard3.getImage() == bCard)
            players.get(turnOwner - 1).setNumOfBonusHackers(players.get(turnOwner - 1).getNumOfBonusHackers() + 13);
        System.out.println("LATER: " + players.get(turnOwner - 1).getNumOfBonusHackers());
    }
}