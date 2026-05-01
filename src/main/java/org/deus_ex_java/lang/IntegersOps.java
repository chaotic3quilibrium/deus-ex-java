package org.deus_ex_java.lang;

import org.deus_ex_java.util.ArraysOps;
import org.deus_ex_java.util.Either;
import org.deus_ex_java.util.SetsOps;
import org.deus_ex_java.util.TryCatchesOps;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Optional;
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

  /**
   * Return an {@link Optional} containing the {@link Integer} value of the successfully parsed
   * {@code String possibleInteger}, otherwise {@link Optional#empty()}.
   * <p>
   * Convenience function to reify any parsing failure into an {@link Optional#empty()}.
   *
   * @param possibleInteger the string value to be parsed
   * @return an {@link Optional} containing the {@link Integer} value of the successfully parsed
   * {@code String possibleInteger}, otherwise {@link Optional#empty()}
   */
  public static Optional<Integer> parseOptional(String possibleInteger) {
    return parseEither(possibleInteger).toOptional();
  }

  /**
   * Return an {@link Either#right(Object)} containing the {@link Integer} value of the successfully
   * parsed {@code String possibleInteger}, otherwise {@link Either#left(Object)} containing the
   * parsing exception.
   * <p>
   * Convenience function to reify any parsing failure into an {@link Either#left(Object)}.
   *
   * @param possibleInteger the string value to be parsed
   * @return an {@link Either#right(Object)} containing the {@link Integer} value of the
   * successfully parsed {@code String possibleInteger}, otherwise {@link Either#left(Object)}
   * containing the parsing exception
   */
  public static Either<NumberFormatException, Integer> parseEither(
      String possibleInteger
  ) {
    return TryCatchesOps.wrap(
        () ->
            Integer.parseInt(possibleInteger),
        NumberFormatException.class);
  }
}
