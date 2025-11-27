package org.deus_ex_java.util.function;

import org.jspecify.annotations.NullMarked;

import java.util.function.IntSupplier;

/**
 * Enables the providing of a {@link IntSupplier} Lambda function which can throw a checked exception, explicitly
 * specifying {@link Exception}.
 */
@FunctionalInterface
@NullMarked
public interface IntSupplierCheckedException extends IntSupplierChecked<Exception> {

}
