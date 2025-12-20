package org.deus_ex_java.util;

import org.deus_ex_java.util.refined.NonEmptySet;
import org.deus_ex_java.util.tuple.Tuple2;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
   * Returns an empty {@link Set} using {@link Set#of}, if {@code ts} is {@code null}, otherwise returns {@code ts}.
   * <p>
   * Note: If the requirement is to both avoid a {@code null} value, and an empty {@link Set}, the refined class of
   * {@link NonEmptySet} enable <i>compile-time enforcement</i> of said contract requirements.
   *
   * @param ts  possibly {@code null} {@link Set} to reify to make {@code null} safe
   * @param <T> the type of instances contained in the {@link Set}
   * @return an empty {@link Set} using {@link Set#of}, if {@code ts} is {@code null}, otherwise returns {@code ts}
   */
  public static <T> Set<T> nullToEmpty(@Nullable Set<T> ts) {
    return ts != null
        ? ts
        : Set.of();
  }

  /**
   * Returns an unmodifiable unordered set with the {@code value} added.
   *
   * @param set   the source from which the unordered copy is made
   * @param value the value to add to the copy of the list
   * @param <T>   the type of instances contained in the set
   * @return an unmodifiable unordered set with the {@code value} added
   */
  public static <T> Set<T> addItem(
      Set<T> set,
      T value
  ) {
    if (!set.isEmpty()) {
      var result = new HashSet<>(set);
      result.add(value);

      return Collections.unmodifiableSet(result);
    }

    return Set.of(value);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> set with the {@code value} appended.
   *
   * @param set   the (assumed to be) <u><i>ordered</i></u> source from which the copy is made
   * @param value the value to add to the copy of the set
   * @param <T>   the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> set with the {@code value} appended
   */
  public static <T> Set<T> appendItem(
      Set<T> set,
      T value
  ) {
    if (!set.isEmpty()) {
      var result = new LinkedHashSet<>(set);
      result.add(value);

      return Collections.unmodifiableSet(result);
    }

    return Set.of(value);
  }

  /**
   * Returns an unmodifiable unordered set consisting of each set (filtered to non-null) from sets added together.
   *
   * @param sets the sets to append
   * @param <T>  the type of instances contained in all the sets
   * @return unmodifiable unordered set consisting of each set (filtered to non-null) from sets added together
   */
  @SuppressWarnings("ConstantValue")
  @SafeVarargs
  public static <T> Set<T> addSets(
      Set<T>... sets
  ) {
    if (sets.length > 0) {
      var result = new HashSet<T>();
      IntStream.range(0, sets.length)
          .forEach(index -> {
            var set = sets[index];
            if (set != null) {
              var resolvedSet =
                  set.stream()
                      .filter(Objects::nonNull)
                      .collect(Collectors.toUnmodifiableSet());
              if (!resolvedSet.isEmpty()) {
                result.addAll(resolvedSet);
              }
            }
          });

      return !result.isEmpty()
          ? Collections.unmodifiableSet(result)
          : Set.of();
    }

    return Set.of();
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> set consisting of each set (filtered to non-null) from sets appended
   * together.
   *
   * @param sets the (assumed to be) <u><i>ordered</i></u> sets to append
   * @param <T>  the type of instances contained in all the sets
   * @return an unmodifiable <u><i>ordered</i></u> set consisting of each set (filtered to non-null) from sets appended
   *     together
   */
  @SuppressWarnings("ConstantValue")
  @SafeVarargs
  public static <T> Set<T> appendSets(
      Set<T>... sets
  ) {
    if (sets.length > 0) {
      var result = new LinkedHashSet<T>();
      IntStream.range(0, sets.length)
          .forEach(index -> {
            var set = sets[index];
            if (set != null) {
              var resolvedSet = toSetOrdered(set.stream());
              if (!resolvedSet.isEmpty()) {
                result.addAll(resolvedSet);
              }
            }
          });

      return !result.isEmpty()
          ? Collections.unmodifiableSet(result)
          : Set.of();
    }

    return Set.of();
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
        .filter(t ->
            !Objects.isNull(t))
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
    var set = stream
        .filter(t ->
            !Objects.isNull(t))
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
        .filter(t ->
            !Objects.isNull(t))
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
   * Returns an unmodifiable {@link Map} of the contrast between two {@link Set}s, where for each key of type
   * {@link SetPairViewKey}, the value associated is an unmodifiable {@link Set} (which includes defensively copying the
   * two {@link Set}s) contains the relevant elements of type {@code T} based on the comparison described by said
   * {@link SetPairViewKey}.
   * <p>
   * This implementation minimizes the amount of iterations, comparisons, and insertions necessary (single pass over
   * each element in both {@link Set}s) to produce the discrete results, explicitly short-circuit optimizing in the
   * event of either or both sets return true for {@link Set#isEmpty}.
   *
   * @param leftTs  the set of instances as the left side
   * @param rightTs the set of instances as the right side
   * @param <T>     the type of instances contained in the sets
   * @return an unmodifiable {@link Map} of the contrast between two {@link Set}s, where for each key of type
   *     {@link SetPairViewKey}, the value associated is an unmodifiable {@link Set} (which includes defensively copying
   *     the two {@link Set}s) contains the relevant elements of type {@code T} based on the comparison described by
   *     said {@link SetPairViewKey}
   * @throws NullPointerException if {@code leftTs} or {@code rightTs} contains any {@code null}s
   * @deprecated Has been replaced by the {@link SetPair#toMap} function.
   */
  @Deprecated
  public static <T> Map<SetPairViewKey, Set<T>> contrastSetPair(
      Set<T> leftTs,
      Set<T> rightTs
  ) {
    return SetPair.from(leftTs, rightTs).toMap();
  }

  /**
   * Returns an {@link SetPair} of the contrast between the {@code left} and {@code right} defensively copied
   * {@link Set}s, where for each property, the value associated is an unmodifiable {@link Set} containing the relevant
   * elements of type {@code T} based on the comparison described by the property's name.
   * <p>
   * This implementation minimizes the amount of iterations, comparisons, and insertions necessary (single pass over
   * each element in both {@link Set}s) to produce the discrete results, explicitly short-circuit optimizing in the
   * event of either or both sets return true for {@link Set#isEmpty}.
   * <p>
   * <b>WARNING: </b> Prefer using the static factory {@link #from} instead of this constructor.
   *
   * @param isEqual         the result of {@code left.equals(right)} (without any additional iteration)
   * @param union           the values of both the left and right sides
   * @param left            the defensively copied <em>original</em> values of the left side
   * @param right           the defensively copied <em>original</em> values of the right side
   * @param intersection    the values in common from both the left side and right sides
   * @param difference      the values not in common from both the left side and right sides
   * @param leftDifference  the values unique to the left side
   * @param rightDifference the values unique to the right side
   * @param <T>             the type of instances contained in the sets
   */
  @SuppressWarnings("DeprecatedIsStillUsed")
  public record SetPair<T>(
      boolean isEqual,
      Set<T> union,
      Set<T> left,
      Set<T> right,
      Set<T> intersection,
      Set<T> leftDifference,
      Set<T> rightDifference,
      Set<T> difference
  ) {

    /**
     * Returns a {@link SetPair} reflecting the contrast between two defensively copied {@link Set}s, where for each
     * property, the value associated is an unmodifiable {@link Set} containing the relevant elements of type {@code T}
     * based on the part of the comparison described by the property's name.
     * <p>
     * This implementation minimizes the amount of iterations, comparisons, and insertions necessary (single pass over
     * each element in each {@link Set}) to produce the discrete results, explicitly short-circuit optimizing in the
     * event of either or both sets return true for {@link Set#isEmpty}.
     *
     * @param leftTs  the defensively copied left set of instances
     * @param rightTs the defensively copied right set of instances
     * @param <T>     the type of instances contained in the sets
     * @return an {@link SetPair} of the contrast between two defensively copied {@link Set}s, where for each property,
     *     the value associated is an unmodifiable {@link Set} containing the relevant elements of type {@code T} based
     *     on the comparison described by the property's name
     * @throws NullPointerException if either {@code leftTs} or {@code rightTs} contains any {@code null}s
     */
    public static <T> SetPair<T> from(
        Set<T> leftTs,
        Set<T> rightTs
    ) {
      return new SetPair<T>(
          false,
          Set.of(),
          leftTs,
          rightTs,
          Set.of(),
          Set.of(),
          Set.of(),
          Set.of());
    }

    /**
     * Returns an unmodifiable {@link Map} of the contrast between two {@link Set}s, where for each key of type
     * {@link SetPairViewKey}, the value associated is an unmodifiable {@link Set} (which includes defensively copying
     * the two {@link Set}s) contains the relevant elements of type {@code T} based on the comparison described by said
     * {@link SetPairViewKey}
     *
     * @return an unmodifiable {@link Map} of the contrast between two {@link Set}s, where for each key of type
     *     {@link SetPairViewKey}, the value associated is an unmodifiable {@link Set} (which includes defensively
     *     copying the two {@link Set}s) contains the relevant elements of type {@code T} based on the comparison
     *     described by said {@link SetPairViewKey}
     */
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

    /**
     * <b>WARNING: </b> Prefer using the static factory {@link #from} instead of this {@code new} constructor.
     * <p>
     * This {@code new} constructor discards any values passed in any of the properties, excluding {@code left} and
     * {@code right}, and replaces the discarded properties with the results of running the contrasting algorithm on the
     * {@code left} and {@code right} properties.
     * <p>
     * Essentially, this constructor behaves as though you called it as such:
     * <pre>{@code
     * var setPair = new SetPair<>(Set.of(), left, right, Set.of(), Set.of(), Set.of(), Set.of());
     * } </pre>
     * <p>This is literally how the static factory {@link #from} method is implemented.
     *
     * @param union           <em>discarded and overridden</em> with the result of the evaluation of {@code left} and
     *                        {@code right}
     * @param left            the source of the left elements
     * @param right           the source of the right elements
     * @param intersection    <em>discarded and overridden</em> with the result of the evaluation of {@code left} and
     *                        {@code right}
     * @param leftDifference  <em>discarded and overridden</em> with the result of the evaluation of {@code left} and
     *                        {@code right}
     * @param rightDifference <em>discarded and overridden</em> with the result of the evaluation of {@code left} and
     *                        {@code right}
     * @param difference      <em>discarded and overridden</em> with the result of the evaluation of {@code left} and
     *                        {@code right}
     * @deprecated Prefer using the static factory {@link #from} instead of this constructor (please see <b>WARNING:
     *     </b>)
     */
    @SuppressWarnings("unchecked")
    @Deprecated
    public SetPair {
      if (!left.isEmpty()) {
        var leftTsDefensiveCopy = Set.copyOf(left);
        if (!right.isEmpty()) {
          var rightTsDefensiveCopy = Set.copyOf(right);
          var accumulators = new Set[]{
              new HashSet<T>(),  //union
              new HashSet<T>(),  //intersection
              new HashSet<T>(),  //difference
              new HashSet<T>(),  //leftDifference
              new HashSet<T>()}; //rightDifference
          Stream.concat(
                  leftTsDefensiveCopy.stream(),
                  rightTsDefensiveCopy.stream())
              .forEachOrdered(t -> {
                if (accumulators[CONTRAST_SET_PAIR_INDEX_UNION].add(t)) {
                  //can only get here if t hadn't been added in a prior iteration
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
          union = Collections.<Set<T>>unmodifiableSet(accumulators[CONTRAST_SET_PAIR_INDEX_UNION]);
          left = leftTsDefensiveCopy;
          right = rightTsDefensiveCopy;
          intersection = Collections.<Set<T>>unmodifiableSet(accumulators[CONTRAST_SET_PAIR_INDEX_INTERSECTION]);
          leftDifference = Collections.<Set<T>>unmodifiableSet(
              accumulators[CONTRAST_SET_PAIR_INDEX_LEFT_DIFFERENCE]);
          rightDifference = Collections.<Set<T>>unmodifiableSet(
              accumulators[CONTRAST_SET_PAIR_INDEX_RIGHT_DIFFERENCE]);
          difference = Collections.<Set<T>>unmodifiableSet(accumulators[CONTRAST_SET_PAIR_INDEX_DIFFERENCE]);
          isEqual = difference.isEmpty();
        } else {
          //left.isEmpty() is false, and right.isEmpty() is true
          isEqual = false;
          union = leftTsDefensiveCopy;
          left = leftTsDefensiveCopy;
          right = Set.of();
          intersection = Set.of();
          leftDifference = leftTsDefensiveCopy;
          rightDifference = Set.of();
          difference = leftTsDefensiveCopy;
        }
      } else {
        if (!right.isEmpty()) {
          //leftTs.isEmpty() is true, and rightTs.isEmpty() is false
          var rightTsDefensiveCopy = Set.copyOf(right);
          isEqual = false;
          union = rightTsDefensiveCopy;
          left = Set.of();
          right = rightTsDefensiveCopy;
          intersection = Set.of();
          leftDifference = Set.of();
          rightDifference = rightTsDefensiveCopy;
          difference = rightTsDefensiveCopy;
        } else {
          //leftTs.isEmpty() is true, and rightTs.isEmpty() is true
          isEqual = true;
          union = Set.of();
          left = Set.of();
          right = Set.of();
          intersection = Set.of();
          leftDifference = Set.of();
          rightDifference = Set.of();
          difference = Set.of();
        }
      }
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
