package org.deus_ex_java.util.function;

import java.util.function.DoubleSupplier;

/**
 * Enables the providing of a {@link DoubleSupplier} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface DoubleSupplierCheckedException extends DoubleSupplierChecked<Exception> {

}
