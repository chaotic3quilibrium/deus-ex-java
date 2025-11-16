package org.deus_ex_java.util.function;

import org.deus_ex_java.lang.FatalThrowable;
import org.deus_ex_java.lang.WrappedCheckedException;
import org.deus_ex_java.util.TryCatchesOps;
import org.deus_ex_java.util.tuple.Tuple2;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.Callable;
import java.util.function.*;
import java.util.stream.Stream;

/**
 * Extends and enhances Java's Lambda Library, specifically for the method type signatures independent of primitives.
 * <p>
 * ---
 * <p>
 * Useful Reference: <a
 * href="https://docs.google.com/spreadsheets/d/1Xljq5x9alDwSHZTY1nkBxDAwF4MKX5x2zy6XD-x2zVk/edit?usp=sharing">Java
 * Lambda Reference Table V2</a>
 */
@NullMarked
public final class FunctionsOps {

  private FunctionsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * A universal and immutable FunctionalInterface instance of VoidSupplier.
   */
  public static final VoidSupplier NO_OP = () -> {
  };

  /**
   * A universal and immutable FunctionalInterface instance of VoidSupplierCheckedException.
   */
  public static final VoidSupplierCheckedException NO_OP_CHECKED_EXCEPTION = () -> {
  };

  /**
   * A simple way to apply a side-effecting (optionally checked-exception) function n number of times. Uses
   * {@code wrapCheckedException} to convert all non-{@link RuntimeException}s into a {@link WrappedCheckedException}.
   *
   * @param nTimes                                              number of times to apply the side-effecting function
   * @param justDoItWithNoInputParametersAndThenIgnoreTheResult side-effecting(optionally checked-exception) function to
   *                                                            apply
   */
  public static void executeSideEffectNTimes(
      int nTimes,
      VoidSupplierCheckedException justDoItWithNoInputParametersAndThenIgnoreTheResult
  ) {
    Stream
        .generate(() -> true)
        .limit(nTimes)
        .forEach(ignored ->
            wrapCheckedException(justDoItWithNoInputParametersAndThenIgnoreTheResult).execute());
  }

  /**
   * Return a {@link Runnable} wrapper around a {@link VoidSupplier} instance.
   *
   * @param voidSupplier the target function instance to wrap
   * @return a {@link Runnable} wrapper around a {@link VoidSupplier} instance
   * @see VoidSupplier#of(Runnable) VoidSupplier.of(Runnable) for the inverted version of this method
   */
  public static Runnable to(
      VoidSupplier voidSupplier
  ) {
    return voidSupplier::execute;
  }

  /**
   * Return a {@link Callable} wrapper around a {@link SupplierCheckedException} instance.
   *
   * @param supplierCheckedException the target function instance to wrap
   * @param <R>                      the type of the result of the function
   * @return a {@link Callable} wrapper around a {@link SupplierCheckedException} instance
   * @see SupplierCheckedException#of(Callable) SupplierCheckedException.of(Callable) for the inverted version of this
   *     method
   */
  public static <R> Callable<R> to(
      SupplierCheckedException<? extends R> supplierCheckedException
  ) {
    return supplierCheckedException::get;
  }

  /**
   * Return a {@link BiFunction} wrapper around a {@link Function2} instance.
   *
   * @param function2 the target function instance to wrap
   * @param <A>       the type of the first argument to the function
   * @param <B>       the type of the second argument to the function
   * @param <R>       the type of the result of the function
   * @return a {@link BiFunction} wrapper around a {@link Function2} instance
   * @see Function2#of(BiFunction) Function2.of(BiFunction) for the inverted version of this method
   */
  public static <A, B, R> BiFunction<A, B, R> to(
      Function2<? super A, ? super B, ? extends R> function2
  ) {
    return function2::apply;
  }

  /**
   * Return a {@link BiFunctionChecked} wrapper around a {@link Function2Checked} instance.
   *
   * @param function2Checked the target function instance to wrap
   * @param <A>              the type of the first argument to the function
   * @param <B>              the type of the second argument to the function
   * @param <R>              the type of the result of the function
   * @param <EX>             the type of the checked {@link Exception} of the function
   * @return a {@link BiFunctionChecked} wrapper around a {@link Function2Checked} instance
   * @see Function2Checked#of(BiFunctionChecked) Function2Checked.of(BiFunctionChecked) for the inverted version of this
   *     method
   */
  public static <A, B, R, EX extends Exception> BiFunctionChecked<A, B, R, EX> to(
      Function2Checked<? super A, ? super B, ? extends R, ? extends EX> function2Checked
  ) {
    return function2Checked::apply;
  }

