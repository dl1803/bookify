package com.example.bookify.utils;

public class UrlUtils {
    public static String resolveLocalUrl(String url) {
        if (url != null && url.contains("localhost")) {
            return url.replace("localhost", "10.0.2.2");
        }
        return url;
    }
}
