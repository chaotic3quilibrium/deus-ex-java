package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple8;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Enables the providing of a {@link Function8} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface Function8Checked<A, B, C, D, E, F, G, H, R, EX extends Exception> {

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
      @NotNull H h) throws EX;

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
  default <V> Function8Checked<A, B, C, D, E, F, G, H, V, EX> andThen(
      @NotNull FunctionChecked<? super R, ? extends V, ? extends EX> after
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c,
        @NotNull D d,
        @NotNull E e,
        @NotNull F f,
        @NotNull G g,
        @NotNull H h) ->
        after.apply(apply(a, b, c, d, e, f, g, h));
  }

  /**
   * Return a {@link Function8Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   * accepts a {@link Tuple8}.
   *
   * @param function target function instance to wrap
   * @return a {@link Function8Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   *     accepts a {@link Tuple8}
   */
  @NotNull
  default Function8Checked<A, B, C, D, E, F, G, H, R, EX> untupled(
      @NotNull Function<
          Tuple8<
              ? super A,
              ? super B,
              ? super C,
              ? super D,
              ? super E,
              ? super F,
              ? super G,
              ? super H>,
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
        @NotNull H h) ->
        function.apply(
            new Tuple8<>(a, b, c, d, e, f, g, h));
  }

  /**
   * Return a {@link FunctionChecked} accepting a {@link Tuple8} of the original input parameters.
   *
   * @return a {@link FunctionChecked} accepting a {@link Tuple8} of the original input parameters
   */
  @NotNull
  default FunctionChecked<Tuple8<A, B, C, D, E, F, G, H>, R, EX> tupled() {
    return (@NotNull Tuple8<A, B, C, D, E, F, G, H> tuple8) ->
        apply(
            tuple8._1(),
            tuple8._2(),
            tuple8._3(),
            tuple8._4(),
            tuple8._5(),
            tuple8._6(),
            tuple8._7(),
            tuple8._8());
  }
}
