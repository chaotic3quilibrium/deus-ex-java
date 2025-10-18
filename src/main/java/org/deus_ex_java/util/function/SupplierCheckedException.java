package org.deus_ex_java.util.function;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * Enables the providing of a {@link Supplier} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface SupplierCheckedException<R> extends SupplierChecked<R, Exception> {

  /**
   * Return a {@link SupplierCheckedException} wrapper around a {@link Callable} instance.
   *
   * @param callable the target function instance to wrap
   * @param <R>      the type of the result of the function
   * @return a {@link SupplierCheckedException} wrapper around a {@link Callable} instance
   * @see FunctionsOps#to(SupplierCheckedException) FunctionsOps.to(SupplierCheckedException) for the inverted version of this method
   */
  @NotNull
  static <R> SupplierCheckedException<R> of(
      @NotNull Callable<? extends R> callable
  ) {
    return callable::call;
  }
}
