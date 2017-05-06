package net.simplifiedcoding.navigationdrawerexample.util;

import android.util.Patterns;

import java.util.regex.Pattern;

/**
 * Created by Vibes on 15-04-2017.
 */
public class CleanMoneyRegExp {
    public static String EMPTY_TEXT = "^(?!\\s*$).+";
    public static String MOBILE_NUMBER = "^\\d{10}$";
    public static String NAME = "^[a-zA-Z ]*$";
    // public static String PASSWORD = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{5,20})";
    public static String PASSWORD = "(.{5,20})";

    public static String EMAIL_ADDRESS = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

    //    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
    public static boolean isValidEmail(String email) {
        System.out.println("Email VAlidation");
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            System.out.println("Email Valid");
            return true;
        } else {
            System.out.println("Email InValid");
            return false;
        }
    }

    public static boolean isValidNAME(String email) {
        System.out.println("Email VAlidation");
        if (Pattern.compile(NAME).matcher(email).matches()) {
            System.out.println("Email Valid");
            return true;
        } else {
            System.out.println("Email InValid");
            return false;
        }
    }

    public static boolean isValidPASSWORD(String password) {
        System.out.println("Password VAlidation");
        if (Pattern.compile(PASSWORD).matcher(password).matches()) {
            System.out.println("Password Valid");
            return true;
        } else {
            System.out.println("Password InValid");
            return false;
        }
    }

    public static boolean isValidPhone(String phone) {
        System.out.println("Phone VAlidation");
        if (Pattern.compile(MOBILE_NUMBER).matcher(phone).matches()) {
            System.out.println("Mobile Valid");
            return true;
        } else {
            System.out.println("Mobile  InValid");
            return false;
        }
    }

    public static boolean chkEmpty(String object) {
        if (object.trim().isEmpty()) {
            System.out.println("Is Empty");
            return true;
        } else {
            System.out.println("Is UnEmpty");
            return false;
        }
    }

    public static boolean chkNull(Object object) {
        if (object == null) {
            return true;
        } else {
            return false;
        }
    }
}
