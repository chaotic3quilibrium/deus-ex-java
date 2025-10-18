package org.deus_ex_java.lang;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegersOpsTests {
  @Test
  public void testFindSetBitIndicesAsList() {
    assertEquals(List.of(), IntegersOps.findSetBitIndicesAsList(0));
    assertEquals(
        List.of(0, 1, 2, 3, 4, 5, 6, 7),
        IntegersOps.findSetBitIndicesAsList(255));
    assertEquals(
        List.of(8),
        IntegersOps.findSetBitIndicesAsList(256));
  }

  @Test
  public void testFindSetBitIndicesAsSet() {
    assertEquals(Set.of(), IntegersOps.findSetBitIndicesAsSet(0));
    assertEquals(
        Set.of(0, 1, 2, 3, 4, 5, 6),
        IntegersOps.findSetBitIndicesAsSet(127));
    assertEquals(
        Set.of(7),
        IntegersOps.findSetBitIndicesAsSet(128));
  }
}