  /**
   * Return a {@link BiFunctionCheckedException} wrapper around a {@link Function2CheckedException} instance.
   *
   * @param function2CheckedException the target function instance to wrap
   * @param <A>                       the type of the first argument to the function
   * @param <B>                       the type of the second argument to the function
   * @param <R>                       the type of the result of the function
   * @return a {@link BiFunctionCheckedException} wrapper around a {@link Function2CheckedException} instance
   * @see Function2CheckedException#of(BiFunctionCheckedException)
   *     Function2CheckedException.of(BiFunctionCheckedException) for the inverted version of this method
   */
  public static <A, B, R> BiFunctionCheckedException<A, B, R> to(
      Function2CheckedException<? super A, ? super B, ? extends R> function2CheckedException
  ) {
    return function2CheckedException::apply;
  }

  /**
   * Returns a {@link Supplier} of a {@link Tuple2} consisting of the result of the {@code fIf} function and the
   * computed value of type {@code R}, which was produced from one of the two supplied functions, {@code fThen} or
   * {@code fElse}.
   *
   * @param fIf   the function to supply a value which determines which of the two functions to call to supply the
   *              return value
   * @param fThen the function, only called if the {@code fIf} function returns true, supplying the return value
   * @param fElse the function, only called if the {@code fIf} function returns false, supplying the return value
   * @param <R>   the type of the result of the value supplying functions
   * @return a {@link Supplier} of a {@link Tuple2} consisting of the result of the {@code fIf} function and the
   *     computed value of type {@code R}, which was produced from one of the two supplied functions, {@code fThen} or
   *     {@code fElse}
   */
  public static <R> Supplier<Tuple2<Boolean, R>> ifThenElse(
      BooleanSupplier fIf,
      Supplier<R> fThen,
      Supplier<R> fElse
  ) {
    return () -> {
      var isThen = fIf.getAsBoolean();

      return new Tuple2<>(
          isThen,
          isThen
              ? fThen.get()
              : fElse.get());
    };
  }

  /**
   * Returns a {@link SupplierCheckedException} of a {@link Tuple2} consisting of the result of the {@code fceIf}
   * function and the computed value of type {@code R}, which was produced from one of the two supplied functions,
   * {@code fceThen} or {@code fceElse}.
   *
   * @param fceIf   the function to supply a value which determines which of the two functions to call to supply the
   *                return value
   * @param fceThen the function, only called if the {@code fceIf} function returns true, supplying the return value
   * @param fceElse the function, only called if the {@code fceIf} function returns false, supplying the return value
   * @param <R>     the type of the result of the value supplying functions
   * @return a {@link SupplierCheckedException} of a {@link Tuple2} consisting of the result of the {@code fceIf}
   *     function and the computed value of type {@code R}, which was produced from one of the two supplied functions,
   *     {@code fceThen} or {@code fceElse}
   */
  public static <R> SupplierCheckedException<Tuple2<R, Boolean>> ifThenElseCheckedException(
      BooleanSupplierCheckedException fceIf,
      SupplierCheckedException<R> fceThen,
      SupplierCheckedException<R> fceElse
  ) {
    return () -> {
      var isThen = fceIf.getAsBoolean();

      return new Tuple2<>(
          isThen
              ? fceThen.get()
              : fceElse.get(),
          isThen);
    };
  }

