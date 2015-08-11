package com.priest.teddybear.login;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by priest on 7/25/15.
 */
public class PasswordValidater {

    public static final int STANDARD_HASH_CYCLE = 1000;

    public static String encryptPasswordSHA256(String password){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        if (md != null) {
            try {
                md.update(password.getBytes("UTF-8")); // Change this to "UTF-16" if needed
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        byte[] digest = md.digest();
        return new String(digest);
    }

    public static String encryptPassword(String password, int hashCycles){
        String hash = password;
        for(int i=0; i < hashCycles; i++)
            hash = encryptPasswordSHA256(hash);
        return hash;
    }

    public static boolean validateUserPassword(String password, String hash){
        String newHash = encryptPassword(password, STANDARD_HASH_CYCLE);
        if(newHash.equals(hash))
            return true;
        else
            return false;
    }

}
