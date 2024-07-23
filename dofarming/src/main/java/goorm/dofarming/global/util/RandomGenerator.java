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

    private static final List<String> adjective = Arrays.asList(
            "눈여겨볼 만한 ", "생동감 있는 ", "대담한 ", "여유로운 ", "화사한 ", "흥미로운 ", "감동적인 ",
            "장엄한 ", "감탄스러운 ", "깨끗한 ", "활기찬 ", "다채로운 ", "산뜻한 ", "고요한 ", "눈부신 ",
            "황홀한 ", "풍성한 ", "아름다운 ", "유쾌한 ", "즐거운 ", "따뜻한 ", "감미로운 ", "포근한 ", "여행하기 좋은 ",
            "명랑한 ", "다정한 ", "이국적인 ", "시원한 ", "매혹적인 ", "상쾌한 ", "자연친화적인 ", "모험적인 ", "낯선 ",
            "기분 좋은 ", "인상적인 ", "걷기 좋아하는 ", "찬란한 ", "낭만적인 ", "환상적인 ", "활달한 ", "잊을 수 없는 "
    );
    private static final List<String> noun = Arrays.asList(
            "휴양객", "승무원", "가이드", "현지인", "동반자", "탐험가", "사진가", "모험가",
            "배낭족", "하드워커", "작가", "여행객", "조종사", "캠퍼들", "등산가", "사업가", "고객",
            "주인", "운전사", "통역사", "유학생", "방문객", "여행자", "크루즈", "캠핑족", "연구자",
            "산악인", "문화인", "이민자", "숙박업자", "유목민", "등반가", "트레커", "애호가", "현지인",
            "연예인", "드라이버", "체험자", "조종사"
    );

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

    public static String generateNickname() {
        List<String> adjectiveCopy = new ArrayList<>(adjective);
        List<String> nounCopy = new ArrayList<>(noun);

        Collections.shuffle(adjectiveCopy);
        Collections.shuffle(nounCopy);

        return adjectiveCopy.get(0) + nounCopy.get(0);
    }

    public static List<String> selectRandomColors(int num) {
        List<String> colorsCopy = new ArrayList<>(colors);
        Collections.shuffle(colorsCopy);

        return colorsCopy.subList(0, num);
    }
}
