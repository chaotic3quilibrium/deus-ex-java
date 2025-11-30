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
  public void testInhibitWrappingFatalThrowables() {
    var interruptedException = new InterruptedException("test");
    var fatalThrowableMessageAndCause = assertThrows(
        InterruptedException.class,
        () -> {
          throw new WrappedCheckedException("ignored", interruptedException);
        });
    var fatalThrowableCause = assertThrows(
        InterruptedException.class,
        () -> {
          throw new WrappedCheckedException(interruptedException);
        });
    var fatalThrowableAllArgs = assertThrows(
        InterruptedException.class,
        () -> {
          throw new WrappedCheckedException("ignored", interruptedException, true, true);
        });
  }
}
