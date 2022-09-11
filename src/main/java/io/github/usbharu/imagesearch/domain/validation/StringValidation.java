package io.github.usbharu.imagesearch.domain.validation;

public interface StringValidation {

  default String nonNullAndNonBlank(String str, String message) {
    if (str == null) {
      throw new NullPointerException(message);
    }
    if (str.isBlank()) {
      throw new IllegalArgumentException(message);
    }
    return str;
  }

  default String nonNullAndNonBlank(String str) {
    if (str == null) {
      throw new NullPointerException();
    }
    if (str.isBlank()) {
      throw new IllegalArgumentException();
    }
    return str;
  }

}
