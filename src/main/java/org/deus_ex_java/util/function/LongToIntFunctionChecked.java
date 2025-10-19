package org.deus_ex_java.util.function;

import java.util.function.LongToIntFunction;

/**
 * Enables the providing of a {@link LongToIntFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface LongToIntFunctionChecked<EX extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  int applyAsInt(long value) throws EX;
}
