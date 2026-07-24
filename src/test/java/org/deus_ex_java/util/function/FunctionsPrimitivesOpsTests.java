package org.deus_ex_java.util.function;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionsPrimitivesOpsTests {

  private static class CustomRuntimeException extends RuntimeException {
    public CustomRuntimeException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  @Test
  public void testBooleanSupplierWrapCheckedException() {
    BooleanSupplierCheckedException sPass = () -> true;
    var bSupplier = FunctionsPrimitivesOps.wrapCheckedException(sPass);
    assertTrue(bSupplier.getAsBoolean());

    BooleanSupplierCheckedException sFail = () -> {
      throw new IOException("BooleanSupplier IOException");
    };
    var wrappedFail = FunctionsPrimitivesOps.wrapCheckedException(sFail);
    var wrappedEx = assertThrows(WrappedCheckedException.class, wrappedFail::getAsBoolean);
    assertInstanceOf(IOException.class, wrappedEx.getCause());

    // Custom runtime exception wrapper with checked exception
    var wrappedCustom = FunctionsPrimitivesOps.wrapCheckedException(sFail, ex -> new CustomRuntimeException("CustomBooleanSupplier", ex));
    var exCustom = assertThrows(CustomRuntimeException.class, wrappedCustom::getAsBoolean);
    assertEquals("CustomBooleanSupplier", exCustom.getMessage());
    assertInstanceOf(IOException.class, exCustom.getCause());

    // Fatal exception check
    BooleanSupplierCheckedException sFatal = () -> {
      throw new InterruptedException("Fatal InterruptedException");
    };
    var wrappedFatal = FunctionsPrimitivesOps.wrapCheckedException(sFatal);
    var exFatal = assertThrows(InterruptedException.class, wrappedFatal::getAsBoolean);
    assertEquals("Fatal InterruptedException", exFatal.getMessage());

    // Null check
    assertThrows(NullPointerException.class, () -> FunctionsPrimitivesOps.wrapCheckedException((BooleanSupplierCheckedException) null));
  }

  @Test
  public void testIntConsumerWrapCheckedException() {
    AtomicInteger val = new AtomicInteger(0);
    IntConsumerCheckedException cPass = val::addAndGet;
    var intConsumer = FunctionsPrimitivesOps.wrapCheckedException(cPass);
    intConsumer.accept(10);
    assertEquals(10, val.get());

    IntConsumerCheckedException cFail = v -> {
      throw new IOException("IntConsumer IOException");
    };
    var wrappedFail = FunctionsPrimitivesOps.wrapCheckedException(cFail);
    assertThrows(WrappedCheckedException.class, () -> wrappedFail.accept(5));

    // Custom runtime exception wrapper
    var wrappedCustom = FunctionsPrimitivesOps.wrapCheckedException(cFail, ex -> new CustomRuntimeException("CustomIntConsumer", ex));
    var exCustom = assertThrows(CustomRuntimeException.class, () -> wrappedCustom.accept(5));
    assertEquals("CustomIntConsumer", exCustom.getMessage());
    assertInstanceOf(IOException.class, exCustom.getCause());

    // Null checks
    assertThrows(NullPointerException.class, () -> FunctionsPrimitivesOps.wrapCheckedException((IntConsumerCheckedException) null));
  }

  @Test
  public void testDoubleBinaryOperatorWrapCheckedException() {
    DoubleBinaryOperatorCheckedException opPass = Double::sum;
    var doubleOp = FunctionsPrimitivesOps.wrapCheckedException(opPass);
    assertEquals(7.5, doubleOp.applyAsDouble(2.5, 5.0), 0.0001);

    DoubleBinaryOperatorCheckedException opFail = (l, r) -> {
      throw new IOException("DoubleBinaryOperator IOException");
    };
    var wrappedCustom = FunctionsPrimitivesOps.wrapCheckedException(opFail, ex -> new CustomRuntimeException("CustomDoubleOp", ex));
    var exCustom = assertThrows(CustomRuntimeException.class, () -> wrappedCustom.applyAsDouble(1.0, 2.0));
    assertEquals("CustomDoubleOp", exCustom.getMessage());
    assertInstanceOf(IOException.class, exCustom.getCause());

    DoubleBinaryOperatorCheckedException opFatal = (l, r) -> {
      throw new OutOfMemoryError("Fatal OOM in DoubleBinaryOperator");
    };
    var wrappedFatal = FunctionsPrimitivesOps.wrapCheckedException(opFatal);
    assertThrows(OutOfMemoryError.class, () -> wrappedFatal.applyAsDouble(1.0, 2.0));

    // Null check
    assertThrows(NullPointerException.class, () -> FunctionsPrimitivesOps.wrapCheckedException((DoubleBinaryOperatorCheckedException) null));
  }

  @Test
  public void testIntSupplierWrapCheckedException() {
    IntSupplierCheckedException supplierPass = () -> 42;
    var intSupplier = FunctionsPrimitivesOps.wrapCheckedException(supplierPass);
    assertEquals(42, intSupplier.getAsInt());

    IntSupplierCheckedException supplierIo = () -> {
      throw new IOException("IO error in IntSupplier");
    };
    var wrappedIo = FunctionsPrimitivesOps.wrapCheckedException(supplierIo);
    assertThrows(WrappedCheckedException.class, wrappedIo::getAsInt);

    var wrappedCustom = FunctionsPrimitivesOps.wrapCheckedException(supplierIo, ex -> new CustomRuntimeException("CustomIntSupplier", ex));
    var exCustom = assertThrows(CustomRuntimeException.class, wrappedCustom::getAsInt);
    assertEquals("CustomIntSupplier", exCustom.getMessage());
    assertInstanceOf(IOException.class, exCustom.getCause());

    // Null check
    assertThrows(NullPointerException.class, () -> FunctionsPrimitivesOps.wrapCheckedException((IntSupplierCheckedException) null));
  }

  @Test
  public void testLongFunctionWrapCheckedException() {
    LongFunctionCheckedException<String> fnPass = l -> "Val: " + l;
    var longFn = FunctionsPrimitivesOps.wrapCheckedException(fnPass);
    assertEquals("Val: 100", longFn.apply(100L));

    LongFunctionCheckedException<String> fnFail = l -> {
      throw new IOException("LongFunction IOException");
    };
    var wrappedCustom = FunctionsPrimitivesOps.wrapCheckedException(fnFail, ex -> new CustomRuntimeException("CustomLongFn", ex));
    var exCustom = assertThrows(CustomRuntimeException.class, () -> wrappedCustom.apply(100L));
    assertEquals("CustomLongFn", exCustom.getMessage());
    assertInstanceOf(IOException.class, exCustom.getCause());

    LongFunctionCheckedException<String> fnFatal = l -> {
      throw new InterruptedException("Interrupted LongFunction");
    };
    var wrappedFatal = FunctionsPrimitivesOps.wrapCheckedException(fnFatal);
    assertThrows(InterruptedException.class, () -> wrappedFatal.apply(100L));

    // Null check
    assertThrows(NullPointerException.class, () -> FunctionsPrimitivesOps.wrapCheckedException((LongFunctionCheckedException<String>) null));
  }
}
