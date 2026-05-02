package org.deus_ex_java.lang;

import org.deus_ex_java.lang.refined.NonEmptyString;
import org.deus_ex_java.util.Either;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class IntegersOpsTests {

  @Test
  public void testFindSetBitIndices() {
    assertEquals(Set.of(), IntegersOps.findSetBitIndices(0));
    assertEquals(
        Set.of(0, 1, 2, 3, 4, 5, 6),
        IntegersOps.findSetBitIndices(127));
    assertEquals(
        List.of(0, 1, 2, 3, 4, 5, 6),
        IntegersOps.findSetBitIndices(127)
            .stream()
            .toList());
    assertEquals(
        Set.of(7),
        IntegersOps.findSetBitIndices(128));
    assertEquals(
        Set.of(0, 1, 2, 3, 4, 5, 6, 7),
        IntegersOps.findSetBitIndices(255));
    assertEquals(
        List.of(0, 1, 2, 3, 4, 5, 6, 7),
        IntegersOps.findSetBitIndices(255)
            .stream()
            .toList());
  }

  @Test
  public void testParseEither() {
    Either<NumberFormatException, Integer> right = Either.right(123);
    assertEquals(right, IntegersOps.parseEither("123"));
    var left = IntegersOps.parseEither("123a");
    assertTrue(left.isLeft());
    var throwableEitherLeftNumberFormatException = assertThrows(
        NumberFormatException.class,
        left::getRightOrThrowLeft);
    assertEquals("""
        For input string: "123a\"""",
        throwableEitherLeftNumberFormatException.getMessage());
    assertNull(throwableEitherLeftNumberFormatException.getCause());
  }

  @Test
  public void testParseOptional() {
    assertTrue(IntegersOps.parseOptional("123").isPresent());
    assertTrue(IntegersOps.parseOptional("123a").isEmpty());
  }
}
