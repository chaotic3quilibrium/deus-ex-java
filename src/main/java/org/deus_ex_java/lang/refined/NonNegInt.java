package org.deus_ex_java.lang.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.util.Either;
import org.deus_ex_java.util.TryCatchesOps;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * A validation wrapper restricting an {@code int} to a value greater than or equal to {@code 0}.
 * <p>
 * The default {@code new NonNegInt(...)} constructor implements enforced validation; i.e. throws a
 * {@link ParametersValidationException} within any attempt to instantiate with a value which returns a non-empty
 * {@link Optional} from the {@link NonNegInt#validate} method.
 *
 * @param value an {@code int} value greater than or equal to {@code 0}
 */
public record NonNegInt(int value) implements Comparable<NonNegInt> {

  /**
   * Returns a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   * the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}.
   * <p>
   * <u><b>Preconditions:</b></u>
   * <ul>
   * <li>{@code value} must greater than or equal to {@code 0}</li>
   * </ul>
   *
   * @param value an {@code int} value greater than or equal to {@code 0}
   * @return a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   *     the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}
   */
  @NotNull
  public static Optional<ParametersValidationException> validate(
      int value
  ) {
    if (value < 0) {

      return Optional.of(new ParametersValidationException(
          "NonNegInt invalid parameter(s)",
          "value [%d] must be greater than or equal to 0".formatted(value)));
    }

    return Optional.empty();
  }

  /**
   * Returns, via the error-by-value pattern, an {@link Either} where an {@link Either#right} contains the validated
   * wrapped instance, otherwise an {@link Either#left} contains the returned {@link ParametersValidationException}
   * instance from the call to the {@link #validate(int)} method.
   *
   * @param value an {@code int} value greater than or equal to {@code 0}
   * @return an {@link Either} where an {@link Either#right} contains the validated wrapped instance, otherwise an
   *     {@link Either#left} contains the returned {@link ParametersValidationException} instance from the call to the
   *     {@link #validate(int)} method
   */
  @NotNull
  public static Either<ParametersValidationException, NonNegInt> from(
      int value
  ) {
    return TryCatchesOps.wrap(
        () ->
            new NonNegInt(value),
        ParametersValidationException.class);
  }

  /**
   * Default constructor ensuring the preconditions are validated before wrapping the value.
   *
   * @param value an {@code int} value greater than or equal to {@code 0}
   * @throws ParametersValidationException when the call to the {@link #validate(int)} method returns a non-empty
   *                                       {@link Optional}.
   */
  public NonNegInt {
    validate(value)
        .ifPresent(parametersValidationException -> {
          throw parametersValidationException;
        });
  }

  /**
   * Returns a value less than {@code 0} when {@code this.value} is numerically less than {@code that.value}, otherwise
   * a value greater than {@code 0} when {@code this.value} is numerically greater than {@code that.value}, otherwise
   * the value {@code 0} because {@code this.value} must be by elimination numerically equal to {@code that.value}
   * (signed comparison).
   *
   * @param that the NonNegInt to be numerically compared.
   * @return a value less than {@code 0} when {@code this.value} is numerically less than {@code that.value}, otherwise
   *     a value greater than {@code 0} when {@code this.value} is numerically greater than {@code that.value},
   *     otherwise the value {@code 0} because {@code this.value} must be by elimination numerically equal to
   *     {@code that.value} (signed comparison)
   */
  @Override
  public int compareTo(@NotNull NonNegInt that) {
    return Integer.compare(this.value, that.value);
  }
}
