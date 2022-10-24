package io.github.usbharu.imagesearch.domain.validation;

public class Validation implements IntegerValidation, StringValidation {

  private static final Validation VALIDATION = new Validation();

  private Validation() {
  }

  public static Validation require() {
    return VALIDATION;
  }
}
