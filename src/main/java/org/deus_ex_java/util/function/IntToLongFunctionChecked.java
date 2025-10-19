package org.deus_ex_java.util.function;

import java.util.function.IntToLongFunction;

/**
 * Enables the providing of a {@link IntToLongFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface IntToLongFunctionChecked<EX extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  long applyAsLong(int value) throws EX;
}
