package utility;

import javafx.util.StringConverter;
import service.UniLinkConfig;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

// Date validation class
public class DateValidator extends StringConverter<LocalDate> {
    // date format to compare
    public String dateFormat = UniLinkConfig.DATE_FORMAT;

    DateTimeFormatter dateFormatter = UniLinkConfig.DATE_FORMATTER;//.ofPattern(UniLinkConfig.DATE_FORMAT);

    public DateValidator() {

    }

    public DateValidator(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    // Check for valid date
    public boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            if (dateStr.length() == this.dateFormat.length()) {
                sdf.parse(dateStr);
            } else {
                System.out.println("Enter valid format for date(ex. 01/12/2020): " + this.dateFormat);
                return false;
            }
        } catch (ParseException ex) {
            System.out.println("Enter valid format for date(ex. 01/12/2020): " + this.dateFormat);
            return false;
        }
        return true;
    }

    // Check for valid date and future date
    public boolean isValid(String dateStr, boolean isFutureDate) {
        DateFormat sdf = new SimpleDateFormat(this.dateFormat);
        sdf.setLenient(false);
        try {
            if (dateStr.length() == this.dateFormat.length()) {
                sdf.parse(dateStr);
                Date date = sdf.parse(dateStr);
                Date today = new Date();
                if (date.compareTo(today) > 0 && isFutureDate) {
                    // System.out.println("Future date");
                } else if (date.compareTo(today) <= 0 && !isFutureDate) {
                    // System.out.println("Past date");
                } else {
                    String errorMessage = isFutureDate ? "Date should be future date" : "Date should be past date";
                    System.out.println(errorMessage);
                    return false;
                }
            } else {
                System.out.println("Enter valid format for date(ex. 01/12/2020): " + this.dateFormat);
                return false;
            }
        } catch (ParseException ex) {
            System.out.println("Enter valid format for date(ex. 01/12/2020): " + this.dateFormat);
            return false;
        }
        return true;
    }


    @Override
    public String toString(LocalDate localDate) {
        if (localDate != null) {
            return dateFormatter.format(localDate);
        } else {
            return "";
        }
    }

    @Override
    public LocalDate fromString(String string) {
        if (string != null && !string.isEmpty()) {
            return LocalDate.parse(string, dateFormatter);
        } else {
            return null;
        }
    }
}