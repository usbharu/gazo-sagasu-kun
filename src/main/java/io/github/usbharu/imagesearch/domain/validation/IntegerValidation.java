package io.github.usbharu.imagesearch.domain.validation;

public interface IntegerValidation {

  default int positive(int number) {
    return positive(number, "");
  }

  default int positive(int number, String message) {
    if (number > 0) {
      return number;
    }
    throw new IllegalArgumentException(message);
  }

  default int positiveOrZero(int number) {
    return positiveOrZero(number, "");
  }

  default int positiveOrZero(int number, String message) {
    if (number >= 0) {
      return number;
    }
    throw new IllegalArgumentException(message);
  }

  default int negative(int number, String message) {
    if (number < 0) {
      return number;
    }
    throw new IllegalArgumentException(message);
  }

  default int negative(int number) {
    return negative(number, "");
  }

  default int negativeOrZero(int number, String message) {
    if (number <= 0) {
      return number;
    }
    throw new IllegalArgumentException(message);
  }

  default int negativeOrZero(int number) {
    return negativeOrZero(number, "");
  }
}
