package org.deus_ex_java.util.function;

import java.util.function.ToDoubleBiFunction;

/**
 * Enables the providing of a {@link ToDoubleBiFunction} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface ToDoubleBiFunctionCheckedException<T, U> extends ToDoubleBiFunctionChecked<T, U, Exception> {

}
