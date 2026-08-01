package org.deus_ex_java.util.refined;

import org.deus_ex_java.lang.ParametersValidationException;
import org.deus_ex_java.util.CollectionsOps;
import org.deus_ex_java.util.Either;
import org.deus_ex_java.util.TryCatchesOps;
import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * A validation encapsulation record ensuring the wrapped {@link List} is both non-empty and unmodifiable. This enables
 * use of both the <em>error-by-returned-value</em> pattern and the <em>error-by-thrown-exception</em> pattern.
 * <p>
 * The <em>error-by-returned-value</em> pattern is implemented via the static factory methods,
 * {@link NonEmptyList#wrap(List)}, {@link NonEmptyList#from(Collection)}, and {@link NonEmptyList#from(Stream)}.
 * <p>
 * The default {@code new NonEmptyList(...)} constructor implements the forced validation via the
 * <em>error-by-thrown-exception</em> pattern; i.e. throws a {@link ParametersValidationException} within any attempt
 * to instantiate with a {@code list} which returns a non-empty {@link Optional} from the
 * {@link NonEmptyList#invalidate} method.
 *
 * @param list a {@link List} that is both non-empty and unmodifiable
 */
@NullMarked
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
   * @param list a {@link List} that is both non-empty and unmodifiable
   * @return a non-empty {@link Optional} containing an instance of {@link ParametersValidationException} that itemizes
   *     the validation preconditions which failed preventing the wrapping, otherwise an {@link Optional#empty()}
   */
  public static <T> Optional<ParametersValidationException> invalidate(
      List<T> list
  ) {
    @SuppressWarnings("ConstantValue")
    //@formatter:off
    var preconditionFailureMessages = (list == null
        ? Stream.of("list must not be null")
        : Stream.of(
            list.isEmpty()
                ? "list.isEmpty() must be false"
                : "",
            !CollectionsOps.isUnmodifiable(list)
                ? "list must be unmodifiable"
                : ""))
        //@formatter:off
        .filter(preconditionFailureMessage ->
            !preconditionFailureMessage.isEmpty())
        .toList();
    if (!preconditionFailureMessages.isEmpty()) {

      return Optional.of(new ParametersValidationException(
          "NonEmptyList<T> invalidated parameter(s)",
          preconditionFailureMessages));
    }

    return Optional.empty();
  }

  /**
   * Returns, via the <em>error-by-returned-value</em> pattern, an {@link Either#right} with a {@link NonEmptyList}
   * wrapping the uncopied and validated {@code list}, otherwise an {@link Either#left} with a
   * {@link ParametersValidationException} is returned containing the non-empty result from the
   * {@link #invalidate(List)} method.
   *
   * @param list a {@link List} source to be wrapped that must be both non-empty and unmodifiable
   * @return via the <em>error-by-returned-value</em> pattern, an {@link Either#right} with a {@link NonEmptyList}
   *     wrapping the uncopied and validated {@code list}, otherwise an {@link Either#left} with a
   *     {@link ParametersValidationException} is returned containing the non-empty result from the
   *     {@link #invalidate(List)} method
   */
  public static <T> Either<ParametersValidationException, NonEmptyList<T>> wrap(
      List<T> list
  ) {
    return TryCatchesOps.wrap(
        () ->
            new NonEmptyList<>(list),
        ParametersValidationException.class);
  }


  /**
   * Returns, via the <em>error-by-returned-value</em> pattern, an {@link Either#right} with a {@link NonEmptyList}
   * wrapping a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   * {@link ParametersValidationException} is returned containing the non-empty result from the
   * {@link #invalidate(List)} method.
   *
   * @param collection a source from which the elements are defensively copied
   * @return via the <em>error-by-returned-value</em> pattern, an {@link Either#right} with a {@link NonEmptyList}
   *     wrapping a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   *     {@link ParametersValidationException} is returned containing the non-empty result from the
   *     {@link #invalidate(List)} method
   */
  public static <T> Either<ParametersValidationException, NonEmptyList<T>> from(
      Collection<T> collection
  ) {
    return from(collection.stream());
  }

  /**
   * Returns, via the <em>error-by-returned-value</em> pattern, an {@link Either#right} with a {@link NonEmptyList}
   * wrapping a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   * {@link ParametersValidationException} is returned containing the non-empty result from the
   * {@link #invalidate(List)} method.
   *
   * @param stream a source from which the elements are defensively copied
   * @return via the <em>error-by-returned-value</em> pattern, an {@link Either#right} with a {@link NonEmptyList}
   *     wrapping a defensively (shallow) copied and validated source, otherwise an {@link Either#left} with a
   *     {@link ParametersValidationException} is returned containing the non-empty result from the
   *     {@link #invalidate(List)} method
   */
  public static <T> Either<ParametersValidationException, NonEmptyList<T>> from(
      Stream<T> stream
  ) {
    return wrap(stream.toList());
  }

  /**
   * Default constructor ensuring the preconditions are validated before wrapping the value.
   *
   * @param list a {@link List} that is both non-empty and unmodifiable
   * @throws ParametersValidationException when the call to the {@link #invalidate(List)} method returns a non-empty
   *                                       {@link Optional}.
   */
  public NonEmptyList {
    invalidate(list).ifPresent(parametersValidationException -> {
      throw parametersValidationException;
    });
    list = List.copyOf(list);
  }
}
