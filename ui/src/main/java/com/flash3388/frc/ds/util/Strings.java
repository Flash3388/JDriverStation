package com.flash3388.frc.ds.util;

public class Strings {

    private Strings() {}

    public static String capitalize(String str) {
        if (str.isEmpty()) {
            return str;
        }

        return str.substring(0, 1).toUpperCase().concat(str.substring(1));
    }
}
