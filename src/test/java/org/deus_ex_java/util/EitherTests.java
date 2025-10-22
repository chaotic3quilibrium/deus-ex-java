package org.deus_ex_java.util;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * File: org.java_underling.util.EitherTests.java
 * <p>
 * Version: v2025.08.02
 *
 * <p>
 **/
public class EitherTests {

  private void validateLeft(Integer leftValue, Either<Integer, String> eitherLeft) {
    assertTrue(eitherLeft.isLeft());
    assertFalse(eitherLeft.isRight());
    assertEquals(leftValue, eitherLeft.getLeft());
    var noSuchElementExceptionLeft =
        assertThrows(
            NoSuchElementException.class,
            eitherLeft::getRight);
    assertEquals(
        "No value present",
        noSuchElementExceptionLeft.getMessage());
  }

  private void validateRight(String rightValue, Either<Integer, String> eitherRight) {
    assertFalse(eitherRight.isLeft());
    assertTrue(eitherRight.isRight());
    assertEquals(rightValue, eitherRight.getRight());
    var noSuchElementExceptionLeft =
        assertThrows(
            NoSuchElementException.class,
            eitherRight::getLeft);
    assertEquals(
        "No value present",
        noSuchElementExceptionLeft.getMessage());
  }

  @Test
  public void testFactoryOptionals() {
    var expectedErrorMessage = "leftOptional.isEmpty() must not be equal to rightOptional.isEmpty()";
    var nullIllegalArgumentExceptionEmpties =
        assertThrows(
            IllegalArgumentException.class,
            () -> Either.from(Optional.empty(), Optional.empty()));
    assertEquals(
        expectedErrorMessage,
        nullIllegalArgumentExceptionEmpties.getMessage());
    var nullIllegalArgumentExceptionNonEmpties =
        assertThrows(
            IllegalArgumentException.class,
            () -> Either.from(Optional.of(1), Optional.of(2)));
    assertEquals(
        expectedErrorMessage,
        nullIllegalArgumentExceptionNonEmpties.getMessage());
  }


  //The testFactory* tests also test via the validate* methods:
  //  - isLeft()
  //  - isRight()
  //  - getLeft()
  //  - getRight()

  @Test
  public void testFactoryLeft() {
    Either<Integer, String> eitherLeft = Either.left(10);
    validateLeft(10, eitherLeft);
  }

  @Test
  public void testFactoryRight() {
    Either<Integer, String> eitherRight = Either.right("Eleven");
    validateRight("Eleven", eitherRight);
  }

  @Test
  public void testFactoryFromLeft() {
    Either<Integer, String> eitherFromLeft = Either.from(() -> 30, Optional.empty());
    validateLeft(30, eitherFromLeft);
  }

  @Test
  public void testFactoryFromRight() {
    Either<Integer, String> eitherFromRight = Either.from(() -> 32, Optional.of("ThirtyOne"));
    validateRight("ThirtyOne", eitherFromRight);
  }

  @Test
  public void testEquals() {
    Either<Integer, String> eitherFromRightA = Either.from(() -> 32, Optional.of("ThirtyOne"));
    //noinspection EqualsWithItself
    assertEquals(eitherFromRightA, eitherFromRightA);
    //noinspection SimplifiableAssertion
    assertFalse(eitherFromRightA.equals(new Object())); //cannot use assertNotEquals() as it never makes the call to Either.equals()
    Either<Integer, String> eitherFromRightB = Either.from(() -> 32, Optional.of("ThirtyOne"));
    assertEquals(eitherFromRightA, eitherFromRightB);
    Either<Integer, String> eitherFromRightC = Either.from(() -> 33, Optional.of("Thirty"));
    assertNotEquals(eitherFromRightA, eitherFromRightC);
    Either<Integer, String> eitherFromLeftA = Either.left(24);
    assertNotEquals(eitherFromRightA, eitherFromLeftA);
  }

  @Test
  public void testHashCode() {
    Either<Integer, String> eitherFromRightA = Either.from(() -> 32, Optional.of("ThirtyOne"));
    var set = Set.of(eitherFromRightA);
    assertTrue(set.contains(eitherFromRightA));
    Either<Integer, String> eitherFromRightB = Either.from(() -> 32, Optional.of("ThirtyOne"));
    assertTrue(set.contains(eitherFromRightB));
    assertEquals(eitherFromRightA.hashCode(), eitherFromRightB.hashCode());
    Either<Integer, String> eitherFromRightC = Either.from(() -> 33, Optional.of("Thirty"));
    assertFalse(set.contains(eitherFromRightC));
    assertNotEquals(eitherFromRightA.hashCode(), eitherFromRightC.hashCode());
    Either<Integer, String> eitherFromLeftA = Either.left(24);
    assertFalse(set.contains(eitherFromLeftA));
    assertNotEquals(eitherFromRightA.hashCode(), eitherFromLeftA.hashCode());
  }

