package org.deus_ex_java.util.function;

import java.util.function.BiPredicate;

/**
 * Enables the providing of a {@link BiPredicate} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface BiPredicateCheckedException<T, U> extends BiPredicateChecked<T, U, Exception> {

}

