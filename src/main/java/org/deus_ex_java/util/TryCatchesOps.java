package org.deus_ex_java.util;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.deus_ex_java.util.function.SupplierCheckedException;
import org.deus_ex_java.util.function.VoidSupplier;
import org.deus_ex_java.util.function.VoidSupplierCheckedException;
import org.jspecify.annotations.NullMarked;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Utility class providing static methods to reify try/catch <em>statements</em> into expressions.
 * <p>
 * <b><u>WARNING:</u></b>
 * <p>
 * Given the legacy of Java's checked exception system, it is imperative that <b><em>fatal</em></b> exceptions (defined
 * in {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) avoid being caught and
 * suppressed.
 * <p>
 * Within this framework, if a <b><em>fatal</em></b> exception is thrown, it will explicitly not be caught.
 */
@NullMarked
public final class TryCatchesOps {

  private TryCatchesOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  private static void throwRuntimeExceptionOrWrappedCheckedException(Throwable throwable) {
    if (throwable instanceof RuntimeException runtimeException) {

      throw runtimeException;
    }

    throw new WrappedCheckedException(throwable);
  }

  @SafeVarargs
  private static <T extends Throwable> T resolveCatchThrowableWrappedCheckedException(
      Throwable throwable,
      Class<? extends T>... throwableClasses
  ) {
    //noinspection ThrowableNotThrown
    WrappedCheckedException.requireNonFatal(throwable);
    if (Arrays.stream(throwableClasses)
        .anyMatch(throwableClass ->
            throwableClass.isInstance(throwable))
    ) {

      //noinspection unchecked
      return (T) throwable;
    }

    throwRuntimeExceptionOrWrappedCheckedException(throwable);

    //this is never reached because the prior method call always throws a RuntimeException
    //noinspection DataFlowIssue
    return null;
  }

  /**
   * Reify a try/catch statement into an {@link Optional} where, after the {@link VoidSupplier#execute()} is invoked, if
   * there was not an {@link Throwable} exception thrown, an {@link Optional#empty()} is returned, otherwise if an
   * {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception satisfies the {@link Class#isInstance(Object)}
   * of one of the provided elements within {@code throwableClasses}, the exception is returned within the
   * {@link Optional#of}, otherwise, the non-fatal unrecognized exception is re-thrown.
   *
   * @param voidSupplier     function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                         invoked, if there is no exception thrown, the results are ignored
   * @param throwableClasses if the {@code voidSupplier} function throws an exception, and the exception satisfies the
   *                         {@link Class#isInstance} of one of the elements of this parameter (explicitly precluding
   *                         any exception where
   *                         {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)})
   *                         returns {@code true}, the exception is returned within the {@link Optional#of}
   * @param <T>              type of the {@link Throwable} instances being caught
   * @return a try/catch statement into an {@link Optional} where, after the {@link VoidSupplier#execute()} is invoked,
   *     if there was not an {@link Throwable} exception thrown, an {@link Optional#empty()} is returned, otherwise if
   *     an {@link Throwable} exception was thrown, if
   *     {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true},
   *     the fatal throwable is immediately re-thrown, otherwise if the exception satisfies the
   *     {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses}, the exception
   *     is returned within the {@link Optional#of}, otherwise, the non-fatal unrecognized exception is re-thrown
   */
  @SafeVarargs
  public static <T extends Throwable> Optional<T> wrap(
      VoidSupplier voidSupplier,
      Class<? extends T>... throwableClasses
  ) {
    Objects.requireNonNull(voidSupplier, "voidSupplier cannot be null");
    Objects.requireNonNull(throwableClasses, "throwableClasses cannot be null");
    try {
      voidSupplier.execute();

      return Optional.empty();
    } catch (Throwable throwable) {

      return Optional.of(resolveCatchThrowableWrappedCheckedException(throwable, throwableClasses));
    }
  }

