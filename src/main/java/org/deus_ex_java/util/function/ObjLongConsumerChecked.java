package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.ObjLongConsumer;

/**
 * Enables the providing of a {@link ObjLongConsumer} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface ObjLongConsumerChecked<T, EX extends Exception> {

  /**
   * Performs this operation on the given arguments.
   *
   * @param t     the first input argument
   * @param value the second input argument
   */
  void accept(T t, long value) throws EX;
}
