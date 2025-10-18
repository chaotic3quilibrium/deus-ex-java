package org.deus_ex_java.lang.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NonNegIntTests {
  @Test
  public void testDefaultConstructor() {
    assertThrows(
        ParametersValidationException.class,
        () ->
            new NonNegInt(-1));
    assertEquals(0, new NonNegInt(0).value());
    assertEquals(1, new NonNegInt(1).value());
  }

  @Test
  public void testValidate() {
    assertFalse(NonNegInt.validate(-1).isEmpty());
    assertTrue(NonNegInt.validate(0).isEmpty());
    assertTrue(NonNegInt.validate(1).isEmpty());
  }

  @Test
  public void testFrom() {
    assertTrue(NonNegInt.from(-1).isLeft());
    assertTrue(NonNegInt.from(0).isRight());
    assertTrue(NonNegInt.from(1).isRight());
  }

  @Test
  public void testCompareTo() {
    var NonNegIntA1 = new NonNegInt(0);
    var NonNegIntA2 = new NonNegInt(0);
    var NonNegIntB = new NonNegInt(1);
    //noinspection EqualsWithItself
    assertEquals(0, NonNegIntA1.compareTo(NonNegIntA1));
    assertEquals(0, NonNegIntA1.compareTo(NonNegIntA2));
    assertEquals(0, NonNegIntA2.compareTo(NonNegIntA1));
    assertEquals(-1, NonNegIntA2.compareTo(NonNegIntB));
    assertEquals(1, NonNegIntB.compareTo(NonNegIntA1));
  }
}
