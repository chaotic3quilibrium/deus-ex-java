package org.deus_ex_java.util;

import org.deus_ex_java.util.refined.NonEmptyList;
import org.deus_ex_java.util.tuple.*;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class providing static methods to create {@link List} instances.
 */
@SuppressWarnings({"RedundantCast", "ConstantValue", "Convert2Diamond"})
@NullMarked
public final class ListsOps {

  private ListsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns a {@link List}{@code <T>} backed by an empty and modifiable {@link ArrayList}{@code <T>}.
   * <p>
   * This enables specifying a function in the shape of {@code ListsOps::<T>newArrayList()} which returns the
   * <em>interface</em> type of {@link List}{@code <T>}, which is preferable over specifying {@code ArrayList::new}
   * which returns the <em>class specific implementation</em> type of {@link ArrayList}{@code <T>}.
   *
   * @param <T> the type of instances contained in the {@link List}
   * @return a {@link List}{@code <T>} backed by an empty and modifiable {@link ArrayList}{@code <T>}
   */
  public static <T> List<T> newArrayList() {
    return new ArrayList<T>();
  }

  /**
   * Returns a {@link List}{@code <T>} backed by an empty and modifiable {@link ArrayList}{@code <T>}.
   * <p>
   * This enables specifying a function in the shape of {@code () -> ListsOps.newArrayList(T.class)} which returns the
   * <em>interface</em> type of {@link List}{@code <T>}, which is preferable over specifying {@code ArrayList::new}
   * which returns the <em>class specific implementation</em> type of {@link ArrayList}{@code <T>}.
   *
   * @param clazz the class of the type
   * @param <T>   the type of instances contained in the {@link List}
   * @return a {@link List}{@code <T>} backed by an empty and modifiable {@link ArrayList}{@code <T>}
   */
  public static <T> List<T> newArrayList(Class<T> clazz) {
    Objects.requireNonNull(clazz);

    return Collections.checkedList(new ArrayList<>(), clazz);
  }

  /**
   * Returns a {@link List}{@code <T>} backed by an empty and modifiable {@link LinkedList}{@code <T>}.
   * <p>
   * This enables specifying a function in the shape of {@code ListsOps::<T>newLinkedList} which returns the
   * <em>interface</em> type of {@link List}{@code <T>}, which is preferable over specifying {@code LinkedList::new}
   * which returns the <em>class specific implementation</em> type of {@link LinkedList}{@code <T>}.
   *
   * @param <T> the type of instances contained in the {@link List}
   * @return a {@link List}{@code <T>} backed by an empty and modifiable {@link LinkedList}{@code <T>}
   */
  public static <T> List<T> newLinkedList() {
    return new LinkedList<T>();
  }

  /**
   * Returns a {@link List}{@code <T>} backed by an empty and modifiable {@link LinkedList}{@code <T>}.
   * <p>
   * This enables specifying a function in the shape of {@code () -> ListsOps.newLinkedList(T.class)} which returns the
   * <em>interface</em> type of {@link List}{@code <T>}, which is preferable over specifying {@code LinkedList::new}
   * which returns the <em>class specific implementation</em> type of {@link LinkedList}{@code <T>}.
   *
   * @param clazz the class of the type
   * @param <T>   the type of instances contained in the {@link List}
   * @return a {@link List}{@code <T>} backed by an empty and modifiable {@link LinkedList}{@code <T>}
   */
  public static <T> List<T> newLinkedList(Class<T> clazz) {
    Objects.requireNonNull(clazz);

    return Collections.checkedList(new LinkedList<>(), clazz);
  }

  /**
   * Returns an empty {@link List} using {@link List#of}, if {@code ts} is {@code null}, otherwise returns {@code ts}.
   * <p>
   * Note: If the requirement is to both avoid a {@code null} value, and an empty {@link List}, the refined class of
   * {@link NonEmptyList} enable <i>compile-time enforcement</i> of said contract requirements.
   *
   * @param ts  possibly {@code null} {@link List} to reify to make {@code null} safe
   * @param <T> the type of instances contained in the source
   * @return an empty {@link List} using {@link List#of}, if {@code ts} is {@code null}, otherwise returns {@code ts}
   */
  public static <T> List<T> nullToEmpty(@Nullable List<T> ts) {
    return ts != null
        ? ts
        : List.of();
  }

  /**
   * Returns an occupied {@link Optional} containing a {@link NonEmptyList} if {@code ts.isEmpty} is {@code false},
   * otherwise an {@link Optional#empty()}.
   *
   * @param ts  the possibly {@code null} or empty source to wrap
   * @param <T> the type of instances contained in the source
   * @return an occupied {@link Optional} containing a {@link NonEmptyList} if {@code ts.isEmpty} is {@code false},
   *     otherwise an {@link Optional#empty()}
   */
  public static <T> Optional<NonEmptyList<T>> toNonEmpty(@Nullable List<T> ts) {
    return (ts != null)
        ? NonEmptyList.wrap(ts).toOptional()
        : Optional.empty();
  }

  /**
   * Returns an unmodifiable {@link List} with either the {@code value} appended if it is non-null, or an unmodifiable
   * copy of the original {@code list}.
   *
   * @param list  the source from which the copy is made
   * @param value the value to append to the copy of the list
   * @param <T>   the type of instances contained in the list
   * @return an  unmodifiable {@link List} with either the {@code value} appended if it is non-null, or an unmodifiable
   *     copy of the original {@code list}
   */
  @SuppressWarnings("RedundantCast")
  public static <T> List<T> appendItem(
      List<T> list,
      @Nullable T value
  ) {
    Objects.requireNonNull(list);

    return Stream.concat(list.stream(), (Stream<T>) Stream.ofNullable(value))
        .toList();
  }

  /**
   * Returns an unmodifiable {@link List} consisting of each filtered of {@code null}s list from lists appended in
   * iteration order.
   *
   * @param lists the lists to append
   * @param <T>   the type of instances contained within all the lists
   * @return an unmodifiable {@link List} consisting of each filtered of {@code null}s list from lists appended in
   *     iteration order
   */
  @SuppressWarnings("ConstantValue")
  @SafeVarargs
  public static <T> List<T> appendLists(List<T>... lists) {
    Objects.requireNonNull(lists);

    if (lists.length == 0) {
      return List.of();
    }
    return Arrays.stream(lists)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .filter(Objects::nonNull)
        .toList();
  }

  /**
   * Returns an unmodifiable {@link List} with the first encountered {@code value} removed if it is non-null, or an
   * unmodifiable copy of the original {@code list}.
   *
   * @param list  the source from which the copy is made
   * @param value the value to remove from the copy of the list
   * @param <T>   the type of instances contained in the list
   * @return an unmodifiable {@link List} with the first encountered {@code value} removed if it is non-null, or an
   *     unmodifiable copy of the original {@code list}
   */
  public static <T> List<T> removeItem(
      List<T> list,
      @Nullable T value
  ) {
    Objects.requireNonNull(list);
    if (value == null || list.isEmpty()) {
      return list.isEmpty()
          ? List.of()
          : List.copyOf(list);
    }

    int index = list.indexOf(value);
    if (index < 0) {
      return List.copyOf(list);
    }

    return IntStream.range(0, list.size())
        .filter(i -> i != index)
        .mapToObj(list::get)
        .toList();
  }

  /**
   * Returns an unmodifiable {@link List} with all instances equal to the {@code value} removed if it is non-null, or an
   * unmodifiable copy of the original {@code list}.
   *
   * @param list  the source from which the copy is made
   * @param value the value to eliminate from the copy of the list
   * @param <T>   the type of instances contained in the list
   * @return an unmodifiable {@link List} with all instances equal to the {@code value} removed if it is non-null, or an
   *     unmodifiable copy of the original {@code list}
   */
  public static <T> List<T> eliminateItem(
      List<T> list,
      @Nullable T value
  ) {
    Objects.requireNonNull(list);
    if (value == null || list.isEmpty()) {
      return list.isEmpty()
          ? List.of()
          : List.copyOf(list);
    }

    return list.stream()
        .filter(t -> !value.equals(t))
        .toList();
  }

  /**
   * Returns an unmodifiable {@link List} consisting of the elements from the original {@code list} with the first
   * encountered instance of each element contained within the {@code collection} removed.
   *
   * @param list       the source from which the copy is made
   * @param collection the collection containing the elements to remove from the copy of the list
   * @param <T>        the type of instances contained in the list and collection
   * @return an unmodifiable {@link List} consisting of the elements from the original {@code list} with the first
   *     encountered instance of each element contained within the {@code collection} removed
   */
  public static <T> List<T> removeAll(
      List<T> list,
      Collection<T> collection
  ) {
    Objects.requireNonNull(list);
    Objects.requireNonNull(collection);

    return removeAll(
        list,
        collection.stream());
  }

