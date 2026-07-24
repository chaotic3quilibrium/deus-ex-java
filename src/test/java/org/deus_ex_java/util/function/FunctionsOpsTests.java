package org.deus_ex_java.util.function;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class FunctionsOpsTests {

  private static class CustomRuntimeException extends RuntimeException {
    public CustomRuntimeException(String message, Throwable cause) {
      super(message, cause);
    }
  }

  @Test
  public void testConstants() {
    assertNotNull(FunctionsOps.NO_OP);
    assertNotNull(FunctionsOps.NO_OP_CHECKED_EXCEPTION);

    assertDoesNotThrow(FunctionsOps.NO_OP::execute);
    assertDoesNotThrow(FunctionsOps.NO_OP_CHECKED_EXCEPTION::execute);
  }

  @Test
  public void testExecuteSideEffectNTimes() {
    var count = 5;
    var arrayCounter = new int[]{0};
    FunctionsOps.executeSideEffectNTimes(
        count,
        () ->
            arrayCounter[0]++);
    assertEquals(count, arrayCounter[0]);
    var reader = new StringReader("x");
    reader.close();
    @SuppressWarnings("ResultOfMethodCallIgnored")
    var wrappedCheckedException = assertThrows(
        WrappedCheckedException.class,
        () ->
            FunctionsOps.executeSideEffectNTimes(2, reader::read));
    assertEquals("java.io.IOException: Stream closed", wrappedCheckedException.getMessage());
    assertEquals(IOException.class, wrappedCheckedException.getCause().getClass());
    assertEquals("Stream closed", wrappedCheckedException.getCause().getMessage());
  }

  @Test
  public void testToAdaptersAndNullSafety() {
    VoidSupplier voidSupplier = () -> {};
    assertNotNull(FunctionsOps.to(voidSupplier));
    assertThrows(NullPointerException.class, () -> FunctionsOps.to((VoidSupplier) null));

    SupplierCheckedException<String> supplier = () -> "hello";
    assertNotNull(FunctionsOps.to(supplier));
    assertThrows(NullPointerException.class, () -> FunctionsOps.to((SupplierCheckedException<String>) null));

    Function2<Integer, Integer, Integer> f2 = Integer::sum;
    assertNotNull(FunctionsOps.to(f2));
    assertThrows(NullPointerException.class, () -> FunctionsOps.to((Function2<Integer, Integer, Integer>) null));

    Function2Checked<Integer, Integer, Integer, IOException> f2Checked = (a, b) -> a + b;
    assertNotNull(FunctionsOps.to(f2Checked));
    assertThrows(NullPointerException.class, () -> FunctionsOps.to((Function2Checked<Integer, Integer, Integer, IOException>) null));

    Function2CheckedException<Integer, Integer, Integer> f2CheckedEx = (a, b) -> a + b;
    assertNotNull(FunctionsOps.to(f2CheckedEx));
    assertThrows(NullPointerException.class, () -> FunctionsOps.to((Function2CheckedException<Integer, Integer, Integer>) null));
  }

  @Test
  public void testIfThenElse() {
    var resultTrue = FunctionsOps.ifThenElse(() -> true, () -> "then", () -> "else").get();
    assertEquals("then", resultTrue._2());
    assertTrue(resultTrue._1());

    var resultFalse = FunctionsOps.ifThenElse(() -> false, () -> "then", () -> "else").get();
    assertEquals("else", resultFalse._2());
    assertFalse(resultFalse._1());

    assertThrows(NullPointerException.class, () -> FunctionsOps.ifThenElse(null, () -> "then", () -> "else"));
    assertThrows(NullPointerException.class, () -> FunctionsOps.ifThenElse(() -> true, null, () -> "else"));
    assertThrows(NullPointerException.class, () -> FunctionsOps.ifThenElse(() -> true, () -> "then", null));
  }

  @Test
  public void testIfThenElseCheckedException() throws Exception {
    var resultTrue = FunctionsOps.ifThenElseCheckedException(() -> true, () -> "then", () -> "else").get();
    assertEquals("then", resultTrue._1());
    assertTrue(resultTrue._2());

    var resultFalse = FunctionsOps.ifThenElseCheckedException(() -> false, () -> "then", () -> "else").get();
    assertEquals("else", resultFalse._1());
    assertFalse(resultFalse._2());

    assertThrows(NullPointerException.class, () -> FunctionsOps.ifThenElseCheckedException(null, () -> "then", () -> "else"));
  }

  @Test
  public void testFatalExceptionPropagationInWrappedFunctions() {
    FunctionCheckedException<String, String> throwingInterrupted = s -> {
      throw new InterruptedException("Fatal thread interrupt");
    };

    var wrappedInterrupted = FunctionsOps.wrapCheckedException(throwingInterrupted);
    var exInterrupted = assertThrows(InterruptedException.class, () -> wrappedInterrupted.apply("test"));
    assertEquals("Fatal thread interrupt", exInterrupted.getMessage());

    SupplierCheckedException<String> throwingOom = () -> {
      throw new OutOfMemoryError("Fatal OOM");
    };

    var wrappedOom = FunctionsOps.wrapCheckedException(throwingOom);
    var exOom = assertThrows(OutOfMemoryError.class, wrappedOom::get);
    assertEquals("Fatal OOM", exOom.getMessage());
  }

  @Test
  public void testCheckedExceptionWrapping() {
    FunctionCheckedException<String, String> throwingIo = s -> {
      throw new IOException("Disk failure");
    };

    var wrappedIo = FunctionsOps.wrapCheckedException(throwingIo);
    var wrappedEx = assertThrows(WrappedCheckedException.class, () -> wrappedIo.apply("test"));
    assertInstanceOf(IOException.class, wrappedEx.getCause());
    assertEquals("Disk failure", wrappedEx.getCause().getMessage());
  }

  @Test
  public void testCustomRuntimeExceptionWrapperForHigherArityAndAdapters() {
    // 1. BiFunctionCheckedException
    BiFunctionCheckedException<String, String, String> biFnIo = (a, b) -> {
      throw new IOException("BiFunction IO error");
    };
    var wrappedBiFn = FunctionsOps.wrapCheckedException(biFnIo, ex -> new CustomRuntimeException("CustomBiFn", ex));
    var exBiFn = assertThrows(CustomRuntimeException.class, () -> wrappedBiFn.apply("a", "b"));
    assertEquals("CustomBiFn", exBiFn.getMessage());
    assertInstanceOf(IOException.class, exBiFn.getCause());

    // 2. Function2CheckedException
    Function2CheckedException<Integer, Integer, Integer> fn2Io = (a, b) -> {
      throw new IOException("Function2 IO error");
    };
    var wrappedFn2 = FunctionsOps.wrapCheckedException(fn2Io, ex -> new CustomRuntimeException("CustomFn2", ex));
    var exFn2 = assertThrows(CustomRuntimeException.class, () -> wrappedFn2.apply(1, 2));
    assertEquals("CustomFn2", exFn2.getMessage());
    assertInstanceOf(IOException.class, exFn2.getCause());

    // 3. BiPredicateCheckedException
    BiPredicateCheckedException<String, String> biPredIo = (a, b) -> {
      throw new IOException("BiPredicate IO error");
    };
    var wrappedBiPred = FunctionsOps.wrapCheckedException(biPredIo, ex -> new CustomRuntimeException("CustomBiPred", ex));
    var exBiPred = assertThrows(CustomRuntimeException.class, () -> wrappedBiPred.test("a", "b"));
    assertEquals("CustomBiPred", exBiPred.getMessage());
    assertInstanceOf(IOException.class, exBiPred.getCause());

    // 4. BinaryOperatorCheckedException
    BinaryOperatorCheckedException<String> binOpIo = (a, b) -> {
      throw new IOException("BinaryOperator IO error");
    };
    var wrappedBinOp = FunctionsOps.wrapCheckedException(binOpIo, ex -> new CustomRuntimeException("CustomBinOp", ex));
    var exBinOp = assertThrows(CustomRuntimeException.class, () -> wrappedBinOp.apply("a", "b"));
    assertEquals("CustomBinOp", exBinOp.getMessage());
    assertInstanceOf(IOException.class, exBinOp.getCause());

    // 5. Function3CheckedException through Function10CheckedException
    Function3CheckedException<String, String, String, String> fn3Io = (a, b, c) -> {
      throw new IOException("Function3 IO error");
    };
    var wrappedFn3 = FunctionsOps.wrapCheckedException(fn3Io, ex -> new CustomRuntimeException("CustomFn3", ex));
    var exFn3 = assertThrows(CustomRuntimeException.class, () -> wrappedFn3.apply("a", "b", "c"));
    assertEquals("CustomFn3", exFn3.getMessage());
    assertInstanceOf(IOException.class, exFn3.getCause());
  }

  @Test
  public void testNullEntryGuards() {
    assertThrows(NullPointerException.class, () -> FunctionsOps.wrapCheckedException((FunctionCheckedException<String, String>) null));
    assertThrows(NullPointerException.class, () -> FunctionsOps.wrapCheckedException((ConsumerCheckedException<String>) null));
    assertThrows(NullPointerException.class, () -> FunctionsOps.wrapCheckedException((SupplierCheckedException<String>) null));
    FunctionCheckedException<String, String> fn = s -> s;
    assertThrows(NullPointerException.class, () -> FunctionsOps.wrapCheckedException(fn, null));
  }
}
