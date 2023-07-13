/*
 * Copyright (C) 2023 Dynamic Solutions
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.ds.kyanite.components.utils;

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

        String shortMonthName = providedDate.getMonth().toString().substring(0, 1).toUpperCase()
            + providedDate.getMonth().toString().substring(1, 3).toLowerCase();

        return providedDateYear == LocalDate.now().getYear() ? shortMonthName + " "
            + providedDateDay : shortMonthName + " " + providedDateDay + " " + providedDateYear;
      } catch (DateTimeParseException e) {
        return null;
      }
    }

    return null;
  }
}