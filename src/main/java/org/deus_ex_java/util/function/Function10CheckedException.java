package org.deus_ex_java.util.function;

/**
 * Enables the providing of a {@link Function10} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface Function10CheckedException<A, B, C, D, E, F, G, H, I, J, R> extends Function10Checked<A, B, C, D, E, F, G, H, I, J, R, Exception> {

}
