package org.deus_ex_java.util;

import org.deus_ex_java.lang.ParametersValidationException;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  public void testNullSanitize() {
    var mapNullAndNull = new HashMap<Integer, String>();
    mapNullAndNull.put(1, "x");
    mapNullAndNull.put(null, "y");
    mapNullAndNull.put(2, null);
    var mapNonNull = MapsOps.nullSanitize(mapNullAndNull);
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
    assertEquals(Map.of(), MapsOps.addMaps());
    //noinspection DataFlowIssue
    assertEquals(Map.of(), MapsOps.addMaps(null, Map.of(), null));
    //noinspection DataFlowIssue
    assertEquals(Map.of(1, "value"), MapsOps.addMaps(null, Map.of(), Map.of(1, "value"), Map.of(), null));
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
    assertEquals(Map.of(), MapsOps.appendMaps());
    //noinspection DataFlowIssue
    assertEquals(Map.of(), MapsOps.appendMaps(null, Map.of(), null));
    //noinspection DataFlowIssue
    assertEquals(Map.of(1, "value"), MapsOps.appendMaps(null, Map.of(), Map.of(1, "value"), Map.of(), null));
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
  public void testNullSanitizeStream() {
    var mapA = new HashMap<Integer, String>();
    mapA.put(null, "xnull");
    mapA.put(1, "x1");
    mapA.put(-1, null);
    mapA.put(2, "x2");
    @SuppressWarnings("SimplifyStreamApiCallChains")
    var entries = mapA.entrySet().stream().collect(Collectors.toList());
    entries.add(1, null);
    var mapB = MapsOps.nullSanitize(entries.stream());
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
    var mapC = MapsOps.toMap(
        list.stream(),
        string -> {
          if (string.length() > 1) {
            return Optional.of(entry(Integer.parseInt(string.substring(1, 2)), string));
          }

          return Optional.empty();
        });
    assertEquals(Map.of(1, "x1", 2, "x2", 3, "x3"), mapC);
  }

  @Test
  public void testNullSanitizeCollection() {
    var mapA = new HashMap<Integer, String>();
    mapA.put(null, "xnull");
    mapA.put(1, "x1");
    mapA.put(-1, null);
    mapA.put(2, "x2");
    @SuppressWarnings("SimplifyStreamApiCallChains")
    var entries = mapA.entrySet().stream().collect(Collectors.toList());
    entries.add(1, null);
    var mapB = MapsOps.nullSanitize(entries);
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
    //intentionally duplicate key
    list.add("x3");
    var mapC = MapsOps.toMap(
        list,
        string -> {
          if (string.length() > 1) {
            return Optional.of(entry(Integer.parseInt(string.substring(1, 2)), string));
          }

          return Optional.empty();
        });
    assertEquals(Map.of(1, "x1", 2, "x2", 3, "x3"), mapC);
  }

  @Test
  public void testToMap() {
    assertEquals(Map.of(), MapsOps.toMap(Stream.empty(), t -> Optional.empty()));
  }

  @Test
  public void testToMapOrderedStream() {
    var mapA = new LinkedHashMap<Integer, String>();
    mapA.put(null, "xnull");
    mapA.put(1, "x1");
    mapA.put(-1, null);
    mapA.put(2, "x2");
    @SuppressWarnings("SimplifyStreamApiCallChains")
    var entries = mapA.entrySet().stream().collect(Collectors.toList());
    entries.add(1, null);
    var mapB = MapsOps.toMapOrdered(entries.stream());
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
    //intentionally duplicate key
    list.add("x3");
    var mapC = MapsOps.toMapOrdered(
        list.stream(),
        string -> {
          if (string.length() > 1) {
            return Optional.of(entry(Integer.parseInt(string.substring(1, 2)), string));
          }

          return Optional.empty();
        });
    assertEquals(MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3"), mapC);
  }

  @Test
  public void testToMapOrdered() {
    var mapA = new LinkedHashMap<Integer, String>();
    mapA.put(null, "xnull");
    mapA.put(1, "x1");
    mapA.put(-1, null);
    mapA.put(2, "x2");
    @SuppressWarnings("SimplifyStreamApiCallChains")
    var entries = mapA.entrySet().stream().collect(Collectors.toList());
    entries.add(1, null);
    var mapB = MapsOps.toMapOrdered(entries);
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
    var mapC = MapsOps.toMapOrdered(
        list,
        string -> {
          if (string.length() > 1) {
            return Optional.of(entry(Integer.parseInt(string.substring(1, 2)), string));
          }

          return Optional.empty();
        });
    assertEquals(MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3"), mapC);
  }

  @Test
  public void testSwap() {
    assertEquals(Map.of(), MapsOps.swap(Map.of()));
    var map = MapsOps.swap(Map.of(1, "x1", 2, "x2", 3, "x3"));
    assertEquals(Map.of("x1", 1, "x2", 2, "x3", 3), map);
  }

  @Test
  public void testSwapOrdered() {
    assertEquals(Map.of(), MapsOps.swapOrdered(Map.of()));
    var map = MapsOps.swap(MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3"));
    assertEquals(MapsOps.ofOrdered("x1", 1, "x2", 2, "x3", 3), map);
  }

  @Test
  public void testReverse() {
    assertEquals(Map.of(), MapsOps.reverse(Map.of()));
    assertEquals(Map.of(), MapsOps.reverse(Stream.empty()));
    var expectedMapOrdered = MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3");
    var nullContainingMapOrdered = new LinkedHashMap<Integer, String>();
    nullContainingMapOrdered.put(null, "xnull");
    nullContainingMapOrdered.put(1, "x1");
    nullContainingMapOrdered.put(-1, null);
    nullContainingMapOrdered.put(2, "x2");
    nullContainingMapOrdered.put(3, "x3");
    nullContainingMapOrdered.put(4, null);

    assertEquals(expectedMapOrdered, MapsOps.reverse(nullContainingMapOrdered.entrySet().stream()));
    assertEquals(expectedMapOrdered, MapsOps.reverse(nullContainingMapOrdered));
  }

  private static final Map.Entry<Integer, String> ENTRY_NULL_NULL =
      new Map.Entry<>() {
        @Override
        public Integer getKey() {
          return null;
        }

        @Override
        public String getValue() {
          return null;
        }

        @Override
        public String setValue(String value) {
          return null;
        }
      };

  @Test
  public void testOfEntriesOrdered() {
    var mapNoArg = MapsOps.ofEntriesOrdered();
    assertTrue(mapNoArg.isEmpty());
    assertEquals(Map.of(), MapsOps.ofEntriesOrdered(null, ENTRY_NULL_NULL, null));
    assertEquals(Map.of(1, "test"), MapsOps.ofEntriesOrdered(null, entry(1, "test"), ENTRY_NULL_NULL, null));
    var illegalArgumentException = assertThrows(
        IllegalArgumentException.class,
        () ->
            MapsOps.ofEntriesOrdered(
                entry(1, "test 1"),
                entry(2, "test 2"),
                entry(1, "test 1 oopsie"),
                entry(3, "test 3"),
                entry(4, "test 4"),
                entry(3, "test 3 oopsie")));
    assertEquals("duplicate keys encountered - 1,3", illegalArgumentException.getMessage());
    var map = MapsOps.ofEntriesOrdered(
        entry(1, "x"));
    assertNotNull(map);
    assertFalse(map.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map));
    var map2 = new LinkedHashMap<Integer, String>();
    map2.put(1, "x");
    assertEquals(map2, map);
    var map3 = MapsOps.ofEntriesOrdered(
        entry(1, "x1"),
        entry(2, "x2"));
    assertNotNull(map3);
    assertFalse(map3.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(map3));
    var map4 = new LinkedHashMap<Integer, String>();
    map4.put(1, "x1");
    map4.put(2, "x2");
    assertEquals(map4, map3);
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
