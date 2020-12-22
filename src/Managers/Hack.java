package Managers;

import StorageRelatedClasses.Country;
import StorageRelatedClasses.Player;
import javafx.scene.input.MouseEvent;

public class Hack{

    static int distance;
    Country baseCountry;
    Country targetCountry;

    public Hack(Country baseCountry, Country targetCountry){
        this.baseCountry = baseCountry;
        this.targetCountry = targetCountry;
        calculateDistance();
        startAttack();
    }

    void calculateDistance(){
        int temp1 = Integer.parseInt(baseCountry.getLocation().substring(0, 1)) - Integer.parseInt(targetCountry.getLocation().substring(0, 1));
        int temp2 = Integer.parseInt(baseCountry.getLocation().substring(2, 3)) - Integer.parseInt(targetCountry.getLocation().substring(2, 3));
        distance = (int) Math.sqrt(Math.pow(temp1, 2) + Math.pow(temp2, 2));
        System.out.println("Distance: " + distance);
    }

    void startAttack(){
        Attack attack = new Attack(baseCountry.getOwner(), targetCountry.getOwner(), baseCountry, targetCountry);
        if(targetCountry.getHackerNumber() == 0){
            targetCountry.setOwner(baseCountry.getOwner());
            targetCountry.setHackerNumber(1);
            baseCountry.setHackerNumber(baseCountry.getHackerNumber() - 1);
            baseCountry.getOwner().setNumOfWins(baseCountry.getOwner().getNumOfWins() + 1);
            giveCard();
        }
    }

    void giveCard(){
        //if(attackIsSuccessful)
        //turnManager.turnOwner

    }

}
