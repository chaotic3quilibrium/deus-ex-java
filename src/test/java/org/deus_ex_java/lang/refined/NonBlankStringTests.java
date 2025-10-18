package org.deus_ex_java.lang.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class NonBlankStringTests {
  private Stream<String> generateStrings() {
    return Stream.of("", " ", "  ");
  }

  @Test
  public void testDefaultConstructor() {
    assertEquals("x", new NonBlankString("x").string());
    generateStrings()
        .forEach(string ->
            assertThrows(
                ParametersValidationException.class,
                () ->
                    new NonBlankString(string)));
  }

  @Test
  public void testValidate() {
    assertTrue(NonBlankString.validate("x").isEmpty());
    generateStrings()
        .forEach(string ->
            assertFalse(NonBlankString.validate(string).isEmpty()));
  }

  @Test
  public void testFrom() {
    var errorOrValue = NonBlankString.from("x");
    assertTrue(errorOrValue.isRight());
    assertEquals("x", errorOrValue.getRight().string());
    generateStrings()
        .forEach(string ->
            assertTrue(NonBlankString.from(string).isLeft()));
  }

  @Test
  public void testCompareTo() {
    var nonBlankStringA1 = new NonBlankString("a");
    var nonBlankStringA2 = new NonBlankString("a");
    var nonBlankStringB = new NonBlankString("b");
    //noinspection EqualsWithItself
    assertEquals(0, nonBlankStringA1.compareTo(nonBlankStringA1));
    assertEquals(0, nonBlankStringA1.compareTo(nonBlankStringA2));
    assertEquals(0, nonBlankStringA2.compareTo(nonBlankStringA1));
    assertEquals(-1, nonBlankStringA2.compareTo(nonBlankStringB));
    assertEquals(1, nonBlankStringB.compareTo(nonBlankStringA1));
  }
}
