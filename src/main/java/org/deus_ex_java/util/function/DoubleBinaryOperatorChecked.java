package org.deus_ex_java.util.function;

import java.util.function.DoubleBinaryOperator;

/**
 * Enables the providing of a {@link DoubleBinaryOperator} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface DoubleBinaryOperatorChecked<E extends Exception> {
  /**
   * Applies this operator to the given operands.
   *
   * @param left  the first operand
   * @param right the second operand
   * @return the operator result
   */
  double applyAsDouble(double left, double right) throws E;
}
