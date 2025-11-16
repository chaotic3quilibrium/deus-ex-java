package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.Predicate;

/**
 * Enables the providing of a {@link Predicate} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
@NullMarked
public interface PredicateCheckedException<T> extends PredicateChecked<T, Exception> {

}