  /**
   * After the {@link VoidSupplier#execute()} is invoked, if there was not an {@link Throwable} exception thrown, the
   * results are ignored, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception satisfies the {@link Class#isInstance(Object)}
   * of one of the provided elements within {@code throwableClasses}, the exception is re-thrown, otherwise, the
   * non-fatal unrecognized exception is re-thrown.
   *
   * @param voidSupplier     function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                         invoked, if there is no exception thrown, the results are ignored
   * @param throwableClasses if the {@code voidSupplier} function throws an exception, and the exception satisfies the
   *                         {@link Class#isInstance} of one of the elements of this parameter (explicitly precluding
   *                         any exception where
   *                         {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)})
   *                         returns {@code true}, the exception is returned within the {@link Optional#of}
   * @param <T>              type of the {@link Throwable} instances being caught
   */
  @SafeVarargs
  public static <T extends Throwable> void wrapOrThrow(
      VoidSupplier voidSupplier,
      Class<? extends T>... throwableClasses
  ) {
    Objects.requireNonNull(voidSupplier, "voidSupplier cannot be null");
    Objects.requireNonNull(throwableClasses, "throwableClasses cannot be null");
    wrap(voidSupplier, throwableClasses)
        .ifPresent(TryCatchesOps::throwRuntimeExceptionOrWrappedCheckedException);
  }

  /**
   * Reify a try/catch statement into an {@link Optional} where, after the {@link VoidSupplier#execute()} is invoked, if
   * there was not an {@link Throwable} exception thrown, an {@link Optional#empty()} is returned, otherwise if an
   * {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception is a {@link RuntimeException}, the exception
   * is returned within the {@link Optional#of}, otherwise, the non-fatal unrecognized exception is re-thrown.
   *
   * @param voidSupplier function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                     invoked, if there is no exception thrown, the results are ignored
   * @return a try/catch statement into an {@link Optional} where, after the {@link VoidSupplier#execute()} is invoked,
   *     if * there was not an {@link Throwable} exception thrown, an {@link Optional#empty()} is returned, otherwise if
   *     an * {@link Throwable} exception was thrown, if *
   *     {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns *
   *     {@code true}, the fatal throwable is immediately re-thrown, otherwise if the exception is a *
   *     {@link RuntimeException}, the exception is returned within the {@link Optional#of}, otherwise, the non-fatal *
   *     unrecognized exception is re-thrown
   */
  public static Optional<RuntimeException> wrap(
      VoidSupplier voidSupplier
  ) {
    Objects.requireNonNull(voidSupplier, "voidSupplier cannot be null");
    return wrap(voidSupplier, RuntimeException.class);
  }

  /**
   * After the {@link VoidSupplier#execute()} is invoked, if there was not an {@link Throwable} exception thrown, the
   * results are ignored, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception is a {@link RuntimeException}, the exception
   * is re-thrown, otherwise, the non-fatal unrecognized exception is re-thrown.
   *
   * @param voidSupplier function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                     invoked, if there is no exception thrown, the results are ignored
   */
  public static void wrapOrThrow(
      VoidSupplier voidSupplier
  ) {
    Objects.requireNonNull(voidSupplier, "voidSupplier cannot be null");
    wrap(voidSupplier)
        .ifPresent(runtimeException -> {

          //can only be a RuntimeException
          throw runtimeException;
        });
  }

  /**
   * Reify a try/catch statement into an {@link Either} where, after the {@link Supplier#get()} is invoked, if there was
   * not an {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within the right
   * side of an {@link Either}, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception satisfies the {@link Class#isInstance(Object)}
   * of one of the provided elements within {@code throwableClasses}, the exception is returned within the left side of
   * an {@link Either}, otherwise, the non-fatal unrecognized exception is re-thrown.
   *
   * @param supplier         function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                         invoked, if there is no exception thrown, the function's return value is returned within
   *                         the right side of an {@link Either}
   * @param throwableClasses if the {@code supplier} function throws an exception, and the exception satisfies the
   *                         {@link Class#isInstance} of one of the elements of this parameter (explicitly precluding
   *                         any exception where
   *                         {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)})
   *                         returns {@code true}, the exception is returned within the left side of an {@link Either}
   * @param <L>              type of the {@link Throwable} instances being caught
   * @param <R>              type of the instance provided by the {@link Supplier}
   * @return a try/catch statement into an {@link Either} where, after the {@link Supplier#get()} is invoked, if there
   *     was not an {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within
   *     the right side of an {@link Either}, otherwise if an {@link Throwable} exception was thrown, if
   *     {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true},
   *     the fatal throwable is immediately re-thrown, otherwise if the exception satisfies the
   *     {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses}, the exception
   *     is returned within the left side of an {@link Either}, otherwise, the non-fatal unrecognized exception is
   *     re-thrown
   */
  @SafeVarargs
  public static <L extends Throwable, R> Either<L, R> wrap(
      Supplier<R> supplier,
      Class<? extends L>... throwableClasses
  ) {
    Objects.requireNonNull(supplier, "supplier cannot be null");
    Objects.requireNonNull(throwableClasses, "throwableClasses cannot be null");
    try {

      return Either.right(supplier.get());
    } catch (Throwable throwable) {

      return Either.left(resolveCatchThrowableWrappedCheckedException(throwable, throwableClasses));
    }
  }

