package org.deus_ex_java.lang;

import org.junit.jupiter.api.Test;

import java.io.Serial;

import static org.junit.jupiter.api.Assertions.*;

public class ForcedFatalThrowableTests {
  private static final RuntimeException runtimeException = new RuntimeException();
  private static final ControlBreakThrowable controlBreakThrowable = new ControlBreakThrowable() {
    @Serial
    private static final long serialVersionUID = 197941024916184170L;
  };
  private static final VirtualMachineError virtualMachineError = new VirtualMachineError() {
    @Serial
    private static final long serialVersionUID = -2155501631536602697L;
  };
  private static final ThreadDeath threadDeath = new ThreadDeath();
  //checked exception
  private static final InterruptedException interruptedException = new InterruptedException("test");
  private static final LinkageError linkageError = new LinkageError();

  @Test
  public void testIsFatalThrowable() {
    assertFalse(ForcedFatalThrowable.isFatalThrowable(runtimeException));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(controlBreakThrowable));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(virtualMachineError));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(threadDeath));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(interruptedException));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(linkageError));

    // Subclasses of fatal throwable types
    assertTrue(ForcedFatalThrowable.isFatalThrowable(new OutOfMemoryError()));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(new StackOverflowError()));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(new NoClassDefFoundError()));

    // Cause chain wrapped fatal throwables
    assertTrue(ForcedFatalThrowable.isFatalThrowable(new RuntimeException("wrapped", new OutOfMemoryError())));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(new RuntimeException("wrapped", new LinkageError())));
  }

  @Test
  public void testRequireNonFatalThrowable() {
    assertEquals(RuntimeException.class, ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(runtimeException).getClass());
    @SuppressWarnings({"ThrowableNotThrown", "unused"})
    var fatalThrowableControlBreakThrowable = assertThrows(
        ControlBreakThrowable.class,
        () ->
            ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(controlBreakThrowable));
    @SuppressWarnings({"ThrowableNotThrown", "unused"})
    var fatalThrowableVirtualMachineError = assertThrows(
        VirtualMachineError.class,
        () ->
            ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(virtualMachineError));
    @SuppressWarnings({"ThrowableNotThrown", "unused"})
    var fatalThrowableThreadDeath = assertThrows(
        ThreadDeath.class,
        () ->
            ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(threadDeath));
    @SuppressWarnings({"ThrowableNotThrown", "unused"})
    var fatalThrowableInterruptedException = assertThrows(
        InterruptedException.class,
        () ->
            ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(interruptedException));
    assertEquals("test", fatalThrowableInterruptedException.getMessage());
    @SuppressWarnings({"ThrowableNotThrown", "unused"})
    var fatalThrowableLinkageError = assertThrows(
        LinkageError.class,
        () ->
            ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(linkageError));
  }

  @Test
  public void testCyclicExceptionHandling() {
    RuntimeException e1 = new RuntimeException("e1");
    RuntimeException e2 = new RuntimeException("e2");
    e1.initCause(e2);
    e2.initCause(e1);

    // Non-fatal cyclic chain should return false without StackOverflowError
    assertFalse(ForcedFatalThrowable.isFatalThrowable(e1));

    // Cyclic chain containing InterruptedException as a cause
    RuntimeException e3 = new RuntimeException("e3");
    InterruptedException ie = new InterruptedException("cyclic interrupted");
    e3.initCause(ie);
    ie.initCause(e3);

    assertTrue(ForcedFatalThrowable.isFatalThrowable(e3));
  }

  @Test
  public void testThreadInterruptFlagPreservation() {
    // Clear initial interrupted status
    Thread.interrupted();

    InterruptedException testInterrupt = new InterruptedException("test interrupt flag");
    assertThrows(
        InterruptedException.class,
        () -> ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(testInterrupt));

    // Verify interrupt flag on current thread was set
    assertTrue(Thread.currentThread().isInterrupted(), "Thread interrupt flag should be set when re-throwing InterruptedException");

    // Clean up interrupted status
    Thread.interrupted();
  }

  @Test
  public void testSneakyThrowsUnwrappedPropagation() {
    InterruptedException checkedInterrupted = new InterruptedException("sneaky throws test");

    Throwable thrown = assertThrows(
        Throwable.class,
        () -> ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(checkedInterrupted));

    // Verify raw exception was propagated directly without wrapping in RuntimeException or WrappedCheckedException
    assertSame(checkedInterrupted, thrown, "Sneaky throws must propagate exact instance without wrapping");
    assertEquals(InterruptedException.class, thrown.getClass());
  }
}
