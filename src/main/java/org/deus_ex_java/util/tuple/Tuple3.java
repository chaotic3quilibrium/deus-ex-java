package org.deus_ex_java.util.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

//TODO: x2 missing javadocs
public record Tuple3<T1, T2, T3>(
    @NotNull T1 _1,
    @NotNull T2 _2,
    @NotNull T3 _3
) implements Tuple {
  @Override
  public int arity() {
    return 3;
  }

  @Override
  public Stream<?> stream() {
    return Stream.of(
        _1, _2, _3);
  }

  //TODO: x7 add "withers"
  // - map() defining functions for each field
  // - map_N() defining a single function to type transform a specific single field
  // - update_N() defining a single function to update the value of a specific single field
}
