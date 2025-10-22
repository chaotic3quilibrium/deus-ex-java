package org.deus_ex_java.util.function;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.deus_ex_java.util.Either;
import org.deus_ex_java.util.TryCatchesOps;
import org.jetbrains.annotations.NotNull;

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
public class FunctionsPrimitivesOps {

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
   */
  @NotNull
  public static BooleanSupplier wrapCheckedException(
      @NotNull BooleanSupplierCheckedException booleanSupplierCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> BooleanSupplier wrapCheckedException(
      @NotNull BooleanSupplierCheckedException booleanSupplierCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return () ->
        TryCatchesOps.wrapChecked(booleanSupplierCheckedException::getAsBoolean)
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
   */
  @NotNull
  public static DoubleBinaryOperator wrapCheckedException(
      @NotNull DoubleBinaryOperatorCheckedException doubleBinaryOperatorCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> DoubleBinaryOperator wrapCheckedException(
      @NotNull DoubleBinaryOperatorCheckedException doubleBinaryOperatorCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (left, right) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static DoubleConsumer wrapCheckedException(
      @NotNull DoubleConsumerCheckedException doubleConsumerCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> DoubleConsumer wrapCheckedException(
      @NotNull DoubleConsumerCheckedException doubleConsumerCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
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
   */
  @NotNull
  public static <R> DoubleFunction<R> wrapCheckedException(
      @NotNull DoubleFunctionCheckedException<R> doubleFunctionCheckedException
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
  @NotNull
  public static <EX extends RuntimeException, R> DoubleFunction<R> wrapCheckedException(
      @NotNull DoubleFunctionCheckedException<R> doubleFunctionCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static DoublePredicate wrapCheckedException(
      @NotNull DoublePredicateCheckedException doublePredicateCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> DoublePredicate wrapCheckedException(
      @NotNull DoublePredicateCheckedException doublePredicateCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static DoubleSupplier wrapCheckedException(
      @NotNull DoubleSupplierCheckedException doubleSupplierCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> DoubleSupplier wrapCheckedException(
      @NotNull DoubleSupplierCheckedException doubleSupplierCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return () ->
        TryCatchesOps.wrapChecked(doubleSupplierCheckedException::getAsDouble)
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
   */
  @NotNull
  public static DoubleToIntFunction wrapCheckedException(
      @NotNull DoubleToIntFunctionCheckedException doubleToIntFunctionCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> DoubleToIntFunction wrapCheckedException(
      @NotNull DoubleToIntFunctionCheckedException doubleToIntFunctionCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static DoubleToLongFunction wrapCheckedException(
      @NotNull DoubleToLongFunctionCheckedException doubleToLongFunctionCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> DoubleToLongFunction wrapCheckedException(
      @NotNull DoubleToLongFunctionCheckedException doubleToLongFunctionCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static DoubleUnaryOperator wrapCheckedException(
      @NotNull DoubleUnaryOperatorCheckedException doubleUnaryOperatorCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> DoubleUnaryOperator wrapCheckedException(
      @NotNull DoubleUnaryOperatorCheckedException doubleUnaryOperatorCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static IntBinaryOperator wrapCheckedException(
      @NotNull IntBinaryOperatorCheckedException intBinaryOperatorCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> IntBinaryOperator wrapCheckedException(
      @NotNull IntBinaryOperatorCheckedException intBinaryOperatorCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (left, right) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static IntConsumer wrapCheckedException(
      @NotNull IntConsumerCheckedException intConsumerCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> IntConsumer wrapCheckedException(
      @NotNull IntConsumerCheckedException intConsumerCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
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
   */
  @NotNull
  public static <R> IntFunction<R> wrapCheckedException(
      @NotNull IntFunctionCheckedException<R> intFunctionCheckedException
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
  @NotNull
  public static <EX extends RuntimeException, R> IntFunction<R> wrapCheckedException(
      @NotNull IntFunctionCheckedException<R> intFunctionCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static IntPredicate wrapCheckedException(
      @NotNull IntPredicateCheckedException intPredicateCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> IntPredicate wrapCheckedException(
      @NotNull IntPredicateCheckedException intPredicateCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static IntSupplier wrapCheckedException(
      @NotNull IntSupplierCheckedException intSupplierCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> IntSupplier wrapCheckedException(
      @NotNull IntSupplierCheckedException intSupplierCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return () ->
        TryCatchesOps.wrapChecked(intSupplierCheckedException::getAsInt)
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
   */
  @NotNull
  public static IntToDoubleFunction wrapCheckedException(
      @NotNull IntToDoubleFunctionCheckedException intToDoubleFunctionCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> IntToDoubleFunction wrapCheckedException(
      @NotNull IntToDoubleFunctionCheckedException intToDoubleFunctionCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static IntToLongFunction wrapCheckedException(
      @NotNull IntToLongFunctionCheckedException intToLongFunctionCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> IntToLongFunction wrapCheckedException(
      @NotNull IntToLongFunctionCheckedException intToLongFunctionCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static IntUnaryOperator wrapCheckedException(
      @NotNull IntUnaryOperatorCheckedException intUnaryOperatorCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> IntUnaryOperator wrapCheckedException(
      @NotNull IntUnaryOperatorCheckedException intUnaryOperatorCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static LongBinaryOperator wrapCheckedException(
      @NotNull LongBinaryOperatorCheckedException longBinaryOperatorCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> LongBinaryOperator wrapCheckedException(
      @NotNull LongBinaryOperatorCheckedException longBinaryOperatorCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (left, right) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static LongConsumer wrapCheckedException(
      @NotNull LongConsumerCheckedException longConsumerCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> LongConsumer wrapCheckedException(
      @NotNull LongConsumerCheckedException longConsumerCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
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
   */
  @NotNull
  public static <R> LongFunction<R> wrapCheckedException(
      @NotNull LongFunctionCheckedException<R> longFunctionCheckedException
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
  @NotNull
  public static <EX extends RuntimeException, R> LongFunction<R> wrapCheckedException(
      @NotNull LongFunctionCheckedException<R> longFunctionCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static LongPredicate wrapCheckedException(
      @NotNull LongPredicateCheckedException longPredicateCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> LongPredicate wrapCheckedException(
      @NotNull LongPredicateCheckedException longPredicateCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static LongSupplier wrapCheckedException(
      @NotNull LongSupplierCheckedException longSupplierCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> LongSupplier wrapCheckedException(
      @NotNull LongSupplierCheckedException longSupplierCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return () ->
        TryCatchesOps.wrapChecked(longSupplierCheckedException::getAsLong)
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
   */
  @NotNull
  public static LongToDoubleFunction wrapCheckedException(
      @NotNull LongToDoubleFunctionCheckedException longToDoubleFunctionCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> LongToDoubleFunction wrapCheckedException(
      @NotNull LongToDoubleFunctionCheckedException longToDoubleFunctionCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static LongToIntFunction wrapCheckedException(
      @NotNull LongToIntFunctionCheckedException longToIntFunctionCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> LongToIntFunction wrapCheckedException(
      @NotNull LongToIntFunctionCheckedException longToIntFunctionCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static LongUnaryOperator wrapCheckedException(
      @NotNull LongUnaryOperatorCheckedException longUnaryOperatorCheckedException
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
  @NotNull
  public static <EX extends RuntimeException> LongUnaryOperator wrapCheckedException(
      @NotNull LongUnaryOperatorCheckedException longUnaryOperatorCheckedException,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static <T> ObjDoubleConsumer<T> wrapCheckedException(
      @NotNull ObjDoubleConsumerCheckedException<T> objDoubleConsumerCheckedExceptionT
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
  @NotNull
  public static <EX extends RuntimeException, T> ObjDoubleConsumer<T> wrapCheckedException(
      @NotNull ObjDoubleConsumerCheckedException<T> objDoubleConsumerCheckedExceptionT,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
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
   */
  @NotNull
  public static <T> ObjIntConsumer<T> wrapCheckedException(
      @NotNull ObjIntConsumerCheckedException<T> objIntConsumerCheckedExceptionT
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
  @NotNull
  public static <EX extends RuntimeException, T> ObjIntConsumer<T> wrapCheckedException(
      @NotNull ObjIntConsumerCheckedException<T> objIntConsumerCheckedExceptionT,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
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
   */
  @NotNull
  public static <T> ObjLongConsumer<T> wrapCheckedException(
      @NotNull ObjLongConsumerCheckedException<T> objLongConsumerCheckedExceptionT
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
  @NotNull
  public static <EX extends RuntimeException, T> ObjLongConsumer<T> wrapCheckedException(
      @NotNull ObjLongConsumerCheckedException<T> objLongConsumerCheckedExceptionT,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
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
   */
  @NotNull
  public static <T, U> ToDoubleBiFunction<T, U> wrapCheckedException(
      @NotNull ToDoubleBiFunctionCheckedException<T, U> toDoubleBiFunctionCheckedExceptionTAndU
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
  @NotNull
  public static <EX extends RuntimeException, T, U> ToDoubleBiFunction<T, U> wrapCheckedException(
      @NotNull ToDoubleBiFunctionCheckedException<T, U> toDoubleBiFunctionCheckedExceptionTAndU,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, u) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static <T, U> ToIntBiFunction<T, U> wrapCheckedException(
      @NotNull ToIntBiFunctionCheckedException<T, U> toIntBiFunctionCheckedExceptionTAndU
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
  @NotNull
  public static <EX extends RuntimeException, T, U> ToIntBiFunction<T, U> wrapCheckedException(
      @NotNull ToIntBiFunctionCheckedException<T, U> toIntBiFunctionCheckedExceptionTAndU,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, u) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static <T, U> ToLongBiFunction<T, U> wrapCheckedException(
      @NotNull ToLongBiFunctionCheckedException<T, U> toLongBiFunctionCheckedExceptionTAndU
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
  @NotNull
  public static <EX extends RuntimeException, T, U> ToLongBiFunction<T, U> wrapCheckedException(
      @NotNull ToLongBiFunctionCheckedException<T, U> toLongBiFunctionCheckedExceptionTAndU,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, u) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static <T> ToDoubleFunction<T> wrapCheckedException(
      @NotNull ToDoubleFunctionCheckedException<T> toDoubleFunctionCheckedExceptionT
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
  @NotNull
  public static <EX extends RuntimeException, T> ToDoubleFunction<T> wrapCheckedException(
      @NotNull ToDoubleFunctionCheckedException<T> toDoubleFunctionCheckedExceptionT,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static <T> ToIntFunction<T> wrapCheckedException(
      @NotNull ToIntFunctionCheckedException<T> toIntFunctionCheckedExceptionT
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
  @NotNull
  public static <EX extends RuntimeException, T> ToIntFunction<T> wrapCheckedException(
      @NotNull ToIntFunctionCheckedException<T> toIntFunctionCheckedExceptionT,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
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
   */
  @NotNull
  public static <T> ToLongFunction<T> wrapCheckedException(
      @NotNull ToLongFunctionCheckedException<T> toLongFunctionCheckedExceptionT
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
  @NotNull
  public static <EX extends RuntimeException, T> ToLongFunction<T> wrapCheckedException(
      @NotNull ToLongFunctionCheckedException<T> toLongFunctionCheckedExceptionT,
      @NotNull Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapChecked(() ->
                toLongFunctionCheckedExceptionT.applyAsLong(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }
}
