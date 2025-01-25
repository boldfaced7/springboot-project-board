package com.boldfaced7;

public class IdParser {
    public static Long parseLong(String id) {
        try {
            return Long.parseLong(id);
        } catch (Exception e) {
            return null;
        }
    }
}
