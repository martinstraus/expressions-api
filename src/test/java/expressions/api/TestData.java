/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expressions.api;

import java.util.Random;

/**
 *
 * @author martinstraus
 */
public class TestData {

    private static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private final Random random = new Random();

    public String randomFunctionId() {
        String str = "";
        for (int i = 0; i < 5; i++) {
            str += LETTERS[random.nextInt(LETTERS.length)];
        }
        return "f_" + str;
    }
}
