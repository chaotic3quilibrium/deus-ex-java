package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple7;
import org.jspecify.annotations.NullMarked;

import java.util.function.Function;

/**
 * Enables the providing of a {@link Function7} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface Function7Checked<A, B, C, D, E, F, G, R, EX extends Exception> {

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
   * @return the result of applying this function to the provided arguments
   */
  R apply(
      A a,
      B b,
      C c,
      D d,
      E e,
      F f,
      G g) throws EX;

  /**
   * Returns a composed function that first applies this function to its input, and then applies the {@code after}
   * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <V>   the type of output of the {@code after} function, and of the composed function
   * @param after the function to apply after this function is applied
   * @return a composed function that first applies this function and then applies the {@code after} function
   */
  default <V> Function7Checked<A, B, C, D, E, F, G, V, EX> andThen(
      FunctionChecked<? super R, ? extends V, ? extends EX> after
  ) {
    return (
        A a,
        B b,
        C c,
        D d,
        E e,
        F f,
        G g) ->
        after.apply(apply(a, b, c, d, e, f, g));
  }

  /**
   * Return a {@link Function7Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   * accepts a {@link Tuple7}.
   *
   * @param function target function instance to wrap
   * @return a {@link Function7Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   *     accepts a {@link Tuple7}
   */
  default Function7Checked<A, B, C, D, E, F, G, R, EX> untupled(
      Function<
          Tuple7<
              ? super A,
              ? super B,
              ? super C,
              ? super D,
              ? super E,
              ? super F,
              ? super G>,
          ? extends R
          > function
  ) {
    return (
        A a,
        B b,
        C c,
        D d,
        E e,
        F f,
        G g) ->
        function.apply(
            new Tuple7<>(a, b, c, d, e, f, g));
  }

  /**
   * Return a {@link FunctionChecked} accepting a {@link Tuple7} of the original input parameters.
   *
   * @return a {@link FunctionChecked} accepting a {@link Tuple7} of the original input parameters
   */
  default FunctionChecked<Tuple7<A, B, C, D, E, F, G>, R, EX> tupled() {
    return (Tuple7<A, B, C, D, E, F, G> tuple7) ->
        apply(
            tuple7._1(),
            tuple7._2(),
            tuple7._3(),
            tuple7._4(),
            tuple7._5(),
            tuple7._6(),
            tuple7._7());
  }
}
