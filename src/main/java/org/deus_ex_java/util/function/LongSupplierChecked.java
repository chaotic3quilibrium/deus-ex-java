package org.deus_ex_java.util.function;

import java.util.function.LongSupplier;

/**
 * Enables the providing of a {@link LongSupplier} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface LongSupplierChecked<E extends Exception> {

  /**
   * Gets a result.
   *
   * @return a result
   */
  long getAsLong() throws E;
}
