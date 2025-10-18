package org.deus_ex_java.util;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CollectionsOpsTests {
  @Test
  public void testIsUnmodifiableCollection() {
    assertTrue(CollectionsOps.isUnmodifiable(List.of()));
    assertTrue(CollectionsOps.isUnmodifiable(Set.of()));
    assertFalse(CollectionsOps.isUnmodifiable(new ArrayList<>()));
    assertFalse(CollectionsOps.isUnmodifiable(new HashSet<>()));
  }

  @Test
  public void testIsUnmodifiableMap() {
    assertTrue(CollectionsOps.isUnmodifiable(Map.of()));
    assertFalse(CollectionsOps.isUnmodifiable(new HashMap<>()));
  }
}
