package org.deus_ex_java.util.function;

import java.util.function.BiConsumer;

/**
 * Enables the providing of a {@link BiConsumer} Lambda function which can throw a checked exception, explicitly specifying an
 * {@link Exception}.
 */
@FunctionalInterface
public interface BiConsumerCheckedException<T, U> extends BiConsumerChecked<T, U, Exception> {

}
