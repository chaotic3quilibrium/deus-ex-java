package org.deus_ex_java.util;

import org.deus_ex_java.lang.MissingImplementationException;
import org.deus_ex_java.lang.WrappedCheckedException;
import org.junit.jupiter.api.Test;

import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

public class UsingCheckedExceptionTests {
  @Test
  public void testSingleResource() {
    var arrayCharsX100 = new char[100];
    var runtimeExceptionOrIntegerA = UsingCheckedException.apply(
        () ->
            new StringReader("x"),
        stringReader ->
            stringReader.read(arrayCharsX100));
    assertTrue(runtimeExceptionOrIntegerA.isRight());
    assertEquals(1, runtimeExceptionOrIntegerA.getRight());
    var wrappedCheckedExceptionOrIntegerB = UsingCheckedException.apply(
        () ->
            new StringReader("x"),
        stringReader -> {
          stringReader.close();

          return stringReader.read(arrayCharsX100);
        });
    assertTrue(wrappedCheckedExceptionOrIntegerB.isLeft());
    assertEquals(WrappedCheckedException.class, wrappedCheckedExceptionOrIntegerB.getLeft().getClass());
    assertEquals(java.io.IOException.class, wrappedCheckedExceptionOrIntegerB.getLeft().getCause().getClass());
    assertEquals("java.io.IOException: Stream closed", wrappedCheckedExceptionOrIntegerB.getLeft().getMessage());
    assertEquals("Stream closed", wrappedCheckedExceptionOrIntegerB.getLeft().getCause().getMessage());
    var integerA = UsingCheckedException.applyUnsafe(
        () ->
            new StringReader("x"),
        stringReader ->
            stringReader.read(arrayCharsX100));
    assertEquals(1, integerA);
    var wrappedCheckedException = assertThrows(
        WrappedCheckedException.class,
        () -> UsingCheckedException.applyUnsafe(
            () ->
                new StringReader("x"),
            stringReader -> {
              stringReader.close();

              return stringReader.read(arrayCharsX100);
            }));
    assertEquals("java.io.IOException: Stream closed", wrappedCheckedException.getMessage());
    assertEquals(java.io.IOException.class, wrappedCheckedException.getCause().getClass());
    assertEquals("Stream closed", wrappedCheckedException.getCause().getMessage());
    var illegalStateException = assertThrows(
        IllegalStateException.class,
        () -> UsingCheckedException.applyUnsafe(
            () ->
                new StringReader("x"),
            stringReader -> {
              //noinspection ConstantValue
              if (true) {
                throw new IllegalStateException("oopsie");
              }

              return stringReader.read(arrayCharsX100);
            }));
    assertEquals("oopsie", illegalStateException.getMessage());
  }

  @Test
  public void testRemainingUnimplemented() {
    //TODO: x16 remaining pathways
    //  - X2 Resources, apply/applyUnsafe/applyNested/applyNestedUnsafe
    //  - X3 Resources, apply/applyUnsafe/applyNested/applyNestedUnsafe
    //  - X4 Resources, apply/applyUnsafe/applyNested/applyNestedUnsafe
    //  - X5 Resources, apply/applyUnsafe/applyNested/applyNestedUnsafe
    //Completed:
    //  - X1 Resource, apply/applyUnsafe

    throw new MissingImplementationException("missing x16 remaining tests");
  }
}
