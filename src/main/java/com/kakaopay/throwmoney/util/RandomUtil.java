package com.kakaopay.throwmoney.util;

import java.util.Random;

public class RandomUtil {

    private static Random random = new Random();

    public static int randomMoney(int totalMoney) {
        return random.nextInt(totalMoney - 2) + 1;
    }

    public static String createToken() {
        StringBuilder token = new StringBuilder();

        while(token.length() < 3) {
            int alphabet = random.nextInt(57) + 65;

            if(Character.isAlphabetic((char)alphabet)) {
                token.append((char) alphabet);
            }
        }

        return token.toString();
    }
}
