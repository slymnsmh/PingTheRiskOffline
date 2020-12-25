package Managers;

import StorageRelatedClasses.*;

public class Hire
{
    int numOfHackers;
    Country country;
    Player turnOwner;

    public Hire(Player turnOwner, Country country, int numOfHackers){
        this.country = country;
        this.turnOwner = turnOwner;
        this.numOfHackers = numOfHackers;
        addNumOfHackers();
        hireHackers();
    }

    void addNumOfHackers()
    {
        if ( turnOwner.getNumOfBonusHackers() != 0)
        {
            turnOwner.setNumOfHackers(turnOwner.getNumOfHackers() + numOfHackers);
            turnOwner.setNumOfBonusHackers(turnOwner.getNumOfBonusHackers() - numOfHackers);
        }
    }

    void hireHackers(){
        country.setHackerNumber(country.getHackerNumber() + numOfHackers);
    }
}