  /**
   * Returns an unmodifiable {@link List} consisting of the elements from the original {@code list} with the first
   * encountered instance of each element contained within the {@code stream} removed.
   *
   * @param list   the source from which the copy is made
   * @param stream the stream containing the elements to remove from the copy of the list
   * @param <T>    the type of instances contained in the list and stream
   * @return an unmodifiable {@link List} consisting of the elements from the original {@code list} with the first
   *     encountered instance of each element contained within the {@code stream} removed
   */
  @SuppressWarnings("ConstantValue")
  public static <T> List<T> removeAll(
      List<T> list,
      Stream<T> stream
  ) {
    Objects.requireNonNull(list);
    Objects.requireNonNull(stream);
    if (list.isEmpty()) {
      return List.of();
    }

    var removalCounts = stream
        .filter(Objects::nonNull)
        .collect(Collectors.groupingBy(Function.identity(), HashMap::new, Collectors.summingInt(x -> 1)));

    if (removalCounts.isEmpty()) {
      return List.copyOf(list);
    }

    return list.stream()
        .filter(item -> {
          var count = removalCounts.get(item);
          if (count != null && count > 0) {
            removalCounts.put(item, count - 1);
            return false;
          }
          return true;
        })
        .toList();
  }

  /**
   * Returns an unmodifiable {@link List} consisting of the elements from the original {@code list} with all instances
   * equal to any element contained within the {@code collection} removed.
   *
   * @param list       the source from which the copy is made
   * @param collection the collection containing the elements to eliminate from the copy of the list
   * @param <T>        the type of instances contained in the list and collection
   * @return an unmodifiable {@link List} consisting of the elements from the original {@code list} with all instances
   *     equal to any element contained within the {@code collection} removed
   */
  public static <T> List<T> eliminateAll(
      List<T> list,
      Collection<T> collection
  ) {
    Objects.requireNonNull(list);
    Objects.requireNonNull(collection);

    return eliminateAll(
        list,
        collection.stream());
  }

  /**
   * Returns an unmodifiable {@link List} consisting of the elements from the original {@code list} with all instances
   * equal to any element contained within the {@code stream} removed.
   *
   * @param list   the source from which the copy is made
   * @param stream the stream containing the elements to eliminate from the copy of the list
   * @param <T>    the type of instances contained in the list and stream
   * @return an unmodifiable {@link List} consisting of the elements from the original {@code list} with all instances
   *     equal to any element contained within the {@code stream} removed
   */
  public static <T> List<T> eliminateAll(
      List<T> list,
      Stream<T> stream
  ) {
    Objects.requireNonNull(list);
    Objects.requireNonNull(stream);
    if (!list.isEmpty()) {
      var removalsAsSet = stream.collect(Collectors.toUnmodifiableSet());
      if (!removalsAsSet.isEmpty()) {

        return list
            .stream()
            .filter(t -> !removalsAsSet.contains(t))
            .toList();
      }

      return List.copyOf(list);
    }

    return List.of();
  }

  /**
   * Returns an unmodifiable {@link List} consisting of the elements from the original {@code list} with the first
   * encountered instance of each element contained within the {@code lists} removed.
   *
   * @param list  the source from which the copy is made
   * @param lists the lists containing the elements to remove from the copy of the list
   * @param <T>   the type of instances contained in the list and lists
   * @return an unmodifiable {@link List} consisting of the elements from the original {@code list} with the first
   *     encountered instance of each element contained within the {@code lists} removed
   */
  @SuppressWarnings("ConstantValue")
  @SafeVarargs
  public static <T> List<T> removeLists(
      List<T> list,
      List<T>... lists
  ) {
    Objects.requireNonNull(list);
    Objects.requireNonNull(lists);

    if (list.isEmpty()) {
      return List.of();
    }
    if (lists.length == 0) {
      return List.copyOf(list);
    }

    return removeAll(
        list,
        Arrays.stream(lists)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(Objects::nonNull));
  }

  /**
   * Returns an unmodifiable {@link List} consisting of the elements from the original {@code list} with all instances
   * equal to any element contained within the {@code lists} eliminated.
   *
   * @param list  the source from which the copy is made
   * @param lists the lists containing the elements to eliminate from the copy of the list
   * @param <T>   the type of instances contained in the list and lists
   * @return an unmodifiable {@link List} consisting of the elements from the original {@code list} with all instances
   *     equal to any element contained within the {@code lists} eliminated
   */
  @SuppressWarnings("ConstantValue")
  @SafeVarargs
  public static <T> List<T> eliminateLists(
      List<T> list,
      List<T>... lists
  ) {
    Objects.requireNonNull(list);
    Objects.requireNonNull(lists);

    if (list.isEmpty()) {
      return List.of();
    }
    if (lists.length == 0) {
      return List.copyOf(list);
    }

    return eliminateAll(
        list,
        Arrays.stream(lists)
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(Objects::nonNull));
  }

  /**
   * Returns an unmodifiable {@link List} filtered of {@code null}s.
   *
   * @param collection the source of the T elements
   * @param <T>        the type of the instances
   * @return an unmodifiable {@link List} filtered of {@code null}s
   */
  public static <T> List<T> nullSanitize(
      Collection<T> collection
  ) {
    return nullSanitize(collection.stream());
  }

  /**
   * Returns an unmodifiable {@link List} filtered of {@code null}s.
   *
   * @param stream the source of the T elements
   * @param <T>    the type of the instances
   * @return an unmodifiable {@link List} filtered of {@code null}s
   */
  public static <T> List<T> nullSanitize(
      Stream<@Nullable T> stream
  ) {
    return stream
        .filter(Objects::nonNull)
        .map(t ->
            (T) t)
        .toList();
  }

  /**
   * Returns the comparison value, after aligning unequally sized collections to their right sides, from a scan
   * performed from right to left (i.e. starting with the last element of each collection) and evaluating each pair of
   * elements via the {@link Comparable#compareTo(Object)} expression and returning upon encountering the first
   * non-{@code 0} result, otherwise a value less than {@code 0} if {@code tsLeft.size()} is less than
   * {@code tsRight.size()}, otherwise a value greater than {@code 0} if {@code tsLeft.size()} is greater than
   * {@code tsRight.size()}, otherwise {@code 0} because the collections are considered equivalent.
   *
   * @param tsLeft  the source of the left side elements
   * @param tsRight the source of the right side elements
   * @param <T>     the type of instances contained in both collections which implement the {@link Comparable}
   *                interface
   * @return the comparison value, after aligning unequally sized collections to their right sides, from a scan
   *     performed from right to left (i.e. starting with the last element of each collection) and evaluating each pair
   *     of elements via the {@link Comparable#compareTo(Object)} expression and returning upon encountering the first
   *     non-{@code 0} result, otherwise a value less than {@code 0} if {@code tsLeft.size()} is less than
   *     {@code tsRight.size()}, otherwise a value greater than {@code 0} if {@code tsLeft.size()} is greater than
   *     {@code tsRight.size()}, otherwise {@code 0} because the collections are considered equivalent
   */
  public static <T extends Comparable<T>> int compareAlignedRight(
      Collection<T> tsLeft,
      Collection<T> tsRight
  ) {

    return compareAlignedLeft(
        ListsOps.reverse(tsLeft.stream()),
        ListsOps.reverse(tsRight.stream()));
  }

  /**
   * Returns the comparison value, after aligning unequally sized collections to their right sides, from a scan
   * performed from right to left (i.e. starting with the last element of each collection) and evaluating each pair of
   * elements via the {@link Comparable#compareTo(Object)} expression and returning upon encountering the first
   * non-{@code 0} result, otherwise a value less than {@code 0} if {@code tsLeft.size()} is less than
   * {@code tsRight.size()}, otherwise a value greater than {@code 0} if {@code tsLeft.size()} is greater than
   * {@code tsRight.size()}, otherwise {@code 0} because the collections are considered equivalent.
   *
   * @param tsLeft  the source of the left side elements
   * @param tsRight the source of the right side elements
   * @param <T>     the type of instances contained in both collections which implement the {@link Comparable}
   *                interface
   * @return the comparison value, after aligning unequally sized collections to their right sides, from a scan
   *     performed from right to left (i.e. starting with the last element of each collection) and evaluating each pair
   *     of elements via the {@link Comparable#compareTo(Object)} expression and returning upon encountering the first
   *     non-{@code 0} result, otherwise a value less than {@code 0} if {@code tsLeft.size()} is less than
   *     {@code tsRight.size()}, otherwise a value greater than {@code 0} if {@code tsLeft.size()} is greater than
   *     {@code tsRight.size()}, otherwise {@code 0} because the collections are considered equivalent
   */
  public static <T extends Comparable<T>> int compareAlignedRight(
      Collection<T> tsLeft,
      Stream<T> tsRight
  ) {
    return compareAlignedRight(tsLeft, tsRight.toList());
  }

