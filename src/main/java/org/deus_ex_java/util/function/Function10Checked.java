package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple10;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Enables the providing of a {@link Function10} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface Function10Checked<A, B, C, D, E, F, G, H, I, J, R, EX extends Exception> {

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
   * @param j the tenth function argument
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
      @NotNull I i,
      @NotNull J j) throws EX;

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
  default <V> Function10Checked<A, B, C, D, E, F, G, H, I, J, V, EX> andThen(
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
        @NotNull H h,
        @NotNull I i,
        @NotNull J j) ->
        after.apply(apply(a, b, c, d, e, f, g, h, i, j));
  }

  /**
   * Return a {@link Function10Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   * accepts a {@link Tuple10}.
   *
   * @param function target function instance to wrap
   * @return a {@link Function10Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   *     accepts a {@link Tuple10}
   */
  @NotNull
  default Function10Checked<A, B, C, D, E, F, G, H, I, J, R, EX> untupled(
      @NotNull Function<
          Tuple10<
              ? super A,
              ? super B,
              ? super C,
              ? super D,
              ? super E,
              ? super F,
              ? super G,
              ? super H,
              ? super I,
              ? super J>,
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
        @NotNull I i,
        @NotNull J j) ->
        function.apply(
            new Tuple10<>(a, b, c, d, e, f, g, h, i, j));
  }

  /**
   * Return a {@link FunctionChecked} accepting a {@link Tuple10} of the original input parameters.
   *
   * @return a {@link FunctionChecked} accepting a {@link Tuple10} of the original input parameters
   */
  @NotNull
  default FunctionChecked<Tuple10<A, B, C, D, E, F, G, H, I, J>, R, EX> tupled() {
    return (@NotNull Tuple10<A, B, C, D, E, F, G, H, I, J> tuple10) ->
        apply(
            tuple10._1(),
            tuple10._2(),
            tuple10._3(),
            tuple10._4(),
            tuple10._5(),
            tuple10._6(),
            tuple10._7(),
            tuple10._8(),
            tuple10._9(),
            tuple10._10());
  }
}