  /**
   * After the {@link Supplier#get()} is invoked, if there was not an {@link Throwable} exception thrown, the value
   * returned by the {@link Supplier} is returned, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception satisfies the {@link Class#isInstance(Object)}
   * of one of the provided elements within {@code throwableClasses}, the exception is returned within the left side of
   * an {@link Either}, otherwise, the non-fatal unrecognized exception is re-thrown.
   *
   * @param supplier         function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                         invoked, if there is no exception thrown, the function's return value is returned
   * @param throwableClasses if the {@code supplier} function throws an exception, and the exception satisfies the
   *                         {@link Class#isInstance} of one of the elements of this parameter (explicitly precluding
   *                         any exception where
   *                         {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)})
   *                         returns {@code true}, the exception is returned within the left side of an {@link Either}
   * @param <L>              type of the {@link Throwable} instances being caught
   * @param <R>              type of the instance provided by the {@link Supplier}
   * @return After the {@link Supplier#get()} is invoked, if there was not an {@link Throwable} exception thrown, the
   *     value returned by the {@link Supplier} is returned, otherwise if an {@link Throwable} exception was thrown, if
   *     {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true},
   *     the fatal throwable is immediately re-thrown, otherwise if the exception satisfies the
   *     {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses}, the exception
   *     is returned within the left side of an {@link Either}, otherwise, the non-fatal unrecognized exception is
   *     re-thrown
   */
  @SafeVarargs
  public static <L extends Throwable, R> R wrapOrThrow(
      Supplier<R> supplier,
      Class<? extends L>... throwableClasses
  ) {
    Objects.requireNonNull(supplier, "supplier cannot be null");
    Objects.requireNonNull(throwableClasses, "throwableClasses cannot be null");
    var either = wrap(supplier, throwableClasses);
    if (either.isLeft()) {
      var throwable = either.getLeft();

      throwRuntimeExceptionOrWrappedCheckedException(throwable);
    }

    return either.getRight();
  }

  /**
   * Reify a try/catch statement into an {@link Either} where, after the {@link Supplier#get()} is invoked, if there was
   * not an {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within the right
   * side of an {@link Either}, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception is a {@link RuntimeException}, the exception
   * is returned within the left side of an {@link Either}, otherwise, the non-fatal unrecognized exception is
   * re-thrown.
   *
   * @param supplier function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when invoked, if
   *                 there is no exception thrown, the function's return value is returned within the right side of an
   *                 {@link Either}
   * @param <R>      type of the instance provided by the {@link Supplier}
   * @return a try/catch statement into an {@link Either} where, after the {@link Supplier#get()} is invoked, if there
   *     was not an {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within
   *     the right side of an {@link Either}, otherwise if an {@link Throwable} exception was thrown, if
   *     {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true},
   *     the fatal throwable is immediately re-thrown, otherwise if the exception is a {@link RuntimeException}, the
   *     exception is returned within the left side of an {@link Either}, otherwise, the non-fatal unrecognized
   *     exception is re-thrown
   */
  public static <R> Either<RuntimeException, R> wrap(
      Supplier<R> supplier
  ) {
    Objects.requireNonNull(supplier, "supplier cannot be null");
    return wrap(supplier, RuntimeException.class);
  }

