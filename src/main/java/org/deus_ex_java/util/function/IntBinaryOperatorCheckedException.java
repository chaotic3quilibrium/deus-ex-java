package org.deus_ex_java.util.function;

import java.util.function.IntBinaryOperator;

/**
 * Enables the providing of a {@link IntBinaryOperator} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface IntBinaryOperatorCheckedException extends IntBinaryOperatorChecked<Exception> {

}
