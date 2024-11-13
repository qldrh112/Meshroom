package com.ssafy.meshroom.backend.global.util;

public class HangulToChosung {
    // 한글 초성 유니코드 값 배열
    private static final char[] CHOSUNG = {
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ',
            'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
    };

    public static String convertToChosung(String word) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);

            // 한글 여부 확인 (가 ~ 힣 범위 내)
            if (ch >= 0xAC00 && ch <= 0xD7A3) {
                // 한글의 초성 추출
                int chosungIndex = (ch - 0xAC00) / (21 * 28);
                sb.append(CHOSUNG[chosungIndex]);
            } else {
                // FIXME: 한글이 아닌 경우
                // 그대로 추가
                sb.append(ch);
            }
        }

        return sb.toString();
    }
}
