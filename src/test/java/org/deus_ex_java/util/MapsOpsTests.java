package org.deus_ex_java.util;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.util.function.VoidSupplier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings({"rawtypes", "unchecked", "DataFlowIssue"})
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
          var rawMap = (Map) checkedMap;
          rawMap.put("ValidKey", "InvalidValue");
        },
        "Checked map should reject invalid value types");
    assertThrows(
        ClassCastException.class,
        () -> {
          var rawMap = (Map) checkedMap;
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
          var rawMap = (Map) checkedMap;
          rawMap.put("ValidKey", "InvalidValue");
        },
        "Checked map should reject invalid value types");
    assertThrows(
        ClassCastException.class,
        () -> {
          var rawMap = (Map) checkedMap;
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
  void toNonEmptyWithNullReturnsEmpty() {
    var result = MapsOps.<String, Integer>toNonEmpty(null);
    assertTrue(result.isEmpty(), "Expected an empty Optional for a null input");
  }

  @Test
  void toNonEmptyWithEmptyListReturnsEmpty() {
    var result = MapsOps.<String, Integer>toNonEmpty(Map.of());
    assertTrue(result.isEmpty(), "Expected an empty Optional for an empty map input");
  }

  @Test
  void toNonEmptyWithElementsReturnsOccupiedOptional() {
    var populatedMap = Map.of("apple", 1, "banana", 2);
    var result = MapsOps.toNonEmpty(populatedMap);
    assertTrue(result.isPresent(), "Expected an occupied Optional for a non-empty map");
    assertEquals(populatedMap, result.get().map());
  }

  @SuppressWarnings("ConstantValue")
  @Test
  public void testIsNonNulls() {
    assertTrue(MapsOps.isNonNulls(1, "x"));
    assertFalse(MapsOps.isNonNulls(1, null));
    assertFalse(MapsOps.isNonNulls(null, "x"));
    assertFalse(MapsOps.isNonNulls(null, null));
  }

  @Test
  public void testEntryIsNonNulls() {
    assertTrue(MapsOps.isNonNulls(Map.of(1, "x").entrySet().iterator().next()));
    var map1AndNull = new HashMap<Integer, String>();
    map1AndNull.put(1, null);
    assertFalse(MapsOps.isNonNulls(map1AndNull.entrySet().iterator().next()));
  }

  @Test
  public void testInvalidate() {
    assertTrue(MapsOps.invalidate(1, "x").isEmpty());
    assertEquals(
        Optional.of(
            new ParametersValidationException(
                "MapsOps.isNonNulls failed preconditions on the key and/or value",
                List.of(
                    "key must not be null",
                    "value must not be null"))),
        MapsOps.invalidate(null, null));
    assertEquals(
        Optional.of(
            new ParametersValidationException(
                "MapsOps.isNonNulls failed preconditions on the key and/or value",
                List.of(
                    "key must not be null"))),
        MapsOps.invalidate(null, "x"));
    assertEquals(
        Optional.of(
            new ParametersValidationException(
                "MapsOps.isNonNulls failed preconditions on the key and/or value",
                List.of(
                    "value must not be null"))),
        MapsOps.invalidate(1, null));
  }

  @Test
  public void testEntryInvalidate() {
    assertTrue(MapsOps.invalidate(Map.entry(1, "x")).isEmpty());
    var mapEntryNullNull = new HashMap<>();
    mapEntryNullNull.put(null, null);
    var mapEntryA = mapEntryNullNull.entrySet().iterator().next();
    assertNull(mapEntryA.getKey());
    assertNull(mapEntryA.getValue());
    assertEquals(
        Optional.of(
            new ParametersValidationException(
                "MapsOps.isNonNulls failed preconditions on the key and/or value",
                List.of(
                    "key must not be null",
                    "value must not be null"))),
        MapsOps.invalidate(mapEntryA));
    var mapEntryNullString = new HashMap<>();
    mapEntryNullString.put(null, "x");
    var mapEntryB = mapEntryNullString.entrySet().iterator().next();
    assertNull(mapEntryB.getKey());
    assertNotNull(mapEntryB.getValue());
    assertEquals("x", mapEntryB.getValue());
    assertEquals(
        Optional.of(
            new ParametersValidationException(
                "MapsOps.isNonNulls failed preconditions on the key and/or value",
                List.of(
                    "key must not be null"))),
        MapsOps.invalidate(mapEntryB));
    var mapEntryIntegerNull = new HashMap<>();
    mapEntryIntegerNull.put(1, null);
    var mapEntryC = mapEntryIntegerNull.entrySet().iterator().next();
    assertNotNull(mapEntryC.getKey());
    assertNull(mapEntryC.getValue());
    assertEquals(1, mapEntryC.getKey());
    assertEquals(
        Optional.of(
            new ParametersValidationException(
                "MapsOps.isNonNulls failed preconditions on the key and/or value",
                List.of(
                    "value must not be null"))),
        MapsOps.invalidate(mapEntryC));
  }

  @Test
  public void testReturnExceptionOrCreatedEntry() {
    assertTrue(MapsOps.returnExceptionOrCreatedEntry(null, null).isLeft());
    assertTrue(MapsOps.returnExceptionOrCreatedEntry(1, null).isLeft());
    assertTrue(MapsOps.returnExceptionOrCreatedEntry(null, "x").isLeft());
    var exceptionOrCreatedEntry = MapsOps.returnExceptionOrCreatedEntry(1, "x");
    assertTrue(exceptionOrCreatedEntry.isRight());
    assertEquals(Map.entry(1, "x"), exceptionOrCreatedEntry.getRight());
  }

  @NullMarked
  private static <K, V> Entry<@Nullable K, @Nullable V> createEntryWithNullableKeyAndValue(
      @Nullable K key,
      @Nullable V value
  ) {
    return new Entry<@Nullable K, @Nullable V>() {
      @Override
      public @Nullable K getKey() {
        return key;
      }

      @Override
      public @Nullable V getValue() {
        return value;
      }

      @Override
      public @Nullable V setValue(@Nullable V value) {
        throw new UnsupportedOperationException();
      }
    };
  }

  @NullMarked
  private static <K, V> Map<@Nullable K, @Nullable V> createHashMapWithNullableKeyAndValue() {
    return new HashMap<@Nullable K, @Nullable V>();
  }

  @NullMarked
  private static <K, V> Map<@Nullable K, @Nullable V> addEntryWithNullableKeyAndValue(
      Map<@Nullable K, @Nullable V> map,
      @Nullable K key,
      @Nullable V value
  ) {
    var mapNew = new HashMap<@Nullable K, @Nullable V>(map);
    mapNew.put(key, value);

    return Collections.unmodifiableMap(mapNew);
  }

  @Test
  public void testReturnExceptionOrValidatedEntry() {
    var entryNullAndNull = createEntryWithNullableKeyAndValue(null, null);
    assertTrue(MapsOps.returnExceptionOrValidatedEntry(entryNullAndNull).isLeft());
    var entryOneAndNull = createEntryWithNullableKeyAndValue(1, null);
    assertTrue(MapsOps.returnExceptionOrValidatedEntry(entryOneAndNull).isLeft());
    var entryNullAndX = createEntryWithNullableKeyAndValue(null, "");
    assertTrue(MapsOps.returnExceptionOrValidatedEntry(entryNullAndX).isLeft());

    var entryValid = Map.entry(1, "x");
    var exceptionOrValidatedEntry = MapsOps.returnExceptionOrValidatedEntry(entryValid);
    assertTrue(exceptionOrValidatedEntry.isRight());
    assertSame(entryValid, exceptionOrValidatedEntry.getRight());
  }

  private void testThrowExceptionOrReturn_EntryHelper(
      VoidSupplier voidSupplier,
      String expectedParametersValidationExceptionMessage
  ) {
    var parametersValidationException = assertThrows(
        ParametersValidationException.class,
        voidSupplier::execute);
    assertEquals(
        expectedParametersValidationExceptionMessage,
        parametersValidationException.getMessage());
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  public void testThrowExceptionOrReturnCreatedEntry() {
    testThrowExceptionOrReturn_EntryHelper(
        () ->
            MapsOps.throwExceptionOrReturnCreatedEntry(null, null),
        "MapsOps.isNonNulls failed preconditions on the key and/or value - Parameter Validation Failures: [key must not be null|value must not be null]");
    testThrowExceptionOrReturn_EntryHelper(
        () ->
            MapsOps.throwExceptionOrReturnCreatedEntry(1, null),
        "MapsOps.isNonNulls failed preconditions on the key and/or value - Parameter Validation Failures: [value must not be null]");
    testThrowExceptionOrReturn_EntryHelper(
        () ->
            MapsOps.throwExceptionOrReturnCreatedEntry(null, "x"),
        "MapsOps.isNonNulls failed preconditions on the key and/or value - Parameter Validation Failures: [key must not be null]");
    var createdEntry = MapsOps.throwExceptionOrReturnCreatedEntry(1, "x");
    assertEquals(Map.entry(1, "x"), createdEntry);
  }

  @SuppressWarnings("NullableProblems")
  @Test
  public void testThrowExceptionOrReturnValidatedEntry() {
    testThrowExceptionOrReturn_EntryHelper(
        () ->
            MapsOps.throwExceptionOrReturnValidatedEntry(createEntryWithNullableKeyAndValue(null, null)),
        "MapsOps.isNonNulls failed preconditions on the key and/or value - Parameter Validation Failures: [key must not be null|value must not be null]");
    testThrowExceptionOrReturn_EntryHelper(
        () ->
            MapsOps.throwExceptionOrReturnValidatedEntry(createEntryWithNullableKeyAndValue(1, null)),
        "MapsOps.isNonNulls failed preconditions on the key and/or value - Parameter Validation Failures: [value must not be null]");
    testThrowExceptionOrReturn_EntryHelper(
        () ->
            MapsOps.throwExceptionOrReturnValidatedEntry(createEntryWithNullableKeyAndValue(null, "x")),
        "MapsOps.isNonNulls failed preconditions on the key and/or value - Parameter Validation Failures: [key must not be null]");
    var entryValid = Map.entry(1, "x");
    var validatedEntry = MapsOps.throwExceptionOrReturnValidatedEntry(entryValid);
    assertSame(entryValid, validatedEntry);
  }

  @Test
  public void testNullSanitize() {
    var mapNullAndNull =
        addEntryWithNullableKeyAndValue(
            addEntryWithNullableKeyAndValue(
                addEntryWithNullableKeyAndValue(
                    createHashMapWithNullableKeyAndValue(),
                    1, "x"),
                null, "y"),
            2, null);
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
    assertEquals(Map.of(), MapsOps.addMaps(null, Map.of(), null));
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
    assertEquals(Map.of(), MapsOps.appendMaps(null, Map.of(), null));
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
  public void testRemoveEntry() {
    var initialMap = Map.of(1, "x1", 2, "x2", 3, "x3");
    var mapRemove2 = MapsOps.removeEntry(initialMap, 2);
    assertEquals(Map.of(1, "x1", 3, "x3"), mapRemove2);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemove2));

    var mapRemove4 = MapsOps.removeEntry(initialMap, 4);
    assertEquals(Map.of(1, "x1", 2, "x2", 3, "x3"), mapRemove4);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemove4));

    var emptyMapRemove = MapsOps.removeEntry(Map.of(), 1);
    assertEquals(Map.of(), emptyMapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyMapRemove));

    var mapRemoveNull = MapsOps.removeEntry(initialMap, null);
    assertEquals(initialMap, mapRemoveNull, "Removing null should be safely ignored");
    assertTrue(CollectionsOps.isUnmodifiable(mapRemoveNull));
  }

  @Test
  public void testRemoveEntryOrdered() {
    var initialMap = MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3");
    var mapRemove2 = MapsOps.removeEntryOrdered(initialMap, 2);
    assertEquals(MapsOps.ofOrdered(1, "x1", 3, "x3"), mapRemove2);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemove2));

    var mapRemove4 = MapsOps.removeEntryOrdered(initialMap, 4);
    assertEquals(MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3"), mapRemove4);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemove4));

    var emptyMapRemove = MapsOps.removeEntryOrdered(Map.of(), 1);
    assertEquals(Map.of(), emptyMapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyMapRemove));

    var mapRemoveNull = MapsOps.removeEntryOrdered(initialMap, null);
    assertEquals(initialMap, mapRemoveNull, "Removing null should be safely ignored");
    assertTrue(CollectionsOps.isUnmodifiable(mapRemoveNull));
  }

  @Test
  public void testRemoveAllWithCollection() {
    var initialMap = Map.of(1, "x1", 2, "x2", 3, "x3", 4, "x4");
    var mapRemove = MapsOps.removeAll(initialMap, List.of(2, 4, 5));
    assertEquals(Map.of(1, "x1", 3, "x3"), mapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemove));

    var mapRemoveEmpty = MapsOps.removeAll(initialMap, List.of());
    assertEquals(Map.of(1, "x1", 2, "x2", 3, "x3", 4, "x4"), mapRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemoveEmpty));

    var emptyMapRemove = MapsOps.removeAll(Map.of(), List.of(1, 2));
    assertEquals(Map.of(), emptyMapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyMapRemove));
  }

  @Test
  public void testRemoveAllOrderedWithCollection() {
    var initialMap = MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3", 4, "x4");
    var mapRemove = MapsOps.removeAllOrdered(initialMap, List.of(2, 4, 5));
    assertEquals(MapsOps.ofOrdered(1, "x1", 3, "x3"), mapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemove));

    var mapRemoveEmpty = MapsOps.removeAllOrdered(initialMap, List.of());
    assertEquals(MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3", 4, "x4"), mapRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemoveEmpty));

    var emptyMapRemove = MapsOps.removeAllOrdered(Map.of(), List.of(1, 2));
    assertEquals(Map.of(), emptyMapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyMapRemove));
  }

  @Test
  public void testRemoveAllWithStream() {
    var initialMap = Map.of(1, "x1", 2, "x2", 3, "x3", 4, "x4");
    var mapRemove = MapsOps.removeAll(initialMap, Stream.of(2, 4, 5));
    assertEquals(Map.of(1, "x1", 3, "x3"), mapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemove));

    var mapRemoveEmpty = MapsOps.removeAll(initialMap, Stream.empty());
    assertEquals(Map.of(1, "x1", 2, "x2", 3, "x3", 4, "x4"), mapRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemoveEmpty));

    var emptyMapRemove = MapsOps.removeAll(Map.of(), Stream.of(1, 2));
    assertEquals(Map.of(), emptyMapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyMapRemove));
  }

  @Test
  public void testRemoveAllOrderedWithStream() {
    var initialMap = MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3", 4, "x4");
    var mapRemove = MapsOps.removeAllOrdered(initialMap, Stream.of(2, 4, 5));
    assertEquals(MapsOps.ofOrdered(1, "x1", 3, "x3"), mapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemove));

    var mapRemoveEmpty = MapsOps.removeAllOrdered(initialMap, Stream.empty());
    assertEquals(MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3", 4, "x4"), mapRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemoveEmpty));

    var emptyMapRemove = MapsOps.removeAllOrdered(Map.of(), Stream.of(1, 2));
    assertEquals(Map.of(), emptyMapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyMapRemove));
  }

  @Test
  public void testRemoveMaps() {
    var initialMap = Map.of(1, "x1", 2, "x2", 3, "x3", 4, "x4", 5, "x5", 6, "x6");

    var mapRemove = MapsOps.removeMaps(initialMap, Set.of(2, 4), Set.of(5));
    assertEquals(Map.of(1, "x1", 3, "x3", 6, "x6"), mapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemove));

    var mapRemoveEmpty = MapsOps.removeMaps(initialMap);
    assertEquals(initialMap, mapRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemoveEmpty));

    var emptyMapRemove = MapsOps.removeMaps(Map.of(), Set.of(1, 2));
    assertEquals(Map.of(), emptyMapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyMapRemove));

    var setContainingNull = new HashSet<Integer>();
    setContainingNull.add(null);
    setContainingNull.add(6);

    @SuppressWarnings("DataFlowIssue")
    var mapRemoveNulls = MapsOps.removeMaps(initialMap, null, Set.of(1), setContainingNull);
    assertEquals(Map.of(2, "x2", 3, "x3", 4, "x4", 5, "x5"), mapRemoveNulls);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemoveNulls));
  }

  @Test
  public void testRemoveMapsOrdered() {
    var initialMap = MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3", 4, "x4", 5, "x5", 6, "x6");

    var mapRemove = MapsOps.removeMapsOrdered(initialMap, Set.of(2, 4), Set.of(5));
    assertEquals(MapsOps.ofOrdered(1, "x1", 3, "x3", 6, "x6"), mapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemove));

    var mapRemoveEmpty = MapsOps.removeMapsOrdered(initialMap);
    assertEquals(MapsOps.ofOrdered(1, "x1", 2, "x2", 3, "x3", 4, "x4", 5, "x5", 6, "x6"), mapRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(mapRemoveEmpty));

    var emptyMapRemove = MapsOps.removeMapsOrdered(Map.of(), Set.of(1, 2));
    assertEquals(Map.of(), emptyMapRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyMapRemove));

    var setContainingNull = new HashSet<Integer>();
    setContainingNull.add(null);
    setContainingNull.add(6);

    @SuppressWarnings("DataFlowIssue")
    var mapRemoveNulls = MapsOps.removeMapsOrdered(initialMap, null, Set.of(1), setContainingNull);
    assertEquals(MapsOps.ofOrdered(2, "x2", 3, "x3", 4, "x4", 5, "x5"), mapRemoveNulls, "Encounter order should be preserved");
    assertTrue(CollectionsOps.isUnmodifiable(mapRemoveNulls));
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
    var mapB = MapsOps.toMap(entries.stream());
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
    var mapB = MapsOps.toMap(entries);
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

  @Test
  public void testStreamPipelineImmutabilityContracts() {
    var map1 = Map.of("a", 1, "b", 2);
    var map2 = Map.of("c", 3, "d", 4);
    var addedMap = MapsOps.addMaps(map1, map2);
    assertTrue(CollectionsOps.isUnmodifiable(addedMap));
    assertThrows(UnsupportedOperationException.class, () -> addedMap.put("e", 5));

    var removedEntryMap = MapsOps.removeEntry(map1, "a");
    assertTrue(CollectionsOps.isUnmodifiable(removedEntryMap));
    assertThrows(UnsupportedOperationException.class, () -> removedEntryMap.remove("b"));

    var swappedMap = MapsOps.swap(map1);
    assertTrue(CollectionsOps.isUnmodifiable(swappedMap));
    assertThrows(UnsupportedOperationException.class, swappedMap::clear);
  }
}
