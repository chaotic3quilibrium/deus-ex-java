package org.deus_ex_java.util.function;

import java.util.function.Supplier;

/**
 * Enables the providing of a {@link Supplier} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface SupplierChecked<R, E extends Exception> {

  /**
   * Gets a result.
   *
   * @return a result
   */
  R get() throws E;
}
