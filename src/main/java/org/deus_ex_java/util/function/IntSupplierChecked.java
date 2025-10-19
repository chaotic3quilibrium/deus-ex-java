package org.deus_ex_java.util.function;

import java.util.function.IntSupplier;

/**
 * Enables the providing of a {@link IntSupplier} Lambda function which can throw a checked exception.
 */
@FunctionalInterface
public interface IntSupplierChecked<EX extends Exception> {

  /**
   * Gets a result.
   *
   * @return a result
   */
  int getAsInt() throws EX;
}
