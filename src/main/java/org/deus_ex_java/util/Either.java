package org.deus_ex_java.util;

import org.jspecify.annotations.NullMarked;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents an immutable value of one of two possible types (a <a
 * href="https://en.wikipedia.org/wiki/Disjoint_union">disjoint union</a>). An instance of {@link Either} is guaranteed
 * to be well-defined for either the left side or the right side, and the contained value is guaranteed to be
 * non-{@code null}.
 * <p>
 * As a Java 17+ sealed interface permitting nested records {@link Either.Left} and {@link Either.Right}, {@link Either}
 * supports exhaustive pattern matching and record destructuring in {@code switch} expressions without requiring a
 * {@code default} clause.
 * <p>
 * A common use of {@link Either} is as an alternative to {@link Optional} for dealing with possibly erred or missing
 * values. In this usage, {@link Optional#isEmpty} is replaced with {@link Either#getLeft} which, unlike
 * {@link Optional#isEmpty}, can contain useful information, like a descriptive error message. {@link Either#getRight}
 * takes the place of {@link java.util.Optional#get}.
 * <p>
 * {@link Either} is right-biased, which means {@link Either#getRight} is assumed to be the default case upon which to
 * operate. If it is defined for the left, operations like {@link Either#toOptional} return {@link Optional#isEmpty},
 * and {@link Either#map} and {@link Either#flatMap} return the left value unchanged.
 * <p>
 * {@link Either} explicitly rejects containing a {@code null} value for either side, and will throw a
 * {@link java.lang.NullPointerException} when passed {@code null}.
 *
 * @param <L> the type of the left value
 * @param <R> the type of the right value
 **/
@SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "unchecked"})
@NullMarked
public sealed interface Either<L, R> permits Either.Left, Either.Right {

  /**
   * Represents the left side of an {@link Either} disjoint union.
   *
   * @param value contained left value, strictly non-null
   * @param <L>   the type of the left value
   * @param <R>   the type of the right value
   */
  record Left<L, R>(L value) implements Either<L, R> {
    public Left {
      Objects.requireNonNull(value, "Left value must not be null");
    }

    @Override
    public boolean isLeft() {
      return true;
    }

    @Override
    public boolean isRight() {
      return false;
    }

    @Override
    public L getLeft() {
      return value;
    }

    @Override
    public R getRight() {
      throw new NoSuchElementException("No value present");
    }

    @Override
    public Optional<L> toOptionalLeft() {
      return Optional.of(value);
    }

    @Override
    public Optional<R> toOptionalRight() {
      return Optional.empty();
    }
  }

  /**
   * Represents the right side of an {@link Either} disjoint union.
   *
   * @param value contained right value, strictly non-null
   * @param <L>   the type of the left value
   * @param <R>   the type of the right value
   */
  record Right<L, R>(R value) implements Either<L, R> {
    public Right {
      Objects.requireNonNull(value, "Right value must not be null");
    }

    @Override
    public boolean isLeft() {
      return false;
    }

    @Override
    public boolean isRight() {
      return true;
    }

    @Override
    public L getLeft() {
      throw new NoSuchElementException("No value present");
    }

    @Override
    public R getRight() {
      return value;
    }

    @Override
    public Optional<L> toOptionalLeft() {
      return Optional.empty();
    }

    @Override
    public Optional<R> toOptionalRight() {
      return Optional.of(value);
    }
  }

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
   * @throws IllegalArgumentException if both return the same value for {@link Optional#isEmpty}
   */
  static <L, R> Either<L, R> from(
      Optional<L> leftOptional,
      Optional<R> rightOptional
  ) {
    if (leftOptional.isEmpty() == rightOptional.isEmpty()) {
      throw new IllegalArgumentException("leftOptional.isEmpty() must not be equal to rightOptional.isEmpty()");
    }

    return rightOptional
        .<Either<L, R>>map(Either::right)
        .orElseGet(() ->
            left(leftOptional.get()));
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
  static <L, R> Either<L, R> right(R value) {
    return new Right<>(value);
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
  static <L, R> Either<L, R> left(L value) {
    return new Left<>(value);
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
  static <L, R> Either<L, R> from(
      Supplier<L> leftSupplier,
      Optional<R> rightOptional
  ) {
    return rightOptional
        .<Either<L, R>>map(Either::right)
        .orElseGet(() ->
            Either.left(Objects.requireNonNull(leftSupplier.get())));
  }

  /**
   * Returns true if this {@link Either} is defined on the left side
   *
   * @return true if the left side of this {@link Either} contains a value
   */
  boolean isLeft();

  /**
   * Returns true if this {@link Either} is defined on the right side
   *
   * @return true if the right side of this {@link Either} contains a value
   */
  boolean isRight();

  /**
   * If defined (which can be detected with {@link Either#isRight}), returns the value for the right side of
   * {@link Either}, or else throws a {@link NoSuchElementException}.
   *
   * @return value of type R for the right side, if defined
   * @throws NoSuchElementException if the right side of this {@link Either} is not defined
   */
  R getRight();

  /**
   * If defined (which can be detected with {@link Either#isLeft}), returns the value for the left side of
   * {@link Either}, or else throws a {@link NoSuchElementException}.
   *
   * @return value of type L for the left side, if defined
   * @throws NoSuchElementException if the left side of this {@link Either} is not defined
   */
  L getLeft();

  /**
   * Reduce to an Optional. If defined, returns the value for the right side of {@link Either} in an
   * {@link Optional#of}, or else returns {@link Optional#empty}. Forwards call to {@link Either#toOptionalRight}.
   *
   * @return an {@link Optional} containing the right side if defined, or else returns {@link Optional#empty}
   */
  default Optional<R> toOptional() {
    return toOptionalRight();
  }

  /**
   * Returns a stream from forwarding the call to {@link Either#toOptionalRight}.
   *
   * @return a stream from forwarding the call to {@link Either#toOptionalRight}
   */
  default Stream<R> stream() {
    return toOptionalRight().stream();
  }

  /**
   * Reduce to an Optional. If defined, returns the value for the right side of {@link Either} in an
   * {@link Optional#of}, or else returns {@link Optional#empty}.
   *
   * @return an {@link Optional} containing the right side if defined, or else returns {@link Optional#empty}
   */
  Optional<R> toOptionalRight();

  /**
   * Reduce to an Optional. If defined, returns the value for the left side of {@link Either} in an {@link Optional#of},
   * or else returns {@link Optional#empty}.
   *
   * @return an {@link Optional} containing the left side if defined, or else returns {@link Optional#empty}
   */
  Optional<L> toOptionalLeft();

  /**
   * If right is defined, the given map translation function is applied. Forwards call to {@link Either#mapRight}.
   *
   * @param rightFunction given function which is only applied if right is defined
   * @param <T>           target type to which R is translated
   * @return result of the function translation, replacing type R with type T
   */
  default <T> Either<L, T> map(Function<? super R, ? extends T> rightFunction) {
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
  default <T> Either<L, T> flatMap(
      Function<? super R, ? extends Either<L, ? extends T>> rightFunction
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
  default Either<L, R> filterOrElse(
      Predicate<R> retainingRight,
      Supplier<L> leftFunction
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
  default <T> Either<T, R> mapLeft(Function<? super L, ? extends T> leftFunction) {
    if (this instanceof Left<L, R> left) {
      return Either.left(Objects.requireNonNull(leftFunction.apply(left.value())));
    }

    return (Either<T, R>) this;
  }

  /**
   * If right is defined, the given map translation function is applied.
   *
   * @param rightFunction given function which is only applied if right is defined
   * @param <T>           target type to which R is translated
   * @return result of the function translation, replacing type R with type T
   * @throws NullPointerException if rightFunction or the value it returns is {@code null}
   */
  default <T> Either<L, T> mapRight(Function<? super R, ? extends T> rightFunction) {
    if (this instanceof Right<L, R> right) {
      return Either.right(Objects.requireNonNull(rightFunction.apply(right.value())));
    }

    return (Either<L, T>) this;
  }

  /**
   * If left is defined, the given flatMap translation function is applied.
   *
   * @param leftFunction given function which is only applied if left is defined
   * @param <T>          target type to which L is translated
   * @return result of the function translation, replacing type L with type T
   * @throws NullPointerException if leftFunction or the value it returns is {@code null}
   */
  default <T> Either<T, R> flatMapLeft(
      Function<? super L, ? extends Either<? extends T, ? extends R>> leftFunction
  ) {
    if (this instanceof Left<L, R> left) {
      //noinspection unchecked
      return (Either<T, R>) Objects.requireNonNull(leftFunction.apply(left.value()));
    }

    return (Either<T, R>) this;
  }

  /**
   * If right is defined, the given flatMap translation function is applied.
   *
   * @param rightFunction given function which is only applied if right is defined
   * @param <T>           target type to which R is translated
   * @return result of the function translation, replacing type R with type T
   * @throws NullPointerException if rightFunction or the value it returns is {@code null}
   */
  default <T> Either<L, T> flatMapRight(
      Function<? super R, ? extends Either<? extends L, ? extends T>> rightFunction
  ) {
    if (this instanceof Right<L, R> right) {
      //noinspection unchecked
      return (Either<L, T>) Objects.requireNonNull(rightFunction.apply(right.value()));
    }

    return (Either<L, T>) this;
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
   *     {@link Either#isLeft} is {@code true} and {@code retainingLeft} returns {@code true}, or returns the value
   *     returned by {@code rightFunction} within {@link Either#right}
   */
  default Either<L, R> filterOrElseLeft(
      Predicate<L> retainingLeft,
      Supplier<R> rightFunction
  ) {
    if (this instanceof Left<L, R> left) {
      return retainingLeft.test(left.value())
          ? this
          : Either.right(rightFunction.get());
    }
    return this;
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
   *     {@link Either#isRight} is {@code true} and {@code retainingRight} returns {@code true}
   */
  default Either<L, R> filterOrElseRight(
      Predicate<R> retainingRight,
      Supplier<L> leftFunction
  ) {
    if (this instanceof Right<L, R> right) {
      return retainingRight.test(right.value())
          ? this
          : Either.left(leftFunction.get());
    }
    return this;
  }

  /**
   * Returns reversed type such that if this is a {@link Either#left}, then return the {@link Either#left} value in
   * {@link Either#right} or vice versa.
   *
   * @return reversed type such that if this is a {@link Either#left}, then return the {@link Either#left} value in
   *     {@link Either#right} or vice versa
   */
  default Either<R, L> swap() {
    if (this instanceof Left<L, R> left) {
      return Either.right(left.value());
    } else {
      Right<L, R> right = (Right<L, R>) this;
      return Either.left(right.value());
    }
  }

  /**
   * Applies {@code leftFunction} if this is a {@link Left} or {@code rightFunction} if this is a {@link Right}.
   * Explicit functional alias to {@link #converge(Function, Function)}.
   *
   * @param leftFunction  given function which is only applied if left is defined
   * @param rightFunction given function which is only applied if right is defined
   * @param <T>           type of the returned instance
   * @return result of applying the corresponding function
   * @throws NullPointerException if leftFunction, rightFunction, or their result is {@code null}
   */
  default <T> T fold(
      Function<? super L, ? extends T> leftFunction,
      Function<? super R, ? extends T> rightFunction
  ) {
    return converge(leftFunction, rightFunction);
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
  default <T> T converge(
      Function<? super L, ? extends T> leftFunction,
      Function<? super R, ? extends T> rightFunction
  ) {
    if (this instanceof Left<L, R> left) {
      return Objects.requireNonNull(leftFunction.apply(left.value()));
    } else {
      Right<L, R> right = (Right<L, R>) this;
      return Objects.requireNonNull(rightFunction.apply(right.value()));
    }
  }

  /**
   * Converge the distinct types, L and R, to a common type, T. This method's implementation is right-biased.
   *
   * @param <T> type of the returned instance
   * @return an instance of T
   */
  default <T> T converge() {
    return (T) converge(this);
  }

  /**
   * Converge the distinct types, L and R, to a common type, T. This method's implementation is right-biased.
   *
   * @param either the instance of {@link Either} where both L and R share T as a common supertype
   * @param <T>    type of the returned instance
   * @return an instance of T
   */
  static <T> T converge(Either<? extends T, ? extends T> either) {
    if (either instanceof Left<? extends T, ? extends T> left) {
      return left.value();
    } else {
      Right<? extends T, ? extends T> right = (Right<? extends T, ? extends T>) either;
      return right.value();
    }
  }

  /**
   * Returns {@link Either#getRight()} when {@link Either#isRight()} is {@code true}, otherwise throws a
   * {@link RuntimeException}, which, if {@link L} is an instance of a {@link RuntimeException}, then {@link L} is
   * directly thrown, otherwise when {@link L} is a {@link Throwable}, {@link L} is wrapped in the
   * {@link IllegalStateException}, otherwise nothing is wrapped in the thrown {@link IllegalStateException}.
   *
   * @return {@link Either#getRight()} when {@link Either#isRight()} is {@code true}
   * @throws RuntimeException if this is a left instance
   */
  default R getRightOrThrowLeft() {
    if (this instanceof Left<L, R> left) {
      if (left.value() instanceof RuntimeException runtimeException) {
        throw runtimeException;
      } else {
        var message = "getLeft() [%s] must be an instance of RuntimeException".formatted(
            left.value().getClass().getName());
        if (left.value() instanceof Throwable throwable) {
          throw new IllegalStateException(message, throwable);
        } else {
          throw new IllegalStateException(message);
        }
      }
    }

    return this.getRight();
  }

  /**
   * Returns {@link Either#getRight()} when {@link Either#isRight()} is {@code true}, otherwise throws a
   * {@link Throwable}, which, if {@link L} is an instance of a {@link Throwable}, then {@link L} is directly thrown,
   * otherwise nothing is wrapped in the thrown {@link IllegalStateException}.
   *
   * @return {@link Either#getRight()} when {@link Either#isRight()} is {@code true}
   * @throws Throwable if this is a left instance
   */
  default R getRightOrThrowLeftCheckedException() throws Throwable {
    if (this instanceof Left<L, R> left) {
      if (left.value() instanceof Throwable throwable) {
        throw throwable;
      } else {
        throw new IllegalStateException(
            "getLeft() [%s] must be an instance of Throwable".formatted(
                left.value().getClass().getName()));
      }
    }

    return this.getRight();
  }

  /**
   * Execute the given side-effecting function depending upon which side is defined.
   *
   * @param leftAction  given function is only executed if left is defined
   * @param rightAction given function is only executed if right is defined
   */
  default void forEach(
      Consumer<? super L> leftAction,
      Consumer<? super R> rightAction
  ) {
    if (this instanceof Left<L, R> left) {
      leftAction.accept(left.value());
    } else if (this instanceof Right<L, R> right) {
      rightAction.accept(right.value());
    }
  }
}