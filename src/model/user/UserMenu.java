package model.user;

import java.util.Scanner;

public class UserMenu {
    public Scanner sc = new Scanner(System.in);
    public int currentChoice = 0;
    public int previousChoice = 0;

    // User welcome menu
    public void welcomeMenu() {
        // int userChoice = 99;
        System.out.println("** UniLink System **");
        System.out.println("1. Log in");
        System.out.println("2. Quit");
        // return userChoice;
    }

    // Student welcome menu
    public void studentMenu() {
        System.out.println("** Student Menu **");
        System.out.println("1. New Event Post");
        System.out.println("2. New Sale Post");
        System.out.println("3. New Job Post");
        System.out.println("4. Reply To Post");
        System.out.println("5. Display My Posts");
        System.out.println("6. Display All Posts");
        System.out.println("7. Close Post");
        System.out.println("8. Delete Post");
        System.out.println("9. Log Out");
    }

    // System exit message
    public void exit() {
        System.out.println("System exited! Thanks for using UniLink system");
    }

    // Wrong menu choice
    public void wrongChoice() {
        System.out.println("You made wrong choice!!");
    }

    public int getCurrentChoice() {
        return currentChoice;
    }

    public void setCurrentChoice(int currentChoice) {
        this.currentChoice = currentChoice;
    }

    public int getPreviousChoice() {
        return previousChoice;
    }

    public void setPreviousChoice(int previousChoice) {
        this.previousChoice = previousChoice;
    }

}