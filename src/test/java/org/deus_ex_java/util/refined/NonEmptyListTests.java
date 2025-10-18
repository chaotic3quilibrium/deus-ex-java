package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NonEmptyListTests {
  @Test
  public void testDefaultConstructor() {
    assertEquals(List.of(1), new NonEmptyList<>(List.of(1)).list());
    assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptyList<>(List.of()));
  }

  @Test
  public void testValidate() {
    assertTrue(NonEmptyList.validate(List.of(1)).isEmpty());
    assertTrue(NonEmptyList.validate(List.of()).isPresent());
  }

  @Test
  public void testFrom() {
    var errorOrValue = NonEmptyList.from(List.of(1));
    assertTrue(errorOrValue.isRight());
    assertEquals(List.of(1), errorOrValue.getRight().list());
    assertTrue(NonEmptyList.from(List.of()).isLeft());
  }

  @Test
  public void testIsUnmodifiable() {
    var parametersValidationExceptionOrNonEmptyList = NonEmptyList.from(
        new ArrayList<>(List.of(2, 3)));
    assertTrue(parametersValidationExceptionOrNonEmptyList.isLeft());
    assertEquals(
        "NonEmptyList<T> invalid parameter(s) - Parameter Validation Failures: [list must be unmodifiable]",
        parametersValidationExceptionOrNonEmptyList.getLeft().getMessage());
    assertEquals(
        1,
        parametersValidationExceptionOrNonEmptyList.getLeft().getParametersValidationFailureMessages().size());
  }
}
