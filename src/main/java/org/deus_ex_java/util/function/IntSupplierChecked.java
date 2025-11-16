package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.IntSupplier;

/**
 * Enables the providing of a {@link IntSupplier} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
@NullMarked
public interface IntSupplierChecked<EX extends Exception> {

  /**
   * Gets a result.
   *
   * @return a result
   */
  int getAsInt() throws EX;
}
