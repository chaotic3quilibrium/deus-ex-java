package org.deus_ex_java.lang.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("EqualsWithItself")
public class NonBlankStringTests {
  private Stream<String> generateStrings() {
    return Stream.of("", " ", "  ");
  }

  @Test
  public void testDefaultConstructor() {
    assertEquals("x", new NonBlankString("x").value());
    generateStrings()
        .forEach(string ->
            assertThrows(
                ParametersValidationException.class,
                () ->
                    new NonBlankString(string)));
  }

  @Test
  public void testInvalidate() {
    assertTrue(NonBlankString.invalidate("x").isEmpty());
    generateStrings()
        .forEach(string ->
            assertFalse(NonBlankString.invalidate(string).isEmpty()));
  }

  @Test
  public void testFrom() {
    var errorOrValue = NonBlankString.from("x");
    assertTrue(errorOrValue.isRight());
    assertEquals("x", errorOrValue.getRight().value());
    generateStrings()
        .forEach(string ->
            assertTrue(NonBlankString.from(string).isLeft()));
  }

  @Test
  public void testCompareTo() {
    var nonBlankStringA1 = new NonBlankString("a");
    var nonBlankStringA2 = new NonBlankString("a");
    var nonBlankStringB = new NonBlankString("b");
    assertEquals(0, nonBlankStringA1.compareTo(nonBlankStringA1));
    assertEquals(0, nonBlankStringA1.compareTo(nonBlankStringA2));
    assertEquals(0, nonBlankStringA2.compareTo(nonBlankStringA1));
    assertEquals(-1, nonBlankStringA2.compareTo(nonBlankStringB));
    assertEquals(1, nonBlankStringB.compareTo(nonBlankStringA1));
  }
}
