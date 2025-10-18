package org.deus_ex_java.lang.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NonEmptyStringTests {
  @Test
  public void testDefaultConstructor() {
    assertEquals("x", new NonEmptyString("x").string());
    assertThrows(
        ParametersValidationException.class,
        () ->
            new NonEmptyString(""));
  }

  @Test
  public void testValidate() {
    assertTrue(NonEmptyString.validate("x").isEmpty());
    assertTrue(NonEmptyString.validate("").isPresent());
  }

  @Test
  public void testFrom() {
    var errorOrValue = NonEmptyString.from("x");
    assertTrue(errorOrValue.isRight());
    assertEquals("x", errorOrValue.getRight().string());
    assertTrue(NonEmptyString.from("").isLeft());
  }

  @Test
  public void testCompareTo() {
    var NonEmptyStringA1 = new NonEmptyString("a");
    var NonEmptyStringA2 = new NonEmptyString("a");
    var NonEmptyStringB = new NonEmptyString("b");
    //noinspection EqualsWithItself
    assertEquals(0, NonEmptyStringA1.compareTo(NonEmptyStringA1));
    assertEquals(0, NonEmptyStringA1.compareTo(NonEmptyStringA2));
    assertEquals(0, NonEmptyStringA2.compareTo(NonEmptyStringA1));
    assertEquals(-1, NonEmptyStringA2.compareTo(NonEmptyStringB));
    assertEquals(1, NonEmptyStringB.compareTo(NonEmptyStringA1));
  }
}
