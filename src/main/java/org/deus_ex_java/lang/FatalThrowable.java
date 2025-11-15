package org.deus_ex_java.lang;

import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.Objects;
import java.util.Optional;

/**
 * {@code FatalThrowable} is an <em>unchecked exception</em> final class used to wrap a <b><em>fatal</em></b> checked
 * exception (guaranteed to be not {@code null}, and returned by {@link #getCause()}) that can be declared and thrown by
 * a method or constructor's explicitly defined {@code throws} clause.
 * <p>
 * This class is the fallback for a {@link WrappedCheckedException} which is inhibited from wrapping a fatal exception.
 */
public final class FatalThrowable extends RuntimeException {
  @Serial
  private static final long serialVersionUID = -3037986701314233065L;

  /**
   * Returns {@code true} if the {@code throwable} matches an instance, or descendant, of {@link VirtualMachineError},
   * {@link ThreadDeath}, {@link InterruptedException}, or {@link LinkageError}.
   * <p>
   * This strategy is inspired by the one used in Scala 2.13's <a
   * href="https://github.com/scala/scala/blob/v2.13.16/src/library/scala/util/control/NonFatal.scala#L17">NonFatal</a>
   * Object.
   *
   * @param throwable instance of an exception against which to test for Fatal
   * @return {@code true} if the {@code throwable} matches an instance, or descendant, of {@link VirtualMachineError},
   *     {@link ThreadDeath}, {@link InterruptedException}, or {@link LinkageError}
   */
  public static boolean isFatalThrowable(
      @NotNull Throwable throwable
  ) {
    return (throwable instanceof VirtualMachineError) ||
        (throwable instanceof ThreadDeath) ||
        (throwable instanceof InterruptedException) ||
        (throwable instanceof LinkageError);
  }

  /**
   * Returns an {@link Optional#empty} when the {@code throwable} is non-fatal, otherwise an {@link Optional} contains a
   * {@link FatalThrowable} containing the {@code throwable} as its cause.
   *
   * @param throwable instance of an exception against which to test for Fatal
   * @return an {@link Optional#empty} when the {@code throwable} is non-fatal, otherwise an {@link Optional} contains a
   *     {@link FatalThrowable} containing the {@code throwable} as its cause
   */
  public static Optional<FatalThrowable> filterToFatalThrowable(
      @NotNull Throwable throwable
  ) {
    return !isFatalThrowable(throwable)
        ? Optional.empty()
        : Optional.of(new FatalThrowable(
            "FatalThrowable.isFatalThrowable(throwable) must be false - %s - %s".formatted(
                throwable.getClass().getName(),
                throwable.getMessage()),
            throwable));
  }

  /**
   * Returns the passed {@code throwable} if it is non-fatal, i.e. returns false from
   * {@link FatalThrowable#isFatalThrowable}, otherwise throws a new instance of {@link FatalThrowable} containing the
   * {@code throwable} as its cause.
   *
   * @param throwable instance of an exception against which to test for Fatal
   * @return the passed {@code throwable} if it is non-fatal, i.e. returns false from
   *     {@link FatalThrowable#isFatalThrowable}, otherwise throws a new instance of {@link FatalThrowable} containing
   *     the {@code throwable} as its cause
   * @throws FatalThrowable when the throwable returns true from isFatalThrowable
   */
  public static Throwable requireNonFatalThrowable(Throwable throwable) {
    filterToFatalThrowable(throwable).ifPresent(fatalThrowable -> {
      throw fatalThrowable;
    });

    return throwable;
  }

  /**
   * Intentionally suppressed to ensure it can only be instantiated via the static factory methods above.
   */
  private FatalThrowable(
      @NotNull String message,
      @NotNull Throwable cause
  ) {
    super(message, Objects.requireNonNull(cause));
  }
}
