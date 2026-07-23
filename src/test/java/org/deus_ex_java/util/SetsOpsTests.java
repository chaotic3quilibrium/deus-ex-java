package org.deus_ex_java.util;

import org.deus_ex_java.util.SetsOps.SetPair;
import org.deus_ex_java.util.SetsOps.SetPairViewKey;
import org.deus_ex_java.util.tuple.Tuple2;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SetsOpsTests {

  @Test
  public void testNewHashSet() {
    var set = SetsOps.<String>newHashSet();
    assertNotNull(set);
    assertTrue(set.isEmpty());
    assertInstanceOf(HashSet.class, set);
    var checkedSet = SetsOps.newHashSet(Integer.class);
    assertNotNull(checkedSet);
    assertTrue(checkedSet.isEmpty());
    assertThrows(
        ClassCastException.class,
        () -> {
          @SuppressWarnings("rawtypes")
          var rawSet = (Set) checkedSet;
          //noinspection unchecked
          rawSet.add("This should fail");
        });
  }

  @Test
  public void testNewLinkedHashSet() {
    var set = SetsOps.<String>newLinkedHashSet();
    assertNotNull(set);
    assertTrue(set.isEmpty());
    assertInstanceOf(LinkedHashSet.class, set);
    var checkedSet = SetsOps.newLinkedHashSet(Double.class);
    assertNotNull(checkedSet);
    assertTrue(checkedSet.isEmpty());
    assertThrows(
        ClassCastException.class,
        () -> {
          @SuppressWarnings("rawtypes")
          var rawSet = (Set) checkedSet;
          //noinspection unchecked
          rawSet.add("This should fail");
        });
  }

  @Test
  public void testNullToEmpty() {
    var setEmptyNull = SetsOps.nullToEmpty(null);
    assertNotNull(setEmptyNull);
    assertTrue(setEmptyNull.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(setEmptyNull));
    var setEmptySetOf = SetsOps.nullToEmpty(Set.of());
    assertNotNull(setEmptySetOf);
    assertTrue(setEmptySetOf.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(setEmptySetOf));
    var setEmptySetOf1 = SetsOps.nullToEmpty(Set.of(1));
    assertNotNull(setEmptySetOf1);
    assertFalse(setEmptySetOf1.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(setEmptySetOf1));
    assertEquals(Set.of(1), setEmptySetOf1);
  }

  @Test
  void toNonEmptyWithNullReturnsEmpty() {
    var result = SetsOps.<String>toNonEmpty(null);
    assertTrue(result.isEmpty(), "Expected an empty Optional for a null input");
  }

  @Test
  void toNonEmptyWithEmptyListReturnsEmpty() {
    var result = SetsOps.<String>toNonEmpty(Set.of());
    assertTrue(result.isEmpty(), "Expected an empty Optional for an empty set input");
  }

  @Test
  void toNonEmptyWithElementsReturnsOccupiedOptional() {
    var populatedSet = Set.of("apple", "banana");
    var result = SetsOps.toNonEmpty(populatedSet);
    assertTrue(result.isPresent(), "Expected an occupied Optional for a non-empty set");
    assertEquals(populatedSet, result.get().set());
  }

  @Test
  public void testAddItem() {
    var setEmptyAdd1 = SetsOps.addItem(Set.of(), 1);
    assertEquals(Set.of(1), setEmptyAdd1);
    assertTrue(CollectionsOps.isUnmodifiable(setEmptyAdd1));
    var setAdd2 = SetsOps.addItem(setEmptyAdd1, 2);
    assertEquals(Set.of(1, 2), setAdd2);
    assertTrue(CollectionsOps.isUnmodifiable(setAdd2));
  }

  @Test
  public void testAddItemNullValueBehavior() {
    var setA = SetsOps.addItem(Set.of(), null);
    assertNotNull(setA);
    assertTrue(setA.isEmpty(), "Adding null to an empty set should safely return an empty set");
    assertTrue(CollectionsOps.isUnmodifiable(setA));
    var setB = SetsOps.addItem(Set.of(1, 2), null);
    assertEquals(2, setB.size(), "Adding null to a populated set should be ignored");
    assertEquals(Set.of(1, 2), setB);
    assertTrue(CollectionsOps.isUnmodifiable(setB));
  }

  @Test
  public void testAppendItem() {
    var setA = SetsOps.appendItem(Set.of(1), 2);
    assertTrue(CollectionsOps.isUnmodifiable(setA));
    assertEquals(List.of(1, 2), setA.stream().toList());
    var setB = SetsOps.appendItem(setA, 3);
    assertTrue(CollectionsOps.isUnmodifiable(setB));
    assertEquals(List.of(1, 2, 3), setB.stream().toList());
    var setC = SetsOps.appendItem(Set.of(), 10);
    assertEquals(Set.of(10), setC);
  }

  @Test
  public void testAddSets() {
    var setAddA = SetsOps.addSets(Set.of(1, 2), Set.of(2, 3));
    assertTrue(CollectionsOps.isUnmodifiable(setAddA));
    assertEquals(Set.of(1, 2, 3), setAddA);
    var setAddB = SetsOps.addSets(setAddA, Set.of(3, 4));
    assertTrue(CollectionsOps.isUnmodifiable(setAddB));
    assertEquals(Set.of(1, 2, 3, 4), setAddB);
    var setContainingNull = new HashSet<Integer>();
    setContainingNull.add(null);
    setContainingNull.add(6);
    var setAddC = SetsOps.addSets(setAddB, Set.of(4, 5), setContainingNull);
    assertTrue(CollectionsOps.isUnmodifiable(setAddC));
    assertEquals(Set.of(1, 2, 3, 4, 5, 6), setAddC);
    @SuppressWarnings("DataFlowIssue")
    var setAddD = SetsOps.addSets(Set.of(), null, Set.of());
    assertEquals(Set.of(), setAddD);
    var a = new Set[]{};
    @SuppressWarnings("unchecked")
    var setAddE = SetsOps.addSets(a);
    assertEquals(Set.of(), setAddE);
  }

  @Test
  public void testAppendItemNullValueBehavior() {
    // Ordered set testing
    var setA = SetsOps.appendItem(Set.of(), null);
    assertNotNull(setA);
    assertTrue(setA.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(setA));

    var setB = SetsOps.appendItem(SetsOps.ofOrdered(1, 2), null);
    assertEquals(2, setB.size());
    assertEquals(List.of(1, 2), setB.stream().toList(), "Encounter order should be preserved while ignoring null");
    assertTrue(CollectionsOps.isUnmodifiable(setB));
  }

  @Test
  public void testAppendSets() {
    var setAppendA = SetsOps.appendSets(Set.of(1), Set.of(2), Set.of(3));
    assertTrue(CollectionsOps.isUnmodifiable(setAppendA));
    assertEquals(List.of(1, 2, 3), setAppendA.stream().toList());
    var setContainingNull = new HashSet<Integer>();
    setContainingNull.add(null);
    setContainingNull.add(6);
    var setAppendB = SetsOps.appendSets(setAppendA, Set.of(4), Set.of(5), setContainingNull);
    assertTrue(CollectionsOps.isUnmodifiable(setAppendB));
    assertEquals(List.of(1, 2, 3, 4, 5, 6), setAppendB.stream().toList());
    @SuppressWarnings("DataFlowIssue")
    var setAddD = SetsOps.appendSets(Set.of(), null, Set.of());
    assertEquals(Set.of(), setAddD);
    var a = new Set[]{};
    @SuppressWarnings("unchecked")
    var setAddE = SetsOps.appendSets(a);
    assertEquals(Set.of(), setAddE);
  }

  @Test
  public void testRemoveItem() {
    var initialSet = Set.of(1, 2, 3);
    var setRemove2 = SetsOps.removeItem(initialSet, 2);
    assertEquals(Set.of(1, 3), setRemove2);
    assertTrue(CollectionsOps.isUnmodifiable(setRemove2));

    var setRemove4 = SetsOps.removeItem(initialSet, 4);
    assertEquals(Set.of(1, 2, 3), setRemove4);
    assertTrue(CollectionsOps.isUnmodifiable(setRemove4));

    var setEmptyRemove = SetsOps.removeItem(Set.of(), 1);
    assertEquals(Set.of(), setEmptyRemove);
    assertTrue(CollectionsOps.isUnmodifiable(setEmptyRemove));
  }

  @Test
  public void testRemoveItemNullValueBehavior() {
    var setA = SetsOps.removeItem(Set.of(), null);
    assertNotNull(setA);
    assertTrue(setA.isEmpty(), "Removing null from an empty set should safely return an empty set");
    assertTrue(CollectionsOps.isUnmodifiable(setA));

    var setB = SetsOps.removeItem(Set.of(1, 2), null);
    assertEquals(2, setB.size(), "Removing null from a populated set should be ignored");
    assertEquals(Set.of(1, 2), setB);
    assertTrue(CollectionsOps.isUnmodifiable(setB));
  }

  @Test
  public void testRemoveItemOrdered() {
    var initialSet = SetsOps.ofOrdered(1, 2, 3);
    var setRemove2 = SetsOps.removeItemOrdered(initialSet, 2);
    assertEquals(List.of(1, 3), setRemove2.stream().toList());
    assertTrue(CollectionsOps.isUnmodifiable(setRemove2));

    var setRemove4 = SetsOps.removeItemOrdered(initialSet, 4);
    assertEquals(List.of(1, 2, 3), setRemove4.stream().toList());
    assertTrue(CollectionsOps.isUnmodifiable(setRemove4));

    var setEmptyRemove = SetsOps.removeItemOrdered(Set.of(), 1);
    assertEquals(Set.of(), setEmptyRemove);
    assertTrue(CollectionsOps.isUnmodifiable(setEmptyRemove));
  }

  @Test
  public void testRemoveItemOrderedNullValueBehavior() {
    var setA = SetsOps.removeItemOrdered(Set.of(), null);
    assertNotNull(setA);
    assertTrue(setA.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(setA));

    var setB = SetsOps.removeItemOrdered(SetsOps.ofOrdered(1, 2), null);
    assertEquals(2, setB.size());
    assertEquals(List.of(1, 2), setB.stream().toList(), "Encounter order should be preserved while ignoring null");
    assertTrue(CollectionsOps.isUnmodifiable(setB));
  }

  @Test
  public void testRemoveAllWithCollection() {
    var initialSet = Set.of(1, 2, 3, 4);
    var setRemove = SetsOps.removeAll(initialSet, List.of(2, 4, 5));
    assertEquals(Set.of(1, 3), setRemove);
    assertTrue(CollectionsOps.isUnmodifiable(setRemove));

    var setRemoveEmpty = SetsOps.removeAll(initialSet, List.of());
    assertEquals(Set.of(1, 2, 3, 4), setRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(setRemoveEmpty));

    var emptySetRemove = SetsOps.removeAll(Set.of(), List.of(1, 2));
    assertEquals(Set.of(), emptySetRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptySetRemove));
  }

  @Test
  public void testRemoveAllOrderedWithCollection() {
    var initialSet = SetsOps.ofOrdered(1, 2, 3, 4);
    var setRemove = SetsOps.removeAllOrdered(initialSet, Set.of(2, 4, 5));
    assertEquals(List.of(1, 3), setRemove.stream().toList());
    assertTrue(CollectionsOps.isUnmodifiable(setRemove));

    var setRemoveEmpty = SetsOps.removeAllOrdered(initialSet, List.of());
    assertEquals(List.of(1, 2, 3, 4), initialSet.stream().toList());
    assertTrue(CollectionsOps.isUnmodifiable(setRemoveEmpty));

    var emptySetRemove = SetsOps.removeAllOrdered(Set.of(), List.of(1, 2));
    assertEquals(Set.of(), emptySetRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptySetRemove));
  }

  @Test
  public void testRemoveAllWithStream() {
    var initialSet = Set.of(1, 2, 3, 4);
    var setRemove = SetsOps.removeAll(initialSet, Stream.of(2, 4, 5));
    assertEquals(Set.of(1, 3), setRemove);
    assertTrue(CollectionsOps.isUnmodifiable(setRemove));

    var setRemoveEmpty = SetsOps.removeAll(initialSet, Stream.empty());
    assertEquals(Set.of(1, 2, 3, 4), setRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(setRemoveEmpty));

    var emptySetRemove = SetsOps.removeAll(Set.of(), Stream.of(1, 2));
    assertEquals(Set.of(), emptySetRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptySetRemove));
  }

  @Test
  public void testRemoveAllOrderedWithStream() {
    var initialSet = SetsOps.ofOrdered(1, 2, 3, 4);
    var setRemove = SetsOps.removeAllOrdered(initialSet, Stream.of(2, 4, 5));
    assertEquals(List.of(1, 3), setRemove.stream().toList());
    assertTrue(CollectionsOps.isUnmodifiable(setRemove));

    var setRemoveEmpty = SetsOps.removeAllOrdered(initialSet, Stream.empty());
    assertEquals(List.of(1, 2, 3, 4), setRemoveEmpty.stream().toList());
    assertTrue(CollectionsOps.isUnmodifiable(setRemoveEmpty));

    var emptySetRemove = SetsOps.removeAllOrdered(Set.of(), Stream.of(1, 2));
    assertEquals(Set.of(), emptySetRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptySetRemove));
  }

  @Test
  public void testRemoveSets() {
    var initialSet = Set.of(1, 2, 3, 4, 5, 6);

    var setRemove = SetsOps.removeSets(initialSet, Set.of(2, 4), Set.of(5));
    assertEquals(Set.of(1, 3, 6), setRemove);
    assertTrue(CollectionsOps.isUnmodifiable(setRemove));

    var setRemoveEmpty = SetsOps.removeSets(initialSet);
    assertEquals(initialSet, setRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(setRemoveEmpty));

    var emptySetRemove = SetsOps.removeSets(Set.of(), Set.of(1, 2));
    assertEquals(Set.of(), emptySetRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptySetRemove));

    var setContainingNull = new HashSet<Integer>();
    setContainingNull.add(null);
    setContainingNull.add(6);

    @SuppressWarnings("DataFlowIssue")
    var setRemoveNulls = SetsOps.removeSets(initialSet, null, Set.of(1), setContainingNull);
    assertEquals(Set.of(2, 3, 4, 5), setRemoveNulls);
    assertTrue(CollectionsOps.isUnmodifiable(setRemoveNulls));
  }

  @Test
  public void testRemoveSetsOrdered() {
    var initialSet = SetsOps.ofOrdered(1, 2, 3, 4, 5, 6);

    var setRemove = SetsOps.removeSetsOrdered(initialSet, Set.of(2, 4), Set.of(5));
    assertEquals(List.of(1, 3, 6), setRemove.stream().toList());
    assertTrue(CollectionsOps.isUnmodifiable(setRemove));

    var setRemoveEmpty = SetsOps.removeSetsOrdered(initialSet);
    assertEquals(List.of(1, 2, 3, 4, 5, 6), setRemoveEmpty.stream().toList());
    assertTrue(CollectionsOps.isUnmodifiable(setRemoveEmpty));

    var emptySetRemove = SetsOps.removeSetsOrdered(Set.of(), Set.of(1, 2));
    assertEquals(Set.of(), emptySetRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptySetRemove));

    var setContainingNull = new HashSet<Integer>();
    setContainingNull.add(null);
    setContainingNull.add(6);

    @SuppressWarnings("DataFlowIssue")
    var setRemoveNulls = SetsOps.removeSetsOrdered(initialSet, null, Set.of(1), setContainingNull);
    assertEquals(List.of(2, 3, 4, 5), setRemoveNulls.stream().toList(), "Encounter order should be preserved");
    assertTrue(CollectionsOps.isUnmodifiable(setRemoveNulls));
  }

  @Test
  public void testNullSanitizeStream() {
    var expectedSet = Set.of(3, 2, 1);
    var nullContainingSet = Stream.of(null, 2, null, 1, null, 3, null).collect(Collectors.toSet());
    assertEquals(4, nullContainingSet.size());
    var actualSet = SetsOps.nullSanitize(nullContainingSet.stream());
    assertEquals(expectedSet, actualSet);
    assertTrue(CollectionsOps.isUnmodifiable(actualSet));
  }

  @Test
  public void testNullSanitize() {
    var expectedSet = Set.of(3, 2, 1);
    var nullContainingSet = Stream.of(null, 2, null, 1, null, 3, null).collect(Collectors.toSet());
    assertEquals(4, nullContainingSet.size());
    var actualSet = SetsOps.nullSanitize(nullContainingSet);
    assertEquals(expectedSet, actualSet);
    assertTrue(CollectionsOps.isUnmodifiable(actualSet));
  }

  @Test
  public void testToSetOrderedStream() {
    assertEquals(Set.of(), SetsOps.toSetOrdered(Stream.empty()));
    var expectedSetOrdered = new LinkedHashSet<>(List.of(3, 2, 1));
    var nullContainingSetOrdered = new LinkedHashSet<>(Stream.of(null, 3, null, 2, null, 1, null).toList());
    assertEquals(4, nullContainingSetOrdered.size());
    assertNull(nullContainingSetOrdered.iterator().next());
    var actualSetOrdered = SetsOps.toSetOrdered(nullContainingSetOrdered.stream());
    assertEquals(expectedSetOrdered, actualSetOrdered);
    assertEquals(expectedSetOrdered.stream().toList(), actualSetOrdered.stream().toList());
    assertTrue(CollectionsOps.isUnmodifiable(actualSetOrdered));
  }

  @Test
  public void testToSetOrdered() {
    var expectedSetOrdered = new LinkedHashSet<>(List.of(3, 2, 1));
    var nullContainingSetOrdered = new LinkedHashSet<>(Stream.of(null, 3, null, 2, null, 1, null).toList());
    assertEquals(4, nullContainingSetOrdered.size());
    assertNull(nullContainingSetOrdered.iterator().next());
    var actualSetOrdered = SetsOps.toSetOrdered(nullContainingSetOrdered);
    assertEquals(expectedSetOrdered, actualSetOrdered);
    assertEquals(expectedSetOrdered.stream().toList(), actualSetOrdered.stream().toList());
    assertTrue(CollectionsOps.isUnmodifiable(actualSetOrdered));
  }

  @Test
  public void testReverse() {
    assertEquals(Set.of(), SetsOps.reverse(Set.of()));
    assertEquals(Set.of(), SetsOps.reverse(Stream.empty()));
    var expectedSetOrdered = new LinkedHashSet<>(List.of(1, 2, 3));
    var nullContainingSetOrdered = new LinkedHashSet<>(Stream.of(null, 3, null, 2, null, 1, null).toList());
    assertEquals(expectedSetOrdered, SetsOps.reverse(nullContainingSetOrdered.stream()));
    assertEquals(expectedSetOrdered, SetsOps.reverse(nullContainingSetOrdered));
  }

  @Test
  public void testContainsAny() {
    //containsAny
    var leftTsEmpty = Set.<Integer>of();
    var rightTsEmpty = Set.<Integer>of();
    assertFalse(SetsOps.containsAny(leftTsEmpty, rightTsEmpty));
    var leftTs1 = Set.of(1);
    var rightTs1 = Set.of(1);
    assertFalse(SetsOps.containsAny(leftTsEmpty, rightTs1));
    assertFalse(SetsOps.containsAny(leftTs1, rightTsEmpty));
    assertTrue(SetsOps.containsAny(leftTs1, rightTs1));
    var leftTs2 = Set.of(2);
    var rightTs2 = Set.of(2);
    assertFalse(SetsOps.containsAny(leftTsEmpty, rightTs2));
    assertFalse(SetsOps.containsAny(leftTs2, rightTsEmpty));
    assertFalse(SetsOps.containsAny(leftTs1, rightTs2));
    assertFalse(SetsOps.containsAny(leftTs2, rightTs1));
    assertTrue(SetsOps.containsAny(leftTs2, rightTs2));
    var leftTs12 = Set.of(1, 2);
    var rightTs12 = Set.of(1, 2);
    assertFalse(SetsOps.containsAny(leftTsEmpty, rightTs12));
    assertFalse(SetsOps.containsAny(leftTs12, rightTsEmpty));
    assertTrue(SetsOps.containsAny(leftTs1, rightTs12));
    assertTrue(SetsOps.containsAny(leftTs12, rightTs1));
    assertTrue(SetsOps.containsAny(leftTs2, rightTs12));
    assertTrue(SetsOps.containsAny(leftTs12, rightTs2));
    assertTrue(SetsOps.containsAny(leftTs12, rightTs12));
  }


  @Test
  public void testToDistinctAndDupes() {
    assertEquals(
        new Tuple2<>(Set.<Integer>of(), Set.<Integer>of()),
        SetsOps.toDistinctAndDupes(List.<Integer>of()));
    assertEquals(
        new Tuple2<>(Set.of(1, 2, 3, 4), Set.of()),
        SetsOps.toDistinctAndDupes(List.of(1, 2, 3, 4)));
    assertEquals(
        new Tuple2<>(Set.of(1, 2, 3, 4), Set.of(2, 3)),
        SetsOps.toDistinctAndDupes(List.of(1, 2, 2, 3, 4, 3)));
    assertEquals(
        new Tuple2<>(Set.of(1, 2, 3, 4), Set.of(1, 2, 3, 4)),
        SetsOps.toDistinctAndDupes(List.of(1, 2, 2, 3, 4, 3, 1, 4)));
    assertEquals(
        new Tuple2<>(Set.<Integer>of(), Set.<Integer>of()),
        SetsOps.toDistinctAndDupes(Stream.<Integer>of()));
    assertEquals(
        new Tuple2<>(Set.of(1, 2, 3, 4), Set.of()),
        SetsOps.toDistinctAndDupes(Stream.of(1, 2, 3, 4)));
    assertEquals(
        new Tuple2<>(Set.of(1, 2, 3, 4), Set.of(2, 3)),
        SetsOps.toDistinctAndDupes(Stream.of(1, 2, 2, 3, 4, 3)));
    assertEquals(
        new Tuple2<>(Set.of(1, 2, 3, 4), Set.of(1, 2, 3, 4)),
        SetsOps.toDistinctAndDupes(Stream.of(1, 2, 2, 3, 4, 3, 1, 4)));
  }

  private static <T1, T2, M1, M2> Tuple2<@NonNull M1, @NonNull M2> tuple2Map(
      Tuple2<@NonNull T1, @NonNull T2> tuple2,
      Function<T1, M1> fT1ToM1,
      Function<T2, M2> fT2ToM2
  ) {
    return new Tuple2<>(
        fT1ToM1.apply(tuple2._1()),
        fT2ToM2.apply(tuple2._2()));
  }

  @Test
  public void testToDistinctAndDupesOrdered() {
    assertEquals(
        new Tuple2<>(Set.<Integer>of(), Set.<Integer>of()),
        SetsOps.toDistinctAndDupesOrdered(List.<Integer>of()));
    assertEquals(
        new Tuple2<>(new LinkedHashSet<>(List.of(1, 2, 3, 4)), Set.of()),
        SetsOps.toDistinctAndDupesOrdered(List.of(1, 2, 3, 4)));
    assertEquals(
        new Tuple2<>(new LinkedHashSet<>(List.of(1, 2, 3, 4)), new LinkedHashSet<>(List.of(2, 3))),
        SetsOps.toDistinctAndDupesOrdered(List.of(1, 2, 2, 3, 4, 3)));
    assertEquals(
        new Tuple2<>(new LinkedHashSet<>(List.of(1, 2, 3, 4)), new LinkedHashSet<>(List.of(2, 3, 1, 4))),
        SetsOps.toDistinctAndDupesOrdered(List.of(1, 2, 2, 3, 4, 3, 1, 4)));
    assertEquals(
        new Tuple2<>(Set.<Integer>of(), Set.<Integer>of()),
        SetsOps.toDistinctAndDupesOrdered(Stream.<Integer>of()));
    assertEquals(
        new Tuple2<>(List.of(1, 2, 3, 4), List.of()),
        tuple2Map(
            SetsOps.toDistinctAndDupesOrdered(Stream.of(1, 2, 3, 4)),
            set -> set.stream().toList(),
            set -> set.stream().toList()));
    assertEquals(
        new Tuple2<>(List.of(1, 2, 3, 4), List.of(2, 3)),
        tuple2Map(
            SetsOps.toDistinctAndDupesOrdered(Stream.of(1, 2, 2, 3, 4, 3)),
            set -> set.stream().toList(),
            set -> set.stream().toList()));
    assertEquals(
        new Tuple2<>(List.of(1, 2, 3, 4), List.of(2, 3, 1, 4)),
        tuple2Map(
            SetsOps.toDistinctAndDupesOrdered(Stream.of(1, 2, 2, 3, 4, 3, 1, 4)),
            set -> set.stream().toList(),
            set -> set.stream().toList()));
  }

  private <T> void validateSetPair(
      Map<SetPairViewKey, Set<T>> expectedTsBySetPairViewKey,
      boolean expectedIsEqual,
      SetPair<T> actualSetPair
  ) {
    assertEquals(expectedIsEqual, actualSetPair.isEqual());
    assertEquals(expectedTsBySetPairViewKey.get(SetPairViewKey.UNION), actualSetPair.union());
    assertEquals(expectedTsBySetPairViewKey.get(SetPairViewKey.LEFT), actualSetPair.left());
    assertEquals(expectedTsBySetPairViewKey.get(SetPairViewKey.RIGHT), actualSetPair.right());
    assertEquals(expectedTsBySetPairViewKey.get(SetPairViewKey.INTERSECTION), actualSetPair.intersection());
    assertEquals(expectedTsBySetPairViewKey.get(SetPairViewKey.LEFT_DIFFERENCE), actualSetPair.leftDifference());
    assertEquals(expectedTsBySetPairViewKey.get(SetPairViewKey.RIGHT_DIFFERENCE), actualSetPair.rightDifference());
    assertEquals(expectedTsBySetPairViewKey.get(SetPairViewKey.DIFFERENCE), actualSetPair.difference());
    var actualTsBySetPairViewKey = actualSetPair.toMap();
    assertEquals(expectedTsBySetPairViewKey, actualTsBySetPairViewKey);
    assertTrue(CollectionsOps.isUnmodifiable(actualTsBySetPairViewKey));
    actualTsBySetPairViewKey.values()
        .forEach(ts ->
            assertTrue(CollectionsOps.isUnmodifiable(ts)));
  }

  @Test
  public void testSetPair() {
    var empty = Set.<Integer>of();
    var set123 = Set.of(1, 2, 3);
    var set234 = Set.of(2, 3, 4);
    var set567 = Set.of(5, 6, 7);
    var set012345678 = Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8);
    validateSetPair(
        Map.of(
            SetPairViewKey.UNION, Set.of(),
            SetPairViewKey.LEFT, Set.of(),
            SetPairViewKey.RIGHT, Set.of(),
            SetPairViewKey.INTERSECTION, Set.of(),
            SetPairViewKey.LEFT_DIFFERENCE, Set.of(),
            SetPairViewKey.RIGHT_DIFFERENCE, Set.of(),
            SetPairViewKey.DIFFERENCE, Set.of()),
        true,
        SetPair.from(empty, empty));
    validateSetPair(
        Map.of(
            SetPairViewKey.UNION, set123,
            SetPairViewKey.LEFT, set123,
            SetPairViewKey.RIGHT, Set.of(),
            SetPairViewKey.INTERSECTION, Set.of(),
            SetPairViewKey.LEFT_DIFFERENCE, set123,
            SetPairViewKey.RIGHT_DIFFERENCE, Set.of(),
            SetPairViewKey.DIFFERENCE, set123),
        false,
        SetPair.from(set123, empty));
    validateSetPair(
        Map.of(
            SetPairViewKey.UNION, set123,
            SetPairViewKey.LEFT, set123,
            SetPairViewKey.RIGHT, set123,
            SetPairViewKey.INTERSECTION, set123,
            SetPairViewKey.LEFT_DIFFERENCE, Set.of(),
            SetPairViewKey.RIGHT_DIFFERENCE, Set.of(),
            SetPairViewKey.DIFFERENCE, Set.of()),
        true,
        SetPair.from(set123, set123));
    validateSetPair(
        Map.of(
            SetPairViewKey.UNION, Set.of(1, 2, 3, 4),
            SetPairViewKey.LEFT, set123,
            SetPairViewKey.RIGHT, set234,
            SetPairViewKey.INTERSECTION, Set.of(2, 3),
            SetPairViewKey.LEFT_DIFFERENCE, Set.of(1),
            SetPairViewKey.RIGHT_DIFFERENCE, Set.of(4),
            SetPairViewKey.DIFFERENCE, Set.of(1, 4)),
        false,
        SetPair.from(set123, set234));
    validateSetPair(
        Map.of(
            SetPairViewKey.UNION, Set.of(1, 2, 3, 5, 6, 7),
            SetPairViewKey.LEFT, set123,
            SetPairViewKey.RIGHT, set567,
            SetPairViewKey.INTERSECTION, Set.of(),
            SetPairViewKey.LEFT_DIFFERENCE, set123,
            SetPairViewKey.RIGHT_DIFFERENCE, set567,
            SetPairViewKey.DIFFERENCE, Set.of(1, 2, 3, 5, 6, 7)),
        false,
        SetPair.from(set123, set567));
    validateSetPair(
        Map.of(
            SetPairViewKey.UNION, set012345678,
            SetPairViewKey.LEFT, set123,
            SetPairViewKey.RIGHT, set012345678,
            SetPairViewKey.INTERSECTION, set123,
            SetPairViewKey.LEFT_DIFFERENCE, Set.of(),
            SetPairViewKey.RIGHT_DIFFERENCE, Set.of(0, 4, 5, 6, 7, 8),
            SetPairViewKey.DIFFERENCE, Set.of(0, 4, 5, 6, 7, 8)),
        false,
        SetPair.from(set123, set012345678));
    validateSetPair(
        Map.of(
            SetPairViewKey.UNION, set123,
            SetPairViewKey.LEFT, Set.of(),
            SetPairViewKey.RIGHT, set123,
            SetPairViewKey.INTERSECTION, Set.of(),
            SetPairViewKey.LEFT_DIFFERENCE, Set.of(),
            SetPairViewKey.RIGHT_DIFFERENCE, set123,
            SetPairViewKey.DIFFERENCE, set123),
        false,
        SetPair.from(empty, set123));
    validateSetPair(
        Map.of(
            SetPairViewKey.UNION, Set.of(1, 2, 3, 4),
            SetPairViewKey.LEFT, set234,
            SetPairViewKey.RIGHT, set123,
            SetPairViewKey.INTERSECTION, Set.of(2, 3),
            SetPairViewKey.LEFT_DIFFERENCE, Set.of(4),
            SetPairViewKey.RIGHT_DIFFERENCE, Set.of(1),
            SetPairViewKey.DIFFERENCE, Set.of(1, 4)),
        false,
        SetPair.from(set234, set123));
    validateSetPair(
        Map.of(
            SetPairViewKey.UNION, Set.of(1, 2, 3, 5, 6, 7),
            SetPairViewKey.LEFT, set567,
            SetPairViewKey.RIGHT, set123,
            SetPairViewKey.INTERSECTION, Set.of(),
            SetPairViewKey.LEFT_DIFFERENCE, set567,
            SetPairViewKey.RIGHT_DIFFERENCE, set123,
            SetPairViewKey.DIFFERENCE, Set.of(1, 2, 3, 5, 6, 7)),
        false,
        SetPair.from(set567, set123));
    validateSetPair(
        Map.of(
            SetPairViewKey.UNION, set012345678,
            SetPairViewKey.LEFT, set012345678,
            SetPairViewKey.RIGHT, set123,
            SetPairViewKey.INTERSECTION, set123,
            SetPairViewKey.LEFT_DIFFERENCE, Set.of(0, 4, 5, 6, 7, 8),
            SetPairViewKey.RIGHT_DIFFERENCE, Set.of(),
            SetPairViewKey.DIFFERENCE, Set.of(0, 4, 5, 6, 7, 8)),
        false,
        SetPair.from(set012345678, set123));
  }

  @Test
  public void testSetPairToString() {
    var emptyPair = SetPair.from(Set.of(), Set.of());
    var expectedEmptyString = "SetPair[" +
        "isEqual=true, " +
        "union=[], " +
        "left=[], " +
        "right=[], " +
        "intersection=[], " +
        "leftDifference=[], " +
        "rightDifference=[], " +
        "difference=[]]";
    assertEquals(
        expectedEmptyString,
        emptyPair.toString(),
        "toString should correctly format an empty SetPair");
    var identicalPair = SetPair.from(Set.of(1), Set.of(1));
    var expectedIdenticalString = "SetPair[" +
        "isEqual=true, " +
        "union=[1], " +
        "left=[1], " +
        "right=[1], " +
        "intersection=[1], " +
        "leftDifference=[], " +
        "rightDifference=[], " +
        "difference=[]]";
    assertEquals(
        expectedIdenticalString,
        identicalPair.toString(),
        "toString should correctly format a perfectly matching SetPair");
    var asymmetricPair = SetPair.from(Set.of(1), Set.of());
    var expectedAsymmetricString = "SetPair[" +
        "isEqual=false, " +
        "union=[1], " +
        "left=[1], " +
        "right=[], " +
        "intersection=[], " +
        "leftDifference=[1], " +
        "rightDifference=[], " +
        "difference=[1]]";
    assertEquals(
        expectedAsymmetricString,
        asymmetricPair.toString(),
        "toString should correctly format an asymmetric SetPair");
  }

  @Test
  public void testSetPairImmutability() {
    var setPair = SetPair.from(Set.of(1, 2), Set.of(2, 3));
    var componentSets = List.of(
        setPair.union(),
        setPair.left(),
        setPair.right(),
        setPair.intersection(),
        setPair.leftDifference(),
        setPair.rightDifference(),
        setPair.difference());
    for (var set : componentSets) {
      assertThrows(UnsupportedOperationException.class, () -> set.add(99));
      assertThrows(UnsupportedOperationException.class, () -> set.remove(1));
      assertThrows(UnsupportedOperationException.class, set::clear);
    }
  }

  @Test
  public void testSetPairRecordPatternMatching() {
    Object obj = SetPair.from(Set.of(1, 2), Set.of(2, 3));
    if (obj instanceof SetPair<?> pair) {
      assertFalse(pair.isEqual());
      assertEquals(Set.of(1, 2, 3), pair.union());
      assertEquals(Set.of(1, 2), pair.left());
      assertEquals(Set.of(2, 3), pair.right());
      assertEquals(Set.of(2), pair.intersection());
      assertEquals(Set.of(1), pair.leftDifference());
      assertEquals(Set.of(3), pair.rightDifference());
      assertEquals(Set.of(1, 3), pair.difference());
    } else {
      fail("Pattern matching failed to match SetPair record instance");
    }
  }

  @Test
  public void testSetPairRecordEqualsAndHashCode() {
    var pair1 = SetPair.from(Set.of(1, 2), Set.of(2, 3));
    var pair2 = SetPair.from(Set.of(1, 2), Set.of(2, 3));
    var pair3 = SetPair.from(Set.of(1, 2), Set.of(3, 4));

    assertEquals(pair1, pair2);
    assertEquals(pair1.hashCode(), pair2.hashCode());
    assertNotEquals(pair1, pair3);
    assertNotEquals(pair1, null);
    assertNotEquals(pair1, "not a set pair");

    var map1 = pair1.toMap();
    assertEquals(7, map1.size());
    assertEquals(Set.of(1, 2, 3), map1.get(SetPairViewKey.UNION));
    assertEquals(Set.of(1, 2), map1.get(SetPairViewKey.LEFT));
    assertEquals(Set.of(2, 3), map1.get(SetPairViewKey.RIGHT));
    assertEquals(Set.of(2), map1.get(SetPairViewKey.INTERSECTION));
    assertEquals(Set.of(1), map1.get(SetPairViewKey.LEFT_DIFFERENCE));
    assertEquals(Set.of(3), map1.get(SetPairViewKey.RIGHT_DIFFERENCE));
    assertEquals(Set.of(1, 3), map1.get(SetPairViewKey.DIFFERENCE));
  }

  @Test
  public void testOfOrdered() {
    var set = SetsOps.ofOrdered();
    assertNotNull(set);
    assertTrue(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
    //noinspection DataFlowIssue
    assertEquals(Set.of(), SetsOps.ofOrdered(null, null, null));
    var set2 = new LinkedHashSet<Integer>();
    assertEquals(set2, set);
    var set3 = SetsOps.ofOrdered(1, 3, 2, 4);
    assertNotNull(set3);
    assertFalse(set3.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set3));
    assertEquals(List.of(1, 3, 2, 4), set3.stream().toList());
    var illegalArgumentException = assertThrows(
        IllegalArgumentException.class,
        () ->
            SetsOps.ofOrdered(1, 2, 2, 3, 5, 7, 3, 9));
    assertEquals("duplicate elements encountered - 2, 3", illegalArgumentException.getMessage());
  }

  @Test
  public void testOfOrderedX1() {
    var set = SetsOps.ofOrdered(
        1);
    assertNotNull(set);
    assertFalse(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
    var set2 = new LinkedHashSet<Integer>();
    set2.add(1);
    assertEquals(set2, set);
  }

  @Test
  public void testOfOrderedX2() {
    var set = SetsOps.ofOrdered(
        1,
        2);
    assertNotNull(set);
    assertFalse(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
    var set2 = new LinkedHashSet<Integer>();
    set2.add(1);
    set2.add(2);
    assertEquals(set2, set);
  }

  @Test
  public void testOfOrderedX3() {
    var set = SetsOps.ofOrdered(
        1,
        2,
        3);
    assertNotNull(set);
    assertFalse(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
    var set2 = new LinkedHashSet<Integer>();
    set2.add(1);
    set2.add(2);
    set2.add(3);
    assertEquals(set2, set);
  }

  @Test
  public void testOfOrderedX4() {
    var set = SetsOps.ofOrdered(
        1,
        2,
        3,
        4);
    assertNotNull(set);
    assertFalse(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
    var set2 = new LinkedHashSet<Integer>();
    set2.add(1);
    set2.add(2);
    set2.add(3);
    set2.add(4);
    assertEquals(set2, set);
  }

  @Test
  public void testOfOrderedX5() {
    var set = SetsOps.ofOrdered(
        1,
        2,
        3,
        4,
        5);
    assertNotNull(set);
    assertFalse(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
    var set2 = new LinkedHashSet<Integer>();
    set2.add(1);
    set2.add(2);
    set2.add(3);
    set2.add(4);
    set2.add(5);
    assertEquals(set2, set);
  }

  @Test
  public void testOfOrderedX6() {
    var set = SetsOps.ofOrdered(
        1,
        2,
        3,
        4,
        5,
        6);
    assertNotNull(set);
    assertFalse(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
    var set2 = new LinkedHashSet<Integer>();
    set2.add(1);
    set2.add(2);
    set2.add(3);
    set2.add(4);
    set2.add(5);
    set2.add(6);
    assertEquals(set2, set);
  }

  @Test
  public void testOfOrderedX7() {
    var set = SetsOps.ofOrdered(
        1,
        2,
        3,
        4,
        5,
        6,
        7);
    assertNotNull(set);
    assertFalse(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
    var set2 = new LinkedHashSet<Integer>();
    set2.add(1);
    set2.add(2);
    set2.add(3);
    set2.add(4);
    set2.add(5);
    set2.add(6);
    set2.add(7);
    assertEquals(set2, set);
  }

  @Test
  public void testOfOrderedX8() {
    var set = SetsOps.ofOrdered(
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8);
    assertNotNull(set);
    assertFalse(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
    var set2 = new LinkedHashSet<Integer>();
    set2.add(1);
    set2.add(2);
    set2.add(3);
    set2.add(4);
    set2.add(5);
    set2.add(6);
    set2.add(7);
    set2.add(8);
    assertEquals(set2, set);
  }

  @Test
  public void testOfOrderedX9() {
    var set = SetsOps.ofOrdered(
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9);
    assertNotNull(set);
    assertFalse(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
    var set2 = new LinkedHashSet<Integer>();
    set2.add(1);
    set2.add(2);
    set2.add(3);
    set2.add(4);
    set2.add(5);
    set2.add(6);
    set2.add(7);
    set2.add(8);
    set2.add(9);
    assertEquals(set2, set);
  }

  @Test
  public void testOfOrderedX10() {
    var set = SetsOps.ofOrdered(
        1,
        2,
        3,
        4,
        5,
        6,
        7,
        8,
        9,
        10);
    assertNotNull(set);
    assertFalse(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
    var set2 = new LinkedHashSet<Integer>();
    set2.add(1);
    set2.add(2);
    set2.add(3);
    set2.add(4);
    set2.add(5);
    set2.add(6);
    set2.add(7);
    set2.add(8);
    set2.add(9);
    set2.add(10);
    assertEquals(set2, set);
  }

  @Test
  public void testToDistinctAndDupesParallelStreamHandling() {
    var parallelStream = Stream.of(1, 2, 2, 3, 4, 3).parallel();
    var parallelStreamOrdered = Stream.of(1, 2, 2, 3, 4, 3).parallel();
    var resultUnordered = SetsOps.toDistinctAndDupes(parallelStream);
    var resultOrdered = SetsOps.toDistinctAndDupesOrdered(parallelStreamOrdered);
    assertEquals(Set.of(1, 2, 3, 4), resultUnordered._1());
    assertEquals(Set.of(2, 3), resultUnordered._2());
    assertEquals(List.of(1, 2, 3, 4), resultOrdered._1().stream().toList());
    assertEquals(List.of(2, 3), resultOrdered._2().stream().toList());
  }

  @Test
  public void testOfOrderedNullSanitization() {
    @SuppressWarnings("DataFlowIssue")
    var set = SetsOps.ofOrdered(1, null, 2, null, 3);
    assertNotNull(set);
    assertEquals(3, set.size(), "The resulting set should not include the null elements");
    assertEquals(
        List.of(1, 2, 3),
        set.stream().toList(),
        "Nulls should be filtered out while preserving the encounter order of valid elements");
    assertTrue(CollectionsOps.isUnmodifiable(set));
  }
}
