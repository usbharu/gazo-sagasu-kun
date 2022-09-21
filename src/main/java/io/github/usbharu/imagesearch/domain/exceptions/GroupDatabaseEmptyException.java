package io.github.usbharu.imagesearch.domain.exceptions;

public class GroupDatabaseEmptyException extends RuntimeException{

  public GroupDatabaseEmptyException() {
  }

  public GroupDatabaseEmptyException(String message) {
    super(message);
  }

  public GroupDatabaseEmptyException(String message, Throwable cause) {
    super(message, cause);
  }

  public GroupDatabaseEmptyException(Throwable cause) {
    super(cause);
  }

  public GroupDatabaseEmptyException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