  /**
   * After the {@link Supplier#get()} is invoked, if there was not an {@link Throwable} exception thrown, the value
   * returned by the {@link Supplier} is returned, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception is a {@link RuntimeException}, the exception
   * is returned within the left side of an {@link Either}, otherwise, the non-fatal unrecognized exception is
   * re-thrown.
   *
   * @param supplier function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when invoked, if
   *                 there is no exception thrown, the function's return value is returned
   * @param <R>      type of the instance provided by the {@link Supplier}
   * @return After the {@link Supplier#get()} is invoked, if there was not an {@link Throwable} exception thrown, the
   *     value returned by the {@link Supplier} is returned, otherwise if an {@link Throwable} exception was thrown, if
   *     {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true},
   *     the fatal throwable is immediately re-thrown, otherwise if the exception is a {@link RuntimeException}, the
   *     exception is returned within the left side of an {@link Either}, otherwise, the non-fatal unrecognized
   *     exception is re-thrown
   */
  public static <R> R wrapOrThrow(
      Supplier<R> supplier
  ) {
    Objects.requireNonNull(supplier, "supplier cannot be null");
    var either = wrap(supplier);
    if (either.isLeft()) {

      //can only be a RuntimeException
      throw either.getLeft();
    }

    return either.getRight();
  }

  /**
   * Reify a try/catch statement into an {@link Optional} where, after the
   * {@link VoidSupplierCheckedException#execute()} is invoked, if there was not an {@link Throwable} exception thrown,
   * an {@link Optional#empty()} is returned, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception satisfies the {@link Class#isInstance(Object)}
   * of one of the provided elements within {@code throwableClasses}, the exception is returned within the
   * {@link Optional#of}, otherwise if the exception is a {@link RuntimeException}, it is re-thrown, otherwise a new
   * {@link WrappedCheckedException} wrapping the non-fatal unrecognized exception is re-thrown.
   *
   * @param voidSupplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block,
   *                                     that when invoked, if there is no exception thrown, the results are ignored
   * @param throwableClasses             if the {@code voidSupplierCheckedException} function throws an exception, and
   *                                     the exception satisfies the {@link Class#isInstance} of one of the elements of
   *                                     this parameter (explicitly precluding any exception where
   *                                     {@link WrappedCheckedException#isFatal(Throwable)
   *                                     WrappedCheckedException.isFatal(...)}) returns {@code true}, the exception is
   *                                     returned within the {@link Optional#of}
   * @param <T>                          type of the {@link Throwable} instances being caught
   * @return a try/catch statement into an {@link Optional} where, after the
   *     {@link VoidSupplierCheckedException#execute()} is invoked, if there was not an {@link Throwable} exception
   *     thrown, an {@link Optional#empty()} is returned, otherwise if an {@link Throwable} exception was thrown, if
   *     {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true},
   *     the fatal throwable is immediately re-thrown, otherwise if the exception satisfies the
   *     {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses}, the exception
   *     is returned within the {@link Optional#of}, otherwise if the exception is a {@link RuntimeException}, it is
   *     re-thrown, otherwise a new {@link WrappedCheckedException} wrapping the non-fatal unrecognized exception is
   *     re-thrown
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  @SafeVarargs
  public static <T extends Throwable> Optional<T> wrapCheckedException(
      VoidSupplierCheckedException voidSupplierCheckedException,
      Class<? extends T>... throwableClasses
  ) {
    Objects.requireNonNull(voidSupplierCheckedException, "voidSupplierCheckedException cannot be null");
    Objects.requireNonNull(throwableClasses, "throwableClasses cannot be null");
    try {
      voidSupplierCheckedException.execute();

      return Optional.empty();
    } catch (Throwable throwable) {

      return Optional.of(resolveCatchThrowableWrappedCheckedException(throwable, throwableClasses));
    }
  }

  /**
   * After the {@link VoidSupplierCheckedException#execute()} is invoked, if there was not an {@link Throwable}
   * exception thrown, the results are ignored, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception satisfies the {@link Class#isInstance(Object)}
   * of one of the provided elements within {@code throwableClasses}, the exception is returned within the
   * {@link Optional#of}, otherwise if the exception is a {@link RuntimeException}, it is re-thrown, otherwise a new
   * {@link WrappedCheckedException} wrapping the non-fatal unrecognized exception is re-thrown.
   *
   * @param voidSupplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block,
   *                                     that when invoked, if there is no exception thrown, the results are ignored
   * @param throwableClasses             if the {@code voidSupplierCheckedException} function throws an exception, and
   *                                     the exception satisfies the {@link Class#isInstance} of one of the elements of
   *                                     this parameter (explicitly precluding any exception where
   *                                     {@link WrappedCheckedException#isFatal(Throwable)
   *                                     WrappedCheckedException.isFatal(...)}) returns {@code true}, the exception is
   *                                     returned within the {@link Optional#of}
   * @param <T>                          type of the {@link Throwable} instances being caught
   */
  @SafeVarargs
  public static <T extends Throwable> void wrapCheckedExceptionOrThrow(
      VoidSupplierCheckedException voidSupplierCheckedException,
      Class<? extends T>... throwableClasses
  ) {
    Objects.requireNonNull(voidSupplierCheckedException, "voidSupplierCheckedException cannot be null");
    Objects.requireNonNull(throwableClasses, "throwableClasses cannot be null");
    wrapCheckedException(voidSupplierCheckedException, throwableClasses)
        .ifPresent(TryCatchesOps::throwRuntimeExceptionOrWrappedCheckedException);
  }

