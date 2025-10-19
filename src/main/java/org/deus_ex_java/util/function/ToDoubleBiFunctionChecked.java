package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.ToDoubleBiFunction;

/**
 * Enables the providing of a {@link ToDoubleBiFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface ToDoubleBiFunctionChecked<T, U, EX extends Exception> {

  /**
   * Applies this function to the given arguments.
   *
   * @param t the first function argument
   * @param u the second function argument
   * @return the function result
   */
  double applyAsDouble(@NotNull T t, @NotNull U u) throws EX;
}
