package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.BinaryOperator;

/**
 * Enables the providing of a {@link BinaryOperator} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
@NullMarked
public interface BinaryOperatorCheckedException<T> extends BinaryOperatorChecked<T, Exception> {

}
