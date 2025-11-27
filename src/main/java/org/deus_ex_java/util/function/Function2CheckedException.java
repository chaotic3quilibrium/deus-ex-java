package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

/**
 * Enables the providing of a {@link Function2} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
@NullMarked
public interface Function2CheckedException<A, B, R> extends Function2Checked<A, B, R, Exception> {

  /**
   * Return a {@link Function2CheckedException} wrapper around a {@link BiFunctionCheckedException} instance.
   *
   * @param biFunctionCheckedException the target function instance to wrap
   * @param <A>                        the type of the first argument to the function
   * @param <B>                        the type of the second argument to the function
   * @param <R>                        the type of the result of the function
   * @return a {@link Function2CheckedException} wrapper around a {@link BiFunctionCheckedException} instance
   * @see FunctionsOps#to(Function2CheckedException) FunctionsOps.to(Function2CheckedException) for the inverted version
   *     of this method
   */
  static <A, B, R> Function2CheckedException<A, B, R> of(
      BiFunctionCheckedException<? super A, ? super B, ? extends R> biFunctionCheckedException
  ) {
    return biFunctionCheckedException::apply;
  }
}
