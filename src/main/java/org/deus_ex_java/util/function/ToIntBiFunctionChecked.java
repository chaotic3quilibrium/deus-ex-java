package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.ToIntBiFunction;

/**
 * Enables the providing of a {@link ToIntBiFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface ToIntBiFunctionChecked<T, U, EX extends Exception> {

  /**
   * Applies this function to the given arguments.
   *
   * @param t the first function argument
   * @param u the second function argument
   * @return the function result
   */
  int applyAsInt(T t, U u) throws EX;
}
