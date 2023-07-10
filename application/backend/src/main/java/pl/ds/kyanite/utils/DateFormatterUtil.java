package pl.ds.kyanite.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DateFormatterUtil {

  private DateFormatterUtil() {
    //unable to initialize util class
  }

  public static String formatDate(String date) {
    if (date != null) {
      try {
        LocalDate providedDate = LocalDate.parse(date);
        int providedDateDay = providedDate.getDayOfMonth();
        int providedDateYear = providedDate.getYear();

        String shortMonthName = providedDate.getMonth().toString().substring(0, 1).toUpperCase() +
            providedDate.getMonth().toString().substring(1, 3).toLowerCase();

        return providedDateYear == LocalDate.now().getYear() ? shortMonthName + " "
            + providedDateDay : shortMonthName + " " + providedDateDay + " " + providedDateYear;
      } catch (DateTimeParseException e) {
        return null;
      }
    }

    return null;
  }
}