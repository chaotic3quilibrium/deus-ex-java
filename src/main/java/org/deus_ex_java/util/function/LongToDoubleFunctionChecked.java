package org.deus_ex_java.util.function;

import java.util.function.LongToDoubleFunction;

/**
 * Enables the providing of a {@link LongToDoubleFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface LongToDoubleFunctionChecked<E extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  double applyAsDouble(long value) throws E;
}
