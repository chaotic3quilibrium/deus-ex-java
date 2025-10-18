package org.deus_ex_java.util.function;

import java.util.function.ToIntFunction;

/**
 * Enables the providing of a {@link ToIntFunction} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface ToIntFunctionCheckedException<T> extends ToIntFunctionChecked<T, Exception> {

}
