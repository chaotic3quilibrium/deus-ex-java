package org.deus_ex_java.util.function;

import java.util.Objects;
import java.util.function.BiPredicate;

/**
 * Enables the providing of a {@link BiPredicate} Lambda function which can throw a checked Exception.
 */
@FunctionalInterface
public interface BiPredicateChecked<T, U, E extends Exception> {

  /**
   * Evaluates this predicate on the given arguments.
   *
   * @param t the first input argument
   * @param u the second input argument
   * @return {@code true} if the input arguments match the predicate, otherwise {@code false}
   */
  boolean test(T t, U u) throws E;

  /**
   * Returns a composed predicate that represents a short-circuiting logical AND of this predicate and another.  When
   * evaluating the composed predicate, if this predicate is {@code false}, then the {@code other} predicate is not
   * evaluated.
   *
   * <p>Any exceptions thrown during evaluation of either predicate are relayed
   * to the caller; if evaluation of this predicate throws an exception, the {@code other} predicate will not be
   * evaluated.
   *
   * @param other a predicate that will be logically-ANDed with this predicate
   * @return a composed predicate that represents the short-circuiting logical AND of this predicate and the
   *     {@code other} predicate
   * @throws NullPointerException if other is null
   */
  default BiPredicateChecked<T, U, E> and(BiPredicateChecked<? super T, ? super U, ? extends E> other) {
    Objects.requireNonNull(other);
    return (T t, U u) -> test(t, u) && other.test(t, u);
  }

  /**
   * Returns a predicate that represents the logical negation of this predicate.
   *
   * @return a predicate that represents the logical negation of this predicate
   */
  default BiPredicateChecked<T, U, E> negate() {
    return (T t, U u) -> !test(t, u);
  }

  /**
   * Returns a composed predicate that represents a short-circuiting logical OR of this predicate and another.  When
   * evaluating the composed predicate, if this predicate is {@code true}, then the {@code other} predicate is not
   * evaluated.
   *
   * <p>Any exceptions thrown during evaluation of either predicate are relayed
   * to the caller; if evaluation of this predicate throws an exception, the {@code other} predicate will not be
   * evaluated.
   *
   * @param other a predicate that will be logically-ORed with this predicate
   * @return a composed predicate that represents the short-circuiting logical OR of this predicate and the
   *     {@code other} predicate
   * @throws NullPointerException if other is null
   */
  default BiPredicateChecked<T, U, E> or(BiPredicateChecked<? super T, ? super U, ? extends E> other) {
    Objects.requireNonNull(other);
    return (T t, U u) -> test(t, u) || other.test(t, u);
  }
}
