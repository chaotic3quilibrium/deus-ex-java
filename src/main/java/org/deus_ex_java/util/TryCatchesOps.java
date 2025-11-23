package org.deus_ex_java.util;

import org.deus_ex_java.lang.FatalThrowable;
import org.deus_ex_java.lang.WrappedCheckedException;
import org.deus_ex_java.util.function.SupplierCheckedException;
import org.deus_ex_java.util.function.VoidSupplier;
import org.deus_ex_java.util.function.VoidSupplierCheckedException;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Utility class providing static methods to reify try/catch <em>statements</em> into expressions.
 * <p>
 * <b><u>WARNING:</b></u>
 * <p>
 * Given the legacy of Java's checked exception system, it is imperative that <b><em>fatal</em></b> exceptions (defined
 * in {@link FatalThrowable#isFatalThrowable(Throwable) FatalThrowable.isFatalThrowable(...)}) avoid being caught and
 * suppressed.
 * <p>
 * Within this framework, if a <b><em>fatal</em></b> exception is thrown, it will be encapsulated within an instance of
 * {@link FatalThrowable}, a descendant of {@link RuntimeException}. This specific exception should be explicitly
 * caught, and its {@code getCause()} be re-thrown at the next checked exception opportunity. This likely means at the
 * root of the thread, or within an event loop.
 */
@NullMarked
public final class TryCatchesOps {

  private TryCatchesOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Reify a try/catch statement into an {@link Optional} where, after the {@link VoidSupplier#execute()} is invoked, if
   * there is no {@link Throwable} exception thrown, an {@link Optional#empty()} is returned, otherwise if an
   * {@link Throwable} exception is thrown and the exception satisfies the {@link Class#isInstance(Object)} of one of
   * the provided elements within {@code throwableClasses} (explicitly excludes {@link FatalThrowable}), the exception
   * is returned within the {@link Optional#of}, otherwise, the unrecognized exception is re-thrown.
   *
   * @param voidSupplier     function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                         invoked, if there is no exception thrown, the result are ignored
   * @param throwableClasses if the {@code voidSupplier} function throws an exception, and the exception satisfies the
   *                         {@link Class#isInstance} of one of the elements of this parameter (explicitly excludes
   *                         {@link FatalThrowable}), the exception is returned within the {@link Optional#of}
   * @param <L>              type of the {@link Throwable} instances being caught
   * @return a try/catch statement into an {@link Optional} where, after the {@link VoidSupplier} is invoked, if there
   *     is no {@link Throwable} exception thrown, an {@link Optional#empty()} is returned, otherwise if an
   *     {@link Throwable} exception is thrown and the exception satisfies the {@link Class#isInstance} of one of the
   *     provided elements within {@code throwableClasses} (explicitly excludes {@link FatalThrowable}), the exception
   *     is returned within the {@link Optional#of}, otherwise, the unrecognized exception is re-thrown
   * @throws FatalThrowable wraps <b><em>fatal</em></b> checked exceptions
   */
  @SafeVarargs
  public static <L extends Throwable> Optional<L> wrap(
      VoidSupplier voidSupplier,
      Class<L>... throwableClasses
  ) {
    try {
      voidSupplier.execute();

      return Optional.empty();
    } catch (Throwable throwable) {
      FatalThrowable.filterToFatalThrowable(throwable)
          .ifPresent(fatalThrowable -> {

            throw fatalThrowable;
          });
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
   * @throws FatalThrowable wraps <b><em>fatal</em></b> checked exceptions
   */
  public static Optional<RuntimeException> wrap(
      VoidSupplier voidSupplier
  ) {
    return wrap(voidSupplier, RuntimeException.class);
  }

  /**
   * Reify a try/catch statement into an {@link Either} where, when the {@link Supplier} is invoked, if there is no
   * {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within the right side of
   * an {@link Either}, otherwise if an {@link Throwable} exception is thrown and the exception satisfies the
   * {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses} (explicitly
   * excludes {@link FatalThrowable}), the exception is returned within the left side of an {@link Either}, otherwise,
   * the exception is re-thrown.
   *
   * @param supplier         function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                         invoked, if there is no exception thrown, the function's return value is returned within
   *                         the right side of an {@link Either}
   * @param throwableClasses if the {@code supplier} function throws an exception, and the exception satisfies the
   *                         {@link Class#isInstance} of one of the elements of this parameter (explicitly excludes
   *                         {@link FatalThrowable}), the exception is returned within the left side of an
   *                         {@link Either} left side of an {@link Either}, otherwise, the exception is
   *                         re-{@code thrown}
   * @param <L>              type of the {@link Throwable} instance being caught
   * @param <R>              type of the instance provided by the {@link Supplier}
   * @return a try/catch statement into an {@link Either} where, when the {@link Supplier} is invoked, if there is no
   *     {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within the right
   *     side of an {@link Either}, otherwise if an {@link Throwable} exception is thrown and the exception satisfies
   *     the {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses}
   *     (explicitly excludes {@link FatalThrowable}), the exception is returned within the left side of an
   *     {@link Either}, otherwise, the exception is re-thrown
   * @throws FatalThrowable wraps <b><em>fatal</em></b> checked exceptions
   */
  @SafeVarargs
  public static <L extends Throwable, R> Either<L, R> wrap(
      Supplier<R> supplier,
      Class<L>... throwableClasses
  ) {
    try {
      return Either.right(supplier.get());
    } catch (Throwable throwable) {
      FatalThrowable.filterToFatalThrowable(throwable)
          .ifPresent(fatalThrowable -> {

            throw fatalThrowable;
          });
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
   * @throws FatalThrowable wraps <b><em>fatal</em></b> checked exceptions
   */
  public static <R> Either<RuntimeException, R> wrap(
      Supplier<R> supplier
  ) {
    return wrap(supplier, RuntimeException.class);
  }

  @SafeVarargs
  private static <T extends Throwable> T resolveCatchThrowableWrappedCheckedException(
      Throwable throwable,
      String s,
      Class<T>... throwableClasses
  ) {
    FatalThrowable.filterToFatalThrowable(throwable)
        .ifPresent(fatalThrowable -> {

          throw fatalThrowable;
        });
    if (Arrays.stream(throwableClasses)
        .anyMatch(throwableClass ->
            throwableClass.isInstance(throwable))
    ) {

      //noinspection unchecked
      return (T) throwable;
    }
    if (throwable instanceof RuntimeException runtimeException) {

      throw runtimeException;
    }

    throw new WrappedCheckedException(
        "wrapCheckedException(%s) failure - %s".formatted(s, throwable.getMessage()),
        throwable);
  }

  /**
   * Reify a try/catch statement into an {@link Optional} where, after the
   * {@link VoidSupplierCheckedException#execute()} is invoked, if there is no {@link Throwable} exception thrown, an
   * {@link Optional#empty()} is returned, otherwise if an {@link Throwable} exception is thrown and the exception
   * satisfies the {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses}
   * (explicitly excludes {@link FatalThrowable}), the exception is returned within the {@link Optional#of}, otherwise,
   * if the unrecognized exception is an instance of {@link RuntimeException}, it is rethrown, otherwise a new
   * {@link WrappedCheckedException} wrapping the unrecognized exception is thrown.
   *
   * @param voidSupplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block,
   *                                     that when invoked, if there is no exception thrown, the results are ignored
   * @param throwableClasses             if the {@code voidSupplier} function throws an exception, and the exception
   *                                     satisfies the {@link Class#isInstance} of one of the elements of this parameter
   *                                     (explicitly excludes {@link FatalThrowable}), the exception is returned within
   *                                     the {@link Optional#of}
   * @param <L>                          type of the {@link Throwable} instances being caught
   * @return a try/catch statement into an {@link Optional} where, after the
   *     {@link VoidSupplierCheckedException#execute()} is invoked, if there is no {@link Throwable} exception thrown,
   *     an {@link Optional#empty()} is returned, otherwise if an {@link Throwable} exception is thrown and the
   *     exception satisfies the {@link Class#isInstance(Object)} of one of the provided elements within
   *     {@code throwableClasses} (explicitly excludes {@link FatalThrowable}), the exception is returned within the
   *     {@link Optional#of}, otherwise, if the unrecognized exception is an instance of {@link RuntimeException}, it is
   *     rethrown, otherwise a new {@link WrappedCheckedException} wrapping the unrecognized exception is thrown
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps <b><em>fatal</em></b> checked exceptions
   */
  @SafeVarargs
  public static <L extends Throwable> Optional<L> wrapCheckedException(
      VoidSupplierCheckedException voidSupplierCheckedException,
      Class<L>... throwableClasses
  ) {
    try {
      voidSupplierCheckedException.execute();

      return Optional.empty();
    } catch (Throwable throwable) {
      return Optional.of(resolveCatchThrowableWrappedCheckedException(throwable, "VoidSupplierCheckedException", throwableClasses));
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
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps <b><em>fatal</em></b> checked exceptions
   */
  public static Optional<RuntimeException> wrapCheckedException(
      VoidSupplierCheckedException voidSupplierCheckedException
  ) {
    return wrapCheckedException(voidSupplierCheckedException, RuntimeException.class);
  }

  /**
   * Reify a try/catch statement into an {@link Either} where, when the {@link SupplierCheckedException} is invoked, if
   * there is no {@link Throwable} exception thrown, the value returned by the {@link SupplierCheckedException} is
   * returned within the right side of an {@link Either}, otherwise if an {@link Throwable} exception is thrown and the
   * exception satisfies the {@link Class#isInstance(Object)} of one of the provided elements within
   * {@code throwableClasses} (explicitly excludes {@link FatalThrowable}), the exception is returned within the left
   * side of an {@link Either}, otherwise, the exception is re-thrown.
   *
   * @param supplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that
   *                                 when invoked, if there is no exception thrown, the function's return value is
   *                                 returned within the right side of an {@link Either}
   * @param throwableClasses         if the {@code supplier} function throws an exception, and the exception satisfies
   *                                 the {@link Class#isInstance} of one of the elements of this parameter (explicitly
   *                                 excludes {@link FatalThrowable}), the exception is returned within the left side of
   *                                 an {@link Either} left side of an {@link Either}, otherwise, the exception is
   *                                 re-{@code thrown}
   * @param <L>                      type of the {@link Throwable} instance being caught
   * @param <R>                      type of the instance provided by the {@link SupplierCheckedException}
   * @return a try/catch statement into an {@link Either} where, when the {@link SupplierCheckedException} is invoked,
   *     if there is no {@link Throwable} exception thrown, the value returned by the {@link SupplierCheckedException}
   *     is returned within the right side of an {@link Either}, otherwise if an {@link Throwable} exception is thrown
   *     and the exception satisfies the {@link Class#isInstance(Object)} of one of the provided elements within
   *     {@code throwableClasses} (explicitly excludes {@link FatalThrowable}), the exception is returned within the
   *     left side of an {@link Either}, otherwise, the exception is re-thrown
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps <b><em>fatal</em></b> checked exceptions
   */
  @SafeVarargs
  public static <L extends Throwable, R> Either<L, R> wrapCheckedException(
      SupplierCheckedException<R> supplierCheckedException,
      Class<L>... throwableClasses
  ) {
    try {
      return Either.right(supplierCheckedException.get());
    } catch (Throwable throwable) {

      return Either.left(resolveCatchThrowableWrappedCheckedException(throwable, "SupplierCheckedException", throwableClasses));
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
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps <b><em>fatal</em></b> checked exceptions
   */
  public static <R> Either<Exception, R> wrapCheckedException(
      SupplierCheckedException<R> supplierCheckedException
  ) {
    return wrapCheckedException(supplierCheckedException, Exception.class);
  }

}
