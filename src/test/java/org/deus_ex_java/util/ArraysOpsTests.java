package org.deus_ex_java.util;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ArraysOpsTests {

  private void testFindSetBitIndicesCase(
      int value,
      int length,
      int[] content
  ) {
    var indices = ArraysOps.findSetBitIndices(value);
    assertEquals(length, indices.length);
    assertArrayEquals(content, indices);
  }

  @Test
  public void testFindSetBitIndices() {
    testFindSetBitIndicesCase(
        0,
        0,
        new int[]{});
    testFindSetBitIndicesCase(
        1,
        1,
        new int[]{0});
    testFindSetBitIndicesCase(
        2,
        1,
        new int[]{1});
    testFindSetBitIndicesCase(
        3,
        2,
        new int[]{0, 1});
    testFindSetBitIndicesCase(
        4,
        1,
        new int[]{2});
    testFindSetBitIndicesCase(
        5,
        2,
        new int[]{0, 2});
    testFindSetBitIndicesCase(
        6,
        2,
        new int[]{1, 2});
    testFindSetBitIndicesCase(
        7,
        3,
        new int[]{0, 1, 2});
    testFindSetBitIndicesCase(
        255,
        8,
        new int[]{0, 1, 2, 3, 4, 5, 6, 7});
    testFindSetBitIndicesCase(
        256,
        1,
        new int[]{8});
  }

  @Test
  public void testToDistinctSortedArrayInt() {
    var arrayFromInt = ArraysOps.toDistinctSortedArrayInt(Stream.of(1, 3, 2, 6, 6, 5, 7, 8, 9, 4, 0, 1));
    var arrayExpected = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
    assertArrayEquals(arrayExpected, arrayFromInt);
    var arrayFromString = ArraysOps.toDistinctSortedArrayInt(Stream.of("1", "3", "2", "6", "6", "5", "7", "8", "9", "4", "0", "1"), Integer::parseInt);
    assertArrayEquals(arrayExpected, arrayFromString);
  }
}
