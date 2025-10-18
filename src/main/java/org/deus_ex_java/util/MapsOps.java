package org.deus_ex_java.util;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.util.refined.NonEmptyMap;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class providing static methods to create {@link Map} instances.
 */
public class MapsOps {

  private MapsOps() {
    throw new UnsupportedOperationException("suppressing class instantiation");
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
  @NotNull
  public static <K, V> Map<K, V> nullToEmpty(@Nullable Map<K, V> mapKv) {
    return mapKv != null
        ? mapKv
        : Map.of();
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
      @NotNull Entry<K, V> entry
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
      @NotNull Entry<K, V> entry
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
   * Returns a new unmodifiable unordered {@link Map} from an existing {@code map}, removing every {@link Entry} from
   * {@code entrySet} where either the key or the value is {@code null}.
   *
   * @param map the source of the existing key/value pairs
   * @param <K> the type of the keys contained in the {@code map}
   * @param <V> the type of the values contained in the {@code map}
   * @return a new unmodifiable unordered {@link Map} from an existing {@code map}, removing every {@link Entry} from
   *     {@code entrySet} where either the key or the value is {@code null}
   */
  public static <K, V> Map<K, V> filterToNonNulls(
      @NotNull Map<K, V> map
  ) {
    return map
        .entrySet()
        .stream()
        .filter(MapsOps::isNonNulls)
        .collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue));
  }

  /**
   * Returns a new unmodifiable unordered {@link Map} from an existing {@code map}, adding/updating an {@link Entry}.
   *
   * @param map   the source of the existing key/value pairs
   * @param entry the key/value pair as an {@link Entry}
   * @param <K>   the type of the keys contained in the {@code map}
   * @param <V>   the type of the values contained in the {@code map}
   * @return a new unmodifiable unordered {@link Map} from an existing {@code map}, adding/updating an {@link Entry}
   * @throws NullPointerException if the provided {@link Entry} contains {@code null} in either its key or value.
   */
  @NotNull
  public static <K, V> Map<K, V> addEntry(
      @NotNull Map<K, V> map,
      @NotNull Entry<K, V> entry
  ) {
    validate(entry)
        .ifPresent(parametersValidationException -> {
          throw parametersValidationException;
        });

    return addKeyAndValue(map, entry.getKey(), entry.getValue());
  }

  /**
   * Returns a new unmodifiable unordered {@link Map} from an existing {@code map}, adding/updating a {@code key} and
   * its associated {@code value}.
   *
   * @param map   the source of the existing key/value pairs
   * @param key   the key with which to associate with the value
   * @param value the value with which to associate with the key
   * @param <K>   the type of the keys contained in the {@code map}
   * @param <V>   the type of the values contained in the {@code map}
   * @return a new unmodifiable unordered {@link Map} from an existing {@code map}, adding/updating a {@code key} and
   *     its associated {@code value}
   */
  @NotNull
  public static <K, V> Map<K, V> addKeyAndValue(
      @NotNull Map<K, V> map,
      @NotNull K key,
      @NotNull V value
  ) {
    var result = new HashMap<>(map);
    result.put(key, value);

    return Collections.unmodifiableMap(result);

  }

  /**
   * Returns a new unmodifiable <u><i>ordered</i></u> {@link Map} from an existing {@code map}, appending (or if the key
   * is already present, updating) an {@link Entry}.
   *
   * @param map   the (assumed to be) <u><i>ordered</i></u> source of the existing key/value pairs
   * @param entry the key/value pair as an {@link Entry}
   * @param <K>   the type of the keys contained in the {@code map}
   * @param <V>   the type of the values contained in the {@code map}
   * @return a new unmodifiable <u><i>ordered</i></u> {@link Map} from an existing {@code map}, appending (or if the key
   *     is already present, updating) an {@link Entry}
   * @throws NullPointerException if the provided {@link Entry} contains {@code null} in either its key or value.
   */
  @NotNull
  public static <K, V> Map<K, V> appendEntry(
      @NotNull Map<K, V> map,
      @NotNull Entry<K, V> entry
  ) {
    validate(entry)
        .ifPresent(parametersValidationException -> {
          throw parametersValidationException;
        });

    return appendKeyAndValue(map, entry.getKey(), entry.getValue());
  }

  /**
   * Returns a new unmodifiable <u><i>ordered</i></u> {@link Map} from an existing {@code map}, appending (or if the key
   * is already present, updating) a {@code key} and its associated {@code value}.
   *
   * @param map   the (assumed to be) <u><i>ordered</i></u> source of the existing key/value pairs
   * @param key   the key with which to associate with the value
   * @param value the value with which to associate with the key
   * @param <K>   the type of the keys contained in the {@code map}
   * @param <V>   the type of the values contained in the {@code map}
   * @return a new unmodifiable <u><i>ordered</i></u> {@link Map} from an existing {@code map}, appending (or if the key
   *     is already present, updating) a {@code key} and its associated {@code value}
   */
  public static <K, V> Map<K, V> appendKeyAndValue(
      @NotNull Map<K, V> map,
      @NotNull K key,
      @NotNull V value
  ) {
    if (!map.isEmpty()) {
      var result = new LinkedHashMap<>(map);
      result.put(key, value);

      return Collections.unmodifiableMap(result);
    }

    return Map.of(key, value);
  }

