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
  public void testIndexOfIgnoreCase() {
    assertEquals(-1, StringsOps.indexOfIgnoreCase("AbCdEAbCdE", "x"));
    assertEquals(0, StringsOps.indexOfIgnoreCase("AbCdEAbCdE", ""));
    assertEquals(0, StringsOps.indexOfIgnoreCase("AbCdEAbCdE", "a"));
    assertEquals(0, StringsOps.indexOfIgnoreCase("AbCdEAbCdE", "A"));
    assertEquals(3, StringsOps.indexOfIgnoreCase("AbCdEAbCdE", "d"));
    assertEquals(3, StringsOps.indexOfIgnoreCase("AbCdEAbCdE", "D"));
    assertEquals(4, StringsOps.indexOfIgnoreCase("AbCdEAbCdE", "eaB"));
  }

  @Test
  public void testLastIndexOfIgnoreCase() {
    assertEquals(-1, StringsOps.lastIndexOfIgnoreCase("AbCdEAbCdE", "x"));
    assertEquals(10, StringsOps.lastIndexOfIgnoreCase("AbCdEAbCdE", ""));
    assertEquals(5, StringsOps.lastIndexOfIgnoreCase("AbCdEAbCdE", "a"));
    assertEquals(5, StringsOps.lastIndexOfIgnoreCase("AbCdEAbCdE", "A"));
    assertEquals(8, StringsOps.lastIndexOfIgnoreCase("AbCdEAbCdE", "d"));
    assertEquals(8, StringsOps.lastIndexOfIgnoreCase("AbCdEAbCdE", "D"));
    assertEquals(4, StringsOps.indexOfIgnoreCase("AbCdEAbCdE", "eaB"));
  }

  @Test
  public void testEqualsIgnoreCase() {
    assertTrue(StringsOps.equalsIgnoreCase("", ""));
    assertFalse(StringsOps.equalsIgnoreCase("a", ""));
    assertFalse(StringsOps.equalsIgnoreCase("", "a"));
    assertTrue(StringsOps.equalsIgnoreCase("a", "a"));
    assertTrue(StringsOps.equalsIgnoreCase("a", "A"));
    assertTrue(StringsOps.equalsIgnoreCase("A", "a"));
    assertTrue(StringsOps.equalsIgnoreCase("A", "A"));
  }

  @Test
  public void testEqualsIgnoreCaseNullable() {
    assertTrue(StringsOps.equalsIgnoreCaseNullable(null, null));
    assertFalse(StringsOps.equalsIgnoreCaseNullable("", null));
    assertFalse(StringsOps.equalsIgnoreCaseNullable(null, ""));
    assertTrue(StringsOps.equalsIgnoreCaseNullable("", ""));
    assertFalse(StringsOps.equalsIgnoreCaseNullable("a", null));
    assertFalse(StringsOps.equalsIgnoreCaseNullable(null, "A"));
    assertTrue(StringsOps.equalsIgnoreCaseNullable("a", "a"));
    assertTrue(StringsOps.equalsIgnoreCaseNullable("a", "A"));
    assertTrue(StringsOps.equalsIgnoreCaseNullable("A", "a"));
    assertTrue(StringsOps.equalsIgnoreCaseNullable("A", "A"));
  }
}
