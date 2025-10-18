package org.deus_ex_java.util.function;

import org.deus_ex_java.util.tuple.Tuple2;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * Enables the providing of a {@link Function2} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface Function2Checked<A, B, R, EX extends Exception> {

  /**
   * Return a {@link Function2Checked} wrapper around a {@link BiFunctionChecked} instance.
   *
   * @param biFunctionChecked the target function instance to wrap
   * @param <A>               the type of the first argument to the function
   * @param <B>               the type of the second argument to the function
   * @param <R>               the type of the result of the function
   * @return a {@link Function2Checked} wrapper around a {@link BiFunctionChecked} instance
   * @see FunctionsOps#to(Function2Checked) FunctionsOps.to(Function2Checked) for the inverted version of this method
   */
  @NotNull
  static <A, B, R, EX extends Exception> Function2Checked<A, B, R, EX> of(
      @NotNull BiFunctionChecked<? super A, ? super B, ? extends R, ? extends EX> biFunctionChecked
  ) {
    return biFunctionChecked::apply;
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
      @NotNull B b) throws EX;

  /**
   * Returns a composed function that first applies this function to its input, and then applies the {@code after}
   * function to the result. If evaluation of either function throws an exception, it is relayed to the caller of the
   * composed function.
   *
   * @param <V>   the type of output of the {@code after} function, and of the composed function
   * @param after the function to apply after this function is applied
   * @return a composed function that first applies this function and then applies the {@code after} function
   * @throws NullPointerException if after is null
   */
  @NotNull
  default <V> Function2Checked<A, B, V, EX> andThen(
      @NotNull FunctionChecked<? super R, ? extends V, ? extends EX> after
  ) {
    return (
        @NotNull A a,
        @NotNull B b) ->
        after.apply(apply(a, b));
  }

  /**
   * Return a {@link Function2Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   * accepts a {@link Tuple2}.
   *
   * @param functionChecked target function instance to wrap
   * @return a {@link Function2Checked} where the input parameters are extracted from a {@link FunctionChecked} which
   * accepts a {@link Tuple2}
   */
  @NotNull
  default Function2Checked<A, B, R, EX> untupled(
      @NotNull Function<
          Tuple2<
              ? super A,
              ? super B>,
          ? extends R
          > functionChecked
  ) {
    return (
        @NotNull A a,
        @NotNull B b) ->
        functionChecked.apply(
            new Tuple2<>(a, b));
  }

  /**
   * Return a {@link FunctionChecked} accepting a {@link Tuple2} of the original input parameters.
   *
   * @return a {@link FunctionChecked} accepting a {@link Tuple2} of the original input parameters
   */
  @NotNull
  default FunctionChecked<Tuple2<A, B>, R, EX> tupled() {
    return (@NotNull Tuple2<A, B> tuple2) ->
        apply(
            tuple2._1(),
            tuple2._2());
  }
}
