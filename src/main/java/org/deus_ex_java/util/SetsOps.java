package org.deus_ex_java.util;

import org.deus_ex_java.util.refined.NonEmptySet;
import org.deus_ex_java.util.tuple.Tuple2;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class providing static methods to create {@link Set} instances.
 */
@NullMarked
public final class SetsOps {

  private SetsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns a {@link Set}{@code <T>} backed by an empty and modifiable {@link HashSet}{@code <T>}.
   * <p>
   * This enables specifying a function in the shape of {@code SetsOps::HashSet} which returns the
   * <em>interface</em> type of {@link Set}{@code <T>}, which is preferable over specifying {@code HashSet::new}
   * which returns the <em>class specific implementation</em> type of {@link HashSet}{@code <T>}.
   *
   * @param <T> the type of instances contained in the {@link Set}
   * @return a {@link Set}{@code <T>} backed by an empty and modifiable {@link HashSet}{@code <T>}
   */
  public static <T> Set<T> newHashSet() {
    //noinspection Convert2Diamond
    return new HashSet<T>();
  }

  /**
   * Returns a {@link Set}{@code <T>} backed by an empty and modifiable {@link HashSet}{@code <T>}.
   * <p>
   * This enables specifying a function in the shape of {@code () -> SetsOps.newHashSet(T.class)} which returns the
   * <em>interface</em> type of {@link Set}{@code <T>}, which is preferable over specifying {@code HashSet::new} which
   * returns the <em>class specific implementation</em> type of {@link HashSet}{@code <T>}.
   *
   * @param clazz the class of the type
   * @param <T>   the type of instances contained in the {@link Set}
   * @return a {@link Set}{@code <T>} backed by an empty and modifiable {@link LinkedHashSet}{@code <T>}
   */
  public static <T> Set<T> newHashSet(Class<T> clazz) {
    Objects.requireNonNull(clazz);

    return Collections.checkedSet(new HashSet<>(), clazz);
  }

  /**
   * Returns a {@link Set}{@code <T>} backed by an empty and modifiable {@link LinkedHashSet}{@code <T>}.
   * <p>
   * This enables specifying a function in the shape of {@code SetsOps::newLinkedHashSet} which returns the
   * <em>interface</em> type of {@link Set}{@code <T>}, which is preferable over specifying {@code LinkedHashSet::new}
   * which returns the <em>class specific implementation</em> type of {@link LinkedHashSet}{@code <T>}.
   *
   * @param <T> the type of instances contained in the {@link Set}
   * @return a {@link Set}{@code <T>} backed by an empty and modifiable {@link LinkedHashSet}{@code <T>}
   */
  public static <T> Set<T> newLinkedHashSet() {
    //noinspection Convert2Diamond
    return new LinkedHashSet<T>();
  }

  /**
   * Returns a {@link Set}{@code <T>} backed by an empty and modifiable {@link LinkedHashSet}{@code <T>}.
   * <p>
   * This enables specifying a function in the shape of {@code () -> SetsOps.newLinkedHashSet(T.class)} which returns
   * the <em>interface</em> type of {@link Set}{@code <T>}, which is preferable over specifying
   * {@code LinkedHashSet::new} which returns the <em>class specific implementation</em> type of
   * {@link LinkedHashSet}{@code <T>}.
   *
   * @param clazz the class of the type
   * @param <T>   the type of instances contained in the {@link Set}
   * @return a {@link Set}{@code <T>} backed by an empty and modifiable {@link LinkedHashSet}{@code <T>}
   */
  public static <T> Set<T> newLinkedHashSet(Class<T> clazz) {
    Objects.requireNonNull(clazz);

    return Collections.checkedSet(new LinkedHashSet<>(), clazz);
  }

  /**
   * Returns an empty {@link Set} using {@link Set#of}, if {@code ts} is {@code null}, otherwise returns {@code ts}.
   * <p>
   * Note: If the requirement is to both avoid a {@code null} value, and an empty {@link Set}, the refined class of
   * {@link NonEmptySet} enable <i>compile-time enforcement</i> of said contract requirements.
   *
   * @param ts  possibly {@code null} {@link Set} to reify to make {@code null} safe
   * @param <T> the type of instances contained in the source
   * @return an empty {@link Set} using {@link Set#of}, if {@code ts} is {@code null}, otherwise returns {@code ts}
   */
  public static <T> Set<T> nullToEmpty(@Nullable Set<T> ts) {
    return ts != null
        ? ts
        : Set.of();
  }

  /**
   * Returns an occupied {@link Optional} containing a {@link NonEmptySet} if {@code ts.isEmpty} is {@code false},
   * otherwise an {@link Optional#empty()}.
   *
   * @param ts  the possibly {@code null} or empty source to wrap
   * @param <T> the type of instances contained in the source
   * @return an occupied {@link Optional} containing a {@link NonEmptySet} if {@code ts.isEmpty} is {@code false},
   *     otherwise an {@link Optional#empty()}
   */
  public static <T> Optional<NonEmptySet<T>> toNonEmpty(@Nullable Set<T> ts) {
    return (ts != null)
        ? NonEmptySet.wrap(ts).toOptional()
        : Optional.empty();
  }

