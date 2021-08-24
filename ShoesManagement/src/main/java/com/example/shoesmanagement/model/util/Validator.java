package com.example.shoesmanagement.model.util;

import com.example.shoesmanagement.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.apache.commons.validator.routines.DateValidator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.shoesmanagement.model.util.ModelConstant.PASSWORD_NOT_MATCH;
import static com.example.shoesmanagement.model.util.ModelConstant.WRONG_EMAIL_FORMAT;


/**
 * @author Phil Conal
 */
public class Validator {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX
            = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}$", Pattern.CASE_INSENSITIVE);

    public static void validateEmail(String emailAddress) {
        if (emailAddress == null || emailAddress.isBlank())
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Email is empty or null!");
        emailAddress = emailAddress.trim();
        boolean isEmailFormat = isEmailFormat(emailAddress);
        if (!isEmailFormat) {
            throw new ApplicationException(WRONG_EMAIL_FORMAT, HttpStatus.BAD_REQUEST);
        }
    }

    private static boolean isEmailFormat(String valueToValidate) {
        // Regex
        String regexExpression = "\\b[\\w.%-]+@[-.\\w]+\\.[A-Za-z]{2,4}\\b";
        Pattern regexPattern = Pattern.compile(regexExpression);

        if (valueToValidate != null) {
            if (valueToValidate.indexOf("@") <= 0) {
                return false;
            }
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(valueToValidate);
            return matcher.matches();
        } else { // The case of empty Regex expression must be accepted
            Matcher matcher = regexPattern.matcher("");
            return matcher.matches();
        }
    }

    public static void checkExistingObject(Object object, String message) {
        if (object != null)
            throw new ApplicationException(message, HttpStatus.BAD_REQUEST);

    }

    public static void checkNotFound(Object object, String message) {
        if (object == null)
            throw new ApplicationException(message, HttpStatus.NOT_FOUND);
    }

    public static void checkMatchObject(Object object_a, Object object_b) {
        if (!object_a.equals(object_b))
            throw new ApplicationException(PASSWORD_NOT_MATCH, HttpStatus.BAD_REQUEST);
    }
    public static Date convertDate(String date, String fieldName) {
        if (date == null || date.isBlank())
            throw new ApplicationException(HttpStatus.BAD_REQUEST, fieldName + " is empty or null!");
        date = date.trim();
        if (!DateValidator.getInstance().isValid(date, Constant.API_FORMAT_DATE))
            throw new ApplicationException(HttpStatus.BAD_REQUEST, fieldName + " is wrong format date!");
        try {
            return new SimpleDateFormat(Constant.API_FORMAT_DATE).parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static void checkNullEmptyAndLength(String text, int length, String fieldName) {
        if (text == null || text.isBlank())
            throw new ApplicationException(HttpStatus.BAD_REQUEST, fieldName + " is empty or null!");
        text = text.trim();
        if (text.length() > length)
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Maximum " + fieldName + " is " + length + " characters.");
    }
    public static void checkPhoneFormat(String text) {
        if (text == null || text.isBlank())
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Phone number is empty or null!");
        text = text.trim();
        if (text.length() > 20)
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Maximum phone number is 20 characters.");
        if (text.length() < 5)
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Minimum phone number is 5 characters.");
        for (char letter : text.toCharArray()){
            if (!Character.isDigit(letter))
                throw new ApplicationException(HttpStatus.BAD_REQUEST, "Wrong format phone number.");
        }
    }
    public static void checkNumber(int num, String fieldName){
        if (num < 0)
            throw new ApplicationException(HttpStatus.BAD_REQUEST, fieldName+" must be greater than 0.");
    }
    public static void checkDiscount(int num){
        if (num < 0)
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Discount must be greater than 0.");
        if (num > 100)
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "Discount must be less than 100.");
    }
    public static void checkNumber(double num, String fieldName){
        if (num < 0)
            throw new ApplicationException(HttpStatus.BAD_REQUEST, fieldName+" must be greater than 0.");
    }

    public static void checkNull(Object object, String fieldName){
        if (object == null)
            throw new ApplicationException(HttpStatus.BAD_REQUEST, fieldName+" is null.");
    }
}

