package org.deus_ex_java.util;

import org.deus_ex_java.util.tuple.Tuple2;
import org.deus_ex_java.util.tuple.Tuple3;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ListsOpsTests {
  @Test
  public void testNullToEmpty() {
    var listEmptyNull = ListsOps.nullToEmpty(null);
    assertNotNull(listEmptyNull);
    assertTrue(listEmptyNull.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(listEmptyNull));
    var listEmptyListOf = ListsOps.nullToEmpty(List.of());
    assertNotNull(listEmptyListOf);
    assertTrue(listEmptyListOf.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(listEmptyListOf));
    var listEmptyListOf1 = ListsOps.nullToEmpty(List.of(1));
    assertNotNull(listEmptyListOf1);
    assertFalse(listEmptyListOf1.isEmpty());
    assertTrue(CollectionsOps.isUnmodifiable(listEmptyListOf1));
    assertEquals(List.of(1), listEmptyListOf1);
  }

  @Test
  public void testAppendItem() {
    var listA = ListsOps.appendItem(List.of(1), 2);
    assertTrue(CollectionsOps.isUnmodifiable(listA));
    assertEquals(List.of(1, 2), listA);
    var listB = ListsOps.appendItem(listA, 3);
    assertTrue(CollectionsOps.isUnmodifiable(listB));
    assertEquals(List.of(1, 2, 3), listB);
    var listC = ListsOps.appendItem(List.of(), 10);
    assertEquals(List.of(10), listC);
  }

  @Test
  public void testAppendLists() {
    var listAppendA = ListsOps.appendLists(List.of(1), List.of(2, 3), List.of(4));
    assertTrue(CollectionsOps.isUnmodifiable(listAppendA));
    assertEquals(List.of(1, 2, 3, 4), listAppendA);
    var listContainingNull = new ArrayList<Integer>();
    listContainingNull.add(null);
    listContainingNull.add(7);
    var listAppendB = ListsOps.appendLists(listAppendA, List.of(4, 5, 6), listContainingNull);
    assertTrue(CollectionsOps.isUnmodifiable(listAppendB));
    assertEquals(List.of(1, 2, 3, 4, 4, 5, 6, 7), listAppendB);
    @SuppressWarnings("DataFlowIssue")
    var listAppendC = ListsOps.appendLists(List.of(), null, List.of());
    assertEquals(List.of(), listAppendC);
    var a = new List[]{};
    @SuppressWarnings("unchecked")
    var listAppendD = ListsOps.appendLists(a);
    assertEquals(List.of(), listAppendD);
  }

  @Test
  public void testToListUnmodifiable() {
    var expectedList = List.of(1, 2, 3);
    var nullContainingList = Arrays.asList(null, 1, null, 2, null, 3, null);
    assertEquals(7, nullContainingList.size());
    var actualList = ListsOps.toListUnmodifiable(nullContainingList.stream());
    assertEquals(expectedList, actualList);
    assertTrue(CollectionsOps.isUnmodifiable(actualList));
  }

  @Test
  public void testToDistinctSortedListInteger() {
    assertEquals(
        List.of(1, 2, 3, 4),
        ListsOps.toDistinctSortedListInteger(
            Stream.of(4, 1, 2, 3)));
    assertEquals(
        List.of(1, 2, 3, 4),
        ListsOps.toDistinctSortedListInteger(
            Stream.of("4", "1", "2", "3"),
            Integer::valueOf));
  }

  @Test
  public void testFlatten() {
    assertEquals(
        ListsOps.flatten(
            Stream.of(
                Optional.empty(),
                Optional.of(1),
                Optional.of(2),
                Optional.empty(),
                Optional.of(3),
                Optional.empty())),
        List.of(1, 2, 3));
  }

  @Test
  public void testUnzipEithers() {
    var eithers = Stream.<Either<String, Integer>>of(
        Either.right(1),
        Either.left("b"),
        Either.left("c"),
        Either.right(4));
    var tuple2 = ListsOps.unzipEithers(eithers);
    assertEquals(List.of(Optional.empty(), Optional.of("b"), Optional.of("c"), Optional.empty()), tuple2._1());
    assertEquals(List.of(Optional.of(1), Optional.empty(), Optional.empty(), Optional.of(4)), tuple2._2());
  }

  @Test
  public void testUnzipAndFlattenEithers() {
    var eithers = Stream.<Either<String, Integer>>of(
        Either.right(1),
        Either.left("b"),
        Either.left("c"),
        Either.right(4));
    var tuple2 = ListsOps.unzipAndFlattenEithers(eithers);
    assertEquals(List.of("b", "c"), tuple2._1());
    assertEquals(List.of(1, 4), tuple2._2());
  }

  @Test
  public void testUnzip() {
    var tuple2s = Stream.of(
        new Tuple2<>("a", 1),
        new Tuple2<>("b", 2),
        new Tuple2<>("c", 3),
        new Tuple2<>("d", 4));
    var tuple2 = ListsOps.unzip(tuple2s);
    assertEquals(List.of("a", "b", "c", "d"), tuple2._1());
    assertEquals(List.of(1, 2, 3, 4), tuple2._2());
  }

  @Test
  public void testUnzipAndFlatten() {
    var tuple2s = Stream.of(
        new Tuple2<>("a", 1),
        new Tuple2<>("b", 2),
        new Tuple2<>("c", 3),
        new Tuple2<>("d", 4));
    var tuple2 = ListsOps.unzipAndFlatten(
        tuple2s,
        stringAndInteger ->
            stringAndInteger._1().equals("c")
                ? Optional.empty()
                : stringAndInteger._2() == 4
                    ? Optional.of(new Tuple2<>(Optional.of(stringAndInteger._1()), Optional.of(stringAndInteger._2())))
                    : stringAndInteger._1().equals("a")
                        ? Optional.of(new Tuple2<>(Optional.of(stringAndInteger._1()), Optional.empty()))
                        : Optional.of(new Tuple2<>(Optional.empty(), Optional.of(stringAndInteger._2()))));
    assertEquals(List.of("a", "d"), tuple2._1());
    assertEquals(List.of(2, 4), tuple2._2());
  }

  @Test
  public void testUnzip3() {
    var tuple3s = Stream.of(
        new Tuple3<>("a", 1, 1.0d),
        new Tuple3<>("b", 2, 2.0d),
        new Tuple3<>("c", 3, 3.0d),
        new Tuple3<>("d", 4, 4.0d));
    var tuple3 = ListsOps.unzip3(tuple3s);
    assertEquals(List.of("a", "b", "c", "d"), tuple3._1());
    assertEquals(List.of(1, 2, 3, 4), tuple3._2());
    assertEquals(List.of(1.0d, 2.0d, 3.0d, 4.0d), tuple3._3());
  }

  @Test
  public void testUnzip3AndFlatten() {
    var tuple3s = Stream.of(
        new Tuple3<>("a", 1, 1.0d),
        new Tuple3<>("b", 2, 2.0d),
        new Tuple3<>("c", 3, 3.0d),
        new Tuple3<>("d", 4, 4.0d));
    var tuple3 = ListsOps.unzip3AndFlatten(
        tuple3s,
        stringAndIntegerAndDouble ->
            stringAndIntegerAndDouble._1().equals("c")
                ? Optional.empty()
                : stringAndIntegerAndDouble._2() == 4
                    ? Optional.of(new Tuple3<>(Optional.of(stringAndIntegerAndDouble._1()), Optional.of(stringAndIntegerAndDouble._2()), Optional.of(stringAndIntegerAndDouble._3())))
                    : stringAndIntegerAndDouble._1().equals("a")
                        ? Optional.of(new Tuple3<>(Optional.of(stringAndIntegerAndDouble._1()), Optional.empty(), Optional.of(stringAndIntegerAndDouble._3())))
                        : Optional.of(new Tuple3<>(Optional.empty(), Optional.of(stringAndIntegerAndDouble._2()), Optional.empty())));
    assertEquals(List.of("a", "d"), tuple3._1());
    assertEquals(List.of(2, 4), tuple3._2());
    assertEquals(List.of(1.0, 4.0), tuple3._3());
  }

  //TODO: x14 missing tests
  //  - testUnzip4
  //  - testUnzip4AndFlatten
  //  - testUnzip5
  //  - testUnzip5AndFlatten
  //  - testUnzip6
  //  - testUnzip6AndFlatten
  //  - testUnzip7
  //  - testUnzip7AndFlatten
  //  - testUnzip8
  //  - testUnzip8AndFlatten
  //  - testUnzip9
  //  - testUnzip9AndFlatten
  //  - testUnzip10
  //  - testUnzip10AndFlatten
}
