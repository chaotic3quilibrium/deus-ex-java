package org.deus_ex_java.util;

import org.deus_ex_java.util.refined.NonEmptyList;
import org.deus_ex_java.util.tuple.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class providing static methods to create {@link List} instances.
 */
public class ListsOps {

  private ListsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns an empty {@link List} using {@link List#of}, if {@code ts} is {@code null}, otherwise returns {@code ts}.
   * <p>
   * Note: If the requirement is to both avoid a {@code null} value, and an empty {@link List}, the refined class of
   * {@link NonEmptyList} enable <i>compile-time enforcement</i> of said contract requirements.
   *
   * @param ts  possibly {@code null} {@link List} to reify to make {@code null} safe
   * @param <T> the type of instances contained in the {@link List}
   * @return an empty {@link List} using {@link List#of}, if {@code ts} is {@code null}, otherwise returns {@code ts}
   */
  @NotNull
  public static <T> List<T> nullToEmpty(@Nullable List<T> ts) {
    return ts != null
        ? ts
        : List.of();
  }

  /**
   * Returns an unmodifiable list with the {@code value} appended.
   *
   * @param list  the source from which the copy is made
   * @param value the value to add to the copy of the list
   * @param <T>   the type of instances contained in the list
   * @return an unmodifiable list with the {@code value} appended
   */
  @NotNull
  public static <T> List<T> appendItem(
      @NotNull List<T> list,
      @NotNull T value
  ) {
    if (!list.isEmpty()) {
      var result = new ArrayList<>(list);
      result.add(value);

      return Collections.unmodifiableList(result);
    }

    return List.of(value);
  }

  /**
   * Returns an unmodifiable list consisting of each list (filtered to non-null) from lists appended in iteration
   * order.
   *
   * @param lists the lists to append
   * @param <T>   the type of instances contained within all the lists
   * @return an unmodifiable List consisting of each List (filtered to non-null) from lists appended in iteration order
   */
  @SuppressWarnings("ConstantValue")
  @NotNull
  @SafeVarargs
  public static <T> List<T> appendLists(@NotNull List<T>... lists) {
    if (lists.length > 0) {
      var result = new ArrayList<T>();
      IntStream.range(0, lists.length)
          .forEach(index -> {
            var list = lists[index];
            if (list != null) {
              var listFilteredToNonNulls = list
                  .stream()
                  .filter(Objects::nonNull)
                  .toList();
              if (!listFilteredToNonNulls.isEmpty()) {
                result.addAll(listFilteredToNonNulls);
              }
            }
          });

      return !result.isEmpty()
          ? Collections.unmodifiableList(result)
          : List.of();
    }

    return List.of();
  }

  /**
   * Returns an unmodifiable list with the null elements filtered out.
   *
   * @param stream the source of the T elements
   * @param <T>    the type of the instances
   * @return an unmodifiable list with the null elements filtered out
   */
  @NotNull
  public static <T> List<T> toList(
      @NotNull Stream<T> stream
  ) {
    return stream
        .filter(t ->
            !Objects.isNull(t))
        .toList();
  }

  /**
   * Returns a new {@link List} from a collection of {@code integers}.
   *
   * @param integers the source of the derived {@link Integer} values
   * @return a new {@link List} from a collection of {@code integers}
   */
  @NotNull
  public static List<Integer> toDistinctSortedListInteger(
      @NotNull Stream<Integer> integers
  ) {
    return toDistinctSortedListInteger(integers, Function.identity());
  }

  /**
   * Returns a new {@link List} from a collection of {@code ts} deriving the {@link Integer} value via the function
   * {@code fTToId}.
   *
   * @param ts     the source of the derived {@link Integer} values
   * @param fTToId the function to use to extract the {@link Integer} value from an element of the collection
   * @param <T>    the type of instances contained in the collection
   * @return a new {@link List} from a collection of {@code ts} deriving the {@link Integer} value via the function
   *     {@code fTToId}
   */
  @NotNull
  public static <T> List<Integer> toDistinctSortedListInteger(
      @NotNull Stream<T> ts,
      @NotNull Function<T, Integer> fTToId
  ) {
    return ts
        .map(fTToId)
        .distinct()
        .sorted()
        .toList();
  }

  /**
   * Returns a new {@link List} from a {@link Stream} of {@link Optional}  elements, extracting the non-empty
   * {@link Optional} elements, and filtering out the {@link Optional} empty elements.
   *
   * @param optionalTs the list from which to extract the non-empty optional values
   * @param <T>        the type of instances contained in the collection
   * @return a new {@link List} from a {@link Stream} of {@link Optional}  elements, extracting the non-empty
   *     {@link Optional} elements, and filtering out the {@link Optional} empty elements
   */
  @NotNull
  public static <T> List<T> flatten(
      @NotNull Stream<Optional<T>> optionalTs
  ) {
    return optionalTs
        .flatMap(Optional::stream)
        .toList();
  }

  /**
   * Returns {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Either}s.
   *
   * @param eithers the list from which to extract the lists
   * @param <L>     the type of instances contained within the left element of each either
   * @param <R>     the type of instances contained within the right element of each either
   * @return {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Either}s
   */
  @NotNull
  public static <L, R> Tuple2<List<Optional<L>>, List<Optional<R>>> unzipEithers(
      @NotNull Stream<Either<L, R>> eithers
  ) {
    return unzip(eithers
        .map(
            either ->
                new Tuple2<>(
                    either.toOptionalLeft(),
                    either.toOptionalRight())));
  }

  /**
   * Returns {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Either}s, {@link #flatten}ing
   * each returned list.
   *
   * @param eithers the list from which to extract the lists
   * @param <L>     the type of instances contained within the left element of each either
   * @param <R>     the type of instances contained within the right element of each either
   * @return {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Either}s, {@link #flatten}ing
   *     each returned list
   */
  @NotNull
  public static <L, R> Tuple2<List<L>, List<R>> unzipAndFlattenEithers(
      @NotNull Stream<Either<L, R>> eithers
  ) {
    var unzippedEithers = unzipEithers(eithers);

    return new Tuple2<>(
        flatten(unzippedEithers._1().stream()),
        flatten(unzippedEithers._2().stream()));
  }

  /**
   * Returns {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Tuple2}s.
   *
   * @param aAndBs the list of tuples from which to extract the lists
   * @param <A>    the type of instances contained within the first element of each tuple
   * @param <B>    the type of instances contained within the second element of each tuple
   * @return {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Tuple2}s
   */
  @NotNull
  public static <A, B> Tuple2<List<A>, List<B>> unzip(
      @NotNull Stream<Tuple2<A, B>> aAndBs
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    aAndBs.forEachOrdered(aAndB -> {
      listA.add(aAndB._1());
      listB.add(aAndB._2());
    });
    if (!listA.isEmpty()) {

      return new Tuple2<>(
          Collections.unmodifiableList(listA),
          Collections.unmodifiableList(listB));
    }

    return new Tuple2<>(
        List.of(),
        List.of());
  }

  /**
   * Return {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Tuple2}s filtered by a the
   * {@code fMapper} function.
   *
   * @param aAndBs  the list of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the collection and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @return {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Tuple2}s filtered by a the
   *     {@code fMapper} function.
   */
  @NotNull
  public static <A, B> Tuple2<List<A>, List<B>> unzipAndFlatten(
      @NotNull Stream<Tuple2<A, B>> aAndBs,
      @NotNull Function<Tuple2<A, B>, Optional<Tuple2<Optional<A>, Optional<B>>>> fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    aAndBs.forEachOrdered(aAndB -> {
      var optionalOptionalAAndOptionalB = fMapper.apply(aAndB);
      optionalOptionalAAndOptionalB
          .ifPresent(optionalAAndOptionalB -> {
            optionalAAndOptionalB._1()
                .ifPresent(listA::add);
            optionalAAndOptionalB._2()
                .ifPresent(listB::add);
          });
    });

    return new Tuple2<>(
        listA.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listA),
        listB.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listB));
  }

