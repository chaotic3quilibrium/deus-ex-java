package org.deus_ex_java.lang;

import org.junit.jupiter.api.Test;

import java.io.Serial;

import static org.junit.jupiter.api.Assertions.*;

public class FatalThrowableTests {
  private static final RuntimeException runtimeException = new RuntimeException();
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
    assertFalse(FatalThrowable.isFatalThrowable(runtimeException));
    assertTrue(FatalThrowable.isFatalThrowable(virtualMachineError));
    assertTrue(FatalThrowable.isFatalThrowable(threadDeath));
    assertTrue(FatalThrowable.isFatalThrowable(interruptedException));
    assertTrue(FatalThrowable.isFatalThrowable(linkageError));
  }

  @Test
  public void testFilterToFatalThrowable() {
    assertTrue(FatalThrowable.filterToFatalThrowable(runtimeException).isEmpty());
    assertFalse(FatalThrowable.filterToFatalThrowable(virtualMachineError).isEmpty());
    assertFalse(FatalThrowable.filterToFatalThrowable(threadDeath).isEmpty());
    assertFalse(FatalThrowable.filterToFatalThrowable(interruptedException).isEmpty());
    assertFalse(FatalThrowable.filterToFatalThrowable(linkageError).isEmpty());
  }

  @Test
  public void testRequireNonFatalThrowable() {
    assertEquals(RuntimeException.class, FatalThrowable.requireNonFatalThrowable(runtimeException).getClass());
    @SuppressWarnings("ThrowableNotThrown")
    var fatalThrowableVirtualMachineError = assertThrows(
        FatalThrowable.class,
        () ->
            FatalThrowable.requireNonFatalThrowable(virtualMachineError));
    assertTrue(fatalThrowableVirtualMachineError.getMessage().startsWith("FatalThrowable.isFatalThrowable(throwable) must be false - "));
    assertInstanceOf(VirtualMachineError.class, fatalThrowableVirtualMachineError.getCause());
    @SuppressWarnings("ThrowableNotThrown")
    var fatalThrowableThreadDeath = assertThrows(
        FatalThrowable.class,
        () ->
            FatalThrowable.requireNonFatalThrowable(threadDeath));
    assertEquals("FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.ThreadDeath", fatalThrowableThreadDeath.getMessage());
    @SuppressWarnings("ThrowableNotThrown")
    var fatalThrowableInterruptedException = assertThrows(
        FatalThrowable.class,
        () ->
            FatalThrowable.requireNonFatalThrowable(interruptedException));
    assertEquals("FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.InterruptedException - test", fatalThrowableInterruptedException.getMessage());
    @SuppressWarnings("ThrowableNotThrown")
    var fatalThrowableLinkageError = assertThrows(
        FatalThrowable.class,
        () ->
            FatalThrowable.requireNonFatalThrowable(linkageError));
    assertEquals("FatalThrowable.isFatalThrowable(throwable) must be false - java.lang.LinkageError", fatalThrowableLinkageError.getMessage());
  }
}
