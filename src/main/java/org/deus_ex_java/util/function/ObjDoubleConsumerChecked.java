package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.ObjDoubleConsumer;

/**
 * Enables the providing of a {@link ObjDoubleConsumer} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface ObjDoubleConsumerChecked<T, E extends Exception> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t     the first input argument
   * @param value the second input argument
   */
  void accept(@NotNull T t, double value) throws E;
}