  /**
   * Returns a {@link BiConsumer} that wraps the checked exception lambda, {@code biConsumerCheckedExceptionT}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param biConsumerCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                    {@link RuntimeException}
   * @param <T>                         the type of the first parameter passed by the bi-consumer
   * @param <U>                         the type of the second parameter passed by the bi-consumer
   * @return a {@link BiConsumer} that wraps the checked exception lambda, {@code biConsumerCheckedExceptionT}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   */
  public static <T, U> BiConsumer<T, U> wrapCheckedException(
      BiConsumerCheckedException<T, U> biConsumerCheckedExceptionT
  ) {
    return wrapCheckedException(biConsumerCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link BiConsumer} that wraps the checked exception lambda, {@code biConsumerCheckedExceptionT}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param biConsumerCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                    {@link RuntimeException}
   * @param fRuntimeExceptionWrapper    the supplier of the RuntimeException descendant instance within which to wrap
   *                                    the checked exception, if thrown
   * @param <EX>                        the type of the RuntimeException descendant instance within which to wrap the
   *                                    checked exception, if thrown
   * @param <T>                         the type of the first parameter passed by the bi-consumer
   * @param <U>                         the type of the second parameter passed by the bi-consumer
   * @return a {@link Consumer} that wraps the checked exception lambda, {@code consumerCheckedExceptionT}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   */
  public static <EX extends RuntimeException, T, U> BiConsumer<T, U> wrapCheckedException(
      BiConsumerCheckedException<T, U> biConsumerCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, u) -> {
      try {
        biConsumerCheckedExceptionT.accept(t, u);
      } catch (Exception exception) {
        throw fRuntimeExceptionWrapper.apply(exception);
      }
    };
  }

