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
  public void testNewHashMap() {
    var map = MapsOps.<String, Integer>newHashMap();
    assertNotNull(map);
    assertTrue(map.isEmpty());
    assertInstanceOf(HashMap.class, map);
    var checkedMap = MapsOps.newHashMap(String.class, Integer.class);
    assertNotNull(checkedMap);
    assertTrue(checkedMap.isEmpty());
    assertThrows(
        ClassCastException.class,
        () -> {
          @SuppressWarnings("rawtypes")
          var rawMap = (Map) checkedMap;
          //noinspection unchecked
          rawMap.put("ValidKey", "InvalidValue");
        },
        "Checked map should reject invalid value types");
    assertThrows(
        ClassCastException.class,
        () -> {
          @SuppressWarnings("rawtypes")
          var rawMap = (Map) checkedMap;
          //noinspection unchecked
          rawMap.put(123, 456);
        },
        "Checked map should reject invalid key types");
  }

  @Test
  public void testNewLinkedHashMap() {
    var map = MapsOps.<String, Double>newLinkedHashMap();
    assertNotNull(map);
    assertTrue(map.isEmpty());
    assertInstanceOf(LinkedHashMap.class, map);
    var checkedMap = MapsOps.newLinkedHashMap(String.class, Double.class);
    assertNotNull(checkedMap);
    assertTrue(checkedMap.isEmpty());
    assertThrows(
        ClassCastException.class,
        () -> {
          @SuppressWarnings("rawtypes")
          var rawMap = (Map) checkedMap;
          //noinspection unchecked
          rawMap.put("ValidKey", "InvalidValue");
        },
        "Checked map should reject invalid value types");
    assertThrows(
        ClassCastException.class,
        () -> {
          @SuppressWarnings("rawtypes")
          var rawMap = (Map) checkedMap;
          //noinspection unchecked
          rawMap.put(123, 45.6);
        },
        "Checked map should reject invalid key types");
  }

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
  public void testAddMapsCollisionResolution() {
    var map1 = Map.of(1, "Original_1", 2, "Original_2");
    var map2 = Map.of(2, "Overwritten_2", 3, "Original_3");
    var map3 = Map.of(3, "Overwritten_3", 4, "Original_4");
    var result = MapsOps.addMaps(map1, map2, map3);
    assertEquals(4, result.size());
    assertEquals("Original_1", result.get(1));
    assertEquals("Overwritten_2", result.get(2), "map2 should overwrite key 2 from map1");
    assertEquals("Overwritten_3", result.get(3), "map3 should overwrite key 3 from map2");
    assertEquals("Original_4", result.get(4));
    assertTrue(CollectionsOps.isUnmodifiable(result));
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
  public void testAppendMapsCollisionResolution() {
    var map1 = MapsOps.ofOrdered(1, "A1", 2, "B1");
    var map2 = MapsOps.ofOrdered(2, "B2", 3, "C1");
    var result = MapsOps.appendMaps(map1, map2);
    assertEquals(3, result.size());
    assertEquals("A1", result.get(1));
    assertEquals("B2", result.get(2), "map2 should overwrite the value for key 2");
    assertEquals("C1", result.get(3));
    var entries = result.entrySet().stream().toList();
    assertEquals(1, entries.get(0).getKey());
    assertEquals(2, entries.get(1).getKey(), "Key 2 should remain in its original encounter position");
    assertEquals("B2", entries.get(1).getValue());
    assertEquals(3, entries.get(2).getKey());
    assertTrue(CollectionsOps.isUnmodifiable(result));
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
  public void testSwapDuplicateValuesCollision() {
    var mapWithDuplicateValues = Map.of(
        1, "unique_value",
        2, "duplicate_value",
        3, "duplicate_value");
    var swappedMap = MapsOps.swap(mapWithDuplicateValues);
    assertEquals(2, swappedMap.size(), "The swapped map should silently drop one of the duplicate entries");
    assertTrue(swappedMap.containsKey("unique_value"));
    assertTrue(swappedMap.containsKey("duplicate_value"));
    assertTrue(CollectionsOps.isUnmodifiable(swappedMap));
  }

  @Test
  public void testSwapOrderedDuplicateValuesCollision() {
    var mapWithDuplicateValues = MapsOps.ofOrdered(
        1, "unique_value",
        2, "duplicate_value",
        3, "duplicate_value");
    var swappedMap = MapsOps.swapOrdered(mapWithDuplicateValues);
    assertEquals(2, swappedMap.size(), "The swapped map should silently drop one of the duplicate entries");
    var entries = swappedMap.entrySet().stream().toList();
    assertEquals("unique_value", entries.get(0).getKey());
    assertEquals("duplicate_value", entries.get(1).getKey());
    assertEquals(2, entries.get(1).getValue(), "The first encountered key (2) should be retained for the duplicate value");
    assertTrue(CollectionsOps.isUnmodifiable(swappedMap));
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
    //noinspection DataFlowIssue
    assertEquals(Map.of(), MapsOps.ofEntriesOrdered(null, ENTRY_NULL_NULL, null));
    //noinspection DataFlowIssue
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
