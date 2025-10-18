package org.deus_ex_java.util.function;

import java.util.function.LongToDoubleFunction;

/**
 * Enables the providing of a {@link LongToDoubleFunction} Lambda function which can throw a checked exception,
 * explicitly specifying {@link Exception}.
 */
@FunctionalInterface
public interface LongToDoubleFunctionCheckedException extends LongToDoubleFunctionChecked<Exception> {

}
