package org.deus_ex_java.util;

import org.jspecify.annotations.NullMarked;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Represents an immutable value of one of three possible domain states (a 3-state disjoint union):
 * <ul>
 *   <li>{@link Present}: Value exists and is valid.</li>
 *   <li>{@link Absent}: Value is validly missing (non-error absence).</li>
 *   <li>{@link Invalid}: Value is structurally or semantically invalid.</li>
 * </ul>
 * <p>
 * As a Java 17+ sealed interface permitting nested records {@link Present}, {@link Absent}, and {@link Invalid},
 * {@link TrinaryValue} supports exhaustive pattern matching and record destructuring in {@code switch} expressions
 * without requiring a {@code default} clause.
 * <p>
 * {@link TrinaryValue} serves as a domain modeling alternative to {@link Optional} and {@link Either} when a
 * computation must distinguish between a successful result, intentional absence, and an explicit processing failure.
 * <p>
 * {@link TrinaryValue} is present-biased, meaning monadic operations such as {@link #map} and {@link #flatMap} operate
 * on the contained value if in the {@link Present} state. If the instance is {@link Absent} or {@link Invalid}, these
 * operations pass through the state unchanged.
 * <p>
 * {@link TrinaryValue} explicitly rejects containing a {@code null} payload for {@link Present} or {@link Invalid}, and
 * will throw a {@link NullPointerException} when instantiated with or passed {@code null}.
 *
 * @param <E> type of the error payload
 * @param <T> type of the present value
 */
@NullMarked
@SuppressWarnings("unchecked")
public sealed interface TrinaryValue<E, T>
    permits TrinaryValue.Present, TrinaryValue.Absent, TrinaryValue.Invalid {

  /**
   * Represents a valid, present value in a {@link TrinaryValue} 3-state union.
   *
   * @param value contained payload, strictly non-null
   * @param <E>   error payload type
   * @param <T>   present value type
   */
  record Present<E, T>(T value) implements TrinaryValue<E, T> {
    public Present {
      Objects.requireNonNull(value, "Present value must not be null");
    }
  }

  /**
   * Represents valid absence (non-error missing state) in a {@link TrinaryValue} 3-state union.
   *
   * @param <E> error payload type
   * @param <T> present value type
   */
  record Absent<E, T>() implements TrinaryValue<E, T> {
    private static final Absent<?, ?> INSTANCE = new Absent<>();

    /**
     * Returns the shared singleton instance of {@link Absent}.
     *
     * @param <E> error payload type
     * @param <T> present value type
     * @return the shared singleton instance of {@link Absent}
     */
    public static <E, T> Absent<E, T> instance() {
      return (Absent<E, T>) INSTANCE;
    }
  }

  /**
   * Represents an invalid or failed state in a {@link TrinaryValue} 3-state union.
   *
   * @param error error payload, strictly non-null
   * @param <E>   error payload type
   * @param <T>   present value type
   */
  record Invalid<E, T>(E error) implements TrinaryValue<E, T> {
    public Invalid {
      Objects.requireNonNull(error, "Error payload must not be null");
    }
  }

  // Factory Methods

  /**
   * Returns an instance of {@link TrinaryValue} in the {@link Present} state containing the specified value.
   *
   * @param value instance of type T to be contained, strictly non-null
   * @param <E>   type of the error payload
   * @param <T>   type of the present value
   * @return an instance of {@link TrinaryValue} in the {@link Present} state containing the specified value
   * @throws NullPointerException if {@code value} is {@code null}
   */
  static <E, T> TrinaryValue<E, T> present(T value) {
    return new Present<>(value);
  }

  /**
   * Returns an instance of {@link TrinaryValue} in the {@link Absent} state representing valid non-error absence.
   *
   * @param <E> type of the error payload
   * @param <T> type of the present value
   * @return an instance of {@link TrinaryValue} in the {@link Absent} state representing valid non-error absence
   */
  static <E, T> TrinaryValue<E, T> absent() {
    return Absent.instance();
  }

  /**
   * Returns an instance of {@link TrinaryValue} in the {@link Invalid} state containing the specified error payload.
   *
   * @param error instance of type E representing the failure payload, strictly non-null
   * @param <E>   type of the error payload
   * @param <T>   type of the present value
   * @return an instance of {@link TrinaryValue} in the {@link Invalid} state containing the specified error payload
   * @throws NullPointerException if {@code error} is {@code null}
   */
  static <E, T> TrinaryValue<E, T> invalid(E error) {
    return new Invalid<>(error);
  }

  // State Queries

  /**
   * Returns true if this {@link TrinaryValue} is in the {@link Present} state.
   *
   * @return true if this {@link TrinaryValue} is in the {@link Present} state
   */
  default boolean isPresent() {
    return this instanceof Present;
  }

  /**
   * Returns true if this {@link TrinaryValue} is in the {@link Absent} state.
   *
   * @return true if this {@link TrinaryValue} is in the {@link Absent} state
   */
  default boolean isAbsent() {
    return this instanceof Absent;
  }

  /**
   * Returns true if this {@link TrinaryValue} is in the {@link Invalid} state.
   *
   * @return true if this {@link TrinaryValue} is in the {@link Invalid} state
   */
  default boolean isInvalid() {
    return this instanceof Invalid;
  }

  // Explicit Extraction

  /**
   * If in the {@link Present} state, returns the contained value, or else throws a {@link NoSuchElementException}.
   *
   * @return if in the {@link Present} state, returns the contained value, or else throws a
   *     {@link NoSuchElementException}
   * @throws NoSuchElementException if this instance is not in the {@link Present} state
   */
  default T get() {
    if (this instanceof Present<E, T> p) {

      return p.value();
    }

    throw new NoSuchElementException("No value present in state: " + this);
  }

  /**
   * If in the {@link Invalid} state, returns the contained error payload, or else throws a
   * {@link NoSuchElementException}.
   *
   * @return if in the {@link Invalid} state, returns the contained error payload, or else throws a
   *     {@link NoSuchElementException}
   * @throws NoSuchElementException if this instance is not in the {@link Invalid} state
   */
  default E getError() {
    if (this instanceof Invalid<E, T> i) {

      return i.error();
    }

    throw new NoSuchElementException("No error present in state: " + this);
  }

  // State Transitions & Escalation

  /**
   * Escalates an {@link Absent} state to an {@link Invalid} state using the supplied error payload. If this instance is
   * {@link Present} or {@link Invalid}, returns {@code this} unchanged.
   *
   * @param errorSupplier supplier invoked to produce an error payload if this instance is {@link Absent}
   * @return an {@link Absent} state to an {@link Invalid} state using the supplied error payload. If this instance is
   *     {@link Present} or {@link Invalid}, returns {@code this} unchanged
   * @throws NullPointerException if {@code errorSupplier} or the error payload it produces is {@code null}
   */
  default TrinaryValue<E, T> required(Supplier<? extends E> errorSupplier) {
    if (this instanceof Absent) {

      return invalid(Objects.requireNonNull(errorSupplier.get(), "Error supplier returned null"));
    }

    return this;
  }

  /**
   * Returns {@code this} if in the {@link Present} or {@link Invalid} state, or else evaluates and returns the result
   * of the given fallback supplier if in the {@link Absent} state.
   *
   * @param supplier supplier invoked to produce a fallback {@link TrinaryValue} if this instance is {@link Absent}
   * @return {@code this} if in the {@link Present} or {@link Invalid} state, or else evaluates and returns the result
   *     of the given fallback supplier if in the {@link Absent} state
   * @throws NullPointerException if {@code supplier} or the {@link TrinaryValue} it returns is {@code null}
   */
  default TrinaryValue<E, T> orElseGet(
      Supplier<? extends TrinaryValue<? extends E, ? extends T>> supplier
  ) {
    if (this instanceof Absent) {

      return (TrinaryValue<E, T>) Objects.requireNonNull(
          supplier.get(),
          "Fallback supplier returned null");
    }

    return this;
  }

  /**
   * If in the {@link Present} state and the contained value matches the predicate, returns {@code this}. If in the
   * {@link Present} state and the predicate evaluates to {@code false}, returns an {@link Invalid} state containing the
   * error produced by {@code errorSupplier}. Returns {@code this} unchanged if in the {@link Absent} or {@link Invalid}
   * state.
   *
   * @param predicate     predicate applied to the value if in the {@link Present} state
   * @param errorSupplier supplier invoked to produce an error payload if the predicate evaluates to {@code false}
   * @return if in the {@link Present} state and the contained value matches the predicate, returns {@code this}. If in
   *     the {@link Present} state and the predicate evaluates to {@code false}, returns an {@link Invalid} state
   *     containing the error produced by {@code errorSupplier}. Returns {@code this} unchanged if in the {@link Absent}
   *     or {@link Invalid} state
   * @throws NullPointerException if {@code predicate}, {@code errorSupplier}, or the error payload produced is
   *                              {@code null}
   */
  default TrinaryValue<E, T> filter(
      Predicate<? super T> predicate,
      Supplier<? extends E> errorSupplier
  ) {
    Objects.requireNonNull(predicate, "Filter predicate must not be null");
    Objects.requireNonNull(errorSupplier, "Error supplier must not be null");
    if (this instanceof Present<E, T> p) {

      return predicate.test(p.value())
          ? this
          : invalid(Objects.requireNonNull(errorSupplier.get(), "Error supplier returned null"));
    }

    return this;
  }

  // Monadic Operations

  /**
   * If in the {@link Present} state, applies the given mapper function to the contained value and returns a new
   * {@link TrinaryValue} containing the result. Returns {@code this} cast to target type if in the {@link Absent} or
   * {@link Invalid} state.
   *
   * @param mapper mapper function applied to the value if in the {@link Present} state
   * @param <U>    target type to which T is translated
   * @return f in the {@link Present} state, applies the given mapper function to the contained value and returns a new
   *     {@link TrinaryValue} containing the result. Returns {@code this} cast to target type if in the {@link Absent}
   *     or {@link Invalid} state
   * @throws NullPointerException if {@code mapper} or the value it returns is {@code null}
   */
  default <U> TrinaryValue<E, U> map(Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper, "Map function must not be null");
    if (this instanceof Present<E, T> p) {

      return present(Objects.requireNonNull(mapper.apply(p.value()), "Map function returned null"));
    }

    return (TrinaryValue<E, U>) this;
  }

  /**
   * If in the {@link Present} state, applies the given monadic mapper function to the contained value and returns the
   * result. Returns {@code this} cast to target type if in the {@link Absent} or {@link Invalid} state.
   *
   * @param mapper mapper function applied to the value if in the {@link Present} state
   * @param <U>    target type contained in the resulting {@link TrinaryValue}
   * @return if in the {@link Present} state, applies the given monadic mapper function to the contained value and
   *     returns the result. Returns {@code this} cast to target type if in the {@link Absent} or {@link Invalid} state
   * @throws NullPointerException if {@code mapper} or the {@link TrinaryValue} it returns is {@code null}
   */
  default <U> TrinaryValue<E, U> flatMap(
      Function<? super T, ? extends TrinaryValue<? extends E, ? extends U>> mapper
  ) {
    Objects.requireNonNull(mapper, "FlatMap function must not be null");
    if (this instanceof Present<E, T> p) {

      return (TrinaryValue<E, U>) Objects.requireNonNull(
          mapper.apply(p.value()), "FlatMap function returned null");
    }

    return (TrinaryValue<E, U>) this;
  }

  /**
   * If in the {@link Invalid} state, applies the given error mapper function to the contained error payload and returns
   * a new {@link TrinaryValue} in the {@link Invalid} state. Returns {@code this} cast to target type if in the
   * {@link Present} or {@link Absent} state.
   *
   * @param errorMapper function applied to the error payload if in the {@link Invalid} state
   * @param <F>         target error type to which E is translated
   * @return if in the {@link Invalid} state, applies the given error mapper function to the contained error payload and
   *     returns a new {@link TrinaryValue} in the {@link Invalid} state. Returns {@code this} cast to target type if in
   *     the {@link Present} or {@link Absent} state
   * @throws NullPointerException if {@code errorMapper} or the error payload it returns is {@code null}
   */
  default <F> TrinaryValue<F, T> mapError(Function<? super E, ? extends F> errorMapper) {
    Objects.requireNonNull(errorMapper, "Error mapper function must not be null");
    if (this instanceof Invalid<E, T> i) {

      return invalid(Objects.requireNonNull(
          errorMapper.apply(i.error()),
          "Error mapper function returned null"));
    }
    return (TrinaryValue<F, T>) this;
  }

  /**
   * Applies the corresponding mapper or supplier based on the domain state of this instance to produce a unified
   * result.
   *
   * @param presentMapper  function applied if this instance is {@link Present}
   * @param absentSupplier supplier invoked if this instance is {@link Absent}
   * @param invalidMapper  function applied if this instance is {@link Invalid}
   * @param <R>            target return type
   * @return applies the corresponding mapper or supplier based on the domain state of this instance to produce a
   *     unified result
   * @throws NullPointerException if any parameter, or any value returned by a mapper/supplier, is {@code null}
   */
  default <R> R fold(
      Function<? super T, ? extends R> presentMapper,
      Supplier<? extends R> absentSupplier,
      Function<? super E, ? extends R> invalidMapper
  ) {
    Objects.requireNonNull(presentMapper, "Present mapper must not be null");
    Objects.requireNonNull(absentSupplier, "Absent supplier must not be null");
    Objects.requireNonNull(invalidMapper, "Invalid mapper must not be null");

    if (this instanceof Present<E, T> p) {

      return Objects.requireNonNull(presentMapper.apply(p.value()), "Present mapper returned null");
    }
    if (this instanceof Absent) {

      return Objects.requireNonNull(absentSupplier.get(), "Absent supplier returned null");
    }
    Invalid<E, T> inv = (Invalid<E, T>) this;

    return Objects.requireNonNull(invalidMapper.apply(inv.error()), "Invalid mapper returned null");
  }

  /**
   * Reduces this instance to an {@link Either}. If {@link Present}, returns {@link Either#right} containing the value.
   * If {@link Absent}, evaluates {@code absentToErrorSupplier} and returns {@link Either#left} containing the error
   * payload. If {@link Invalid}, returns {@link Either#left} containing the error payload.
   *
   * @param absentToErrorSupplier supplier invoked to produce an error payload if this instance is {@link Absent}
   * @return educes this instance to an {@link Either}. If {@link Present}, returns {@link Either#right} containing the
   *     value. If {@link Absent}, evaluates {@code absentToErrorSupplier} and returns {@link Either#left} containing
   *     the error payload. If {@link Invalid}, returns {@link Either#left} containing the error payload
   * @throws NullPointerException if {@code absentToErrorSupplier} or the error payload it returns is {@code null}
   */
  default Either<E, T> toEither(Supplier<? extends E> absentToErrorSupplier) {
    Objects.requireNonNull(absentToErrorSupplier, "Absent-to-error supplier must not be null");
    if (this instanceof Present<E, T> p) {

      return Either.right(p.value());
    }
    if (this instanceof Absent) {

      return Either.left(Objects.requireNonNull(
          absentToErrorSupplier.get(), "Absent-to-error supplier returned null"));
    }
    Invalid<E, T> inv = (Invalid<E, T>) this;

    return Either.left(inv.error());
  }

  /**
   * Reduces this instance to an {@link Optional}. If {@link Present}, returns {@link Optional#of(Object)}. If
   * {@link Absent}, returns {@link Optional#empty()}. If {@link Invalid}, throws an {@link IllegalStateException}.
   *
   * @return educes this instance to an {@link Optional}. If {@link Present}, returns {@link Optional#of(Object)}. If
   *     {@link Absent}, returns {@link Optional#empty()}. If {@link Invalid}, throws an {@link IllegalStateException}
   * @throws IllegalStateException if this instance is in an {@link Invalid} state
   */
  default Optional<T> toOptional() {
    if (this instanceof Present<E, T> p) {

      return Optional.of(p.value());
    }
    if (this instanceof Absent) {

      return Optional.empty();
    }
    Invalid<E, T> inv = (Invalid<E, T>) this;

    throw new IllegalStateException(
        "Cannot convert Invalid state to Optional. Error context: " + inv.error());
  }

  /**
   * Returns a sequential {@link Stream} containing the present value if in the {@link Present} state, an empty stream
   * if in the {@link Absent} state, or throws an {@link IllegalStateException} if in the {@link Invalid} state by
   * forwarding to {@link #toOptional()}.
   *
   * @return a sequential {@link Stream} containing the present value if in the {@link Present} state, an empty stream
   *     if in the {@link Absent} state, or throws an {@link IllegalStateException} if in the {@link Invalid} state by
   *     forwarding to {@link #toOptional()}
   * @throws IllegalStateException if this instance is in an {@link Invalid} state
   */
  default Stream<T> stream() {
    return toOptional().stream();
  }
}
