package org.deus_ex_java.util.function;

import java.util.function.UnaryOperator;

/**
 * Enables the providing of a {@link UnaryOperator} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface UnaryOperatorChecked<T, EX extends Exception> extends FunctionChecked<T, T, EX> {

  /**
   * Returns a unary operator that always returns its input argument.
   *
   * @param <T> the type of the input and output of the operator
   * @return a unary operator that always returns its input argument
   */
  static <T, EX extends Exception> UnaryOperatorChecked<T, EX> identity() {
    return t ->
        t;
  }
}
