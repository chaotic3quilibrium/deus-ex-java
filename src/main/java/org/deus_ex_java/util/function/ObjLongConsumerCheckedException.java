package org.deus_ex_java.util.function;

import java.util.function.ObjLongConsumer;

/**
 * Enables the providing of a {@link ObjLongConsumer} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface ObjLongConsumerCheckedException<T> extends ObjLongConsumerChecked<T, Exception> {

}
