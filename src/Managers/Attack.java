package Managers;

import StorageRelatedClasses.Country;
import StorageRelatedClasses.Player;

public class Attack extends PingManager {
    Player attacker;
    Player defender;
    Country attackerCountry;
    Country defenderCountry;
    PingManager pingManager;
    DiceManager roll;

    Attack(Player attacker, Player defender, Country attackersCountry, Country defendersCountry,
           int numOfAttackerHackers) {
        this.attacker = attacker;
        this.defender = defender;
        this.attackerCountry = attackersCountry;
        this.defenderCountry = defendersCountry;
        pingManager = new PingManager();
        pingManager.setPing();

        roll = new DiceManager(numOfAttackerHackers, Math.min(defenderCountry.getHackerNumber(), 2),
                pingManager.pingLevel);
        roll.compareBiggest(numOfAttackerHackers, Math.min(defenderCountry.getHackerNumber(), 2));

        attackersCountry.setHackerNumber(attackerCountry.getHackerNumber() - roll.defenderWins);
        attacker.setNumOfHackers(attacker.getNumOfHackers() - roll.defenderWins);
        defendersCountry.setHackerNumber(defenderCountry.getHackerNumber() - roll.attackerWins);
        defender.setNumOfHackers(defender.getNumOfHackers() - roll.attackerWins);
    }
}

