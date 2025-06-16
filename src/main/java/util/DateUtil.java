package util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateUtil {

    // Formato para datas exibidas na UI (entrada do usuário)
    private static final DateTimeFormatter UI_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // Formato para datas lidas do banco de dados (SQL DATE)
    private static final DateTimeFormatter DB_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    // Formato para data e hora do banco de dados (TIMESTAMP)
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATETIME_FULL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");


    // Formata LocalDate para exibição na UI
    public static String formatLocalDate(LocalDate date) {
        if (date == null) {
            return null;
        }
        return date.format(UI_DATE_FORMATTER);
    }

    // Tenta parsear LocalDate: primeiro UI (dd/MM/yyyy), depois DB (yyyy-MM-dd)
    public static LocalDate parseLocalDate(String dateString) throws DateTimeParseException {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            // Tenta formato dd/MM/yyyy (para entrada do usuário)
            return LocalDate.parse(dateString, UI_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            // Se falhar, tenta formato yyyy-MM-dd (para dados do banco)
            return LocalDate.parse(dateString, DB_DATE_FORMATTER);
        }
    }

    public static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return dateTime.format(DATETIME_FORMATTER);
    }

    public static LocalDateTime parseLocalDateTime(String dateTimeString) throws DateTimeParseException {
        if (dateTimeString == null || dateTimeString.trim().isEmpty()) {
            return null;
        }
        try {
            // Tenta formato com milissegundos
            return LocalDateTime.parse(dateTimeString, DATETIME_FULL_FORMATTER);
        } catch (DateTimeParseException e) {
            // Se falhar, tenta formato sem milissegundos
            return LocalDateTime.parse(dateTimeString, DATETIME_FORMATTER);
        }
    }
}