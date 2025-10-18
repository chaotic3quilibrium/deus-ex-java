package org.deus_ex_java.util.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * Utility class providing static methods to create and work with tuple instances.
 */
public class TuplesOps {
  private TuplesOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Return a conversion from an {@link Entry}.
   *
   * @param entry the value from which to extract the two values
   * @param <K>   the type of the first element
   * @param <V>   the type of the second element
   * @return a conversion from an {@link Entry}
   * @see Tuple2#to() Tuple2.to() for the inverted version of this method
   */
  @NotNull
  public static <K, V> Tuple2<K, V> from(
      @NotNull Entry<K, V> entry
  ) {
    return new Tuple2<>(
        Objects.requireNonNull(entry.getKey()),
        Objects.requireNonNull(entry.getValue()));
  }

  /**
   * Return a conversion from an {@link SimpleImmutableEntry}.
   *
   * @param simpleImmutableEntry the value from which to extract the two values
   * @param <K>                  the type of the first element
   * @param <V>                  the type of the second element
   * @return a conversion from an {@link SimpleImmutableEntry}
   * @see Tuple2#toSimpleImmutableEntry() Tuple2.toSimpleImmutableEntry() for the inverted version of this method
   */
  @NotNull
  public static <K, V> Tuple2<K, V> from(
      @NotNull SimpleImmutableEntry<K, V> simpleImmutableEntry
  ) {
    return new Tuple2<>(
        Objects.requireNonNull(simpleImmutableEntry.getKey()),
        Objects.requireNonNull(simpleImmutableEntry.getValue()));
  }
}
