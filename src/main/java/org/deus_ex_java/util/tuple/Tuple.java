package org.deus_ex_java.util.tuple;

import java.util.stream.Stream;

//TODO: x4 missing javadocs
public sealed interface Tuple permits
    Tuple1,
    Tuple2,
    Tuple3,
    Tuple4,
    Tuple5,
    Tuple6,
    Tuple7,
    Tuple8,
    Tuple9,
    Tuple10 {
  int MIN_ARITY = 1;
  int MAX_ARITY = 10;

  int arity();

  Stream<?> stream();
}
