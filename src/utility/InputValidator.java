package utility;

public class InputValidator {
    private final DateValidator dateValidator = new DateValidator("dd/MM/yyyy");

    public InputValidator() {

    }

    // Int input
    public boolean isValidNumber(String input) throws CustomException {
        boolean isValid = false;
        int in = 999;
        try {
            in = Integer.parseInt(input);
            isValid = true;
        } catch (Exception ex) {
            isValid = false;
            throw new CustomException("Enter valid input");
        }
        return isValid;
    }

    // +ve Int input
    public boolean isValidNumber(String input, boolean isPositive) throws CustomException {
        boolean isValid = false;
        int in = 999;
        try {
            in = Integer.parseInt(input);
            isValid = true;
            if (isPositive) {
                if (in <= 0) {
                    isValid = false;
                    throw new CustomException("Enter only positive value");
                }
            }
        } catch (NumberFormatException ex) {
            isValid = false;
            throw new CustomException("Enter valid input");
        }
        return isValid;
    }

    // Int input
    public boolean isValidDouble(String input) throws CustomException {
        boolean isValid = false;
        double in = 0.0;
        try {
            in = Double.parseDouble(input);
            isValid = true;
        } catch (NumberFormatException ex) {
            isValid = false;
            throw new CustomException("Enter valid input");
        }
        return isValid;
    }

    // +ve Int input
    public boolean isValidDouble(String input, boolean isPositive) throws CustomException {
        boolean isValid = false;
        double in = 0.0;
        try {
            in = Double.parseDouble(input);
            isValid = true;
            if (isPositive) {
                if (in <= 0) {
                    isValid = false;
                    throw new CustomException("Enter only positive value");
                }
            }
        } catch (NumberFormatException | CustomException ex) {
            isValid = false;
            throw new CustomException("Enter valid input");
        }
        return isValid;
    }

//    // Char input
//    public char inputChar(String input) {
//        String inputStr = inputString(input);
//        return inputStr.charAt(0);
//    }

    // String input
    public boolean isValidString(String input) throws CustomException {
        boolean isValid = false;
        // System.out.println("+" + inputStr.trim() + "+");
        if (input.trim().isEmpty()) {
            isValid = false;
            throw new CustomException("Only Space is not allowed");
        } else {
            //input = input.replaceAll("\\s+", " ");
            isValid = true;
        }
        return isValid;
    }

    // Date input for anydate
    public boolean isValidDate(String input) {
        boolean isValid = false;
        isValid = dateValidator.isValid(input);
        return isValid;
    }

    // Date input and check for future date true and pastdate = false
    public boolean isValidDate(String input, boolean isFutureDate) {
        boolean isValid = false;
        isValid = dateValidator.isValid(input, isFutureDate);
        return isValid;
    }
}
