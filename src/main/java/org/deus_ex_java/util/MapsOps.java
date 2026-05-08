package org.deus_ex_java.util;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.util.refined.NonEmptyMap;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utility class providing static methods to create {@link Map} instances.
 */
@NullMarked
public final class MapsOps {

  private MapsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns a {@link Map}{@code <K, V>} backed by an empty and modifiable {@link HashMap}{@code <K, V>}.
   * <p>
   * This enables specifying a function in the shape of {@code () -> MapsOps::newHashMap} which returns the
   * <em>interface</em> type of {@link Map}{@code <K, V>}, which is preferable over specifying {@code HashMap::new}
   * which returns the <em>class specific implementation</em> type of {@link HashMap}{@code <K, V>}.
   *
   * @param <K> the type of the key instances contained in the {@link Map}
   * @param <V> the type of the value instances contained in the {@link Map}
   * @return a {@link Map}{@code <K, V>} backed by an empty and modifiable {@link HashMap}{@code <K, V>}
   */
  public static <K, V> Map<K, V> newHashMap() {
    //noinspection Convert2Diamond
    return new HashMap<K, V>();
  }

  /**
   * Returns a {@link Map}{@code <K, V>} backed by an empty and modifiable {@link HashMap}{@code <K, V>}.
   * <p>
   * This enables specifying a function in the shape of {@code () -> MapsOps.newHashMap(K.class, T.class)} which returns
   * the <em>interface</em> type of {@link Map}{@code <K, V>}, which is preferable over specifying {@code HashMap::new}
   * which returns the <em>class specific implementation</em> type of {@link HashMap}{@code <K, V>}.
   *
   * @param clazzK the class of the type for the key
   * @param clazzV the class of the type for the value
   * @param <K>    the type of the key instances contained in the {@link Map}
   * @param <V>    the type of the value instances contained in the {@link Map}
   * @return a {@link Map}{@code <K, V>} backed by an empty and modifiable {@link HashMap}{@code <K, V>}
   */
  public static <K, V> Map<K, V> newHashMap(Class<K> clazzK, Class<V> clazzV) {
    Objects.requireNonNull(clazzK);
    Objects.requireNonNull(clazzV);

    return Collections.checkedMap(new HashMap<>(), clazzK, clazzV);
  }

  /**
   * Returns a {@link Map}{@code <K, V>} backed by an empty and modifiable {@link LinkedHashMap}{@code <K, V>}.
   * <p>
   * This enables specifying a function in the shape of {@code () -> MapsOps::newLinkedHashMap} which returns the
   * <em>interface</em> type of {@link Map}{@code <K, V>}, which is preferable over specifying
   * {@code LinkedHashMap::new} which returns the <em>class specific implementation</em> type of
   * {@link LinkedHashMap}{@code <K, V>}.
   *
   * @param <K> the type of the key instances contained in the {@link Map}
   * @param <V> the type of the value instances contained in the {@link Map}
   * @return a {@link Map}{@code <K, V>} backed by an empty and modifiable {@link LinkedHashMap}{@code <K, V>}
   */
  public static <K, V> Map<K, V> newLinkedHashMap() {
    //noinspection Convert2Diamond
    return new LinkedHashMap<K, V>();
  }

  /**
   * Returns a {@link Map}{@code <K, V>} backed by an empty and modifiable {@link LinkedHashMap}{@code <K, V>}.
   * <p>
   * This enables specifying a function in the shape of {@code () -> MapsOps.newLinkedHashMap(K.class, T.class)} which
   * returns the <em>interface</em> type of {@link Map}{@code <K, V>}, which is preferable over specifying
   * {@code LinkedHashMap::new} which returns the <em>class specific implementation</em> type of
   * {@link LinkedHashMap}{@code <K, V>}.
   *
   * @param clazzK the class of the type for the key
   * @param clazzV the class of the type for the value
   * @param <K>    the type of the key instances contained in the {@link Map}
   * @param <V>    the type of the value instances contained in the {@link Map}
   * @return a {@link Map}{@code <K, V>} backed by an empty and modifiable {@link LinkedHashMap}{@code <K, V>}
   */
  public static <K, V> Map<K, V> newLinkedHashMap(Class<K> clazzK, Class<V> clazzV) {
    Objects.requireNonNull(clazzK);
    Objects.requireNonNull(clazzV);

    return Collections.checkedMap(new LinkedHashMap<>(), clazzK, clazzV);
  }

  /**
   * Returns an empty {@link Map} using {@link Map#of}, if {@code mapKv} is {@code null}, otherwise returns
   * {@code mapKv}.
   * <p>
   * Note: If the requirement is to both avoid a {@code null} value, and an empty {@link Map}, the refined class of
   * {@link NonEmptyMap} enable <i>compile-time enforcement</i> of said contract requirements.
   *
   * @param mapKv possibly {@code null} {@link Map} to reify to make {@code null} safe
   * @param <K>   the type of the key instances contained in the {@link Map}
   * @param <V>   the type of the value instances contained in the {@link Map}
   * @return an empty {@link Map} using {@link Map#of}, if {@code mapKv} is {@code null}, otherwise returns
   *     {@code mapKv}.
   */
  public static <K, V> Map<K, V> nullToEmpty(@Nullable Map<K, V> mapKv) {
    return mapKv != null
        ? mapKv
        : Map.of();
  }

  /**
   * Returns an occupied {@link Optional} containing a {@link NonEmptyMap} if {@code map.isEmpty} is {@code false},
   * otherwise an {@link Optional#empty()}.
   *
   * @param map the possibly {@code null} or empty source to wrap
   * @param <K> the type of the key instances contained in the source
   * @param <V> the type of the value instances contained in the source
   * @return an occupied {@link Optional} containing a {@link NonEmptyMap} if {@code map.isEmpty} is {@code false},
   *     otherwise an {@link Optional#empty()}
   */
  public static <K, V> Optional<NonEmptyMap<K, V>> toNonEmpty(@Nullable Map<K, V> map) {
    return (map != null)
        ? NonEmptyMap.wrap(map).toOptional()
        : Optional.empty();
  }

  /**
   * Returns {@code true} if both the key and the value are non-{@code null}, otherwise {@code false}.
   *
   * @param entry the key/value pair as an {@link Entry}
   * @param <K>   the type of the keys contained in the {@code map}
   * @param <V>   the type of the values contained in the {@code map}
   * @return {@code true} if both the key and the value are non-{@code null}, otherwise {@code false}
   */
  public static <K, V> boolean isNonNulls(
      Entry<@Nullable K, @Nullable V> entry
  ) {
    return (entry.getKey() != null) && (entry.getValue() != null);
  }

