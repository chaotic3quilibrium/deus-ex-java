package org.deus_ex_java.util.function;

import java.util.function.DoubleToLongFunction;

/**
 * Enables the providing of a {@link DoubleToLongFunction} Lambda function which can throw a checked exception,
 * explicitly specifying {@link Exception}.
 */
@FunctionalInterface
public interface DoubleToLongFunctionCheckedException extends DoubleToLongFunctionChecked<Exception> {

}
