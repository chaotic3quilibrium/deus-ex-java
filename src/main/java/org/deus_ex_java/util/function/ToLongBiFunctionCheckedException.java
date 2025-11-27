package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.ToLongBiFunction;

/**
 * Enables the providing of a {@link ToLongBiFunction} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
@NullMarked
public interface ToLongBiFunctionCheckedException<T, U> extends ToLongBiFunctionChecked<T, U, Exception> {

}
