package org.deus_ex_java.util.function;

import java.util.function.DoubleFunction;

/**
 * Enables the providing of a {@link DoubleFunction} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface DoubleFunctionCheckedException<R> extends DoubleFunctionChecked<R, Exception> {

}