  /**
   * Returns a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   * the validation preconditions which failed, otherwise an {@link Optional#empty()}.
   * <p>
   * <u><b>Preconditions:</b></u>
   * <ul>
   * <li>{@code entry.getKey()} must not be {@code null}</li>
   * <li>{@code entry.getValue()} must not be {@code null}</li>
   * </ul>
   * <p>
   *
   * @param entry the key/value pair as an {@link Entry}
   * @param <K>   the type of the keys contained in the {@code map}
   * @param <V>   the type of the values contained in the {@code map}
   * @return a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   *     the validation preconditions which failed, otherwise an {@link Optional#empty()}
   */
  public static <K, V> Optional<ParametersValidationException> validate(
      Entry<@Nullable K, @Nullable V> entry
  ) {
    return !isNonNulls(entry)
        //@formatter:off
        ? Optional.of(
            new ParametersValidationException(
                "MapsOps.containsNulls failed preconditions on the entry",
                (entry.getKey() == null) && (entry.getValue() == null)
                    ? List.of(
                        "entry.getKey() is null",
                        "entry.getValue() is null")
                    : List.of(
                        entry.getKey() == null
                              ? "entry.getKey() is null"
                              : "entry.getValue() is null")))
        : Optional.empty();
        //@formatter:on
  }

  /**
   * Returns a new unmodifiable unordered {@link Map} from an existing {@link Map}, removing every {@link Entry} from
   * {@code entrySet} where either the key or the value is {@code null}.
   *
   * @param map the source of the existing key/value pairs
   * @param <K> the type of the keys contained in the {@code map}
   * @param <V> the type of the values contained in the {@code map}
   * @return a new unmodifiable unordered {@link Map} from an existing {@link Map}, removing every {@link Entry} from
   *     {@code entrySet} where either the key or the value is {@code null}
   */
  public static <K, V> Map<K, V> nullSanitize(
      Map<@Nullable K, @Nullable V> map
  ) {
    return map
        .entrySet()
        .stream()
        .filter(MapsOps::isNonNulls)
        .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
  }

  /**
   * Returns a new unmodifiable unordered {@link Map} from an existing {@link Map}, adding/updating an {@link Entry}.
   *
   * @param map   the source of the existing key/value pairs
   * @param entry the key/value pair as an {@link Entry}
   * @param <K>   the type of the keys contained in the {@code map}
   * @param <V>   the type of the values contained in the {@code map}
   * @return a new unmodifiable unordered {@link Map} from an existing {@link Map}, adding/updating an {@link Entry}
   * @throws NullPointerException if the provided {@link Entry} contains {@code null} in either its key or value.
   */
  public static <K, V> Map<K, V> addEntry(
      Map<K, V> map,
      @Nullable Entry<K, V> entry
  ) {
    Objects.requireNonNull(map);
    if (entry == null) {

      return map.isEmpty()
          ? Map.of()
          : Map.copyOf(map);
    }
    validate(entry)
        .ifPresent(parametersValidationException -> {
          throw parametersValidationException;
        });

    return addKeyAndValue(map, entry.getKey(), entry.getValue());
  }

