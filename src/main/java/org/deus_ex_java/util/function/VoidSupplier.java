package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an action to execute without having to unnecessarily specify an input argument, and unnecessarily supply a
 * return value; i.e. exists entirely to optionally generate side-effects.
 *
 * <p>There is no requirement that a new or distinct action occur, or new or distinct side-effects be generated, each
 * time it is invoked.
 *
 * <p>This is a <a href="package-summary.html">functional interface</a>
 * whose functional method is {@link #execute()}.
 */
@FunctionalInterface
public interface VoidSupplier {

  /**
   * Return a {@link VoidSupplier} wrapper around a {@link Runnable} instance.
   *
   * @param runnable the target function instance to wrap
   * @param <R>      the type of the result of the function
   * @return a {@link VoidSupplier} wrapper around a {@link Runnable} instance.
   * @see FunctionsOps#to(VoidSupplier) FunctionsOps.to(VoidSupplier) for the inverted version of this method
   */
  @NotNull
  static <R> VoidSupplier of(
      @NotNull Runnable runnable
  ) {
    return runnable::run;
  }

  /**
   * Performs an optional side-effect.
   */
  void execute();
}
