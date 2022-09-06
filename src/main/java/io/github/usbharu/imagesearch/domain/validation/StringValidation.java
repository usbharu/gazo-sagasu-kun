package io.github.usbharu.imagesearch.domain.validation;

public class StringValidation {

  private StringValidation() {
  }

  public static String requireNonNullAndNonBlank(String str, String message) {
    if (str == null) {
      throw new NullPointerException(message);
    }
    if (str.isBlank()) {
      throw new IllegalArgumentException(message);
    }
    return str;
  }

  public static String requireNonNullAndNonBlank(String str) {
    if (str == null) {
      throw new NullPointerException();
    }
    if (str.isBlank()) {
      throw new IllegalArgumentException();
    }
    return str;
  }

}
