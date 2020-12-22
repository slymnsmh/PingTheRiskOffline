package Managers;

public class PingManager{
    int pingLevel;
    int attackerRange = 0;

    void setPing(){
        pingLevel = Hack.distance;
        if(Hack.distance > 5)
            pingLevel = 5;
        System.out.println(pingLevel);
    }

    void setAttackerRange(int ping){
        attackerRange = ping;
    }

}

