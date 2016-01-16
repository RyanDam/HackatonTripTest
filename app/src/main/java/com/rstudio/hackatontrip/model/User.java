package com.rstudio.hackatontrip.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ryan on 1/16/16.
 */
@ParseClassName("User")
public class User extends ParseUser {
    private static final String KEY_NICKNAME = "nickname";
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void setUsername(String username) {
        super.setUsername(username);
        super.setEmail(username);
    }

    public void setNickName(String nickName) {
        this.put(KEY_NICKNAME, nickName);
    }

    public static boolean validate(String username , String password) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(username);
        boolean emailValid =  matcher.find();
        boolean passwordValid = (password.compareTo("") != 0 && password.length() > 5);
        return emailValid && passwordValid;
    }

    public  static boolean validatePassword(String password) {
        return (password.compareTo("") != 0 && password.length() > 5);
    }

    public static boolean validateUsername(String username) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(username);
        return matcher.find();
    }
}
