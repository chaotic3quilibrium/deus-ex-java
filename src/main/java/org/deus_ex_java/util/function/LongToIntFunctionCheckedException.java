package org.deus_ex_java.util.function;

import java.util.function.LongToIntFunction;

/**
 * Enables the providing of a {@link LongToIntFunction} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface LongToIntFunctionCheckedException extends LongToIntFunctionChecked<Exception> {

}

