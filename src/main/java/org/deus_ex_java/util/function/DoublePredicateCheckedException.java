package org.deus_ex_java.util.function;

import java.util.function.DoublePredicate;

/**
 * Enables the providing of a {@link DoublePredicate} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface DoublePredicateCheckedException extends DoublePredicateChecked<Exception> {

}

