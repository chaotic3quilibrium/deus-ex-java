package org.deus_ex_java.util.function;

import java.util.function.IntToLongFunction;

/**
 * Enables the providing of a {@link IntToLongFunction} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface IntToLongFunctionCheckedException extends IntToLongFunctionChecked<Exception> {

}
