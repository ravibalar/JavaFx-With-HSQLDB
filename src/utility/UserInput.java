package utility;

import java.util.Scanner;

public class UserInput {
    private Scanner sc = new Scanner(System.in);
    private DateValidator dateValidator = new DateValidator("dd/MM/yyyy");

    public UserInput() {

    }

    // Int input
    public int inputInt(String input) {
        int in = 999;
        boolean isValid = false;
        do {
            try {
                System.out.print(input + ":");
                in = Integer.parseInt(sc.nextLine());
                isValid = true;
            } catch (Exception ex) {
                isValid = false;
                System.out.println("Enter valid input");
            }
        } while (!isValid);
        return in;
    }

    // +ve Int input
    public int inputInt(String input, boolean isPositive) {
        int in = 999;
        boolean isValid = false;
        do {
            try {
                System.out.print(input + ":");
                in = Integer.parseInt(sc.nextLine());
                isValid = true;
                if (isPositive) {
                    if (in <= 0) {
                        isValid = false;
                        System.out.println("Enter only positive value");
                    }
                }
            } catch (Exception ex) {
                isValid = false;
                System.out.println("Enter valid input");
            }
        } while (!isValid);
        return in;
    }

    // Double input
    public double inputDouble(String input) {
        double in = 0.0;
        boolean isValid = false;
        do {
            try {
                System.out.print(input + ":");
                in = Double.parseDouble(sc.nextLine());
                isValid = true;
            } catch (Exception ex) {
                isValid = false;
                System.out.println("Enter valid input");
            }
        } while (!isValid);
        return in;
    }

    // +ve Double input
    public double inputDouble(String input, boolean isPositive) {
        double in = 0.0;
        boolean isValid = false;
        do {
            try {
                System.out.print(input + ":");
                in = Double.parseDouble(sc.nextLine());
                isValid = true;
                if (isPositive) {
                    if (in <= 0) {
                        isValid = false;
                        System.out.println("Enter only positive value");
                    }
                }
            } catch (Exception ex) {
                isValid = false;
                System.out.println("Enter valid input");
            }
        } while (!isValid);
        return in;
    }

    // Char input
    public char inputChar(String input) {
        String inputStr = inputString(input);
        return inputStr.charAt(0);
    }

    // String input
    public String inputString(String input) {
        String inputStr = "";
        do {
            System.out.print(input + ":");
            inputStr = sc.nextLine();
            // System.out.println("+" + inputStr.trim() + "+");
            if (inputStr.trim().isEmpty()) {
                System.out.println("Only Space is not allowed in input");
                inputStr = "";
            } else {
                inputStr = inputStr.replaceAll("\\s+", " ");
            }
        } while (inputStr.isEmpty());
        return inputStr;
    }

    // Date input for anydate
    public String inputDate(String input) {
        String inputStr = "";
        do {
            System.out.print(input + ":");
            inputStr = sc.nextLine();
            // validate dates
            if (!dateValidator.isValid(inputStr)) {
                inputStr = "";
            }
        } while (inputStr == "" || inputStr.length() == 0);
        return inputStr;
    }

    // Date input and check for future date true and pastdate = false
    public String inputDate(String input, boolean isFutureDate) {
        String inputStr = "";
        do {
            System.out.print(input + ":");
            inputStr = sc.nextLine();
            // validate dates
            if (!dateValidator.isValid(inputStr, isFutureDate)) {
                inputStr = "";
            }
        } while (inputStr == "" || inputStr.length() == 0);
        return inputStr;
    }
}