  /**
   * Reify a try/catch statement into an {@link Optional} where, after the
   * {@link VoidSupplierCheckedException#execute()} is invoked, if there was not an {@link Throwable} exception thrown,
   * an {@link Optional#empty()} is returned, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception is a {@link RuntimeException}, the exception
   * is returned within the {@link Optional#of}, otherwise a new {@link WrappedCheckedException} wrapping the non-fatal
   * unrecognized exception is re-thrown.
   *
   * @param voidSupplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block,
   *                                     that when invoked, if there is no exception thrown, the results are ignored
   * @return a try/catch statement into an {@link Optional} where, after the
   *     {@link VoidSupplierCheckedException#execute()} is invoked, if there is no {@link Throwable} exception thrown,
   *     an {@link Optional#empty()} is returned, otherwise if an {@link Throwable} exception is thrown and the
   *     exception is a {@link RuntimeException}, the exception is returned within the {@link Optional#of}, otherwise,
   *     if the unrecognized exception is an instance of {@link RuntimeException}, it is re-thrown, otherwise a new
   *     {@link WrappedCheckedException} wrapping the unrecognized exception is thrown.
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static Optional<RuntimeException> wrapCheckedException(
      VoidSupplierCheckedException voidSupplierCheckedException
  ) {
    Objects.requireNonNull(voidSupplierCheckedException, "voidSupplierCheckedException cannot be null");
    return wrapCheckedException(voidSupplierCheckedException, RuntimeException.class);
  }

  /**
   * After the {@link VoidSupplierCheckedException#execute()} is invoked, if there was not an {@link Throwable}
   * exception thrown, the results are ignored, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception is a {@link RuntimeException}, the exception
   * is returned within the {@link Optional#of}, otherwise a new {@link WrappedCheckedException} wrapping the non-fatal
   * unrecognized exception is re-thrown.
   *
   * @param voidSupplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block,
   *                                     that when invoked, if there is no exception thrown, the results are ignored
   */
  public static void wrapCheckedExceptionOrThrow(
      VoidSupplierCheckedException voidSupplierCheckedException
  ) {
    Objects.requireNonNull(voidSupplierCheckedException, "voidSupplierCheckedException cannot be null");
    wrapCheckedException(voidSupplierCheckedException)
        .ifPresent(runtimeException -> {

          //can only be a RuntimeException
          throw runtimeException;
        });
  }

