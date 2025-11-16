package org.deus_ex_java.lang;

import org.deus_ex_java.util.ArraysOps;
import org.deus_ex_java.util.SetsOps;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Set;

/**
 * Utility class providing static methods to create and work with {@link Integer} instances.
 */
@NullMarked
public final class IntegersOps {

  private IntegersOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Return an unmodifiable <u><i>ordered</i></u> {@link Set} containing the index for each bit set in {@code bits}.
   *
   * @param bits the value from which to extract the list of indexes
   * @return a {@link Set} containing the index for each bit set in {@code bits}
   */
  public static Set<Integer> findSetBitIndices(int bits) {
    if (bits == 0) {
      return Set.of();
    }

    return SetsOps.toSetOrdered(Arrays.stream(ArraysOps.findSetBitIndices(bits)).boxed());
  }
}
