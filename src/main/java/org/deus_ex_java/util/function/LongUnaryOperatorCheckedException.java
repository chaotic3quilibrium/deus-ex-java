package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.LongUnaryOperator;

/**
 * Enables the providing of a {@link LongUnaryOperator} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
@NullMarked
public interface LongUnaryOperatorCheckedException extends LongUnaryOperatorChecked<Exception> {

}
