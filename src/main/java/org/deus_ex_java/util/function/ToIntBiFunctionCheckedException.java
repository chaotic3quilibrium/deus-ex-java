package org.deus_ex_java.util.function;

import java.util.function.ToIntBiFunction;

/**
 * Enables the providing of a {@link ToIntBiFunction} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface ToIntBiFunctionCheckedException<T, U> extends ToIntBiFunctionChecked<T, U, Exception> {

}
