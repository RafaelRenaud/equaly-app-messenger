package com.br.equaly.messenger.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilTools {

    public static String formatTimestamp(String timestamp){
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
