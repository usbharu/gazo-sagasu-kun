package io.github.usbharu.imagesearch.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class TimeOutTest {

  static Runnable runnable;
  static Runnable dontRunnable;


  @BeforeAll
  static void beforeAll() {
    runnable = new Runnable() {
      @Override
      public void run() {
        System.out.println("runnable");
      }
    };
    dontRunnable = new Runnable() {
      @Override
      public void run() {
        System.out.println("dontRunnable");
        try {
          Thread.sleep(2000);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
        System.out.println("dontRunnable2");
      }
    };
  }

  @Test
  void with_withNoOption_success() {
    assertDoesNotThrow(() -> TimeOut.with(runnable));
  }

  @Test
  void with_withNoOption_fail() {
    assertThrows(TimeoutException.class, () -> TimeOut.with(dontRunnable));
  }

  @Test
  void with_withNegativeTime_fail()
      throws ExecutionException, InterruptedException, TimeoutException {
    assertThrows(IllegalArgumentException.class,() -> TimeOut.with(runnable,-1000));
  }
}
