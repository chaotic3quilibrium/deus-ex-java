package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.IntUnaryOperator;

/**
 * Enables the providing of a {@link IntUnaryOperator} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface IntUnaryOperatorChecked<E extends Exception> {

  /**
   * Applies this operator to the given operand.
   *
   * @param operand the operand
   * @return the operator result
   */
  int applyAsInt(int operand) throws E;

  /**
   * Returns a composed operator that first applies the {@code before} operator to its input, and then applies this
   * operator to the result. If evaluation of either operator throws an exception, it is relayed to the caller of the
   * composed operator.
   *
   * @param before the operator to apply before this operator is applied
   * @return a composed operator that first applies the {@code before} operator and then applies this operator
   * @see #andThen(IntUnaryOperatorChecked)
   */
  @NotNull
  default IntUnaryOperatorChecked<E> compose(
      @NotNull IntUnaryOperatorChecked<? extends E> before
  ) {
    return (int v) ->
        applyAsInt(before.applyAsInt(v));
  }

  /**
   * Returns a composed operator that first applies this operator to its input, and then applies the {@code after}
   * operator to the result. If evaluation of either operator throws an exception, it is relayed to the caller of the
   * composed operator.
   *
   * @param after the operator to apply after this operator is applied
   * @return a composed operator that first applies this operator and then applies the {@code after} operator
   * @see #compose(IntUnaryOperatorChecked)
   */
  @NotNull
  default IntUnaryOperatorChecked<E> andThen(
      @NotNull IntUnaryOperatorChecked<? extends E> after
  ) {
    return (int t) ->
        after.applyAsInt(applyAsInt(t));
  }

  /**
   * Returns a unary operator that always returns its input argument.
   *
   * @return a unary operator that always returns its input argument
   */
  static <E extends Exception> IntUnaryOperatorChecked<E> identity() {
    return t ->
        t;
  }
}
