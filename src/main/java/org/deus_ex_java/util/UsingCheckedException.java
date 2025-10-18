package org.deus_ex_java.util;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.deus_ex_java.util.function.FunctionCheckedException;
import org.deus_ex_java.util.function.SupplierCheckedException;
import org.deus_ex_java.util.tuple.Tuple2;
import org.deus_ex_java.util.tuple.Tuple3;
import org.deus_ex_java.util.tuple.Tuple4;
import org.deus_ex_java.util.tuple.Tuple5;
import org.jetbrains.annotations.NotNull;

//TODO: x18 missing javadocs

/**
 * A utility class focused on {@link WrappedCheckedException}s for transforming the Java try-with-resources statement
 * {@code try(...) {}} into an expression, enabling the use of both the error-by-value ({@code apply()}) and
 * error-by-exception ({@code applyUnsafe()}) while ensuring the proper {@link AutoCloseable#close()} of successfully
 * obtained resources.
 * <p>
 * In contrast with {@link Using}'s ensuring all the non-{@link AutoCloseable#close()} pathways remain based on
 * {@link RuntimeException}s exceptions, this class ensures all checked exception pathways return a
 * {@link WrappedCheckedException}.
 */
public class UsingCheckedException {

  private UsingCheckedException() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  @NotNull
  public static <A extends AutoCloseable, T> Either<RuntimeException, T> apply(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull FunctionCheckedException<A, T> fceAToT
  ) {
    try (
        var a = fceSupplierA.get()
    ) {

      return Either.right(fceAToT.apply(a));
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  @NotNull
  public static <A extends AutoCloseable, T> T applyUnsafe(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull FunctionCheckedException<A, T> fceAToT
  ) {

    return apply(
        fceSupplierA,
        fceAToT)
        .getRightOrThrowLeft();
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      T> Either<RuntimeException, T> apply(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull SupplierCheckedException<B> fceSupplierB,
      @NotNull FunctionCheckedException<Tuple2<A, B>, T> fceAAndBToT
  ) {
    try (
        var a = fceSupplierA.get();
        var b = fceSupplierB.get();
    ) {

      return Either.right(fceAAndBToT.apply(new Tuple2<>(a, b)));
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      T> T applyUnsafe(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull SupplierCheckedException<B> fceSupplierB,
      @NotNull FunctionCheckedException<Tuple2<A, B>, T> fceAAndBToT
  ) {
    return apply(
        fceSupplierA,
        fceSupplierB,
        fceAAndBToT)
        .getRightOrThrowLeft();
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      T> Either<RuntimeException, T> applyNested(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull FunctionCheckedException<A, B> fceAToB,
      @NotNull FunctionCheckedException<Tuple2<A, B>, T> fceTuple2ToT
  ) {
    try (
        var a = fceSupplierA.get();
        var b = fceAToB.apply(a);
    ) {

      return Either.right(fceTuple2ToT.apply(new Tuple2<>(a, b)));
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      T> T applyNestedUnsafe(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull FunctionCheckedException<A, B> fceAToB,
      @NotNull FunctionCheckedException<Tuple2<A, B>, T> fceTuple2ToT
  ) {
    return applyNested(
        fceSupplierA,
        fceAToB,
        fceTuple2ToT)
        .getRightOrThrowLeft();
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      T> Either<RuntimeException, T> apply(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull SupplierCheckedException<B> fceSupplierB,
      @NotNull SupplierCheckedException<C> fceSupplierC,
      @NotNull FunctionCheckedException<Tuple3<A, B, C>, T> fceAAndBAndCToT
  ) {
    try (
        var a = fceSupplierA.get();
        var b = fceSupplierB.get();
        var c = fceSupplierC.get();
    ) {

      return Either.right(fceAAndBAndCToT.apply(new Tuple3<>(a, b, c)));
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      T> T applyUnsafe(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull SupplierCheckedException<B> fceSupplierB,
      @NotNull SupplierCheckedException<C> fceSupplierC,
      @NotNull FunctionCheckedException<Tuple3<A, B, C>, T> fceAAndBAndCToT
  ) {
    return apply(
        fceSupplierA,
        fceSupplierB,
        fceSupplierC,
        fceAAndBAndCToT)
        .getRightOrThrowLeft();
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      T> Either<RuntimeException, T> applyNested(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull FunctionCheckedException<A, B> fceAToB,
      @NotNull FunctionCheckedException<Tuple2<A, B>, C> fceAndBToC,
      @NotNull FunctionCheckedException<Tuple3<A, B, C>, T> fceAAndBAndCToT
  ) {
    try (
        var a = fceSupplierA.get();
        var b = fceAToB.apply(a);
        var c = fceAndBToC.apply(new Tuple2<>(a, b));
    ) {

      return Either.right(fceAAndBAndCToT.apply(new Tuple3<>(a, b, c)));
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      T> Either<RuntimeException, T> applyNestedUnsafe(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull FunctionCheckedException<A, B> fceAToB,
      @NotNull FunctionCheckedException<Tuple2<A, B>, C> fceAndBToC,
      @NotNull FunctionCheckedException<Tuple3<A, B, C>, T> fceAAndBAndCToT
  ) {
    return applyNested(
        fceSupplierA,
        fceAToB,
        fceAndBToC,
        fceAAndBAndCToT);
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      T> Either<RuntimeException, T> apply(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull SupplierCheckedException<B> fceSupplierB,
      @NotNull SupplierCheckedException<C> fceSupplierC,
      @NotNull SupplierCheckedException<D> fceSupplierD,
      @NotNull FunctionCheckedException<Tuple4<A, B, C, D>, T> fceAAndBAndCAndDToT
  ) {
    try (
        var a = fceSupplierA.get();
        var b = fceSupplierB.get();
        var c = fceSupplierC.get();
        var d = fceSupplierD.get();
    ) {

      return Either.right(fceAAndBAndCAndDToT.apply(new Tuple4<>(a, b, c, d)));
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      T> T applyUnsafe(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull SupplierCheckedException<B> fceSupplierB,
      @NotNull SupplierCheckedException<C> fceSupplierC,
      @NotNull SupplierCheckedException<D> fceSupplierD,
      @NotNull FunctionCheckedException<Tuple4<A, B, C, D>, T> fceAAndBAndCAndDToT
  ) {
    return apply(
        fceSupplierA,
        fceSupplierB,
        fceSupplierC,
        fceSupplierD,
        fceAAndBAndCAndDToT)
        .getRightOrThrowLeft();
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      T> Either<RuntimeException, T> applyNested(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull FunctionCheckedException<A, B> fceAToB,
      @NotNull FunctionCheckedException<Tuple2<A, B>, C> fceAndBToC,
      @NotNull FunctionCheckedException<Tuple3<A, B, C>, D> fceAndBAndCToD,
      @NotNull FunctionCheckedException<Tuple4<A, B, C, D>, T> fceAAndBAndCAndDToT
  ) {
    try (
        var a = fceSupplierA.get();
        var b = fceAToB.apply(a);
        var c = fceAndBToC.apply(new Tuple2<>(a, b));
        var d = fceAndBAndCToD.apply(new Tuple3<>(a, b, c));
    ) {

      return Either.right(fceAAndBAndCAndDToT.apply(new Tuple4<>(a, b, c, d)));
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      T> Either<RuntimeException, T> applyNestedUnsafe(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull FunctionCheckedException<A, B> fceAToB,
      @NotNull FunctionCheckedException<Tuple2<A, B>, C> fceAndBToC,
      @NotNull FunctionCheckedException<Tuple3<A, B, C>, D> fceAndBAndCToD,
      @NotNull FunctionCheckedException<Tuple4<A, B, C, D>, T> fceAAndBAndCAndDToT
  ) {
    return applyNested(
        fceSupplierA,
        fceAToB,
        fceAndBToC,
        fceAndBAndCToD,
        fceAAndBAndCAndDToT);
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      E extends AutoCloseable,
      T> Either<RuntimeException, T> apply(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull SupplierCheckedException<B> fceSupplierB,
      @NotNull SupplierCheckedException<C> fceSupplierC,
      @NotNull SupplierCheckedException<D> fceSupplierD,
      @NotNull SupplierCheckedException<E> fceSupplierE,
      @NotNull FunctionCheckedException<Tuple5<A, B, C, D, E>, T> fceAAndBAndCAndDAndEToT
  ) {
    try (
        var a = fceSupplierA.get();
        var b = fceSupplierB.get();
        var c = fceSupplierC.get();
        var d = fceSupplierD.get();
        var e = fceSupplierE.get();
    ) {

      return Either.right(fceAAndBAndCAndDAndEToT.apply(new Tuple5<>(a, b, c, d, e)));
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      E extends AutoCloseable,
      T> T applyUnsafe(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull SupplierCheckedException<B> fceSupplierB,
      @NotNull SupplierCheckedException<C> fceSupplierC,
      @NotNull SupplierCheckedException<D> fceSupplierD,
      @NotNull SupplierCheckedException<E> fceSupplierE,
      @NotNull FunctionCheckedException<Tuple5<A, B, C, D, E>, T> fceAAndBAndCAndDAndEToT
  ) {
    return apply(
        fceSupplierA,
        fceSupplierB,
        fceSupplierC,
        fceSupplierD,
        fceSupplierE,
        fceAAndBAndCAndDAndEToT)
        .getRightOrThrowLeft();
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      E extends AutoCloseable,
      T> Either<RuntimeException, T> applyNested(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull FunctionCheckedException<A, B> fceAToB,
      @NotNull FunctionCheckedException<Tuple2<A, B>, C> fceAndBToC,
      @NotNull FunctionCheckedException<Tuple3<A, B, C>, D> fceAndBAndCToD,
      @NotNull FunctionCheckedException<Tuple4<A, B, C, D>, E> fceAndBAndCAndDToE,
      @NotNull FunctionCheckedException<Tuple5<A, B, C, D, E>, T> fceAAndBAndCAndDAndEToT
  ) {
    try (
        var a = fceSupplierA.get();
        var b = fceAToB.apply(a);
        var c = fceAndBToC.apply(new Tuple2<>(a, b));
        var d = fceAndBAndCToD.apply(new Tuple3<>(a, b, c));
        var e = fceAndBAndCAndDToE.apply(new Tuple4<>(a, b, c, d));
    ) {

      return Either.right(fceAAndBAndCAndDAndEToT.apply(new Tuple5<>(a, b, c, d, e)));
    } catch (RuntimeException runtimeException) {
      return Either.left(runtimeException);
    } catch (Exception exception) {
      return Either.left(new WrappedCheckedException(exception));
    }
  }

  @NotNull
  public static <
      A extends AutoCloseable,
      B extends AutoCloseable,
      C extends AutoCloseable,
      D extends AutoCloseable,
      E extends AutoCloseable,
      T> Either<RuntimeException, T> applyNestedUnsafe(
      @NotNull SupplierCheckedException<A> fceSupplierA,
      @NotNull FunctionCheckedException<A, B> fceAToB,
      @NotNull FunctionCheckedException<Tuple2<A, B>, C> fceAndBToC,
      @NotNull FunctionCheckedException<Tuple3<A, B, C>, D> fceAndBAndCToD,
      @NotNull FunctionCheckedException<Tuple4<A, B, C, D>, E> fceAndBAndCAndDToE,
      @NotNull FunctionCheckedException<Tuple5<A, B, C, D, E>, T> fceAAndBAndCAndDAndEToT
  ) {
    return applyNested(
        fceSupplierA,
        fceAToB,
        fceAndBToC,
        fceAndBAndCToD,
        fceAndBAndCAndDToE,
        fceAAndBAndCAndDAndEToT);
  }
}