  /**
   * Returns the comparison value, after aligning unequally sized collections to their right sides, from a scan
   * performed from right to left (i.e. starting with the last element of each collection) and evaluating each pair of
   * elements via the {@link Comparable#compareTo(Object)} expression and returning upon encountering the first
   * non-{@code 0} result, otherwise a value less than {@code 0} if {@code tsLeft.size()} is less than
   * {@code tsRight.size()}, otherwise a value greater than {@code 0} if {@code tsLeft.size()} is greater than
   * {@code tsRight.size()}, otherwise {@code 0} because the collections are considered equivalent.
   *
   * @param tsLeft  the source of the left side elements
   * @param tsRight the source of the right side elements
   * @param <T>     the type of instances contained in both collections which implement the {@link Comparable}
   *                interface
   * @return the comparison value, after aligning unequally sized collections to their right sides, from a scan
   *     performed from right to left (i.e. starting with the last element of each collection) and evaluating each pair
   *     of elements via the {@link Comparable#compareTo(Object)} expression and returning upon encountering the first
   *     non-{@code 0} result, otherwise a value less than {@code 0} if {@code tsLeft.size()} is less than
   *     {@code tsRight.size()}, otherwise a value greater than {@code 0} if {@code tsLeft.size()} is greater than
   *     {@code tsRight.size()}, otherwise {@code 0} because the collections are considered equivalent
   */
  public static <T extends Comparable<T>> int compareAlignedRight(
      Stream<T> tsLeft,
      Collection<T> tsRight
  ) {
    return compareAlignedRight(tsLeft.toList(), tsRight);
  }

  /**
   * Returns the comparison value, after aligning unequally sized collections to their right sides, from a scan
   * performed from right to left (i.e. starting with the last element of each collection) and evaluating each pair of
   * elements via the {@link Comparable#compareTo(Object)} expression and returning upon encountering the first
   * non-{@code 0} result, otherwise a value less than {@code 0} if {@code tsLeft.size()} is less than
   * {@code tsRight.size()}, otherwise a value greater than {@code 0} if {@code tsLeft.size()} is greater than
   * {@code tsRight.size()}, otherwise {@code 0} because the collections are considered equivalent.
   *
   * @param tsLeft  the source of the left side elements
   * @param tsRight the source of the right side elements
   * @param <T>     the type of instances contained in both collections which implement the {@link Comparable}
   *                interface
   * @return the comparison value, after aligning unequally sized collections to their right sides, from a scan
   *     performed from right to left (i.e. starting with the last element of each collection) and evaluating each pair
   *     of elements via the {@link Comparable#compareTo(Object)} expression and returning upon encountering the first
   *     non-{@code 0} result, otherwise a value less than {@code 0} if {@code tsLeft.size()} is less than
   *     {@code tsRight.size()}, otherwise a value greater than {@code 0} if {@code tsLeft.size()} is greater than
   *     {@code tsRight.size()}, otherwise {@code 0} because the collections are considered equivalent
   */
  public static <T extends Comparable<T>> int compareAlignedRight(
      Stream<T> tsLeft,
      Stream<T> tsRight
  ) {
    return compareAlignedRight(tsLeft.toList(), tsRight.toList());
  }

  /**
   * Returns the comparison value from a scan performed from left to right (i.e. starting with the first element of each
   * collection) and evaluating each pair of elements via the {@link Comparable#compareTo(Object)} expression and
   * returning upon encountering the first non-{@code 0} result, otherwise a value less than {@code 0} if
   * {@code tsLeft.size()} is less than {@code tsRight.size()}, otherwise a value greater than {@code 0} if
   * {@code tsLeft.size()} is greater than {@code tsRight.size()}, otherwise {@code 0} because the collections are
   * considered equivalent.
   *
   * @param tsLeft  the source of the left side elements
   * @param tsRight the source of the right side elements
   * @param <T>     the type of instances contained in both collections which implement the {@link Comparable}
   *                interface
   * @return the comparison value from a scan performed from left to right (i.e. starting with the first element of each
   *     collection) and evaluating each pair of elements via the {@link Comparable#compareTo(Object)} expression and
   *     returning upon encountering the first non-{@code 0} result, otherwise a value less than {@code 0} if
   *     {@code tsLeft.size()} is less than {@code tsRight.size()}, otherwise a value greater than {@code 0} if
   *     {@code tsLeft.size()} is greater than {@code tsRight.size()}, otherwise {@code 0} because the collections are
   *     considered equivalent
   */
  public static <T extends Comparable<T>> int compareAlignedLeft(
      Collection<T> tsLeft,
      Collection<T> tsRight
  ) {
    var tsLeftIterator = tsLeft.iterator();
    var tsRightIterator = tsRight.iterator();
    while (tsLeftIterator.hasNext() && tsRightIterator.hasNext()) {
      var tLeft = tsLeftIterator.next();
      var tRight = tsRightIterator.next();
      int comparison = tLeft.compareTo(tRight);
      if (comparison != 0) {

        return comparison;
      }
    }

    //If the loop finishes, all elements matched up to the end of the possibly shorter iterator, and shorter is treated
    //  as "less than" longer
    return tsLeftIterator.hasNext()
        //itRight.hasNext() is necessarily false
        ? 1
        : tsRightIterator.hasNext()
            ? -1
            : 0;
  }

  /**
   * Returns the comparison value from a scan performed from left to right (i.e. starting with the first element of each
   * collection) and evaluating each pair of elements via the {@link Comparable#compareTo(Object)} expression and
   * returning upon encountering the first non-{@code 0} result, otherwise a value less than {@code 0} if
   * {@code tsLeft.size()} is less than {@code tsRight.size()}, otherwise a value greater than {@code 0} if
   * {@code tsLeft.size()} is greater than {@code tsRight.size()}, otherwise {@code 0} because the collections are
   * considered equivalent.
   *
   * @param tsLeft  the source of the left side elements
   * @param tsRight the source of the right side elements
   * @param <T>     the type of instances contained in both collections which implement the {@link Comparable}
   *                interface
   * @return the comparison value from a scan performed from left to right (i.e. starting with the first element of each
   *     collection) and evaluating each pair of elements via the {@link Comparable#compareTo(Object)} expression and
   *     returning upon encountering the first non-{@code 0} result, otherwise a value less than {@code 0} if
   *     {@code tsLeft.size()} is less than {@code tsRight.size()}, otherwise a value greater than {@code 0} if
   *     {@code tsLeft.size()} is greater than {@code tsRight.size()}, otherwise {@code 0} because the collections are
   *     considered equivalent
   */
  public static <T extends Comparable<T>> int compareAlignedLeft(
      Collection<T> tsLeft,
      Stream<T> tsRight
  ) {
    return compareAlignedLeft(tsLeft, tsRight.toList());
  }

  /**
   * Returns the comparison value from a scan performed from left to right (i.e. starting with the first element of each
   * collection) and evaluating each pair of elements via the {@link Comparable#compareTo(Object)} expression and
   * returning upon encountering the first non-{@code 0} result, otherwise a value less than {@code 0} if
   * {@code tsLeft.size()} is less than {@code tsRight.size()}, otherwise a value greater than {@code 0} if
   * {@code tsLeft.size()} is greater than {@code tsRight.size()}, otherwise {@code 0} because the collections are
   * considered equivalent.
   *
   * @param tsLeft  the source of the left side elements
   * @param tsRight the source of the right side elements
   * @param <T>     the type of instances contained in both collections which implement the {@link Comparable}
   *                interface
   * @return the comparison value from a scan performed from left to right (i.e. starting with the first element of each
   *     collection) and evaluating each pair of elements via the {@link Comparable#compareTo(Object)} expression and
   *     returning upon encountering the first non-{@code 0} result, otherwise a value less than {@code 0} if
   *     {@code tsLeft.size()} is less than {@code tsRight.size()}, otherwise a value greater than {@code 0} if
   *     {@code tsLeft.size()} is greater than {@code tsRight.size()}, otherwise {@code 0} because the collections are
   *     considered equivalent
   */
  public static <T extends Comparable<T>> int compareAlignedLeft(
      Stream<T> tsLeft,
      Collection<T> tsRight
  ) {
    return compareAlignedLeft(tsLeft.toList(), tsRight);
  }

