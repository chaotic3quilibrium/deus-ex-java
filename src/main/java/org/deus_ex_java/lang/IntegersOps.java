package org.deus_ex_java.lang;

import org.deus_ex_java.util.ArraysOps;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility class providing static methods to create and work with {@link Integer} instances.
 */
public class IntegersOps {

  private IntegersOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Return a {@link List} containing the index for each bit set in {@code bits}.
   *
   * @param bits the value from which to extract the list of indexes
   * @return a {@link List} containing the index for each bit set in {@code bits}
   */
  @NotNull
  public static List<Integer> findSetBitIndicesAsList(int bits) {
    if (bits == 0) {
      return List.of();
    }

    return Arrays.stream(ArraysOps.findSetBitIndices(bits))
        .boxed()
        .toList();
  }

  /**
   * Return a {@link Set} containing the index for each bit set in {@code bits}.
   *
   * @param bits the value from which to extract the list of indexes
   * @return a {@link Set} containing the index for each bit set in {@code bits}
   */
  @NotNull
  public static Set<Integer> findSetBitIndicesAsSet(int bits) {
    if (bits == 0) {
      return Set.of();
    }

    return Arrays.stream(ArraysOps.findSetBitIndices(bits))
        .boxed()
        .collect(Collectors.toUnmodifiableSet());
  }
}
