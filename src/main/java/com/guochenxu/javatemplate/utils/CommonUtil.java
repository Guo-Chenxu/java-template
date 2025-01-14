package com.guochenxu.javatemplate.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;

@Slf4j
public class CommonUtil {
    private static final String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String CHAR_UPPER = CHAR_LOWER.toUpperCase();
    private static final String NUMBER = "0123456789";
    private static final String OTHER_CHAR = "!@#$%^&*()_+-=[]|,./?><~`";

    private static final String PASSWORD_ALLOW_CHAR = CHAR_LOWER + CHAR_UPPER + NUMBER;
    private static final SecureRandom random = new SecureRandom();

    public static String formatPhone(String phone) {
        phone = phone.replace(" ", "");
        if (phone.startsWith("1")) {
            phone = "+86" + phone;
        } else if (phone.startsWith("86")) {
            phone = "+" + phone;
        }
        return phone;
    }

    /**
     * num补成n位的字符串
     */
    public static String padZero(int n, int num) {
        return String.format("%0" + n + "d", num);
    }

    public static String generateStrongPassword(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Password length must be positive.");
        }

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(PASSWORD_ALLOW_CHAR.length());
            sb.append(PASSWORD_ALLOW_CHAR.charAt(randomIndex));
        }
        return sb.toString();
    }
}
