package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.DoubleUnaryOperator;

/**
 * Enables the providing of a {@link DoubleUnaryOperator} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface DoubleUnaryOperatorChecked<EX extends Exception> {
  /**
   * Applies this operator to the given operand.
   *
   * @param operand the operand
   * @return the operator result
   */
  double applyAsDouble(double operand) throws EX;

  /**
   * Returns a composed operator that first applies the {@code before} operator to its input, and then applies this
   * operator to the result. If evaluation of either operator throws an exception, it is relayed to the caller of the
   * composed operator.
   *
   * @param before the operator to apply before this operator is applied
   * @return a composed operator that first applies the {@code before} operator and then applies this operator
   * @see #andThen(DoubleUnaryOperatorChecked)
   */
  default DoubleUnaryOperatorChecked<EX> compose(
      DoubleUnaryOperatorChecked<? extends EX> before
  ) {
    return (double v) ->
        applyAsDouble(before.applyAsDouble(v));
  }

  /**
   * Returns a composed operator that first applies this operator to its input, and then applies the {@code after}
   * operator to the result. If evaluation of either operator throws an exception, it is relayed to the caller of the
   * composed operator.
   *
   * @param after the operator to apply after this operator is applied
   * @return a composed operator that first applies this operator and then applies the {@code after} operator
   * @see #compose(DoubleUnaryOperatorChecked)
   */
  default DoubleUnaryOperatorChecked<EX> andThen(
      DoubleUnaryOperatorChecked<? extends EX> after
  ) {
    return (double t) ->
        after.applyAsDouble(applyAsDouble(t));
  }

  /**
   * Returns a unary operator that always returns its input argument.
   *
   * @return a unary operator that always returns its input argument
   */
  static <EX extends Exception> DoubleUnaryOperatorChecked<EX> identity() {
    return t ->
        t;
  }
}
