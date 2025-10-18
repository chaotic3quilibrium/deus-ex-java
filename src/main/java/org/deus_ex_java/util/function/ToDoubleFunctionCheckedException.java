package org.deus_ex_java.util.function;

import java.util.function.ToDoubleFunction;

/**
 * Enables the providing of a {@link ToDoubleFunction} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface ToDoubleFunctionCheckedException<T> extends ToDoubleFunctionChecked<T, Exception> {

}
