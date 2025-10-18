package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.LongFunction;

/**
 * Enables the providing of a {@link LongFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface LongFunctionChecked<R, E extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param value the function argument
   * @return the function result
   */
  @NotNull
  R apply(long value) throws E;
}