  /**
   * Returns the comparison value from a scan performed from left to right (i.e. starting with the first element of each
   * collection) and evaluating each pair of elements via the {@link Comparable#compareTo(Object)} expression and
   * returning upon encountering the first non-{@code 0} result, otherwise a value less than {@code 0} if
   * {@code tsLeft.size()} is less than {@code tsRight.size()}, otherwise a value greater than {@code 0} if
   * {@code tsLeft.size()} is greater than {@code tsRight.size()}, otherwise {@code 0} because the collections are
   * considered equivalent.
   *
   * @param tsLeft  the source of the left side elements
   * @param tsRight the source of the right side elements
   * @param <T>     the type of instances contained in both collections which implement the {@link Comparable}
   *                interface
   * @return the comparison value from a scan performed from left to right (i.e. starting with the first element of each
   *     collection) and evaluating each pair of elements via the {@link Comparable#compareTo(Object)} expression and
   *     returning upon encountering the first non-{@code 0} result, otherwise a value less than {@code 0} if
   *     {@code tsLeft.size()} is less than {@code tsRight.size()}, otherwise a value greater than {@code 0} if
   *     {@code tsLeft.size()} is greater than {@code tsRight.size()}, otherwise {@code 0} because the collections are
   *     considered equivalent
   */
  public static <T extends Comparable<T>> int compareAlignedLeft(
      Stream<T> tsLeft,
      Stream<T> tsRight
  ) {
    return compareAlignedLeft(tsLeft.toList(), tsRight.toList());
  }

  /**
   * Returns an unmodifiable {@link List} from a collection of {@code ts} filtering out {@code null}s, and then sorted.
   *
   * @param collection the source of the {@code T} values
   * @return an unmodifiable {@link List} from a collection of {@code ts} filtering out {@code null}s, and then sorted
   */
  public static <T extends Comparable<T>> List<T> toDistinctSortedList(
      Collection<T> collection
  ) {
    return toDistinctSortedList(collection.stream());
  }

  /**
   * Returns an unmodifiable {@link List} from a collection of {@code ts} filtering out {@code null}s, and then sorted.
   *
   * @param stream the source of the {@code T} values
   * @return an unmodifiable {@link List} from a collection of {@code ts} filtering out {@code null}s, and then sorted
   */
  public static <T extends Comparable<T>> List<T> toDistinctSortedList(
      Stream<T> stream
  ) {
    return toDistinctSortedList(stream, Function.identity());
  }

  /**
   * Returns an unmodifiable {@link List} from a collection of {@code ts} filtering out {@code null}s, deriving the
   * {@code R} value via the function {@code fTToR}, and then sorted.
   *
   * @param collection the source of the {@code T} values
   * @param fTToR      the function to use to extract the {@code R} value from an {@code T} element
   * @param <T>        the type of source instances
   * @param <R>        the type of target instances, which must extend {@link Comparable}
   * @return an unmodifiable {@link List} from a collection of {@code ts} filtering out {@code null}s, and then deriving
   *     the {@code R} value via the function {@code fTToR}
   */
  public static <T, R extends Comparable<R>> List<R> toDistinctSortedList(
      Collection<T> collection,
      Function<T, R> fTToR
  ) {
    return toDistinctSortedList(collection.stream(), fTToR);
  }

  /**
   * Returns an unmodifiable {@link List} from a collection of {@code ts} filtering out {@code null}s, deriving the
   * {@code R} value via the function {@code fTToR}, and then sorted.
   *
   * @param stream the source of the {@code T} values
   * @param fTToR  the function to use to extract the {@code R} value from an {@code T} element
   * @param <T>    the type of source instances
   * @param <R>    the type of target instances, which must extend {@link Comparable}
   * @return an unmodifiable {@link List} from a collection of {@code ts} filtering out {@code null}s, deriving the
   *     {@code R} value via the function {@code fTToR}, and then sorted
   */
  public static <T, R extends Comparable<R>> List<R> toDistinctSortedList(
      Stream<@Nullable T> stream,
      Function<T, R> fTToR
  ) {
    return stream
        .filter(Objects::nonNull)
        .map(fTToR)
        .distinct()
        .sorted()
        .toList();
  }

  /**
   * Returns an unmodifiable {@link List} of the source's elements in reverse order.
   *
   * @param ts  the source of the T elements
   * @param <T> the type of instances contained in the list
   * @return an unmodifiable {@link List} of the source's elements in reverse order
   */
  public static <T> List<T> reverse(
      List<@Nullable T> ts
  ) {
    if (!ts.isEmpty()) {

      return reverse(ts.stream());
    }

    return List.of();
  }

  /**
   * Returns an unmodifiable {@link List} of the source's elements in reverse order with the {@code null} elements
   * filtered out.
   *
   * @param stream the source of the T elements
   * @param <T>    the type of instances contained in the stream
   * @return an unmodifiable {@link List} of the source's elements in reverse order with the {@code null} elements
   *     filtered out
   */
  public static <T> List<T> reverse(
      Stream<@Nullable T> stream
  ) {
    Objects.requireNonNull(stream);
    var sanitized = stream
        .filter(Objects::nonNull)
        .toList();
    if (sanitized.isEmpty()) {
      return List.of();
    }

    return IntStream.range(0, sanitized.size())
        .mapToObj(i -> sanitized.get(sanitized.size() - 1 - i))
        .toList();
  }

  /**
   * Returns a new {@link List} extracting the non-empty {@link Optional} elements, and filtering out the {@code null}
   * and {@link Optional} empty elements.
   *
   * @param collection the source from which to extract the non-empty optional values
   * @param <T>        the type of instances contained in the source
   * @return a new {@link List} extracting the non-empty {@link Optional} elements, and filtering out the {@code null}
   *     and {@link Optional} empty elements
   */
  public static <T> List<T> flatten(
      Collection<@Nullable Optional<T>> collection
  ) {
    return flatten(collection.stream());
  }

  /**
   * Returns a new {@link List} extracting the non-empty {@link Optional} elements, and filtering out the {@code null}
   * and {@link Optional} empty elements.
   *
   * @param stream the source from which to extract the non-empty optional values
   * @param <T>    the type of instances contained in the stream
   * @return a new {@link List} extracting the non-empty {@link Optional} elements, and filtering out the {@code null}
   *     and {@link Optional} empty elements
   */
  public static <T> List<T> flatten(
      Stream<@Nullable Optional<T>> stream
  ) {
    return stream
        .filter(Objects::nonNull)
        .flatMap(Optional::stream)
        .toList();
  }

  /**
   * Returns {@link List}s in a {@link Tuple2} extracted from a {@link Collection} of {@link Either}s.
   *
   * @param collections the source from which to extract the lists
   * @param <L>         the type of instances contained within the left element of each either
   * @param <R>         the type of instances contained within the right element of each either
   * @return {@link List}s in a {@link Tuple2} extracted from a {@link Collection} of {@link Either}s
   */
  @SuppressWarnings("SpellCheckingInspection")
  public static <L, R> Tuple2<List<Optional<L>>, List<Optional<R>>> unzipEithers(
      Collection<@Nullable Either<L, R>> collections
  ) {
    return unzipEithers(collections.stream());
  }

  /**
   * Returns {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Either}s.
   *
   * @param stream the source from which to extract the lists
   * @param <L>    the type of instances contained within the left element of each either
   * @param <R>    the type of instances contained within the right element of each either
   * @return {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Either}s
   */
  @SuppressWarnings("SpellCheckingInspection")
  public static <L, R> Tuple2<List<Optional<L>>, List<Optional<R>>> unzipEithers(
      Stream<@Nullable Either<L, R>> stream
  ) {
    return unzip(stream
        .filter(Objects::nonNull)
        .map(
            either ->
                new Tuple2<>(
                    either.toOptionalLeft(),
                    either.toOptionalRight())));
  }

  /**
   * Returns {@link List}s in a {@link Tuple2} extracted from a {@link Collection} of {@link Either}s,
   * {@link #flatten}ing each returned list.
   *
   * @param collection the source from which to extract the lists
   * @param <L>        the type of instances contained within the left element of each either
   * @param <R>        the type of instances contained within the right element of each either
   * @return {@link List}s in a {@link Tuple2} extracted from a {@link Collection} of {@link Either}s,
   *     {@link #flatten}ing each returned list
   */
  @SuppressWarnings("SpellCheckingInspection")
  public static <L, R> Tuple2<List<L>, List<R>> unzipAndFlattenEithers(
      Collection<@Nullable Either<L, R>> collection
  ) {
    return unzipAndFlattenEithers(collection.stream());
  }

  /**
   * Returns {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Either}s, {@link #flatten}ing
   * each returned list.
   *
   * @param stream the source from which to extract the lists
   * @param <L>    the type of instances contained within the left element of each either
   * @param <R>    the type of instances contained within the right element of each either
   * @return {@link List}s in a {@link Tuple2} extracted from a {@link Stream} of {@link Either}s, {@link #flatten}ing
   *     each returned list
   */
  @SuppressWarnings("SpellCheckingInspection")
  public static <L, R> Tuple2<List<L>, List<R>> unzipAndFlattenEithers(
      Stream<@Nullable Either<L, R>> stream
  ) {
    var unzippedEithers = unzipEithers(stream);

    return new Tuple2<>(
        flatten(unzippedEithers._1().stream()),
        flatten(unzippedEithers._2().stream()));
  }

