package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.IntFunction;

/**
 * Enables the providing of a {@link IntFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface IntFunctionChecked<R, EX extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  R apply(int value) throws EX;
}
