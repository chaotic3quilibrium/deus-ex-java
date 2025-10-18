package org.deus_ex_java.util.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

//TODO: x2 missing javadocs
public record Tuple10<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10>(
    @NotNull T1 _1,
    @NotNull T2 _2,
    @NotNull T3 _3,
    @NotNull T4 _4,
    @NotNull T5 _5,
    @NotNull T6 _6,
    @NotNull T7 _7,
    @NotNull T8 _8,
    @NotNull T9 _9,
    @NotNull T10 _10
) implements Tuple {
  @Override
  public int arity() {
    return 10;
  }

  @Override
  public Stream<?> stream() {
    return Stream.of(
        _1, _2, _3, _4, _5, _6, _7, _8, _9, _10);
  }

  //TODO: x21 add "withers"
  // - map() defining functions for each field
  // - map_N() defining a single function to type transform a specific single field
  // - update_N() defining a single function to update the value of a specific single field
}
