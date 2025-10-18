package org.deus_ex_java.util;

import org.deus_ex_java.lang.ClassesOps;
import org.deus_ex_java.lang.ParametersValidationException;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.Entry;
import static java.util.Map.entry;

/**
 * An {@link EnumsOps} is a thread-safe immutable non-{@code null} utility class that produces a system-wide singleton
 * for augmenting the {@link Enum}'s mutable {@code values()} array by wrapping it in an unmodifiable {@link List}.
 * <p>
 * It also produces an internal cache for enabling a fast O(1) case-insensitive name search for a specific {@link Enum}
 * value (as opposed to the slower O(n) common pattern scanning through the mutable {@code values()} array).
 * <p>
 * The {@link EnumsOps} class may be added as a property directly to a newly defined {@link Enum}. And/or, it can be
 * used to wrap a pre-existing {@link Enum} which is unable to be directly enhanced/modified (for example, it is part of
 * a vendor SDK).
 * <p>
 * Both methods provide all the same functionality, augmentation, and enhancement. And because it is a system-wide
 * singleton, implementing both will result in the exact same instance being returned for both regardless of context.
 * <p>
 *
 * @param <E> type of the {@link Enum}
 */
public final class EnumsOps<E extends Enum<E>> {

  public static final String DEFAULT_SEPARATOR = ", ";

  private static final Object ENUM_OPS_BY_CLASS_E_SYNC = new Object();
  private static volatile Memoizer<Class<?>, EnumsOps<?>> ENUM_OPS_BY_CLASS_E;

  /**
   * Returns an {@link EnumsOps} <i>singleton</i> for the provided {@link Enum}'s class.
   * <p>
   * Due to use of a thread-safe internal cache, upon the first call to this factory method with a specific {@link Enum}
   * class, an instance of {@link EnumsOps} will be generated stored within the internal cache. All future calls to this
   * factory method specifying the same {@link Enum} class (at least within the same {@link ClassLoader}) will ensure
   * the same {@link EnumsOps} instance is returned. There are no means provided to allow additional independent
   * instances of {@link EnumsOps} for the same {@link Enum} class.
   *
   * @param classE the {@link Class} of the specific enum being augmented
   * @param <E>    the specific Enum's type
   * @return an {@link EnumsOps} <i>singleton</i> for the provided {@link Enum}'s class
   */
  @NotNull
  public static <E extends Enum<E>> EnumsOps<E> from(@NotNull Class<E> classE) {
    if (ENUM_OPS_BY_CLASS_E == null) {
      synchronized (ENUM_OPS_BY_CLASS_E_SYNC) {
        if (ENUM_OPS_BY_CLASS_E == null) {
          //noinspection unchecked
          ENUM_OPS_BY_CLASS_E = Memoizer.from(classWildcard ->
              ClassesOps.narrow(() ->
                      new EnumsOps<>((Class<E>) classWildcard))
                  .orElseThrow(() ->
                      new IllegalStateException("unable to narrow to Class<E> for class " + classWildcard.getName())));
        }
      }
    }

    //noinspection unchecked
    return ClassesOps.narrow(() ->
            (EnumsOps<E>) ENUM_OPS_BY_CLASS_E.get(classE))
        .orElseThrow(() ->
            new IllegalStateException("unable to narrow to EnumsOps<E> for class " + classE.getName()));
  }

  private final Class<E> classE;
  private final List<E> enumsValues;
  private final Map<String, E> orderedMapEnumValueByNameLowerCase;

  private EnumsOps(@NotNull Class<E> classE) {
    var enumsValues = Collections.unmodifiableList(Arrays.asList(classE.getEnumConstants()));
    var nameLowerCaseAndEnumValues = enumsValues
        .stream()
        .map(enumValue ->
            entry(
                enumValue.name().toLowerCase(),
                enumValue))
        .toList();
    //validate name().toLowerCase() are distinct as all 3 valueOf* methods depend
    //  upon this assumption.
    var nameLowerCaseAndEnumValueCollisions =
        nameLowerCaseAndEnumValues
            .stream()
            .collect(Collectors.groupingBy(Map.Entry::getKey))
            .values()
            .stream()
            .filter(es ->
                es.size() > 1)
            .flatMap(Collection::stream)
            .toList();
    if (!nameLowerCaseAndEnumValueCollisions.isEmpty()) {
      throw new ParametersValidationException(
          "EnumsOps invalid parameter(s)",
          "invalid state for enum [%s] where name().toLowerCase() is not unique across all the enums values - erred values: %s".formatted(
              classE.getSimpleName(),
              String.join(
                  DEFAULT_SEPARATOR,
                  nameLowerCaseAndEnumValueCollisions
                      .stream()
                      .sorted(
                          Comparator.<Entry<String, E>, Integer>comparing(entry ->
                                  entry.getValue().ordinal())
                              .thenComparing(Entry::getKey))
                      .map(entry ->
                          "keyLowerCase: %s -> enumValueName: %s".formatted(
                              entry.getKey(),
                              entry.getValue()))
                      .toList())));
    }
    //all preconditions have been validated, so assign the instance fields
    this.classE = classE;
    this.enumsValues = enumsValues;
    this.orderedMapEnumValueByNameLowerCase = MapsOps.toMapOrdered(
        enumsValues
            .stream()
            .map(enumValue ->
                entry(
                    enumValue.name().toLowerCase(),
                    enumValue)));
  }

