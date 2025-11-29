package org.deus_ex_java.util;

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
  public void testAddItem() {
    var setEmptyAdd1 = SetsOps.addItem(Set.of(), 1);
    assertEquals(Set.of(1), setEmptyAdd1);
    assertTrue(CollectionsOps.isUnmodifiable(setEmptyAdd1));
    var setAdd2 = SetsOps.addItem(setEmptyAdd1, 2);
    assertEquals(Set.of(1, 2), setAdd2);
    assertTrue(CollectionsOps.isUnmodifiable(setAdd2));
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

  private <T> void validateContrastSetPairMap(
      Map<SetPairViewKey, Set<T>> expectedTsBySetPairViewKey,
      Map<SetPairViewKey, Set<T>> actualTsBySetPairViewKey
  ) {
    assertEquals(expectedTsBySetPairViewKey, actualTsBySetPairViewKey);
    assertTrue(CollectionsOps.isUnmodifiable(actualTsBySetPairViewKey));
    actualTsBySetPairViewKey.values()
        .forEach(ts ->
            assertTrue(CollectionsOps.isUnmodifiable(ts)));
  }

  @Test
  public void testContrastSetPair() {
    var setA = Set.of(1, 2, 3);
    var setB = Set.of(2, 3, 4);
    validateContrastSetPairMap(
        Map.of(
            SetPairViewKey.UNION, Set.of(),
            SetPairViewKey.LEFT, Set.of(),
            SetPairViewKey.RIGHT, Set.of(),
            SetPairViewKey.INTERSECTION, Set.of(),
            SetPairViewKey.LEFT_DIFFERENCE, Set.of(),
            SetPairViewKey.RIGHT_DIFFERENCE, Set.of(),
            SetPairViewKey.DIFFERENCE, Set.of()),
        SetsOps.contrastSetPair(Set.of(), Set.of()));
    validateContrastSetPairMap(
        Map.of(
            SetPairViewKey.UNION, setA,
            SetPairViewKey.LEFT, setA,
            SetPairViewKey.RIGHT, Set.of(),
            SetPairViewKey.INTERSECTION, Set.of(),
            SetPairViewKey.LEFT_DIFFERENCE, setA,
            SetPairViewKey.RIGHT_DIFFERENCE, Set.of(),
            SetPairViewKey.DIFFERENCE, setA),
        SetsOps.contrastSetPair(setA, Set.of()));
    validateContrastSetPairMap(
        Map.of(
            SetPairViewKey.UNION, setA,
            SetPairViewKey.LEFT, Set.of(),
            SetPairViewKey.RIGHT, setA,
            SetPairViewKey.INTERSECTION, Set.of(),
            SetPairViewKey.LEFT_DIFFERENCE, Set.of(),
            SetPairViewKey.RIGHT_DIFFERENCE, setA,
            SetPairViewKey.DIFFERENCE, setA),
        SetsOps.contrastSetPair(Set.of(), setA));
    validateContrastSetPairMap(
        Map.of(
            SetPairViewKey.UNION, Set.of(1, 2, 3, 4),
            SetPairViewKey.LEFT, setA,
            SetPairViewKey.RIGHT, setB,
            SetPairViewKey.INTERSECTION, Set.of(2, 3),
            SetPairViewKey.LEFT_DIFFERENCE, Set.of(1),
            SetPairViewKey.RIGHT_DIFFERENCE, Set.of(4),
            SetPairViewKey.DIFFERENCE, Set.of(1, 4)),
        SetsOps.contrastSetPair(setA, setB));
    validateContrastSetPairMap(
        Map.of(
            SetPairViewKey.UNION, Set.of(1, 2, 3, 4),
            SetPairViewKey.LEFT, setB,
            SetPairViewKey.RIGHT, setA,
            SetPairViewKey.INTERSECTION, Set.of(2, 3),
            SetPairViewKey.LEFT_DIFFERENCE, Set.of(4),
            SetPairViewKey.RIGHT_DIFFERENCE, Set.of(1),
            SetPairViewKey.DIFFERENCE, Set.of(1, 4)),
        SetsOps.contrastSetPair(setB, setA));
  }

  @Test
  public void testOfOrdered() {
    var set = SetsOps.ofOrdered();
    assertNotNull(set);
    assertTrue(set.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(set));
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
}
