package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.BooleanSupplier;

/**
 * Enables the providing of a {@link BooleanSupplier} Lambda function which can throw a checked Exception.
 */
@FunctionalInterface
@NullMarked
public interface BooleanSupplierChecked<EX extends Exception> {
  /**
   * Returns a boolean value
   *
   * @return a boolean value
   */
  boolean getAsBoolean() throws EX;
}
