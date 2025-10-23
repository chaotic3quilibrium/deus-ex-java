package org.deus_ex_java.util;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.deus_ex_java.util.function.SupplierCheckedException;
import org.deus_ex_java.util.function.VoidSupplier;
import org.deus_ex_java.util.function.VoidSupplierCheckedException;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

public final class TryCatchesOps {

  /**
   * Reify a try/catch statement into an {@link Optional} where, after the {@link VoidSupplier#execute()} is invoked, if
   * there is no {@link Throwable} exception thrown, an {@link Optional#empty()} is returned, otherwise if an
   * {@link Throwable} exception is thrown and the exception satisfies the {@link Class#isInstance(Object)} of one of
   * the provided elements within {@code throwableClasses}, the exception is returned within the {@link Optional#of},
   * otherwise, the unrecognized exception is re-thrown.
   *
   * @param voidSupplier     function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                         invoked, if there is no exception thrown, the result are ignored
   * @param throwableClasses if the {@code voidSupplier} function throws an exception, and the exception satisfies the
   *                         {@link Class#isInstance} of one of the elements of this parameter, the exception is
   *                         returned within the {@link Optional#of}
   * @param <L>              type of the {@link Throwable} instances being caught
   * @return a try/catch statement into an {@link Optional} where, after the {@link VoidSupplier} is invoked, if there
   *     is no {@link Throwable} exception thrown, an {@link Optional#empty()} is returned, otherwise if an
   *     {@link Throwable} exception is thrown and the exception satisfies the {@link Class#isInstance} of one of the
   *     provided elements within {@code throwableClasses}, the exception is returned within the {@link Optional#of},
   *     otherwise, the unrecognized exception is re-thrown
   */
  @SafeVarargs
  @NotNull
  public static <L extends Throwable> Optional<L> wrap(
      @NotNull VoidSupplier voidSupplier,
      @NotNull Class<L>... throwableClasses
  ) {
    try {
      voidSupplier.execute();

      return Optional.empty();
    } catch (Throwable throwable) {
      if (Arrays.stream(throwableClasses)
          .anyMatch(throwableClass ->
              throwableClass.isInstance(throwable))
      ) {

        //noinspection unchecked
        return Optional.of((L) throwable);
      }

      throw throwable;
    }
  }

  /**
   * Reify a try/catch statement into an {@link Optional} where, after the {@link VoidSupplier#execute()} is invoked, if
   * there is no {@link Throwable} exception thrown, an {@link Optional#empty()} is returned, otherwise if an
   * {@link Throwable} exception is thrown and the exception is a {@link RuntimeException}, the exception is returned
   * within the {@link Optional#of}, otherwise, the unrecognized exception is re-thrown.
   *
   * @param voidSupplier function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                     invoked, if there is no exception thrown, the result are ignored
   * @return a try/catch statement into an {@link Optional} where, after the {@link VoidSupplier#execute()} is invoked,
   *     if there is no {@link Throwable} exception thrown, an {@link Optional#empty()} is returned, otherwise if an
   *     {@link Throwable} exception is thrown and the exception is a {@link RuntimeException}, the exception is
   *     returned within the {@link Optional#of}, otherwise, the unrecognized exception is re-thrown
   */
  @NotNull
  public static Optional<RuntimeException> wrap(
      @NotNull VoidSupplier voidSupplier
  ) {
    return wrap(voidSupplier, RuntimeException.class);
  }

  /**
   * Reify a try/catch statement into an {@link Either} where, when the {@link Supplier} is invoked, if there is no
   * {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within the right side of
   * an {@link Either}, otherwise if an {@link Throwable} exception is thrown and the exception satisfies the
   * {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses}, the exception is
   * returned within the left side of an {@link Either}, otherwise, the exception is re-thrown.
   *
   * @param supplier         function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                         invoked, if there is no exception thrown, the function's return value is returned within
   *                         the right side of an {@link Either}
   * @param throwableClasses if the {@code supplier} function throws an exception, and the exception satisfies the
   *                         {@link Class#isInstance} of one of the elements of this parameter, the exception is
   *                         returned within the left side of an {@link Either} left side of an {@link Either},
   *                         otherwise, the exception is re-{@code thrown}
   * @param <L>              type of the {@link Throwable} instance being caught
   * @param <R>              type of the instance provided by the {@link Supplier}
   * @return a well-defined instance of {@link Either}
   */
  @SafeVarargs
  @NotNull
  public static <L extends Throwable, R> Either<L, R> wrap(
      @NotNull Supplier<R> supplier,
      @NotNull Class<L>... throwableClasses
  ) {
    try {
      return Either.right(supplier.get());
    } catch (Throwable throwable) {
      if (Arrays.stream(throwableClasses)
          .anyMatch(throwableClass ->
              throwableClass.isInstance(throwable))
      ) {

        //noinspection unchecked
        return Either.left((L) throwable);
      }

      throw throwable;
    }
  }

