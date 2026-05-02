package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.util.CollectionsOps;
import org.deus_ex_java.util.Either;
import org.deus_ex_java.util.TryCatchesOps;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A validation encapsulation record ensuring the wrapped {@link Map} is both non-empty and unmodifiable. This enables
 * use of both the <em>error-by-return-value</em> pattern and the <em>error-by-throw-exception</em> pattern.
 * <p>
 * The <em>error-by-return-value</em> pattern is implemented via the static factory methods,
 * {@link NonEmptyMap#wrap(Map)}, {@link NonEmptyMap#from(Collection)}, and {@link NonEmptyMap#from(Stream)}.
 * <p>
 * The default {@code new NonEmptyMap(...)} constructor implements the forced validation via the
 * <em>error-by-throw-exception</em> pattern; i.e. throws a {@link ParametersValidationException} within any attempt to
 * instantiate with a {@code map} which returns a non-empty {@link Optional} from the {@link NonEmptyMap#invalidate}
 * method.
 *
 * @param map a {@link Map} that is both non-empty and unmodifiable
 */
@NullMarked
public record NonEmptyMap<K, V>(Map<K, V> map) {

  /**
   * Returns a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   * the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}.
   * <p>
   * <u><b>Preconditions:</b></u>
   * <ul>
   * <li>{@code map} must be non-empty</li>
   * <li>{@code map} must be unmodifiable</li>
   * </ul>
   *
   * @param map a {@link Map} that is both non-empty and unmodifiable
   * @return a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   *     the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}
   */
  public static <K, V> Optional<ParametersValidationException> invalidate(
      Map<K, V> map
  ) {
    @SuppressWarnings("ConstantValue")
    //@formatter:off
    var preconditionFailureMessages = (map == null
        ? Stream.of("map must not be null")
        : Stream.of(
            map.isEmpty()
                ? "map.isEmpty() must be false"
                : "",
            !CollectionsOps.isUnmodifiable(map)
                ? "map must be unmodifiable"
                : ""))
        //@formatter:off
        .filter(preconditionFailureMessage ->
            !preconditionFailureMessage.isEmpty())
        .toList();
    if (!preconditionFailureMessages.isEmpty()) {

      return Optional.of(new ParametersValidationException(
          "NonEmptyMap<K, V> invalidated parameter(s)",
          preconditionFailureMessages));
    }

    return Optional.empty();
  }

  /**
   * Returns, via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptyMap}
   * wrapping the uncopied and validated {@code map}, otherwise an {@link Either#left} with a
   * {@link ParametersValidationException} is returned containing the non-empty result from the {@link #invalidate(Map)}
   * method.
   *
   * @param map a {@link Map} source to be wrapped that must be both non-empty and unmodifiable
   * @return via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptyMap} wrapping
   *     the uncopied and validated {@code map}, otherwise an {@link Either#left} with a
   *     {@link ParametersValidationException} is returned containing the non-empty result from the
   *     {@link #invalidate(Map)} method
   */
  public static <K, V> Either<ParametersValidationException, NonEmptyMap<K, V>> wrap(
      Map<K, V> map
  ) {
    return TryCatchesOps.wrap(
        () ->
            new NonEmptyMap<>(map),
        ParametersValidationException.class);
  }

  /**
   * Returns, via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptyMap}
   * wrapping a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   * {@link ParametersValidationException} is returned containing the non-empty result from the {@link #invalidate(Map)}
   * method.
   *
   * @param collection a source from which the entries are defensively copied
   * @return via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptyMap} wrapping
   *     a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   *     {@link ParametersValidationException} is returned containing the non-empty result from the
   *     {@link #invalidate(Map)} method
   */
  public static <K, V> Either<ParametersValidationException, NonEmptyMap<K, V>> from(
      Collection<Entry<K, V>> collection
  ) {
    return from(collection.stream());
  }

  /**
   * Returns, via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptyMap}
   * wrapping a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   * {@link ParametersValidationException} is returned containing the non-empty result from the {@link #invalidate(Map)}
   * method.
   *
   * @param stream a source from which the entries are defensively copied
   * @return via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptyMap} wrapping
   *     a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   *     {@link ParametersValidationException} is returned containing the non-empty result from the
   *     {@link #invalidate(Map)} method
   */
  public static <K, V> Either<ParametersValidationException, NonEmptyMap<K, V>> from(
      Stream<Entry<K, V>> stream
  ) {
    return wrap(stream.collect(Collectors.toUnmodifiableMap(Entry::getKey, Entry::getValue)));
  }

  /**
   * Default constructor ensuring the preconditions are validated before wrapping the value.
   *
   * @param map a {@link Map} that is both non-empty and unmodifiable
   * @throws ParametersValidationException when the call to the {@link #invalidate(Map)} method returns a non-empty
   *                                       {@link Optional}.
   */
  public NonEmptyMap {
    invalidate(map).ifPresent(parametersValidationException -> {
      throw parametersValidationException;
    });
  }
}
