package com.guochenxu.javatemplate.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Slf4j
public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128; // Key size in bits

    /**
     * Encrypts the content using AES algorithm and encodes the result to Base64 string.
     *
     * @param content The content to encrypt.
     * @param seed    The seed used for key generation.
     * @return Base64 encoded encrypted content as a string.
     */
    public static String encryptBase64(String content, String seed) {
        byte[] encryptedContent = encrypt(content, seed);
        return encryptedContent != null ? Base64.getEncoder().encodeToString(encryptedContent) : null;
    }

    /**
     * Decrypts the Base64 encoded string using AES algorithm.
     *
     * @param encryptedContent The Base64 encoded encrypted content.
     * @param seed             The seed used for key generation.
     * @return Decrypted content as a string.
     */
    public static String decryptBase64(String encryptedContent, String seed) {
        byte[] decodedContent = Base64.getDecoder().decode(encryptedContent);
        return new String(decrypt(decodedContent, seed), StandardCharsets.UTF_8);
    }

    /**
     * Encrypts the content using AES algorithm.
     *
     * @param content The content to encrypt.
     * @param seed    The seed used for key generation.
     * @return Encrypted content as a byte array.
     */
    private static byte[] encrypt(String content, String seed) {
        try {
            byte[] byteContent = content.getBytes(StandardCharsets.UTF_8);
            return operator(seed, Cipher.ENCRYPT_MODE, byteContent);
        } catch (Exception e) {
            log.error("### AES加密失败", e);
            return null;
        }
    }

    /**
     * Decrypts the encrypted content using AES algorithm.
     *
     * @param encryptedContent The encrypted content.
     * @param seed             The seed used for key generation.
     * @return Decrypted content as a byte array.
     */
    private static byte[] decrypt(byte[] encryptedContent, String seed) {
        try {
            return operator(seed, Cipher.DECRYPT_MODE, encryptedContent);
        } catch (Exception e) {
            log.error("### AES解密失败", e);
            return null;
        }
    }

    /**
     * Performs encryption or decryption based on the mode provided.
     *
     * @param seed        The seed used for key generation.
     * @param mode        The operation mode (ENCRYPT_MODE or DECRYPT_MODE).
     * @param byteContent The content to operate on.
     * @return Operated content as a byte array.
     */
    private static byte[] operator(String seed, int mode, byte[] byteContent) throws Exception {
        SecretKey secretKey = getSecretKey(seed);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, secretKey);
        return cipher.doFinal(byteContent);
    }

    /**
     * Generates a secret key for AES encryption/decryption.
     *
     * @param seed The seed used for key generation.
     * @return A secret key for AES.
     */
    private static SecretKey getSecretKey(String seed) throws NoSuchAlgorithmException {
        KeyGenerator kgen = KeyGenerator.getInstance(ALGORITHM);
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed.getBytes(StandardCharsets.UTF_8));
        kgen.init(KEY_SIZE, secureRandom);
        SecretKey originalKey = kgen.generateKey();
        byte[] keyBytes = originalKey.getEncoded();
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }
}