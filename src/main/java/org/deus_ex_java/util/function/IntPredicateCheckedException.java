package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.IntPredicate;

/**
 * Enables the providing of a {@link IntPredicate} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
@NullMarked
public interface IntPredicateCheckedException extends IntPredicateChecked<Exception> {

}
