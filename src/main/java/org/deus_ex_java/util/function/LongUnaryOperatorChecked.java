package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.function.LongUnaryOperator;

/**
 * Enables the providing of a {@link LongUnaryOperator} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface LongUnaryOperatorChecked<EX extends Exception> {

  /**
   * Applies this operator to the given operand.
   *
   * @param operand the operand
   * @return the operator result
   */
  long applyAsLong(long operand) throws EX;

  /**
   * Returns a composed operator that first applies the {@code before} operator to its input, and then applies this
   * operator to the result. If evaluation of either operator throws an exception, it is relayed to the caller of the
   * composed operator.
   *
   * @param before the operator to apply before this operator is applied
   * @return a composed operator that first applies the {@code before} operator and then applies this operator
   * @see #andThen(LongUnaryOperatorChecked)
   */
  @NotNull
  default LongUnaryOperatorChecked<EX> compose(
      @NotNull LongUnaryOperatorChecked<? extends EX> before
  ) {
    return (long v) ->
        applyAsLong(before.applyAsLong(v));
  }

  /**
   * Returns a composed operator that first applies this operator to its input, and then applies the {@code after}
   * operator to the result. If evaluation of either operator throws an exception, it is relayed to the caller of the
   * composed operator.
   *
   * @param after the operator to apply after this operator is applied
   * @return a composed operator that first applies this operator and then applies the {@code after} operator
   * @see #compose(LongUnaryOperatorChecked)
   */
  @NotNull
  default LongUnaryOperatorChecked<EX> andThen(
      @NotNull LongUnaryOperatorChecked<? extends EX> after
  ) {
    return (long t) ->
        after.applyAsLong(applyAsLong(t));
  }

  /**
   * Returns a unary operator that always returns its input argument.
   *
   * @return a unary operator that always returns its input argument
   */
  static <EX extends Exception> LongUnaryOperatorChecked<EX> identity() {
    return t ->
        t;
  }
}
