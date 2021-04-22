package service.user;

import model.user.User;

public class UserService {
    private User user = null;
    private boolean userLoginStatus = false;

    public UserService() {
        // TODO Auto-generated constructor stub
        this.userLoginStatus = false;
    }

    public User getUser() {
        return user;
    }

    // Return login status
    public boolean isUserLoginStatus() {
        return userLoginStatus;
    }

    // Check for user login ID
    public boolean userLogin(String userID) {

        return checkUserCred(userID, "");
    }

    // Check for user login ID and password
    public boolean userLogin(String userID, String password) {
        return checkUserCred(userID, password);
    }

    // User logout
    public boolean userLogout() {
        this.userLoginStatus = false;
        this.user = null;
        System.out.println("Logout succesfully!!");
        return this.userLoginStatus;
    }

    // Check for user cred
    private boolean checkUserCred(String userID, String password) {
        this.userLoginStatus = false;
        userID = userID.toUpperCase();
        if (userID.matches("^S\\d+")) {
            this.user = new User(userID);
            this.userLoginStatus = true;
        } else {
            System.out.println("Error! Username should start with S and followed by digit(s).");
        }
        return this.userLoginStatus;
    }

}
