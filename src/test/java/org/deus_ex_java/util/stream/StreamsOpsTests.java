package org.deus_ex_java.util.stream;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Map.entry;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("DataFlowIssue")
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
  public void testZipStreamAndCollectionPermutations() {
    var listInts0To4 = List.of(0, 1, 2, 3, 4);
    var listStringsX0to2 = List.of("x0", "x1", "x2");
    var zipped = List.of(entry(0, "x0"), entry(1, "x1"), entry(2, "x2"));
    assertEquals(zipped, StreamsOps.zip(listInts0To4, listStringsX0to2).toList());
    assertEquals(zipped, StreamsOps.zip(listInts0To4.stream(), listStringsX0to2).toList());
    assertEquals(zipped, StreamsOps.zip(listInts0To4, listStringsX0to2.stream()).toList());
  }

  @Test
  public void testZipStream() {
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
  public void testZipWithIndexCollection() {
    var listEmpty = StreamsOps.zipWithIndex(List.of()).toList();
    assertTrue(listEmpty.isEmpty());
    var stringAndIndexes = StreamsOps.zipWithIndex(List.of("x0", "x1", "x2")).toList();
    assertEquals(List.of(entry("x0", 0), entry("x1", 1), entry("x2", 2)), stringAndIndexes);
  }

  @Test
  public void testZipWithIndexStream() {
    var listEmpty = StreamsOps.zipWithIndex(Stream.empty()).toList();
    assertTrue(listEmpty.isEmpty());
    var stringAndIndexes = StreamsOps.zipWithIndex(Stream.of("x0", "x1", "x2")).toList();
    assertEquals(List.of(entry("x0", 0), entry("x1", 1), entry("x2", 2)), stringAndIndexes);
  }

  @Test
  void testFilterMatchesType() {
    var input = "Hello World";
    var resultList = StreamsOps.filter(String.class)
        .apply(input)
        .toList();
    assertEquals(1, resultList.size(), "Stream should contain exactly one element");
    assertEquals("Hello World", resultList.get(0), "Stream element should match the input");
  }

  @Test
  void testFilterDoesNotMatchType() {
    var input = 123; // Mismatched type
    var resultList = StreamsOps.filter(String.class)
        .apply(input)
        .toList();
    assertTrue(resultList.isEmpty(), "Stream should be empty for a mismatched type");
  }

  @Test
  void testFilterNullInput() {
    var resultList = StreamsOps.filter(String.class).apply(null).toList();
    assertTrue(resultList.isEmpty(), "Stream should be empty when evaluating a null input");
  }

  @Test
  void testFilterNullClassType() {
    var input = "Test";
    assertThrows(
        NullPointerException.class,
        () -> StreamsOps.filter(null).apply(input),
        "Expected NullPointerException when the target class type is null");
  }

  @Test
  void testFilterIntegrationWithFlatMap() {
    var mixedList = List.of(
        "Apple",
        42,
        "Banana",
        3.14,
        "Cherry");
    var stringOnlyList = mixedList.stream()
        .flatMap(StreamsOps.filter(String.class))
        .toList();
    var expected = List.of("Apple", "Banana", "Cherry");
    assertEquals(
        expected,
        stringOnlyList,
        "Should successfully filter out non-String elements");
  }

  @Test
  void testFilterNotDoesNotMatchType() {
    var input = 123; // Mismatched type
    var resultList = StreamsOps.filterNot(String.class)
        .apply(input)
        .toList();
    assertEquals(1, resultList.size(), "Stream should contain exactly one element");
    assertEquals(123, resultList.get(0), "Stream element should be retained");
  }

  @Test
  void testFilterNotMatchesType() {
    var input = "Hello World"; // Matched type
    var resultList = StreamsOps.filterNot(String.class)
        .apply(input)
        .toList();
    assertTrue(resultList.isEmpty(), "Stream should be empty for a matched type");
  }

  @Test
  void testFilterNotNullInput() {
    var resultList = StreamsOps.filterNot(String.class).apply(null).toList();
    assertEquals(1, resultList.size(), "Stream should retain null as Class.isInstance(null) is false");
    assertNull(resultList.get(0), "Retained element should be null");
  }

  @Test
  void testFilterNotNullClassType() {
    var input = "Test";
    assertThrows(
        NullPointerException.class,
        () ->
            StreamsOps.filterNot(null).apply(input),
        "Expected NullPointerException when the target class type is null");
  }

  @Test
  void testFilterNotIntegrationWithFlatMap() {
    var mixedList = List.of(
        "Apple",
        42,
        "Banana",
        3.14,
        "Cherry");
    var nonStringList = mixedList.stream()
        .flatMap(StreamsOps.filterNot(String.class))
        .toList();
    var expected = List.of(42, 3.14);
    assertEquals(
        expected,
        nonStringList,
        "Should successfully filter out String elements");
  }

  @Test
  void testZipWithIndexParallelism() {
    var parallelStream = Stream.of("A", "B", "C", "D", "E").parallel();
    var result = StreamsOps.zipWithIndex(parallelStream).toList();
    var expected = List.of(
        entry("A", 0),
        entry("B", 1),
        entry("C", 2),
        entry("D", 3),
        entry("E", 4));
    assertEquals(
        expected,
        result,
        "Stream should be forced sequential, assigning ordered indices");
  }

  @Test
  void testZipAsymmetricStreams() {
    var shortList = List.of(1, 2);
    var longList = List.of("A", "B", "C", "D");
    var leftShorter = StreamsOps.zip(shortList.stream(), longList.stream()).toList();
    var expectedLeftShorter = List.of(entry(1, "A"), entry(2, "B"));
    assertEquals(
        expectedLeftShorter,
        leftShorter,
        "Stream should terminate gracefully when the left stream ends first"
    );
    var rightShorter = StreamsOps.zip(longList.stream(), shortList.stream()).toList();
    var expectedRightShorter = List.of(entry("A", 1), entry("B", 2));
    assertEquals(
        expectedRightShorter,
        rightShorter,
        "Stream should terminate gracefully when the right stream ends first"
    );
  }

  @SuppressWarnings("DataFlowIssue")
  @Test
  void testFromNullValidations() {
    assertThrows(
        NullPointerException.class,
        () -> StreamsOps.from((Iterator<Object>) null),
        "Expected NPE when providing a null Iterator"
    );
    assertThrows(
        NullPointerException.class,
        () -> StreamsOps.from((Iterator<Object>) null, true),
        "Expected NPE when providing a null Iterator with parallel flag"
    );

    // Validate fail-fast Iterable factories
    assertThrows(
        NullPointerException.class,
        () -> StreamsOps.from((Iterable<Object>) null),
        "Expected NPE when providing a null Iterable"
    );
    assertThrows(
        NullPointerException.class,
        () -> StreamsOps.from((Iterable<Object>) null, true),
        "Expected NPE when providing a null Iterable with parallel flag"
    );
  }
}
