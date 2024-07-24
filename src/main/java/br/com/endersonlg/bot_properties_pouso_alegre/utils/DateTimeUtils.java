package br.com.endersonlg.bot_properties_pouso_alegre.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtils {
  public static String formatISODateTime(String isoDateTimeString, String outputFormat) {
    try {
      // Define o formatador de entrada para ISO 8601
      DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

      // Parse a string para um objeto OffsetDateTime
      OffsetDateTime offsetDateTime = OffsetDateTime.parse(isoDateTimeString, inputFormatter);

      // Define o formatador de saída com o formato desejado
      DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputFormat);

      // Formata a data e retorna a string formatada
      return offsetDateTime.format(outputFormatter);
    } catch (DateTimeParseException e) {
      // Retorna uma mensagem de erro em caso de falha no parsing
      return "Erro ao analisar a data: " + e.getMessage();
    }
  }
}
