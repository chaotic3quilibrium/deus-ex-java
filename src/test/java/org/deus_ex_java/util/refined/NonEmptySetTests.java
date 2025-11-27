package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NonEmptySetTests {
  @Test
  public void testDefaultConstructor() {
    assertEquals(Set.of(1), new NonEmptySet<>(Set.of(1)).set());
    var parametersValidationExceptionEmpty = assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptySet<>(Set.of()));
    assertEquals(1, parametersValidationExceptionEmpty.getParametersValidationFailureMessages().size());
    assertEquals(
        "NonEmptySet<T> invalidated parameter(s) - Parameter Validation Failures: [set.isEmpty() must be false]",
        parametersValidationExceptionEmpty.getMessage());
    var parametersValidationExceptionModifiable = assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptySet<>(new HashSet<Integer>()));
    assertEquals(2, parametersValidationExceptionModifiable.getParametersValidationFailureMessages().size());
    assertEquals(
        "NonEmptySet<T> invalidated parameter(s) - Parameter Validation Failures: [set.isEmpty() must be false|set must be unmodifiable]",
        parametersValidationExceptionModifiable.getMessage());
  }

  @Test
  public void testInvalidate() {
    assertTrue(NonEmptySet.invalidate(Set.of()).isPresent());
    assertTrue(NonEmptySet.invalidate(new HashSet<Integer>()).isPresent());
    assertFalse(NonEmptySet.invalidate(Set.of(1)).isPresent());
  }

  @Test
  public void testWrap() {
    assertTrue(NonEmptySet.wrap(Set.of()).isLeft());
    assertTrue(NonEmptySet.wrap(new HashSet<Integer>()).isLeft());
    var set = Set.of(1, 2, 3);
    var errorOrValue = NonEmptySet.wrap(set);
    assertTrue(errorOrValue.isRight());
    assertEquals(set, errorOrValue.getRight().set());
    assertSame(set, errorOrValue.getRight().set());
  }

  @Test
  public void testFromCollection() {
    assertTrue(NonEmptySet.from(Set.of()).isLeft());
    var errorOrValue = NonEmptySet.from(Set.of(1));
    assertTrue(errorOrValue.isRight());
    assertEquals(Set.of(1), errorOrValue.getRight().set());
  }

  @Test
  public void testFromStream() {
    assertTrue(NonEmptySet.from(Stream.empty()).isLeft());
    var errorOrValue = NonEmptySet.from(Stream.of(1));
    assertTrue(errorOrValue.isRight());
    assertEquals(Set.of(1), errorOrValue.getRight().set());
  }
}