  /**
   * Returns the {@link Class} of the enum being augmented.
   *
   * @return the {@link Class} of the enum being augmented
   */
  @NotNull
  public Class<E> getClassE() {
    return this.classE;
  }

  /**
   * Returns the {@link Enum}'s mutable array {@code values} as an unmodifiable {@link List}.
   *
   * @return the {@link Enum}'s mutable array {@code values} as an unmodifiable {@link List}
   */
  @NotNull
  public List<E> toList() {
    return this.enumsValues;
  }

  /**
   * Returns the {@link Enum}'s mutable array {@code values} as a {@link Stream}.
   *
   * @return the {@link Enum}'s mutable array {@code values} as a {@link Stream}
   */
  @NotNull
  public Stream<E> stream() {
    return this.enumsValues.stream();
  }

  /**
   * Returns the {@link Enum}'s mutable array {@code values} as an unmodifiable {@link Set} specifically wrapping with
   * the highly performant {@link EnumSet}.
   *
   * @return the {@link Enum}'s mutable array {@code values} as an unmodifiable {@link Set} specifically wrapping with
   *     the highly performant {@link EnumSet}
   */
  @NotNull
  public Set<E> toOrderedSet() {
    return Collections.unmodifiableSet(EnumSet.allOf(this.classE));
  }

  /**
   * Returns the {@link Enum}'s mutable array {@code values} as an unmodifiable <i>ordered</i> {@link Map} where the key
   * is the {@link Enum#name()}, and the enum constant itself is the value.
   *
   * @return the {@link Enum}'s mutable array {@code values} as an unmodifiable <i>ordered</i> {@link Map} where the key
   *     is the {@link Enum#name()}, and the enum constant itself is the value
   */
  @NotNull
  public Map<String, E> toOrderedMapByName() {
    return MapsOps.toMapOrdered(
        stream(),
        (e) ->
            Optional.of(entry(e.name(), e)));
  }

  /**
   * Performs a non-interfering action for each element of this stream.
   *
   * @param consumer a non-interfering action to perform on the elements
   */
  public void forEach(@NotNull Consumer<E> consumer) {
    stream().forEach(consumer);
  }

  /**
   * Performs a non-interfering action for each element of this stream, in the encounter order of the stream if the
   * stream has a defined encounter order.
   *
   * @param consumer a non-interfering action to perform on the elements
   */
  public void forEachOrdered(@NotNull Consumer<E> consumer) {
    stream().forEachOrdered(consumer);
  }

  /**
   * Returns the case-insensitive search by name for the enum value, otherwise the first enum value in
   * {@link EnumsOps#toList}.
   *
   * @param search the name used to locate the enum value, case-insensitive
   * @return the case-insensitive search by name for the enum value, otherwise the first enum value in
   *     {@link EnumsOps#toList}
   */
  @NotNull
  public E valueOfOrDefaultToFirst(
      @NotNull String search
  ) {
    return valueOf(search, this.enumsValues.get(0));
  }

  /**
   * Returns the case-insensitive search by name for the enum value, otherwise the {@code orElseDefault}.
   *
   * @param search        the name used to locate the enum value, case-insensitive
   * @param orElseDefault the value to provide if the enum value cannot be found by its case-insensitive name
   * @return the case-insensitive search by name for the enum value, otherwise the {@code orElseDefault}
   */
  @NotNull
  public E valueOf(
      @NotNull String search,
      @NotNull E orElseDefault
  ) {
    return valueOf(search)
        .orElse(orElseDefault);
  }

  /**
   * Returns an {@link Optional} wrapping the case-insensitive search by name for the enum, otherwise an empty
   * {@link Optional}.
   *
   * @param search the name used to locate the enum value, case-insensitive
   * @return an {@link Optional} wrapping the case-insensitive search by name for the enum, otherwise an empty
   *     {@link Optional}
   */
  @NotNull
  public Optional<E> valueOf(
      @NotNull String search
  ) {
    return Optional.ofNullable(
        this.orderedMapEnumValueByNameLowerCase
            .get(search.toLowerCase()));
  }

