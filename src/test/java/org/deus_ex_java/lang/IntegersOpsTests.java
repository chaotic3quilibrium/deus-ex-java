package org.deus_ex_java.lang;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IntegersOpsTests {

  @Test
  public void testFindSetBitIndices() {
    assertEquals(Set.of(), IntegersOps.findSetBitIndices(0));
    assertEquals(
        Set.of(0, 1, 2, 3, 4, 5, 6),
        IntegersOps.findSetBitIndices(127));
    assertEquals(
        List.of(0, 1, 2, 3, 4, 5, 6),
        IntegersOps.findSetBitIndices(127)
            .stream()
            .toList());
    assertEquals(
        Set.of(7),
        IntegersOps.findSetBitIndices(128));
    assertEquals(
        Set.of(0, 1, 2, 3, 4, 5, 6, 7),
        IntegersOps.findSetBitIndices(255));
    assertEquals(
        List.of(0, 1, 2, 3, 4, 5, 6, 7),
        IntegersOps.findSetBitIndices(255)
            .stream()
            .toList());
  }
}
