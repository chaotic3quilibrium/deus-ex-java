package org.deus_ex_java.lang;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.Serial;

/**
 * A parent abstract class for throwable instances intended for flow control.
 * <p>
 * Instances of {@code ControlBreakThrowable} should not normally have a cause. Legacy subclasses may set a cause using
 * {@link Throwable#initCause(Throwable) Throwable.initCause()}.
 * <p>
 * Suppression is disabled, because flow control should not suppress an exceptional condition. Stack traces are also
 * disabled, allowing instances of {@code ControlBreakThrowable} to be safely reused.
 * <p>
 * Instances of {@code ControlBreakThrowable} should not normally be caught.
 * <p>
 * The method {@link WrappedCheckedException#isFatal(Throwable)} will match {@code ControlBreakThrowable} which
 * means the TryCatchesOps methods are not able to filter/catch these instances; i.e. they tunnel through the fatal
 * exception pattern and must be explicitly caught with a try/catch statement.
 */
@NullMarked
public abstract class ControlBreakThrowable extends Throwable {

  @Serial
  private static final long serialVersionUID = -5222421435185083666L;

  /**
   * Creates a new {@link Throwable} with {@code message} and {@code cause} set to {@code null}, and
   * {@code enabledSuppression} and {@code writableStackTrace} set to {@code false}.
   */
  public ControlBreakThrowable() {
    this(null);
  }

  /**
   * Creates a new {@link Throwable} with optionally specified {@code message}, {@code cause} to {@code null}, and
   * {@code enabledSuppression} and {@code writableStackTrace} set to {@code false}.
   * <p>
   * Instances of {@code ControlBreakThrowable} should not normally have a cause. Legacy subclasses may set a cause
   * using `initCause`.
   * <p>
   * Suppression is disabled, because flow control should not suppress an exceptional condition. Stack traces are also
   * disabled, allowing instances of {@code ControlBreakThrowable} to be safely reused.
   *
   * @param message the detail message
   */
  public ControlBreakThrowable(
      @Nullable String message
  ) {
    super(
        message,
        null,
        false,
        false);
  }
}
