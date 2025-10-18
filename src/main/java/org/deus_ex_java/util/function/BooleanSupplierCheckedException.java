package org.deus_ex_java.util.function;

import java.util.function.BooleanSupplier;

/**
 * Enables the providing of a {@link BooleanSupplier} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
public interface BooleanSupplierCheckedException extends BooleanSupplierChecked<Exception> {

}
