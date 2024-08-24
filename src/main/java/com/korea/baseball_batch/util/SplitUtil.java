package com.korea.baseball_batch.util;

public class SplitUtil {

    public static String splitDate(String date, String regex, int i) {
        String[] strings = date.split(regex);
        return strings[i].trim();
    }

}
