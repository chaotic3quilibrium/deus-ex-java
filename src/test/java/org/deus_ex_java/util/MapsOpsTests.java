package org.deus_ex_java.util;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

public class MapsOpsTests {
  @Test
  public void testNullToEmpty() {
    var mapEmptyNull = MapsOps.nullToEmpty(null);
    assertNotNull(mapEmptyNull);
    assertTrue(mapEmptyNull.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapEmptyNull));
    var mapEmptyMapOf = MapsOps.nullToEmpty(Map.of());
    assertNotNull(mapEmptyMapOf);
    assertTrue(mapEmptyMapOf.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapEmptyMapOf));
    var mapEmptyMapOfIntegerAndString = MapsOps.nullToEmpty(Map.of(1, "x"));
    assertNotNull(mapEmptyMapOfIntegerAndString);
    assertFalse(mapEmptyMapOfIntegerAndString.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapEmptyMapOfIntegerAndString));
    assertEquals(Map.of(1, "x"), mapEmptyMapOfIntegerAndString);
  }

  @Test
  public void testIsNonNulls() {
    assertTrue(MapsOps.isNonNulls(Map.of(1, "x").entrySet().iterator().next()));
    var map1AndNull = new HashMap<Integer, String>();
    map1AndNull.put(1, null);
    assertFalse(MapsOps.isNonNulls(map1AndNull.entrySet().iterator().next()));
  }

  @Test
  public void testEntryValidate() {
    var mapEntryNullNull = new HashMap<>();
    mapEntryNullNull.put(null, null);
    var mapEntryA = mapEntryNullNull.entrySet().iterator().next();
    assertNull(mapEntryA.getKey());
    assertNull(mapEntryA.getValue());
    assertEquals(
        Optional.of(
            new ParametersValidationException(
                "MapsOps.containsNulls failed preconditions on the entry",
                List.of(
                    "entry.getKey() is null",
                    "entry.getValue() is null"))),
        MapsOps.validate(mapEntryA));
    var mapEntryNullString = new HashMap<>();
    mapEntryNullString.put(null, "x");
    var mapEntryB = mapEntryNullString.entrySet().iterator().next();
    assertNull(mapEntryB.getKey());
    assertNotNull(mapEntryB.getValue());
    assertEquals("x", mapEntryB.getValue());
    assertEquals(
        Optional.of(
            new ParametersValidationException(
                "MapsOps.containsNulls failed preconditions on the entry",
                List.of(
                    "entry.getKey() is null"))),
        MapsOps.validate(mapEntryB));
    var mapEntryIntegerNull = new HashMap<>();
    mapEntryIntegerNull.put(1, null);
    var mapEntryC = mapEntryIntegerNull.entrySet().iterator().next();
    assertNotNull(mapEntryC.getKey());
    assertNull(mapEntryC.getValue());
    assertEquals(1, mapEntryC.getKey());
    assertEquals(
        Optional.of(
            new ParametersValidationException(
                "MapsOps.containsNulls failed preconditions on the entry",
                List.of(
                    "entry.getValue() is null"))),
        MapsOps.validate(mapEntryC));
  }

  @Test
  public void testFilterToNonNulls() {
    var mapNullAndNull = new HashMap<Integer, String>();
    mapNullAndNull.put(1, "x");
    mapNullAndNull.put(null, "y");
    mapNullAndNull.put(2, null);
    var mapNonNull = MapsOps.filterToNonNulls(mapNullAndNull);
    assertEquals(Map.of(1, "x"), mapNonNull);
    assertFalse(mapNonNull.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapNonNull));
  }

  @Test
  public void testAddEntry() {
    var mapEmptyAdd1AndX = MapsOps.addEntry(Map.of(), entry(1, "x"));
    assertNotNull(mapEmptyAdd1AndX);
    assertFalse(mapEmptyAdd1AndX.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapEmptyAdd1AndX));
    assertEquals(Map.of(1, "x"), mapEmptyAdd1AndX);
    var mapAdd2AndY = MapsOps.addEntry(mapEmptyAdd1AndX, entry(2, "y"));
    assertNotNull(mapAdd2AndY);
    assertFalse(mapAdd2AndY.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapAdd2AndY));
    assertEquals(Map.of(1, "x", 2, "y"), mapAdd2AndY);
    var map1AndNull = new HashMap<Integer, String>();
    map1AndNull.put(1, null);
    assertThrows(
        ParametersValidationException.class,
        () ->
            MapsOps.addEntry(
                Map.of(3, "z"),
                map1AndNull.entrySet().iterator().next()));
  }

  @Test
  public void testAddKeyAndValue() {
    var mapEmptyAdd1AndX = MapsOps.addKeyAndValue(Map.of(), 1, "x");
    assertNotNull(mapEmptyAdd1AndX);
    assertFalse(mapEmptyAdd1AndX.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapEmptyAdd1AndX));
    assertEquals(Map.of(1, "x"), mapEmptyAdd1AndX);
    var mapAdd2AndY = MapsOps.addKeyAndValue(mapEmptyAdd1AndX, 2, "y");
    assertNotNull(mapAdd2AndY);
    assertFalse(mapAdd2AndY.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapAdd2AndY));
    assertEquals(Map.of(1, "x", 2, "y"), mapAdd2AndY);
  }

  @Test
  public void testAppendEntry() {
    var mapEmptyAdd1AndX = MapsOps.appendEntry(Map.of(), entry(1, "x"));
    assertNotNull(mapEmptyAdd1AndX);
    assertFalse(mapEmptyAdd1AndX.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapEmptyAdd1AndX));
    assertEquals(Map.of(1, "x"), mapEmptyAdd1AndX);
    var mapAdd2AndY = MapsOps.appendEntry(mapEmptyAdd1AndX, entry(2, "y"));
    assertNotNull(mapAdd2AndY);
    assertFalse(mapAdd2AndY.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapAdd2AndY));
    assertEquals(MapsOps.ofOrdered(1, "x", 2, "y"), mapAdd2AndY);
    var map1AndNull = new HashMap<Integer, String>();
    map1AndNull.put(1, null);
    assertThrows(
        ParametersValidationException.class,
        () ->
            MapsOps.addEntry(
                Map.of(3, "z"),
                map1AndNull.entrySet().iterator().next()));
  }

  @Test
  public void testAppendKeyAndValue() {
    var mapEmptyAdd1AndX = MapsOps.appendKeyAndValue(Map.of(), 1, "x");
    assertNotNull(mapEmptyAdd1AndX);
    assertFalse(mapEmptyAdd1AndX.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapEmptyAdd1AndX));
    assertEquals(MapsOps.ofOrdered(1, "x"), mapEmptyAdd1AndX);
    var mapAdd2AndY = MapsOps.appendKeyAndValue(mapEmptyAdd1AndX, 2, "y");
    assertNotNull(mapAdd2AndY);
    assertFalse(mapAdd2AndY.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(mapAdd2AndY));
    assertEquals(MapsOps.ofOrdered(1, "x", 2, "y"), mapAdd2AndY);
  }

  @Test
  public void testAddMaps() {
    var mapAddA = MapsOps.addMaps(Map.of(1, "x1", 2, "x2"), Map.of(2, "x2", 3, "x3"));
    assertTrue(CollectionsOps.isUnmodifiable(mapAddA));
    assertEquals(Map.of(1, "x1", 2, "x2", 3, "x3"), mapAddA);
    var mapAddB = MapsOps.addMaps(mapAddA, Map.of(3, "x3", 4, "x4"));
    assertTrue(CollectionsOps.isUnmodifiable(mapAddB));
    assertEquals(Map.of(1, "x1", 2, "x2", 3, "x3", 4, "x4"), mapAddB);
    var mapContainingNull = new HashMap<Integer, String>();
    mapContainingNull.put(null, "y");
    mapContainingNull.put(6, "x6");
    var mapAddC = MapsOps.addMaps(mapAddB, Map.of(4, "x4", 5, "x5"), mapContainingNull);
    assertTrue(CollectionsOps.isUnmodifiable(mapAddC));
    assertEquals(Map.of(1, "x1", 2, "x2", 3, "x3", 4, "x4", 5, "x5", 6, "x6"), mapAddC);
  }

  @Test
  public void testAppendMaps() {
    var mapAppendA = MapsOps.appendMaps(Map.of(1, "x1"), Map.of(2, "x2"), Map.of(3, "x3"));
    assertTrue(CollectionsOps.isUnmodifiable(mapAppendA));
    assertEquals(
        List.of(entry(1, "x1"), entry(2, "x2"), entry(3, "x3")),
        mapAppendA.entrySet().stream().toList());
    var mapContainingNull = new LinkedHashMap<Integer, String>();
    mapContainingNull.put(7, null);
    mapContainingNull.put(6, "x6");
    var mapAppendB = MapsOps.appendMaps(mapAppendA, Map.of(4, "x4"), Map.of(5, "x5"), mapContainingNull);
    assertTrue(CollectionsOps.isUnmodifiable(mapAppendB));
    assertEquals(
        List.of(entry(1, "x1"), entry(2, "x2"), entry(3, "x3"), entry(4, "x4"), entry(5, "x5"), entry(6, "x6")),
        mapAppendB.entrySet().stream().toList());
  }

  @Test
  public void testToMapUnmodifiable() {
    var mapA = new HashMap<Integer, String>();
    mapA.put(null, "xnull");
    mapA.put(1, "x1");
    mapA.put(-1, null);
    mapA.put(2, "x2");
    @SuppressWarnings("SimplifyStreamApiCallChains")
    var entries = mapA.entrySet().stream().collect(Collectors.toList());
    entries.add(1, null);
    var mapB = MapsOps.toMapUnmodifiable(entries.stream());
    assertEquals(Map.of(1, "x1", 2, "x2"), mapB);
    var list = new ArrayList<String>();
    list.add(null);
    list.add("x1");
    list.add(null);
    list.add("x");
    list.add(null);
    list.add("2");
    list.add("x2");
    list.add("3");
    list.add("x3");
    var mapC = MapsOps.toMapUnmodifiable(
        list.stream(),
        string -> {
          if ((string != null) && (string.length() > 1)) {
            return Optional.of(entry(Integer.parseInt(string.substring(1, 2)), string));
          }

          return Optional.empty();
        });
    assertEquals(Map.of(1, "x1", 2, "x2", 3, "x3"), mapC);
  }

  @Test
  public void testToMapOrderedUnmodifiable() {
    var mapA = new LinkedHashMap<Integer, String>();
    mapA.put(null, "xnull");
    mapA.put(1, "x1");
    mapA.put(-1, null);
    mapA.put(2, "x2");
    @SuppressWarnings("SimplifyStreamApiCallChains")
    var entries = mapA.entrySet().stream().collect(Collectors.toList());
    entries.add(1, null);
    var mapB = MapsOps.toMapOrderedUnmodifiable(entries.stream());
    assertEquals(MapsOps.ofOrdered(1, "x1", 2, "x2"), mapB);
    var list = new ArrayList<String>();
    list.add(null);
    list.add("x1");
    list.add(null);
    list.add("x");
    list.add(null);
    list.add("2");
    list.add("x2");
    list.add("3");
    list.add("x3");
    var mapC = MapsOps.toMapOrderedUnmodifiable(
        list.stream(),
        string -> {
          if ((string != null) && (string.length() > 1)) {
            return Optional.of(entry(Integer.parseInt(string.substring(1, 2)), string));
          }

          return Optional.empty();
        });
    assertEquals(MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3"), mapC);
  }

  @Test
  public void testSwap() {
    var map = MapsOps.swap(Map.of(1, "x1", 2, "x2", 3, "x3"));
    assertEquals(Map.of("x1", 1, "x2", 2, "x3", 3), map);
  }

  @Test
  public void testSwapOrdered() {
    var map = MapsOps.swap(MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3"));
    assertEquals(MapsOps.ofOrdered("x1", 1, "x2", 2, "x3", 3), map);
  }

  @Test
  public void testOfOrderedX1() {
    var map = MapsOps.ofOrdered(
        1, "x");
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map));
    var map2 = new LinkedHashMap<Integer, String>();
    map2.put(1, "x");
    assertEquals(map2, map);
  }

  @Test
  public void testOfOrderedX2() {
    var map = MapsOps.ofOrdered(
        1, "x1",
        2, "x2");
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map));
    var map2 = new LinkedHashMap<Integer, String>();
    map2.put(1, "x1");
    map2.put(2, "x2");
    assertEquals(map2, map);
  }

  @Test
  public void testOfOrderedX3() {
    var map = MapsOps.ofOrdered(
        1, "x1",
        2, "x2",
        3, "x3");
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map));
    var map2 = new LinkedHashMap<Integer, String>();
    map2.put(1, "x1");
    map2.put(2, "x2");
    map2.put(3, "x3");
    assertEquals(map2, map);
  }

  @Test
  public void testOfOrderedX4() {
    var map = MapsOps.ofOrdered(
        1, "x1",
        2, "x2",
        3, "x3",
        4, "x4");
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map));
    var map2 = new LinkedHashMap<Integer, String>();
    map2.put(1, "x1");
    map2.put(2, "x2");
    map2.put(3, "x3");
    map2.put(4, "x4");
    assertEquals(map2, map);
  }

  @Test
  public void testOfOrderedX5() {
    var map = MapsOps.ofOrdered(
        1, "x1",
        2, "x2",
        3, "x3",
        4, "x4",
        5, "x5");
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map));
    var map2 = new LinkedHashMap<Integer, String>();
    map2.put(1, "x1");
    map2.put(2, "x2");
    map2.put(3, "x3");
    map2.put(4, "x4");
    map2.put(5, "x5");
    assertEquals(map2, map);
  }

  @Test
  public void testOfOrderedX6() {
    var map = MapsOps.ofOrdered(
        1, "x1",
        2, "x2",
        3, "x3",
        4, "x4",
        5, "x5",
        6, "x6");
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map));
    var map2 = new LinkedHashMap<Integer, String>();
    map2.put(1, "x1");
    map2.put(2, "x2");
    map2.put(3, "x3");
    map2.put(4, "x4");
    map2.put(5, "x5");
    map2.put(6, "x6");
    assertEquals(map2, map);
  }

  @Test
  public void testOfOrderedX7() {
    var map = MapsOps.ofOrdered(
        1, "x1",
        2, "x2",
        3, "x3",
        4, "x4",
        5, "x5",
        6, "x6",
        7, "x7");
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map));
    var map2 = new LinkedHashMap<Integer, String>();
    map2.put(1, "x1");
    map2.put(2, "x2");
    map2.put(3, "x3");
    map2.put(4, "x4");
    map2.put(5, "x5");
    map2.put(6, "x6");
    map2.put(7, "x7");
    assertEquals(map2, map);
  }

  @Test
  public void testOfOrderedX8() {
    var map = MapsOps.ofOrdered(
        1, "x1",
        2, "x2",
        3, "x3",
        4, "x4",
        5, "x5",
        6, "x6",
        7, "x7",
        8, "x8");
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map));
    var map2 = new LinkedHashMap<Integer, String>();
    map2.put(1, "x1");
    map2.put(2, "x2");
    map2.put(3, "x3");
    map2.put(4, "x4");
    map2.put(5, "x5");
    map2.put(6, "x6");
    map2.put(7, "x7");
    map2.put(8, "x8");
    assertEquals(map2, map);
  }

  @Test
  public void testOfOrderedX9() {
    var map = MapsOps.ofOrdered(
        1, "x1",
        2, "x2",
        3, "x3",
        4, "x4",
        5, "x5",
        6, "x6",
        7, "x7",
        8, "x8",
        9, "x9");
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map));
    var map2 = new LinkedHashMap<Integer, String>();
    map2.put(1, "x1");
    map2.put(2, "x2");
    map2.put(3, "x3");
    map2.put(4, "x4");
    map2.put(5, "x5");
    map2.put(6, "x6");
    map2.put(7, "x7");
    map2.put(8, "x8");
    map2.put(9, "x9");
    assertEquals(map2, map);
  }

  @Test
  public void testOfOrderedX10() {
    var map = MapsOps.ofOrdered(
        1, "x1",
        2, "x2",
        3, "x3",
        4, "x4",
        5, "x5",
        6, "x6",
        7, "x7",
        8, "x8",
        9, "x9",
        10, "x10");
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map));
    var map2 = new LinkedHashMap<Integer, String>();
    map2.put(1, "x1");
    map2.put(2, "x2");
    map2.put(3, "x3");
    map2.put(4, "x4");
    map2.put(5, "x5");
    map2.put(6, "x6");
    map2.put(7, "x7");
    map2.put(8, "x8");
    map2.put(9, "x9");
    map2.put(10, "x10");
    assertEquals(map2, map);
  }
}
