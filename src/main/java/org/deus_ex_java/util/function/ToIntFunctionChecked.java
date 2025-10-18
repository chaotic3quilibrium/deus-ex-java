package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntFunction;

/**
 * Enables the providing of a {@link ToIntFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface ToIntFunctionChecked<T, E extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  int applyAsInt(@NotNull T value) throws E;
}
