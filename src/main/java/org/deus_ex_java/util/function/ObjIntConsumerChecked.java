package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.ObjIntConsumer;

/**
 * Enables the providing of a {@link ObjIntConsumer} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface ObjIntConsumerChecked<T, E extends Exception> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t     the first input argument
   * @param value the second input argument
   */
  void accept(@NotNull T t, int value) throws E;
}
