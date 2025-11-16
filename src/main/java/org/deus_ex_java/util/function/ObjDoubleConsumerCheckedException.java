package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.ObjDoubleConsumer;

/**
 * Enables the providing of a {@link ObjDoubleConsumer} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
@NullMarked
public interface ObjDoubleConsumerCheckedException<T> extends ObjDoubleConsumerChecked<T, Exception> {

}
