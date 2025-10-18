package org.deus_ex_java.util.tuple;

import java.util.stream.Stream;

//TODO: x4 missing javadocs
public interface Tuple {
  int MIN_ARITY = 1;
  int MAX_ARITY = 10;

  int arity();

  Stream<?> stream();
}
