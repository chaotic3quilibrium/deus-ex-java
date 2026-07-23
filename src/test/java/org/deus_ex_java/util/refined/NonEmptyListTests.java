package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NonEmptyListTests {
  @Test
  public void testDefaultConstructor() {
    assertEquals(List.of(1), new NonEmptyList<>(List.of(1)).list());
    var parametersValidationExceptionEmpty = assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptyList<>(List.of()));
    assertEquals(1, parametersValidationExceptionEmpty.getParametersValidationFailureMessages().size());
    assertEquals(
        "NonEmptyList<T> invalidated parameter(s) - Parameter Validation Failures: [list.isEmpty() must be false]",
        parametersValidationExceptionEmpty.getMessage());
    var parametersValidationExceptionModifiable = assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptyList<>(new ArrayList<Integer>()));
    assertEquals(2, parametersValidationExceptionModifiable.getParametersValidationFailureMessages().size());
    assertEquals(
        "NonEmptyList<T> invalidated parameter(s) - Parameter Validation Failures: [list.isEmpty() must be false|list must be unmodifiable]",
        parametersValidationExceptionModifiable.getMessage());
  }

  @Test
  public void testInvalidate() {
    assertTrue(NonEmptyList.invalidate(List.of()).isPresent());
    assertTrue(NonEmptyList.invalidate(new ArrayList<Integer>()).isPresent());
    assertFalse(NonEmptyList.invalidate(List.of(1)).isPresent());
  }

  @Test
  public void testWrap() {
    assertTrue(NonEmptyList.wrap(List.of()).isLeft());
    assertTrue(NonEmptyList.wrap(new ArrayList<Integer>()).isLeft());
    var list = List.of(1, 2, 3);
    var errorOrValue = NonEmptyList.wrap(list);
    assertTrue(errorOrValue.isRight());
    assertEquals(list, errorOrValue.getRight().list());
    assertSame(list, errorOrValue.getRight().list());
  }

  @Test
  public void testFromCollection() {
    assertTrue(NonEmptyList.from(List.of()).isLeft());
    var errorOrValue = NonEmptyList.from(Set.of(1));
    assertTrue(errorOrValue.isRight());
    assertEquals(List.of(1), errorOrValue.getRight().list());
  }

  @Test
  public void testFromStream() {
    assertTrue(NonEmptyList.from(Stream.empty()).isLeft());
    var errorOrValue = NonEmptyList.from(Stream.of(1));
    assertTrue(errorOrValue.isRight());
    assertEquals(List.of(1), errorOrValue.getRight().list());
  }

  @Test
  public void testReferenceLeakIsolation() {
    var mutableList = new ArrayList<>(List.of(1, 2, 3));
    var unmodifiableView = Collections.unmodifiableList(mutableList);
    var nonEmptyList = new NonEmptyList<>(unmodifiableView);

    mutableList.clear();

    assertEquals(3, nonEmptyList.list().size());
    assertEquals(List.of(1, 2, 3), nonEmptyList.list());
    assertFalse(nonEmptyList.list().isEmpty());
  }
}
