package org.deus_ex_java.util.tuple;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tuple2Tests {
  @Test
  public void testEntry() {
    var entry = new Tuple2<>("a", 1).to();
    assertEquals("a", entry.getKey());
    assertEquals(1, entry.getValue());
  }

  @Test
  public void testSimpleImmutableEntry() {
    var simpleImmutableEntry = new Tuple2<>("a", 1).toSimpleImmutableEntry();
    assertEquals("a", simpleImmutableEntry.getKey());
    assertEquals(1, simpleImmutableEntry.getValue());
  }
}
