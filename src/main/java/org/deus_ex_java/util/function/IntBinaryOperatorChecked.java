package org.deus_ex_java.util.function;

import java.util.function.IntBinaryOperator;

/**
 * Enables the providing of a {@link IntBinaryOperator} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface IntBinaryOperatorChecked<EX extends Exception> {
  /**
   * Applies this operator to the given operands.
   *
   * @param left  the first operand
   * @param right the second operand
   * @return the operator result
   */
  int applyAsInt(int left, int right) throws EX;
}
