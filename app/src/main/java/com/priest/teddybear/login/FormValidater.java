package com.priest.teddybear.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by priest on 7/25/15.
 */
public class FormValidater {

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_NAME_REGEX =
            Pattern.compile("^[A-Z._]$", Pattern.CASE_INSENSITIVE);

    public static final Pattern VALID_PASSSWORD_REGEX =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    public static boolean validateName(String name) {
        Matcher matcher = VALID_NAME_REGEX.matcher(name);
        return matcher.find();
    }

    public static boolean validatePassword(String password){
        Matcher matcher = VALID_PASSSWORD_REGEX.matcher(password);
        return matcher.find();
    }

}
