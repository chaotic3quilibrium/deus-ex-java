package org.deus_ex_java.util;

import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.function.ToIntFunction;
import java.util.stream.Stream;

/**
 * Utility class providing static methods to create and work with array instances.
 */
@NullMarked
public final class ArraysOps {

  private ArraysOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * An immutable singleton instance of an empty int array.
   */
  public static final int[] EMPTY_INT_ARRAY = new int[0];

  /**
   * Return an array containing the index for each bit set in {@code bits}.
   *
   * @param bits the value from which to extract the array of indexes
   * @return an array containing the index for each bit set in {@code bits}
   */
  public static int[] findSetBitIndices(int bits) {
    if (bits == 0) {
      return ArraysOps.EMPTY_INT_ARRAY;
    }

    int[] bitsIndices = new int[32];
    int size = 0;
    while (bits != 0) {
      int c = bits & -bits;
      int index = Integer.numberOfTrailingZeros(c);
      bitsIndices[size++] = index;
      bits = bits ^ c;
    }
    var result = new int[size];
    System.arraycopy(bitsIndices, 0, result, 0, size);

    return result;
  }

  /**
   * Returns a new {@code int} array from a source of {@link Integer}s.
   *
   * @param collection the source of the derived {@code int} values
   * @return a new {@code int} array from a source of {@link Integer}s
   */
  public static int[] toDistinctSortedArrayInt(
      Collection<Integer> collection
  ) {
    return toDistinctSortedArrayInt(collection.stream());
  }

  /**
   * Returns a new {@code int} array from a source of {@link Integer}s.
   *
   * @param stream the source of the derived {@code int} values
   * @return a new {@code int} array from a source of {@link Integer}s
   */
  public static int[] toDistinctSortedArrayInt(
      Stream<Integer> stream
  ) {
    return toDistinctSortedArrayInt(stream, Integer::intValue);
  }

  /**
   * Returns a new {@code int} array from a source of {@link Integer}s deriving the {@code int} value via the function
   * {@code fTToId}.
   *
   * @param collection the source of the derived {@code int} values
   * @param fTToId     the function to use to extract the {@code int} value from an element of the source
   * @return a new {@code int} array from a source of {@link Integer}s deriving the {@code int} value via the function
   *     {@code fTToId}
   */
  public static <T> int[] toDistinctSortedArrayInt(
      Collection<T> collection,
      ToIntFunction<T> fTToId
  ) {
    return toDistinctSortedArrayInt(collection.stream(), fTToId);
  }

  /**
   * Returns a new {@code int} array from a source of {@code ts} deriving the {@code int} value via the function
   * {@code fTToId}.
   *
   * @param stream the source of the derived {@code int} values
   * @param fTToId the function to use to extract the {@code int} value from an element of the source
   * @param <T>    the type of instances contained in the source
   * @return a new {@code int} array from a source of {@code ts} deriving the {@code int} value via the function
   *     {@code fTToId}
   */
  public static <T> int[] toDistinctSortedArrayInt(
      Stream<T> stream,
      ToIntFunction<T> fTToId
  ) {
    return stream
        .mapToInt(fTToId)
        .distinct()
        .sorted()
        .toArray();
  }
}
