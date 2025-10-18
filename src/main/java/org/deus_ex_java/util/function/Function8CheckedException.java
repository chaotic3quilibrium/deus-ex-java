package org.deus_ex_java.util.function;

/**
 * Enables the providing of a {@link Function8} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface Function8CheckedException<A, B, C, D, E, F, G, H, R> extends Function8Checked<A, B, C, D, E, F, G, H, R, Exception> {

}