  /**
   * Return a {@link Tuple2} containing the {@link List}s extracted from a source of {@link Tuple2}s filtered of
   * {@code null}s.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @return a {@link Tuple2} containing the {@link List}s extracted from a source of {@link Tuple2}s filtered of
   *     {@code null}s
   */
  public static <A, B> Tuple2<List<A>, List<B>> unzip(
      Collection<Tuple2<A, B>> collection
  ) {
    return unzip(collection.stream());
  }

  /**
   * Returns a {@link Tuple2} containing the {@link List}s extracted from a source of {@link Tuple2}s filtered of
   * {@code null}s.
   *
   * @param stream the source of tuples from which to extract the lists
   * @param <A>    the type of instances contained within the first element of each tuple
   * @param <B>    the type of instances contained within the second element of each tuple
   * @return a {@link Tuple2} containing the {@link List}s extracted from a source of {@link Tuple2}s filtered of
   *     {@code null}s
   */
  @SuppressWarnings("ConstantValue")
  public static <A, B> Tuple2<List<A>, List<B>> unzip(
      Stream<Tuple2<A, B>> stream
  ) {
    Objects.requireNonNull(stream);

    return stream
        .filter(Objects::nonNull)
        .collect(Collectors.teeing(
            Collectors.mapping(Tuple2::_1, Collectors.toUnmodifiableList()),
            Collectors.mapping(Tuple2::_2, Collectors.toUnmodifiableList()),
            Tuple2::new));
  }

  /**
   * Return a {@link Tuple2} containing the {@link List}s extracted from a source of {@link Tuple2}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param fMapper    the function to use to extract the values from an element of the collection and optionally return
   *                   a value, and when non-empty, optionally return each of the element values
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @return a {@link Tuple2} containing the {@link List}s extracted from a source of {@link Tuple2}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   */
  public static <A, B> Tuple2<List<A>, List<B>> unzipAndFlatten(
      Collection<Tuple2<A, B>> collection,
      Function<
          Tuple2<A, B>,
          Optional<Tuple2<Optional<A>, Optional<B>>>> fMapper
  ) {
    return unzipAndFlatten(collection.stream(), fMapper);
  }

  /**
   * Returns a {@link Tuple2} containing the {@link List}s extracted from a source of {@link Tuple2}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param stream  the source of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the source and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @return a {@link Tuple2} containing the {@link List}s extracted from a source of {@link Tuple2}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function
   */
  public static <A, B> Tuple2<List<A>, List<B>> unzipAndFlatten(
      Stream<Tuple2<A, B>> stream,
      Function<
          Tuple2<A, B>,
          Optional<Tuple2<Optional<A>, Optional<B>>>> fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple2 ->
            fMapper.apply(tuple2)
                .ifPresent(optionalOfOptionals -> {
                  optionalOfOptionals._1().ifPresent(listA::add);
                  optionalOfOptionals._2().ifPresent(listB::add);
                }));

