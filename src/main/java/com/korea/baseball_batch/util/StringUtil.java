package com.korea.baseball_batch.util;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringUtil {

    public static LocalDate dateFormat(String date) {
        int year = LocalDate.now().getYear();
        String string = year + "년 " + date;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일");
        return LocalDate.parse(string, formatter);
    }

    public static LocalTime timeFormat(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return LocalTime.parse(time,formatter);
    }

}
