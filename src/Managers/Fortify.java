package Managers;

import StorageRelatedClasses.Country;

public class Fortify{
    Country baseCountry,targetCountry;
    int hackerNum;


    Fortify(Country baseCountry, Country targetCountry, int hackerNum){
        this.baseCountry = baseCountry;
        this.targetCountry = targetCountry;
        fortifyHackers();
    }

    void fortifyHackers(){
        baseCountry.setHackerNumber(baseCountry.getHackerNumber() - hackerNum);
        targetCountry.setHackerNumber(targetCountry.getHackerNumber() + hackerNum);
    }
}