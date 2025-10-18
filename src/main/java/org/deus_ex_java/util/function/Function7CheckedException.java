package org.deus_ex_java.util.function;

/**
 * Enables the providing of a {@link Function7} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface Function7CheckedException<A, B, C, D, E, F, G, R> extends Function7Checked<A, B, C, D, E, F, G, R, Exception> {

}
