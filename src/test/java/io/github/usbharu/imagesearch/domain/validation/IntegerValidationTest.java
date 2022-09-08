package io.github.usbharu.imagesearch.domain.validation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class IntegerValidationTest {

  IntegerValidation integerValidation = new IntegerValidation() {
  };

  @Test
  void positive_positive_returnNumber() {
    assertEquals(1, integerValidation.positive(1));
    assertEquals(815, integerValidation.positive(815));
  }

  @Test
  void positive_zero_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> integerValidation.positive(0));
  }

  @Test
  void positive_negative_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> integerValidation.positive(-1));
    assertThrows(IllegalArgumentException.class, () -> integerValidation.positive(-261));
  }

  @Test
  void positiveWithMessage_positive_returnNumber() {
    assertEquals(1, integerValidation.positive(1, "Number is Zero or Negative"));
    assertEquals(19, integerValidation.positive(19, "Number is Zero or Negative"));
  }

  @Test
  void positiveWithMessage_zero_throwIllegalArgumentException() {
    String message = "Number is Zero.";
    IllegalArgumentException illegalArgumentException =
        assertThrows(IllegalArgumentException.class, () -> integerValidation.positive(0, message));
    assertEquals(message, illegalArgumentException.getMessage());
  }

  @Test
  void positiveWithMessage_negative_throwIllegalArgumentExceptionWithMessage() {
    String message = "Number is Negative.";
    IllegalArgumentException illegalArgumentException =
        assertThrows(IllegalArgumentException.class, () -> integerValidation.positive(-1, message));
    assertEquals(message, illegalArgumentException.getMessage());
  }

  @Test
  void positiveWithMessage_positiveAndNullMessage_returnNumber() {
    assertEquals(1, integerValidation.positive(1, null));
    assertEquals(244, integerValidation.positive(244, null));
  }

  @Test
  void positiveWithMessage_zeroAndNullMessage_throwIllegalArgumentExceptionWithNullMessage() {
    String message = null;
    IllegalArgumentException illegalArgumentException =
        assertThrows(IllegalArgumentException.class, () -> integerValidation.positive(0, message));
    assertEquals(message, illegalArgumentException.getMessage());
  }

  @Test
  void positiveWithMessage_negativeAndNullMessage_throwIllegalArgumentExceptionWithNullMessage() {
    String message = null;
    IllegalArgumentException illegalArgumentException =
        assertThrows(IllegalArgumentException.class, () -> integerValidation.positive(-1, message));
    assertEquals(message, illegalArgumentException.getMessage());
  }

  @Test
  void positiveOrZero_positive_returnNumber() {
    assertEquals(1, integerValidation.positiveOrZero(1));
    assertEquals(44, integerValidation.positiveOrZero(44));
  }

  @Test
  void positiveOrZero_zero_returnZero() {
    assertEquals(0, integerValidation.positiveOrZero(0));
  }

  @Test
  void positiveOrZero_negative_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> integerValidation.positiveOrZero(-510));
    assertThrows(IllegalArgumentException.class, () -> integerValidation.positiveOrZero(-59));
  }

  @Test
  void positiveOrZeroWithMessage_positive_returnNumber() {
    assertEquals(1, integerValidation.positiveOrZero(1, "Number is Negative"));
    assertEquals(795, integerValidation.positiveOrZero(795, "Number is Negative"));
  }

  @Test
  void positiveOrZeroWithMessage_zero_returnZero() {
    assertEquals(0, integerValidation.positiveOrZero(0, "Number is Negative"));
  }

  @Test
  void positiveOrZeroWithMessage_negative_throwIllegalArgumentException() {
    String message = "Number is Negative";
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
        () -> integerValidation.positiveOrZero(-1, message));
    assertEquals(message, illegalArgumentException.getMessage());
  }

  @Test
  void negative_negative_returnNumber() {
    assertEquals(-1, integerValidation.negative(-1));
    assertEquals(-710, integerValidation.negative(-710));
  }

  @Test
  void negative_zero_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> integerValidation.negative(0));
  }

  @Test
  void negative_positive_throwIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> integerValidation.negative(1));
    assertThrows(IllegalArgumentException.class, () -> integerValidation.negative(590));
  }

  @Test
  void negativeWithMessage_negative_returnNumber() {
    assertEquals(-1, integerValidation.negative(-1, "Number is Zero or Positive"));
    assertEquals(-790, integerValidation.negative(-790, "Number is Zero or Positive"));
  }

  @Test
  void negativeWithMessage_zero_throwIllegalArgumentException() {
    String message = "Number is Zero";
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
        () -> integerValidation.negative(0, message));
    assertEquals(message, illegalArgumentException.getMessage());
  }

  @Test
  void negativeWithMessage_positive_throwIllegalArgumentException() {
    String message = "Number is Positive";
    IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
        () -> integerValidation.negative(1, message));
    assertEquals(message, illegalArgumentException.getMessage());
  }

  // TODO: 2022/09/09 残りのテストも書く 
}