  /**
   * Returns a new {@code String} composed of copies of the {@code Enum name} for all the enum values joined together
   * with a copy of the {@link EnumsOps#DEFAULT_SEPARATOR}.
   *
   * @return a new {@code String} composed of copies of the {@code Enum name} for all the enum values joined together
   *     with a copy of the {@link EnumsOps#DEFAULT_SEPARATOR}
   */
  @NotNull
  public String join() {
    return join(DEFAULT_SEPARATOR);
  }

  /**
   * Returns a new {@code String} composed of copies of the {@code Enum name} for all the enum values joined together
   * with a copy of the specified {@code separator}.
   *
   * @param separator the string used to separate the enum values
   * @return a new {@code String} composed of copies of the {@code Enum name} for all the enum values joined together
   *     with a copy of the specified {@code separator}
   */
  @NotNull
  public String join(@NotNull String separator) {
    return join(Enum::name, separator);
  }

  /**
   * Returns a new {@code String} composed of copies of the {@code Enum} transformed by the {@code eToString} function,
   * and joined together with a copy of the {@link EnumsOps#DEFAULT_SEPARATOR}.
   *
   * @param eToString the function to transform an enum value into a String
   * @return a new {@code String} composed of copies of the {@code Enum} transformed by the {@code eToString} function,
   *     and joined together with a copy of the {@link EnumsOps#DEFAULT_SEPARATOR}
   */
  @NotNull
  public String join(@NotNull Function<E, String> eToString) {
    return join(eToString, DEFAULT_SEPARATOR);
  }

  /**
   * Returns a new {@code String} composed of copies of the {@code Enum} transformed by the {@code eToString} function,
   * and joined together with a copy of the specified {@code separator}.
   *
   * @param eToString the function to transform an enum value into a String
   * @param separator the string used to separate the enum values
   * @return a new {@code String} composed of copies of the {@code Enum} transformed by the {@code eToString} function,
   *     and joined together with a copy of the specified {@code separator}
   */
  @NotNull
  public String join(
      @NotNull Function<E, String> eToString,
      @NotNull String separator
  ) {
    return join(stream(), eToString, separator);
  }

  /**
   * Returns a new {@code String} composed of copies of the {@link Enum#name()} of each of the provided {@link Enum}s
   * joined together with a copy of the {@link EnumsOps#DEFAULT_SEPARATOR}.
   *
   * @param es the list of enum values to use
   * @return a new {@code String} composed of copies of the {@link Enum#name()} of each of the provided {@link Enum}s
   *     joined together with a copy of the {@link EnumsOps#DEFAULT_SEPARATOR}
   */
  @NotNull
  public String join(@NotNull Stream<E> es) {
    return join(es, Enum::name);
  }

  /**
   * Returns a new {@code String} composed of copies of the provided {@code Enum}s transformed by the {@code eToString}
   * function, and joined together with a copy of the {@link EnumsOps#DEFAULT_SEPARATOR}.
   *
   * @param es        the list of enum values to use
   * @param eToString the function to transform an enum value into a String
   * @return a new {@code String} composed of copies of the provided {@code Enum}s transformed by the {@code eToString}
   *     function, and joined together with a copy of the {@link EnumsOps#DEFAULT_SEPARATOR}
   */
  @NotNull
  public String join(
      @NotNull Stream<E> es,
      @NotNull Function<E, String> eToString
  ) {
    return join(es, eToString, DEFAULT_SEPARATOR);
  }

  /**
   * Returns a new {@code String} composed of copies of the {@link Enum#name()} of each of the provided {@link Enum}s
   * joined together with a copy of the specified {@code separator}.
   *
   * @param es        the list of enum values to use
   * @param separator the string used to separate the enum values
   * @return a new {@code String} composed of copies of the {@link Enum#name()} of each of the provided {@link Enum}s
   *     joined together with a copy of the specified {@code separator}
   */
  @NotNull
  public String join(
      @NotNull Stream<E> es,
      @NotNull String separator
  ) {
    return join(es, Enum::name, separator);
  }

  /**
   * Returns a new {@code String} composed of copies of the provided {@code Enum}s transformed by the {@code eToString}
   * function, and joined together with a copy of the specified {@code separator}.
   *
   * @param es        the list of enum values to use
   * @param eToString the function to transform an enum value into a String
   * @param separator the string used to separate the enum values
   * @return a new {@code String} composed of copies of the provided {@code Enum}s transformed by the {@code eToString}
   *     function, and joined together with a copy of the specified {@code separator}
   */
  @NotNull
  public String join(
      @NotNull Stream<E> es,
      @NotNull Function<E, String> eToString,
      @NotNull String separator
  ) {
    return String.join(
        separator,
        es
            .map(eToString)
            .toList());
  }
}
