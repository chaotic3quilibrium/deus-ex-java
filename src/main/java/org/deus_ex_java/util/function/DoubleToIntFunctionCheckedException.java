package org.deus_ex_java.util.function;

import java.util.function.DoubleToIntFunction;

/**
 * Enables the providing of a {@link DoubleToIntFunction} Lambda function which can throw a checked exception,
 * explicitly specifying {@link Exception}.
 */
@FunctionalInterface
public interface DoubleToIntFunctionCheckedException extends DoubleToIntFunctionChecked<Exception> {

}
