package org.deus_ex_java.util;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TryCatchesOpsTests {
  @Test
  public void testTryCatchRuntimeException() {
    @SuppressWarnings({"NumericOverflow", "divzero"})
    var eitherLeft = TryCatchesOps.wrap(() -> 60 / 0);
    assertTrue(eitherLeft.isLeft());
    assertEquals(ArithmeticException.class, eitherLeft.getLeft().getClass());
    assertEquals("/ by zero", eitherLeft.getLeft().getMessage());
    var eitherRight = TryCatchesOps.wrap(() -> 60 / 2);
    assertTrue(eitherRight.isRight());
    assertEquals(30, eitherRight.getRight());
  }

  private static Stream<Arguments> provideTestTryCatchThrowable() {
    return Stream.of(
        Arguments.of(Throwable.class),
        Arguments.of(Exception.class),
        Arguments.of(RuntimeException.class),
        Arguments.of(ArithmeticException.class));
  }

  @ParameterizedTest
  @MethodSource("provideTestTryCatchThrowable")
  public void testTryCatchThrowable(
      Class<Throwable> exceptionTypeProvided
  ) {
    @SuppressWarnings({"NumericOverflow", "divzero"})
    var eitherLeft = TryCatchesOps.wrap(() -> 60 / 0, exceptionTypeProvided);
    assertTrue(eitherLeft.isLeft());
    assertEquals(ArithmeticException.class, eitherLeft.getLeft().getClass());
    assertEquals("/ by zero", eitherLeft.getLeft().getMessage());
  }

  @Test
  public void testTryCatchThrowableRethrow() {
    var message = "testTryCatchThrowableRethrow message";
    var throwable = assertThrows(
        IllegalStateException.class,
        () -> TryCatchesOps.wrap(() -> {
              throw new IllegalStateException(message);
            },
            ArithmeticException.class));
    assertEquals(message, throwable.getMessage());
  }

  @Test
  public void testTryCatchCheckedException() {
    @SuppressWarnings({"NumericOverflow", "divzero"})
    var eitherLeft = TryCatchesOps.wrapCheckedException(() -> 60 / 0);
    assertTrue(eitherLeft.isLeft());
    assertEquals(ArithmeticException.class, eitherLeft.getLeft().getClass());
    assertEquals("/ by zero", eitherLeft.getLeft().getMessage());
    var eitherRight = TryCatchesOps.wrapCheckedException(() -> 60 / 2);
    assertTrue(eitherRight.isRight());
    assertEquals(30, eitherRight.getRight());
    var eitherLeftB = TryCatchesOps.wrapCheckedException(() -> {
      //noinspection resource
      InputStreamReader.nullReader().reset(); //intentionally throwing a checked exception
      return 10;
    });
    assertTrue(eitherLeftB.isLeft());
    assertEquals(IOException.class, eitherLeftB.getLeft().getClass());
    assertEquals("reset() not supported", eitherLeftB.getLeft().getMessage());
    var throwable = assertThrows(
        IllegalStateException.class,
        () ->
            TryCatchesOps.wrapCheckedException(() -> {
                  //noinspection ConstantValue
                  if (true) {
                    throw new IllegalStateException("generate RuntimeException in an IOException expected context");
                  }
                  //noinspection resource
                  InputStreamReader.nullReader().reset(); //intentionally throwing a checked exception
                  return 10;
                },
                IOException.class));
    assertEquals("generate RuntimeException in an IOException expected context", throwable.getMessage());
  }

  private static Stream<Arguments> provideTestTryCatchCheckedThrowable() {
    return Stream.of(
        Arguments.of(Throwable.class),
        Arguments.of(Exception.class),
        Arguments.of(RuntimeException.class),
        Arguments.of(IOException.class));
  }

  @ParameterizedTest
  @MethodSource("provideTestTryCatchCheckedThrowable")
  public void testTryCatchCheckedThrowable(
      Class<Throwable> exceptionTypeProvided
  ) {
    if (Objects.equals(exceptionTypeProvided.getCanonicalName(), "java.lang.RuntimeException")) {
      var throwable = assertThrows(
          WrappedCheckedException.class,
          () -> TryCatchesOps.wrapCheckedException(() -> {
                //noinspection resource
                InputStreamReader.nullReader().reset(); //intentionally throwing a checked exception
                return 10;
              },
              exceptionTypeProvided));
      assertEquals("wrapChecked(SupplierCheckedException) failure - reset() not supported", throwable.getMessage());
    } else {
      var eitherLeft = TryCatchesOps.wrapCheckedException(() -> {
            //noinspection resource
            InputStreamReader.nullReader().reset(); //intentionally throwing a checked exception
            return 10;
          },
          exceptionTypeProvided);
      assertTrue(eitherLeft.isLeft());
      assertEquals(IOException.class, eitherLeft.getLeft().getClass());
      assertEquals("reset() not supported", eitherLeft.getLeft().getMessage());
    }
  }

  @Test
  public void testRemainingUnimplemented() {
    //throw new MissingImplementationException("x? remaining to write, the multiple class signatures not tested specifically");
  }
}
