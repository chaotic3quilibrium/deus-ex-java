package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.ObjIntConsumer;

/**
 * Enables the providing of a {@link ObjIntConsumer} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface ObjIntConsumerChecked<T, EX extends Exception> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t     the first input argument
   * @param value the second input argument
   */
  void accept(T t, int value) throws EX;
}
