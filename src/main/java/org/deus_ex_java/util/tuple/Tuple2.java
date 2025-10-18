package org.deus_ex_java.util.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.stream.Stream;

import static java.util.Map.entry;

/**
 * An implementor of {@link Tuple} consisting of two elements, which can be seen as a cartesian product across all of
 * its elements.
 *
 * @param _1   the value contained in the first element
 * @param _2   the value contained in the second element
 * @param <T1> the type of the value contained in the first element
 * @param <T2> the type of the value contained in the second element
 */
public record Tuple2<T1, T2>(
    @NotNull T1 _1,
    @NotNull T2 _2
) implements Tuple {
  @Override
  public int arity() {
    return 2;
  }

  @Override
  public Stream<?> stream() {
    return Stream.of(
        _1, _2);
  }

  /**
   * Return a conversion to as {@link Entry}.
   *
   * @return a conversion to as {@link Entry}
   * @see TuplesOps#from(Entry) TuplesOps.from(Entry) for the inverted version of this method
   */
  @NotNull
  public Entry<T1, T2> to() {
    return entry(_1, _2);
  }

  /**
   * Return a conversion to a {@link SimpleImmutableEntry}.
   *
   * @return a conversion to a {@link SimpleImmutableEntry}
   * @see TuplesOps#from(SimpleImmutableEntry) TuplesOps.from(SimpleImmutableEntry) for the inverted version of this
   *     method
   */
  @NotNull
  public SimpleImmutableEntry<T1, T2> toSimpleImmutableEntry() {
    return new SimpleImmutableEntry<>(_1, _2);
  }
}
