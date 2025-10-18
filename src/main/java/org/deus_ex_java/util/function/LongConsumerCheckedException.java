package org.deus_ex_java.util.function;

import java.util.function.LongConsumer;

/**
 * Enables the providing of a {@link LongConsumer} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface LongConsumerCheckedException extends LongConsumerChecked<Exception> {

}

