package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple3;
import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

/**
 * Represents a function that accepts four arguments, and produces a result.
 * <p>
 * This is the four-arity specialization of {@link Function}.
 *
 * @param <A> the type of the first argument to the function
 * @param <B> the type of the second argument to the function
 * @param <C> the type of the third argument to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
@NullMarked
public interface Function3<A, B, C, R> {

  /**
   * Returns the result of applying this function to the provided arguments.
   *
   * @param a the first function argument
   * @param b the second function argument
   * @param c the third function argument
   * @return the result of applying this function to the provided arguments
   */
  R apply(
      A a,
      B b,
      C c);

  /**
   * Returns a composed function that first applies this function to its input, and then applies the {@code after}
   * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <V>   the type of output of the {@code after} function, and of the composed function
   * @param after the function to apply after this function is applied
   * @return a composed function that first applies this function and then applies the {@code after} function
   */
  default <V> Function3<A, B, C, V> andThen(
      Function<? super R, ? extends V> after
  ) {
    return (
        A a,
        B b,
        C c) ->
        after.apply(apply(a, b, c));
  }

  /**
   * Return a {@link Function3} where the input parameters are extracted from a {@link Function} which accepts a
   * {@link Tuple3}.
   *
   * @param function target function instance to wrap
   * @return a {@link Function3} where the input parameters are extracted from a {@link Function} which accepts a
   *     {@link Tuple3}
   */
  default Function3<A, B, C, R> untupled(
      Function<
          Tuple3<
              ? super A,
              ? super B,
              ? super C>,
          ? extends R
          > function
  ) {
    return (
        A a,
        B b,
        C c) ->
        function.apply(
            new Tuple3<>(a, b, c));
  }

  /**
   * Return a {@link Function} accepting a {@link Tuple3} of the original input parameters.
   *
   * @return a {@link Function} accepting a {@link Tuple3} of the original input parameters
   */
  default Function<Tuple3<A, B, C>, R> tupled() {
    return (Tuple3<A, B, C> tuple3) ->
        apply(
            tuple3._1(),
            tuple3._2(),
            tuple3._3());
  }
}
