package pl.ds.kyanite.utils;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

  public static List<String> encryptEmail(String email) {
    int atIndex = email.indexOf("@");
    int dotIndex = email.lastIndexOf(".");
    if (atIndex < 0 || dotIndex < 0) {
      return List.of();
    }
    String emailPart1 = email.substring(0, atIndex);
    String emailPart2 = email.substring(atIndex + 1, dotIndex);
    String emailPart3 = email.substring(dotIndex + 1);
    return Arrays.asList(emailPart1, emailPart2, emailPart3);
  }
}
