package org.deus_ex_java.lang.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NonEmptyLowerCaseStringTests {
  @Test
  public void testDefaultConstructor() {
    assertEquals("x", new NonEmptyLowerCaseString("x").string());
    var parametersValidationExceptionEmpty = assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptyLowerCaseString(""));
    assertEquals(
        "NonEmptyLowerCaseString invalid parameter(s) - Parameter Validation Failures: [string.isEmpty() must be false]",
        parametersValidationExceptionEmpty.getMessage());
    var parametersValidationExceptionUpperCase = assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptyLowerCaseString("X"));
    assertEquals(
        "NonEmptyLowerCaseString invalid parameter(s) - Parameter Validation Failures: [string.equals(string.toLowerCase()) must be true]",
        parametersValidationExceptionUpperCase.getMessage());
  }

  @Test
  public void testValidate() {
    assertTrue(NonEmptyLowerCaseString.validate("x").isEmpty());
    assertTrue(NonEmptyLowerCaseString.validate("").isPresent());
    assertTrue(NonEmptyLowerCaseString.validate("X").isPresent());
  }

  @Test
  public void testFrom() {
    var errorOrValue = NonEmptyLowerCaseString.from("x");
    assertTrue(errorOrValue.isRight());
    assertEquals("x", errorOrValue.getRight().string());
    assertTrue(NonEmptyLowerCaseString.from("").isLeft());
  }

  @Test
  public void testCompareTo() {
    var nonEmptyLowerCaseStringA1 = new NonEmptyLowerCaseString("a");
    var nonEmptyLowerCaseStringA2 = new NonEmptyLowerCaseString("a");
    var nonEmptyLowerCaseStringB = new NonEmptyLowerCaseString("b");
    //noinspection EqualsWithItself
    assertEquals(0, nonEmptyLowerCaseStringA1.compareTo(nonEmptyLowerCaseStringA1));
    assertEquals(0, nonEmptyLowerCaseStringA1.compareTo(nonEmptyLowerCaseStringA2));
    assertEquals(0, nonEmptyLowerCaseStringA2.compareTo(nonEmptyLowerCaseStringA1));
    assertEquals(-1, nonEmptyLowerCaseStringA2.compareTo(nonEmptyLowerCaseStringB));
    assertEquals(1, nonEmptyLowerCaseStringB.compareTo(nonEmptyLowerCaseStringA1));
  }
}
