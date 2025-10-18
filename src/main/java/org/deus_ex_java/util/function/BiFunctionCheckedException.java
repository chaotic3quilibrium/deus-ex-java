package org.deus_ex_java.util.function;

import java.util.function.BiFunction;

/**
 * Enables the providing of a {@link BiFunction} Lambda function which can throw a checked exception, explicitly specifying
 * {@link Exception}.
 */
@FunctionalInterface
public interface BiFunctionCheckedException<T, R, U> extends BiFunctionChecked<T, R, U, Exception> {

}
