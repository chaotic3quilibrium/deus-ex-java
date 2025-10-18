package org.deus_ex_java.util.function;

import java.util.function.LongSupplier;

/**
 * Enables the providing of a {@link LongSupplier} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface LongSupplierCheckedException extends LongSupplierChecked<Exception> {

}
