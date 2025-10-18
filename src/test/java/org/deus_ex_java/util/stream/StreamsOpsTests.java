package org.deus_ex_java.util.stream;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StreamsOpsTests {

  @Test
  public void testFromFactories() {
    var fruitsString = "apple,banana,cantaloupe";
    var fruits = List.of(
        "apple",
        "banana",
        "cantaloupe");
    var fruitsStreamFromIterable = StreamsOps.from(fruits);
    assertEquals(fruitsString, String.join(",", fruitsStreamFromIterable.toList()));
    var fruitsStreamFromIterableParallel = StreamsOps.from(fruits, true);
    assertEquals(fruitsString, String.join(",", fruitsStreamFromIterableParallel.toList()));
    var fruitsStreamFromIterator = StreamsOps.from(fruits.iterator());
    assertEquals(fruitsString, String.join(",", fruitsStreamFromIterator.toList()));
    var fruitsStreamFromIteratorParallel = StreamsOps.from(fruits.iterator(), true);
    assertEquals(fruitsString, String.join(",", fruitsStreamFromIteratorParallel.toList()));
  }

  @Test
  public void testZip() {
    var listEmpty = StreamsOps.zip(Stream.empty(), Stream.empty()).toList();
    assertTrue(listEmpty.isEmpty());
    var listInts0To4 = List.of(0, 1, 2, 3, 4);
    var listStringsX0to2 = List.of("x0", "x1", "x2");
    var listA = StreamsOps.zip(listInts0To4.stream(), listStringsX0to2.stream()).toList();
    assertEquals(List.of(entry(0, "x0"), entry(1, "x1"), entry(2, "x2")), listA);
    var listStringsX0to5 = List.of("x0", "x1", "x2", "x3", "x4", "x5");
    var listB = StreamsOps.zip(listInts0To4.stream(), listStringsX0to5.stream()).toList();
    assertEquals(List.of(entry(0, "x0"), entry(1, "x1"), entry(2, "x2"), entry(3, "x3"), entry(4, "x4")), listB);
    var listInts0To5 = List.of(0, 1, 2, 3, 4, 5);
    var listC = StreamsOps.zip(listInts0To5.stream(), listStringsX0to5.stream()).toList();
    assertEquals(List.of(entry(0, "x0"), entry(1, "x1"), entry(2, "x2"), entry(3, "x3"), entry(4, "x4"), entry(5, "x5")), listC);
  }

  @Test
  public void testZipWithIndex() {
    var listEmpty = StreamsOps.zipWithIndex(Stream.empty()).toList();
    assertTrue(listEmpty.isEmpty());
    var stringAndIndexes = StreamsOps.zipWithIndex(Stream.of("x0", "x1", "x2")).toList();
    assertEquals(List.of(entry("x0", 0), entry("x1", 1), entry("x2", 2)), stringAndIndexes);
  }

}
