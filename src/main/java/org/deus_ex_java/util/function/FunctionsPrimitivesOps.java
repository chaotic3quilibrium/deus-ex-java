package org.deus_ex_java.util.function;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.deus_ex_java.util.TryCatchesOps;
import org.jspecify.annotations.NullMarked;

import java.util.function.*;
import java.util.stream.Stream;

/**
 * Extends and enhances Java's Lambda Library, specifically for the method type signatures dependent upon primitives.
 * <p>
 * ---
 * <p>
 * Useful Reference: <a
 * href="https://docs.google.com/spreadsheets/d/1Xljq5x9alDwSHZTY1nkBxDAwF4MKX5x2zy6XD-x2zVk/edit?usp=sharing">Java
 * Lambda Reference Table V2</a>
 */
@NullMarked
public final class FunctionsPrimitivesOps {

  private FunctionsPrimitivesOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns a {@link BooleanSupplier} that wraps the checked exception lambda, {@code booleanSupplierCheckedException},
   * with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   * {@link Stream} operations.
   *
   * @param booleanSupplierCheckedException the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @return a {@link BooleanSupplier} that wraps the checked exception lambda, {@code booleanSupplierCheckedException},
   *     with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   *     {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static BooleanSupplier wrapCheckedException(
      BooleanSupplierCheckedException booleanSupplierCheckedException
  ) {
    return wrapCheckedException(booleanSupplierCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link BooleanSupplier} that wraps the checked exception lambda, {@code booleanSupplierCheckedException},
   * with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   * lambda within {@link Stream} operations.
   *
   * @param booleanSupplierCheckedException the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper        the supplier of the RuntimeException descendant instance within which to
   *                                        wrap the checked exception, if thrown
   * @param <EX>                            the type of the RuntimeException descendant instance within which to wrap
   *                                        the checked exception, if thrown
   * @return a {@link BooleanSupplier} that wraps the checked exception lambda, {@code booleanSupplierCheckedException},
   *     with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of
   *     the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> BooleanSupplier wrapCheckedException(
      BooleanSupplierCheckedException booleanSupplierCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return () ->
        TryCatchesOps.wrapCheckedException(booleanSupplierCheckedException::getAsBoolean)
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link DoubleBinaryOperator} that wraps the checked exception lambda,
   * {@code doubleBinaryOperatorCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param doubleBinaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                             with a {@link RuntimeException}
   * @return a {@link DoubleBinaryOperator} that wraps the checked exception lambda,
   *     {@code doubleBinaryOperatorCheckedException}, with a {@link RuntimeException} of
   *     {@link WrappedCheckedException} to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static DoubleBinaryOperator wrapCheckedException(
      DoubleBinaryOperatorCheckedException doubleBinaryOperatorCheckedException
  ) {
    return wrapCheckedException(doubleBinaryOperatorCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link DoubleBinaryOperator} that wraps the checked exception lambda,
   * {@code doubleBinaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param doubleBinaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                             with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper             the supplier of the RuntimeException descendant instance within which
   *                                             to wrap the checked exception, if thrown
   * @param <EX>                                 the type of the RuntimeException descendant instance within which to
   *                                             wrap the checked exception, if thrown
   * @return a {@link DoubleBinaryOperator} that wraps the checked exception lambda,
   *     {@code doubleBinaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> DoubleBinaryOperator wrapCheckedException(
      DoubleBinaryOperatorCheckedException doubleBinaryOperatorCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (left, right) ->
        TryCatchesOps.wrapCheckedException(() ->
                doubleBinaryOperatorCheckedException.applyAsDouble(left, right))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link DoubleConsumer} that wraps the checked exception lambda, {@code doubleConsumerCheckedException},
   * with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   * {@link Stream} operations.
   *
   * @param doubleConsumerCheckedException the lambda which may throw a checked exception that needs to be wrapped with
   *                                       a {@link RuntimeException}
   * @return a {@link DoubleConsumer} that wraps the checked exception lambda, {@code doubleConsumerCheckedException},
   *     with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   *     {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static DoubleConsumer wrapCheckedException(
      DoubleConsumerCheckedException doubleConsumerCheckedException
  ) {
    return wrapCheckedException(doubleConsumerCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link DoubleConsumer} that wraps the checked exception lambda, {@code doubleConsumerCheckedException},
   * with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   * lambda within {@link Stream} operations.
   *
   * @param doubleConsumerCheckedException the lambda which may throw a checked exception that needs to be wrapped with
   *                                       a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper       the supplier of the RuntimeException descendant instance within which to wrap
   *                                       the checked exception, if thrown
   * @param <EX>                           the type of the RuntimeException descendant instance within which to wrap the
   *                                       checked exception, if thrown
   * @return a {@link DoubleConsumer} that wraps the checked exception lambda, {@code doubleConsumerCheckedException},
   *     with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of
   *     the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> DoubleConsumer wrapCheckedException(
      DoubleConsumerCheckedException doubleConsumerCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) -> {
      try {
        doubleConsumerCheckedException.accept(t);
      } catch (Exception exception) {
        throw fRuntimeExceptionWrapper.apply(exception);
      }
    };
  }

  /**
   * Returns a {@link DoubleFunction} that wraps the checked exception lambda, {@code doubleFunctionCheckedException},
   * with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   * {@link Stream} operations.
   *
   * @param doubleFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped with
   *                                       a {@link RuntimeException}
   * @param <R>                            the type of the result returned by the function
   * @return a {@link DoubleFunction} that wraps the checked exception lambda, {@code doubleFunctionCheckedException},
   *     with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   *     {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <R> DoubleFunction<R> wrapCheckedException(
      DoubleFunctionCheckedException<R> doubleFunctionCheckedException
  ) {
    return wrapCheckedException(doubleFunctionCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link DoubleFunction} that wraps the checked exception lambda, {@code doubleFunctionCheckedException},
   * with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   * lambda within {@link Stream} operations.
   *
   * @param doubleFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped with
   *                                       a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper       the supplier of the RuntimeException descendant instance within which to wrap
   *                                       the checked exception, if thrown
   * @param <EX>                           the type of the RuntimeException descendant instance within which to wrap the
   *                                       checked exception, if thrown
   * @param <R>                            the type of the result returned by the function
   * @return a {@link DoubleFunction} that wraps the checked exception lambda, {@code doubleFunctionCheckedException},
   *     with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of
   *     the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, R> DoubleFunction<R> wrapCheckedException(
      DoubleFunctionCheckedException<R> doubleFunctionCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                doubleFunctionCheckedException.apply(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link DoublePredicate} that wraps the checked exception lambda, {@code doublePredicateCheckedException},
   * with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   * {@link Stream} operations.
   *
   * @param doublePredicateCheckedException the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @return a {@link DoublePredicate} that wraps the checked exception lambda, {@code doublePredicateCheckedException},
   *     with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   *     {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static DoublePredicate wrapCheckedException(
      DoublePredicateCheckedException doublePredicateCheckedException
  ) {
    return wrapCheckedException(doublePredicateCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link DoublePredicate} that wraps the checked exception lambda, {@code doublePredicateCheckedException},
   * with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   * lambda within {@link Stream} operations.
   *
   * @param doublePredicateCheckedException the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper        the supplier of the RuntimeException descendant instance within which to
   *                                        wrap the checked exception, if thrown
   * @param <EX>                            the type of the RuntimeException descendant instance within which to wrap
   *                                        the checked exception, if thrown
   * @return a {@link DoublePredicate} that wraps the checked exception lambda, {@code doublePredicateCheckedException},
   *     with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of
   *     the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> DoublePredicate wrapCheckedException(
      DoublePredicateCheckedException doublePredicateCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                doublePredicateCheckedException.test(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link DoubleSupplier} that wraps the checked exception lambda, {@code doubleSupplierCheckedException},
   * with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   * {@link Stream} operations.
   *
   * @param doubleSupplierCheckedException the lambda which may throw a checked exception that needs to be wrapped with
   *                                       a {@link RuntimeException}
   * @return a {@link DoubleSupplier} that wraps the checked exception lambda, {@code doubleSupplierCheckedException},
   *     with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   *     {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static DoubleSupplier wrapCheckedException(
      DoubleSupplierCheckedException doubleSupplierCheckedException
  ) {
    return wrapCheckedException(doubleSupplierCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link DoubleSupplier} that wraps the checked exception lambda, {@code doubleSupplierCheckedException},
   * with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   * lambda within {@link Stream} operations.
   *
   * @param doubleSupplierCheckedException the lambda which may throw a checked exception that needs to be wrapped with
   *                                       a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper       the supplier of the RuntimeException descendant instance within which to wrap
   *                                       the checked exception, if thrown
   * @param <EX>                           the type of the RuntimeException descendant instance within which to wrap the
   *                                       checked exception, if thrown
   * @return a {@link DoubleSupplier} that wraps the checked exception lambda, {@code doubleSupplierCheckedException},
   *     with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of
   *     the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> DoubleSupplier wrapCheckedException(
      DoubleSupplierCheckedException doubleSupplierCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return () ->
        TryCatchesOps.wrapCheckedException(doubleSupplierCheckedException::getAsDouble)
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link DoubleToIntFunction} that wraps the checked exception lambda,
   * {@code doubleToIntFunctionCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param doubleToIntFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                            with a {@link RuntimeException}
   * @return a {@link DoubleToIntFunction} that wraps the checked exception lambda,
   *     {@code doubleToIntFunctionCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static DoubleToIntFunction wrapCheckedException(
      DoubleToIntFunctionCheckedException doubleToIntFunctionCheckedException
  ) {
    return wrapCheckedException(doubleToIntFunctionCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link DoubleToIntFunction} that wraps the checked exception lambda,
   * {@code doubleToIntFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param doubleToIntFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                            with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper            the supplier of the RuntimeException descendant instance within which to
   *                                            wrap the checked exception, if thrown
   * @param <EX>                                the type of the RuntimeException descendant instance within which to
   *                                            wrap the checked exception, if thrown
   * @return a {@link DoubleToIntFunction} that wraps the checked exception lambda,
   *     {@code doubleToIntFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> DoubleToIntFunction wrapCheckedException(
      DoubleToIntFunctionCheckedException doubleToIntFunctionCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                doubleToIntFunctionCheckedException.applyAsInt(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link DoubleToLongFunction} that wraps the checked exception lambda,
   * {@code doubleToLongFunctionCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param doubleToLongFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                             with a {@link RuntimeException}
   * @return a {@link DoubleToLongFunction} that wraps the checked exception lambda,
   *     {@code doubleToLongFunctionCheckedException}, with a {@link RuntimeException} of
   *     {@link WrappedCheckedException} to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static DoubleToLongFunction wrapCheckedException(
      DoubleToLongFunctionCheckedException doubleToLongFunctionCheckedException
  ) {
    return wrapCheckedException(doubleToLongFunctionCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link DoubleToLongFunction} that wraps the checked exception lambda,
   * {@code doubleToLongFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param doubleToLongFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                             with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper             the supplier of the RuntimeException descendant instance within which
   *                                             to wrap the checked exception, if thrown
   * @param <EX>                                 the type of the RuntimeException descendant instance within which to
   *                                             wrap the checked exception, if thrown
   * @return a {@link DoubleToLongFunction} that wraps the checked exception lambda,
   *     {@code doubleToLongFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> DoubleToLongFunction wrapCheckedException(
      DoubleToLongFunctionCheckedException doubleToLongFunctionCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                doubleToLongFunctionCheckedException.applyAsLong(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link DoubleUnaryOperator} that wraps the checked exception lambda,
   * {@code doubleUnaryOperatorCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param doubleUnaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                            with a {@link RuntimeException}
   * @return a {@link DoubleUnaryOperator} that wraps the checked exception lambda,
   *     {@code doubleUnaryOperatorCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static DoubleUnaryOperator wrapCheckedException(
      DoubleUnaryOperatorCheckedException doubleUnaryOperatorCheckedException
  ) {
    return wrapCheckedException(doubleUnaryOperatorCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link DoubleUnaryOperator} that wraps the checked exception lambda,
   * {@code doubleUnaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param doubleUnaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                            with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper            the supplier of the RuntimeException descendant instance within which to
   *                                            wrap the checked exception, if thrown
   * @param <EX>                                the type of the RuntimeException descendant instance within which to
   *                                            wrap the checked exception, if thrown
   * @return a {@link DoubleUnaryOperator} that wraps the checked exception lambda,
   *     {@code doubleUnaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> DoubleUnaryOperator wrapCheckedException(
      DoubleUnaryOperatorCheckedException doubleUnaryOperatorCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                doubleUnaryOperatorCheckedException.applyAsDouble(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link IntBinaryOperator} that wraps the checked exception lambda,
   * {@code intBinaryOperatorCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param intBinaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                          with a {@link RuntimeException}
   * @return a {@link IntBinaryOperator} that wraps the checked exception lambda,
   *     {@code intBinaryOperatorCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static IntBinaryOperator wrapCheckedException(
      IntBinaryOperatorCheckedException intBinaryOperatorCheckedException
  ) {
    return wrapCheckedException(intBinaryOperatorCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link IntBinaryOperator} that wraps the checked exception lambda,
   * {@code intBinaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param intBinaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                          with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper          the supplier of the RuntimeException descendant instance within which to
   *                                          wrap the checked exception, if thrown
   * @param <EX>                              the type of the RuntimeException descendant instance within which to wrap
   *                                          the checked exception, if thrown
   * @return a {@link IntBinaryOperator} that wraps the checked exception lambda,
   *     {@code intBinaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> IntBinaryOperator wrapCheckedException(
      IntBinaryOperatorCheckedException intBinaryOperatorCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (left, right) ->
        TryCatchesOps.wrapCheckedException(() ->
                intBinaryOperatorCheckedException.applyAsInt(left, right))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link IntConsumer} that wraps the checked exception lambda, {@code intConsumerCheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param intConsumerCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                    {@link RuntimeException}
   * @return a {@link IntConsumer} that wraps the checked exception lambda, {@code intConsumerCheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static IntConsumer wrapCheckedException(
      IntConsumerCheckedException intConsumerCheckedException
  ) {
    return wrapCheckedException(intConsumerCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link IntConsumer} that wraps the checked exception lambda, {@code intConsumerCheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param intConsumerCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                    {@link RuntimeException}
   * @param fRuntimeExceptionWrapper    the supplier of the RuntimeException descendant instance within which to wrap
   *                                    the checked exception, if thrown
   * @param <EX>                        the type of the RuntimeException descendant instance within which to wrap the
   *                                    checked exception, if thrown
   * @return a {@link IntConsumer} that wraps the checked exception lambda, {@code intConsumerCheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> IntConsumer wrapCheckedException(
      IntConsumerCheckedException intConsumerCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) -> {
      try {
        intConsumerCheckedException.accept(t);
      } catch (Exception exception) {
        throw fRuntimeExceptionWrapper.apply(exception);
      }
    };
  }

  /**
   * Returns a {@link IntFunction} that wraps the checked exception lambda, {@code intFunctionCheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param intFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                    {@link RuntimeException}
   * @param <R>                         the type of the result returned by the function
   * @return a {@link IntFunction} that wraps the checked exception lambda, {@code intFunctionCheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <R> IntFunction<R> wrapCheckedException(
      IntFunctionCheckedException<R> intFunctionCheckedException
  ) {
    return wrapCheckedException(intFunctionCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link IntFunction} that wraps the checked exception lambda, {@code intFunctionCheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param intFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                    {@link RuntimeException}
   * @param fRuntimeExceptionWrapper    the supplier of the RuntimeException descendant instance within which to wrap
   *                                    the checked exception, if thrown
   * @param <EX>                        the type of the RuntimeException descendant instance within which to wrap the
   *                                    checked exception, if thrown
   * @param <R>                         the type of the result returned by the function
   * @return a {@link IntFunction} that wraps the checked exception lambda, {@code intFunctionCheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, R> IntFunction<R> wrapCheckedException(
      IntFunctionCheckedException<R> intFunctionCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                intFunctionCheckedException.apply(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link IntPredicate} that wraps the checked exception lambda, {@code intPredicateCheckedException}, with
   * a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param intPredicateCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @return a {@link IntPredicate} that wraps the checked exception lambda, {@code intPredicateCheckedException}, with
   *     a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static IntPredicate wrapCheckedException(
      IntPredicateCheckedException intPredicateCheckedException
  ) {
    return wrapCheckedException(intPredicateCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link IntPredicate} that wraps the checked exception lambda, {@code intPredicateCheckedException}, with
   * a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param intPredicateCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @param fRuntimeExceptionWrapper     the supplier of the RuntimeException descendant instance within which to wrap
   *                                     the checked exception, if thrown
   * @param <EX>                         the type of the RuntimeException descendant instance within which to wrap the
   *                                     checked exception, if thrown
   * @return a {@link IntPredicate} that wraps the checked exception lambda, {@code intPredicateCheckedException}, with
   *     a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> IntPredicate wrapCheckedException(
      IntPredicateCheckedException intPredicateCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                intPredicateCheckedException.test(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link IntSupplier} that wraps the checked exception lambda, {@code intSupplierCheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param intSupplierCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                    {@link RuntimeException}
   * @return a {@link IntSupplier} that wraps the checked exception lambda, {@code intSupplierCheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static IntSupplier wrapCheckedException(
      IntSupplierCheckedException intSupplierCheckedException
  ) {
    return wrapCheckedException(intSupplierCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link IntSupplier} that wraps the checked exception lambda, {@code intSupplierCheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param intSupplierCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                    {@link RuntimeException}
   * @param fRuntimeExceptionWrapper    the supplier of the RuntimeException descendant instance within which to wrap
   *                                    the checked exception, if thrown
   * @param <EX>                        the type of the RuntimeException descendant instance within which to wrap the
   *                                    checked exception, if thrown
   * @return a {@link IntSupplier} that wraps the checked exception lambda, {@code intSupplierCheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> IntSupplier wrapCheckedException(
      IntSupplierCheckedException intSupplierCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return () ->
        TryCatchesOps.wrapCheckedException(intSupplierCheckedException::getAsInt)
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link IntToDoubleFunction} that wraps the checked exception lambda,
   * {@code intToDoubleFunctionCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param intToDoubleFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                            with a {@link RuntimeException}
   * @return a {@link IntToDoubleFunction} that wraps the checked exception lambda,
   *     {@code intToDoubleFunctionCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static IntToDoubleFunction wrapCheckedException(
      IntToDoubleFunctionCheckedException intToDoubleFunctionCheckedException
  ) {
    return wrapCheckedException(intToDoubleFunctionCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link IntToDoubleFunction} that wraps the checked exception lambda,
   * {@code intToDoubleFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param intToDoubleFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                            with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper            the supplier of the RuntimeException descendant instance within which to
   *                                            wrap the checked exception, if thrown
   * @param <EX>                                the type of the RuntimeException descendant instance within which to
   *                                            wrap the checked exception, if thrown
   * @return a {@link IntToDoubleFunction} that wraps the checked exception lambda,
   *     {@code intToDoubleFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> IntToDoubleFunction wrapCheckedException(
      IntToDoubleFunctionCheckedException intToDoubleFunctionCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                intToDoubleFunctionCheckedException.applyAsDouble(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link IntToLongFunction} that wraps the checked exception lambda,
   * {@code intToLongFunctionCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param intToLongFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                          with a {@link RuntimeException}
   * @return a {@link IntToLongFunction} that wraps the checked exception lambda,
   *     {@code intToLongFunctionCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static IntToLongFunction wrapCheckedException(
      IntToLongFunctionCheckedException intToLongFunctionCheckedException
  ) {
    return wrapCheckedException(intToLongFunctionCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link IntToLongFunction} that wraps the checked exception lambda,
   * {@code intToLongFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param intToLongFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                          with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper          the supplier of the RuntimeException descendant instance within which to
   *                                          wrap the checked exception, if thrown
   * @param <EX>                              the type of the RuntimeException descendant instance within which to wrap
   *                                          the checked exception, if thrown
   * @return a {@link IntToLongFunction} that wraps the checked exception lambda,
   *     {@code intToLongFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> IntToLongFunction wrapCheckedException(
      IntToLongFunctionCheckedException intToLongFunctionCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                intToLongFunctionCheckedException.applyAsLong(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link IntUnaryOperator} that wraps the checked exception lambda,
   * {@code intUnaryOperatorCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param intUnaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                         with a {@link RuntimeException}
   * @return a {@link IntUnaryOperator} that wraps the checked exception lambda,
   *     {@code intUnaryOperatorCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   *     enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static IntUnaryOperator wrapCheckedException(
      IntUnaryOperatorCheckedException intUnaryOperatorCheckedException
  ) {
    return wrapCheckedException(intUnaryOperatorCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link IntUnaryOperator} that wraps the checked exception lambda,
   * {@code intUnaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param intUnaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                         with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper         the supplier of the RuntimeException descendant instance within which to
   *                                         wrap the checked exception, if thrown
   * @param <EX>                             the type of the RuntimeException descendant instance within which to wrap
   *                                         the checked exception, if thrown
   * @return a {@link IntUnaryOperator} that wraps the checked exception lambda,
   *     {@code intUnaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> IntUnaryOperator wrapCheckedException(
      IntUnaryOperatorCheckedException intUnaryOperatorCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                intUnaryOperatorCheckedException.applyAsInt(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link LongBinaryOperator} that wraps the checked exception lambda,
   * {@code longBinaryOperatorCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param longBinaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                           with a {@link RuntimeException}
   * @return a {@link LongBinaryOperator} that wraps the checked exception lambda,
   *     {@code longBinaryOperatorCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static LongBinaryOperator wrapCheckedException(
      LongBinaryOperatorCheckedException longBinaryOperatorCheckedException
  ) {
    return wrapCheckedException(longBinaryOperatorCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link LongBinaryOperator} that wraps the checked exception lambda,
   * {@code longBinaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param longBinaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                           with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper           the supplier of the RuntimeException descendant instance within which to
   *                                           wrap the checked exception, if thrown
   * @param <EX>                               the type of the RuntimeException descendant instance within which to wrap
   *                                           the checked exception, if thrown
   * @return a {@link LongBinaryOperator} that wraps the checked exception lambda,
   *     {@code longBinaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> LongBinaryOperator wrapCheckedException(
      LongBinaryOperatorCheckedException longBinaryOperatorCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (left, right) ->
        TryCatchesOps.wrapCheckedException(() ->
                longBinaryOperatorCheckedException.applyAsLong(left, right))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link LongConsumer} that wraps the checked exception lambda, {@code longConsumerCheckedException}, with
   * a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param longConsumerCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @return a {@link LongConsumer} that wraps the checked exception lambda, {@code longConsumerCheckedException}, with
   *     a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static LongConsumer wrapCheckedException(
      LongConsumerCheckedException longConsumerCheckedException
  ) {
    return wrapCheckedException(longConsumerCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link LongConsumer} that wraps the checked exception lambda, {@code longConsumerCheckedException}, with
   * a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param longConsumerCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @param fRuntimeExceptionWrapper     the supplier of the RuntimeException descendant instance within which to wrap
   *                                     the checked exception, if thrown
   * @param <EX>                         the type of the RuntimeException descendant instance within which to wrap the
   *                                     checked exception, if thrown
   * @return a {@link LongConsumer} that wraps the checked exception lambda, {@code longConsumerCheckedException}, with
   *     a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> LongConsumer wrapCheckedException(
      LongConsumerCheckedException longConsumerCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) -> {
      try {
        longConsumerCheckedException.accept(t);
      } catch (Exception exception) {
        throw fRuntimeExceptionWrapper.apply(exception);
      }
    };
  }

  /**
   * Returns a {@link LongFunction} that wraps the checked exception lambda, {@code longFunctionCheckedException}, with
   * a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param longFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @param <R>                          the type of the result returned by the function
   * @return a {@link LongFunction} that wraps the checked exception lambda, {@code longFunctionCheckedException}, with
   *     a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <R> LongFunction<R> wrapCheckedException(
      LongFunctionCheckedException<R> longFunctionCheckedException
  ) {
    return wrapCheckedException(longFunctionCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link LongFunction} that wraps the checked exception lambda, {@code longFunctionCheckedException}, with
   * a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param longFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @param fRuntimeExceptionWrapper     the supplier of the RuntimeException descendant instance within which to wrap
   *                                     the checked exception, if thrown
   * @param <EX>                         the type of the RuntimeException descendant instance within which to wrap the
   *                                     checked exception, if thrown
   * @param <R>                          the type of the result returned by the function
   * @return a {@link LongFunction} that wraps the checked exception lambda, {@code longFunctionCheckedException}, with
   *     a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, R> LongFunction<R> wrapCheckedException(
      LongFunctionCheckedException<R> longFunctionCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                longFunctionCheckedException.apply(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link LongPredicate} that wraps the checked exception lambda, {@code longPredicateCheckedException},
   * with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   * {@link Stream} operations.
   *
   * @param longPredicateCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                      {@link RuntimeException}
   * @return a {@link LongPredicate} that wraps the checked exception lambda, {@code longPredicateCheckedException},
   *     with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   *     {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static LongPredicate wrapCheckedException(
      LongPredicateCheckedException longPredicateCheckedException
  ) {
    return wrapCheckedException(longPredicateCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link LongPredicate} that wraps the checked exception lambda, {@code longPredicateCheckedException},
   * with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   * lambda within {@link Stream} operations.
   *
   * @param longPredicateCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                      {@link RuntimeException}
   * @param fRuntimeExceptionWrapper      the supplier of the RuntimeException descendant instance within which to wrap
   *                                      the checked exception, if thrown
   * @param <EX>                          the type of the RuntimeException descendant instance within which to wrap the
   *                                      checked exception, if thrown
   * @return a {@link LongPredicate} that wraps the checked exception lambda, {@code longPredicateCheckedException},
   *     with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of
   *     the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> LongPredicate wrapCheckedException(
      LongPredicateCheckedException longPredicateCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                longPredicateCheckedException.test(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link LongSupplier} that wraps the checked exception lambda, {@code longSupplierCheckedException}, with
   * a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param longSupplierCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @return a {@link LongSupplier} that wraps the checked exception lambda, {@code longSupplierCheckedException}, with
   *     a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static LongSupplier wrapCheckedException(
      LongSupplierCheckedException longSupplierCheckedException
  ) {
    return wrapCheckedException(longSupplierCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link LongSupplier} that wraps the checked exception lambda, {@code longSupplierCheckedException}, with
   * a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param longSupplierCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @param fRuntimeExceptionWrapper     the supplier of the RuntimeException descendant instance within which to wrap
   *                                     the checked exception, if thrown
   * @param <EX>                         the type of the RuntimeException descendant instance within which to wrap the
   *                                     checked exception, if thrown
   * @return a {@link LongSupplier} that wraps the checked exception lambda, {@code longSupplierCheckedException}, with
   *     a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> LongSupplier wrapCheckedException(
      LongSupplierCheckedException longSupplierCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return () ->
        TryCatchesOps.wrapCheckedException(longSupplierCheckedException::getAsLong)
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link LongToDoubleFunction} that wraps the checked exception lambda,
   * {@code longToDoubleFunctionCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param longToDoubleFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                             with a {@link RuntimeException}
   * @return a {@link LongToDoubleFunction} that wraps the checked exception lambda,
   *     {@code longToDoubleFunctionCheckedException}, with a {@link RuntimeException} of
   *     {@link WrappedCheckedException} to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static LongToDoubleFunction wrapCheckedException(
      LongToDoubleFunctionCheckedException longToDoubleFunctionCheckedException
  ) {
    return wrapCheckedException(longToDoubleFunctionCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link LongToDoubleFunction} that wraps the checked exception lambda,
   * {@code longToDoubleFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param longToDoubleFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                             with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper             the supplier of the RuntimeException descendant instance within which
   *                                             to wrap the checked exception, if thrown
   * @param <EX>                                 the type of the RuntimeException descendant instance within which to
   *                                             wrap the checked exception, if thrown
   * @return a {@link LongToDoubleFunction} that wraps the checked exception lambda,
   *     {@code longToDoubleFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> LongToDoubleFunction wrapCheckedException(
      LongToDoubleFunctionCheckedException longToDoubleFunctionCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                longToDoubleFunctionCheckedException.applyAsDouble(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link LongToIntFunction} that wraps the checked exception lambda,
   * {@code longToIntFunctionCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param longToIntFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                          with a {@link RuntimeException}
   * @return a {@link LongToIntFunction} that wraps the checked exception lambda,
   *     {@code longToIntFunctionCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static LongToIntFunction wrapCheckedException(
      LongToIntFunctionCheckedException longToIntFunctionCheckedException
  ) {
    return wrapCheckedException(longToIntFunctionCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link LongToIntFunction} that wraps the checked exception lambda,
   * {@code longToIntFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param longToIntFunctionCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                          with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper          the supplier of the RuntimeException descendant instance within which to
   *                                          wrap the checked exception, if thrown
   * @param <EX>                              the type of the RuntimeException descendant instance within which to wrap
   *                                          the checked exception, if thrown
   * @return a {@link LongToIntFunction} that wraps the checked exception lambda,
   *     {@code longToIntFunctionCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> LongToIntFunction wrapCheckedException(
      LongToIntFunctionCheckedException longToIntFunctionCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                longToIntFunctionCheckedException.applyAsInt(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link LongUnaryOperator} that wraps the checked exception lambda,
   * {@code longUnaryOperatorCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param longUnaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                          with a {@link RuntimeException}
   * @return a {@link LongUnaryOperator} that wraps the checked exception lambda,
   *     {@code longUnaryOperatorCheckedException}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static LongUnaryOperator wrapCheckedException(
      LongUnaryOperatorCheckedException longUnaryOperatorCheckedException
  ) {
    return wrapCheckedException(longUnaryOperatorCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link LongUnaryOperator} that wraps the checked exception lambda,
   * {@code longUnaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param longUnaryOperatorCheckedException the lambda which may throw a checked exception that needs to be wrapped
   *                                          with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper          the supplier of the RuntimeException descendant instance within which to
   *                                          wrap the checked exception, if thrown
   * @param <EX>                              the type of the RuntimeException descendant instance within which to wrap
   *                                          the checked exception, if thrown
   * @return a {@link LongUnaryOperator} that wraps the checked exception lambda,
   *     {@code longUnaryOperatorCheckedException}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException> LongUnaryOperator wrapCheckedException(
      LongUnaryOperatorCheckedException longUnaryOperatorCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                longUnaryOperatorCheckedException.applyAsLong(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link ObjDoubleConsumer} that wraps the checked exception lambda,
   * {@code objDoubleConsumerCheckedExceptionT}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param objDoubleConsumerCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped
   *                                           with a {@link RuntimeException}
   * @param <T>                                the type of the parameter passed by the objDoubleConsumer
   * @return a {@link ObjDoubleConsumer} that wraps the checked exception lambda,
   *     {@code objDoubleConsumerCheckedExceptionT}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <T> ObjDoubleConsumer<T> wrapCheckedException(
      ObjDoubleConsumerCheckedException<T> objDoubleConsumerCheckedExceptionT
  ) {
    return wrapCheckedException(objDoubleConsumerCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link ObjDoubleConsumer} that wraps the checked exception lambda,
   * {@code objDoubleConsumerCheckedExceptionT}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param objDoubleConsumerCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped
   *                                           with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper           the supplier of the RuntimeException descendant instance within which to
   *                                           wrap the checked exception, if thrown
   * @param <EX>                               the type of the RuntimeException descendant instance within which to wrap
   *                                           the checked exception, if thrown
   * @param <T>                                the type of the parameter passed by the objDoubleConsumer
   * @return a {@link ObjDoubleConsumer} that wraps the checked exception lambda,
   *     {@code objDoubleConsumerCheckedExceptionT}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, T> ObjDoubleConsumer<T> wrapCheckedException(
      ObjDoubleConsumerCheckedException<T> objDoubleConsumerCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, value) -> {
      try {
        objDoubleConsumerCheckedExceptionT.accept(t, value);
      } catch (Exception exception) {
        throw fRuntimeExceptionWrapper.apply(exception);
      }
    };
  }

  /**
   * Returns a {@link ObjIntConsumer} that wraps the checked exception lambda, {@code objIntConsumerCheckedExceptionT},
   * with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   * {@link Stream} operations.
   *
   * @param objIntConsumerCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @param <T>                             the type of the parameter passed by the objIntConsumer
   * @return a {@link ObjIntConsumer} that wraps the checked exception lambda, {@code objIntConsumerCheckedExceptionT},
   *     with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   *     {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <T> ObjIntConsumer<T> wrapCheckedException(
      ObjIntConsumerCheckedException<T> objIntConsumerCheckedExceptionT
  ) {
    return wrapCheckedException(objIntConsumerCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link ObjIntConsumer} that wraps the checked exception lambda, {@code objIntConsumerCheckedExceptionT},
   * with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   * lambda within {@link Stream} operations.
   *
   * @param objIntConsumerCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper        the supplier of the RuntimeException descendant instance within which to
   *                                        wrap the checked exception, if thrown
   * @param <EX>                            the type of the RuntimeException descendant instance within which to wrap
   *                                        the checked exception, if thrown
   * @param <T>                             the type of the parameter passed by the objIntConsumer
   * @return a {@link ObjIntConsumer} that wraps the checked exception lambda, {@code objIntConsumerCheckedExceptionT},
   *     with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of
   *     the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, T> ObjIntConsumer<T> wrapCheckedException(
      ObjIntConsumerCheckedException<T> objIntConsumerCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, value) -> {
      try {
        objIntConsumerCheckedExceptionT.accept(t, value);
      } catch (Exception exception) {
        throw fRuntimeExceptionWrapper.apply(exception);
      }
    };
  }

  /**
   * Returns a {@link ObjLongConsumer} that wraps the checked exception lambda,
   * {@code objLongConsumerCheckedExceptionT}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param objLongConsumerCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped
   *                                         with a {@link RuntimeException}
   * @param <T>                              the type of the parameter passed by the objLongConsumer
   * @return a {@link ObjLongConsumer} that wraps the checked exception lambda,
   *     {@code objLongConsumerCheckedExceptionT}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   *     enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <T> ObjLongConsumer<T> wrapCheckedException(
      ObjLongConsumerCheckedException<T> objLongConsumerCheckedExceptionT
  ) {
    return wrapCheckedException(objLongConsumerCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link ObjLongConsumer} that wraps the checked exception lambda,
   * {@code objLongConsumerCheckedExceptionT}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param objLongConsumerCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped
   *                                         with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper         the supplier of the RuntimeException descendant instance within which to
   *                                         wrap the checked exception, if thrown
   * @param <EX>                             the type of the RuntimeException descendant instance within which to wrap
   *                                         the checked exception, if thrown
   * @param <T>                              the type of the parameter passed by the objLongConsumer
   * @return a {@link ObjLongConsumer} that wraps the checked exception lambda,
   *     {@code objLongConsumerCheckedExceptionT}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, T> ObjLongConsumer<T> wrapCheckedException(
      ObjLongConsumerCheckedException<T> objLongConsumerCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, value) -> {
      try {
        objLongConsumerCheckedExceptionT.accept(t, value);
      } catch (Exception exception) {
        throw fRuntimeExceptionWrapper.apply(exception);
      }
    };
  }

  /**
   * Returns a {@link ToDoubleBiFunction} that wraps the checked exception lambda,
   * {@code toDoubleBiFunctionCheckedExceptionTAndU}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   * to enable use of the lambda within {@link Stream} operations.
   *
   * @param toDoubleBiFunctionCheckedExceptionTAndU the lambda which may throw a checked exception that needs to be
   *                                                wrapped with a {@link RuntimeException}
   * @param <T>                                     the type of the first parameter passed into the function
   * @param <U>                                     the type of the second parameter passed into the function
   * @return a {@link ToDoubleBiFunction} that wraps the checked exception lambda,
   *     {@code toDoubleBiFunctionCheckedExceptionT}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <T, U> ToDoubleBiFunction<T, U> wrapCheckedException(
      ToDoubleBiFunctionCheckedException<T, U> toDoubleBiFunctionCheckedExceptionTAndU
  ) {
    return wrapCheckedException(toDoubleBiFunctionCheckedExceptionTAndU, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link ToDoubleBiFunction} that wraps the checked exception lambda,
   * {@code toDoubleBiFunctionCheckedExceptionTAndU}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param toDoubleBiFunctionCheckedExceptionTAndU the lambda which may throw a checked exception that needs to be
   *                                                wrapped with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper                the supplier of the RuntimeException descendant instance within
   *                                                which to wrap the checked exception, if thrown
   * @param <EX>                                    the type of the RuntimeException descendant instance within which to
   *                                                wrap the checked exception, if thrown
   * @param <T>                                     the type of the first parameter passed into the function
   * @param <U>                                     the type of the second parameter passed into the function
   * @return a {@link ToDoubleBiFunction} that wraps the checked exception lambda,
   *     {@code toDoubleBiFunctionCheckedExceptionT}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, T, U> ToDoubleBiFunction<T, U> wrapCheckedException(
      ToDoubleBiFunctionCheckedException<T, U> toDoubleBiFunctionCheckedExceptionTAndU,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, u) ->
        TryCatchesOps.wrapCheckedException(() ->
                toDoubleBiFunctionCheckedExceptionTAndU.applyAsDouble(t, u))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link ToIntBiFunction} that wraps the checked exception lambda,
   * {@code toIntBiFunctionCheckedExceptionTAndU}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param toIntBiFunctionCheckedExceptionTAndU the lambda which may throw a checked exception that needs to be wrapped
   *                                             with a {@link RuntimeException}
   * @param <T>                                  the type of the first parameter passed into the function
   * @param <U>                                  the type of the second parameter passed into the function
   * @return a {@link ToIntBiFunction} that wraps the checked exception lambda,
   *     {@code toIntBiFunctionCheckedExceptionT}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   *     enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <T, U> ToIntBiFunction<T, U> wrapCheckedException(
      ToIntBiFunctionCheckedException<T, U> toIntBiFunctionCheckedExceptionTAndU
  ) {
    return wrapCheckedException(toIntBiFunctionCheckedExceptionTAndU, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link ToIntBiFunction} that wraps the checked exception lambda,
   * {@code toIntBiFunctionCheckedExceptionTAndU}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param toIntBiFunctionCheckedExceptionTAndU the lambda which may throw a checked exception that needs to be wrapped
   *                                             with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper             the supplier of the RuntimeException descendant instance within which
   *                                             to wrap the checked exception, if thrown
   * @param <EX>                                 the type of the RuntimeException descendant instance within which to
   *                                             wrap the checked exception, if thrown
   * @param <T>                                  the type of the first parameter passed into the function
   * @param <U>                                  the type of the second parameter passed into the function
   * @return a {@link ToIntBiFunction} that wraps the checked exception lambda,
   *     {@code toIntBiFunctionCheckedExceptionT}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, T, U> ToIntBiFunction<T, U> wrapCheckedException(
      ToIntBiFunctionCheckedException<T, U> toIntBiFunctionCheckedExceptionTAndU,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, u) ->
        TryCatchesOps.wrapCheckedException(() ->
                toIntBiFunctionCheckedExceptionTAndU.applyAsInt(t, u))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link ToLongBiFunction} that wraps the checked exception lambda,
   * {@code toLongBiFunctionCheckedExceptionTAndU}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   * to enable use of the lambda within {@link Stream} operations.
   *
   * @param toLongBiFunctionCheckedExceptionTAndU the lambda which may throw a checked exception that needs to be
   *                                              wrapped with a {@link RuntimeException}
   * @param <T>                                   the type of the first parameter passed into the function
   * @param <U>                                   the type of the second parameter passed into the function
   * @return a {@link ToLongBiFunction} that wraps the checked exception lambda,
   *     {@code toLongBiFunctionCheckedExceptionT}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <T, U> ToLongBiFunction<T, U> wrapCheckedException(
      ToLongBiFunctionCheckedException<T, U> toLongBiFunctionCheckedExceptionTAndU
  ) {
    return wrapCheckedException(toLongBiFunctionCheckedExceptionTAndU, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link ToLongBiFunction} that wraps the checked exception lambda,
   * {@code toLongBiFunctionCheckedExceptionTAndU}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param toLongBiFunctionCheckedExceptionTAndU the lambda which may throw a checked exception that needs to be
   *                                              wrapped with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper              the supplier of the RuntimeException descendant instance within which
   *                                              to wrap the checked exception, if thrown
   * @param <EX>                                  the type of the RuntimeException descendant instance within which to
   *                                              wrap the checked exception, if thrown
   * @param <T>                                   the type of the first parameter passed into the function
   * @param <U>                                   the type of the second parameter passed into the function
   * @return a {@link ToLongBiFunction} that wraps the checked exception lambda,
   *     {@code toLongBiFunctionCheckedExceptionT}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, T, U> ToLongBiFunction<T, U> wrapCheckedException(
      ToLongBiFunctionCheckedException<T, U> toLongBiFunctionCheckedExceptionTAndU,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, u) ->
        TryCatchesOps.wrapCheckedException(() ->
                toLongBiFunctionCheckedExceptionTAndU.applyAsLong(t, u))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link ToDoubleFunction} that wraps the checked exception lambda,
   * {@code toDoubleFunctionCheckedExceptionT}, with a {@link RuntimeException} of {@link WrappedCheckedException} to
   * enable use of the lambda within {@link Stream} operations.
   *
   * @param toDoubleFunctionCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped
   *                                          with a {@link RuntimeException}
   * @param <T>                               the type of the parameter passed into the toDoubleFunction
   * @return a {@link ToDoubleFunction} that wraps the checked exception lambda,
   *     {@code toDoubleFunctionCheckedExceptionT}, with a {@link RuntimeException} of {@link WrappedCheckedException}
   *     to enable use of the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <T> ToDoubleFunction<T> wrapCheckedException(
      ToDoubleFunctionCheckedException<T> toDoubleFunctionCheckedExceptionT
  ) {
    return wrapCheckedException(toDoubleFunctionCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link ToDoubleFunction} that wraps the checked exception lambda,
   * {@code toDoubleFunctionCheckedExceptionT}, with a {@link RuntimeException} returned by the supplier,
   * {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations.
   *
   * @param toDoubleFunctionCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped
   *                                          with a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper          the supplier of the RuntimeException descendant instance within which to
   *                                          wrap the checked exception, if thrown
   * @param <EX>                              the type of the RuntimeException descendant instance within which to wrap
   *                                          the checked exception, if thrown
   * @param <T>                               the type of the parameter passed into the toDoubleFunction
   * @return a {@link ToDoubleFunction} that wraps the checked exception lambda,
   *     {@code toDoubleFunctionCheckedExceptionT}, with a {@link RuntimeException} returned by the supplier,
   *     {@code fRuntimeExceptionWrapper}, to enable use of the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, T> ToDoubleFunction<T> wrapCheckedException(
      ToDoubleFunctionCheckedException<T> toDoubleFunctionCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                toDoubleFunctionCheckedExceptionT.applyAsDouble(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link ToIntFunction} that wraps the checked exception lambda, {@code toIntFunctionCheckedExceptionT},
   * with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   * {@link Stream} operations.
   *
   * @param toIntFunctionCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with
   *                                       a {@link RuntimeException}
   * @param <T>                            the type of the parameter passed into the toIntFunction
   * @return a {@link ToIntFunction} that wraps the checked exception lambda, {@code toIntFunctionCheckedExceptionT},
   *     with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   *     {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <T> ToIntFunction<T> wrapCheckedException(
      ToIntFunctionCheckedException<T> toIntFunctionCheckedExceptionT
  ) {
    return wrapCheckedException(toIntFunctionCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link ToIntFunction} that wraps the checked exception lambda, {@code toIntFunctionCheckedExceptionT},
   * with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   * lambda within {@link Stream} operations.
   *
   * @param toIntFunctionCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with
   *                                       a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper       the supplier of the RuntimeException descendant instance within which to wrap
   *                                       the checked exception, if thrown
   * @param <EX>                           the type of the RuntimeException descendant instance within which to wrap the
   *                                       checked exception, if thrown
   * @param <T>                            the type of the parameter passed into the toIntFunction
   * @return a {@link ToIntFunction} that wraps the checked exception lambda, {@code toIntFunctionCheckedExceptionT},
   *     with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of
   *     the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, T> ToIntFunction<T> wrapCheckedException(
      ToIntFunctionCheckedException<T> toIntFunctionCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                toIntFunctionCheckedExceptionT.applyAsInt(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link ToLongFunction} that wraps the checked exception lambda, {@code toLongFunctionCheckedExceptionT},
   * with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   * {@link Stream} operations.
   *
   * @param toLongFunctionCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @param <T>                             the type of the parameter passed into the toLongFunction
   * @return a {@link ToLongFunction} that wraps the checked exception lambda, {@code toLongFunctionCheckedExceptionT},
   *     with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   *     {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   */
  public static <T> ToLongFunction<T> wrapCheckedException(
      ToLongFunctionCheckedException<T> toLongFunctionCheckedExceptionT
  ) {
    return wrapCheckedException(toLongFunctionCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link ToLongFunction} that wraps the checked exception lambda, {@code toLongFunctionCheckedExceptionT},
   * with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   * lambda within {@link Stream} operations.
   *
   * @param toLongFunctionCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper        the supplier of the RuntimeException descendant instance within which to
   *                                        wrap the checked exception, if thrown
   * @param <EX>                            the type of the RuntimeException descendant instance within which to wrap
   *                                        the checked exception, if thrown
   * @param <T>                             the type of the parameter passed into the toLongFunction
   * @return a {@link ToLongFunction} that wraps the checked exception lambda, {@code toLongFunctionCheckedExceptionT},
   *     with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of
   *     the lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, T> ToLongFunction<T> wrapCheckedException(
      ToLongFunctionCheckedException<T> toLongFunctionCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                toLongFunctionCheckedExceptionT.applyAsLong(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }
}
