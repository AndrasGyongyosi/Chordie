package com.example.ChordCalculator.Helper;

import java.security.SecureRandom;

public class RandomToken {

    static final String ABC = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    public static String randomString(int len) {
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++)
            sb.append(ABC.charAt(rnd.nextInt(ABC.length())));
        return sb.toString();
    }
}