  /**
   * Returns an unmodifiable unordered map consisting of each map, filtered to non-null in both the keys and the values,
   * from maps added together.
   *
   * @param maps the maps to append
   * @param <K>  the type of the keys contained in the {@code map}
   * @param <V>  the type of the values contained in the {@code map}
   * @return an unmodifiable unordered map consisting of each map ,filtered to non-null in both the keys and the values,
   *     from maps added together
   */
  @SuppressWarnings("ConstantValue")
  @NotNull
  @SafeVarargs
  public static <K, V> Map<K, V> addMaps(
      @NotNull Map<K, V>... maps
  ) {
    if (maps.length > 0) {
      var result = new HashMap<K, V>();
      IntStream.range(0, maps.length)
          .forEach(index -> {
            var map = maps[index];
            if (map != null) {
              var resolvedMap = filterToNonNulls(map);
              if (!resolvedMap.isEmpty()) {
                result.putAll(resolvedMap);
              }
            }
          });

      return !result.isEmpty()
          ? Collections.unmodifiableMap(result)
          : Map.of();
    }

    return Map.of();
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> map consisting of each map, filtered to non-null in both the keys and
   * the values, from maps appended together.
   *
   * @param maps the (assumed to be) <u><i>ordered</i></u> maps to append
   * @param <K>  the type of the keys contained in the {@code map}
   * @param <V>  the type of the values contained in the {@code map}
   * @return an unmodifiable <u><i>ordered</i></u> map consisting of each map, filtered to non-null in both the keys and
   *     the values, from maps appended together
   */
  @SuppressWarnings("ConstantValue")
  @NotNull
  @SafeVarargs
  public static <K, V> Map<K, V> appendMaps(
      @NotNull Map<K, V>... maps
  ) {
    if (maps.length > 0) {
      var result = new LinkedHashMap<K, V>();
      IntStream.range(0, maps.length)
          .forEach(index -> {
            var map = maps[index];
            if (map != null) {
              var resolvedMap = toMapOrdered(map.entrySet().stream());
              if (!resolvedMap.isEmpty()) {
                result.putAll(resolvedMap);
              }
            }
          });

      return !result.isEmpty()
          ? Collections.unmodifiableMap(result)
          : Map.of();
    }

    return Map.of();
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
  @NotNull
  public static <T, K, V> Map<K, V> toMap(
      @NotNull Stream<Entry<K, V>> kAndVs
  ) {
    return toMap(kAndVs, Optional::of);
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
  @NotNull
  public static <T, K, V> Map<K, V> toMap(
      @NotNull Stream<T> ts,
      @NotNull Function<T, Optional<Entry<K, V>>> fTtoOptionalEntry
  ) {
    var map = ts
        .filter(t ->
            !Objects.isNull(t))
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
   * @param kAndVs the (assumed to be) <u><i>ordered</i></u> source of the entries
   * @param <K>    the type of the key in the entries
   * @param <V>    the type of the value in the entries
   * @return an unmodifiable <u><i>ordered</i></u> map filtering out each {@code Entry} where it is {@code null},
   *     otherwise filtering the entry out where either the contained entry's key and/or the value are {@code null}, and
   *     then if the entry remains, it is ignored if it contains a duplicate key
   */
  @NotNull
  public static <T, K, V> Map<K, V> toMapOrdered(
      @NotNull Stream<Entry<K, V>> kAndVs
  ) {
    return toMapOrdered(kAndVs, Optional::of);
  }

  /**
   * Returns an unmodifiable <u><i>ordered</i></u> map filtering out each {@code T} where it is {@code null}, otherwise
   * optionally transforming the {@code T} via the function, {@code fTtoOptionalEntry}, into an {@link Optional}
   * {@link Entry}, and then filtering the {@link Optional} out where either the contained entry's key and/or the value
   * are {@code null}, and then if the entry remains, it is ignored if it contains a duplicate key.
   *
   * @param ts                the (assumed to be) <u><i>ordered</i></u> source of the input to create entries
   * @param fTtoOptionalEntry function to filter and/or transform a T into an instance of {@link Entry}
   * @param <T>               the type of the source value the entries
   * @param <K>               the type of the key in the entries
   * @param <V>               the type of the value in the entries
   * @return an unmodifiable <u><i>ordered</i></u> map filtering out each {@code T} where it is {@code null}, otherwise
   *     optionally transforming the {@code T} via the function, {@code fTtoOptionalEntry}, into an {@link Optional}
   *     {@link Entry}, and then filtering the {@link Optional} out where either the contained entry's key and/or the
   *     value are {@code null}, and then if the entry remains, it is ignored if it contains a duplicate key
   */
  @NotNull
  public static <T, K, V> Map<K, V> toMapOrdered(
      @NotNull Stream<T> ts,
      @NotNull Function<T, Optional<Entry<K, V>>> fTtoOptionalEntry
  ) {
    var map = ts
        .filter(t ->
            !Objects.isNull(t))
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
  @NotNull
  public static <K, V> Map<V, K> swap(
      @NotNull Map<K, V> map
  ) {
    return !map.isEmpty()
        //@formatter:off
        ? map.entrySet()
            .stream()
            .peek(entry ->
                validate(entry)
                    .ifPresent(parametersValidationException -> {
                      throw parametersValidationException;
                    }))
            .collect(Collectors.toUnmodifiableMap(Entry::getValue, Entry::getKey))
        : Map.of();
        //@formatter:on
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
  @NotNull
  public static <K, V> Map<V, K> swapOrdered(
      @NotNull Map<K, V> map
  ) {
    if (!map.isEmpty()) {
      var result = new LinkedHashMap<V, K>();
      //noinspection SimplifyStreamApiCallChains
      map.entrySet()
          .stream()
          .forEachOrdered(
              entry -> {
                validate(entry)
                    .ifPresent(parametersValidationException -> {
                      throw parametersValidationException;
                    });
                result.put(entry.getValue(), entry.getKey());
              });

      return Collections.unmodifiableMap(result);
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
  @NotNull
  private static <K, V> Map<K, V> put(
      @NotNull Map<K, V> mutableMap,
      @NotNull K key,
      @NotNull V value
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
  @NotNull
  public static <K, V> Map<K, V> ofOrdered(
      @NotNull K key1, @NotNull V value1
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
  @NotNull
  public static <K, V> Map<K, V> ofOrdered(
      @NotNull K key1, @NotNull V value1,
      @NotNull K key2, @NotNull V value2
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
  @NotNull
  public static <K, V> Map<K, V> ofOrdered(
      @NotNull K key1, @NotNull V value1,
      @NotNull K key2, @NotNull V value2,
      @NotNull K key3, @NotNull V value3
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
  @NotNull
  public static <K, V> Map<K, V> ofOrdered(
      @NotNull K key1, @NotNull V value1,
      @NotNull K key2, @NotNull V value2,
      @NotNull K key3, @NotNull V value3,
      @NotNull K key4, @NotNull V value4
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
  @NotNull
  public static <K, V> Map<K, V> ofOrdered(
      @NotNull K key1, @NotNull V value1,
      @NotNull K key2, @NotNull V value2,
      @NotNull K key3, @NotNull V value3,
      @NotNull K key4, @NotNull V value4,
      @NotNull K key5, @NotNull V value5
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
  @NotNull
  public static <K, V> Map<K, V> ofOrdered(
      @NotNull K key1, @NotNull V value1,
      @NotNull K key2, @NotNull V value2,
      @NotNull K key3, @NotNull V value3,
      @NotNull K key4, @NotNull V value4,
      @NotNull K key5, @NotNull V value5,
      @NotNull K key6, @NotNull V value6
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
  @NotNull
  public static <K, V> Map<K, V> ofOrdered(
      @NotNull K key1, @NotNull V value1,
      @NotNull K key2, @NotNull V value2,
      @NotNull K key3, @NotNull V value3,
      @NotNull K key4, @NotNull V value4,
      @NotNull K key5, @NotNull V value5,
      @NotNull K key6, @NotNull V value6,
      @NotNull K key7, @NotNull V value7
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
  @NotNull
  public static <K, V> Map<K, V> ofOrdered(
      @NotNull K key1, @NotNull V value1,
      @NotNull K key2, @NotNull V value2,
      @NotNull K key3, @NotNull V value3,
      @NotNull K key4, @NotNull V value4,
      @NotNull K key5, @NotNull V value5,
      @NotNull K key6, @NotNull V value6,
      @NotNull K key7, @NotNull V value7,
      @NotNull K key8, @NotNull V value8
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
  @NotNull
  public static <K, V> Map<K, V> ofOrdered(
      @NotNull K key1, @NotNull V value1,
      @NotNull K key2, @NotNull V value2,
      @NotNull K key3, @NotNull V value3,
      @NotNull K key4, @NotNull V value4,
      @NotNull K key5, @NotNull V value5,
      @NotNull K key6, @NotNull V value6,
      @NotNull K key7, @NotNull V value7,
      @NotNull K key8, @NotNull V value8,
      @NotNull K key9, @NotNull V value9
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
  @NotNull
  public static <K, V> Map<K, V> ofOrdered(
      @NotNull K key1, @NotNull V value1,
      @NotNull K key2, @NotNull V value2,
      @NotNull K key3, @NotNull V value3,
      @NotNull K key4, @NotNull V value4,
      @NotNull K key5, @NotNull V value5,
      @NotNull K key6, @NotNull V value6,
      @NotNull K key7, @NotNull V value7,
      @NotNull K key8, @NotNull V value8,
      @NotNull K key9, @NotNull V value9,
      @NotNull K key10, @NotNull V value10
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
