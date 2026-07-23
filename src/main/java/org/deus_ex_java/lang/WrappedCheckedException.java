package org.deus_ex_java.lang;

import org.deus_ex_java.util.function.FunctionsOps;
import org.jspecify.annotations.NullMarked;

import java.io.Serial;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * {@code WrappedCheckedException} is an <em>unchecked exception</em> final class used to wrap a checked exception
 * (which is returned by {@link #getCause()}, and guaranteed to be non-{@code null}).
 * <p>
 * {@link FunctionsOps} uses this to wrap the checked exception lambdas to enable use of the lambda within
 * {@link Stream} operations.
 * <p>
 * <b>WARNING</b>: If
 * {@link ForcedFatalThrowable#isFatalThrowable(Throwable) ForcedFatalThrowable.isFatalThrowable(...)} returns
 * {@code true}, the <em>fatal</em> exception is not wrapped, and is instead immediately rethrown.
 */
@NullMarked
public final class WrappedCheckedException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -7411859319326389055L;

  /**
   * Returns a new runtime exception with the specified detail message and cause when
   * {@link ForcedFatalThrowable#isFatalThrowable(Throwable) ForcedFatalThrowable.isFatalThrowable(...)} is false,
   * otherwise immediately rethrows the <em>fatal</em> exception.
   * <p>
   * <b>Note:</b> The detail message associated with {@code cause} is <i>not</i> automatically incorporated in this
   * runtime exception's detail message.
   *
   * @param message the detail message (which is saved for later retrieval by the {@link #getMessage()} method).
   * @param cause   the cause (which is saved for later retrieval by the {@link #getCause()} method).  (A {@code null}
   *                value is <i>not</i> permitted.)
   * @throws NullPointerException if the provided {@code cause} is {@code null}
   */
  public WrappedCheckedException(
      String message,
      Throwable cause
  ) {
    super(
        message,
        Objects.requireNonNull(ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(cause)));
  }

  /**
   * Returns a new runtime exception with the specified cause and a detail message of {@code cause.toString())} (which
   * typically contains the class and detail message of {@code cause}) when
   * {@link ForcedFatalThrowable#isFatalThrowable(Throwable) ForcedFatalThrowable.isFatalThrowable(...)} is false,
   * otherwise immediately rethrows the <em>fatal</em> exception.
   * <p>
   * This constructor is useful for runtime exceptions that are little more than wrappers for other throwables.
   *
   * @param cause the cause (which is saved for later retrieval by the {@link #getCause()} method).  (A {@code null}
   *              value is <i>not</i> permitted.)
   * @throws NullPointerException if the provided {@code cause} is {@code null}.
   */
  public WrappedCheckedException(
      Throwable cause
  ) {
    super(Objects.requireNonNull(ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(cause)));
  }

  /**
   * Returns a new runtime exception with the specified detail message, cause, suppression enabled or disabled, and
   * writable stack trace enabled or disabled when
   * {@link ForcedFatalThrowable#isFatalThrowable(Throwable) ForcedFatalThrowable.isFatalThrowable(...)} is false,
   * otherwise immediately rethrows the <em>fatal</em> exception.
   *
   * @param message            the detail message.
   * @param cause              the cause.  (A {@code null} value is <i>not</i> permitted.)
   * @param enableSuppression  whether suppression is enabled or disabled
   * @param writableStackTrace whether the stack trace should be writable
   * @throws NullPointerException if the provided {@code cause} is {@code null}.
   */
  public WrappedCheckedException(
      String message,
      Throwable cause,
      boolean enableSuppression,
      boolean writableStackTrace
  ) {
    super(
        message,
        Objects.requireNonNull(ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(cause)),
        enableSuppression,
        writableStackTrace);
  }

  /**
   * Evaluates whether the specified throwable is non-fatal, returning it if non-fatal,
   * or throwing it immediately (even if checked) if it is a fatal throwable.
   *
   * @param throwable throwable instance to evaluate
   * @return the passed {@code throwable} if non-fatal
   */
  public static Throwable requireNonFatal(Throwable throwable) {
    return ForcedFatalThrowable.requireNonFatalThrowableOrElseThrowFatalThrowable(throwable);
  }
}
