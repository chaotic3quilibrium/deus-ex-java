package org.deus_ex_java.util.function;

import java.util.function.BooleanSupplier;

/**
 * Enables the providing of a {@link BooleanSupplier} Lambda function which can throw a checked Exception.
 */
@FunctionalInterface
public interface BooleanSupplierChecked<E extends Exception> {
  /**
   * Returns a boolean value
   *
   * @return a boolean value
   */
  boolean getAsBoolean() throws E;
}