  /**
   * Returns {@link List}s in a {@link Tuple3} extracted from a {@link Stream} of {@link Tuple3}s.
   *
   * @param aAndBAndCs the list of tuples from which to extract the lists
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @return {@link List}s in a {@link Tuple3} extracted from a {@link Stream} of {@link Tuple3}s
   */
  @NotNull
  public static <A, B, C> Tuple3<List<A>, List<B>, List<C>> unzip3(
      @NotNull Stream<Tuple3<A, B, C>> aAndBAndCs
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    aAndBAndCs.forEachOrdered(aAndBAndC -> {
      listA.add(aAndBAndC._1());
      listB.add(aAndBAndC._2());
      listC.add(aAndBAndC._3());
    });
    if (!listA.isEmpty()) {

      return new Tuple3<>(
          Collections.unmodifiableList(listA),
          Collections.unmodifiableList(listB),
          Collections.unmodifiableList(listC));
    }

    return new Tuple3<>(
        List.of(),
        List.of(),
        List.of());
  }

  /**
   * Return {@link List}s in a {@link Tuple3} extracted from a {@link Stream} of {@link Tuple3}s filtered by a the
   * {@code fMapper} function.
   *
   * @param aAndBAndCs the list of tuples from which to extract the lists
   * @param fMapper    the function to use to extract the values from an element of the collection and optionally return
   *                   a value, and when non-empty, optionally return each of the element values
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @return {@link List}s in a {@link Tuple3} extracted from a {@link Stream} of {@link Tuple3}s filtered by a the
   *     {@code fMapper} function
   */
  @NotNull
  public static <A, B, C> Tuple3<List<A>, List<B>, List<C>> unzip3AndFlatten(
      @NotNull Stream<Tuple3<A, B, C>> aAndBAndCs,
      @NotNull Function<Tuple3<A, B, C>, Optional<Tuple3<Optional<A>, Optional<B>, Optional<C>>>> fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    aAndBAndCs.forEachOrdered(aAndBAndC ->
        fMapper.apply(aAndBAndC)
            .ifPresent(optionalAAndOptionalBOptionalC -> {
              optionalAAndOptionalBOptionalC._1()
                  .ifPresent(listA::add);
              optionalAAndOptionalBOptionalC._2()
                  .ifPresent(listB::add);
              optionalAAndOptionalBOptionalC._3()
                  .ifPresent(listC::add);
            }));

    return new Tuple3<>(
        listA.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listA),
        listB.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listB),
        listC.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listC));
  }

  /**
   * Returns {@link List}s in a {@link Tuple4} extracted from a {@link Stream} of {@link Tuple4}s.
   *
   * @param tuple4s the list of tuples from which to extract the lists
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @return {@link List}s in a {@link Tuple4} extracted from a {@link Stream} of {@link Tuple4}s
   */
  @NotNull
  public static <A, B, C, D>
  Tuple4<
      List<A>,
      List<B>,
      List<C>,
      List<D>
      > unzip4(
      @NotNull Stream<Tuple4<A, B, C, D>> tuple4s
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    tuple4s.forEachOrdered(element -> {
      listA.add(element._1());
      listB.add(element._2());
      listC.add(element._3());
      listD.add(element._4());
    });
    if (!listA.isEmpty()) {

      return new Tuple4<>(
          Collections.unmodifiableList(listA),
          Collections.unmodifiableList(listB),
          Collections.unmodifiableList(listC),
          Collections.unmodifiableList(listD));
    }

    return new Tuple4<>(
        List.of(),
        List.of(),
        List.of(),
        List.of());
  }

  /**
   * Return {@link List}s in a {@link Tuple4} extracted from a {@link Stream} of {@link Tuple4}s filtered by a the
   * {@code fMapper} function.
   *
   * @param tuple4s the list of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the collection and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @return {@link List}s in a {@link Tuple4} extracted from a {@link Stream} of {@link Tuple4}s filtered by a the
   *     {@code fMapper} function
   */
  @NotNull
  public static <A, B, C, D>
  Tuple4<
      List<A>,
      List<B>,
      List<C>,
      List<D>
      > unzip4AndFlatten(
      @NotNull Stream<Tuple4<A, B, C, D>> tuple4s,
      @NotNull Function<
          Tuple4<A, B, C, D>,
          Optional<
              Tuple4<
                  Optional<A>,
                  Optional<B>,
                  Optional<C>,
                  Optional<D>>>
          > fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    tuple4s.forEachOrdered(element ->
        fMapper.apply(element)
            .ifPresent(optionalOfOptionals -> {
              optionalOfOptionals._1()
                  .ifPresent(listA::add);
              optionalOfOptionals._2()
                  .ifPresent(listB::add);
              optionalOfOptionals._3()
                  .ifPresent(listC::add);
              optionalOfOptionals._4()
                  .ifPresent(listD::add);
            }));

    return new Tuple4<>(
        listA.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listA),
        listB.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listB),
        listC.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listC),
        listD.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listD));
  }

  /**
   * Returns {@link List}s in a {@link Tuple5} extracted from a {@link Stream} of {@link Tuple5}s.
   *
   * @param tuple5s the list of tuples from which to extract the lists
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @return {@link List}s in a {@link Tuple5} extracted from a {@link Stream} of {@link Tuple5}s
   */
  @NotNull
  public static <A, B, C, D, E>
  Tuple5<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>
      > unzip5(
      @NotNull Stream<Tuple5<A, B, C, D, E>> tuple5s
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    tuple5s.forEachOrdered(element -> {
      listA.add(element._1());
      listB.add(element._2());
      listC.add(element._3());
      listD.add(element._4());
      listE.add(element._5());
    });
    if (!listA.isEmpty()) {

      return new Tuple5<>(
          Collections.unmodifiableList(listA),
          Collections.unmodifiableList(listB),
          Collections.unmodifiableList(listC),
          Collections.unmodifiableList(listD),
          Collections.unmodifiableList(listE));
    }

    return new Tuple5<>(
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of());
  }

  /**
   * Return {@link List}s in a {@link Tuple5} extracted from a {@link Stream} of {@link Tuple5}s filtered by a the
   * {@code fMapper} function.
   *
   * @param tuple5s the list of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the collection and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @return {@link List}s in a {@link Tuple5} extracted from a {@link Stream} of {@link Tuple5}s filtered by a the
   *     {@code fMapper} function
   */
  @NotNull
  public static <A, B, C, D, E>
  Tuple5<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>
      > unzip5AndFlatten(
      @NotNull Stream<Tuple5<A, B, C, D, E>> tuple5s,
      @NotNull Function<
          Tuple5<A, B, C, D, E>,
          Optional<
              Tuple5<
                  Optional<A>,
                  Optional<B>,
                  Optional<C>,
                  Optional<D>,
                  Optional<E>>>
          > fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    tuple5s.forEachOrdered(element ->
        fMapper.apply(element)
            .ifPresent(optionalOfOptionals -> {
              optionalOfOptionals._1()
                  .ifPresent(listA::add);
              optionalOfOptionals._2()
                  .ifPresent(listB::add);
              optionalOfOptionals._3()
                  .ifPresent(listC::add);
              optionalOfOptionals._4()
                  .ifPresent(listD::add);
              optionalOfOptionals._5()
                  .ifPresent(listE::add);
            }));

    return new Tuple5<>(
        listA.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listA),
        listB.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listB),
        listC.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listC),
        listD.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listD),
        listE.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listE));
  }

  /**
   * Returns {@link List}s in a {@link Tuple6} extracted from a {@link Stream} of {@link Tuple6}s.
   *
   * @param tuple6s the list of tuples from which to extract the lists
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @param <F>     the type of instances contained within the sixth element of each tuple
   * @return {@link List}s in a {@link Tuple6} extracted from a {@link Stream} of {@link Tuple6}s
   */
  @NotNull
  public static <A, B, C, D, E, F>
  Tuple6<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>,
      List<F>
      > unzip6(
      @NotNull Stream<Tuple6<A, B, C, D, E, F>> tuple6s
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    tuple6s.forEachOrdered(element -> {
      listA.add(element._1());
      listB.add(element._2());
      listC.add(element._3());
      listD.add(element._4());
      listE.add(element._5());
      listF.add(element._6());
    });
    if (!listA.isEmpty()) {

      return new Tuple6<>(
          Collections.unmodifiableList(listA),
          Collections.unmodifiableList(listB),
          Collections.unmodifiableList(listC),
          Collections.unmodifiableList(listD),
          Collections.unmodifiableList(listE),
          Collections.unmodifiableList(listF));
    }

    return new Tuple6<>(
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of());
  }

  /**
   * Return {@link List}s in a {@link Tuple6} extracted from a {@link Stream} of {@link Tuple6}s filtered by a the
   * {@code fMapper} function.
   *
   * @param tuple6s the list of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the collection and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @param <F>     the type of instances contained within the sixth element of each tuple
   * @return {@link List}s in a {@link Tuple6} extracted from a {@link Stream} of {@link Tuple6}s filtered by a the
   *     {@code fMapper} function
   */
  @NotNull
  public static <A, B, C, D, E, F>
  Tuple6<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>,
      List<F>
      > unzip6AndFlatten(
      @NotNull Stream<Tuple6<A, B, C, D, E, F>> tuple6s,
      @NotNull Function<
          Tuple6<A, B, C, D, E, F>,
          Optional<
              Tuple6<
                  Optional<A>,
                  Optional<B>,
                  Optional<C>,
                  Optional<D>,
                  Optional<E>,
                  Optional<F>>>
          > fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    tuple6s.forEachOrdered(element ->
        fMapper.apply(element)
            .ifPresent(optionalOfOptionals -> {
              optionalOfOptionals._1()
                  .ifPresent(listA::add);
              optionalOfOptionals._2()
                  .ifPresent(listB::add);
              optionalOfOptionals._3()
                  .ifPresent(listC::add);
              optionalOfOptionals._4()
                  .ifPresent(listD::add);
              optionalOfOptionals._5()
                  .ifPresent(listE::add);
              optionalOfOptionals._6()
                  .ifPresent(listF::add);
            }));

    return new Tuple6<>(
        listA.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listA),
        listB.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listB),
        listC.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listC),
        listD.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listD),
        listE.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listE),
        listF.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listF));
  }

  /**
   * Returns {@link List}s in a {@link Tuple7} extracted from a {@link Stream} of {@link Tuple7}s.
   *
   * @param tuple7s the list of tuples from which to extract the lists
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @param <F>     the type of instances contained within the sixth element of each tuple
   * @param <G>     the type of instances contained within the seventh element of each tuple
   * @return {@link List}s in a {@link Tuple7} extracted from a {@link Stream} of {@link Tuple7}s
   */
  @NotNull
  public static <A, B, C, D, E, F, G>
  Tuple7<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>,
      List<F>,
      List<G>
      > unzip7(
      @NotNull Stream<Tuple7<A, B, C, D, E, F, G>> tuple7s
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    tuple7s.forEachOrdered(element -> {
      listA.add(element._1());
      listB.add(element._2());
      listC.add(element._3());
      listD.add(element._4());
      listE.add(element._5());
      listF.add(element._6());
      listG.add(element._7());
    });
    if (!listA.isEmpty()) {

      return new Tuple7<>(
          Collections.unmodifiableList(listA),
          Collections.unmodifiableList(listB),
          Collections.unmodifiableList(listC),
          Collections.unmodifiableList(listD),
          Collections.unmodifiableList(listE),
          Collections.unmodifiableList(listF),
          Collections.unmodifiableList(listG));
    }

    return new Tuple7<>(
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of());
  }

  /**
   * Return {@link List}s in a {@link Tuple7} extracted from a {@link Stream} of {@link Tuple7}s filtered by a the
   * {@code fMapper} function.
   *
   * @param tuple7s the list of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the collection and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @param <F>     the type of instances contained within the sixth element of each tuple
   * @param <G>     the type of instances contained within the seventh element of each tuple
   * @return {@link List}s in a {@link Tuple7} extracted from a {@link Stream} of {@link Tuple7}s filtered by a the
   *     {@code fMapper} function
   */
  @NotNull
  public static <A, B, C, D, E, F, G>
  Tuple7<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>,
      List<F>,
      List<G>
      > unzip7AndFlatten(
      @NotNull Stream<Tuple7<A, B, C, D, E, F, G>> tuple7s,
      @NotNull Function<
          Tuple7<A, B, C, D, E, F, G>,
          Optional<
              Tuple7<
                  Optional<A>,
                  Optional<B>,
                  Optional<C>,
                  Optional<D>,
                  Optional<E>,
                  Optional<F>,
                  Optional<G>>>
          > fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    tuple7s.forEachOrdered(element ->
        fMapper.apply(element)
            .ifPresent(optionalOfOptionals -> {
              optionalOfOptionals._1()
                  .ifPresent(listA::add);
              optionalOfOptionals._2()
                  .ifPresent(listB::add);
              optionalOfOptionals._3()
                  .ifPresent(listC::add);
              optionalOfOptionals._4()
                  .ifPresent(listD::add);
              optionalOfOptionals._5()
                  .ifPresent(listE::add);
              optionalOfOptionals._6()
                  .ifPresent(listF::add);
              optionalOfOptionals._7()
                  .ifPresent(listG::add);
            }));

    return new Tuple7<>(
        listA.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listA),
        listB.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listB),
        listC.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listC),
        listD.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listD),
        listE.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listE),
        listF.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listF),
        listG.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listG));
  }

  /**
   * Returns {@link List}s in a {@link Tuple8} extracted from a {@link Stream} of {@link Tuple8}s.
   *
   * @param tuple8s the list of tuples from which to extract the lists
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @param <F>     the type of instances contained within the sixth element of each tuple
   * @param <G>     the type of instances contained within the seventh element of each tuple
   * @param <H>     the type of instances contained within the eighth element of each tuple
   * @return {@link List}s in a {@link Tuple8} extracted from a {@link Stream} of {@link Tuple8}s
   */
  @NotNull
  public static <A, B, C, D, E, F, G, H>
  Tuple8<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>,
      List<F>,
      List<G>,
      List<H>
      > unzip8(
      @NotNull Stream<Tuple8<A, B, C, D, E, F, G, H>> tuple8s
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    var listH = new ArrayList<H>();
    tuple8s.forEachOrdered(element -> {
      listA.add(element._1());
      listB.add(element._2());
      listC.add(element._3());
      listD.add(element._4());
      listE.add(element._5());
      listF.add(element._6());
      listG.add(element._7());
      listH.add(element._8());
    });
    if (!listA.isEmpty()) {

      return new Tuple8<>(
          Collections.unmodifiableList(listA),
          Collections.unmodifiableList(listB),
          Collections.unmodifiableList(listC),
          Collections.unmodifiableList(listD),
          Collections.unmodifiableList(listE),
          Collections.unmodifiableList(listF),
          Collections.unmodifiableList(listG),
          Collections.unmodifiableList(listH));
    }

    return new Tuple8<>(
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of());
  }

  /**
   * Return {@link List}s in a {@link Tuple8} extracted from a {@link Stream} of {@link Tuple8}s filtered by a the
   * {@code fMapper} function.
   *
   * @param tuple8s the list of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the collection and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @param <F>     the type of instances contained within the sixth element of each tuple
   * @param <G>     the type of instances contained within the seventh element of each tuple
   * @param <H>     the type of instances contained within the eighth element of each tuple
   * @return {@link List}s in a {@link Tuple8} extracted from a {@link Stream} of {@link Tuple8}s filtered by a the
   *     {@code fMapper} function
   */
  @NotNull
  public static <A, B, C, D, E, F, G, H>
  Tuple8<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>,
      List<F>,
      List<G>,
      List<H>
      > unzip8AndFlatten(
      @NotNull Stream<Tuple8<A, B, C, D, E, F, G, H>> tuple8s,
      @NotNull Function<
          Tuple8<A, B, C, D, E, F, G, H>,
          Optional<
              Tuple8<
                  Optional<A>,
                  Optional<B>,
                  Optional<C>,
                  Optional<D>,
                  Optional<E>,
                  Optional<F>,
                  Optional<G>,
                  Optional<H>>>
          > fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    var listH = new ArrayList<H>();
    tuple8s.forEachOrdered(element ->
        fMapper.apply(element)
            .ifPresent(optionalOfOptionals -> {
              optionalOfOptionals._1()
                  .ifPresent(listA::add);
              optionalOfOptionals._2()
                  .ifPresent(listB::add);
              optionalOfOptionals._3()
                  .ifPresent(listC::add);
              optionalOfOptionals._4()
                  .ifPresent(listD::add);
              optionalOfOptionals._5()
                  .ifPresent(listE::add);
              optionalOfOptionals._6()
                  .ifPresent(listF::add);
              optionalOfOptionals._7()
                  .ifPresent(listG::add);
              optionalOfOptionals._8()
                  .ifPresent(listH::add);
            }));

    return new Tuple8<>(
        listA.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listA),
        listB.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listB),
        listC.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listC),
        listD.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listD),
        listE.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listE),
        listF.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listF),
        listG.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listG),
        listH.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listH));
  }

  /**
   * Returns {@link List}s in a {@link Tuple9} extracted from a {@link Stream} of {@link Tuple9}s.
   *
   * @param tuple9s the list of tuples from which to extract the lists
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @param <F>     the type of instances contained within the sixth element of each tuple
   * @param <G>     the type of instances contained within the seventh element of each tuple
   * @param <H>     the type of instances contained within the eighth element of each tuple
   * @param <I>     the type of instances contained within the ninth element of each tuple
   * @return {@link List}s in a {@link Tuple9} extracted from a {@link Stream} of {@link Tuple9}s
   */
  @NotNull
  public static <A, B, C, D, E, F, G, H, I>
  Tuple9<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>,
      List<F>,
      List<G>,
      List<H>,
      List<I>
      > unzip9(
      @NotNull Stream<Tuple9<A, B, C, D, E, F, G, H, I>> tuple9s
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    var listH = new ArrayList<H>();
    var listI = new ArrayList<I>();
    tuple9s.forEachOrdered(element -> {
      listA.add(element._1());
      listB.add(element._2());
      listC.add(element._3());
      listD.add(element._4());
      listE.add(element._5());
      listF.add(element._6());
      listG.add(element._7());
      listH.add(element._8());
      listI.add(element._9());
    });
    if (!listA.isEmpty()) {

      return new Tuple9<>(
          Collections.unmodifiableList(listA),
          Collections.unmodifiableList(listB),
          Collections.unmodifiableList(listC),
          Collections.unmodifiableList(listD),
          Collections.unmodifiableList(listE),
          Collections.unmodifiableList(listF),
          Collections.unmodifiableList(listG),
          Collections.unmodifiableList(listH),
          Collections.unmodifiableList(listI));
    }

    return new Tuple9<>(
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of());
  }

  /**
   * Return {@link List}s in a {@link Tuple9} extracted from a {@link Stream} of {@link Tuple9}s filtered by a the
   * {@code fMapper} function.
   *
   * @param tuple9s the list of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the collection and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @param <F>     the type of instances contained within the sixth element of each tuple
   * @param <G>     the type of instances contained within the seventh element of each tuple
   * @param <H>     the type of instances contained within the eighth element of each tuple
   * @param <I>     the type of instances contained within the ninth element of each tuple
   * @return {@link List}s in a {@link Tuple9} extracted from a {@link Stream} of {@link Tuple9}s filtered by a the
   *     {@code fMapper} function
   */
  @NotNull
  public static <A, B, C, D, E, F, G, H, I>
  Tuple9<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>,
      List<F>,
      List<G>,
      List<H>,
      List<I>
      > unzip9AndFlatten(
      @NotNull Stream<Tuple9<A, B, C, D, E, F, G, H, I>> tuple9s,
      @NotNull Function<
          Tuple9<A, B, C, D, E, F, G, H, I>,
          Optional<
              Tuple9<
                  Optional<A>,
                  Optional<B>,
                  Optional<C>,
                  Optional<D>,
                  Optional<E>,
                  Optional<F>,
                  Optional<G>,
                  Optional<H>,
                  Optional<I>>>
          > fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    var listH = new ArrayList<H>();
    var listI = new ArrayList<I>();
    tuple9s.forEachOrdered(element ->
        fMapper.apply(element)
            .ifPresent(optionalOfOptionals -> {
              optionalOfOptionals._1()
                  .ifPresent(listA::add);
              optionalOfOptionals._2()
                  .ifPresent(listB::add);
              optionalOfOptionals._3()
                  .ifPresent(listC::add);
              optionalOfOptionals._4()
                  .ifPresent(listD::add);
              optionalOfOptionals._5()
                  .ifPresent(listE::add);
              optionalOfOptionals._6()
                  .ifPresent(listF::add);
              optionalOfOptionals._7()
                  .ifPresent(listG::add);
              optionalOfOptionals._8()
                  .ifPresent(listH::add);
              optionalOfOptionals._9()
                  .ifPresent(listI::add);
            }));

    return new Tuple9<>(
        listA.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listA),
        listB.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listB),
        listC.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listC),
        listD.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listD),
        listE.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listE),
        listF.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listF),
        listG.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listG),
        listH.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listH),
        listI.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listI));
  }

  /**
   * Returns {@link List}s in a {@link Tuple10} extracted from a {@link Stream} of {@link Tuple10}s.
   *
   * @param tuple10s the list of tuples from which to extract the lists
   * @param <A>      the type of instances contained within the first element of each tuple
   * @param <B>      the type of instances contained within the second element of each tuple
   * @param <C>      the type of instances contained within the third element of each tuple
   * @param <D>      the type of instances contained within the fourth element of each tuple
   * @param <E>      the type of instances contained within the fifth element of each tuple
   * @param <F>      the type of instances contained within the sixth element of each tuple
   * @param <G>      the type of instances contained within the seventh element of each tuple
   * @param <H>      the type of instances contained within the eighth element of each tuple
   * @param <I>      the type of instances contained within the ninth element of each tuple
   * @param <J>      the type of instances contained within the tenth element of each tuple
   * @return {@link List}s in a {@link Tuple10} extracted from a {@link Stream} of {@link Tuple10}s
   */
  @NotNull
  public static <A, B, C, D, E, F, G, H, I, J>
  Tuple10<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>,
      List<F>,
      List<G>,
      List<H>,
      List<I>,
      List<J>
      > unzip10(
      @NotNull Stream<Tuple10<A, B, C, D, E, F, G, H, I, J>> tuple10s
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    var listH = new ArrayList<H>();
    var listI = new ArrayList<I>();
    var listJ = new ArrayList<J>();
    tuple10s.forEachOrdered(element -> {
      listA.add(element._1());
      listB.add(element._2());
      listC.add(element._3());
      listD.add(element._4());
      listE.add(element._5());
      listF.add(element._6());
      listG.add(element._7());
      listH.add(element._8());
      listI.add(element._9());
      listJ.add(element._10());
    });
    if (!listA.isEmpty()) {

      return new Tuple10<>(
          Collections.unmodifiableList(listA),
          Collections.unmodifiableList(listB),
          Collections.unmodifiableList(listC),
          Collections.unmodifiableList(listD),
          Collections.unmodifiableList(listE),
          Collections.unmodifiableList(listF),
          Collections.unmodifiableList(listG),
          Collections.unmodifiableList(listH),
          Collections.unmodifiableList(listI),
          Collections.unmodifiableList(listJ));
    }

    return new Tuple10<>(
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of(),
        List.of());
  }

  /**
   * Return {@link List}s in a {@link Tuple10} extracted from a {@link Stream} of {@link Tuple10}s filtered by a the
   * {@code fMapper} function.
   *
   * @param tuple10s the list of tuples from which to extract the lists
   * @param fMapper  the function to use to extract the values from an element of the collection and optionally return a
   *                 value, and when non-empty, optionally return each of the element values
   * @param <A>      the type of instances contained within the first element of each tuple
   * @param <B>      the type of instances contained within the second element of each tuple
   * @param <C>      the type of instances contained within the third element of each tuple
   * @param <D>      the type of instances contained within the fourth element of each tuple
   * @param <E>      the type of instances contained within the fifth element of each tuple
   * @param <F>      the type of instances contained within the sixth element of each tuple
   * @param <G>      the type of instances contained within the seventh element of each tuple
   * @param <H>      the type of instances contained within the eighth element of each tuple
   * @param <I>      the type of instances contained within the ninth element of each tuple
   * @param <J>      the type of instances contained within the tenth element of each tuple
   * @return {@link List}s in a {@link Tuple10} extracted from a {@link Stream} of {@link Tuple10}s filtered by a the
   *     {@code fMapper} function
   */
  @NotNull
  public static <A, B, C, D, E, F, G, H, I, J>
  Tuple10<
      List<A>,
      List<B>,
      List<C>,
      List<D>,
      List<E>,
      List<F>,
      List<G>,
      List<H>,
      List<I>,
      List<J>
      > unzip10AndFlatten(
      @NotNull Stream<Tuple10<A, B, C, D, E, F, G, H, I, J>> tuple10s,
      @NotNull Function<
          Tuple10<A, B, C, D, E, F, G, H, I, J>,
          Optional<
              Tuple10<
                  Optional<A>,
                  Optional<B>,
                  Optional<C>,
                  Optional<D>,
                  Optional<E>,
                  Optional<F>,
                  Optional<G>,
                  Optional<H>,
                  Optional<I>,
                  Optional<J>>>
          > fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    var listH = new ArrayList<H>();
    var listI = new ArrayList<I>();
    var listJ = new ArrayList<J>();
    tuple10s.forEachOrdered(element ->
        fMapper.apply(element)
            .ifPresent(optionalOfOptionals -> {
              optionalOfOptionals._1()
                  .ifPresent(listA::add);
              optionalOfOptionals._2()
                  .ifPresent(listB::add);
              optionalOfOptionals._3()
                  .ifPresent(listC::add);
              optionalOfOptionals._4()
                  .ifPresent(listD::add);
              optionalOfOptionals._5()
                  .ifPresent(listE::add);
              optionalOfOptionals._6()
                  .ifPresent(listF::add);
              optionalOfOptionals._7()
                  .ifPresent(listG::add);
              optionalOfOptionals._8()
                  .ifPresent(listH::add);
              optionalOfOptionals._9()
                  .ifPresent(listI::add);
              optionalOfOptionals._10()
                  .ifPresent(listJ::add);
            }));

    return new Tuple10<>(
        listA.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listA),
        listB.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listB),
        listC.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listC),
        listD.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listD),
        listE.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listE),
        listF.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listF),
        listG.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listG),
        listH.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listH),
        listI.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listI),
        listJ.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listJ));
  }
}
