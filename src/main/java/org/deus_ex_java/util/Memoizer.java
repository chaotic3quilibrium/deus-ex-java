package org.deus_ex_java.util;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Source URL: <a href="https://gist.github.com/chaotic3quilibrium/7535c231bb11bd9abc5712c982c46690">
 * https://gist.github.com/chaotic3quilibrium/7535c231bb11bd9abc5712c982c46690</a>
 * <p>
 * -
 * <p>
 * File: org.public_domain.java.utils.Memoizer.java
 * <p>
 * Version: v2023.12.18
 * <p>
 * -
 * <p>
 * A {@link Memoizer} is a thread-safe utility class that caches the resulting value of (expensively?) computing a
 * function taking a single argument.
 * <p>
 * By storing the result (the value) of a call to the defining function for a particular input value (the key), all
 * subsequent calls with the same input value (i.e. key) return the previously computed (cached) value as opposed to
 * recomputing the function. This can significantly improve the performance of computationally expensive functions,
 * especially those that are called repeatedly with the same inputs.
 * <p>
 * -
 * <p>
 * Memoization is particularly useful for a function with the following profile:
 * <p>
 * <ul>
 * <li>Likely to be time-consuming to execute
 * <li>Likely to be repeatedly called with the same inputs
 * <li>Deterministic; i.e. always returns the same output for a given input
 * </ul>
 * <p>
 * -
 * <p>
 * <b>CAUTION:</b> Because this design eliminates multi-thread race conditions, and this
 * implementation allows a compute function to be defined within the {@link #from(Function, MethodOverride, InsertionOrder)}
 * factory method, as well as allowing a compute function to be
 * provided with the {@link #get(K, Function)} method, it is important to note that when a computed
 * result (the value) is associated with the input parameter (the key), the association is made
 * permanent and immutable in a thread-safe manner.
 * <p>
 * -
 * <p>
 * Said another way, the {@link #get(K, Function)} method is unable to "update" an existing
 * association. Once an association is generated, it remains permanent and immutable.
 *
 * @param <K> type of the input value (the key) permanently associated with the computed, and subsequently cached,
 *            value
 * @param <V> type of the cached computed value
 **/
public final class Memoizer<K, V> {

  /**
   * Creates a lazy {@link Supplier<T>} that delays the creating of an instance of {@code t} until the first time it is
   * requested. Upon request, the generated instance of {@code t} is, in a thread-safe way, cached and returned. And
   * then for all future requests, the cached value of {@code t} is returned, as opposed to recomputing it.
   * <p>
   * -
   * <p>
   * <b>CAUTION:</b> If {@link Supplier#get()} throws an {@link Exception}, the exception is
   * captured, suppressed, and the returned instance of {@link Supplier<T>} is defined to short-circuit by having
   * {@link Supplier#get()} hardcoded to return <code>null</code>.
   *
   * @param executeExactlyOnceSupplierT The {@link Supplier#get()} used to create the {@code t} instance exactly once
   * @param <T>                         type of computed function's resulting value instance of {@code t}
   * @return lazy {@link Supplier<T>} that creates the instance of {@code t} upon the first call to
   *     {@link Supplier#get()}.
   */
  @NotNull
  public static <T> Supplier<T> lazyInstantiation(
      @NotNull Supplier<T> executeExactlyOnceSupplierT
  ) {
    Objects.requireNonNull(executeExactlyOnceSupplierT);

    return new Supplier<>() {
      private boolean isInitialized;
      private Supplier<T> supplierT = this::executeExactlyOnce;

      private synchronized T executeExactlyOnce() {
        if (!isInitialized) {
          try {
            var t = executeExactlyOnceSupplierT.get();
            supplierT = () -> t;
          } catch (Exception exception) {
            supplierT = () -> null;
          }
          isInitialized = true;
        }

        return supplierT.get();
      }

      public T get() {
        return supplierT.get();
      }
    };
  }

  public enum InsertionOrder {
    NONE,
    RETAIN
  }

  public enum MethodOverride {
    INHIBITED,
    ALLOWED
  }

  private static final Object vByKWriteLock = new Object();

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private final Optional<Entry<Function<K, V>, MethodOverride>> optionalDefaultDeriveVFromKAndMethodOverride;
  private final InsertionOrder insertionOrder;
  private final Map<K, V> vByK;

  /**
   * Creates a {@link Memoizer} without a default {@code deriveVFromK} function and doesn't maintain insertion order of
   * the keys.
   * <p>
   * -
   * <p>
   * <b>CAUTION:</b> This requires all calls be routed through the {@link #get(K, Function)}.
   *
   * @return an instance of {@link Memoizer} without a default {@code deriveVFromK} function and doesn't maintain
   *     insertion order of the keys
   */
  @NotNull
  public static <K, V> Memoizer<K, V> from() {
    return new Memoizer<>(
        Optional.empty(),
        Memoizer.InsertionOrder.NONE);
  }

  /**
   * Creates a {@link Memoizer} with a specified default {@code deriveVFromK} function, disallows the function to be
   * overridden by the {@link #get(K, Function)} method, and doesn't maintain insertion order of the keys.
   *
   * @param defaultDeriveVFromK function to use to derive {@link V} from {@link K}, if the association is not already
   *                            cached
   * @return an instance of {@link Memoizer} with a specified default {@code deriveVFromK} function, disallows the
   *     function to be overridden by the {@link #get(K, Function)} method, and doesn't maintain insertion order of the
   *     keys
   */
  @NotNull
  public static <K, V> Memoizer<K, V> from(
      @NotNull Function<K, V> defaultDeriveVFromK
  ) {
    return new Memoizer<>(
        Optional.of(
            Map.entry(Objects.requireNonNull(defaultDeriveVFromK), Memoizer.MethodOverride.INHIBITED)),
        Memoizer.InsertionOrder.NONE);
  }

  /**
   * Creates a new {@link Memoizer} with a specified default {@code deriveVFromK} function, optionally allows the
   * function to be overridden by the {@link #get(K, Function)} method, and doesn't maintain insertion order of the
   * keys.
   *
   * @param defaultDeriveVFromK function to use to derive {@link V} from {@link K}, if the association is not already
   *                            cached
   * @param methodOverride      when {@link Memoizer.MethodOverride#ALLOWED}, enables {@link #get(Object, Function)} to
   *                            be called, otherwise the {@link #get(Object, Function)} method will throw an
   *                            {@link UnsupportedOperationException}
   * @return an instance of {@link Memoizer} with a specified default {@code deriveVFromK} function, optionally allows
   *     the function to be overridden by the {@link #get(K, Function)} method, and doesn't maintain insertion order of
   *     the keys
   */
  @NotNull
  public static <K, V> Memoizer<K, V> from(
      @NotNull Function<K, V> defaultDeriveVFromK,
      @NotNull Memoizer.MethodOverride methodOverride
  ) {
    return new org.deus_ex_java.util.Memoizer<>(
        Optional.of(Map.entry(Objects.requireNonNull(defaultDeriveVFromK), methodOverride)),
        Memoizer.InsertionOrder.NONE);
  }

  /**
   * Creates a {@link Memoizer} without a default {@code deriveVFromK} function, and optionally maintain insertion order
   * of the keys.
   * <p>
   * -
   * <p>
   * <b>CAUTION:</b> This requires all calls be routed through the {@link #get(K, Function)}.
   *
   * @param insertionOrder when {@link Memoizer.InsertionOrder#RETAIN}, retains the insertion order of the keys
   * @return an instance of {@link Memoizer} without a default {@code deriveVFromK} function, and optionally maintain
   *     insertion order of the keys
   */
  @NotNull
  public static <K, V> Memoizer<K, V> from(
      @NotNull Memoizer.InsertionOrder insertionOrder
  ) {
    return new org.deus_ex_java.util.Memoizer<>(
        Optional.empty(),
        insertionOrder);
  }

  /**
   * Creates a {@link Memoizer} with a specified default {@code deriveVFromK} function, optionally allowing this default
   * function to be overridden by the {@link #get(K, Function)} method, and optionally maintaining the insertion order
   * of the keys.
   *
   * @param defaultDeriveVFromK function to use to derive V from K, if the association is not already cached
   * @param methodOverride      when {@link Memoizer.MethodOverride#ALLOWED}, enables {@link #get(Object, Function)} to
   *                            be called, otherwise the {@link #get(Object, Function)} method will throw an
   *                            {@link UnsupportedOperationException}
   * @param insertionOrder      when {@link Memoizer.InsertionOrder#RETAIN}, retains the insertion order of the keys
   * @return an instance of {@link Memoizer} with a specified default {@code deriveVFromK} function, optionally allowing
   *     this default function to be overridden by the {@link #get(K, Function)} method, and optionally maintaining the
   *     insertion order of the keys.
   */
  @NotNull
  public static <K, V> Memoizer<K, V> from(
      @NotNull Function<K, V> defaultDeriveVFromK,
      @NotNull Memoizer.MethodOverride methodOverride,
      @NotNull Memoizer.InsertionOrder insertionOrder
  ) {
    return new org.deus_ex_java.util.Memoizer<>(
        Optional.of(Map.entry(Objects.requireNonNull(defaultDeriveVFromK), methodOverride)),
        insertionOrder);
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  private Memoizer(
      @NotNull Optional<Entry<Function<K, V>, Memoizer.MethodOverride>> optionalDefaultDeriveVFromKAndMethodOverride,
      @NotNull Memoizer.InsertionOrder insertionOrder
  ) {
    this.optionalDefaultDeriveVFromKAndMethodOverride = optionalDefaultDeriveVFromKAndMethodOverride;
    this.insertionOrder = insertionOrder;
    this.vByK = insertionOrder == Memoizer.InsertionOrder.RETAIN
        ? new LinkedHashMap<>()
        : new HashMap<>();
  }

  /**
   * Returns whether the {@link #get(K, Function)} method is allowed to provide an {@code overrideDeriveVFromK}
   * function. If {@link Memoizer.MethodOverride#INHIBITED}, the {@link #get(K, Function)} method call will throw an
   * {@link UnsupportedOperationException}.
   *
   * @return if {@code true}, indicates the {@link #get(K, Function)} method is allowed to provide an
   *     {@code overrideDeriveVFromK} function, otherwise the same method call will throw an
   *     {@link UnsupportedOperationException}
   */
  @NotNull
  public Memoizer.MethodOverride getMethodOverride() {
    return this.optionalDefaultDeriveVFromKAndMethodOverride
        .map(Entry::getValue)
        .orElse(Memoizer.MethodOverride.ALLOWED);
  }

  /**
   * Returns whether {@link #keySet()} returns a {@link Set<K>} of keys currently managed, and in temporal (insertion)
   * order, otherwise encounter order is unspecified.
   *
   * @return if {@link Memoizer.InsertionOrder#RETAIN}, indicates {@link #keySet} returns a {@link Set} of the keys
   *     currently managed, and in temporal (insertion) order, otherwise encounter order is unspecified
   */
  @NotNull
  public Memoizer.InsertionOrder getInsertionOrder() {
    return this.insertionOrder;
  }

  @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
  @NotNull
  private V get(
      @NotNull K k,
      @NotNull Optional<Function<K, V>> optionalOverrideDeriveVFromK
  ) {
    var result = this.vByK.get(Objects.requireNonNull(k, "k must not be null"));
    if (result == null) {
      var lambda = optionalOverrideDeriveVFromK
          .or(() -> this.optionalDefaultDeriveVFromKAndMethodOverride.map(
              Entry::getKey))
          .orElseThrow(() -> new IllegalArgumentException(
              "when calling the get() method without providing an overrideDeriveVFromK function, defaultDeriveVFromK must be provided in the from() factory method"));
      var v = Objects.requireNonNull(lambda.apply(k),
          "v returned from the %sDeriveVFromK function, provided by the %s, must not be null".formatted(
              optionalOverrideDeriveVFromK.isEmpty()
                  ? "default"
                  : "override",
              optionalOverrideDeriveVFromK.isEmpty()
                  ? "from() factory method"
                  : "get() method"));
      result = this.vByK.get(k);
      if (result == null) {
        synchronized (vByKWriteLock) {
          result = this.vByK.get(k);
          if (result == null) {
            var oldV = this.vByK.put(k, v);
            result = v;
            if (oldV != null) {
              System.out.println("Memoizer.get() internal - SHOULD NEVER GET HERE");
            }
          }
        }
      }
    }

    return result;
  }

  /**
   * Retrieves the cached value for the specified key, first computing the value using the {@code defaultDeriveVFromK}
   * function provided by the {@link #from(Function, Memoizer.MethodOverride, Memoizer.InsertionOrder)} factory method,
   * if this is the first request for this input value.
   *
   * @param k key to use to retrieve the value
   * @return cached (computed) value for the specified key
   * @throws IllegalArgumentException if no value has been associated with {@code k}
   * @throws NullPointerException     if the value returned by the *DeriveVFromF function is {@code null}
   */
  @NotNull
  public V get(@NotNull K k) {
    return get(k, Optional.empty());
  }

  /**
   * Retrieves the cached value for the specified key, first computing the value using the provided
   * {@code overrideDeriveVFromK} function if this is the first request for this input value.
   * <p>
   * -
   * <p>
   * <b>CAUTION:</b> Because this design eliminates multi-thread race conditions, and this
   * implementation allows a compute function to be defined within the
   * {@link #from(Function, Memoizer.MethodOverride, Memoizer.InsertionOrder)} factory method, as well as allowing a
   * compute function to be provided with this method, it is important to note that when a computed result (the value)
   * is associated with the input parameter (the key), the association is made permanent and immutable in a thread-safe
   * manner.
   * <p>
   * -
   * <p>
   * Said another way, this method is unable to "update" an existing association. Once an association has been
   * generated, it remains permanent and immutable.
   *
   * @param k key to use to retrieve the permanently associated computed value
   * @return cached (computed) value for the specified key
   * @throws UnsupportedOperationException if {@link #getMethodOverride()} method returns
   *                                       {@link Memoizer.MethodOverride#INHIBITED}
   * @throws NullPointerException          if the value returned by the {@code *DeriveVFromF} function is {@code null}
   */
  @NotNull
  public V get(
      @NotNull K k,
      @NotNull Function<K, V> overrideDeriveVFromK
  ) {
    if (this.getMethodOverride() == Memoizer.MethodOverride.INHIBITED) {
      throw new UnsupportedOperationException(
          "overrideDeriveVFromK is restricted from being provided by the from() factory method selected");
    }

    return get(k, Optional.of(overrideDeriveVFromK));
  }

  /**
   * Provides a {@link Set<K>} of keys currently managed, and is in temporal (insertion) order when
   * {@link #getInsertionOrder()} returns {@link Memoizer.InsertionOrder#RETAIN}.
   *
   * @return a {@link Set<K>} of keys currently managed, and is in temporal (insertion) order when
   *     {@link #getInsertionOrder()} returns {@link Memoizer.InsertionOrder#RETAIN}
   */
  @NotNull
  public Set<K> keySet() {
    return Collections.unmodifiableSet(this.vByK.keySet());
  }
}
