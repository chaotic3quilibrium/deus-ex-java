package org.deus_ex_java.lang;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serial;

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

  @SuppressWarnings("unused")
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

  @SuppressWarnings({"unused", "DataFlowIssue"})
  @Test
  public void testFatalThrowableTypesUnmodifiable() {
    assertTrue(WrappedCheckedException.FATAL_THROWABLE_TYPES.contains(ControlBreakThrowable.class));
    assertTrue(WrappedCheckedException.FATAL_THROWABLE_TYPES.contains(InterruptedException.class));
    assertTrue(WrappedCheckedException.FATAL_THROWABLE_TYPES.contains(LinkageError.class));
    assertTrue(WrappedCheckedException.FATAL_THROWABLE_TYPES.contains(ThreadDeath.class));
    assertTrue(WrappedCheckedException.FATAL_THROWABLE_TYPES.contains(VirtualMachineError.class));
    assertEquals(5, WrappedCheckedException.FATAL_THROWABLE_TYPES.size());

    var thrownException = assertThrows(
        UnsupportedOperationException.class,
        () -> WrappedCheckedException.FATAL_THROWABLE_TYPES.add(RuntimeException.class));
  }

  @Test
  public void testIsFatalType() {
    // Direct fatal types
    assertTrue(WrappedCheckedException.isFatalType(ControlBreakThrowable.class));
    assertTrue(WrappedCheckedException.isFatalType(InterruptedException.class));
    assertTrue(WrappedCheckedException.isFatalType(LinkageError.class));
    assertTrue(WrappedCheckedException.isFatalType(ThreadDeath.class));
    assertTrue(WrappedCheckedException.isFatalType(VirtualMachineError.class));

    // Subtypes of fatal types
    assertTrue(WrappedCheckedException.isFatalType(OutOfMemoryError.class));
    assertTrue(WrappedCheckedException.isFatalType(StackOverflowError.class));
    assertTrue(WrappedCheckedException.isFatalType(NoClassDefFoundError.class));

    class CustomControlBreak extends ControlBreakThrowable {
      @Serial
      private static final long serialVersionUID = 1L;
    }
    assertTrue(WrappedCheckedException.isFatalType(CustomControlBreak.class));

    // Non-fatal types
    assertFalse(WrappedCheckedException.isFatalType(IllegalArgumentException.class));
    assertFalse(WrappedCheckedException.isFatalType(IOException.class));
    assertFalse(WrappedCheckedException.isFatalType(RuntimeException.class));
    assertFalse(WrappedCheckedException.isFatalType(Exception.class));

    // Null safety check
    @SuppressWarnings("DataFlowIssue")
    var npe = assertThrows(
        NullPointerException.class,
        () -> WrappedCheckedException.isFatalType(null));
    assertEquals("throwableClass cannot be null", npe.getMessage());
  }

  @SuppressWarnings({"UnnecessaryInitCause", "DataFlowIssue"})
  @Test
  public void testIsFatal() {
    // Direct fatal instances
    assertTrue(WrappedCheckedException.isFatal(new InterruptedException("test")));
    assertTrue(WrappedCheckedException.isFatal(new LinkageError()));
    assertTrue(WrappedCheckedException.isFatal(new ThreadDeath()));
    assertTrue(WrappedCheckedException.isFatal(new OutOfMemoryError("test OOM")));

    // Non-fatal instances
    assertFalse(WrappedCheckedException.isFatal(new IllegalArgumentException("test")));
    assertFalse(WrappedCheckedException.isFatal(new IOException("test")));

    // Cause chain fatal instances
    assertTrue(WrappedCheckedException.isFatal(new RuntimeException("wrapper", new InterruptedException("nested"))));
    assertTrue(WrappedCheckedException.isFatal(new RuntimeException("wrapper", new OutOfMemoryError("nested OOM"))));

    // Cyclic cause chains without fatal exceptions
    RuntimeException e1 = new RuntimeException("e1");
    RuntimeException e2 = new RuntimeException("e2");
    e1.initCause(e2);
    e2.initCause(e1);
    assertFalse(WrappedCheckedException.isFatal(e1));

    // Cyclic cause chains containing fatal exceptions
    RuntimeException e3 = new RuntimeException("e3");
    InterruptedException ie = new InterruptedException("cyclic interrupted");
    e3.initCause(ie);
    ie.initCause(e3);
    assertTrue(WrappedCheckedException.isFatal(e3));

    // Null safety check
    var npe = assertThrows(
        NullPointerException.class,
        () -> WrappedCheckedException.isFatal(null));
    assertEquals("throwable cannot be null", npe.getMessage());
  }
}
