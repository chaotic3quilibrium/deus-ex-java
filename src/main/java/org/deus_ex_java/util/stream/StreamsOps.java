package org.deus_ex_java.util.stream;

import org.jspecify.annotations.NullMarked;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Map.entry;

/**
 * Utility class providing static methods to create {@link Stream} instances from various types of <i>iterable</i>
 * sources.
 * <p>
 * And to facilitate easier creation of unmodifiable non-{@code null} {@link List}s, and unmodifiable <i>ordered</i>
 * non-{@code null} {@link Set}s and {@link Map}s.
 */
@NullMarked
public final class StreamsOps {

  private StreamsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns a sequential {@link Stream} from a given {@link Iterator}.
   *
   * @param iterator The {@code Iterator} to create the stream from. Must not be {@code null}.
   * @param <T>      The type of elements in the stream.
   * @return A new sequential {@code Stream} containing the elements from the iterator.
   * @throws NullPointerException if the provided {@code iterator} is {@code null}.
   */
  public static <T> Stream<T> from(
      Iterator<T> iterator
  ) {
    Objects.requireNonNull(iterator, "iterator must not be null");

    return from(iterator, false);
  }

  /**
   * Returns a {@link Stream} from a given {@link Iterator}, allowing for parallel processing.
   *
   * @param iterator   The {@code Iterator} to create the stream from. Must not be {@code null}.
   * @param isParallel If {@code true}, the resulting stream will be parallel; otherwise, it will be sequential.
   * @param <T>        The type of elements in the stream.
   * @return A new {@code Stream} containing the elements from the iterator, with the specified parallelism.
   * @throws NullPointerException if the provided {@code iterator} is {@code null}.
   */
  public static <T> Stream<T> from(
      Iterator<T> iterator,
      boolean isParallel
  ) {
    Objects.requireNonNull(iterator, "iterator must not be null");

    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(
            iterator,
            Spliterator.ORDERED),
        isParallel);
  }

  /**
   * Returns a sequential {@link Stream} from a given {@link Iterable}.
   *
   * @param iterable The {@code Iterable} to create the stream from. Must not be {@code null}.
   * @param <T>      The type of elements in the stream.
   * @return A new sequential {@code Stream} containing the elements from the iterable.
   * @throws NullPointerException if the provided {@code iterable} is {@code null}.
   */
  public static <T> Stream<T> from(
      Iterable<T> iterable
  ) {
    Objects.requireNonNull(iterable, "iterable must not be null");

    return from(iterable, false);
  }

  /**
   * Returns a {@link Stream} from a given {@link Iterable}, allowing for parallel processing.
   *
   * @param iterable   The {@code Iterable} to create the stream from. Must not be {@code null}.
   * @param isParallel If {@code true}, the resulting stream will be parallel; otherwise, it will be sequential.
   * @param <T>        The type of elements in the stream.
   * @return A new {@code Stream} containing the elements from the iterable, with the specified parallelism.
   * @throws NullPointerException if the provided {@code iterable} is {@code null}.
   */
  public static <T> Stream<T> from(
      Iterable<T> iterable,
      boolean isParallel
  ) {
    Objects.requireNonNull(iterable, "iterable must not be null");

    return StreamSupport.stream(iterable.spliterator(), isParallel);
  }

  /**
   * Return a {@link Stream} filtering to only instances of a specific class.
   *
   * @param type what the instance must conform to; i.e. isInstance returns true
   * @param <E>  the type of the elements in the stream.
   * @param <T>  the type of the element to return
   * @return a {@link Stream} filtering to only instances of a specific class
   */
  public static <E, T> Function<E, Stream<T>> filter(Class<T> type) {
    Objects.requireNonNull(type, "type must not be null");

    return e ->
        type.isInstance(e)
            ? Stream.of(type.cast(e))
            : Stream.empty();
  }

  /**
   * Return a {@link Stream} filtering to only instances <em>not</em> of a specific class.
   *
   * @param type what the instance must not conform to; i.e. isInstance returns false
   * @param <E>  the type of the elements in the stream.
   * @return a {@link Stream} filtering to only instances <em>not</em> of a specific class
   */
  public static <E> Function<E, Stream<E>> filterNot(Class<?> type) {
    Objects.requireNonNull(type, "type must not be null");

    return e ->
        type.isInstance(e)
            ? Stream.empty()
            : Stream.of(e);
  }

  /**
   * Returns a new lazy Stream of Entry where each entry is composed of the next element at the same index in both
   * streams, terminating with the shorter of the two Streams.
   *
   * @param collectionLs the source of the left side (key) elements
   * @param collectionRs the source of the right side (value) elements
   * @param <L>          the type of the left elements in the stream
   * @param <R>          the type of the right elements in the stream
   * @return a new lazy Stream of Entry where each entry is composed of the next element at the same index in both
   *     streams, terminating with the shorter of the two Streams
   * @throws NullPointerException if either stream returns a {@code null}
   */
  public static <L, R> Stream<Entry<L, R>> zip(
      Collection<L> collectionLs,
      Collection<R> collectionRs
  ) {
    Objects.requireNonNull(collectionLs, "collectionLs must not be null");
    Objects.requireNonNull(collectionRs, "collectionRs must not be null");

    return zip(collectionLs.stream(), collectionRs.stream());
  }

  /**
   * Returns a new lazy Stream of Entry where each entry is composed of the next element at the same index in both
   * streams, terminating with the shorter of the two Streams.
   *
   * @param streamLs     the source of the left side (key) elements
   * @param collectionRs the source of the right side (value) elements
   * @param <L>          the type of the left elements in the stream
   * @param <R>          the type of the right elements in the stream
   * @return a new lazy Stream of Entry where each entry is composed of the next element at the same index in both
   *     streams, terminating with the shorter of the two Streams
   * @throws NullPointerException if either stream returns a {@code null}
   */
  public static <L, R> Stream<Entry<L, R>> zip(
      Stream<L> streamLs,
      Collection<R> collectionRs
  ) {
    Objects.requireNonNull(streamLs, "streamLs must not be null");
    Objects.requireNonNull(collectionRs, "collectionRs must not be null");

    return zip(streamLs, collectionRs.stream());
  }

  /**
   * Returns a new lazy Stream of Entry where each entry is composed of the next element at the same index in both
   * streams, terminating with the shorter of the two Streams.
   *
   * @param collectionLs the source of the left side (key) elements
   * @param streamRs     the source of the right side (value) elements
   * @param <L>          the type of the left elements in the stream
   * @param <R>          the type of the right elements in the stream
   * @return a new lazy Stream of Entry where each entry is composed of the next element at the same index in both
   *     streams, terminating with the shorter of the two Streams
   * @throws NullPointerException if either stream returns a {@code null}
   */
  public static <L, R> Stream<Entry<L, R>> zip(
      Collection<L> collectionLs,
      Stream<R> streamRs
  ) {
    Objects.requireNonNull(collectionLs, "collectionLs must not be null");
    Objects.requireNonNull(streamRs, "streamRs must not be null");

    return zip(collectionLs.stream(), streamRs);
  }

  /**
   * Returns a new lazy Stream of Entry where each entry is composed of the next element at the same index in both
   * streams, terminating with the shorter of the two Streams.
   * <p>
   * Note: The second parameter invokes {@code streamRs.iterator()}, a terminal operation, which may result in the
   * immediate loading of its elements.
   *
   * @param streamLs the source of the left side (key) elements
   * @param streamRs the source of the right side (value) elements
   * @param <L>      the type of the left elements in the stream
   * @param <R>      the type of the right elements in the stream
   * @return a new lazy Stream of Entry where each entry is composed of the next element at the same index in both
   *     streams, terminating with the shorter of the two Streams
   * @throws NullPointerException if either stream returns a {@code null}
   */
  public static <L, R> Stream<Entry<L, R>> zip(
      Stream<L> streamLs,
      Stream<R> streamRs
  ) {
    Objects.requireNonNull(streamLs, "streamLs must not be null");
    Objects.requireNonNull(streamRs, "streamRs must not be null");

    var iteratorRs = streamRs.iterator();

    return streamLs
        .filter(l ->
            iteratorRs.hasNext())
        .map(l ->
            entry(l, iteratorRs.next()));
  }

  /**
   * Returns a new lazy {@link Stream#sequential()} of Entry where each entry is composed of the next element, and its associated zero-based
   * index.
   *
   * @param collectionTs the source of the elements (keys) with which to associate a zero based index
   * @param <T>          the type of the elements in the stream
   * @return a new lazy {@link Stream#sequential()} of Entry where each entry is composed of the next element, and its associated zero-based
   *     index
   * @throws NullPointerException if the stream returns a {@code null}
   */
  public static <T> Stream<Entry<T, Integer>> zipWithIndex(
      Collection<T> collectionTs
  ) {
    Objects.requireNonNull(collectionTs, "collectionTs must not be null");

    return zipWithIndex(collectionTs.stream());

  }

  /**
   * Returns a new lazy {@link Stream#sequential()} of Entry where each entry is composed of the next element, and its associated zero-based
   * index.
   *
   * @param streamTs the source of the elements (keys) with which to associate a zero based index
   * @param <T>      the type of the elements in the stream
   * @return a new lazy {@link Stream#sequential()} of Entry where each entry is composed of the next element, and its associated zero-based
   *     index
   * @throws NullPointerException if the stream returns a {@code null}
   */
  public static <T> Stream<Entry<T, Integer>> zipWithIndex(
      Stream<T> streamTs
  ) {
    Objects.requireNonNull(streamTs, "streamTs must not be null");
    var atomicInteger = new AtomicInteger(0);

    return streamTs
        .sequential()
        .map(t ->
            entry(t, atomicInteger.getAndIncrement()));
  }
}
