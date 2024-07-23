package goorm.dofarming.global.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RandomGenerator {

    private static final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    private static final  List<String> colors = Arrays.asList(
            "AFC2FF", "93E0B0", "67B5A0", "CFB6FF", "97D8F0", "FFD3B6",
            "DFE199", "FFCEEA", "F2BCBE", "FFB22C", "F4E35E", "DFDAD5"
    );

    public static String generateCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(RANDOM.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

    public static List<String> selectRandomColors(int num) {
        List<String> colorsCopy = new ArrayList<>(colors);
        Collections.shuffle(colorsCopy);

        return colorsCopy.subList(0, num);
    }
}
