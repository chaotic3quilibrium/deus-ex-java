package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.ToIntBiFunction;

/**
 * Enables the providing of a {@link ToIntBiFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface ToIntBiFunctionChecked<T, U, E extends Exception> {

  /**
   * Applies this function to the given arguments.
   *
   * @param t the first function argument
   * @param u the second function argument
   * @return the function result
   */
  int applyAsInt(@NotNull T t, @NotNull U u) throws E;
}
