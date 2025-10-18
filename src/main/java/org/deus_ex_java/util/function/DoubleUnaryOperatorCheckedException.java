package org.deus_ex_java.util.function;

import java.util.function.DoubleUnaryOperator;

/**
 * Enables the providing of a {@link DoubleUnaryOperator} Lambda function which can throw a checked exception,
 * explicitly specifying {@link Exception}.
 */
@FunctionalInterface
public interface DoubleUnaryOperatorCheckedException extends DoubleUnaryOperatorChecked<Exception> {

}
