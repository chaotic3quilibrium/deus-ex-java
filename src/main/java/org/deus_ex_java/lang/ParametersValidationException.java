package org.deus_ex_java.lang;

import org.deus_ex_java.util.ListsOps;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

/**
 * {@code ParametersValidationException} is an <em>unchecked exception</em> final class designed to collect a set of
 * failed validations during the construction of a class or record.
 * <p>
 * It's intended to facilitate the "validated record instance" pattern that implements the principle of only allowing
 * the creation of instances with a valid state.
 */
@NullMarked
public final class ParametersValidationException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -2463403636851524272L;

  /**
   * The default value when the {@code message} parameter is not provided.
   */
  public static final String DEFAULT_MESSAGE = "Parameters validation failed";

  private final List<String> parametersValidationFailureMessages;

  private static String formatMessage(
      String message,
      List<String> parametersValidationFailureMessages
  ) {
    return parametersValidationFailureMessages.isEmpty()
        ? message
        : "%s - Parameter Validation Failures: [%s]".formatted(
            message,
            String.join("|", parametersValidationFailureMessages));
  }

  /**
   * Default no-arg constructor which forwards to the single argument providing the {@code DEFAULT_MESSAGE}.
   */
  public ParametersValidationException() {
    this(DEFAULT_MESSAGE);
  }

  /**
   * Constructor providing a simple {@code message} with no further detail messages, for when the context is a single
   * parameter validation.
   *
   * @param message the main message parameter
   */
  public ParametersValidationException(
      String message
  ) {
    this(message, List.of());
  }

  /**
   * Constructor providing a default of {@code DEFAULT_MESSAGE} for the {@code message} with a list of detail parameter
   * validation failure messages.
   *
   * @param parametersValidationFailureMessages detail parameter validation failure messages
   */
  public ParametersValidationException(
      List<String> parametersValidationFailureMessages
  ) {
    this(DEFAULT_MESSAGE, parametersValidationFailureMessages);
  }

  /**
   * Constructor providing a default of {@code DEFAULT_MESSAGE} for the {@code message} and for wrapping another
   * exception.
   * <p>
   * NOTE: While this is provided for completeness, it should be avoided in preference to those providing either a
   * {@code message} and/or a list of {@code parametersValidationFailureMessages}.
   *
   * @param cause an exception leading to a parameter validation failure
   */
  public ParametersValidationException(
      @Nullable Throwable cause
  ) {
    this(DEFAULT_MESSAGE, cause, List.of());
  }

  /**
   * Constructor providing a simple {@code message} and wrapping another exception with no further detail messages, for
   * when the context is a single parameter validation.
   *
   * @param message the main message parameter
   * @param cause   an exception leading to a parameter validation failure
   */
  public ParametersValidationException(
      String message,
      @Nullable Throwable cause
  ) {
    super(message, cause);
    this.parametersValidationFailureMessages = List.of();
  }

  /**
   * Constructor providing a simple {@code message} with a single detail {@code parametersValidationFailureMessage}.
   *
   * @param message                            the main message parameter
   * @param parametersValidationFailureMessage detail parameter validation failure message
   */
  public ParametersValidationException(
      String message,
      String parametersValidationFailureMessage
  ) {
    this(message, List.of(parametersValidationFailureMessage));
  }

  /**
   * Constructor providing a simple {@code message} with detail {@code parametersValidationFailureMessages}.
   *
   * @param message                             the main message parameter
   * @param parametersValidationFailureMessages detail parameter validation failure messages
   */
  public ParametersValidationException(
      String message,
      List<String> parametersValidationFailureMessages
  ) {
    super(
        formatMessage(
            message,
            ListsOps.nullSanitize(parametersValidationFailureMessages.stream())));
    this.parametersValidationFailureMessages = ListsOps.nullSanitize(parametersValidationFailureMessages.stream());
  }

  /**
   * Constructor providing a default of {@code DEFAULT_MESSAGE} for the {@code message}, for wrapping another exception,
   * and with detail {@code parametersValidationFailureMessages}.
   *
   * @param cause                               an exception leading to a parameter validation failure
   * @param parametersValidationFailureMessages detail parameter validation failure messages
   */
  public ParametersValidationException(
      @Nullable Throwable cause,
      List<String> parametersValidationFailureMessages
  ) {
    this(
        DEFAULT_MESSAGE,
        cause,
        ListsOps.nullSanitize(parametersValidationFailureMessages.stream()));
  }

  /**
   * Constructor providing a simple {@code message}, for wrapping another exception, and with a single detail
   * {@code parametersValidationFailureMessage}.
   *
   * @param message                            the main message parameter
   * @param cause                              an exception leading to a parameter validation failure
   * @param parametersValidationFailureMessage detail parameter validation failure message
   */
  public ParametersValidationException(
      String message,
      @Nullable Throwable cause,
      String parametersValidationFailureMessage
  ) {
    this(
        message,
        cause,
        List.of(parametersValidationFailureMessage));
  }

  /**
   * Constructor providing a simple {@code message}, for wrapping another exception, and with detail
   * {@code parametersValidationFailureMessages}.
   *
   * @param message                             the main message parameter
   * @param cause                               an exception leading to a parameter validation failure
   * @param parametersValidationFailureMessages detail parameter validation failure messages
   */
  public ParametersValidationException(
      String message,
      @Nullable Throwable cause,
      List<String> parametersValidationFailureMessages
  ) {
    super(
        formatMessage(
            message,
            ListsOps.nullSanitize(parametersValidationFailureMessages.stream())),
        cause);
    this.parametersValidationFailureMessages = ListsOps.nullSanitize(parametersValidationFailureMessages.stream());
  }

  /**
   * Constructor providing a simple {@code message}, for wrapping another exception, with detail
   * {@code parametersValidationFailureMessages}, suppression enabled or disabled, and writable stack trace enabled or
   * disabled.
   *
   * @param message                             the main message parameter
   * @param cause                               an exception leading to a parameter validation failure
   * @param enableSuppression                   whether suppression is enabled or disabled
   * @param writableStackTrace                  whether the stack trace should be writable
   * @param parametersValidationFailureMessages detail parameter validation failure messages
   */
  public ParametersValidationException(
      String message,
      @Nullable Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace,
      List<String> parametersValidationFailureMessages
  ) {
    super(
        formatMessage(
            message,
            ListsOps.nullSanitize(parametersValidationFailureMessages.stream())),
        cause,
        enableSuppression,
        writableStackTrace);
    this.parametersValidationFailureMessages = ListsOps.nullSanitize(parametersValidationFailureMessages.stream());
  }

  /**
   * Return a list consisting of a message for each failed validation.
   *
   * @return a list consisting of a message for each failed validation
   */
  public List<String> getParametersValidationFailureMessages() {
    return this.parametersValidationFailureMessages;
  }

  @Override
  public boolean equals(Object object) {
    return ((this == object) ||
        ((object instanceof ParametersValidationException that) &&
            Objects.equals(this.getMessage(), that.getMessage()) &&
            Objects.equals(this.getCause(), that.getCause()) &&
            Objects.equals(this.getParametersValidationFailureMessages(), that.getParametersValidationFailureMessages())));
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        this.getMessage(),
        this.getCause(),
        this.getParametersValidationFailureMessages());
  }
}
