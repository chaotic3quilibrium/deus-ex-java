package org.deus_ex_java.util.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

/**
 * An implementor of {@link Tuple} consisting of one element.
 *
 * @param _1   the value contained in the first element
 * @param <T1> the type of the value contained in the first element
 */
public record Tuple1<T1>(
    @NotNull T1 _1
) implements Tuple {
  @Override
  public int arity() {
    return 1;
  }

  @Override
  public Stream<?> stream() {
    return Stream.of(
        _1);
  }
}

