package org.deus_ex_java.util.function;

import java.util.function.DoubleToIntFunction;

/**
 * Enables the providing of a {@link DoubleToIntFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface DoubleToIntFunctionChecked<E extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  int applyAsInt(double value) throws E;
}
