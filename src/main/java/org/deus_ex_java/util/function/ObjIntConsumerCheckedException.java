package org.deus_ex_java.util.function;

import java.util.function.ObjIntConsumer;

/**
 * Enables the providing of a {@link ObjIntConsumer} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface ObjIntConsumerCheckedException<T> extends ObjIntConsumerChecked<T, Exception> {

}
