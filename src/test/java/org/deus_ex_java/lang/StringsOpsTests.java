package org.deus_ex_java.lang;

import org.deus_ex_java.util.SetsOps;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"ObviousNullCheck", "ConstantValue"})
public class StringsOpsTests {
  @Test
  public void testNullToEmpty() {
    var stringEmptyNull = StringsOps.nullToEmpty(null);
    assertNotNull(stringEmptyNull);
    assertTrue(stringEmptyNull.isEmpty());
    var stringEmptySetOf = StringsOps.nullToEmpty("");
    assertNotNull(stringEmptySetOf);
    assertTrue(stringEmptySetOf.isEmpty());
    var stringEmptySetOf1 = StringsOps.nullToEmpty("x");
    assertNotNull(stringEmptySetOf1);
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
  public void testEqualsIgnoreCaseEnglish() {
    assertTrue(StringsOps.equalsIgnoreCase("", ""));
    assertFalse(StringsOps.equalsIgnoreCase("a", ""));
    assertFalse(StringsOps.equalsIgnoreCase("", "a"));
    assertTrue(StringsOps.equalsIgnoreCase("a", "a"));
    assertTrue(StringsOps.equalsIgnoreCase("a", "A"));
    assertTrue(StringsOps.equalsIgnoreCase("A", "a"));
    assertTrue(StringsOps.equalsIgnoreCase("A", "A"));
  }

  public static Stream<Arguments> testEqualsIgnoreCaseTurkish() {
    var turkishIsUpperCase = SetsOps.ofOrdered("İ", "I"); //U+0130, U+0049
    var turkishIsLowerCase = SetsOps.ofOrdered("i", "ı"); //U+0069, U+0131
    var turkishIs = SetsOps.appendSets(turkishIsUpperCase, turkishIsLowerCase);
    var quitUpperCase = "QUIT";
    var quitLowerCase = "quit";
    var quitMixedCase = "Quit";
    var quits = SetsOps.ofOrdered(quitUpperCase, quitLowerCase, quitMixedCase);

    return quits
        .stream()
        .flatMap(quitA ->
            quits
                .stream()
                .flatMap(quitB ->
                    turkishIs
                        .stream()
                        .flatMap(turkishIA ->
                            turkishIs
                                .stream()
                                .map(turkishIB ->
                                    Arguments.of(
                                        quitA.substring(0, 2) + turkishIA + quitA.substring(3),
                                        quitB.substring(0, 2) + turkishIB + quitB.substring(3))))));
  }

  @ParameterizedTest
  @MethodSource
  public void testEqualsIgnoreCaseTurkish(
      String stringA,
      String stringB
  ) {
    assertTrue(StringsOps.equalsIgnoreCase(stringA, stringB));
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