    return new Tuple2<>(
        listA.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listA),
        listB.isEmpty()
            ? List.of()
            : Collections.unmodifiableList(listB));
  }

  /**
   * Return a {@link Tuple3} containing the {@link List}s extracted from a source of {@link Tuple3}s filtered of
   * {@code null}s.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @return a {@link Tuple3} containing the {@link List}s extracted from a source of {@link Tuple3}s filtered of
   *     {@code null}s
   */
  public static <A, B, C> Tuple3<List<A>, List<B>, List<C>> unzip3(
      Collection<Tuple3<A, B, C>> collection
  ) {
    return unzip3(collection.stream());
  }

  /**
   * Returns a {@link Tuple3} containing the {@link List}s extracted from a source of {@link Tuple3}s filtered of
   * {@code null}s.
   *
   * @param stream the source of tuples from which to extract the lists
   * @param <A>    the type of instances contained within the first element of each tuple
   * @param <B>    the type of instances contained within the second element of each tuple
   * @param <C>    the type of instances contained within the third element of each tuple
   * @return a {@link Tuple3} containing the {@link List}s extracted from a source of {@link Tuple3}s filtered of
   *     {@code null}s
   */
  public static <A, B, C> Tuple3<List<A>, List<B>, List<C>> unzip3(
      Stream<Tuple3<A, B, C>> stream
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple3 -> {
          listA.add(tuple3._1());
          listB.add(tuple3._2());
          listC.add(tuple3._3());
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
   * Return a {@link Tuple3} containing the {@link List}s extracted from a source of {@link Tuple3}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param fMapper    the function to use to extract the values from an element of the collection and optionally return
   *                   a value, and when non-empty, optionally return each of the element values
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @return a {@link Tuple3} containing the {@link List}s extracted from a source of {@link Tuple3}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   */
  public static <A, B, C> Tuple3<List<A>, List<B>, List<C>> unzip3AndFlatten(
      Collection<Tuple3<A, B, C>> collection,
      Function<
          Tuple3<A, B, C>,
          Optional<Tuple3<Optional<A>, Optional<B>, Optional<C>>>> fMapper
  ) {
    return unzip3AndFlatten(collection.stream(), fMapper);
  }

  /**
   * Returns a {@link Tuple3} containing the {@link List}s extracted from a source of {@link Tuple3}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param stream  the source of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the source and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @return a {@link Tuple3} containing the {@link List}s extracted from a source of {@link Tuple3}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function
   */
  public static <A, B, C> Tuple3<List<A>, List<B>, List<C>> unzip3AndFlatten(
      Stream<Tuple3<A, B, C>> stream,
      Function<
          Tuple3<A, B, C>,
          Optional<Tuple3<Optional<A>, Optional<B>, Optional<C>>>> fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple3 ->
            fMapper.apply(tuple3)
                .ifPresent(optionalOfOptionals -> {
                  optionalOfOptionals._1().ifPresent(listA::add);
                  optionalOfOptionals._2().ifPresent(listB::add);
                  optionalOfOptionals._3().ifPresent(listC::add);
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
   * Return a {@link Tuple4} containing the {@link List}s extracted from a source of {@link Tuple4}s filtered of
   * {@code null}s.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @return a {@link Tuple4} containing the {@link List}s extracted from a source of {@link Tuple4}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D> Tuple4<List<A>, List<B>, List<C>, List<D>> unzip4(
      Collection<Tuple4<A, B, C, D>> collection
  ) {
    return unzip4(collection.stream());
  }

  /**
   * Returns a {@link Tuple4} containing the {@link List}s extracted from a source of {@link Tuple4}s filtered of
   * {@code null}s.
   *
   * @param stream the source of tuples from which to extract the lists
   * @param <A>    the type of instances contained within the first element of each tuple
   * @param <B>    the type of instances contained within the second element of each tuple
   * @param <C>    the type of instances contained within the third element of each tuple
   * @param <D>    the type of instances contained within the fourth element of each tuple
   * @return a {@link Tuple4} containing the {@link List}s extracted from a source of {@link Tuple4}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D> Tuple4<List<A>, List<B>, List<C>, List<D>> unzip4(
      Stream<Tuple4<A, B, C, D>> stream
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple4 -> {
          listA.add(tuple4._1());
          listB.add(tuple4._2());
          listC.add(tuple4._3());
          listD.add(tuple4._4());
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
   * Return a {@link Tuple4} containing the {@link List}s extracted from a source of {@link Tuple4}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param fMapper    the function to use to extract the values from an element of the collection and optionally return
   *                   a value, and when non-empty, optionally return each of the element values
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @return a {@link Tuple4} containing the {@link List}s extracted from a source of {@link Tuple4}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   */
  public static <A, B, C, D> Tuple4<List<A>, List<B>, List<C>, List<D>> unzip4AndFlatten(
      Collection<Tuple4<A, B, C, D>> collection,
      Function<
          Tuple4<A, B, C, D>,
          Optional<Tuple4<Optional<A>, Optional<B>, Optional<C>, Optional<D>>>> fMapper
  ) {
    return unzip4AndFlatten(collection.stream(), fMapper);
  }

  /**
   * Returns a {@link Tuple4} containing the {@link List}s extracted from a source of {@link Tuple4}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param stream  the source of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the source and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @return a {@link Tuple4} containing the {@link List}s extracted from a source of {@link Tuple4}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function
   */
  public static <A, B, C, D> Tuple4<List<A>, List<B>, List<C>, List<D>> unzip4AndFlatten(
      Stream<Tuple4<A, B, C, D>> stream,
      Function<
          Tuple4<A, B, C, D>,
          Optional<Tuple4<Optional<A>, Optional<B>, Optional<C>, Optional<D>>>> fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple4 ->
            fMapper.apply(tuple4)
                .ifPresent(optionalOfOptionals -> {
                  optionalOfOptionals._1().ifPresent(listA::add);
                  optionalOfOptionals._2().ifPresent(listB::add);
                  optionalOfOptionals._3().ifPresent(listC::add);
                  optionalOfOptionals._4().ifPresent(listD::add);
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
   * Return a {@link Tuple5} containing the {@link List}s extracted from a source of {@link Tuple5}s filtered of
   * {@code null}s.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @return a {@link Tuple5} containing the {@link List}s extracted from a source of {@link Tuple5}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E> Tuple5<List<A>, List<B>, List<C>, List<D>, List<E>> unzip5(
      Collection<Tuple5<A, B, C, D, E>> collection
  ) {
    return unzip5(collection.stream());
  }

  /**
   * Returns a {@link Tuple5} containing the {@link List}s extracted from a source of {@link Tuple5}s filtered of
   * {@code null}s.
   *
   * @param stream the source of tuples from which to extract the lists
   * @param <A>    the type of instances contained within the first element of each tuple
   * @param <B>    the type of instances contained within the second element of each tuple
   * @param <C>    the type of instances contained within the third element of each tuple
   * @param <D>    the type of instances contained within the fourth element of each tuple
   * @param <E>    the type of instances contained within the fifth element of each tuple
   * @return a {@link Tuple5} containing the {@link List}s extracted from a source of {@link Tuple5}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E> Tuple5<List<A>, List<B>, List<C>, List<D>, List<E>> unzip5(
      Stream<Tuple5<A, B, C, D, E>> stream
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple5 -> {
          listA.add(tuple5._1());
          listB.add(tuple5._2());
          listC.add(tuple5._3());
          listD.add(tuple5._4());
          listE.add(tuple5._5());
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
   * Return a {@link Tuple5} containing the {@link List}s extracted from a source of {@link Tuple5}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param fMapper    the function to use to extract the values from an element of the collection and optionally return
   *                   a value, and when non-empty, optionally return each of the element values
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @return a {@link Tuple5} containing the {@link List}s extracted from a source of {@link Tuple5}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   */
  public static <A, B, C, D, E> Tuple5<List<A>, List<B>, List<C>, List<D>, List<E>> unzip5AndFlatten(
      Collection<Tuple5<A, B, C, D, E>> collection,
      Function<
          Tuple5<A, B, C, D, E>,
          Optional<Tuple5<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>>>> fMapper
  ) {
    return unzip5AndFlatten(collection.stream(), fMapper);
  }

  /**
   * Returns a {@link Tuple5} containing the {@link List}s extracted from a source of {@link Tuple5}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param stream  the source of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the source and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @return a {@link Tuple5} containing the {@link List}s extracted from a source of {@link Tuple5}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function
   */
  public static <A, B, C, D, E> Tuple5<List<A>, List<B>, List<C>, List<D>, List<E>> unzip5AndFlatten(
      Stream<Tuple5<A, B, C, D, E>> stream,
      Function<
          Tuple5<A, B, C, D, E>,
          Optional<Tuple5<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>>>> fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple5 ->
            fMapper.apply(tuple5)
                .ifPresent(optionalOfOptionals -> {
                  optionalOfOptionals._1().ifPresent(listA::add);
                  optionalOfOptionals._2().ifPresent(listB::add);
                  optionalOfOptionals._3().ifPresent(listC::add);
                  optionalOfOptionals._4().ifPresent(listD::add);
                  optionalOfOptionals._5().ifPresent(listE::add);
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
   * Return a {@link Tuple6} containing the {@link List}s extracted from a source of {@link Tuple6}s filtered of
   * {@code null}s.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @param <F>        the type of instances contained within the sixth element of each tuple
   * @return a {@link Tuple6} containing the {@link List}s extracted from a source of {@link Tuple6}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E, F> Tuple6<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>> unzip6(
      Collection<Tuple6<A, B, C, D, E, F>> collection
  ) {
    return unzip6(collection.stream());
  }

  /**
   * Returns a {@link Tuple6} containing the {@link List}s extracted from a source of {@link Tuple6}s filtered of
   * {@code null}s.
   *
   * @param stream the source of tuples from which to extract the lists
   * @param <A>    the type of instances contained within the first element of each tuple
   * @param <B>    the type of instances contained within the second element of each tuple
   * @param <C>    the type of instances contained within the third element of each tuple
   * @param <D>    the type of instances contained within the fourth element of each tuple
   * @param <E>    the type of instances contained within the fifth element of each tuple
   * @param <F>    the type of instances contained within the sixth element of each tuple
   * @return a {@link Tuple6} containing the {@link List}s extracted from a source of {@link Tuple6}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E, F> Tuple6<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>> unzip6(
      Stream<Tuple6<A, B, C, D, E, F>> stream
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple6 -> {
          listA.add(tuple6._1());
          listB.add(tuple6._2());
          listC.add(tuple6._3());
          listD.add(tuple6._4());
          listE.add(tuple6._5());
          listF.add(tuple6._6());
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
   * Return a {@link Tuple6} containing the {@link List}s extracted from a source of {@link Tuple6}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param fMapper    the function to use to extract the values from an element of the collection and optionally return
   *                   a value, and when non-empty, optionally return each of the element values
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @param <F>        the type of instances contained within the sixth element of each tuple
   * @return a {@link Tuple6} containing the {@link List}s extracted from a source of {@link Tuple6}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   */
  public static <A, B, C, D, E, F> Tuple6<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>> unzip6AndFlatten(
      Collection<Tuple6<A, B, C, D, E, F>> collection,
      Function<
          Tuple6<A, B, C, D, E, F>,
          Optional<Tuple6<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>, Optional<F>>>> fMapper
  ) {
    return unzip6AndFlatten(collection.stream(), fMapper);
  }

  /**
   * Returns a {@link Tuple6} containing the {@link List}s extracted from a source of {@link Tuple6}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param stream  the source of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the source and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @param <F>     the type of instances contained within the sixth element of each tuple
   * @return a {@link Tuple6} containing the {@link List}s extracted from a source of {@link Tuple6}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function
   */
  public static <A, B, C, D, E, F> Tuple6<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>> unzip6AndFlatten(
      Stream<Tuple6<A, B, C, D, E, F>> stream,
      Function<
          Tuple6<A, B, C, D, E, F>,
          Optional<Tuple6<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>, Optional<F>>>> fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple6 ->
            fMapper.apply(tuple6)
                .ifPresent(optionalOfOptionals -> {
                  optionalOfOptionals._1().ifPresent(listA::add);
                  optionalOfOptionals._2().ifPresent(listB::add);
                  optionalOfOptionals._3().ifPresent(listC::add);
                  optionalOfOptionals._4().ifPresent(listD::add);
                  optionalOfOptionals._5().ifPresent(listE::add);
                  optionalOfOptionals._6().ifPresent(listF::add);
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
   * Return a {@link Tuple7} containing the {@link List}s extracted from a source of {@link Tuple7}s filtered of
   * {@code null}s.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @param <F>        the type of instances contained within the sixth element of each tuple
   * @param <G>        the type of instances contained within the seventh element of each tuple
   * @return a {@link Tuple7} containing the {@link List}s extracted from a source of {@link Tuple7}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E, F, G> Tuple7<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>> unzip7(
      Collection<Tuple7<A, B, C, D, E, F, G>> collection
  ) {
    return unzip7(collection.stream());
  }

  /**
   * Returns a {@link Tuple7} containing the {@link List}s extracted from a source of {@link Tuple7}s filtered of
   * {@code null}s.
   *
   * @param stream the source of tuples from which to extract the lists
   * @param <A>    the type of instances contained within the first element of each tuple
   * @param <B>    the type of instances contained within the second element of each tuple
   * @param <C>    the type of instances contained within the third element of each tuple
   * @param <D>    the type of instances contained within the fourth element of each tuple
   * @param <E>    the type of instances contained within the fifth element of each tuple
   * @param <F>    the type of instances contained within the sixth element of each tuple
   * @param <G>    the type of instances contained within the seventh element of each tuple
   * @return a {@link Tuple7} containing the {@link List}s extracted from a source of {@link Tuple7}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E, F, G> Tuple7<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>> unzip7(
      Stream<Tuple7<A, B, C, D, E, F, G>> stream
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple7 -> {
          listA.add(tuple7._1());
          listB.add(tuple7._2());
          listC.add(tuple7._3());
          listD.add(tuple7._4());
          listE.add(tuple7._5());
          listF.add(tuple7._6());
          listG.add(tuple7._7());
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
   * Return a {@link Tuple7} containing the {@link List}s extracted from a source of {@link Tuple7}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param fMapper    the function to use to extract the values from an element of the collection and optionally return
   *                   a value, and when non-empty, optionally return each of the element values
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @param <F>        the type of instances contained within the sixth element of each tuple
   * @param <G>        the type of instances contained within the seventh element of each tuple
   * @return a {@link Tuple7} containing the {@link List}s extracted from a source of {@link Tuple7}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   */
  public static <A, B, C, D, E, F, G> Tuple7<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>> unzip7AndFlatten(
      Collection<Tuple7<A, B, C, D, E, F, G>> collection,
      Function<
          Tuple7<A, B, C, D, E, F, G>,
          Optional<Tuple7<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>, Optional<F>, Optional<G>>>> fMapper
  ) {
    return unzip7AndFlatten(collection.stream(), fMapper);
  }

  /**
   * Returns a {@link Tuple7} containing the {@link List}s extracted from a source of {@link Tuple7}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param stream  the source of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the source and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @param <F>     the type of instances contained within the sixth element of each tuple
   * @param <G>     the type of instances contained within the seventh element of each tuple
   * @return a {@link Tuple7} containing the {@link List}s extracted from a source of {@link Tuple7}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function
   */
  public static <A, B, C, D, E, F, G> Tuple7<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>> unzip7AndFlatten(
      Stream<Tuple7<A, B, C, D, E, F, G>> stream,
      Function<
          Tuple7<A, B, C, D, E, F, G>,
          Optional<Tuple7<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>, Optional<F>, Optional<G>>>> fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple7 ->
            fMapper.apply(tuple7)
                .ifPresent(optionalOfOptionals -> {
                  optionalOfOptionals._1().ifPresent(listA::add);
                  optionalOfOptionals._2().ifPresent(listB::add);
                  optionalOfOptionals._3().ifPresent(listC::add);
                  optionalOfOptionals._4().ifPresent(listD::add);
                  optionalOfOptionals._5().ifPresent(listE::add);
                  optionalOfOptionals._6().ifPresent(listF::add);
                  optionalOfOptionals._7().ifPresent(listG::add);
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
   * Return a {@link Tuple8} containing the {@link List}s extracted from a source of {@link Tuple8}s filtered of
   * {@code null}s.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @param <F>        the type of instances contained within the sixth element of each tuple
   * @param <G>        the type of instances contained within the seventh element of each tuple
   * @param <H>        the type of instances contained within the eighth element of each tuple
   * @return a {@link Tuple8} containing the {@link List}s extracted from a source of {@link Tuple8}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E, F, G, H> Tuple8<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>> unzip8(
      Collection<Tuple8<A, B, C, D, E, F, G, H>> collection
  ) {
    return unzip8(collection.stream());
  }

  /**
   * Returns a {@link Tuple8} containing the {@link List}s extracted from a source of {@link Tuple8}s filtered of
   * {@code null}s.
   *
   * @param stream the source of tuples from which to extract the lists
   * @param <A>    the type of instances contained within the first element of each tuple
   * @param <B>    the type of instances contained within the second element of each tuple
   * @param <C>    the type of instances contained within the third element of each tuple
   * @param <D>    the type of instances contained within the fourth element of each tuple
   * @param <E>    the type of instances contained within the fifth element of each tuple
   * @param <F>    the type of instances contained within the sixth element of each tuple
   * @param <G>    the type of instances contained within the seventh element of each tuple
   * @param <H>    the type of instances contained within the eighth element of each tuple
   * @return a {@link Tuple8} containing the {@link List}s extracted from a source of {@link Tuple8}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E, F, G, H> Tuple8<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>> unzip8(
      Stream<Tuple8<A, B, C, D, E, F, G, H>> stream
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    var listH = new ArrayList<H>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple8 -> {
          listA.add(tuple8._1());
          listB.add(tuple8._2());
          listC.add(tuple8._3());
          listD.add(tuple8._4());
          listE.add(tuple8._5());
          listF.add(tuple8._6());
          listG.add(tuple8._7());
          listH.add(tuple8._8());
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
   * Return a {@link Tuple8} containing the {@link List}s extracted from a source of {@link Tuple8}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param fMapper    the function to use to extract the values from an element of the collection and optionally return
   *                   a value, and when non-empty, optionally return each of the element values
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @param <F>        the type of instances contained within the sixth element of each tuple
   * @param <G>        the type of instances contained within the seventh element of each tuple
   * @param <H>        the type of instances contained within the eighth element of each tuple
   * @return a {@link Tuple8} containing the {@link List}s extracted from a source of {@link Tuple8}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   */
  public static <A, B, C, D, E, F, G, H> Tuple8<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>> unzip8AndFlatten(
      Collection<Tuple8<A, B, C, D, E, F, G, H>> collection,
      Function<
          Tuple8<A, B, C, D, E, F, G, H>,
          Optional<Tuple8<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>, Optional<F>, Optional<G>, Optional<H>>>> fMapper
  ) {
    return unzip8AndFlatten(collection.stream(), fMapper);
  }

  /**
   * Returns a {@link Tuple8} containing the {@link List}s extracted from a source of {@link Tuple8}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param stream  the source of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the source and optionally return a
   *                value, and when non-empty, optionally return each of the element values
   * @param <A>     the type of instances contained within the first element of each tuple
   * @param <B>     the type of instances contained within the second element of each tuple
   * @param <C>     the type of instances contained within the third element of each tuple
   * @param <D>     the type of instances contained within the fourth element of each tuple
   * @param <E>     the type of instances contained within the fifth element of each tuple
   * @param <F>     the type of instances contained within the sixth element of each tuple
   * @param <G>     the type of instances contained within the seventh element of each tuple
   * @param <H>     the type of instances contained within the eighth element of each tuple
   * @return a {@link Tuple8} containing the {@link List}s extracted from a source of {@link Tuple8}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function
   */
  public static <A, B, C, D, E, F, G, H> Tuple8<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>> unzip8AndFlatten(
      Stream<Tuple8<A, B, C, D, E, F, G, H>> stream,
      Function<
          Tuple8<A, B, C, D, E, F, G, H>,
          Optional<Tuple8<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>, Optional<F>, Optional<G>, Optional<H>>>> fMapper
  ) {
    var listA = new ArrayList<A>();
    var listB = new ArrayList<B>();
    var listC = new ArrayList<C>();
    var listD = new ArrayList<D>();
    var listE = new ArrayList<E>();
    var listF = new ArrayList<F>();
    var listG = new ArrayList<G>();
    var listH = new ArrayList<H>();
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple8 ->
            fMapper.apply(tuple8)
                .ifPresent(optionalOfOptionals -> {
                  optionalOfOptionals._1().ifPresent(listA::add);
                  optionalOfOptionals._2().ifPresent(listB::add);
                  optionalOfOptionals._3().ifPresent(listC::add);
                  optionalOfOptionals._4().ifPresent(listD::add);
                  optionalOfOptionals._5().ifPresent(listE::add);
                  optionalOfOptionals._6().ifPresent(listF::add);
                  optionalOfOptionals._7().ifPresent(listG::add);
                  optionalOfOptionals._8().ifPresent(listH::add);
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
   * Return a {@link Tuple9} containing the {@link List}s extracted from a source of {@link Tuple9}s filtered of
   * {@code null}s.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @param <F>        the type of instances contained within the sixth element of each tuple
   * @param <G>        the type of instances contained within the seventh element of each tuple
   * @param <H>        the type of instances contained within the eighth element of each tuple
   * @param <I>        the type of instances contained within the ninth element of each tuple
   * @return a {@link Tuple9} containing the {@link List}s extracted from a source of {@link Tuple9}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E, F, G, H, I> Tuple9<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>, List<I>> unzip9(
      Collection<Tuple9<A, B, C, D, E, F, G, H, I>> collection
  ) {
    return unzip9(collection.stream());
  }

  /**
   * Returns a {@link Tuple9} containing the {@link List}s extracted from a source of {@link Tuple9}s filtered of
   * {@code null}s.
   *
   * @param stream the source of tuples from which to extract the lists
   * @param <A>    the type of instances contained within the first element of each tuple
   * @param <B>    the type of instances contained within the second element of each tuple
   * @param <C>    the type of instances contained within the third element of each tuple
   * @param <D>    the type of instances contained within the fourth element of each tuple
   * @param <E>    the type of instances contained within the fifth element of each tuple
   * @param <F>    the type of instances contained within the sixth element of each tuple
   * @param <G>    the type of instances contained within the seventh element of each tuple
   * @param <H>    the type of instances contained within the eighth element of each tuple
   * @param <I>    the type of instances contained within the ninth element of each tuple
   * @return a {@link Tuple9} containing the {@link List}s extracted from a source of {@link Tuple9}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E, F, G, H, I> Tuple9<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>, List<I>> unzip9(
      Stream<Tuple9<A, B, C, D, E, F, G, H, I>> stream
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
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple9 -> {
          listA.add(tuple9._1());
          listB.add(tuple9._2());
          listC.add(tuple9._3());
          listD.add(tuple9._4());
          listE.add(tuple9._5());
          listF.add(tuple9._6());
          listG.add(tuple9._7());
          listH.add(tuple9._8());
          listI.add(tuple9._9());
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
   * Return a {@link Tuple9} containing the {@link List}s extracted from a source of {@link Tuple9}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param fMapper    the function to use to extract the values from an element of the collection and optionally return
   *                   a value, and when non-empty, optionally return each of the element values
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @param <F>        the type of instances contained within the sixth element of each tuple
   * @param <G>        the type of instances contained within the seventh element of each tuple
   * @param <H>        the type of instances contained within the eighth element of each tuple
   * @param <I>        the type of instances contained within the ninth element of each tuple
   * @return a {@link Tuple9} containing the {@link List}s extracted from a source of {@link Tuple9}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   */
  public static <A, B, C, D, E, F, G, H, I> Tuple9<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>, List<I>> unzip9AndFlatten(
      Collection<Tuple9<A, B, C, D, E, F, G, H, I>> collection,
      Function<
          Tuple9<A, B, C, D, E, F, G, H, I>,
          Optional<Tuple9<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>, Optional<F>, Optional<G>, Optional<H>, Optional<I>>>> fMapper
  ) {
    return unzip9AndFlatten(collection.stream(), fMapper);
  }

  /**
   * Returns a {@link Tuple9} containing the {@link List}s extracted from a source of {@link Tuple9}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param stream  the source of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the source and optionally return a
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
   * @return a {@link Tuple9} containing the {@link List}s extracted from a source of {@link Tuple9}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function
   */
  public static <A, B, C, D, E, F, G, H, I> Tuple9<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>, List<I>> unzip9AndFlatten(
      Stream<Tuple9<A, B, C, D, E, F, G, H, I>> stream,
      Function<
          Tuple9<A, B, C, D, E, F, G, H, I>,
          Optional<Tuple9<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>, Optional<F>, Optional<G>, Optional<H>, Optional<I>>>> fMapper
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
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple9 ->
            fMapper.apply(tuple9)
                .ifPresent(optionalOfOptionals -> {
                  optionalOfOptionals._1().ifPresent(listA::add);
                  optionalOfOptionals._2().ifPresent(listB::add);
                  optionalOfOptionals._3().ifPresent(listC::add);
                  optionalOfOptionals._4().ifPresent(listD::add);
                  optionalOfOptionals._5().ifPresent(listE::add);
                  optionalOfOptionals._6().ifPresent(listF::add);
                  optionalOfOptionals._7().ifPresent(listG::add);
                  optionalOfOptionals._8().ifPresent(listH::add);
                  optionalOfOptionals._9().ifPresent(listI::add);
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
   * Return a {@link Tuple10} containing the {@link List}s extracted from a source of {@link Tuple10}s filtered of
   * {@code null}s.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @param <F>        the type of instances contained within the sixth element of each tuple
   * @param <G>        the type of instances contained within the seventh element of each tuple
   * @param <H>        the type of instances contained within the eighth element of each tuple
   * @param <I>        the type of instances contained within the ninth element of each tuple
   * @param <J>        the type of instances contained within the tenth element of each tuple
   * @return a {@link Tuple10} containing the {@link List}s extracted from a source of {@link Tuple10}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E, F, G, H, I, J> Tuple10<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>, List<I>, List<J>> unzip10(
      Collection<Tuple10<A, B, C, D, E, F, G, H, I, J>> collection
  ) {
    return unzip10(collection.stream());
  }

  /**
   * Returns a {@link Tuple10} containing the {@link List}s extracted from a source of {@link Tuple10}s filtered of
   * {@code null}s.
   *
   * @param stream the source of tuples from which to extract the lists
   * @param <A>    the type of instances contained within the first element of each tuple
   * @param <B>    the type of instances contained within the second element of each tuple
   * @param <C>    the type of instances contained within the third element of each tuple
   * @param <D>    the type of instances contained within the fourth element of each tuple
   * @param <E>    the type of instances contained within the fifth element of each tuple
   * @param <F>    the type of instances contained within the sixth element of each tuple
   * @param <G>    the type of instances contained within the seventh element of each tuple
   * @param <H>    the type of instances contained within the eighth element of each tuple
   * @param <I>    the type of instances contained within the ninth element of each tuple
   * @param <J>    the type of instances contained within the tenth element of each tuple
   * @return a {@link Tuple10} containing the {@link List}s extracted from a source of {@link Tuple10}s filtered of
   *     {@code null}s
   */
  public static <A, B, C, D, E, F, G, H, I, J> Tuple10<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>, List<I>, List<J>> unzip10(
      Stream<Tuple10<A, B, C, D, E, F, G, H, I, J>> stream
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
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple10 -> {
          listA.add(tuple10._1());
          listB.add(tuple10._2());
          listC.add(tuple10._3());
          listD.add(tuple10._4());
          listE.add(tuple10._5());
          listF.add(tuple10._6());
          listG.add(tuple10._7());
          listH.add(tuple10._8());
          listI.add(tuple10._9());
          listJ.add(tuple10._10());
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
   * Return a {@link Tuple10} containing the {@link List}s extracted from a source of {@link Tuple10}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param collection the source of tuples from which to extract the lists
   * @param fMapper    the function to use to extract the values from an element of the collection and optionally return
   *                   a value, and when non-empty, optionally return each of the element values
   * @param <A>        the type of instances contained within the first element of each tuple
   * @param <B>        the type of instances contained within the second element of each tuple
   * @param <C>        the type of instances contained within the third element of each tuple
   * @param <D>        the type of instances contained within the fourth element of each tuple
   * @param <E>        the type of instances contained within the fifth element of each tuple
   * @param <F>        the type of instances contained within the sixth element of each tuple
   * @param <G>        the type of instances contained within the seventh element of each tuple
   * @param <H>        the type of instances contained within the eighth element of each tuple
   * @param <I>        the type of instances contained within the ninth element of each tuple
   * @param <J>        the type of instances contained within the tenth element of each tuple
   * @return a {@link Tuple10} containing the {@link List}s extracted from a source of {@link Tuple10}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   */
  public static <A, B, C, D, E, F, G, H, I, J> Tuple10<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>, List<I>, List<J>> unzip10AndFlatten(
      Collection<Tuple10<A, B, C, D, E, F, G, H, I, J>> collection,
      Function<
          Tuple10<A, B, C, D, E, F, G, H, I, J>,
          Optional<Tuple10<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>, Optional<F>, Optional<G>, Optional<H>, Optional<I>, Optional<J>>>> fMapper
  ) {
    return unzip10AndFlatten(collection.stream(), fMapper);
  }

  /**
   * Returns a {@link Tuple10} containing the {@link List}s extracted from a source of {@link Tuple10}s filtered of
   * {@code null}s, and then filtered and transformed by the {@code fMapper} function.
   *
   * @param stream  the source of tuples from which to extract the lists
   * @param fMapper the function to use to extract the values from an element of the source and optionally return a
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
   * @param <J>     the type of instances contained within the tenth element of each tuple
   * @return a {@link Tuple10} containing the {@link List}s extracted from a source of {@link Tuple10}s filtered of
   *     {@code null}s, and then filtered and transformed by the {@code fMapper} function
   */
  public static <A, B, C, D, E, F, G, H, I, J> Tuple10<List<A>, List<B>, List<C>, List<D>, List<E>, List<F>, List<G>, List<H>, List<I>, List<J>> unzip10AndFlatten(
      Stream<Tuple10<A, B, C, D, E, F, G, H, I, J>> stream,
      Function<
          Tuple10<A, B, C, D, E, F, G, H, I, J>,
          Optional<Tuple10<Optional<A>, Optional<B>, Optional<C>, Optional<D>, Optional<E>, Optional<F>, Optional<G>, Optional<H>, Optional<I>, Optional<J>>>> fMapper
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
    stream
        .filter(Objects::nonNull)
        .forEachOrdered(tuple10 ->
            fMapper.apply(tuple10)
                .ifPresent(optionalOfOptionals -> {
                  optionalOfOptionals._1().ifPresent(listA::add);
                  optionalOfOptionals._2().ifPresent(listB::add);
                  optionalOfOptionals._3().ifPresent(listC::add);
                  optionalOfOptionals._4().ifPresent(listD::add);
                  optionalOfOptionals._5().ifPresent(listE::add);
                  optionalOfOptionals._6().ifPresent(listF::add);
                  optionalOfOptionals._7().ifPresent(listG::add);
                  optionalOfOptionals._8().ifPresent(listH::add);
                  optionalOfOptionals._9().ifPresent(listI::add);
                  optionalOfOptionals._10().ifPresent(listJ::add);
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
