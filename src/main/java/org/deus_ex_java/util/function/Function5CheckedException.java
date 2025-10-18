package org.deus_ex_java.util.function;

/**
 * Enables the providing of a {@link Function5} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface Function5CheckedException<A, B, C, D, E, R> extends Function5Checked<A, B, C, D, E, R, Exception> {

}