  /**
   * Returns a {@link BiFunction} that wraps the checked exception lambda, {@code biFunctionCheckedExceptionTAndU}, with
   * a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param biFunctionCheckedExceptionTAndU the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @param <T>                             the type of the first parameter passed into the function
   * @param <U>                             the type of the second parameter passed into the function
   * @param <R>                             the type of the result returned by the function
   * @return a {@link BiFunction} that wraps the checked exception lambda, {@code biFunctionCheckedExceptionT}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <T, U, R> BiFunction<T, U, R> wrapCheckedException(
      BiFunctionCheckedException<T, U, R> biFunctionCheckedExceptionTAndU
  ) {
    return wrapCheckedException(biFunctionCheckedExceptionTAndU, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link BiFunction} that wraps the checked exception lambda, {@code biFunctionCheckedExceptionTAndU}, with
   * a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param biFunctionCheckedExceptionTAndU the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper        the supplier of the RuntimeException descendant instance within which to
   *                                        wrap the checked exception, if thrown
   * @param <EX>                            the type of the RuntimeException descendant instance within which to wrap
   *                                        the checked exception, if thrown
   * @param <T>                             the type of the first parameter passed into the function
   * @param <U>                             the type of the second parameter passed into the function
   * @param <R>                             the type of the result returned by the function
   * @return a {@link BiFunction} that wraps the checked exception lambda, {@code biFunctionCheckedExceptionT}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, T, U, R> BiFunction<T, U, R> wrapCheckedException(
      BiFunctionCheckedException<T, U, R> biFunctionCheckedExceptionTAndU,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, u) ->
        TryCatchesOps.wrapCheckedException(() ->
                biFunctionCheckedExceptionTAndU.apply(t, u))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link BinaryOperator} that wraps the checked exception lambda, {@code binaryOperatorCheckedExceptionT},
   * with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   * {@link Stream} operations.
   *
   * @param binaryOperatorCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @param <T>                             the type of the two parameters passed into and the result returned by the
   *                                        binary operator
   * @return a {@link BinaryOperator} that wraps the checked exception lambda, {@code binaryOperatorCheckedExceptionT},
   *     with a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within
   *     {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <T> BinaryOperator<T> wrapCheckedException(
      BinaryOperatorCheckedException<T> binaryOperatorCheckedExceptionT
  ) {
    return wrapCheckedException(binaryOperatorCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link BinaryOperator} that wraps the checked exception lambda, {@code binaryOperatorCheckedExceptionT},
   * with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   * lambda within {@link Stream} operations.
   *
   * @param binaryOperatorCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with
   *                                        a {@link RuntimeException}
   * @param fRuntimeExceptionWrapper        the supplier of the RuntimeException descendant instance within which to
   *                                        wrap the checked exception, if thrown
   * @param <EX>                            the type of the RuntimeException descendant instance within which to wrap
   *                                        the checked exception, if thrown
   * @param <T>                             the type of the two parameters passed into and the result returned by the
   *                                        binary operator
   * @return a {@link BinaryOperator} that wraps the checked exception lambda, {@code binaryOperatorCheckedExceptionT},
   *     with a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of
   *     the lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, T> BinaryOperator<T> wrapCheckedException(
      BinaryOperatorCheckedException<T> binaryOperatorCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t1, t2) ->
        TryCatchesOps.wrapCheckedException(() ->
                binaryOperatorCheckedExceptionT.apply(t1, t2))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link BiPredicate} that wraps the checked exception lambda, {@code biPredicateCheckedExceptionT}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param biPredicateCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @param <T>                          the type of the first parameter passed by the predicate
   * @param <U>                          the type of the second parameter passed by the predicate
   * @return a {@link BiPredicate} that wraps the checked exception lambda, {@code biPredicateCheckedExceptionT}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <T, U> BiPredicate<T, U> wrapCheckedException(
      BiPredicateCheckedException<T, U> biPredicateCheckedExceptionT
  ) {
    return wrapCheckedException(biPredicateCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link BiPredicate} that wraps the checked exception lambda, {@code biPredicateCheckedExceptionT}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param biPredicateCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @param fRuntimeExceptionWrapper     the supplier of the RuntimeException descendant instance within which to wrap
   *                                     the checked exception, if thrown
   * @param <EX>                         the type of the RuntimeException descendant instance within which to wrap the
   *                                     checked exception, if thrown
   * @param <T>                          the type of the first parameter passed by the predicate
   * @param <U>                          the type of the second parameter passed by the predicate
   * @return a {@link BiPredicate} that wraps the checked exception lambda, {@code biPredicateCheckedExceptionT}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, T, U> BiPredicate<T, U> wrapCheckedException(
      BiPredicateCheckedException<T, U> biPredicateCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t, u) ->
        TryCatchesOps.wrapCheckedException(() ->
                biPredicateCheckedExceptionT.test(t, u))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link Consumer} that wraps the checked exception lambda, {@code consumerCheckedExceptionT}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param consumerCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param <T>                       the type of the parameter passed by the consumer
   * @return a {@link Consumer} that wraps the checked exception lambda, {@code consumerCheckedExceptionT}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <T> Consumer<T> wrapCheckedException(
      ConsumerCheckedException<T> consumerCheckedExceptionT
  ) {
    return wrapCheckedException(consumerCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Consumer} that wraps the checked exception lambda, {@code consumerCheckedExceptionT}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param consumerCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param fRuntimeExceptionWrapper  the supplier of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <EX>                      the type of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <T>                       the type of the parameter passed by the consumer
   * @return a {@link Consumer} that wraps the checked exception lambda, {@code consumerCheckedExceptionT}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, T> Consumer<T> wrapCheckedException(
      ConsumerCheckedException<T> consumerCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) -> {
      try {
        consumerCheckedExceptionT.accept(t);
      } catch (Exception exception) {
        throw fRuntimeExceptionWrapper.apply(exception);
      }
    };
  }

  /**
   * Returns a {@link Function} that wraps the checked exception lambda, {@code functionCheckedExceptionT}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param functionCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param <T>                       the type of the parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function} that wraps the checked exception lambda, {@code functionCheckedExceptionT}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <T, R> Function<T, R> wrapCheckedException(
      FunctionCheckedException<T, R> functionCheckedExceptionT
  ) {
    return wrapCheckedException(functionCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Function} that wraps the checked exception lambda, {@code functionCheckedExceptionT}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param functionCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param fRuntimeExceptionWrapper  the supplier of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <EX>                      the type of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <T>                       the type of the parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function} that wraps the checked exception lambda, {@code functionCheckedExceptionT}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, T, R> Function<T, R> wrapCheckedException(
      FunctionCheckedException<T, R> functionCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                functionCheckedExceptionT.apply(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }


  /**
   * Returns a {@link Function2} that wraps the checked exception lambda, {@code function2CheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param function2CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function2} that wraps the checked exception lambda, {@code function2CheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <A, B, R> Function2<A, B, R> wrapCheckedException(
      Function2CheckedException<A, B, R> function2CheckedException
  ) {
    return wrapCheckedException(function2CheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Function2} that wraps the checked exception lambda, {@code function2CheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param function2CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param fRuntimeExceptionWrapper  the supplier of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <EX>                      the type of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function2} that wraps the checked exception lambda, {@code function2CheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, A, B, R> Function2<A, B, R> wrapCheckedException(
      Function2CheckedException<A, B, R> function2CheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (a, b) ->
        TryCatchesOps.wrapCheckedException(() ->
                function2CheckedException.apply(a, b))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link Function3} that wraps the checked exception lambda, {@code function3CheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param function3CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function3} that wraps the checked exception lambda, {@code function3CheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <A, B, C, R> Function3<A, B, C, R> wrapCheckedException(
      Function3CheckedException<A, B, C, R> function3CheckedException
  ) {
    return wrapCheckedException(function3CheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Function3} that wraps the checked exception lambda, {@code function3CheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param function3CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param fRuntimeExceptionWrapper  the supplier of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <EX>                      the type of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function3} that wraps the checked exception lambda, {@code function3CheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, A, B, C, R> Function3<A, B, C, R> wrapCheckedException(
      Function3CheckedException<A, B, C, R> function3CheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (a, b, c) ->
        TryCatchesOps.wrapCheckedException(() ->
                function3CheckedException.apply(a, b, c))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link Function4} that wraps the checked exception lambda, {@code function4CheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param function4CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function4} that wraps the checked exception lambda, {@code function4CheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <A, B, C, D, R> Function4<A, B, C, D, R> wrapCheckedException(
      Function4CheckedException<A, B, C, D, R> function4CheckedException
  ) {
    return wrapCheckedException(function4CheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Function4} that wraps the checked exception lambda, {@code function4CheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param function4CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param fRuntimeExceptionWrapper  the supplier of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <EX>                      the type of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function4} that wraps the checked exception lambda, {@code function4CheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, A, B, C, D, R> Function4<A, B, C, D, R> wrapCheckedException(
      Function4CheckedException<A, B, C, D, R> function4CheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (a, b, c, d) ->
        TryCatchesOps.wrapCheckedException(() ->
                function4CheckedException.apply(a, b, c, d))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link Function5} that wraps the checked exception lambda, {@code function5CheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param function5CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <E>                       the type of the fifth parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function5} that wraps the checked exception lambda, {@code function5CheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <A, B, C, D, E, R> Function5<A, B, C, D, E, R> wrapCheckedException(
      Function5CheckedException<A, B, C, D, E, R> function5CheckedException
  ) {
    return wrapCheckedException(function5CheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Function5} that wraps the checked exception lambda, {@code function5CheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param function5CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param fRuntimeExceptionWrapper  the supplier of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <EX>                      the type of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <E>                       the type of the fifth parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function5} that wraps the checked exception lambda, {@code function5CheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, A, B, C, D, E, R> Function5<A, B, C, D, E, R> wrapCheckedException(
      Function5CheckedException<A, B, C, D, E, R> function5CheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (a, b, c, d, e) ->
        TryCatchesOps.wrapCheckedException(() ->
                function5CheckedException.apply(a, b, c, d, e))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link Function6} that wraps the checked exception lambda, {@code function6CheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param function6CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <E>                       the type of the fifth parameter passed into the function
   * @param <F>                       the type of the sixth parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function6} that wraps the checked exception lambda, {@code function6CheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <A, B, C, D, E, F, R> Function6<A, B, C, D, E, F, R> wrapCheckedException(
      Function6CheckedException<A, B, C, D, E, F, R> function6CheckedException
  ) {
    return wrapCheckedException(function6CheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Function6} that wraps the checked exception lambda, {@code function6CheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param function6CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param fRuntimeExceptionWrapper  the supplier of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <EX>                      the type of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <E>                       the type of the fifth parameter passed into the function
   * @param <F>                       the type of the sixth parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function6} that wraps the checked exception lambda, {@code function6CheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, A, B, C, D, E, F, R> Function6<A, B, C, D, E, F, R> wrapCheckedException(
      Function6CheckedException<A, B, C, D, E, F, R> function6CheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (a, b, c, d, e, f) ->
        TryCatchesOps.wrapCheckedException(() ->
                function6CheckedException.apply(a, b, c, d, e, f))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link Function7} that wraps the checked exception lambda, {@code function7CheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param function7CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <E>                       the type of the fifth parameter passed into the function
   * @param <F>                       the type of the sixth parameter passed into the function
   * @param <G>                       the type of the seventh parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function7} that wraps the checked exception lambda, {@code function7CheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <A, B, C, D, E, F, G, R> Function7<A, B, C, D, E, F, G, R> wrapCheckedException(
      Function7CheckedException<A, B, C, D, E, F, G, R> function7CheckedException
  ) {
    return wrapCheckedException(function7CheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Function7} that wraps the checked exception lambda, {@code function7CheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param function7CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param fRuntimeExceptionWrapper  the supplier of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <EX>                      the type of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <E>                       the type of the fifth parameter passed into the function
   * @param <F>                       the type of the sixth parameter passed into the function
   * @param <G>                       the type of the seventh parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function7} that wraps the checked exception lambda, {@code function7CheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, A, B, C, D, E, F, G, R> Function7<A, B, C, D, E, F, G, R> wrapCheckedException(
      Function7CheckedException<A, B, C, D, E, F, G, R> function7CheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (a, b, c, d, e, f, g) ->
        TryCatchesOps.wrapCheckedException(() ->
                function7CheckedException.apply(a, b, c, d, e, f, g))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link Function8} that wraps the checked exception lambda, {@code function8CheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param function8CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <E>                       the type of the fifth parameter passed into the function
   * @param <F>                       the type of the sixth parameter passed into the function
   * @param <G>                       the type of the seventh parameter passed into the function
   * @param <H>                       the type of the eighth parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function8} that wraps the checked exception lambda, {@code function8CheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <A, B, C, D, E, F, G, H, R> Function8<A, B, C, D, E, F, G, H, R> wrapCheckedException(
      Function8CheckedException<A, B, C, D, E, F, G, H, R> function8CheckedException
  ) {
    return wrapCheckedException(function8CheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Function8} that wraps the checked exception lambda, {@code function8CheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param function8CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param fRuntimeExceptionWrapper  the supplier of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <EX>                      the type of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <E>                       the type of the fifth parameter passed into the function
   * @param <F>                       the type of the sixth parameter passed into the function
   * @param <G>                       the type of the seventh parameter passed into the function
   * @param <H>                       the type of the eighth parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function8} that wraps the checked exception lambda, {@code function8CheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, A, B, C, D, E, F, G, H, R> Function8<A, B, C, D, E, F, G, H, R> wrapCheckedException(
      Function8CheckedException<A, B, C, D, E, F, G, H, R> function8CheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (a, b, c, d, e, f, g, h) ->
        TryCatchesOps.wrapCheckedException(() ->
                function8CheckedException.apply(a, b, c, d, e, f, g, h))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link Function9} that wraps the checked exception lambda, {@code function9CheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param function9CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <E>                       the type of the fifth parameter passed into the function
   * @param <F>                       the type of the sixth parameter passed into the function
   * @param <G>                       the type of the seventh parameter passed into the function
   * @param <H>                       the type of the eighth parameter passed into the function
   * @param <I>                       the type of the ninth parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function9} that wraps the checked exception lambda, {@code function9CheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <A, B, C, D, E, F, G, H, I, R> Function9<A, B, C, D, E, F, G, H, I, R> wrapCheckedException(
      Function9CheckedException<A, B, C, D, E, F, G, H, I, R> function9CheckedException
  ) {
    return wrapCheckedException(function9CheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Function9} that wraps the checked exception lambda, {@code function9CheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param function9CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param fRuntimeExceptionWrapper  the supplier of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <EX>                      the type of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <A>                       the type of the first parameter passed into the function
   * @param <B>                       the type of the second parameter passed into the function
   * @param <C>                       the type of the third parameter passed into the function
   * @param <D>                       the type of the fourth parameter passed into the function
   * @param <E>                       the type of the fifth parameter passed into the function
   * @param <F>                       the type of the sixth parameter passed into the function
   * @param <G>                       the type of the seventh parameter passed into the function
   * @param <H>                       the type of the eighth parameter passed into the function
   * @param <I>                       the type of the ninth parameter passed into the function
   * @param <R>                       the type of the result returned by the function
   * @return a {@link Function9} that wraps the checked exception lambda, {@code function9CheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, A, B, C, D, E, F, G, H, I, R> Function9<A, B, C, D, E, F, G, H, I, R> wrapCheckedException(
      Function9CheckedException<A, B, C, D, E, F, G, H, I, R> function9CheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (a, b, c, d, e, f, g, h, i) ->
        TryCatchesOps.wrapCheckedException(() ->
                function9CheckedException.apply(a, b, c, d, e, f, g, h, i))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link Function10} that wraps the checked exception lambda, {@code function10CheckedException}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param function10CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                   {@link RuntimeException}
   * @param <A>                        the type of the first parameter passed into the function
   * @param <B>                        the type of the second parameter passed into the function
   * @param <C>                        the type of the third parameter passed into the function
   * @param <D>                        the type of the fourth parameter passed into the function
   * @param <E>                        the type of the fifth parameter passed into the function
   * @param <F>                        the type of the sixth parameter passed into the function
   * @param <G>                        the type of the seventh parameter passed into the function
   * @param <H>                        the type of the eighth parameter passed into the function
   * @param <I>                        the type of the ninth parameter passed into the function
   * @param <J>                        the type of the tenth parameter passed into the function
   * @param <R>                        the type of the result returned by the function
   * @return a {@link Function10} that wraps the checked exception lambda, {@code function10CheckedException}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <A, B, C, D, E, F, G, H, I, J, R> Function10<A, B, C, D, E, F, G, H, I, J, R> wrapCheckedException(
      Function10CheckedException<A, B, C, D, E, F, G, H, I, J, R> function10CheckedException
  ) {
    return wrapCheckedException(function10CheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Function10} that wraps the checked exception lambda, {@code function10CheckedException}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param function10CheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                   {@link RuntimeException}
   * @param fRuntimeExceptionWrapper   the supplier of the RuntimeException descendant instance within which to wrap the
   *                                   checked exception, if thrown
   * @param <EX>                       the type of the RuntimeException descendant instance within which to wrap the
   *                                   checked exception, if thrown
   * @param <A>                        the type of the first parameter passed into the function
   * @param <B>                        the type of the second parameter passed into the function
   * @param <C>                        the type of the third parameter passed into the function
   * @param <D>                        the type of the fourth parameter passed into the function
   * @param <E>                        the type of the fifth parameter passed into the function
   * @param <F>                        the type of the sixth parameter passed into the function
   * @param <G>                        the type of the seventh parameter passed into the function
   * @param <H>                        the type of the eighth parameter passed into the function
   * @param <I>                        the type of the ninth parameter passed into the function
   * @param <J>                        the type of the tenth parameter passed into the function
   * @param <R>                        the type of the result returned by the function
   * @return a {@link Function10} that wraps the checked exception lambda, {@code function10CheckedException}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, A, B, C, D, E, F, G, H, I, J, R> Function10<A, B, C, D, E, F, G, H, I, J, R> wrapCheckedException(
      Function10CheckedException<A, B, C, D, E, F, G, H, I, J, R> function10CheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (a, b, c, d, e, f, g, h, i, j) ->
        TryCatchesOps.wrapCheckedException(() ->
                function10CheckedException.apply(a, b, c, d, e, f, g, h, i, j))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link Predicate} that wraps the checked exception lambda, {@code predicateCheckedExceptionT}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param predicateCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                   {@link RuntimeException}
   * @param <T>                        the type of the parameter passed by the predicate
   * @return a {@link Predicate} that wraps the checked exception lambda, {@code predicateCheckedExceptionT}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <T> Predicate<T> wrapCheckedException(
      PredicateCheckedException<T> predicateCheckedExceptionT
  ) {
    return wrapCheckedException(predicateCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Predicate} that wraps the checked exception lambda, {@code predicateCheckedExceptionT}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param predicateCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                   {@link RuntimeException}
   * @param fRuntimeExceptionWrapper   the supplier of the RuntimeException descendant instance within which to wrap the
   *                                   checked exception, if thrown
   * @param <EX>                       the type of the RuntimeException descendant instance within which to wrap the
   *                                   checked exception, if thrown
   * @param <T>                        the type of the parameter passed by the predicate
   * @return a {@link Predicate} that wraps the checked exception lambda, {@code predicateCheckedExceptionT}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, T> Predicate<T> wrapCheckedException(
      PredicateCheckedException<T> predicateCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                predicateCheckedExceptionT.test(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link Supplier} that wraps the checked exception lambda, {@code supplierCheckedExceptionT}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param supplierCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param <T>                       the type of the result returned by the supplier
   * @return a {@link Supplier} that wraps the checked exception lambda, {@code supplierCheckedExceptionT}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <T> Supplier<T> wrapCheckedException(
      SupplierCheckedException<T> supplierCheckedExceptionT
  ) {
    return wrapCheckedException(supplierCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link Supplier} that wraps the checked exception lambda, {@code supplierCheckedExceptionT}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param supplierCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                                  {@link RuntimeException}
   * @param fRuntimeExceptionWrapper  the supplier of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <EX>                      the type of the RuntimeException descendant instance within which to wrap the
   *                                  checked exception, if thrown
   * @param <T>                       the type of the result returned by the supplier
   * @return a {@link Supplier} that wraps the checked exception lambda, {@code supplierCheckedExceptionT}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, T> Supplier<T> wrapCheckedException(
      SupplierCheckedException<T> supplierCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return () ->
        TryCatchesOps.wrapCheckedException(supplierCheckedExceptionT)
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link UnaryOperator} that wraps the checked exception lambda, {@code unaryCheckedExceptionT}, with a
   * {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param unaryCheckedExceptionT the lambda which may throw a checked exception that needs to be wrapped with a
   *                               {@link RuntimeException}
   * @param <T>                    the type of the parameter passed into, and returned by the unary operator
   * @return a {@link UnaryOperator} that wraps the checked exception lambda, {@code unaryCheckedExceptionT}, with a
   *     {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <T> UnaryOperator<T> wrapCheckedException(
      UnaryOperatorCheckedException<T> unaryCheckedExceptionT
  ) {
    return wrapCheckedException(unaryCheckedExceptionT, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link UnaryOperator} that wraps the checked exception lambda, {@code unaryCheckedExceptionT}, with a
   * {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param unaryCheckedExceptionT   the lambda which may throw a checked exception that needs to be wrapped with a
   *                                 {@link RuntimeException}
   * @param fRuntimeExceptionWrapper the supplier of the RuntimeException descendant instance within which to wrap the
   *                                 checked exception, if thrown
   * @param <EX>                     the type of the RuntimeException descendant instance within which to wrap the
   *                                 checked exception, if thrown
   * @param <T>                      the type of the parameter passed into, and returned by the unary operator
   * @return a {@link UnaryOperator} that wraps the checked exception lambda, {@code unaryCheckedExceptionT}, with a
   *     {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException, T> UnaryOperator<T> wrapCheckedException(
      UnaryOperatorCheckedException<T> unaryCheckedExceptionT,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return (t) ->
        TryCatchesOps.wrapCheckedException(() ->
                unaryCheckedExceptionT.apply(t))
            .mapLeft(fRuntimeExceptionWrapper)
            .getRightOrThrowLeft();
  }

  /**
   * Returns a {@link VoidSupplier} that wraps the checked exception lambda, {@code voidSupplierCheckedException}, with
   * a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   * operations.
   *
   * @param voidSupplierCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @return a {@link VoidSupplier} that wraps the checked exception lambda, {@code voidSupplierCheckedException}, with
   *     a {@link RuntimeException} of {@link WrappedCheckedException} to enable use of the lambda within {@link Stream}
   *     operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static VoidSupplier wrapCheckedException(
      VoidSupplierCheckedException voidSupplierCheckedException
  ) {
    return wrapCheckedException(voidSupplierCheckedException, WrappedCheckedException::new);
  }

  /**
   * Returns a {@link VoidSupplier} that wraps the checked exception lambda, {@code voidSupplierCheckedException}, with
   * a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the lambda
   * within {@link Stream} operations.
   *
   * @param voidSupplierCheckedException the lambda which may throw a checked exception that needs to be wrapped with a
   *                                     {@link RuntimeException}
   * @param fRuntimeExceptionWrapper     the supplier of the RuntimeException descendant instance within which to wrap
   *                                     the checked exception, if thrown
   * @param <EX>                         the type of the RuntimeException descendant instance within which to wrap the
   *                                     checked exception, if thrown
   * @return a {@link VoidSupplier} that wraps the checked exception lambda, {@code voidSupplierCheckedException}, with
   *     a {@link RuntimeException} returned by the supplier, {@code fRuntimeExceptionWrapper}, to enable use of the
   *     lambda within {@link Stream} operations
   * @throws WrappedCheckedException wraps unrecognized non-fatal checked exceptions
   * @throws FatalThrowable          wraps unrecognized <b><em>fatal</em></b> checked exceptions
   */
  public static <EX extends RuntimeException> VoidSupplier wrapCheckedException(
      VoidSupplierCheckedException voidSupplierCheckedException,
      Function<Exception, EX> fRuntimeExceptionWrapper
  ) {
    return () -> {
      try {
        voidSupplierCheckedException.execute();
      } catch (Exception exception) {
        throw fRuntimeExceptionWrapper.apply(exception);
      }
    };
  }
}
