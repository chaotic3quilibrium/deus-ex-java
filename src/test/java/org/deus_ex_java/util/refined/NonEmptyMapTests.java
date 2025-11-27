package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

public class NonEmptyMapTests {
  @Test
  public void testDefaultConstructor() {
    assertEquals(Map.of(1, "x"), new NonEmptyMap<>(Map.of(1, "x")).map());
    var parametersValidationExceptionEmpty = assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptyMap<>(Map.of()));
    assertEquals(1, parametersValidationExceptionEmpty.getParametersValidationFailureMessages().size());
    assertEquals(
        "NonEmptyMap<K, V> invalidated parameter(s) - Parameter Validation Failures: [map.isEmpty() must be false]",
        parametersValidationExceptionEmpty.getMessage());
    var parametersValidationExceptionModifiable = assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptyMap<>(new HashMap<Integer, String>()));
    assertEquals(2, parametersValidationExceptionModifiable.getParametersValidationFailureMessages().size());
    assertEquals(
        "NonEmptyMap<K, V> invalidated parameter(s) - Parameter Validation Failures: [map.isEmpty() must be false|map must be unmodifiable]",
        parametersValidationExceptionModifiable.getMessage());
  }

  @Test
  public void testInvalidate() {
    assertTrue(NonEmptyMap.invalidate(Map.of()).isPresent());
    assertTrue(NonEmptyMap.invalidate(new HashMap<Integer, String>()).isPresent());
    assertTrue(NonEmptyMap.invalidate(Map.of(1, "x")).isEmpty());
  }

  @Test
  public void testWrap() {
    assertTrue(NonEmptyMap.wrap(Map.of()).isLeft());
    assertTrue(NonEmptyMap.wrap(new HashMap<Integer, String>()).isLeft());
    var map = Map.of(1, "x", 2, "y", 3, "z");
    var errorOrValue = NonEmptyMap.wrap(map);
    assertTrue(errorOrValue.isRight());
    assertEquals(map, errorOrValue.getRight().map());
    assertSame(map, errorOrValue.getRight().map());
  }

  @Test
  public void testFromCollection() {
    assertTrue(NonEmptyMap.from(Map.of().entrySet()).isLeft());
    var errorOrValue = NonEmptyMap.from(Map.of(1, "x").entrySet());
    assertTrue(errorOrValue.isRight());
    assertEquals(Map.of(1, "x"), errorOrValue.getRight().map());
  }

  @Test
  public void testFromStream() {
    assertTrue(NonEmptyMap.from(Stream.empty()).isLeft());
    var errorOrValue = NonEmptyMap.from(Stream.of(entry(1, "x")));
    assertTrue(errorOrValue.isRight());
    assertEquals(Map.of(1, "x"), errorOrValue.getRight().map());
  }
}
