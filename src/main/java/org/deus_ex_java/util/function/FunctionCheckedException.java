package org.deus_ex_java.util.function;

import java.util.function.Function;

/**
 * Enables the providing of a {@link Function} Lambda function which can throw a checked exception, explicitly specifying
 * {@link Exception}.
 */
@FunctionalInterface
public interface FunctionCheckedException<T, R> extends FunctionChecked<T, R, Exception> {

}
