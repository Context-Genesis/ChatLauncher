package com.contextgenesis.chatlauncher.utils;

import androidx.annotation.Nullable;

public class StringUtils extends org.apache.commons.lang3.StringUtils {
    /**
     * @param text input text
     * @param n    index from the input text
     * @return nth item from the input text split by space
     */
    @Nullable
    public static String getNthString(String text, int n) {
        if (text == null || n < 0) {
            return null;
        }
        String[] split = text.split(" ");
        if (split.length == 0) {
            return text;
        } else if (n >= split.length) {
            return null;
        } else {
            return split[n];
        }
    }
}
