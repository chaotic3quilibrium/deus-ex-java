package org.deus_ex_java.util.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

//TODO: x2 missing javadocs
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

  //TODO: x5 add "withers"
  // - map() defining functions for each field
  // - map_N() defining a single function to type transform a specific single field
  // - update_N() defining a single function to update the value of a specific single field
}
