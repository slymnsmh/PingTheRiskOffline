package Managers;

import StorageRelatedClasses.Player;
import javafx.scene.input.MouseEvent;

public class Hack{

    int distance;
    boolean timesOut = false;
    boolean attackIsSuccessful = false;
    Player attacker;
    Player  defender;
    public Hack(Player attacker, Player defender){
        this.attacker = attacker;
        this.defender = defender;
        this.distance = distance;
        //boolean timesOut = false;
        boolean attackIsSuccessful = false;
    }

    /*void calculateDistance(){
        int temp1 = Integer.parseInt(baseCountry.location.substring(0, 1)) - Integer.parseInt(targetCountry.location.substring(0, 1));
        int temp2 = Integer.parseInt(baseCountry.location.substring(2, 3)) - Integer.parseInt(targetCountry.location.substring(2, 3));
        distance = (int) Math.sqrt(Math.pow(temp1, 2) + Math.pow(temp2, 2));
    }
    void startHack(){
        Attack atak = new Attack(one ,two, baseCountry, targetCountry);
    }

    void giveCard(){
        //if(attackIsSuccessful)
        //turnManager.turnOwner

    }*/

}
