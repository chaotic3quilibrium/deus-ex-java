package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.Supplier;

/**
 * Enables the providing of a {@link Supplier} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface SupplierChecked<R, EX extends Exception> {

  /**
   * Gets a result.
   *
   * @return a result
   */
  R get() throws EX;
}
