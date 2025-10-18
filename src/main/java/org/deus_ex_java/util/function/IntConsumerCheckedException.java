package org.deus_ex_java.util.function;

import java.util.function.IntConsumer;

/**
 * Enables the providing of a {@link IntConsumer} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface IntConsumerCheckedException extends IntConsumerChecked<Exception> {

}
