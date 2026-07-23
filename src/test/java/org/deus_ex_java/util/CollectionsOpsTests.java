package org.deus_ex_java.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CollectionsOpsTests {

  @Test
  @DisplayName("Verify full matrix of JDK unmodifiable/immutable Collection types return true")
  public void testIsUnmodifiableCollectionMatrix() {
    // JDK Immutable Collections (Java 9+)
    assertTrue(CollectionsOps.isUnmodifiable(List.of()));
    assertTrue(CollectionsOps.isUnmodifiable(List.of(1, 2, 3)));
    assertTrue(CollectionsOps.isUnmodifiable(Set.of()));
    assertTrue(CollectionsOps.isUnmodifiable(Set.of("a", "b")));
    assertTrue(CollectionsOps.isUnmodifiable(List.copyOf(new ArrayList<>(List.of(1)))));
    assertTrue(CollectionsOps.isUnmodifiable(Set.copyOf(new HashSet<>(Set.of("a")))));
    assertTrue(CollectionsOps.isUnmodifiable(Stream.of(1, 2).toList()));

    // JDK Collections.unmodifiable* wrappers
    assertTrue(CollectionsOps.isUnmodifiable(Collections.unmodifiableList(new ArrayList<>())));
    assertTrue(CollectionsOps.isUnmodifiable(Collections.unmodifiableSet(new HashSet<>())));
    assertTrue(CollectionsOps.isUnmodifiable(Collections.unmodifiableCollection(new ArrayList<>())));
    assertTrue(CollectionsOps.isUnmodifiable(Collections.unmodifiableNavigableSet(new TreeSet<>())));
    assertTrue(CollectionsOps.isUnmodifiable(Collections.unmodifiableSortedSet(new TreeSet<>())));

    // JDK Empty & Singleton Collections
    assertTrue(CollectionsOps.isUnmodifiable(Collections.emptyList()));
    assertTrue(CollectionsOps.isUnmodifiable(Collections.emptySet()));
    assertTrue(CollectionsOps.isUnmodifiable(Collections.singletonList(1)));
    assertTrue(CollectionsOps.isUnmodifiable(Collections.singleton("a")));
  }

  @Test
  @DisplayName("Verify full matrix of JDK unmodifiable/immutable Map types return true")
  public void testIsUnmodifiableMapMatrix() {
    // JDK Immutable Maps (Java 9+)
    assertTrue(CollectionsOps.isUnmodifiable(Map.of()));
    assertTrue(CollectionsOps.isUnmodifiable(Map.of("k", "v")));
    assertTrue(CollectionsOps.isUnmodifiable(Map.copyOf(new HashMap<>(Map.of("k", "v")))));

    // JDK Collections.unmodifiable* Map wrappers
    assertTrue(CollectionsOps.isUnmodifiable(Collections.unmodifiableMap(new HashMap<>())));
    assertTrue(CollectionsOps.isUnmodifiable(Collections.unmodifiableNavigableMap(new TreeMap<>())));
    assertTrue(CollectionsOps.isUnmodifiable(Collections.unmodifiableSortedMap(new TreeMap<>())));

    // JDK Empty & Singleton Maps
    assertTrue(CollectionsOps.isUnmodifiable(Collections.emptyMap()));
    assertTrue(CollectionsOps.isUnmodifiable(Collections.singletonMap("k", "v")));
  }

  @Test
  @DisplayName("Verify standard JDK mutable Collection and Map types return false")
  public void testMutableTypesReturnFalse() {
    assertFalse(CollectionsOps.isUnmodifiable(new ArrayList<>()));
    assertFalse(CollectionsOps.isUnmodifiable(new LinkedList<>()));
    assertFalse(CollectionsOps.isUnmodifiable(new HashSet<>()));
    assertFalse(CollectionsOps.isUnmodifiable(new TreeSet<>()));
    assertFalse(CollectionsOps.isUnmodifiable(new HashMap<>()));
    assertFalse(CollectionsOps.isUnmodifiable(new TreeMap<>()));
    assertFalse(CollectionsOps.isUnmodifiable(new ConcurrentHashMap<>()));
  }

  @Test
  @DisplayName("Verify isUnmodifiable causes zero ConcurrentModificationException during concurrent active iteration")
  public void testConcurrentIterationNoModificationException() {
    var mutableList = new ArrayList<>(List.of("element1", "element2", "element3"));
    var iterator = mutableList.iterator();

    // Advance iterator partially
    assertTrue(iterator.hasNext());
    assertEquals("element1", iterator.next());

    // Invoke isUnmodifiable while iterator is active
    assertDoesNotThrow(() -> {
      boolean unmodifiable = CollectionsOps.isUnmodifiable(mutableList);
      assertFalse(unmodifiable);
    });

    // Verify iterator continues cleanly without ConcurrentModificationException
    assertTrue(iterator.hasNext());
    assertEquals("element2", iterator.next());
    assertTrue(iterator.hasNext());
    assertEquals("element3", iterator.next());
    assertFalse(iterator.hasNext());
  }

  @Test
  @DisplayName("Verify isUnmodifiable is completely side-effect free and mutates zero elements or size")
  public void testNonMutationGuarantee() {
    var mutableList = new ArrayList<>(List.of("alpha", "beta", "gamma"));
    var snapshotBefore = new ArrayList<>(mutableList);

    boolean unmodifiable = CollectionsOps.isUnmodifiable(mutableList);

    assertFalse(unmodifiable);
    assertEquals(snapshotBefore, mutableList, "Collection contents must remain identical");
    assertEquals(3, mutableList.size(), "Collection size must remain unchanged");

    var mutableMap = new HashMap<>(Map.of(1, "one", 2, "two"));
    var mapSnapshotBefore = new HashMap<>(mutableMap);

    boolean mapUnmodifiable = CollectionsOps.isUnmodifiable(mutableMap);

    assertFalse(mapUnmodifiable);
    assertEquals(mapSnapshotBefore, mutableMap, "Map contents must remain identical");
    assertEquals(2, mutableMap.size(), "Map size must remain unchanged");
  }
}

