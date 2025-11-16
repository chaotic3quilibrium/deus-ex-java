package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.LongFunction;

/**
 * Enables the providing of a {@link LongFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface LongFunctionChecked<R, EX extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  R apply(long value) throws EX;
}
