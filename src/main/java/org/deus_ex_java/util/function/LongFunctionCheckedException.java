package org.deus_ex_java.util.function;

import java.util.function.LongFunction;

/**
 * Enables the providing of a {@link LongFunction} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface LongFunctionCheckedException<R> extends LongFunctionChecked<R, Exception> {

}
