package io.github.usbharu.imagesearch.domain.exceptions;

public class IllegalPropertyValueException extends IllegalStateException{

  public IllegalPropertyValueException() {
    super();
  }

  public IllegalPropertyValueException(String s) {
    super(s);
  }

  public IllegalPropertyValueException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalPropertyValueException(Throwable cause) {
    super(cause);
  }
}
