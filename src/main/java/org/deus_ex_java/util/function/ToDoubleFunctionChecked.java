package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.ToDoubleFunction;

/**
 * Enables the providing of a {@link ToDoubleFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface ToDoubleFunctionChecked<T, EX extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  double applyAsDouble(T value) throws EX;
}
