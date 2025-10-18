package org.deus_ex_java.util.function;

/**
 * Enables the providing of a {@link Function4} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface Function4CheckedException<A, B, C, D, R> extends Function4Checked<A, B, C, D, R, Exception> {

}