  /**
   * Reify a try/catch statement into an {@link Either} where, when the {@link Supplier} is invoked, if there is no
   * {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within the right side of
   * an {@link Either}, otherwise if an {@link Throwable} exception is thrown and the exception is a
   * {@link RuntimeException}, the exception is returned within the left side of an {@link Either}, otherwise, the
   * exception is re-thrown.
   *
   * @param supplier function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when invoked, if
   *                 there is no exception thrown, the function's return value is returned within the right side of an
   *                 {@link Either}
   * @param <R>      type of the instance provided by the {@link Supplier}
   * @return a try/catch statement into an {@link Either} where, when the {@link Supplier} is invoked, if there is no
   *     {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within the right
   *     side of an {@link Either}, otherwise if an {@link Throwable} exception is thrown and the exception is a
   *     {@link RuntimeException}, the exception is returned within the left side of an {@link Either}, otherwise, the
   *     exception is re-thrown
   */
  @NotNull
  public static <R> Either<RuntimeException, R> wrap(
      @NotNull Supplier<R> supplier
  ) {
    return wrap(supplier, RuntimeException.class);
  }

  /**
   * Reify a try/catch statement into an {@link Optional} where, after the
   * {@link VoidSupplierCheckedException#execute()} is invoked, if there is no {@link Throwable} exception thrown, an
   * {@link Optional#empty()} is returned, otherwise if an {@link Throwable} exception is thrown and the exception
   * satisfies the {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses}, the
   * exception is returned within the {@link Optional#of}, otherwise, if the unrecognized exception is an instance of
   * {@link RuntimeException}, it is rethrown, otherwise a new {@link WrappedCheckedException} wrapping the unrecognized
   * exception is thrown.
   *
   * @param voidSupplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block,
   *                                     that when invoked, if there is no exception thrown, the results are ignored
   * @param throwableClasses             if the {@code voidSupplier} function throws an exception, and the exception
   *                                     satisfies the {@link Class#isInstance} of one of the elements of this
   *                                     parameter, the exception is returned within the {@link Optional#of}
   * @param <L>                          type of the {@link Throwable} instances being caught
   * @return a try/catch statement into an {@link Optional} where, after the
   *     {@link VoidSupplierCheckedException#execute()} is invoked, if there is no {@link Throwable} exception thrown,
   *     an {@link Optional#empty()} is returned, otherwise if an {@link Throwable} exception is thrown and the
   *     exception satisfies the {@link Class#isInstance(Object)} of one of the provided elements within
   *     {@code throwableClasses}, the exception is returned within the {@link Optional#of}, otherwise, if the
   *     unrecognized exception is an instance of {@link RuntimeException}, it is rethrown, otherwise a new
   *     {@link WrappedCheckedException} wrapping the unrecognized exception is thrown
   */
  @SafeVarargs
  @NotNull
  public static <L extends Throwable> Optional<L> wrapCheckedException(
      @NotNull VoidSupplierCheckedException voidSupplierCheckedException,
      @NotNull Class<L>... throwableClasses
  ) {
    try {
      voidSupplierCheckedException.execute();

      return Optional.empty();
    } catch (Throwable throwable) {
      if (Arrays.stream(throwableClasses)
          .anyMatch(throwableClass ->
              throwableClass.isInstance(throwable))
      ) {

        //noinspection unchecked
        return Optional.of((L) throwable);
      }
      if (throwable instanceof RuntimeException runtimeException) {

        throw runtimeException;
      }

      throw new WrappedCheckedException(
          "wrapChecked(VoidSupplierCheckedException) failure - " + throwable.getMessage(),
          throwable);
    }
  }

