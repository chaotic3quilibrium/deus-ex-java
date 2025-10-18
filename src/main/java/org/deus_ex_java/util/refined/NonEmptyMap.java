package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.util.CollectionsOps;
import org.deus_ex_java.util.Either;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A validation wrapper restricting an {@link Map} to be non-empty and unmodifiable.
 * <p>
 * The default {@code new NonEmptyMap(...)} constructor implements enforced validation; i.e. throws a
 * {@link ParametersValidationException} within any attempt to instantiate with a value which returns a non-empty
 * {@link Optional} from the {@link NonEmptyMap#validate} method.
 *
 * @param map a {@link Map} that is non-empty and unmodifiable
 */
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
   * @param map a {@link Map} that is non-empty and unmodifiable
   * @return a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   *     the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}
   */
  @NotNull
  public static <K, V> Optional<ParametersValidationException> validate(
      @NotNull Map<K, V> map
  ) {
    var preconditionFailureMessages = Stream.of(
            map.isEmpty()
                ? "map.isEmpty() must be false"
                : "",
            !CollectionsOps.isUnmodifiable(map)
                ? "map must be unmodifiable"
                : "")
        .filter(preconditionFailureMessage ->
            !preconditionFailureMessage.isEmpty())
        .toList();
    if (!preconditionFailureMessages.isEmpty()) {

      return Optional.of(new ParametersValidationException(
          "NonEmptyMap<K, V> invalid parameter(s)",
          preconditionFailureMessages));
    }

    return Optional.empty();
  }

  /**
   * Returns, via the error-by-value pattern, an {@link Either} where an {@link Either#right} contains the validated
   * wrapped instance, otherwise an {@link Either#left} contains the returned {@link ParametersValidationException}
   * instance from the call to the {@link #validate(Map)} method.
   *
   * @param map a {@link Map} that is non-empty and unmodifiable
   * @return an {@link Either} where an {@link Either#right} contains the validated wrapped instance, otherwise an
   *     {@link Either#left} contains the returned {@link ParametersValidationException} instance from the call to the
   *     {@link #validate(Map)} method
   */
  @NotNull
  public static <K, V> Either<ParametersValidationException, NonEmptyMap<K, V>> from(
      @NotNull Map<K, V> map
  ) {
    try {
      return Either.right(new NonEmptyMap<>(map));
    } catch (ParametersValidationException parametersValidationException) {
      return Either.left(parametersValidationException);
    }
  }

  /**
   * Default constructor ensuring the preconditions are validated before wrapping the value.
   *
   * @param map a {@link Map} that is non-empty and unmodifiable
   * @throws ParametersValidationException when the call to the {@link #validate(Map)} method returns a non-empty
   *                                       {@link Optional}.
   */
  public NonEmptyMap {
    validate(map).ifPresent(parametersValidationException -> {
      throw parametersValidationException;
    });
  }
}