  @Test
  public void testToOptional() {
    Either<Integer, String> eitherFromLeft = Either.from(() -> 40, Optional.empty());
    assertTrue(eitherFromLeft.toOptional().isEmpty());
    Either<Integer, String> eitherFromRight = Either.from(() -> 42, Optional.of("FortyOne"));
    assertTrue(eitherFromRight.toOptional().isPresent());
    assertEquals("FortyOne", eitherFromRight.toOptional().get());
  }

  @Test
  public void testMapLeft() {
    Either<Integer, String> eitherLeft = Either.left(30);
    var eitherLeftTransformed = eitherLeft.mapLeft(Object::toString);
    assertTrue(eitherLeftTransformed.isLeft());
    assertFalse(eitherLeftTransformed.isRight());
    assertEquals("30", eitherLeftTransformed.getLeft());
    var nullReturnValuePointerException =
        assertThrows(
            NullPointerException.class,
            () -> eitherLeftTransformed.mapLeft(integer -> (String) null));
    assertNull(nullReturnValuePointerException.getMessage());
    //mapRight should return an equivalent instance
    assertEquals(
        eitherLeftTransformed,
        eitherLeftTransformed.mapRight(string -> "should never get here"));
  }

  @Test
  public void testMap() {
    Either<Integer, String> eitherRight = Either.right("30");
    var eitherRightTransformed = eitherRight.map(Integer::parseInt);
    assertFalse(eitherRightTransformed.isLeft());
    assertTrue(eitherRightTransformed.isRight());
    assertEquals(30, eitherRightTransformed.getRight());
    //Since map is forwarded to mapRight, skipping writing redundant tests
  }

  @Test
  public void testMapRight() {
    Either<Integer, String> eitherRight = Either.right("31");
    var eitherRightTransformed = eitherRight.mapRight(Integer::parseInt);
    assertFalse(eitherRightTransformed.isLeft());
    assertTrue(eitherRightTransformed.isRight());
    assertEquals(31, eitherRightTransformed.getRight());
    var nullReturnValuePointerException =
        assertThrows(
            NullPointerException.class,
            () -> eitherRightTransformed.mapRight(string -> (Integer) null));
    assertNull(nullReturnValuePointerException.getMessage());
    //mapLeft should return an equivalent instance
    assertEquals(
        eitherRightTransformed,
        eitherRightTransformed.mapLeft(integer ->
            Integer.MAX_VALUE)); //should never get to the lambda
  }

  @Test
  public void testFlatMapLeft() {
    Either<Integer, String> eitherLeft = Either.left(40);
    var eitherLeftTransformed = eitherLeft.flatMapLeft(l -> Either.left(l.toString()));
    assertTrue(eitherLeftTransformed.isLeft());
    assertFalse(eitherLeftTransformed.isRight());
    assertEquals("40", eitherLeftTransformed.getLeft());
    var nullReturnValuePointerException =
        assertThrows(
            NullPointerException.class,
            () -> eitherLeftTransformed.mapLeft(integer -> (String) null));
    assertNull(nullReturnValuePointerException.getMessage());
    //mapRight should return an equivalent instance
    assertEquals(
        eitherLeftTransformed,
        eitherLeftTransformed.mapRight(string -> "should never get here"));
    Either<Integer, String> eitherRight = Either.right("fortyOne");
    assertSame(eitherRight, eitherRight.flatMapLeft(l -> Either.left(l - 1)));
  }

  @Test
  public void testFlatMap() {
    Either<Integer, String> eitherRight = Either.right("40");
    var eitherRightTransformed = eitherRight.flatMap(r -> Either.right(Integer.parseInt(r)));
    assertFalse(eitherRightTransformed.isLeft());
    assertTrue(eitherRightTransformed.isRight());
    assertEquals(40, eitherRightTransformed.getRight());
    //Since flatMap is forwarded to flatMapRight, skipping writing redundant tests
  }

