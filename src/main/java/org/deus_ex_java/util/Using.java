package org.deus_ex_java.util;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.deus_ex_java.util.tuple.Tuple2;
import org.deus_ex_java.util.tuple.Tuple3;
import org.deus_ex_java.util.tuple.Tuple4;
import org.deus_ex_java.util.tuple.Tuple5;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A utility class focused on {@link RuntimeException}s for transforming the Java try-with-resources statement
 * {@code try(...) {}} into an expression, enabling the use of both the error-by-value ({@code apply()}) and
 * error-by-exception ({@code applyUnsafe()}) while ensuring the proper {@link AutoCloseable#close()} of successfully
 * obtained resources.
 * <p>
 * In contrast with {@link UsingCheckedException}'s ensuring all checked exceptions are wrapped with a
 * {@link WrappedCheckedException}, this class ensures the non-{@link AutoCloseable#close()} pathways remain based on
 * {@link RuntimeException}s, and only returns a {@link WrappedCheckedException} if any call to
 * {@link AutoCloseable#close()} causes a checked exception.
 */
public class Using {

  private Using() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns an {@link Either#right(Object)} with a value of type {@code T} obtained from a single {@link AutoCloseable}
   * resource, ensuring the resource is properly closed via the proper {@link AutoCloseable#close()}, otherwise
   * {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   * {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException} with
   * its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance.
   *
   * @param fSupplierA the function to "open" the resource
   * @param fAToT      the function to "obtain" the value from resource
   * @param <A>        the type of the {@link AutoCloseable} resource
   * @param <T>        the type of the value obtained from the resource
   * @return an {@link Either#right(Object)} with a value of type {@code T} obtained from a single {@link AutoCloseable}
   *     resource, ensuring the resource is properly closed via the proper {@link AutoCloseable#close()}, otherwise
   *     {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   *     {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException}
   *     with its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance
   */
  @NotNull
  public static <A extends AutoCloseable, T> Either<RuntimeException, T> apply(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Function<A, T> fAToT
  ) {
    try (
        var a = fSupplierA.get()
    ) {

      return Either.right(fAToT.apply(a));
    } catch (WrappedCheckedException wrappedCheckedException) {
      return Either.left(
          wrappedCheckedException.getCause() instanceof RuntimeException runtimeException
              ? runtimeException
              : wrappedCheckedException);
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  /**
   * Returns a value of type {@code T} obtained from a single {@link AutoCloseable} resource, ensuring the resource is
   * properly closed via the proper {@link AutoCloseable#close()}, otherwise throws a {@link RuntimeException}, which in
   * the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   * {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown checked
   * exception instance.
   *
   * @param fSupplierA the function to "open" the resource
   * @param fAToT      the function to "obtain" the value from resource
   * @param <A>        the type of the {@link AutoCloseable} resource
   * @param <T>        the type of the value obtained from the resource
   * @return a value of type {@code T} obtained from a single {@link AutoCloseable} resource, ensuring the resource is
   *     properly closed via the proper {@link AutoCloseable#close()}, otherwise throws a {@link RuntimeException},
   *     which in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   *     {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown
   *     checked exception instance
   */
  @NotNull
  public static <A extends AutoCloseable, T> T applyUnsafe(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Function<A, T> fAToT
  ) {

    return apply(
        fSupplierA,
        fAToT)
        .getRightOrThrowLeft();
  }

  /**
   * Returns an {@link Either#right(Object)} with a value of type {@code T} obtained from two {@link AutoCloseable}
   * resources, ensuring the resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   * {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   * {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException} with
   * its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance.
   *
   * @param fSupplierA the function to "open" the {@code A} resource
   * @param fSupplierB the function to "open" the {@code B} resource
   * @param fAAndBToT  the function to "obtain" the value from the resources
   * @param <A>        the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>        the type of the {@code B} {@link AutoCloseable} resource
   * @param <T>        the type of the value obtained from the resources
   * @return an {@link Either#right(Object)} with a value of type {@code T} obtained from two {@link AutoCloseable}
   *     resources, ensuring the resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   *     {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   *     {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException}
   *     with its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      T> Either<RuntimeException, T> apply(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Supplier<B> fSupplierB,
      @NotNull Function<Tuple2<A, B>, T> fAAndBToT
  ) {
    try (
        var a = fSupplierA.get();
        var b = fSupplierB.get()
    ) {

      return Either.right(fAAndBToT.apply(new Tuple2<>(a, b)));
    } catch (WrappedCheckedException wrappedCheckedException) {
      return Either.left(
          wrappedCheckedException.getCause() instanceof RuntimeException runtimeException
              ? runtimeException
              : wrappedCheckedException);
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  /**
   * Returns a value of type {@code T} obtained from two {@link AutoCloseable} resources, ensuring the resources are
   * properly closed via the proper {@link AutoCloseable#close()}, otherwise throws a {@link RuntimeException}, which in
   * the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   * {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown checked
   * exception instance.
   *
   * @param fSupplierA the function to "open" the {@code A} resource
   * @param fSupplierB the function to "open" the {@code B} resource
   * @param fAAndBToT  the function to "obtain" the value from the resources
   * @param <A>        the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>        the type of the {@code B} {@link AutoCloseable} resource
   * @param <T>        the type of the value obtained from the resources
   * @return an {@link Either#right(Object)} with a value of type {@code T} obtained from two {@link AutoCloseable}
   *     resources, ensuring the resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   *     {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   *     {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException}
   *     with its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      T> T applyUnsafe(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Supplier<B> fSupplierB,
      @NotNull Function<Tuple2<A, B>, T> fAAndBToT
  ) {
    return apply(
        fSupplierA,
        fSupplierB,
        fAAndBToT)
        .getRightOrThrowLeft();
  }

  /**
   * Returns an {@link Either#right(Object)} with a value of type {@code T} obtained from two <i>nested</i>
   * {@link AutoCloseable} resources, ensuring the resources are properly closed via the proper
   * {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)} containing the {@link RuntimeException}, which
   * in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   * {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown checked
   * exception instance.
   *
   * @param fSupplierA the function to "open" the {@code A} resource
   * @param fAToB      the function to "open" the {@code B} resource, possibly depending upon the {@code A} resource
   * @param fTuple2ToT the function to "obtain" the value from the resources
   * @param <A>        the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>        the type of the {@code B} {@link AutoCloseable} resource
   * @param <T>        the type of the value obtained from the resources
   * @return an {@link Either#right(Object)} with a value of type {@code T} obtained from two <i>nested</i>
   *     {@link AutoCloseable} resources, ensuring the resources are properly closed via the proper
   *     {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)} containing the {@link RuntimeException},
   *     which in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   *     {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown
   *     checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      T> Either<RuntimeException, T> applyNested(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Function<A, B> fAToB,
      @NotNull Function<Tuple2<A, B>, T> fTuple2ToT
  ) {
    try (
        var a = fSupplierA.get();
        var b = fAToB.apply(a)
    ) {

      return Either.right(fTuple2ToT.apply(new Tuple2<>(a, b)));
    } catch (WrappedCheckedException wrappedCheckedException) {
      return Either.left(
          wrappedCheckedException.getCause() instanceof RuntimeException runtimeException
              ? runtimeException
              : wrappedCheckedException);
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  /**
   * Returns a value of type {@code T} obtained from two <i>nested</i> {@link AutoCloseable} resources, ensuring the
   * resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)}
   * containing the {@link RuntimeException}, which in the case of an {@link AutoCloseable#close()} failure throwing a
   * checked exception, will be a {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()}
   * containing the thrown checked exception instance.
   *
   * @param fSupplierA the function to "open" the {@code A} resource
   * @param fAToB      the function to "open" the {@code B} resource, possibly depending upon the {@code A} resource
   * @param fTuple2ToT the function to "obtain" the value from the resources
   * @param <A>        the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>        the type of the {@code B} {@link AutoCloseable} resource
   * @param <T>        the type of the value obtained from the resources
   * @return a value of type {@code T} obtained from two <i>nested</i> {@link AutoCloseable} resources, ensuring the
   *     resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   *     {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   *     {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException}
   *     with its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      T> T applyNestedUnsafe(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Function<A, B> fAToB,
      @NotNull Function<Tuple2<A, B>, T> fTuple2ToT
  ) {
    return applyNested(
        fSupplierA,
        fAToB,
        fTuple2ToT)
        .getRightOrThrowLeft();
  }

  /**
   * Returns an {@link Either#right(Object)} with a value of type {@code T} obtained from three {@link AutoCloseable}
   * resources, ensuring the resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   * {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   * {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException} with
   * its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance.
   *
   * @param fSupplierA    the function to "open" the {@code A} resource
   * @param fSupplierB    the function to "open" the {@code B} resource
   * @param fSupplierC    the function to "open" the {@code C} resource
   * @param fAAndBAndCToT the function to "obtain" the value from the resources
   * @param <A>           the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>           the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>           the type of the {@code C} {@link AutoCloseable} resource
   * @param <T>           the type of the value obtained from the resources
   * @return an {@link Either#right(Object)} with a value of type {@code T} obtained from three {@link AutoCloseable}
   *     resources, ensuring the resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   *     {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   *     {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException}
   *     with its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      T> Either<RuntimeException, T> apply(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Supplier<B> fSupplierB,
      @NotNull Supplier<C> fSupplierC,
      @NotNull Function<Tuple3<A, B, C>, T> fAAndBAndCToT
  ) {
    try (
        var a = fSupplierA.get();
        var b = fSupplierB.get();
        var c = fSupplierC.get()
    ) {

      return Either.right(fAAndBAndCToT.apply(new Tuple3<>(a, b, c)));
    } catch (WrappedCheckedException wrappedCheckedException) {
      return Either.left(
          wrappedCheckedException.getCause() instanceof RuntimeException runtimeException
              ? runtimeException
              : wrappedCheckedException);
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  /**
   * Returns a value of type {@code T} obtained from three {@link AutoCloseable} resources, ensuring the resources are
   * properly closed via the proper {@link AutoCloseable#close()}, otherwise throws a {@link RuntimeException}, which in
   * the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   * {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown checked
   * exception instance.
   *
   * @param fSupplierA    the function to "open" the {@code A} resource
   * @param fSupplierB    the function to "open" the {@code B} resource
   * @param fSupplierC    the function to "open" the {@code C} resource
   * @param fAAndBAndCToT the function to "obtain" the value from the resources
   * @param <A>           the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>           the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>           the type of the {@code C} {@link AutoCloseable} resource
   * @param <T>           the type of the value obtained from the resources
   * @return a value of type {@code T} obtained from three {@link AutoCloseable} resources, ensuring the resources are
   *     properly closed via the proper {@link AutoCloseable#close()}, otherwise throws a {@link RuntimeException},
   *     which in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   *     {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown
   *     checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      T> T applyUnsafe(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Supplier<B> fSupplierB,
      @NotNull Supplier<C> fSupplierC,
      @NotNull Function<Tuple3<A, B, C>, T> fAAndBAndCToT
  ) {
    return apply(
        fSupplierA,
        fSupplierB,
        fSupplierC,
        fAAndBAndCToT)
        .getRightOrThrowLeft();
  }

  /**
   * Returns an {@link Either#right(Object)} with a value of type {@code T} obtained from three <i>nested</i>
   * {@link AutoCloseable} resources, ensuring the resources are properly closed via the proper
   * {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)} containing the {@link RuntimeException}, which
   * in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   * {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown checked
   * exception instance.
   *
   * @param fSupplierA    the function to "open" the {@code A} resource
   * @param fAToB         the function to "open" the {@code B} resource, possibly depending upon the {@code A} resource
   * @param fAndBToC      the function to "open" the {@code C} resource, possibly depending upon the {@code A} and
   *                      {@code B} resources
   * @param fAAndBAndCToT the function to "obtain" the value from the resources
   * @param <A>           the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>           the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>           the type of the {@code C} {@link AutoCloseable} resource
   * @param <T>           the type of the value obtained from the resources
   * @return an {@link Either#right(Object)} with a value of type {@code T} obtained from three <i>nested</i>
   *     {@link AutoCloseable} resources, ensuring the resources are properly closed via the proper
   *     {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)} containing the {@link RuntimeException},
   *     which in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   *     {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown
   *     checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      T> Either<RuntimeException, T> applyNested(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Function<A, B> fAToB,
      @NotNull Function<Tuple2<A, B>, C> fAndBToC,
      @NotNull Function<Tuple3<A, B, C>, T> fAAndBAndCToT
  ) {
    try (
        var a = fSupplierA.get();
        var b = fAToB.apply(a);
        var c = fAndBToC.apply(new Tuple2<>(a, b))
    ) {

      return Either.right(fAAndBAndCToT.apply(new Tuple3<>(a, b, c)));
    } catch (WrappedCheckedException wrappedCheckedException) {
      return Either.left(
          wrappedCheckedException.getCause() instanceof RuntimeException runtimeException
              ? runtimeException
              : wrappedCheckedException);
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  /**
   * Returns a value of type {@code T} obtained from three <i>nested</i> {@link AutoCloseable} resources, ensuring the
   * resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)}
   * containing the {@link RuntimeException}, which in the case of an {@link AutoCloseable#close()} failure throwing a
   * checked exception, will be a {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()}
   * containing the thrown checked exception instance.
   *
   * @param fSupplierA    the function to "open" the {@code A} resource
   * @param fAToB         the function to "open" the {@code B} resource, possibly depending upon the {@code A} resource
   * @param fAndBToC      the function to "open" the {@code C} resource, possibly depending upon the {@code A} and
   *                      {@code B} resources
   * @param fAAndBAndCToT the function to "obtain" the value from the resources
   * @param <A>           the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>           the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>           the type of the {@code C} {@link AutoCloseable} resource
   * @param <T>           the type of the value obtained from the resources
   * @return a value of type {@code T} obtained from three <i>nested</i> {@link AutoCloseable} resources, ensuring the
   *     resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   *     {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   *     {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException}
   *     with its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      T> Either<RuntimeException, T> applyNestedUnsafe(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Function<A, B> fAToB,
      @NotNull Function<Tuple2<A, B>, C> fAndBToC,
      @NotNull Function<Tuple3<A, B, C>, T> fAAndBAndCToT
  ) {
    return applyNested(
        fSupplierA,
        fAToB,
        fAndBToC,
        fAAndBAndCToT);
  }

  /**
   * Returns an {@link Either#right(Object)} with a value of type {@code T} obtained from four {@link AutoCloseable}
   * resources, ensuring the resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   * {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   * {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException} with
   * its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance.
   *
   * @param fSupplierA        the function to "open" the {@code A} resource
   * @param fSupplierB        the function to "open" the {@code B} resource
   * @param fSupplierC        the function to "open" the {@code C} resource
   * @param fSupplierD        the function to "open" the {@code D} resource
   * @param fAAndBAndCAndDToT the function to "obtain" the value from the resources
   * @param <A>               the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>               the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>               the type of the {@code C} {@link AutoCloseable} resource
   * @param <D>               the type of the {@code D} {@link AutoCloseable} resource
   * @param <T>               the type of the value obtained from the resources
   * @return an {@link Either#right(Object)} with a value of type {@code T} obtained from four {@link AutoCloseable}
   *     resources, ensuring the resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   *     {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   *     {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException}
   *     with its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      T> Either<RuntimeException, T> apply(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Supplier<B> fSupplierB,
      @NotNull Supplier<C> fSupplierC,
      @NotNull Supplier<D> fSupplierD,
      @NotNull Function<Tuple4<A, B, C, D>, T> fAAndBAndCAndDToT
  ) {
    try (
        var a = fSupplierA.get();
        var b = fSupplierB.get();
        var c = fSupplierC.get();
        var d = fSupplierD.get()
    ) {

      return Either.right(fAAndBAndCAndDToT.apply(new Tuple4<>(a, b, c, d)));
    } catch (WrappedCheckedException wrappedCheckedException) {
      return Either.left(
          wrappedCheckedException.getCause() instanceof RuntimeException runtimeException
              ? runtimeException
              : wrappedCheckedException);
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  /**
   * Returns a value of type {@code T} obtained from four {@link AutoCloseable} resources, ensuring the resources are
   * properly closed via the proper {@link AutoCloseable#close()}, otherwise throws a {@link RuntimeException}, which in
   * the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   * {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown checked
   * exception instance.
   *
   * @param fSupplierA        the function to "open" the {@code A} resource
   * @param fSupplierB        the function to "open" the {@code B} resource
   * @param fSupplierC        the function to "open" the {@code C} resource
   * @param fSupplierD        the function to "open" the {@code D} resource
   * @param fAAndBAndCAndDToT the function to "obtain" the value from the resources
   * @param <A>               the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>               the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>               the type of the {@code C} {@link AutoCloseable} resource
   * @param <D>               the type of the {@code D} {@link AutoCloseable} resource
   * @param <T>               the type of the value obtained from the resources
   * @return a value of type {@code T} obtained from four {@link AutoCloseable} resources, ensuring the resources are
   *     properly closed via the proper {@link AutoCloseable#close()}, otherwise throws a {@link RuntimeException},
   *     which in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   *     {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown
   *     checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      T> T applyUnsafe(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Supplier<B> fSupplierB,
      @NotNull Supplier<C> fSupplierC,
      @NotNull Supplier<D> fSupplierD,
      @NotNull Function<Tuple4<A, B, C, D>, T> fAAndBAndCAndDToT
  ) {
    return apply(
        fSupplierA,
        fSupplierB,
        fSupplierC,
        fSupplierD,
        fAAndBAndCAndDToT)
        .getRightOrThrowLeft();
  }

  /**
   * Returns an {@link Either#right(Object)} with a value of type {@code T} obtained from four <i>nested</i>
   * {@link AutoCloseable} resources, ensuring the resources are properly closed via the proper
   * {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)} containing the {@link RuntimeException}, which
   * in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   * {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown checked
   * exception instance.
   *
   * @param fSupplierA        the function to "open" the {@code A} resource
   * @param fAToB             the function to "open" the {@code B} resource, possibly depending upon the {@code A}
   *                          resource
   * @param fAndBToC          the function to "open" the {@code C} resource, possibly depending upon the {@code A} and
   *                          {@code B} resources
   * @param fAndBAndCToD      the function to "open" the {@code D} resource, possibly depending upon the {@code A},
   *                          {@code B}, and {@code C} resources
   * @param fAAndBAndCAndDToT the function to "obtain" the value from the resources
   * @param <A>               the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>               the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>               the type of the {@code C} {@link AutoCloseable} resource
   * @param <D>               the type of the {@code D} {@link AutoCloseable} resource
   * @param <T>               the type of the value obtained from the resources
   * @return an {@link Either#right(Object)} with a value of type {@code T} obtained from four <i>nested</i>
   *     {@link AutoCloseable} resources, ensuring the resources are properly closed via the proper
   *     {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)} containing the {@link RuntimeException},
   *     which in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   *     {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown
   *     checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      T> Either<RuntimeException, T> applyNested(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Function<A, B> fAToB,
      @NotNull Function<Tuple2<A, B>, C> fAndBToC,
      @NotNull Function<Tuple3<A, B, C>, D> fAndBAndCToD,
      @NotNull Function<Tuple4<A, B, C, D>, T> fAAndBAndCAndDToT
  ) {
    try (
        var a = fSupplierA.get();
        var b = fAToB.apply(a);
        var c = fAndBToC.apply(new Tuple2<>(a, b));
        var d = fAndBAndCToD.apply(new Tuple3<>(a, b, c))
    ) {

      return Either.right(fAAndBAndCAndDToT.apply(new Tuple4<>(a, b, c, d)));
    } catch (WrappedCheckedException wrappedCheckedException) {
      return Either.left(
          wrappedCheckedException.getCause() instanceof RuntimeException runtimeException
              ? runtimeException
              : wrappedCheckedException);
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  /**
   * Returns a value of type {@code T} obtained from four <i>nested</i> {@link AutoCloseable} resources, ensuring the
   * resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)}
   * containing the {@link RuntimeException}, which in the case of an {@link AutoCloseable#close()} failure throwing a
   * checked exception, will be a {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()}
   * containing the thrown checked exception instance.
   *
   * @param fSupplierA        the function to "open" the {@code A} resource
   * @param fAToB             the function to "open" the {@code B} resource, possibly depending upon the {@code A}
   *                          resource
   * @param fAndBToC          the function to "open" the {@code C} resource, possibly depending upon the {@code A} and
   *                          {@code B} resources
   * @param fAndBAndCToD      the function to "open" the {@code C} resource, possibly depending upon the {@code A},
   *                          {@code B}, and {@code C} resources
   * @param fAAndBAndCAndDToT the function to "obtain" the value from the resources
   * @param <A>               the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>               the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>               the type of the {@code C} {@link AutoCloseable} resource
   * @param <D>               the type of the {@code D} {@link AutoCloseable} resource
   * @param <T>               the type of the value obtained from the resources
   * @return a value of type {@code T} obtained from four <i>nested</i> {@link AutoCloseable} * resources, ensuring the
   *     resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise *
   *     {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an *
   *     {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException}
   *     with * its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      T> Either<RuntimeException, T> applyNestedUnsafe(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Function<A, B> fAToB,
      @NotNull Function<Tuple2<A, B>, C> fAndBToC,
      @NotNull Function<Tuple3<A, B, C>, D> fAndBAndCToD,
      @NotNull Function<Tuple4<A, B, C, D>, T> fAAndBAndCAndDToT
  ) {
    return applyNested(
        fSupplierA,
        fAToB,
        fAndBToC,
        fAndBAndCToD,
        fAAndBAndCAndDToT);
  }

  /**
   * Returns an {@link Either#right(Object)} with a value of type {@code T} obtained from five {@link AutoCloseable}
   * resources, ensuring the resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   * {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   * {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException} with
   * its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance.
   *
   * @param fSupplierA            the function to "open" the {@code A} resource
   * @param fSupplierB            the function to "open" the {@code B} resource
   * @param fSupplierC            the function to "open" the {@code C} resource
   * @param fSupplierD            the function to "open" the {@code D} resource
   * @param fSupplierE            the function to "open" the {@code E} resource
   * @param fAAndBAndCAndDAndEToT the function to "obtain" the value from the resources
   * @param <A>                   the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>                   the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>                   the type of the {@code C} {@link AutoCloseable} resource
   * @param <D>                   the type of the {@code D} {@link AutoCloseable} resource
   * @param <E>                   the type of the {@code E} {@link AutoCloseable} resource
   * @param <T>                   the type of the value obtained from the resources
   * @return an {@link Either#right(Object)} with a value of type {@code T} obtained from five {@link AutoCloseable}
   *     resources, ensuring the resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   *     {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   *     {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException}
   *     with its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      E extends AutoCloseable,
      T> Either<RuntimeException, T> apply(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Supplier<B> fSupplierB,
      @NotNull Supplier<C> fSupplierC,
      @NotNull Supplier<D> fSupplierD,
      @NotNull Supplier<E> fSupplierE,
      @NotNull Function<Tuple5<A, B, C, D, E>, T> fAAndBAndCAndDAndEToT
  ) {
    try (
        var a = fSupplierA.get();
        var b = fSupplierB.get();
        var c = fSupplierC.get();
        var d = fSupplierD.get();
        var e = fSupplierE.get()
    ) {

      return Either.right(fAAndBAndCAndDAndEToT.apply(new Tuple5<>(a, b, c, d, e)));
    } catch (WrappedCheckedException wrappedCheckedException) {
      return Either.left(
          wrappedCheckedException.getCause() instanceof RuntimeException runtimeException
              ? runtimeException
              : wrappedCheckedException);
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  /**
   * Returns a value of type {@code T} obtained from five {@link AutoCloseable} resources, ensuring the resources are
   * properly closed via the proper {@link AutoCloseable#close()}, otherwise throws a {@link RuntimeException}, which in
   * the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   * {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown checked
   * exception instance.
   *
   * @param fSupplierA            the function to "open" the {@code A} resource
   * @param fSupplierB            the function to "open" the {@code B} resource
   * @param fSupplierC            the function to "open" the {@code C} resource
   * @param fSupplierD            the function to "open" the {@code D} resource
   * @param fSupplierE            the function to "open" the {@code E} resource
   * @param fAAndBAndCAndDAndEToT the function to "obtain" the value from the resources
   * @param <A>                   the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>                   the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>                   the type of the {@code C} {@link AutoCloseable} resource
   * @param <D>                   the type of the {@code D} {@link AutoCloseable} resource
   * @param <E>                   the type of the {@code E} {@link AutoCloseable} resource
   * @param <T>                   the type of the value obtained from the resources
   * @return a value of type {@code T} obtained from five {@link AutoCloseable} resources, ensuring the resources are
   *     properly closed via the proper {@link AutoCloseable#close()}, otherwise throws a {@link RuntimeException},
   *     which in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   *     {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown
   *     checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      E extends AutoCloseable,
      T> T applyUnsafe(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Supplier<B> fSupplierB,
      @NotNull Supplier<C> fSupplierC,
      @NotNull Supplier<D> fSupplierD,
      @NotNull Supplier<E> fSupplierE,
      @NotNull Function<Tuple5<A, B, C, D, E>, T> fAAndBAndCAndDAndEToT
  ) {
    return apply(
        fSupplierA,
        fSupplierB,
        fSupplierC,
        fSupplierD,
        fSupplierE,
        fAAndBAndCAndDAndEToT)
        .getRightOrThrowLeft();
  }

  /**
   * Returns an {@link Either#right(Object)} with a value of type {@code T} obtained from five <i>nested</i>
   * {@link AutoCloseable} resources, ensuring the resources are properly closed via the proper
   * {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)} containing the {@link RuntimeException}, which
   * in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   * {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown checked
   * exception instance.
   *
   * @param fSupplierA            the function to "open" the {@code A} resource
   * @param fAToB                 the function to "open" the {@code B} resource, possibly depending upon the {@code A}
   *                              resource
   * @param fAndBToC              the function to "open" the {@code C} resource, possibly depending upon the {@code A}
   *                              and {@code B} resources
   * @param fAndBAndCToD          the function to "open" the {@code D} resource, possibly depending upon the {@code A},
   *                              {@code B}, and {@code C} resources
   * @param fAndBAndCAndDToE      the function to "open" the {@code E} resource, possibly depending upon the {@code A},
   *                              {@code B}, {@code C}, and {@code D} resources
   * @param fAAndBAndCAndDAndEToT the function to "obtain" the value from the resources
   * @param <A>                   the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>                   the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>                   the type of the {@code C} {@link AutoCloseable} resource
   * @param <D>                   the type of the {@code D} {@link AutoCloseable} resource
   * @param <E>                   the type of the {@code E} {@link AutoCloseable} resource
   * @param <T>                   the type of the value obtained from the resources
   * @return an {@link Either#right(Object)} with a value of type {@code T} obtained from five <i>nested</i>
   *     {@link AutoCloseable} resources, ensuring the resources are properly closed via the proper
   *     {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)} containing the {@link RuntimeException},
   *     which in the case of an {@link AutoCloseable#close()} failure throwing a checked exception, will be a
   *     {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()} containing the thrown
   *     checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      E extends AutoCloseable,
      T> Either<RuntimeException, T> applyNested(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Function<A, B> fAToB,
      @NotNull Function<Tuple2<A, B>, C> fAndBToC,
      @NotNull Function<Tuple3<A, B, C>, D> fAndBAndCToD,
      @NotNull Function<Tuple4<A, B, C, D>, E> fAndBAndCAndDToE,
      @NotNull Function<Tuple5<A, B, C, D, E>, T> fAAndBAndCAndDAndEToT
  ) {
    try (
        var a = fSupplierA.get();
        var b = fAToB.apply(a);
        var c = fAndBToC.apply(new Tuple2<>(a, b));
        var d = fAndBAndCToD.apply(new Tuple3<>(a, b, c));
        var e = fAndBAndCAndDToE.apply(new Tuple4<>(a, b, c, d))
    ) {

      return Either.right(fAAndBAndCAndDAndEToT.apply(new Tuple5<>(a, b, c, d, e)));
    } catch (WrappedCheckedException wrappedCheckedException) {
      return Either.left(
          wrappedCheckedException.getCause() instanceof RuntimeException runtimeException
              ? runtimeException
              : wrappedCheckedException);
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  /**
   * Returns a value of type {@code T} obtained from five <i>nested</i> {@link AutoCloseable} resources, ensuring the
   * resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise {@link Either#left(Object)}
   * containing the {@link RuntimeException}, which in the case of an {@link AutoCloseable#close()} failure throwing a
   * checked exception, will be a {@link WrappedCheckedException} with its {@link WrappedCheckedException#getCause()}
   * containing the thrown checked exception instance.
   *
   * @param fSupplierA            the function to "open" the {@code A} resource
   * @param fAToB                 the function to "open" the {@code B} resource, possibly depending upon the {@code A}
   *                              resource
   * @param fAndBToC              the function to "open" the {@code C} resource, possibly depending upon the {@code A}
   *                              and {@code B} resources
   * @param fAndBAndCToD          the function to "open" the {@code D} resource, possibly depending upon the {@code A},
   *                              {@code B}, and {@code C} resources
   * @param fAndBAndCAndDToE      the function to "open" the {@code E} resource, possibly depending upon the {@code A},
   *                              {@code B}, {@code C}, and {@code D} resources
   * @param fAAndBAndCAndDAndEToT the function to "obtain" the value from the resources
   * @param <A>                   the type of the {@code A} {@link AutoCloseable} resource
   * @param <B>                   the type of the {@code B} {@link AutoCloseable} resource
   * @param <C>                   the type of the {@code C} {@link AutoCloseable} resource
   * @param <D>                   the type of the {@code D} {@link AutoCloseable} resource
   * @param <E>                   the type of the {@code E} {@link AutoCloseable} resource
   * @param <T>                   the type of the value obtained from the resources
   * @return a value of type {@code T} obtained from five <i>nested</i> {@link AutoCloseable} resources, ensuring the
   *     resources are properly closed via the proper {@link AutoCloseable#close()}, otherwise
   *     {@link Either#left(Object)} containing the {@link RuntimeException}, which in the case of an
   *     {@link AutoCloseable#close()} failure throwing a checked exception, will be a {@link WrappedCheckedException}
   *     with its {@link WrappedCheckedException#getCause()} containing the thrown checked exception instance
   */
  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      E extends AutoCloseable,
      T> T applyNestedUnsafe(
      @NotNull Supplier<A> fSupplierA,
      @NotNull Function<A, B> fAToB,
      @NotNull Function<Tuple2<A, B>, C> fAndBToC,
      @NotNull Function<Tuple3<A, B, C>, D> fAndBAndCToD,
      @NotNull Function<Tuple4<A, B, C, D>, E> fAndBAndCAndDToE,
      @NotNull Function<Tuple5<A, B, C, D, E>, T> fAAndBAndCAndDAndEToT
  ) {
    return applyNested(
        fSupplierA,
        fAToB,
        fAndBToC,
        fAndBAndCToD,
        fAndBAndCAndDToE,
        fAAndBAndCAndDAndEToT)
        .getRightOrThrowLeft();
  }
}
