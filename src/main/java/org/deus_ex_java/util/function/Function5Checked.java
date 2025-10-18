package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple5;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Enables the providing of a {@link Function5} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface Function5Checked<A, B, C, D, E, R, EX extends Exception> {

  /**
   * Returns the result of applying this function to the provided arguments.
   *
   * @param a the first function argument
   * @param b the second function argument
   * @param c the third function argument
   * @param d the fourth function argument
   * @param e the fifth function argument
   * @return the result of applying this function to the provided arguments
   */
  @NotNull R apply(
      @NotNull A a,
      @NotNull B b,
      @NotNull C c,
      @NotNull D d,
      @NotNull E e) throws EX;

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
  default <V> Function5Checked<A, B, C, D, E, V, EX> andThen(
      @NotNull FunctionChecked<? super R, ? extends V, ? extends EX> after
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c,
        @NotNull D d,
        @NotNull E e) ->
        after.apply(apply(a, b, c, d, e));
  }

  /**
   * Return a {@link Function5Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   * accepts a {@link Tuple5}.
   *
   * @param function target function instance to wrap
   * @return a {@link Function5Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   *     accepts a {@link Tuple5}
   */
  @NotNull
  default Function5Checked<A, B, C, D, E, R, EX> untupled(
      @NotNull Function<
          Tuple5<
              ? super A,
              ? super B,
              ? super C,
              ? super D,
              ? super E>,
          ? extends R
          > function
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c,
        @NotNull D d,
        @NotNull E e) ->
        function.apply(
            new Tuple5<>(a, b, c, d, e));
  }

  /**
   * Return a {@link FunctionChecked} accepting a {@link Tuple5} of the original input parameters.
   *
   * @return a {@link FunctionChecked} accepting a {@link Tuple5} of the original input parameters
   */
  @NotNull
  default FunctionChecked<Tuple5<A, B, C, D, E>, R, EX> tupled() {
    return (@NotNull Tuple5<A, B, C, D, E> tuple5) ->
        apply(
            tuple5._1(),
            tuple5._2(),
            tuple5._3(),
            tuple5._4(),
            tuple5._5());
  }
}