  @Test
  public void testFlatMapRight() {
    Either<Integer, String> eitherRight = Either.right("41");
    var eitherRightTransformed = eitherRight.flatMapRight(r -> Either.right(Integer.parseInt(r)));
    assertFalse(eitherRightTransformed.isLeft());
    assertTrue(eitherRightTransformed.isRight());
    assertEquals(41, eitherRightTransformed.getRight());
    var nullReturnValuePointerException =
        assertThrows(
            NullPointerException.class,
            () -> eitherRightTransformed.mapRight(string -> (Integer) null));
    assertNull(nullReturnValuePointerException.getMessage());
    //mapLeft should return an equivalent instance
    assertEquals(
        eitherRightTransformed,
        eitherRightTransformed.mapLeft(integer ->
            Integer.MAX_VALUE)); //should never get to the lambda
    Either<Integer, String> eitherLeft = Either.left(40);
    assertSame(eitherLeft, eitherLeft.flatMapRight(r -> Either.right("40 + 1")));
  }

  @Test
  public void testConvergeFunctions() {
    Either<Integer, Double> eitherLeft = Either.left(50);
    var convergedLeft = eitherLeft.converge(Object::toString, Object::toString);
    assertEquals("50", convergedLeft);
    Either<Integer, Double> eitherRight = Either.right(51.0d);
    var convergedRight = eitherRight.converge(Object::toString, Object::toString);
    assertEquals("51.0", convergedRight);
  }

  @Test
  public void testConvergeIdentityInstance() {
    Either<Integer, Double> eitherLeft = Either.left(50);
    var convergedLeft = eitherLeft.<Number>converge();
    assertEquals(50, convergedLeft);
    Either<Integer, Double> eitherRight = Either.right(51.0d);
    var convergedRight = eitherRight.<Number>converge();
    assertEquals(51.0d, convergedRight);
  }

  @Test
  public void testConvergeIdentityStatic() {
    Either<Integer, Double> eitherLeft = Either.left(50);
    var convergedLeft = Either.converge(eitherLeft);
    assertEquals(50, convergedLeft);
    Either<Integer, Double> eitherRight = Either.right(51.0d);
    var convergedRight = Either.converge(eitherRight);
    assertEquals(51.0d, convergedRight);
  }

  @Test
  public void testForEach() {
    Either<Integer, String> eitherLeft = Either.left(60);
    var leftLr = new boolean[2];
    eitherLeft.forEach(
        l -> leftLr[0] = true,
        r -> leftLr[1] = true);
    assertArrayEquals(new boolean[]{true, false}, leftLr);
    Either<Integer, String> eitherRight = Either.right("SixtyOne");
    var rightLr = new boolean[2];
    eitherRight.forEach(
        l -> rightLr[0] = true,
        r -> rightLr[1] = true);
    assertArrayEquals(new boolean[]{false, true}, rightLr);
  }

  @Test
  public void testFilterOrElse() {
    var integer = 162;
    var string = "OneSixtyThree";
    Either<Integer, String> eitherLeft = Either.left(integer);
    Either<Integer, String> eitherRight = Either.right(string);
    var eitherRight2 = eitherRight.filterOrElse(r ->
        Objects.equals(r, string), () -> integer);
    assertEquals(eitherRight, eitherRight2);
    var eitherRight3 = eitherRight.filterOrElse(r ->
        !Objects.equals(r, string), () -> integer);
    assertEquals(eitherLeft, eitherRight3);
    var eitherRight4 = eitherLeft.filterOrElse(r ->
        Objects.equals(r, string), () -> integer);
    assertEquals(eitherLeft, eitherRight4);
  }

  @Test
  public void testFilterOrElseLeft() {
    var integer = 60;
    var string = "SixtyOne";
    Either<Integer, String> eitherLeft = Either.left(integer);
    Either<Integer, String> eitherRight = Either.right(string);
    var eitherLeft2 = eitherLeft.filterOrElseLeft(l ->
        Objects.equals(l, integer), () -> string);
    assertEquals(eitherLeft, eitherLeft2);
    var eitherLeft3 = eitherLeft.filterOrElseLeft(l ->
        !Objects.equals(l, integer), () -> string);
    assertEquals(eitherRight, eitherLeft3);
    var eitherLeft4 = eitherRight.filterOrElseLeft(l ->
        Objects.equals(l, integer), () -> string);
    assertEquals(eitherRight, eitherLeft4);
  }

  @Test
  public void testFilterOrElseRight() {
    var integer = 62;
    var string = "SixtyThree";
    Either<Integer, String> eitherLeft = Either.left(integer);
    Either<Integer, String> eitherRight = Either.right(string);
    var eitherRight2 = eitherRight.filterOrElseRight(r ->
        Objects.equals(r, string), () -> integer);
    assertEquals(eitherRight, eitherRight2);
    var eitherRight3 = eitherRight.filterOrElseRight(r ->
            !Objects.equals(r, string),
        () -> integer);
    assertEquals(eitherLeft, eitherRight3);
    var eitherRight4 = eitherLeft.filterOrElseRight(r ->
        Objects.equals(r, string), () -> integer);
    assertEquals(eitherLeft, eitherRight4);
  }

