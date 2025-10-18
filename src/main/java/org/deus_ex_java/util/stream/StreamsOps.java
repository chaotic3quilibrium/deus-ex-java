package org.deus_ex_java.util.stream;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
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
  @NotNull
  public static <T> Stream<T> from(
      @NotNull Iterator<T> iterator
  ) {
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
  @NotNull
  public static <T> Stream<T> from(
      @NotNull Iterator<T> iterator,
      boolean isParallel
  ) {
    Iterable<T> iterable = () -> iterator;

    return from(iterable, isParallel);
  }

  /**
   * Returns a sequential {@link Stream} from a given {@link Iterable}.
   *
   * @param iterable The {@code Iterable} to create the stream from. Must not be {@code null}.
   * @param <T>      The type of elements in the stream.
   * @return A new sequential {@code Stream} containing the elements from the iterable.
   * @throws NullPointerException if the provided {@code iterable} is {@code null}.
   */
  @NotNull
  public static <T> Stream<T> from(
      @NotNull Iterable<T> iterable
  ) {
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
  @NotNull
  public static <T> Stream<T> from(
      @NotNull Iterable<T> iterable,
      boolean isParallel
  ) {
    return StreamSupport.stream(iterable.spliterator(), isParallel);
  }

  /**
   * Returns a new lazy Stream of Entry where each entry is composed of the next element at the same index in both
   * streams, terminating with the shorter of the two Streams.
   *
   * @param streamLs the source of the left side (key) elements
   * @param streamRs the source of the right side (value) elements
   * @param <L>      the type of the left elements in the stream
   * @param <R>      the type of the right elements in the stream
   * @return a new lazy Stream of Entry where each entry is composed of the next element at the same index in both
   *     streams, terminating with the shorter of the two Streams
   */
  @NotNull
  public static <L, R> Stream<Entry<L, R>> zip(
      @NotNull Stream<L> streamLs,
      @NotNull Stream<R> streamRs
  ) {
    Iterator<R> iteratorRs = streamRs.iterator();

    return streamLs
        .filter(l ->
            iteratorRs.hasNext())
        .map(l ->
            entry(l, iteratorRs.next()));
  }

  /**
   * Returns a new lazy Stream of Entry where each entry is composed of the next element, and its associated zero-based
   * index.
   *
   * @param streamTs the source of the elements (keys) with which to associate an zero based index
   * @param <T>      the type of the elements in the stream
   * @return a new lazy Stream of Entry where each entry is composed of the next element, and its associated zero-based
   *     index
   */
  @NotNull
  public static <T> Stream<Entry<T, Integer>> zipWithIndex(
      @NotNull Stream<T> streamTs
  ) {
    var atomicInteger = new AtomicInteger(0);

    return streamTs
        .map(t ->
            entry(t, atomicInteger.getAndIncrement()));
  }
}
