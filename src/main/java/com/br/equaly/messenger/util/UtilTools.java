package com.br.equaly.messenger.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UtilTools {

    public static String formatTimestamp(String timestamp){
        LocalDateTime localDateTime = LocalDateTime.parse(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static String formatCnpj(String cnpj) {
        return cnpj.replaceFirst("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
                "$1.$2.$3/$4-$5");
    }
}