  /**
   * Returns an unmodifiable unordered {@link Set} with either the {@code value} added if it is non-null, or an
   * unmodifiable unordered copy of the original {@code set}.
   *
   * @param set   the source from which the unordered copy is made
   * @param value the value to add to the copy of the list
   * @param <T>   the type of instances contained in the set
   * @return an unmodifiable unordered {@link Set} with either the {@code value} added if it is non-null, or an
   *     unmodifiable copy of the original {@code set}
   */
  public static <T> Set<T> addItem(
      Set<T> set,
      @Nullable T value
  ) {
    Objects.requireNonNull(set);
    if (value == null) {

      return set.isEmpty()
          ? Set.of()
          : Set.copyOf(set);
    }

    if (!set.isEmpty()) {
      var result = new HashSet<>(set);
      result.add(value);

      return Collections.unmodifiableSet(result);
    }

    return Set.of(value);
  }

  private static final Set<?> UNMODIFIABLE_LINKED_HASH_SET_EMPTY = Collections.unmodifiableSet(new LinkedHashSet<>());

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Set} with either the {@code value} appended if it is non-null,
   * or an unmodifiable <u><i>ordered</i></u> copy of the original {@code set}.
   *
   * @param set   the (assumed to be) <u><i>ordered</i></u> source from which the copy is made
   * @param value the value to add to the copy of the set
   * @param <T>   the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@link Set} with either the {@code value} appended if it is non-null,
   *     or an unmodifiable <u><i>ordered</i></u> copy of the original {@code set}
   */
  public static <T> Set<T> appendItem(
      Set<T> set,
      @Nullable T value
  ) {
    Objects.requireNonNull(set);
    if (set.isEmpty() && (value == null)) {

      //noinspection unchecked
      return (Set<T>) UNMODIFIABLE_LINKED_HASH_SET_EMPTY;
    }
    var result = set.isEmpty()
        ? new LinkedHashSet<T>()
        : new LinkedHashSet<>(set);
    if (value != null) {
      result.add(value);
    }

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable unordered {@link Set} consisting of each {@link Set} (filtered to non-null) from
   * {@code sets} combined.
   *
   * @param sets the sets to combine
   * @param <T>  the type of instances contained in all the sets
   * @return an unmodifiable unordered {@link Set} consisting of each {@link Set} (filtered to non-null) from
   *     {@code sets} combined
   */
  @SuppressWarnings("ConstantValue")
  @SafeVarargs
  public static <T> Set<T> addSets(Set<T>... sets) {
    Objects.requireNonNull(sets);
    if (sets.length == 0) {

      return Set.of();
    }

    return Arrays.stream(sets)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Set} consisting of each {@link Set} (filtered to non-null)
   * {@code sets} appended together.
   *
   * @param sets the (assumed to be) <u><i>ordered</i></u> sets to append
   * @param <T>  the type of instances contained in all the sets
   * @return an unmodifiable <u><i>ordered</i></u> {@link Set} consisting of each {@link Set} (filtered to non-null)
   *     {@code sets} appended together
   */
  @SuppressWarnings("ConstantValue")
  @SafeVarargs
  public static <T> Set<T> appendSets(
      Set<T>... sets
  ) {
    Objects.requireNonNull(sets);
    if (sets.length == 0) {

      //noinspection unchecked
      return (Set<T>) UNMODIFIABLE_LINKED_HASH_SET_EMPTY;
    }
    var result = Arrays.stream(sets)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .collect(Collectors.toCollection(LinkedHashSet::new));

    //noinspection unchecked
    return result.isEmpty()
        ? (Set<T>) UNMODIFIABLE_LINKED_HASH_SET_EMPTY
        : Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable unordered {@link Set} with the {@code value} removed if it is non-null, or an unmodifiable
   * unordered copy of the original {@code set}.
   *
   * @param set   the source from which the unordered copy is made
   * @param value the value to remove from the copy of the set
   * @param <T>   the type of instances contained in the set
   * @return an unmodifiable unordered {@link Set} with the {@code value} removed if it is non-null, or an unmodifiable
   *     copy of the original {@code set}
   */
  public static <T> Set<T> removeItem(
      Set<T> set,
      @Nullable T value
  ) {
    Objects.requireNonNull(set);
    if (set.isEmpty()) {

      return Set.of();
    }
    if (value == null) {

      return Set.copyOf(set);
    }
    var result = new HashSet<>(set);
    result.remove(value);

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Set} with the {@code value} removed if it is non-null, or an
   * unmodifiable <u><i>ordered</i></u> copy of the original {@code set}.
   *
   * @param set   the (assumed to be) <u><i>ordered</i></u> source from which the copy is made
   * @param value the value to remove from the copy of the set
   * @param <T>   the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@link Set} with the {@code value} removed if it is non-null, or an
   *     unmodifiable <u><i>ordered</i></u> copy of the original {@code set}
   */
  public static <T> Set<T> removeItemOrdered(
      Set<T> set,
      @Nullable T value
  ) {
    Objects.requireNonNull(set);
    if (set.isEmpty()) {

      //noinspection unchecked
      return (Set<T>) UNMODIFIABLE_LINKED_HASH_SET_EMPTY;
    }
    var result = new LinkedHashSet<>(set);
    if (value != null) {
      result.remove(value);
    }

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable unordered {@link Set} consisting of the elements from the original {@code set} with all
   * elements contained within the {@code collection} removed.
   *
   * @param set        the source from which the unordered copy is made
   * @param collection the collection containing the elements to remove from the copy of the set
   * @param <T>        the type of instances contained in the set and collection
   * @return an unmodifiable unordered {@link Set} consisting of the elements from the original {@code set} with all
   *     elements contained within the {@code collection} removed
   */
  public static <T> Set<T> removeAll(
      Set<T> set,
      Collection<T> collection
  ) {
    Objects.requireNonNull(set);
    Objects.requireNonNull(collection);

    return removeAll(
        set,
        collection.stream());
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Set} consisting of the elements from the original {@code set}
   * with all elements contained within the {@code collection} removed.
   *
   * @param set        the (assumed to be) <u><i>ordered</i></u> source from which the copy is made
   * @param collection the collection containing the elements to remove from the copy of the set
   * @param <T>        the type of instances contained in the set and collection
   * @return an unmodifiable <u><i>ordered</i></u> {@link Set} consisting of the elements from the original {@code set}
   *     with all elements contained within the {@code collection} removed
   */
  public static <T> Set<T> removeAllOrdered(
      Set<T> set,
      Collection<T> collection
  ) {
    Objects.requireNonNull(set);
    Objects.requireNonNull(collection);

    return removeAllOrdered(
        set,
        collection.stream());
  }

  private static <T> Set<T> helperRemoveAllStream(
      Set<T> set,
      Stream<T> stream,
      Set<T> setEmpty,
      Function<Set<T>, Set<T>> fSetConstructor
  ) {
    Objects.requireNonNull(set);
    Objects.requireNonNull(stream);
    if (set.isEmpty()) {

      return setEmpty;
    }
    var removalsAsSet = stream
        .collect(Collectors.toUnmodifiableSet());
    var result = fSetConstructor.apply(set);
    if (!removalsAsSet.isEmpty()) {
      result.removeAll(removalsAsSet);
    }

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable unordered {@link Set} consisting of the elements from the original {@code set} with all
   * elements contained within the {@code stream} removed.
   *
   * @param set    the source from which the unordered copy is made
   * @param stream the stream containing the elements to remove from the copy of the set
   * @param <T>    the type of instances contained in the set and stream
   * @return an unmodifiable unordered {@link Set} consisting of the elements from the original {@code set} with all
   *     elements contained within the {@code stream} removed
   */
  public static <T> Set<T> removeAll(
      Set<T> set,
      Stream<T> stream
  ) {
    return helperRemoveAllStream(
        set,
        stream,
        Set.of(),
        HashSet::new);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Set} consisting of the elements from the original {@code set}
   * with all elements contained within the {@code stream} removed.
   *
   * @param set    the (assumed to be) <u><i>ordered</i></u> source from which the copy is made
   * @param stream the stream containing the elements to remove from the copy of the set
   * @param <T>    the type of instances contained in the set and stream
   * @return an unmodifiable <u><i>ordered</i></u> {@link Set} consisting of the elements from the original {@code set}
   *     with all elements contained within the {@code stream} removed
   */
  public static <T> Set<T> removeAllOrdered(
      Set<T> set,
      Stream<T> stream
  ) {
    //noinspection unchecked
    return helperRemoveAllStream(
        set,
        stream,
        (Set<T>) UNMODIFIABLE_LINKED_HASH_SET_EMPTY,
        LinkedHashSet::new);
  }

  @SafeVarargs
  private static <T> Set<T> helperRemoveSets(
      Set<T> set,
      Set<T> setEmpty,
      Function<Set<T>, Set<T>> fSetConstructor,
      Set<T>... sets
  ) {
    Objects.requireNonNull(set);
    Objects.requireNonNull(sets);

    return TernaryOps.get(
        set.isEmpty(),
        () ->
            setEmpty,
        () -> {
          var result = fSetConstructor.apply(set);
          if (sets.length != 0) {
            @SuppressWarnings("ConstantValue")
            var removals = Arrays.stream(sets)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
            if (!removals.isEmpty()) {
              result.removeAll(removals);
            }

            return result.isEmpty()
                ? setEmpty
                : Collections.unmodifiableSet(result);
          }

          return Collections.unmodifiableSet(result);
        });
  }

  /**
   * Returns an unmodifiable unordered {@link Set} consisting of the elements from the original {@code set} with all
   * elements contained within the {@code sets} removed.
   *
   * @param set  the source from which the unordered copy is made
   * @param sets the sets containing the elements to remove from the copy of the set
   * @param <T>  the type of instances contained in the set and sets
   * @return an unmodifiable unordered {@link Set} consisting of the elements from the original {@code set} with all
   *     elements contained within the {@code sets} removed
   */
  @SafeVarargs
  public static <T> Set<T> removeSets(
      Set<T> set,
      Set<T>... sets
  ) {
    return helperRemoveSets(
        set,
        Set.of(),
        HashSet::new,
        sets);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Set} consisting of the elements from the original {@code set}
   * with all elements contained within the {@code sets} removed.
   *
   * @param set  the (assumed to be) <u><i>ordered</i></u> source from which the copy is made
   * @param sets the sets containing the elements to remove from the copy of the set
   * @param <T>  the type of instances contained in the set and sets
   * @return an unmodifiable <u><i>ordered</i></u> {@link Set} consisting of the elements from the original {@code set}
   *     with all elements contained within the {@code sets} removed
   */
  @SafeVarargs
  public static <T> Set<T> removeSetsOrdered(
      Set<T> set,
      Set<T>... sets
  ) {
    //noinspection unchecked
    return helperRemoveSets(
        set,
        (Set<T>) UNMODIFIABLE_LINKED_HASH_SET_EMPTY,
        LinkedHashSet::new,
        sets);
  }

  /**
   * Returns an unmodifiable unordered {@link Set} filtered of {@code null}s.
   *
   * @param collection the source of the T elements
   * @param <T>        the type of the instances
   * @return an unmodifiable unordered {@link Set} filtered of {@code null}s
   */
  public static <T> Set<T> nullSanitize(
      Collection<@Nullable T> collection
  ) {
    return nullSanitize(collection.stream());
  }

  /**
   * Returns an unmodifiable unordered {@link Set} filtered of {@code null}s.
   *
   * @param stream the source of the T elements
   * @param <T>    the type of the instances
   * @return an unmodifiable unordered {@link Set} filtered of {@code null}s
   */
  public static <T> Set<T> nullSanitize(
      Stream<@Nullable T> stream
  ) {
    return stream
        .filter(Objects::nonNull)
        .collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Set} filtered of {@code null}s.
   *
   * @param collection the source of the T elements
   * @param <T>        the type of the instances
   * @return an unmodifiable <u><i>ordered</i></u> {@link Set} filtered of {@code null}s.
   */
  public static <T> Set<T> toSetOrdered(
      Collection<@Nullable T> collection
  ) {
    return toSetOrdered(collection.stream());
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Set} filtered of {@code null}s.
   *
   * @param stream the (assumed to be) <u><i>ordered</i></u> source of the T elements
   * @param <T>    the type of the instances
   * @return an unmodifiable <u><i>ordered</i></u> {@link Set} filtered of {@code null}s
   */
  public static <T> Set<T> toSetOrdered(
      Stream<@Nullable T> stream
  ) {
    @SuppressWarnings("RedundantCast")
    var set = stream
        .filter(Objects::nonNull)
        .map(t ->
            (T) t)
        .collect(Collectors.toCollection(LinkedHashSet::new));

    return !set.isEmpty()
        ? Collections.unmodifiableSet(set)
        : Set.of();
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Set} of the source's elements in reverse order filtered of
   * {@code null}s.
   *
   * @param ts  the (assumed to be) <u><i>ordered</i></u> source of the T elements
   * @param <T> the type of instances contained in the source
   * @return an unmodifiable <u><i>ordered</i></u> {@link Set} of the source's elements in reverse order filtered of
   *     {@code null}s
   */
  public static <T> Set<T> reverse(
      Set<@Nullable T> ts
  ) {
    if (!ts.isEmpty()) {

      return reverse(ts.stream());
    }

    return Set.of();
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Set} of the source's elements in reverse order filtered of
   * {@code null}s.
   *
   * @param stream the (assumed to be) <u><i>ordered</i></u> source of the T elements
   * @param <T>    the type of instances contained in the source
   * @return an unmodifiable <u><i>ordered</i></u> {@link Set} of the source's elements in reverse order filtered of
   *     {@code null}s
   */
  public static <T> Set<T> reverse(
      Stream<@Nullable T> stream
  ) {
    var mutableList = stream
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    if (!mutableList.isEmpty()) {
      Collections.reverse(mutableList);

      return toSetOrdered(mutableList.stream());
    }

    return Set.of();
  }

  /**
   * Returns {@code true} if any of the elements within {@code leftTs} are contained within {@code rightTs}, otherwise
   * {@code false}.
   *
   * @param leftTs  the first source of the T elements
   * @param rightTs the second source of the T elements
   * @param <T>     the type of instances contained in the source
   * @return {@code true} if any of the elements within {@code leftTs} are contained within {@code rightTs}, otherwise
   *     {@code false}
   */
  public static <T> boolean containsAny(
      Set<T> leftTs,
      Set<T> rightTs
  ) {
    return (leftTs.size() < rightTs.size())
        ? leftTs.stream().anyMatch(rightTs::contains)
        : rightTs.stream().anyMatch(leftTs::contains);
  }

  /**
   * Returns a {@link Tuple2} assigning the first property an unmodifiable unordered {@link Set} containing all the
   * distinct elements from the source, and assigning to the second property an unmodifiable unordered {@link Set}
   * containing all the elements which appeared more than once from the source.
   *
   * @param collection the source of the T elements
   * @param <T>        the type of instances contained in the source
   * @return a {@link Tuple2} assigning the first property an unmodifiable unordered {@link Set} containing all the
   *     distinct elements from the source, and assigning to the second property an unmodifiable unordered {@link Set}
   *     containing all the elements which appeared more than once from the source
   */
  public static <T> Tuple2<Set<T>, Set<T>> toDistinctAndDupes(
      Collection<T> collection
  ) {
    if (!collection.isEmpty()) {
      return toDistinctAndDupes(collection.stream());
    }

    return new Tuple2<>(Set.of(), Set.of());
  }

  @SuppressWarnings("SpellCheckingInspection")
  private record ToDistinctAndDupesState<T>(
      Set<T> distincts,
      Set<T> dupes
  ) {

  }

  /**
   * Returns a {@link Tuple2} assigning the first property an unmodifiable unordered {@link Set} containing all the
   * distinct elements from the source, and assigning to the second property an unmodifiable unordered {@link Set}
   * containing all the elements which appeared more than once from the source.
   *
   * @param stream the source of the T elements
   * @param <T>    the type of instances contained in the source
   * @return a {@link Tuple2} assigning the first property an unmodifiable unordered {@link Set} containing all the
   *     distinct elements from the source, and assigning to the second property an unmodifiable unordered {@link Set}
   *     containing all the elements which appeared more than once from the source
   */
  public static <T> Tuple2<Set<T>, Set<T>> toDistinctAndDupes(
      Stream<T> stream
  ) {
    var sequentialStream = stream.isParallel()
        ? stream.sequential()
        : stream;
    var toDistinctAndDupesState = sequentialStream
        .collect(
            () ->
                new ToDistinctAndDupesState<T>(
                    new HashSet<>(),
                    new HashSet<>()),
            (toDistinctAndDupesStateInterim, t) -> {
              if (!toDistinctAndDupesStateInterim.distincts.add(t)) {
                toDistinctAndDupesStateInterim.dupes.add(t);
              }
            },
            (toDistinctAndDupesStateInterim1, toDistinctAndDupesStateInterim2) -> {
              throw new IllegalStateException("should never get here - combiner was called on sequential stream");
            }
        );

    return new Tuple2<>(
        Collections.unmodifiableSet(toDistinctAndDupesState.distincts()),
        Collections.unmodifiableSet(toDistinctAndDupesState.dupes()));
  }

  /**
   * Returns a {@link Tuple2} assigning the first property an unmodifiable <em>ordered</em> {@link Set} containing all
   * the distinct elements from the source, and assigning to the second property an unmodifiable <em>ordered</em>
   * {@link Set} containing all the elements which appeared more than once from the source.
   *
   * @param collection the source of the T elements
   * @param <T>        the type of instances contained in the source
   * @return a {@link Tuple2} assigning the first property an unmodifiable <em>ordered</em> {@link Set} containing all
   *     the distinct elements from the source, and assigning to the second property an unmodifiable <em>ordered</em>
   *     {@link Set} containing all the elements which appeared more than once from the source
   */
  public static <T> Tuple2<Set<T>, Set<T>> toDistinctAndDupesOrdered(
      Collection<T> collection
  ) {
    if (!collection.isEmpty()) {
      return toDistinctAndDupesOrdered(collection.stream());
    }

    return new Tuple2<>(Set.of(), Set.of());
  }

  /**
   * Returns a {@link Tuple2} assigning the first property an unmodifiable <em>ordered</em> {@link Set} containing all
   * the distinct elements from the source, and assigning to the second property an unmodifiable <em>ordered</em>
   * {@link Set} containing all the elements which appeared more than once from the source.
   *
   * @param stream the source of the T elements
   * @param <T>    the type of instances contained in the source
   * @return a {@link Tuple2} assigning the first property an unmodifiable <em>ordered</em> {@link Set} containing all
   *     the distinct elements from the source, and assigning to the second property an unmodifiable <em>ordered</em>
   *     {@link Set} containing all the elements which appeared more than once from the source
   */
  public static <T> Tuple2<Set<T>, Set<T>> toDistinctAndDupesOrdered(
      Stream<T> stream
  ) {
    var sequentialStream = stream.isParallel()
        ? stream.sequential()
        : stream;
    var toDistinctAndDupesState = sequentialStream
        .collect(
            () ->
                new ToDistinctAndDupesState<T>(
                    new LinkedHashSet<>(),
                    new LinkedHashSet<>()),
            (toDistinctAndDupesStateInterim, t) -> {
              if (!toDistinctAndDupesStateInterim.distincts.add(t)) {
                toDistinctAndDupesStateInterim.dupes.add(t);
              }
            },
            (toDistinctAndDupesStateInterim1, toDistinctAndDupesStateInterim2) -> {
              throw new IllegalStateException("should never get here - combiner was called on sequential stream");
            }
        );

    return new Tuple2<>(
        Collections.unmodifiableSet(toDistinctAndDupesState.distincts()),
        Collections.unmodifiableSet(toDistinctAndDupesState.dupes()));
  }

  /**
   * Represents the {@link Map#keySet} values in the returned by the function, {@link SetPair#toMap} that is used to
   * obtain the various sub-set views when comparing and contrasting a pair of {@link Set}s, each of which may be
   * obtained via {@link SetPairViewKey#LEFT} and {@link SetPairViewKey#RIGHT}.
   * <p>
   * Please see each individual value for a more detailed explanation of the associated sub-set view.
   */
  public enum SetPairViewKey {
    /**
     * The values of both the left and right sides.
     * <p>
     * <ul>
     *  <li>Consider the following example code snippet:
     * <pre>{@code
     * var setLeft  = Set.of(1, 2, 3);
     * var setRight = Set.of(2, 3, 4);
     * var valuesBySetPairViewKey = contrastSetPair(setA, setB);
     * assertEquals(Set.of(1, 2, 3, 4), valuesBySetPairViewKey.get(SetPairViewKey.UNION));
     * } </pre>
     * </li>
     * </ul>
     */
    UNION,
    /**
     * The defensively copied <em>original</em> values of the left side.
     * <p>
     * <ul>
     *  <li>Consider the following example code snippet:
     * <pre>{@code
     * var setLeft  = Set.of(1, 2, 3);
     * var setRight = Set.of(2, 3, 4);
     * var valuesBySetPairViewKey = contrastSetPair(setA, setB);
     * assertEquals(Set.of(1, 2, 3), valuesBySetPairViewKey.get(SetPairViewKey.LEFT));
     * } </pre>
     * </li>
     * </ul>
     */
    LEFT,
    /**
     * The defensively copied <em>original</em> values of the right side.
     * <p>
     * <ul>
     *  <li>Consider the following example code snippet:
     * <pre>{@code
     * var setLeft  = Set.of(1, 2, 3);
     * var setRight = Set.of(2, 3, 4);
     * var valuesBySetPairViewKey = contrastSetPair(setA, setB);
     * assertEquals(Set.of(2, 3, 4), valuesBySetPairViewKey.get(SetPairViewKey.RIGHT));
     * } </pre>
     * </li>
     * </ul>
     */
    RIGHT,
    /**
     * The values in common from both the left side and right sides.
     * <p>
     * <ul>
     *  <li>Consider the following example code snippet:
     * <pre>{@code
     * var setLeft  = Set.of(1, 2, 3);
     * var setRight = Set.of(2, 3, 4);
     * var valuesBySetPairViewKey = contrastSetPair(setA, setB);
     * assertEquals(Set.of(2, 3), valuesBySetPairViewKey.get(SetPairViewKey.INTERSECTION));
     * } </pre>
     * </li>
     * </ul>
     */
    INTERSECTION,
    /**
     * The values not in common from both the left side and right sides.
     * <p>
     * <ul>
     *  <li>Consider the following example code snippet:
     * <pre>{@code
     * var setLeft  = Set.of(1, 2, 3);
     * var setRight = Set.of(2, 3, 4);
     * var valuesBySetPairViewKey = contrastSetPair(setA, setB);
     * assertEquals(Set.of(1, 4), valuesBySetPairViewKey.get(SetPairViewKey.DIFFERENCE));
     * } </pre>
     * </li>
     * </ul>
     */
    DIFFERENCE,
    /**
     * The values unique to the left side.
     * <p>
     * <ul>
     *  <li>Consider the following example code snippet:
     * <pre>{@code
     * var setLeft  = Set.of(1, 2, 3);
     * var setRight = Set.of(2, 3, 4);
     * var valuesBySetPairViewKey = contrastSetPair(setA, setB);
     * assertEquals(Set.of(1), valuesBySetPairViewKey.get(SetPairViewKey.LEFT_DIFFERENCE));
     * } </pre>
     * </li>
     * </ul>
     */
    LEFT_DIFFERENCE,
    /**
     * The values unique to the right side.
     * <p>
     * <ul>
     *  <li>Consider the following example code snippet:
     * <pre>{@code
     * var setLeft  = Set.of(1, 2, 3);
     * var setRight = Set.of(2, 3, 4);
     * var valuesBySetPairViewKey = contrastSetPair(setA, setB);
     * assertEquals(Set.of(4), valuesBySetPairViewKey.get(SetPairViewKey.RIGHT_DIFFERENCE));
     * } </pre>
     * </li>
     * </ul>
     */
    RIGHT_DIFFERENCE
  }

  private static final int CONTRAST_SET_PAIR_INDEX_UNION = 0;
  private static final int CONTRAST_SET_PAIR_INDEX_INTERSECTION = 1;
  private static final int CONTRAST_SET_PAIR_INDEX_DIFFERENCE = 2;
  private static final int CONTRAST_SET_PAIR_INDEX_LEFT_DIFFERENCE = 3;
  private static final int CONTRAST_SET_PAIR_INDEX_RIGHT_DIFFERENCE = 4;

  /**
   * Returns an {@link SetPair} of the contrast between the {@code left} and {@code right} defensively copied
   * {@link Set}s, where for each property, the value associated is an unmodifiable {@link Set} containing the relevant
   * elements of type {@code T} based on the comparison described by the property's name.
   * <p>
   * This implementation minimizes the amount of iterations, comparisons, and insertions necessary (single pass over
   * each element in both {@link Set}s) to produce the discrete results, explicitly short-circuit optimizing in the
   * event of either or both sets return true for {@link Set#isEmpty}.
   *
   * @param <T> the type of instances contained in the sets
   */
  public static final class SetPair<T> {
    private final boolean isEqual;
    private final Set<T> union;
    private final Set<T> left;
    private final Set<T> right;
    private final Set<T> intersection;
    private final Set<T> leftDifference;
    private final Set<T> rightDifference;
    private final Set<T> difference;

    // Private constructor absolutely prevents illegal states from being instantiated
    private SetPair(
        boolean isEqual,
        Set<T> union,
        Set<T> left,
        Set<T> right,
        Set<T> intersection,
        Set<T> leftDifference,
        Set<T> rightDifference,
        Set<T> difference
    ) {
      this.isEqual = isEqual;
      this.union = union;
      this.left = left;
      this.right = right;
      this.intersection = intersection;
      this.leftDifference = leftDifference;
      this.rightDifference = rightDifference;
      this.difference = difference;
    }

    /**
     * Returns a {@link SetPair} reflecting the contrast between two defensively copied {@link Set}s.
     *
     * @param leftTs  the defensively copied left set of instances
     * @param rightTs the defensively copied right set of instances
     * @param <T>     the type of instances contained in the sets
     * @return an {@link SetPair} of the contrast between two defensively copied {@link Set}s
     * @throws NullPointerException if either {@code leftTs} or {@code rightTs} contains any {@code null}s
     */
    @SuppressWarnings("unchecked")
    public static <T> SetPair<T> from(Set<T> leftTs, Set<T> rightTs) {
      if (!leftTs.isEmpty()) {
        var leftTsDefensiveCopy = Set.copyOf(leftTs);
        if (!rightTs.isEmpty()) {
          var rightTsDefensiveCopy = Set.copyOf(rightTs);
          var accumulators = new Set[]{
              new HashSet<T>(),  // union
              new HashSet<T>(),  // intersection
              new HashSet<T>(),  // difference
              new HashSet<T>(),  // leftDifference
              new HashSet<T>()}; // rightDifference
          Stream.concat(
                  leftTsDefensiveCopy.stream(),
                  rightTsDefensiveCopy.stream())
              .forEachOrdered(t -> {
                if (accumulators[CONTRAST_SET_PAIR_INDEX_UNION].add(t)) {
                  //can only get here if it hadn't been added in a prior iteration
                  var tInLeft = leftTsDefensiveCopy.contains(t);
                  var tInRight = rightTsDefensiveCopy.contains(t);
                  if (tInLeft) {
                    if (tInRight) {
                      accumulators[CONTRAST_SET_PAIR_INDEX_INTERSECTION].add(t);
                    } else { //!tInRight
                      accumulators[CONTRAST_SET_PAIR_INDEX_DIFFERENCE].add(t);
                      accumulators[CONTRAST_SET_PAIR_INDEX_LEFT_DIFFERENCE].add(t);
                    }
                  } else { //!tInLeft
                    if (tInRight) {
                      accumulators[CONTRAST_SET_PAIR_INDEX_DIFFERENCE].add(t);
                      accumulators[CONTRAST_SET_PAIR_INDEX_RIGHT_DIFFERENCE].add(t);
                    } else { //!tInRight
                      //given the contents of this was derived from the two defensive copies, this is
                      //  an unreachable, and therefore an insane, state
                      throw new IllegalStateException("should never get here");
                    }
                  }
                }
              });
          var differenceSet = Collections.unmodifiableSet(accumulators[CONTRAST_SET_PAIR_INDEX_DIFFERENCE]);

          return new SetPair<T>(
              differenceSet.isEmpty(),
              Collections.unmodifiableSet(accumulators[CONTRAST_SET_PAIR_INDEX_UNION]),
              leftTsDefensiveCopy,
              rightTsDefensiveCopy,
              Collections.unmodifiableSet(accumulators[CONTRAST_SET_PAIR_INDEX_INTERSECTION]),
              Collections.unmodifiableSet(accumulators[CONTRAST_SET_PAIR_INDEX_LEFT_DIFFERENCE]),
              Collections.unmodifiableSet(accumulators[CONTRAST_SET_PAIR_INDEX_RIGHT_DIFFERENCE]),
              differenceSet);
        } else {
          //rightTs.isEmpty() is true

          return new SetPair<>(
              false,
              leftTsDefensiveCopy,
              leftTsDefensiveCopy,
              Set.of(),
              Set.of(),
              leftTsDefensiveCopy,
              Set.of(),
              leftTsDefensiveCopy);

        }
      } else {
        //leftTs.isEmpty() is true
        if (!rightTs.isEmpty()) {
          var rightTsDefensiveCopy = Set.copyOf(rightTs);

          return new SetPair<>(
              false,
              rightTsDefensiveCopy,
              Set.of(),
              rightTsDefensiveCopy,
              Set.of(),
              Set.of(),
              rightTsDefensiveCopy,
              rightTsDefensiveCopy);
        } else {
          //leftTs.isEmpty() and //rightTs.isEmpty()

          return new SetPair<>(
              true,
              Set.of(),
              Set.of(),
              Set.of(),
              Set.of(),
              Set.of(),
              Set.of(),
              Set.of());
        }
      }
    }

    public Map<SetPairViewKey, Set<T>> toMap() {
      return Map.of(
          SetPairViewKey.UNION, union,
          SetPairViewKey.LEFT, left,
          SetPairViewKey.RIGHT, right,
          SetPairViewKey.INTERSECTION, intersection,
          SetPairViewKey.LEFT_DIFFERENCE, leftDifference,
          SetPairViewKey.RIGHT_DIFFERENCE, rightDifference,
          SetPairViewKey.DIFFERENCE, difference);
    }

    public boolean isEqual() {
      return isEqual;
    }

    public Set<T> union() {
      return union;
    }

    public Set<T> left() {
      return left;
    }

    public Set<T> right() {
      return right;
    }

    public Set<T> intersection() {
      return intersection;
    }

    public Set<T> leftDifference() {
      return leftDifference;
    }

    public Set<T> rightDifference() {
      return rightDifference;
    }

    public Set<T> difference() {
      return difference;
    }

    @Override
    public boolean equals(Object object) {
      return ((this == object) ||
          ((object instanceof SetPair<?> that) &&
              left.equals(that.left) &&
              right.equals(that.right)));
    }

    @Override
    public int hashCode() {
      return Objects.hash(
          left,
          right);
    }

    @Override
    public String toString() {
      return "SetPair[" +
          "isEqual=" + isEqual + ", " +
          "union=" + union + ", " +
          "left=" + left + ", " +
          "right=" + right + ", " +
          "intersection=" + intersection + ", " +
          "leftDifference=" + leftDifference + ", " +
          "rightDifference=" + rightDifference + ", " +
          "difference=" + difference + ']';
    }
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Set} containing the (filtered to non-null) elements.
   *
   * @param ts  the source of the values
   * @param <T> the type of instances contained in the source
   * @return an unmodifiable <u><i>ordered</i></u> {@code Set} containing the (filtered to non-null) elements
   * @throws IllegalArgumentException if any t instance is duplicated; i.e. all ts must be unique, and identifies the
   *                                  {@code key}(s) causing the collision
   */
  @SuppressWarnings("ConstantValue")
  @SafeVarargs
  public static <T> Set<T> ofOrdered(
      T... ts
  ) {
    if (ts.length > 0) {
      var tsSansNulls = Arrays.stream(ts)
          .filter(Objects::nonNull)
          .toList();
      if (!tsSansNulls.isEmpty()) {
        var result = new LinkedHashSet<T>();
        var duplicates = new ArrayList<T>();
        //noinspection SimplifyStreamApiCallChains
        tsSansNulls
            .stream()
            .forEachOrdered(t -> {
              if (!result.add(t)) {
                duplicates.add(t);
              }
            });
        if (!duplicates.isEmpty()) {
          throw new IllegalArgumentException("duplicate elements encountered - %s".formatted(
              String.join(
                  ", ",
                  duplicates
                      .stream()
                      .map(Object::toString)
                      .toList())));
        }

        return Collections.unmodifiableSet(result);
      }
    }

    return Set.of();
  }
}
