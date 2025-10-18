package org.deus_ex_java.util.function;

import java.util.function.ToLongFunction;

/**
 * Enables the providing of a {@link ToLongFunction} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface ToLongFunctionCheckedException<T> extends ToLongFunctionChecked<T, Exception> {

}

