package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.util.CollectionsOps;
import org.deus_ex_java.util.Either;
import org.deus_ex_java.util.TryCatchesOps;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

/**
 * A validation wrapper restricting an {@link Set} to be non-empty and unmodifiable.
 * <p>
 * The default {@code new NonEmptySet(...)} constructor implements enforced validation; i.e. throws a
 * {@link ParametersValidationException} within any attempt to instantiate with a value which returns a non-empty
 * {@link Optional} from the {@link NonEmptySet#validate} method.
 *
 * @param set a {@link Set} that is non-empty and unmodifiable
 */
public record NonEmptySet<T>(Set<T> set) {

  /**
   * Returns a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   * the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}.
   * <p>
   * <u><b>Preconditions:</b></u>
   * <ul>
   * <li>{@code set} must be non-empty</li>
   * <li>{@code set} must be unmodifiable</li>
   * </ul>
   *
   * @param set a {@link Set} that is non-empty and unmodifiable
   * @return a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   *     the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}
   */
  @NotNull
  public static <T> Optional<ParametersValidationException> validate(
      @NotNull Set<T> set
  ) {
    var preconditionFailureMessages = Stream.of(
            set.isEmpty()
                ? "set.isEmpty() must be false"
                : "",
            !CollectionsOps.isUnmodifiable(set)
                ? "set must be unmodifiable"
                : "")
        .filter(preconditionFailureMessage ->
            !preconditionFailureMessage.isEmpty())
        .toList();
    if (!preconditionFailureMessages.isEmpty()) {

      return Optional.of(new ParametersValidationException(
          "NonEmptySet<T> invalid parameter(s)",
          preconditionFailureMessages));
    }

    return Optional.empty();
  }

  /**
   * Returns, via the error-by-value pattern, an {@link Either} where an {@link Either#right} contains the validated
   * wrapped instance, otherwise an {@link Either#left} contains the returned {@link ParametersValidationException}
   * instance from the call to the {@link #validate(Set)} method.
   *
   * @param set a {@link Set} that is non-empty and unmodifiable
   * @return an {@link Either} where an {@link Either#right} contains the validated wrapped instance, otherwise an
   *     {@link Either#left} contains the returned {@link ParametersValidationException} instance from the call to the
   *     {@link #validate(Set)} method
   */
  @NotNull
  public static <T> Either<ParametersValidationException, NonEmptySet<T>> from(
      @NotNull Set<T> set
  ) {
    return TryCatchesOps.wrap(
        () ->
            new NonEmptySet<>(set),
        ParametersValidationException.class);
  }

  /**
   * Default constructor ensuring the preconditions are validated before wrapping the value.
   *
   * @param set a {@link Set} that is non-empty and unmodifiable
   * @throws ParametersValidationException when the call to the {@link #validate(Set)} method returns a non-empty
   *                                       {@link Optional}.
   */
  public NonEmptySet {
    validate(set).ifPresent(parametersValidationException -> {
      throw parametersValidationException;
    });
  }
}
