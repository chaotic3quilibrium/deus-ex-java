package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents a function that accepts four arguments, and produces a result.
 * <p>
 * This is the four-arity specialization of {@link Function}.
 *
 * @param <A> the type of the first argument to the function
 * @param <B> the type of the second argument to the function
 * @param <C> the type of the third argument to the function
 * @param <D> the type of the fourth argument to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface Function4<A, B, C, D, R> {

  /**
   * Returns the result of applying this function to the provided arguments.
   *
   * @param a the first function argument
   * @param b the second function argument
   * @param c the third function argument
   * @param d the fourth function argument
   * @return the result of applying this function to the provided arguments
   */
  @NotNull R apply(
      @NotNull A a,
      @NotNull B b,
      @NotNull C c,
      @NotNull D d);

  /**
   * Returns a composed function that first applies this function to its input, and then applies the {@code after}
   * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <V>   the type of output of the {@code after} function, and of the composed function
   * @param after the function to apply after this function is applied
   * @return a composed function that first applies this function and then applies the {@code after} function
   */
  @NotNull
  default <V> Function4<A, B, C, D, V> andThen(
      @NotNull Function<? super R, ? extends V> after
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c,
        @NotNull D d) ->
        after.apply(apply(a, b, c, d));
  }

  /**
   * Return a {@link Function4} where the input parameters are extracted from a {@link Function} which accepts a
   * {@link Tuple4}.
   *
   * @param function target function instance to wrap
   * @return a {@link Function4} where the input parameters are extracted from a {@link Function} which accepts a
   *     {@link Tuple4}
   */
  @NotNull
  default Function4<A, B, C, D, R> untupled(
      @NotNull Function<
          Tuple4<
              ? super A,
              ? super B,
              ? super C,
              ? super D>,
          ? extends R
          > function
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c,
        @NotNull D d) ->
        function.apply(
            new Tuple4<>(a, b, c, d));
  }

  /**
   * Return a {@link Function} accepting a {@link Tuple4} of the original input parameters.
   *
   * @return a {@link Function} accepting a {@link Tuple4} of the original input parameters
   */
  @NotNull
  default Function<Tuple4<A, B, C, D>, R> tupled() {
    return (@NotNull Tuple4<A, B, C, D> tuple4) ->
        apply(
            tuple4._1(),
            tuple4._2(),
            tuple4._3(),
            tuple4._4());
  }
}
