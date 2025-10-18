package org.deus_ex_java.util.function;

/**
 * Enables the providing of a {@link Function3} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface Function3CheckedException<A, B, C, R> extends Function3Checked<A, B, C, R, Exception> {

}
