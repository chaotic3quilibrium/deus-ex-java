package org.deus_ex_java.util.function;

import java.util.function.IntFunction;

/**
 * Enables the providing of a {@link IntFunction} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface IntFunctionCheckedException<R> extends IntFunctionChecked<R, Exception> {

}
