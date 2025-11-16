package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.ToIntFunction;

/**
 * Enables the providing of a {@link ToIntFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface ToIntFunctionChecked<T, EX extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  int applyAsInt(T value) throws EX;
}
