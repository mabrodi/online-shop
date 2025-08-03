package org.dimchik.crypto;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptoHash {
    private final static String ALGORITHM = "SHA-256";
    private final static String SALT = "specific salt";

    public static String hash(String input) {
        String saltedInput = input + SALT;

        try {
            MessageDigest digest = MessageDigest.getInstance(ALGORITHM);
            byte[] hash = digest.digest(saltedInput.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte b : hash) {
                stringBuilder.append(String.valueOf(b));
            }

            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Hash algorithm not available: " + ALGORITHM, e);
        }
    }

    public static boolean isMatch(String input, String hashedInput) {
        String hashOfInput = hash(input);

        return hashOfInput.equals(hashedInput);
    }
}
