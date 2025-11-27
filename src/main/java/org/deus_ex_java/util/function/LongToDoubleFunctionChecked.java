package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.LongToDoubleFunction;

/**
 * Enables the providing of a {@link LongToDoubleFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface LongToDoubleFunctionChecked<EX extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  double applyAsDouble(long value) throws EX;
}
