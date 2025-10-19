package org.deus_ex_java.util.function;

import java.util.Objects;
import java.util.function.Function;

/**
 * Enables the providing of a {@link Function} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface FunctionChecked<T, R, EX extends Exception> {

  /**
   * Applies this function to the given argument.
   *
   * @param t the function argument
   * @return the function result
   */
  R apply(T t) throws EX;

  /**
   * Returns a composed function that first applies the {@code before} function to its input, and then applies this
   * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <V>    the type of input to the {@code before} function, and to the composed function
   * @param before the function to apply before this function is applied
   * @return a composed function that first applies the {@code before} function and then applies this function
   * @throws NullPointerException if before is null
   * @see #andThen(FunctionChecked)
   */
  default <V> FunctionChecked<V, R, EX> compose(FunctionChecked<? super V, ? extends T, ? extends EX> before) {
    Objects.requireNonNull(before);

    return (V v) ->
        apply(before.apply(v));
  }

  /**
   * Returns a composed function that first applies this function to its input, and then applies the {@code after}
   * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <V>   the type of output of the {@code after} function, and of the composed function
   * @param after the function to apply after this function is applied
   * @return a composed function that first applies this function and then applies the {@code after} function
   * @throws NullPointerException if after is null
   * @see #compose(FunctionChecked)
   */
  default <V> FunctionChecked<T, V, EX> andThen(FunctionChecked<? super R, ? extends V, ? extends EX> after) {
    Objects.requireNonNull(after);

    return (T t) ->
        after.apply(apply(t));
  }

  /**
   * Returns a function that always returns its input argument.
   *
   * @param <T> the type of the input and output objects to the function
   * @return a function that always returns its input argument
   */
  static <T, EX extends Exception> FunctionChecked<T, T, EX> identity() {
    return t ->
        t;
  }
}
