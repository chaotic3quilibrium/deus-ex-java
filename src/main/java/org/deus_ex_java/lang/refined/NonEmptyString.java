package org.deus_ex_java.lang.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.util.Either;
import org.deus_ex_java.util.TryCatchesOps;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A validation wrapper restricting an {@code String} to a non-empty value.
 * <p>
 * The default {@code new NonEmptyString(...)} constructor implements enforced validation; i.e. throws a
 * {@link ParametersValidationException} within any attempt to instantiate with a value which returns a non-empty
 * {@link Optional} from the {@link NonEmptyString#validate} method.
 *
 * @param string an {@code String} with a non-empty value
 */
public record NonEmptyString(
    @NotNull String string
) implements Comparable<NonEmptyString> {

  /**
   * Returns a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   * the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}.
   * <p>
   * <u><b>Preconditions:</b></u>
   * <ul>
   * <li>{@code string} must be non-empty</li>
   * </ul>
   *
   * @param string an {@code String} with a non-empty value
   * @return a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   *     the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}
   */
  @NotNull
  public static Optional<ParametersValidationException> validate(
      @NotNull String string
  ) {
    if (string.isEmpty()) {

      return Optional.of(new ParametersValidationException(
          "NonEmptyString invalid parameter(s)",
          "string.isEmpty() must be false"));
    }

    return Optional.empty();
  }

  /**
   * Returns, via the error-by-value pattern, an {@link Either} where an {@link Either#right} contains the validated
   * wrapped instance, otherwise an {@link Either#left} contains the returned {@link ParametersValidationException}
   * instance from the call to the {@link #validate(String)} method.
   *
   * @param string an {@code String} with a non-empty value
   * @return an {@link Either} where an {@link Either#right} contains the validated wrapped instance, otherwise an
   *     {@link Either#left} contains the returned {@link ParametersValidationException} instance from the call to the
   *     {@link #validate(String)} method
   */
  @NotNull
  public static Either<ParametersValidationException, NonEmptyString> from(
      @NotNull String string
  ) {
    return TryCatchesOps.wrap(
        () ->
            new NonEmptyString(string),
        ParametersValidationException.class);
  }

  /**
   * Default constructor ensuring the preconditions are validated before wrapping the value.
   *
   * @param string an {@code String} with a non-empty value
   * @throws ParametersValidationException when the call to the {@link #validate(String)} method returns a non-empty
   *                                       {@link Optional}.
   */
  public NonEmptyString {
    validate(string)
        .ifPresent(parametersValidationException -> {
          throw parametersValidationException;
        });
  }

  /**
   * Returns a value less than {@code 0} when {@code this.string} is lexicographically less than {@code that.value},
   * otherwise a value greater than {@code 0} when {@code this.value} is lexicographically greater than
   * {@code that.value}, otherwise the value {@code 0} because {@code this.value} must be by elimination
   * lexicographically equal to {@code that.value} (signed comparison).
   *
   * @param that the NonEmptyString to be numerically compared.
   * @return a value less than {@code 0} when {@code this.string} is lexicographically less than {@code that.value},
   *     otherwise a value greater than {@code 0} when {@code this.value} is lexicographically greater than
   *     {@code that.value}, otherwise the value {@code 0} because {@code this.value} must be by elimination
   *     lexicographically equal to {@code that.value} (signed comparison)
   */
  @Override
  public int compareTo(@NotNull NonEmptyString that) {
    return this.string.compareTo(that.string);
  }
}