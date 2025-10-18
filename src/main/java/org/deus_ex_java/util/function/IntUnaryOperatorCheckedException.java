package org.deus_ex_java.util.function;

import java.util.function.IntUnaryOperator;

/**
 * Enables the providing of a {@link IntUnaryOperator} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface IntUnaryOperatorCheckedException extends IntUnaryOperatorChecked<Exception> {

}