  /**
   * Reify a try/catch statement into an {@link Either} where, after the {@link SupplierCheckedException#get()} is
   * invoked, if there was not an {@link Throwable} exception thrown, the value returned by the {@link Supplier} is
   * returned within the right side of an {@link Either}, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception satisfies the {@link Class#isInstance(Object)}
   * of one of the provided elements within {@code throwableClasses}, the exception is returned within the left side of
   * an {@link Either}, otherwise if the exception is a {@link RuntimeException}, it is re-thrown, otherwise a new
   * {@link WrappedCheckedException} wrapping the non-fatal unrecognized exception is re-thrown.
   *
   * @param supplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that
   *                                 when invoked, if there is no exception thrown, the function's return value is
   *                                 returned within the right side of an {@link Either}
   * @param throwableClasses         if the {@code supplierCheckedException} function throws an exception, and the
   *                                 exception satisfies the {@link Class#isInstance} of one of the elements of this
   *                                 parameter (explicitly precluding any exception where
   *                                 {@link WrappedCheckedException#isFatal(Throwable)
   *                                 WrappedCheckedException.isFatal(...)}) returns {@code true}, the exception is
   *                                 returned within the left side of an {@link Either}
   * @param <L>                      type of the {@link Throwable} instances being caught
   * @param <R>                      type of the instance provided by the {@link SupplierCheckedException}
   * @return a try/catch statement into an {@link Either} where, after the {@link SupplierCheckedException#get()} is
   *     invoked, if there was not an {@link Throwable} exception thrown, the value returned by the {@link Supplier} is
   *     returned within the right side of an {@link Either}, otherwise if an {@link Throwable} exception was thrown, if
   *     {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true},
   *     the fatal throwable is immediately re-thrown, otherwise if the exception satisfies the
   *     {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses}, the exception
   *     is returned within the left side of an {@link Either}, otherwise a new {@link WrappedCheckedException} wrapping
   *     the non-fatal unrecognized exception is re-thrown
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  @SafeVarargs
  public static <L extends Throwable, R> Either<L, R> wrapCheckedException(
      SupplierCheckedException<R> supplierCheckedException,
      Class<? extends L>... throwableClasses
  ) {
    Objects.requireNonNull(supplierCheckedException, "supplierCheckedException cannot be null");
    Objects.requireNonNull(throwableClasses, "throwableClasses cannot be null");
    try {

      return Either.right(supplierCheckedException.get());
    } catch (Throwable throwable) {

      return Either.left(resolveCatchThrowableWrappedCheckedException(throwable, throwableClasses));
    }
  }

  /**
   * After the {@link SupplierCheckedException#get()} is invoked, if there was not an {@link Throwable} exception
   * thrown, the value returned by the {@link Supplier} is returned, otherwise if an {@link Throwable} exception was
   * thrown, if {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns
   * {@code true}, the fatal throwable is immediately re-thrown, otherwise if the exception satisfies the
   * {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses}, the exception is
   * returned within the left side of an {@link Either}, otherwise if the exception is a {@link RuntimeException}, it is
   * re-thrown, otherwise a new {@link WrappedCheckedException} wrapping the non-fatal unrecognized exception is
   * re-thrown.
   *
   * @param supplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that
   *                                 when invoked, if there is no exception thrown, the function's return value is
   *                                 returned
   * @param throwableClasses         if the {@code supplierCheckedException} function throws an exception, and the
   *                                 exception satisfies the {@link Class#isInstance} of one of the elements of this
   *                                 parameter (explicitly precluding any exception where
   *                                 {@link WrappedCheckedException#isFatal(Throwable)
   *                                 WrappedCheckedException.isFatal(...)}) returns {@code true}, the exception is
   *                                 returned within the left side of an {@link Either}
   * @param <L>                      type of the {@link Throwable} instances being caught
   * @param <R>                      type of the instance provided by the {@link SupplierCheckedException}
   * @return After the {@link SupplierCheckedException#get()} is invoked, if there was not an {@link Throwable}
   *     exception thrown, the value returned by the {@link Supplier} is returned, otherwise if an {@link Throwable}
   *     exception was thrown, if
   *     {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true},
   *     the fatal throwable is immediately re-thrown, otherwise if the exception satisfies the
   *     {@link Class#isInstance(Object)} of one of the provided elements within {@code throwableClasses}, the exception
   *     is returned within the left side of an {@link Either}, otherwise if the exception is a
   *     {@link RuntimeException}, it is re-thrown, otherwise a new {@link WrappedCheckedException} wrapping the
   *     non-fatal unrecognized exception is re-thrown
   */
  @SafeVarargs
  public static <L extends Throwable, R> R wrapCheckedExceptionOrThrow(
      SupplierCheckedException<R> supplierCheckedException,
      Class<? extends L>... throwableClasses
  ) {
    Objects.requireNonNull(supplierCheckedException, "supplierCheckedException cannot be null");
    Objects.requireNonNull(throwableClasses, "throwableClasses cannot be null");
    var either = wrapCheckedException(supplierCheckedException, throwableClasses);
    if (either.isLeft()) {
      var throwable = either.getLeft();

      throwRuntimeExceptionOrWrappedCheckedException(throwable);
    }

    return either.getRight();

  }

