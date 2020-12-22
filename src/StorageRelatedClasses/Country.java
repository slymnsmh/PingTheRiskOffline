package StorageRelatedClasses;

public class Country
{
    int id;
    String name;
    String location;
    int hackerNumber;
    Player owner;

    public Country( int id, String name, String location, Player owner )
    {
        this.id = id;
        this.name = name;
        this.location = location;
        this.owner = owner;
    }

    public Country( int id, String name, String location )
    {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getHackerNumber() {
        return hackerNumber;
    }

    public void setHackerNumber(int hackerNumber) {
        this.hackerNumber = hackerNumber;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
