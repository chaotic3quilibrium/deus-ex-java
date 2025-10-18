package org.deus_ex_java.util.function;

/**
 * Enables the providing of a {@link Function9} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface Function9CheckedException<A, B, C, D, E, F, G, H, I, R> extends Function9Checked<A, B, C, D, E, F, G, H, I, R, Exception> {

}
