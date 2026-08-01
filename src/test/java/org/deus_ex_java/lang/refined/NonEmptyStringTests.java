package org.deus_ex_java.lang.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("EqualsWithItself")
public class NonEmptyStringTests {
  @Test
  public void testDefaultConstructor() {
    assertEquals("x", new NonEmptyString("x").value());
    assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptyString(""));
  }

  @Test
  public void testInvalidate() {
    assertTrue(NonEmptyString.invalidate("x").isEmpty());
    assertTrue(NonEmptyString.invalidate("").isPresent());
  }

  @Test
  public void testFrom() {
    var errorOrValue = NonEmptyString.from("x");
    assertTrue(errorOrValue.isRight());
    assertEquals("x", errorOrValue.getRight().value());
    assertTrue(NonEmptyString.from("").isLeft());
  }

  @Test
  public void testCompareTo() {
    var nonEmptyStringA1 = new NonEmptyString("a");
    var nonEmptyStringA2 = new NonEmptyString("a");
    var nonEmptyStringB = new NonEmptyString("b");
    assertEquals(0, nonEmptyStringA1.compareTo(nonEmptyStringA1));
    assertEquals(0, nonEmptyStringA1.compareTo(nonEmptyStringA2));
    assertEquals(0, nonEmptyStringA2.compareTo(nonEmptyStringA1));
    assertEquals(-1, nonEmptyStringA2.compareTo(nonEmptyStringB));
    assertEquals(1, nonEmptyStringB.compareTo(nonEmptyStringA1));
  }
}
