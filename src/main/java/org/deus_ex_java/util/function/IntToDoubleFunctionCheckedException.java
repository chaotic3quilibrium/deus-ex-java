package org.deus_ex_java.util.function;

import java.util.function.IntToDoubleFunction;

/**
 * Enables the providing of a {@link IntToDoubleFunction} Lambda function which can throw a checked exception,
 * explicitly specifying {@link Exception}.
 */
@FunctionalInterface
public interface IntToDoubleFunctionCheckedException extends IntToDoubleFunctionChecked<Exception> {

}
