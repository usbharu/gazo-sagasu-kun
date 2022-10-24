package io.github.usbharu.imagesearch.domain.exceptions;

public class TagDatabaseEmptyException extends RuntimeException {

  public TagDatabaseEmptyException() {
  }

  public TagDatabaseEmptyException(String message) {
    super(message);
  }

  public TagDatabaseEmptyException(String message, Throwable cause) {
    super(message, cause);
  }

  public TagDatabaseEmptyException(Throwable cause) {
    super(cause);
  }

  public TagDatabaseEmptyException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
