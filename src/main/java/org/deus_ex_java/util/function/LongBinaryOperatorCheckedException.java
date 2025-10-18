package org.deus_ex_java.util.function;

import java.util.function.LongBinaryOperator;

/**
 * Enables the providing of a {@link  LongBinaryOperator} Lambda function which can throw a checked exception,
 * explicitly specifying {@link Exception}.
 */
@FunctionalInterface
public interface LongBinaryOperatorCheckedException extends LongBinaryOperatorChecked<Exception> {

}
