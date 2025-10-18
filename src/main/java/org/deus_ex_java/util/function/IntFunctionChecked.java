package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.IntFunction;

/**
 * Enables the providing of a {@link IntFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface IntFunctionChecked<R, E extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  @NotNull
  R apply(int value) throws E;
}
