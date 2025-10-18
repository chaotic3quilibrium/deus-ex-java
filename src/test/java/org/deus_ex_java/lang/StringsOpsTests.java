package org.deus_ex_java.lang;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StringsOpsTests {
  @Test
  public void testNullToEmpty() {
    var stringEmptyNull = StringsOps.nullToEmpty(null);
    assertNotNull(stringEmptyNull);
    assertTrue(stringEmptyNull.isEmpty());
    @SuppressWarnings("ObviousNullCheck")
    var stringEmptySetOf = StringsOps.nullToEmpty("");
    assertNotNull(stringEmptySetOf);
    //noinspection ConstantValue
    assertTrue(stringEmptySetOf.isEmpty());
    @SuppressWarnings("ObviousNullCheck")
    var stringEmptySetOf1 = StringsOps.nullToEmpty("x");
    assertNotNull(stringEmptySetOf1);
    //noinspection ConstantValue
    assertFalse(stringEmptySetOf1.isEmpty());
    assertEquals("x", stringEmptySetOf1);
  }

  @Test
  public void testEqualsIgnoreCaseNullable() {
    assertTrue(StringsOps.equalsIgnoreCaseNullable(null, null));
    assertFalse(StringsOps.equalsIgnoreCaseNullable("a", null));
    assertFalse(StringsOps.equalsIgnoreCaseNullable(null, "A"));
    assertTrue(StringsOps.equalsIgnoreCaseNullable("a", "A"));
  }
}
