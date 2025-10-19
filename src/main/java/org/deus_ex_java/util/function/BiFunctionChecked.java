package org.deus_ex_java.util.function;

import java.util.Objects;
import java.util.function.BiFunction;

/**
 * Enables the providing of a {@link BiFunction} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface BiFunctionChecked<T, U, R, EX extends Exception> {

  /**
   * Applies this function to the given arguments.
   *
   * @param t the first function argument
   * @param u the second function argument
   * @return the function result
   */
  R apply(T t, U u) throws EX;

  /**
   * Returns a composed function that first applies this function to its input, and then applies the {@code after}
   * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <V>   the type of output of the {@code after} function, and of the composed function
   * @param after the function to apply after this function is applied
   * @return a composed function that first applies this function and then applies the {@code after} function
   * @throws NullPointerException if after is null
   */
  default <V> BiFunctionChecked<T, U, V, EX> andThen(FunctionChecked<? super R, ? extends V, ? extends EX> after) {
    Objects.requireNonNull(after);
    return (T t, U u) ->
        after.apply(apply(t, u));
  }
}
