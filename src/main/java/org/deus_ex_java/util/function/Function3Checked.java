package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple3;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Enables the providing of a {@link Function3} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface Function3Checked<A, B, C, R, EX extends Exception> {

  /**
   * Returns the result of applying this function to the provided arguments.
   *
   * @param a the first function argument
   * @param b the second function argument
   * @param c the third function argument
   * @return the result of applying this function to the provided arguments
   */
  @NotNull R apply(
      @NotNull A a,
      @NotNull B b,
      @NotNull C c) throws EX;

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
  default <V> Function3Checked<A, B, C, V, EX> andThen(
      @NotNull FunctionChecked<? super R, ? extends V, ? extends EX> after
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c) ->
        after.apply(apply(a, b, c));
  }

  /**
   * Return a {@link Function3Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   * accepts a {@link Tuple3}.
   *
   * @param function target function instance to wrap
   * @return a {@link Function3Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   *     accepts a {@link Tuple3}
   */
  @NotNull
  default Function3Checked<A, B, C, R, EX> untupled(
      @NotNull Function<
          Tuple3<
              ? super A,
              ? super B,
              ? super C>,
          ? extends R
          > function
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c) ->
        function.apply(
            new Tuple3<>(a, b, c));
  }

  /**
   * Return a {@link FunctionChecked} accepting a {@link Tuple3} of the original input parameters.
   *
   * @return a {@link FunctionChecked} accepting a {@link Tuple3} of the original input parameters
   */
  @NotNull
  default FunctionChecked<Tuple3<A, B, C>, R, EX> tupled() {
    return (@NotNull Tuple3<A, B, C> tuple3) ->
        apply(
            tuple3._1(),
            tuple3._2(),
            tuple3._3());
  }
}
