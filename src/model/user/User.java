package model.user;

public class User {
    private String ID;
    private String name;

    public User() {
        // TODO Auto-generated constructor stub
    }

    public User(String userID) {
        ID = userID.toUpperCase();
    }

    public User(String userID, String userName) {
        ID = userID.toUpperCase();
        name = userName;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD.toUpperCase();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
