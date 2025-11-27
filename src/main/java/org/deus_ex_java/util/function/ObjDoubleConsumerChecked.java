package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.ObjDoubleConsumer;

/**
 * Enables the providing of a {@link ObjDoubleConsumer} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface ObjDoubleConsumerChecked<T, EX extends Exception> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t     the first input argument
   * @param value the second input argument
   */
  void accept(T t, double value) throws EX;
}
