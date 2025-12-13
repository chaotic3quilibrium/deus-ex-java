package org.deus_ex_java.lang;

import org.deus_ex_java.util.TryCatchesOps;
import org.deus_ex_java.util.Using;
import org.deus_ex_java.util.UsingCheckedException;
import org.deus_ex_java.util.function.FunctionsOps;
import org.deus_ex_java.util.function.FunctionsPrimitivesOps;
import org.jspecify.annotations.NullMarked;

/**
 * A utility class to detect ({@link #isFatalThrowable(Throwable)}) and support tunneling <em>fatal</em> throwable
 * exceptions through the various try/catch guard patterns; i.e., preventing these <em>fatal</em> exceptions from being
 * successfully "caught".
 * <p>
 * The primary value is captured in the unchecked exception method,
 * {@link #requireNonFatalThrowableOrElseThrowFatalThrowable(Throwable)}. This encapsulates the checked exception
 * pathway such that it does not leak into the method's signature, thereby forcing the clients to handle the checked
 * exception.
 * <p>
 * This class is used extensively by:
 * <ul>
 * <li>{@link TryCatchesOps}</li>
 * <li>{@link WrappedCheckedException}</li>
 * </ul>
 * And indirectly by:
 * <ul>
 * <li>{@link FunctionsOps}</li>
 * <li>{@link FunctionsPrimitivesOps}</li>
 * <li>{@link Using}</li>
 * <li>{@link UsingCheckedException}</li>
 * </ul>
 */
@NullMarked
public class ForcedFatalThrowable {

  private ForcedFatalThrowable() {
    throw new UnsupportedOperationException("suppressing class instantiation");
  }

  /**
   * Returns {@code true} if the {@code throwable} matches an instance, or descendant, of:
   * <ul>
   * <li>{@link InterruptedException} - a <em>checked</em> exception</li>
   * <li>{@link LinkageError}</li>
   * <li>{@link ThreadDeath}</li>
   * <li>{@link VirtualMachineError}</li>
   * </ul>
   * This strategy is inspired by the one used in Scala 2.13's <a
   * href="https://github.com/scala/scala/blob/v2.13.16/src/library/scala/util/control/NonFatal.scala#L17">NonFatal</a>
   * Object.
   *
   * @param throwable instance of an exception against which to test for Fatal
   * @return {@code true} if the {@code throwable} matches an instance, or descendant, of:
   *     <ul>
   *     <li>{@link InterruptedException} - a <em>checked</em> exception</li>
   *     <li>{@link LinkageError}</li>
   *     <li>{@link ThreadDeath}</li>
   *     <li>{@link VirtualMachineError}</li>
   *     </ul>
   */
  public static boolean isFatalThrowable(
      Throwable throwable
  ) {
    return (throwable instanceof InterruptedException) ||
        (throwable instanceof LinkageError) ||
        (throwable instanceof ThreadDeath) ||
        (throwable instanceof VirtualMachineError);
  }

  /**
   * Returns the passed {@code throwable} if it is non-fatal, i.e. returns {@code false} from
   * {@link ForcedFatalThrowable#isFatalThrowable}, otherwise throws the throwable, even if it is a checked exception,
   * because there is no {@code throws} clause in this method's signature.
   *
   * @param throwable instance of an exception against which to test {@link ForcedFatalThrowable#isFatalThrowable}
   * @return the passed {@code throwable} if it is non-fatal, i.e. returns {@code false} from
   *     {@link ForcedFatalThrowable#isFatalThrowable}, otherwise throws the throwable, even if it is a checked
   *     exception, because there is no {@code throws} clause in this method's signature
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
