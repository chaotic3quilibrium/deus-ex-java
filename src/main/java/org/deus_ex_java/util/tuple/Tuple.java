package org.deus_ex_java.util.tuple;

import java.util.stream.Stream;

/**
 * The base interface of all tuples.
 */
public interface Tuple {
  /**
   * Arity of smallest tuple implementor.
   */
  int MIN_ARITY = 1;

  /**
   * Arity of largest tuple implementor.
   */
  int MAX_ARITY = 10;

  /**
   * Returns the number of elements of the specific {@link Tuple} implementor.
   *
   * @return the number of elements of the specific {@link Tuple} implementor
   */
  int arity();

  /**
   * Returns the values of the elements as a {@link Stream} of the specific {@link Tuple} implementor.
   *
   * @return the values of the elements as a {@link Stream} of the specific {@link Tuple} implementor
   */
  Stream<?> stream();
}
