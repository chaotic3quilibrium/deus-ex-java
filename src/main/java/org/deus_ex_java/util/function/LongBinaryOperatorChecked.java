package org.deus_ex_java.util.function;

import java.util.function.LongBinaryOperator;

/**
 * Enables the providing of a {@link LongBinaryOperator} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface LongBinaryOperatorChecked<EX extends Exception> {

  /**
   * Applies this operator to the given operands.
   *
   * @param left  the first operand
   * @param right the second operand
   * @return the operator result
   */
  long applyAsLong(long left, long right) throws EX;
}
