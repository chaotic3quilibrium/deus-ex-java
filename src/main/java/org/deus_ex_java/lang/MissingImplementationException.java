package org.deus_ex_java.lang;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;

/**
 * {@code MissingImplementationException} is an <em>unchecked exception</em> final class used to be a placeholder when a
 * method implementation needs to be temporarily stubbed out.
 */
public final class MissingImplementationException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -1277898104227409358L;

  /**
   * Returns a new runtime exception sans any additional properties.
   */
  public MissingImplementationException() {
    super();
  }

  /**
   * Returns a new runtime exception with the specified detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
   */
  public MissingImplementationException(
      @NotNull String message
  ) {
    super(message);
  }

  /**
   * Returns a new runtime exception with the specified detail message, cause, suppression enabled or disabled, and
   * writable stack trace enabled or disabled.
   *
   * @param message            the detail message.
   * @param cause              the cause.  (A {@code null} value is <i>not</i> permitted.)
   * @param enableSuppression  whether suppression is enabled or disabled
   * @param writableStackTrace whether the stack trace should be writable
   */
  public MissingImplementationException(
      @NotNull String message,
      @NotNull Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace
  ) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
