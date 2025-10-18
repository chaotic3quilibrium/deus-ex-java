package org.deus_ex_java.util.function;

import java.util.function.DoubleSupplier;

/**
 * Enables the providing of a {@link DoubleSupplier} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface DoubleSupplierChecked<E extends Exception> {

  /**
   * Gets a result.
   *
   * @return a result
   */
  double getAsDouble() throws E;
}

