package org.deus_ex_java.util.function;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BinaryOperator;

/**
 * Enables the providing of a {@link BinaryOperator} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface BinaryOperatorChecked<T, E extends Exception> extends BiFunctionChecked<T, T, T, E> {

  /**
   * Returns a {@link BinaryOperatorChecked} which returns the lesser of two elements
   * according to the specified {@code Comparator}.
   *
   * @param <T>        the type of the input arguments of the comparator
   * @param comparator a {@code Comparator} for comparing the two values
   * @return a {@code BinaryOperatorChecked} which returns the lesser of its operands,
   * according to the supplied {@code Comparator}
   * @throws NullPointerException if the argument is null
   */
  public static <T, E extends Exception> BinaryOperatorChecked<T, E> minBy(Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator);
    return (a, b) ->
        comparator.compare(a, b) <= 0
            ? a
            : b;
  }

  /**
   * Returns a {@link BinaryOperatorChecked} which returns the greater of two elements
   * according to the specified {@code Comparator}.
   *
   * @param <T>        the type of the input arguments of the comparator
   * @param comparator a {@code Comparator} for comparing the two values
   * @return a {@code BinaryOperatorChecked} which returns the greater of its operands,
   * according to the supplied {@code Comparator}
   * @throws NullPointerException if the argument is null
   */
  public static <T, E extends Exception> BinaryOperatorChecked<T, E> maxBy(Comparator<? super T> comparator) {
    Objects.requireNonNull(comparator);
    return (a, b) ->
        comparator.compare(a, b) >= 0
            ? a
            : b;
  }
}
