package org.deus_ex_java.util;

import org.deus_ex_java.util.function.VoidSupplier;
import org.jspecify.annotations.NullMarked;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * Utility class reifying the {@code if} statement and {@code ?:} (a.k.a. ternary) operator to enable the use of a
 * lazily executed code-block in each of three positions; the condition, the {@code true} pathway, and the {@code false}
 * pathway.
 * <p>
 * NOTE: This is provided because the alternative of using the {@code switch} expression on a {@code boolean} is only
 * available as of Java 21 or greater.
 */
@NullMarked
public final class TernaryOps {

  private TernaryOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns the result from {@code supplierTrue} when {@code isSelectingSupplierTrue} is {@code true}, otherwise
   * returns the result from {@code supplierFalse}.
   *
   * @param isSelectingSupplierTrue the condition to evaluate
   * @param supplierTrue            invoked to produce the result when the condition is {@code true}
   * @param supplierFalse           invoked to produce the result when the condition is {@code false}
   * @param <T>                     the type of the returned instance
   * @return the result from {@code supplierTrue} when {@code isSelectingSupplierTrue}, otherwise returns the result
   *     from {@code supplierFalse}
   */
  public static <T> T get(
      boolean isSelectingSupplierTrue,
      Supplier<T> supplierTrue,
      Supplier<T> supplierFalse
  ) {
    return isSelectingSupplierTrue
        ? supplierTrue.get()
        : supplierFalse.get();
  }

  /**
   * Returns a {@link Supplier} which computes the result from {@code supplierTrue} when
   * {@code supplierCondition.getAsBoolean()} returns {@code true}, otherwise returns the result from
   * {@code supplierFalse}.
   *
   * @param supplierCondition invoked to produce the condition to evaluate
   * @param supplierTrue      invoked to produce the result when the condition is {@code true}
   * @param supplierFalse     invoked to produce the result when the condition is {@code false}
   * @param <T>               the type of the returned instance
   * @return a {@link Supplier} which computes the result from {@code supplierTrue} when
   *     {@code supplierCondition.getAsBoolean()} returns {@code true}, otherwise returns the result from
   *     {@code supplierFalse}
   */
  public static <T> Supplier<T> of(
      BooleanSupplier supplierCondition,
      Supplier<T> supplierTrue,
      Supplier<T> supplierFalse
  ) {
    return () ->
        get(
            supplierCondition.getAsBoolean(),
            supplierTrue,
            supplierFalse);
  }

  /**
   * Executes the result from {@code voidSupplierTrue} when {@code isSelectingVoidSupplierTrue} is {@code true},
   * otherwise returns the result from {@code voidSupplierFalse}.
   *
   * @param isSelectingVoidSupplierTrue the condition to evaluate
   * @param voidSupplierTrue            invoked to produce a side-effect when the condition is {@code true}
   * @param voidSupplierFalse           invoked to produce a side-effect when the condition is {@code false}
   */
  public static void execute(
      boolean isSelectingVoidSupplierTrue,
      VoidSupplier voidSupplierTrue,
      VoidSupplier voidSupplierFalse
  ) {
    if (isSelectingVoidSupplierTrue) {
      voidSupplierTrue.execute();
    } else {
      voidSupplierFalse.execute();
    }
  }

  /**
   * Returns a {@link Supplier} which executes {@code voidSupplierTrue} when
   * {@code voidSupplierCondition.getAsBoolean()} returns {@code true}, otherwise returns the result from
   * {@code voidSupplierFalse}.
   *
   * @param voidSupplierCondition invoked to produce the condition to evaluate
   * @param voidSupplierTrue      invoked to produce a side-effect when the condition is {@code true}
   * @param voidSupplierFalse     invoked to produce a side-effect when the condition is {@code false}
   * @return a {@link Supplier} which executes {@code voidSupplierTrue} when
   *     {@code voidSupplierCondition.getAsBoolean()} returns {@code true}, otherwise returns the result from
   *     {@code voidSupplierFalse}
   */
  public static VoidSupplier of(
      BooleanSupplier voidSupplierCondition,
      VoidSupplier voidSupplierTrue,
      VoidSupplier voidSupplierFalse
  ) {
    return () ->
        execute(
            voidSupplierCondition.getAsBoolean(),
            voidSupplierTrue,
            voidSupplierFalse);
  }
}
