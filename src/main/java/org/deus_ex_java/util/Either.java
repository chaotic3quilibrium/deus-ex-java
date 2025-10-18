package org.deus_ex_java.util;

import org.deus_ex_java.lang.WrappedCheckedException;
import org.deus_ex_java.util.function.SupplierChecked;
import org.deus_ex_java.util.function.SupplierCheckedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Represents an immutable value of one of two possible types (a <a
 * href="https://en.wikipedia.org/wiki/Disjoint_union">disjoint union</a>). An instance of {@link Either} is
 * (constructor enforced via preconditions) to be well-defined and for whichever side is defined, the left or right, the
 * value is guaranteed to be not-{@code null}.
 * <p>
 * -
 * <p>
 * A common use of {@link Either} is as an alternative to {@link Optional} for dealing with possibly erred or missing
 * values. In this usage, {@link Optional#isEmpty} is replaced with {@link Either#getLeft} which, unlike
 * {@link Optional#isEmpty}, can contain useful information, like a descriptive error message. {@link Either#getRight}
 * takes the place of {@link java.util.Optional#get}
 * <p>
 * -
 * <p>
 * {@link Either} is right-biased, which means {@link Either#getRight} is assumed to be the default case upon which to
 * operate. If it is defined for the left, operations like {@link Either#toOptional} returns {@link Optional#isEmpty},
 * and {@link Either#map} and {@link Either#flatMap} return the left value unchanged.
 * <p>
 * -
 * <p>
 * In an effort to enhance {@link Either}'s right-biased nature, the projections for each of the sides have not been
 * provided, in favor of using the {@link #swap} method.
 * <p>
 * -
 * <p>
 * {@link Either} explicitly rejects containing a {@code null} value for either side, and will throw a
 * {@link java.lang.NullPointerException} when passed to any methods.
 **/
@SuppressWarnings("ClassCanBeRecord")
public final class Either<L, R> {

  /**
   * Reify to an {@link Either}. Throws an {@link IllegalArgumentException} if both return the same value for
   * {@link Optional#isEmpty}, otherwise if {@code rightOptional} is defined, place the {@link Optional} value into the
   * right side of the {@link Either}, otherwise place the {@link Optional} value from {@code leftOptional} into the
   * left side of the {@link Either}.
   *
   * @param leftOptional  the contained value is placed into the left side of the {@link Either}
   * @param rightOptional the contained value is placed into the right side of the {@link Either}
   * @param <L>           type of the value in the instance of the {@link Optional}
   * @param <R>           type of the value in the instance of the {@link Optional}
   * @return a well-defined instance of {@link Either}
   * @throws IllegalArgumentException if both, return the same value for {@link Optional#isEmpty}
   */
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  @NotNull
  public static <L, R> Either<L, R> from(
      @NotNull Optional<L> leftOptional,
      @NotNull Optional<R> rightOptional
  ) {
    if (leftOptional.isEmpty() == rightOptional.isEmpty()) {
      throw new IllegalArgumentException("leftOptional.isEmpty() must not be equal to rightOptional.isEmpty()");
    }

    return new Either<>(
        rightOptional.isPresent(),
        rightOptional.isPresent()
            ? rightOptional
            : leftOptional);
  }

  /**
   * Returns the right side of a disjoint union, as opposed to the left side.
   *
   * @param value instance of type R to be contained
   * @param <L>   the type of the left value to be contained
   * @param <R>   the type of the right value to be contained
   * @return an instance of {@link Either} well-defined for the right side
   * @throws NullPointerException if value is {@code null}
   */
  @NotNull
  public static <L, R> Either<L, R> right(@NotNull R value) {
    return Either.from(Optional.empty(), Optional.of(value));
  }

  /**
   * Returns the left side of a disjoint union, as opposed to the right side.
   *
   * @param value instance of type L to be contained
   * @param <L>   the type of the left value to be contained
   * @param <R>   the type of the right value to be contained
   * @return an instance of {@link Either} well-defined for the left side
   * @throws NullPointerException if value is {@code null}
   */
  @NotNull
  public static <L, R> Either<L, R> left(@NotNull L value) {
    return Either.from(Optional.of(value), Optional.empty());
  }

  /**
   * Reify to an {@link Either}. If defined, place the {@link Optional} value into the right side of the {@link Either},
   * or else use the {@link Supplier} to define the left side of the {@link Either}.
   *
   * @param leftSupplier  function invoked (only if rightOptional.isEmpty() returns true) to place the returned value
   *                      for the left side of the {@link Either}
   * @param rightOptional the contained value is placed into the right side of the {@link Either}
   * @param <L>           type of the instance provided by the {@link Supplier}
   * @param <R>           type of the value in the instance of the {@link Optional}
   * @return a well-defined instance of {@link Either}
   * @throws NullPointerException if leftSupplier, the value returned if called, rightOptional, or the value returned if
   *                              extracted, is {@code null}
   */
  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  @NotNull
  public static <L, R> Either<L, R> from(
      @NotNull Supplier<L> leftSupplier,
      @NotNull Optional<R> rightOptional
  ) {
    return rightOptional
        .map(Either::<L, R>right)
        .orElseGet(() ->
            Either.left(Objects.requireNonNull(leftSupplier.get())));
  }

  /**
   * Reify a try/catch statement into an {@link Either}. When the {@link Supplier} is invoked, if there is no
   * {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within the right side of
   * an {@link Either}. If an {@link Throwable} exception is thrown and the exception is an instance of the
   * {@code throwableClass}, the exception is returned within the left side of an {@link Either}. Otherwise, the
   * exception is re-thrown.
   *
   * @param successSupplier function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, that when
   *                        invoked, if there is no exception thrown, the function's return value is returned within the
   *                        right side of an {@link Either}
   * @param throwableClass  if the {@code successSupplier} function throws an exception, if it is an instance of
   *                        {@code throwableClass}, the exception is returned within the left side of an {@link Either},
   *                        otherwise, the exception is re-{@code thrown}
   * @param <L>             type of the {@link Throwable} instance being caught
   * @param <R>             type of the instance provided by the {@link Supplier}
   * @return a well-defined instance of {@link Either}
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public static <L extends Throwable, R> Either<L, R> tryCatch(
      @NotNull Supplier<R> successSupplier,
      @NotNull Class<L> throwableClass
  ) {
    try {
      return Either.right(Objects.requireNonNull(successSupplier.get()));
    } catch (Throwable throwable) {
      if (throwableClass.isInstance(throwable)) {
        return Either.left((L) throwable);
      }

      throw throwable;
    }
  }

  /**
   * Reify a try/catch statement into an {@link Either}. If when the {@link Supplier} is invoked there is no
   * {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within the right side of
   * an {@link Either}. If an {@link RuntimeException} exception is thrown, the exception is returned within the left
   * side of an {@link Either}, otherwise the exception is re-thrown.
   *
   * @param successSupplier function wrapped in the {@code try {...} catch (Throwable ...) {...} } block, and when
   *                        invoked, if there is no exception thrown, the function's return value is returned within the
   *                        right side of an {@link Either}
   * @param <R>             type of the instance provided by the {@link Supplier}
   * @return a well-defined instance of {@link Either}
   */
  @NotNull
  public static <R> Either<RuntimeException, R> tryCatch(@NotNull Supplier<R> successSupplier) {
    return tryCatch(successSupplier, RuntimeException.class);
  }

  /**
   * Reify a try/catch statement into an {@link Either}. When the {@link Supplier} is invoked, if there is no
   * {@link Throwable} exception thrown, the value returned by the {@link Supplier} is returned within the right side of
   * an {@link Either}. If an {@link Throwable} exception is thrown and the exception is an instance of the
   * {@code throwableClass}, the exception is returned within the left side of an {@link Either}. Otherwise, if the
   * exception is an instance of {@link RuntimeException}, it is rethrown, otherwise a new
   * {@link WrappedCheckedException} wrapping the checked exception is thrown.
   *
   * @param successSupplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} }
   *                                        block, that when invoked, if there is no exception thrown, the function's
   *                                        return value is returned within the right side of an {@link Either}
   * @param throwableClass                  if the {@code successSupplier} function throws an exception, if it is an
   *                                        instance of {@code throwableClass}, the exception is returned within the
   *                                        left side of an {@link Either}, otherwise, if the exception is an instance
   *                                        of {@link RuntimeException}, it is rethrown, otherwise a new
   *                                        {@link WrappedCheckedException} wrapping the exception is thrown
   * @param <L>                             type of the {@link Throwable} instance being caught
   * @param <R>                             type of the instance provided by the {@link Supplier}
   * @return a well-defined instance of {@link Either}
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public static <L extends Throwable, R> Either<L, R> tryCatchChecked(
      @NotNull SupplierCheckedException<R> successSupplierCheckedException,
      @NotNull Class<L> throwableClass
  ) {
    try {
      return Either.right(Objects.requireNonNull(successSupplierCheckedException.get()));
    } catch (Throwable throwable) {
      if (throwableClass.isInstance(throwable)) {
        return Either.left((L) throwable);
      }
      if (throwable instanceof RuntimeException runtimeException) {
        throw runtimeException;
      }

      throw new WrappedCheckedException("tryCatchChecked failure - " + throwable.getMessage(),
          throwable);
    }
  }

  /**
   * Reify a try/catch statement into an {@link Either}. If when the {@link SupplierChecked} is invoked there is no
   * {@link Throwable} exception thrown, the value returned by the {@link SupplierChecked} is returned within the right
   * side of an {@link Either}. If an {@link Exception} exception is thrown, the exception is returned within the left
   * side of an {@link Either}, otherwise a {@link WrappedCheckedException}, wrapping the caught exception, is thrown.
   *
   * @param successSupplierCheckedException function wrapped in the {@code try {...} catch (Throwable ...) {...} }
   *                                        block, and when invoked, if there is no exception thrown, the function's
   *                                        return value is returned within the right side of an {@link Either}
   * @param <R>                             type of the instance provided by the {@link Supplier}
   * @return a well-defined instance of {@link Either}
   */
  @NotNull
  public static <R> Either<Exception, R> tryCatchChecked(
      @NotNull SupplierCheckedException<R> successSupplierCheckedException
  ) {
    return tryCatchChecked(successSupplierCheckedException, Exception.class);
  }

  private final boolean isRight;
  private final Object untypedOptionalValue;

  private Either(boolean isRight, Object untypedOptionalValue) {
    this.isRight = isRight;
    this.untypedOptionalValue = untypedOptionalValue;
  }

  /**
   * Indicates whether some other instance is equivalent to {@code this}.
   *
   * @param object reference instance with which to compare
   * @return true if {@code this} instance is the equivalent value as the object argument
   */
  @Override
  public boolean equals(@Nullable Object object) {
    return (this == object) ||
        ((object instanceof Either<?, ?> that)
            && Objects.equals(this.isRight, that.isRight)
            && Objects.equals(this.untypedOptionalValue, that.untypedOptionalValue));
  }

  /**
   * Returns a hash code value for this instance.
   *
   * @return a hash code value for this instance
   */
  @Override
  public int hashCode() {
    return Objects.hash(this.isRight, this.untypedOptionalValue);
  }

  /**
   * Returns true if this {@link Either} is defined on the right side
   *
   * @return true if the right side of this {@link Either} contains a value
   */
  public boolean isRight() {
    return this.isRight;
  }

  /**
   * Returns true if this {@link Either} is defined on the left side
   *
   * @return true if the left side of this {@link Either} contains a value
   */
  public boolean isLeft() {
    return !this.isRight;
  }

  /**
   * If defined (which can be detected with {@link Either#isRight}), returns the value for the right side of
   * {@link Either}, or else throws an {@link NoSuchElementException}
   *
   * @return value of type R for the left, if the right side of this {@link Either} is defined
   * @throws NoSuchElementException if the right side of this {@link Either} is not defined
   */
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  @NotNull
  public R getRight() {
    return toOptionalRight().get();
  }

  /**
   * If defined (which can be detected with {@link Either#isLeft}), returns the value for the left side of
   * {@link Either}, or else throws an {@link NoSuchElementException}.
   *
   * @return value of type L for the left, if the left side of this {@link Either} is defined
   * @throws NoSuchElementException if the left side of this {@link Either} is not defined
   */
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  @NotNull
  public L getLeft() {
    return toOptionalLeft().get();
  }

  /**
   * Reduce to an Optional. If defined, returns the value for the right side of {@link Either} in an
   * {@link Optional#of}, or else returns {@link Optional#empty}. Forwards call to {@link Either#toOptionalRight}.
   *
   * @return an {@link Optional} containing the right side if defined, or else returns {@link Optional#empty}
   */
  @NotNull
  public Optional<R> toOptional() {
    return toOptionalRight();
  }

  /**
   * Reduce to an Optional. If defined, returns the value for the right side of {@link Either} in an
   * {@link Optional#of}, or else returns {@link Optional#empty}.
   *
   * @return an {@link Optional} containing the right side if defined, or else returns {@link Optional#empty}
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public Optional<R> toOptionalRight() {
    return this.isRight()
        ? ((Optional<R>) this.untypedOptionalValue)
        : Optional.empty();
  }

  /**
   * Reduce to an Optional. If defined, returns the value for the left side of {@link Either} in an {@link Optional#of},
   * or else returns {@link Optional#empty}.
   *
   * @return an {@link Optional} containing the left side if defined, or else returns {@link Optional#empty}
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public Optional<L> toOptionalLeft() {
    return !this.isRight()
        ? ((Optional<L>) this.untypedOptionalValue)
        : Optional.empty();
  }

  /**
   * If right is defined, the given map translation function is applied. Forwards call to {@link Either#mapRight}.
   *
   * @param rightFunction given function which is only applied if right is defined
   * @param <T>           target type to which R is translated
   * @return result of the function translation, replacing type R with type T
   */
  @NotNull
  public <T> Either<L, T> map(@NotNull Function<? super R, ? extends T> rightFunction) {
    return mapRight(rightFunction);
  }

  /**
   * If right is defined, the given flatMap translation function is applied. Forwards call to
   * {@link Either#flatMapRight}.
   *
   * @param rightFunction given function which is only applied if right is defined
   * @param <T>           target type to which R is translated
   * @return result of the function translation, replacing type R with type T
   */
  @NotNull
  public <T> Either<L, T> flatMap(
      @NotNull Function<? super R, ? extends Either<L, ? extends T>> rightFunction
  ) {
    return flatMapRight(rightFunction);
  }

  /**
   * Returns {@link Either#left} when {@link Either#isLeft} is {@code true}, or returns {@link Either#right} when
   * {@link Either#isRight} is {@code true} and {@code retainingRight} returns {@code true}, or returns the value
   * returned by {@code leftFunction} within {@link Either#left}. Forwards call to {@link Either#filterOrElseRight}
   *
   * @param retainingRight predicate which is only applied if {@link Either#isRight} is {@code true}
   * @param leftFunction   given function which is only applied if {@link Either#isRight} is {@code true} and
   *                       {@code retainingRight} returns {@code false}
   * @return {@link Either#left} when {@link Either#isLeft} is {@code true}, or returns {@link Either#right} when
   *     {@link Either#isRight} is {@code true} and {@code retainingRight} returns {@code true}
   */
  @NotNull
  public Either<L, R> filterOrElse(
      @NotNull Predicate<R> retainingRight,
      @NotNull Supplier<L> leftFunction
  ) {
    return filterOrElseRight(retainingRight, leftFunction);
  }

  /**
   * If left is defined, the given map translation function is applied.
   *
   * @param leftFunction given function which is only applied if left is defined
   * @param <T>          target type to which L is translated
   * @return result of the function translation, replacing type L with type T
   * @throws NullPointerException if leftFunction or the value it returns is {@code null}
   */
  @NotNull
  public <T> Either<T, R> mapLeft(@NotNull Function<? super L, ? extends T> leftFunction) {
    //noinspection unchecked
    return isLeft()
        //@formatter:off
        ? Either.from(
            this.toOptionalLeft().map(l ->
                Objects.requireNonNull(leftFunction.apply(l))),
            Optional.empty())
        : (Either<T, R>) this;
        //@formatter:off
  }

  /**
   * If right is defined, the given map translation function is applied.
   *
   * @param rightFunction given function which is only applied if right is defined
   * @param <T>           target type to which R is translated
   * @return result of the function translation, replacing type R with type T
   * @throws NullPointerException if rightFunction or the value it returns is {@code null}
   */
  @NotNull
  public <T> Either<L, T> mapRight(@NotNull Function<? super R, ? extends T> rightFunction) {
    //noinspection unchecked
    return isRight()
        //@formatter:off
        ? Either.from(
            this.toOptionalLeft(),
            this.toOptionalRight().map(r ->
                Objects.requireNonNull(rightFunction.apply(r))))
        : (Either<L, T>) this;
        //@formatter:off
  }

  /**
   * If left is defined, the given flatMap translation function is applied.
   *
   * @param leftFunction given function which is only applied if left is defined
   * @param <T>          target type to which L is translated
   * @return result of the function translation, replacing type L with type T
   * @throws NullPointerException if leftFunction or the value it returns is {@code null}
   */
  @NotNull
  public <T> Either<T, R> flatMapLeft(
      @NotNull Function<? super L, ? extends Either<? extends T, R>> leftFunction
  ) {
    //noinspection unchecked
    return this.toOptionalLeft()
        .<Either<T, R>>map(l ->
            Either.left(Objects.requireNonNull(leftFunction.apply(l)).getLeft()))
        .orElseGet(() ->
            (Either<T, R>) this);
  }

  /**
   * If right is defined, the given flatMap translation function is applied.
   *
   * @param rightFunction given function which is only applied if right is defined
   * @param <T>           target type to which R is translated
   * @return result of the function translation, replacing type R with type T
   * @throws NullPointerException if rightFunction or the value it returns is {@code null}
   */
  @NotNull
  public <T> Either<L, T> flatMapRight(
      @NotNull Function<? super R, ? extends Either<L, ? extends T>> rightFunction
  ) {
    //noinspection unchecked
    return this.toOptionalRight()
        .<Either<L, T>>map(r ->
            Either.right(Objects.requireNonNull(rightFunction.apply(r)).getRight()))
        .orElseGet(() ->
            (Either<L, T>) this);
  }

  /**
   * Returns {@link Either#right} when {@link Either#isRight} is {@code true}, or returns {@link Either#left} when
   * {@link Either#isLeft} is {@code true} and {@code retainingLeft} returns {@code true}, or returns the value returned
   * by {@code rightFunction} within {@link Either#right}.
   *
   * @param retainingLeft predicate which is only applied if {@link Either#isLeft} is {@code true}
   * @param rightFunction given function which is only applied if {@link Either#isLeft} is {@code true} and
   *                      {@code retainingLeft} returns {@code false}
   * @return {@link Either#right} when {@link Either#isRight} is {@code true}, or returns {@link Either#left} when
   * {@link Either#isLeft} is {@code true} and {@code retainingLeft} returns {@code true}, or returns the value
   * returned by {@code rightFunction} within {@link Either#right}
   */
  @NotNull
  public Either<L, R> filterOrElseLeft(
      @NotNull Predicate<L> retainingLeft,
      @NotNull Supplier<R> rightFunction
  ) {
    return this.toOptionalLeft()
        .<Either<L, R>>map(l ->
            retainingLeft.test(l)
                ? this
                : Either.right(rightFunction.get()))
        .orElse(this);
  }

  /**
   * Returns {@link Either#left} when {@link Either#isLeft} is {@code true}, or returns {@link Either#right} when
   * {@link Either#isRight} is {@code true} and {@code retainingRight} returns {@code true}, or returns the value
   * returned by {@code leftFunction} within {@link Either#left}.
   *
   * @param retainingRight predicate which is only applied if {@link Either#isRight} is {@code true}
   * @param leftFunction   given function which is only applied if {@link Either#isRight} is {@code true} and
   *                       {@code retainingRight} returns {@code false}
   * @return {@link Either#left} when {@link Either#isLeft} is {@code true}, or returns {@link Either#right} when
   * {@link Either#isRight} is {@code true} and {@code retainingRight} returns {@code true}
   */
  @NotNull
  public Either<L, R> filterOrElseRight(
      @NotNull Predicate<R> retainingRight,
      @NotNull Supplier<L> leftFunction
  ) {
    return this.toOptionalRight()
        .<Either<L, R>>map(r ->
            retainingRight.test(r)
                ? this
                : Either.left(leftFunction.get()))
        .orElse(this);
  }

  /**
   * Returns reversed type such that if this is a {@link Either#left}, then return the {@link Either#left} value in
   * {@link Either#right} or vice versa.
   *
   * @return reversed type such that if this is a {@link Either#left}, then return the {@link Either#left} value in
   * {@link Either#right} or vice versa
   */
  @NotNull
  public Either<R, L> swap() {
    return new Either<>(!this.isRight, this.untypedOptionalValue);
  }

  /**
   * Converge the distinct types, L and R, to a common type, T. This method's implementation is right-biased.
   *
   * @param leftFunction  given function which is only applied if left is defined
   * @param rightFunction given function which is only applied if right is defined
   * @param <T>           type of the returned instance
   * @return an instance of T
   * @throws NullPointerException if leftFunction, the value it returns, rightFunction, or the value it returns is
   *                              {@code null}
   */
  @NotNull
  public <T> T converge(
      @NotNull Function<? super L, ? extends T> leftFunction,
      @NotNull Function<? super R, ? extends T> rightFunction
  ) {
    return this.toOptionalRight()
        .<T>map(r ->
            Objects.requireNonNull(rightFunction.apply(r)))
        .orElseGet(() ->
            this.toOptionalLeft()
                .map(l ->
                    Objects.requireNonNull(leftFunction.apply(l)))
                .orElseThrow(() ->
                    new IllegalStateException("should never get here")));
  }

  /**
   * Converge the distinct types, L and R, to a common type, T. This method's implementation is right-biased. It is the
   * equivalent of the following method call:
   * <p>
   * {@code this.converge(Function.identity(), Function.identity())}
   * <p>
   * ---
   * <p>
   * **WARNING:**:
   * <p>
   * The validity of type T is only checked at run time, not at compile time. This is due to an issue with Java
   * generics.
   * <p>
   * My preferred method of solving this would have been...
   * <pre>
   *   {@code public <T extends L & R> T converge() {
   *     return left.isPresent()
   *         ? left.get()
   *         : right.get();
   *   }}
   * </pre>
   * However, this produces a compiler error on the R in {@code <T extends L & R> }, as explained
   * <a href="https://stackoverflow.com/a/30829160/501113">here</a>.
   *
   * @param <T> type of the returned instance
   * @return an instance of T
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public <T> T converge() {
    return (T) converge(this);
  }

  /**
   * Converge the distinct types, L and R, to a common type, T. This method's implementation is right-biased.
   * <p>
   * {@code var t = either.converge(Function.identity(), Function.identity())}
   * <p>
   * ---
   * <p>
   * Note: The validity of type T is checked at compile time.
   *
   * @param either the instance of {@link Either} where both L and R share T as a common supertype
   * @param <T>    type of the returned instance
   * @return an instance of T
   */
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  @NotNull
  public static <T> T converge(@NotNull Either<? extends T, ? extends T> either) {
    return either.toOptionalRight().isPresent()
        ? either.toOptionalRight().get()
        : either.toOptionalLeft().get();
  }

  /**
   * Returns {@link Either#getRight()} when is {@link Either#isRight()} is {@code true}, otherwise throws a
   * {@link RuntimeException}, which, if {@link L} is an instance of a {@link RuntimeException}, then {@link L} is
   * directly thrown, otherwise when {@link L} is a {@link Throwable}, {@link L} is wrapped in the
   * {@link IllegalStateException}, otherwise nothing is wrapped in the thrown {@link IllegalStateException}.
   *
   * @return {@link Either#getRight()} when is {@link Either#isRight()} is {@code true}, otherwise throws a
   * {@link RuntimeException}, which, if {@link L} is an instance of a {@link RuntimeException}, then {@link L} is
   * directly thrown, otherwise when {@link L} is a {@link Throwable}, {@link L} is wrapped in the
   * {@link IllegalStateException}, otherwise nothing is wrapped in the thrown {@link IllegalStateException}
   */
  @NotNull
  public R getRightOrThrowLeft() {
    if (this.isLeft()) {
      if (this.getLeft() instanceof RuntimeException runtimeException) {
        throw runtimeException;
      } else {
        var message = "getLeft() [%s] must be an instance of RuntimeException".formatted(
            this.getLeft().getClass().getName());
        if (this.getLeft() instanceof Throwable throwable) {
          throw new IllegalStateException(message, throwable);
        } else {
          throw new IllegalStateException(message);
        }
      }
    }

    return this.getRight();
  }

  /**
   * Returns {@link Either#getRight()} when is {@link Either#isRight()} is {@code true}, otherwise throws a
   * {@link Throwable}, which, if {@link L} is an instance of a {@link Throwable}, then {@link L} is directly thrown,
   * otherwise nothing is wrapped in the thrown {@link IllegalStateException}.
   *
   * @return {@link Either#getRight()} when is {@link Either#isRight()} is {@code true}, otherwise throws a
   * {@link Throwable}, which, if {@link L} is an instance of a {@link Throwable}, then {@link L} is directly
   * thrown, otherwise nothing is wrapped in the thrown {@link IllegalStateException}
   */
  @NotNull
  public R getRightOrThrowLeftCheckedException() throws Throwable {
    if (this.isLeft()) {
      if (this.getLeft() instanceof Throwable throwable) {
        throw throwable;
      } else {
        throw new IllegalStateException(
            "getLeft() [%s] must be an instance of Throwable".formatted(
                this.getLeft().getClass().getName()));
      }
    }

    return this.getRight();
  }

  /**
   * Execute the given side-effecting function depending upon which side is defined. This method's implementation is
   * right-biased.
   *
   * @param leftAction  given function is only executed if left is defined
   * @param rightAction given function is only executed if right is defined
   */
  public void forEach(
      @NotNull Consumer<? super L> leftAction,
      @NotNull Consumer<? super R> rightAction
  ) {
    this.toOptionalRight().ifPresent(rightAction);
    this.toOptionalLeft().ifPresent(leftAction);
  }
}