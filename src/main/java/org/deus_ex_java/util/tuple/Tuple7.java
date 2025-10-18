package org.deus_ex_java.util.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * An implementor of {@link Tuple} consisting of seven elements, which can be seen as a cartesian product across all of
 * its elements.
 *
 * @param _1   the value contained in the first element
 * @param _2   the value contained in the second element
 * @param _3   the value contained in the third element
 * @param _4   the value contained in the fourth element
 * @param _5   the value contained in the fifth element
 * @param _6   the value contained in the sixth element
 * @param _7   the value contained in the seventh element
 * @param <T1> the type of the value contained in the first element
 * @param <T2> the type of the value contained in the second element
 * @param <T3> the type of the value contained in the third element
 * @param <T4> the type of the value contained in the fourth element
 * @param <T5> the type of the value contained in the fifth element
 * @param <T6> the type of the value contained in the sixth element
 * @param <T7> the type of the value contained in the seventh element
 */
public record Tuple7<T1, T2, T3, T4, T5, T6, T7>(
    @NotNull T1 _1,
    @NotNull T2 _2,
    @NotNull T3 _3,
    @NotNull T4 _4,
    @NotNull T5 _5,
    @NotNull T6 _6,
    @NotNull T7 _7
) implements Tuple {
  @Override
  public int arity() {
    return 7;
  }

  @Override
  public Stream<?> stream() {
    return Stream.of(
        _1, _2, _3, _4, _5, _6, _7);
  }
}