  /**
   * Reify a try/catch statement into an {@link Optional} where, after the
   * {@link VoidSupplierCheckedException#execute()} is invoked, if there is no {@link Throwable} exception thrown, an
   * {@link Optional#empty()} is returned, otherwise if an {@link Throwable} exception is thrown and the exception is a
   * {@link RuntimeException}, the exception is returned within the {@link Optional#of}, otherwise, if the unrecognized
   * exception is an instance of {@link RuntimeException}, it is rethrown, otherwise a new
   * {@link WrappedCheckedException} wrapping the unrecognized exception is thrown.
   *
   * @param voidSupplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block,
   *                                     that when invoked, if there is no exception thrown, the results are ignored
   * @return a try/catch statement into an {@link Optional} where, after the
   *     {@link VoidSupplierCheckedException#execute()} is invoked, if there is no {@link Throwable} exception thrown,
   *     an {@link Optional#empty()} is returned, otherwise if an {@link Throwable} exception is thrown and the
   *     exception is a {@link RuntimeException}, the exception is returned within the {@link Optional#of}, otherwise,
   *     if the unrecognized exception is an instance of {@link RuntimeException}, it is rethrown, otherwise a new
   *     {@link WrappedCheckedException} wrapping the unrecognized exception is thrown.
   */
  @NotNull
  public static Optional<RuntimeException> wrapCheckedException(
      @NotNull VoidSupplierCheckedException voidSupplierCheckedException
  ) {
    return wrapCheckedException(voidSupplierCheckedException, RuntimeException.class);
  }

  /**
   * Reify a try/catch statement into an {@link Either} where, when the {@link SupplierCheckedException} is invoked, if
   * there is no {@link Throwable} exception thrown, the value returned by the {@link SupplierCheckedException} is
   * returned within the right side of an {@link Either}, otherwise if an {@link Throwable} exception is thrown and the
   * exception satisfies the {@link Class#isInstance(Object)} of one of the provided elements within
   * {@code throwableClasses}, the exception is returned within the left side of an {@link Either}, otherwise, the
   * exception is re-thrown.
   *
   * @param supplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that
   *                                 when invoked, if there is no exception thrown, the function's return value is
   *                                 returned within the right side of an {@link Either}
   * @param throwableClasses         if the {@code supplier} function throws an exception, and the exception satisfies
   *                                 the {@link Class#isInstance} of one of the elements of this parameter, the
   *                                 exception is returned within the left side of an {@link Either} left side of an
   *                                 {@link Either}, otherwise, the exception is re-{@code thrown}
   * @param <L>                      type of the {@link Throwable} instance being caught
   * @param <R>                      type of the instance provided by the {@link SupplierCheckedException}
   * @return a try/catch statement into an {@link Either} where, when the {@link SupplierCheckedException} is invoked,
   *     if there is no {@link Throwable} exception thrown, the value returned by the {@link SupplierCheckedException}
   *     is returned within the right side of an {@link Either}, otherwise if an {@link Throwable} exception is thrown
   *     and the exception satisfies the {@link Class#isInstance(Object)} of one of the provided elements within
   *     {@code throwableClasses}, the exception is returned within the left side of an {@link Either}, otherwise, the
   *     exception is re-thrown
   */
  @SafeVarargs
  @NotNull
  public static <L extends Throwable, R> Either<L, R> wrapCheckedException(
      @NotNull SupplierCheckedException<R> supplierCheckedException,
      @NotNull Class<L>... throwableClasses
  ) {
    try {
      return Either.right(supplierCheckedException.get());
    } catch (Throwable throwable) {
      if (Arrays.stream(throwableClasses)
          .anyMatch(throwableClass ->
              throwableClass.isInstance(throwable))
      ) {

        //noinspection unchecked
        return Either.left((L) throwable);
      }
      if (throwable instanceof RuntimeException runtimeException) {

        throw runtimeException;
      }

      throw new WrappedCheckedException(
          "wrapChecked(SupplierCheckedException) failure - " + throwable.getMessage(),
          throwable);
    }
  }

  /**
   * Reify a try/catch statement into an {@link Either} where, when the {@link SupplierCheckedException} is invoked, if
   * there is no {@link Throwable} exception thrown, the value returned by the {@link SupplierCheckedException} is
   * returned within the right side of an {@link Either}, otherwise if an {@link Throwable} exception is thrown and the
   * exception is a {@link RuntimeException}, the exception is returned within the left side of an {@link Either},
   * otherwise, the exception is re-thrown.
   *
   * @param supplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that
   *                                 when invoked, if there is no exception thrown, the function's return value is
   *                                 returned within the right side of an {@link Either}
   * @param <R>                      type of the instance provided by the {@link SupplierCheckedException}
   * @return a try/catch statement into an {@link Either} where, when the {@link SupplierCheckedException} is invoked,
   *     if there is no {@link Throwable} exception thrown, the value returned by the {@link SupplierCheckedException}
   *     is returned within the right side of an {@link Either}, otherwise if an {@link Throwable} exception is thrown
   *     and the exception is a {@link RuntimeException}, the exception is returned within the left side of an
   *     {@link Either}, otherwise, the exception is re-throw
   */
  @NotNull
  public static <R> Either<Exception, R> wrapCheckedException(
      @NotNull SupplierCheckedException<R> supplierCheckedException
  ) {
    return wrapCheckedException(supplierCheckedException, Exception.class);
  }

}
