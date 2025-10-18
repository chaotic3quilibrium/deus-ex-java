package org.deus_ex_java.util.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

//TODO: x2 missing javadocs
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

  //TODO: x3 add "withers"
  // - map() defining functions for each field
  // - map_N() defining a single function to type transform a specific single field
  // - update_N() defining a single function to update the value of a specific single field
}

