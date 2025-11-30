package org.deus_ex_java.lang;

import org.junit.jupiter.api.Test;

import java.io.Serial;

import static org.junit.jupiter.api.Assertions.*;

public class ForcedFatalThrowableTests {
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
    assertFalse(ForcedFatalThrowable.isFatalThrowable(runtimeException));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(virtualMachineError));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(threadDeath));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(interruptedException));
    assertTrue(ForcedFatalThrowable.isFatalThrowable(linkageError));
  }

  @Test
  public void testRequireNonFatalThrowable() {
    assertEquals(RuntimeException.class, ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(runtimeException).getClass());
    @SuppressWarnings("ThrowableNotThrown")
    var fatalThrowableVirtualMachineError = assertThrows(
        VirtualMachineError.class,
        () ->
            ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(virtualMachineError));
    @SuppressWarnings("ThrowableNotThrown")
    var fatalThrowableThreadDeath = assertThrows(
        ThreadDeath.class,
        () ->
            ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(threadDeath));
    @SuppressWarnings("ThrowableNotThrown")
    var fatalThrowableInterruptedException = assertThrows(
        InterruptedException.class,
        () ->
            ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(interruptedException));
    assertEquals("test", fatalThrowableInterruptedException.getMessage());
    @SuppressWarnings("ThrowableNotThrown")
    var fatalThrowableLinkageError = assertThrows(
        LinkageError.class,
        () ->
            ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(linkageError));
  }
}
