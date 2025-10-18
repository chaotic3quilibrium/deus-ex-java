package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.ToLongFunction;

/**
 * Enables the providing of a {@link ToLongFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface ToLongFunctionChecked<T, E extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  long applyAsLong(@NotNull T value) throws E;
}
