package io.github.usbharu.imagesearch.util;

import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TimeOut {

  private TimeOut() {
  }

  public static void with(Runnable runnable)
      throws ExecutionException, InterruptedException, TimeoutException {
    with(runnable, 1000);
  }

  public static void with(Runnable runnable, long timeout)
      throws InterruptedException, ExecutionException, TimeoutException {
    with(runnable, timeout, TimeUnit.MILLISECONDS);
  }

  public static void with(Runnable runnable, long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException {
    Objects.requireNonNull(runnable, "Runnable is Null");
    if (timeout <= 0) {
      throw new IllegalArgumentException("Timeout is negative or null");
    }
    ExecutorService es = Executors.newSingleThreadExecutor();
    Future<?> future = es.submit(runnable);
    future.get(timeout, unit);
  }
}
