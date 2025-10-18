package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class NonEmptySetTests {
  @Test
  public void testDefaultConstructor() {
    assertEquals(Set.of(1), new NonEmptySet<>(Set.of(1)).set());
    assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptySet<>(Set.of()));
  }

  @Test
  public void testValidate() {
    assertTrue(NonEmptySet.validate(Set.of(1)).isEmpty());
    assertTrue(NonEmptySet.validate(Set.of()).isPresent());
  }

  @Test
  public void testFrom() {
    var errorOrValue = NonEmptySet.from(Set.of(1));
    assertTrue(errorOrValue.isRight());
    assertEquals(Set.of(1), errorOrValue.getRight().set());
    assertTrue(NonEmptySet.from(Set.of()).isLeft());
  }

  @Test
  public void testIsUnmodifiable() {
    var parametersValidationExceptionOrNonEmptySet = NonEmptySet.from(
        new HashSet<>(List.of(2, 3)));
    assertTrue(parametersValidationExceptionOrNonEmptySet.isLeft());
    assertEquals(
        "NonEmptySet<T> invalid parameter(s) - Parameter Validation Failures: [set must be unmodifiable]",
        parametersValidationExceptionOrNonEmptySet.getLeft().getMessage());
    assertEquals(
        1,
        parametersValidationExceptionOrNonEmptySet.getLeft().getParametersValidationFailureMessages().size());
  }
}
