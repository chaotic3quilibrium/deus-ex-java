package org.deus_ex_java.lang;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WrappedCheckedExceptionTests {
  @Test
  public void testConstructorMessageAndCause() {
    var cause = new IOException("test");
    var wrappedCheckedException = new WrappedCheckedException("test", cause);
    assertEquals("test", wrappedCheckedException.getMessage());
    assertEquals(cause, wrappedCheckedException.getCause());
  }

  @Test
  public void testConstructorCause() {
    var cause = new IOException("test");
    var wrappedCheckedException = new WrappedCheckedException(cause);
    assertEquals(cause, wrappedCheckedException.getCause());
  }

  @Test
  public void testConstructorAll() {
    var cause = new IOException("test");
    var wrappedCheckedException = new WrappedCheckedException("test", cause, false, false);
    assertEquals("test", wrappedCheckedException.getMessage());
    assertEquals(cause, wrappedCheckedException.getCause());
  }

  @Test
  public void testThrowsNullPointerExceptionOnNullCause() {
    @SuppressWarnings({"ThrowableNotThrown", "DataFlowIssue"})
    var nullPointerException = assertThrows(
        NullPointerException.class,
        () ->
            new WrappedCheckedException(null));
    assertNull(nullPointerException.getMessage());
  }

  @Test
  public void testThrowsFatalThrowable() {
    var interruptedException = new InterruptedException("test");
    var fatalThrowableMessage = "FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.InterruptedException - test";
    var fatalThrowableMessageAndCause = assertThrows(
        FatalThrowable.class,
        () -> {
          throw new WrappedCheckedException("ignored", interruptedException);
        });
    assertEquals(fatalThrowableMessage, fatalThrowableMessageAndCause.getMessage());
    var fatalThrowableCause = assertThrows(
        FatalThrowable.class,
        () -> {
          throw new WrappedCheckedException(interruptedException);
        });
    assertEquals(fatalThrowableMessage, fatalThrowableCause.getMessage());
    var fatalThrowableAllArgs = assertThrows(
        FatalThrowable.class,
        () -> {
          throw new WrappedCheckedException("ignored", interruptedException, true, true);
        });
    assertEquals(fatalThrowableMessage, fatalThrowableAllArgs.getMessage());
  }
}
