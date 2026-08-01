package org.deus_ex_java.util;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.lang.StringsOps;
import org.jspecify.annotations.NullMarked;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
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
@SuppressWarnings({"rawtypes", "unchecked"})
@NullMarked
public final class EnumsOps<E extends Enum<E>> {

  private static final ClassValue<EnumsOps<?>> ENUMS_OPS_CLASS_VALUE_CACHE = new ClassValue<>() {
    @Override
    protected EnumsOps<?> computeValue(Class<?> classE) {
      if (!classE.isEnum()) {

        throw new IllegalArgumentException("classE [%s] must be an enum".formatted(classE.getName()));
      }

      return new EnumsOps(classE);
    }
  };

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
  public static <E extends Enum<E>> EnumsOps<E> from(Class<E> classE) {
    return (EnumsOps<E>) ENUMS_OPS_CLASS_VALUE_CACHE.get(classE);
  }

  private final Class<E> classE;
  private final List<E> enumsList;
  private final Set<E> enumsSet;
  private final FormatBuilder<E> formatBuilder = FormatBuilder.from(this);
  private final Map<String, E> enumValueByNameLowerCase;

  private EnumsOps(Class<E> classE) {
    var enumsList = List.of(classE.getEnumConstants());
    var nameLowerCaseAndEnumValues = enumsList
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
            .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.toUnmodifiableList()))
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
                  FormatBuilder.DEFAULT_SEPARATOR,
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
    this.enumsList = enumsList;
    this.enumsSet = Collections.unmodifiableSet(EnumSet.allOf(this.classE));
    this.enumValueByNameLowerCase = nameLowerCaseAndEnumValues
        .stream()
        .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  /**
   * Returns the {@link Class} of the enum being augmented.
   *
   * @return the {@link Class} of the enum being augmented
   */
  public Class<E> getClassE() {
    return this.classE;
  }

  /**
   * Returns the {@link Enum}'s mutable array {@code values} as an unmodifiable {@link List}.
   *
   * @return the {@link Enum}'s mutable array {@code values} as an unmodifiable {@link List}
   */
  public List<E> toList() {
    return this.enumsList;
  }

  /**
   * Returns the {@link Enum}'s mutable array {@code values} as a {@link Stream}.
   *
   * @return the {@link Enum}'s mutable array {@code values} as a {@link Stream}
   */
  public Stream<E> stream() {
    return this.enumsList.stream();
  }

  /**
   * Returns the {@link Enum}'s mutable array {@code values} as an unmodifiable <i>ordered</i> {@link Set} specifically
   * wrapping with the highly performant {@link EnumSet}.
   *
   * @return the {@link Enum}'s mutable array {@code values} as an unmodifiable <i>ordered</i> {@link Set} specifically
   *     wrapping with the highly performant {@link EnumSet}
   */
  public Set<E> toOrderedSet() {
    return this.enumsSet;
  }

  /**
   * Returns the {@link Enum}'s mutable array {@code values} as an unmodifiable <i>ordered</i> {@link Map} where the key
   * is the {@link Enum#name()}, and the enum constant itself is the value.
   *
   * @return the {@link Enum}'s mutable array {@code values} as an unmodifiable <i>ordered</i> {@link Map} where the key
   *     is the {@link Enum#name()}, and the enum constant itself is the value
   */
  public Map<String, E> toOrderedMapByName() {
    return Collections.unmodifiableMap(
        stream().collect(Collectors.toMap(
            Enum::name,
            Function.identity(),
            (vOld, vNew) -> vOld,
            LinkedHashMap::new)));
  }

  /**
   * Performs a non-interfering action for each element of this stream.
   *
   * @param consumer a non-interfering action to perform on the elements
   */
  public void forEach(Consumer<E> consumer) {
    stream().forEach(consumer);
  }

  /**
   * Performs a non-interfering action for each element of this stream, in the encounter order of the stream if the
   * stream has a defined encounter order.
   *
   * @param consumer a non-interfering action to perform on the elements
   */
  public void forEachOrdered(Consumer<E> consumer) {
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
  public E valueOfOrDefaultToFirst(
      String search
  ) {
    return valueOf(search)
        .orElse(this.enumsList.get(0));
  }

  /**
   * Returns the case-insensitive search by name for the enum value, otherwise the {@code orElseDefault}.
   *
   * @param search        the name used to locate the enum value, case-insensitive
   * @param orElseDefault the value to provide if the enum value cannot be found by its case-insensitive name
   * @return the case-insensitive search by name for the enum value, otherwise the {@code orElseDefault}
   * @deprecated use the {@link Optional#orElse(Object)} method upon the result of the {@link #valueOf(String)} method
   */
  @Deprecated
  public E valueOf(
      String search,
      E orElseDefault
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
  public Optional<E> valueOf(
      String search
  ) {
    return Optional.ofNullable(
        this.enumValueByNameLowerCase
            .get(search.toLowerCase()));
  }

  /**
   * Returns an {@link Optional} wrapping the using the Unicode case-insensitive aware function,
   * {@link StringsOps#indexOfIgnoreCase(String, String)}, to search O(N) by name for the enum, otherwise an empty
   * {@link Optional}.
   *
   * @param search the name used to locate the enum value, Unicode case-insensitive
   * @return an {@link Optional} wrapping the using the Unicode case-insensitive aware function,
   *     {@link StringsOps#indexOfIgnoreCase(String, String)}, to search O(N) by name for the enum, otherwise an empty
   *     {@link Optional}
   */
  public Optional<E> valueOfByRegionMatches(
      String search
  ) {
    return stream()
        .filter(enumValue ->
            StringsOps.equalsIgnoreCase(enumValue.name(), search))
        .findFirst();
  }

  /**
   * Returns a {@link FormatBuilder} with defaults to assist with String encodings of this collection.
   *
   * @return a {@link FormatBuilder} to assist with String encodings of this collection
   */
  public FormatBuilder<E> getFormatBuilder() {
    return this.formatBuilder;
  }

  /**
   * Represents the immutable configuration state for formatting an {@link EnumsOps} collection.
   *
   * @param filter       a {@link Predicate} used to filter enum values
   * @param sortStrategy a {@link Comparator} used to sort filtered enum values
   * @param reformat     a {@link Function} used to transform an enum value into a String representation
   * @param separator    a {@link String} delimiter used when joining formatted enum representations
   * @param <E>          the type of the {@link Enum}
   */
  public record FormatConfig<E extends Enum<E>>(
      Predicate<E> filter,
      Comparator<E> sortStrategy,
      Function<E, String> reformat,
      String separator
  ) {
    public FormatConfig {
      Objects.requireNonNull(filter, "filter must not be null");
      Objects.requireNonNull(sortStrategy, "sortStrategy must not be null");
      Objects.requireNonNull(reformat, "reformat must not be null");
      Objects.requireNonNull(separator, "separator must not be null");
    }

    public FormatConfig<E> withFilter(Predicate<E> filter) {
      Objects.requireNonNull(filter, "filter must not be null");
      return new FormatConfig<>(filter, this.sortStrategy, this.reformat, this.separator);
    }

    public FormatConfig<E> withSortStrategy(Comparator<E> sortStrategy) {
      Objects.requireNonNull(sortStrategy, "sortStrategy must not be null");
      return new FormatConfig<>(this.filter, sortStrategy, this.reformat, this.separator);
    }

    public FormatConfig<E> withReformat(Function<E, String> reformat) {
      Objects.requireNonNull(reformat, "reformat must not be null");
      return new FormatConfig<>(this.filter, this.sortStrategy, reformat, this.separator);
    }

    public FormatConfig<E> withSeparator(String separator) {
      Objects.requireNonNull(separator, "separator must not be null");
      if (this.separator.equals(separator)) {
        return this;
      }
      return new FormatConfig<>(this.filter, this.sortStrategy, this.reformat, separator);
    }
  }

  /**
   * Defines a format builder to assist with String encodings of this collection.
   *
   * @param <E> the type of the {@link Enum} being enhanced
   */
  public static class FormatBuilder<E extends Enum<E>> {
    public static final String DEFAULT_SEPARATOR = ", ";

    private final EnumsOps<E> enumsOps;
    private final FormatConfig<E> config;

    private FormatBuilder(
        EnumsOps<E> enumsOps,
        FormatConfig<E> config
    ) {
      this.enumsOps = Objects.requireNonNull(enumsOps, "enumsOps must not be null");
      this.config = Objects.requireNonNull(config, "config must not be null");
    }

    private FormatBuilder(
        EnumsOps<E> enumsOps,
        Predicate<E> filter,
        Comparator<E> sortStrategy,
        Function<E, String> reformat,
        String separator
    ) {
      this(enumsOps, new FormatConfig<>(filter, sortStrategy, reformat, separator));
    }

    /**
     * Returns a new {@link FormatBuilder} that augments an {@link EnumsOps}, and provides overridable defaults for each
     * of the {@link FormatBuilder}'s properties.
     * <p>
     * Defaults are:
     * <ul>
     *   <li>includes all the {@link Enum} values; i.e., no filtering</li>
     *   <li>sorts by each {@link Enum} value's {@code ordinal} property</li>
     *   <li>displays each {@link Enum} value's {@code name} property</li>
     *   <li>separates each {@link Enum} value's display value with the {@link FormatBuilder#DEFAULT_SEPARATOR}</li>
     * </ul>
     *
     * @param enumsOps the {@link EnumsOps} being augmented
     * @param <E>      the type of the {@link Enum} being augmented
     * @return a new {@link FormatBuilder} that augments an {@link EnumsOps}, and provides overridable defaults for each
     *     of the {@link FormatBuilder}'s properties
     */
    public static <E extends Enum<E>> FormatBuilder<E> from(
        EnumsOps<E> enumsOps
    ) {
      return new FormatBuilder<>(
          enumsOps,
          e ->
              true,
          Comparator.comparingInt(Enum::ordinal),
          Enum::name,
          DEFAULT_SEPARATOR);
    }

    /**
     * Returns the {@link EnumsOps} being augmented.
     *
     * @return the {@link EnumsOps} being augmented
     */
    public EnumsOps<E> getEnumsOps() {
      return this.enumsOps;
    }

    /**
     * Returns the {@link FormatConfig} containing the immutable formatting configuration.
     *
     * @return the {@link FormatConfig} containing the immutable formatting configuration
     */
    public FormatConfig<E> getConfig() {
      return this.config;
    }

    /**
     * Returns the {@code filter} {@link Predicate} property this {@link FormatBuilder} instance is using to
     * include/exclude {@link Enum} values.
     * <p>
     * The default {@code filter} {@link Predicate} property includes all the {@link Enum} values; i.e., no filtering.
     *
     * @return the {@code filter} {@link Predicate} property this {@link FormatBuilder} instance is using to
     *     include/exclude {@link Enum} values
     */
    public Predicate<E> getFilter() {
      return this.config.filter();
    }

    /**
     * Returns a copy of the {@link FormatBuilder} with the {@code filter} {@link Predicate} property that defines how
     * to include/exclude {@link Enum} values.
     *
     * @param filter a {@link Predicate} property that defines how to include/exclude {@link Enum} values
     * @return a copy of the {@link FormatBuilder} with the {@code filter} {@link Predicate} property that defines how
     *     to include/exclude {@link Enum} values
     */
    public FormatBuilder<E> setFilter(Predicate<E> filter) {
      Objects.requireNonNull(filter, "filter must not be null");
      return new FormatBuilder<>(this.enumsOps, this.config.withFilter(filter));
    }

    /**
     * Returns a copy of the {@link FormatBuilder} with the {@code filter} {@link Predicate} property that defines how
     * to include/exclude {@link Enum} values based their presence within a source.
     *
     * @param collection a source that is defensively copied into a {@link Set}, and via the {@link Set#contains}
     *                   method, determines what {@link Enum} values to include/exclude
     * @return a copy of the {@link FormatBuilder} with the {@code filter} {@link Predicate} property that defines how
     *     to include/exclude {@link Enum} values based their presence within a source
     */
    public FormatBuilder<E> setFilter(Collection<E> collection) {
      Objects.requireNonNull(collection, "collection must not be null");
      return setFilter(collection.stream());
    }

    /**
     * Returns a copy of the {@link FormatBuilder} with the {@code filter} {@link Predicate} property that defines how
     * to include/exclude {@link Enum} values based their presence within a source.
     *
     * @param stream a source that is defensively copied into a {@link Set}, and via the {@link Set#contains} method,
     *               determines what {@link Enum} values to include/exclude
     * @return a copy of the {@link FormatBuilder} with the {@code filter} {@link Predicate} property that defines how
     *     to include/exclude {@link Enum} values based their presence within a source
     */
    public FormatBuilder<E> setFilter(Stream<E> stream) {
      Objects.requireNonNull(stream, "stream must not be null");
      var defensiveCopy = stream.collect(Collectors.toUnmodifiableSet());

      return setFilter(defensiveCopy::contains);
    }

    /**
     * Returns the {@code sortStrategy} {@link Comparator} property this {@link FormatBuilder} instance is using to
     * reorder the filtered {@link Enum} values.
     * <p>
     * The default {@code sortStrategy} {@link Comparator} property sorts by each {@link Enum} value's {@code ordinal}
     * property.
     *
     * @return the {@code sortStrategy} {@link Comparator} property this {@link FormatBuilder} instance is using to
     *     reorder the filtered {@link Enum} values
     */
    public Comparator<E> getSortStrategy() {
      return this.config.sortStrategy();
    }

    /**
     * Returns a copy of the {@link FormatBuilder} with the {@code sortStrategy} {@link Comparator} property that
     * defines how to reorder the filtered {@link Enum} values.
     *
     * @param sortStrategy a {@link Comparator} that defines how to reorder the filtered {@link Enum} values
     * @return a copy of the {@link FormatBuilder} with the {@code sortStrategy} {@link Comparator} property that
     *     defines how to reorder the filtered {@link Enum} values
     */
    public FormatBuilder<E> setSortStrategy(Comparator<E> sortStrategy) {
      Objects.requireNonNull(sortStrategy, "sortStrategy must not be null");
      return new FormatBuilder<>(this.enumsOps, this.config.withSortStrategy(sortStrategy));
    }

    /**
     * Returns the {@code reformat} {@link Function} property this {@link FormatBuilder} instance is using to display
     * each {@link Enum} value.
     * <p>
     * The default {@code reformat} {@link Function} property displays each {@link Enum} value's {@code name} property.
     *
     * @return the {@code reformat} {@link Function} property this {@link FormatBuilder} instance is using to display
     *     each {@link Enum} value
     */
    public Function<E, String> getReformat() {
      return this.config.reformat();
    }

    /**
     * Returns a copy of the {@link FormatBuilder} with the {@code reformat} {@link Function} property that defines how
     * to display each {@link Enum} value.
     *
     * @param reformat a {@link Function} that defines how to display each {@link Enum} value
     * @return a copy of the {@link FormatBuilder} with the {@code reformat} {@link Function} property that defines how
     *     to display each {@link Enum} value
     */
    public FormatBuilder<E> setReformat(Function<E, String> reformat) {
      Objects.requireNonNull(reformat, "reformat must not be null");
      return new FormatBuilder<>(this.enumsOps, this.config.withReformat(reformat));
    }

    /**
     * Returns the {@code separator} String property this {@link FormatBuilder} instance is using to separate and
     * display the filtered and reordered {@link Enum} values.
     * <p>
     * The default {@code separator} String property separates each {@link Enum} value's display value with the
     * {@link FormatBuilder#DEFAULT_SEPARATOR}.
     *
     * @return the {@code separator} String property this {@link FormatBuilder} instance is using to separate and
     *     display the filtered and reordered {@link Enum} values
     */
    public String getSeparator() {
      return this.config.separator();
    }

    /**
     * Returns a copy of the {@link FormatBuilder} with the {@code separator} String property that defines how to
     * separate and display the filtered and reordered {@link Enum} values.
     *
     * @param separator a {@link String} that defines how to separate and display the filtered and reordered
     *                  {@link Enum} values
     * @return a copy of the {@link FormatBuilder} with the {@code separator} String property that defines how to
     *     separate and display the filtered and reordered {@link Enum} values
     */
    public FormatBuilder<E> setSeparator(String separator) {
      Objects.requireNonNull(separator, "separator must not be null");
      var newConfig = this.config.withSeparator(separator);
      if (newConfig == this.config) {
        return this;
      }

      return new FormatBuilder<>(this.enumsOps, newConfig);
    }

    /**
     * Returns the {@link Stream} used by {@code join} to obtain the filtered and reordered {@link Enum} values.
     *
     * @return the {@link Stream} used by {@code join} to obtain the filtered and reordered {@link Enum} values
     */
    public Stream<E> toFilteredAndSorted() {
      return this.getEnumsOps()
          .stream()
          .filter(this.getFilter())
          .sorted(this.getSortStrategy());
    }

    /**
     * Returns the {@link Stream} used by {@code join} to display the filtered and reordered {@link Enum} values.
     *
     * @return the {@link Stream} used by {@code join} to display the filtered and reordered {@link Enum} values
     */
    public Stream<String> toStrings() {
      return toFilteredAndSorted()
          .map(this.getReformat());
    }

    /**
     * Returns the {@link List} used by {@code join} to display the filtered and reordered {@link Enum} values.
     *
     * @return the {@link List} used by {@code join} to display the filtered and reordered {@link Enum} values
     */
    public List<String> toList() {
      return toStrings().toList();
    }

    /**
     * Returns a new {@code String} composed of copies of the provided {@code Enum}s; filtered, sorted, and transformed,
     * and then String joined.
     *
     * @return a new {@code String} composed of copies of the provided {@code Enum}s; filtered, sorted, and transformed,
     *     and then String joined
     */
    public String join() {
      return toStrings().collect(Collectors.joining(this.getSeparator()));
    }
  }
}