  /**
   * Returns a new unmodifiable unordered {@link Map} from an existing {@link Map}, adding/updating a {@code key} and
   * its associated {@code value}.
   *
   * @param map   the source of the existing key/value pairs
   * @param key   the key with which to associate with the value
   * @param value the value with which to associate with the key
   * @param <K>   the type of the keys contained in the {@code map}
   * @param <V>   the type of the values contained in the {@code map}
   * @return a new unmodifiable unordered {@link Map} from an existing {@link Map}, adding/updating a {@code key} and
   *     its associated {@code value}
   */
  public static <K, V> Map<K, V> addKeyAndValue(
      Map<K, V> map,
      K key,
      V value
  ) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);
    var result = new HashMap<>(map);
    result.put(key, value);

    return Collections.unmodifiableMap(result);

  }

  private static final Map<?, ?> UNMODIFIABLE_LINKED_HASH_MAP_EMPTY = Collections.unmodifiableMap(new LinkedHashMap<>());

  /**
   * Returns a new unmodifiable <u><i>ordered</i></u> {@link Map} from an existing {@link Map}, appending (or if the key
   * is already present, updating) an {@link Entry}.
   *
   * @param map   the (assumed to be) <u><i>ordered</i></u> source of the existing key/value pairs
   * @param entry the key/value pair as an {@link Entry}
   * @param <K>   the type of the keys contained in the {@code map}
   * @param <V>   the type of the values contained in the {@code map}
   * @return a new unmodifiable <u><i>ordered</i></u> {@link Map} from an existing {@link Map}, appending (or if the key
   *     is already present, updating) an {@link Entry}
   * @throws NullPointerException if the provided {@link Entry} contains {@code null} in either its key or value.
   */
  public static <K, V> Map<K, V> appendEntry(
      Map<K, V> map,
      @Nullable Entry<K, V> entry
  ) {
    Objects.requireNonNull(map);
    if (map.isEmpty() && (entry == null)) {

      //noinspection unchecked
      return (Map<K, V>) UNMODIFIABLE_LINKED_HASH_MAP_EMPTY;
    }
    if (entry == null) {

      return Collections.unmodifiableMap(new LinkedHashMap<>(map));
    }
    validate(entry)
        .ifPresent(parametersValidationException -> {
          throw parametersValidationException;
        });

    return appendKeyAndValue(map, entry.getKey(), entry.getValue());
  }

  /**
   * Returns a new unmodifiable <u><i>ordered</i></u> {@link Map} from an existing {@link Map}, appending (or if the key
   * is already present, updating) a {@code key} and its associated {@code value}.
   *
   * @param map   the (assumed to be) <u><i>ordered</i></u> source of the existing key/value pairs
   * @param key   the key with which to associate with the value
   * @param value the value with which to associate with the key
   * @param <K>   the type of the keys contained in the {@code map}
   * @param <V>   the type of the values contained in the {@code map}
   * @return a new unmodifiable <u><i>ordered</i></u> {@link Map} from an existing {@link Map}, appending (or if the key
   *     is already present, updating) a {@code key} and its associated {@code value}
   */
  public static <K, V> Map<K, V> appendKeyAndValue(
      Map<K, V> map,
      K key,
      V value
  ) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(key);
    Objects.requireNonNull(value);
    var result = map.isEmpty()
        ? new LinkedHashMap<K, V>()
        : new LinkedHashMap<>(map);
    result.put(key, value);

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable unordered {@link Map} consisting of each {@link Map}, filtered to non-null in both the keys
   * and the values, from maps added together.
   *
   * @param maps the maps to append
   * @param <K>  the type of the keys contained in the {@code map}
   * @param <V>  the type of the values contained in the {@code map}
   * @return an unmodifiable unordered {@link Map} consisting of each {@link Map}, filtered to non-null in both the keys
   *     and the values, from maps added together
   */
  @SuppressWarnings("ConstantValue")
  @SafeVarargs
  public static <K, V> Map<K, V> addMaps(
      Map<K, V>... maps
  ) {
    Objects.requireNonNull(maps);
    if (maps.length == 0) {
      return Map.of();
    }
    var result = Arrays.stream(maps)
        .filter(Objects::nonNull)
        .flatMap(map ->
            map.entrySet().stream())
        .filter(MapsOps::isNonNulls)
        .collect(Collectors.toMap(
            Entry::getKey,
            Entry::getValue,
            (vOld, vNew) ->
                vNew)); //last-wins: later maps overwrite earlier maps

    return result.isEmpty()
        ? Map.of()
        : Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Map} consisting of each {@link Map}, filtered to non-null in
   * both the keys and the values, from maps appended together.
   *
   * @param maps the (assumed to be) <u><i>ordered</i></u> maps to append
   * @param <K>  the type of the keys contained in the {@code map}
   * @param <V>  the type of the values contained in the {@code map}
   * @return an unmodifiable <u><i>ordered</i></u> {@link Map} consisting of each {@link Map}, filtered to non-null in
   *     both the keys and the values, from maps appended together
   */
  @SuppressWarnings("ConstantValue")
  @SafeVarargs
  public static <K, V> Map<K, V> appendMaps(
      Map<K, V>... maps
  ) {
    Objects.requireNonNull(maps);
    if (maps.length == 0) {

      //noinspection unchecked
      return (Map<K, V>) UNMODIFIABLE_LINKED_HASH_MAP_EMPTY;
    }
    var result = Arrays.stream(maps)
        .filter(Objects::nonNull)
        .flatMap(map ->
            map.entrySet().stream())
        .filter(MapsOps::isNonNulls)
        .collect(Collectors.toMap(
            Entry::getKey,
            Entry::getValue,
            (vOld, vNew) ->
                vNew, //last-wins: later maps overwrite earlier maps
            LinkedHashMap::new
        ));

    //noinspection unchecked
    return result.isEmpty()
        ? (Map<K, V>) UNMODIFIABLE_LINKED_HASH_MAP_EMPTY
        : Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable unordered {@link Map} with the entry associated with the {@code key} removed if it is
   * non-null, or an unmodifiable unordered copy of the original {@code map}.
   *
   * @param map the source from which the unordered copy is made
   * @param key the key to remove from the copy of the map
   * @param <K> the type of the keys contained in the {@code map}
   * @param <V> the type of the values contained in the {@code map}
   * @return an unmodifiable unordered {@link Map} with the entry associated with the {@code key} removed if it is
   *     non-null, or an unmodifiable copy of the original {@code map}
   */
  public static <K, V> Map<K, V> removeEntry(
      Map<K, V> map,
      @Nullable K key
  ) {
    Objects.requireNonNull(map);
    if (map.isEmpty()) {

      return Map.of();
    }
    if (key == null) {

      return Map.copyOf(map);
    }
    var result = new HashMap<>(map);
    result.remove(key);

    return Collections.unmodifiableMap(result);

  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Map} with the entry associated with the {@code key} removed if
   * it is non-null, or an unmodifiable <u><i>ordered</i></u> copy of the original {@code map}.
   *
   * @param map the (assumed to be) <u><i>ordered</i></u> source from which the copy is made
   * @param key the key to remove from the copy of the map
   * @param <K> the type of the keys contained in the {@code map}
   * @param <V> the type of the values contained in the {@code map}
   * @return an unmodifiable <u><i>ordered</i></u> {@link Map} with the entry associated with the {@code key} removed if
   *     it is non-null, or an unmodifiable <u><i>ordered</i></u> copy of the original {@code map}
   */
  public static <K, V> Map<K, V> removeEntryOrdered(
      Map<K, V> map,
      @Nullable K key
  ) {
    Objects.requireNonNull(map);
    if (map.isEmpty()) {

      //noinspection unchecked
      return (Map<K, V>) UNMODIFIABLE_LINKED_HASH_MAP_EMPTY;
    }
    var result = new LinkedHashMap<>(map);
    if (key != null) {
      result.remove(key);
    }

    return Collections.unmodifiableMap(result);

  }

  /**
   * Returns an unmodifiable unordered {@link Map} consisting of the entries from the original {@code map} with all
   * entries whose keys are contained within the {@code collection} removed.
   *
   * @param map        the source from which the unordered copy is made
   * @param collection the collection containing the keys to remove from the copy of the map
   * @param <K>        the type of the keys contained in the {@code map} and {@code collection}
   * @param <V>        the type of the values contained in the {@code map}
   * @return an unmodifiable unordered {@link Map} consisting of the entries from the original {@code map} with all
   *     entries whose keys are contained within the {@code collection} removed
   */
  public static <K, V> Map<K, V> removeAll(
      Map<K, V> map,
      Collection<K> collection
  ) {
    return removeAll(
        map,
        collection.stream());
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Map} consisting of the entries from the original {@code map}
   * with all entries whose keys are contained within the {@code collection} removed.
   *
   * @param map        the (assumed to be) <u><i>ordered</i></u> source from which the copy is made
   * @param collection the collection containing the keys to remove from the copy of the map
   * @param <K>        the type of the keys contained in the {@code map} and {@code collection}
   * @param <V>        the type of the values contained in the {@code map}
   * @return an unmodifiable <u><i>ordered</i></u> {@link Map} consisting of the entries from the original {@code map}
   *     with all entries whose keys are contained within the {@code collection} removed
   */
  public static <K, V> Map<K, V> removeAllOrdered(
      Map<K, V> map,
      Collection<K> collection
  ) {
    return removeAllOrdered(
        map,
        collection.stream());
  }

  private static <K, V> Map<K, V> helperRemoveAllStream(
      Map<K, V> map,
      Stream<K> stream,
      Map<K, V> mapEmpty,
      Function<Map<K, V>, Map<K, V>> fMapConstructor
  ) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(stream);
    if (map.isEmpty()) {

      return mapEmpty;
    }
    var removalsAsSet = stream
        .collect(Collectors.toUnmodifiableSet());
    var result = fMapConstructor.apply(map);
    if (!removalsAsSet.isEmpty()) {
      removalsAsSet.forEach(result::remove);
    }

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable unordered {@link Map} consisting of the entries from the original {@code map} with all
   * entries whose keys are contained within the {@code stream} removed.
   *
   * @param map    the source from which the unordered copy is made
   * @param stream the stream containing the keys to remove from the copy of the map
   * @param <K>    the type of the keys contained in the {@code map} and {@code stream}
   * @param <V>    the type of the values contained in the {@code map}
   * @return an unmodifiable unordered {@link Map} consisting of the entries from the original {@code map} with all
   *     entries whose keys are contained within the {@code stream} removed
   */
  public static <K, V> Map<K, V> removeAll(
      Map<K, V> map,
      Stream<K> stream
  ) {
    return helperRemoveAllStream(
        map,
        stream,
        Map.of(),
        HashMap::new);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Map} consisting of the entries from the original {@code map}
   * with all entries whose keys are contained within the {@code stream} removed.
   *
   * @param map    the (assumed to be) <u><i>ordered</i></u> source from which the copy is made
   * @param stream the stream containing the keys to remove from the copy of the map
   * @param <K>    the type of the keys contained in the {@code map} and {@code stream}
   * @param <V>    the type of the values contained in the {@code map}
   * @return an unmodifiable <u><i>ordered</i></u> {@link Map} consisting of the entries from the original {@code map}
   *     with all entries whose keys are contained within the {@code stream} removed
   */
  public static <K, V> Map<K, V> removeAllOrdered(
      Map<K, V> map,
      Stream<K> stream
  ) {
    //noinspection unchecked
    return helperRemoveAllStream(
        map,
        stream,
        (Map<K, V>) UNMODIFIABLE_LINKED_HASH_MAP_EMPTY,
        LinkedHashMap::new);
  }

  @SafeVarargs
  private static <K, V> Map<K, V> helperRemoveMaps(
      Map<K, V> map,
      Map<K, V> mapEmpty,
      Function<Map<K, V>, Map<K, V>> fMapConstructor,
      Set<K>... keySets
  ) {
    Objects.requireNonNull(map);
    Objects.requireNonNull(keySets);

    return TernaryOps.get(
        map.isEmpty(),
        () ->
            mapEmpty,
        () -> {
          var result = fMapConstructor.apply(map);
          if (keySets.length != 0) {
            @SuppressWarnings("ConstantValue")
            var removalsAsSet = Arrays.stream(keySets)
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableSet());
            if (!removalsAsSet.isEmpty()) {
              removalsAsSet.forEach(result::remove);
            }

            return result.isEmpty()
                ? mapEmpty
                : Collections.unmodifiableMap(result);
          }

          return Collections.unmodifiableMap(result);
        });
  }

  /**
   * Returns an unmodifiable unordered {@link Map} consisting of the entries from the original {@code map} with all
   * entries whose keys are contained within the {@code keySets} removed.
   *
   * @param map     the source from which the unordered copy is made
   * @param keySets the sets containing the keys to remove from the copy of the map
   * @param <K>     the type of the keys contained in the {@code map} and {@code keySets}
   * @param <V>     the type of the values contained in the {@code map}
   * @return an unmodifiable unordered {@link Map} consisting of the entries from the original {@code map} with all
   *     entries whose keys are contained within the {@code keySets} removed
   */
  @SafeVarargs
  public static <K, V> Map<K, V> removeMaps(
      Map<K, V> map,
      Set<K>... keySets
  ) {
    return helperRemoveMaps(
        map,
        Map.of(),
        HashMap::new,
        keySets);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Map} consisting of the entries from the original {@code map}
   * with all entries whose keys are contained within the {@code keySets} removed.
   *
   * @param map     the (assumed to be) <u><i>ordered</i></u> source from which the copy is made
   * @param keySets the sets containing the keys to remove from the copy of the map
   * @param <K>     the type of the keys contained in the {@code map} and {@code keySets}
   * @param <V>     the type of the values contained in the {@code map}
   * @return an unmodifiable <u><i>ordered</i></u> {@link Map} consisting of the entries from the original {@code map}
   *     with all entries whose keys are contained within the {@code keySets} removed
   */
  @SafeVarargs
  public static <K, V> Map<K, V> removeMapsOrdered(
      Map<K, V> map,
      Set<K>... keySets
  ) {
    //noinspection unchecked
    return helperRemoveMaps(
        map,
        (Map<K, V>) UNMODIFIABLE_LINKED_HASH_MAP_EMPTY,
        LinkedHashMap::new,
        keySets);
  }

  /**
   * Returns an unmodifiable unordered map filtering out each {@code Entry} where it is {@code null}, otherwise
   * filtering the entry out where either the contained entry's key and/or the value are {@code null}, and then if the
   * entry remains, it is ignored if it contains a duplicate key.
   *
   * @param collection the source of the entries
   * @param <K>        the type of the key in the entries
   * @param <V>        the type of the value in the entries
   * @return an unmodifiable unordered map filtering out each {@code Entry} where it is {@code null}, otherwise
   *     filtering the entry out where either the contained entry's key and/or the value are {@code null}, and then if
   *     the entry remains, it is ignored if it contains a duplicate key
   */
  public static <K, V> Map<K, V> toMap(
      Collection<@Nullable Entry<K, V>> collection
  ) {
    return toMap(collection.stream());
  }

  /**
   * Returns an unmodifiable unordered map filtering out each {@code Entry} where it is {@code null}, otherwise
   * filtering the entry out where either the contained entry's key and/or the value are {@code null}, and then if the
   * entry remains, it is ignored if it contains a duplicate key.
   *
   * @param kAndVs the source of the entries
   * @param <K>    the type of the key in the entries
   * @param <V>    the type of the value in the entries
   * @return an unmodifiable unordered map filtering out each {@code Entry} where it is {@code null}, otherwise
   *     filtering the entry out where either the contained entry's key and/or the value are {@code null}, and then if
   *     the entry remains, it is ignored if it contains a duplicate key
   */
  public static <K, V> Map<K, V> toMap(
      Stream<@Nullable Entry<K, V>> kAndVs
  ) {
    return toMap(kAndVs, Optional::of);
  }

  /**
   * Returns an unmodifiable unordered map filtering out each {@code T} where it is {@code null}, otherwise optionally
   * transforming the {@code T} via the function, {@code fTtoOptionalEntry}, into an {@link Optional} {@link Entry}, and
   * then filtering the {@link Optional} out where either the contained entry's key and/or the value are {@code null},
   * and then if the entry remains, it is ignored if it contains a duplicate key.
   *
   * @param collection        the source of the input to create entries
   * @param fTtoOptionalEntry function to filter and/or transform a T into an instance of {@link Entry}
   * @param <T>               the type of the source value the entries
   * @param <K>               the type of the key in the entries
   * @param <V>               the type of the value in the entries
   * @return an unmodifiable unordered map filtering out each {@code T} where it is {@code null}, otherwise optionally
   *     transforming the {@code T} via the function, {@code fTtoOptionalEntry}, into an {@link Optional} {@link Entry},
   *     and then filtering the {@link Optional} out where either the contained entry's key and/or the value are
   *     {@code null}, and then if the entry remains, it is ignored if it contains a duplicate key
   */
  public static <T, K, V> Map<K, V> toMap(
      Collection<@Nullable T> collection,
      Function<T, Optional<Entry<K, V>>> fTtoOptionalEntry
  ) {
    return toMap(collection.stream(), fTtoOptionalEntry);
  }

  /**
   * Returns an unmodifiable unordered map filtering out each {@code T} where it is {@code null}, otherwise optionally
   * transforming the {@code T} via the function, {@code fTtoOptionalEntry}, into an {@link Optional} {@link Entry}, and
   * then filtering the {@link Optional} out where either the contained entry's key and/or the value are {@code null},
   * and then if the entry remains, it is ignored if it contains a duplicate key.
   *
   * @param ts                the source of the input to create entries
   * @param fTtoOptionalEntry function to filter and/or transform a T into an instance of {@link Entry}
   * @param <T>               the type of the source value the entries
   * @param <K>               the type of the key in the entries
   * @param <V>               the type of the value in the entries
   * @return an unmodifiable unordered map filtering out each {@code T} where it is {@code null}, otherwise optionally
   *     transforming the {@code T} via the function, {@code fTtoOptionalEntry}, into an {@link Optional} {@link Entry},
   *     and then filtering the {@link Optional} out where either the contained entry's key and/or the value are
   *     {@code null}, and then if the entry remains, it is ignored if it contains a duplicate key
   */
  public static <T, K, V> Map<K, V> toMap(
      Stream<@Nullable T> ts,
      Function<T, Optional<Entry<K, V>>> fTtoOptionalEntry
  ) {
    Objects.requireNonNull(ts);
    Objects.requireNonNull(fTtoOptionalEntry);
    var map = ts
        .filter(Objects::nonNull)
        .flatMap(t ->
            fTtoOptionalEntry
                .apply(t)
                .filter(MapsOps::isNonNulls)
                .stream())
        .collect(Collectors.toMap(
            Entry::getKey,
            Entry::getValue,
            (vOld, vNew) ->
                vOld));

    return !map.isEmpty()
        ? Collections.unmodifiableMap(map)
        : Map.of();
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> map filtering out each {@code Entry} where it is {@code null},
   * otherwise filtering the entry out where either the contained entry's key and/or the value are {@code null}, and
   * then if the entry remains, it is ignored if it contains a duplicate key.
   *
   * @param collection the (assumed to be) <u><i>ordered</i></u> source of the entries
   * @param <K>        the type of the key in the entries
   * @param <V>        the type of the value in the entries
   * @return an unmodifiable <u><i>ordered</i></u> map filtering out each {@code Entry} where it is {@code null},
   *     otherwise filtering the entry out where either the contained entry's key and/or the value are {@code null}, and
   *     then if the entry remains, it is ignored if it contains a duplicate key
   */
  public static <K, V> Map<K, V> toMapOrdered(
      Collection<@Nullable Entry<K, V>> collection
  ) {
    return toMapOrdered(collection.stream(), Optional::of);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> map filtering out each {@code Entry} where it is {@code null},
   * otherwise filtering the entry out where either the contained entry's key and/or the value are {@code null}, and
   * then if the entry remains, it is ignored if it contains a duplicate key.
   *
   * @param stream the (assumed to be) <u><i>ordered</i></u> source of the entries
   * @param <K>    the type of the key in the entries
   * @param <V>    the type of the value in the entries
   * @return an unmodifiable <u><i>ordered</i></u> map filtering out each {@code Entry} where it is {@code null},
   *     otherwise filtering the entry out where either the contained entry's key and/or the value are {@code null}, and
   *     then if the entry remains, it is ignored if it contains a duplicate key
   */
  public static <K, V> Map<K, V> toMapOrdered(
      Stream<@Nullable Entry<K, V>> stream
  ) {
    return toMapOrdered(stream, Optional::of);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> map filtering out each {@code T} where it is {@code null}, otherwise
   * optionally transforming the {@code T} via the function, {@code fTtoOptionalEntry}, into an {@link Optional}
   * {@link Entry}, and then filtering the {@link Optional} out where either the contained entry's key and/or the value
   * are {@code null}, and then if the entry remains, it is ignored if it contains a duplicate key.
   *
   * @param collection        the (assumed to be) <u><i>ordered</i></u> source of the input to create entries
   * @param fTtoOptionalEntry function to filter and/or transform a T into an instance of {@link Entry}
   * @param <T>               the type of the source value the entries
   * @param <K>               the type of the key in the entries
   * @param <V>               the type of the value in the entries
   * @return an unmodifiable <u><i>ordered</i></u> map filtering out each {@code T} where it is {@code null}, otherwise
   *     optionally transforming the {@code T} via the function, {@code fTtoOptionalEntry}, into an {@link Optional}
   *     {@link Entry}, and then filtering the {@link Optional} out where either the contained entry's key and/or the
   *     value are {@code null}, and then if the entry remains, it is ignored if it contains a duplicate key
   */
  public static <T, K, V> Map<K, V> toMapOrdered(
      Collection<@Nullable T> collection,
      Function<T, Optional<Entry<K, V>>> fTtoOptionalEntry
  ) {
    return toMapOrdered(collection.stream(), fTtoOptionalEntry);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> map filtering out each {@code T} where it is {@code null}, otherwise
   * optionally transforming the {@code T} via the function, {@code fTtoOptionalEntry}, into an {@link Optional}
   * {@link Entry}, and then filtering the {@link Optional} out where either the contained entry's key and/or the value
   * are {@code null}, and then if the entry remains, it is ignored if it contains a duplicate key.
   *
   * @param stream            the (assumed to be) <u><i>ordered</i></u> source of the input to create entries
   * @param fTtoOptionalEntry function to filter and/or transform a T into an instance of {@link Entry}
   * @param <T>               the type of the source value the entries
   * @param <K>               the type of the key in the entries
   * @param <V>               the type of the value in the entries
   * @return an unmodifiable <u><i>ordered</i></u> map filtering out each {@code T} where it is {@code null}, otherwise
   *     optionally transforming the {@code T} via the function, {@code fTtoOptionalEntry}, into an {@link Optional}
   *     {@link Entry}, and then filtering the {@link Optional} out where either the contained entry's key and/or the
   *     value are {@code null}, and then if the entry remains, it is ignored if it contains a duplicate key
   */
  public static <T, K, V> Map<K, V> toMapOrdered(
      Stream<@Nullable T> stream,
      Function<T, Optional<Entry<K, V>>> fTtoOptionalEntry
  ) {
    var map = stream
        .filter(Objects::nonNull)
        .flatMap(t ->
            fTtoOptionalEntry
                .apply(t)
                .filter(MapsOps::isNonNulls)
                .stream())
        .collect(Collectors.toMap(
            Entry::getKey,
            Entry::getValue,
            (vOld, vNew) ->
                vOld,
            LinkedHashMap::new));

    return !map.isEmpty()
        ? Collections.unmodifiableMap(map)
        : Map.of();
  }

  /**
   * Returns an unmodifiable unordered {@link Map} where each key is swapped with its value, and may result in a smaller
   * {@link Map} if any of the {@code map} parameter's values are not unique; i.e. there is more than one key with the
   * same value (according to {@link Object#equals}).
   * <p>
   * ---
   * <p>
   * Swaps a {@link Map} where, for each entry, the value becomes the key, and the key becomes the value. In the event
   * the values in the supplied {@link Map} are not unique, i.e. there is more than one key with the same value
   * (according to {@link Object#equals}), it is undefined which value is retained as the unique key, and which other
   * keys and their associated values are <i>silently</i> dropped.
   *
   * @param map the map in which every entry will swap the key and value
   * @param <K> the type of the key instances
   * @param <V> the type of the value instances
   * @return an unmodifiable unordered {@link Map} where each key is swapped with its value, and may result in a smaller
   *     {@link Map} if any of the {@code map} parameter's values are not unique; i.e. there is more than one key with
   *     the same value (according to {@link Object#equals})
   * @throws NullPointerException if an {@link Entry} contains {@code null} in either its key or value.
   */
  public static <K, V> Map<V, K> swap(
      Map<K, V> map
  ) {
    if (map.isEmpty()) {

      return Map.of();
    }
    var result = map.entrySet()
        .stream()
        .peek(entry ->
            validate(entry).ifPresent(parametersValidationException -> {
              throw parametersValidationException;
            }))
        .collect(Collectors.toMap(
            Entry::getValue,
            Entry::getKey,
            (kOld, kNew) ->
                kOld)); //first-wins collision resolution

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Map} where each key is swapped with its value, and may result
   * in a smaller {@link Map} if any of the {@code map} parameter's values are not unique; i.e. there is more than one
   * key with the same value (according to {@link Object#equals}).
   * <p>
   * ---
   * <p>
   * Returns a {@link Map} where, for each entry, the value becomes the key, and the key becomes the value. In the event
   * the values in the supplied {@link Map} are not unique, i.e. there is more than one key with the same value
   * (according to {@link Object#equals}), it is undefined which value is retained as the unique key, and which other
   * keys and their associated values are <i>silently</i> dropped.
   *
   * @param map the (assumed to be) <u><i>ordered</i></u> map in which every entry will swap the key and value
   * @param <K> the type of the key instances
   * @param <V> the type of the value instances
   * @return an unmodifiable <u><i>ordered</i></u> {@link Map} where each key is swapped with its value, and may result
   *     in a smaller {@link Map} if any of the {@code map} parameter's values are not unique; i.e. there is more than
   *     one key with the same value (according to {@link Object#equals})
   * @throws NullPointerException if an {@link Entry} contains {@code null} in either its key or value.
   */
  public static <K, V> Map<V, K> swapOrdered(
      Map<K, V> map
  ) {
    if (map.isEmpty()) {

      return Map.of();
    }
    var result = map.entrySet()
        .stream()
        .peek(entry ->
            validate(entry).ifPresent(parametersValidationException -> {
              throw parametersValidationException;
            }))
        .collect(Collectors.toMap(
            Entry::getValue,
            Entry::getKey,
            (kOld, kNew) ->
                kOld, //first-wins collision resolution
            LinkedHashMap::new
        ));

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Map} of the source's elements in reverse order.
   *
   * @param vByK the (assumed to be) <u><i>ordered</i></u> source of the entries
   * @param <K>  the type of the key instances of the entry in the stream
   * @param <V>  the type of the value instances of the entry in the stream
   * @return an unmodifiable <u><i>ordered</i></u> {@link Map} of the source's elements in reverse order
   */
  public static <K, V> Map<K, V> reverse(
      Map<K, V> vByK
  ) {
    if (!vByK.isEmpty()) {

      return reverse(vByK.entrySet().stream());
    }

    return Map.of();
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@link Map} of the source's elements in reverse order.
   *
   * @param stream the (assumed to be) <u><i>ordered</i></u> source of the entries
   * @param <K>    the type of the key instances of the entry in the stream
   * @param <V>    the type of the value instances of the entry in the stream
   * @return an unmodifiable <u><i>ordered</i></u> {@link Map} of the source's elements in reverse order
   */
  public static <K, V> Map<K, V> reverse(
      Stream<Entry<K, V>> stream
  ) {
    var mutableList = stream.collect(Collectors.toList());
    if (!mutableList.isEmpty()) {
      Collections.reverse(mutableList);

      return toMapOrdered(mutableList.stream());
    }

    return Map.of();
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Map} containing the (filtered to non-null, including key and
   * value) entries.
   *
   * @param kAndVs the source of the entries
   * @param <K>    the type of the key instances of the entry in the source
   * @param <V>    the type of the value instances of the entry in the source
   * @return an unmodifiable <u><i>ordered</i></u> {@code Map} containing the (filtered to non-null, including key and
   *     value) entries
   * @throws IllegalArgumentException if any key instance is duplicated; i.e. all keys must be unique, and identifies
   *                                  the {@code key}(s) causing the collision
   */
  @SuppressWarnings("ConstantValue")
  @SafeVarargs
  public static <K, V> Map<K, V> ofEntriesOrdered(
      Entry<K, V>... kAndVs
  ) {
    if (kAndVs.length > 0) {
      var result = new LinkedHashMap<K, V>();
      var duplicates = new ArrayList<Entry<K, V>>();
      Arrays.stream(kAndVs)
          .filter(kAndV ->
              Objects.nonNull(kAndV) && isNonNulls(kAndV))
          .forEachOrdered(kAndV -> {
            if (result.put(kAndV.getKey(), kAndV.getValue()) != null) {
              duplicates.add(kAndV);
            }
          });
      if (!duplicates.isEmpty()) {
        throw new IllegalArgumentException("duplicate keys encountered - %s".formatted(
            String.join(
                ",",
                duplicates
                    .stream()
                    .map(kAndV ->
                        kAndV.getKey().toString())
                    .toList())));
      }

      return !result.isEmpty()
          ? Collections.unmodifiableMap(result)
          : Map.of();
    }

    return Map.of();
  }

  /**
   * Returns the passed in <u><i>mutable</i></u> {@code Map}, if an entry was successfully added/appended without
   * displacing a pre-existing entry, otherwise throws an {@link IllegalArgumentException} that identifies the
   * {@code key} causing the collision.
   * <p>
   * ---
   * <p>
   * <b>WARNING:</b> This is a <b>SIDE-EFFECTING</b> method in that it modifies the {@code mutableMap} parameter.
   *
   * @param mutableMap the map into which the entry will be added/appended - SIDE EFFECTING
   * @param key        the key with which to associate with value
   * @param value      the value with which to associate with the key
   * @param <K>        the type of the key instances
   * @param <V>        the type of the value instances
   * @return the passed in <u><i>mutable</i></u> {@code Map}, if an entry was successfully added/appended without
   *     displacing a pre-existing entry, otherwise throws an {@link IllegalArgumentException} that identifies the
   *     {@code key} causing the collision
   */
  @SuppressWarnings("UnusedReturnValue")
  private static <K, V> Map<K, V> put(
      Map<K, V> mutableMap,
      K key,
      V value
  ) {
    if (mutableMap.put(key, value) != null) {
      throw new IllegalArgumentException("duplicate key: " + key);
    }

    return mutableMap;
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Map} containing a single mapping.
   *
   * @param key1   the key with which to associate with value1
   * @param value1 the value with which to associate with the key1
   * @param <K>    the type of the key instances
   * @param <V>    the type of the value instances
   * @return an unmodifiable <u><i>ordered</i></u> {@code Map} containing as single mapping
   * @throws NullPointerException if the key or the value is {@code null}
   */
  public static <K, V> Map<K, V> ofOrdered(
      K key1, V value1
  ) {
    var result = new LinkedHashMap<K, V>();
    result.put(key1, value1);

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Map} containing two mappings.
   *
   * @param key1   the key with which to associate with value1
   * @param value1 the value with which to associate with the key1
   * @param key2   the key with which to associate with value2
   * @param value2 the value with which to associate with the key2
   * @param <K>    the type of the key instances
   * @param <V>    the type of the value instances
   * @return an unmodifiable <u><i>ordered</i></u> {@code Map} containing two mappings
   * @throws IllegalArgumentException if the keys are not unique
   * @throws NullPointerException     if any key or value is {@code null}
   */
  public static <K, V> Map<K, V> ofOrdered(
      K key1, V value1,
      K key2, V value2
  ) {
    var result = new LinkedHashMap<K, V>();
    result.put(key1, value1);
    put(result, key2, value2);

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Map} containing three mappings.
   *
   * @param key1   the key with which to associate with value1
   * @param value1 the value with which to associate with the key1
   * @param key2   the key with which to associate with value2
   * @param value2 the value with which to associate with the key2
   * @param key3   the key with which to associate with value3
   * @param value3 the value with which to associate with the key3
   * @param <K>    the type of the key instances
   * @param <V>    the type of the value instances
   * @return an unmodifiable <u><i>ordered</i></u> {@code Map} containing three mappings
   * @throws IllegalArgumentException if the keys are not unique
   * @throws NullPointerException     if any key or value is {@code null}
   */
  public static <K, V> Map<K, V> ofOrdered(
      K key1, V value1,
      K key2, V value2,
      K key3, V value3
  ) {
    var result = new LinkedHashMap<K, V>();
    result.put(key1, value1);
    put(result, key2, value2);
    put(result, key3, value3);

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Map} containing four mappings.
   *
   * @param key1   the key with which to associate with value1
   * @param value1 the value with which to associate with the key1
   * @param key2   the key with which to associate with value2
   * @param value2 the value with which to associate with the key2
   * @param key3   the key with which to associate with value3
   * @param value3 the value with which to associate with the key3
   * @param key4   the key with which to associate with value4
   * @param value4 the value with which to associate with the key4
   * @param <K>    the type of the key instances
   * @param <V>    the type of the value instances
   * @return an unmodifiable <u><i>ordered</i></u> {@code Map} containing four mappings
   * @throws IllegalArgumentException if the keys are not unique
   * @throws NullPointerException     if any key or value is {@code null}
   */
  public static <K, V> Map<K, V> ofOrdered(
      K key1, V value1,
      K key2, V value2,
      K key3, V value3,
      K key4, V value4
  ) {
    var result = new LinkedHashMap<K, V>();
    result.put(key1, value1);
    put(result, key2, value2);
    put(result, key3, value3);
    put(result, key4, value4);

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Map} containing five mappings.
   *
   * @param key1   the key with which to associate with value1
   * @param value1 the value with which to associate with the key1
   * @param key2   the key with which to associate with value2
   * @param value2 the value with which to associate with the key2
   * @param key3   the key with which to associate with value3
   * @param value3 the value with which to associate with the key3
   * @param key4   the key with which to associate with value4
   * @param value4 the value with which to associate with the key4
   * @param key5   the key with which to associate with value5
   * @param value5 the value with which to associate with the key5
   * @param <K>    the type of the key instances
   * @param <V>    the type of the value instances
   * @return an unmodifiable <u><i>ordered</i></u> {@code Map} containing five mappings
   * @throws IllegalArgumentException if the keys are not unique
   * @throws NullPointerException     if any key or value is {@code null}
   */
  @SuppressWarnings("DuplicatedCode")
  public static <K, V> Map<K, V> ofOrdered(
      K key1, V value1,
      K key2, V value2,
      K key3, V value3,
      K key4, V value4,
      K key5, V value5
  ) {
    var result = new LinkedHashMap<K, V>();
    result.put(key1, value1);
    put(result, key2, value2);
    put(result, key3, value3);
    put(result, key4, value4);
    put(result, key5, value5);

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Map} containing six mappings.
   *
   * @param key1   the key with which to associate with value1
   * @param value1 the value with which to associate with the key1
   * @param key2   the key with which to associate with value2
   * @param value2 the value with which to associate with the key2
   * @param key3   the key with which to associate with value3
   * @param value3 the value with which to associate with the key3
   * @param key4   the key with which to associate with value4
   * @param value4 the value with which to associate with the key4
   * @param key5   the key with which to associate with value5
   * @param value5 the value with which to associate with the key5
   * @param key6   the key with which to associate with value6
   * @param value6 the value with which to associate with the key6
   * @param <K>    the type of the key instances
   * @param <V>    the type of the value instances
   * @return an unmodifiable <u><i>ordered</i></u> {@code Map} containing six mappings
   * @throws IllegalArgumentException if the keys are not unique
   * @throws NullPointerException     if any key or value is {@code null}
   */
  @SuppressWarnings("DuplicatedCode")
  public static <K, V> Map<K, V> ofOrdered(
      K key1, V value1,
      K key2, V value2,
      K key3, V value3,
      K key4, V value4,
      K key5, V value5,
      K key6, V value6
  ) {
    var result = new LinkedHashMap<K, V>();
    result.put(key1, value1);
    put(result, key2, value2);
    put(result, key3, value3);
    put(result, key4, value4);
    put(result, key5, value5);
    put(result, key6, value6);

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Map} containing seven mappings.
   *
   * @param key1   the key with which to associate with value1
   * @param value1 the value with which to associate with the key1
   * @param key2   the key with which to associate with value2
   * @param value2 the value with which to associate with the key2
   * @param key3   the key with which to associate with value3
   * @param value3 the value with which to associate with the key3
   * @param key4   the key with which to associate with value4
   * @param value4 the value with which to associate with the key4
   * @param key5   the key with which to associate with value5
   * @param value5 the value with which to associate with the key5
   * @param key6   the key with which to associate with value6
   * @param value6 the value with which to associate with the key6
   * @param key7   the key with which to associate with value7
   * @param value7 the value with which to associate with the key7
   * @param <K>    the type of the key instances
   * @param <V>    the type of the value instances
   * @return an unmodifiable <u><i>ordered</i></u> {@code Map} containing seven mappings
   * @throws IllegalArgumentException if the keys are not unique
   * @throws NullPointerException     if any key or value is {@code null}
   */
  @SuppressWarnings("DuplicatedCode")
  public static <K, V> Map<K, V> ofOrdered(
      K key1, V value1,
      K key2, V value2,
      K key3, V value3,
      K key4, V value4,
      K key5, V value5,
      K key6, V value6,
      K key7, V value7
  ) {
    var result = new LinkedHashMap<K, V>();
    result.put(key1, value1);
    put(result, key2, value2);
    put(result, key3, value3);
    put(result, key4, value4);
    put(result, key5, value5);
    put(result, key6, value6);
    put(result, key7, value7);

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Map} containing eight mappings.
   *
   * @param key1   the key with which to associate with value1
   * @param value1 the value with which to associate with the key1
   * @param key2   the key with which to associate with value2
   * @param value2 the value with which to associate with the key2
   * @param key3   the key with which to associate with value3
   * @param value3 the value with which to associate with the key3
   * @param key4   the key with which to associate with value4
   * @param value4 the value with which to associate with the key4
   * @param key5   the key with which to associate with value5
   * @param value5 the value with which to associate with the key5
   * @param key6   the key with which to associate with value6
   * @param value6 the value with which to associate with the key6
   * @param key7   the key with which to associate with value7
   * @param value7 the value with which to associate with the key7
   * @param key8   the key with which to associate with value8
   * @param value8 the value with which to associate with the key8
   * @param <K>    the type of the key instances
   * @param <V>    the type of the value instances
   * @return an unmodifiable <u><i>ordered</i></u> {@code Map} containing eight mappings
   * @throws IllegalArgumentException if the keys are not unique
   * @throws NullPointerException     if any key or value is {@code null}
   */
  @SuppressWarnings("DuplicatedCode")
  public static <K, V> Map<K, V> ofOrdered(
      K key1, V value1,
      K key2, V value2,
      K key3, V value3,
      K key4, V value4,
      K key5, V value5,
      K key6, V value6,
      K key7, V value7,
      K key8, V value8
  ) {
    var result = new LinkedHashMap<K, V>();
    result.put(key1, value1);
    put(result, key2, value2);
    put(result, key3, value3);
    put(result, key4, value4);
    put(result, key5, value5);
    put(result, key6, value6);
    put(result, key7, value7);
    put(result, key8, value8);

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Map} containing nine mappings.
   *
   * @param key1   the key with which to associate with value1
   * @param value1 the value with which to associate with the key1
   * @param key2   the key with which to associate with value2
   * @param value2 the value with which to associate with the key2
   * @param key3   the key with which to associate with value3
   * @param value3 the value with which to associate with the key3
   * @param key4   the key with which to associate with value4
   * @param value4 the value with which to associate with the key4
   * @param key5   the key with which to associate with value5
   * @param value5 the value with which to associate with the key5
   * @param key6   the key with which to associate with value6
   * @param value6 the value with which to associate with the key6
   * @param key7   the key with which to associate with value7
   * @param value7 the value with which to associate with the key7
   * @param key8   the key with which to associate with value8
   * @param value8 the value with which to associate with the key8
   * @param key9   the key with which to associate with value9
   * @param value9 the value with which to associate with the key9
   * @param <K>    the type of the key instances
   * @param <V>    the type of the value instances
   * @return an unmodifiable <u><i>ordered</i></u> {@code Map} containing nine mappings
   * @throws IllegalArgumentException if the keys are not unique
   * @throws NullPointerException     if any key or value is {@code null}
   */
  @SuppressWarnings("DuplicatedCode")
  public static <K, V> Map<K, V> ofOrdered(
      K key1, V value1,
      K key2, V value2,
      K key3, V value3,
      K key4, V value4,
      K key5, V value5,
      K key6, V value6,
      K key7, V value7,
      K key8, V value8,
      K key9, V value9
  ) {
    var result = new LinkedHashMap<K, V>();
    result.put(key1, value1);
    put(result, key2, value2);
    put(result, key3, value3);
    put(result, key4, value4);
    put(result, key5, value5);
    put(result, key6, value6);
    put(result, key7, value7);
    put(result, key8, value8);
    put(result, key9, value9);

    return Collections.unmodifiableMap(result);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> {@code Map} containing ten mappings.
   *
   * @param key1    the key with which to associate with value1
   * @param value1  the value with which to associate with the key1
   * @param key2    the key with which to associate with value2
   * @param value2  the value with which to associate with the key2
   * @param key3    the key with which to associate with value3
   * @param value3  the value with which to associate with the key3
   * @param key4    the key with which to associate with value4
   * @param value4  the value with which to associate with the key4
   * @param key5    the key with which to associate with value5
   * @param value5  the value with which to associate with the key5
   * @param key6    the key with which to associate with value6
   * @param value6  the value with which to associate with the key6
   * @param key7    the key with which to associate with value7
   * @param value7  the value with which to associate with the key7
   * @param key8    the key with which to associate with value8
   * @param value8  the value with which to associate with the key8
   * @param key9    the key with which to associate with value9
   * @param value9  the value with which to associate with the key9
   * @param key10   the key with which to associate with value10
   * @param value10 the value with which to associate with the key10
   * @param <K>     the type of the key instances
   * @param <V>     the type of the value instances
   * @return an unmodifiable <u><i>ordered</i></u> {@code Map} containing ten mappings
   * @throws IllegalArgumentException if the keys are not unique
   * @throws NullPointerException     if any key or value is {@code null}
   */
  @SuppressWarnings("DuplicatedCode")
  public static <K, V> Map<K, V> ofOrdered(
      K key1, V value1,
      K key2, V value2,
      K key3, V value3,
      K key4, V value4,
      K key5, V value5,
      K key6, V value6,
      K key7, V value7,
      K key8, V value8,
      K key9, V value9,
      K key10, V value10
  ) {
    var result = new LinkedHashMap<K, V>();
    result.put(key1, value1);
    put(result, key2, value2);
    put(result, key3, value3);
    put(result, key4, value4);
    put(result, key5, value5);
    put(result, key6, value6);
    put(result, key7, value7);
    put(result, key8, value8);
    put(result, key9, value9);
    put(result, key10, value10);

    return Collections.unmodifiableMap(result);
  }
}
