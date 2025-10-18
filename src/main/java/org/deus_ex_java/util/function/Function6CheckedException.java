package org.deus_ex_java.util.function;

/**
 * Enables the providing of a {@link Function6} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface Function6CheckedException<A, B, C, D, E, F, R> extends Function6Checked<A, B, C, D, E, F, R, Exception> {

}
