package org.deus_ex_java.util.function;

import java.util.function.IntToDoubleFunction;

/**
 * Enables the providing of a {@link IntToDoubleFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface IntToDoubleFunctionChecked<EX extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  double applyAsDouble(int value) throws EX;
}
