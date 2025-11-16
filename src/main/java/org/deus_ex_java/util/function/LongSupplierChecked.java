package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.LongSupplier;

/**
 * Enables the providing of a {@link LongSupplier} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface LongSupplierChecked<EX extends Exception> {

  /**
   * Gets a result.
   *
   * @return a result
   */
  long getAsLong() throws EX;
}
