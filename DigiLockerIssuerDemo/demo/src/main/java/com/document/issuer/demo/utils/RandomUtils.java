package com.document.issuer.demo.utils;

import java.util.Random;

public final class RandomUtils {

    private static final String ALPHA_SALT = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final String NUMERIC_SALT = "0123456789";

    private static final String SPECIAL_CHARACTER = "!#$%&*+-=?@|_";

    private static final String ALPHA_NUMERIC_SALT = NUMERIC_SALT + ALPHA_SALT;

    private static final String ALPHA_NUMERIC_SPECIAL_CHAR_SALT = ALPHA_NUMERIC_SALT + SPECIAL_CHARACTER;

    private static final Random RANDOM = new Random();

    private RandomUtils() {
    }

    /**
     * Generates random numeric string of the given size
     *
     * @param size size of the string
     * @return random numeric string
     */
    public static String generateNumeric(int size) {
        return generateRandomFromString(size, NUMERIC_SALT);
    }

    /**
     * Generates alphabetic random string of the given size
     *
     * @param size size of the string
     * @return random alpha string
     */
    public static String generateAlphaString(int size) {
        return generateRandomFromString(size, ALPHA_SALT);
    }

    /**
     * Generates alpha-numeric random string of the given size
     *
     * @param size size of the string
     * @return random alpha-numeric string
     */
    public static String generateAlphaNumericString(int size) {
        return generateRandomFromString(size, ALPHA_NUMERIC_SALT);
    }

    public static String generateAlphaNumericSpecialCharacterString(int size) {
        return generateRandomFromString(size, ALPHA_NUMERIC_SPECIAL_CHAR_SALT);
    }

    /**
     * Generates random string from the given salt of given size
     *
     * @param size size of the string
     * @param salt string from what the random string to be generated
     * @return random string
     */
    public static String generateRandomFromString(int size, String salt) {
        if (salt == null || salt.isEmpty()) {
            throw new IllegalArgumentException("Salt value cannot be empty");
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            result.append(salt.charAt(RANDOM.nextInt(salt.length())));
        }
        return result.toString();
    }

}