package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class NonEmptyMapTests {
  @Test
  public void testDefaultConstructor() {
    assertEquals(Map.of(1, "x"), new NonEmptyMap<>(Map.of(1, "x")).map());
    assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptyMap<>(Map.of()));
  }

  @Test
  public void testValidate() {
    assertTrue(NonEmptyMap.validate(Map.of(1, "x")).isEmpty());
    assertTrue(NonEmptyMap.validate(Map.of()).isPresent());
  }

  @Test
  public void testFrom() {
    var errorOrValue = NonEmptyMap.from(Map.of(1, "x"));
    assertTrue(errorOrValue.isRight());
    assertEquals(Map.of(1, "x"), errorOrValue.getRight().map());
    assertTrue(NonEmptyMap.from(Map.of()).isLeft());
  }

  @Test
  public void testIsUnmodifiable() {
    Map<Integer, String> map = new HashMap<>();
    map.put(2, "b");
    map.put(3, "c");
    var parametersValidationExceptionOrNonEmptyMap = NonEmptyMap.from(map);
    assertTrue(parametersValidationExceptionOrNonEmptyMap.isLeft());
    assertEquals(
        "NonEmptyMap<K, V> invalid parameter(s) - Parameter Validation Failures: [map must be unmodifiable]",
        parametersValidationExceptionOrNonEmptyMap.getLeft().getMessage());
    assertEquals(
        1,
        parametersValidationExceptionOrNonEmptyMap.getLeft().getParametersValidationFailureMessages().size());
  }
}
