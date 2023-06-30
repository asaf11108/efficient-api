package com.example.api.qs;

import java.util.regex.Pattern;

public class Format {
    private static final Pattern percentTwenties = Pattern.compile("%20");

    public static String RFC1738(String value) {
        return percentTwenties.matcher(value).replaceAll("+");
    }

    public static String RFC3986(String value) {
        return String.valueOf(value);
    }
}
