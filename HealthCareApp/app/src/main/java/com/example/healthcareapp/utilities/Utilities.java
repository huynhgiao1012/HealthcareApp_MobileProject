package com.example.healthcareapp.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilities {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.reset();
        messageDigest.update(password.getBytes());
        byte[] mdArray = messageDigest.digest();

        StringBuilder stringBuilder = new StringBuilder(mdArray.length*2);
        for (byte b : mdArray) {
            int v = b & 0xff;
            if (v < 16) {
                stringBuilder.append('0');
            }
            stringBuilder.append(Integer.toHexString(v));
        }

        return stringBuilder.toString();
    };

    public static String setEmptyErrorText(String nameOfInput) {
        return nameOfInput + " cannot be empty";
    }
}

