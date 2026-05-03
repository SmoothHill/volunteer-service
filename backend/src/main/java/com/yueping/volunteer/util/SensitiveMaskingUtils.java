package com.yueping.volunteer.util;

import org.springframework.util.StringUtils;

public final class SensitiveMaskingUtils {

    private SensitiveMaskingUtils() {
    }

    public static String maskRealName(String value) {
        String text = normalize(value);
        if (!StringUtils.hasText(text)) {
            return "未认证";
        }
        return text.substring(0, 1) + "*";
    }

    public static String maskIdCardNo(String value) {
        String text = normalize(value);
        if (!StringUtils.hasText(text)) {
            return "未填写";
        }
        if (text.length() <= 5) {
            return text;
        }
        return text.substring(0, 3)
                + repeatMask(Math.max(1, text.length() - 5))
                + text.substring(text.length() - 2);
    }

    public static String maskPhoneNo(String value) {
        String text = normalize(value);
        if (!StringUtils.hasText(text)) {
            return "未填写";
        }
        if (text.length() <= 7) {
            return text;
        }
        return text.substring(0, 3)
                + repeatMask(Math.max(1, text.length() - 7))
                + text.substring(text.length() - 4);
    }

    public static String maskVolunteerCardNo(String value) {
        String text = normalize(value);
        if (!StringUtils.hasText(text)) {
            return "未填写";
        }
        if (text.length() <= 6) {
            return text;
        }
        return text.substring(0, 3)
                + repeatMask(Math.max(1, text.length() - 6))
                + text.substring(text.length() - 3);
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private static String repeatMask(int count) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < count; index++) {
            builder.append('*');
        }
        return builder.toString();
    }
}
