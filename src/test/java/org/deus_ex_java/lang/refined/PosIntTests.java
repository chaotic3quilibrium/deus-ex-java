package org.deus_ex_java.lang.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PosIntTests {
  @Test
  public void testDefaultConstructor() {
    assertThrows(
        ParametersValidationException.class,
        () ->
            new PosInt(-1));
    assertThrows(
        ParametersValidationException.class,
        () ->
            new PosInt(0));
    assertEquals(1, new PosInt(1).value());
    assertEquals(2, new PosInt(2).value());
  }

  @Test
  public void testValidate() {
    assertFalse(PosInt.validate(-1).isEmpty());
    assertFalse(PosInt.validate(0).isEmpty());
    assertTrue(PosInt.validate(1).isEmpty());
    assertTrue(PosInt.validate(2).isEmpty());
  }

  @Test
  public void testFrom() {
    assertTrue(PosInt.from(-1).isLeft());
    assertTrue(PosInt.from(0).isLeft());
    assertTrue(PosInt.from(1).isRight());
    assertTrue(PosInt.from(2).isRight());
  }

  @Test
  public void testCompareTo() {
    var PosIntA1 = new PosInt(1);
    var PosIntA2 = new PosInt(1);
    var PosIntB = new PosInt(2);
    //noinspection EqualsWithItself
    assertEquals(0, PosIntA1.compareTo(PosIntA1));
    assertEquals(0, PosIntA1.compareTo(PosIntA2));
    assertEquals(0, PosIntA2.compareTo(PosIntA1));
    assertEquals(-1, PosIntA2.compareTo(PosIntB));
    assertEquals(1, PosIntB.compareTo(PosIntA1));
  }
}
