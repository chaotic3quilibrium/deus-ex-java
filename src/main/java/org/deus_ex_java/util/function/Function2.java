package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a function that accepts two arguments, and produces a result.
 * <p>
 * This is the two-arity specialization of {@link Function}.
 *
 * @param <A> the type of the first argument to the function
 * @param <B> the type of the second argument to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface Function2<A, B, R> {

  /**
   * Return a {@link Function2} wrapper around a {@link BiFunction} instance.
   *
   * @param biFunction the target function instance to wrap
   * @param <A>        the type of the first argument to the function
   * @param <B>        the type of the second argument to the function
   * @param <R>        the type of the result of the function
   * @return a {@link Function2} wrapper around a {@link BiFunction} instance
   * @see FunctionsOps#to(Function2) FunctionsOps.to(Function2) for the inverted version of this method
   */
  @NotNull
  static <A, B, R> Function2<A, B, R> of(
      @NotNull BiFunction<? super A, ? super B, ? extends R> biFunction
  ) {
    return biFunction::apply;
  }

  /**
   * Returns the result of applying this function to the provided arguments.
   *
   * @param a the first function argument
   * @param b the second function argument
   * @return the result of applying this function to the provided arguments
   */
  @NotNull R apply(
      @NotNull A a,
      @NotNull B b);

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
  default <V> Function2<A, B, V> andThen(
      @NotNull Function<? super R, ? extends V> after
  ) {
    return (A a, B b) ->
        after.apply(apply(a, b));
  }

  /**
   * Return a {@link Function2} where the input parameters are extracted from a {@link Function} which accepts a
   * {@link Tuple2}.
   *
   * @param function1 target function instance to wrap
   * @return a {@link Function2} where the input parameters are extracted from a {@link Function} which accepts a
   * {@link Tuple2}
   */
  @NotNull
  default Function2<A, B, R> untupled(
      @NotNull Function<
          Tuple2<
              ? super A,
              ? super B>,
          ? extends R
          > function1
  ) {
    return (A a, B b) ->
        function1.apply(
            new Tuple2<>(a, b));
  }

  /**
   * Return a {@link Function} accepting a {@link Tuple2} of the original input parameters.
   *
   * @return a {@link Function} accepting a {@link Tuple2} of the original input parameters
   */
  @NotNull
  default Function<Tuple2<A, B>, R> tupled() {
    return (tuple2) ->
        apply(
            tuple2._1(),
            tuple2._2());
  }
}
