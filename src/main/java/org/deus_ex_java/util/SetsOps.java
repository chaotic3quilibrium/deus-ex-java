package org.deus_ex_java.util;

import org.deus_ex_java.util.refined.NonEmptySet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class providing static methods to create {@link Set} instances.
 */
public class SetsOps {

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
  @NotNull
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
  @NotNull
  public static <T> Set<T> addItem(
      @NotNull Set<T> set,
      @NotNull T value
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
  @NotNull
  public static <T> Set<T> appendItem(
      @NotNull Set<T> set,
      @NotNull T value
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
  @NotNull
  @SafeVarargs
  public static <T> Set<T> addSets(
      @NotNull Set<T>... sets
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
  @NotNull
  @SafeVarargs
  public static <T> Set<T> appendSets(
      @NotNull Set<T>... sets
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
   * Returns an unmodifiable set with the null elements filtered out.
   *
   * @param stream the source of the T elements
   * @param <T>    the type of the instances
   * @return an unmodifiable set with the null elements filtered out
   */
  @NotNull
  public static <T> Set<T> toSet(
      @NotNull Stream<T> stream
  ) {
    return stream
        .filter(t ->
            !Objects.isNull(t))
        .collect(Collectors.toUnmodifiableSet());
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> set with the null elements filtered out.
   *
   * @param stream the (assumed to be) <u><i>ordered</i></u> source of the T elements
   * @param <T>    the type of the instances
   * @return an unmodifiable <u><i>ordered</i></u> set with the null elements filtered out
   */
  @NotNull
  public static <T> Set<T> toSetOrdered(
      @NotNull Stream<T> stream
  ) {
    var set = stream
        .filter(t ->
            !Objects.isNull(t))
        .collect(Collectors.toCollection(LinkedHashSet::new));

    return !set.isEmpty()
        ? Collections.unmodifiableSet(set)
        : Set.of();
  }

  /**
   * Represents the {@link Map#keySet} values in the returned by the {@code static} function,
   * {@link SetsOps#contrastSetPair} that is used to obtain the various sub-set views when comparing and contrasting a
   * pair of {@link Set}s, each of which may be obtained via {@link SetPairViewKey#LEFT} and
   * {@link SetPairViewKey#RIGHT}.
   * <p>
   * Please see each individual value for a more detailed explanation of the associated sub-set view.
   */
  public enum SetPairViewKey {
    /**
     * The values of both the left and right side of the pair of {@link Set}s, with the common values only represented
     * once.
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
     * The values of the left side of the pair of {@link Set}s being compared, including any possible overlap with the
     * right side.
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
     * The values of the right side of the pair of {@link Set}s being compared, including any possible overlap with the
     * left side.
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
     * The values in common from both the left side and right side of the pair of {@link Set}s being compared.
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
     * The values not in common from both the left side and right side of the pair of {@link Set}s being compared.
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
     * The values unique to the left side of the pair of {@link Set}s being compared.
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
     * The values unique to the right side of the pair of {@link Set}s being compared.
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
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public static <T> Map<SetPairViewKey, Set<T>> contrastSetPair(
      @NotNull Set<T> leftTs,
      @NotNull Set<T> rightTs
  ) {
    if (!leftTs.isEmpty()) {
      if (!rightTs.isEmpty()) {
        var leftTsDefensiveCopy = Set.copyOf(leftTs);
        var rightTsDefensiveCopy = Set.copyOf(rightTs);
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
                var tInLeft = leftTs.contains(t);
                var tInRight = rightTs.contains(t);
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

        return Map.of(
            SetPairViewKey.UNION,
            Collections.<Set<T>>unmodifiableSet(accumulators[CONTRAST_SET_PAIR_INDEX_UNION]),
            SetPairViewKey.LEFT, leftTsDefensiveCopy,
            SetPairViewKey.RIGHT, rightTsDefensiveCopy,
            SetPairViewKey.INTERSECTION,
            Collections.<Set<T>>unmodifiableSet(accumulators[CONTRAST_SET_PAIR_INDEX_INTERSECTION]),
            SetPairViewKey.LEFT_DIFFERENCE,
            Collections.<Set<T>>unmodifiableSet(
                accumulators[CONTRAST_SET_PAIR_INDEX_LEFT_DIFFERENCE]),
            SetPairViewKey.RIGHT_DIFFERENCE,
            Collections.<Set<T>>unmodifiableSet(
                accumulators[CONTRAST_SET_PAIR_INDEX_RIGHT_DIFFERENCE]),
            SetPairViewKey.DIFFERENCE,
            Collections.<Set<T>>unmodifiableSet(accumulators[CONTRAST_SET_PAIR_INDEX_DIFFERENCE]));
      }
      //leftTs.isEmpty() is false, and rightTs.isEmpty() is true
      var leftTsDefensiveCopy = Set.copyOf(leftTs);

      return Map.of(
          SetPairViewKey.UNION, leftTsDefensiveCopy,
          SetPairViewKey.LEFT, leftTsDefensiveCopy,
          SetPairViewKey.RIGHT, Set.of(),
          SetPairViewKey.INTERSECTION, Set.of(),
          SetPairViewKey.LEFT_DIFFERENCE, leftTsDefensiveCopy,
          SetPairViewKey.RIGHT_DIFFERENCE, Set.of(),
          SetPairViewKey.DIFFERENCE, leftTsDefensiveCopy);
    }
    if (!rightTs.isEmpty()) {
      //leftTs.isEmpty() is true, and rightTs.isEmpty() is false
      var rightTsDefensiveCopy = Set.copyOf(rightTs);

      return Map.of(
          SetPairViewKey.UNION, rightTsDefensiveCopy,
          SetPairViewKey.LEFT, Set.of(),
          SetPairViewKey.RIGHT, rightTsDefensiveCopy,
          SetPairViewKey.INTERSECTION, Set.of(),
          SetPairViewKey.LEFT_DIFFERENCE, Set.of(),
          SetPairViewKey.RIGHT_DIFFERENCE, rightTsDefensiveCopy,
          SetPairViewKey.DIFFERENCE, rightTsDefensiveCopy);
    }
    //leftTs.isEmpty() is true, and rightTs.isEmpty() is true

    return Map.of(
        SetPairViewKey.UNION, Set.of(),
        SetPairViewKey.LEFT, Set.of(),
        SetPairViewKey.RIGHT, Set.of(),
        SetPairViewKey.INTERSECTION, Set.of(),
        SetPairViewKey.LEFT_DIFFERENCE, Set.of(),
        SetPairViewKey.RIGHT_DIFFERENCE, Set.of(),
        SetPairViewKey.DIFFERENCE, Set.of());
  }

  /**
   * Returns the passed in <u><i>mutable</i></u> {@code Set}, if a value was successfully added/appended without the
   * value pre-existing, otherwise throws an {@link IllegalArgumentException} that identifies the {@code value} causing
   * the collision.
   * <p>
   * ---
   * <p>
   * <b>WARNING:</b> This is a <b>SIDE-EFFECTING</b> method in that it modifies the {@code mutableSet} parameter.
   *
   * @param mutableSet the map into which the entry will be added/appended - SIDE EFFECTING
   * @param t          the value to add/append
   * @param <T>        the type of instances contained in the set
   * @return the passed in <u><i>mutable</i></u> {@code Set}, if a value was successfully added/appended without the
   *     value pre-existing, otherwise throws an {@link IllegalArgumentException} that identifies the {@code value}
   *     causing the collision
   */
  @SuppressWarnings("UnusedReturnValue")
  private static <T> Set<T> add(
      Set<T> mutableSet,
      T t
  ) {
    if (!mutableSet.add(t)) {
      throw new IllegalArgumentException("duplicate element: " + t);
    }

    return mutableSet;
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Set} containing a single element.
   *
   * @param t1  the element
   * @param <T> the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@code Set} containing a single element
   */
  @NotNull
  public static <T> Set<T> ofOrdered(
      @NotNull T t1
  ) {
    var result = new LinkedHashSet<T>();
    result.add(t1);

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements.
   *
   * @param t1  the first element
   * @param t2  the second element
   * @param <T> the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements
   * @throws IllegalArgumentException if the values are not unique
   */
  @NotNull
  public static <T> Set<T> ofOrdered(
      @NotNull T t1,
      @NotNull T t2
  ) {
    var result = new LinkedHashSet<T>();
    result.add(t1);
    add(result, t2);

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements.
   *
   * @param t1  the first element
   * @param t2  the second element
   * @param t3  the third element
   * @param <T> the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements
   * @throws IllegalArgumentException if the values are not unique
   */
  @NotNull
  public static <T> Set<T> ofOrdered(
      @NotNull T t1,
      @NotNull T t2,
      @NotNull T t3
  ) {
    var result = new LinkedHashSet<T>();
    result.add(t1);
    add(result, t2);
    add(result, t3);

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements.
   *
   * @param t1  the first element
   * @param t2  the second element
   * @param t3  the third element
   * @param t4  the fourth element
   * @param <T> the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements
   * @throws IllegalArgumentException if the values are not unique
   */
  @NotNull
  public static <T> Set<T> ofOrdered(
      @NotNull T t1,
      @NotNull T t2,
      @NotNull T t3,
      @NotNull T t4
  ) {
    var result = new LinkedHashSet<T>();
    result.add(t1);
    add(result, t2);
    add(result, t3);
    add(result, t4);

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements.
   *
   * @param t1  the first element
   * @param t2  the second element
   * @param t3  the third element
   * @param t4  the fourth element
   * @param t5  the fifth element
   * @param <T> the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements
   * @throws IllegalArgumentException if the values are not unique
   */
  @NotNull
  public static <T> Set<T> ofOrdered(
      @NotNull T t1,
      @NotNull T t2,
      @NotNull T t3,
      @NotNull T t4,
      @NotNull T t5
  ) {
    var result = new LinkedHashSet<T>();
    result.add(t1);
    add(result, t2);
    add(result, t3);
    add(result, t4);
    add(result, t5);

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements.
   *
   * @param t1  the first element
   * @param t2  the second element
   * @param t3  the third element
   * @param t4  the fourth element
   * @param t5  the fifth element
   * @param t6  the sixth element
   * @param <T> the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements
   * @throws IllegalArgumentException if the values are not unique
   */
  @NotNull
  public static <T> Set<T> ofOrdered(
      @NotNull T t1,
      @NotNull T t2,
      @NotNull T t3,
      @NotNull T t4,
      @NotNull T t5,
      @NotNull T t6
  ) {
    var result = new LinkedHashSet<T>();
    result.add(t1);
    add(result, t2);
    add(result, t3);
    add(result, t4);
    add(result, t5);
    add(result, t6);

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements.
   *
   * @param t1  the first element
   * @param t2  the second element
   * @param t3  the third element
   * @param t4  the fourth element
   * @param t5  the fifth element
   * @param t6  the sixth element
   * @param t7  the seventh element
   * @param <T> the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements
   * @throws IllegalArgumentException if the values are not unique
   */
  @NotNull
  public static <T> Set<T> ofOrdered(
      @NotNull T t1,
      @NotNull T t2,
      @NotNull T t3,
      @NotNull T t4,
      @NotNull T t5,
      @NotNull T t6,
      @NotNull T t7
  ) {
    var result = new LinkedHashSet<T>();
    result.add(t1);
    add(result, t2);
    add(result, t3);
    add(result, t4);
    add(result, t5);
    add(result, t6);
    add(result, t7);

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements.
   *
   * @param t1  the first element
   * @param t2  the second element
   * @param t3  the third element
   * @param t4  the fourth element
   * @param t5  the fifth element
   * @param t6  the sixth element
   * @param t7  the seventh element
   * @param t8  the eighth element
   * @param <T> the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements
   * @throws IllegalArgumentException if the values are not unique
   */
  @NotNull
  public static <T> Set<T> ofOrdered(
      @NotNull T t1,
      @NotNull T t2,
      @NotNull T t3,
      @NotNull T t4,
      @NotNull T t5,
      @NotNull T t6,
      @NotNull T t7,
      @NotNull T t8
  ) {
    var result = new LinkedHashSet<T>();
    result.add(t1);
    add(result, t2);
    add(result, t3);
    add(result, t4);
    add(result, t5);
    add(result, t6);
    add(result, t7);
    add(result, t8);

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements.
   *
   * @param t1  the first element
   * @param t2  the second element
   * @param t3  the third element
   * @param t4  the fourth element
   * @param t5  the fifth element
   * @param t6  the sixth element
   * @param t7  the seventh element
   * @param t8  the eighth element
   * @param t9  the nineth element
   * @param <T> the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements
   * @throws IllegalArgumentException if the values are not unique
   */
  @NotNull
  public static <T> Set<T> ofOrdered(
      @NotNull T t1,
      @NotNull T t2,
      @NotNull T t3,
      @NotNull T t4,
      @NotNull T t5,
      @NotNull T t6,
      @NotNull T t7,
      @NotNull T t8,
      @NotNull T t9
  ) {
    var result = new LinkedHashSet<T>();
    result.add(t1);
    add(result, t2);
    add(result, t3);
    add(result, t4);
    add(result, t5);
    add(result, t6);
    add(result, t7);
    add(result, t8);
    add(result, t9);

    return Collections.unmodifiableSet(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements.
   *
   * @param t1  the first element
   * @param t2  the second element
   * @param t3  the third element
   * @param t4  the fourth element
   * @param t5  the fifth element
   * @param t6  the sixth element
   * @param t7  the seventh element
   * @param t8  the eighth element
   * @param t9  the nineth element
   * @param t10 the tenth element
   * @param <T> the type of instances contained in the set
   * @return an unmodifiable <u><i>ordered</i></u> {@code Set} containing ten elements
   * @throws IllegalArgumentException if the values are not unique
   */
  @NotNull
  public static <T> Set<T> ofOrdered(
      @NotNull T t1,
      @NotNull T t2,
      @NotNull T t3,
      @NotNull T t4,
      @NotNull T t5,
      @NotNull T t6,
      @NotNull T t7,
      @NotNull T t8,
      @NotNull T t9,
      @NotNull T t10
  ) {
    var result = new LinkedHashSet<T>();
    result.add(t1);
    add(result, t2);
    add(result, t3);
    add(result, t4);
    add(result, t5);
    add(result, t6);
    add(result, t7);
    add(result, t8);
    add(result, t9);
    add(result, t10);

    return Collections.unmodifiableSet(result);
  }
}
