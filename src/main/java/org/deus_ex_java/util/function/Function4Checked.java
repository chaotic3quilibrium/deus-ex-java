package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple4;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Enables the providing of a {@link Function4} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface Function4Checked<A, B, C, D, R, EX extends Exception> {

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
      @NotNull D d) throws EX;

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
  default <V> Function4Checked<A, B, C, D, V, EX> andThen(
      @NotNull FunctionChecked<? super R, ? extends V, ? extends EX> after
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c,
        @NotNull D d) ->
        after.apply(apply(a, b, c, d));
  }

  /**
   * Return a {@link Function4Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   * accepts a {@link Tuple4}.
   *
   * @param function target function instance to wrap
   * @return a {@link Function4Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   *     accepts a {@link Tuple4}
   */
  @NotNull
  default Function4Checked<A, B, C, D, R, EX> untupled(
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
   * Return a {@link FunctionChecked} accepting a {@link Tuple4} of the original input parameters.
   *
   * @return a {@link FunctionChecked} accepting a {@link Tuple4} of the original input parameters
   */
  @NotNull
  default FunctionChecked<Tuple4<A, B, C, D>, R, EX> tupled() {
    return (@NotNull Tuple4<A, B, C, D> tuple4) ->
        apply(
            tuple4._1(),
            tuple4._2(),
            tuple4._3(),
            tuple4._4());
  }
}