  @Test
  public void testSwap() {
    var integer = 60;
    Either<Integer, String> eitherLeft = Either.left(integer);
    assertEquals(integer, eitherLeft.getLeft());
    var eitherRight = eitherLeft.swap();
    assertEquals(integer, eitherRight.getRight());
    var eitherLeft2 = eitherRight.swap();
    assertEquals(integer, eitherLeft2.getLeft());
  }

  @Test
  public void testGetRightOrThrowLeft() {
    var integer = 789;
    var string = "SevenNinety";
    var throwableEitherLeftIntegerMessage = "getLeft() [java.lang.Integer] must be an instance of RuntimeException";
    var throwableEitherLeftIllegalArgumentExceptionMessage = "testGetRightOrThrowLeft IllegalArgumentException message";
    var throwableEitherLeftIOExceptionMessage = "getLeft() [java.io.IOException] must be an instance of RuntimeException";
    var throwableEitherLeftIOExceptionSubMessage = "testGetRightOrThrowLeft IOException message";
    Either<Integer, String> eitherLeftInteger = Either.left(integer);
    Either<IllegalArgumentException, String> eitherLeftIllegalArgumentException = Either.left(
        new IllegalArgumentException(throwableEitherLeftIllegalArgumentExceptionMessage));
    Either<IOException, String> eitherLeftIOException = Either.left(
        new IOException(throwableEitherLeftIOExceptionSubMessage));
    Either<IOException, String> eitherRightString = Either.right(string);
    var throwableEitherLeftInteger = assertThrows(
        IllegalStateException.class,
        eitherLeftInteger::getRightOrThrowLeft);
    assertEquals(
        throwableEitherLeftIntegerMessage,
        throwableEitherLeftInteger.getMessage());
    assertNull(throwableEitherLeftInteger.getCause());
    var throwableEitherLeftIllegalArgumentException = assertThrows(
        IllegalArgumentException.class,
        eitherLeftIllegalArgumentException::getRightOrThrowLeft);
    assertEquals(
        throwableEitherLeftIllegalArgumentExceptionMessage,
        throwableEitherLeftIllegalArgumentException.getMessage());
    assertNull(throwableEitherLeftIllegalArgumentException.getCause());
    var throwableEitherLeftIOException = assertThrows(
        IllegalStateException.class,
        eitherLeftIOException::getRightOrThrowLeft);
    assertEquals(
        throwableEitherLeftIOExceptionMessage,
        throwableEitherLeftIOException.getMessage());
    assertNotNull(throwableEitherLeftIOException.getCause());
    assertEquals(
        IOException.class,
        throwableEitherLeftIOException.getCause().getClass());
    assertEquals(
        throwableEitherLeftIOExceptionSubMessage,
        throwableEitherLeftIOException.getCause().getMessage());
    assertEquals(string, eitherRightString.getRightOrThrowLeft());
  }

  @Test
  public void testGetRightOrThrowLeftCheckedException() throws Throwable {
    var integer = 791;
    var string = "SevenNinetyTwo";
    var throwableEitherLeftIntegerMessage = "getLeft() [java.lang.Integer] must be an instance of Throwable";
    var throwableEitherLeftIOExceptionMessage = "testGetRightOrThrowLeft IOException message";
    Either<Integer, String> eitherLeftInteger = Either.left(integer);
    Either<IOException, String> eitherLeftIOException = Either.left(
        new IOException(throwableEitherLeftIOExceptionMessage));
    Either<IOException, String> eitherRightString = Either.right(string);
    var throwableEitherLeftInteger = assertThrows(
        IllegalStateException.class,
        eitherLeftInteger::getRightOrThrowLeftCheckedException);
    assertEquals(
        throwableEitherLeftIntegerMessage,
        throwableEitherLeftInteger.getMessage());
    assertNull(throwableEitherLeftInteger.getCause());
    var throwableEitherLeftIOException = assertThrows(
        IOException.class,
        eitherLeftIOException::getRightOrThrowLeftCheckedException);
    assertEquals(
        throwableEitherLeftIOExceptionMessage,
        throwableEitherLeftIOException.getMessage());
    assertNull(throwableEitherLeftIOException.getCause());
    assertEquals(string, eitherRightString.getRightOrThrowLeftCheckedException());
  }
}
