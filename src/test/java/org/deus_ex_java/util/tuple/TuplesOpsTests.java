package org.deus_ex_java.util.tuple;

import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleImmutableEntry;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TuplesOpsTests {
  @Test
  public void testEntry() {
    var tuple2 = TuplesOps.from(entry(2, "B"));
    assertEquals(2, tuple2._1());
    assertEquals("B", tuple2._2());
  }

  @Test
  public void testSimpleImmutableEntry() {
    var tuple2 = TuplesOps.from(new SimpleImmutableEntry<>(2, "B"));
    assertEquals(2, tuple2._1());
    assertEquals("B", tuple2._2());
  }
}
