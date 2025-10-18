package org.deus_ex_java.util.function;

import java.util.function.Consumer;

/**
 * Enables the providing of a {@link Consumer} Lambda function which can throw a checked exception, explicitly specifying
 * {@link Exception}.
 */
public interface ConsumerCheckedException<T> extends ConsumerChecked<T, Exception> {

}
