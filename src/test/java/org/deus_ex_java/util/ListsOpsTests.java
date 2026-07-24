package org.deus_ex_java.util;

import org.deus_ex_java.util.tuple.*;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ListsOpsTests {
  @Test
  public void testNewArrayList() {
    var list = ListsOps.<String>newArrayList();
    assertNotNull(list);
    assertTrue(list.isEmpty());
    assertInstanceOf(ArrayList.class, list);
    assertInstanceOf(java.util.RandomAccess.class, list);
    var checkedList = ListsOps.newArrayList(Integer.class);
    assertNotNull(checkedList);
    assertTrue(checkedList.isEmpty());
    assertThrows(
        ClassCastException.class,
        () -> {
          @SuppressWarnings("rawtypes")
          var rawList = (List) checkedList;
          //noinspection unchecked
          rawList.add("This should fail");
        });
  }

  @Test
  public void testNewLinkedList() {
    var list = ListsOps.<String>newLinkedList();
    assertNotNull(list);
    assertTrue(list.isEmpty());
    assertInstanceOf(LinkedList.class, list);
    assertFalse(list instanceof java.util.RandomAccess);
    var checkedList = ListsOps.newLinkedList(Double.class);
    assertNotNull(checkedList);
    assertTrue(checkedList.isEmpty());
    assertThrows(
        ClassCastException.class,
        () -> {
          @SuppressWarnings("rawtypes")
          var rawList = (List) checkedList;
          //noinspection unchecked
          rawList.add("This should fail");
        });
  }

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
  void toNonEmptyWithNullReturnsEmpty() {
    var result = ListsOps.<String>toNonEmpty(null);
    assertTrue(result.isEmpty(), "Expected an empty Optional for a null input");
  }

  @Test
  void toNonEmptyWithEmptyListReturnsEmpty() {
    var result = ListsOps.<String>toNonEmpty(List.of());
    assertTrue(result.isEmpty(), "Expected an empty Optional for an empty list input");
  }

  @Test
  void toNonEmptyWithElementsReturnsOccupiedOptional() {
    var populatedList = List.of("apple", "banana");
    var result = ListsOps.toNonEmpty(populatedList);
    assertTrue(result.isPresent(), "Expected an occupied Optional for a non-empty list");
    assertEquals(populatedList, result.get().list());
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
  public void testAppendItemNullValueBehavior() {
    var listA = ListsOps.appendItem(List.of(), null);
    assertNotNull(listA);
    assertTrue(listA.isEmpty(), "Appending null to an empty list should safely return an empty list");
    assertTrue(CollectionsOps.isUnmodifiable(listA));
    var listB = ListsOps.appendItem(List.of(1, 2), null);
    assertEquals(2, listB.size(), "Appending null to a populated list should be ignored");
    assertEquals(List.of(1, 2), listB);
    assertTrue(CollectionsOps.isUnmodifiable(listB));
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
  public void testRemoveItem() {
    var initialList = List.of(1, 2, 3, 2, 4);
    var listRemove2 = ListsOps.removeItem(initialList, 2);
    assertEquals(List.of(1, 3, 2, 4), listRemove2, "Should only remove the first encountered instance of 2");
    assertTrue(CollectionsOps.isUnmodifiable(listRemove2));

    var listRemove5 = ListsOps.removeItem(initialList, 5);
    assertEquals(initialList, listRemove5);
    assertTrue(CollectionsOps.isUnmodifiable(listRemove5));

    var emptyListRemove = ListsOps.removeItem(List.of(), 1);
    assertEquals(List.of(), emptyListRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyListRemove));

    var listRemoveNull = ListsOps.removeItem(initialList, null);
    assertEquals(initialList, listRemoveNull, "Removing null should be safely ignored");
    assertTrue(CollectionsOps.isUnmodifiable(listRemoveNull));
  }

  @Test
  public void testEliminateItem() {
    var initialList = List.of(1, 2, 3, 2, 4);
    var listEliminate2 = ListsOps.eliminateItem(initialList, 2);
    assertEquals(List.of(1, 3, 4), listEliminate2, "Should remove all instances of 2");
    assertTrue(CollectionsOps.isUnmodifiable(listEliminate2));

    var listEliminate5 = ListsOps.eliminateItem(initialList, 5);
    assertEquals(initialList, listEliminate5);
    assertTrue(CollectionsOps.isUnmodifiable(listEliminate5));

    var emptyListEliminate = ListsOps.eliminateItem(List.of(), 1);
    assertEquals(List.of(), emptyListEliminate);
    assertTrue(CollectionsOps.isUnmodifiable(emptyListEliminate));

    var listEliminateNull = ListsOps.eliminateItem(initialList, null);
    assertEquals(initialList, listEliminateNull, "Eliminating null should be safely ignored");
    assertTrue(CollectionsOps.isUnmodifiable(listEliminateNull));
  }

  @Test
  public void testRemoveAllWithCollection() {
    var initialList = List.of(1, 2, 3, 2, 4, 5);
    var listRemove = ListsOps.removeAll(initialList, List.of(2, 4, 5));
    assertEquals(List.of(1, 3, 2), listRemove, "Should only remove the first encountered instance of each element in the collection");
    assertTrue(CollectionsOps.isUnmodifiable(listRemove));

    var listRemoveEmpty = ListsOps.removeAll(initialList, List.of());
    assertEquals(initialList, listRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(listRemoveEmpty));

    var emptyListRemove = ListsOps.removeAll(List.of(), List.of(1, 2));
    assertEquals(List.of(), emptyListRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyListRemove));
  }

  @Test
  public void testRemoveAllWithStream() {
    var initialList = List.of(1, 2, 3, 2, 4, 5);
    var listRemove = ListsOps.removeAll(initialList, Stream.of(2, 4, 5));
    assertEquals(List.of(1, 3, 2), listRemove);
    assertTrue(CollectionsOps.isUnmodifiable(listRemove));

    var listRemoveDuplicateInStream = ListsOps.removeAll(initialList, Stream.of(2, 2));
    assertEquals(List.of(1, 3, 4, 5), listRemoveDuplicateInStream, "Stream containing duplicates should trigger multiple removals");
    assertTrue(CollectionsOps.isUnmodifiable(listRemoveDuplicateInStream));

    var listRemoveEmpty = ListsOps.removeAll(initialList, Stream.empty());
    assertEquals(initialList, listRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(listRemoveEmpty));

    var emptyListRemove = ListsOps.removeAll(List.of(), Stream.of(1, 2));
    assertEquals(List.of(), emptyListRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyListRemove));
  }

  @Test
  public void testEliminateAllWithCollection() {
    var initialList = List.of(1, 2, 3, 2, 4, 5);
    var listEliminate = ListsOps.eliminateAll(initialList, List.of(2, 4, 5));
    assertEquals(List.of(1, 3), listEliminate, "Should eliminate all matching instances for elements in the collection");
    assertTrue(CollectionsOps.isUnmodifiable(listEliminate));

    var listEliminateEmpty = ListsOps.eliminateAll(initialList, List.of());
    assertEquals(initialList, listEliminateEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(listEliminateEmpty));

    var emptyListEliminate = ListsOps.eliminateAll(List.of(), List.of(1, 2));
    assertEquals(List.of(), emptyListEliminate);
    assertTrue(CollectionsOps.isUnmodifiable(emptyListEliminate));
  }

  @Test
  public void testEliminateAllWithStream() {
    var initialList = List.of(1, 2, 3, 2, 4, 5);
    var listEliminate = ListsOps.eliminateAll(initialList, Stream.of(2, 4, 5));
    assertEquals(List.of(1, 3), listEliminate);
    assertTrue(CollectionsOps.isUnmodifiable(listEliminate));

    var listEliminateEmpty = ListsOps.eliminateAll(initialList, Stream.empty());
    assertEquals(initialList, listEliminateEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(listEliminateEmpty));

    var emptyListEliminate = ListsOps.eliminateAll(List.of(), Stream.of(1, 2));
    assertEquals(List.of(), emptyListEliminate);
    assertTrue(CollectionsOps.isUnmodifiable(emptyListEliminate));
  }

  @Test
  public void testRemoveLists() {
    var initialList = List.of(1, 2, 3, 2, 4, 5, 6);

    var listRemove = ListsOps.removeLists(initialList, List.of(2, 4), List.of(5));
    assertEquals(List.of(1, 3, 2, 6), listRemove, "Should only remove the first encountered instance of each element in the lists");
    assertTrue(CollectionsOps.isUnmodifiable(listRemove));

    var listRemoveEmpty = ListsOps.removeLists(initialList);
    assertEquals(initialList, listRemoveEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(listRemoveEmpty));

    var emptyListRemove = ListsOps.removeLists(List.of(), List.of(1, 2));
    assertEquals(List.of(), emptyListRemove);
    assertTrue(CollectionsOps.isUnmodifiable(emptyListRemove));

    var listContainingNull = new ArrayList<Integer>();
    listContainingNull.add(null);
    listContainingNull.add(6);

    @SuppressWarnings("DataFlowIssue")
    var listRemoveNulls = ListsOps.removeLists(initialList, null, List.of(1), listContainingNull);
    assertEquals(List.of(2, 3, 2, 4, 5), listRemoveNulls);
    assertTrue(CollectionsOps.isUnmodifiable(listRemoveNulls));
  }

  @Test
  public void testEliminateLists() {
    var initialList = List.of(1, 2, 3, 2, 4, 5, 6);

    var listEliminate = ListsOps.eliminateLists(initialList, List.of(2, 4), List.of(5));
    assertEquals(List.of(1, 3, 6), listEliminate);
    assertTrue(CollectionsOps.isUnmodifiable(listEliminate));

    var listEliminateEmpty = ListsOps.eliminateLists(initialList);
    assertEquals(initialList, listEliminateEmpty);
    assertTrue(CollectionsOps.isUnmodifiable(listEliminateEmpty));

    var emptyListEliminate = ListsOps.eliminateLists(List.of(), List.of(1, 2));
    assertEquals(List.of(), emptyListEliminate);
    assertTrue(CollectionsOps.isUnmodifiable(emptyListEliminate));

    var listContainingNull = new ArrayList<Integer>();
    listContainingNull.add(null);
    listContainingNull.add(6);

    @SuppressWarnings("DataFlowIssue")
    var listEliminateNulls = ListsOps.eliminateLists(initialList, null, List.of(1), listContainingNull);
    assertEquals(List.of(2, 3, 2, 4, 5), listEliminateNulls);
    assertTrue(CollectionsOps.isUnmodifiable(listEliminateNulls));
  }

  @Test
  public void testNullSanitizeStream() {
    var expectedList = List.of(1, 2, 3);
    var nullContainingList = Arrays.asList(null, 1, null, 2, null, 3, null);
    assertEquals(7, nullContainingList.size());
    var actualList = ListsOps.nullSanitize(nullContainingList.stream());
    assertEquals(expectedList, actualList);
    assertTrue(CollectionsOps.isUnmodifiable(actualList));
  }

  @Test
  public void testNullSanitize() {
    var expectedList = List.of(1, 2, 3);
    var nullContainingList = Arrays.asList(null, 1, null, 2, null, 3, null);
    assertEquals(7, nullContainingList.size());
    @SuppressWarnings("NullableProblems")
    var actualList = ListsOps.nullSanitize(nullContainingList);
    assertEquals(expectedList, actualList);
    assertTrue(CollectionsOps.isUnmodifiable(actualList));
  }

  @Test
  public void testCompareAlignedRight() {
    assertEquals(0, ListsOps.<Integer>compareAlignedRight(List.of(), List.of()));
    assertEquals(1, ListsOps.compareAlignedRight(List.of(1), List.of()));
    assertEquals(-1, ListsOps.compareAlignedRight(List.of(), List.of(1)));
    assertEquals(0, ListsOps.compareAlignedRight(List.of(1), List.of(1)));
    assertEquals(1, ListsOps.compareAlignedRight(List.of(1, 2), List.of(1)));
    assertEquals(-1, ListsOps.compareAlignedRight(List.of(1), List.of(1, 2)));
    assertEquals(0, ListsOps.compareAlignedRight(List.of(1, 2), List.of(1, 2)));
    assertEquals(0, ListsOps.<Integer>compareAlignedRight(Stream.empty(), Stream.empty()));
    assertEquals(1, ListsOps.compareAlignedRight(Stream.of(1), Stream.of()));
    assertEquals(-1, ListsOps.compareAlignedRight(Stream.of(), Stream.of(1)));
    assertEquals(0, ListsOps.compareAlignedRight(Stream.of(1), Stream.of(1)));
    assertEquals(1, ListsOps.compareAlignedRight(Stream.of(1, 2), Stream.of(1)));
    assertEquals(-1, ListsOps.compareAlignedRight(Stream.of(1), Stream.of(1, 2)));
    assertEquals(0, ListsOps.compareAlignedRight(Stream.of(1, 2), Stream.of(1, 2)));
    assertEquals(0, ListsOps.<Integer>compareAlignedRight(List.of(), Stream.empty()));
    assertEquals(1, ListsOps.compareAlignedRight(List.of(1), Stream.of()));
    assertEquals(-1, ListsOps.compareAlignedRight(List.of(), Stream.of(1)));
    assertEquals(0, ListsOps.compareAlignedRight(List.of(1), Stream.of(1)));
    assertEquals(1, ListsOps.compareAlignedRight(List.of(1, 2), Stream.of(1)));
    assertEquals(-1, ListsOps.compareAlignedRight(List.of(1), Stream.of(1, 2)));
    assertEquals(0, ListsOps.compareAlignedRight(List.of(1, 2), Stream.of(1, 2)));
    assertEquals(0, ListsOps.<Integer>compareAlignedRight(Stream.empty(), List.of()));
    assertEquals(1, ListsOps.compareAlignedRight(Stream.of(1), List.of()));
    assertEquals(-1, ListsOps.compareAlignedRight(Stream.of(), List.of(1)));
    assertEquals(0, ListsOps.compareAlignedRight(Stream.of(1), List.of(1)));
    assertEquals(1, ListsOps.compareAlignedRight(Stream.of(1, 2), List.of(1)));
    assertEquals(-1, ListsOps.compareAlignedRight(Stream.of(1), List.of(1, 2)));
    assertEquals(0, ListsOps.compareAlignedRight(Stream.of(1, 2), List.of(1, 2)));
  }

  @Test
  public void testCompareAlignedLeft() {
    assertEquals(0, ListsOps.<Integer>compareAlignedLeft(List.of(), List.of()));
    assertEquals(1, ListsOps.compareAlignedLeft(List.of(1), List.of()));
    assertEquals(-1, ListsOps.compareAlignedLeft(List.of(), List.of(1)));
    assertEquals(0, ListsOps.compareAlignedLeft(List.of(1), List.of(1)));
    assertEquals(1, ListsOps.compareAlignedLeft(List.of(1, 2), List.of(1)));
    assertEquals(-1, ListsOps.compareAlignedLeft(List.of(1), List.of(1, 2)));
    assertEquals(0, ListsOps.compareAlignedLeft(List.of(1, 2), List.of(1, 2)));
    assertEquals(0, ListsOps.<Integer>compareAlignedLeft(Stream.empty(), Stream.empty()));
    assertEquals(1, ListsOps.compareAlignedLeft(Stream.of(1), Stream.of()));
    assertEquals(-1, ListsOps.compareAlignedLeft(Stream.of(), Stream.of(1)));
    assertEquals(0, ListsOps.compareAlignedLeft(Stream.of(1), Stream.of(1)));
    assertEquals(1, ListsOps.compareAlignedLeft(Stream.of(1, 2), Stream.of(1)));
    assertEquals(-1, ListsOps.compareAlignedLeft(Stream.of(1), Stream.of(1, 2)));
    assertEquals(0, ListsOps.compareAlignedLeft(Stream.of(1, 2), Stream.of(1, 2)));
    assertEquals(0, ListsOps.<Integer>compareAlignedLeft(List.of(), Stream.empty()));
    assertEquals(1, ListsOps.compareAlignedLeft(List.of(1), Stream.of()));
    assertEquals(-1, ListsOps.compareAlignedLeft(List.of(), Stream.of(1)));
    assertEquals(0, ListsOps.compareAlignedLeft(List.of(1), Stream.of(1)));
    assertEquals(1, ListsOps.compareAlignedLeft(List.of(1, 2), Stream.of(1)));
    assertEquals(-1, ListsOps.compareAlignedLeft(List.of(1), Stream.of(1, 2)));
    assertEquals(0, ListsOps.compareAlignedLeft(List.of(1, 2), Stream.of(1, 2)));
    assertEquals(0, ListsOps.<Integer>compareAlignedLeft(Stream.empty(), List.of()));
    assertEquals(1, ListsOps.compareAlignedLeft(Stream.of(1), List.of()));
    assertEquals(-1, ListsOps.compareAlignedLeft(Stream.of(), List.of(1)));
    assertEquals(0, ListsOps.compareAlignedLeft(Stream.of(1), List.of(1)));
    assertEquals(1, ListsOps.compareAlignedLeft(Stream.of(1, 2), List.of(1)));
    assertEquals(-1, ListsOps.compareAlignedLeft(Stream.of(1), List.of(1, 2)));
    assertEquals(0, ListsOps.compareAlignedLeft(Stream.of(1, 2), List.of(1, 2)));
  }

  @Test
  public void testToDistinctSortedListStream() {
    assertEquals(
        List.of(1, 2, 3, 4),
        ListsOps.toDistinctSortedList(
            Stream.of(4, 1, 2, 3)));
    assertEquals(
        List.of(1, 2, 3, 4),
        ListsOps.toDistinctSortedList(
            Stream.of("4", "1", "2", "3"),
            Integer::valueOf));
  }

  @Test
  public void testToDistinctSortedList() {
    assertEquals(
        List.of(1, 2, 3, 4),
        ListsOps.toDistinctSortedList(
            List.of(4, 1, 2, 3)));
    assertEquals(
        List.of(1, 2, 3, 4),
        ListsOps.toDistinctSortedList(
            List.of("4", "1", "2", "3"),
            Integer::valueOf));
  }

  @Test
  public void testToDistinctSortedListFiltersNulls() {
    var streamWithNulls = Stream.of(5, null, 1, 5, 2, null, 1);
    @SuppressWarnings("NullableProblems")
    var result = ListsOps.toDistinctSortedList(streamWithNulls);
    assertEquals(
        List.of(1, 2, 5),
        result,
        "Should sort, deduplicate, and drop nulls");
  }

  @Test
  public void testReverse() {
    assertEquals(List.of(), ListsOps.reverse(List.of()));
    assertEquals(List.of(), ListsOps.reverse(Stream.empty()));
    var expectedListOrdered = List.of(1, 2, 3);
    var nullContainingListOrdered = Stream.of(null, 3, null, 2, null, 1, null).toList();
    assertEquals(expectedListOrdered, ListsOps.reverse(nullContainingListOrdered.stream()));
    assertEquals(expectedListOrdered, ListsOps.reverse(nullContainingListOrdered));
    assertEquals(List.of(), ListsOps.reverse(List.of()));
  }

  @Test
  public void testFlattenStream() {
    assertEquals(
        List.of(1, 2, 3),
        ListsOps.flatten(
            Stream.of(
                Optional.empty(),
                Optional.of(1),
                Optional.of(2),
                Optional.empty(),
                Optional.of(3),
                Optional.empty())));
  }

  @Test
  public void testFlatten() {
    assertEquals(
        List.of(1, 2, 3),
        ListsOps.flatten(
            List.of(
                Optional.empty(),
                Optional.of(1),
                Optional.of(2),
                Optional.empty(),
                Optional.of(3),
                Optional.empty())));
  }

  @SuppressWarnings("SpellCheckingInspection")
  @Test
  public void testUnzipEithers() {
    var eithersStream = Stream.<Either<String, Integer>>of(
        Either.right(1),
        Either.left("b"),
        Either.left("c"),
        Either.right(4));
    var tuple2Stream = ListsOps.unzipEithers(eithersStream);
    assertEquals(List.of(Optional.empty(), Optional.of("b"), Optional.of("c"), Optional.empty()), tuple2Stream._1());
    assertEquals(List.of(Optional.of(1), Optional.empty(), Optional.empty(), Optional.of(4)), tuple2Stream._2());
    var eithersList = List.<Either<String, Integer>>of(
        Either.right(1),
        Either.left("b"),
        Either.left("c"),
        Either.right(4));
    var tuple2List = ListsOps.unzipEithers(eithersList);
    assertEquals(List.of(Optional.empty(), Optional.of("b"), Optional.of("c"), Optional.empty()), tuple2List._1());
    assertEquals(List.of(Optional.of(1), Optional.empty(), Optional.empty(), Optional.of(4)), tuple2List._2());
  }

  @Test
  @SuppressWarnings("SpellCheckingInspection")
  public void testUnzipAndFlattenEithers() {
    var eithersStream = Stream.<Either<String, Integer>>of(
        Either.right(1),
        Either.left("b"),
        Either.left("c"),
        Either.right(4));
    var tuple2Stream = ListsOps.unzipAndFlattenEithers(eithersStream);
    assertEquals(List.of("b", "c"), tuple2Stream._1());
    assertEquals(List.of(1, 4), tuple2Stream._2());
    var eithersList = List.<Either<String, Integer>>of(
        Either.right(1),
        Either.left("b"),
        Either.left("c"),
        Either.right(4));
    var tuple2List = ListsOps.unzipAndFlattenEithers(eithersList);
    assertEquals(List.of("b", "c"), tuple2List._1());
    assertEquals(List.of(1, 4), tuple2List._2());
  }

  @Test
  public void testUnzip() {
    assertEquals(
        new Tuple2<>(
            List.of(),
            List.of()),
        ListsOps.unzip(Stream.empty()));
    var tuple2sStream = Stream.of(
        new Tuple2<>("a", 1),
        new Tuple2<>("b", 2),
        new Tuple2<>("c", 3),
        new Tuple2<>("d", 4));
    var tuple2Stream = ListsOps.unzip(tuple2sStream);
    assertEquals(List.of("a", "b", "c", "d"), tuple2Stream._1());
    assertEquals(List.of(1, 2, 3, 4), tuple2Stream._2());
    assertEquals(
        new Tuple2<>(
            List.of(),
            List.of()),
        ListsOps.unzip(List.of()));
    var tuple2sList = List.of(
        new Tuple2<>("a", 1),
        new Tuple2<>("b", 2),
        new Tuple2<>("c", 3),
        new Tuple2<>("d", 4));
    var tuple2List = ListsOps.unzip(tuple2sList);
    assertEquals(List.of("a", "b", "c", "d"), tuple2List._1());
    assertEquals(List.of(1, 2, 3, 4), tuple2List._2());
  }

  @Test
  public void testUnzipAndFlatten() {
    assertEquals(
        new Tuple2<>(
            List.of(),
            List.of()),
        ListsOps.unzipAndFlatten(
            Stream.empty(),
            t ->
                Optional.empty()));
    var tuple2sStream = Stream.of(
        new Tuple2<>("a", 1),
        new Tuple2<>("b", 2),
        new Tuple2<>("c", 3),
        new Tuple2<>("d", 4));
    var tuple2Stream = ListsOps.unzipAndFlatten(
        tuple2sStream,
        stringAndInteger ->
            stringAndInteger._1().equals("c")
                ? Optional.empty()
                : stringAndInteger._2() == 4
                  ? Optional.of(new Tuple2<>(Optional.of(stringAndInteger._1()), Optional.of(stringAndInteger._2())))
                    : stringAndInteger._1().equals("a")
                      ? Optional.of(new Tuple2<>(Optional.of(stringAndInteger._1()), Optional.empty()))
                        : Optional.of(new Tuple2<>(Optional.empty(), Optional.of(stringAndInteger._2()))));
    assertEquals(List.of("a", "d"), tuple2Stream._1());
    assertEquals(List.of(2, 4), tuple2Stream._2());
    assertEquals(
        new Tuple2<>(
            List.of(),
            List.of()),
        ListsOps.unzipAndFlatten(
            List.of(),
            t ->
                Optional.empty()));
    var tuple2sList = List.of(
        new Tuple2<>("a", 1),
        new Tuple2<>("b", 2),
        new Tuple2<>("c", 3),
        new Tuple2<>("d", 4));
    var tuple2List = ListsOps.unzipAndFlatten(
        tuple2sList,
        stringAndInteger ->
            stringAndInteger._1().equals("c")
                ? Optional.empty()
                : stringAndInteger._2() == 4
                  ? Optional.of(new Tuple2<>(Optional.of(stringAndInteger._1()), Optional.of(stringAndInteger._2())))
                    : stringAndInteger._1().equals("a")
                      ? Optional.of(new Tuple2<>(Optional.of(stringAndInteger._1()), Optional.empty()))
                        : Optional.of(new Tuple2<>(Optional.empty(), Optional.of(stringAndInteger._2()))));
    assertEquals(List.of("a", "d"), tuple2List._1());
    assertEquals(List.of(2, 4), tuple2List._2());
  }

  @Test
  public void testUnzip3() {
    assertEquals(
        new Tuple3<>(
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip3(Stream.empty()));
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
    assertEquals(
        new Tuple3<>(
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip3AndFlatten(
            Stream.empty(),
            t ->
                Optional.empty()));
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

  @Test
  public void testUnzip4() {
    assertEquals(
        new Tuple4<>(
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip4(Stream.empty()));
    var tuple4s = Stream.of(
        new Tuple4<>("a", 1, 1.0d, false),
        new Tuple4<>("b", 2, 2.0d, true),
        new Tuple4<>("c", 3, 3.0d, false),
        new Tuple4<>("d", 4, 4.0d, true));
    var tuple4 = ListsOps.unzip4(tuple4s);
    assertEquals(List.of("a", "b", "c", "d"), tuple4._1());
    assertEquals(List.of(1, 2, 3, 4), tuple4._2());
    assertEquals(List.of(1.0d, 2.0d, 3.0d, 4.0d), tuple4._3());
    assertEquals(List.of(false, true, false, true), tuple4._4());
  }

  @Test
  public void testUnzip4AndFlatten() {
    assertEquals(
        new Tuple4<>(
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip4AndFlatten(
            Stream.empty(),
            t ->
                Optional.empty()));
    var tuple4s = Stream.of(
        new Tuple4<>("a", 1, 1.0d, false),
        new Tuple4<>("b", 2, 2.0d, true),
        new Tuple4<>("c", 3, 3.0d, false),
        new Tuple4<>("d", 4, 4.0d, true));
    var tuple4 = ListsOps.unzip4AndFlatten(
        tuple4s,
        mapperTuple4 ->
            //@formatter:off
            mapperTuple4._1().equals("c")
                ? Optional.empty()
                : mapperTuple4._2() == 4
                    ? Optional.of(new Tuple4<>(
                        Optional.of(mapperTuple4._1()),
                        Optional.of(mapperTuple4._2()),
                        Optional.of(mapperTuple4._3()),
                        Optional.of(mapperTuple4._4())))
                    : mapperTuple4._1().equals("a")
                        ? Optional.of(new Tuple4<>(
                            Optional.of(mapperTuple4._1()),
                            Optional.empty(),
                            Optional.of(mapperTuple4._3()),
                            Optional.empty()))
                        : Optional.of(new Tuple4<>(
                            Optional.empty(),
                            Optional.of(mapperTuple4._2()),
                            Optional.empty(),
                            Optional.of(mapperTuple4._4()))));
    //@formatter:off
    assertEquals(List.of("a", "d"), tuple4._1());
    assertEquals(List.of(2, 4), tuple4._2());
    assertEquals(List.of(1.0, 4.0), tuple4._3());
    assertEquals(List.of(true, true), tuple4._4());
  }

  @Test
  public void testUnzip5() {
    assertEquals(
        new Tuple5<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip5(Stream.empty()));
    var tuple5s = Stream.of(
        new Tuple5<>("a", 1, 1.0d, false, 'A'),
        new Tuple5<>("b", 2, 2.0d, true, 'B'),
        new Tuple5<>("c", 3, 3.0d, false, 'C'),
        new Tuple5<>("d", 4, 4.0d, true, 'D'));
    var tuple5 = ListsOps.unzip5(tuple5s);
    assertEquals(List.of("a", "b", "c", "d"), tuple5._1());
    assertEquals(List.of(1, 2, 3, 4), tuple5._2());
    assertEquals(List.of(1.0d, 2.0d, 3.0d, 4.0d), tuple5._3());
    assertEquals(List.of(false, true, false, true), tuple5._4());
    assertEquals(List.of('A', 'B', 'C', 'D'), tuple5._5());
  }

  @Test
  public void testUnzip5AndFlatten() {
    assertEquals(
        new Tuple5<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip5AndFlatten(
            Stream.empty(),
            t ->
                Optional.empty()));
    var tuple5s = Stream.of(
        new Tuple5<>("a", 1, 1.0d, false, 'A'),
        new Tuple5<>("b", 2, 2.0d, true, 'B'),
        new Tuple5<>("c", 3, 3.0d, false, 'C'),
        new Tuple5<>("d", 4, 4.0d, true, 'D'));
    var tuple5 = ListsOps.unzip5AndFlatten(
        tuple5s,
        mapperTuple5 ->
            //@formatter:off
            mapperTuple5._1().equals("c")
                ? Optional.empty()
                : mapperTuple5._2() == 4
                    ? Optional.of(new Tuple5<>(
                        Optional.of(mapperTuple5._1()),
                        Optional.of(mapperTuple5._2()),
                        Optional.of(mapperTuple5._3()),
                        Optional.of(mapperTuple5._4()),
                        Optional.of(mapperTuple5._5())))
                    : mapperTuple5._1().equals("a")
                        ? Optional.of(new Tuple5<>(
                            Optional.of(mapperTuple5._1()),
                            Optional.empty(),
                            Optional.of(mapperTuple5._3()),
                            Optional.empty(),
                            Optional.of(mapperTuple5._5())))
                        : Optional.of(new Tuple5<>(
                            Optional.empty(),
                            Optional.of(mapperTuple5._2()),
                            Optional.empty(),
                            Optional.of(mapperTuple5._4()),
                            Optional.empty())));
    //@formatter:off
    assertEquals(List.of("a", "d"), tuple5._1());
    assertEquals(List.of(2, 4), tuple5._2());
    assertEquals(List.of(1.0, 4.0), tuple5._3());
    assertEquals(List.of(true, true), tuple5._4());
    assertEquals(List.of('A', 'D'), tuple5._5());
  }

  @Test
  public void testUnzip6() {
    assertEquals(
        new Tuple6<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip6(Stream.empty()));
    var tuple6s = Stream.of(
        new Tuple6<>("a", 1, 1.0d, false, 'A', "E"),
        new Tuple6<>("b", 2, 2.0d, true, 'B', "f"),
        new Tuple6<>("c", 3, 3.0d, false, 'C', "G"),
        new Tuple6<>("d", 4, 4.0d, true, 'D', "h"));
    var tuple6 = ListsOps.unzip6(tuple6s);
    assertEquals(List.of("a", "b", "c", "d"), tuple6._1());
    assertEquals(List.of(1, 2, 3, 4), tuple6._2());
    assertEquals(List.of(1.0d, 2.0d, 3.0d, 4.0d), tuple6._3());
    assertEquals(List.of(false, true, false, true), tuple6._4());
    assertEquals(List.of('A', 'B', 'C', 'D'), tuple6._5());
    assertEquals(List.of("E", "f", "G", "h"), tuple6._6());
  }

  @Test
  public void testUnzip6AndFlatten() {
    assertEquals(
        new Tuple6<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip6AndFlatten(
            Stream.empty(),
            t ->
                Optional.empty()));
    var tuple6s = Stream.of(
        new Tuple6<>("a", 1, 1.0d, false, 'A', "E"),
        new Tuple6<>("b", 2, 2.0d, true, 'B', "f"),
        new Tuple6<>("c", 3, 3.0d, false, 'C', "G"),
        new Tuple6<>("d", 4, 4.0d, true, 'D', "h"));
    var tuple6 = ListsOps.unzip6AndFlatten(
        tuple6s,
        mapperTuple6 ->
            //@formatter:off
            mapperTuple6._1().equals("c")
                ? Optional.empty()
                : mapperTuple6._2() == 4
                    ? Optional.of(new Tuple6<>(
                        Optional.of(mapperTuple6._1()),
                        Optional.of(mapperTuple6._2()),
                        Optional.of(mapperTuple6._3()),
                        Optional.of(mapperTuple6._4()),
                        Optional.of(mapperTuple6._5()),
                        Optional.of(mapperTuple6._6())))
                    : mapperTuple6._1().equals("a")
                        ? Optional.of(new Tuple6<>(
                            Optional.of(mapperTuple6._1()),
                            Optional.empty(),
                            Optional.of(mapperTuple6._3()),
                            Optional.empty(),
                            Optional.of(mapperTuple6._5()),
                            Optional.empty()))
                        : Optional.of(new Tuple6<>(
                            Optional.empty(),
                            Optional.of(mapperTuple6._2()),
                            Optional.empty(),
                            Optional.of(mapperTuple6._4()),
                            Optional.empty(),
                            Optional.of(mapperTuple6._6()))));
    //@formatter:off
    assertEquals(List.of("a", "d"), tuple6._1());
    assertEquals(List.of(2, 4), tuple6._2());
    assertEquals(List.of(1.0, 4.0), tuple6._3());
    assertEquals(List.of(true, true), tuple6._4());
    assertEquals(List.of('A', 'D'), tuple6._5());
    assertEquals(List.of("f", "h"), tuple6._6());
  }

  @Test
  public void testUnzip7() {
    assertEquals(
        new Tuple7<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip7(Stream.empty()));
    var tuple7s = Stream.of(
        new Tuple7<>("a", 1, 1.0d, false, 'A', "E", 5),
        new Tuple7<>("b", 2, 2.0d, true, 'B', "f", 6),
        new Tuple7<>("c", 3, 3.0d, false, 'C', "G", 7),
        new Tuple7<>("d", 4, 4.0d, true, 'D', "h", 8));
    var tuple7 = ListsOps.unzip7(tuple7s);
    assertEquals(List.of("a", "b", "c", "d"), tuple7._1());
    assertEquals(List.of(1, 2, 3, 4), tuple7._2());
    assertEquals(List.of(1.0d, 2.0d, 3.0d, 4.0d), tuple7._3());
    assertEquals(List.of(false, true, false, true), tuple7._4());
    assertEquals(List.of('A', 'B', 'C', 'D'), tuple7._5());
    assertEquals(List.of("E", "f", "G", "h"), tuple7._6());
    assertEquals(List.of(5, 6, 7, 8), tuple7._7());
  }

  @Test
  public void testUnzip7AndFlatten() {
    assertEquals(
        new Tuple7<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip7AndFlatten(
            Stream.empty(),
            t ->
                Optional.empty()));
    var tuple7s = Stream.of(
        new Tuple7<>("a", 1, 1.0d, false, 'A', "E", 5),
        new Tuple7<>("b", 2, 2.0d, true, 'B', "f", 6),
        new Tuple7<>("c", 3, 3.0d, false, 'C', "G", 7),
        new Tuple7<>("d", 4, 4.0d, true, 'D', "h", 8));
    var tuple7 = ListsOps.unzip7AndFlatten(
        tuple7s,
        mapperTuple7 ->
            //@formatter:off
            mapperTuple7._1().equals("c")
                ? Optional.empty()
                : mapperTuple7._2() == 4
                    ? Optional.of(new Tuple7<>(
                        Optional.of(mapperTuple7._1()),
                        Optional.of(mapperTuple7._2()),
                        Optional.of(mapperTuple7._3()),
                        Optional.of(mapperTuple7._4()),
                        Optional.of(mapperTuple7._5()),
                        Optional.of(mapperTuple7._6()),
                        Optional.of(mapperTuple7._7())))
                    : mapperTuple7._1().equals("a")
                        ? Optional.of(new Tuple7<>(
                            Optional.of(mapperTuple7._1()),
                            Optional.empty(),
                            Optional.of(mapperTuple7._3()),
                            Optional.empty(),
                            Optional.of(mapperTuple7._5()),
                            Optional.empty(),
                            Optional.of(mapperTuple7._7())))
                        : Optional.of(new Tuple7<>(
                            Optional.empty(),
                            Optional.of(mapperTuple7._2()),
                            Optional.empty(),
                            Optional.of(mapperTuple7._4()),
                            Optional.empty(),
                            Optional.of(mapperTuple7._6()),
                            Optional.empty())));
    //@formatter:off
    assertEquals(List.of("a", "d"), tuple7._1());
    assertEquals(List.of(2, 4), tuple7._2());
    assertEquals(List.of(1.0, 4.0), tuple7._3());
    assertEquals(List.of(true, true), tuple7._4());
    assertEquals(List.of('A', 'D'), tuple7._5());
    assertEquals(List.of("f", "h"), tuple7._6());
    assertEquals(List.of(5, 8), tuple7._7());
  }

  @Test
  public void testUnzip8() {
    assertEquals(
        new Tuple8<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip8(Stream.empty()));
    var tuple8s = Stream.of(
        new Tuple8<>("a", 1, 1.0d, false, 'A', "E", 5, 5.0f),
        new Tuple8<>("b", 2, 2.0d, true, 'B', "f", 6, 6.0f),
        new Tuple8<>("c", 3, 3.0d, false, 'C', "G", 7, 7.0f),
        new Tuple8<>("d", 4, 4.0d, true, 'D', "h", 8, 8.0f));
    var tuple8 = ListsOps.unzip8(tuple8s);
    assertEquals(List.of("a", "b", "c", "d"), tuple8._1());
    assertEquals(List.of(1, 2, 3, 4), tuple8._2());
    assertEquals(List.of(1.0d, 2.0d, 3.0d, 4.0d), tuple8._3());
    assertEquals(List.of(false, true, false, true), tuple8._4());
    assertEquals(List.of('A', 'B', 'C', 'D'), tuple8._5());
    assertEquals(List.of("E", "f", "G", "h"), tuple8._6());
    assertEquals(List.of(5, 6, 7, 8), tuple8._7());
    assertEquals(List.of(5.0f, 6.0f, 7.0f, 8.0f), tuple8._8());
  }

  @Test
  public void testUnzip8AndFlatten() {
    assertEquals(
        new Tuple8<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip8AndFlatten(
            Stream.empty(),
            t ->
                Optional.empty()));
    var tuple8s = Stream.of(
        new Tuple8<>("a", 1, 1.0d, false, 'A', "E", 5, 5.0f),
        new Tuple8<>("b", 2, 2.0d, true, 'B', "f", 6, 6.0f),
        new Tuple8<>("c", 3, 3.0d, false, 'C', "G", 7, 7.0f),
        new Tuple8<>("d", 4, 4.0d, true, 'D', "h", 8, 8.0f));
    var tuple8 = ListsOps.unzip8AndFlatten(
        tuple8s,
        mapperTuple8 ->
            //@formatter:off
            mapperTuple8._1().equals("c")
                ? Optional.empty()
                : mapperTuple8._2() == 4
                    ? Optional.of(new Tuple8<>(
                        Optional.of(mapperTuple8._1()),
                        Optional.of(mapperTuple8._2()),
                        Optional.of(mapperTuple8._3()),
                        Optional.of(mapperTuple8._4()),
                        Optional.of(mapperTuple8._5()),
                        Optional.of(mapperTuple8._6()),
                        Optional.of(mapperTuple8._7()),
                            Optional.of(mapperTuple8._8())))
                    : mapperTuple8._1().equals("a")
                        ? Optional.of(new Tuple8<>(
                            Optional.of(mapperTuple8._1()),
                            Optional.empty(),
                            Optional.of(mapperTuple8._3()),
                            Optional.empty(),
                            Optional.of(mapperTuple8._5()),
                            Optional.empty(),
                            Optional.of(mapperTuple8._7()),
                            Optional.empty()))
                        : Optional.of(new Tuple8<>(
                            Optional.empty(),
                            Optional.of(mapperTuple8._2()),
                            Optional.empty(),
                            Optional.of(mapperTuple8._4()),
                            Optional.empty(),
                            Optional.of(mapperTuple8._6()),
                            Optional.empty(),
                            Optional.of(mapperTuple8._8()))));
    //@formatter:off
    assertEquals(List.of("a", "d"), tuple8._1());
    assertEquals(List.of(2, 4), tuple8._2());
    assertEquals(List.of(1.0, 4.0), tuple8._3());
    assertEquals(List.of(true, true), tuple8._4());
    assertEquals(List.of('A', 'D'), tuple8._5());
    assertEquals(List.of("f", "h"), tuple8._6());
    assertEquals(List.of(5, 8), tuple8._7());
    assertEquals(List.of(6.0f, 8.0f), tuple8._8());
  }

  @Test
  public void testUnzip9() {
    assertEquals(
        new Tuple9<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip9(Stream.empty()));
    var tuple9s = Stream.of(
        new Tuple9<>("a", 1, 1.0d, false, 'A', "E", 5, 5.0f, true),
        new Tuple9<>("b", 2, 2.0d, true, 'B', "f", 6, 6.0f, true),
        new Tuple9<>("c", 3, 3.0d, false, 'C', "G", 7, 7.0f, false),
        new Tuple9<>("d", 4, 4.0d, true, 'D', "h", 8, 8.0f, false));
    var tuple9 = ListsOps.unzip9(tuple9s);
    assertEquals(List.of("a", "b", "c", "d"), tuple9._1());
    assertEquals(List.of(1, 2, 3, 4), tuple9._2());
    assertEquals(List.of(1.0d, 2.0d, 3.0d, 4.0d), tuple9._3());
    assertEquals(List.of(false, true, false, true), tuple9._4());
    assertEquals(List.of('A', 'B', 'C', 'D'), tuple9._5());
    assertEquals(List.of("E", "f", "G", "h"), tuple9._6());
    assertEquals(List.of(5, 6, 7, 8), tuple9._7());
    assertEquals(List.of(5.0f, 6.0f, 7.0f, 8.0f), tuple9._8());
    assertEquals(List.of(true, true, false, false), tuple9._9());
  }

  @Test
  public void testUnzip9AndFlatten() {
    assertEquals(
        new Tuple9<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip9AndFlatten(
            Stream.empty(),
            t ->
                Optional.empty()));
    var tuple9s = Stream.of(
        new Tuple9<>("a", 1, 1.0d, false, 'A', "E", 5, 5.0f, true),
        new Tuple9<>("b", 2, 2.0d, true, 'B', "f", 6, 6.0f, true),
        new Tuple9<>("c", 3, 3.0d, false, 'C', "G", 7, 7.0f, false),
        new Tuple9<>("d", 4, 4.0d, true, 'D', "h", 8, 8.0f, false));
    var tuple9 = ListsOps.unzip9AndFlatten(
        tuple9s,
        mapperTuple9 ->
            //@formatter:off
            mapperTuple9._1().equals("c")
                ? Optional.empty()
                : mapperTuple9._2() == 4
                    ? Optional.of(new Tuple9<>(
                        Optional.of(mapperTuple9._1()),
                        Optional.of(mapperTuple9._2()),
                        Optional.of(mapperTuple9._3()),
                        Optional.of(mapperTuple9._4()),
                        Optional.of(mapperTuple9._5()),
                        Optional.of(mapperTuple9._6()),
                        Optional.of(mapperTuple9._7()),
                        Optional.of(mapperTuple9._8()),
                        Optional.of(mapperTuple9._9())))
                    : mapperTuple9._1().equals("a")
                        ? Optional.of(new Tuple9<>(
                            Optional.of(mapperTuple9._1()),
                            Optional.empty(),
                            Optional.of(mapperTuple9._3()),
                            Optional.empty(),
                            Optional.of(mapperTuple9._5()),
                            Optional.empty(),
                            Optional.of(mapperTuple9._7()),
                            Optional.empty(),
                            Optional.of(mapperTuple9._9())))
                        : Optional.of(new Tuple9<>(
                            Optional.empty(),
                            Optional.of(mapperTuple9._2()),
                            Optional.empty(),
                            Optional.of(mapperTuple9._4()),
                            Optional.empty(),
                            Optional.of(mapperTuple9._6()),
                            Optional.empty(),
                            Optional.of(mapperTuple9._8()),
                            Optional.empty())));
    //@formatter:off
    assertEquals(List.of("a", "d"), tuple9._1());
    assertEquals(List.of(2, 4), tuple9._2());
    assertEquals(List.of(1.0, 4.0), tuple9._3());
    assertEquals(List.of(true, true), tuple9._4());
    assertEquals(List.of('A', 'D'), tuple9._5());
    assertEquals(List.of("f", "h"), tuple9._6());
    assertEquals(List.of(5, 8), tuple9._7());
    assertEquals(List.of(6.0f, 8.0f), tuple9._8());
    assertEquals(List.of(true, false), tuple9._9());
  }

  @Test
  public void testUnzip10() {
    assertEquals(
        new Tuple10<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip10(Stream.empty()));
    var tuple10s = Stream.of(
        new Tuple10<>("a", 1, 1.0d, false, 'A', "E", 5, 5.0f, true, 'w'),
        new Tuple10<>("b", 2, 2.0d, true, 'B', "f", 6, 6.0f, true, 'x'),
        new Tuple10<>("c", 3, 3.0d, false, 'C', "G", 7, 7.0f, false, 'y'),
        new Tuple10<>("d", 4, 4.0d, true, 'D', "h", 8, 8.0f, false, 'z'));
    var tuple10 = ListsOps.unzip10(tuple10s);
    assertEquals(List.of("a", "b", "c", "d"), tuple10._1());
    assertEquals(List.of(1, 2, 3, 4), tuple10._2());
    assertEquals(List.of(1.0d, 2.0d, 3.0d, 4.0d), tuple10._3());
    assertEquals(List.of(false, true, false, true), tuple10._4());
    assertEquals(List.of('A', 'B', 'C', 'D'), tuple10._5());
    assertEquals(List.of("E", "f", "G", "h"), tuple10._6());
    assertEquals(List.of(5, 6, 7, 8), tuple10._7());
    assertEquals(List.of(5.0f, 6.0f, 7.0f, 8.0f), tuple10._8());
    assertEquals(List.of(true, true, false, false), tuple10._9());
    assertEquals(List.of('w', 'x', 'y', 'z'), tuple10._10());
  }

  @Test
  public void testUnzip10AndFlatten() {
    assertEquals(
        new Tuple10<>(
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of(),
            List.of()),
        ListsOps.unzip10AndFlatten(
            Stream.empty(),
            t ->
                Optional.empty()));
    var tuple10s = Stream.of(
        new Tuple10<>("a", 1, 1.0d, false, 'A', "E", 5, 5.0f, true, 'w'),
        new Tuple10<>("b", 2, 2.0d, true, 'B', "f", 6, 6.0f, true, 'x'),
        new Tuple10<>("c", 3, 3.0d, false, 'C', "G", 7, 7.0f, false, 'y'),
        new Tuple10<>("d", 4, 4.0d, true, 'D', "h", 8, 8.0f, false, 'z'));
    var tuple10 = ListsOps.unzip10AndFlatten(
        tuple10s,
        mapperTuple10 ->
            //@formatter:off
            mapperTuple10._1().equals("c")
                ? Optional.empty()
                : mapperTuple10._2() == 4
                    ? Optional.of(new Tuple10<>(
                        Optional.of(mapperTuple10._1()),
                        Optional.of(mapperTuple10._2()),
                        Optional.of(mapperTuple10._3()),
                        Optional.of(mapperTuple10._4()),
                        Optional.of(mapperTuple10._5()),
                        Optional.of(mapperTuple10._6()),
                        Optional.of(mapperTuple10._7()),
                        Optional.of(mapperTuple10._8()),
                    Optional.of(mapperTuple10._9()),
                        Optional.of(mapperTuple10._10())))
                    : mapperTuple10._1().equals("a")
                        ? Optional.of(new Tuple10<>(
                            Optional.of(mapperTuple10._1()),
                            Optional.empty(),
                            Optional.of(mapperTuple10._3()),
                            Optional.empty(),
                            Optional.of(mapperTuple10._5()),
                            Optional.empty(),
                            Optional.of(mapperTuple10._7()),
                            Optional.empty(),
                            Optional.of(mapperTuple10._9()),
                            Optional.empty()))
                        : Optional.of(new Tuple10<>(
                            Optional.empty(),
                            Optional.of(mapperTuple10._2()),
                            Optional.empty(),
                            Optional.of(mapperTuple10._4()),
                            Optional.empty(),
                            Optional.of(mapperTuple10._6()),
                            Optional.empty(),
                            Optional.of(mapperTuple10._8()),
                            Optional.empty(),
                            Optional.of(mapperTuple10._10()))));
    //@formatter:off
    assertEquals(List.of("a", "d"), tuple10._1());
    assertEquals(List.of(2, 4), tuple10._2());
    assertEquals(List.of(1.0, 4.0), tuple10._3());
    assertEquals(List.of(true, true), tuple10._4());
    assertEquals(List.of('A', 'D'), tuple10._5());
    assertEquals(List.of("f", "h"), tuple10._6());
    assertEquals(List.of(5, 8), tuple10._7());
    assertEquals(List.of(6.0f, 8.0f), tuple10._8());
    assertEquals(List.of(true, false), tuple10._9());
    assertEquals(List.of('x', 'z'), tuple10._10());
  }

  @Test
  public void testUnzipNullTupleResilience() {
    var tuple2sStreamWithNulls = Stream.of(
        new Tuple2<>("a", 1),
        null,
        new Tuple2<>("b", 2));
    @SuppressWarnings("NullableProblems")
    var result = ListsOps.unzip(tuple2sStreamWithNulls);
    assertEquals(List.of("a", "b"), result._1());
    assertEquals(List.of(1, 2), result._2());
    assertTrue(CollectionsOps.isUnmodifiable(result._1()));
    assertTrue(CollectionsOps.isUnmodifiable(result._2()));
  }

  @Test
  public void testModernizedPipelinesImmutabilityAndEdgeCases() {
    // appendItem
    var listAppended = ListsOps.appendItem(List.of("x", "y"), "z");
    assertEquals(List.of("x", "y", "z"), listAppended);
    assertTrue(CollectionsOps.isUnmodifiable(listAppended));
    assertThrows(UnsupportedOperationException.class, () -> listAppended.add("fail"));

    // removeItem first match
    var listRemoved = ListsOps.removeItem(List.of("a", "b", "a", "c"), "a");
    assertEquals(List.of("b", "a", "c"), listRemoved);
    assertTrue(CollectionsOps.isUnmodifiable(listRemoved));

    // removeAll frequency count
    var initialList = List.of(1, 2, 2, 3, 2, 4);
    var resultRemoveAll = ListsOps.removeAll(initialList, Stream.of(2, 2));
    assertEquals(List.of(1, 3, 2, 4), resultRemoveAll);
    assertTrue(CollectionsOps.isUnmodifiable(resultRemoveAll));

    // reverse
    var reversedList = ListsOps.reverse(Stream.of("first", "second", "third"));
    assertEquals(List.of("third", "second", "first"), reversedList);
    assertTrue(CollectionsOps.isUnmodifiable(reversedList));
    assertThrows(UnsupportedOperationException.class, () -> reversedList.remove(0));
  }
}
