package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.util.CollectionsOps;
import org.deus_ex_java.util.Either;
import org.deus_ex_java.util.TryCatchesOps;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A validation encapsulation record ensuring the wrapped {@link Set} is both non-empty and unmodifiable. This enables
 * use of both the <em>error-by-return-value</em> pattern and the <em>error-by-thrown-exception</em> pattern.
 * <p>
 * The <em>error-by-return-value</em> pattern is implemented via the static factory methods,
 * {@link NonEmptySet#wrap(Set)}, {@link NonEmptySet#from(Collection)}, and {@link NonEmptySet#from(Stream)}.
 * <p>
 * The default {@code new NonEmptySet(...)} constructor implements the forced validation via the
 * <em>error-by-thrown-exception</em> pattern; i.e. throws a {@link ParametersValidationException} within any attempt
 * to
 * instantiate with a {@code set} which returns a non-empty {@link Optional} from the {@link NonEmptySet#invalidate}
 * method.
 *
 * @param set a {@link Set} that is both non-empty and unmodifiable
 */
@NullMarked
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
   * @param set a {@link Set} that is both non-empty and unmodifiable
   * @return a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   *     the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}
   */
  public static <T> Optional<ParametersValidationException> invalidate(
      Set<T> set
  ) {
    @SuppressWarnings("ConstantValue")
    //@formatter:off
    var preconditionFailureMessages = (set == null
        ? Stream.of("set must not be null")
        : Stream.of(
            set.isEmpty()
                ? "set.isEmpty() must be false"
                : "",
            !CollectionsOps.isUnmodifiable(set)
                ? "set must be unmodifiable"
                : ""))
        //@formatter:off
        .filter(preconditionFailureMessage ->
            !preconditionFailureMessage.isEmpty())
        .toList();
    if (!preconditionFailureMessages.isEmpty()) {

      return Optional.of(new ParametersValidationException(
          "NonEmptySet<T> invalidated parameter(s)",
          preconditionFailureMessages));
    }

    return Optional.empty();
  }

  /**
   * Returns, via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptySet}
   * wrapping the uncopied and validated {@code set}, otherwise an {@link Either#left} with a
   * {@link ParametersValidationException} is returned containing the non-empty result from the {@link #invalidate(Set)}
   * method.
   *
   * @param set a {@link Set} that is both non-empty and unmodifiable
   * @return via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptySet} wrapping
   *     the uncopied and validated {@code set}, otherwise an {@link Either#left} with a
   *     {@link ParametersValidationException} is returned containing the non-empty result from the
   *     {@link #invalidate(Set)} method
   */
  public static <T> Either<ParametersValidationException, NonEmptySet<T>> wrap(
      Set<T> set
  ) {
    return TryCatchesOps.wrap(
        () ->
            new NonEmptySet<>(set),
        ParametersValidationException.class);
  }

  /**
   * Returns, via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptySet}
   * wrapping a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   * {@link ParametersValidationException} is returned containing the non-empty result from the {@link #invalidate(Set)}
   * method.
   *
   * @param collection a source from which the elements are defensively copied
   * @return via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptySet} wrapping
   *     a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   *     {@link ParametersValidationException} is returned containing the non-empty result from the
   *     {@link #invalidate(Set)} method
   */
  public static <T> Either<ParametersValidationException, NonEmptySet<T>> from(
      Collection<T> collection
  ) {
    return from(collection.stream());
  }

  /**
   * Returns, via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptySet}
   * wrapping a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   * {@link ParametersValidationException} is returned containing the non-empty result from the {@link #invalidate(Set)}
   * method.
   *
   * @param stream a source from which the elements are defensively copied
   * @return via the <em>error-by-return-value</em> pattern, an {@link Either#right} with a {@link NonEmptySet} wrapping
   *     a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   *     {@link ParametersValidationException} is returned containing the non-empty result from the
   *     {@link #invalidate(Set)} method
   */
  public static <T> Either<ParametersValidationException, NonEmptySet<T>> from(
      Stream<T> stream
  ) {
    return wrap(stream.collect(Collectors.toUnmodifiableSet()));
  }

  /**
   * Default constructor ensuring the preconditions are validated before wrapping the value.
   *
   * @param set a {@link Set} that is both non-empty and unmodifiable
   * @throws ParametersValidationException when the call to the {@link #invalidate(Set)} method returns a non-empty
   *                                       {@link Optional}.
   */
  public NonEmptySet {
    invalidate(set).ifPresent(parametersValidationException -> {
      throw parametersValidationException;
    });
  }
}
