package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple9;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Represents a function that accepts nine arguments, and produces a result.
 * <p>
 * This is the nine-arity specialization of {@link Function}.
 *
 * @param <A>  the type of the first argument to the function
 * @param <B>  the type of the second argument to the function
 * @param <C>  the type of the third argument to the function
 * @param <D>  the type of the fourth argument to the function
 * @param <EX> the type of the fifth argument to the function
 * @param <F>  the type of the sixth argument to the function
 * @param <G>  the type of the seventh argument to the function
 * @param <H>  the type of the eighth argument to the function
 * @param <I>  the type of the ninth argument to the function
 * @param <R>  the type of the result of the function
 */
@FunctionalInterface
public interface Function9<A, B, C, D, E, F, G, H, I, R> {

  /**
   * Returns the result of applying this function to the provided arguments.
   *
   * @param a the first function argument
   * @param b the second function argument
   * @param c the third function argument
   * @param d the fourth function argument
   * @param e the fifth function argument
   * @param f the sixth function argument
   * @param g the seventh function argument
   * @param h the eighth function argument
   * @param i the ninth function argument
   * @return the result of applying this function to the provided arguments
   */
  @NotNull R apply(
      @NotNull A a,
      @NotNull B b,
      @NotNull C c,
      @NotNull D d,
      @NotNull E e,
      @NotNull F f,
      @NotNull G g,
      @NotNull H h,
      @NotNull I i);

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
  default <V> Function9<A, B, C, D, E, F, G, H, I, V> andThen(
      @NotNull Function<? super R, ? extends V> after
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c,
        @NotNull D d,
        @NotNull E e,
        @NotNull F f,
        @NotNull G g,
        @NotNull H h,
        @NotNull I i) ->
        after.apply(apply(a, b, c, d, e, f, g, h, i));
  }

  /**
   * Return a {@link Function9} where the input parameters are extracted from a {@link Function} which accepts a
   * {@link Tuple9}.
   *
   * @param function target function instance to wrap
   * @return a {@link Function9} where the input parameters are extracted from a {@link Function} which accepts a
   *     {@link Tuple9}
   */
  @NotNull
  default Function9<A, B, C, D, E, F, G, H, I, R> untupled(
      @NotNull Function<
          Tuple9<
              ? super A,
              ? super B,
              ? super C,
              ? super D,
              ? super E,
              ? super F,
              ? super G,
              ? super H,
              ? super I>,
          ? extends R
          > function
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c,
        @NotNull D d,
        @NotNull E e,
        @NotNull F f,
        @NotNull G g,
        @NotNull H h,
        @NotNull I i) ->
        function.apply(
            new Tuple9<>(a, b, c, d, e, f, g, h, i));
  }

  /**
   * Return a {@link Function} accepting a {@link Tuple9} of the original input parameters.
   *
   * @return a {@link Function} accepting a {@link Tuple9} of the original input parameters
   */
  @NotNull
  default Function<Tuple9<A, B, C, D, E, F, G, H, I>, R> tupled() {
    return (@NotNull Tuple9<A, B, C, D, E, F, G, H, I> tuple9) ->
        apply(
            tuple9._1(),
            tuple9._2(),
            tuple9._3(),
            tuple9._4(),
            tuple9._5(),
            tuple9._6(),
            tuple9._7(),
            tuple9._8(),
            tuple9._9());
  }
}
