package org.deus_ex_java.lang;

import org.jspecify.annotations.NullMarked;

@NullMarked
public class ForcedFatalThrowable {

  private ForcedFatalThrowable() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

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
      Throwable throwable
  ) {
    return (throwable instanceof VirtualMachineError) ||
        (throwable instanceof ThreadDeath) ||
        (throwable instanceof InterruptedException) ||
        (throwable instanceof LinkageError);
  }

  /**
   * Returns the passed {@code throwable} if it is non-fatal, i.e. returns false from
   * {@link ForcedFatalThrowable#isFatalThrowable}, otherwise throws the throwable, even if it is checked exception.
   *
   * @param throwable instance of an exception against which to test for Fatal
   * @return the passed {@code throwable} if it is non-fatal, i.e. returns false from
   *     {@link ForcedFatalThrowable#isFatalThrowable}, otherwise throws the throwable, even if it is checked exception
   */
  public static Throwable requireNonFatalThrowableOrElseThrowFatalThrowable(Throwable throwable) {
    if (isFatalThrowable(throwable)) {
      throwFatalThrowable(throwable);
    }

    return throwable;
  }

  private static boolean isInterruptedException(Throwable throwable) {
    if (throwable instanceof InterruptedException) {

      return true;
    }
    if (throwable.getCause() != null) {

      return isInterruptedException(throwable.getCause());
    }

    return false;
  }

  @SuppressWarnings("unchecked")
  private static <T extends Throwable> void throwFatalThrowableAsUncheckedException(
      Throwable throwable
  ) throws T {
    throw (T) throwable;
  }

  private static void throwFatalThrowable(Throwable throwable) {
    if (isInterruptedException(throwable)) {
      Thread.currentThread().interrupt();
    }
    throwFatalThrowableAsUncheckedException(throwable);
  }
}
