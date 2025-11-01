package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.lang.refined.NonBlankString;
import org.deus_ex_java.util.CollectionsOps;
import org.deus_ex_java.util.Either;
import org.deus_ex_java.util.TryCatchesOps;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A validation wrapper restricting an {@link List} to be non-empty and unmodifiable.
 * <p>
 * The default {@code new NonEmptyList(...)} constructor implements enforced validation; i.e. throws a
 * {@link ParametersValidationException} within any attempt to instantiate with a value which returns a non-empty
 * {@link Optional} from the {@link NonEmptyList#validate} method.
 *
 * @param list a {@link List} that is non-empty and unmodifiable
 */
public record NonEmptyList<T>(List<T> list) {

  /**
   * Returns a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   * the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}.
   * <p>
   * <u><b>Preconditions:</b></u>
   * <ul>
   * <li>{@code list} must be non-empty</li>
   * <li>{@code list} must be unmodifiable</li>
   * </ul>
   *
   * @param list a {@link List} that is non-empty and unmodifiable
   * @return a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   *     the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}
   */
  @NotNull
  public static <T> Optional<ParametersValidationException> validate(
      @NotNull List<T> list
  ) {
    var preconditionFailureMessages = Stream.of(
            list.isEmpty()
                ? "list.isEmpty() must be false"
                : "",
            !CollectionsOps.isUnmodifiable(list)
                ? "list must be unmodifiable"
                : "")
        .filter(preconditionFailureMessage ->
            !preconditionFailureMessage.isEmpty())
        .toList();
    if (!preconditionFailureMessages.isEmpty()) {

      return Optional.of(new ParametersValidationException(
          "NonEmptyList<T> invalid parameter(s)",
          preconditionFailureMessages));
    }

    return Optional.empty();
  }

  /**
   * Returns, via the error-by-value pattern, an {@link Either} where an {@link Either#right} contains the validated
   * wrapped instance, otherwise an {@link Either#left} contains the returned {@link ParametersValidationException}
   * instance from the call to the {@link #validate(List)} method.
   *
   * @param list a {@link List} that is non-empty and unmodifiable
   * @return an {@link Either} where an {@link Either#right} contains the validated wrapped instance, otherwise an
   *     {@link Either#left} contains the returned {@link ParametersValidationException} instance from the call to the
   *     {@link #validate(List)} method
   */
  @NotNull
  public static <T> Either<ParametersValidationException, NonEmptyList<T>> from(
      @NotNull List<T> list
  ) {
    return TryCatchesOps.wrap(
        () ->
            new NonEmptyList<>(list),
        ParametersValidationException.class);
  }

  /**
   * Default constructor ensuring the preconditions are validated before wrapping the value.
   *
   * @param list a {@link List} that is non-empty and unmodifiable
   * @throws ParametersValidationException when the call to the {@link #validate(List)} method returns a non-empty
   *                                       {@link Optional}.
   */
  public NonEmptyList {
    validate(list).ifPresent(parametersValidationException -> {
      throw parametersValidationException;
    });
  }
}
