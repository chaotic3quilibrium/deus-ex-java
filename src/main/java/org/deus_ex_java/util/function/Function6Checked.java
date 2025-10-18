package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple6;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Enables the providing of a {@link Function6} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface Function6Checked<A, B, C, D, E, F, R, EX extends Exception> {

  /**
   * Returns the result of applying this function to the provided arguments.
   *
   * @param a the first function argument
   * @param b the second function argument
   * @param c the third function argument
   * @param d the fourth function argument
   * @param e the fifth function argument
   * @param f the sixth function argument
   * @return the result of applying this function to the provided arguments
   */
  @NotNull R apply(
      @NotNull A a,
      @NotNull B b,
      @NotNull C c,
      @NotNull D d,
      @NotNull E e,
      @NotNull F f) throws EX;

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
  default <V> Function6Checked<A, B, C, D, E, F, V, EX> andThen(
      @NotNull FunctionChecked<? super R, ? extends V, ? extends EX> after
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c,
        @NotNull D d,
        @NotNull E e,
        @NotNull F f) ->
        after.apply(apply(a, b, c, d, e, f));
  }

  /**
   * Return a {@link Function6Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   * accepts a {@link Tuple6}.
   *
   * @param function target function instance to wrap
   * @return a {@link Function6Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   *     accepts a {@link Tuple6}
   */
  @NotNull
  default Function6Checked<A, B, C, D, E, F, R, EX> untupled(
      @NotNull Function<
          Tuple6<
              ? super A,
              ? super B,
              ? super C,
              ? super D,
              ? super E,
              ? super F>,
          ? extends R
          > function
  ) {
    return (
        @NotNull A a,
        @NotNull B b,
        @NotNull C c,
        @NotNull D d,
        @NotNull E e,
        @NotNull F f) ->
        function.apply(
            new Tuple6<>(a, b, c, d, e, f));
  }

  /**
   * Return a {@link FunctionChecked} accepting a {@link Tuple6} of the original input parameters.
   *
   * @return a {@link FunctionChecked} accepting a {@link Tuple6} of the original input parameters
   */
  @NotNull
  default FunctionChecked<Tuple6<A, B, C, D, E, F>, R, EX> tupled() {
    return (@NotNull Tuple6<A, B, C, D, E, F> tuple6) ->
        apply(
            tuple6._1(),
            tuple6._2(),
            tuple6._3(),
            tuple6._4(),
            tuple6._5(),
            tuple6._6());
  }
}
