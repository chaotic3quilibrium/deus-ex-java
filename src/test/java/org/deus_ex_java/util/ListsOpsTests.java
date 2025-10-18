package org.deus_ex_java.util;

import org.deus_ex_java.util.tuple.*;
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
  public void testToList() {
    var expectedList = List.of(1, 2, 3);
    var nullContainingList = Arrays.asList(null, 1, null, 2, null, 3, null);
    assertEquals(7, nullContainingList.size());
    var actualList = ListsOps.toList(nullContainingList.stream());
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
    assertEquals(
        new Tuple2<>(
            List.of(),
            List.of()),
        ListsOps.unzip(Stream.empty()));
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
    assertEquals(
        new Tuple2<>(
            List.of(),
            List.of()),
        ListsOps.unzipAndFlatten(
            Stream.empty(),
            t ->
                Optional.empty()));
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
}
