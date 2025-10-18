package org.deus_ex_java.util.function;

import java.util.function.UnaryOperator;

/**
 * Enables the providing of a {@link UnaryOperator} Lambda function which can throw a checked exception, explicitly specifying
 * {@link Exception}.
 */
@FunctionalInterface
public interface UnaryOperatorCheckedException<T> extends UnaryOperatorChecked<T, Exception> {

}
