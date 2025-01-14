package com.guochenxu.javatemplate.utils;

import com.guochenxu.javatemplate.entity.AdminUser;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;

/**
 * 哈希工具
 *
 * @author: guochenxu
 * @create: 2024-10-30 21:39
 * @version: 1.0
 */
@Slf4j
public class HashUtil {
    public static String hash256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            log.error("hash error", e);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
        String p = "superadmin135@";
        System.out.println(hash256(p));
        AdminUser adminUser = new AdminUser();
        adminUser.setPassword(p);
        adminUser.encryptPassword();
        System.out.println(adminUser.getPassword());
        System.out.println(adminUser.getPassword().equals("87427f22d36fef8e48bf6c4c67ff53be4983c3e4f2c84b426ec390223f26ed01"));
    }
}
