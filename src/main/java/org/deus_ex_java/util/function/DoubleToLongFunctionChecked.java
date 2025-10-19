package org.deus_ex_java.util.function;

import java.util.function.DoubleToLongFunction;

/**
 * Enables the providing of a {@link DoubleToLongFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface DoubleToLongFunctionChecked<EX extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  long applyAsLong(double value) throws EX;
}