  /**
   * Reify a try/catch statement into an {@link Either} where, after the {@link SupplierCheckedException#get()} is
   * invoked, if there was not an {@link Throwable} exception thrown, the value returned by the {@link Supplier} is
   * returned within the right side of an {@link Either}, otherwise if an {@link Throwable} exception was thrown, if
   * {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true}, the
   * fatal throwable is immediately re-thrown, otherwise if the exception is a {@link RuntimeException}, the exception
   * is returned within the left side of an {@link Either}, otherwise a new {@link WrappedCheckedException} wrapping the
   * non-fatal unrecognized exception is re-thrown.
   *
   * @param supplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that
   *                                 when invoked, if there is no exception thrown, the function's return value is
   *                                 returned within the right side of an {@link Either}
   * @param <R>                      type of the instance provided by the {@link SupplierCheckedException}
   * @return a try/catch statement into an {@link Either} where, after the {@link SupplierCheckedException#get()} is
   *     invoked, if there was not an {@link Throwable} exception thrown, the value returned by the {@link Supplier} is
   *     returned within the right side of an {@link Either}, otherwise if an {@link Throwable} exception was thrown, if
   *     {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true},
   *     the fatal throwable is immediately re-thrown, otherwise if the exception is a {@link RuntimeException}, the
   *     exception is returned within the left side of an {@link Either}, otherwise a new
   *     {@link WrappedCheckedException} wrapping the non-fatal unrecognized exception is re-thrown
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <R> Either<RuntimeException, R> wrapCheckedException(
      SupplierCheckedException<R> supplierCheckedException
  ) {
    Objects.requireNonNull(supplierCheckedException, "supplierCheckedException cannot be null");
    return wrapCheckedException(supplierCheckedException, RuntimeException.class);
  }

  /**
   * After the {@link SupplierCheckedException#get()} is invoked, if there was not an {@link Throwable} exception
   * thrown, the value returned by the {@link Supplier} is returned, otherwise if an {@link Throwable} exception was
   * thrown, if {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns
   * {@code true}, the fatal throwable is immediately re-thrown, otherwise if the exception is a
   * {@link RuntimeException}, the exception is returned within the left side of an {@link Either}, otherwise a new
   * {@link WrappedCheckedException} wrapping the non-fatal unrecognized exception is re-thrown.
   *
   * @param supplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that
   *                                 when invoked, if there is no exception thrown, the function's return value is
   *                                 returned
   * @param <R>                      type of the instance provided by the {@link SupplierCheckedException}
   * @return After the {@link SupplierCheckedException#get()} is invoked, if there was not an {@link Throwable}
   *     exception thrown, the value returned by the {@link Supplier} is returned, otherwise if an {@link Throwable}
   *     exception was thrown, if
   *     {@link WrappedCheckedException#isFatal(Throwable) WrappedCheckedException.isFatal(...)}) returns {@code true},
   *     the fatal throwable is immediately re-thrown, otherwise if the exception is a {@link RuntimeException}, the
   *     exception is returned within the left side of an {@link Either}, otherwise a new
   *     {@link WrappedCheckedException} wrapping the non-fatal unrecognized exception is re-thrown
   */
  public static <R> R wrapCheckedExceptionOrThrow(
      SupplierCheckedException<R> supplierCheckedException
  ) {
    Objects.requireNonNull(supplierCheckedException, "supplierCheckedException cannot be null");
    var either = wrapCheckedException(supplierCheckedException);
    if (either.isLeft()) {

      //can only be a RuntimeException
      throw either.getLeft();
    }

    return either.getRight();
  }
}
