package com.guochenxu.javatemplate.utils;

import com.guochenxu.javatemplate.entity.AdminUser;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 哈希工具
 *
 * @author: guochenxu
 * @create: 2024-10-30 21:39
 * @version: 1.0
 */
@Slf4j
public class HashUtil {

    /**
     * sha256加密
     */
    public static String sha256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return bytesToHex(hash);
        } catch (Exception e) {
            log.error("hash error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * MD5加密方法
     *
     * @param input 输入字符串
     * @return 32位小写MD5哈希值
     */
    public static String md5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            log.error("hash error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        String p = "superadmin135@";
        System.out.println(sha256(p));
        AdminUser adminUser = new AdminUser();
        adminUser.setPassword(p);
        adminUser.encryptPassword();
        System.out.println(adminUser.getPassword());
        System.out.println(adminUser.getPassword().equals("87427f22d36fef8e48bf6c4c67ff53be4983c3e4f2c84b426ec390223f26ed01"));
    }
}
